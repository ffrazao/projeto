package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;

@Entity
@Table(name = "pessoa_arquivo", schema = EntidadeBase.PESSOA_SCHEMA)
public class PessoaArquivo extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "arquivo_id")
	private Arquivo arquivo;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Enumerated(EnumType.STRING)
	private Confirmacao perfil;

	@ManyToOne
	@JoinColumn(name = "pessoa_id")
	private Pessoa pessoa;

	public PessoaArquivo() {
	}

	public Arquivo getArquivo() {
		return arquivo;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Confirmacao getPerfil() {
		return perfil;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setPerfil(Confirmacao perfil) {
		this.perfil = perfil;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

}
