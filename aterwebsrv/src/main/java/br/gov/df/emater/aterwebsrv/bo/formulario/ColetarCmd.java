package br.gov.df.emater.aterwebsrv.bo.formulario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.formulario.ColetaDao;
import br.gov.df.emater.aterwebsrv.modelo.formulario.FormularioVersao;

@Service("FormularioColetarCmd")
public class ColetarCmd extends _Comando {

	@Autowired
	private ColetaDao dao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		System.out.println("Filtrando formulario...");
		FormularioVersao formularioVersao = (FormularioVersao) contexto.getRequisicao();

//		Coleta coleta = (Coleta) formularioVersao.getValor();
//		dao.save(coleta);
//		contexto.setResposta(coleta.getId());
		return false;
	}

}
