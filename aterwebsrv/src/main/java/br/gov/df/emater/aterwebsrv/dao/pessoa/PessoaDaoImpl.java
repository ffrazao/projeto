package br.gov.df.emater.aterwebsrv.dao.pessoa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaGenero;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaGeracao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaSituacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaTipo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PublicoAlvoCategoria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PublicoAlvoSegmento;
import br.gov.df.emater.aterwebsrv.modelo.dto.PessoaCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.dto.TagDto;

public class PessoaDaoImpl implements PessoaDaoCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Object[]> filtrar(PessoaCadFiltroDto filtro) {
		// objetos de trabalho
		List<Object[]> result = null;
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql, sqlTemp;

		// construção do sql
		sql = new StringBuilder();
		sql.append("select distinct p.id").append("\n");
		sql.append("     , p.nome").append("\n");
		sql.append("     , p.apelidoSigla").append("\n");
		sql.append("     , p.pessoaTipo").append("\n");
		sql.append("     , p.cpf").append("\n");
		sql.append("     , p.cnpj").append("\n");
		sql.append("     , p.publicoAlvoConfirmacao").append("\n");
		sql.append("     , perfil.localDiretorioWeb").append("\n");
		sql.append("     , p.nascimento").append("\n");
		sql.append("     , p.genero").append("\n");
		sql.append("     , alvo.id").append("\n");
		sql.append("from Pessoa p").append("\n");
		sql.append("left join p.publicoAlvo alvo").append("\n");
		sql.append("left join p.perfilArquivo perfil").append("\n");
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
		if (!CollectionUtils.isEmpty(filtro.getNomes())) {
			sql.append("and (").append("\n");
			sqlTemp = new StringBuilder();
			for (TagDto nome : filtro.getNomes()) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append(" or ");
				}
				String n = nome.getText().replaceAll("\\s", "%"); 
				params.add(String.format("%%%s%%", n));
				sqlTemp.append(" (p.nome like ?").append(params.size());
				params.add(String.format("%%%s%%", n));
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
		if (!StringUtils.isEmpty(filtro.getPublicoAlvoPropriedadeUtilizacaoEspacoRural())) {
			// TODO Este campo nao foi implementado no BD - resolver
			// params.add(filtro.getPublicoAlvoPropriedadeUtilizacaoEspacoRural().getId());
			// sql.append("and paSetor.setor.id =
			// ?").append(params.size()).append("\n");
		}
		sql.append("order by p.nome, p.apelidoSigla").append("\n");

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

	private boolean filtrarPublicoAlvo(PessoaCadFiltroDto filtro) {
		return (!CollectionUtils.isEmpty(filtro.getPublicoAlvoSegmento()) && (PublicoAlvoSegmento.values().length != (filtro.getPublicoAlvoSegmento().size()))) || (!CollectionUtils.isEmpty(filtro.getPublicoAlvoCategoria()) && (PublicoAlvoCategoria.values().length != (filtro.getPublicoAlvoCategoria().size())))
				|| (filtro.getPublicoAlvoSetor() != null) || (filtro.getPublicoAlvoPropriedadeUtilizacaoEspacoRural() != null) || (!CollectionUtils.isEmpty(filtro.getEmpresaList())) || (!CollectionUtils.isEmpty(filtro.getUnidadeOrganizacionalList())) || (!CollectionUtils.isEmpty(filtro.getComunidadeList()));
	}

}