package br.gov.df.emater.aterwebsrv.bo.projeto_credito_rural;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.bo._SalvarCmd;
import br.gov.df.emater.aterwebsrv.dao.projeto_credito_rural.ProjetoCreditoRuralDao;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRural;

@Service("ProjetoCreditoRuralSalvarCmd")
public class SalvarCmd extends _SalvarCmd {

	@Autowired
	private ProjetoCreditoRuralDao dao;

	public SalvarCmd() {
	}

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		ProjetoCreditoRural result = (ProjetoCreditoRural) contexto.getRequisicao();

		// captar o registro de atualização da tabela
		logAtualizar(result, contexto);

		dao.save(result);

		dao.flush();

		contexto.setResposta(result.getId());
		return false;
	}
}