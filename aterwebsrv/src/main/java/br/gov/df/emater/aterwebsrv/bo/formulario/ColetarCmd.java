package br.gov.df.emater.aterwebsrv.bo.formulario;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;

@Service("FormularioColetarCmd")
public class ColetarCmd extends _Comando {

	// @Autowired
	// private ColetaDao dao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		// FormularioVersao formularioVersao = (FormularioVersao)
		// contexto.getRequisicao();

		// Coleta coleta = (Coleta) formularioVersao.getValor();
		// dao.save(coleta);
		// contexto.setResposta(coleta.getId());
		return false;
	}

}
