package br.gov.df.emater.aterwebsrv.importador.sisater;

import java.security.Principal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ater.PropriedadeRuralDao;
import br.gov.df.emater.aterwebsrv.dao.ater.PublicoAlvoDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.BemClassificacaoDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.BemDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.FormaProducaoItemDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.FormaProducaoValorDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoProprietarioDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.UnidadeMedidaDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaDao;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.importador.apoio.ConexaoFirebird.DbSater;
import br.gov.df.emater.aterwebsrv.importador.apoio.ImpUtil;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificado;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificacao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.FormaProducaoItem;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.FormaProducaoValor;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoProprietario;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoComposicao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.UnidadeMedida;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.seguranca.UserAuthentication;

@Service
public class SisaterIndiceProducaoCmd extends _Comando {

	private class CheckProducaoList {

		private Integer bemClassificacaoApiculturaAnimal;

		private Integer bemClassificacaoCorteAvicultura;

		private Integer bemClassificacaoCorteBovinocultura;

		private Integer bemClassificacaoCorteCoturnicultura;

		private Integer bemClassificacaoLeiteBovinocultura;

		private List<BemClassificacao> bemClassificacaoList = new ArrayList<BemClassificacao>();

		private Integer bemClassificacaoPosturaAvicultura;

		private Integer bemClassificacaoPosturaCoturnicultura;

		private List<BemClassificado> bemClassificadoList = new ArrayList<BemClassificado>();

		private List<FormaProducaoItem> formaProducaoItemList = new ArrayList<FormaProducaoItem>();

		private List<FormaProducaoValor> formaProducaoValorList = new ArrayList<FormaProducaoValor>();

		private int geral;

		private String nomeTabela;

		private OrigemList tabela;

		private List<UnidadeMedida> unidadeMedidaList = new ArrayList<UnidadeMedida>();

		private void bemClassificacaoAtualizaUnidadeMedida(BemClassificacao bemClassificacao, String nomeUnidadeMedida) throws BoException {
			UnidadeMedida unidadeMedida = unidadeMedidaPegar(nomeUnidadeMedida);
			if (bemClassificacao.getUnidadeMedida() == null && unidadeMedida != null) {
				bemClassificacao.setUnidadeMedida(unidadeMedida);
				bemClassificacao = bemClassificacaoDao.save(bemClassificacao);
			} else if (unidadeMedida != null && !unidadeMedida.getId().equals(bemClassificacao.getUnidadeMedida().getId())) {
				logger.error(String.format("Unidade de Medida duplicada [%s, já tem %s, tentando inserir %s]", bemClassificacao.getNome(), bemClassificacao.getUnidadeMedida().getNome(), unidadeMedida.getNome()));
				// throw new BoException("Unidade de Medida duplicada [%s, já
				// tem %s, tentando inserir %s]", bemClassificacao.getNome(),
				// bemClassificacao.getUnidadeMedida().getNome(),
				// unidadeMedida.getNome());
			}
		}

		private BemClassificacao bemClassificacaoPegar(String nome, String nomePai) throws BoException {
			if (nome == null || nome.trim().length() == 0) {
				return null;
			}
			for (BemClassificacao bemClassificacao : bemClassificacaoList) {
				if (bemClassificacao.getNome().equalsIgnoreCase(nome) && bemClassificacao.getBemClassificacao() != null && bemClassificacao.getBemClassificacao().getNome().equalsIgnoreCase(nomePai)) {
					return bemClassificacao;
				}
			}
			List<BemClassificacao> result = bemClassificacaoDao.findByNomeLikeIgnoreCase(nome);
			if (result == null || result.isEmpty()) {
				throw new BoException("Bem Classificação não encontrado [%s, %s]", nome, nomePai);
			}
			BemClassificacao bemClassificacao = null;
			for (BemClassificacao b : result) {
				if (b.getBemClassificacao().getNome().equalsIgnoreCase(nomePai)) {
					bemClassificacao = b;
					break;
				}
			}
			if (bemClassificacao != null) {
				bemClassificacaoList.add(bemClassificacao);
			} else {
				throw new BoException("Bem Classificação não identificado [%s, %s]", nome, nomePai);
			}
			return bemClassificacao;
		}

		private BemClassificado bemClassificadoPegar(String nome, BemClassificacao bemClassificacao) throws BoException {
			if (nome == null || nome.trim().length() == 0 || bemClassificacao == null) {
				throw new BoException("Bem não identificado [%s, %s]", nome, bemClassificacao);
			}
			for (BemClassificado bemClassificado : bemClassificadoList) {
				if (bemClassificado.getNome().equalsIgnoreCase(nome)) {
					return bemClassificado;
				}
			}
			List<BemClassificado> result = bemDao.findByBemClassificacaoAndNomeLikeIgnoreCase(bemClassificacao, nome);
			if (result == null || result.isEmpty()) {
				BemClassificado bemClassificado = new BemClassificado();
				bemClassificado.setBemClassificacao(bemClassificacao);
				bemClassificado.setNome(nome);
				bemClassificado = bemDao.save(bemClassificado);
				bemClassificadoList.add(bemClassificado);
				return bemClassificado;
			} else if (result != null && result.size() != 1 && result.get(0) == null) {
				throw new BoException("Bem não identificado [%s, %s]", nome, bemClassificacao);
			}
			bemClassificadoList.add(result.get(0));
			return result.get(0);
		}

