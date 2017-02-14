package br.gov.df.emater.aterwebsrv.bo.atividade;

import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaList;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.atividade.AtividadeDao;
import br.gov.df.emater.aterwebsrv.modelo.atividade.Atividade;
import br.gov.df.emater.aterwebsrv.modelo.atividade.AtividadeAssunto;
import br.gov.df.emater.aterwebsrv.modelo.atividade.AtividadeMetaTatica;
import br.gov.df.emater.aterwebsrv.modelo.atividade.AtividadePessoa;
import br.gov.df.emater.aterwebsrv.modelo.atividade.Ocorrencia;
import br.gov.df.emater.aterwebsrv.modelo_planejamento.planejamento.MetaTatica;

@Service("AtividadeVisualizarCmd")
public class VisualizarCmd extends _Comando {

	@Autowired
	private AtividadeDao dao;

	@Autowired
	private EntityManager em;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Integer id = (Integer) contexto.getRequisicao();
		Atividade result = dao.findOne(id);

		if (result == null) {
			throw new BoException("Registro não localizado");
		}

		// limpar dados
		result.setAlteracaoUsuario(result.getAlteracaoUsuario().infoBasica());
		result.setInclusaoUsuario(result.getInclusaoUsuario().infoBasica());

		if (result.getAssuntoList() != null) {
			List<AtividadeAssunto> l = new ArrayList<AtividadeAssunto>();
			for (AtividadeAssunto r : result.getAssuntoList()) {
				AtividadeAssunto n = new AtividadeAssunto();
				n.setId(r.getId());
				n.setAssunto(r.getAssunto().infoBasica());
				n.setObservacao(r.getObservacao());
				l.add(n);
			}
			result.setAssuntoList(l);
		}

		if (result.getMetaTaticaList() != null) {
			List<AtividadeMetaTatica> l = new ArrayList<AtividadeMetaTatica>();
			for (AtividadeMetaTatica r : result.getMetaTaticaList()) {
				AtividadeMetaTatica n = new AtividadeMetaTatica();
				n.setId(r.getId());
				n.setMetaTaticaId(r.getMetaTaticaId());
				n.setMetaTaticaNome(r.getMetaTaticaNome());
				n.setMetaTatica( new MetaTatica(r.getMetaTaticaId(),r.getMetaTaticaNome(), null ) );
				l.add(n);
			}
			result.setMetaTaticaList(l);
		}

		
		if (result.getPessoaDemandanteList() != null) {
			List<AtividadePessoa> l = new ArrayList<AtividadePessoa>();
			for (AtividadePessoa r : result.getPessoaDemandanteList()) {
				AtividadePessoa n = new AtividadePessoa();
				n.setId(r.getId());
				n.setPessoa(r.getPessoa().infoBasica());
				n.setResponsavel(r.getResponsavel());
				n.setInicio(r.getInicio());
				n.setAtivo(r.getAtivo());
				n.setTermino(r.getTermino());
				n.setDuracao(r.getDuracao());
				l.add(n);
			}
			result.setPessoaDemandanteList(l);
		}

		if (result.getPessoaExecutorList() != null) {
			List<AtividadePessoa> l = new ArrayList<AtividadePessoa>();
			for (AtividadePessoa r : result.getPessoaExecutorList()) {
				AtividadePessoa n = new AtividadePessoa();
				n.setId(r.getId());
				n.setPessoa(r.getPessoa() == null ? null : r.getPessoa().infoBasica());
				n.setUnidadeOrganizacional(
						r.getUnidadeOrganizacional() == null ? null : r.getUnidadeOrganizacional().infoBasica());
				n.setResponsavel(r.getResponsavel());
				n.setInicio(r.getInicio());
				n.setAtivo(r.getAtivo());
				n.setTermino(r.getTermino());
				n.setDuracao(r.getDuracao());
				l.add(n);
			}
			result.setPessoaExecutorList(l);
		}

		if (result.getOcorrenciaList() != null) {
			List<Ocorrencia> l = new ArrayList<Ocorrencia>();
			for (Ocorrencia r : result.getOcorrenciaList()) {
				Ocorrencia n = new Ocorrencia();
				n.setId(r.getId());
				n.setUsuario(r.getUsuario().infoBasica());
				n.setRegistro(r.getRegistro());
				n.setRelato(r.getRelato());
				n.setIncidente(r.getIncidente());
				n.setAutomatico(r.getAutomatico());
				l.add(n);
			}
			result.setOcorrenciaList(l);
		}

		result.setChaveSisaterList(infoBasicaList(result.getChaveSisaterList()));

		em.detach(result);
		contexto.setResposta(result);

		return false;
	}
}