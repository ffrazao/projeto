package br.gov.df.emater.aterwebsrv.bo.formulario;

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

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		String codigo = (String) contexto.getRequisicao();

		Formulario result = dao.findByCodigo(codigo);

		if (result == null) {
			throw new BoException("Registro não localizado");
		}

		contexto.setRequisicao(result.getId());

		return false;
	}
}