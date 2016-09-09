package br.gov.df.emater.aterwebsrv.bo.formulario;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.formulario.FormularioDao;
import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.formulario.Elemento;
import br.gov.df.emater.aterwebsrv.modelo.formulario.Formulario;
import br.gov.df.emater.aterwebsrv.modelo.formulario.FormularioVersao;
import br.gov.df.emater.aterwebsrv.modelo.formulario.FormularioVersaoElemento;

@Service("FormularioVisualizarCmd")
public class VisualizarCmd extends _Comando {

	@Autowired
	private FormularioDao dao;

	@PersistenceContext(unitName = EntidadeBase.PERSISTENCE_UNIT)
	private EntityManager em;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Integer id = null;
		String posicao = null;

		if (contexto.getResposta() != null && contexto.getResposta() instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> requisicao = (Map<String, Object>) contexto.getResposta();
			id = (Integer) requisicao.get("codigo");
			posicao = (String) requisicao.get("posicao");
		} else {
			id = (Integer) contexto.getRequisicao();
		}

		Formulario formulario = dao.findOne(id);

		if (formulario == null) {
			throw new BoException("Registro não localizado");
		}

		if (formulario.getFormularioVersaoList() != null) {
			for (FormularioVersao formularioVersao : formulario.getFormularioVersaoList()) {
				if (formularioVersao.getFormularioVersaoElementoList() != null) {
					for (FormularioVersaoElemento formularioVersaoElemento : formularioVersao.getFormularioVersaoElementoList()) {
						Elemento elemento = formularioVersaoElemento.getElemento();
						if (elemento.getObservarList() != null) {
							elemento.getObservarList().size();
						}
					}
				}
			}
		}

		em.detach(formulario);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("formulario", formulario);
		result.put("posicao", posicao);

		contexto.setResposta(result);

		return false;
	}
}