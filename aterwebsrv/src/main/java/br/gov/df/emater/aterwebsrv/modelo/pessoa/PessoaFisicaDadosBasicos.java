package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import java.util.Calendar;

import br.gov.df.emater.aterwebsrv.modelo.dominio.Escolaridade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaGenero;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaNacionalidade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.RegimeCasamento;

public interface PessoaFisicaDadosBasicos {

	public String getApelidoSigla();

	public RegimeCasamento getCertidaoCasamentoRegime();

	public String getCpf();

	public Escolaridade getEscolaridade();

	public PessoaGenero getGenero();

	public PessoaNacionalidade getNacionalidade();

	public Calendar getNascimento();

	public Estado getNascimentoEstado();

	public Municipio getNascimentoMunicipio();

	public Pais getNascimentoPais();

	public String getNome();

	public String getNomeMaeConjuge();

	public Profissao getProfissao();

	public Calendar getRgDataEmissao();

	public String getRgNumero();

	public String getRgOrgaoEmissor();

	public String getRgUf();

	public void setApelidoSigla(String apelidoSigla);

	public void setCertidaoCasamentoRegime(RegimeCasamento certidaoCasamentoRegime);

	public void setCpf(String cpf);

	public void setEscolaridade(Escolaridade escolaridade);

	public void setGenero(PessoaGenero genero);

	public void setNacionalidade(PessoaNacionalidade nacionalidade);

	public void setNascimento(Calendar nascimento);

	public void setNascimentoEstado(Estado nascimentoEstado);

	public void setNascimentoMunicipio(Municipio nascimentoMunicipio);

	public void setNascimentoPais(Pais nascimentoPais);

	public void setNome(String nome);

	public void setNomeMaeConjuge(String nomeMaeConjuge);

	public void setProfissao(Profissao profissao);

	public void setRgDataEmissao(Calendar rgDataEmissao);

	public void setRgNumero(String rgNumero);

	public void setRgOrgaoEmissor(String rgOrgaoEmissor);

	public void setRgUf(String rgUf);

}
