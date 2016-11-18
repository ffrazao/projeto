package br.gov.df.emater.aterwebsrv.bo.agenda;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dto.agenda.AgendaCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaTipo;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

@Service("AgendaFiltroNovoCmd")
public class FiltroNovoCmd extends _Comando {

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		AgendaCadFiltroDto filtro = new AgendaCadFiltroDto();
		Calendar hoje = Calendar.getInstance();
		Calendar inicio = new GregorianCalendar(hoje.get(Calendar.YEAR), hoje.get(Calendar.MONTH), 1);
		inicio.add(Calendar.MONTH, -6);
		Calendar termino = new GregorianCalendar(hoje.get(Calendar.YEAR), hoje.get(Calendar.MONTH), hoje.get(Calendar.DATE));
		filtro.setInicio(inicio);
		filtro.setTermino(termino);
		Set<Integer> pessoaIdList = new HashSet<>();

		Usuario usuario = getUsuario(contexto.getUsuario().getName());
		if (usuario != null && PessoaTipo.PF.equals(usuario.getPessoa().getPessoaTipo())) {
			pessoaIdList.add(usuario.getPessoa().getId());
		} else {
			pessoaIdList.add(null);
		}
		filtro.setPessoaIdList(pessoaIdList);
		contexto.setResposta(filtro);
		return false;
	}

}
