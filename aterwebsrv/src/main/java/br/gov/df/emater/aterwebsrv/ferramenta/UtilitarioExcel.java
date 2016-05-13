package br.gov.df.emater.aterwebsrv.ferramenta;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class UtilitarioExcel {

	public static List<Map<String, Object>> criarMapaDoArquivoExcel(File excel, int aba) throws Exception {
		return UtilitarioExcel.criarMapaDoArquivoExcel(excel, aba, 0);
	}

	public static List<Map<String, Object>> criarMapaDoArquivoExcel(File excel, int aba, int pularLinha) throws Exception {
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
				if (pularLinha-- > 0) {
					continue;
				}
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

	public static boolean criarArquivoExcelDoMapa(List<Map<String, Object>> mapList, List<String> cabecalhoList, String arqExcel) throws IOException {
		if (mapList == null || mapList.size() == 0) {
			return false;
		}
		Workbook book = new XSSFWorkbook();
		try {
			Sheet sheet = book.createSheet("Planilha1");
			int rownum = 0;
			int cellnum = 0;
			Row row = sheet.createRow(rownum++);
			for (String cabecalho: cabecalhoList) {
				Cell cell = row.createCell(cellnum++);
				cell.setCellValue(cabecalho);
			}
			for (Map<String, Object> registro : mapList) {
				row = sheet.createRow(rownum++);
				cellnum = 0;
				for (String cabecalho: cabecalhoList) {
					Object valor = registro.get(cabecalho);
					Cell cell = row.createCell(cellnum++);
					if (valor instanceof String) {
						cell.setCellValue((String) valor);
					} else if (valor instanceof Boolean) {
						cell.setCellValue((Boolean) valor);
					} else if (valor instanceof Date) {
						cell.setCellValue((Date) valor);
					} else if (valor instanceof Double) {
						cell.setCellValue((Double) valor);
					}
				}
			}
			try (FileOutputStream os = new FileOutputStream(arqExcel);) {
				book.write(os);
			}
		} finally {
			book.close();
		}
		return true;
	}
}
