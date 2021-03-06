package br.gov.df.emater.aterwebsrv.bo.atividade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.bo._SalvarCmd;
import br.gov.df.emater.aterwebsrv.dao.atividade.AtividadeAssuntoDao;
import br.gov.df.emater.aterwebsrv.dao.atividade.AtividadeCadeiaProdutivaDao;
import br.gov.df.emater.aterwebsrv.dao.atividade.AtividadeDao;
import br.gov.df.emater.aterwebsrv.dao.atividade.AtividadeMetaTaticaDao;
import br.gov.df.emater.aterwebsrv.dao.atividade.AtividadePessoaDao;
import br.gov.df.emater.aterwebsrv.dao.atividade.OcorrenciaDao;
import br.gov.df.emater.aterwebsrv.dao_planejamento.planejamento.MetaTaticaDao;
import br.gov.df.emater.aterwebsrv.ferramenta.Util;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.atividade.Atividade;
import br.gov.df.emater.aterwebsrv.modelo.atividade.AtividadePessoa;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadeFinalidade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadePessoaParticipacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo_planejamento.planejamento.MetaTatica;

@Service("AtividadeSalvarCmd")
public class SalvarCmd extends _SalvarCmd {

	private static final String CODIGO_ATIVIDADE_CARACTERES = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private static final String[] NOME_INDEVIDO = { "B0CET", "C4R4LH", "CU", "G4I", "G4Y", "GAI", "GAY", "M3RD4", "M3RDA", "P1NT0", "PAU", "V14D0", "V1AD0", "V1ADO", "VI4D0", "V4GIN", "V4G1N" };

	@Autowired
	private AtividadeAssuntoDao atividadeAssuntoDao;

	@Autowired
	private AtividadeCadeiaProdutivaDao atividadeCadeiaProdutivaDao;

	@Autowired
	private AtividadeMetaTaticaDao atividadeMetaTaticaDao;

	@Autowired
	private AtividadePessoaDao atividadePessoaDao;

	@Autowired
	private AtividadeDao dao;

	@Autowired
	private MetaTaticaDao metaTaticaDao;

	@Autowired
	private OcorrenciaDao ocorrenciaDao;

	public SalvarCmd() {
	}

	private void criticaAtividadePessoa(AtividadePessoa pessoa) throws BoException {

		if (pessoa.getInicio() == null) {
			pessoa.setInicio(Calendar.getInstance());
		}
		if (pessoa.getAtivo() == null) {
			pessoa.setAtivo(Confirmacao.S);
		}
		if (pessoa.getTermino() != null) {
			if (pessoa.getTermino().before(pessoa.getInicio())) {
				throw new BoException("Termino da participação antes do início");
			}
			pessoa.setAtivo(Confirmacao.N);
		}
		if (Confirmacao.N.equals(pessoa.getAtivo()) && pessoa.getTermino() == null) {
			pessoa.setTermino(Calendar.getInstance());
		}
	}

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Atividade result = (Atividade) contexto.getRequisicao();

		// captar o registro de atualização da tabela
		logAtualizar(result, contexto);

		limparChavePrimaria(result.getAssuntoList());
		limparChavePrimaria(result.getCadeiaProdutivaList());
		limparChavePrimaria(result.getMetaTaticaList());
		limparChavePrimaria(result.getPessoaDemandanteList());
		limparChavePrimaria(result.getPessoaExecutorList());
		limparChavePrimaria(result.getOcorrenciaList());

		List<AtividadePessoa> atividadePessoaDemandList = result.getPessoaDemandanteList();
		List<AtividadePessoa> atividadePessoaExecList = result.getPessoaExecutorList();

		if (result.getId() == null) {
			// gerar o código da atividade
			result.setCodigo(gerarCodigoAtividade());
		}

		if (StringUtils.isBlank(result.getCodigo()) || !Util.codigoAtividadeEhValido(result.getCodigo())) {
			throw new BoException("O código da atividade é inválido! [%s]", result.getCodigo());
		}

