package br.gov.df.emater.aterwebsrv.bo.projeto_credito_rural;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.bo._SalvarCmd;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioFinanceiro;
import br.gov.df.emater.aterwebsrv.modelo.dto.ProjetoCreditoRuralCronogramaDto;

@Service("ProjetoCreditoRuralCalcularCronogramaCmd")
public class CalcularCronogramaCmd extends _SalvarCmd {

	public CalcularCronogramaCmd() {
	}

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		ProjetoCreditoRuralCronogramaDto result = (ProjetoCreditoRuralCronogramaDto) contexto.getRequisicao();
				
		List<Map<String, Object>> tabelaPrice = UtilitarioFinanceiro.getInstance().tabelaPriceCalculaParcelas(result.getValorPresenteStr(), result.getTaxaJurosStr(), result.getTotalParcelas());

		contexto.setResposta(tabelaPrice);
		
		return false;
	}
}