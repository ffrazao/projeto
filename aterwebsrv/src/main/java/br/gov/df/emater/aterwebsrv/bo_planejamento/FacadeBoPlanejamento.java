package br.gov.df.emater.aterwebsrv.bo_planejamento;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dto.ater.PropriedadeRuralCadFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.ater.PublicoAlvoPropriedadeRuralCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;

@Service
public class FacadeBoPlanejamento extends FacadeBo {

	@Transactional
	public _Contexto propriedadeRuralExcluir(Principal usuario, Integer id) throws Exception {
		return this._executar(usuario, "PropriedadeRuralExcluirCh", id);
	}

	@Transactional(readOnly = true)
	public _Contexto propriedadeRuralFiltrarPorPublicoAlvo(Principal usuario, List<PublicoAlvo> publicoAlvoList) throws Exception {
		return this._executar(usuario, "PropriedadeRuralFiltrarPorPublicoAlvoCmd", publicoAlvoList);
	}

	@Transactional(readOnly = true)
	public _Contexto propriedadeRuralFiltrarPorPublicoAlvoPropriedadeRuralComunidade(Principal usuario, PublicoAlvoPropriedadeRuralCadFiltroDto filtro) throws Exception {
		return this._executar(usuario, "PropriedadeRuralFiltrarPorPublicoAlvoPropriedadeRuralComunidadeCmd", filtro);
	}

	@Transactional(readOnly = true)
	public _Contexto propriedadeRuralFiltroExecutar(Principal usuario, PropriedadeRuralCadFiltroDto filtro) throws Exception {
		return this._executar(usuario, "PropriedadeRuralFiltroExecutarCh", filtro);
	}

	@Transactional(readOnly = true)
	public _Contexto propriedadeRuralFiltroNovo(Principal usuario) throws Exception {
		return this._executar(usuario, "PropriedadeRuralFiltroNovoCmd");
	}

	@Transactional(readOnly = true)
	public _Contexto propriedadeRuralNovo(Principal usuario) throws Exception {
		return this._executar(usuario, "PropriedadeRuralNovoCh");
	}

	@Transactional
	public _Contexto propriedadeRuralSalvar(Principal usuario, PropriedadeRural propriedadeRural) throws Exception {
		return this._executar(usuario, "PropriedadeRuralSalvarCh", propriedadeRural);
	}

	@Transactional(readOnly = true)
	public _Contexto propriedadeRuralVisualizar(Principal usuario, Integer id) throws Exception {
		return this._executar(usuario, "PropriedadeRuralVisualizarCh", id);
	}

}