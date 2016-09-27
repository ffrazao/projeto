package br.gov.df.emater.aterwebsrv.modelo;

import java.util.ArrayList;
import java.util.List;

public class UtilitarioInfoBasica {

	public static <T extends InfoBasica<? extends T>> T infoBasicaReg(final T registro) {
		return registro == null ? null : (T) registro.infoBasica();
	}

	public static <T extends InfoBasica<? extends T>> List<T> infoBasicaList(final List<T> lista) {
		List<T> result = null;
		if (lista != null) {
			for (T registro : lista) {
				if (result == null) {
					result = new ArrayList<>();
				}
				result.add(registro == null ? null : (T) registro.infoBasica());
			}
		}
		return result;
	}

}