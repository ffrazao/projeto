package br.gov.df.emater.aterwebsrv.bo.atividade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.atividade.AtividadeAssuntoDao;
import br.gov.df.emater.aterwebsrv.dao.atividade.AtividadeDao;
import br.gov.df.emater.aterwebsrv.dao.atividade.AtividadePessoaDao;
import br.gov.df.emater.aterwebsrv.dao.atividade.OcorrenciaDao;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.atividade.Atividade;
import br.gov.df.emater.aterwebsrv.modelo.atividade.AtividadeAssunto;
import br.gov.df.emater.aterwebsrv.modelo.atividade.AtividadeChaveSisater;
import br.gov.df.emater.aterwebsrv.modelo.atividade.AtividadePessoa;
import br.gov.df.emater.aterwebsrv.modelo.atividade.Ocorrencia;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadeFinalidade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadePessoaParticipacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;

@Service("AtividadeSalvarCmd")
public class SalvarCmd extends _Comando {

	private static final String CODIGO_ATIVIDADE_CARACTERES = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";;

	@Autowired
	private AtividadeAssuntoDao atividadeAssuntoDao;

	@Autowired
	private AtividadePessoaDao atividadePessoaDao;

	@Autowired
	private AtividadeDao dao;

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
		if (result.getId() == null) {
			result.setInclusaoUsuario(getUsuario(contexto.getUsuario().getName()));
			result.setCodigo(gerarCodigoAtividade());
		} else {
			String username = result.getInclusaoUsuario().getUsername();
			result.setInclusaoUsuario(null);
			result.setInclusaoUsuario(getUsuario(username));
		}
		result.setAlteracaoUsuario(getUsuario(contexto.getUsuario().getName()));

		if (result.getCodigo() == null) {
			throw new BoException("Não foi identificado o código da atividade!");
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
		
		for (AtividadeChaveSisater c: result.getChaveSisaterList()) {
			c.setAtividade(result);
		}

		dao.save(result);

		for (AtividadeAssunto assunto : result.getAssuntoList()) {
			assunto.setAtividade(result);
			atividadeAssuntoDao.save(assunto);
		}

		Pessoa responsavel = null;
		List<Integer> demandanteList = new ArrayList<Integer>();
		for (AtividadePessoa ativPess : result.getPessoaDemandanteList()) {
			if (demandanteList.contains(ativPess.getPessoa().getId())) {
				throw new BoException("Demandante vinculado mais de uma vez à Atividade");
			} else {
				demandanteList.add(ativPess.getPessoa().getId());
			}
			ativPess.setAtividade(result);
			ativPess.setParticipacao(AtividadePessoaParticipacao.D);
			criticaAtividadePessoa(ativPess);

			if (Confirmacao.S.equals(ativPess.getResponsavel())) {
				responsavel = ativPess.getPessoa();
			}
			atividadePessoaDao.save(ativPess);
		}
		if (responsavel == null) {
			// throw new BoException("Demandante Responsável não foi
			// identificado");
		}

		responsavel = null;
		List<Integer> executorList = new ArrayList<Integer>();
		for (AtividadePessoa ativPess : result.getPessoaExecutorList()) {
			if (ativPess.getPessoa() != null) {
				if (executorList.contains(ativPess.getPessoa().getId())) {
					throw new BoException("Executor vinculado mais de uma vez à Atividade");
				} else {
					executorList.add(ativPess.getPessoa().getId());
				}
			}
			ativPess.setAtividade(result);
			ativPess.setParticipacao(AtividadePessoaParticipacao.E);
			criticaAtividadePessoa(ativPess);

			if (Confirmacao.S.equals(ativPess.getResponsavel())) {
				responsavel = ativPess.getPessoa();
			}
			atividadePessoaDao.save(ativPess);
		}
		if (responsavel == null) {
			// throw new BoException("Executor Responsável não foi
			// identificado");
		}

		if (demandanteList.containsAll(executorList)) {
			throw new BoException("Demandante também vinculado como executor à atividade");
		}

		if (result.getOcorrenciaList() != null) {
			for (Ocorrencia ocorrencia : result.getOcorrenciaList()) {
				ocorrencia.setAtividade(result);
				ocorrenciaDao.save(ocorrencia);
			}
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
		String str = CODIGO_ATIVIDADE_CARACTERES;
		StringBuilder sb = new StringBuilder();
		Random r = new Random();
		for (int i = 0; i < 8; i++) {
			int j = r.nextInt(str.length());
			sb.append(str.charAt(j));
		}
		sb.append(modulo10(extraiNumeroProtocolo(sb.toString())));
		sb.insert(4, ".");
		sb.insert(9, "-");
		return sb.toString();
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
}