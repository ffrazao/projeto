package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.pendencia.VerificarPendenciasCmd;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PendenciaCodigo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;

@Service("PessoaVerifPendTelefoneCmd")
public class VerifPendTelefoneCmd extends VerificarPendenciasCmd {

	@Override
	public String constatarPendencia() {
		Pessoa pessoa = (Pessoa) getPendenciavel();

		List<String> mensagemList = new ArrayList<String>();
		if (pessoa.getTelefoneList() != null) {
			for (int i = pessoa.getTelefoneList().size() - 1; i >= 0; i--) {
				if (pessoa.getTelefoneList().get(i).getTelefone() == null) {
					pessoa.getTelefoneList().remove(i);
				}
				String informado = pessoa.getTelefoneList().get(i).getTelefone().getNumero();
				if (informado == null || informado.trim().length() == 0) {
					pessoa.getTelefoneList().remove(i);
				}
				if (!UtilitarioString.soNumero(informado).equals(UtilitarioString.soNumero(UtilitarioString.formataTelefone(informado)))) {
					mensagemList.add(String.format("Telefone, número ou formato inválido [%s]", informado));
				} else {
					pessoa.getTelefoneList().get(i).getTelefone().setNumero(UtilitarioString.formataTelefone(informado));
				}
			}
		}
		if (mensagemList.size() == 0) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (String mensagem : mensagemList) {
			sb.append(mensagem).append("\n");
		}
		return sb.toString();
	}

	@Override
	public PendenciaCodigo getCodigo() {
		return PendenciaCodigo.TELEFONE;
	}

}