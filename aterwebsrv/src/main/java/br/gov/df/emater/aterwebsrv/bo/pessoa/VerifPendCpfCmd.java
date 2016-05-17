package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.pendencia.VerificarPendenciasCmd;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaFisicaDao;
import br.gov.df.emater.aterwebsrv.ferramenta.Util;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PendenciaCodigo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica;

@Service("PessoaVerifPendCpfCmd")
public class VerifPendCpfCmd extends VerificarPendenciasCmd {

	@Autowired
	private PessoaFisicaDao dao;

	@Override
	public String constatarPendencia() {
		if (!(getPendenciavel() instanceof PessoaFisica)) {
			return null;
		}
		PessoaFisica pessoa = (PessoaFisica) getPendenciavel();

		String numero = pessoa.getCpf();
		if (numero == null || numero.trim().length() == 0) {
			pessoa.setCpf(null);
			return null;
		}
		if (!Util.isCpfValido(numero)) {
			pessoa.setCpf(null);
			return String.format("O número de CPF informado [%s] é inválido", numero);
		}
		pessoa.setCpf(UtilitarioString.formataCpf(numero));
		List<PessoaFisica> salvoList = null;
		salvoList = dao.findByCpf(pessoa.getCpf());
		if (salvoList != null) {
			for (PessoaFisica salvo : salvoList) {
				if (!salvo.getId().equals(pessoa.getId())) {
					pessoa.setCpf(null);
					return String.format("O número de CPF informado [%s] já está vinculado à pessoa <a ng-click=\"modalVerPessoa(%d)\">%s</a><br>", numero, salvo.getId(), salvo.getNome());
				}
			}
		}
		return null;
	}

	@Override
	public PendenciaCodigo getCodigo() {
		return PendenciaCodigo.CPF;
	}

}