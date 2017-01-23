package br.gov.df.emater.aterwebsrv.dao.indice_producao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.EnumUtils;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import br.gov.df.emater.aterwebsrv.dto.indice_producao.IndiceProducaoCadFiltroDto;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioData;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.ater.Comunidade;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaTipo;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificacao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificacaoFormaProducaoValor;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificado;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.FormaProducaoValor;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoComposicao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoProprietario;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.GrupoSocial;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

public class ProducaoProprietarioDaoImpl implements ProducaoProprietarioDaoCustom {

	@Autowired
	private BemClassificacaoDao bemClassificacaoDao;

	@Autowired
	private EntityManager em;

	@Autowired
	private SessionFactory sessionFactory;

	private void captaBemClassificacao(BemClassificacao bemClassificacao, Map<String, List<_ChavePrimaria<Integer>>> result) {
		if (bemClassificacao != null) {
			List<_ChavePrimaria<Integer>> bemClassificadoList = result.get("bem_classificado_id");
			// captar os bens da classificacao
			if (bemClassificacao.getBemClassificadoList() != null) {
				bemClassificadoList.addAll(bemClassificacao.getBemClassificadoList());
			}
			// captar os bens das classificacoes descendentes
			if (bemClassificacao.getBemClassificacaoList() != null) {
				for (BemClassificacao bc : bemClassificacao.getBemClassificacaoList()) {
					captaBemClassificacao(bc, result);
				}
			}
			result.put("bem_classificado_id", bemClassificadoList);
		}
	}

