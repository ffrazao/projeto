package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaSituacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaTipo;

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

	public PessoaJuridica() {
		setPessoaTipo(PessoaTipo.PJ);
	}

	public PessoaJuridica(Integer id) {
		super(id);
		setPessoaTipo(PessoaTipo.PJ);
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

	@Override
	public Pessoa infoBasica() {
		return new PessoaJuridica(this.getId(), this.getNome(), this.getApelidoSigla(), this.getPerfilArquivo() == null ? null : this.getPerfilArquivo().infoBasica(), this.getSituacao(), this.getPublicoAlvoConfirmacao(), this.getCnpj());
	}

	public void setCnaeList(List<PessoaJuridicaCnae> cnaeList) {
		this.cnaeList = cnaeList;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = UtilitarioString.formataCnpj(cnpj);
	}

}