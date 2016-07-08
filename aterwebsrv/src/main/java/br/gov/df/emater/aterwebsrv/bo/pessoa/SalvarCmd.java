package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.bo._SalvarCmd;
import br.gov.df.emater.aterwebsrv.dao.ater.ComunidadeDao;
import br.gov.df.emater.aterwebsrv.dao.ater.PropriedadeRuralDao;
import br.gov.df.emater.aterwebsrv.dao.ater.PublicoAlvoDao;
import br.gov.df.emater.aterwebsrv.dao.ater.PublicoAlvoPropriedadeRuralDao;
import br.gov.df.emater.aterwebsrv.dao.ater.PublicoAlvoSetorDao;
import br.gov.df.emater.aterwebsrv.dao.formulario.ColetaDao;
import br.gov.df.emater.aterwebsrv.dao.funcional.EmpregoDao;
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
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaSituacao;
import br.gov.df.emater.aterwebsrv.modelo.formulario.Coleta;
import br.gov.df.emater.aterwebsrv.modelo.formulario.FormularioVersao;
import br.gov.df.emater.aterwebsrv.modelo.funcional.Emprego;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Arquivo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Email;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Endereco;
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
public class SalvarCmd extends _SalvarCmd {

	@Autowired
	private ArquivoDao arquivoDao;

	@Autowired
	private ColetaDao coletaDao;

	@Autowired
	private ComunidadeDao comunidadeDao;

	@Autowired
	private PropriedadeRuralDao propriedadeRuralDao;

	@Autowired
	private PessoaDao dao;

	@Autowired
	private EntityManager em;

	@Autowired
	private EmailDao emailDao;

	@Autowired
	private EmpregoDao empregoDao;

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

		// limpar chaves primárias para futura inclusão do registro
		if (result.getPublicoAlvo() != null) {
			limparChavePrimaria(result.getPublicoAlvo().getPublicoAlvoPropriedadeRuralList());
		}
		limparChavePrimaria(result.getEnderecoList());
		limparChavePrimaria(result.getTelefoneList());
		limparChavePrimaria(result.getEmailList());
		limparChavePrimaria(result.getRelacionamentoList());
		limparChavePrimaria(result.getGrupoSocialList());
		limparChavePrimaria(result.getArquivoList());
		limparChavePrimaria(result.getPendenciaList());

