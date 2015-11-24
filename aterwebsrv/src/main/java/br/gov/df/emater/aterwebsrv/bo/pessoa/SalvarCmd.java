package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ater.PublicoAlvoDao;
import br.gov.df.emater.aterwebsrv.dao.ater.PublicoAlvoPropriedadeRuralDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.ArquivoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.EmailDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.EnderecoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaArquivoDao;
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
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoPropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.dominio.CadastroAcao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Arquivo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Email;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Endereco;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaArquivo;
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
	private ArquivoDao arquivoDao;

	@Autowired
	private PessoaArquivoDao pessoaArquivoDao;

	@Autowired
	private PessoaEmailDao pessoaEmailDao;

	@Autowired
	private PessoaRelacionamentoDao pessoaRelacionamentoDao;

	@Autowired
	private RelacionamentoDao relacionamentoDao;

	private RelacionamentoTipo relacionamentoTipo;

	@Autowired
	private PessoaGrupoSocialDao pessoaGrupoSocialDao;

	@Autowired
	private PublicoAlvoPropriedadeRuralDao publicoAlvoPropriedadeRuralDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Pessoa result = (Pessoa) contexto.getRequisicao();
		if (result.getId() == null) {
			result.setUsuarioInclusao(getUsuario(contexto.getUsuario().getName()));
			result.setInclusaoData(Calendar.getInstance());
		} else {
			result.setUsuarioInclusao(getUsuario(result.getUsuarioInclusao().getUsername()));
		}
		result.setUsuarioAlteracao(getUsuario(contexto.getUsuario().getName()));
		result.setAlteracaoData(Calendar.getInstance());

		// publico alvo
		if (Confirmacao.S.equals(result.getPublicoAlvoConfirmacao())) {
			PublicoAlvo publicoAlvo = result.getPublicoAlvo();
			if (publicoAlvo == null) {
				publicoAlvo = new PublicoAlvo();
			}
			publicoAlvo.setPessoa(result);
			publicoAlvoDao.save(publicoAlvo);

			// salvar as propriedades vinculadas ao publico alvo
			if (publicoAlvo.getPublicoAlvoPropriedadeRuralList() != null) {
				// tratar a exclusao de registros
				for (PublicoAlvoPropriedadeRural publicoAlvoPropriedadeRural : publicoAlvo.getPublicoAlvoPropriedadeRuralList()) {
					if (publicoAlvoPropriedadeRural == null) {
						continue;
					}
					if (CadastroAcao.E.equals(publicoAlvoPropriedadeRural.getCadastroAcao())) {
						publicoAlvoPropriedadeRuralDao.delete(publicoAlvoPropriedadeRural);
					}
				}
				// tratar a insersao de registros
				for (PublicoAlvoPropriedadeRural publicoAlvoPropriedadeRural : publicoAlvo.getPublicoAlvoPropriedadeRuralList()) {
					if (publicoAlvoPropriedadeRural == null) {
						continue;
					}
					if (!CadastroAcao.E.equals(publicoAlvoPropriedadeRural.getCadastroAcao())) {
						publicoAlvoPropriedadeRural.setPublicoAlvo(publicoAlvo);
						publicoAlvoPropriedadeRuralDao.save(publicoAlvoPropriedadeRural);
					}
				}
			}
		}

		dao.save(result);

		// salvar enderecos
		if (result.getEnderecoList() != null) {
			// tratar a exclusao de registros
			for (PessoaEndereco pessoaEndereco : result.getEnderecoList()) {
				if (CadastroAcao.E.equals(pessoaEndereco.getCadastroAcao())) {
					pessoaEnderecoDao.delete(pessoaEndereco);
				}
			}
			// tratar a insersao de registros
			Integer ordem = 0;
			for (PessoaEndereco pessoaEndereco : result.getEnderecoList()) {
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
					pessoaEndereco.setPessoa(result);
					pessoaEndereco.setOrdem(++ordem);
					pessoaEnderecoDao.save(pessoaEndereco);
				}
			}
		}

		// salvar telefones
		if (result.getTelefoneList() != null) {
			// tratar a exclusao de registros
			for (PessoaTelefone pessoaTelefone : result.getTelefoneList()) {
				if (CadastroAcao.E.equals(pessoaTelefone.getCadastroAcao())) {
					pessoaTelefoneDao.delete(pessoaTelefone);
				}
			}
			// tratar a insersao de registros
			Integer ordem = 0;
			for (PessoaTelefone pessoaTelefone : result.getTelefoneList()) {
				if (!CadastroAcao.E.equals(pessoaTelefone.getCadastroAcao())) {
					Telefone telefone = pessoaTelefone.getTelefone();
					telefone = telefoneDao.findByNumero(telefone.getNumero());
					if (telefone == null) {
						telefone = telefoneDao.save(pessoaTelefone.getTelefone());
					}
					pessoaTelefone.setTelefone(telefone);
					pessoaTelefone.setPessoa(result);
					pessoaTelefone.setOrdem(++ordem);
					pessoaTelefoneDao.save(pessoaTelefone);
				}
			}
		}

		// salvar emails
		if (result.getEmailList() != null) {
			// tratar a exclusao de registros
			for (PessoaEmail pessoaEmail : result.getEmailList()) {
				if (CadastroAcao.E.equals(pessoaEmail.getCadastroAcao())) {
					pessoaEmailDao.delete(pessoaEmail);
				}
			}
			// tratar a insersao de registros
			Integer ordem = 0;
			for (PessoaEmail pessoaEmail : result.getEmailList()) {
				if (!CadastroAcao.E.equals(pessoaEmail.getCadastroAcao())) {
					Email email = pessoaEmail.getEmail();
					email = emailDao.findByEndereco(email.getEndereco());
					if (email == null) {
						email = emailDao.save(pessoaEmail.getEmail());
					}
					pessoaEmail.setEmail(email);
					pessoaEmail.setPessoa(result);
					pessoaEmail.setOrdem(++ordem);
					pessoaEmailDao.save(pessoaEmail);
				}
			}
		}

		// salvar relacionamentos
		if (result.getRelacionamentoList() != null) {
			for (PessoaRelacionamento pessoaRelacionamento : result.getRelacionamentoList()) {
				if (CadastroAcao.E.equals(pessoaRelacionamento.getCadastroAcao())) {
					relacionamentoDao.delete(pessoaRelacionamento.getRelacionamento());
				}
			}
			for (PessoaRelacionamento pessoaRelacionamento : result.getRelacionamentoList()) {
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
							if (rel.getPessoa().getId().equals(result.getId())) {
								relacionador = rel;
								encontrou = true;
								break;
							}
						}
					}
					if (!encontrou) {
						relacionador = new PessoaRelacionamento();
						relacionador.setPessoa(result);
						relacionador.setRelacionamento(relacionamento);
					}
					relacionador.setRelacionamentoFuncao(new RelacionamentoFuncao(relacionamentoConfiguracaoVi.getRelacionadoId()));
					pessoaRelacionamentoDao.save(relacionador);
				}
			}
		}

		// salvar grupos sociais
		if (result.getGrupoSocialList() != null) {
			// tratar a exclusao de registros
			for (PessoaGrupoSocial pessoaGrupoSocial : result.getGrupoSocialList()) {
				if (CadastroAcao.E.equals(pessoaGrupoSocial.getCadastroAcao())) {
					pessoaGrupoSocialDao.delete(pessoaGrupoSocial);
				}
			}
			// tratar a insersao de registros
			for (PessoaGrupoSocial pessoaGrupoSocial : result.getGrupoSocialList()) {
				if (!CadastroAcao.E.equals(pessoaGrupoSocial.getCadastroAcao())) {
					pessoaGrupoSocial.setPessoa(result);
					pessoaGrupoSocialDao.save(pessoaGrupoSocial);
				}
			}
		}

		// salvar arquivos vinculados
		if (result.getArquivoList() != null) {
			// tratar a exclusao de registros
			for (PessoaArquivo pessoaArquivo : result.getArquivoList()) {
				if (CadastroAcao.E.equals(pessoaArquivo.getCadastroAcao())) {
					pessoaArquivoDao.delete(pessoaArquivo);
				}
			}
			// tratar a insersao de registros
			for (PessoaArquivo pessoaArquivo : result.getArquivoList()) {
				if (!CadastroAcao.E.equals(pessoaArquivo.getCadastroAcao())) {
					Arquivo arquivo = pessoaArquivo.getArquivo();
					arquivo = arquivoDao.findByMd5(arquivo.getMd5());
					if (arquivo != null) {
						arquivo.setMd5(pessoaArquivo.getArquivo().getMd5());
						arquivo.setNomeOriginal(pessoaArquivo.getArquivo().getNomeOriginal());
						arquivo.setDataUpload(pessoaArquivo.getArquivo().getDataUpload());
						arquivo.setExtensao(pessoaArquivo.getArquivo().getExtensao());
						arquivo.setTamanho(pessoaArquivo.getArquivo().getTamanho());
						arquivo.setTipo(pessoaArquivo.getArquivo().getTipo());
					}
					arquivo = arquivoDao.save(pessoaArquivo.getArquivo());
					pessoaArquivo.setArquivo(arquivo);
					pessoaArquivo.setPessoa(result);
					pessoaArquivoDao.save(pessoaArquivo);
				}
			}
		}

		dao.flush();

		contexto.setResposta(result.getId());
		return true;
	}

}