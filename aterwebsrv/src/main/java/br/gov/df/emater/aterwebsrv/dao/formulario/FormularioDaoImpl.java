package br.gov.df.emater.aterwebsrv.dao.formulario;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import br.gov.df.emater.aterwebsrv.dao.DaoException;
import br.gov.df.emater.aterwebsrv.dto.CadFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.formulario.FormularioCadFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.formulario.FormularioColetaCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.FormularioDestino;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Situacao;
import br.gov.df.emater.aterwebsrv.modelo.formulario.FormularioVersao;

public class FormularioDaoImpl implements FormularioDaoCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Object[]> filtrar(FormularioCadFiltroDto filtro) {
		// objetos de trabalho
		List<Object[]> result = null;
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql, sqlTemp;

		// construção do sql
		sql = new StringBuilder();
		sql.append("select distinct").append("\n");
		sql.append("       f.id").append("\n");
		sql.append("     , f.nome").append("\n");
		sql.append("     , f.codigo").append("\n");
		sql.append("     , f.situacao").append("\n");
		sql.append("     , f.inicio").append("\n");
		sql.append("     , f.termino").append("\n");
		sql.append("     , f.destino").append("\n");
		sql.append("     , f.subformulario").append("\n");
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
			for (String nome : filtro.getNome().split(CadFiltroDto.SEPARADOR_CAMPO)) {
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
			for (String codigo : filtro.getCodigo().split(CadFiltroDto.SEPARADOR_CAMPO)) {
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

	@SuppressWarnings("unchecked")
	@Override
	public List<FormularioVersao> filtrarComColeta(FormularioColetaCadFiltroDto filtro) throws Exception {
		if (filtro.getDestino() == null) {
			throw new DaoException("Dados incompletos!");
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
		sql.append("select fv.*").append("\n");
		sql.append("from formulario.formulario_versao fv").append("\n");
		if (enviaColetas) {
			sql.append("left join formulario.coleta c on c.formulario_versao_id = fv.id").append("\n");

			if (filtro.getPessoa() != null && filtro.getPessoa().getId() != null) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append("and ");
				}
				params.add(filtro.getPessoa());
				sqlTemp.append("c.pessoa_id = ?").append(params.size()).append("\n");
			}
			if (filtro.getPropriedadeRural() != null && filtro.getPropriedadeRural().getId() != null) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append("and ");
				}
				params.add(filtro.getPropriedadeRural());
				sqlTemp.append("c.propriedade_rural_id = ?").append(params.size()).append("\n");
			}
			if (filtro.getInicio() != null) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append("and ");
				}
				params.add(filtro.getInicio());
				sqlTemp.append("c.data_coleta >= ?").append(params.size()).append("\n");
			}
			if (filtro.getTermino() != null) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append("and ");
				}
				params.add(filtro.getTermino());
				sqlTemp.append("c.data_coleta <= ?").append(params.size()).append("\n");
			}
			if (!CollectionUtils.isEmpty(filtro.getCompletada()) && (Confirmacao.values().length != (filtro.getCompletada().size()))) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append("and ");
				}
				params.add(filtro.getCompletada());
				sqlTemp.append("c.finalizado in ?").append(params.size()).append("\n");
			}
			if (sqlTemp.length() > 0) {
				sql.append("and ((c.id = null) or (").append(sqlTemp.toString()).append("))\n");
			}
		}
		sql.append("where fv.inicio < now()").append("\n");
		params.add(filtro.getFormularioId());
		sql.append("and fv.formulario_id = ?").append(params.size()).append("\n");
		sql.append("order by fv.versao desc").append("\n");
		if (enviaColetas) {
			sql.append("       , c.data_coleta desc").append("\n");
		}

		// criar a query
		Query query = em.createNativeQuery(sql.toString(), FormularioVersao.class);

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

}