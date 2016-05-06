package br.gov.df.emater.aterwebsrv.ferramenta;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

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

}