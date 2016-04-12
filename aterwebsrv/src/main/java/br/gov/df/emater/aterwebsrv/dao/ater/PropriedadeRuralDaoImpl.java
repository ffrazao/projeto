package br.gov.df.emater.aterwebsrv.dao.ater;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import br.gov.df.emater.aterwebsrv.modelo.ater.BaciaHidrografica;
import br.gov.df.emater.aterwebsrv.modelo.ater.Comunidade;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoPropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dto.FiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.dto.PropriedadeRuralCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Area;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Endereco;

public class PropriedadeRuralDaoImpl implements PropriedadeRuralDaoCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Object[]> filtrar(PropriedadeRuralCadFiltroDto filtro) {
		// objetos de trabalho
		List<Object[]> result = null;
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql, sqlTemp;

		// construção do sql
		sql = new StringBuilder();
		sql.append("select p.id").append("\n");
		sql.append("     , p.nome").append("\n");
		sql.append("     , p.endereco").append("\n");
		sql.append("     , p.comunidade").append("\n");
		sql.append("     , p.baciaHidrografica").append("\n");
		sql.append("     , p.areaTotal").append("\n");
		sql.append("     , vinculados").append("\n");
		sql.append("from PropriedadeRural p").append("\n");
		sql.append("left join p.publicoAlvoPropriedadeRuralList vinculados").append("\n");
		sql.append("where (1 = 1)").append("\n");

		if (!StringUtils.isEmpty(filtro.getPessoaVinculada())) {
			sql.append("and (").append("\n");
			sqlTemp = new StringBuilder();
			for (String nome : filtro.getPessoaVinculada().split(FiltroDto.SEPARADOR_CAMPO)) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append(" or ");
				}
				params.add(String.format("%%%s%%", nome.trim()));
				sqlTemp.append(" (vinculados.publicoAlvo.pessoa.nome like ?").append(params.size());
				params.add(String.format("%%%s%%", nome.trim()));
				sqlTemp.append(" or vinculados.publicoAlvo.pessoa.apelidoSigla like ?").append(params.size()).append(")").append("\n");
			}
			sql.append(sqlTemp);
			sql.append(" )").append("\n");
		}

		if (!StringUtils.isEmpty(filtro.getNomePropriedade())) {
			sql.append("and (").append("\n");
			sqlTemp = new StringBuilder();
			for (String nome : filtro.getNomePropriedade().split(FiltroDto.SEPARADOR_CAMPO)) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append(" or ");
				}
				params.add(String.format("%%%s%%", nome.trim()));
				sqlTemp.append(" p.nome like ?").append(params.size()).append("\n");
			}
			sql.append(sqlTemp);
			sql.append(" )").append("\n");
		}

		if (!CollectionUtils.isEmpty(filtro.getEmpresaList())) {
			params.add(filtro.getEmpresaList());
			sql.append("and p.comunidade.unidadeOrganizacional.pessoaJuridica in ?").append(params.size()).append("\n");
		}		
		if (!CollectionUtils.isEmpty(filtro.getUnidadeOrganizacionalList())) {
			params.add(filtro.getUnidadeOrganizacionalList());
			sql.append("and p.comunidade.unidadeOrganizacional in ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getComunidadeList())) {
			params.add(filtro.getComunidadeList());
			sql.append("and p.comunidade in ?").append(params.size()).append("\n");
		}
		
		if (!CollectionUtils.isEmpty(filtro.getPublicoAlvoList())) {
			params.add(filtro.getPublicoAlvoList());
			sql.append("and vinculados.publicoAlvo in ?").append(params.size()).append("\n");
		}		
		
		if (filtro.getAreaUtil() != null) {
			if (filtro.getAreaUtil().getAte() == null) {
				params.add(filtro.getAreaUtil().getDe());
				sql.append("and p.areaTotal > ?").append(params.size()).append("\n");
			} else if (filtro.getAreaUtil().getDe() == null) {
				params.add(filtro.getAreaUtil().getAte());
				sql.append("and p.areaTotal <= ?").append(params.size()).append("\n");
			} else {
				params.add(filtro.getAreaUtil().getDe());
				sql.append("and p.areaTotal > ?").append(params.size()).append("\n");
				params.add(filtro.getAreaUtil().getAte());
				sql.append("and p.areaTotal <= ?").append(params.size()).append("\n");
			}
		}

		if (!CollectionUtils.isEmpty(filtro.getOutorga()) && (Confirmacao.values().length != (filtro.getOutorga().size()))) {
			params.add(filtro.getOutorga());
			sql.append("and p.outorga in ?").append(params.size()).append("\n");
		}

		if (filtro.getSistemaProducao() != null && filtro.getSistemaProducao().getId() != null) {
			params.add(filtro.getSistemaProducao());
			sql.append("and p.sistemaProducao = ?").append(params.size()).append("\n");
		}

		if (filtro.getSituacaoFundiaria() != null) {
			params.add(filtro.getSituacaoFundiaria());
			sql.append("and p.situacaoFundiaria = ?").append(params.size()).append("\n");
		}
		
		if (!CollectionUtils.isEmpty(filtro.getAreaList())) {
			sqlTemp = new StringBuilder();
			for (Area area: filtro.getAreaList()) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append("or ").append("\n");
				}
				params.add(area.getPoligono());
				sqlTemp.append("within(p.endereco.entradaPrincipal, ?").append(params.size()).append(") = true").append("\n");
			}
			if (sqlTemp.length() > 0) {
				sql.append("and (").append(sqlTemp).append(")").append("\n");
			}
		}


		sql.append("order by p.nome").append("\n");

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
		return processaResultado(result);
	}

	@SuppressWarnings("unchecked")
	private List<Object[]> processaResultado(List<Object[]> lista) {
		if (lista == null || lista.size() == 0) {
			return lista;
		}
		List<Object[]> result = new ArrayList<Object[]>();

		Integer id = null;
		Object[] reg = null;
		int c = 0;
		for (Object lin : (List<Object[]>) lista) {
			Object[] l = (Object[]) lin;
			if (!((Integer) l[0]).equals(id)) {
				if (reg != null) {
					result.add(reg);					
				}
				reg = new Object[l.length];
				c = 0;
				reg[c] = l[c];
				reg[++c] = l[c];
				reg[++c] = Endereco.FORMATA((Endereco) l[c]);
				reg[++c] = (Comunidade) l[c] == null ? null : ((Comunidade) l[c]).getNome();
				reg[++c] = (BaciaHidrografica) l[c] == null ? null : ((BaciaHidrografica) l[c]).getNome();				
				reg[++c] = l[c];
				reg[++c] = new ArrayList<Object[]>();
				
				id = (Integer) reg[0]; 
			}
			PublicoAlvoPropriedadeRural papr = (PublicoAlvoPropriedadeRural) l[c];
			if (papr != null) {
				((List<Object[]>) reg[c]).add(new Object[] {papr.getPublicoAlvo().getPessoa().getNome(), papr.getPublicoAlvo().getId(), papr.getPublicoAlvo().getPessoa().getId()});
			}
		}
		if (reg != null) {
			result.add(reg);					
		}

		return result;
	}

}
