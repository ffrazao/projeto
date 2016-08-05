package br.gov.df.emater.aterwebsrv.bo.atividade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Cadeia;
import br.gov.df.emater.aterwebsrv.bo.projeto_credito_rural.VisualizarCh;

@Service("AtividadeVisualizarComplementoCh")
public class VisualizarComplementoCh extends _Cadeia {

	@Autowired
	public VisualizarComplementoCh(@Qualifier("ProjetoCreditoRuralVisualizarCh") VisualizarCh c1) {
		super.addCommand(c1);
	}

}