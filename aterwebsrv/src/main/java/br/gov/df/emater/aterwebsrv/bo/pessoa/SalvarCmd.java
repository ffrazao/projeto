package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ater.PublicoAlvoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.EmailDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.EnderecoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaEmailDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaEnderecoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaGrupoSocialDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaRelacionamentoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaTelefoneDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.RelacionamentoConfiguracaoViDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.RelacionamentoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.RelacionamentoTipoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.TelefoneDao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.CadastroAcao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Email;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Endereco;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaEmail;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaEndereco;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaGrupoSocial;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaRelacionamento;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaTelefone;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Relacionamento;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.RelacionamentoConfiguracaoVi;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.RelacionamentoFuncao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.RelacionamentoTipo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Telefone;

@Service("PessoaSalvarCmd")
public class SalvarCmd extends _Comando {

	public SalvarCmd() {
	}

	private RelacionamentoTipo getRelacionamentoTipo() {
		if (this.relacionamentoTipo == null) {
			this.relacionamentoTipo = relacionamentoTipoDao.findByCodigo(RelacionamentoTipo.Codigo.FAMILIAR.toString());
		}
		return relacionamentoTipo;
	}

	@Autowired
	private RelacionamentoConfiguracaoViDao relacionamentoConfiguracaoViDao;

	@Autowired
	private PessoaDao dao;
	
	@Autowired
	private PublicoAlvoDao publicoAlvoDao;

	@Autowired
	private RelacionamentoTipoDao relacionamentoTipoDao;

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

	@Autowired
	private PessoaRelacionamentoDao pessoaRelacionamentoDao;

	@Autowired
	private RelacionamentoDao relacionamentoDao;

	private RelacionamentoTipo relacionamentoTipo;

	@Autowired
	private PessoaGrupoSocialDao pessoaGrupoSocialDao;

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
		
		if (Confirmacao.S.equals(pessoa.getPublicoAlvoConfirmacao())) {
			pessoa.getPublicoAlvo().setPessoa(pessoa);
			publicoAlvoDao.saveAndFlush(pessoa.getPublicoAlvo());
		}

		// salvar enderecos
		if (pessoa.getEnderecoList() != null) {
			// tratar a exclusao de registros
			for (PessoaEndereco pessoaEndereco : pessoa.getEnderecoList()) {
				if (CadastroAcao.E.equals(pessoaEndereco.getCadastroAcao())) {
					pessoaEnderecoDao.delete(pessoaEndereco);
				}
			}
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

		// salvar relacionamentos
		if (pessoa.getRelacionamentoList() != null) {
			for (PessoaRelacionamento pessoaRelacionamento : pessoa.getRelacionamentoList()) {
				if (CadastroAcao.E.equals(pessoaRelacionamento.getCadastroAcao())) {
					relacionamentoDao.delete(pessoaRelacionamento.getRelacionamento());
				}
			}
			for (PessoaRelacionamento pessoaRelacionamento : pessoa.getRelacionamentoList()) {
				if (!CadastroAcao.E.equals(pessoaRelacionamento.getCadastroAcao())) {
					Relacionamento relacionamento = pessoaRelacionamento.getRelacionamento();

					if (relacionamento == null || relacionamento.getId() == null) {
						relacionamento = new Relacionamento();
						relacionamento.setRelacionamentoTipo(getRelacionamentoTipo());
					} else {
						Relacionamento salvo = relacionamentoDao.findById(relacionamento.getId());
						salvo.getPessoaRelacionamentoList().size();
						relacionamento.setPessoaRelacionamentoList(salvo.getPessoaRelacionamentoList());
					}
					relacionamento = relacionamentoDao.saveAndFlush(relacionamento);

					pessoaRelacionamento.setRelacionamento(relacionamento);
					pessoaRelacionamentoDao.save(pessoaRelacionamento);

					RelacionamentoConfiguracaoVi relacionamentoConfiguracaoVi = relacionamentoConfiguracaoViDao.findByTipoIdAndRelacionadorId(getRelacionamentoTipo().getId(), pessoaRelacionamento.getRelacionamentoFuncao().getId());

					// salvar o relacionador
					PessoaRelacionamento relacionador = null;
					boolean encontrou = false;
					if (relacionamento.getPessoaRelacionamentoList() != null) {
						for (PessoaRelacionamento rel : relacionamento.getPessoaRelacionamentoList()) {
							if (rel.getPessoa().getId().equals(pessoa.getId())) {
								relacionador = rel;
								encontrou = true;
								break;
							}
						}
					}
					if (!encontrou) {
						relacionador = new PessoaRelacionamento();
						relacionador.setPessoa(pessoa);
						relacionador.setRelacionamento(relacionamento);
					}
					relacionador.setRelacionamentoFuncao(new RelacionamentoFuncao(relacionamentoConfiguracaoVi.getRelacionadoId()));
					pessoaRelacionamentoDao.save(relacionador);
				}
			}
		}

		// salvar grupos sociais
		if (pessoa.getGrupoSocialList() != null) {
			// tratar a exclusao de registros
			for (PessoaGrupoSocial pessoaGrupoSocial : pessoa.getGrupoSocialList()) {
				if (CadastroAcao.E.equals(pessoaGrupoSocial.getCadastroAcao())) {
					pessoaGrupoSocialDao.delete(pessoaGrupoSocial);
				}
			}
			// tratar a insersao de registros
			for (PessoaGrupoSocial pessoaGrupoSocial : pessoa.getGrupoSocialList()) {
				if (!CadastroAcao.E.equals(pessoaGrupoSocial.getCadastroAcao())) {
					pessoaGrupoSocial.setPessoa(pessoa);
					pessoaGrupoSocialDao.save(pessoaGrupoSocial);
				}
			}
		}
		
		dao.flush();

		contexto.setResposta(pessoa.getId());
		return true;
	}

}