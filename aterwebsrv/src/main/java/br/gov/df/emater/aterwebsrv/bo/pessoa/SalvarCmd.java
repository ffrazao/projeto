package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ater.PublicoAlvoDao;
import br.gov.df.emater.aterwebsrv.dao.ater.PublicoAlvoPropriedadeRuralDao;
import br.gov.df.emater.aterwebsrv.dao.ater.PublicoAlvoSetorDao;
import br.gov.df.emater.aterwebsrv.dao.formulario.ColetaDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.ArquivoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.EmailDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.EnderecoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.EstadoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.MunicipioDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PaisDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaArquivoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaEmailDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaEnderecoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaGrupoSocialDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaPendenciaDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaRelacionamentoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaTelefoneDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.RelacionamentoConfiguracaoViDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.RelacionamentoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.TelefoneDao;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioData;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoPropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoSetor;
import br.gov.df.emater.aterwebsrv.modelo.dominio.CadastroAcao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaSituacao;
import br.gov.df.emater.aterwebsrv.modelo.formulario.Coleta;
import br.gov.df.emater.aterwebsrv.modelo.formulario.FormularioVersao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Arquivo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Email;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Endereco;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Estado;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Municipio;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaArquivo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaEmail;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaEndereco;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaGrupoSocial;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaPendencia;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaRelacionamento;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaTelefone;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Relacionamento;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.RelacionamentoConfiguracaoVi;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.RelacionamentoFuncao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Telefone;

@Service("PessoaSalvarCmd")
public class SalvarCmd extends _Comando {

	@Autowired
	private ArquivoDao arquivoDao;

	@Autowired
	private ColetaDao coletaDao;

	@Autowired
	private PessoaDao dao;

	@Autowired
	private EmailDao emailDao;

	@Autowired
	private EnderecoDao enderecoDao;

	@Autowired
	private EstadoDao estadoDao;

	@Autowired
	private MunicipioDao municipioDao;

	@Autowired
	private PaisDao paisDao;

	@Autowired
	private PessoaArquivoDao pessoaArquivoDao;

	@Autowired
	private PessoaEmailDao pessoaEmailDao;

	@Autowired
	private PessoaEnderecoDao pessoaEnderecoDao;

	@Autowired
	private PessoaGrupoSocialDao pessoaGrupoSocialDao;

	@Autowired
	private PessoaPendenciaDao pessoaPendenciaDao;

	@Autowired
	private PessoaRelacionamentoDao pessoaRelacionamentoDao;

	@Autowired
	private PessoaTelefoneDao pessoaTelefoneDao;

	@Autowired
	private PublicoAlvoDao publicoAlvoDao;

	@Autowired
	private PublicoAlvoPropriedadeRuralDao publicoAlvoPropriedadeRuralDao;

	@Autowired
	private PublicoAlvoSetorDao publicoAlvoSetorDao;

	@Autowired
	private RelacionamentoConfiguracaoViDao relacionamentoConfiguracaoViDao;

	@Autowired
	private RelacionamentoDao relacionamentoDao;

	@Autowired
	private TelefoneDao telefoneDao;

	public SalvarCmd() {
	}

