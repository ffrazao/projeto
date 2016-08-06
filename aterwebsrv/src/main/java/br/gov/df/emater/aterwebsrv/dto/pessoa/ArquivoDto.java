package br.gov.df.emater.aterwebsrv.dto.pessoa;

public class ArquivoDto {

	private String arquivo;

	private String extensao;

	private String md5;

	public ArquivoDto() {

	}

	public ArquivoDto(String arquivo, String md5, String extensao) {
		super();
		this.arquivo = arquivo;
		this.md5 = md5;
		this.extensao = extensao;
	}

	public String getArquivo() {
		return arquivo;
	}

	public String getExtensao() {
		return extensao;
	}

	public String getMd5() {
		return md5;
	}

	public void setArquivo(String arquivo) {
		this.arquivo = arquivo;
	}

	public void setExtensao(String extensao) {
		this.extensao = extensao;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}
}