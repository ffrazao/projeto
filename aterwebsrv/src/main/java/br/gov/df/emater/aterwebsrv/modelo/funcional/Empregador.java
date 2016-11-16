package br.gov.df.emater.aterwebsrv.modelo.funcional;

import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaReg;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaSituacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Arquivo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;

@Entity
@Table(name = "empregador", schema = EntidadeBase.FUNCIONAL_SCHEMA)
@PrimaryKeyJoinColumn(name = "id")
public class Empregador extends PessoaJuridica {

	private static final long serialVersionUID = 1L;

	public Empregador() {
		super();
	}

	public Empregador(Integer id, String nome, String apelidoSigla, Arquivo perfilArquivo, PessoaSituacao situacao, Confirmacao publicoAlvoConfirmacao, String cnpj) {
		super(id, nome, apelidoSigla, perfilArquivo, situacao, publicoAlvoConfirmacao, cnpj);
	}

	public Empregador(Integer id) {
		super(id);
	}
	
	@Override
	public Empregador infoBasica() {
		return new Empregador(getId(), getNome(), getApelidoSigla(), infoBasicaReg(getPerfilArquivo()), getSituacao(), getPublicoAlvoConfirmacao(), getCnpj());
	}

}