package br.gov.df.emater.aterwebsrv.bo.projeto_credito_rural;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dto.TagDto;
import br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural.ProjetoCreditoRuralCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.seguranca.UserAuthentication;

@Service("ProjetoCreditoRuralFiltroNovoCmd")
public class FiltroNovoCmd extends _Comando {

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		ProjetoCreditoRuralCadFiltroDto filtro = new ProjetoCreditoRuralCadFiltroDto();

		Usuario usuario = ((UserAuthentication) contexto.get("usuario")).getDetails();

		filtro.setExecutorList(getListaPrepreenchida(usuario));

		getPeridoBusca(filtro);

		contexto.setResposta(filtro);
		return false;
	}

	private void getPeridoBusca(ProjetoCreditoRuralCadFiltroDto filtro) {
		Calendar hoje = Calendar.getInstance();

		Calendar inicio = new GregorianCalendar(hoje.get(Calendar.YEAR), 0, 1);
		inicio.roll(Calendar.YEAR, -1);

		Calendar termino = new GregorianCalendar(hoje.get(Calendar.YEAR), hoje.get(Calendar.MONTH), hoje.get(Calendar.DATE));

		filtro.setInicio(inicio);
		filtro.setTermino(termino);
	}

	private Set<TagDto> getListaPrepreenchida(Usuario usuario) {
		Set<TagDto> lista = new HashSet<>();

		if (usuario.getPessoa() != null) {
			lista.add(getValorDaBusca(usuario.getPessoa().getNome()));
		}
		if (usuario.getLotacaoAtual() != null) {
			lista.add(getValorDaBusca(usuario.getLotacaoAtual().getNome()));
			if (usuario.getLotacaoAtual().getPessoaJuridica() != null) {
				lista.add(getValorDaBusca(usuario.getLotacaoAtual().getPessoaJuridica().getNome()));
			}
		}

		return lista;
	}

	private TagDto getValorDaBusca(String valor) {
		TagDto t = new TagDto();
		t.setText(valor);
		return t;
	}

}
