package br.gov.df.emater.aterwebsrv.dao.atividade;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.transaction.annotation.Transactional;

import br.gov.df.emater.aterwebsrv.modelo.dto.AtividadeCadFiltroDto;

public class AtividadeDaoImpl implements AtividadeDaoCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	@Transactional(readOnly = true)
	public List<Object[]> filtrar(AtividadeCadFiltroDto filtro) {
		// objetos de trabalho
		List<Object[]> result = null;
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql;

		// construção do sql
		sql = new StringBuilder();
		sql.append("select a.id").append("\n"); // ATIV_ID
		sql.append("     , a.codigo").append("\n"); // ATIV_CODIGO
		sql.append("     , a.formato").append("\n"); // ATIV_FORMATO
		sql.append("     , a.finalidade").append("\n"); // ATIV_FINALIDADE
		sql.append("     , a.natureza").append("\n"); // ATIV_NATUREZA
		sql.append("     , a.prioridade").append("\n"); // ATIV_PRIORIDADE
		sql.append("     , a.inicio").append("\n"); // ATIV_INICIO
		sql.append("     , a.previsaoConclusao").append("\n"); // ATIV_PREVISAO_CONCLUSAO
		sql.append("     , a.conclusao").append("\n"); // ATIV_CONCLUSAO
		sql.append("     , a.duracaoEstimada").append("\n"); // ATIV_DURACAO_ESTIMADA
		sql.append("     , a.duracaoReal").append("\n"); // ATIV_DURACAO_REAL
		sql.append("     , a.duracaoSuspensao").append("\n"); // ATIV_DURACAO_SUSPENSAO
		sql.append("     , a.metodo.id").append("\n"); // ATIV_METODO_ID
		sql.append("     , a.metodo.nome").append("\n"); // ATIV_METODO_NOME
		sql.append("     , a.publicoEstimado").append("\n"); // ATIV_PUBLICO_ESTIMADO
		sql.append("     , a.publicoReal").append("\n"); // ATIV_PUBLICO_REAL
		sql.append("     , a.situacao").append("\n"); // ATIV_SITUACAO
		sql.append("     , a.situacaoData").append("\n"); // ATIV_SITUACAO_DATA
		sql.append("     , a.percentualConclusao").append("\n"); // ATIV_PERCENTUAL_CONCLUSAO
		sql.append("     , a.detalhamento").append("\n"); // ATIV_DETALHAMENTO
		sql.append("     , a.inclusaoUsuario.id").append("\n"); // ATIV_INCLUSAO_USUARIO_ID
		sql.append("     , a.inclusaoUsuario.pessoa.nome").append("\n"); // ATIV_INCLUSAO_USUARIO_PESSOA_NOME
		sql.append("     , a.inclusaoData").append("\n"); // ATIV_INCLUSAO_DATA
		sql.append("     , a.alteracaoUsuario.id").append("\n"); // ATIV_ALTERACAO_USUARIO_ID
		sql.append("     , a.alteracaoUsuario.pessoa.nome").append("\n"); // ATIV_ALTERACAO_USUARIO_PESSOA_NOME
		sql.append("     , a.alteracaoData").append("\n"); // ATIV_ALTERACAO_DATA
		sql.append("from Atividade a").append("\n");
		sql.append("order by a.inicio").append("\n");
		sql.append("       , a.metodo.nome").append("\n");

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