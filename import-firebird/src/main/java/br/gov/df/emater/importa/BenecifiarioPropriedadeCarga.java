package br.gov.df.emater.importa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.gov.df.emater.conexao.Conexao;
import br.gov.df.emater.conexao.EscritorioBanco;

public class BenecifiarioPropriedadeCarga {

	static Conexao cn = new Conexao();
	static Connection my ;
	static Comunidade comunidade;
	
	@SuppressWarnings({ "unused" })
	public static void main(String[] args) throws SQLException {
		String myQuery = new String();
		my = cn.cnnMySql(); 
		comunidade = new Comunidade();

		EscritorioBanco esc = new EscritorioBanco();
		
		int tot, updtTot,insrTot;
		String benId = new String(); String propId = new String();
		String idBen = new String(); String idProp = new String();
		
		for( int i=17; i<22; i++ ){
			Connection fb = new Conexao().cnnFireBird(esc.getEscritorio(i));
			ResultSet rs = fb.prepareStatement("SELECT * FROM BENEF01").executeQuery();
			tot = 0; updtTot =0 ; insrTot=0;
			System.out.println( esc.getEscritorio(i)  );
			while( rs.next()){
				
				idBen = ""; idProp = "";
				benId = esc.getEscritorio(i) + "/" +rs.getString("IDBEN");
				propId = esc.getEscritorio(i) + "/" + rs.getString("IDPRP");
				System.out.println(benId + " - " + propId );
				
				ResultSet rsExistBenf = my.prepareStatement( "select * from ater.publico_alvo use index (codigo_sisater_UNIQUE) where codigo_sisater = "+StringExt.MyString(benId) ).executeQuery(); 
				if( rsExistBenf.next() ){
					idBen = rsExistBenf.getString("id");
				} else {
					System.out.println( "Não achou Publico Alvo : " + benId );
				}
				
				ResultSet rsExistProp = my.prepareStatement( "select * from ater.propriedade_rural use index (idx_codigo) where codigo = "+StringExt.MyString(propId) ).executeQuery(); 
				if( rsExistProp.next() ){
					idProp = rsExistProp.getString("id");
				} else {
					System.out.println( "Não achou Propriedade : " + propId );
				}
				
				if( idBen != "" && idProp != ""){
					
					ResultSet rsExistVinculo = my.prepareStatement( "select * from ater.publico_alvo_propriedade_rural where publico_alvo_id = " +idBen ).executeQuery(); 
					if( rsExistVinculo.next() ){
						myQuery = "update ater.publico_alvo_propriedade_rural "+ 
					              "   set propriedade_rural_id = "+idProp + 
					              "     , tipo_vinculo = "+ StringExt.MyString(getTipo(rs.getString("EXPREG"))) + 
					              "     , area = "+ StringExt.STRFloat(rs.getString("EXPAREA")) + 
								  " where publico_alvo_id = " +idBen ;
						updtTot++;
					} else {
						myQuery = "insert into ater.publico_alvo_propriedade_rural ( publico_alvo_id, propriedade_rural_id, tipo_vinculo, area ) " +
					              "                                         values ( "+ idBen +", "+ idProp +", "+ StringExt.MyString(getTipo(rs.getString("EXPREG"))) +", "+ StringExt.STRFloat(rs.getString("EXPAREA")) +");" ;
						insrTot++;
					}
					System.out.println( myQuery );
					
					my.prepareStatement(myQuery.toString()).execute(); 
					my.commit();
					
				}
								
			}
			
			System.out.println( "Insert :" +insrTot );
			System.out.println( "Update :" +updtTot );
					
		}
	}
	
	private static String getTipo( String tipo){
		String ret = "";
		switch (tipo) {  
			case "Arrendamento" :    ret="AR"; break;   
			case "Comodato" :        ret="AR"; break;
			case "Parceria" :        ret="PA"; break;
			case "Próprio" :         ret="PR"; break;
			case "arrendamento" :    ret="AR"; break;
			case "ex-proprietário" : ret="PR"; break;
			default:                 ret="PR"; break;
		}
		return ret;
	}



	
}
