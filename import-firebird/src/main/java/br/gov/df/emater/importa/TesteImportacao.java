package br.gov.df.emater.importa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import br.gov.df.emater.conexao.Conexao;
import br.gov.df.emater.conexao.EscritorioBanco;

public class TesteImportacao {
	
	public static String STR( String str ){	
		return str == null ? "" : str.replaceAll("[\"\']","").replaceAll(Pattern.quote("\\"), "") ;
	}
	public static String Telefone( String tel, String cel ){
		cel=cel.replaceAll("[a-zA-Z]","");
		tel=tel.replaceAll("[a-zA-Z]","");
		if( tel.isEmpty() && cel.isEmpty() ){ return ""; }
		if( tel.isEmpty() ){ return cel; }
		if( cel.isEmpty() ){ return tel; }
		return tel+" - "+cel;	
	}

	public static void main(String[] args) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("");
		sql.append("SELECT * FROM benef00");
		String mysql = new String();
		Connection my = new Conexao().cnnMySql();

		EscritorioBanco esc = new EscritorioBanco();
		
		int tot = 0;
		
		for( int i=0; i<19; i++ ){
			//System.out.println(esc.getEscritorio(i) );
			Connection x = new Conexao().cnnFireBird(esc.getEscritorio(i));
			PreparedStatement ps = x.prepareStatement(sql.toString());
			ResultSet rs = ps.executeQuery();
			rs.next();
			System.out.println( esc.getEscritorio(i) + " - " + STR(rs.getString("IDBEN")) + " - " + STR(rs.getString("BFNOME")) );


/*			while( rs.next()){
				tot++;
				System.out.println( STR(rs.getString("BFNOME")) );
				
				mysql = "insert into sisater.beneficiario values(\""+STR(rs.getString("IDBEN"))+"\", \""+
				                                                     STR(rs.getString("BFNOME"))+"\", \""+
						                                             STR(rs.getString("BFCPF"))+"\", \""+
						                                             STR(esc.getAterwrbid(i))+"\", \""+
						                                             STR(rs.getString("idcom"))+"\", \""+
						                                             STR(rs.getString("BFCATEGORIA"))+"\", \""+
						                                             Telefone( STR(rs.getString("BFTELEF")), STR(rs.getString("BFCELULAR")) )+"\", \""+
								                                     STR(rs.getString("STATUS") )+"\" ); ";
				System.out.println( mysql.toString() );
				PreparedStatement psMy = my.prepareStatement(mysql.toString()); 
				psMy.execute() ;

				for( int j=1; j<=rs.getMetaData().getColumnCount(); j++ ){
					if( rs.getMetaData().getColumnClassName(j) != "[B" ){	
						System.out.println( rs.getMetaData().getColumnName(j) + " - " + rs.getString(j) );
					}
				}

				System.out.print( "Salvo" );
				
			}
*/		}
//		System.out.println("TOTAL" + " - " + tot );
		
		
	} 
}
