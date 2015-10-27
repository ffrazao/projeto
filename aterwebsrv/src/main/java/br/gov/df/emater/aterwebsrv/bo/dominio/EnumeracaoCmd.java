package br.gov.df.emater.aterwebsrv.bo.dominio;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.UtilDao;

@Service("EnumeracaoCmd")
public class EnumeracaoCmd extends _Comando {

	@Autowired
	private UtilDao dao;

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean executar(_Contexto contexto) throws Exception {
		Map<String, Object> requisicao = (Map<String, Object>) contexto.getRequisicao();
		String[] entidade = (String[]) requisicao.get("entidade");

		List[] result = (List[]) contexto.getResposta();
		
		for (int i = 0; i < result.length; i++) {
			result[i] = dao.getEnumeracao(entidade[i]); 
		}

		return false;
	}

}