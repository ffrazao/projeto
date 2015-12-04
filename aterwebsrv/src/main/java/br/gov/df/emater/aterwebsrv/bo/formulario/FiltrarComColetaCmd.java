package br.gov.df.emater.aterwebsrv.bo.formulario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.formulario.FormularioDao;
import br.gov.df.emater.aterwebsrv.modelo.dto.FormularioColetaCadFiltroDto;

@Service("FormularioFiltrarComColetaCmd")
public class FiltrarComColetaCmd extends _Comando {

	@Autowired
	private FormularioDao dao;

	@Override
	@SuppressWarnings("unchecked")
	public boolean executar(_Contexto contexto) throws Exception {
		List<Object[]> resposta = (List<Object[]>) contexto.getResposta();
		
		if (resposta == null || resposta.size() == 0) {
			return false;
		}
		
		FormularioColetaCadFiltroDto filtro = (FormularioColetaCadFiltroDto) contexto.getRequisicao();
		
		List<Object[]> result = new ArrayList<Object[]>();
		
		for (Object[] resp: resposta) {
			List<Object> linha = new ArrayList<Object>();
			linha.addAll(Arrays.asList(resp));
			filtro.setFormularioId((Integer) linha.get(0));
			linha.add(dao.filtrarComColeta(filtro));
			result.add(linha.toArray());
		}
		
		contexto.setResposta(result);

		return false;
	}
}