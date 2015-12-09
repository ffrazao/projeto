package br.gov.df.emater.aterwebsrv.modelo;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.gov.df.emater.aterwebsrv.modelo.dominio.CadastroAcao;

// para marcar esta classe como o topo de hierarquia de entidades, porém não
// persiste informação
@MappedSuperclass
// para evitar o acesso recursivo as classes do conjunto de objetos serializados
// @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class,
// property = "@jsonId")
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property = "@jsonId")
public abstract class EntidadeBase implements Serializable {

	public static final String ATER_SCHEMA = "ater";

	public static final String ATIVIDADE_SCHEMA = "atividade";

	public static final String CAD_GERAL_SCHEMA = "cad_geral";

	public static final String ENQUETE_SCHEMA = "enquete";

	public static final String FUNCIONAL_SCHEMA = "funcional";

	public static final String INDICE_PRODUCAO_SCHEMA = "indice_producao";

	public static final String PESSOA_SCHEMA = "pessoa";

	private static final long serialVersionUID = 1L;

	public static final String SISTEMA_SCHEMA = "sistema";

	public static final String FORMULARIO_SCHEMA = "formulario";

	@Transient
	private CadastroAcao cadastroAcao;

	public EntidadeBase() {

	}

	@SuppressWarnings("unchecked")
	public EntidadeBase(Serializable id) {
		if (this instanceof _ChavePrimaria) {
			((_ChavePrimaria<Serializable>) this).setId(id);
		} else {
			throw new IllegalArgumentException(String.format("A classe %s não implementa _ChavePrimaria<Serializable>", this.getClass().getName()));
		}
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	public CadastroAcao getCadastroAcao() {
		return cadastroAcao;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public void setCadastroAcao(CadastroAcao cadastroAcao) {
		this.cadastroAcao = cadastroAcao;
	}

}
