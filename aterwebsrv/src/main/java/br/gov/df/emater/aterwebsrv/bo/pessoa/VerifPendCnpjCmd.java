package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Contexto;
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
	public String constatarPendencia(_Contexto contexto) {
		if (!(getPendenciavel() instanceof PessoaJuridica)) {
			return null;
		}
		PessoaJuridica pessoa = (PessoaJuridica) getPendenciavel();

		String numero = pessoa.getCnpj();
		if (StringUtils.isBlank(numero)) {
			pessoa.setCnpj(null);
			return null;
		}
		/*
		if (!Util.isCnpjValido(numero)) {
			pessoa.setCnpj(null);
			return String.format("O número de CNPJ informado [%s] é inválido", numero);
		}
        
		// formatar o número
		/* numero = UtilitarioString.formataCnpj(numero);
		
		// pesquisa de PessoaJuridica
		List<PessoaJuridica> salvoList = null;
		salvoList = dao.findByCnpj(numero);
		if (salvoList != null) {
			for (PessoaJuridica salvo : salvoList) {
				if (!salvo.getId().equals(pessoa.getId())) {
					pessoa.setCnpj(null);
					return String.format("O número de CNPJ informado [%s] já está vinculado à pessoa <a ng-click=\"modalVerPessoa(%d)\">%s</a><br>", numero, salvo.getId(), salvo.getNome());
				}
			}
		}
		*/
		return null;
	}

	@Override
	public PendenciaCodigo getCodigo() {
		return PendenciaCodigo.CNPJ;
	}

}