		private BemClassificado deParaBemClassificadoAgricolaFlores(ResultSet rs) throws BoException, SQLException {
			String cultura = rs.getString("CULTURA");
			String grupo = rs.getString("GRUPO");
			String subgrupo = rs.getString("SUBGRUPO");

			// pegar a classificação do bem
			if (subgrupo != null && (grupo == null || grupo.trim().length() == 0)) {
				grupo = "Ornamentais";
			}
			if ("frutiferas".equalsIgnoreCase(UtilitarioString.semAcento(grupo))) {
				subgrupo = grupo;
				grupo = "Agrícola";
			}
			BemClassificacao bemClassificacao = bemClassificacaoPegar(subgrupo, grupo);
			if (bemClassificacao == null) {
				throw new BoException("Não foi possível identificar a classificação do bem");
			}
			if ("Lichia".equalsIgnoreCase(cultura)) {
				bemClassificacao = bemClassificacaoPegar("Frutíferas", "Agrícola");
			}

			// captar a unidade de medida
			bemClassificacaoAtualizaUnidadeMedida(bemClassificacao, rs.getString("UNIDADE"));

			// pegar o bem
			BemClassificado bemClassificado = bemClassificadoPegar(cultura, bemClassificacao);
			if (bemClassificado == null) {
				throw new BoException("Não foi possível identificar o bem");
			}
			return bemClassificado;
		}

		private List<Producao> deParaBemAgricolaFloresProducaoList(ProducaoProprietario salvo, BemClassificado bemClassificado, ResultSet rs) throws BoException, SQLException {
			List<Producao> result = producaoListCriar(salvo);

			FormaProducaoValor sistema = formaProducaoValorPegar(formaProducaoItemPegar("Sistema"), rs.getString("SISTEMA"));
			FormaProducaoValor usoAgua = formaProducaoValorPegar(formaProducaoItemPegar("Uso d´água"), rs.getString("USOAGUA"));
			FormaProducaoValor protecao = formaProducaoValorPegar(formaProducaoItemPegar("Proteção/ Época/ Forma"), rs.getString("PROTECAO"));
			Producao pf = producaoCriarOuPegar(salvo, producaoComposicaoListCriar(sistema, usoAgua, protecao));

			if (geral == 0) {
				pf.setItemAValor(impUtil.captaBigDecimal(rs.getDouble("AREAESP")));
				pf.setItemBValor(impUtil.captaBigDecimal(rs.getDouble("PDCESP")));
				pf.setQuantidadeProdutores(rs.getInt("PRDESP"));
				pf.setDataConfirmacao(null);
			} else {
				pf.setItemAValor(impUtil.captaBigDecimal(rs.getDouble("AREA")));
				pf.setItemBValor(impUtil.captaBigDecimal(rs.getDouble("QUANTIDADE")));
				pf.setDataConfirmacao(impUtil.captaData(rs.getDate("DTIPA")));
				pf.setQuantidadeProdutores(null);
			}
			pf.setItemCValor(null);

			return producaoListColetar(result, pf, salvo, bemClassificado, rs);
		}

		private BemClassificado deParaBemClassificadoAnimal(ResultSet rs) throws BoException, SQLException {
			String cultura = rs.getString("CULTURA");

			// pegar a classificação do bem
			String[] classificacao = cultura.split(" ");
			BemClassificacao bemClassificacao = null;
			if (classificacao.length == 1) {
				bemClassificacao = bemClassificacaoPegar(cultura, "Animal");
			} else {
				bemClassificacao = bemClassificacaoPegar(classificacao[1], classificacao[0]);
			}
			if (bemClassificacao == null) {
				throw new BoException("Não foi possível identificar a classificação do bem");
			}

			// captar a unidade de medida
			bemClassificacaoAtualizaUnidadeMedida(bemClassificacao, rs.getString("UNIDADE"));

			// pegar o bem
			BemClassificado bemClassificado = bemClassificadoPegar(deParaNomeBemAnimal(cultura), bemClassificacao);
			if (bemClassificado == null) {
				throw new BoException("Não foi possível identificar o bem");
			}
			return bemClassificado;
		}

