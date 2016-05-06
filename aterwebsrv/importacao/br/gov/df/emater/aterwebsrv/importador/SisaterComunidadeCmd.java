package br.gov.df.emater.aterwebsrv.importador;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.importador.apoio.ConexaoFirebird;

@Service
public class SisaterComunidadeCmd extends _Comando {

	// private static final String SQL = "SELECT * FROM COM00 ORDER BY
	// COMUNIDADE";

	private static final String SQL = "SELECT * FROM BENEF02";

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Connection con = (Connection) contexto.get("conexao");
		try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(SQL);) {
			while (rs.next()) {
				byte[] foto = ConexaoFirebird.lerCampoBlob(rs, "BFFOTO");
				System.out.println(foto == null ? null : foto.length);
			}
		}
		return false;
	}

}