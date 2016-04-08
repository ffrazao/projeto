package br.gov.df.emater;

import java.io.File;
import java.sql.Connection;

import br.gov.df.emater.banco.ConexaoMysql;
import br.gov.df.emater.importa.ImportaIBGEMunicipio;

public class ImportaDadosApoioApplication {

	public static final File TEMP_DIR = new File(ImportaDadosApoioApplication.class.getResource("/").getPath() + "/../temp");

	public static void main(String[] args) throws Exception {
		Connection con = null;
		try {
			con = ConexaoMysql.getConnection();

			// ImportaIBGEMunicipio mun = new ImportaIBGEMunicipio(con);
			// mun.pais();
			// mun.executar(1);

			// new ImportaIBGECNAE(con).executar();

			con.commit();
		} catch (Exception e) {
			con.rollback();
			e.printStackTrace();
		} finally {
			con.close();
			con = null;
		}
	}
}
