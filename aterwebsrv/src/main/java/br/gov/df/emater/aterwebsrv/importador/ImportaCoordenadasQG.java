package br.gov.df.emater.aterwebsrv.importador;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioExcel;

public final class ImportaCoordenadasQG {

	static Connection con;

	static PreparedStatement ps1, ps2;

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306", "emater", "emater");
			con.setAutoCommit(true);
			StringBuilder sql = new StringBuilder();
			sql.append("select endereco_id").append("\n");
			sql.append("from ater.propriedade_rural").append("\n");
			sql.append("where chave_sisater = ?").append("\n");
			ps1 = con.prepareStatement(sql.toString());
			sql = new StringBuilder();
			sql.append("update pessoa.endereco").append("\n");
			sql.append("set entrada_principal = GeomFromText(?)").append("\n");
			sql.append("where id = ?").append("\n");
			ps2 = con.prepareStatement(sql.toString());
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static void main(String[] args) throws Exception {
		// "_coord_final.xlsx", "_coord-chave.xlsx"
		final List<Map<String, Object>> mapaCoord = UtilitarioExcel.criarMapaDoArquivoExcel(new File(args[0]), 0, 0);
		final List<Map<String, Object>> mapaChave = UtilitarioExcel.criarMapaDoArquivoExcel(new File(args[1]), 0, 0);

		int cont = 0;
		for (Map<String, Object> coord : mapaCoord) {
			for (Map<String, Object> chave : mapaChave) {
				if (chave.get("seq").equals(coord.get("seq"))) {
					String c1 = String.format("POINT (%s %s)", coord.get("Long"), coord.get("Lat"));
					String c2 = (String) chave.get("chave");

					ps1.setString(1, c2);
					ResultSet rs = ps1.executeQuery();
					if (rs.next()) {
						StringBuilder sql = new StringBuilder();
						sql.append("update pessoa.endereco").append("\n");
						sql.append("set entrada_principal = GeomFromText('").append(c1).append("')").append("\n");
						sql.append("where id = ").append(rs.getInt(1)).append("\n");
						ps2 = con.prepareStatement(sql.toString());
						System.out.println(sql);
						System.out.println(";");
					}

					cont++;
				}
			}
		}

		System.out.println(cont);
	}
}
