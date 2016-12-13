package br.gov.df.emater.aterwebsrv.bo.atividade;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.dto.TagDto;
import br.gov.df.emater.aterwebsrv.dto.atividade.AtividadeCadFiltroDto;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioData;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.seguranca.UserAuthentication;

@Service("AtividadeFiltroNovoCmd")
public class FiltroNovoCmd implements Command {

	@SuppressWarnings("unchecked")
	@Override
	public boolean execute(Context context) throws Exception {
		AtividadeCadFiltroDto filtro = new AtividadeCadFiltroDto();

		Map<String, Object> requisicao = (Map<String, Object>) context.get("requisicao");

		Usuario usuario = ((UserAuthentication) context.get("usuario")).getDetails();

		String opcao = (String) requisicao.get("opcao");

		if ("demandar".equals(opcao)) {
			filtro.setDemandanteList(getListaPrepreenchida(usuario));
		} else {
			filtro.setExecutorList(getListaPrepreenchida(usuario));
		}

		Calendar hoje = Calendar.getInstance();

		Calendar inicio = new GregorianCalendar(hoje.get(Calendar.YEAR), hoje.get(Calendar.MONTH), 1);
		inicio.add(Calendar.MONTH, -3);
		
		Calendar termino = new GregorianCalendar(hoje.get(Calendar.YEAR), hoje.get(Calendar.MONTH), hoje.get(Calendar.DATE));

		filtro.setInicio(inicio);
		filtro.setTermino(termino);

		context.put("resposta", filtro);
		return false;
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
