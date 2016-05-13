package br.gov.df.emater.aterwebsrv.importador.sisater;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ater.PropriedadeRuralDao;
import br.gov.df.emater.aterwebsrv.dao.ater.PublicoAlvoPropriedadeRuralDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaDao;
import br.gov.df.emater.aterwebsrv.importador.apoio.ConexaoFirebird.DbSater;
import br.gov.df.emater.aterwebsrv.importador.apoio.ImpUtil;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoPropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;

@Service
public class SisaterPublicoAlvoPropriedadeRuralCmd extends _Comando {

	private static final String SISATER_TABELA = "BENEF01";

	private static final String SQL = String.format("SELECT T.* FROM %s T ORDER BY T.IDPRP, T.IDBEN", SISATER_TABELA);

	private Calendar agora;

	private DbSater base;

	private Connection con;

	private ImpUtil impUtil;

	@Autowired
	private PublicoAlvoPropriedadeRuralDao publicoAlvoPropriedadeRuralDao;

	@Autowired
	private PessoaDao pessoaDao;

	@Autowired
	private PropriedadeRuralDao propriedadeRuralDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		con = (Connection) contexto.get("conexao");
		base = (DbSater) contexto.get("base");
		impUtil = (ImpUtil) contexto.get("impUtil");

		agora = (Calendar) contexto.get("agora");

		impUtil.criarMarcaTabela(con, SISATER_TABELA);

		try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(SQL);) {
			int cont = 0;
			while (rs.next()) {
				cont++;
				PublicoAlvoPropriedadeRural publicoAlvoPropriedadeRural = null;
				try {
					publicoAlvoPropriedadeRural = novoPublicoAlvoPropriedadeRural(rs);
				} catch (BoException e) {
					continue;
				}
				publicoAlvoPropriedadeRural.setChaveSisater(impUtil.chavePublicoAlvoPropriedadeRural(base, rs.getString("IDPRP"), rs.getString("IDBEN")));

				// recuperar os IDs
				PublicoAlvoPropriedadeRural salvo = publicoAlvoPropriedadeRuralDao.findOneByChaveSisater(publicoAlvoPropriedadeRural.getChaveSisater());
				if (salvo != null) {
					publicoAlvoPropriedadeRural.setId(salvo.getId());
				}

				// salvar no MySQL e no Firebird
				publicoAlvoPropriedadeRural = publicoAlvoPropriedadeRuralDao.save(publicoAlvoPropriedadeRural);

				impUtil.chaveAterWebAtualizar(con, publicoAlvoPropriedadeRural.getId(), SISATER_TABELA, "IDPRP = ? AND IDBEN =  ?", rs.getString("IDPRP"), rs.getString("IDBEN"));
			}
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("[%s] importado %d vinculo entre pessoas e propriedades rurais", base.name(), cont));
			}
		}
		return false;
	}

	private PublicoAlvoPropriedadeRural novoPublicoAlvoPropriedadeRural(ResultSet rs) throws SQLException, BoException {
		PublicoAlvoPropriedadeRural result = new PublicoAlvoPropriedadeRural();
		Pessoa pessoa = pessoaDao.findOneByChaveSisater(impUtil.chaveBeneficiario(base, rs.getString("IDBEN").substring(0, 2), rs.getString("IDBEN")));
		PropriedadeRural propriedadeRural = propriedadeRuralDao.findOneByChaveSisater(impUtil.chavePropriedadeRural(base, rs.getString("IDPRP").substring(0, 2), rs.getString("IDPRP")));

		if (pessoa == null || propriedadeRural == null) {
			throw new BoException("Pessoa ou Propriedade Rural inexistente");
		}
		result.setPublicoAlvo(pessoa.getPublicoAlvo());
		result.setPropriedadeRural(propriedadeRural);
		result.setArea(impUtil.captaBigDecimal(rs.getDouble("EXPAREA")));
		result.setInicio(agora);
		result.setVinculo(impUtil.deParaPropriedadeRuralVinculoTipo(rs.getString("EXPREG")));

		return result;
	}

}