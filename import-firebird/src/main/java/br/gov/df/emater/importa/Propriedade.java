package br.gov.df.emater.importa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.gov.df.emater.conexao.Conexao;
import br.gov.df.emater.conexao.EscritorioBanco;

public class Propriedade {
	
	public static void main(String[] args) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("");
		sql.append("SELECT * FROM prop00");
		
		Connection my = new Conexao().cnnMySql();

		EscritorioBanco esc = new EscritorioBanco();
		
		int tot = 0;
		
		for( int i=0; i<19; i++ ){
			
			System.out.println(esc.getEscritorio(i) );
			Connection x = new Conexao().cnnFireBird(esc.getEscritorio(i));
			PreparedStatement ps = x.prepareStatement(sql.toString());
			ResultSet rs = ps.executeQuery();
			while( rs.next()){
				tot++;
				System.out.println( rs.getString("sistema") );
//				System.out.println( rs.getString("idund") + " - " + rs.getString("idcom") + " - " + rs.getString("comunidade")  + " - " + rs.getString("bacia")  + " - " + rs.getString("regiao") );
				
//				PreparedStatement psMy = my.prepareStatement("sp_ ");
//				ResultSet rs = ps.executeQuery();
				
				
			}
		}
		System.out.println("TOTAL" + " - " + tot );
	}
}
