package br.gov.df.emater.aterwebsrv.modelo.dto;

import java.util.List;

import br.gov.df.emater.aterwebsrv.modelo.ater.Setor;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ConfirmacaoOpcional;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaGeracao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaSituacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaTipo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PropriedadeUtilizacaoEspacoRural;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PublicoAlvoCategoria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Sexo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaGrupo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaGrupoComunidadeVi;

public class PessoaCadFiltroDto implements FiltroDto {

	private static final long serialVersionUID = 1L;

	private String cnpj;

	private String cpf;

	private List<PessoaGrupo> grupoSocialList;

	private String nome;

	private Integer numeroPagina;

	private Sexo pessoaGenero;

	private PessoaGeracao pessoaGeracao;

	private PessoaSituacao pessoaSituacao;

	private ConfirmacaoOpcional publicoAlvoAtepa;

	private ConfirmacaoOpcional publicoAlvoAter;

	private ConfirmacaoOpcional publicoAlvoBeneficioSocial;

	private ConfirmacaoOpcional publicoAlvoBsm;

	private PublicoAlvoCategoria publicoAlvoCategoria;

	private ConfirmacaoOpcional publicoAlvoComprasInstitucionais;

	private ConfirmacaoOpcional publicoAlvoDap;

	private ConfirmacaoOpcional publicoAlvoIncra;

	private ConfirmacaoOpcional publicoAlvoIpaAgricola;

	private ConfirmacaoOpcional publicoAlvoIpaAnimal;

	private ConfirmacaoOpcional publicoAlvoIpaFlores;

	private ConfirmacaoOpcional publicoAlvoIpaNaoAgricola;

	private ConfirmacaoOpcional publicoAlvoOrganizacao;

	private PessoaGrupoComunidadeVi publicoAlvoPessoaGrupoComunidadeVi;

	private PropriedadeUtilizacaoEspacoRural publicoAlvoPropriedadeUtilizacaoEspacoRural;

	private Setor publicoAlvoSetor;

	private ConfirmacaoOpcional publicoAlvoSustentabilidade;

	private Integer registrosPagina;

	private PessoaTipo tipoPessoa;

	public String getCnpj() {
		return cnpj;
	}

	public String getCpf() {
		return cpf;
	}

