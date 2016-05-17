package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.pendencia.VerificarPendenciasCmd;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaDao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PendenciaCodigo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;

@Service("PessoaVerifPendInscricaoEstadualCmd")
public class VerifPendInscricaoEstadualCmd extends VerificarPendenciasCmd {

	@Autowired
	private PessoaDao pessoaDao;

	@Override
	public String constatarPendencia() {
		Pessoa pessoa = (Pessoa) getPendenciavel();
		String uf = pessoa.getInscricaoEstadualUf();
		String numero = pessoa.getInscricaoEstadual();
		if (numero == null || numero.trim().length() == 0) {
			pessoa.setInscricaoEstadualUf(null);
			pessoa.setInscricaoEstadual(null);
			return null;
		}
		List<Pessoa> salvoList = null;
		if (uf == null || uf.trim().length() == 0) {
			salvoList = pessoaDao.findByInscricaoEstadual(numero);
		} else {
			salvoList = pessoaDao.findByInscricaoEstadualUfAndInscricaoEstadual(uf, numero);
		}
		if (salvoList != null) {
			for (Pessoa salvo : salvoList) {
				if (!salvo.getId().equals(pessoa.getId())) {
					pessoa.setInscricaoEstadualUf(null);
					pessoa.setInscricaoEstadual(null);
					return String.format("A Inscrição Estadual informada [%s, %s] já está vinculada à pessoa <a ng-click=\"modalVerPessoa(%d)\">%s</a><br>", uf, numero, salvo.getId(), salvo.getNome());
				}
			}
		}
		return null;
	}

	@Override
	public PendenciaCodigo getCodigo() {
		return PendenciaCodigo.INSCRICAO_ESTADUAL;
	}

}