		private List<Producao> deParaBemAnimalProducaoList(ProducaoProprietario salvo, BemClassificado bemClassificado, ResultSet rs) throws BoException, SQLException {
			List<Producao> result = producaoListCriar(salvo);

			FormaProducaoValor sistema = formaProducaoValorPegar(formaProducaoItemPegar("Sistema"), rs.getString("SISTEMA"));
			FormaProducaoValor exploracao = formaProducaoValorPegar(formaProducaoItemPegar("Exploração"), rs.getString("EXPLORACAO"));
			Producao pf = producaoCriarOuPegar(salvo, producaoComposicaoListCriar(sistema, exploracao));

			if (geral == 0) {
				if (bemClassificado.getBemClassificacao().getId().equals(bemClassificacaoCorteAvicultura) || bemClassificado.getBemClassificacao().getId().equals(bemClassificacaoCorteBovinocultura) || bemClassificado.getBemClassificacao().getId().equals(bemClassificacaoCorteCoturnicultura)) {
					pf.setItemAValor(impUtil.captaBigDecimal(rs.getDouble("REBEX")));
					pf.setItemBValor(impUtil.captaBigDecimal(rs.getDouble("REND1EX")));
					pf.setItemCValor(impUtil.captaBigDecimal(rs.getDouble("MATEX")));
				} else if (bemClassificado.getBemClassificacao().getId().equals(bemClassificacaoLeiteBovinocultura)) {
					pf.setItemAValor(impUtil.captaBigDecimal(rs.getDouble("MATEX")));
					pf.setItemBValor(impUtil.captaBigDecimal(rs.getDouble("REND2EX")));
					pf.setItemCValor(impUtil.captaBigDecimal(rs.getDouble("REBEX")));
				} else if (bemClassificado.getBemClassificacao().getId().equals(bemClassificacaoPosturaAvicultura) || bemClassificado.getBemClassificacao().getId().equals(bemClassificacaoPosturaCoturnicultura)) {
					pf.setItemAValor(impUtil.captaBigDecimal(rs.getDouble("REBEX")));
					pf.setItemBValor(impUtil.captaBigDecimal(rs.getDouble("REND3EX")));
					pf.setItemCValor(null);
				} else if (bemClassificado.getBemClassificacao().getId().equals(bemClassificacaoApiculturaAnimal)) {
					pf.setItemAValor(impUtil.captaBigDecimal(rs.getDouble("REBEX")));
					pf.setItemBValor(impUtil.captaBigDecimal(rs.getDouble("REND4EX")));
					pf.setItemCValor(null);
				} else {
					pf.setItemAValor(impUtil.captaBigDecimal(rs.getDouble("REBEX")));
					// CARNE LEITE OVOS MEL REND1EX REND2EX REND3EX REND4EX
					pf.setItemBValor(impUtil.captaBigDecimal(rs.getDouble("REND1EX")));
					pf.setItemCValor(null);
				}
				pf.setQuantidadeProdutores(rs.getInt("CRIADOREX"));
			} else {
				if (bemClassificado.getBemClassificacao().getId().equals(bemClassificacaoCorteAvicultura) || bemClassificado.getBemClassificacao().getId().equals(bemClassificacaoCorteBovinocultura) || bemClassificado.getBemClassificacao().getId().equals(bemClassificacaoCorteCoturnicultura)) {
					pf.setItemAValor(impUtil.captaBigDecimal(rs.getDouble("REBANHO")));
					pf.setItemBValor(impUtil.captaBigDecimal(rs.getDouble("PROD1AC")));
					pf.setItemCValor(impUtil.captaBigDecimal(rs.getDouble("MATRIZES")));
				} else if (bemClassificado.getBemClassificacao().getId().equals(bemClassificacaoLeiteBovinocultura)) {
					pf.setItemAValor(impUtil.captaBigDecimal(rs.getDouble("MATRIZES")));
					pf.setItemBValor(impUtil.captaBigDecimal(rs.getDouble("PROD2AC")));
					pf.setItemCValor(impUtil.captaBigDecimal(rs.getDouble("REBANHO")));
				} else if (bemClassificado.getBemClassificacao().getId().equals(bemClassificacaoPosturaAvicultura) || bemClassificado.getBemClassificacao().getId().equals(bemClassificacaoPosturaCoturnicultura)) {
					pf.setItemAValor(impUtil.captaBigDecimal(rs.getDouble("REBANHO")));
					pf.setItemBValor(impUtil.captaBigDecimal(rs.getDouble("PROD3AC")));
					pf.setItemCValor(null);
				} else if (bemClassificado.getBemClassificacao().getId().equals(bemClassificacaoApiculturaAnimal)) {
					pf.setItemAValor(impUtil.captaBigDecimal(rs.getDouble("REBANHO")));
					pf.setItemBValor(impUtil.captaBigDecimal(rs.getDouble("PROD4AC")));
					pf.setItemCValor(null);
				} else {
					pf.setItemAValor(impUtil.captaBigDecimal(rs.getDouble("REBANHO")));
					// CARNE LEITE DIA LEITE ANO OVOS MEL PRODAC1 PROD5AC
					// PROD2AC PROD3AC PROD4AC
					pf.setItemBValor(impUtil.captaBigDecimal(rs.getDouble("PROD1AC")));
					pf.setItemCValor(null);
				}
				pf.setDataConfirmacao(impUtil.captaData(rs.getDate("DTIPA")));
			}

			return producaoListColetar(result, pf, salvo, bemClassificado, rs);
		}

