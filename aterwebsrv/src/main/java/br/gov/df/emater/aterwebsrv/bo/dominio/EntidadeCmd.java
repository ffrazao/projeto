package br.gov.df.emater.aterwebsrv.bo.dominio;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.UtilDao;

@Service("EntidadeCmd")
public class EntidadeCmd extends _Comando {

	@Autowired
	private UtilDao dao;

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean executar(_Contexto contexto) throws Exception {
		Map<String, Object> requisicao = (Map<String, Object>) contexto.getRequisicao();
		String[] entidade = (String[]) requisicao.get("entidade");
		String nomeChavePrimaria = (String) requisicao.get("nomeChavePrimaria");
		String valorChavePrimaria = (String) requisicao.get("valorChavePrimaria");
		String order = (String) requisicao.get("order");
		String[] fetchs = (String[]) requisicao.get("fetchs");

		List[] result = (List[]) contexto.getResposta();

		for (int i = 0; i < result.length; i++) {
			if (result[i] == null) {
				result[i] = dao.getDominio(entidade[i], nomeChavePrimaria, valorChavePrimaria, order);
				fetch(result[i], entidade[i], fetchs);
			}
		}

		return false;
	}

	private void fetch(List<?> result, String entidade, String[] fetchs) throws Exception {
		// verificar se deve fazer o fetch de algum resultado
		if (fetchs != null && result != null) {
			List<Method> metodos = new ArrayList<Method>();
			for (String campo : fetchs) {
				metodos.add(Class.forName(String.format("gov.emater.aterweb.model.%s", entidade)).getMethod(String.format("get%s%s", campo.substring(0, 1).toUpperCase(), campo.substring(1, campo.length()))));
			}
			for (Object registro : result) {
				for (Method fetch : metodos) {
					List<?> itens = (List<?>) fetch.invoke(registro);
					itens.size();
				}
			}
		}
	}
}