package br.gov.df.emater.importa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import br.gov.df.emater.conexao.Conexao;
import br.gov.df.emater.conexao.EscritorioBanco;

public class BeneficiarioCarga {

	static Conexao cn = new Conexao();
	static Connection my ;
	static Comunidade comunidade;
	
	@SuppressWarnings("null")
	public static void main(String[] args) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("");
		sql.append("SELECT * FROM benef00"); // where idben='53000078'"
		String myQuery = new String();
		my = cn.cnnMySql(); 
		comunidade = new Comunidade();

		EscritorioBanco esc = new EscritorioBanco();
		
		int com, tot = 0;
		String id = new String();
		String dat = new String();
		
		for( int i=0; i<22; i++ ){
			Connection x = new Conexao().cnnFireBird(esc.getEscritorio(i));
			PreparedStatement ps = x.prepareStatement(sql.toString());
			ResultSet rs = ps.executeQuery();
			tot = 0;
			while( rs.next()){
				tot++;
				
				Integer my_pessoa_id = 0,  my_publico_alvo_id = 0;
				String tipo_pessoa, situacao;
				Boolean erro = false;
				
				id = esc.getEscritorio(i) + "/" +rs.getString("IDBEN");
				
				tipo_pessoa = rs.getString("BFNATUREZA").equals("Física") ? "PF" : "PJ";
				situacao = rs.getString("STATUS").equals("Ativo") ? "A" : "U";
								
				System.out.println(id);
				
				myQuery = "select id, pessoa_id from ater.publico_alvo where codigo_sisater = \""+id+"\" ; ";
				PreparedStatement stmId = my.prepareStatement(myQuery.toString());
				ResultSet rsId = stmId.executeQuery(); 										
				if( rsId.next() ){
					my_pessoa_id =  Integer.parseInt(rsId.getString("pessoa_id"));
					my_publico_alvo_id = Integer.parseInt(rsId.getString("id"));
				} else {
					
					erro = false;
					
					if(  tipo_pessoa == "PF" && rs.getString("BFGENERO") == null ){
						myQuery = "insert into carga.erro ( escritorio, beneficiario, descricao ) ";
						myQuery+= "            values ( \""+esc.getEscritorio(i) +"\",  \""+ rs.getString("IDBEN") +" - "+ rs.getString("BFNOME") +"\", \"Pessoa física com Genero == null\"); " ;
						my.prepareStatement(myQuery.toString()).execute();
						erro = true;
						my.commit();
					}

					if( !erro ){
					
						myQuery = "insert into pessoa.pessoa (nome, pessoa_tipo, apelido_sigla, publico_alvo, situacao ) " ;
						myQuery+= "                   values ( \""+STR( rs.getString("BFNOME") ) +"\", \""+ tipo_pessoa +"\", \""+ STR( rs.getString("BFAPELIDO") ) +"\", \"S\", \""+ situacao +"\"); " ;
						PreparedStatement psInsertPessoa = my.prepareStatement(myQuery.toString(), PreparedStatement.RETURN_GENERATED_KEYS); 
						psInsertPessoa.executeUpdate();
						ResultSet rsInsertPessoa = psInsertPessoa.getGeneratedKeys();
						if (rsInsertPessoa.next()){ 
							my_pessoa_id = rsInsertPessoa.getInt(1); 
						}
							
						myQuery = "insert into ater.publico_alvo ( pessoa_id, codigo_sisater ) ";
						myQuery+= "                       values ( "+ my_pessoa_id.toString()+ ", \"" + id + "\" );";
						PreparedStatement psInsertPublicoAlvo = my.prepareStatement(myQuery.toString(), PreparedStatement.RETURN_GENERATED_KEYS); 
						my_publico_alvo_id = psInsertPublicoAlvo.executeUpdate();					
						ResultSet rsInsertPublicoAlvo = psInsertPublicoAlvo.getGeneratedKeys();
						if (rsInsertPublicoAlvo.next()){ 
							my_publico_alvo_id = rsInsertPublicoAlvo.getInt(1); 
						}
						
						
						if( tipo_pessoa == "PF"  ){
							myQuery = "insert into pessoa.pessoa_fisica ( id, genero, cpf ) ";
							myQuery+= "                       values ( "+ my_pessoa_id + ", \"" + ( rs.getString("BFGENERO").equals("Masculino") ? "M" : "F") + "\", \"" + CPF(STR(rs.getString("BFCPF"))) + "\" );";
							my.prepareStatement(myQuery.toString()).execute(); 
						} else {
							myQuery = "insert into pessoa.pessoa_juridica ( id, cnpj ) ";
							myQuery+= "                       values ( "+ my_pessoa_id + ", \"" + CNPJ(STR(rs.getString("BFCPF"))) + "\" );";
							my.prepareStatement(myQuery.toString()).execute(); 
							
						}
						my.commit();
					}
					
				}
				
				if( my_pessoa_id != 0 ){ // não cadastrou a pessoa
					
					// Documentos
					if( tipo_pessoa == "PF"  ){					
						Profissao   	profiss  = new Profissao();
						Escolaridade 	escolar  = new Escolaridade();
						EstadoCivil 	estCivil = new EstadoCivil();
						RegimeCasamento regiCasa = new RegimeCasamento();
						myQuery = "update pessoa.pessoa_fisica  " 
						        + "   set nascimento_local = " + MyString( rs.getString("BFNATURALIDADE")) + "," 
								+ "       nascimento = " + MyString( rs.getString("BFDTNASC")) + "," 
								+ "       estado_civil = " + MyString(estCivil.getSisater(rs.getString("BFCIVIL"))) + "," 
								+ "       escolaridade = " + MyString(escolar.getSisater(rs.getString("BFINS"))) + ","
								+ "       profissao_id = " + MyInteger(profiss.getSisater(rs.getString("BFPROFISSAO"))) + ","
								+ "       rg_numero = "  + MyString(rs.getString("BFIDENR")) + "," 
								+ "       rg_data_emissao = " + MyString(rs.getString("BFIDEEXP")) + "," 
								+ "       rg_orgao_emissor = " + MyString(rs.getString("BFIDEORG")) + "," 
								+ "       certidao_casamento_regime = " + MyString(regiCasa.getSisater(rs.getString("BFREGCASAMENTO"))) + ","
								+ "       dap_registro = " + MyString(rs.getString("BFDAPNR")) + ","
								+ "       dap_situacao =  " + MyString(rs.getString("BFDAP").substring(0,1)) + ","
								+ "       dap_observacao = " + MyString(rs.getString("BFDAPOBS")) 							
					            + " where id = " + my_pessoa_id + " ; ";
					} else {
						myQuery = "update pessoa.pessoa_juridica  " 
						        + "   set inscricao_estadual = " + MyString( rs.getString("BFINSCEST")) 				
					            + " where id = " + my_pessoa_id + " ; ";					
					}
					my.prepareStatement(myQuery.toString()).execute(); 
					my.commit();					
	
					// vincula produtor à comunidade
					if( my_publico_alvo_id!=0 ){ // So vincula se não é publico alvo
						String comun = comunidade.getComunidade( rs.getString("IDUND"), rs.getString("IDCOM") );
			            myQuery = "select * from ater.publico_alvo_propriedade_rural " +
						          "   where publico_alvo_id = " + my_publico_alvo_id + 
						          "     and comunidade_id = " + MyString( comun );
						ResultSet rsExistTel = my.prepareStatement(myQuery.toString()).executeQuery(); 
						if( !rsExistTel.next() ){
							myQuery = "insert into ater.publico_alvo_propriedade_rural ( publico_alvo_id, comunidade_id ) "
									+ "values ( " + my_publico_alvo_id + ", " + MyString( comun ) + " ) ;" ;
							System.out.println(myQuery.toString());
							my.prepareStatement(myQuery.toString()).execute(); my.commit();					
						}
					}
		 			
					// endereços
					my.prepareStatement( "delete from pessoa.pessoa_endereco where pessoa_id = "+my_pessoa_id ).execute( ); 
					Endereco( my_pessoa_id, STR(rs.getString("BFENDERECO1")), STR(rs.getString("BFCEP1")), 1 );
					Endereco( my_pessoa_id, STR(rs.getString("BFENDERECO2")), STR(rs.getString("BFCEP2")), 2 );
					my.commit();
	
					// telefone
					my.prepareStatement("delete from pessoa.pessoa_telefone where pessoa_id = "+my_pessoa_id).execute();
					Telefone( my_pessoa_id, STR(rs.getString("BFTELEF")), 1 );
					Telefone( my_pessoa_id, STR(rs.getString("BFCELULAR")), 2 );
					my.commit();
					
					// grupo socical
					my.prepareStatement("delete from pessoa.pessoa_grupo_social where pessoa_id = "+my_pessoa_id).execute();
					GrupoSocial( my_pessoa_id, 1, STR(rs.getString("BENEFSOC1")) ); // Carteira de trabalho
					GrupoSocial( my_pessoa_id, 2, STR(rs.getString("BENEFSOC2")) ); // Aposentadoria
					GrupoSocial( my_pessoa_id, 3, STR(rs.getString("BENEFSOC3")) ); // programas sociais
					GrupoSocial( my_pessoa_id, 4, STR(rs.getString("BENEFSOC4")) ); // PNE
					GrupoSocial( my_pessoa_id, 5, STR(rs.getString("PEATEPA")) ); // Carteira de trabalho
					GrupoSocial( my_pessoa_id, 6, STR(rs.getString("PEINCRA")) ); // Carteira de trabalho
					GrupoSocial( my_pessoa_id, 7, STR(rs.getString("PEBSM")) ); // Carteira de trabalho
					GrupoSocial( my_pessoa_id, 8, STR(rs.getString("PESUSTENTABILIDADE")) ); // Carteira de trabalho
					GrupoSocial( my_pessoa_id, 9, STR(rs.getString("BFCOMPRAS")) ); // Carteira de trabalho
					my.commit();

				}
				
			}

			System.out.println(esc.getEscritorio(i)+ " --------------------------------- " + tot );

		}
		
		
	} 

	private static void Endereco( Integer id, String end, String cep, Integer ord ) throws SQLException{
		String myQuery = new String();
		String endId = null;
		if( end!=null && end.trim().length()>0 ){
			
			ResultSet rsExistEnd = my.prepareStatement(  "select * from pessoa.endereco USE INDEX (i_iln)  where endereco_sisater = "+StringExt.MyString(end) ).executeQuery(); 
			if( rsExistEnd.next() ){
				endId = rsExistEnd.getString("id");
			} else {				
			
				myQuery = "insert into pessoa.endereco ( cep, endereco_sisater, endereco_atualizado ) " ;
				myQuery+= "                     values ( "+StringExt.MyString(StringExt.CEP(cep)) +", "+ StringExt.MyString(end) +", \"N\" ); " ;
				PreparedStatement psEnd = my.prepareStatement(myQuery.toString(), PreparedStatement.RETURN_GENERATED_KEYS); 
				psEnd.executeUpdate(); ResultSet rsEnd = psEnd.getGeneratedKeys(); rsEnd.next();
				endId = "" + rsEnd.getInt(1);
			}
			 
			if( !my.prepareStatement( "select * from pessoa.pessoa_endereco where pessoa_id ="+id+" and endereco_id="+endId ).executeQuery().next() ){
				myQuery = "insert into pessoa.pessoa_endereco ( pessoa_id, endereco_id, ordem, finalidade ) " ;
				myQuery+= "                     values ( "+ id +", "+ endId +", "+ ord +", \"C\" ); " ;
				my.prepareStatement(myQuery.toString()).execute();
			}

		}				
	}

	private static void Telefone( Integer id, String tel, Integer ord ) throws SQLException{
		String myQuery = new String();
		String telId = null;
		if( tel!=null && tel.trim().length()>0 ){
			
			ResultSet rsExistTel = my.prepareStatement( "select * from pessoa.telefone where numero = "+MyString(tel) ).executeQuery(); 
			
			if( rsExistTel.next() ){
				telId = rsExistTel.getString("id");
			} else {				
				myQuery = "insert into pessoa.telefone ( numero ) values ( "+MyString(tel) +" ); " ;
				PreparedStatement psTel = my.prepareStatement(myQuery.toString(), PreparedStatement.RETURN_GENERATED_KEYS); 
				psTel.executeUpdate(); ResultSet rsTel = psTel.getGeneratedKeys(); rsTel.next();
				telId = "" + rsTel.getInt(1);
			}
			
			if( !my.prepareStatement( "select * from pessoa.pessoa_telefone where pessoa_id ="+id+" and telefone_id="+telId ).executeQuery().next() ){
				myQuery = "insert into pessoa.pessoa_telefone ( pessoa_id, telefone_id, ordem, finalidade ) values ("+id+", "+telId +", "+ ord +", \"C\" ); " ;
				my.prepareStatement(myQuery.toString()).execute();
			}
		}				
	}

	private static void GrupoSocial( Integer id, Integer grp, String val ) throws SQLException{
		String myQuery = new String();
		if( val!=null && val.trim().length()>0 && val == "Sim"  ){
			myQuery = "insert into pessoa.pessoa_grupo_social ( pessoa_id, grupo_social_id ) values ( "+ id +", "+ grp + " ); " ;
			my.prepareStatement(myQuery.toString()).execute();
		}				
	}


	public static String STR(String str){
		return str == null ? "" : str.replaceAll("[\"\']","").replaceAll(Pattern.quote("\\"), "").trim() ;
	}
	public static String MyString( String str ){
		return str == null ? "null" : "\"" + str.replaceAll("[\"\']","").replaceAll(Pattern.quote("\\"), "").trim() + "\"" ;
	}
	public static String MyInteger( String str ){
		return str == null ? "null" : str.replaceAll("[\"\']","").replaceAll(Pattern.quote("\\"), "").trim() ;
	}
	
	public static String STRFloat( String str ){	
		return (String) (STR(str)!="" ? Float.toString(Float.parseFloat(STR(str))) : "0.0")  ;
	}
	
	public static String Telefone( String tel, String cel ){
		cel=cel.replaceAll("[a-zA-Z]","");
		tel=tel.replaceAll("[a-zA-Z]","");
		if( tel.isEmpty() && cel.isEmpty() ){ return ""; }
		if( tel.isEmpty() ){ return cel; }
		if( cel.isEmpty() ){ return tel; }
		return tel+" - "+cel;	
	}
	
	public static String CPF( String val ){
		return formatar(val.replaceAll("[.-]", ""), "###.###.###-##" );
	}

	public static String CNPJ( String val ){
		return formatar(val.replaceAll("[.-]", ""), "##.###.###/####-##");
	}
	
    public static String formatar( String valor, String mascara ) {
        
        String dado = "";      
        // remove caracteres nao numericos
        for ( int i = 0; i < valor.length(); i++ )  {
            char c = valor.charAt(i);
            if ( Character.isDigit( c ) ) { dado += c; }
        }

        int indMascara = mascara.length();
        int indCampo = dado.length();

        for ( ; indCampo > 0 && indMascara > 0; ) {
            if ( mascara.charAt( --indMascara ) == '#' ) { indCampo--; }
        }

        String saida = "";
        for ( ; indMascara < mascara.length(); indMascara++ ) {    
            saida += ( ( mascara.charAt( indMascara ) == '#' ) ? dado.charAt( indCampo++ ) : mascara.charAt( indMascara ) );
        }    
        return saida;
    }
	
	
	
}