		private BemClassificado deParaBemClassificadoNaoAgricola(ResultSet rs) throws BoException, SQLException {
			String projeto = rs.getString("PROJETO");
			String produto = rs.getString("PRODUTO");

			// pegar a classificação do bem
			BemClassificacao bemClassificacao = null;
			bemClassificacao = bemClassificacaoPegar(projeto, "Não-agrícola");
			if (bemClassificacao == null) {
				throw new BoException("Não foi possível identificar a classificação do bem");
			}
			// captar a unidade de medida
			bemClassificacaoAtualizaUnidadeMedida(bemClassificacao, rs.getString("UNIDADE"));

			// pegar o bem
			BemClassificado bemClassificado = bemClassificadoPegar(produto, bemClassificacao);
			if (bemClassificado == null) {
				throw new BoException("Não foi possível identificar o bem");
			}
			return bemClassificado;
		}

		private List<Producao> deParaBemNaoAgricolaProducaoList(ProducaoProprietario salvo, BemClassificado bemClassificado, ResultSet rs) throws BoException, SQLException {
			List<Producao> result = producaoListCriar(salvo);

			FormaProducaoValor condicao = formaProducaoValorPegar(formaProducaoItemPegar("Condição"), rs.getString("CONDICAO"));
			Producao pf = producaoCriarOuPegar(salvo, producaoComposicaoListCriar(condicao));

			if (geral == 0) {
				pf.setItemAValor(impUtil.captaBigDecimal(rs.getDouble("PRDESP")));
				pf.setItemBValor(null);
				pf.setQuantidadeProdutores(rs.getInt("PRDESP"));
			} else {
				pf.setItemAValor(impUtil.captaBigDecimal(rs.getDouble("QUANTIDADE")));
				pf.setItemBValor(null);
				pf.setDataConfirmacao(impUtil.captaData(rs.getDate("DTIPA")));
			}
			pf.setItemCValor(null);

			return producaoListColetar(result, pf, salvo, bemClassificado, rs);
		}

		private String deParaFormaProducaoValorNome(String nome) {
			if (nome == null) {
				return nome;
			}
			nome = nome.trim();
			switch (nome) {
			case "Agroecológico":
			case "Orgânico cert":
				return "Orgânico Certificado";
			case "Subsistência":
				return "Extensivo";
			default:
				return nome;
			}
		}

		private String deParaNomeBemAnimal(String nome) {
			if (nome.indexOf(" ") >= 0) {
				nome = nome.substring(0, nome.indexOf(" "));
			}
			switch (UtilitarioString.semAcento(nome.toLowerCase())) {
			case "apicultura":
				return "Abelha";
			case "avicultura":
				return "Galinha";
			case "bovinocultura":
				return "Gado";
			case "bubalinocultura":
				return "Bufalo";
			case "caprinocultura":
				return "Cabra";
			case "coturnicultura":
				return "Codorna";
			case "cunicultura":
				return "Coelho";
			case "estrutiocultura":
				return "Avestruz";
			case "ovinocultura":
				return "Ovelha";
			case "piscicultura":
				return "Peixe";
			case "suinocultura":
				return "Porco";
			default:
				return null;
			}
		}

		private FormaProducaoItem formaProducaoItemPegar(String nome) throws BoException {
			if (nome == null || nome.trim().length() == 0) {
				return null;
			}
			for (FormaProducaoItem formaProducaoItem : formaProducaoItemList) {
				if (formaProducaoItem.getNome().equalsIgnoreCase(nome)) {
					return formaProducaoItem;
				}
			}
			List<FormaProducaoItem> lista = formaProducaoItemDao.findByNomeLikeIgnoreCase(nome);
			if (lista == null || lista.isEmpty()) {
				return null;
			}
			if (lista.size() == 1) {
				formaProducaoItemList.add(lista.get(0));
				return lista.get(0);
			}
			throw new BoException("Forma Produção Item não cadastrado");
		}

		private FormaProducaoValor formaProducaoValorPegar(FormaProducaoItem formaProducaoItem, String nome) throws BoException {
			if (nome == null || nome.trim().length() == 0) {
				return null;
			}
			nome = deParaFormaProducaoValorNome(nome);
			for (FormaProducaoValor formaProducaoValor : formaProducaoValorList) {
				if (formaProducaoValor.getNome().equalsIgnoreCase(nome)) {
					return formaProducaoValor;
				}
			}
			List<FormaProducaoValor> lista = formaProducaoValorDao.findByFormaProducaoItemAndNomeLikeIgnoreCase(formaProducaoItem, nome);
			if (lista == null || lista.isEmpty()) {
				throw new BoException("Forma Produção Valor não cadastrado [%s]", nome);
			}
			if (lista.size() == 1) {
				formaProducaoValorList.add(lista.get(0));
				return lista.get(0);
			}
			throw new BoException("Forma Produção Valor não cadastrado");
		}

