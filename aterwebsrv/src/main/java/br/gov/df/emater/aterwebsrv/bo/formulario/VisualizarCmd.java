package br.gov.df.emater.aterwebsrv.bo.formulario;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.formulario.FormularioDao;
import br.gov.df.emater.aterwebsrv.modelo.formulario.Elemento;
import br.gov.df.emater.aterwebsrv.modelo.formulario.Formulario;
import br.gov.df.emater.aterwebsrv.modelo.formulario.FormularioVersao;
import br.gov.df.emater.aterwebsrv.modelo.formulario.FormularioVersaoElemento;
import br.gov.df.emater.aterwebsrv.modelo.formulario.Opcao;

@Service("FormularioVisualizarCmd")
public class VisualizarCmd extends _Comando {

	@Autowired
	private FormularioDao dao;

	@Autowired
	private EntityManager em;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Integer id = (Integer) contexto.getRequisicao();
		Formulario result = dao.findOne(id);

		if (result == null) {
			throw new BoException("Registro não localizado");
		}

		if (result.getFormularioVersaoList() != null) {
			for (FormularioVersao formularioVersao : result.getFormularioVersaoList()) {
				if (formularioVersao.getFormularioVersaoElementoList() != null) {
					for (FormularioVersaoElemento formularioVersaoElemento : formularioVersao.getFormularioVersaoElementoList()) {
						Elemento elemento = formularioVersaoElemento.getElemento();
						if (elemento.getObservarList() != null) {
							elemento.getObservarList().size();
						}
						if (elemento.getOpcaoList() != null) {
							for (Opcao opcao : elemento.getOpcaoList()) {
								if (opcao.getFormularioList() != null) {
									opcao.getFormularioList().size();
								}
							}
						}
					}
				}
			}
		}

		em.detach(result);
		contexto.setResposta(result);

		return true;
	}
}