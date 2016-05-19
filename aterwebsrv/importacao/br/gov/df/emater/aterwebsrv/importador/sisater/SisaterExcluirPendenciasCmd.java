package br.gov.df.emater.aterwebsrv.importador.sisater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ater.PropriedadeRuralPendenciaDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaPendenciaDao;

@Service
public class SisaterExcluirPendenciasCmd extends _Comando {

	@Autowired
	private PessoaPendenciaDao pessoaPendenciaDao;

	@Autowired
	private PropriedadeRuralPendenciaDao propriedadeRuralPendenciaDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		pessoaPendenciaDao.deleteAll();
		propriedadeRuralPendenciaDao.deleteAll();
		return false;
	}

}