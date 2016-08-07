package br.gov.df.emater.aterwebsrv.dao.projeto_credito_rural;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import br.gov.df.emater.aterwebsrv.dto.TagDto;
import br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural.ProjetoCreditoRuralCadFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural.ProjetoCreditoRuralCadListaDto;
import br.gov.df.emater.aterwebsrv.ferramenta.Util;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioData;
import br.gov.df.emater.aterwebsrv.modelo.dominio.FinanciamentoTipo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ProjetoCreditoRuralStatus;

public class ProjetoCreditoRuralDaoImpl implements ProjetoCreditoRuralDaoCustom {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<ProjetoCreditoRuralCadListaDto> filtrar(ProjetoCreditoRuralCadFiltroDto filtro) {
		// objetos de trabalho
		List<ProjetoCreditoRuralCadListaDto> result = null;
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql, sqlTemp;

		// construção do sql
		sql = new StringBuilder();
		sql.append("SELECT ").append("\n");
		sql.append("    ativ.id as atividadeId,").append("\n");
		sql.append("    cred.id as projetoCreditoRuralId,").append("\n");
		sql.append("    pes.nome as beneficiarioNome,").append("\n");
		sql.append("    ativ.inicio as inicio,").append("\n");
		sql.append("    ativ.codigo as atividadeCodigo,").append("\n");
		sql.append("    cred.status as status,").append("\n");
		sql.append("    agente.id as agenteFinanceiroId,").append("\n");
		sql.append("    agente_pessoa.nome as agenteFiananceiroNome,").append("\n");
		sql.append("    cred.agencia as agencia,").append("\n");
		sql.append("    linha.id as linhaCreditoId,").append("\n");
		sql.append("    linha.nome as linhaCreditoNome,").append("\n");
		sql.append("    cred.numero_cedula as numeroCedula,").append("\n");
		sql.append("	(SELECT COUNT(*) as total").append("\n");
		sql.append("	 FROM credito_rural.projeto_credito_rural_financiamento financCust").append("\n");
		sql.append("	 WHERE financCust.projeto_credito_rural_id = cred.id").append("\n");
		sql.append("     AND   financCust.tipo = 'C') as custeioTotal,").append("\n");
		sql.append("	(SELECT COUNT(*) as total").append("\n");
		sql.append("	 FROM credito_rural.projeto_credito_rural_financiamento financInvest").append("\n");
		sql.append("	 WHERE financInvest.projeto_credito_rural_id = cred.id").append("\n");
		sql.append("     AND   financInvest.tipo = 'I') as investimentoTotal").append("\n");
		sql.append("FROM").append("\n");
		sql.append("    atividade.atividade ativ").append("\n");
		sql.append("        JOIN").append("\n");
		sql.append("    credito_rural.projeto_credito_rural cred ON cred.atividade_id = ativ.id").append("\n");
		sql.append("        JOIN").append("\n");
		sql.append("    ater.publico_alvo pa ON cred.publico_alvo_id = pa.id").append("\n");
		sql.append("        JOIN").append("\n");
		sql.append("    pessoa.pessoa pes ON pes.id = pa.pessoa_id").append("\n");
		sql.append("        JOIN").append("\n");
		sql.append("    credito_rural.agente_financeiro agente ON cred.agente_financeiro_id = agente.id").append("\n");
		sql.append("        JOIN").append("\n");
		sql.append("    pessoa.pessoa agente_pessoa ON agente_pessoa.id = agente.pessoa_juridica_id").append("\n");
		sql.append("        JOIN").append("\n");
		sql.append("    credito_rural.linha_credito linha ON cred.linha_credito_id = linha.id").append("\n");
		sql.append("WHERE 1 = 1").append("\n");

