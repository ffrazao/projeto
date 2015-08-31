package br.gov.df.emater.aterwebsrv.modelo.teste;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "teste", schema = "teste")
public class Teste {
	
	public Teste() {
		
	}
	
	public Teste(Long id) {
		setId(id);
	}

	public Teste(Long id, String nome) {
		this(id);
		setNome(nome);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "nome")
	private String nome;

	@Version
	private Long version;

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public Long getVersion() {
		return version;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}
