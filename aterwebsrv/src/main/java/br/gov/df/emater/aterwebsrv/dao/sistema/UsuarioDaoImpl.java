package br.gov.df.emater.aterwebsrv.dao.sistema;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.util.CollectionUtils;

import br.gov.df.emater.aterwebsrv.modelo.dto.TagDto;
import br.gov.df.emater.aterwebsrv.modelo.dto.UsuarioCadFiltroDto;

public class UsuarioDaoImpl implements UsuarioDaoCustom {

	@PersistenceContext
	private EntityManager em;

	public UsuarioDaoImpl() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> filtrar(UsuarioCadFiltroDto filtro) {
		// objetos de trabalho
		List<Object[]> result = null;
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql, sqlTemp;

		// construção do sql
		sql = new StringBuilder();

		sql.append("select          a.id").append("\n");
		sql.append("               ,ifnull(b.nome, c.nome) as nome").append("\n");
		sql.append("               ,if(c.nome is null, b.pessoa_tipo, 'UO') as tipo").append("\n");
		sql.append("               ,d.md5").append("\n");
		sql.append("               ,b.situacao as pessoa_situacao").append("\n");
		sql.append("               ,a.situacao as usuario_situacao").append("\n");
		sql.append("               ,a.nome_usuario").append("\n");
		sql.append("from            sistema.usuario a").append("\n");
		sql.append("left join       pessoa.pessoa b").append("\n");
		sql.append("on              b.id = a.pessoa_id").append("\n");
		sql.append("left join       funcional.unidade_organizacional c").append("\n");
		sql.append("on              c.id = a.unidade_organizacional_id").append("\n");

		sql.append("left join       pessoa.arquivo d").append("\n");
		sql.append("on              d.id = b.perfil_arquivo_id").append("\n");

		sql.append("where (1 = 1)").append("\n");
		if (!CollectionUtils.isEmpty(filtro.getPessoaNome())) {
			sql.append("and (").append("\n");
			sqlTemp = new StringBuilder();
			for (TagDto nome : filtro.getPessoaNome()) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append(" or ");
				}
				String n = nome.getText().replaceAll("\\s", "%");
				params.add(String.format("%%%s%%", n));
				sqlTemp.append(" (ifnull(b.nome, c.nome) like ?").append(params.size()).append(")").append("\n");
			}
			sql.append(sqlTemp);
			sql.append(" )").append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getUsuarioNome())) {
			sql.append("and (").append("\n");
			sqlTemp = new StringBuilder();
			for (TagDto nome : filtro.getUsuarioNome()) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append(" or ");
				}
				String n = nome.getText().replaceAll("\\s", "%");
				params.add(String.format("%%%s%%", n));
				sqlTemp.append(" (a.nome_usuario like ?").append(params.size()).append(")").append("\n");
			}
			sql.append(sqlTemp);
			sql.append(" )").append("\n");
		}
		sql.append("order by        2").append("\n");

		// criar a query
		Query query = em.createNativeQuery(sql.toString());

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