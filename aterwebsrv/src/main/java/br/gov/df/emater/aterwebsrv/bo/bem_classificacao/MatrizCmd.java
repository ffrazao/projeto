package br.gov.df.emater.aterwebsrv.bo.bem_classificacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;

@Service("BemProducaoMatrizCmd")
public class MatrizCmd extends _Comando {

	@Autowired
	private BemClassificacaoUtilSrv servico;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		// foi necessário fazer assim para que o cache do spring funcione
		// corretamente veja
		// http://stackoverflow.com/questions/16899604/spring-cache-cacheable-not-working-while-calling-from-another-method-of-the-s
		contexto.setResposta(servico.geraMatriz());
		return false;
	}

}
