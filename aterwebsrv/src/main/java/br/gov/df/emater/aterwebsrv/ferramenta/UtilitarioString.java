package br.gov.df.emater.aterwebsrv.ferramenta;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

import org.apache.commons.lang3.text.WordUtils;

// Singleton utilitario para Strings
public class UtilitarioString {

	public static String calculaNomeUsuario(String nome) {
		if (nome == null) {
			return "";
		}
		String[] temp1 = semAcento(nome.toLowerCase()).split("\\s");
		StringBuilder temp2 = new StringBuilder();
		StringBuilder temp3 = new StringBuilder();
		temp2.append(temp1[0]);
		if (temp1.length > 1) {
			temp2.append(".");
			temp2.append(temp1[temp1.length - 1]);
		}
		for (char c : temp2.toString().toCharArray()) {
			if ((c >= 'a' && c <= 'z') || (c == '.')) {
				temp3.append(c);
			}
		}
		return temp3.toString();
	}

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

	public static String semAcento(String str) {
		String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(nfdNormalizedString).replaceAll("");
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

	public static boolean temCaractereRepetido(String str) {
		// TODO Auto-generated method stub
		return temCaractereRepetido(str, 2);
	}

	public static boolean temCaractereRepetido(String str, int vezes) {
		if (str == null || str.length() < vezes) {
			return false;
		}
		StringBuilder repete = new StringBuilder();
		int cont = 0;
		for (char c : str.toCharArray()) {
			if (repete.length() > 0 && c == repete.charAt(repete.length() - 1)) {
				cont++;
			} else {
				cont = 0;
			}
			repete.append(c);
			if (cont + 1 == vezes) {
				return true;
			}
		}

		return false;
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

	public static String limitarTextoEm(String texto, int tamanho) {
		if (texto == null) {
			return null;
		}
		String result = null;
		if (texto.length() > tamanho) {
			result = texto.substring(0, tamanho);
		} else {
			result = texto;
		}
		return result;
	}

	public static String formataNomeProprio(String nome) {
		if (nome == null) {
			return null;
		}
		nome = WordUtils.capitalizeFully(nome.trim(), new char[] {'\'', ' ', '-', '.', '\"' , '('});
		StringBuilder result = new StringBuilder();
		for (String palavra: nome.split("\\s")) {
			if (result.length() > 0) {
				result.append(' ');
			}
			if (Arrays.asList(new String[] { "de", "da", "do", "das", "dos", "e" }).contains(palavra.toLowerCase())) {
				result.append(palavra.toLowerCase());
			} else {
				result.append(palavra);
			}
		}
		return result.toString();
	}

}