package br.gov.df.emater.aterwebsrv.importador.sisater;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ater.BaciaHidrograficaDao;
import br.gov.df.emater.aterwebsrv.dao.ater.ComunidadeBaciaHidrograficaDao;
import br.gov.df.emater.aterwebsrv.dao.ater.ComunidadeDao;
import br.gov.df.emater.aterwebsrv.importador.apoio.ConexaoFirebird.DbSater;
import br.gov.df.emater.aterwebsrv.importador.apoio.ImpUtil;
import br.gov.df.emater.aterwebsrv.modelo.ater.BaciaHidrografica;
import br.gov.df.emater.aterwebsrv.modelo.ater.Comunidade;
import br.gov.df.emater.aterwebsrv.modelo.ater.ComunidadeBaciaHidrografica;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Municipio;

@Service
public class SisaterComunidadeCmd extends _Comando {

	private static final String SQL = "SELECT * FROM COM00 ORDER BY COMUNIDADE";

	@Autowired
	private BaciaHidrograficaDao baciaHidrograficaDao;

	private DbSater base;

	@Autowired
	private ComunidadeBaciaHidrograficaDao comunidadeBaciaHidrograficaDao;

	// private Municipio brasilia;

	// private Municipio cristalina;

	// private Municipio formosa;

	// private Municipio padreBernardo;

	@Autowired
	private ComunidadeDao comunidadeDao;

	private Connection con;

	private ImpUtil impUtil;

	private Municipio[] municipioAtendimentoList;

	private UnidadeOrganizacional unidadeOrganizacional;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {

		con = (Connection) contexto.get("conexao");
		unidadeOrganizacional = (UnidadeOrganizacional) contexto.get("unidadeOrganizacional");
		base = (DbSater) contexto.get("base");
		impUtil = (ImpUtil) contexto.get("impUtil");
		// brasilia = (Municipio) contexto.get("brasilia");
		// cristalina = (Municipio) contexto.get("cristalina");
		// formosa = (Municipio) contexto.get("formosa");
		// padreBernardo = (Municipio) contexto.get("padreBernardo");
		municipioAtendimentoList = (Municipio[]) contexto.get("municipioAtendimentoList");

		PlatformTransactionManager transactionManager = (PlatformTransactionManager) contexto.get("transactionManager");
		DefaultTransactionDefinition transactionDefinition = (DefaultTransactionDefinition) contexto.get("transactionDefinition");

		try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(SQL);) {
			int cont = 0;
			while (rs.next()) {
				TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
				try {
					Comunidade comunidade = null;
					String comunidadeNome = impUtil.deNomeComunidadeSisaterParaAterWeb(rs.getString("COMUNIDADE"));
					if (comunidadeNome == null || comunidadeNome.length() == 0) {
						throw new BoException("Nome da comunidade não informada");
					}
					List<Comunidade> comunidadeList = comunidadeDao.findByNomeOrderByNomeAsc(comunidadeNome);
					if (comunidadeList == null || comunidadeList.isEmpty()) {
						comunidade = novaComunidade(comunidadeNome, rs);
					} else {
						for (Comunidade c : comunidadeList) {
							if (c.getUnidadeOrganizacional().getId().equals(unidadeOrganizacional.getId())) {
								if (comunidade != null) {
									throw new BoException("Comunidade duplicada - %s", comunidade.getNome());
								}
								comunidade = c;
							}
						}
						if (comunidade == null) {
							comunidade = novaComunidade(comunidadeNome, rs);
						}
					}

					BaciaHidrografica baciaHidrografica = null;
					String baciaHidrograficaNome = impUtil.deNomeBaciaHidrograficaSisaterParaAterWeb(rs.getString("BACIA"));
					List<BaciaHidrografica> baciaHidrograficaList = baciaHidrograficaDao.findByNomeOrderByNomeAsc(baciaHidrograficaNome);
					if (baciaHidrograficaList == null || baciaHidrograficaList.isEmpty()) {
						baciaHidrografica = novaBaciaHidrografica(baciaHidrograficaNome);
					} else {
						for (BaciaHidrografica b : baciaHidrograficaList) {
							if (baciaHidrografica != null) {
								throw new BoException("Bacia Hidrografica duplicada - %s", baciaHidrografica.getNome());
							}
							baciaHidrografica = b;
						}
					}

					if (comunidade == null || baciaHidrografica == null) {
						throw new BoException("Comunidade ou Bacia Hidrográfica não localizados %s - %s", comunidade, baciaHidrografica);
					}

					ComunidadeBaciaHidrografica comunidadeBaciaHidrografica = null;
					List<ComunidadeBaciaHidrografica> comunidadeBaciaHidrograficaList = comunidadeBaciaHidrograficaDao.findByComunidadeAndBaciaHidrografica(comunidade, baciaHidrografica);
					if (comunidadeBaciaHidrograficaList == null || comunidadeBaciaHidrograficaList.isEmpty()) {
						comunidadeBaciaHidrografica = novaComunidadeBaciaHidrografica(comunidade, baciaHidrografica);
					} else {
						for (ComunidadeBaciaHidrografica cb : comunidadeBaciaHidrograficaList) {
							if (comunidadeBaciaHidrografica != null) {
								throw new BoException("Comunidade Bacia Hidrografica duplicada - %s, %s", comunidadeBaciaHidrografica.getComunidade().getNome(), comunidadeBaciaHidrografica.getBaciaHidrografica().getNome());
							}
							comunidadeBaciaHidrografica = cb;
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
				logger.debug(String.format("[%s] importado %d comunidades", base.name(), cont));
			}
		}
		return false;
	}

	private BaciaHidrografica novaBaciaHidrografica(String baciaHidrograficaNome) throws SQLException {
		BaciaHidrografica result = new BaciaHidrografica();
		result.setNome(baciaHidrograficaNome);
		result.setChaveSisater(impUtil.chaveBaciaHidrografica(base, baciaHidrograficaNome));
		return baciaHidrograficaDao.save(result);
	}

	private Comunidade novaComunidade(String comunidadeNome, ResultSet rs) throws SQLException, BoException {
		Comunidade result = new Comunidade();
		result.setUnidadeOrganizacional(unidadeOrganizacional);
		result.setNome(comunidadeNome);
		result.setAssentamento(rs.getString("TIPO").equals("Comunidade") ? Confirmacao.N : Confirmacao.S);
		result.setChaveSisater(impUtil.chaveComunidade(base, rs.getString("IDUND"), rs.getString("IDCOM")));
		result.setCidade(impUtil.deNomeCidadeComunidadeSisaterParaAterWeb(rs.getString("REGIAO"), base, municipioAtendimentoList));
		return comunidadeDao.save(result);
	}

	private ComunidadeBaciaHidrografica novaComunidadeBaciaHidrografica(Comunidade comunidade, BaciaHidrografica baciaHidrografica) throws SQLException {
		ComunidadeBaciaHidrografica result = new ComunidadeBaciaHidrografica();
		result.setComunidade(comunidade);
		result.setBaciaHidrografica(baciaHidrografica);
		return comunidadeBaciaHidrograficaDao.save(result);
	}

}