package br.gov.df.emater.importa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.gov.df.emater.conexao.Conexao;
import br.gov.df.emater.conexao.EscritorioBanco;

public class ComunidadeCarga {

	public static void main(String[] args) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("");
		sql.append("SELECT * FROM com00");
		
		Cidade cid = new Cidade();
		
		String mysql = new String();

		Connection my = new Conexao().cnnMySql();

		EscritorioBanco esc = new EscritorioBanco();
		
		int tot = 0;
		
		for( int i=0; i<22; i++ ){
			//System.out.println(esc.getEscritorio(i)  );

			Connection x = new Conexao().cnnFireBird(esc.getEscritorio(i));
			PreparedStatement ps = x.prepareStatement(sql.toString());
			ResultSet rs = ps.executeQuery();
			while( rs.next()){
				tot++;
				//System.out.println( rs.getString("regiao") + " - " + cid.getCodigo(rs.getString("regiao")) + " - " + esc.getAssentamento(i)  );
			
				mysql = "insert into ater.comunidade ( nome, cidade_id, unidade_organizacional_id, assentamento, codigo_sisater ) " +
                        "values(\""+rs.getString("comunidade")+"\", "+cid.getCodigo(rs.getString("regiao"))+", "+esc.getAterwebId(i)+", " +
                        " \"" + esc.getAssentamento(i) +"\", \""+ rs.getString("idund")+"-"+rs.getString("idcom")+ "\" ); ";
				System.out.println( mysql.toString() );

//				PreparedStatement psMy = my.prepareStatement(mysql.toString()); 
//				psMy.execute() ;
				
				
			}
		}

		System.out.println("TOTAL" + " - " + tot );
	}
}

/*
insert into comunidade (nome, cidade_id, unidade_organizacional_id, codigo_sisater) values("NÃO VINCULADO", 1, 	1,	0);
insert into comunidade (nome, cidade_id, unidade_organizacional_id, codigo_sisater) values("NÃO VINCULADO", 1, 	2,	0);
insert into comunidade (nome, cidade_id, unidade_organizacional_id, codigo_sisater) values("NÃO VINCULADO", 1, 	9,	0);
insert into comunidade (nome, cidade_id, unidade_organizacional_id, codigo_sisater) values("NÃO VINCULADO", 1, 	13,	0);
insert into comunidade (nome, cidade_id, unidade_organizacional_id, codigo_sisater) values("NÃO VINCULADO", 1, 	15,	0);
insert into comunidade (nome, cidade_id, unidade_organizacional_id, codigo_sisater) values("NÃO VINCULADO", 1, 	16,	0);
insert into comunidade (nome, cidade_id, unidade_organizacional_id, codigo_sisater) values("NÃO VINCULADO", 1, 	18,	0);
insert into comunidade (nome, cidade_id, unidade_organizacional_id, codigo_sisater) values("NÃO VINCULADO", 1, 	19,	0);
insert into comunidade (nome, cidade_id, unidade_organizacional_id, codigo_sisater) values("NÃO VINCULADO", 1, 	20,	0);
insert into comunidade (nome, cidade_id, unidade_organizacional_id, codigo_sisater) values("NÃO VINCULADO", 1, 	22,	0);
insert into comunidade (nome, cidade_id, unidade_organizacional_id, codigo_sisater) values("NÃO VINCULADO", 1, 	31,	0);
insert into comunidade (nome, cidade_id, unidade_organizacional_id, codigo_sisater) values("NÃO VINCULADO", 1, 	40,	0);
insert into comunidade (nome, cidade_id, unidade_organizacional_id, codigo_sisater) values("NÃO VINCULADO", 1, 	42,	0);
insert into comunidade (nome, cidade_id, unidade_organizacional_id, codigo_sisater) values("NÃO VINCULADO", 1, 	43,	0);
insert into comunidade (nome, cidade_id, unidade_organizacional_id, codigo_sisater) values("NÃO VINCULADO", 1, 	44,	0);
insert into comunidade (nome, cidade_id, unidade_organizacional_id, codigo_sisater) values("NÃO VINCULADO", 1, 	47,	0);
insert into comunidade (nome, cidade_id, unidade_organizacional_id, codigo_sisater) values("NÃO VINCULADO", 1, 	48,	0);
insert into comunidade (nome, cidade_id, unidade_organizacional_id, codigo_sisater) values("NÃO VINCULADO", 1, 	52,	0);
insert into comunidade (nome, cidade_id, unidade_organizacional_id, codigo_sisater) values("NÃO VINCULADO", 1, 	53,	0);
insert into comunidade (nome, cidade_id, unidade_organizacional_id, codigo_sisater) values("NÃO VINCULADO", 1, 	63,	0);
insert into comunidade (nome, cidade_id, unidade_organizacional_id, codigo_sisater) values("NÃO VINCULADO", 1, 	64,	0);

*/

