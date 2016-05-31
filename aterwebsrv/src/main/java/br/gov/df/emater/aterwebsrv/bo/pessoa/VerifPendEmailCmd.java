package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.bo.pendencia.VerificarPendenciasCmd;
import br.gov.df.emater.aterwebsrv.ferramenta.Util;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PendenciaCodigo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;

@Service("PessoaVerifPendEmailCmd")
public class VerifPendEmailCmd extends VerificarPendenciasCmd {

	@Override
	public String constatarPendencia(_Contexto contexto) {
		Pessoa pessoa = (Pessoa) getPendenciavel();

		List<String> mensagemList = new ArrayList<String>();
		if (pessoa.getEmailList() != null) {
			for (int i = pessoa.getEmailList().size() - 1; i >= 0; i--) {
				if (pessoa.getEmailList().get(i).getEmail() == null) {
					pessoa.getEmailList().remove(i);
					continue;
				}
				String informado = pessoa.getEmailList().get(i).getEmail().getEndereco();
				if (informado == null || informado.trim().length() == 0) {
					pessoa.getEmailList().remove(i);
					continue;
				}
				if (!Util.isEmailValido(informado)) {
					mensagemList.add(String.format("Email inválido [%s]", informado));
				} else {
					informado = UtilitarioString.formataEmail(informado);
					if (!Util.isEmailValido(informado)) {
						mensagemList.add(String.format("Email inválido [%s]", informado));
					} else {
						pessoa.getEmailList().get(i).getEmail().setEndereco(informado);
					}
				}
			}
		}
		return extraiResultado(mensagemList);
	}

	@Override
	public PendenciaCodigo getCodigo() {
		return PendenciaCodigo.EMAIL;
	}

}