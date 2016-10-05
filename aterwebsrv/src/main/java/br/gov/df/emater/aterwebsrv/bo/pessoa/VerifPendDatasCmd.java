//30112002
package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.bo.pendencia.VerificarPendenciasCmd;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PendenciaCodigo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica;

@Service("PessoaVerifPendDatasCmd")
public class VerifPendDatasCmd extends VerificarPendenciasCmd {

	@Override
	public String constatarPendencia(_Contexto contexto) {
		if (!(getPendenciavel() instanceof PessoaFisica)) {
			return null;
		}
		PessoaFisica pessoa = (PessoaFisica) getPendenciavel();

		Calendar dataNascimento = pessoa.getNascimento();
		List<String> pendenciaList = new ArrayList<>();

		// TODO fazer verificacao no proprio registro se pf ver em seus relac.
		// se relac ver principa e outros relacs
		if (dataNascimento != null) {
			if (dataNascimento.after(Calendar.getInstance())) {
				pendenciaList.add(String.format("A data de nascimento informada é inválida\n"));
			}
		}

		return extraiResultado(pendenciaList);
	}

	@Override
	public PendenciaCodigo getCodigo() {
		return PendenciaCodigo.DATA;
	}

}