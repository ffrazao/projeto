//30112002
package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.bo.pendencia.VerificarPendenciasCmd;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaFisicaDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaRelacionamentoDao;
import br.gov.df.emater.aterwebsrv.ferramenta.Util;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PendenciaCodigo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaRelacionamento;

@Service("PessoaVerifPendCpfCmd")
public class VerifPendCpfCmd extends VerificarPendenciasCmd {

	@Autowired
	private PessoaFisicaDao pessoaFisicaDao;

	@Autowired
	private PessoaRelacionamentoDao pessoaRelacionamentoDao;

	@Override
	public String constatarPendencia(_Contexto contexto) {

		PessoaFisica pessoaFisica = null;
		PessoaRelacionamento pessoaRelacionamento = null;
		String numero = null;

		if (contexto.getRequisicao() instanceof PessoaRelacionamento) {
			pessoaRelacionamento = (PessoaRelacionamento) contexto.getRequisicao();
			numero = pessoaRelacionamento.getCpf();
		} else if (getPendenciavel() instanceof PessoaFisica) {
			pessoaFisica = (PessoaFisica) getPendenciavel();
			numero = pessoaFisica.getCpf();
		} else {
			return null;
		}
		// TODO fazer verificacao no proprio registro se pf ver em seus relac.
		// se relac ver principa e outros relacs
		if (StringUtils.isBlank(numero)) {
			if (pessoaFisica == null) {
				pessoaRelacionamento.setCpf(null);
			} else {
				pessoaFisica.setCpf(null);
			}
			return null;
		}
		if (!Util.isCpfValido(numero)) {
			if (pessoaFisica == null) {
				pessoaRelacionamento.setCpf(null);
				return String.format("O número de CPF informado da pessoa relacionada [%s] é inválido", numero);
			} else {
				pessoaFisica.setCpf(null);
				return String.format("O número de CPF informado [%s] é inválido", numero);
			}
		}

		// formatar o número
		numero = UtilitarioString.formataCpf(numero);

		// pesquisa de PessoaFisica ou PessoaRelacionamento em PessoaFisica
		List<PessoaFisica> salvoPessoaFisicaList = pessoaFisicaDao.findByCpf(numero);
		if (salvoPessoaFisicaList != null) {
			for (PessoaFisica salvo : salvoPessoaFisicaList) {
				if (pessoaFisica != null) {
					// pesquisa de PessoaFisica em PessoaFisica
					if (!salvo.getId().equals(pessoaFisica.getId())) {
						pessoaFisica.setCpf(null);
						return String.format("O número de CPF informado [%s] já está vinculado à pessoa <a ng-click=\"modalVerPessoa(%d)\">%s</a><br>", numero, salvo.getId(), salvo.getNome());
					}
				} else {
					// pesquisa de PessoaRelacionamento em PessoaFisica
					pessoaRelacionamento.setCpf(null);
					return String.format("O número de CPF informado [%s] da pessoa vinculada [%s] já está vinculado à pessoa <a ng-click=\"modalVerPessoa(%d)\">%s</a><br>", numero, pessoaRelacionamento.getNome(), salvo.getId(), salvo.getNome());
				}
			}
		}

		// pesquisa de PessoaFisica ou PessoaRelacionamento em
		// PessoaRelacionamento
		List<PessoaRelacionamento> salvoPessoaRelacionamentoList = pessoaRelacionamentoDao.findByCpf(numero);
		if (salvoPessoaRelacionamentoList != null) {
			for (PessoaRelacionamento salvoPessoaRelacionamento : salvoPessoaRelacionamentoList) {
				Pessoa relacionado = null;
				List<PessoaRelacionamento> pessoaRelacionamentoList = salvoPessoaRelacionamento.getRelacionamento().getPessoaRelacionamentoList();
				if (pessoaRelacionamentoList == null) {
					pessoaRelacionamentoList = pessoaRelacionamentoDao.findByRelacionamento(salvoPessoaRelacionamento.getRelacionamento());
				}
				for (PessoaRelacionamento pr : pessoaRelacionamentoList) {
					if (pr.getPessoa() != null) {
						relacionado = pr.getPessoa();
						break;
					}
				}
				if (pessoaFisica != null) {
					// pesquisa de PessoaFisica em PessoaRelacionamento
					pessoaFisica.setCpf(null);
					return String.format("O número de CPF informado [%s] já está vinculado à pessoa [%s] que está relacionada a pessoa <a ng-click=\"modalVerPessoa(%d)\">%s</a><br>", numero, salvoPessoaRelacionamento.getNome(), relacionado.getId(), relacionado.getNome());
				} else {
					// pesquisa de PessoaRelacionamento em PessoaRelacionamento
					if (!salvoPessoaRelacionamento.getId().equals(pessoaRelacionamento.getId())) {
						pessoaRelacionamento.setCpf(null);
						return String.format("O número de CPF informado [%s] da pessoa vinculada [%s] já está vinculado à uma vinculada a pessoa <a ng-click=\"modalVerPessoa(%d)\">%s</a><br>", numero, pessoaRelacionamento.getNome(), relacionado.getId(), relacionado.getNome());
					}
				}
			}
		}

		// atualizar o cpf com valor formatado
		if (pessoaFisica == null) {
			pessoaRelacionamento.setCpf(numero);
		} else {
			pessoaFisica.setCpf(numero);
		}
		return null;
	}

	@Override
	public PendenciaCodigo getCodigo() {
		return PendenciaCodigo.CPF;
	}

}