		void importar(DbSater base, Principal usuario, OrigemList tabela, int geral) throws Exception {

			this.tabela = tabela;

			this.geral = geral;

			this.nomeTabela = String.format("%s%d", tabela.name().substring(0, tabela.name().length() - 1), geral);

			impUtil.criarMarcaTabelaSisater(con, nomeTabela);

			try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(geral == 0 ? String.format(SQL, nomeTabela) : tabela.sql);) {
				int cont = 0;
				while (rs.next()) {
					TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
					try {
						ProducaoProprietario producaoProprietario = null;
						producaoProprietario = novoProducao(rs);

						// FIXME quebra-galho para teste. remover este código
						// if (cont > 50) {
						// break;
						// }

						em.detach(producaoProprietario);

						// salvar no MySQL e no Firebird
						producaoProprietario.setId((Integer) facadeBo.indiceProducaoSalvar(usuario, producaoProprietario).getResposta());

						if (geral == 0) {
							impUtil.chaveAterWebAtualizar(base, producaoProprietario.getId(), agora, nomeTabela, "IDUND = ? AND IDIPA = ? AND SAFRA = ?", rs.getString("IDUND"), rs.getInt("IDIPA"), rs.getString("SAFRA"));
						} else {
							if (tabela.equals(OrigemList.IPAN00)) {
								impUtil.chaveAterWebAtualizar(base, producaoProprietario.getId(), agora, nomeTabela, "IDUND = ? AND IDIPA = ? AND IDBEN = ?", rs.getString("IDUND"), rs.getInt("IDIPA"), rs.getString("IDBEN"));
							} else {
								impUtil.chaveAterWebAtualizar(base, producaoProprietario.getId(), agora, nomeTabela, "IDUND = ? AND IDIPA = ? AND IDBEN = ? AND IDPRP = ?", rs.getString("IDUND"), rs.getInt("IDIPA"), rs.getString("IDBEN"), rs.getString("IDPRP"));
							}
						}
						if (cont % 500 == 0) {
							long memo = Runtime.getRuntime().freeMemory();
							if (logger.isInfoEnabled()) {
								logger.info(String.format("memória atual [%d]", memo));
							}
							System.gc();
							if (logger.isInfoEnabled()) {
								memo -= Runtime.getRuntime().freeMemory();
								logger.info(String.format("memória atual [%d] foi liberado [%d]", Runtime.getRuntime().freeMemory(), memo));
							}
						}
						cont++;
						transactionManager.commit(transactionStatus);
					} catch (Exception e) {
						logger.error(e);
						e.printStackTrace();
						transactionManager.rollback(transactionStatus);
					}
				}
				if (logger.isDebugEnabled()) {
					logger.debug(String.format("[%s] importado %d indice de producao %s geral %b", base.name(), cont, nomeTabela, geral == 0));
				}
			}
		}

