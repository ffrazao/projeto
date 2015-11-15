package br.gov.df.emater.aterwebsrv.ferramenta;

import java.util.Collection;
import java.util.regex.Pattern;

// Singleton utilitario para Strings
public class UtilitarioString {

	public static String collectionToString(Collection<?> objetos) {
		return collectionToString(objetos, null);
	}

	public static String collectionToString(Collection<?> objetos, String delimitador) {
		return collectionToString(objetos, delimitador, false, true);
	}

	public static String collectionToString(Collection<?> objetos, String delimitador, boolean stringValue, boolean ignoreNull) {
		StringBuilder result = new StringBuilder();
		if (objetos == null) {
			if (ignoreNull) {
				return null;
			} else {
				result.append("null");
			}
		} else {
			for (Object o : objetos) {
				if (ignoreNull && o == null) {
					continue;
				}
				if (result.length() > 0) {
					result.append(delimitador == null ? ", " : delimitador);
				}
				if (stringValue) {
					result.append("\'").append(o).append("\'");
				} else {
					result.append(o);
				}
			}
		}
		return result.toString();
	}

	public static String complemento(char c, int tam) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tam; i++) {
			sb.append(c);
		}
		return sb.toString();
	}

	public static String formataCep(String cep) {
		if (cep == null) {
			return null;
		}
		cep = zeroEsquerda(soNumero(cep.trim()), 8);
		return Pattern.compile("(\\d{2})(\\d{3})(\\d{3})").matcher(cep).replaceAll("$1.$2-$3");
	}

	public static String formataCnpj(String cnpj) {
		if (cnpj == null) {
			return null;
		}
		cnpj = zeroEsquerda(soNumero(cnpj.trim()), 14);
		return Pattern.compile("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})").matcher(cnpj).replaceAll("$1.$2.$3/$4-$5");
	}

	public static String formataCpf(String cpf) {
		if (cpf == null) {
			return null;
		}
		cpf = zeroEsquerda(soNumero(cpf.trim()), 11);
		return Pattern.compile("(\\d{3})(\\d{3})(\\d{3})(\\d{2})").matcher(cpf).replaceAll("$1.$2.$3-$4");
	}

	public static boolean isEmpty(String vlr) {
		return vlr == null || vlr.trim().length() == 0;
	}

	public static String removeAspas(String valor) {
		// remover as aspas da string
		if (valor.startsWith("\"")) {
			valor = valor.substring(1);
		}
		if (valor.endsWith("\"")) {
			valor = valor.substring(0, valor.length() - 1);
		}
		return valor;
	}

	public static String soNumero(String numero) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < numero.length(); i++) {
			if (Character.isDigit(numero.charAt(i))) {
				sb.append(numero.charAt(i));
			}
		}
		return sb.toString();
	}

	public static String zeroDireita(int num, int tam) {
		return zeroDireita(String.valueOf(num), tam);
	}

	public static String zeroDireita(String num, int tam) {
		StringBuilder sb = new StringBuilder();
		sb.append(num);
		sb.append(complemento('0', tam));
		return sb.substring(0, tam);
	}

	public static String zeroEsquerda(int num, int tam) {
		return zeroEsquerda(String.valueOf(num), tam);
	}

	public static String zeroEsquerda(String num, int tam) {
		StringBuilder sb = new StringBuilder();
		sb.append(complemento('0', tam));
		sb.append(num);
		return sb.substring(sb.length() - tam, sb.length());
	}

}