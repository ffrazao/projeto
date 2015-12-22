package br.gov.df.emater.aterwebsrv.ferramenta;

import java.util.Random;

public class Util {

	public static String gerarCodigoAtividade() {
		String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder sb = new StringBuilder();
		Random r = new Random();
		for (int i = 0; i < 8; i++) {
			int j = r.nextInt(str.length());
			sb.append(str.charAt(j));
		}
		sb.append(modulo10(extraiNumeroProtocolo(sb.toString())));
		sb.insert(4, ".");
		sb.insert(9, "-");
		return sb.toString();
	}

	private static String extraiNumeroProtocolo(String numero) {
		String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder sb = new StringBuilder();
		for (char c : numero.toCharArray()) {
			int pos = str.indexOf(c);
			if (pos >= 0) {
				sb.append(UtilitarioString.zeroEsquerda(pos, 2));
			}
		}
		return sb.toString();
	}

	private static int modulo10(String numero) {
		// variáveis de instancia
		int soma = 0;
		int resto = 0;
		int dv = 0;
		String[] numeros = new String[numero.length() + 1];
		int multiplicador = 2;
		String aux;
		String aux2;
		String aux3;

		for (int i = numero.length(); i > 0; i--) {
			// Multiplica da direita pra esquerda, alternando os algarismos 2 e
			// 1
			if (multiplicador % 2 == 0) {
				// pega cada numero isoladamente
				numeros[i] = String.valueOf(Integer.valueOf(numero.substring(i - 1, i)) * 2);
				multiplicador = 1;
			} else {
				numeros[i] = String.valueOf(Integer.valueOf(numero.substring(i - 1, i)) * 1);
				multiplicador = 2;
			}
		}

		// Realiza a soma dos campos de acordo com a regra
		for (int i = (numeros.length - 1); i > 0; i--) {
			aux = String.valueOf(Integer.valueOf(numeros[i]));

			if (aux.length() > 1) {
				aux2 = aux.substring(0, aux.length() - 1);
				aux3 = aux.substring(aux.length() - 1, aux.length());
				numeros[i] = String.valueOf(Integer.valueOf(aux2) + Integer.valueOf(aux3));
			} else {
				numeros[i] = aux;
			}
		}

		// Realiza a soma de todos os elementos do array e calcula o digito
		// verificador
		// na base 10 de acordo com a regra.
		for (int i = numeros.length; i > 0; i--) {
			if (numeros[i - 1] != null) {
				soma += Integer.valueOf(numeros[i - 1]);
			}
		}
		resto = soma % 10;
		dv = 10 - resto;
		if (dv == 10) {
			dv = 0;
		}

		// retorna o digito verificador
		return dv;
	}

}
