package br.gov.df.emater.aterwebsrv.ferramenta;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class UtilitarioExcel {

	public static boolean criarArquivoExcelDoMapa(List<Map<String, Object>> mapList, List<String> cabecalhoList, String arqExcel) throws IOException {
		if (mapList == null || mapList.size() == 0) {
			File file = new File(arqExcel);
			if (!file.exists()) {
				new FileOutputStream(file).close();
			}
			file.setLastModified(System.currentTimeMillis());
			return false;
		}
		Workbook book = new XSSFWorkbook();
		try {
			Sheet sheet = book.createSheet("Planilha1");
			int rownum = 0;
			int cellnum = 0;
			Row row = sheet.createRow(rownum++);
			for (String cabecalho : cabecalhoList) {
				Cell cell = row.createCell(cellnum++);
				cell.setCellValue(cabecalho);
			}
			for (Map<String, Object> registro : mapList) {
				row = sheet.createRow(rownum++);
				cellnum = 0;
				for (String cabecalho : cabecalhoList) {
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

	public static List<Map<String, Object>> criarMapaDoArquivoExcel(File excel, int aba) throws Exception {
		return UtilitarioExcel.criarMapaDoArquivoExcel(new FileInputStream(excel), aba);
	}

	public static List<Map<String, Object>> criarMapaDoArquivoExcel(File excel, int aba, int pularLinha) throws Exception {
		return UtilitarioExcel.criarMapaDoArquivoExcel(new FileInputStream(excel), aba, pularLinha);
	}

	public static List<Map<String, Object>> criarMapaDoArquivoExcel(InputStream excel, int aba) throws Exception {
		return UtilitarioExcel.criarMapaDoArquivoExcel(excel, aba, 0);
	}

	public static List<Map<String, Object>> criarMapaDoArquivoExcel(InputStream excel, int aba, int pularLinha) throws Exception {
		List<Map<String, Object>> result = null;
		try (Workbook workbook = WorkbookFactory.create(excel)) {
			Sheet firstSheet = workbook.getSheetAt(aba);
			Iterator<Row> iterator = firstSheet.iterator();

			FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

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
					valor = getValor(cellIterator, evaluator);
					if (primeiraLinha) {
						colunas.add(valor.toString());
					} else {
						if (linha == null) {
							linha = new HashMap<String, Object>();
						}
						if (colCont < colunas.size()) {
							linha.put(colunas.get(colCont), valor);
						}
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

	private static Object getValor(Iterator<Cell> cellIterator, FormulaEvaluator evaluator) {
		Object result = null;
		Cell cell = cellIterator.next();
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			result = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			result = cell.getBooleanCellValue();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			result = cell.getNumericCellValue();
			break;
		case Cell.CELL_TYPE_FORMULA:
			evaluator.evaluateFormulaCell(cell);
			result = handleCell(cell.getCellType(), cell, evaluator);
			break;
		}
		return result;
	}

	private static Object handleCell(int type, Cell cell, FormulaEvaluator evaluator) {
		if (type == HSSFCell.CELL_TYPE_STRING) {
			return cell.getStringCellValue();
		} else if (type == HSSFCell.CELL_TYPE_NUMERIC) {
			return cell.getNumericCellValue();
		} else if (type == HSSFCell.CELL_TYPE_BOOLEAN) {
			return cell.getBooleanCellValue();
		} else if (type == HSSFCell.CELL_TYPE_FORMULA) {
			// Re-run based on the formula type
			evaluator.evaluateFormulaCell(cell);
			return handleCell(cell.getCachedFormulaResultType(), cell, evaluator);
		}
		return null;
	}

	public static List<List<List<Object>>> lerPlanilha(File excel) throws Exception {
		return UtilitarioExcel.lerPlanilha(new FileInputStream(excel));
	}

	public static List<List<List<Object>>> lerPlanilha(InputStream excel) throws Exception {
		List<List<List<Object>>> result = null;

		try (Workbook workbook = WorkbookFactory.create(excel)) {

			FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

			for (int abaCont = 0; abaCont < workbook.getNumberOfSheets(); abaCont++) {

				Iterator<Row> sheet = workbook.getSheetAt(abaCont).iterator();

				List<List<Object>> linha = new ArrayList<>();

				while (sheet.hasNext()) {
					Row nextRow = sheet.next();

					Iterator<Cell> cellIterator = nextRow.cellIterator();

					List<Object> colunas = new ArrayList<>();

					while (cellIterator.hasNext()) {
						colunas.add(getValor(cellIterator, evaluator));
					}
					linha.add(colunas);
				}
				if (result == null) {
					result = new ArrayList<>();
				}
				result.add(linha);
			}
		}
		return result;
	}
}
