package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Escolaridade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaGenero;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaNacionalidade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaRelacionamentoInformadoTipo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.RegimeCasamento;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

/**
 * The persistent class for the pessoa_relacionamento database table.
 * 
 */
@Entity
@Table(name = "pessoa_relacionamento", schema = EntidadeBase.PESSOA_SCHEMA)
public class PessoaRelacionamento extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	private String apelido;

	@Column(name = "regime_casamento")
	@Enumerated(EnumType.STRING)
	private RegimeCasamento certidaoCasamentoRegime;

	@Column(name = "chave_sisater")
	private String chaveSisater;

	private String cpf;

	@Enumerated(EnumType.STRING)
	private Escolaridade escolaridade;

	@Enumerated(EnumType.STRING)
	private PessoaGenero genero;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Enumerated(EnumType.STRING)
	private PessoaNacionalidade nacionalidade;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar nascimento;

	@Transient
	private Estado nascimentoEstado;

	@ManyToOne
	@JoinColumn(name = "nascimento_municipio_id")
	private Municipio nascimentoMunicipio;

	@ManyToOne
	@JoinColumn(name = "nascimento_pais_id")
	private Pais nascimentoPais;

	private String nome;

	@Column(name = "nome_mae_conjuge")
	private String nomeMaeConjuge;

	@ManyToOne
	@JoinColumn(name = "pessoa_id")
	private Pessoa pessoa;

	@ManyToOne
	@JoinColumn(name = "profissao_id")
	private Profissao profissao;

	@ManyToOne
	@JoinColumn(name = "relacionamento_id")
	private Relacionamento relacionamento;

	@ManyToOne
	@JoinColumn(name = "relacionamento_funcao_id")
	private RelacionamentoFuncao relacionamentoFuncao;

	@Column(name = "rg_data_emissao")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar rgDataEmissao;

	@Column(name = "rg_numero")
	private String rgNumero;

	@Column(name = "rg_orgao_emissor")
	private String rgOrgaoEmissor;

	@Column(name = "rg_uf")
	private String rgUf;

	@Column(name = "tipo_vinculo_informado")
	@Enumerated(EnumType.STRING)
	private PessoaRelacionamentoInformadoTipo tipoVinculoInformado;

	public PessoaRelacionamento() {
	}

	public PessoaRelacionamento(Integer id, Relacionamento relacionamento, PessoaRelacionamentoInformadoTipo tipoVinculoInformado, String nome, String apelido, String cpf, String rgNumero, Calendar rgDataEmissao, String rgOrgaoEmissor, String rgUf, Profissao profissaoId,
			PessoaGenero genero, PessoaNacionalidade nacionalidade, Municipio nascimentoMunicipio, Pais nascimentoPais, Calendar nascimento, Escolaridade escolaridade, RegimeCasamento regimeCasamento, String nomeMaeConjuge) {
		this.setId(id);
		this.setRelacionamento(relacionamento);
		this.setTipoVinculoInformado(tipoVinculoInformado);
		this.setNome(nome);
		this.setApelido(apelido);
		this.setCpf(cpf);
		this.setRgNumero(rgNumero);
		this.setRgDataEmissao(rgDataEmissao);
		this.setRgOrgaoEmissor(rgOrgaoEmissor);
		this.setRgUf(rgUf);
		this.setProfissao(profissaoId);
		this.setGenero(genero);
		this.setNacionalidade(nacionalidade);
		this.setNascimentoMunicipio(nascimentoMunicipio);
		this.setNascimentoPais(nascimentoPais);
		this.setNascimento(nascimento);
		this.setEscolaridade(escolaridade);
		this.setCertidaoCasamentoRegime(regimeCasamento);
		this.setNomeMaeConjuge(nomeMaeConjuge);
	}

	public PessoaRelacionamento(Serializable id) {
		super(id);
	}

	public PessoaRelacionamento(Serializable id, Relacionamento relacionamento, Pessoa pessoa, RelacionamentoFuncao relacionamentoFuncao) {
		this(id);
		this.setRelacionamento(relacionamento);
		this.setPessoa(pessoa);
		this.setRelacionamentoFuncao(relacionamentoFuncao);
	}

	public String getApelido() {
		return apelido;
	}

	public RegimeCasamento getCertidaoCasamentoRegime() {
		return certidaoCasamentoRegime;
	}

	public String getChaveSisater() {
		return chaveSisater;
	}

	public String getCpf() {
		return cpf;
	}

	public Escolaridade getEscolaridade() {
		return escolaridade;
	}

	public PessoaGenero getGenero() {
		return genero;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public PessoaNacionalidade getNacionalidade() {
		return nacionalidade;
	}

	public Calendar getNascimento() {
		return nascimento;
	}

	public Estado getNascimentoEstado() {
		return nascimentoEstado;
	}

	public Municipio getNascimentoMunicipio() {
		return nascimentoMunicipio;
	}

	public Pais getNascimentoPais() {
		return nascimentoPais;
	}

	public String getNome() {
		return nome;
	}

	public String getNomeMaeConjuge() {
		return nomeMaeConjuge;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public Profissao getProfissao() {
		return profissao;
	}

	public Relacionamento getRelacionamento() {
		return relacionamento;
	}

	public RelacionamentoFuncao getRelacionamentoFuncao() {
		return relacionamentoFuncao;
	}

	public Calendar getRgDataEmissao() {
		return rgDataEmissao;
	}

	public String getRgNumero() {
		return rgNumero;
	}

	public String getRgOrgaoEmissor() {
		return rgOrgaoEmissor;
	}

	public String getRgUf() {
		return rgUf;
	}

	public PessoaRelacionamentoInformadoTipo getTipoVinculoInformado() {
		return tipoVinculoInformado;
	}

	public void setApelido(String apelido) {
		this.apelido = apelido;
	}

	public void setCertidaoCasamentoRegime(RegimeCasamento certidaoCasamentoRegime) {
		this.certidaoCasamentoRegime = certidaoCasamentoRegime;
	}

	public void setChaveSisater(String chaveSisater) {
		this.chaveSisater = chaveSisater;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public void setEscolaridade(Escolaridade escolaridade) {
		this.escolaridade = escolaridade;
	}

	public void setGenero(PessoaGenero genero) {
		this.genero = genero;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNacionalidade(PessoaNacionalidade nacionalidade) {
		this.nacionalidade = nacionalidade;
	}

	public void setNascimento(Calendar nascimento) {
		this.nascimento = nascimento;
	}

	public void setNascimentoEstado(Estado nascimentoEstado) {
		this.nascimentoEstado = nascimentoEstado;
	}

	public void setNascimentoMunicipio(Municipio nascimentoMunicipio) {
		this.nascimentoMunicipio = nascimentoMunicipio;
	}

	public void setNascimentoPais(Pais nascimentoPais) {
		this.nascimentoPais = nascimentoPais;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setNomeMaeConjuge(String nomeMaeConjuge) {
		this.nomeMaeConjuge = nomeMaeConjuge;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public void setProfissao(Profissao profissao) {
		this.profissao = profissao;
	}

	public void setRelacionamento(Relacionamento relacionamento) {
		this.relacionamento = relacionamento;
	}

	public void setRelacionamentoFuncao(RelacionamentoFuncao relacionamentoFuncao) {
		this.relacionamentoFuncao = relacionamentoFuncao;
	}

	public void setRgDataEmissao(Calendar rgDataEmissao) {
		this.rgDataEmissao = rgDataEmissao;
	}

	public void setRgNumero(String rgNumero) {
		this.rgNumero = rgNumero;
	}

	public void setRgOrgaoEmissor(String rgOrgaoEmissor) {
		this.rgOrgaoEmissor = rgOrgaoEmissor;
	}

	public void setRgUf(String rgUf) {
		this.rgUf = rgUf;
	}

	public void setTipoVinculoInformado(PessoaRelacionamentoInformadoTipo tipoVinculoInformado) {
		this.tipoVinculoInformado = tipoVinculoInformado;
	}

	public void transformarInformacao(RelacionamentoFuncao relacionamentoFuncao, PessoaRelacionamentoInformadoTipo tipoVinculoInformado) throws BoException {
		if (this.getPessoa() == null && this.getTipoVinculoInformado() != null) {
			if (relacionamentoFuncao == null) {
				throw new BoException("Funcao não informada");
			}
			PessoaFisica pessoa = new PessoaFisica();
			pessoa.setNome(this.getNome());
			pessoa.setApelidoSigla(this.getApelido());
			pessoa.setCpf(this.getCpf());
			pessoa.setRgNumero(this.getRgNumero());
			pessoa.setRgDataEmissao(this.getRgDataEmissao());
			pessoa.setRgOrgaoEmissor(this.getRgOrgaoEmissor());
			pessoa.setRgUf(this.getRgUf());
			pessoa.setProfissao(this.getProfissao());
			pessoa.setGenero(this.getGenero());
			pessoa.setNacionalidade(this.getNacionalidade());
			pessoa.setNascimentoMunicipio(this.getNascimentoMunicipio());
			pessoa.setNascimentoPais(this.getNascimentoPais());
			pessoa.setNascimento(this.getNascimento());
			pessoa.setEscolaridade(this.getEscolaridade());
			pessoa.setCertidaoCasamentoRegime(this.getCertidaoCasamentoRegime());
			pessoa.setNomeMaeConjuge(this.getNomeMaeConjuge());

			// atribuir os novos dados
			this.setPessoa(pessoa);
			this.setRelacionamentoFuncao(relacionamentoFuncao);

			// limpar os dados movidos
			this.setNome(null);
			this.setApelido(null);
			this.setCpf(null);
			this.setRgNumero(null);
			this.setRgDataEmissao(null);
			this.setRgOrgaoEmissor(null);
			this.setRgUf(null);
			this.setProfissao(null);
			this.setGenero(null);
			this.setNacionalidade(null);
			this.setNascimentoMunicipio(null);
			this.setNascimentoPais(null);
			this.setNascimento(null);
			this.setEscolaridade(null);
			this.setCertidaoCasamentoRegime(null);
			this.setNomeMaeConjuge(null);
		} else if (this.getPessoa() != null && this.getTipoVinculoInformado() == null) {
			if (tipoVinculoInformado == null) {
				throw new BoException("Funcao não informada");
			}
			// atribuir os novos dados
			this.setTipoVinculoInformado(tipoVinculoInformado);
			this.setNome(((PessoaFisica) this.getPessoa()).getNome());
			this.setApelido(((PessoaFisica) this.getPessoa()).getApelidoSigla());
			this.setCpf(((PessoaFisica) this.getPessoa()).getCpf());
			this.setRgNumero(((PessoaFisica) this.getPessoa()).getRgNumero());
			this.setRgDataEmissao(((PessoaFisica) this.getPessoa()).getRgDataEmissao());
			this.setRgOrgaoEmissor(((PessoaFisica) this.getPessoa()).getRgOrgaoEmissor());
			this.setRgUf(((PessoaFisica) this.getPessoa()).getRgUf());
			this.setProfissao(((PessoaFisica) this.getPessoa()).getProfissao());
			this.setGenero(((PessoaFisica) this.getPessoa()).getGenero());
			this.setNacionalidade(((PessoaFisica) this.getPessoa()).getNacionalidade());
			this.setNascimentoMunicipio(((PessoaFisica) this.getPessoa()).getNascimentoMunicipio());
			this.setNascimentoPais(((PessoaFisica) this.getPessoa()).getNascimentoPais());
			this.setNascimento(((PessoaFisica) this.getPessoa()).getNascimento());
			this.setEscolaridade(((PessoaFisica) this.getPessoa()).getEscolaridade());
			this.setCertidaoCasamentoRegime(((PessoaFisica) this.getPessoa()).getCertidaoCasamentoRegime());
			this.setNomeMaeConjuge(((PessoaFisica) this.getPessoa()).getNomeMaeConjuge());

			// limpar os dados movidos
			this.setPessoa(null);
			this.setRelacionamentoFuncao(relacionamentoFuncao);
		} else {
			throw new BoException("Informações inválidas para fazer a conversão");
		}
	}

}