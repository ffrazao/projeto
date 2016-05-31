package br.gov.df.emater.aterwebsrv.importador.sisater;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioExcel;
import br.gov.df.emater.aterwebsrv.importador.apoio.ConexaoFirebird.DbSater;
import br.gov.df.emater.aterwebsrv.importador.apoio.ImpUtil;

@Service
public class SisaterPropriedadeRuralExportaUTMCmd extends _Comando {

	private static final String SISATER_CAMPO = "PPNOME";

	private static final String SISATER_TABELA = "PROP00";

	private static final String SQL = String.format("SELECT T.* FROM %s T WHERE PPUTMN IS NOT NULL OR PPUTME IS NOT NULL ORDER BY T.%s", SISATER_TABELA, SISATER_CAMPO);

	private DbSater base;

	private Connection con;

	private ImpUtil impUtil;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		con = (Connection) contexto.get("conexao");
		base = (DbSater) contexto.get("base");
		impUtil = (ImpUtil) contexto.get("impUtil");

		impUtil.criarMarcaTabelaSisater(con, SISATER_TABELA);

		List<Map<String, Object>> regList = new ArrayList<Map<String, Object>>();
		boolean encontrou = false;
		List<String> cabecalho = new ArrayList<String>();
		cabecalho.add("chave");
		cabecalho.add("PPUTMN");
		cabecalho.add("PPUTME");
		cabecalho.add("PPUTMN-str");
		cabecalho.add("PPUTME-str");
		try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(SQL);) {
			int cont = 0;
			while (rs.next()) {
				try {
					Map<String, Object> linha = new HashMap<String, Object>();
					String[] coords = impUtil.captaUtmNE(rs.getString("PPUTMN"), rs.getString("PPUTME"));
					linha.put(cabecalho.get(0), impUtil.chavePropriedadeRural(base, rs.getString("IDUND"), rs.getString("IDPRP")));
					for (int i = 0; i < 4; i++) {
						linha.put(cabecalho.get(i + 1), coords[i]);
					}
					if ((linha.get("PPUTMN") != null && linha.get("PPUTME") != null) || (linha.get("PPUTMN-str") != null && linha.get("PPUTME-str") != null)) {
						if (!encontrou) {
							encontrou = true;
						}
						regList.add(linha);
					}
					cont++;
				} catch (Exception e) {
					logger.error(e);
					e.printStackTrace();
				}
			}
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("[%s] exportado %d coordenadas de propriedades rurais", base.name(), cont));
			}
		}
		UtilitarioExcel.criarArquivoExcelDoMapa(regList, cabecalho, String.format("c:\\temp\\prop\\%s.xlsx", base.name()));
		return false;
	}

}