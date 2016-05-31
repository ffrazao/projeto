package br.gov.df.emater.aterwebsrv.bo.formulario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.formulario.ElementoDao;
import br.gov.df.emater.aterwebsrv.dao.formulario.FormularioDao;
import br.gov.df.emater.aterwebsrv.dao.formulario.FormularioVersaoDao;
import br.gov.df.emater.aterwebsrv.dao.formulario.FormularioVersaoElementoDao;
import br.gov.df.emater.aterwebsrv.dao.formulario.ObservarDao;
import br.gov.df.emater.aterwebsrv.modelo.formulario.Elemento;
import br.gov.df.emater.aterwebsrv.modelo.formulario.Formulario;
import br.gov.df.emater.aterwebsrv.modelo.formulario.FormularioVersao;
import br.gov.df.emater.aterwebsrv.modelo.formulario.FormularioVersaoElemento;
import br.gov.df.emater.aterwebsrv.modelo.formulario.Observar;

@Service("FormularioSalvarCmd")
public class SalvarCmd extends _Comando {

	@Autowired
	private FormularioDao dao;

	@Autowired
	private ElementoDao elementoDao;

	@Autowired
	private FormularioVersaoDao formularioVersaoDao;

	@Autowired
	private FormularioVersaoElementoDao formularioVersaoElementoDao;

	@Autowired
	private ObservarDao observarDao;

	public SalvarCmd() {
	}

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Formulario result = (Formulario) contexto.getRequisicao();

		dao.save(result);

		if (result.getFormularioVersaoList() != null) {
			for (FormularioVersao formularioVersao : result.getFormularioVersaoList()) {
				formularioVersao.setFormulario(result);
				if (formularioVersao.getVersao() == null) {
					Integer cont = formularioVersaoDao.countByFormulario(result);
					formularioVersao.setVersao(cont + 1);
				}
				formularioVersaoDao.save(formularioVersao);
				if (formularioVersao.getFormularioVersaoElementoList() != null) {
					for (FormularioVersaoElemento formularioVersaoElemento : formularioVersao.getFormularioVersaoElementoList()) {
						formularioVersaoElemento.setFormularioVersao(formularioVersao);
						if (formularioVersaoElemento.getElemento() != null) {
							Elemento elemento = formularioVersaoElemento.getElemento();

							if (elemento.getOpcao() != null) {
								ObjectMapper om = new ObjectMapper();
								String json = om.writeValueAsString(elemento.getOpcao());
								elemento.setOpcaoString(json);
							}

							elementoDao.save(elemento);
							if (elemento.getObservarList() != null) {
								for (Observar observar : elemento.getObservarList()) {
									observar.setElemento(elemento);
									observarDao.save(observar);
								}
							}
						}
						formularioVersaoElementoDao.save(formularioVersaoElemento);
					}
				}
			}
		}

		dao.flush();

		contexto.setResposta(result.getId());
		return true;
	}

}