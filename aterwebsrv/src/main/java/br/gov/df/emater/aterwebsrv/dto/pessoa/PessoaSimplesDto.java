package br.gov.df.emater.aterwebsrv.dto.pessoa;

import br.gov.df.emater.aterwebsrv.modelo.pessoa.Relacionamento;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.RelacionamentoFuncao;

public class PessoaSimplesDto {
	
	private String apelidoSigla;
	
	private String chaveSisater;
	
	private String genero;
	
	private Integer id;
	
	private String nome;
	
	private Relacionamento relacionamento;
	
	private RelacionamentoFuncao relacionamentoFuncao;
	
	private String cpf;
	
	private String RgNumero;

	public String getApelidoSigla() {
		return apelidoSigla;
	}

	public void setApelidoSigla(String apelidoSigla) {
		this.apelidoSigla = apelidoSigla;
	}

	public String getChaveSisater() {
		return chaveSisater;
	}

	public void setChaveSisater(String chaveSisater) {
		this.chaveSisater = chaveSisater;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Relacionamento getRelacionamento() {
		return relacionamento;
	}

	public void setRelacionamento(Relacionamento relacionamento) {
		this.relacionamento = relacionamento;
	}

	public RelacionamentoFuncao getRelacionamentoFuncao() {
		return relacionamentoFuncao;
	}

	public void setRelacionamentoFuncao(RelacionamentoFuncao relacionamentoFuncao) {
		this.relacionamentoFuncao = relacionamentoFuncao;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getRgNumero() {
		return RgNumero;
	}

	public void setRgNumero(String rgNumero) {
		RgNumero = rgNumero;
	}
	
	

}