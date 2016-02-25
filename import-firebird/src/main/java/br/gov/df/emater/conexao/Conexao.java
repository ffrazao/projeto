package br.gov.df.emater.conexao;
import java.sql.Connection;
import java.sql.SQLException;

public class Conexao {

    String ibURL = "jdbc:firebirdsql:localhost:F:/mysql/SATERBD/ESCRITORIO/DBSATER.FDB";
    String ibUSR = "sysdba";
    String ibPWD = "masterkey";
    String ibDRV = "org.firebirdsql.jdbc.FBDriver";

    String myURL = "jdbc:mysql://127.0.0.1:3306/";
    String myUSR = "root";
    String myPWD = ""; 
    String myDBS = "pessoa";
    String myDRV = "com.mysql.jdbc.Driver";

    public Conexao(){
    	
	    try {
	        Class.forName (ibDRV);
	        java.sql.DriverManager.registerDriver (
	                (java.sql.Driver) Class.forName (ibDRV).newInstance ()
	             );
	      }
	      catch (Exception e) {
	        // A call to Class.forName() forces us to consider this exception :-)...
	        System.out.println ("Firebird JCA-JDBC driver not found in class path");
	        System.out.println (e.getMessage ());
	        return;
	      }
	    
	    try {
	        Class.forName (myDRV);
	        java.sql.DriverManager.registerDriver (
	                (java.sql.Driver) Class.forName (myDRV).newInstance ()
	             );
	      }
	      catch (Exception e) {
	        // A call to Class.forName() forces us to consider this exception :-)...
	        System.out.println ("Firebird JCA-JDBC driver not found in class path");
	        System.out.println (e.getMessage ());
	        return;
	      }
	    
	    
    }
       
    public Connection cnnFireBird(String escritorio ) throws SQLException{
        Connection c = java.sql.DriverManager.getConnection (ibURL.replace("ESCRITORIO", escritorio) , ibUSR, ibPWD);
        return c;
    }
    
    public Connection cnnMySql( ) throws SQLException{
        Connection c = java.sql.DriverManager.getConnection( myURL + myDBS, myUSR, myPWD );     
        c.setAutoCommit(false);
        return c;
    }
    
    
}
