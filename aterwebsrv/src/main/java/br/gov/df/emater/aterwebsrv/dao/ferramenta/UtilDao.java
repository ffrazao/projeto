package br.gov.df.emater.aterwebsrv.dao.ferramenta;

import java.util.List;
import java.util.Map;

import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificacao;

public interface UtilDao {

	/**
	 * M�todo gen�rico para retorno de entidades de dom�nio do sistema
	 * 
	 * @param entidade
	 *            nome da entidade a ser chamada
	 * @param nomeChavePrimaria
	 *            nome da chave prim�ria da entidade
	 * @param valorChavePrimaria
	 *            valor da chave prim�ria da entidade
	 * @return a rela��o das entidades
	 */
	List<?> getDominio(String entidade, String nomeChavePrimaria, String valorChavePrimaria, String order);

	/**
	 * Método genérico para retorno de Enumerações do sistema (enum)
	 * 
	 * @param enumeracao
	 *            nome da Enumeração a ser chamada
	 */
	List<Map<String, Object>> getEnumeracao(String enumeracao) throws Exception;

	Map<String, Object> ipaBemClassificacaoDetalhes(BemClassificacao bemClassificacao);

}
