package br.gov.df.emater.aterwebsrv.importador.sisater;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.funcional.UnidadeOrganizacionalDao;
import br.gov.df.emater.aterwebsrv.importador.FacadeBoImportar;
import br.gov.df.emater.aterwebsrv.importador.apoio.ConexaoFirebird;
import br.gov.df.emater.aterwebsrv.importador.apoio.ConexaoFirebird.DbSater;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;

@Service
public class SisaterCmd extends _Comando {

	private class Check {

		void baseComando(_Contexto contexto, Map<Object, Object> map) throws Exception {
			importFacadeBo.sisater(contexto.getUsuario(), map);
		}

		void empregoComando(_Contexto contexto, Map<Object, Object> map) throws Exception {
			importFacadeBo.sisaterEmpregado(contexto.getUsuario(), map);
		}

		@SuppressWarnings("unchecked")
		void rodar(_Contexto contexto, String importando) throws Exception {
			int cont = 1;
			for (DbSater base : DbSater.values()) {
				if (base.getSigla() == null) {
					continue;
				}
				
				if (base.compareTo(DbSater.ELALG) != 0) {
					continue;
				}

				if (logger.isInfoEnabled()) {
					logger.info(String.format("%s. importando %s [%s]", cont, importando, base.name()));
				}

				try (Connection con = ConexaoFirebird.getConnection(base)) {
					try {
						Map<Object, Object> map = new HashMap<Object, Object>();
						map.putAll(contexto);
						map.put("base", base);
						map.put("conexao", con);
						map.put("unidadeOrganizacional", unidadeOrganizacionalDao.findOneByPessoaJuridicaAndSigla((PessoaJuridica) contexto.get("emater"), base.getSigla()));

						switch (importando) {
						case "Emprego":
							empregoComando(contexto, map);
							break;
						case "Base":
							baseComando(contexto, map);
							break;
						}

						if (!con.isClosed()) {
							con.commit();
							if (logger.isDebugEnabled()) {
								logger.debug(String.format("[%s] Commit NO SISATER", base.name()));
							}
						}
					} catch (Exception e) {
						if (!con.isClosed()) {
							con.rollback();
							if (logger.isErrorEnabled()) {
								logger.error("Rollback NO SISATER");
							}
						}
						throw e;
					}
				}
				// executar o garbage collector
				System.gc();
				cont++;
			}

		}
	}

	@Autowired
	private FacadeBoImportar importFacadeBo;

	@Autowired
	private UnidadeOrganizacionalDao unidadeOrganizacionalDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("INICIO DA IMPORTAÇÃO");
		}
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

		Check check = new Check();

		// importar primeiro todos os dados de empregado de todas as bases
		// check.rodar(contexto, "Emprego");

		// importar demais dados
		check.rodar(contexto, "Base");

		if (logger.isInfoEnabled()) {
			logger.info("FIM DA IMPORTAÇÃO");
		}
		
		Thread.currentThread().setPriority(Thread.NORM_PRIORITY);

		return false;
	}

}