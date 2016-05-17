package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.pendencia.VerificarPendenciasCmd;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaJuridicaDao;
import br.gov.df.emater.aterwebsrv.ferramenta.Util;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PendenciaCodigo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;

@Service("PessoaVerifPendCnpjCmd")
public class VerifPendCnpjCmd extends VerificarPendenciasCmd {

	@Autowired
	private PessoaJuridicaDao dao;

	@Override
	public String constatarPendencia() {
		if (!(getPendenciavel() instanceof PessoaJuridica)) {
			return null;
		}
		PessoaJuridica pessoa = (PessoaJuridica) getPendenciavel();

		String numero = pessoa.getCnpj();
		if (numero == null || numero.trim().length() == 0) {
			pessoa.setCnpj(null);
			return null;
		}
		if (!Util.isCnpjValido(numero)) {
			pessoa.setCnpj(null);
			return String.format("O número de CNPJ informado [%s] é inválido", numero);
		}
		pessoa.setCnpj(UtilitarioString.formataCnpj(numero));
		List<PessoaJuridica> salvoList = null;
		salvoList = dao.findByCnpj(pessoa.getCnpj());
		if (salvoList != null) {
			for (PessoaJuridica salvo : salvoList) {
				if (!salvo.getId().equals(pessoa.getId())) {
					pessoa.setCnpj(null);
					return String.format("O número de CNPJ informado [%s] já está vinculado à pessoa <a ng-click=\"modalVerPessoa(%d)\">%s</a><br>", numero, salvo.getId(), salvo.getNome());
				}
			}
		}
		return null;
	}

	@Override
	public PendenciaCodigo getCodigo() {
		return PendenciaCodigo.CNPJ;
	}

}