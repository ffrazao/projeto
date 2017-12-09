package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Ipa;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoProprietario;

@Service("IndiceProducaoProducaoCmd")
public class ProducaoCmd extends _Comando {

	@Autowired
	private ProducaoSrv servico;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		// foi necessário fazer assim para que o cache do spring funcione
		// corretamente veja
		// http://stackoverflow.com/questions/16899604/spring-cache-cacheable-not-working-while-calling-from-another-method-of-the-s
		
		//ProducaoProprietario producaoProprietario =  (ProducaoProprietario) contexto.getRequisicao();
		//contexto.setResposta(servico.geraProducao( producaoProprietario ));
		
		//ProducaoGravaDto result = (ProducaoGravaDto) contexto.getRequisicao();
		
		Ipa producaoProprietario = (Ipa) contexto.getRequisicao();
		contexto.setResposta(servico.geraProducao( producaoProprietario ));
		return false;
	}

}
