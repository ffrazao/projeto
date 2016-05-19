package br.gov.df.emater.aterwebsrv.ferramenta;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class Util {

	private static final int[] PESO_CNPJ = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };

	private static final int[] PESO_CPF = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };

	private static final int[] PESO_NIS = { 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };

	private static final String STR_NUMERO_ALFABETO = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private static int calcularDigitoCnp(String str, int[] peso) {
		int soma = 0;
		for (int indice = str.length() - 1, digito; indice >= 0; indice--) {
			digito = Integer.parseInt(str.substring(indice, indice + 1));
			soma += digito * peso[peso.length - str.length() + indice];
		}
		soma = 11 - soma % 11;
		return soma > 9 ? 0 : soma;
	}

	private static Integer calcularDigitoNis(String nis) {
		// este cálculo foi extraído de http://www.macoratti.net/alg_pis.htm
		int result = 0, soma = 0;
		for (int i = 0; i < 10; i++) {
			soma += Character.getNumericValue(nis.charAt(i)) * PESO_NIS[i];
		}
		result = soma % 11;
		if (result < 2) {
			result = 0;
		} else {
			result = 11 - result;
		}
		return result;
	}

	private static String extraiNumeroProtocolo(String numero) {
		StringBuilder sb = new StringBuilder();
		for (char c : numero.toCharArray()) {
			int pos = STR_NUMERO_ALFABETO.indexOf(c);
			if (pos >= 0) {
				sb.append(UtilitarioString.zeroEsquerda(pos, 2));
			}
		}
		return sb.toString();
	}

	public static String gerarCodigoAtividade() {
		StringBuilder sb = new StringBuilder();
		Random r = new Random();
		for (int i = 0; i < 8; i++) {
			int j = r.nextInt(STR_NUMERO_ALFABETO.length());
			sb.append(STR_NUMERO_ALFABETO.charAt(j));
		}
		sb.append(modulo10(extraiNumeroProtocolo(sb.toString())));
		sb.insert(4, ".");
		sb.insert(9, "-");
		return sb.toString();
	}

	public static boolean isCnpjValido(String numero) {
		if (numero == null) {
			return false;
		}
		numero = UtilitarioString.zeroEsquerda(UtilitarioString.soNumero(numero), 14);
		if (UtilitarioString.temCaractereRepetido(numero, 14)) {
			return false;
		}
		Integer digito1 = calcularDigitoCnp(numero.substring(0, 12), PESO_CNPJ);
		Integer digito2 = calcularDigitoCnp(numero.substring(0, 12) + digito1, PESO_CNPJ);
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
		Integer digito1 = calcularDigitoCnp(numero.substring(0, 9), PESO_CPF);
		Integer digito2 = calcularDigitoCnp(numero.substring(0, 9) + digito1, PESO_CPF);
		return numero.equals(numero.substring(0, 9) + digito1.toString() + digito2.toString());
	}

	public static boolean isEmailValido(String endereco) {
		if (endereco == null || endereco.trim().length() == 0 || !(endereco.indexOf("@") > 0) || endereco.endsWith("@") || StringUtils.countMatches(endereco, "@") > 1) {
			return false;
		}
		String emailPattern = "\\b(^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b";
		Pattern pattern = Pattern.compile(emailPattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(endereco);
		return matcher.matches();
	}

	public static boolean isNisValido(String numero) {
		if (numero == null) {
			return false;
		}
		numero = UtilitarioString.soNumero(numero);
		if (numero.length() > 11) {
			return false;
		}
		numero = UtilitarioString.zeroEsquerda(numero, 11);
		if (UtilitarioString.temCaractereRepetido(numero, 11)) {
			return false;
		}
		Integer resto = calcularDigitoNis(numero);
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
}