	private List<Map<String, List<_ChavePrimaria<Integer>>>> captaBemClassificacaoParams(List<BemClassificacao> bemClassificacaoList) {
		List<Map<String, List<_ChavePrimaria<Integer>>>> result = new ArrayList<>();

		for (BemClassificacao bemClassificacao : bemClassificacaoList) {
			Map<String, List<_ChavePrimaria<Integer>>> config = new HashMap<>();

			// captar os bens
			config.put("bem_classificado_id", new ArrayList<>());
			captaBemClassificacao(bemClassificacaoDao.findOne(bemClassificacao.getId()), config);

			// captar a forma de producao dos bens
			List<_ChavePrimaria<Integer>> formaProducaoValorList = null;
			if (bemClassificacao.getBemClassificacaoFormaProducaoValorList() != null) {
				if (formaProducaoValorList == null) {
					formaProducaoValorList = new ArrayList<>();
				}
				for (BemClassificacaoFormaProducaoValor bemClassificacaoFormaProducaoValor : bemClassificacao.getBemClassificacaoFormaProducaoValorList()) {
					if (bemClassificacaoFormaProducaoValor != null && bemClassificacaoFormaProducaoValor.getFormaProducaoValor() != null) {
						formaProducaoValorList.add(bemClassificacaoFormaProducaoValor.getFormaProducaoValor());
					}
				}
			}
			config.put("forma_producao_valor_id", formaProducaoValorList);

			result.add(config);
		}

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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<ProducaoProprietario> filtrarNovo(IndiceProducaoCadFiltroDto filtro) {
		List<ProducaoProprietario> result = null;
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql, sqlTemp;
		String situacao = filtro.getSituacao();

		sql = new StringBuilder();
		sql.append("SELECT DISTINCT a.*,").append("\n");
		sql.append("                d.bem_classificacao_id,").append("\n");
		sql.append("                d.nome as nomeBemClassificado,").append("\n");
		sql.append("                e1.nome as nomeUnidadeOrganizacional,").append("\n");
		sql.append("                f.nome as nomePropriedadeRural,").append("\n");
		sql.append("                f1.id as idComunidade,").append("\n");
		sql.append("                f1.nome as nomeComunidade,").append("\n");
		sql.append("                h.id as idPessoa,").append("\n");
		sql.append("                h.nome as nomePessoa,").append("\n");
		sql.append("                h.pessoa_tipo").append("\n");
		sql.append("FROM   indice_producao.producao_proprietario a").append("\n");
		sql.append("JOIN   indice_producao.producao b").append("\n");
		sql.append("ON     b.producao_proprietario_id = a.id").append("\n");
		sql.append("JOIN   indice_producao.producao_composicao c").append("\n");
		sql.append("ON     c.producao_id = b.id").append("\n");
		sql.append("JOIN   indice_producao.bem_classificado d").append("\n");
		sql.append("ON     d.id = a.bem_classificado_id").append("\n");
		sql.append("LEFT JOIN   funcional.unidade_organizacional e").append("\n");
		sql.append("ON     e.id = a.unidade_organizacional_id").append("\n");
		sql.append("LEFT JOIN   pessoa.pessoa e1").append("\n");
		sql.append("ON     e1.id = e.id").append("\n");
		sql.append("LEFT JOIN   ater.propriedade_rural f").append("\n");
		sql.append("ON     f.id = a.propriedade_rural_id").append("\n");
		// caso o filtro seja para o detalhamento da produçao de uma unidade
		// organizacional
//		if (filtro.getAno() != null && filtro.getBemClassificado() != null && filtro.getBemClassificado().getId() != null && filtro.getUnidadeOrganizacional() != null && filtro.getUnidadeOrganizacional().getId() != null) {
			sql.append("LEFT JOIN   ater.comunidade f1").append("\n");
			sql.append("ON     f1.id = f.comunidade_id").append("\n");
//		}
		sql.append("LEFT JOIN   ater.publico_alvo g").append("\n");
		sql.append("ON     g.id = a.publico_alvo_id").append("\n");
		sql.append("LEFT JOIN   pessoa.pessoa h").append("\n");
		sql.append("ON     h.id = g.pessoa_id").append("\n");

		if (filtro.getId() != null) {
			// filtrar producao especifica
			params.add(filtro.getId());
			sql.append("WHERE a.id = :p").append(params.size()).append("\n");
		} else if (filtro.getPropriedadeRural() != null && filtro.getPropriedadeRural().getId() != null) {
			// filtrar producao por propriedade rural
			params.add(filtro.getPropriedadeRural().getId());
			sql.append("WHERE a.propriedade_rural_id = :p").append(params.size()).append("\n");
		} else if (filtro.getPublicoAlvo() != null && filtro.getPublicoAlvo().getId() != null) {
			// filtrar producao por publicoalvo
			params.add(filtro.getPublicoAlvo().getId());
			sql.append("WHERE a.publico_alvo_id = :p").append(params.size()).append("\n");
		} else if (filtro.getAno() != null && filtro.getBemClassificado() != null && filtro.getBemClassificado().getId() != null && filtro.getUnidadeOrganizacional() != null && filtro.getUnidadeOrganizacional().getId() != null) {
			// filtrar a producao dos produtores de uma unidade organizacional
			// ATENÇÃO! A ativacao deste filtro deve ser exclusiva do serviço,
			// não permitir que a camada de visão preencha estes dados
			// este é preenchido pelo FiltroDetalheCmd
			params.add(filtro.getAno());
			sql.append("WHERE a.ano = :p").append(params.size()).append("\n");
			params.add(filtro.getBemClassificado().getId());
			sql.append("AND   a.bem_classificado_id = :p").append(params.size()).append("\n");
			params.add(filtro.getUnidadeOrganizacional().getId());
			sql.append("AND   f1.unidade_organizacional_id = :p").append(params.size()).append("\n");
		} else {
			// filtrar producao por unidade organizacional
			sql.append("WHERE a.propriedade_rural_id is null").append("\n");
			sql.append("AND   a.publico_alvo_id is null").append("\n");
		}

		if (filtro.getAno() != null) {
			params.add(filtro.getAno());
			sql.append("AND    a.ano = :p").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getBemClassificadoList()) || !CollectionUtils.isEmpty(filtro.getBemClassificacaoList())) {
			sqlTemp = new StringBuilder();
			if (!CollectionUtils.isEmpty(filtro.getBemClassificadoList())) {
				params.add(getIdList(filtro.getBemClassificadoList()));
				sqlTemp.append("a.bem_classificado_id IN (:p").append(params.size()).append(")\n");
			}
			if (!CollectionUtils.isEmpty(filtro.getBemClassificacaoList())) {
				List<Map<String, List<_ChavePrimaria<Integer>>>> bemClassificacaoParams = captaBemClassificacaoParams(filtro.getBemClassificacaoList());
				StringBuilder sqlTempSub = new StringBuilder();
				if (sqlTemp.length() > 0) {
					sqlTemp.append("AND ").append("\n");
				}
				for (Map<String, List<_ChavePrimaria<Integer>>> p : bemClassificacaoParams) {
					if (sqlTempSub.length() > 0) {
						sqlTempSub.append("OR").append("\n");
					}
					if (!CollectionUtils.isEmpty(p.get("bem_classificado_id"))) {
						params.add(getIdList(p.get("bem_classificado_id")));
						sqlTempSub.append("(a.bem_classificado_id IN (SELECT a1.id").append("\n");
						sqlTempSub.append("                           FROM indice_producao.bem_classificado a1").append("\n");
						sqlTempSub.append("                           JOIN indice_producao.bem_classificacao b1").append("\n");
						sqlTempSub.append("                           ON a1.bem_classificacao_id = b1.id").append("\n");
						sqlTempSub.append("                           WHERE b1.id IN (:p").append(params.size()).append("))\n");
					}
					if (!CollectionUtils.isEmpty(p.get("forma_producao_valor_id"))) {
						params.add(getIdList(p.get("forma_producao_valor_id")));
						sqlTempSub.append("AND    c.forma_producao_valor_id IN (:p").append(params.size()).append(")\n");
					}
					if (sqlTempSub.length() > 0) {
						sqlTempSub.append(")").append("\n");
					}
				}
				if (sqlTempSub.length() > 0) {
					sqlTemp.append(sqlTempSub).append("\n");
				}
			}
			if (sqlTemp.length() > 0) {
				sql.append("AND    (").append(sqlTemp).append(")\n");
			}
		}

		if (filtro.getBemClassificado() == null && filtro.getUnidadeOrganizacional() == null) {
			// este filtro só pode ser executado se nao for detalhamento

			if (!CollectionUtils.isEmpty(filtro.getUnidadeOrganizacionalList()) || !CollectionUtils.isEmpty(filtro.getComunidadeList())) {
				sqlTemp = new StringBuilder();

				if (!CollectionUtils.isEmpty(filtro.getEmpresaList())) {
					params.add(getIdList(filtro.getEmpresaList()));
					sqlTemp.append("e.pessoa_juridica_id IN (:p").append(params.size()).append(")\n");
				}
				if (!CollectionUtils.isEmpty(filtro.getUnidadeOrganizacionalList())) {
					params.add(getIdList(filtro.getUnidadeOrganizacionalList()));
					sqlTemp.append("a.unidade_organizacional_id IN (:p").append(params.size()).append(")\n");
				}
				if (!CollectionUtils.isEmpty(filtro.getComunidadeList())) {
					if (sqlTemp.length() > 0) {
						sqlTemp.append("OR").append("\n");
					}
					params.add(getIdList(filtro.getComunidadeList()));
					sqlTemp.append("a.unidade_organizacional_id IN (SELECT  a2.unidade_organizacional_id").append("\n");
					sqlTemp.append("                                FROM    ater.comunidade a2").append("\n");
					sqlTemp.append("                                WHERE   a2.id IN (:p").append(params.size()).append("))\n");
				}
				sql.append("AND    (").append(sqlTemp).append(")\n");
			}
		}

		sql.append("ORDER BY a.ano, d.nome, e1.nome, h.nome, f.nome").append("\n");

		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql.toString()).setResultTransformer(new ResultTransformer() {

			private static final long serialVersionUID = 1L;

			@Override
			public List transformList(List collection) {
				List<ProducaoProprietario> result = (List<ProducaoProprietario>) collection;
				if (result != null && result.size() > 0) {
					List<Producao> producaoList = getProducaoList(result, situacao);
					if (producaoList != null && producaoList.size() > 0) {
						result.stream().forEach(o -> o.setProducaoList(producaoList.stream().filter(p -> p.getProducaoProprietario().getId().equals(o.getId())).collect(Collectors.toList())));
					}
				}
				return result;
			}

			@Override
			public Object transformTuple(Object[] tuple, String[] aliases) {
				List<String> campos = Arrays.asList(aliases);
				ProducaoProprietario result = new ProducaoProprietario();
				result.setId((Integer) tuple[campos.indexOf("id")]);
				result.setAlteracaoData(UtilitarioData.getInstance().sqlTimestampToCalendar((Timestamp) tuple[campos.indexOf("alteracao_data")]));
				result.setAlteracaoUsuario(new Usuario((Integer) tuple[campos.indexOf("alteracao_usuario_id")]));
				result.setAno((Integer) tuple[campos.indexOf("ano")]);
				result.setBemClassificado(new BemClassificado((Integer) tuple[campos.indexOf("bem_classificado_id")], (String) tuple[campos.indexOf("nomeBemClassificado")], new BemClassificacao((Integer) tuple[campos.indexOf("bem_classificacao_id")])));
				result.setChaveSisater((String) tuple[campos.indexOf("chave_sisater")]);
				result.setInclusaoData(UtilitarioData.getInstance().sqlTimestampToCalendar((Timestamp) tuple[campos.indexOf("inclusao_data")]));
				result.setInclusaoUsuario(new Usuario((Integer) tuple[campos.indexOf("inclusao_usuario_id")]));
				result.setUnidadeOrganizacional(new UnidadeOrganizacional((Integer) tuple[campos.indexOf("unidade_organizacional_id")], (String) tuple[campos.indexOf("nomeUnidadeOrganizacional")], null, null, null, null));
				result.setPropriedadeRural(new PropriedadeRural((Integer) tuple[campos.indexOf("propriedade_rural_id")], (String) tuple[campos.indexOf("nomePropriedadeRural")], new Comunidade((Integer) tuple[campos.indexOf("idComunidade")], (String) tuple[campos.indexOf("nomeComunidade")], null), null, null));
				Pessoa pessoa = null;
				if (tuple[campos.indexOf("pessoa_tipo")] != null) {
					switch (EnumUtils.getEnum(PessoaTipo.class, (String) tuple[campos.indexOf("pessoa_tipo")])) {
					case PF:
						pessoa = new PessoaFisica((Integer) tuple[campos.indexOf("idPessoa")], (String) tuple[campos.indexOf("nomePessoa")], null, null, null, null, null, null);
						break;
					case PJ:
						pessoa = new PessoaJuridica((Integer) tuple[campos.indexOf("idPessoa")], (String) tuple[campos.indexOf("nomePessoa")], null, null, null, null, null);
						break;
					case GS:
						pessoa = new GrupoSocial((Integer) tuple[campos.indexOf("idPessoa")], (String) tuple[campos.indexOf("nomePessoa")], null, null, null, null, null, null);
						break;
					default:
						break;
					}
				}
				result.setPublicoAlvo(new PublicoAlvo((Integer) tuple[campos.indexOf("publico_alvo_id")], pessoa));

				return result;
			}

		});

