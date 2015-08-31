package br.gov.df.emater.aterwebsrv.ferramenta;

import java.util.Collection;

// Singleton utilitario para Strings
public class UtilitarioString {

	public static boolean isEmpty(String vlr) {
		return vlr == null || vlr.trim().length() == 0;
	}

	public static String collectionToString(Collection<?> objetos) {
		return collectionToString(objetos, false);
	}

	public static String collectionToString(Collection<?> objetos, boolean stringValue) {
		StringBuilder result = new StringBuilder();
		if (objetos == null) {
			result.append("null");
		} else {
			for (Object o : objetos) {
				if (result.length() > 0) {
					result.append(",");
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

	public static String zeroEsquerda(int num, int tam) {
		StringBuilder sb = new StringBuilder();
		sb.append(complemento('0', tam));
		sb.append(num);
		return sb.substring(sb.length() - tam, sb.length());
	}

	public static String zeroDireita(int num, int tam) {
		StringBuilder sb = new StringBuilder();
		sb.append(num);
		sb.append(complemento('0', tam));
		return sb.substring(0, tam);
	}

	public static String complemento(char c, int tam) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tam; i++) {
			sb.append(c);
		}
		return sb.toString();
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
	
	public static String formataCep(String cep) {
		StringBuilder sb = new StringBuilder();
		sb.append(complemento('0', 8));
		sb.append(soNumero(cep.trim()));
		return String.format("%s-%s", sb.substring(sb.length() - 8, sb.length() - 3), sb.substring(sb.length() - 3, sb.length()));
	}

}