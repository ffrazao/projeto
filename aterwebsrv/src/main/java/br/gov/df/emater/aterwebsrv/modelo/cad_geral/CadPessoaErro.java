package br.gov.df.emater.aterwebsrv.modelo.cad_geral;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

@Entity
@Table(name = "cad_pessoa_erro", schema = EntidadeBase.CAD_GERAL_SCHEMA)
public class CadPessoaErro extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "cad_pessoa_id")
	private CadPessoa cadPessoa;

	@Column(name = "erro")
	private String erro;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	public CadPessoaErro() {
	}

	public CadPessoa getCadPessoa() {
		return cadPessoa;
	}

	public String getErro() {
		return erro;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setCadPessoa(CadPessoa cadPessoa) {
		this.cadPessoa = cadPessoa;
	}

	public void setErro(String erro) {
		this.erro = erro;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

}