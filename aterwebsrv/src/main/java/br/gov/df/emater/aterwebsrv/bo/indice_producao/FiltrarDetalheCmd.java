package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoProprietarioDao;
import br.gov.df.emater.aterwebsrv.dto.indice_producao.IndiceProducaoCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoProprietario;

@Service("IndiceProducaoFiltrarDetalheCmd")
public class FiltrarDetalheCmd extends _Comando {

	@Autowired
	private ProducaoProprietarioDao dao;

	@SuppressWarnings("unchecked")
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		IndiceProducaoCadFiltroDto filtro = (IndiceProducaoCadFiltroDto) contexto.getRequisicao();
		if (filtro.getId() == null && 
				(filtro.getPropriedadeRural() == null || filtro.getPropriedadeRural().getId() == null) && 
				(filtro.getPublicoAlvo() == null || filtro.getPublicoAlvo().getId() == null)) {
			List<ProducaoProprietario> resposta = (List<ProducaoProprietario>) contexto.getResposta();
			filtro.setSituacao("Acumulado");
			if (!CollectionUtils.isEmpty(resposta)) {
				for (ProducaoProprietario producaoProprietario : resposta) {
					filtro.setAno(producaoProprietario.getAno());
					filtro.setBemClassificado(producaoProprietario.getBemClassificado());
					filtro.setUnidadeOrganizacional(producaoProprietario.getUnidadeOrganizacional());
					producaoProprietario.setProducaoProprietarioList(dao.filtrarNovo(filtro));
				}
			}
		}
		return false;
	}

}