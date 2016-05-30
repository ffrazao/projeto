package br.gov.df.emater.aterwebsrv.importador.basico;

import java.util.Calendar;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.pessoa.EstadoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.MunicipioDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PaisDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.RelacionamentoConfiguracaoViDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.RelacionamentoFuncaoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.RelacionamentoTipoDao;
import br.gov.df.emater.aterwebsrv.importador.apoio.ImpUtil;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.RelacionamentoParticipacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Estado;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Municipio;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pais;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.RelacionamentoConfiguracaoVi;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.RelacionamentoTipo;

@Service
public class BasicoInicioImportacaoCmd extends _Comando {

	@Autowired
	private PlatformTransactionManager transactionManager;

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

	@Autowired
	private ImpUtil impUtil;

	@Autowired
	private PaisDao paisDao;

	@Autowired
	private EstadoDao estadoDao;

	@Autowired
	private MunicipioDao municipioDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		RelacionamentoConfiguracaoVi relacionamentoConfiguracaoVi = relacionamentoConfiguracaoViDao.findOneByTipoCodigoAndRelacionadorParticipacao(RelacionamentoTipo.Codigo.PROFISSIONAL.name(), RelacionamentoParticipacao.A);

		DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
		transactionDefinition.setName("ImportacaoATERweb");
		transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

		contexto.put("enviarEmailUsuario", false);
		contexto.put("agora", Calendar.getInstance());

		contexto.put("em", em);
		contexto.put("facadeBo", facadeBo);
		contexto.put("impUtil", impUtil);

		contexto.put("relacionamentoTipo", relacionamentoTipoDao.findOne(relacionamentoConfiguracaoVi.getTipoId()));
		contexto.put("empregadorFuncao", relacionamentoFuncaoDao.findOne(relacionamentoConfiguracaoVi.getRelacionadorId()));
		contexto.put("empregadoFuncao", relacionamentoFuncaoDao.findOne(relacionamentoConfiguracaoVi.getRelacionadoId()));

		contexto.put("brasil", paisDao.findByPadrao(Confirmacao.S).get(0));

		contexto.put("distritoFederal", estadoDao.findByCapitalAndPais(Confirmacao.S, (Pais) contexto.get("brasil")).get(0));
		contexto.put("brasilia", municipioDao.findByCapitalAndEstado(Confirmacao.S, (Estado) contexto.get("distritoFederal")).get(0));

		contexto.put("goias", estadoDao.findOneByPaisAndSigla((Pais) contexto.get("brasil"), "GO"));
		contexto.put("cristalina", municipioDao.findByEstadoAndNomeLike((Estado) contexto.get("goias"), "Cristalina").get(0));
		contexto.put("formosa", municipioDao.findByEstadoAndNomeLike((Estado) contexto.get("goias"), "Formosa").get(0));
		contexto.put("padreBernardo", municipioDao.findByEstadoAndNomeLike((Estado) contexto.get("goias"), "Padre Bernardo").get(0));
		contexto.put("cocalzinhoDeGoias", municipioDao.findByEstadoAndNomeLike((Estado) contexto.get("goias"), "Cocalzinho de Goiás").get(0));
		contexto.put("planaltina", municipioDao.findByEstadoAndNomeLike((Estado) contexto.get("goias"), "Planaltina").get(0));

		contexto.put("municipioAtendimentoList", new Municipio[] { (Municipio) contexto.get("brasilia"), (Municipio) contexto.get("cristalina"), (Municipio) contexto.get("formosa"), (Municipio) contexto.get("padreBernardo"), (Municipio) contexto.get("cocalzinhoDeGoias"),
				(Municipio) contexto.get("planaltina") });

		contexto.put("transactionManager", transactionManager);

		contexto.put("transactionDefinition", transactionDefinition);

		return false;
	}

}