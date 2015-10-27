package br.gov.df.emater.aterwebsrv.bo.dominio;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;

@Service("DominioPreparaRespostaCmd")
public class PreparaRespostaCmd extends _Comando {

	@Override
	@SuppressWarnings("unchecked")
	public boolean executar(_Contexto contexto) throws Exception {
		Map<String, Object> requisicao = (Map<String, Object>) contexto.getRequisicao();
		String[] entidade = (String[]) requisicao.get("entidade");

		List<Object>[] result = new List[entidade.length];

		contexto.setResposta(result);
		return false;
	}

}