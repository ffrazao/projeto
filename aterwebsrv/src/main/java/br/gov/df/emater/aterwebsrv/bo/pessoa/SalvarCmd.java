package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.pessoa.EmailDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.EnderecoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaEmailDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaEnderecoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaTelefoneDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.TelefoneDao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.CadastroAcao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Email;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Endereco;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaEmail;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaEndereco;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaTelefone;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Telefone;

@Service("PessoaSalvarCmd")
public class SalvarCmd extends _Comando {

	@Autowired
	private PessoaDao dao;

	@Autowired
	private EnderecoDao enderecoDao;

	@Autowired
	private PessoaEnderecoDao pessoaEnderecoDao;

	@Autowired
	private TelefoneDao telefoneDao;

	@Autowired
	private PessoaTelefoneDao pessoaTelefoneDao;

	@Autowired
	private EmailDao emailDao;

	@Autowired
	private PessoaEmailDao pessoaEmailDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Pessoa pessoa = (Pessoa) contexto.getRequisicao();
		if (pessoa.getId() == null) {
			pessoa.setUsuarioInclusao(getUsuario(contexto.getUsuario().getName()));
			pessoa.setInclusaoData(Calendar.getInstance());
		} else {
			pessoa.setUsuarioInclusao(getUsuario(pessoa.getUsuarioInclusao().getUsername()));
		}
		pessoa.setUsuarioAlteracao(getUsuario(contexto.getUsuario().getName()));
		pessoa.setAlteracaoData(Calendar.getInstance());
		dao.saveAndFlush(pessoa);

		// salvar enderecos
		if (pessoa.getEnderecoList() != null) {
			// tratar a exclusao de registros
			for (PessoaEndereco pessoaEndereco : pessoa.getEnderecoList()) {
				if (CadastroAcao.E.equals(pessoaEndereco.getCadastroAcao())) {
					pessoaEnderecoDao.delete(pessoaEndereco);
				}
			}
			pessoaEnderecoDao.flush();
			// tratar a insersao de registros
			Integer ordem = 0;
			for (PessoaEndereco pessoaEndereco : pessoa.getEnderecoList()) {
				if (!CadastroAcao.E.equals(pessoaEndereco.getCadastroAcao())) {
					Endereco endereco = pessoaEndereco.getEndereco();
					List<Endereco> pesquisa = enderecoDao.procurar(endereco);
					if (CollectionUtils.isEmpty(pesquisa)) {
						if (endereco.getPropriedadeRuralConfirmacao() == null) {
							endereco.setPropriedadeRuralConfirmacao(Confirmacao.N);
						}
						endereco = enderecoDao.save(pessoaEndereco.getEndereco());
					}
					pessoaEndereco.setEndereco(endereco);
					pessoaEndereco.setPessoa(pessoa);
					pessoaEndereco.setOrdem(++ordem);
					pessoaEnderecoDao.save(pessoaEndereco);
				}
			}
		}

		// salvar telefones
		if (pessoa.getTelefoneList() != null) {
			// tratar a exclusao de registros
			for (PessoaTelefone pessoaTelefone : pessoa.getTelefoneList()) {
				if (CadastroAcao.E.equals(pessoaTelefone.getCadastroAcao())) {
					pessoaTelefoneDao.delete(pessoaTelefone);
				}
			}
			pessoaTelefoneDao.flush();
			// tratar a insersao de registros
			Integer ordem = 0;
			for (PessoaTelefone pessoaTelefone : pessoa.getTelefoneList()) {
				if (!CadastroAcao.E.equals(pessoaTelefone.getCadastroAcao())) {
					Telefone telefone = pessoaTelefone.getTelefone();
					telefone = telefoneDao.findByNumero(telefone.getNumero());
					if (telefone == null) {
						telefone = telefoneDao.save(pessoaTelefone.getTelefone());
					}
					pessoaTelefone.setTelefone(telefone);
					pessoaTelefone.setPessoa(pessoa);
					pessoaTelefone.setOrdem(++ordem);
					pessoaTelefoneDao.save(pessoaTelefone);
				}
			}
		}

		// salvar emails
		if (pessoa.getEmailList() != null) {
			// tratar a exclusao de registros
			for (PessoaEmail pessoaEmail : pessoa.getEmailList()) {
				if (CadastroAcao.E.equals(pessoaEmail.getCadastroAcao())) {
					pessoaEmailDao.delete(pessoaEmail);
				}
			}
			pessoaEmailDao.flush();
			// tratar a insersao de registros
			Integer ordem = 0;
			for (PessoaEmail pessoaEmail : pessoa.getEmailList()) {
				if (!CadastroAcao.E.equals(pessoaEmail.getCadastroAcao())) {
					Email email = pessoaEmail.getEmail();
					email = emailDao.findByEndereco(email.getEndereco());
					if (email == null) {
						email = emailDao.save(pessoaEmail.getEmail());
					}
					pessoaEmail.setEmail(email);
					pessoaEmail.setPessoa(pessoa);
					pessoaEmail.setOrdem(++ordem);
					pessoaEmailDao.save(pessoaEmail);
				}
			}
		}
		contexto.setResposta(pessoa.getId());
		return true;
	}

}