package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaSituacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaTipo;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

/**
 * The persistent class for the pessoa_juridica database table.
 * 
 */
@Entity
@Table(name = "pessoa_juridica", schema = EntidadeBase.PESSOA_SCHEMA)
@PrimaryKeyJoinColumn(name = "id")
// @Indexed
public class PessoaJuridica extends Pessoa {

	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "pessoaJuridica")
	private List<PessoaJuridicaCnae> cnaeList;

	// @Field(index = Index.YES, store = Store.YES)
	private String cnpj;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar fundacao;

	public PessoaJuridica() {
		this(null, null, null, null, null, null, null);
	}

	public PessoaJuridica(Integer id) {
		this(id, null, null, null, null, null, null);
	}

	public PessoaJuridica(Integer id, String nome, String apelidoSigla, Arquivo perfilArquivo, PessoaSituacao situacao, Confirmacao publicoAlvoConfirmacao, String cnpj) {
		super(id, PessoaTipo.PJ, nome, apelidoSigla, perfilArquivo, situacao, publicoAlvoConfirmacao);
		setCnpj(cnpj);
	}

	public List<PessoaJuridicaCnae> getCnaeList() {
		return cnaeList;
	}

	public String getCnpj() {
		return cnpj;
	}

	public Calendar getFundacao() {
		return fundacao;
	}

	@Override
	public Pessoa infoBasica() {
		return new PessoaJuridica(this.getId(), this.getNome(), this.getApelidoSigla(), this.getPerfilArquivo() == null ? null : this.getPerfilArquivo().infoBasica(), this.getSituacao(), this.getPublicoAlvoConfirmacao(), this.getCnpj());
	}

	public void setCnaeList(List<PessoaJuridicaCnae> cnaeList) {
		this.cnaeList = cnaeList;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public void setFundacao(Calendar fundacao) {
		this.fundacao = fundacao;
	}

}