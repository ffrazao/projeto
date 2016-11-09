package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoProprietario;

@Service("IndiceProducaoSalvarDescendenteCmd")
public class SalvarDescendenteCmd extends _Comando {

	@Autowired
	private FacadeBo facadeBo;

	public SalvarDescendenteCmd() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		List<ProducaoProprietario> producaoProprietarioList = (List<ProducaoProprietario>) contexto.get("producaoProprietarioList");

		if (producaoProprietarioList != null) {
			for (ProducaoProprietario producaoProprietario : producaoProprietarioList) {
				facadeBo.indiceProducaoSalvarDescendente(contexto.getUsuario(), producaoProprietario);
			}
		}

		return false;
	}

}