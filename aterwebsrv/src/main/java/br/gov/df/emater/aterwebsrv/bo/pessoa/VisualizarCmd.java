package br.gov.df.emater.aterwebsrv.bo.pessoa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaFisicaDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaJuridicaDao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;

@Service("PessoaVisualizarCmd")
public class VisualizarCmd extends _Comando {

	@Autowired
	private PessoaDao dao;

	@Autowired
	private PessoaFisicaDao pfDao;

	@Autowired
	private PessoaJuridicaDao pjDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Integer id = (Integer) contexto.getRequisicao();
		Pessoa pessoa = dao.getOne(id);
		switch (pessoa.getPessoaTipo()) {
		case PF:
			contexto.setResposta(pfDao.getOne(id));
			break;
		case PJ:
			contexto.setResposta(pjDao.getOne(id));
			break;
		}
		return true;
	}

}