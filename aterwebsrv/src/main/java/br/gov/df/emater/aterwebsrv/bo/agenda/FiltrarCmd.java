package br.gov.df.emater.aterwebsrv.bo.agenda;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.atividade.AtividadeDao;
import br.gov.df.emater.aterwebsrv.dto.agenda.AgendaCadFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.agenda.AgendaDto;

@Service("AgendaFiltrarCmd")
public class FiltrarCmd extends _Comando {

	@Autowired
	private AtividadeDao atividadeDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		AgendaCadFiltroDto filtro = (AgendaCadFiltroDto) contexto.getRequisicao();
		List<AgendaDto> result = null;
		result = (List<AgendaDto>) atividadeDao.filtrarAgenda(filtro);
		contexto.setResposta(result);
		return false;
	}

}
