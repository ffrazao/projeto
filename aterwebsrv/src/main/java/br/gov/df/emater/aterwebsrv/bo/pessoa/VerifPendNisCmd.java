package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.bo.pendencia.VerificarPendenciasCmd;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaFisicaDao;
import br.gov.df.emater.aterwebsrv.ferramenta.Util;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PendenciaCodigo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica;

@Service("PessoaVerifPendNisCmd")
public class VerifPendNisCmd extends VerificarPendenciasCmd {

	@Autowired
	private PessoaFisicaDao dao;

	@Override
	public String constatarPendencia(_Contexto contexto) {
		if (!(getPendenciavel() instanceof PessoaFisica)) {
			return null;
		}
		PessoaFisica pessoa = (PessoaFisica) getPendenciavel();

		String numero = pessoa.getNisNumero();
		if (numero == null || numero.trim().length() == 0) {
			pessoa.setNisNumero(null);
			return null;
		}
		if (!Util.isNisValido(numero)) {
			return String.format("O número de NIS informado [%s] é inválido", numero);
		}
		pessoa.setNisNumero(UtilitarioString.formataNis(numero));
		List<PessoaFisica> salvoList = null;
		salvoList = dao.findByNisNumero(pessoa.getNisNumero());
		if (salvoList != null) {
			for (PessoaFisica salvo : salvoList) {
				if (!salvo.getId().equals(pessoa.getId())) {
					pessoa.setNisNumero(null);
					return String.format("O número de NIS informado [%s] já está vinculado à pessoa <a ng-click=\"modalVerPessoa(%d)\">%s</a><br>", numero, salvo.getId(), salvo.getNome());
				}
			}
		}
		return null;
	}

	@Override
	public PendenciaCodigo getCodigo() {
		return PendenciaCodigo.NIS;
	}

}