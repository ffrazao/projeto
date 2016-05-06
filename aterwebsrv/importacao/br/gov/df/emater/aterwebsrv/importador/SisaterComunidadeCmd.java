package br.gov.df.emater.aterwebsrv.importador;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ater.BaciaHidrograficaDao;
import br.gov.df.emater.aterwebsrv.dao.ater.ComunidadeBaciaHidrograficaDao;
import br.gov.df.emater.aterwebsrv.dao.ater.ComunidadeDao;
import br.gov.df.emater.aterwebsrv.importador.apoio.ConexaoFirebird.DbSater;
import br.gov.df.emater.aterwebsrv.modelo.ater.BaciaHidrografica;
import br.gov.df.emater.aterwebsrv.modelo.ater.Comunidade;
import br.gov.df.emater.aterwebsrv.modelo.ater.ComunidadeBaciaHidrografica;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;

@Service
public class SisaterComunidadeCmd extends _Comando {

	private static final String SQL = "SELECT * FROM COM00 ORDER BY COMUNIDADE";

	@Autowired
	private ComunidadeDao comunidadeDao;

	@Autowired
	private BaciaHidrograficaDao baciaHidrograficaDao;

	@Autowired
	private ComunidadeBaciaHidrograficaDao comunidadeBaciaHidrograficaDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		
		Connection con = (Connection) contexto.get("conexao");
		UnidadeOrganizacional unidadeOrganizacional = (UnidadeOrganizacional) contexto.get("unidadeOrganizacional");
		DbSater base = (DbSater) contexto.get("base");
		
		try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(SQL);) {
			while (rs.next()) {
				Comunidade comunidade = null;
				String comunidadeNome = rs.getString("COMUNIDADE");
				List<Comunidade> comunidadeList = comunidadeDao.findByNomeOrderByNomeAsc(comunidadeNome);
				if (comunidadeList == null || comunidadeList.isEmpty()) {
					comunidade = novaComunidade(unidadeOrganizacional, comunidadeNome, rs, base);
				} else {
					for (Comunidade c : comunidadeList) {
						if (c.getUnidadeOrganizacional().getId().equals(unidadeOrganizacional.getId())) {
							if (comunidade != null) {
								throw new BoException("Comunidade duplicada - " + comunidade.getNome());
							}
							comunidade = c;
						}
					}
					if (comunidade == null) {
						comunidade = novaComunidade(unidadeOrganizacional, comunidadeNome, rs, base);
					}
				}

				BaciaHidrografica baciaHidrografica = null;
				String baciaHidrograficaNome = rs.getString("COMUNIDADE");
				List<BaciaHidrografica> baciaHidrograficaList = baciaHidrograficaDao.findByNomeOrderByNomeAsc(baciaHidrograficaNome);
				if (baciaHidrograficaList == null || baciaHidrograficaList.isEmpty()) {
					baciaHidrografica = novaBaciaHidrografica(baciaHidrograficaNome, rs, base);
				} else {
					for (BaciaHidrografica b : baciaHidrograficaList) {
						if (baciaHidrografica != null) {
							throw new BoException("Bacia Hidrografica duplicada - " + baciaHidrografica.getNome());
						}
						baciaHidrografica = b;
					}
				}

				ComunidadeBaciaHidrografica comunidadeBaciaHidrografica = null;
				List<ComunidadeBaciaHidrografica> comunidadeBaciaHidrograficaList = comunidadeBaciaHidrograficaDao.findByComunidadeAndBaciaHidrografica(comunidade, baciaHidrografica);
				if (comunidadeBaciaHidrograficaList == null || comunidadeBaciaHidrograficaList.isEmpty()) {
					comunidadeBaciaHidrografica = novaComunidadeBaciaHidrografica(comunidade, baciaHidrografica);
				} else {
					for (ComunidadeBaciaHidrografica b : comunidadeBaciaHidrograficaList) {
						if (comunidadeBaciaHidrografica != null) {
							throw new BoException(String.format("Comunidade Bacia Hidrografica duplicada - %s, %s" + comunidadeBaciaHidrografica.getComunidade().getNome(), comunidadeBaciaHidrografica.getBaciaHidrografica().getNome()));
						}
						comunidadeBaciaHidrografica = b;
					}
				}

			}
		}
		return false;
	}

	private Comunidade novaComunidade(UnidadeOrganizacional unidadeOrganizacional, String comunidadeNome, ResultSet rs, DbSater base) throws SQLException {
		Comunidade result = new Comunidade();
		result.setUnidadeOrganizacional(unidadeOrganizacional);
		result.setNome(comunidadeNome);
		result.setAssentamento(rs.getString("").equals("") ? Confirmacao.S : Confirmacao.N);
		result.setChaveSisater(String.format("%s[%s=%s]", base.name(), "IDCOM", rs.getString("")));
		return comunidadeDao.save(result);
	}

	private BaciaHidrografica novaBaciaHidrografica(String baciaHidrograficaNome, ResultSet rs, DbSater base) throws SQLException {
		BaciaHidrografica result = new BaciaHidrografica();
		result.setNome(baciaHidrograficaNome);
		result.setChaveSisater(String.format("%s[%s=%s]", base.name(), "IDCOM", rs.getString("")));
		return baciaHidrograficaDao.save(result);
	}

	private ComunidadeBaciaHidrografica novaComunidadeBaciaHidrografica(Comunidade comunidade, BaciaHidrografica baciaHidrografica) throws SQLException {
		ComunidadeBaciaHidrografica result = new ComunidadeBaciaHidrografica();
		result.setComunidade(comunidade);
		result.setBaciaHidrografica(baciaHidrografica);
		return comunidadeBaciaHidrograficaDao.save(result);
	}

}