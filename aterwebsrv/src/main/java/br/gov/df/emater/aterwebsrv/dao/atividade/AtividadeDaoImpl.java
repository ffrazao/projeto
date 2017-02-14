package br.gov.df.emater.aterwebsrv.dao.atividade;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import br.gov.df.emater.aterwebsrv.dto.TagDto;
import br.gov.df.emater.aterwebsrv.dto.agenda.AgendaCadFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.agenda.AgendaDto;
import br.gov.df.emater.aterwebsrv.dto.atividade.AtividadeCadFiltroDto;
import br.gov.df.emater.aterwebsrv.ferramenta.Util;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioData;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaGenero;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaGeracao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PublicoAlvoCategoria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PublicoAlvoSegmento;

public class AtividadeDaoImpl implements AtividadeDaoCustom {

	@Autowired
	private EntityManager em;

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Object[]> filtrar(AtividadeCadFiltroDto filtro) {
		// objetos de trabalho
		List<Object[]> result = null;
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql, sqlTemp, sqlFiltro, sqlCodigo;

		// construção do sql
		sql = new StringBuilder();
		sql.append("select distinct a.id").append("\n"); // ATIV_ID
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
		
		if (!CollectionUtils.isEmpty(filtro.getExecutorList())) {
			sql.append("left join a.pessoaExecutorList exec").append("\n");
		}
		
		if (!CollectionUtils.isEmpty(filtro.getDemandanteList())) {
			sql.append("left join a.pessoaDemandanteList deman").append("\n");
		}

		if (filtro.getAssunto() != null) {
			sql.append("left join a.assuntoList ass").append("\n");
		}
		
		if (!CollectionUtils.isEmpty(filtro.getGenero()) || (!CollectionUtils.isEmpty(filtro.getGeracao()))) {
			sql.append("join Treat(deman.pessoa as PessoaFisica) pf").append("\n");
		}
		
		if (filtro.getMetaTatica() != null) {
			sql.append("left join a.metaTaticaList metaTatica").append("\n");
		}

		sql.append("where 1 = 1").append("\n");

		sqlCodigo = new StringBuilder();
		sqlFiltro = new StringBuilder();

		if (!CollectionUtils.isEmpty(filtro.getCodigoList())) {
			sql.append("and (").append("\n");
			sqlTemp = new StringBuilder();
			for (TagDto nome : filtro.getCodigoList()) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append(" or ");
				}
				String n = Util.codigoAtividadeFormatar(nome.getText());
				// if (Util.codigoAtividadeEhValido(n)) {
				params.add(n);
				sqlTemp.append(" a.codigo = ?").append(params.size()).append("\n");
				// }
			}
			sql.append(sqlTemp);
			sql.append(" )").append("\n");
		}

		if (filtro.getInicio() != null) {
			params.add(UtilitarioData.getInstance().ajustaInicioDia(filtro.getInicio()));
			sqlFiltro.append("and a.inicio >= ?").append(params.size()).append("\n");
		}
		if (filtro.getTermino() != null) {
			params.add(UtilitarioData.getInstance().ajustaFinalDia(filtro.getTermino()));
			sqlFiltro.append("and a.inicio <= ?").append(params.size()).append("\n");
		}
		if (filtro.getMetodo() != null) {
			params.add(filtro.getMetodo());
			sqlFiltro.append("and a.metodo = ?").append(params.size()).append("\n");
		}
		if (filtro.getAssunto() != null) {
			params.add(filtro.getAssunto());
			sqlFiltro.append("and ass.assunto = ?").append(params.size()).append("\n");
		}
		if (filtro.getMetaTatica() != null) {
			params.add(filtro.getMetaTatica());
			sql.append("and metaTatica.metaTaticaId = ?").append(params.size()).append("\n");
		}

		if (!CollectionUtils.isEmpty(filtro.getDemandanteList())) {
			sql.append("and (").append("\n");
			sqlTemp = new StringBuilder();
			for (TagDto nome : filtro.getDemandanteList()) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append(" or ");
				}
				String n = nome.getText().replaceAll("\\s", "%");
				params.add(String.format("%%%s%%", n));
				sqlTemp.append(" (deman.pessoa.nome like ?").append(params.size());
				params.add(String.format("%%%s%%", n));
				sqlTemp.append(" or deman.pessoa.apelidoSigla like ?").append(params.size()).append(")").append("\n");
			}
			sql.append(sqlTemp);
			sql.append(" )").append("\n");
		}

		if (!CollectionUtils.isEmpty(filtro.getExecutorList())) {
			sql.append("and (").append("\n");
			sqlTemp = new StringBuilder();
			for (TagDto nome : filtro.getExecutorList()) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append(" or ");
				}
				String n = nome.getText().replaceAll("\\s", "%");
				params.add(String.format("%%%s%%", n));
				sqlTemp.append(" (exec.pessoa.nome like ?").append(params.size());
				params.add(String.format("%%%s%%", n));
				sqlTemp.append(" or exec.pessoa.apelidoSigla like ?").append(params.size()).append(")").append("\n");
			}
			sql.append(sqlTemp);
			sql.append(" )").append("\n");
		}

		// filtro de beneficiario
		boolean benef = false;
		if (!CollectionUtils.isEmpty(filtro.getSegmento())
				&& (PublicoAlvoSegmento.values().length != (filtro.getSegmento().size()))) {
			benef = true;
			params.add(filtro.getSegmento());
			sqlFiltro.append("and deman.pessoa.publicoAlvo.segmento in ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getCategoria())
				&& (PublicoAlvoCategoria.values().length != (filtro.getCategoria().size()))) {
			benef = true;
			params.add(filtro.getCategoria());
			sqlFiltro.append("and deman.pessoa.publicoAlvo.categoria in ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getGenero())
				&& (PessoaGenero.values().length != (filtro.getGenero().size()))) {
			benef = true;
			params.add(filtro.getGenero().iterator().next());
			sqlFiltro.append("and pf.genero = ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getGeracao())
				&& (PessoaGeracao.values().length != (filtro.getGeracao().size()))) {
			benef = true;
			sqlFiltro.append("and (").append("\n");
			sqlTemp = new StringBuilder();
			for (PessoaGeracao pg : filtro.getGeracao()) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append(" or ");
				}
				params.add(pg.getIdadeInicio());
				sqlTemp.append("TIMESTAMPDIFF(YEAR, pf.nascimento, CURDATE()) between ?").append(params.size());
				params.add(pg.getIdadeFim());
				sqlTemp.append(" and ?").append(params.size()).append("\n");
			}
			sqlFiltro.append(sqlTemp).append("	)").append("\n");
		}

		// pesquisar por localizacao
		if (!CollectionUtils.isEmpty(filtro.getEmpresaList())
				|| !CollectionUtils.isEmpty(filtro.getUnidadeOrganizacionalList())
				|| !CollectionUtils.isEmpty(filtro.getComunidadeList())) {
			benef = true;
			sql.append("and deman.pessoa in (select publ.pessoa").append("\n");
			sql.append("                     from PublicoAlvo publ").append("\n");
			sql.append("                     join publ.publicoAlvoPropriedadeRuralList parr").append("\n");
			sql.append("                     where 1 = 1").append("\n");
			if (!CollectionUtils.isEmpty(filtro.getEmpresaList())) {
				params.add(filtro.getEmpresaList());
				sql.append("and parr.comunidade.unidadeOrganizacional.pessoaJuridica in ?").append(params.size())
						.append("\n");
			}
			if (!CollectionUtils.isEmpty(filtro.getUnidadeOrganizacionalList())) {
				params.add(filtro.getUnidadeOrganizacionalList());
				sql.append("and parr.comunidade.unidadeOrganizacional in ?").append(params.size()).append("\n");
			}
			if (!CollectionUtils.isEmpty(filtro.getComunidadeList())) {
				params.add(filtro.getComunidadeList());
				sql.append("and parr.comunidade in ?").append(params.size()).append("\n");
			}
			sql.append("                     )").append("\n");
		}

		if (benef) {
			sqlFiltro.append("and deman.pessoa.publicoAlvoConfirmacao = 'S'").append("\n");
		}

		/*
		 * 
		 * 
		 * 
		 * 
		 * 
		 * if (filtrarPublicoAlvo(filtro)) { if (filtro.getPublicoAlvoSetor() !=
		 * null) { sql.append("left join alvo.publicoAlvoSetorList paSetor"
		 * ).append("\n"); } if
		 * (!CollectionUtils.isEmpty(filtro.getEmpresaList()) ||
		 * !CollectionUtils.isEmpty(filtro.getUnidadeOrganizacionalList()) ||
		 * !CollectionUtils.isEmpty(filtro.getComunidadeList())) { sql.append(
		 * "left join alvo.publicoAlvoPropriedadeRuralList paPropriedadeRural"
		 * ).append("\n"); } } sql.append("where (1 = 1)").append("\n"); if
		 * (filtrarPublicoAlvo(filtro)) { sql.append(
		 * "and   p.publicoAlvoConfirmacao = 'S'").append("\n"); } if
		 * (!StringUtils.isEmpty(filtro.getNome())) { sql.append("and ("
		 * ).append("\n"); sqlTemp = new StringBuilder(); for (String nome :
		 * filtro.getNome().split(FiltroDto.SEPARADOR_CAMPO)) { if
		 * (sqlTemp.length() > 0) { sqlTemp.append(" or "); }
		 * params.add(String.format("%%%s%%", nome.trim())); sqlTemp.append(
		 * " (p.nome like ?").append(params.size());
		 * params.add(String.format("%%%s%%", nome.trim())); sqlTemp.append(
		 * " or p.apelidoSigla like ?"
		 * ).append(params.size()).append(")").append("\n"); }
		 * sql.append(sqlTemp); sql.append(" )").append("\n"); } if
		 * (!CollectionUtils.isEmpty(filtro.getTipoPessoa()) &&
		 * (PessoaTipo.values().length != (filtro.getTipoPessoa().size()))) {
		 * params.add(filtro.getTipoPessoa()); sql.append(
		 * "and p.pessoaTipo in ?").append(params.size()).append("\n"); } if
		 * (!StringUtils.isEmpty(filtro.getCpf())) {
		 * params.add(UtilitarioString.formataCpf(filtro.getCpf())); sql.append(
		 * "and p.cpf = ?").append(params.size()).append("\n"); } if
		 * (!StringUtils.isEmpty(filtro.getCnpj())) {
		 * params.add(UtilitarioString.formataCnpj(filtro.getCnpj()));
		 * sql.append("and p.cnpj = ?").append(params.size()).append("\n"); } if
		 * (!CollectionUtils.isEmpty(filtro.getPessoaGenero()) &&
		 * (PessoaGenero.values().length != (filtro.getPessoaGenero().size())))
		 * { params.add(filtro.getPessoaGenero()); sql.append(
		 * "and p.pessoaGenero in ?").append(params.size()).append("\n"); } if
		 * (!CollectionUtils.isEmpty(filtro.getPessoaGeracao()) &&
		 * (PessoaGeracao.values().length !=
		 * (filtro.getPessoaGeracao().size()))) { sql.append("and ("
		 * ).append("\n"); sqlTemp = new StringBuilder(); for (PessoaGeracao pg
		 * : filtro.getPessoaGeracao()) { if (sqlTemp.length() > 0) {
		 * sqlTemp.append(" or "); } params.add(pg.getIdadeInicio());
		 * sqlTemp.append(
		 * "TIMESTAMPDIFF(YEAR, p.nascimento, CURDATE()) between ?"
		 * ).append(params.size()); params.add(pg.getIdadeFim());
		 * sqlTemp.append(" and ?").append(params.size()).append("\n"); }
		 * sql.append(sqlTemp).append("	)").append("\n"); } if
		 * (!CollectionUtils.isEmpty(filtro.getPessoaSituacao()) &&
		 * (PessoaSituacao.values().length !=
		 * (filtro.getPessoaSituacao().size()))) {
		 * params.add(filtro.getPessoaSituacao()); sql.append(
		 * "and p.situacao in ?").append(params.size()).append("\n"); } //
		 * filtro de publico alvo if
		 * (!CollectionUtils.isEmpty(filtro.getPublicoAlvoSegmento()) &&
		 * (PublicoAlvoSegmento.values().length !=
		 * (filtro.getPublicoAlvoSegmento().size()))) {
		 * params.add(filtro.getPublicoAlvoSegmento()); sql.append(
		 * "and alvo.segmento in ?").append(params.size()).append("\n"); } if
		 * (!CollectionUtils.isEmpty(filtro.getPublicoAlvoCategoria()) &&
		 * (PublicoAlvoCategoria.values().length !=
		 * (filtro.getPublicoAlvoCategoria().size()))) {
		 * params.add(filtro.getPublicoAlvoCategoria()); sql.append(
		 * "and alvo.categoria in ?").append(params.size()).append("\n"); } if
		 * (!StringUtils.isEmpty(filtro.getPublicoAlvoSetor())) {
		 * params.add(filtro.getPublicoAlvoSetor()); sql.append(
		 * "and paSetor.setor.id = ?").append(params.size()).append("\n"); } if
		 * (!CollectionUtils.isEmpty(filtro.getComunidadeList())) {
		 * params.add(filtro.getComunidadeList()); sql.append(
		 * "and paPropriedadeRural.comunidade in ?"
		 * ).append(params.size()).append("\n"); } if
		 * (!CollectionUtils.isEmpty(filtro.getUnidadeOrganizacionalList())) {
		 * params.add(filtro.getUnidadeOrganizacionalList()); sql.append(
		 * "and paPropriedadeRural.comunidade.unidadeOrganizacional in ?"
		 * ).append(params.size()).append("\n"); } if
		 * (!CollectionUtils.isEmpty(filtro.getEmpresaList())) {
		 * params.add(filtro.getEmpresaList()); sql.append(
		 * "and paPropriedadeRural.comunidade.unidadeOrganizacional.pessoaJuridica in ?"
		 * ).append(params.size()).append("\n"); }
		 * 
		 * 
		 */

		// este é o codigo que garante que o filtro por codigo é independente
		// dos demais filtros
		if (sqlCodigo.length() > 0) {
			if (sqlFiltro.length() > 0) {
				sql.append("and (").append(sqlCodigo).append(" or (1 = 1 ").append(sqlFiltro).append("))\n");
			} else {
				sql.append("and (").append(sqlCodigo).append(")\n");
			}
		} else if (sqlFiltro.length() > 0) {
			sql.append("and (1 = 1 ").append(sqlFiltro).append(")\n");
		}

		sql.append("order by a.inicio desc").append("\n");
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

	@SuppressWarnings("unchecked")
	@Override
	public List<AgendaDto> filtrarAgenda(AgendaCadFiltroDto filtro) {
		List<AgendaDto> result = null;
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql;

		// construção do sql
		sql = new StringBuilder();
		sql.append("select distinct").append("\n");
		sql.append("        a.id").append("\n");
		sql.append("      , a.codigo as title").append("\n");
		// sql.append(" , DATE_FORMAT(a.inicio, '%d/%m/%Y %H:%i:%s') as
		// d1").append("\n");
		// sql.append(" , DATE_FORMAT(a.conclusao, '%d/%m/%Y %H:%i:%s') as
		// d2").append("\n");
		sql.append("      , a.inicio as startF").append("\n");
		sql.append("      , a.conclusao as endF").append("\n");
		sql.append("      , '' as className").append("\n");
		sql.append("      , b.id as metodoId").append("\n");
		sql.append("      , b.nome as metodoNome").append("\n");
		sql.append("      , d.id as pessoaId").append("\n");
		sql.append("      , d.nome as pessoaNome").append("\n");
		sql.append("      , a.detalhamento").append("\n");
		sql.append("from    atividade.atividade a").append("\n");
		sql.append("join    atividade.metodo b").append("\n");
		sql.append("on      b.id = a.metodo_id").append("\n");
		sql.append("join    atividade.atividade_pessoa c").append("\n");
		sql.append("on      c.atividade_id = a.id").append("\n");
		sql.append("and     c.participacao = 'E'").append("\n");
		sql.append("join    pessoa.pessoa d").append("\n");
		sql.append("on      d.id = c.pessoa_id").append("\n");
		sql.append("join    atividade.atividade_assunto e").append("\n");
		sql.append("on      e.atividade_id = a.id").append("\n");
		sql.append("where   a.inicio between :p1 and :p2").append("\n");
		sql.append("and     (a.conclusao is null or a.conclusao between :p1 and :p2)").append("\n");
		params.add(filtro.getInicio());
		params.add(filtro.getTermino());
		// remover o item null
		if (!CollectionUtils.isEmpty(filtro.getPessoaIdList())) {
			filtro.setPessoaIdList(
					filtro.getPessoaIdList().stream().filter(n -> n != null).collect(Collectors.toSet()));
		}
		if (!CollectionUtils.isEmpty(filtro.getPessoaIdList())) {
			sql.append("and    c.pessoa_id in (").append(UtilitarioString.collectionToString(filtro.getPessoaIdList()))
					.append(")").append("\n");
		}
		if (filtro.getMetodo() != null && filtro.getMetodo().getId() != null) {
			params.add(filtro.getMetodo().getId());
			sql.append("and    a.metodo_id = :p").append(params.size()).append("\n");
		}
		if (filtro.getAssunto() != null && filtro.getAssunto().getId() != null) {
			params.add(filtro.getAssunto().getId());
			sql.append("and    e.assunto_id = :p").append(params.size()).append("\n");
		}
		sql.append("order by a.inicio, d.nome").append("\n");

		// criar a query
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql.toString())
				.setResultTransformer(new ResultTransformer() {

					private static final long serialVersionUID = 1L;

					@Override
					public List transformList(List collection) {
						return new ArrayList<>(new HashSet<>((List<AgendaDto>) collection));
					}

					@Override
					public Object transformTuple(Object[] tuple, String[] aliases) {
						List<String> campos = Arrays.asList(aliases);

						return new AgendaDto((Integer) tuple[campos.indexOf("id")],

								(String) tuple[campos.indexOf("title")],
								UtilitarioData.getInstance()
										.sqlTimestampToCalendar((Timestamp) tuple[campos.indexOf("startF")]),
								UtilitarioData.getInstance()
										.sqlTimestampToCalendar((Timestamp) tuple[campos.indexOf("endF")]),
								new String[] {}, (Integer) tuple[campos.indexOf("metodoId")],
								(String) tuple[campos.indexOf("metodoNome")],
								(Integer) tuple[campos.indexOf("pessoaId")],
								(String) tuple[campos.indexOf("pessoaNome")],
								(String) tuple[campos.indexOf("detalhamento")]);
					}

				});

		// inserir os parametros
		for (int i = 1; i <= params.size(); i++) {
			query.setParameter(String.format("p%d", i), params.get(i - 1));
		}

		// definir a pagina a ser consultada
		filtro.configuraPaginacao(query);

		// executar a consulta
		result = query.list();

		// retornar
		return result;
	}

}