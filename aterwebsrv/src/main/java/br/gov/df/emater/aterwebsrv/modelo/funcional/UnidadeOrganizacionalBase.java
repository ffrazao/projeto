package br.gov.df.emater.aterwebsrv.modelo.funcional;

import java.util.Calendar;
import java.util.List;

import br.gov.df.emater.aterwebsrv.modelo.ater.Comunidade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.UnidadeOrganizacionalClassificacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;

public interface UnidadeOrganizacionalBase {

	public String getChaveSisater();

	public UnidadeOrganizacionalClassificacao getClassificacao();

	public List<Comunidade> getComunidadeList();

	public Integer getId();

	public Calendar getInicio();

	public String getNome();

	public PessoaJuridica getPessoaJuridica();

	public String getSigla();

	public Calendar getTermino();

	public UnidadeOrganizacional infoBasica();

	public void setChaveSisater(String chaveSisater);

	public void setClassificacao(UnidadeOrganizacionalClassificacao classificacao);

	public void setComunidadeList(List<Comunidade> comunidadeList);

	public void setId(Integer id);

	public void setInicio(Calendar inicio);

	public void setNome(String nome);

	public void setPessoaJuridica(PessoaJuridica pessoaJuridica);

	public void setSigla(String sigla);

	public void setTermino(Calendar termino);

}