		// atualizar valores
		if (AtividadeFinalidade.O.equals(result.getFinalidade()) && result.getPessoaDemandanteList() != null) {
			result.setPublicoReal(result.getPessoaDemandanteList().size());
			if (result.getPublicoReal() > result.getPublicoEstimado()) {
				result.setPublicoEstimado(result.getPublicoReal());
			}
		} else {
			result.setPublicoReal(null);
			result.setPublicoEstimado(null);
		}

		result.getChaveSisaterList().forEach((chave) -> chave.setAtividade(result));

		// FIX-ME enquanto não for implementado o novo modelo de Atividades,
		// depois remover esta condição
		result.setPrevisaoConclusao(result.getInicio());
		result.setConclusao(result.getInicio());
		result.setPercentualConclusao(5);

		dao.save(result);

		// tratar a exclusão de registros
		excluirRegistros(result, "assuntoList", atividadeAssuntoDao);
		if (!CollectionUtils.isEmpty(result.getAssuntoList())) {
			result.getAssuntoList().forEach((assunto) -> {
				assunto.setAtividade(result);
				atividadeAssuntoDao.save(assunto);
			});
		}
		
		excluirRegistros(result, "cadeiaProdutivaList", atividadeCadeiaProdutivaDao);
		if (!CollectionUtils.isEmpty(result.getCadeiaProdutivaList())) {
			result.getCadeiaProdutivaList().forEach((cadeiaProdutiva) -> {
				cadeiaProdutiva.setAtividade(result);
				atividadeCadeiaProdutivaDao.save(cadeiaProdutiva);
			});
		}


		excluirRegistros(result, "metaTaticaList", atividadeMetaTaticaDao);
		if (!CollectionUtils.isEmpty(result.getMetaTaticaList())) {
			result.getMetaTaticaList().forEach((metaTatica) -> {
				metaTatica.setAtividade(result);
				//MetaTatica mt = metaTaticaDao.findOne(metaTatica.getMetaTaticaId());
				//metaTatica.setMetaTaticaNome(mt.getDescricao());
				atividadeMetaTaticaDao.save(metaTatica);
			});
		}


		List<Integer> demandanteList = salvarAtividadePessoaList(result, atividadePessoaDemandList, AtividadePessoaParticipacao.D);

		List<Integer> executorList = salvarAtividadePessoaList(result, atividadePessoaExecList, AtividadePessoaParticipacao.E);

		if (executorList.size() > 0 && CollectionUtils.containsAny(executorList, demandanteList)) {
			throw new BoException("Demandante também vinculado como executor à atividade");
		}

		excluirRegistros(result, "ocorrenciaList", ocorrenciaDao);
		if (result.getOcorrenciaList() != null) {
			result.getOcorrenciaList().forEach((ocorrencia) -> {
				ocorrencia.setAtividade(result);
				ocorrencia.setUsuario(getUsuario(ocorrencia.getUsuario().getUsername()));
				ocorrenciaDao.save(ocorrencia);
			});
		}

		dao.flush();