		// ajustar os dados de nascimento da pessoa
		if (result instanceof PessoaFisica) {
			PessoaFisica pf = (PessoaFisica) result;
			pf.setCpf(UtilitarioString.formataCpf(pf.getCpf()));

			// ajustar dados do local de nascimento
			if (pf.getNascimentoMunicipio() != null && pf.getNascimentoMunicipio().getId() != null) {
				pf.setNascimentoMunicipio(municipioDao.findOne(pf.getNascimentoMunicipio().getId()));
				pf.setNascimentoEstado(pf.getNascimentoMunicipio().getEstado());
				pf.setNascimentoPais(pf.getNascimentoEstado().getPais());
			} else {
				pf.setNascimentoMunicipio(null);
				if (pf.getNascimentoEstado() != null && pf.getNascimentoEstado().getId() != null) {
					pf.setNascimentoEstado(estadoDao.findOne(pf.getNascimentoEstado().getId()));
					pf.setNascimentoPais(pf.getNascimentoEstado().getPais());
				} else {
					pf.setNascimentoEstado(null);
					if (pf.getNascimentoEstado() != null && pf.getNascimentoEstado().getId() != null) {
						pf.setNascimentoPais(paisDao.findOne(pf.getNascimentoPais().getId()));
					} else {
						pf.setNascimentoPais(null);
					}
				}
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
		if (result.getSituacaoData() == null) {
			result.setSituacaoData(Calendar.getInstance());
		}

		// guardar os dados do publico alvo
		PublicoAlvo publicoAlvo = result.getPublicoAlvo();
		result.setPublicoAlvo(null);

		// salvar o registro da pessoa
		dao.save(result);

		// publico alvo
		if (Confirmacao.S.equals(result.getPublicoAlvoConfirmacao())) {
			if (publicoAlvo == null) {
				throw new BoException("Informações sobre o público alvo não foram informadas!");
			}
			publicoAlvo.setPessoa(result);
			if (publicoAlvo.getId() == null) {
				// tentar recuperar ID pela pessoa
				PublicoAlvo salvo = publicoAlvoDao.findOneByPessoa(result);
				if (salvo != null) {
					publicoAlvo.setId(salvo.getId());
				}
			}
			// ajustar o setor
			if (!CollectionUtils.isEmpty(publicoAlvo.getPublicoAlvoSetorList())) {
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
			// salvar público alvo
			publicoAlvoDao.save(publicoAlvo);

			// excluir as propriedades vinculadas ao publico alvo
			excluirRegistros(result, "publicoAlvoPropriedadeRuralList", publicoAlvoPropriedadeRuralDao);

			// salvar as propriedades vinculadas ao publico alvo
			if (!CollectionUtils.isEmpty(publicoAlvo.getPublicoAlvoPropriedadeRuralList())) {
				// tratar a insersão de registros
				int tot = 0;
				boolean comunidade = false;
				for (PublicoAlvoPropriedadeRural publicoAlvoPropriedadeRural : publicoAlvo.getPublicoAlvoPropriedadeRuralList()) {
					tot++;
					publicoAlvoPropriedadeRural.setPublicoAlvo(publicoAlvo);
					if (publicoAlvoPropriedadeRural.getPropriedadeRural() == null) {
						if (publicoAlvoPropriedadeRural.getComunidade() == null) {
							throw new BoException("Não foi possível identificar a comunidade!");
						}
						publicoAlvoPropriedadeRural.setComunidade(comunidadeDao.findOne(publicoAlvoPropriedadeRural.getComunidade().getId()));
						comunidade = true;
					} else {
						publicoAlvoPropriedadeRural.setPropriedadeRural(propriedadeRuralDao.findOne(publicoAlvoPropriedadeRural.getPropriedadeRural().getId()));
						publicoAlvoPropriedadeRural.setComunidade(publicoAlvoPropriedadeRural.getPropriedadeRural().getComunidade());
					}
					publicoAlvoPropriedadeRuralDao.save(publicoAlvoPropriedadeRural);
				}
				if (comunidade && tot > 1) {
					throw new BoException("Só é possível um vínculo com uma comunidade. Cadastre a propriedade rural ao invés!");
				}
			}
		}

		// tratar a exclusao de registros
		excluirRegistros(result, "enderecoList", pessoaEnderecoDao);

		// salvar enderecos
		if (!CollectionUtils.isEmpty(result.getEnderecoList())) {
			// tratar a insersao de registros
			Integer ordem = 0;
			for (PessoaEndereco pessoaEndereco : result.getEnderecoList()) {
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
				pessoaEndereco.setPessoa(result);
				pessoaEndereco.setEndereco(endereco);
				pessoaEndereco.setOrdem(++ordem);
				pessoaEnderecoDao.save(pessoaEndereco);

			}
		}

		// tratar a exclusao de registros
		excluirRegistros(result, "telefoneList", pessoaTelefoneDao);

		// salvar telefones
		if (!CollectionUtils.isEmpty(result.getTelefoneList())) {
			Integer ordem = 0;
			for (PessoaTelefone pessoaTelefone : result.getTelefoneList()) {
				if (pessoaTelefone.getTelefone() == null || StringUtils.isBlank(pessoaTelefone.getTelefone().getNumero())) {
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
				pessoaTelefone.setPessoa(result);
				pessoaTelefone.setTelefone(telefone);
				pessoaTelefone.setOrdem(++ordem);
				pessoaTelefoneDao.save(pessoaTelefone);
			}
		}

		// tratar a exclusao de registros
		excluirRegistros(result, "emailList", pessoaEmailDao);

		// salvar emails
		if (!CollectionUtils.isEmpty(result.getEmailList())) {
			// tratar a insersao de registros
			Integer ordem = 0;
			for (PessoaEmail pessoaEmail : result.getEmailList()) {
				if (pessoaEmail.getEmail() == null || StringUtils.isBlank(pessoaEmail.getEmail().getEndereco())) {
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
				pessoaEmail.setPessoa(result);
				pessoaEmail.setEmail(email);
				pessoaEmail.setOrdem(++ordem);
				pessoaEmailDao.save(pessoaEmail);
			}
		}

		// tratar a exclusao de registros
		//excluirRegistros(result, "relacionamentoList", pessoaRelacionamentoDao);
		if (result.getExcluidoMap() != null && result.getExcluidoMap().get("relacionamentoList") != null) {
			result.getExcluidoMap().get("relacionamentoList").forEach((registro) -> {
				PessoaRelacionamento pr = pessoaRelacionamentoDao.findOne((Integer) registro);
				relacionamentoDao.delete(pr.getRelacionamento());
			});
		}

		// salvar relacionamentos
		if (!CollectionUtils.isEmpty(result.getRelacionamentoList())) {
			for (PessoaRelacionamento pessoaRelacionamento : result.getRelacionamentoList()) {
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
					List<PessoaRelacionamento> prList = salvo.getPessoaRelacionamentoList();
					prList.size();
					BeanUtils.copyProperties(salvo, relacionamento);
					relacionamento = salvo;
					relacionamento.setPessoaRelacionamentoList(prList);
					em.detach(salvo);
					salvo = null;
				}
				relacionamento.setInicio(pessoaRelacionamento.getRelacionamento().getInicio());
				relacionamento.setTermino(pessoaRelacionamento.getRelacionamento().getTermino());
				if (relacionamento instanceof Emprego) {
					relacionamento = empregoDao.save((Emprego) relacionamento);
				} else {
					relacionamento = relacionamentoDao.save(relacionamento);
				}

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

		// tratar a exclusao de registros
		excluirRegistros(result, "grupoSocialList", pessoaGrupoSocialDao);

		// salvar grupos sociais
		if (!CollectionUtils.isEmpty(result.getGrupoSocialList())) {
			// tratar a insersao de registros
			for (PessoaGrupoSocial pessoaGrupoSocial : result.getGrupoSocialList()) {
				pessoaGrupoSocial.setPessoa(result);
				pessoaGrupoSocialDao.save(pessoaGrupoSocial);
			}
		}

		// tratar a exclusao de registros
		excluirRegistros(result, "arquivoList", pessoaArquivoDao);

		// salvar arquivos vinculados
		if (!CollectionUtils.isEmpty(result.getArquivoList())) {
			// tratar a insersao de registros
			for (PessoaArquivo pessoaArquivo : result.getArquivoList()) {
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
				pessoaArquivo.setPessoa(result);
				pessoaArquivo.setArquivo(arquivo);
				pessoaArquivoDao.save(pessoaArquivo);
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

		// tratar a exclusao de registros
		excluirRegistros(result, "pendenciaList", pessoaPendenciaDao);

		// salvar pendencias do cadastro
		if (!CollectionUtils.isEmpty(result.getPendenciaList())) {
			// tratar a insersao de registros
			for (PessoaPendencia pendencia : result.getPendenciaList()) {
				pendencia.setPessoa(result);
				pessoaPendenciaDao.save(pendencia);
			}
		}

		dao.flush();

		contexto.setResposta(result.getId());
		return false;
	}

}