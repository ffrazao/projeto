package br.gov.df.emater.aterwebsrv.relatorio;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Component
public class _RelatorioImpl implements _Relatorio {

	@Autowired
	private ResourceLoader resourceLoader;

	@Override
	public byte[] imprimir(String relatorioNome, Map<String, Object> parametros, List<?> lista) throws Exception {
		return imprimir(relatorioNome, parametros, lista, null);
	}

	@Override
	public byte[] imprimir(String relatorioNome, Map<String, Object> parametros, List<?> lista, Formato formato) throws Exception {
		JasperReport relatorio;
		
		File dir = criarDiretorios(relatorioNome);

		Resource fonte = resourceLoader.getResource(String.format("classpath:jasper/%s%s ",relatorioNome, EXTENSAO_ARQUIVO_FONTE));
		Resource compilado = resourceLoader.getResource(String.format("classpath:jasper/%s%s",relatorioNome, EXTENSAO_ARQUIVO_COMPILADO));
		
		if (!fonte.exists()) {
			FileUtils.copyInputStreamToFile(fonte.getInputStream(), new File(dir, fonte.getFilename()));
		}
	
		while (!compilado.exists()) {
			JasperCompileManager.compileReportToFile(fonte.getFile().getAbsolutePath(), new File(dir, compilado.getFilename()).getAbsolutePath());
		}

		relatorio = (JasperReport) JRLoader.loadObject(compilado.getFile());

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

	private File criarDiretorios(String relatorioNome) throws BoException {
		URL jasperUrl = _RelatorioImpl.class.getResource("/jasper");
		File jasperDir = new File(jasperUrl.getFile());
		if (!jasperDir.exists()) {
			jasperDir.mkdirs();
		} else if (!jasperDir.isDirectory()) {
			throw new BoException("Diretório dos relatórios inválido %s", jasperDir);
		}
		File result = jasperDir;
		if (relatorioNome.indexOf("/") >= 0) {
			result = new File(jasperDir, relatorioNome.substring(0, relatorioNome.lastIndexOf("/")));
			if (!result.exists()) {
				result.mkdirs();
			} else if (!result.isDirectory()) {
				throw new BoException("Diretório dos relatórios inválido %s", result);
			}
		}

		return result;
	}

}