package br.gov.df.emater.aterwebsrv.modelo.dto;

public class TagDto {
	
	public TagDto() {
		
	}
	
	public TagDto(String text) {
		this.text = text;
	}
	
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
