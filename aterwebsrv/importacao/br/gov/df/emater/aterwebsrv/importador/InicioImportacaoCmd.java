package br.gov.df.emater.aterwebsrv.importador;

import java.util.Calendar;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.pessoa.RelacionamentoConfiguracaoViDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.RelacionamentoFuncaoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.RelacionamentoTipoDao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.RelacionamentoParticipacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.RelacionamentoConfiguracaoVi;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.RelacionamentoFuncao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.RelacionamentoTipo;

@Service
public class InicioImportacaoCmd extends _Comando {

	@Autowired
	private RelacionamentoConfiguracaoViDao relacionamentoConfiguracaoViDao;

	@Autowired
	private RelacionamentoFuncaoDao relacionamentoFuncaoDao;

	@Autowired
	private RelacionamentoTipoDao relacionamentoTipoDao;
	
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private FacadeBo facadeBo;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		contexto.put("agora", Calendar.getInstance());
		contexto.put("enviarEmailUsuario", false);
		
		contexto.put("em", em);
		contexto.put("facadeBo", facadeBo);

		RelacionamentoConfiguracaoVi relacionamentoConfiguracaoVi = relacionamentoConfiguracaoViDao.findOneByTipoCodigoAndRelacionadorParticipacao(RelacionamentoTipo.Codigo.PROFISSIONAL.name(), RelacionamentoParticipacao.A);
		RelacionamentoTipo relacionamentoTipo = relacionamentoTipoDao.findOne(relacionamentoConfiguracaoVi.getTipoId());
		RelacionamentoFuncao empregadorFuncao = relacionamentoFuncaoDao.findOne(relacionamentoConfiguracaoVi.getRelacionadorId());
		RelacionamentoFuncao empregadoFuncao = relacionamentoFuncaoDao.findOne(relacionamentoConfiguracaoVi.getRelacionadoId());

		contexto.put("relacionamentoTipo", relacionamentoTipo);
		contexto.put("empregadorFuncao", empregadorFuncao);
		contexto.put("empregadoFuncao", empregadoFuncao);

		return false;
	}

}