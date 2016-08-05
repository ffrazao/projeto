package br.gov.df.emater.aterwebsrv.bo.atividade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Cadeia;
import br.gov.df.emater.aterwebsrv.bo.projeto_credito_rural.SalvarCh;

@Service("AtividadeSalvarComplementoCh")
public class SalvarComplementoCh extends _Cadeia {

	@Autowired
	public SalvarComplementoCh(@Qualifier("ProjetoCreditoRuralSalvarCh") SalvarCh c1) {
		super.addCommand(c1);
		// ATENÇÃO! a característica dos comandos aqui relacionados é que eles
		// não modificam o valor da resposta, ou seja, deve-se manter o id da
		// atividade inserido pelo comando SalvarCmd
	}

}