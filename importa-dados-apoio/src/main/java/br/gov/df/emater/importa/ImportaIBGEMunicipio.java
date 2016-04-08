package br.gov.df.emater.importa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import br.gov.df.emater.ImportaDadosApoioApplication;

public class ImportaIBGEMunicipio {

	private Connection con;

	public void executar(int paisId) throws Exception {
		File tempDir = criarTempDir();

		// a tabela de municipios foi copiada do site ibge
		// http://concla.ibge.gov.br/classificacoes/por-tema/codigo-de-areas/codigo-de-areas.html
		File zip = downloadOrigem("ftp://geoftp.ibge.gov.br/organizacao_territorial/divisao_territorial/2013/dtb_2013.zip", tempDir);

		File excel = unzipOrigem(zip, tempDir);

		List<Map<String, Object>> mapa = criarMapaDoArquivoExcel(excel, 0);

		for (Map<String, Object> linha : mapa) {
			String distrito = (String) linha.get("Distrito");
			String municipio = (String) linha.get("Município");
			String nomeDistrito = (String) linha.get("Nome_Distrito");
			String nomeMunicipio = (String) linha.get("Nome_Município");
			String nomeUF = (String) linha.get("Nome_UF");
			String uf = (String) linha.get("Uf");
			
			if ("53".equals(uf)) {
				distrito += (String) linha.get("Subdistrito");
				nomeDistrito = (String) linha.get("Nome_Subdistrito");
			}

			int estadoId = atualiza("estado", "pais", paisId, nomeUF, uf);
			int municipioId = atualiza("municipio", "estado", estadoId, nomeMunicipio, municipio);
			atualiza("cidade", "municipio", municipioId, nomeDistrito, distrito);
		}
		System.out.println("Fim!");
	}

	private Integer atualiza(String tabela, String colunaPai, Integer ancestralId, String nome, String codigo) throws SQLException {
		return atualiza(tabela, colunaPai, ancestralId, nome, codigo, null);
	}

	private Integer atualiza(String tabela, String colunaPai, Integer ancestralId, String nome, String codigo, String sigla) throws SQLException {
		Integer result = null;
		
		if (nome == null || nome.trim().length() == 0) {
			return null;
		}

		StringBuilder sql;

		sql = new StringBuilder();
		sql.append("select id").append("\n");
		sql.append("from pessoa.").append(tabela).append("\n");
		sql.append("where nome like ?").append("\n");
		if (colunaPai != null) {
			sql.append("  and ").append(colunaPai).append("_id = ?").append("\n");
		}
		PreparedStatement selectPs = con.prepareStatement(sql.toString());

		sql = new StringBuilder();
		sql.append("update pessoa.").append(tabela).append("\n");
		sql.append("set nome = ?").append("\n");
		sql.append("   ,codigo = ?").append("\n");
		if (sigla != null) {
			sql.append("   ,sigla = ?").append("\n");
		}
		sql.append("where id = ?").append("\n");
		PreparedStatement updatePs = con.prepareStatement(sql.toString());

		sql = new StringBuilder();
		sql.append("insert into pessoa.").append(tabela).append("\n");
		sql.append("   (nome, codigo").append("\n");
		if (colunaPai != null) {
			sql.append(", ").append(colunaPai).append("_id\n");
		}
		if (sigla != null) {
			sql.append(", sigla").append("\n");
		}
		sql.append(") values").append("\n");
		sql.append("   (?, ?").append("\n");
		if (colunaPai != null) {
			sql.append(", ?").append("\n");
		}
		if (sigla != null) {
			sql.append(", ?").append("\n");
		}
		sql.append(")").append("\n");
		PreparedStatement insertPs = con.prepareStatement(sql.toString());

		do {
			selectPs.setString(1, nome);
			if (ancestralId != null) {				
				selectPs.setInt(2, ancestralId);
			}
			ResultSet rs = selectPs.executeQuery();
			
			PreparedStatement ps = null;
			int i = 0;
			if (rs.next()) {
				result = rs.getInt(1);
				
				ps = updatePs;
				
				ps.setString(++i, nome);
				ps.setString(++i, codigo);
				if (sigla != null) {
					ps.setString(++i, sigla);
				}
				ps.setInt(++i, result);
			} else {
				ps = insertPs;
				
				ps.setString(++i, nome);
				ps.setString(++i, codigo);
				if (colunaPai != null) {
					ps.setInt(++i, ancestralId);
				}
				if (sigla != null) {
					ps.setString(++i, sigla);
				}
			}
			ps.execute();
		} while (result == null);

		return result;
	}

	private List<Map<String, Object>> criarMapaDoArquivoExcel(File excel, int aba) throws Exception {
		List<Map<String, Object>> result = null;
		try (FileInputStream inputStream = new FileInputStream(excel); Workbook workbook = WorkbookFactory.create(inputStream)) {
			Sheet firstSheet = workbook.getSheetAt(aba);
			Iterator<Row> iterator = firstSheet.iterator();

			List<String> colunas = new ArrayList<String>();
			Map<String, Object> linha = null;
			Object valor = null;
			boolean primeiraLinha = true;

			while (iterator.hasNext()) {
				Row nextRow = iterator.next();
				Iterator<Cell> cellIterator = nextRow.cellIterator();
				int colCont = 0;
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						valor = cell.getStringCellValue();
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						valor = cell.getBooleanCellValue();
						break;
					case Cell.CELL_TYPE_NUMERIC:
						valor = cell.getNumericCellValue();
						break;
					}
					if (primeiraLinha) {
						colunas.add(valor.toString());
					} else {
						if (linha == null) {
							linha = new HashMap<String, Object>();
						}
						linha.put(colunas.get(colCont), valor);
					}
					colCont++;
				}
				if (primeiraLinha) {
					primeiraLinha = false;
				} else {
					if (result == null) {
						result = new ArrayList<Map<String, Object>>();
					}
					result.add(linha);
					linha = null;
				}
			}
		}

		return result;
	}

	private File criarTempDir() {
		File result = ImportaDadosApoioApplication.TEMP_DIR;
		if (!result.exists()) {
			result.mkdir();
		} else if (!result.isDirectory()) {
			throw new RuntimeException("Diretório temporario invalido!");
		}
		return result;
	}

	private File unzipOrigem(File zip, File tempDir) throws Exception {
		File result = null;
		try (final InputStream is = new FileInputStream(zip)) {
			final ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream("zip", is);
			final ZipArchiveEntry entry = (ZipArchiveEntry) in.getNextEntry();
			result = new File(tempDir, entry.getName());
			if (!result.exists()) {
				try (final OutputStream out = new FileOutputStream(result)) {
					IOUtils.copy(in, out);
				}
			}
		}
		return result;
	}

	private File downloadOrigem(String urlStr, File tempDir) throws Exception {
		File arquivo = new File(tempDir.getPath() + "/temp.zip");
		if (!arquivo.exists()) {
			URL url = new URL(urlStr);
			FileUtils.copyURLToFile(url, arquivo);
		}
		return arquivo;
	}

	public void pais() throws Exception {

		StringBuilder sql;
		sql = new StringBuilder();
		sql.append("select * from pessoa.paises order by nome").append("\n");
		PreparedStatement ps = con.prepareStatement(sql.toString());
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			atualiza("pais", null, null, rs.getString("nome"), rs.getString("iso3"), rs.getString("iso"));
		}
	}

	public ImportaIBGEMunicipio(Connection con) {
		this.con = con;
	}

}
