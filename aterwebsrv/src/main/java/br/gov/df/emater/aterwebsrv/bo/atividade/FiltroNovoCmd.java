package br.gov.df.emater.aterwebsrv.bo.atividade;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

import javax.sql.DataSource;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.dto.atividade.AtividadeCadFiltroDto;

@Service("AtividadeFiltroNovoCmd")
public class FiltroNovoCmd implements Command {
	
	@Autowired
	public DataSource planejamentoDS;

	@SuppressWarnings("unchecked")
	@Override
	public boolean execute(Context context) throws Exception {
		AtividadeCadFiltroDto filtro = new AtividadeCadFiltroDto();
		
		Connection c = planejamentoDS.getConnection();
		Statement st = c.createStatement();
		ResultSet rs = st.executeQuery("select count(*) from planejamento.assunto");
		rs.next();
		System.out.println(rs.getInt(0));

		Calendar hoje = Calendar.getInstance();

		Calendar inicio = Calendar.getInstance();
		inicio.set(hoje.get(Calendar.YEAR), hoje.get(Calendar.YEAR), hoje.get(Calendar.DATE));
		inicio.set(Calendar.MONTH, -1);

		Calendar termino = Calendar.getInstance();
		termino.set(hoje.get(Calendar.YEAR), hoje.get(Calendar.YEAR), hoje.get(Calendar.DATE));

		filtro.setInicio(inicio);
		filtro.setTermino(termino);

		context.put("resultado", filtro);
		return false;
	}

}
