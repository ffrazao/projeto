package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.dto.pessoa.PessoaCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaSituacao;

@Service("PessoaFiltroNovoCmd")
public class FiltroNovoCmd implements Command {

	@SuppressWarnings("unchecked")
	@Override
	public boolean execute(Context context) throws Exception {
		PessoaCadFiltroDto filtro = new PessoaCadFiltroDto();
		Set<PessoaSituacao> pessoaSituacaoList = new HashSet<>();
		pessoaSituacaoList.add(PessoaSituacao.A);
		filtro.setPessoaSituacao(pessoaSituacaoList);
		context.put("resultado", filtro);
		return false;
	}

}
