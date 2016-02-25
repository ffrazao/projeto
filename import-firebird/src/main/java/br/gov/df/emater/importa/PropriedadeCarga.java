package br.gov.df.emater.importa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.gov.df.emater.conexao.Conexao;
import br.gov.df.emater.conexao.EscritorioBanco;

public class PropriedadeCarga {

	static Conexao cn = new Conexao();
	static Connection my ;
	static Comunidade comunidade;
	
	@SuppressWarnings({ "unused" })
	public static void main(String[] args) throws SQLException {
		String myQuery = new String();
		my = cn.cnnMySql(); 
		comunidade = new Comunidade();

		EscritorioBanco esc = new EscritorioBanco();
		
		int tot = 0;
		String id = new String(); String dat = new String();
		String idProp = new String();
		String idEnd = new String();
		
		for( int i=0; i<22; i++ ){
			Connection fb = new Conexao().cnnFireBird(esc.getEscritorio(i));
			ResultSet rs = fb.prepareStatement("SELECT * FROM prop00").executeQuery();
			tot = 0;
			while( rs.next()){
				
				id = esc.getEscritorio(i) + "/" + rs.getString("IDPRP");
				System.out.println(id);
				
				idEnd = Endereco(  rs.getString("PPEND"), rs.getString("PPCEP"), rs.getString("PPROT")==null ? "":  rs.getString("PPROT").toString(), rs.getString("PPNOME") );
				
				ResultSet rsExistProp = my.prepareStatement( "select * from ater.propriedade_rural use index (idx_codigo) where codigo = "+StringExt.MyString(id) ).executeQuery(); 
				if( rsExistProp.next() ){
					idProp = rsExistProp.getString("id");
				} else {
				
					myQuery = "insert into ater.propriedade_rural ( nome, endereco_id, codigo, comunidade_id, bacia_hidrografica_id, inclusao_usuario_id, alteracao_usuario_id ) " ;
					myQuery+= "                     values ( "+StringExt.MyString(rs.getString("PPNOME")) +", "+ idEnd +", "+ StringExt.MyString(id) +",  " +
							  "                              "+StringExt.MyString( comunidade.getComunidade( rs.getString("IDUND"), rs.getString("IDCOM") ) )+ ", " + 
							  "                              "+ comunidade.getBacia( esc.getEscritorio(i), rs.getString("IDCOM") ) + ", 1, 1 ); " ;
					System.out.println(myQuery);
					PreparedStatement psProp = my.prepareStatement(myQuery.toString(), PreparedStatement.RETURN_GENERATED_KEYS); 
					psProp.executeUpdate(); ResultSet rsProp = psProp.getGeneratedKeys(); rsProp.next();
										
					idProp = rsProp.getString(1);
					my.commit();
				}
				System.out.println(idProp);
				tot++;
			}
					
		}
	}
	

	private static String Endereco( String end, String cep, String roteiro, String nome ) throws SQLException{
		String myQuery = new String();
		String endId = "";
		end = StringExt.STR(end)=="" ? nome : end;
		ResultSet rsExistEnd = my.prepareStatement(  "select * from pessoa.endereco USE INDEX (fk_endereco_sisater) where endereco_sisater = "+StringExt.MyString(end) ).executeQuery(); 
		if( rsExistEnd.next() ){
			endId = rsExistEnd.getString("id");
			myQuery = "update pessoa.endereco set propriedade_rural_confirmacao = \"S\" "+
			                                   ", nome_prop_ou_estab = "+StringExt.MyString(nome) + 
                                               ", roteiro_acesso = "+StringExt.MyString(roteiro);
            if( cep!=null && StringExt.CEP(cep).length() > StringExt.CEP(rsExistEnd.getString("id")).length() ){
                myQuery+= "                     , cep = "+StringExt.MyString(StringExt.CEP(cep)) ;
            }
            myQuery+= " where id = "+endId ;
			my.prepareStatement(myQuery.toString()).execute();
			
		} else {				
		
			myQuery = "insert into pessoa.endereco ( cep, endereco_sisater, endereco_atualizado, nome_prop_ou_estab, roteiro_acesso ) " ;
			myQuery+= "                     values ( "+StringExt.MyString(StringExt.CEP(cep)) +", "+ StringExt.MyString(end) +", \"N\",  " +
					  "                              "+StringExt.MyString(nome)+ ", " + StringExt.MyString(roteiro)+ " ); " ;
			PreparedStatement psEnd = my.prepareStatement(myQuery.toString(), PreparedStatement.RETURN_GENERATED_KEYS); 
			psEnd.executeUpdate(); ResultSet rsEnd = psEnd.getGeneratedKeys(); rsEnd.next();
			endId = rsEnd.getString(1);
		}
		
		return endId;

	}
	
	
}
