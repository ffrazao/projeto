package br.gov.df.emater.aterwebsrv.dao.formulario;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.FormularioDestino;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Situacao;
import br.gov.df.emater.aterwebsrv.modelo.dto.FiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.dto.FormularioCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.dto.FormularioColetaCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.formulario.Coleta;
import br.gov.df.emater.aterwebsrv.modelo.formulario.FormularioVersao;

public class FormularioDaoImpl implements FormularioDaoCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	@Transactional(readOnly = true)
	public List<Object[]> filtrar(FormularioCadFiltroDto filtro) {
		// objetos de trabalho
		List<Object[]> result = null;
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql, sqlTemp;

		// construção do sql
		sql = new StringBuilder();
		sql.append("select distinct f.id, f.nome, f.codigo, f.situacao, f.inicio, f.termino, f.destino, f.subformulario").append("\n");
		sql.append("from Formulario f").append("\n");
		if (filtro.getVersao() != null) {
			sql.append("join f.formularioVersaoList fvl").append("\n");
		}
		sql.append("where 1 = 1").append("\n");
		if (filtro.getVigente() != null && filtro.getVigente().contains(Confirmacao.S)) {
			sql.append("and f.situacao = 'A'").append("\n");
			sql.append("and now() >= f.inicio").append("\n");
			sql.append("and (f.termino is null or now() <= f.termino)").append("\n");
		} else {
			if (!CollectionUtils.isEmpty(filtro.getSituacao()) && (Situacao.values().length != (filtro.getSituacao().size()))) {
				params.add(filtro.getSituacao());
				sql.append("and f.situacao in ?").append(params.size()).append("\n");
			}
			if (filtro.getVigencia() != null) {
				params.add(filtro.getVigencia());
				sql.append("and f.inicio >= ?").append(params.size()).append("\n");
				params.add(filtro.getVigencia());
				sql.append("and (f.termino is null or f.termino <= ?").append(params.size()).append(")\n");
			}
		}
		if (!StringUtils.isEmpty(filtro.getNome())) {
			sql.append("and (").append("\n");
			sqlTemp = new StringBuilder();
			for (String nome : filtro.getNome().split(FiltroDto.SEPARADOR_CAMPO)) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append(" or ");
				}
				params.add(String.format("%%%s%%", nome.trim()));
				sqlTemp.append(" (f.nome like ?").append(params.size()).append(")").append("\n");
			}
			sql.append(sqlTemp);
			sql.append(" )").append("\n");
		}
		if (!StringUtils.isEmpty(filtro.getCodigo())) {
			sql.append("and (").append("\n");
			sqlTemp = new StringBuilder();
			for (String codigo : filtro.getCodigo().split(FiltroDto.SEPARADOR_CAMPO)) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append(" or ");
				}
				params.add(String.format("%%%s%%", codigo.trim()));
				sqlTemp.append(" (f.codigo like ?").append(params.size()).append(")").append("\n");
			}
			sql.append(sqlTemp);
			sql.append(" )").append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getDestino()) && (FormularioDestino.values().length != (filtro.getDestino().size()))) {
			params.add(filtro.getDestino());
			sql.append("and f.destino in ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getSubformulario()) && (Confirmacao.values().length != (filtro.getSubformulario().size()))) {
			params.add(filtro.getSubformulario());
			sql.append("and f.subformulario in ?").append(params.size()).append("\n");
		}
		if (filtro.getVersao() != null) {
			params.add(filtro.getVersao());
			sql.append("and fvl.versao = ?").append(params.size()).append("\n");
		}

		sql.append("order by f.nome, f.codigo").append("\n");

		// criar a query
		TypedQuery<Object[]> query = em.createQuery(sql.toString(), Object[].class);

		// inserir os parametros
		for (int i = 1; i <= params.size(); i++) {
			query.setParameter(i, params.get(i - 1));
		}

		// definir a pagina a ser consultada
		filtro.configuraPaginacao(query);

		// executar a consulta
		result = query.getResultList();

		// retornar
		return result;
	}

	@Override
	public List<FormularioVersao> filtrarComColeta(FormularioColetaCadFiltroDto filtro) {
		if (filtro.getDestino() == null) {
			throw new BoException("Dados incompletos!");
		}
		boolean enviaColetas = false;
		if ((filtro.getPessoa() != null && filtro.getPessoa().getId() != null) || (filtro.getPropriedadeRural() != null && filtro.getPropriedadeRural().getId() != null)) {
			enviaColetas = true;
		}

		// objetos de trabalho
		List<FormularioVersao> result = null;
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql, sqlTemp;

		// construção do sql
		sql = new StringBuilder();
		sqlTemp = new StringBuilder();
		sql.append("select fv").append("\n");
		sql.append("from FormularioVersao fv").append("\n");
		if (enviaColetas) {
			sql.append("left join fv.coletaList c").append("\n");
		}
		sql.append("where fv.inicio < now()").append("\n");
		params.add(filtro.getFormularioId());
		sql.append("and fv.formulario.id = ?").append(params.size()).append("\n");
		if (enviaColetas) {
			if (filtro.getPessoa() != null && filtro.getPessoa().getId() != null) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append("and ");
				}
				params.add(filtro.getPessoa());
				sqlTemp.append("c.pessoa = ?").append(params.size()).append("\n");
			}
			if (filtro.getPropriedadeRural() != null && filtro.getPropriedadeRural().getId() != null) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append("and ");
				}
				params.add(filtro.getPropriedadeRural());
				sqlTemp.append("c.propriedadeRural = ?").append(params.size()).append("\n");
			}
			if (filtro.getInicio() != null) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append("and ");
				}
				params.add(filtro.getInicio());
				sqlTemp.append("c.dataColeta >= ?").append(params.size()).append("\n");
			}
			if (filtro.getTermino() != null) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append("and ");
				}
				params.add(filtro.getTermino());
				sqlTemp.append("c.dataColeta <= ?").append(params.size()).append("\n");
			}
			if (!CollectionUtils.isEmpty(filtro.getCompletada()) && (Confirmacao.values().length != (filtro.getCompletada().size()))) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append("and ");
				}
				params.add(filtro.getCompletada());
				sqlTemp.append("c.finalizado in ?").append(params.size()).append("\n");
			}
		}
		if (sqlTemp.length() > 0) {
			sql.append("and ((c = null) or (").append(sqlTemp.toString()).append("))\n");
		}
		sql.append("order by fv.versao desc").append("\n");
		if (enviaColetas) {
			sql.append("       , c.dataColeta desc").append("\n");
		}

		// criar a query
		TypedQuery<FormularioVersao> query = em.createQuery(sql.toString(), FormularioVersao.class);

		// inserir os parametros
		for (int i = 1; i <= params.size(); i++) {
			query.setParameter(i, params.get(i - 1));
		}

		// definir a pagina a ser consultada
		filtro.configuraPaginacao(query);

		// executar a consulta
		result = query.getResultList();

		for (FormularioVersao fv : result) {
			fv.setFormulario(null);
			fv.setFormularioVersaoElementoList(null);
			if (!enviaColetas) {
				fv.setColetaList(null);
			} else {
				for (Coleta coleta : fv.getColetaList()) {
					coleta.setFormularioVersao(null);
					if (coleta.getUsuario() != null) {
						coleta.setUsuario(coleta.getUsuario().infoBasica());
					}
					if (coleta.getPessoa() != null) {
						coleta.setPessoa(coleta.getPessoa().infoBasica());
					}
					if (coleta.getPropriedadeRural() != null) {
						coleta.setPropriedadeRural(coleta.getPropriedadeRural().infoBasica());
					}
				}
			}
		}

		// retornar
		return result;
	}

}