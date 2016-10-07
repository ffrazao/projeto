package br.gov.df.emater.aterwebsrv.bo.projeto_credito_rural;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoProprietarioDao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoProprietario;

@Service("ProjetoCreditoRuralExcluirCmd")
public class ExcluirCmd extends _Comando {

	@Autowired
	private ProducaoProprietarioDao dao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		ProducaoProprietario producaoProprietario = (ProducaoProprietario) contexto.getRequisicao();
		producaoProprietario = dao.findOne(producaoProprietario.getId());

		List<ProducaoProprietario> produtorProducaoProprietarioList = dao.findByAnoAndBemClassificadoAndPropriedadeRuralComunidadeUnidadeOrganizacional(producaoProprietario.getAno(), producaoProprietario.getBemClassificado(), producaoProprietario.getUnidadeOrganizacional());

		if (produtorProducaoProprietarioList != null) {
			for (ProducaoProprietario p : produtorProducaoProprietarioList) {
				dao.delete(p);
			}
		}
		dao.delete(producaoProprietario);

		contexto.setResposta(producaoProprietario);
		return false;
	}

}