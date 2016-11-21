package br.gov.df.emater.aterwebsrv.dto.agenda;

import java.util.Calendar;

import br.gov.df.emater.aterwebsrv.dto.CadFiltroDtoCustom;

public class AgendaDto extends CadFiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private String[] className;

	private String detalhamento;

	private Calendar end;

	private Integer id;

	private Integer metodoId;

	private String metodoNome;

	private Integer pessoaId;

	private String pessoaNome;

	private Calendar start;

	private String title;

	public AgendaDto(Integer id, String title, Calendar start, Calendar end, String[] className, Integer metodoId, String metodoNome, Integer pessoaId, String pessoaNome, String detalhamento) {
		super();
		this.id = id;
		this.title = title;
		this.start = start;
		this.end = end;
		this.className = className;
		this.metodoId = metodoId;
		this.metodoNome = metodoNome;
		this.pessoaId = pessoaId;
		this.pessoaNome = pessoaNome;
		this.detalhamento = detalhamento;
	}

	public String[] getClassName() {
		return className;
	}

	public String getDetalhamento() {
		return detalhamento;
	}

	public Calendar getEnd() {
		return end;
	}

	public Integer getId() {
		return id;
	}

	public Integer getMetodoId() {
		return metodoId;
	}

	public String getMetodoNome() {
		return metodoNome;
	}

	public Integer getPessoaId() {
		return pessoaId;
	}

	public String getPessoaNome() {
		return pessoaNome;
	}

	public Calendar getStart() {
		return start;
	}

	public String getTitle() {
		return title;
	}

	public void setClassName(String[] className) {
		this.className = className;
	}

	public void setDetalhamento(String detalhamento) {
		this.detalhamento = detalhamento;
	}

	public void setEnd(Calendar end) {
		this.end = end;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setMetodoId(Integer metodoId) {
		this.metodoId = metodoId;
	}

	public void setMetodoNome(String metodoNome) {
		this.metodoNome = metodoNome;
	}

	public void setPessoaId(Integer pessoaId) {
		this.pessoaId = pessoaId;
	}

	public void setPessoaNome(String pessoaNome) {
		this.pessoaNome = pessoaNome;
	}

	public void setStart(Calendar start) {
		this.start = start;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}