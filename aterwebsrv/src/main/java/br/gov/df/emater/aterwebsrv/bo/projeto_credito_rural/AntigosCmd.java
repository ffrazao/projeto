package br.gov.df.emater.aterwebsrv.bo.projeto_credito_rural;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.projeto_credito_rural.ProjetoCreditoAntigoDao;
import br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural.ProjetoCreditoAntigoCadFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural.ProjetoCreditoRuralCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoAntigo;

@Service("ProjetoCreditoRuralAntigosCmd")
public class AntigosCmd extends _Comando {

	@Autowired
	private ProjetoCreditoAntigoDao dao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		ProjetoCreditoAntigo filtro = (ProjetoCreditoAntigo) contexto.getRequisicao();
		
		if(filtro.getId() == 0){
			List<ProjetoCreditoAntigo> result = null;		
			result = (List<ProjetoCreditoAntigo>) dao.findAll();
			contexto.setResposta(result);
		}
		
		if(filtro.getId() >= 1){
			ProjetoCreditoAntigo result = null;
			result = dao.findById(filtro.getId());
			contexto.setResposta(result);
		}
			
		
		return false;
	}

}
