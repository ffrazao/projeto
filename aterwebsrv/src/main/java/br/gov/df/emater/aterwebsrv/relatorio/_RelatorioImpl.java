package br.gov.df.emater.aterwebsrv.relatorio;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Component
public class _RelatorioImpl implements _Relatorio {

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private ResourceLoader resourceLoader;

	public void compilar(String relatorioNome) throws IOException, JRException {
		compilarInterno(resourceLoader.getResource("classpath:jasper").getFile(), relatorioNome);
	}

	private void compilarInterno(File fonte, String relatorioNome) throws JRException {
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Procurando relatórios para compilar em [%s]", fonte.getAbsolutePath()));
		}
		for (File origem : fonte.listFiles()) {
			if (origem.isFile() && origem.getName().endsWith(EXTENSAO_ARQUIVO_FONTE) && (relatorioNome == null || (origem.getAbsolutePath().endsWith(relatorioNome.concat(EXTENSAO_ARQUIVO_FONTE))))) {
				if (logger.isDebugEnabled()) {
					logger.debug(String.format("Compilando relatório [%s]", origem.getAbsolutePath()));
				}
				File compilado = new File(origem.getAbsolutePath().substring(0, origem.getAbsolutePath().lastIndexOf(EXTENSAO_ARQUIVO_FONTE)).concat(EXTENSAO_ARQUIVO_COMPILADO));
				compilado.delete();
				JasperCompileManager.compileReportToFile(origem.getAbsolutePath(), compilado.getAbsolutePath());
			} else if (origem.isDirectory()) {
				compilarInterno(origem, relatorioNome);
			}
		}
	}

	@Override
	public byte[] imprimir(String relatorioNome, Map<String, Object> parametros, List<?> lista) throws Exception {
		return imprimir(relatorioNome, parametros, lista, null);
	}

	@Override
	public byte[] imprimir(String relatorioNome, Map<String, Object> parametros, List<?> lista, Formato formato) throws Exception {
		JasperReport relatorio;

		// carregar o modelo do relatório
		String relatorioNomeCompleto = String.format("classpath:jasper/%s%s", relatorioNome, EXTENSAO_ARQUIVO_COMPILADO);
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Imprimindo o relatório [%s]", relatorioNomeCompleto));
		}
		Resource compilado = resourceLoader.getResource(relatorioNomeCompleto);

		// carregar o modelo do relatório
		relatorio = (JasperReport) JRLoader.loadObject(compilado.getInputStream());

		// gerar uma impressão
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