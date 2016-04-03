package br.gov.df.emater.aterwebsrv.relatorio;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Component
public class _RelatorioImpl implements _Relatorio {

	private void criarDiretorioSeNaoExistir(String diretorio) {
		String[] partes = diretorio.split("/");
		StringBuilder caminho = new StringBuilder();
		for (int i = 0; i < partes.length - 2; i++) {
			if (caminho.length() > 0) {
				caminho.append(File.separator);
			}
			if (partes[i] == null || partes[i].length() == 0) {
				continue;
			}
			caminho.append(partes[i]);

			File item = new File(caminho.toString());

			if (!item.exists()) {
				item.mkdir();
			}
		}
	}

	private String getRelatorioNome(String relatorioNome) {
		return this.getClass().getPackage().getName().concat(".").concat(relatorioNome).replaceAll("\\.", "/");
	}

	private URL getURL(String relatorioNome) {
		return this.getClass().getClassLoader().getResource(relatorioNome);
	}

	public byte[] imprimir(String relatorioNome, Map<String, Object> parametros, List<?> lista) throws Exception {
		return imprimir(relatorioNome, parametros, lista, null);
	}

	public byte[] imprimir(String relatorioNome, Map<String, Object> parametros, List<?> lista, Formato formato) throws Exception {
		JasperReport relatorio;
		URL compilado;
		relatorioNome = getRelatorioNome(relatorioNome);

		criarDiretorioSeNaoExistir(relatorioNome.substring(0, relatorioNome.lastIndexOf("/")));

		JasperCompileManager.compileReportToFile(getURL("").getPath().concat(relatorioNome.concat(EXTENSAO_ARQUIVO_FONTE)), getURL("").getPath().concat(relatorioNome.concat(EXTENSAO_ARQUIVO_COMPILADO)));
		do {
			compilado = getURL(relatorioNome.concat(EXTENSAO_ARQUIVO_COMPILADO));

			if (compilado == null) {
				JasperCompileManager.compileReportToFile(getURL("").getPath().concat(relatorioNome.concat(EXTENSAO_ARQUIVO_FONTE)), getURL("").getPath().concat(relatorioNome.concat(EXTENSAO_ARQUIVO_COMPILADO)));
			}

		} while (compilado == null);

		relatorio = (JasperReport) JRLoader.loadObject(compilado);

		JasperPrint impressao = JasperFillManager.fillReport(relatorio, parametros, new JRBeanCollectionDataSource(lista));

		// JasperExportManager.exportReportToPdfFile(impressao,
		// "e:/CarteiraProdutorRel.pdf");

		if (formato == null) {
			formato = Formato.PDF;
		}

		switch (formato) {
		default:
		case PDF:
			return JasperExportManager.exportReportToPdf(impressao);
		}
	}

}