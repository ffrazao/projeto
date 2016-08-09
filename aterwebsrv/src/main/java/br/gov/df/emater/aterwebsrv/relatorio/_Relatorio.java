package br.gov.df.emater.aterwebsrv.relatorio;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public interface _Relatorio {
	enum Formato {
		PDF
	};

	String EXTENSAO_ARQUIVO_COMPILADO = ".jasper";

	String EXTENSAO_ARQUIVO_FONTE = ".jrxml";

	void compilar(String relatorioNome) throws BoException;

	byte[] imprimir(JasperPrint principal) throws BoException;

	byte[] imprimir(JasperPrint principal, Formato formato) throws BoException;

	byte[] imprimir(String relatorioNome, Map<String, Object> parametros, List<?> lista) throws BoException;

	byte[] imprimir(String relatorioNome, Map<String, Object> parametros, List<?> lista, Formato formato) throws BoException;

	JasperPrint montarRelatorio(String relatorioNome, Map<String, Object> parametros, List<?> lista) throws BoException;

}
