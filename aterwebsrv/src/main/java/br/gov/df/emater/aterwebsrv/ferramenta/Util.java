package br.gov.df.emater.aterwebsrv.ferramenta;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

	private static final int[] pesoCNPJ = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };

	private static final int[] pesoCPF = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };

	private static int cnpCalcularDigito(String str, int[] peso) {
		int soma = 0;
		for (int indice = str.length() - 1, digito; indice >= 0; indice--) {
			digito = Integer.parseInt(str.substring(indice, indice + 1));
			soma += digito * peso[peso.length - str.length() + indice];
		}
		soma = 11 - soma % 11;
		return soma > 9 ? 0 : soma;
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

	public static boolean isEmailValido(String endereco) {
		if ((endereco == null) || (endereco.trim().length() == 0))
			return false;
		String emailPattern = "\\b(^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b";
		Pattern pattern = Pattern.compile(emailPattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(endereco);
		return matcher.matches();
	}

	public static boolean isCnpjValido(String numero) {
		if (numero == null) {
			return false;
		}
		numero = UtilitarioString.zeroEsquerda(UtilitarioString.soNumero(numero), 14);
		if (UtilitarioString.temCaractereRepetido(numero, 14)) {
			return false;
		}
		Integer digito1 = cnpCalcularDigito(numero.substring(0, 12), pesoCNPJ);
		Integer digito2 = cnpCalcularDigito(numero.substring(0, 12) + digito1, pesoCNPJ);
		return numero.equals(numero.substring(0, 12) + digito1.toString() + digito2.toString());
	}

	public static boolean isCpfValido(String numero) {
		if (numero == null) {
			return false;
		}
		numero = UtilitarioString.zeroEsquerda(UtilitarioString.soNumero(numero), 11);
		if (UtilitarioString.temCaractereRepetido(numero, 11)) {
			return false;
		}
		Integer digito1 = cnpCalcularDigito(numero.substring(0, 9), pesoCPF);
		Integer digito2 = cnpCalcularDigito(numero.substring(0, 9) + digito1, pesoCPF);
		return numero.equals(numero.substring(0, 9) + digito1.toString() + digito2.toString());
	}

	public static boolean isNisValido(String numero) {
		if (numero == null) {
			return false;
		}
		numero = UtilitarioString.soNumero(numero);
		if (numero.length() > 13) {
			return false;
		}
		numero = UtilitarioString.zeroEsquerda(numero, 11);
		if (UtilitarioString.temCaractereRepetido(numero, 11)) {
			return false;
		}
		Integer resto = nisCalcularDigito(numero);
		return numero.endsWith(resto.toString());
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

	private static Integer nisCalcularDigito(String nis) {
		nis = nis.substring(0, 11);
		int[] multiplicador = new int[] { 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };
		int soma;
		int resto;
		if (nis == null || nis.trim().length() == 0) {
			return null;
		}
		soma = 0;
		for (int i = 0; i < 10; i++) {
			soma += Character.getNumericValue(nis.charAt(i)) * multiplicador[i];
		}
		resto = soma % 11;
		if (resto < 2) {
			resto = 0;
		} else {
			resto = 11 - resto;
		}
		return resto;
	}

}
