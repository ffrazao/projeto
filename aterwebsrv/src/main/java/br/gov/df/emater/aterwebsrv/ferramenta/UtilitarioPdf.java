package br.gov.df.emater.aterwebsrv.ferramenta;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfReader;

public class UtilitarioPdf {

	public static byte[] juntarPdf(List<byte[]> arquivoList) throws IOException, DocumentException {
		byte[][] tempList = new byte[arquivoList.size()][];
		for (int i = 0; i < arquivoList.size(); i++) {
			tempList[i] = (byte[]) arquivoList.get(i);
		}
		return UtilitarioPdf.juntarPdf(tempList);
	}

	public static byte[] juntarPdf(byte[]... arquivoList) throws IOException, DocumentException {
		ByteArrayOutputStream result = new ByteArrayOutputStream(0);

		// abrir a instancia do documento principal que vai receber os dados
		// copiados
		Document document = new Document();
		PdfCopy copy = new PdfCopy(document, result);
		document.open();

		// percorrer os arquivos a serem juntados
		for (byte[] arquivo : arquivoList) {
			PdfReader reader = new PdfReader(arquivo);
			for (int page = 0; page < reader.getNumberOfPages();) {
				copy.addPage(copy.getImportedPage(reader, ++page));
			}
			copy.freeReader(reader);
			reader.close();
		}
		document.close();

		return result.toByteArray();
	}

}
