package br.gov.df.emater.aterwebsrv.importador.basico;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTReader;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaJuridicaDao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.funcional.Empregador;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Cidade;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Email;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Endereco;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Estado;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Municipio;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pais;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaEmail;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaEndereco;

@Service
public class BasicoEmaterCmd extends _Comando {

	@Autowired
	private PessoaJuridicaDao pessoaDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		FacadeBo facadeBo = (FacadeBo) contexto.get("facadeBo");

		Empregador emater = null;
		while ((emater = (Empregador) pessoaDao.findOneByNome("Empresa de Assistência Técnica e Extensão Rural do Distrito Federal")) == null) {
			PlatformTransactionManager transactionManager = (PlatformTransactionManager) contexto.get("transactionManager");
			DefaultTransactionDefinition transactionDefinition = (DefaultTransactionDefinition) contexto.get("transactionDefinition");

			TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
			try {
				emater = new Empregador();
				emater.setNome("Empresa de Assistência Técnica e Extensão Rural do Distrito Federal");
				emater.setApelidoSigla("EMATER-DF");
				emater.setPublicoAlvoConfirmacao(Confirmacao.N);
				List<PessoaEmail> pessoaEmailList = new ArrayList<PessoaEmail>();
				pessoaEmailList.add(new PessoaEmail(null, new Email("aterweb@emater.df.gov.br"), "C"));
				emater.setEmailList(pessoaEmailList);
				List<PessoaEndereco> pessoaEnderecoList = new ArrayList<PessoaEndereco>();
				pessoaEnderecoList.add(new PessoaEndereco(null, new Endereco("Asa Norte", "70.770-915", new Cidade(10306), null, "Edifício Sede", "", new Estado(1), null, (Point) new WKTReader().read("POINT (-15.732687616157767 -47.90378594955473)"), "Parque Estação Biológica",
						new Municipio(1), emater.getNome(), null, new Pais(1), Confirmacao.N, null, null), "C"));
				emater.setEnderecoList(pessoaEnderecoList);
				emater.setCnpj("00.509.612/0001-04");
				emater.setFundacao(new GregorianCalendar(1978, 5, 19));
				facadeBo.pessoaSalvar(null, emater).getResposta();

				transactionManager.commit(transactionStatus);
			} catch (Exception e) {
				logger.error(e);
				e.printStackTrace();
				transactionManager.rollback(transactionStatus);
				throw e;
			}
		}

		contexto.put("emater", emater);

		if (logger.isInfoEnabled()) {
			logger.info("Cadastro da Emater-DF verificado e carregado");
		}

		return false;
	}

}