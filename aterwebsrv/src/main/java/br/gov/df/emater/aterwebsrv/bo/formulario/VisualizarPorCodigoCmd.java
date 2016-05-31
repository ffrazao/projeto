package br.gov.df.emater.aterwebsrv.bo.formulario;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.formulario.FormularioDao;
import br.gov.df.emater.aterwebsrv.modelo.formulario.Formulario;

@Service("FormularioVisualizarPorCodigoCmd")
public class VisualizarPorCodigoCmd extends _Comando {

	@Autowired
	private FormularioDao dao;

	@SuppressWarnings("unchecked")
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Map<String, Object> requisicao = (Map<String, Object>) contexto.getRequisicao();
		String codigoStr = (String) requisicao.get("codigo");
		String posicaoStr = (String) requisicao.get("posicao");

		Formulario codigo = dao.findByCodigo(codigoStr);

		if (codigo == null) {
			throw new BoException("Registro não localizado");
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("codigo", codigo.getId());
		result.put("posicao", posicaoStr);

		contexto.setResposta(result);

		return false;
	}
}