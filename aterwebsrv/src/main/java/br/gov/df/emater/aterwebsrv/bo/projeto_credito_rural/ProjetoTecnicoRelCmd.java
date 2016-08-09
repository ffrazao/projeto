package br.gov.df.emater.aterwebsrv.bo.projeto_credito_rural;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.projeto_credito_rural.ProjetoCreditoRuralDao;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRural;
import br.gov.df.emater.aterwebsrv.relatorio._Relatorio;

@Service("ProjetoCreditoRuralProjetoTecnicoRelCmd")
public class ProjetoTecnicoRelCmd extends _Comando {

	@Autowired
	private ProjetoCreditoRuralDao dao;

	@Autowired
	private _Relatorio relatorio;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Integer[] idList = (Integer[]) contexto.getRequisicao();
		
		if (ArrayUtils.isEmpty(idList)) {
			throw new BoException("Nenhum projeto informado");
		}

		List<ProjetoCreditoRural> lista = dao.findAll(Arrays.asList(idList));

		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("RelatorioNome", "PROJETO TÉCNICO");
		parametros.put("Dados", lista);

		byte[] result = relatorio.imprimir("projeto_credito_rural/ProjetoCreditoRuralRel", parametros, lista);

		contexto.setResposta(result);

		return false;
	}

}
