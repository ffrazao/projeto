package br.gov.df.emater.aterwebsrv.dao.formulario;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Situacao;
import br.gov.df.emater.aterwebsrv.modelo.dto.FiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.dto.FormularioCadFiltroDto;

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
		sql.append("select f.id, f.nome, f.codigo, f.situacao, f.inicio, f.termino").append("\n");
		sql.append("from Formulario f").append("\n");
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

}