		// BENEFICIARIO
		if (!CollectionUtils.isEmpty(filtro.getDemandanteList())) {
			sql.append("and (").append("\n");
			sqlTemp = new StringBuilder();
			for (TagDto nome : filtro.getDemandanteList()) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append(" or ");
				}
				String n = nome.getText().replaceAll("\\s", "%");
				params.add(String.format("%%%s%%", n));
				sqlTemp.append(" (pes.nome like ?");
				params.add(String.format("%%%s%%", n));
				sqlTemp.append(" or pes.apelido_sigla like ?").append(")").append("\n");
			}
			sql.append(sqlTemp);
			sql.append(" )").append("\n");
		}

		// CODIGO ATIVIDADE
		if (!CollectionUtils.isEmpty(filtro.getCodigoList())) {
			sql.append("and (").append("\n");
			sqlTemp = new StringBuilder();
			for (TagDto nome : filtro.getCodigoList()) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append(" or ");
				}
				String n = Util.codigoAtividadeFormatar(nome.getText());
				params.add(n);
				sqlTemp.append(" ativ.codigo = ?").append("\n");
			}
			sql.append(sqlTemp);
			sql.append(" )").append("\n");
		}

		// PERIODO
		if (filtro.getInicio() != null) {
			params.add(UtilitarioData.getInstance().ajustaInicioDia(filtro.getInicio()));
			sql.append("and ativ.inicio >= ?").append("\n");
		}
		if (filtro.getTermino() != null) {
			params.add(UtilitarioData.getInstance().ajustaFinalDia(filtro.getTermino()));
			sql.append("and ativ.inicio <= ?").append("\n");
		}

		// TECNICO EXECUTOR
		// if (!CollectionUtils.isEmpty(filtro.getExecutorList())) {
		// sql.append("and (").append("\n");
		// sqlTemp = new StringBuilder();
		// for (TagDto nome : filtro.getExecutorList()) {
		// if (sqlTemp.length() > 0) {
		// sqlTemp.append(" or ");
		// }
		// String n = nome.getText().replaceAll("\\s", "%");
		// params.add(String.format("%%%s%%", n));
		// sqlTemp.append(" (exec.pessoa.nome like ?");
		// params.add(String.format("%%%s%%", n));
		// sqlTemp.append(" or exec.pessoa.apelido_sigla like
		// ?").append(")").append("\n");
		// }
		// sql.append(sqlTemp);
		// sql.append(" )").append("\n");
		// }

		// FINALIDADE
		if (!CollectionUtils.isEmpty(filtro.getFinanciamentoTipoList())) {
			sql.append("AND	(SELECT COUNT(*) as total").append("\n");
			sql.append("	 FROM credito_rural.projeto_credito_rural_financiamento financ").append("\n");
			sql.append("	 WHERE financ.projeto_credito_rural_id = cred.id").append("\n");
			if (filtro.getFinanciamentoTipoList().size() == 2) {
				sql.append("	 AND tipo IN (\'C\', \'I\')) > 0").append("\n");
			} else {
				sql.append("	 AND tipo IN (\'").append(((FinanciamentoTipo) filtro.getFinanciamentoTipoList().toArray()[0]).name()).append("\')) > 0").append("\n");
			}
		}

		// STATUS
		if (!CollectionUtils.isEmpty(filtro.getStatusList()) && (ProjetoCreditoRuralStatus.values().length != (filtro.getStatusList().size()))) {
			sql.append("and (").append("\n");
			sqlTemp = new StringBuilder();
			for (ProjetoCreditoRuralStatus item : filtro.getStatusList()) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append(" or ");
				}
				params.add(item.name());
				sqlTemp.append(" cred.status = ?").append("\n");
			}
			sql.append(sqlTemp);
			sql.append(" )").append("\n");
		}

		// AGENTE_FINANCEIRO
		if (!CollectionUtils.isEmpty(filtro.getAgenteFinanceiroIdList())) {
			sql.append("and (").append("\n");
			sqlTemp = new StringBuilder();
			for (Integer item : filtro.getAgenteFinanceiroIdList()) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append(" or ");
				}
				params.add(item);
				sqlTemp.append(" agente.id = ?").append("\n");
			}
			sql.append(sqlTemp);
			sql.append(" )").append("\n");
		}

		// AGENTE_FINANCEIRO
		if (!CollectionUtils.isEmpty(filtro.getLinhaCreditoIdList())) {
			sql.append("and (").append("\n");
			sqlTemp = new StringBuilder();
			for (Integer item : filtro.getLinhaCreditoIdList()) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append(" or ");
				}
				params.add(item);
				sqlTemp.append(" linha.id = ?").append("\n");
			}
			sql.append(sqlTemp);
			sql.append(" )").append("\n");
		}
		sql.append("ORDER BY pes.nome , ativ.inicio DESC").append("\n");

		// criar a query
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql.toString()).setResultTransformer(Transformers.aliasToBean(ProjetoCreditoRuralCadListaDto.class));

		// inserir os parametros
		for (int i = 1; i <= params.size(); i++) {
			query.setParameter(i - 1, params.get(i - 1));
		}

		// definir a pagina a ser consultada
		filtro.configuraPaginacao(query);

		// executar a consulta
		result = query.list();

		// retornar
		return result;
	}

}