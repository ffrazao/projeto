package br.gov.df.emater.aterwebsrv.modelo.dto;

public class TagDto {

	private String text;

	public TagDto() {

	}

	public TagDto(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
