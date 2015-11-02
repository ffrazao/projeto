package br.gov.df.emater.aterwebsrv.bo.pessoa;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaSituacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaTipo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;

@Service("PessoaNovoCmd")
public class NovoCmd extends _Comando {

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Pessoa result = null;

		PessoaTipo pessoaTipo = (PessoaTipo) contexto.getRequisicao();

		if (pessoaTipo == null) {
			throw new BoException("Tipo de Pessoa não informado");
		}

		switch (pessoaTipo) {
		case PF:
			result = new PessoaFisica();
			break;
		case PJ:
			result = new PessoaJuridica();
			break;
		default:
			throw new BoException("Tipo de Pessoa inválido");
		}

		result.setSituacao(PessoaSituacao.A);
		result.setPublicoAlvoConfirmacao(Confirmacao.N);
		contexto.setResposta(result);

		return true;
	}

}