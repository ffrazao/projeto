package br.gov.df.emater.aterwebsrv.bo;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.jpa.repository.JpaRepository;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

public abstract class _SalvarCmd extends _Comando {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void excluirRegistros(EntidadeBase entidade, String nomeLista, JpaRepository dao) {
		if (entidade.getExcluidoMap() != null && entidade.getExcluidoMap().get(nomeLista) != null) {
			entidade.getExcluidoMap().get(nomeLista).forEach((registro) -> dao.delete((Integer) registro));
		}
	}

	public void limparChavePrimaria(Collection<? extends _ChavePrimaria<? extends Serializable>> lista) {
		if (CollectionUtils.isEmpty(lista)) {
			return;
		}
		lista.stream().filter(e -> e.getId() != null && ((Integer) e.getId()) < 0).collect(Collectors.toList()).forEach(e -> e.setId(null));
	}

}
