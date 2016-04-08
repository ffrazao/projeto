package br.gov.df.emater.banco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoMysql {
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static Connection getConnection() throws SQLException {
		Connection result = null;

		String serverName = "localhost";
		String mydatabase = "";
		String url = "jdbc:mysql://" + serverName + "/" + mydatabase;
		String username = "emater";
		String password = "emater";
		result = DriverManager.getConnection(url, username, password);
		result.setAutoCommit(false);

		return result;
	}
}
