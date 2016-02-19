package br.gov.df.emater.aterwebsrv.dao.atividade;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import br.gov.df.emater.aterwebsrv.modelo.dto.AtividadeCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.dto.FiltroDto;

public class AtividadeDaoImpl implements AtividadeDaoCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	@Transactional(readOnly = true)
	public List<Object[]> filtrar(AtividadeCadFiltroDto filtro) {
		// objetos de trabalho
		List<Object[]> result = null;
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql, sqlTemp;

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
		sql.append("join a.assuntoList.assunto ass").append("\n");
		sql.append("where 1 = 1").append("\n");
		
		if (!StringUtils.isEmpty(filtro.getCodigo())) {
			sql.append("and (").append("\n");
			sqlTemp = new StringBuilder();
			for (String codigo : filtro.getCodigo().split(FiltroDto.SEPARADOR_CAMPO)) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append(" or ");
				}
				params.add(String.format("%s", codigo.toUpperCase().trim()));
				sqlTemp.append("a.codigo = ?").append(params.size());
			}
			sql.append(sqlTemp);
			sql.append(" )").append("\n");
		}
		
		if (filtro.getInicio() != null) {
			params.add(filtro.getInicio());
			sql.append("and a.inicio >= ?").append(params.size()).append("\n");
		}
		if (filtro.getTermino() != null) {
			params.add(filtro.getTermino());
			sql.append("and a.inicio <= ?").append(params.size()).append("\n");
		}
		if (filtro.getMetodo() != null) {
			params.add(filtro.getMetodo());
			sql.append("and a.metodo = ?").append(params.size()).append("\n");
		}
		if (filtro.getAssunto() != null) {
			params.add(filtro.getAssunto());
			sql.append("and ass.assunto = ?").append(params.size()).append("\n");			
		}
/*
		
		
		
		
		
		if (filtrarPublicoAlvo(filtro)) {
			if (filtro.getPublicoAlvoSetor() != null) {
				sql.append("left join alvo.publicoAlvoSetorList paSetor").append("\n");
			}
			if (!CollectionUtils.isEmpty(filtro.getEmpresaList()) || !CollectionUtils.isEmpty(filtro.getUnidadeOrganizacionalList()) || !CollectionUtils.isEmpty(filtro.getComunidadeList())) {
				sql.append("left join alvo.publicoAlvoPropriedadeRuralList paPropriedadeRural").append("\n");
			}
		}
		sql.append("where (1 = 1)").append("\n");
		if (filtrarPublicoAlvo(filtro)) {
			sql.append("and   p.publicoAlvoConfirmacao = 'S'").append("\n");
		}
		if (!StringUtils.isEmpty(filtro.getNome())) {
			sql.append("and (").append("\n");
			sqlTemp = new StringBuilder();
			for (String nome : filtro.getNome().split(FiltroDto.SEPARADOR_CAMPO)) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append(" or ");
				}
				params.add(String.format("%%%s%%", nome.trim()));
				sqlTemp.append(" (p.nome like ?").append(params.size());
				params.add(String.format("%%%s%%", nome.trim()));
				sqlTemp.append(" or p.apelidoSigla like ?").append(params.size()).append(")").append("\n");
			}
			sql.append(sqlTemp);
			sql.append(" )").append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getTipoPessoa()) && (PessoaTipo.values().length != (filtro.getTipoPessoa().size()))) {
			params.add(filtro.getTipoPessoa());
			sql.append("and p.pessoaTipo in ?").append(params.size()).append("\n");
		}
		if (!StringUtils.isEmpty(filtro.getCpf())) {
			params.add(UtilitarioString.formataCpf(filtro.getCpf()));
			sql.append("and p.cpf = ?").append(params.size()).append("\n");
		}
		if (!StringUtils.isEmpty(filtro.getCnpj())) {
			params.add(UtilitarioString.formataCnpj(filtro.getCnpj()));
			sql.append("and p.cnpj = ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getPessoaGenero()) && (PessoaGenero.values().length != (filtro.getPessoaGenero().size()))) {
			params.add(filtro.getPessoaGenero());
			sql.append("and p.pessoaGenero in ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getPessoaGeracao()) && (PessoaGeracao.values().length != (filtro.getPessoaGeracao().size()))) {
			sql.append("and (").append("\n");
			sqlTemp = new StringBuilder();
			for (PessoaGeracao pg : filtro.getPessoaGeracao()) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append(" or ");
				}
				params.add(pg.getIdadeInicio());
				sqlTemp.append("TIMESTAMPDIFF(YEAR, p.nascimento, CURDATE()) between ?").append(params.size());
				params.add(pg.getIdadeFim());
				sqlTemp.append(" and ?").append(params.size()).append("\n");
			}
			sql.append(sqlTemp).append("	)").append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getPessoaSituacao()) && (PessoaSituacao.values().length != (filtro.getPessoaSituacao().size()))) {
			params.add(filtro.getPessoaSituacao());
			sql.append("and p.situacao in ?").append(params.size()).append("\n");
		}
		// filtro de publico alvo
		if (!CollectionUtils.isEmpty(filtro.getPublicoAlvoSegmento()) && (PublicoAlvoSegmento.values().length != (filtro.getPublicoAlvoSegmento().size()))) {
			params.add(filtro.getPublicoAlvoSegmento());
			sql.append("and alvo.segmento in ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getPublicoAlvoCategoria()) && (PublicoAlvoCategoria.values().length != (filtro.getPublicoAlvoCategoria().size()))) {
			params.add(filtro.getPublicoAlvoCategoria());
			sql.append("and alvo.categoria in ?").append(params.size()).append("\n");
		}
		if (!StringUtils.isEmpty(filtro.getPublicoAlvoSetor())) {
			params.add(filtro.getPublicoAlvoSetor());
			sql.append("and paSetor.setor.id = ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getComunidadeList())) {
			params.add(filtro.getComunidadeList());
			sql.append("and paPropriedadeRural.comunidade in ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getUnidadeOrganizacionalList())) {
			params.add(filtro.getUnidadeOrganizacionalList());
			sql.append("and paPropriedadeRural.comunidade.unidadeOrganizacional in ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getEmpresaList())) {
			params.add(filtro.getEmpresaList());
			sql.append("and paPropriedadeRural.comunidade.unidadeOrganizacional.pessoaJuridica in ?").append(params.size()).append("\n");
		}
		
		
	*/	
		
		
		
		
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