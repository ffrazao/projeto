package br.gov.df.emater.aterwebsrv.importador.apoio;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConexaoFirebird {

	public static enum DbSater {
		ELGAM("GEGAM", "DF"), 
		ELJAR("GEJAR", "DF"), 
		ELPLA("GEPLA", "DF"), 
		ELVAR("GEVAB", "DF");

/*		ASBRA("GEPRE NORTE", "GO"), 
		ASCRI("GEPRE SUDESTE", "GO"), 
		ASFOR("GEPRE LESTE", "GO"), 
		ASPBE("GEPRE NOROESTE", "GO"), 
		ASSOB("GESOB", "GO"), // repetido
		ELALG("GERAG", "DF"), 
		ELBRA("GBRAZ", "DF"), 
		ELCEI("GECEI", "DF"), 
		ELGAM("GEGAM", "DF"), 
		ELJAR("GEJAR", "DF"), 
		ELPAD("GEPAD", "DF"), 
		ELPAR("GEPAR", "DF"), 
		ELPIP("GEPIP", "DF"), 
		ELPLA("GEPLA", "DF"), 
		ELRIP("GERIP", "DF"), 
		ELRSS("GESEB", "DF"), 
		ELSOB("GESOB", "DF"), 
		ELTAB("GETAB", "DF"), 
		ELTAQ("GETAQ", "DF"), 
		ELVAR("GEVAB", "DF");
*/
		

		private String sigla;

		private String siglaEstado;

		DbSater(String sigla, String siglaEstado) {
			this.sigla = sigla;
			this.siglaEstado = siglaEstado;
		}

		public String getSigla() {
			return sigla;
		}

		public String getSiglaEstado() {
			return siglaEstado;
		}
	}

	public static enum DbSuporte {
		DBSUPORTE;
	}

	public static enum DbUnificada {
		EMATER;
	}

	private static final String DRIVER = "org.firebirdsql.jdbc.FBDriver";

	private static final String SENHA = "masterkey";

	// private static final String URL =
	// "jdbc:firebirdsql:localhost:c:\\SATERBD\\%s?encoding=ISO8859_1";

	private static final String URL = "jdbc:firebirdsql:localhost:c:\\SATERBD\\%s?encoding=WIN1252";

	private static final String USUARIO = "sysdba";

	static {
		try {
			Class.forName(DRIVER);
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
		Connection result = DriverManager.getConnection(String.format(URL, nomeBase), USUARIO, SENHA);
		result.setAutoCommit(false);
		return result;
	}

	public static byte[] lerCampoBlob(ResultSet rs, String nomeCampoBlob) throws SQLException, IOException {
		byte[] result = null;
		try {
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream(1024); BufferedInputStream bis = new BufferedInputStream(rs.getBinaryStream(nomeCampoBlob))) {
				byte buffer[] = new byte[1024];
				int bytesLidos = 0;
				if (!rs.wasNull()) {
					while ((bytesLidos = bis.read(buffer, 0, buffer.length)) != -1) {
						baos.write(buffer, 0, bytesLidos);
					}
					result = baos.toByteArray();
				}
			}
		} catch (Exception e) {
			throw e;
		}
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
		for (DbUnificada base : DbUnificada.values()) {
			try (Connection c = getConnection(base)) {
				System.out.println(c.getCatalog());
			}
		}
	}

}
