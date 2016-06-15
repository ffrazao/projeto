package br.gov.df.emater.aterwebsrv.bo.seguranca;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.funcional.EmpregoDao;
import br.gov.df.emater.aterwebsrv.dao.funcional.LotacaoDao;
import br.gov.df.emater.aterwebsrv.dao.sistema.UsuarioDao;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioData;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaTipo;
import br.gov.df.emater.aterwebsrv.modelo.funcional.Emprego;
import br.gov.df.emater.aterwebsrv.modelo.funcional.Lotacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Email;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaEmail;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

@Service("SegurancaVisualizarPerfilCmd")
public class VisualizarPerfilCmd extends _Comando {

	@Autowired
	private EmpregoDao empregoDao;

	@Autowired
	private LotacaoDao lotacaoDao;

	@Autowired
	private UsuarioDao usuarioDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		String username = (String) contexto.getRequisicao();

		if (username != null) {
			contexto.setResposta(fetchUnico(username));
		} else {
			contexto.setResposta(fetchTodos());
		}

		return false;
	}

	private Usuario fetch(Usuario usuario) {
		if (usuario.getPessoaEmail() != null) {
			PessoaEmail pessoaEmail = new PessoaEmail();
			pessoaEmail.setId(usuario.getPessoaEmail().getId());

			Email email = new Email();
			email.setId(usuario.getPessoaEmail().getEmail().getId());
			email.setEndereco(usuario.getPessoaEmail().getEmail().getEndereco());

			pessoaEmail.setEmail(email);

			usuario.setPessoaEmail(pessoaEmail);
		}

		List<PessoaEmail> emailList = usuario.getPessoa().getEmailList();
		if (emailList != null) {
			for (PessoaEmail email : emailList) {
				email.setPessoa(null);
			}
		}
		usuario.setPessoa(usuario.getPessoa() == null ? null : usuario.getPessoa().infoBasica());
		usuario.getPessoa().setEmailList(emailList);

		usuario.setPassword(null);

		usuario.setInclusaoUsuario(usuario.getInclusaoUsuario() == null ? null : usuario.getInclusaoUsuario().infoBasica());
		usuario.setAlteracaoUsuario(usuario.getAlteracaoUsuario() == null ? null : usuario.getAlteracaoUsuario().infoBasica());

		return usuario;
	}

	private List<Map<String, Object>> fetchTodos() throws BoException {
		List<Usuario> usuarioList = usuarioDao.findByPessoaPessoaTipoAndUsuarioAtualizouPerfilOrderByPessoaNome(PessoaTipo.PF, Confirmacao.S);
		List<Map<String, Object>> result = null;
		if (usuarioList != null && usuarioList.size() > 0) {
			for (Usuario usuario : usuarioList) {
				PessoaFisica pessoa = (PessoaFisica) usuario.getPessoa();
				Lotacao lotacao = null;

				// Encontrar a lotação atual da pessoa
				List<Lotacao> lotacaoList = new ArrayList<Lotacao>();
				for (Emprego emprego : empregoDao.findByPessoaRelacionamentoListPessoaIn(usuario.getPessoa())) {
					if ((emprego.getInicio() == null || emprego.getInicio().before(Calendar.getInstance())) && (emprego.getTermino() == null || emprego.getTermino().after(Calendar.getInstance()))) {
						// inicio avaliar perfis da lotacoes do usuario
						for (Lotacao l : lotacaoDao.findByEmprego(emprego)) {
							if ((l.getInicio() == null || l.getInicio().before(Calendar.getInstance())) && (l.getTermino() == null || l.getTermino().after(Calendar.getInstance()))) {
								lotacaoList.add(l);
							}
						}
					}
				}
				Collections.sort(lotacaoList, Collections.reverseOrder(new Comparator<Lotacao>() {
					@Override
					public int compare(Lotacao l1, Lotacao l2) {
						return l1.getInicio() == null ? -1 : l1.getInicio().compareTo(l2.getInicio());
					}
				}));
				for (Lotacao l : lotacaoList) {
					if (l.getInicio().before(Calendar.getInstance())) {
						lotacao = l;
						break;
					}
				}
				Map<String, Object> linha = new HashMap<String, Object>();

				linha.put("nome", pessoa.getNome());
				linha.put("apelidoSigla", pessoa.getApelidoSigla());
				if (pessoa.getPerfilArquivo() != null) {
					linha.put("imagem", pessoa.getPerfilArquivo().getMd5());
				}
				StringBuilder temp = new StringBuilder();
				if (pessoa.getNascimentoMunicipio() != null) {
					temp.append(pessoa.getNascimentoMunicipio().getNome());
				}
				if (pessoa.getNascimentoMunicipio() != null && pessoa.getNascimentoMunicipio().getEstado() != null) {
					if (temp.toString().trim().length() > 0) {
						temp.append(", ");
					}
					temp.append(pessoa.getNascimentoMunicipio().getEstado().getSigla());
				}
				if (pessoa.getNascimentoPais() != null && temp.toString().length() == 0) {
					temp.append(pessoa.getNascimentoPais().getNome());
				}
				if (temp.toString().length() > 0) {
					linha.put("nascimentoLocal", temp.toString());
				}
				if (pessoa.getNascimento() != null) {
					linha.put("nascimentoData", UtilitarioData.getInstance().formataData(pessoa.getNascimento()));
				}
				if (lotacao != null) {
					linha.put("cargo", lotacao.getEmprego().getCargo().getNome());
					if (lotacao.getEmprego().getInicio() != null) {
						linha.put("admissao", UtilitarioData.getInstance().formataData(lotacao.getEmprego().getInicio()));
					}
					linha.put("lotacao", lotacao.getUnidadeOrganizacional().getNome());
				}
				linha.put("informacaoSobreUsuario", usuario.getInfoSobreUsuario());

				if (result == null) {
					result = new ArrayList<Map<String, Object>>();
				}
				result.add(linha);
			}
		}
		return result;
	}

	private Usuario fetchUnico(String username) throws BoException {
		Usuario result = usuarioDao.findByUsername(username);

		if (result == null) {
			throw new BoException("Usuário não cadastrado");
		}

		result = fetch(result);

		return result;
	}

}