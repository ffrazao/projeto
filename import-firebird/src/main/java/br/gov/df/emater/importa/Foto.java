package br.gov.df.emater.importa;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import br.gov.df.emater.conexao.Conexao;
import br.gov.df.emater.conexao.EscritorioBanco;

public class Foto {
	
	
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

	@SuppressWarnings("resource")
	public static void main(String[] args) throws SQLException, IOException  {
		StringBuilder sql = new StringBuilder();
		sql.append("");
		sql.append("SELECT benef00.idben, benef00.idcom, benef02.bffoto FROM benef00 inner join benef02 on benef02.idben = benef00.idben where not benef02.bffoto is null "); 
		String mysql = new String();
		Connection my = new Conexao().cnnMySql();

		EscritorioBanco esc = new EscritorioBanco();
		
		int tot = 0;
		int id = 0;
		int com = 0;
		String dat = "";
		
		// Deu erro ca carga de IMG do RIO PRETO -  1011000648 ( próximo )
		
		for( int i=17; i<22; i++ ){
			System.out.println(esc.getEscritorio(i) );
			Connection x = new Conexao().cnnFireBird(esc.getEscritorio(i));
			PreparedStatement ps = x.prepareStatement(sql.toString());
			ResultSet rs = ps.executeQuery();
			tot = 0;
			while( rs.next()){
				tot++;
						
				id = Integer.parseInt(rs.getString("IDBEN"))+1000000000;
				id = esc.getEscritorio(i)!="ASBRA"? id : id+18000000; 
				com = rs.getString("IDCOM")==null || rs.getString("IDCOM").trim().length()==0 ?  0 : Integer.parseInt(rs.getString("IDCOM"));  

				System.out.println( Integer.toString(tot) );

		        Blob blob = rs.getBlob(3);  
		        
	        	String filename = Integer.toString(id) ;
				String dirname = "files"+ "/" + esc.getEscritorio(i) + "/" + Integer.toString(com) + "/";
				String completo = dirname + filename+".jpg";
				
				File dir = new File( "R:/"+dirname );
				if( ! dir.exists() ){
					dir.mkdirs();
				}
				
				FileOutputStream fos = new FileOutputStream("R:/"+completo);
				InputStream is = blob.getBinaryStream();
		        int b = 0;  
		        while ((b = is.read()) != -1)  {  
		        	fos.write(b);   
		        }
		        fos.close();
		        			    
				//grava dendereco da img no banco
				mysql = "call sisater.sp_cargaFoto( "+STR( Integer.toString(id) )+", \""+completo+"\" )";				
				System.out.println( mysql ); 
				PreparedStatement psMy = my.prepareStatement(mysql.toString()); psMy.execute() ;
		        												
			}
			System.out.println( "- - - - - - - - - - - - - - - - - - - - - - - - - -" );
			System.out.println( " - " + Integer.toString(i) + " - " + tot );
		}
		
		
	} 

}
