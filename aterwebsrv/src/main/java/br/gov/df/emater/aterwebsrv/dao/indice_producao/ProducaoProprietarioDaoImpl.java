package br.gov.df.emater.aterwebsrv.dao.indice_producao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import br.gov.df.emater.aterwebsrv.dto.indice_producao.IndiceProducaoCadFiltroDto;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioData;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificacao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificado;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoProprietario;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

public class ProducaoProprietarioDaoImpl implements ProducaoProprietarioDaoCustom {

	@Autowired
	private BemClassificacaoDao bemClassificacaoDao;

	@Autowired
	private EntityManager em;
	
	@Autowired
	private SessionFactory sessionFactory;

	private void captarBemClassificacaoList(List<BemClassificacao> origem, List<BemClassificacao> destino) {
		if (origem != null) {
			for (BemClassificacao bcOrigem : origem) {
				BemClassificacao bemClassificacao = bemClassificacaoDao.findOne(bcOrigem.getId());
				captarBemClassificacaoList(bemClassificacao.getBemClassificacaoList(), destino);
				destino.add(new BemClassificacao(bemClassificacao.getId()));
			}
		}
	}

	@Override
	public List<ProducaoProprietario> filtrar(IndiceProducaoCadFiltroDto filtro) {
		// ATENCAO: quando o parametro id do filtro estiver preenchido significa
		// que este método vai coletar uma producao especifica, ou seja, é
		// utilizado para recuperar os dados vinculados a uma producao principal
		// que é representada por um conjunto especifico dos valores dos
		// parametros ano e comunidade

		// objetos de trabalho
		List<ProducaoProprietario> result = null;
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql;// , sqlTemp;

		// construção do sql
		sql = new StringBuilder();
		sql.append("select distinct p").append("\n");
		sql.append("from ProducaoProprietario p").append("\n");

		if (!CollectionUtils.isEmpty(filtro.getComunidadeList())) {
			sql.append("join p.unidadeOrganizacional.comunidadeList comun").append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getFormaProducaoValorList())) {
			sql.append("join p.producaoList pfl").append("\n");
			sql.append("join pfl.producaoComposicaoList composicao").append("\n");
		}