		void init() {
			try {
				bemClassificacaoApiculturaAnimal = bemClassificacaoPegar("Apicultura", "Animal").getId();
				bemClassificacaoCorteAvicultura = bemClassificacaoPegar("Corte", "Avicultura").getId();
				bemClassificacaoCorteBovinocultura = bemClassificacaoPegar("Corte", "Bovinocultura").getId();
				bemClassificacaoCorteCoturnicultura = bemClassificacaoPegar("Corte", "Coturnicultura").getId();
				bemClassificacaoLeiteBovinocultura = bemClassificacaoPegar("Leite", "Bovinocultura").getId();
				bemClassificacaoPosturaAvicultura = bemClassificacaoPegar("Postura", "Avicultura").getId();
				bemClassificacaoPosturaCoturnicultura = bemClassificacaoPegar("Postura", "Coturnicultura").getId();
			} catch (BoException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}

		private ProducaoProprietario novoProducao(ResultSet rs) throws SQLException, BoException {
			ProducaoProprietario result = new ProducaoProprietario();

			// info basicas
			result.setAno(rs.getInt("SAFRA"));
			result.setInclusaoData(agora);
			result.setInclusaoUsuario(ematerUsuario);
			result.setAlteracaoData(agora);
			result.setAlteracaoUsuario(ematerUsuario);

			// recuperar o bem
			switch (tabela) {
			case IPAA00:
			case IPAF00:
				result.setBemClassificado(deParaBemClassificadoAgricolaFlores(rs));
				break;
			case IPAP00:
				result.setBemClassificado(deParaBemClassificadoAnimal(rs));
				break;
			case IPAN00:
				result.setBemClassificado(deParaBemClassificadoNaoAgricola(rs));
				break;
			}

			// recuperar os IDs
			ProducaoProprietario salvo = null;
			if (geral == 0) {
				result.setUnidadeOrganizacional(impUtil.deParaUnidadeOrganizacional(emater, base.getSigla()));
				result.setPublicoAlvo(null);
				result.setPropriedadeRural(null);
				result.setChaveSisater(impUtil.chaveProducaoAgricolaGeral(base, rs.getString("IDUND"), rs.getInt("IDIPA"), rs.getString("SAFRA"), nomeTabela));
				salvo = producaoProprietarioDao.findOneByAnoAndBemClassificadoAndUnidadeOrganizacionalAndPublicoAlvoIsNullAndPropriedadeRuralIsNull(result.getAno(), result.getBemClassificado(), result.getUnidadeOrganizacional());
			} else {
				result.setUnidadeOrganizacional(null);
				result.setPublicoAlvo(publicoAlvoDao.findOneByPessoaId(rs.getInt("CHAVE_ATER_WEB_PESSOA")));
				if (result.getPublicoAlvo() == null) {
					result.setPublicoAlvo(publicoAlvoDao.findOneByPessoa(pessoaDao.findOneByChaveSisater(impUtil.chaveBeneficiario(base, rs.getString("IDUND"), rs.getString("IDBEN")))));
				}
				if (!OrigemList.IPAN00.equals(tabela)) {
					result.setPropriedadeRural(propriedadeRuralDao.findOne(rs.getInt("CHAVE_ATER_WEB_PROP")));
				} else {
					if (result.getPublicoAlvo() != null && result.getPublicoAlvo().getPublicoAlvoPropriedadeRuralList() != null && result.getPublicoAlvo().getPublicoAlvoPropriedadeRuralList().size() > 0) {
						result.setPropriedadeRural(result.getPublicoAlvo().getPublicoAlvoPropriedadeRuralList().get(0).getPropriedadeRural());
					}
				}
				if (result.getPropriedadeRural() == null) {
					result.setPropriedadeRural(propriedadeRuralDao.findOneByChaveSisater(impUtil.chavePropriedadeRural(base, rs.getString("IDUND"), rs.getString("IDPRP"))));
				}
				if (result.getPropriedadeRural() == null) {
					logger.error(String.format("Propriedade Rural não identificada [%s][%s][%s][%s][%s]", rs.getString("IDUND"), rs.getString("IDIPA"), rs.getString("SAFRA"), rs.getString("IDBEN"), rs.getString("IDPRP"), rs.getString("DTIPA")));
					// throw new BoException("Propriedade Rural não
					// identificada");
				}
				result.setChaveSisater(impUtil.chaveProducaoAgricola(base, rs.getString("IDUND"), rs.getInt("IDIPA"), rs.getString("IDBEN"), tabela.equals(OrigemList.IPAN00) ? null : rs.getString("IDPRP"), nomeTabela));
				salvo = producaoProprietarioDao.findOneByAnoAndBemClassificadoAndPublicoAlvoAndPropriedadeRuralAndUnidadeOrganizacionalIsNull(result.getAno(), result.getBemClassificado(), result.getPublicoAlvo(), result.getPropriedadeRural());
			}
			if (salvo == null) {
				salvo = producaoProprietarioDao.findOneByChaveSisater(result.getChaveSisater());
			}
			if (salvo != null) {
				result.setId(salvo.getId());
				for (int i = salvo.getProducaoList().size() - 1; i >= 0; i--) {
					if (salvo.getProducaoList().get(i).getProducaoComposicaoList() == null) {
						salvo.getProducaoList().remove(i);
					}
				}
			}

			switch (tabela) {
			case IPAA00:
			case IPAF00:
				result.setProducaoList(deParaBemAgricolaFloresProducaoList(salvo, result.getBemClassificado(), rs));
				break;
			case IPAP00:
				result.setProducaoList(deParaBemAnimalProducaoList(salvo, result.getBemClassificado(), rs));
				break;
			case IPAN00:
				result.setProducaoList(deParaBemNaoAgricolaProducaoList(salvo, result.getBemClassificado(), rs));
				break;
			}

			if (salvo != null) {
				em.detach(salvo);
			}

			return result;
		}

		private Producao producaoClonar(Producao pfSalvo) {
			Producao result = new Producao();
			result.setAlteracaoData(pfSalvo.getAlteracaoData());
			result.setAlteracaoUsuario(new Usuario(pfSalvo.getAlteracaoUsuario() == null ? null : pfSalvo.getAlteracaoUsuario().getId()));
			result.setDataConfirmacao(pfSalvo.getDataConfirmacao());
			result.setId(pfSalvo.getId());
			result.setInclusaoData(pfSalvo.getInclusaoData());
			result.setInclusaoUsuario(new Usuario(pfSalvo.getInclusaoUsuario() == null ? null : pfSalvo.getInclusaoUsuario().getId()));
			result.setItemAValor(pfSalvo.getItemAValor());
			result.setItemBValor(pfSalvo.getItemBValor());
			result.setItemCValor(pfSalvo.getItemCValor());
			result.setQuantidadeProdutores(pfSalvo.getQuantidadeProdutores());
			result.setValorTotal(pfSalvo.getValorTotal());
			result.setValorUnitario(pfSalvo.getValorUnitario());
			List<ProducaoComposicao> producaoComposicaoList = null;
			for (ProducaoComposicao formaComposicao : pfSalvo.getProducaoComposicaoList()) {
				if (producaoComposicaoList == null) {
					producaoComposicaoList = new ArrayList<ProducaoComposicao>();
				}
				producaoComposicaoList.add(producaoComposicaoClonar(formaComposicao));
			}
			result.setProducaoComposicaoList(producaoComposicaoList);
			return result;
		}

		private ProducaoComposicao producaoComposicaoClonar(ProducaoComposicao formaComposicao) {
			ProducaoComposicao result = new ProducaoComposicao();
			result.setId(formaComposicao.getId());
			FormaProducaoValor pfv = new FormaProducaoValor(formaComposicao.getFormaProducaoValor().getId());
			pfv.setFormaProducaoItem(new FormaProducaoItem(formaComposicao.getFormaProducaoValor().getFormaProducaoItem().getId()));
			result.setFormaProducaoValor(pfv);
			result.setOrdem(formaComposicao.getOrdem());
			return result;
		}

		private boolean producaoFormaComposicaoIguais(List<ProducaoComposicao> aList, List<ProducaoComposicao> bList) {
			for (ProducaoComposicao a : aList) {
				boolean iguais = true;
				for (ProducaoComposicao b : bList) {
					if (!a.getFormaProducaoValor().getFormaProducaoItem().getId().equals(b.getFormaProducaoValor().getFormaProducaoItem().getId())) {
						iguais = false;
						break;
					}
				}
				if (iguais) {
					return true;
				}
			}
			return false;
		}

		private List<ProducaoComposicao> producaoComposicaoListCriar(FormaProducaoValor... formaProducaoValorList) throws BoException {
			if (formaProducaoValorList == null || formaProducaoValorList.length == 0) {
				throw new BoException("Lista de Valor da Forma Producao está nula");
			}
			List<ProducaoComposicao> result = null;
			int cont = 0;
			for (FormaProducaoValor formaProducaoValor : formaProducaoValorList) {
				if (result == null) {
					result = new ArrayList<ProducaoComposicao>();
				}
				ProducaoComposicao producaoFormaComposicao = new ProducaoComposicao();
				producaoFormaComposicao.setFormaProducaoValor(formaProducaoValor);
				producaoFormaComposicao.setOrdem(++cont);
				result.add(producaoFormaComposicao);
			}
			return result;
		}

		private Producao producaoCriarOuPegar(ProducaoProprietario salvo, List<ProducaoComposicao> producaoFormaComposicaoList) throws BoException {
			if (producaoFormaComposicaoList == null) {
				throw new BoException("Composição da Forma de Produção nula");
			}
			Producao result = new Producao();
			result.setProducaoComposicaoList(producaoFormaComposicaoList);
			if (salvo != null && salvo.getProducaoList() != null) {
				for (Producao pfSalvo : salvo.getProducaoList()) {
					if (producaoFormaComposicaoIguais(result.getProducaoComposicaoList(), pfSalvo.getProducaoComposicaoList())) {
						result = producaoClonar(pfSalvo);
						return result;
					}
				}
			}
			return result;
		}

		private List<Producao> producaoListColetar(List<Producao> result, Producao pf, ProducaoProprietario salvo, BemClassificado bemClassificado, ResultSet rs) throws BoException, SQLException {
			for (Producao producaoForma : result) {
				if (producaoFormaComposicaoIguais(producaoForma.getProducaoComposicaoList(), pf.getProducaoComposicaoList())) {
					return result;
				}
			}
			result.add(pf);
			return result;
		}

		private List<Producao> producaoListCriar(ProducaoProprietario salvo) throws BoException, SQLException {
			List<Producao> result = new ArrayList<Producao>();
			if (salvo != null) {
				for (Producao producao : salvo.getProducaoList()) {
					result.add(producaoClonar(producao));
				}
			}
			return result;
		}

		private UnidadeMedida unidadeMedidaPegar(String nome) throws BoException {
			if (nome == null || nome.trim().length() == 0) {
				return null;
			}
			for (UnidadeMedida unidadeMedida : unidadeMedidaList) {
				if (unidadeMedida.getNome().equalsIgnoreCase(nome)) {
					return unidadeMedida;
				}
			}
			List<UnidadeMedida> lista = unidadeMedidaDao.findByNomeLikeIgnoreCase(nome);
			if (lista == null || lista.isEmpty()) {
				throw new BoException("Unidade de Medida não cadastrada");
				// logger.error(String.format("Unidade de Medida não
				// cadastrada"));
				// return null;
			}
			if (lista.size() == 1) {
				unidadeMedidaList.add(lista.get(0));
				return lista.get(0);
			}
			throw new BoException("Unidade de Medida não identificada");
		}
	}

