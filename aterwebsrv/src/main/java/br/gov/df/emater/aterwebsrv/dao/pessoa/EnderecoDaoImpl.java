package br.gov.df.emater.aterwebsrv.dao.pessoa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.util.StringUtils;

import br.gov.df.emater.aterwebsrv.modelo.pessoa.Endereco;

public class EnderecoDaoImpl implements EnderecoDaoCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Endereco> procurar(Endereco filtro) {
		// objetos de trabalho
		List<Endereco> result = null;
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql;

		// construção do sql
		sql = new StringBuilder();
		sql.append("from Endereco e").append("\n");
		sql.append("where (1 = 1)").append("\n");
		if (!StringUtils.isEmpty(filtro.getEstado())) {
			params.add(filtro.getEstado());
			sql.append("and e.estado = ?").append(params.size()).append("\n");
		}
		if (!StringUtils.isEmpty(filtro.getMunicipio())) {
			params.add(filtro.getMunicipio());
			sql.append("and e.municipio = ?").append(params.size()).append("\n");
		}
		if (!StringUtils.isEmpty(filtro.getCidade())) {
			params.add(filtro.getCidade());
			sql.append("and e.cidade = ?").append(params.size()).append("\n");
		}
		if (!StringUtils.isEmpty(filtro.getBairro())) {
			params.add(filtro.getBairro());
			sql.append("and e.bairro = ?").append(params.size()).append("\n");
		}
		if (!StringUtils.isEmpty(filtro.getLogradouro())) {
			params.add(filtro.getLogradouro());
//			sql.append("and convert(e.logradouro using utf8) like _utf8 ?").append(params.size()).append(" collate utf8_general_ci").append("\n");
			sql.append("and e.logradouro = ?").append(params.size()).append("\n");
		}
		if (!StringUtils.isEmpty(filtro.getComplemento())) {
			params.add(filtro.getComplemento());
			sql.append("and e.complemento = ?").append(params.size()).append("\n");
		}
		if (!StringUtils.isEmpty(filtro.getNumero())) {
			params.add(filtro.getNumero());
			sql.append("and e.numero = ?").append(params.size()).append("\n");
		}

		// criar a query
		TypedQuery<Endereco> query = em.createQuery(sql.toString(), Endereco.class);

		// inserir os parametros
		for (int i = 1; i <= params.size(); i++) {
			query.setParameter(i, params.get(i - 1));
		}

		// executar a consulta
		result = query.getResultList();

		// retornar
		return result;
	}

}