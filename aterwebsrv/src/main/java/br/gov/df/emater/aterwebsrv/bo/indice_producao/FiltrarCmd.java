package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoProprietarioDao;
import br.gov.df.emater.aterwebsrv.dto.indice_producao.IndiceProducaoCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoProprietario;

@Service("IndiceProducaoFiltrarCmd")
public class FiltrarCmd extends _Comando {

	@Autowired
	private ProducaoProprietarioDao dao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		IndiceProducaoCadFiltroDto filtro = (IndiceProducaoCadFiltroDto) contexto.getRequisicao();
		filtro.setSituacao("Esperado");
		List<ProducaoProprietario> result = dao.filtrarNovo(filtro);
		contexto.setResposta(result);
		return false;
	}

}