package br.gov.df.emater.aterwebsrv.relatorio;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface _Relatorio {
	enum Formato {
		PDF
	};

	String EXTENSAO_ARQUIVO_COMPILADO = ".jasper";

	String EXTENSAO_ARQUIVO_FONTE = ".jrxml";

	byte[] imprimir(String relatorioNome, Map<String, Object> parametros, List<?> lista) throws Exception;

	byte[] imprimir(String relatorioNome, Map<String, Object> parametros, List<?> lista, Formato formato) throws Exception;

}
