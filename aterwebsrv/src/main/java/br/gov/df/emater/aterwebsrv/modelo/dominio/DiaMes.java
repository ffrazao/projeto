package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum DiaMes {

	_01("Primeiro"), _02("Segundo"), _03("Terceiro"), _04("Quarto"), _05("Quinto"), _06("Sexto"), _07("S�timo"), _08("Oitavo"), _09("Nono"), _10("D�cimo"), _11("D�cimo Primeiro"), _12("D�cimo Segundo"), _13("D�cimo Terceiro"), _14("D�cimo Quarto"), _15("D�cimo Quinto"), _16(
			"D�cimo Sexto"), _17("D�cimo S�timo"), _18("D�cimo Oitavo"), _19("D�cimo Nono"), _20("Vig�simo"), _21("Vig�simo Primeiro"), _22("Vig�simo Segundo"), _23("Vig�simo Terceiro"), _24("Vig�simo Quarto"), _25("Vig�simo Quinto"), _26("Vig�simo Sexto"), _27("Vig�simo S�timo"), _28(
			"Vig�simo Oitavo"), _29("Vig�simo Nono"), _30("Trig�simo"), _31("Trig�simo Primeiro");

	private String descricao;

	private DiaMes(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}