		if (filtro.getId() != null) {
			// filtrar producao especifica
			params.add(filtro.getId());
			sql.append("where p.id = ?").append(params.size()).append("\n");
		} else if (filtro.getPropriedadeRural() != null && filtro.getPropriedadeRural().getId() != null) {
			// filtrar producao por propriedade rural
			params.add(filtro.getPropriedadeRural());
			sql.append("where p.propriedadeRural = ?").append(params.size()).append("\n");
		} else if (filtro.getPublicoAlvo() != null && filtro.getPublicoAlvo().getId() != null) {
			// filtrar producao por publicoalvo
			params.add(filtro.getPublicoAlvo());
			sql.append("where p.publicoAlvo = ?").append(params.size()).append("\n");
		} else {
			// filtrar producao por unidade organizacional
			sql.append("where p.propriedadeRural is null").append("\n");
			sql.append("and   p.publicoAlvo is null").append("\n");
		}
		if (filtro.getAno() != null) {
			params.add(filtro.getAno());
			sql.append("and p.ano = ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getEmpresaList())) {
			params.add(filtro.getEmpresaList());
			sql.append("and p.unidadeOrganizacional.pessoaJuridica in ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getUnidadeOrganizacionalList())) {
			params.add(filtro.getUnidadeOrganizacionalList());
			sql.append("and p.unidadeOrganizacional in ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getComunidadeList())) {
			params.add(filtro.getComunidadeList());
			sql.append("and comun in ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getBemClassificadoList())) {
			params.add(filtro.getBemClassificadoList());
			sql.append("and p.bemClassificado in ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getBemClassificacaoList())) {
			List<BemClassificacao> bemClassificacaoList = new ArrayList<BemClassificacao>();
			captarBemClassificacaoList(filtro.getBemClassificacaoList(), bemClassificacaoList);
			params.add(bemClassificacaoList);
			sql.append("and p.bemClassificado.bemClassificacao in ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getFormaProducaoValorList())) {
			params.add(filtro.getFormaProducaoValorList());
			sql.append("and composicao.formaProducaoValor in ?").append(params.size()).append("\n");
		}

		sql.append("order by p.ano, p.bemClassificado.bemClassificacao.nome, p.bemClassificado.nome").append("\n");
		if ((filtro.getPropriedadeRural() != null && filtro.getPropriedadeRural().getId() != null) || (filtro.getPublicoAlvo() != null && filtro.getPublicoAlvo().getId() != null)) {
			sql.append("   , p.propriedadeRural.nome, p.publicoAlvo.pessoa.nome").append("\n");
		} else {
			sql.append("   , p.unidadeOrganizacional.nome").append("\n");
		}

		// criar a query
		TypedQuery<ProducaoProprietario> query = em.createQuery(sql.toString(), ProducaoProprietario.class);

		// inserir os parametros
		for (int i = 1; i <= params.size(); i++) {
			query.setParameter(i, params.get(i - 1));
		}

		// definir a pagina a ser consultada
		if (filtro.getId() == null && (filtro.getPropriedadeRural() == null || filtro.getPropriedadeRural().getId() == null) && (filtro.getPublicoAlvo() == null || filtro.getPublicoAlvo().getId() == null)) {
			filtro.configuraPaginacao(query);
		}

		// executar a consulta
		result = query.getResultList();

		// retornar
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProducaoProprietario> filtrarNovo(IndiceProducaoCadFiltroDto filtro) {
		// objetos de trabalho
		List<ProducaoProprietario> result = null;
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql;// , sqlTemp;
	
		// construção do sql
		sql = new StringBuilder();
		sql.append("SELECT DISTINCT a.*, d.nome as nomeBemClassificado, e.nome as nomeUnidadeOrganizacional").append("\n");
		sql.append("FROM   indice_producao.producao_proprietario a").append("\n");
		sql.append("JOIN   indice_producao.producao b").append("\n");
		sql.append("ON     b.producao_proprietario_id = a.id").append("\n");
		sql.append("JOIN   indice_producao.producao_composicao c").append("\n");
		sql.append("ON     c.producao_id = b.id").append("\n");
		sql.append("JOIN   indice_producao.bem_classificado d").append("\n");
		sql.append("ON     a.bem_classificado_id = d.id").append("\n");
		sql.append("JOIN   funcional.unidade_organizacional e").append("\n");
		sql.append("ON     a.unidade_organizacional_id = e.id").append("\n");
		sql.append("WHERE  a.ano = 2016").append("\n");
		sql.append("AND    (   a.bem_classificado_id IN (61, 1, 2, 3, 4, 6, 10)").append("\n");
		sql.append("        OR a.bem_classificado_id IN (SELECT a1.id").append("\n");
		sql.append("                                     FROM indice_producao.bem_classificado a1").append("\n");
		sql.append("                                     JOIN indice_producao.bem_classificacao b1").append("\n");
		sql.append("                                     ON a1.bem_classificacao_id = b1.id").append("\n");
		sql.append("                                     WHERE b1.id IN (3, 55, 21))").append("\n");
		sql.append("       )").append("\n");
		sql.append("AND    (   a.unidade_organizacional_id IN (2, 65, 71, 72)").append("\n");
		sql.append("        OR a.unidade_organizacional_id IN (SELECT  a2.unidade_organizacional_id").append("\n");
		sql.append("                                           FROM    ater.comunidade a2").append("\n");
		sql.append("                                           WHERE   a2.id IN (78))").append("\n");
		sql.append("       )").append("\n");
		sql.append("AND    c.forma_producao_valor_id IN (2)").append("\n");
		sql.append("ORDER BY a.ano, d.nome, e.nome").append("\n");
		
		
/*		sql = new StringBuilder();
		sql.append("select distinct p").append("\n");
		sql.append("from ProducaoProprietario p").append("\n");
	
		if (!CollectionUtils.isEmpty(filtro.getComunidadeList())) {
			sql.append("join p.unidadeOrganizacional.comunidadeList comun").append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getFormaProducaoValorList())) {
			sql.append("join p.producaoList pfl").append("\n");
			sql.append("join pfl.producaoComposicaoList composicao").append("\n");
		}
	
		if (filtro.getId() != null) {
			// filtrar producao especifica
			params.add(filtro.getId());
			sql.append("where p.id = ?").append(params.size()).append("\n");
		} else if (filtro.getPropriedadeRural() != null && filtro.getPropriedadeRural().getId() != null) {
			// filtrar producao por propriedade rural
			params.add(filtro.getPropriedadeRural());
			sql.append("where p.propriedadeRural = ?").append(params.size()).append("\n");
		} else if (filtro.getPublicoAlvo() != null && filtro.getPublicoAlvo().getId() != null) {
			// filtrar producao por publicoalvo
			params.add(filtro.getPublicoAlvo());
			sql.append("where p.publicoAlvo = ?").append(params.size()).append("\n");
		} else {
			// filtrar producao por unidade organizacional
			sql.append("where p.propriedadeRural is null").append("\n");
			sql.append("and   p.publicoAlvo is null").append("\n");
		}
		if (filtro.getAno() != null) {
			params.add(filtro.getAno());
			sql.append("and p.ano = ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getEmpresaList())) {
			params.add(filtro.getEmpresaList());
			sql.append("and p.unidadeOrganizacional.pessoaJuridica in ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getUnidadeOrganizacionalList())) {
			params.add(filtro.getUnidadeOrganizacionalList());
			sql.append("and p.unidadeOrganizacional in ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getComunidadeList())) {
			params.add(filtro.getComunidadeList());
			sql.append("and comun in ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getBemClassificadoList())) {
			params.add(filtro.getBemClassificadoList());
			sql.append("and p.bemClassificado in ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getBemClassificacaoList())) {
			List<BemClassificacao> bemClassificacaoList = new ArrayList<BemClassificacao>();
			captarBemClassificacaoList(filtro.getBemClassificacaoList(), bemClassificacaoList);
			params.add(bemClassificacaoList);
			sql.append("and p.bemClassificado.bemClassificacao in ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getFormaProducaoValorList())) {
			params.add(filtro.getFormaProducaoValorList());
			sql.append("and composicao.formaProducaoValor in ?").append(params.size()).append("\n");
		}
	
		sql.append("order by p.ano, p.bemClassificado.bemClassificacao.nome, p.bemClassificado.nome").append("\n");
		if ((filtro.getPropriedadeRural() != null && filtro.getPropriedadeRural().getId() != null) || (filtro.getPublicoAlvo() != null && filtro.getPublicoAlvo().getId() != null)) {
			sql.append("   , p.propriedadeRural.nome, p.publicoAlvo.pessoa.nome").append("\n");
		} else {
			sql.append("   , p.unidadeOrganizacional.nome").append("\n");
		}*/
		
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql.toString()).setResultTransformer(new ResultTransformer() {
			
			@Override
			public Object transformTuple(Object[] tuple, String[] aliases) {
				List<String> campos = Arrays.asList(aliases);
				ProducaoProprietario result = new ProducaoProprietario();
		        result.setId((Integer) tuple[campos.indexOf("id")]);
		        result.setAlteracaoData(UtilitarioData.getInstance().sqlTimestampToCalendar((Timestamp) tuple[campos.indexOf("alteracao_data")]));
		        result.setAlteracaoUsuario(new Usuario((Integer) tuple[campos.indexOf("alteracao_usuario_id")]));
		        result.setAno((Integer) tuple[campos.indexOf("ano")]);
		        result.setBemClassificado(new BemClassificado((Integer) tuple[campos.indexOf("bem_classificado_id")], (String) tuple[campos.indexOf("nomeBemClassificado")], null));
		        result.setChaveSisater((String) tuple[campos.indexOf("chave_sisater")]);
		        result.setInclusaoData(UtilitarioData.getInstance().sqlTimestampToCalendar((Timestamp) tuple[campos.indexOf("inclusao_data")]));
		        result.setInclusaoUsuario(new Usuario((Integer) tuple[campos.indexOf("inclusao_usuario_id")]));
		        result.setUnidadeOrganizacional(new UnidadeOrganizacional((Integer) tuple[campos.indexOf("unidade_organizacional_id")], (String) tuple[campos.indexOf("nomeUnidadeOrganizacional")], null, null, null));
		        return result;
			}
			
			@Override
			public List transformList(List collection) {
				// TODO Auto-generated method stub
				return collection;
			}
		})
				//.addEntity("a", ProducaoProprietario.class)
				//.addJoin("d", "a.bemClassificado")
				//.addJoin("e", "a.unidadeOrganizacional")
				//.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				;
	
		// inserir os parametros
		for (int i = 1; i <= params.size(); i++) {
			query.setParameter(i, params.get(i - 1));
		}
	
		// definir a pagina a ser consultada
		if (filtro.getId() == null && (filtro.getPropriedadeRural() == null || filtro.getPropriedadeRural().getId() == null) && (filtro.getPublicoAlvo() == null || filtro.getPublicoAlvo().getId() == null)) {
			filtro.configuraPaginacao(query);
		}
	
		// executar a consulta
		result = (List<ProducaoProprietario>) query.list();
	
		// retornar
		return result;
	}

}