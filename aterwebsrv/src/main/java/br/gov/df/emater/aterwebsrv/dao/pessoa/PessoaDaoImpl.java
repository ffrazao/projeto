package br.gov.df.emater.aterwebsrv.dao.pessoa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaGeracao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaSituacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PublicoAlvoCategoria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PublicoAlvoSegmento;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Sexo;
import br.gov.df.emater.aterwebsrv.modelo.dto.FiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.dto.PessoaCadFiltroDto;

public class PessoaDaoImpl implements PessoaDaoCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	@Transactional(readOnly = true)
	public List<Object[]> filtrar(PessoaCadFiltroDto filtro) {
		// objetos de trabalho
		List<Object[]> result = null;
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql, sqlTemp;

		// construção do sql
		sql = new StringBuilder();
		sql.append("select p.id").append("\n");
		sql.append("     , p.nome").append("\n");
		sql.append("     , p.apelidoSigla").append("\n");
		sql.append("     , p.pessoaTipo").append("\n");
		sql.append("     , p.cpf").append("\n");
		sql.append("     , p.cnpj").append("\n");
		sql.append("     , p.publicoAlvoConfirmacao").append("\n");
		sql.append("     , a.md5").append("\n");
		sql.append("     , a.extensao").append("\n");
		sql.append("     , a.tipo").append("\n");
		sql.append("     , p.nascimento").append("\n");
		sql.append("from Pessoa p").append("\n");
		sql.append("left join p.arquivoList pa").append("\n");
		sql.append("left join pa.arquivo a").append("\n");
		if (filtrarPublicoAlvo(filtro)) {
			sql.append("left join p.publicoAlvo alvo").append("\n");
			if (filtro.getPublicoAlvoSetor() != null) {
				sql.append("left join alvo.publicoAlvoSetorList paSetor").append("\n");
			}
		}
		sql.append("where p.pessoaTipo in ('PF', 'PJ')").append("\n");
		sql.append("and   (pa is null or pa.perfil = 'S')").append("\n");
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
		if (filtro.getTipoPessoa() != null && !(Arrays.asList(0, 2).contains(filtro.getTipoPessoa().size()))) {
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
		if (filtro.getPessoaGenero() != null && (Sexo.values().length != (filtro.getPessoaGenero().size()))) {
			params.add(filtro.getPessoaGenero());
			sql.append("and p.sexo in ?").append(params.size()).append("\n");
		}
		if (filtro.getPessoaGeracao() != null && (PessoaGeracao.values().length != (filtro.getPessoaGeracao().size()))) {
			// TODO fazer o filtro por geracao
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
		if (filtro.getPessoaSituacao() != null && (PessoaSituacao.values().length != (filtro.getPessoaSituacao().size()))) {
			params.add(filtro.getPessoaSituacao());
			sql.append("and p.situacao in ?").append(params.size()).append("\n");
		}
		// filtro de publico alvo
		if (filtro.getPublicoAlvoSegmento() != null && (PublicoAlvoSegmento.values().length != (filtro.getPublicoAlvoSegmento().size()))) {
			params.add(filtro.getPublicoAlvoSegmento());
			sql.append("and alvo.segmento in ?").append(params.size()).append("\n");
		}
		if (filtro.getPublicoAlvoCategoria() != null && (PublicoAlvoCategoria.values().length != (filtro.getPublicoAlvoCategoria().size()))) {
			params.add(filtro.getPublicoAlvoCategoria());
			sql.append("and alvo.categoria in ?").append(params.size()).append("\n");
		}
		if (!StringUtils.isEmpty(filtro.getPublicoAlvoSetor())) {
			params.add(filtro.getPublicoAlvoSetor());
			sql.append("and paSetor.setor.id = ?").append(params.size()).append("\n");
		}
		if (!StringUtils.isEmpty(filtro.getPublicoAlvoPropriedadeUtilizacaoEspacoRural())) {
			// TODO Este campo nao foi implementado no BD - resolver
//			params.add(filtro.getPublicoAlvoPropriedadeUtilizacaoEspacoRural().getId());
//			sql.append("and paSetor.setor.id = ?").append(params.size()).append("\n");
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
		// TODO Auto-generated method stub
		return 	(filtro.getPublicoAlvoSegmento() != null && (PublicoAlvoSegmento.values().length != (filtro.getPublicoAlvoSegmento().size()))) 
				||  (filtro.getPublicoAlvoCategoria() != null && (PublicoAlvoCategoria.values().length != (filtro.getPublicoAlvoCategoria().size())))
				||  (filtro.getPublicoAlvoSetor() != null)
				||  (filtro.getPublicoAlvoPropriedadeUtilizacaoEspacoRural() != null);
	}

}