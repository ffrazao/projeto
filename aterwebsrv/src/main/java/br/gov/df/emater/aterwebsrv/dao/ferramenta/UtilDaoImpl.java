package br.gov.df.emater.aterwebsrv.dao.ferramenta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo.dominio.FormulaProduto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificacao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ItemNome;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.UnidadeMedida;

@Repository
public class UtilDaoImpl implements UtilDao {

	private static final Logger logger = Logger.getLogger(UtilDaoImpl.class);

	@Autowired
	private EntityManager entityManager;

	/**
	 * Método genérico para retorno de entidades de domínio do sistema
	 * 
	 * @param entidade
	 *            nome da entidade a ser chamada
	 * @param nomeChavePrimaria
	 *            nome da chave primária da entidade
	 * @param valorChavePrimaria
	 *            valor da chave primária da entidade
	 * @param order
	 *            definição da ordem dos dados
	 * @return a relação das entidades em formato JSon
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<?> getDominio(String entidade, String nomeChavePrimaria, String valorChavePrimaria, String order) {
		if (logger.isTraceEnabled()) {
			logger.trace(String.format("Recuperando domínio para Entidade[%s], NomeChavePrimaria[%s], ValorChavePrim�ria[%s]", entidade, nomeChavePrimaria, valorChavePrimaria));
		}

		StringBuilder sql = new StringBuilder();
		sql.append("from ").append(entidade).append(" this\n");
		if (nomeChavePrimaria != null && nomeChavePrimaria.trim().length() > 0) {
			if (valorChavePrimaria == null) {
				sql.append("where ").append(nomeChavePrimaria).append(" is null").append("\n");
			} else {
				sql.append("where ").append(nomeChavePrimaria).append(" = ?1").append("\n");
			}
		}
		if (order != null && order.trim().length() > 0) {
			sql.append("order by ").append(order).append("\n");
		}

		Query query = entityManager.createQuery(sql.toString());

		if (nomeChavePrimaria != null && nomeChavePrimaria.trim().length() > 0 && valorChavePrimaria != null) {
			try {
				query.setParameter("1", Integer.parseInt(valorChavePrimaria));
			} catch (NumberFormatException e) {
				query.setParameter("1", valorChavePrimaria);
			}
		}

		List<?> result = query.getResultList();

		if (result != null && result.get(0) != null) {
			if (result.get(0) instanceof InfoBasica) {
				List<Object> newResult = new ArrayList<Object>();
				for (Object info : result) {
					newResult.add(((InfoBasica) info).infoBasica());
				}
				result = newResult;
			}
		}

		return result;
	}

	/**
	 * Método genérico para retorno de Enumerações do sistema (enum)
	 * 
	 * @param enumeracao
	 *            nome da Enumeração a ser chamada
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Map<String, Object>> getEnumeracao(String enumeracao) throws Exception {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Class<?> c = null;
		try {
			c = Class.forName(String.format("br.gov.df.emater.aterwebsrv.modelo.dominio.%s", enumeracao));
		} catch (ClassNotFoundException e) {
			try {
				c = Class.forName(enumeracao);
			} catch (ClassNotFoundException e1) {
				return null;
			}
		}
		if (!c.isEnum()) {
			throw new ServiceException(String.format("%s não é uma enumeração válida!", enumeracao));
		}
		boolean emOrdem = false;
		for (Field campo : c.getDeclaredFields()) {
			if (campo.isEnumConstant()) {
				Map<String, Object> item = new HashMap<String, Object>();
				item.put("codigo", ((Enum<?>) campo.get(c)).name());

				if (campo.getDeclaringClass().getDeclaredFields() != null) {
					for (Field subCampo : campo.getDeclaringClass().getDeclaredFields()) {
						if (!subCampo.isEnumConstant() && !"ENUM$VALUES".equals(subCampo.getName())) {
							subCampo.setAccessible(true);
							item.put(subCampo.getName(), subCampo.get(campo.get(c)));
							if (!emOrdem && "ordem".equals(subCampo.getName())) {
								emOrdem = true;
							}
						}
					}
				}
				result.add(item);
			}
		}
		if (emOrdem) {
			Collections.sort(result, new Comparator<Map<String, Object>>() {
				@Override
				public int compare(Map<String, Object> o1, Map<String, Object> o2) {
					return ((Integer) o1.get("ordem")).compareTo(((Integer) o2.get("ordem")));
				}
			});
		}
		return result;
	}

	public Map<String, Object> ipaBemClassificacaoDetalhes(BemClassificacao bemClassificacao) {
		Map<String, Object> result = null;
		if (bemClassificacao != null) {
			UnidadeMedida unidadeMedida = null;
			FormulaProduto formulaProduto = null;
			ItemNome itemA = null;
			ItemNome itemB = null;
			ItemNome itemC = null;
			List<BemClassificacao> bemClassificacaoList = new ArrayList<BemClassificacao>();

			// percorrer a classificacao ate a raiz da arvore
			BemClassificacao b = bemClassificacao;
			do {
				bemClassificacaoList.add(b);
				if (unidadeMedida == null && b.getUnidadeMedida() != null) {
					unidadeMedida = b.getUnidadeMedida();
				}
				if (formulaProduto == null && b.getFormula() != null) {
					formulaProduto = b.getFormula();
				}
				if (itemA == null && b.getItemANome() != null) {
					itemA = b.getItemANome();
				}
				if (itemB == null && b.getItemBNome() != null) {
					itemB = b.getItemBNome();
				}
				if (itemC == null && b.getItemCNome() != null) {
					itemC = b.getItemCNome();
				}
				b = b.getBemClassificacao();
			} while (b != null);

			Collections.reverse(bemClassificacaoList);
			StringBuilder bemClassificacaoSb = new StringBuilder();
			for (BemClassificacao item : bemClassificacaoList) {
				if (bemClassificacaoSb.length() > 0) {
					bemClassificacaoSb.append("/");
				}
				bemClassificacaoSb.append(item.getNome());
			}
			if (result == null) {
				result = new HashMap<String, Object>();
			}
			result.put("unidadeMedida", unidadeMedida != null ? unidadeMedida : null);
			result.put("bemClassificacao", bemClassificacaoSb != null ? bemClassificacaoSb.toString() : null);
			result.put("formulaProduto", formulaProduto != null ? formulaProduto : null);
			result.put("itemA", itemA != null ? itemA : null);
			result.put("itemB", itemB != null ? itemB : null);
			result.put("itemC", itemC != null ? itemC : null);
		}
		return result;
	}

}