	@SuppressWarnings("unchecked")
	private Coleta criarColeta(LinkedHashMap<Object, Object> r) throws Exception {
		Coleta result = null;
		try {
			result = new Coleta();
			result.setId((Integer) r.get("id"));
			result.setDataColeta((Calendar) UtilitarioData.getInstance().stringParaData(r.get("dataColeta")));
			result.setFinalizada(r.get("finalizada") != null ? Confirmacao.valueOf((String) r.get("finalizada")) : null);
			result.setFormularioVersao(new FormularioVersao(((Integer) ((LinkedHashMap<Object, Object>) r.get("formularioVersao")).get("id"))));

			if (r.get("valor") != null) {
				ObjectMapper om = new ObjectMapper();
				String json = om.writeValueAsString(r.get("valor"));
				result.setValorString(json);
			}
		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Pessoa result = (Pessoa) contexto.getRequisicao();
		
		// captar o registro de atualização da tabela
		logAtualizar(result, contexto);

		// ajustar os dados de nascimento da pessoa
		if (result instanceof PessoaFisica) {
			PessoaFisica pf = (PessoaFisica) result;

			pf.setCpf(UtilitarioString.formataCpf(pf.getCpf()));

			if (pf.getNascimentoPais() != null && pf.getNascimentoPais().getId() != null) {
				pf.setNascimentoPais(paisDao.findOne(pf.getNascimentoPais().getId()));
			} else {
				pf.setNascimentoPais(null);
			}
			if (pf.getNascimentoPais() != null && pf.getNascimentoEstado() != null && pf.getNascimentoEstado().getId() != null) {
				Estado estado = estadoDao.findOne(pf.getNascimentoEstado().getId());
				if (estado.getPais().getId() != pf.getNascimentoPais().getId()) {
					pf.setNascimentoEstado(null);
				} else {
					pf.setNascimentoEstado(estado);
				}
			} else {
				pf.setNascimentoEstado(null);
			}
			if (pf.getNascimentoEstado() != null && pf.getNascimentoMunicipio() != null && pf.getNascimentoMunicipio().getId() != null) {
				Municipio municipio = municipioDao.findOne(pf.getNascimentoMunicipio().getId());
				if (municipio.getEstado().getId() != pf.getNascimentoEstado().getId()) {
					pf.setNascimentoMunicipio(null);
				} else {
					pf.setNascimentoMunicipio(municipio);
				}
			} else {
				pf.setNascimentoMunicipio(null);
			}
		} else {
			PessoaJuridica pj = (PessoaJuridica) result;
			pj.setCnpj(UtilitarioString.formataCnpj(pj.getCnpj()));
		}

		// gravar o perfil
		if (result.getPerfilArquivo() != null) {
			result.setPerfilArquivo(arquivoDao.findByMd5(result.getPerfilArquivo().getMd5()));
		}
		if (result.getSituacao() == null) {
			result.setSituacao(PessoaSituacao.A);
		}

		// guardar os dados do publico alvo
		PublicoAlvo publicoAlvo = result.getPublicoAlvo();
		result.setPublicoAlvo(null);

		// salvar o registro da pessoa
		dao.save(result);

		// publico alvo
		if (Confirmacao.S.equals(result.getPublicoAlvoConfirmacao())) {
			if (publicoAlvo == null) {
				throw new BoException("Informações sobre o público alvo não informadas");
			}
			publicoAlvo.setPessoa(result);
			if (publicoAlvo.getId() == null) {
				PublicoAlvo salvo = publicoAlvoDao.findOneByPessoa(publicoAlvo.getPessoa());
				if (salvo != null) {
					publicoAlvo.setId(salvo.getId());
				}
			}
			// ajustar o setor
			if (publicoAlvo.getPublicoAlvoSetorList() != null) {
				for (PublicoAlvoSetor publicoAlvoSetor : publicoAlvo.getPublicoAlvoSetorList()) {
					publicoAlvoSetor.setPublicoAlvo(publicoAlvo);
					if (publicoAlvo.getId() != null) {
						PublicoAlvoSetor salvo = publicoAlvoSetorDao.findOneByPublicoAlvoAndSetor(publicoAlvo, publicoAlvoSetor.getSetor());
						if (salvo != null) {
							publicoAlvoSetor.setId(salvo.getId());
						}
					}
				}
			}
			if (publicoAlvo.getDapNumero() != null) {
				publicoAlvo.setDapNumero(publicoAlvo.getDapNumero().toUpperCase());
			}
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
						endereco.setId(null);
					}
					if (endereco.getPropriedadeRuralConfirmacao() == null) {
						endereco.setPropriedadeRuralConfirmacao(Confirmacao.N);
					}
					endereco.setCep(UtilitarioString.formataCep(endereco.getCep()));
					endereco = enderecoDao.save(endereco);

					PessoaEndereco salvo = pessoaEnderecoDao.findOneByPessoaAndEndereco(result, endereco);
					if (salvo != null) {
						pessoaEndereco.setId(salvo.getId());
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
					if (pessoaTelefone.getTelefone() == null || pessoaTelefone.getTelefone().getNumero() == null || pessoaTelefone.getTelefone().getNumero().trim().length() == 0) {
						throw new BoException("Telefone não informado");
					}
					Telefone telefone = telefoneDao.findByNumero(pessoaTelefone.getTelefone().getNumero());
					if (telefone == null) {
						telefone = pessoaTelefone.getTelefone();
						telefone.setId(null);
						telefone = telefoneDao.save(telefone);
					}
					PessoaTelefone salvo = pessoaTelefoneDao.findOneByPessoaAndTelefone(result, telefone);
					if (salvo != null) {
						pessoaTelefone.setId(salvo.getId());
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
					if (pessoaEmail.getEmail() == null || pessoaEmail.getEmail().getEndereco() == null || pessoaEmail.getEmail().getEndereco().trim().length() == 0) {
						throw new BoException("E-mail não informado");
					}
					Email email = emailDao.findByEndereco(pessoaEmail.getEmail().getEndereco());
					if (email == null) {
						email = pessoaEmail.getEmail();
						email.setId(null);
						email.setEndereco(email.getEndereco().trim().toLowerCase());
						email = emailDao.save(email);
					}
					PessoaEmail salvo = pessoaEmailDao.findOneByPessoaAndEmail(result, email);
					if (salvo != null) {
						pessoaEmail.setId(salvo.getId());
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
						if (relacionamento == null) {
							relacionamento = new Relacionamento();
						}
						if (relacionamento.getRelacionamentoTipo() == null) {
							relacionamento.setRelacionamentoTipo(pessoaRelacionamento.getRelacionamento().getRelacionamentoTipo());
						}
					} else {
						Relacionamento salvo = relacionamentoDao.findById(relacionamento.getId());
						salvo.getPessoaRelacionamentoList().size();
						relacionamento.setPessoaRelacionamentoList(salvo.getPessoaRelacionamentoList());
					}
					relacionamento.setInicio(pessoaRelacionamento.getRelacionamento().getInicio());
					relacionamento.setTermino(pessoaRelacionamento.getRelacionamento().getTermino());
					relacionamento = relacionamentoDao.save(relacionamento);

					// salvar o relacionado
					pessoaRelacionamento.setRelacionamento(relacionamento);
					pessoaRelacionamentoDao.save(pessoaRelacionamento);

					RelacionamentoConfiguracaoVi relacionamentoConfiguracaoVi = relacionamentoConfiguracaoViDao.findByTipoIdAndRelacionadorId(pessoaRelacionamento.getRelacionamento().getRelacionamentoTipo().getId(), pessoaRelacionamento.getRelacionamentoFuncao().getId());

					// salvar o relacionador
					PessoaRelacionamento relacionador = null;
					boolean encontrou = false;
					if (relacionamento.getPessoaRelacionamentoList() != null) {
						for (PessoaRelacionamento rel : relacionamento.getPessoaRelacionamentoList()) {
							if (rel.getPessoa() != null && rel.getPessoa().getId().equals(result.getId())) {
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
				if (CadastroAcao.E.equals(pessoaArquivo.getCadastroAcao()) && pessoaArquivo.getId() != null) {
					pessoaArquivoDao.delete(pessoaArquivo);
				}
			}
			// tratar a insersao de registros
			for (PessoaArquivo pessoaArquivo : result.getArquivoList()) {
				if (!CadastroAcao.E.equals(pessoaArquivo.getCadastroAcao())) {
					Arquivo arquivo = pessoaArquivo.getArquivo();
					arquivo = arquivoDao.findByMd5(arquivo.getMd5());
					if (arquivo != null) {
						arquivo.setNomeOriginal(pessoaArquivo.getArquivo().getNomeOriginal());
						arquivo.setDataUpload(pessoaArquivo.getArquivo().getDataUpload());
						arquivo.setExtensao(pessoaArquivo.getArquivo().getExtensao());
						arquivo.setTamanho(pessoaArquivo.getArquivo().getTamanho());
						arquivo.setTipo(pessoaArquivo.getArquivo().getTipo());
					}
					arquivoDao.save(arquivo);
					pessoaArquivo.setArquivo(arquivo);
					pessoaArquivo.setPessoa(result);
					pessoaArquivoDao.save(pessoaArquivo);
				}
			}
		}

		// salvar os formularios de diagnostico
		if (result.getDiagnosticoList() instanceof List && !(CollectionUtils.isEmpty((List<?>) result.getDiagnosticoList()))) {
			if (!(((List<?>) result.getDiagnosticoList()).get(0) instanceof LinkedHashMap)) {				
				for (List<Object> formulario : (List<List<Object>>) result.getDiagnosticoList()) {
					LinkedHashMap<Object, Object> f = (LinkedHashMap<Object, Object>) formulario.get(9);
					if (f != null) {
						List<LinkedHashMap<Object, Object>> c = (List<LinkedHashMap<Object, Object>>) f.get("coletaList");
						if (c != null) {
							for (LinkedHashMap<Object, Object> r : c) {
								Coleta coleta = criarColeta(r);
								logAtualizar(coleta, contexto);
								coleta.setPessoa(result);
								coleta.setPropriedadeRural(null);
								coletaDao.save(coleta);
							}
						}
					}
				}
			}
		}

		// salvar pendencias do cadastro
		if (result.getPendenciaList() != null) {
			// tratar a exclusao de registros
			for (PessoaPendencia pendencia : result.getPendenciaList()) {
				if (CadastroAcao.E.equals(pendencia.getCadastroAcao())) {
					pessoaPendenciaDao.delete(pendencia);
				}
			}
			// tratar a insersao de registros
			for (PessoaPendencia pendencia : result.getPendenciaList()) {
				if (!CadastroAcao.E.equals(pendencia.getCadastroAcao())) {
					pendencia.setPessoa(result);
					pessoaPendenciaDao.save(pendencia);
				}
			}
		}

		dao.flush();

		contexto.setResposta(result.getId());
		return false;
	}

}