		// inserir os parametros
		for (int i = 1; i <= params.size(); i++) {
			if (params.get(i - 1) instanceof Collection) {
				query.setParameterList(String.format("p%d", i), (Collection) params.get(i - 1));
			} else {
				query.setParameter(String.format("p%d", i), params.get(i - 1));
			}
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

	private List<Integer> getIdList(List<? extends _ChavePrimaria<Integer>> chavePrimariaList) {
		return chavePrimariaList == null ? null : chavePrimariaList.stream().map(_ChavePrimaria<Integer>::getId).collect(Collectors.toCollection(ArrayList::new));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Producao> getProducaoList(List<ProducaoProprietario> producaoProprietarioList, String situacao) {

		// objetos de trabalho
		List<Producao> result = null;
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql;

		// construção do sql
		sql = new StringBuilder();
		sql.append("SELECT ").append("\n");
		sql.append("    a.*,").append("\n");
		sql.append("    b.id AS producaoComposicaoId,").append("\n");
		sql.append("    b.forma_producao_valor_id,").append("\n");
		sql.append("    b.ordem,").append("\n");
		sql.append("    c.nome AS formaProducaoValorNome,").append("\n");
		sql.append("    c.forma_producao_item_id,").append("\n");
		sql.append("    c.ordem AS formaProducaoValorOrdem").append("\n");
		sql.append("FROM").append("\n");
		sql.append("    indice_producao.producao a").append("\n");
		sql.append("        JOIN").append("\n");
		sql.append("    indice_producao.producao_composicao b ON b.producao_id = a.id").append("\n");
		sql.append("        JOIN").append("\n");
		sql.append("    indice_producao.forma_producao_valor c ON c.id = b.forma_producao_valor_id").append("\n");
		sql.append("WHERE").append("\n");
		sql.append("    a.producao_proprietario_id IN (:p1)").append("\n");
		sql.append("ORDER BY a.id , b.ordem , b.id").append("\n");

		params.add(getIdList(producaoProprietarioList));

		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql.toString()).setResultTransformer(new ResultTransformer() {

			private static final long serialVersionUID = 1L;

			private Producao ultimaProducao = new Producao();

			@Override
			public List transformList(List collection) {
				return new ArrayList<>(new HashSet<>((List<Producao>) collection));
			}

			@Override
			public Object transformTuple(Object[] tuple, String[] aliases) {
				List<String> campos = Arrays.asList(aliases);

				Producao result;
				if (((Integer) tuple[campos.indexOf("id")]).equals(this.ultimaProducao.getId())) {
					result = this.ultimaProducao;
				} else {
					result = new Producao();
					result.setId((Integer) tuple[campos.indexOf("id")]);
					result.setSituacao(situacao);
					result.setProducaoComposicaoList(new ArrayList<>());
					if (((Float) tuple[campos.indexOf("item_a_valor")]) != null) {
						result.setItemAValor(new BigDecimal((Float) tuple[campos.indexOf("item_a_valor")]));
					}
					if (((Float) tuple[campos.indexOf("item_b_valor")]) != null) {
						result.setItemBValor(new BigDecimal((Float) tuple[campos.indexOf("item_b_valor")]));
					}
					if (((Float) tuple[campos.indexOf("item_c_valor")]) != null) {
						result.setItemCValor(new BigDecimal((Float) tuple[campos.indexOf("item_c_valor")]));
					}
					result.setProducaoProprietario(new ProducaoProprietario((Integer) tuple[campos.indexOf("producao_proprietario_id")]));
					if (((Float) tuple[campos.indexOf("valor_total")]) != null) {
						result.setValorTotal(new BigDecimal((Float) tuple[campos.indexOf("valor_total")]));
					}
					if (((Float) tuple[campos.indexOf("valor_unitario")]) != null) {
						result.setValorUnitario(new BigDecimal((Float) tuple[campos.indexOf("valor_unitario")]));
					}
					if (((Float) tuple[campos.indexOf("volume")]) != null) {
						result.setVolume(new BigDecimal((Float) tuple[campos.indexOf("volume")]));
					}
					result.setQuantidadeProdutores((Integer) tuple[campos.indexOf("quantidade_produtores")]);
					result.setDataConfirmacao(UtilitarioData.getInstance().sqlTimestampToCalendar((Timestamp) tuple[campos.indexOf("data_confirmacao")]));
					
					//result.setInclusaoUsuario(new Usuario((Integer) tuple[campos.indexOf("inclusao_usuario_id")]));
					result.setInclusaoData(UtilitarioData.getInstance().sqlTimestampToCalendar((Timestamp) tuple[campos.indexOf("inclusao_data")]));
					//result.setAlteracaoUsuario(new Usuario((Integer) tuple[campos.indexOf("alteracao_usuario_id")]));
					result.setAlteracaoData(UtilitarioData.getInstance().sqlTimestampToCalendar((Timestamp) tuple[campos.indexOf("alteracao_data")]));
					
					this.ultimaProducao = result;
				}

				ProducaoComposicao producaoComposicao = new ProducaoComposicao();
				producaoComposicao.setId((Integer) tuple[campos.indexOf("producaoComposicaoId")]);
				producaoComposicao.setFormaProducaoValor(new FormaProducaoValor((Integer) tuple[campos.indexOf("forma_producao_valor_id")], (String) tuple[campos.indexOf("formaProducaoValorNome")], null, (Integer) tuple[campos.indexOf("formaProducaoValorOrdem")]));
				producaoComposicao.setOrdem((Integer) tuple[campos.indexOf("ordem")]);
				result.getProducaoComposicaoList().add(producaoComposicao);

				return result;
			}

		});

		// inserir os parametros
		for (int i = 1; i <= params.size(); i++) {
			if (params.get(i - 1) instanceof Collection) {
				query.setParameterList(String.format("p%d", i), (Collection) params.get(i - 1));
			} else {
				query.setParameter(String.format("p%d", i), params.get(i - 1));
			}
		}

		// executar a consulta
		result = (List<Producao>) query.list();

		// retornar
		return result;
	}

}