		contexto.setResposta(result.getId());
		return false;
	}

	private String extraiNumeroProtocolo(String numero) {
		String str = CODIGO_ATIVIDADE_CARACTERES;
		StringBuilder sb = new StringBuilder();
		for (char c : numero.toCharArray()) {
			int pos = str.indexOf(c);
			if (pos >= 0) {
				sb.append(UtilitarioString.zeroEsquerda(pos, 2));
			}
		}
		return sb.toString();
	}

	private String gerarCodigoAtividade() {
		StringBuilder result = null;
		do {
			do {
				result = new StringBuilder();
				Random random = new Random();
				int letra = 0;
				while (result.length() < 8) {
					int posicao = random.nextInt(letra > 3 ? 10 : CODIGO_ATIVIDADE_CARACTERES.length());
					if (posicao > 9) {
						letra++;
					} else {
						letra = 0;
					}
					// para evitar que códigos possuam mais de 3 letras
					// consecutivas
					if (letra > 3) {
						continue;
					}
					result.append(CODIGO_ATIVIDADE_CARACTERES.charAt(posicao));
				}
				// evitar nomes indevidos
			} while (nomeIndevido(result.toString()));
			result.append(modulo10(extraiNumeroProtocolo(result.toString())));
			result.insert(4, ".");
			result.insert(9, "-");
			// evitar códigos repetidos
		} while (dao.findOneByCodigo(result.toString()) != null);
		return result.toString();
	}

	private int modulo10(String numero) {
		// variáveis de instancia
		int soma = 0;
		int resto = 0;
		int dv = 0;
		String[] numeros = new String[numero.length() + 1];
		int multiplicador = 2;
		String aux;
		String aux2;
		String aux3;

		for (int i = numero.length(); i > 0; i--) {
			// Multiplica da direita pra esquerda, alternando os algarismos 2 e
			// 1
			if (multiplicador % 2 == 0) {
				// pega cada numero isoladamente
				numeros[i] = String.valueOf(Integer.valueOf(numero.substring(i - 1, i)) * 2);
				multiplicador = 1;
			} else {
				numeros[i] = String.valueOf(Integer.valueOf(numero.substring(i - 1, i)) * 1);
				multiplicador = 2;
			}
		}

		// Realiza a soma dos campos de acordo com a regra
		for (int i = (numeros.length - 1); i > 0; i--) {
			aux = String.valueOf(Integer.valueOf(numeros[i]));

			if (aux.length() > 1) {
				aux2 = aux.substring(0, aux.length() - 1);
				aux3 = aux.substring(aux.length() - 1, aux.length());
				numeros[i] = String.valueOf(Integer.valueOf(aux2) + Integer.valueOf(aux3));
			} else {
				numeros[i] = aux;
			}
		}

		// Realiza a soma de todos os elementos do array e calcula o digito
		// verificador
		// na base 10 de acordo com a regra.
		for (int i = numeros.length; i > 0; i--) {
			if (numeros[i - 1] != null) {
				soma += Integer.valueOf(numeros[i - 1]);
			}
		}
		resto = soma % 10;
		dv = 10 - resto;
		if (dv == 10) {
			dv = 0;
		}

		// retorna o digito verificador
		return dv;
	}

	private boolean nomeIndevido(String codigo) {
		if (codigo == null) {
			return false;
		}
		for (String nome : NOME_INDEVIDO) {
			if (codigo.toUpperCase().indexOf(nome) >= 0) {
				return true;
			}
		}
		return false;
	}

	private List<Integer> salvarAtividadePessoaList(Atividade atividade, List<AtividadePessoa> atividadePessoaList, AtividadePessoaParticipacao participacao) throws BoException {
		// tratar a exclusão de registros
		excluirRegistros(atividade, participacao.equals(AtividadePessoaParticipacao.D) ? "pessoaDemandanteList" : "pessoaExecutorList", atividadePessoaDao);
		Pessoa responsavel = null;
		List<Integer> result = new ArrayList<Integer>();
		for (AtividadePessoa ativPess : atividadePessoaList) {
			if (ativPess.getPessoa() != null) {
				if (result.contains(ativPess.getPessoa().getId())) {
					throw new BoException("O %s (%s) vinculado mais de uma vez à Atividade", participacao.toString(), ativPess.getPessoa().getNome());
				} else {
					result.add(ativPess.getPessoa().getId());
				}
			}
			ativPess.setAtividade(atividade);
			ativPess.setParticipacao(participacao);
			criticaAtividadePessoa(ativPess);
			if (responsavel == null && Confirmacao.S.equals(ativPess.getResponsavel())) {
				responsavel = ativPess.getPessoa();
			} else {
				ativPess.setResponsavel(Confirmacao.N);
			}
			atividadePessoaDao.save(ativPess);
		}
		if (responsavel == null) {
			if (atividadePessoaList.size() > 0) {
				atividadePessoaList.get(0).setResponsavel(Confirmacao.S);
			} else {
				throw new BoException("O %s responsável não foi identificado", participacao.toString());
			}
		}
		return result;
	}
}