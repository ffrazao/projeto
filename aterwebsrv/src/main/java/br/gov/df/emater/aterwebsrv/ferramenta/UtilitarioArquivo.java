package br.gov.df.emater.aterwebsrv.ferramenta;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class UtilitarioArquivo {

	public static File downloadOrigem(String enderecoUrl, File arquivo) throws Exception {
		FileUtils.copyURLToFile(new URL(enderecoUrl), arquivo);
		return arquivo;
	}

	public static File downloadOrigem(String enderecoUrl, String arquivoDestino) throws Exception {
		return UtilitarioArquivo.downloadOrigem(enderecoUrl, new File(arquivoDestino));
	}

	public static String removeArquivo(String filePath) {
		// These first few lines the same as Justin's
		File f = new File(filePath);

		// if it's a directory, don't remove the extention
		if (f.isDirectory())
			return filePath;

		String name = f.getName();

		return filePath.substring(0, filePath.lastIndexOf(name));
	}

	private static String removeDados(String filePath, String caractere) {
		// These first few lines the same as Justin's
		File f = new File(filePath);

		// if it's a directory, don't remove the extention
		if (f.isDirectory())
			return filePath;

		String name = f.getName();

		// Now we know it's a file - don't need to do any special hidden
		// checking or contains() checking because of:
		final int lastPeriodPos = name.lastIndexOf(caractere);
		if (lastPeriodPos <= 0) {
			// No period after first character - return name as it was passed in
			return filePath;
		} else {
			// Remove the last period and everything after it
			File renamed = new File(f.getParent(), name.substring(0, lastPeriodPos));
			return renamed.getPath();
		}
	}

	public static String removeExtensao(String filePath) {
		return removeDados(filePath, ".");
	}

	public static File unzipOrigem(File zip, File tempDir) throws Exception {
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
}