	private static enum OrigemList {
		IPAA00("SELECT D.IDUND, D.IDIPA, D3.SAFRA, D.IDBEN, D.IDPRP, D.DTIPA, CULTURA, GRUPO, SUBGRUPO, CULTURA, SISTEMA, PROTECAO, USOAGUA, D.AREA, D1.CHAVE_ATER_WEB AS CHAVE_ATER_WEB_PESSOA, D2.CHAVE_ATER_WEB AS CHAVE_ATER_WEB_PROP, D3.QUANTIDADE, D3.UNIDADE, D.AREA * D3.QUANTIDADE AS PRODUTIVIDADE, D3.PDCUND FROM IPAA01 D, BENEF00 D1, PROP00 D2, IPAA00 D3 WHERE (D.IDBEN = D1.IDBEN) AND (D.IDPRP = D2.IDPRP) AND (D.IDIPA = D3.IDIPA) ORDER BY 1, 2, 3, 4"), IPAF00(
				"SELECT D.IDUND, D.IDIPA, D3.SAFRA, D.IDBEN, D.IDPRP, D.DTIPA, CULTURA, GRUPO, SUBGRUPO, CULTURA, SISTEMA, PROTECAO, USOAGUA, D.AREA, D1.CHAVE_ATER_WEB AS CHAVE_ATER_WEB_PESSOA, D2.CHAVE_ATER_WEB AS CHAVE_ATER_WEB_PROP, D3.QUANTIDADE, D3.UNIDADE, D.AREA * D3.QUANTIDADE AS PRODUTIVIDADE, D3.PDCUND FROM IPAF01 D, BENEF00 D1, PROP00 D2, IPAF00 D3 WHERE (D.IDBEN = D1.IDBEN) AND (D.IDPRP = D2.IDPRP) AND (D.IDIPA = D3.IDIPA) ORDER BY 1, 2, 3, 4"), IPAN00(
						"SELECT D.IDUND, D.IDIPA, D2.SAFRA, D.IDBEN, \'\' AS IDPRP, D.DTIPA, PROJETO, CONDICAO, PRODUTO, D.QUANTIDADE, D1.CHAVE_ATER_WEB AS CHAVE_ATER_WEB_PESSOA, D2.UNIDADE FROM IPAN01 D, BENEF00 D1, IPAN00 D2 WHERE (D.IDBEN = D1.IDBEN) AND (D.IDIPA = D2.IDIPA) ORDER BY 1, 2, 3, 4"), IPAP00(
								"SELECT D.IDUND, D.IDIPA, D3.SAFRA, D.IDBEN, D.IDPRP, D.DTIPA, CULTURA, SISTEMA, EXPLORACAO, D.PROD1AC, D.PROD2AC, D.PROD3AC, D.PROD4AC, D.PROD5AC, D.PROD6AC, D.REBANHO, D.MATRIZES, D1.CHAVE_ATER_WEB AS CHAVE_ATER_WEB_PESSOA, D2.CHAVE_ATER_WEB AS CHAVE_ATER_WEB_PROP, D2.SISTEMA, D3.UNIDADE FROM IPAP01 D, BENEF00 D1, PROP00 D2, IPAP00 D3 WHERE (D.IDBEN = D1.IDBEN) AND (D.IDPRP = D2.IDPRP) AND (D.IDIPA = D3.IDIPA) ORDER BY 1, 2, 3, 4");

