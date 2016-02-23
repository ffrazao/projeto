package br.gov.df.emater.aterwebsrv.dao.indice_producao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import br.gov.df.emater.aterwebsrv.modelo.dto.IndiceProducaoCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificacao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;

public class ProducaoDaoImpl implements ProducaoDaoCustom {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private BemClassificacaoDao bemClassificacaoDao;

	@Override
	@Transactional(readOnly = true)
	public List<Producao> filtrar(IndiceProducaoCadFiltroDto filtro) {
		// ATENCAO: quando o parametro id do filtro estiver preenchido significa
		// que este método vai coletar uma producao especifica, ou seja, é
		// utilizado para recuperar os dados vinculados a uma producao principal
		// que é representada por um conjunto especifico dos valores dos
		// parametros ano e comunidade

		// objetos de trabalho
		List<Producao> result = null;
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql;// , sqlTemp;

		// construção do sql
		sql = new StringBuilder();
		sql.append("select distinct p").append("\n");
		sql.append("from Producao p").append("\n");
		sql.append("join p.unidadeOrganizacional.comunidadeList comun").append("\n");
		sql.append("join p.producaoFormaList pfl").append("\n");
		sql.append("join pfl.producaoFormaComposicaoList composicao").append("\n");
		if (filtro.getId() != null) {
			params.add(filtro.getId());
			sql.append("where p.id = ?").append(params.size()).append("\n");
		} else {
			sql.append("where p.propriedadeRural is null").append("\n");
			sql.append("and   p.publicoAlvo is null").append("\n");
			if (filtro.getAno() != null) {
				params.add(filtro.getAno());
				sql.append("and p.ano = ?").append(params.size()).append("\n");
			}
			// TODO implementar o restante do filtro
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
		if (!CollectionUtils.isEmpty(filtro.getBemList())) {
			params.add(filtro.getBemList());
			sql.append("and p.bem in ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getBemClassificacaoList())) {
			List<BemClassificacao> bemClassificacaoList = new ArrayList<BemClassificacao>();
			captarBemClassificacaoList(filtro.getBemClassificacaoList(), bemClassificacaoList);
			params.add(bemClassificacaoList);
			sql.append("and p.bem.bemClassificacao in ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getFormaProducaoValorList())) {
			params.add(filtro.getFormaProducaoValorList());
			sql.append("and composicao.formaProducaoValor in ?").append(params.size()).append("\n");
		}
		
		sql.append("order by p.ano").append("\n");

		// criar a query
		TypedQuery<Producao> query = em.createQuery(sql.toString(), Producao.class);

		// inserir os parametros
		for (int i = 1; i <= params.size(); i++) {
			query.setParameter(i, params.get(i - 1));
		}

		// definir a pagina a ser consultada
		if (filtro.getId() == null) {
			filtro.configuraPaginacao(query);
		}

		// executar a consulta
		result = query.getResultList();

		// retornar
		return result;
	}

	private void captarBemClassificacaoList(List<BemClassificacao> origem, List<BemClassificacao> destino) {
		if (origem != null) {
			for (BemClassificacao bcOrigem : origem) {
				BemClassificacao bemClassificacao = bemClassificacaoDao.findOne(bcOrigem.getId());
				captarBemClassificacaoList(bemClassificacao.getBemClassificacaoList(), destino);
				destino.add(new BemClassificacao(bemClassificacao.getId()));
			}
		}
	}

}