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
		
		sql.append(" select distinct ").append("\n");
		sql.append(" planejamento.matriz_planejamento.id, ").append("\n");
		sql.append(" planejamento.matriz_planejamento.codigo, ").append("\n");
		sql.append(" planejamento.matriz_planejamento.descricao, ").append("\n");
		sql.append(" planejamento.matriz_planejamento.objetivo, ").append("\n");
		sql.append(" concat( planejamento.matriz_planejamento.codigo, ' - ' , "
				+ "          planejamento.matriz_planejamento.descricao , ' - ', "
				+ "          planejamento.matriz_planejamento.objetivo) as filtro, ").append("\n");
		sql.append(" 	     planejamento.gerencia_tatica.nome as gerenciaTatica, ").append("\n");
		sql.append(" planejamento.meta_estrategica.ano  ").append("\n");
		sql.append(" from planejamento.meta_estrategica ").append("\n");
		sql.append("      inner join planejamento.matriz_planejamento on planejamento.matriz_planejamento.meta_estrategica_id = planejamento.meta_estrategica.id ").append("\n"); 
        sql.append("      inner join planejamento.gerencia_tatica on planejamento.gerencia_tatica.id = planejamento.matriz_planejamento.gerencia_tatica_id ").append("\n");
        sql.append("      inner join planejamento.plano_acao on planejamento.plano_acao.matriz_planejamento_id = planejamento.matriz_planejamento.id ").append("\n");
        // sql.append("      inner join pessoa.tbr_unidade on pessoa.tbr_unidade.int_id = planejamento.matriz_planejamento.unidade_organizacional_id ").append("\n");
		sql.append("where planejamento.meta_estrategica.ano = ").append(filtro.getAno()).append(" ").append("\n");
		sql.append("  and planejamento.matriz_planejamento.id <> 684 ").append("\n");

		//System.out.println(sql.toString() );
		
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
				m.setCodigo((String) l[1]);
				m.setDescricao((String) l[2]);
				m.setObjetivo((String) l[3]);
				m.setFiltro((String) l[4]);
				m.setGerenciaTatica((String) l[5]);
				result.add(m);
			}
		}

		// retornar
		return result;

	}

}