	public List<PessoaGrupo> getGrupoSocialList() {
		return grupoSocialList;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public Integer getNumeroPagina() {
		return numeroPagina;
	}

	public Sexo getPessoaGenero() {
		return pessoaGenero;
	}

	public PessoaGeracao getPessoaGeracao() {
		return pessoaGeracao;
	}

	public PessoaSituacao getPessoaSituacao() {
		return pessoaSituacao;
	}

	public ConfirmacaoOpcional getPublicoAlvoAtepa() {
		return publicoAlvoAtepa;
	}

	public ConfirmacaoOpcional getPublicoAlvoAter() {
		return publicoAlvoAter;
	}

	public ConfirmacaoOpcional getPublicoAlvoBeneficioSocial() {
		return publicoAlvoBeneficioSocial;
	}

	public ConfirmacaoOpcional getPublicoAlvoBsm() {
		return publicoAlvoBsm;
	}

	public PublicoAlvoCategoria getPublicoAlvoCategoria() {
		return publicoAlvoCategoria;
	}

	public ConfirmacaoOpcional getPublicoAlvoComprasInstitucionais() {
		return publicoAlvoComprasInstitucionais;
	}

	public ConfirmacaoOpcional getPublicoAlvoDap() {
		return publicoAlvoDap;
	}

	public ConfirmacaoOpcional getPublicoAlvoIncra() {
		return publicoAlvoIncra;
	}

	public ConfirmacaoOpcional getPublicoAlvoIpaAgricola() {
		return publicoAlvoIpaAgricola;
	}

	public ConfirmacaoOpcional getPublicoAlvoIpaAnimal() {
		return publicoAlvoIpaAnimal;
	}

	public ConfirmacaoOpcional getPublicoAlvoIpaFlores() {
		return publicoAlvoIpaFlores;
	}

	public ConfirmacaoOpcional getPublicoAlvoIpaNaoAgricola() {
		return publicoAlvoIpaNaoAgricola;
	}

	public ConfirmacaoOpcional getPublicoAlvoOrganizacao() {
		return publicoAlvoOrganizacao;
	}

	public PessoaGrupoComunidadeVi getPublicoAlvoPessoaGrupoComunidadeVi() {
		return publicoAlvoPessoaGrupoComunidadeVi;
	}

	public PropriedadeUtilizacaoEspacoRural getPublicoAlvoPropriedadeUtilizacaoEspacoRural() {
		return publicoAlvoPropriedadeUtilizacaoEspacoRural;
	}

	public Setor getPublicoAlvoSetor() {
		return publicoAlvoSetor;
	}

	public ConfirmacaoOpcional getPublicoAlvoSustentabilidade() {
		return publicoAlvoSustentabilidade;
	}

	public Integer getRegistrosPagina() {
		return registrosPagina;
	}

	public PessoaTipo getTipoPessoa() {
		return tipoPessoa;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public void setGrupoSocialList(List<PessoaGrupo> grupoSocialList) {
		this.grupoSocialList = grupoSocialList;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public void setNumeroPagina(Integer numeroPagina) {
		this.numeroPagina = numeroPagina;
	}

	public void setPessoaGenero(Sexo pessoaGenero) {
		this.pessoaGenero = pessoaGenero;
	}

	public void setPessoaGeracao(PessoaGeracao pessoaGeracao) {
		this.pessoaGeracao = pessoaGeracao;
	}

	public void setPessoaSituacao(PessoaSituacao pessoaSituacao) {
		this.pessoaSituacao = pessoaSituacao;
	}

	public void setPublicoAlvoAtepa(ConfirmacaoOpcional publicoAlvoAtepa) {
		this.publicoAlvoAtepa = publicoAlvoAtepa;
	}
	
	public void setPublicoAlvoAter(ConfirmacaoOpcional publicoAlvoAter) {
		this.publicoAlvoAter = publicoAlvoAter;
	}
	
	public void setPublicoAlvoBeneficioSocial(
			ConfirmacaoOpcional publicoAlvoBeneficioSocial) {
		this.publicoAlvoBeneficioSocial = publicoAlvoBeneficioSocial;
	}
	
	public void setPublicoAlvoBsm(ConfirmacaoOpcional publicoAlvoBsm) {
		this.publicoAlvoBsm = publicoAlvoBsm;
	}

	public void setPublicoAlvoCategoria(PublicoAlvoCategoria publicoAlvoCategoria) {
		this.publicoAlvoCategoria = publicoAlvoCategoria;
	}

	public void setPublicoAlvoComprasInstitucionais(
			ConfirmacaoOpcional publicoAlvoComprasInstitucionais) {
		this.publicoAlvoComprasInstitucionais = publicoAlvoComprasInstitucionais;
	}

	public void setPublicoAlvoDap(ConfirmacaoOpcional publicoAlvoDap) {
		this.publicoAlvoDap = publicoAlvoDap;
	}


	public void setPublicoAlvoIncra(ConfirmacaoOpcional publicoAlvoIncra) {
		this.publicoAlvoIncra = publicoAlvoIncra;
	}

	public void setPublicoAlvoIpaAgricola(ConfirmacaoOpcional publicoAlvoIpaAgricola) {
		this.publicoAlvoIpaAgricola = publicoAlvoIpaAgricola;
	}

	public void setPublicoAlvoIpaAnimal(ConfirmacaoOpcional publicoAlvoIpaAnimal) {
		this.publicoAlvoIpaAnimal = publicoAlvoIpaAnimal;
	}

	public void setPublicoAlvoIpaFlores(ConfirmacaoOpcional publicoAlvoIpaFlores) {
		this.publicoAlvoIpaFlores = publicoAlvoIpaFlores;
	}

	public void setPublicoAlvoIpaNaoAgricola(
			ConfirmacaoOpcional publicoAlvoIpaNaoAgricola) {
		this.publicoAlvoIpaNaoAgricola = publicoAlvoIpaNaoAgricola;
	}

	public void setPublicoAlvoOrganizacao(ConfirmacaoOpcional publicoAlvoOrganizacao) {
		this.publicoAlvoOrganizacao = publicoAlvoOrganizacao;
	}

	public void setPublicoAlvoPessoaGrupoComunidadeVi(
			PessoaGrupoComunidadeVi publicoAlvoPessoaGrupoComunidadeVi) {
		this.publicoAlvoPessoaGrupoComunidadeVi = publicoAlvoPessoaGrupoComunidadeVi;
	}

	public void setPublicoAlvoPropriedadeUtilizacaoEspacoRural(
			PropriedadeUtilizacaoEspacoRural publicoAlvoPropriedadeUtilizacaoEspacoRural) {
		this.publicoAlvoPropriedadeUtilizacaoEspacoRural = publicoAlvoPropriedadeUtilizacaoEspacoRural;
	}

	public void setPublicoAlvoSetor(Setor publicoAlvoSetor) {
		this.publicoAlvoSetor = publicoAlvoSetor;
	}

	public void setPublicoAlvoSustentabilidade(
			ConfirmacaoOpcional publicoAlvoSustentabilidade) {
		this.publicoAlvoSustentabilidade = publicoAlvoSustentabilidade;
	}

	public void setRegistrosPagina(Integer registrosPagina) {
		this.registrosPagina = registrosPagina;
	}

	public void setTipoPessoa(PessoaTipo tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}
}
