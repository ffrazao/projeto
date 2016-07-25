package br.gov.df.emater.aterwebsrv.bo.projeto_credito_rural;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.bo._SalvarCmd;
import br.gov.df.emater.aterwebsrv.modelo.dto.ProjetoCreditoRuralCronogramaDto;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRural;

@Service("ProjetoCreditoRuralCalcularCronogramaCmd")
public class CalcularCronogramaCmd extends _SalvarCmd {

	public CalcularCronogramaCmd() {
	}

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		ProjetoCreditoRural result = (ProjetoCreditoRural) contexto.getRequisicao();
		
		ProjetoCreditoRuralCronogramaDto cronograma = result.getCronograma();

		// contexto.setResposta(tabelaPrice);

		return false;
	}
}