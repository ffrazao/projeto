package br.gov.df.emater.aterwebsrv.bo.formulario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.formulario.ColetaDao;
import br.gov.df.emater.aterwebsrv.modelo.formulario.Coleta;

@Service("FormularioColetarCmd")
public class ColetarCmd extends _Comando {

	@Autowired
	private ColetaDao dao;

	private ObjectMapper om = new ObjectMapper();

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Coleta coleta = (Coleta) contexto.getRequisicao();
		coleta.setValorString(coleta.getValor() == null ? null : om.writeValueAsString(coleta.getValor()));
		if( coleta.getId()==null){
			coleta.setInclusaoUsuario(null);
		}
		logAtualizar(coleta, contexto);
		dao.save(coleta);
		contexto.setResposta(coleta.getId());
		return false;
	}

}