		String sql;

		OrigemList(String sql) {
			this.sql = sql;
		}
	}

	private static final String SQL = "SELECT * FROM %s";

	private Calendar agora;

	private DbSater base;

	@Autowired
	private BemClassificacaoDao bemClassificacaoDao;

	@Autowired
	private BemDao bemDao;

	private CheckProducaoList checkProducaoList = new CheckProducaoList();

	private Connection con;

	@Autowired
	private EntityManager em;

	private PessoaJuridica emater;

	private Usuario ematerUsuario;

	private FacadeBo facadeBo;

	@Autowired
	private FormaProducaoItemDao formaProducaoItemDao;

	@Autowired
	private FormaProducaoValorDao formaProducaoValorDao;

	private ImpUtil impUtil;

	@Autowired
	private PessoaDao pessoaDao;

	@Autowired
	private ProducaoProprietarioDao producaoProprietarioDao;

	@Autowired
	private PropriedadeRuralDao propriedadeRuralDao;

	@Autowired
	private PublicoAlvoDao publicoAlvoDao;

	private DefaultTransactionDefinition transactionDefinition;

	private PlatformTransactionManager transactionManager;
	@Autowired
	private UnidadeMedidaDao unidadeMedidaDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		con = (Connection) contexto.get("conexao");
		facadeBo = (FacadeBo) contexto.get("facadeBo");
		base = (DbSater) contexto.get("base");
		impUtil = (ImpUtil) contexto.get("impUtil");

		emater = (PessoaJuridica) contexto.get("emater");
		ematerUsuario = ((UserAuthentication) contexto.getUsuario()).getDetails();

		agora = (Calendar) contexto.get("agora");

		transactionManager = (PlatformTransactionManager) contexto.get("transactionManager");
		transactionDefinition = (DefaultTransactionDefinition) contexto.get("transactionDefinition");

		checkProducaoList.init();

		for (OrigemList origem : OrigemList.values()) {
			// if (origem.compareTo(OrigemList.IPAP00) < 0) {
			// continue;
			// }
			for (int i = 0; i <= 1; i++) {
				checkProducaoList.importar(base, contexto.getUsuario(), origem, i);
			}
		}
		return false;
	}

}