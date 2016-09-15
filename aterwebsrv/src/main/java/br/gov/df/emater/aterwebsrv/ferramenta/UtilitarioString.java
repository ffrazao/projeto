package br.gov.df.emater.aterwebsrv.ferramenta;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.text.WordUtils;

// Singleton utilitario para Strings
public class UtilitarioString {

	public static final int[] DDD_TELEFONE = new int[] { 11, 12, 13, 14, 15, 16, 17, 18, 19, 21, 22, 24, 27, 28, 31, 32, 33, 34, 35, 37, 38, 41, 42, 43, 44, 45, 46, 47, 48, 49, 51, 53, 54, 55, 61, 62, 63, 64, 65, 66, 67, 68, 69, 71, 73, 74, 75, 77, 79, 81, 82, 83, 84, 85, 86, 87,
			88, 89, 91, 92, 93, 94, 95, 96, 97, 98, 99 };

	private static NumberFormat inteiroFormatter = NumberFormat.getNumberInstance();

	private static NumberFormat moedaFormatter = NumberFormat.getCurrencyInstance();

	private static NumberFormat numeroFormatter = NumberFormat.getNumberInstance();

	private static NumberFormat percentualFormatter = NumberFormat.getPercentInstance();

	public static String calculaNomeUsuario(String nome) {
		if (StringUtils.isBlank(nome)) {
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
		if (CollectionUtils.isEmpty(objetos)) {
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

	public static String formataCep(String numero) {
		if (StringUtils.isBlank(numero)) {
			return null;
		}
		numero = zeroEsquerda(soNumero(numero.trim()), 8);
		return Pattern.compile("(\\d{2})(\\d{3})(\\d{3})").matcher(numero).replaceAll("$1.$2-$3");
	}

	public static String formataCnpj(String numero) {
		if (StringUtils.isBlank(numero)) {
			return null;
		}
		numero = zeroEsquerda(soNumero(numero.trim()), 14);
		return Pattern.compile("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})").matcher(numero).replaceAll("$1.$2.$3/$4-$5");
	}

	public static String formataCpf(String numero) {
		if (StringUtils.isBlank(numero)) {
			return null;
		}
		numero = zeroEsquerda(soNumero(numero.trim()), 11);
		return Pattern.compile("(\\d{3})(\\d{3})(\\d{3})(\\d{2})").matcher(numero).replaceAll("$1.$2.$3-$4");
	}

	public static String formataEmail(String endereco) {
		if (StringUtils.isBlank(endereco)) {
			return null;
		}
		endereco = UtilitarioString.substituirTudo(UtilitarioString.semAcento(endereco.trim().toLowerCase()), " ", "");
		if (endereco.trim().length() == 0) {
			return null;
		}
		return endereco;
	}

	public static String formataInteiro(BigDecimal numero) {
		String result = inteiroFormatter.format(numero.doubleValue());
		return result;
	}

	public static String formataMoeda(BigDecimal numero) {
		String result = moedaFormatter.format(numero.doubleValue());
		return result;
	}

	public static String formataNis(String numero) {
		if (StringUtils.isBlank(numero)) {
			return null;
		}
		numero = zeroEsquerda(soNumero(numero.trim()), 11);
		// este formato foi encontrado em http://www.geradorpis.com/
		return Pattern.compile("(\\d{3})(\\d{4})(\\d{3})(\\d{1})").matcher(numero).replaceAll("$1.$2.$3-$4");
	}

	public static String formataNomeProprio(String nome) {
		if (StringUtils.isBlank(nome)) {
			return null;
		}
		nome = WordUtils.capitalizeFully(nome.trim(), new char[] { '\'', ' ', '-', '.', '\"', '(' });
		StringBuilder result = new StringBuilder();
		for (String palavra : nome.split("\\s")) {
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

	public static String formataNumero(BigDecimal numero) {
		String result = numeroFormatter.format(numero.doubleValue());
		return result;
	}

	public static String formataPercentual(BigDecimal numero) {
		String result = percentualFormatter.format(numero.doubleValue());
		return result;
	}

	public static String formataTelefone(String numero) {
		if (StringUtils.isBlank(numero)) {
			return null;
		}
		class Check {
			public String[] numeroComDddEntreParenteses(String numero) {
				numero = numero.trim();
				if (numero.startsWith("(") && numero.indexOf(")") > 0) {
					String ddd = soNumero(numero.substring(1, numero.indexOf(")")));
					numero = soNumero(numero.substring(numero.indexOf(")") + 1, numero.length()));
					return numeroComDddTudoJunto(ddd + numero);
				}
				return null;
			}

			String[] numeroComDddSeparado(String numero, int posicao) {
				String[] result = null;
				if (numero.length() - 1 < posicao) {
					return null;
				}
				switch (numero.charAt(posicao)) {
				case ' ':
				case '-':
				case '.':
					if (NumberUtils.isDigits(numero.substring(0, posicao))) {
						result = new String[2];
						result[0] = numero.substring(0, posicao);
						result[1] = numero.substring(posicao, numero.length());
						return numeroComDddTudoJunto(result[0] + result[1]);
					}
				}
				return null;
			}

			public String[] numeroComDddTudoJunto(String numero) {
				numero = soNumero(numero);
				if (numero.startsWith("0") && (numero.length() == 11 || numero.length() == 12) && ArrayUtils.contains(DDD_TELEFONE, Integer.parseInt(numero.substring(1, 3)))) {
					return new String[] { numero.substring(1, 3), numero.substring(3, numero.length()) };
				}
				if ((numero.length() == 10 || numero.length() == 11) && ArrayUtils.contains(DDD_TELEFONE, Integer.parseInt(numero.substring(0, 2)))) {
					return new String[] { numero.substring(0, 2), numero.substring(2, numero.length()) };
				}
				return null;
			}

			public String[] numeroSemDdd(String numero) {
				numero = soNumero(numero);
				if (numero.length() == 8 || numero.length() == 9) {
					return numeroComDddTudoJunto("61" + numero);
				}
				return null;
			}
		}
		Check check = new Check();
		// verificar se foi informado o ddd no numero
		String[] num = check.numeroSemDdd(numero);
		if (num == null) {
			num = check.numeroComDddSeparado(numero, 2);
		}
		if (num == null) {
			num = check.numeroComDddSeparado(numero, 3);
		}
		if (num == null) {
			num = check.numeroComDddEntreParenteses(numero);
		}
		if (num == null) {
			num = check.numeroComDddTudoJunto(numero);
		}
		if (num != null) {
			int pos = 4;
			if (num[1].length() == 9) {
				pos++;
			}
			return String.format("(%s) %s-%s", num[0], num[1].substring(0, pos), num[1].substring(pos, num[1].length()));
		}
		return numero;
	}

	public static boolean isEmpty(String vlr) {
		return StringUtils.isBlank(vlr);
	}

	public static String limitarTextoEm(String texto, int tamanho) {
		if (StringUtils.isBlank(texto)) {
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

	public static String removeAspas(String valor) {
		if (StringUtils.isBlank(valor)) {
			return null;
		}
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
		if (StringUtils.isBlank(str)) {
			return null;
		}
		String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(nfdNormalizedString).replaceAll("");
	}

	public static String soNumero(String numero, char... ignorar) {
		if (StringUtils.isBlank(numero)) {
			return null;
		}
		String ignorarStr = new String(ignorar);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < numero.length(); i++) {
			if (Character.isDigit(numero.charAt(i)) || ignorarStr.indexOf(numero.charAt(i)) >= 0) {
				sb.append(numero.charAt(i));
			}
		}
		return sb.toString();
	}

	public static String substituirTudo(String str, String encontrar, String substituir) {
		if (StringUtils.isBlank(str)) {
			return null;
		}
		while (str.indexOf(encontrar) >= 0) {
			str = str.replaceAll(encontrar, substituir);
		}
		return str;
	}

	public static boolean temCaractereRepetido(String str) {
		if (StringUtils.isBlank(str)) {
			return false;
		}
		return temCaractereRepetido(str, 2);
	}

	public static boolean temCaractereRepetido(String str, int vezes) {
		if (StringUtils.isBlank(str) || str.length() < vezes) {
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
		if (StringUtils.isBlank(num)) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(num);
		sb.append(complemento('0', tam));
		return sb.substring(0, tam);
	}

	public static String zeroEsquerda(int num, int tam) {
		return zeroEsquerda(String.valueOf(num), tam);
	}

	public static String zeroEsquerda(String num, int tam) {
		if (StringUtils.isBlank(num)) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(complemento('0', tam));
		sb.append(num);
		return sb.substring(sb.length() - tam, sb.length());
	}

}