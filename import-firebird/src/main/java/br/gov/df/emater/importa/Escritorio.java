package br.gov.df.emater.importa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.gov.df.emater.conexao.Conexao;
import br.gov.df.emater.conexao.EscritorioBanco;

public class Escritorio {
	
	public static void main(String[] args) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("");
		sql.append("SELECT * FROM und00");

		EscritorioBanco esc = new EscritorioBanco();
		
		
//		Connection my = new Conexao().cnnMySql();
		
		
		//Ecritórios ASBRA ASPIP e ASSOB - Colocar nos escritórios ELBRA, ELPIP, ELSOB
		
		
		int tot = 0;
		for( int i=0; i<22; i++ ){
			//System.out.println(esc.getEscritorio(i) );
			Connection x = new Conexao().cnnFireBird(esc.getEscritorio(i));
			PreparedStatement ps = x.prepareStatement(sql.toString());
			ResultSet rs = ps.executeQuery();
			while( rs.next()){
				tot++;
				System.out.println( "ID - " + rs.getString("IDUND") + "    Sigla   - " + rs.getString("unidade")+ "    Nome   - " + rs.getString("nomeund") );
/*				for( int j=1; j<=rs.getMetaData().getColumnCount(); j++ ){
					if( rs.getMetaData().getColumnClassName(j) != "[B" ){	
						System.out.println( rs.getMetaData().getColumnName(j) + " - " + rs.getString(j) );
					}
				}
*/
//				System.out.println( "-------------------------------------------------------------" );				
			}
		}
		// System.out.println("TOTAL" + " - " + tot );
	}
}
