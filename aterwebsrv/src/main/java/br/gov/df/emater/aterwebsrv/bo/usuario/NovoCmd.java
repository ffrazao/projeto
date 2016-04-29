package br.gov.df.emater.aterwebsrv.bo.usuario;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaDao;
import br.gov.df.emater.aterwebsrv.dao.sistema.UsuarioDao;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.UsuarioStatusConta;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaEmail;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

@Service("UsuarioNovoCmd")
public class NovoCmd extends _Comando {

	@Autowired
	private UsuarioDao dao;

	@Autowired
	private PessoaDao pessoaDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Usuario result = (Usuario) contexto.getRequisicao();

		if (result == null) {
			result = new Usuario();
		}
		if (result.getPessoa() != null && result.getPessoa().getId() != null) {
			Pessoa salvo = pessoaDao.findOne(result.getPessoa().getId());
			if (salvo == null) {
				throw new BoException("Registro de Pessoa Inexistente!");
			}
			if (!dao.findByPessoa(salvo).isEmpty()) {
				throw new BoException("Usuário já cadastrado!");
			}
			result.setPessoa(salvo.infoBasica());
			result.getPessoa().setObservacoes(salvo.getObservacoes());
			List<PessoaEmail> pessoaEmailList = new ArrayList<PessoaEmail>();
			for (PessoaEmail pessoaEmail : salvo.getEmailList()) {
				pessoaEmailList.add(new PessoaEmail(pessoaEmail.getId(), pessoaEmail.getEmail().infoBasica()));
			}
			if (pessoaEmailList.size() == 0) {
				throw new BoException("Nenhum e-mail vinculado a esta pessoa!");
			} else  if (pessoaEmailList.size() == 1) {
				result.setPessoaEmail(new PessoaEmail(pessoaEmailList.get(0).getId(), pessoaEmailList.get(0).getEmail().infoBasica()));
			}
			result.getPessoa().setEmailList(pessoaEmailList);

			result.setUsername(UtilitarioString.calculaNomeUsuario(result.getPessoa().getNome()));
		}

		result.setId(null);
		result.setUsuarioStatusConta(UsuarioStatusConta.A);
		result.setUsuarioAtualizouPerfil(Confirmacao.N);

		contexto.setResposta(result);

		return true;
	}

}