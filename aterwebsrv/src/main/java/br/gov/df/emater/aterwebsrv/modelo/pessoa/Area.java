package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Polygon;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerGeometry;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerGeometry;

/**
 * The persistent class for the area database table.
 * 
 */
@Entity
@Table(name = "area", schema = EntidadeBase.PESSOA_SCHEMA)
public class Area extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "endereco_id")
	private Endereco endereco;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nome;

	@Type(type = "org.hibernate.spatial.GeometryType")
	@JsonDeserialize(using = JsonDeserializerGeometry.class)
	@JsonSerialize(using = JsonSerializerGeometry.class)
	private Polygon poligono;

	private BigDecimal tamanho;

	public Area() {
	}

	public Area(Serializable id) {
		super(id);
	}

	public Endereco getEndereco() {
		return endereco;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public Polygon getPoligono() {
		return poligono;
	}

	public BigDecimal getTamanho() {
		return tamanho;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setPoligono(Polygon poligono) {
		this.poligono = poligono;
	}

	public void setTamanho(BigDecimal tamanho) {
		this.tamanho = tamanho;
	}

}