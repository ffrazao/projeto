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

import br.gov.df.emater.aterwebsrv.bo.BoException;
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

	private final static String CLASSPATH = "classpath:jasper";

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private ResourceLoader resourceLoader;

	public void compilar(String relatorioNome) throws BoException {
		try {
			compilarInterno(resourceLoader.getResource(CLASSPATH).getFile(), relatorioNome);
		} catch (IOException e) {
			throw new BoException(e);
		}
	}

	private void compilarInterno(File diretorioFonte, String relatorioNome) throws BoException {
		if (diretorioFonte == null || !diretorioFonte.isDirectory()) {
			throw new BoException("Diretório dos arquivos fontes de relatórios inválido, [%s]", diretorioFonte);
		}
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Procurando relatórios para compilar no diretório [%s]", diretorioFonte.getAbsolutePath()));
		}

		String relatorioNomeInterno = relatorioNome == null ? EXTENSAO_ARQUIVO_FONTE : relatorioNome.concat(EXTENSAO_ARQUIVO_FONTE).replaceAll("\\\\", "").replaceAll("/", "").toLowerCase();
		for (File arquivo : diretorioFonte.listFiles()) {
			if (arquivo.isFile() && arquivo.getAbsoluteFile().toString().replaceAll("\\\\", "").replaceAll("/", "").toLowerCase().endsWith(relatorioNomeInterno)) {
				if (logger.isDebugEnabled()) {
					logger.debug(String.format("Compilando relatório [%s]", arquivo.getAbsolutePath()));
				}
				File compilado = new File(arquivo.getAbsolutePath().substring(0, arquivo.getAbsolutePath().lastIndexOf(EXTENSAO_ARQUIVO_FONTE)).concat(EXTENSAO_ARQUIVO_COMPILADO));
				compilado.delete();
				try {
					JasperCompileManager.compileReportToFile(arquivo.getAbsolutePath(), compilado.getAbsolutePath());
				} catch (JRException e) {
					throw new BoException(e);
				}
			} else if (arquivo.isDirectory()) {
				compilarInterno(arquivo, relatorioNome);
			}
		}
	}

	@Override
	public byte[] imprimir(JasperPrint impressao) throws BoException {
		return imprimir(impressao, null);
	}

	@Override
	public byte[] imprimir(JasperPrint impressao, Formato formato) throws BoException {
		try {
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
		} catch (Exception e) {
			throw new BoException(e);
		}
	}

	@Override
	public byte[] imprimir(String relatorioNome, Map<String, Object> parametros, List<?> lista) throws BoException {
		return imprimir(relatorioNome, parametros, lista, null);
	}

	@Override
	public byte[] imprimir(String relatorioNome, Map<String, Object> parametros, List<?> lista, Formato formato) throws BoException {
		return imprimir(montarRelatorio(relatorioNome, parametros, lista));
	}

	@Override
	public JasperPrint montarRelatorio(String relatorioNome, Map<String, Object> parametros, List<?> lista) throws BoException {
		try {
			JasperReport relatorio;

			// carregar o modelo do relatório
			String relatorioNomeCompleto = String.format("%s/%s%s", CLASSPATH, relatorioNome, EXTENSAO_ARQUIVO_COMPILADO);
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("Imprimindo o relatório [%s]", relatorioNomeCompleto));
			}
			Resource compilado = resourceLoader.getResource(relatorioNomeCompleto);

			// verificar se o relatório já foi compilado
			if (!compilado.exists()) {
				compilar(relatorioNome);
			}

			// carregar o modelo do relatório
			relatorio = (JasperReport) JRLoader.loadObject(compilado.getInputStream());
			
			// inserir o local dos relatórios
			parametros. put("DiretorioJasper", resourceLoader.getResource(CLASSPATH).getFile().getAbsolutePath());

			// gerar uma impressão
			JasperPrint result = JasperFillManager.fillReport(relatorio, parametros, new JRBeanCollectionDataSource(lista));

			return result;

		} catch (Exception e) {
			throw new BoException(e);
		}
	}

}