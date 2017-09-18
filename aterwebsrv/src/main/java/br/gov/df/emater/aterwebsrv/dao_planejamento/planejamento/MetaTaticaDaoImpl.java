package br.gov.df.emater.aterwebsrv.dao_planejamento.planejamento;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;

import br.gov.df.emater.aterwebsrv.dto_planejamento.PlanejamentoCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.atividade.AtividadeAssunto;
import br.gov.df.emater.aterwebsrv.modelo_planejamento.planejamento.MetaTatica;

public class MetaTaticaDaoImpl implements MetaTaticaDaoCustom {
	

	@Autowired
	@Qualifier("planejamentoEntityManager")
	private EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public List<MetaTatica> filtrar(PlanejamentoCadFiltroDto filtro) {
		// objetos de trabalho
		List<MetaTatica> result = null;
		StringBuilder sql;

		// construção do sql
		sql = new StringBuilder();
		
/*		
		sql.append("select distinct planejamento.matriz_planejamento.id, ").append("\n");
		sql.append("     concat( planejamento.matriz_planejamento.codigo, ' - ',  planejamento.matriz_planejamento.descricao ) as descricao, ").append("\n");
		sql.append("     planejamento.gerencia_tatica.nome, ").append("\n");

		// Metodo
		sql.append("     exists( select planejamento.metodo.id ").append("\n");
		sql.append("               from planejamento.plano_acao_metodo ").append("\n");
		sql.append("                    inner join planejamento.metodo on planejamento.metodo.id = planejamento.plano_acao_metodo.metodo_id").append("\n");
		sql.append(" 		      where planejamento.plano_acao_metodo.plano_acao_id = planejamento.plano_acao.id ").append("\n");
		sql.append("                and planejamento.metodo.ematerweb in ( ").append( filtro.getMetodo().getId() ).append(" ) ) as metodo, ").append("\n");

		// Assuntos
		sql.append("      exists( select planejamento.assunto.id ").append("\n");
		sql.append("                from planejamento.plano_acao_assunto ").append("\n");
		sql.append("     	                    inner join planejamento.assunto on planejamento.assunto.id = planejamento.plano_acao_assunto.assunto_id").append("\n");
		sql.append("     			  where planejamento.plano_acao_assunto.plano_acao_id = planejamento.plano_acao.id  ").append("\n"); 
		if (!CollectionUtils.isEmpty(filtro.getAtividadeList())) {
			sql.append("                and planejamento.assunto.ematerweb in ( ");
			for ( AtividadeAssunto atividadeAssunto : filtro.getAtividadeList() ){
				//params.add(atividadeAssunto);
				sql.append(" ").append(atividadeAssunto.getAssunto().getId()).append(",");
			}
			sql.setLength(sql.length() - 1);
			sql.append(") ");
		}
		sql.append(" ) as assunto ").append("\n");

		sql.append("  from planejamento.meta_estrategica ").append("\n");
		sql.append("    inner join planejamento.matriz_planejamento on planejamento.matriz_planejamento.meta_estrategica_id = planejamento.meta_estrategica.id ").append("\n");
		sql.append("    inner join planejamento.gerencia_tatica on planejamento.gerencia_tatica.id = planejamento.matriz_planejamento.gerencia_tatica_id ").append("\n");
		//params.add(filtro.getUnidadeOrganizacional());
		sql.append("    inner join planejamento.plano_acao on planejamento.plano_acao.matriz_planejamento_id = planejamento.matriz_planejamento.id ").append("\n");
		sql.append("    inner join pessoa.tbr_unidade on pessoa.tbr_unidade.int_id = planejamento.matriz_planejamento.unidade_organizacional_id ").append("\n");

		//params.add(filtro.getAno());
		sql.append("where planejamento.meta_estrategica.ano = ").append(filtro.getAno()).append(" ").append("\n");
		//params.add(filtro.getUnidadeOrganizacional());
		sql.append("  and pessoa.tbr_unidade.ematerweb in ( 8, ").append(filtro.getUnidadeOrganizacional().getId()).append(" ) ").append("\n");
		sql.append(" order by metodo+assunto desc, metodo desc, assunto desc, planejamento.matriz_planejamento.codigo ").append("\n");

*/		
		
		sql.append(" select distinct planejamento.matriz_planejamento.id, ").append("\n");
		sql.append(" concat( planejamento.matriz_planejamento.codigo, ' - ',  planejamento.matriz_planejamento.descricao ) as descricao, ").append("\n");
		sql.append(" 	     planejamento.gerencia_tatica.nome as gerenciaTatica ").append("\n");
		sql.append(" from planejamento.meta_estrategica ").append("\n");
		sql.append("      inner join planejamento.matriz_planejamento on planejamento.matriz_planejamento.meta_estrategica_id = planejamento.meta_estrategica.id ").append("\n"); 
        sql.append("      inner join planejamento.gerencia_tatica on planejamento.gerencia_tatica.id = planejamento.matriz_planejamento.gerencia_tatica_id ").append("\n");
        sql.append("      inner join planejamento.plano_acao on planejamento.plano_acao.matriz_planejamento_id = planejamento.matriz_planejamento.id ").append("\n");
        // sql.append("      inner join pessoa.tbr_unidade on pessoa.tbr_unidade.int_id = planejamento.matriz_planejamento.unidade_organizacional_id ").append("\n");
		sql.append("where planejamento.meta_estrategica.ano = ").append(filtro.getAno()).append(" ").append("\n");
		sql.append("order by planejamento.matriz_planejamento.codigo ").append("\n");

		// criar a query
		Query query = em.createNativeQuery(sql.toString());

		// executar a consultaLios
		List<Object[]> r = query.getResultList(); 
		if (!CollectionUtils.isEmpty(r)) {
			for (Object[] l: r) {
				if (result == null) {
					result = new ArrayList<>();
				}
				MetaTatica m = new MetaTatica();
				m.setId((Integer) l[0]); 
				m.setDescricao((String) l[1]);
				m.setGerenciaTatica((String) l[2]);
				result.add(m);
			}
		}

		// retornar
		return result;

	}

}