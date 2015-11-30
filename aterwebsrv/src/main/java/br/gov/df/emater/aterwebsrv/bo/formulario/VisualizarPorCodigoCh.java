package br.gov.df.emater.aterwebsrv.bo.formulario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Cadeia;
import br.gov.df.emater.aterwebsrv.bo.seguranca.AutenticarUsuarioCmd;
import br.gov.df.emater.aterwebsrv.bo.seguranca.LogCmd;

@Service("FormularioVisualizarPorCodigoCh")
public class VisualizarPorCodigoCh extends _Cadeia {

	@Autowired
	public VisualizarPorCodigoCh(AutenticarUsuarioCmd c1, VisualizarPorCodigoCmd c2, VisualizarCmd c3, LogCmd c4) {
		super.addCommand(c1);
		super.addCommand(c2);
		super.addCommand(c3);
		super.addCommand(c4);
	}

}