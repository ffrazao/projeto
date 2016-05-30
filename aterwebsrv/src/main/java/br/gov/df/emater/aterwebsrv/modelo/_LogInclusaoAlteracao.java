package br.gov.df.emater.aterwebsrv.modelo;

import java.util.Calendar;

import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

public interface _LogInclusaoAlteracao {

	public Calendar getAlteracaoData();

	public Usuario getAlteracaoUsuario();

	public Calendar getInclusaoData();

	public Usuario getInclusaoUsuario();

	public void setAlteracaoData(Calendar alteracaoData);

	public void setAlteracaoUsuario(Usuario alteracaoUsuario);

	public void setInclusaoData(Calendar inclusaoData);

	public void setInclusaoUsuario(Usuario inclusaoUsuario);

}
