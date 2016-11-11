package br.gov.df.emater.aterwebsrv.bo.agenda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.atividade.AtividadeAssuntoDao;
import br.gov.df.emater.aterwebsrv.dao.atividade.AtividadeDao;
import br.gov.df.emater.aterwebsrv.dao.atividade.AtividadePessoaDao;
import br.gov.df.emater.aterwebsrv.dao.atividade.OcorrenciaDao;
import br.gov.df.emater.aterwebsrv.dto.atividade.AtividadeCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.atividade.AtividadeAssunto;
import br.gov.df.emater.aterwebsrv.modelo.atividade.AtividadePessoa;
import br.gov.df.emater.aterwebsrv.modelo.atividade.Ocorrencia;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadePessoaParticipacao;

@Service("AgendaFiltrarCmd")
public class FiltrarCmd extends _Comando {

	@Autowired
	private AtividadeAssuntoDao atividadeAssuntoDao;

	@Autowired
	private AtividadeDao atividadeDao;

	@Autowired
	private AtividadePessoaDao atividadePessoaDao;

	@Autowired
	private OcorrenciaDao ocorrenciaDao;

	@SuppressWarnings("unchecked")
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		AtividadeCadFiltroDto filtro = (AtividadeCadFiltroDto) contexto.getRequisicao();
		List<Object> result = null;
		result = (List<Object>) atividadeDao.filtrar(filtro);
		result = fetch(result);
		contexto.setResposta(result);
		return false;
	}

	private List<Object> fetch(List<Object> atividadeList) {
		if (atividadeList == null) {
			return null;
		}
		List<Object> result = null;
		for (Object reg : atividadeList) {
			List<Object> linha = new ArrayList<Object>();
			linha.addAll(Arrays.asList((Object[]) reg));
			linha.add(fetchAssunto((Integer) ((Object[]) reg)[0]));
			linha.add(fetchPessoa((Integer) ((Object[]) reg)[0], AtividadePessoaParticipacao.D));
			linha.add(fetchPessoa((Integer) ((Object[]) reg)[0], AtividadePessoaParticipacao.E));
			linha.add(fetchOcorrencia((Integer) ((Object[]) reg)[0]));
			if (result == null) {
				result = new ArrayList<Object>();
			}
			result.add(linha);
		}
		return result;
	}

	private List<Object> fetchAssunto(Integer atividadeId) {
		List<AtividadeAssunto> lista = atividadeAssuntoDao.findTop10ByAtividadeIdOrderByAssuntoNomeAsc(atividadeId);
		if (lista == null) {
			return null;
		}
		List<Object> result = null;
		for (AtividadeAssunto reg : lista) {
			List<Object> linha = new ArrayList<Object>();
			linha.add(reg.getId()); // ATIV_ASSUNTO_ID
			linha.add(reg.getAssunto().getId()); // ATIV_ASSUNTO_ASSUNTO_ID
			linha.add(reg.getAssunto().getNome()); // ATIV_ASSUNTO_ASSUNTO_NOME
			linha.add(reg.getObservacao()); // ATIV_ASSUNTO_OBSERVACAO
			if (result == null) {
				result = new ArrayList<Object>();
			}
			result.add(linha);
		}
		return result;
	}

	private List<Object> fetchOcorrencia(Integer atividadeId) {
		List<Ocorrencia> lista = ocorrenciaDao.findTop10ByAtividadeIdOrderByRegistroDesc(atividadeId);
		if (lista == null) {
			return null;
		}
		List<Object> result = null;
		for (Ocorrencia reg : lista) {
			List<Object> linha = new ArrayList<Object>();
			linha.add(reg.getId()); // OCORR_ID
			linha.add(reg.getUsuario().getId()); // OCORR_USUARIO_ID
			linha.add(reg.getUsuario().getPessoa().getNome()); // OCORR_USUARIO_PESSOA_NOME
			linha.add(reg.getRegistro()); // OCORR_REGISTRO
			linha.add(reg.getRelato()); // OCORR_RELATO
			linha.add(reg.getAutomatico()); // OCORR_AUTOMATICO
			linha.add(reg.getIncidente()); // OCORR_INCIDENTE
			if (result == null) {
				result = new ArrayList<Object>();
			}
			result.add(linha);
		}
		return result;
	}

	private List<Object> fetchPessoa(Integer atividadeId, AtividadePessoaParticipacao participacao) {
		List<AtividadePessoa> lista = atividadePessoaDao.findTop10ByAtividadeIdAndParticipacaoOrderByPessoaNomeDesc(atividadeId, participacao);
		if (lista == null) {
			return null;
		}
		List<Object> result = null;
		for (AtividadePessoa reg : lista) {
			List<Object> linha = new ArrayList<Object>();
			linha.add(reg.getId()); // ATIV_PESS_ID
			linha.add(reg.getPessoa() == null ? null : reg.getPessoa().getId()); // ATIV_PESS_PESSOA_ID
			linha.add(reg.getPessoa() == null ? null : reg.getPessoa().getNome()); // ATIV_PESS_PESSOA_NOME
			linha.add(reg.getUnidadeOrganizacional() == null ? null : reg.getUnidadeOrganizacional().getId()); // ATIV_UNIDADE_ORGANIZACIONAL_ID
			linha.add(reg.getUnidadeOrganizacional() == null ? null : reg.getUnidadeOrganizacional().getNome()); // ATIV_UNIDADE_ORGANIZACIONAL_NOME
			linha.add(reg.getResponsavel()); // ATIV_PESS_RESPONSAVEL
			linha.add(reg.getInicio()); // ATIV_PESS_INICIO
			linha.add(reg.getAtivo()); // ATIV_PESS_ATIVO
			linha.add(reg.getTermino()); // ATIV_PESS_TERMINO
			linha.add(reg.getDuracao()); // ATIV_PESS_DURACAO
			if (result == null) {
				result = new ArrayList<Object>();
			}
			result.add(linha);
		}
		return result;
	}

}
