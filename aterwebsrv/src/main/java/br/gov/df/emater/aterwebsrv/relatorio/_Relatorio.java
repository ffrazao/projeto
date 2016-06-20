package br.gov.df.emater.aterwebsrv.relatorio;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JRException;

@Service
public interface _Relatorio {
	enum Formato {
		PDF
	};

	String EXTENSAO_ARQUIVO_COMPILADO = ".jasper";

	String EXTENSAO_ARQUIVO_FONTE = ".jrxml";

	void compilar(String relatorioNome) throws IOException, JRException;

	byte[] imprimir(String relatorioNome, Map<String, Object> parametros, List<?> lista) throws Exception;

	byte[] imprimir(String relatorioNome, Map<String, Object> parametros, List<?> lista, Formato formato) throws Exception;
}
