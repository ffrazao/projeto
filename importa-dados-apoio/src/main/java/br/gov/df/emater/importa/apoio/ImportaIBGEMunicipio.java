package br.gov.df.emater.importa.apoio;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import br.gov.df.emater.importa.Importador;

public class ImportaIBGEMunicipio extends Importador {

	private int paisId;
	
	public ImportaIBGEMunicipio(Connection con) {
		super(con);
	}
	
	public void executar() throws Exception {
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

			int estadoId = atualiza("estado", "pais", getPaisId(), nomeUF, uf);
			int municipioId = atualiza("municipio", "estado", estadoId, nomeMunicipio, municipio);
			atualiza("cidade", "municipio", municipioId, nomeDistrito, distrito);
		}
		System.out.println("Fim!");
	}

	public int getPaisId() {
		return paisId;
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

	public void setPaisId(int paisId) {
		this.paisId = paisId;
	}

	protected Integer atualiza(String tabela, String colunaPai, Integer ancestralId, String nome, String codigo) throws SQLException {
		return atualiza(tabela, colunaPai, ancestralId, nome, codigo, null);
	}

	protected Integer atualiza(String tabela, String colunaPai, Integer ancestralId, String nome, String codigo, String sigla) throws SQLException {
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

}
