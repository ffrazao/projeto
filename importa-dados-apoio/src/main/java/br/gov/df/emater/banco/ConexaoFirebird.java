package br.gov.df.emater.banco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoFirebird {

	public static enum DbSater {
		ASBRA, ASCRI, ASFOR, ASPBE, ASSOB, ELALG, ELBRA, ELCEI, ELGAM, ELJAR, ELPAD, ELPAR, ELPIP, ELPLA, ELRIP, ELRSS, ELSOB, ELTAB, ELTAQ, ELVAR;
	}

	public static enum DbSuporte {
		DBSUPORTE;
	}

	public static enum DbUnificada {
		EMATER;
	}

	static {
		try {
			Class.forName("org.firebirdsql.jdbc.FBDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static Connection getConnection(DbSater base) throws SQLException {
		return getConnection(base.name() + "\\DBSATER.FDB");
	}

	public static Connection getConnection(DbSuporte baseApoio) throws SQLException {
		return getConnection(baseApoio.name() + "\\DBSUPORTE.FDB");
	}

	public static Connection getConnection(DbUnificada base) throws SQLException {
		return getConnection(base.name() + "\\DBSATER.FDB");
	}

	private static Connection getConnection(String nomeBase) throws SQLException {
		Connection result = null;

		String url = String.format("jdbc:firebirdsql:localhost:c:\\SATERBD\\%s", nomeBase);
		String username = "sysdba";
		String password = "masterkey";
		result = DriverManager.getConnection(url, username, password);
		result.setAutoCommit(false);

		return result;
	}

	public static void main(String[] args) throws SQLException {
		for (DbSater base : DbSater.values()) {
			try (Connection c = getConnection(base)) {
				System.out.println(c.getCatalog());
			}
		}
		for (DbSuporte base : DbSuporte.values()) {
			try (Connection c = getConnection(base)) {
				System.out.println(c.getCatalog());
			}
		}
	}
}
