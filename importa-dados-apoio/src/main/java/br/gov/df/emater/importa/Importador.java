package br.gov.df.emater.importa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
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

public abstract class Importador {

	protected Connection con;

	protected abstract void executar() throws Exception;

	protected List<Map<String, Object>> criarMapaDoArquivoExcel(File excel, int aba) throws Exception {
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

	protected File criarTempDir() {
		File result = ImportaDadosApoioApplication.TEMP_DIR;
		if (!result.exists()) {
			result.mkdir();
		} else if (!result.isDirectory()) {
			throw new RuntimeException("Diretório temporario invalido!");
		}
		return result;
	}

	protected File unzipOrigem(File zip, File tempDir) throws Exception {
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

	protected File downloadOrigem(String urlStr, File tempDir) throws Exception {
		File arquivo = new File(tempDir.getPath() + "/temp.zip");
		if (!arquivo.exists()) {
			URL url = new URL(urlStr);
			FileUtils.copyURLToFile(url, arquivo);
		}
		return arquivo;
	}

	public Importador(Connection con) {
		this.con = con;
	}

}
