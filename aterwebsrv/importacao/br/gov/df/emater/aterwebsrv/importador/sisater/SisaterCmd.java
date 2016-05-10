package br.gov.df.emater.aterwebsrv.importador.sisater;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.funcional.UnidadeOrganizacionalDao;
import br.gov.df.emater.aterwebsrv.importador.ImportFacadeBo;
import br.gov.df.emater.aterwebsrv.importador.apoio.ConexaoFirebird;
import br.gov.df.emater.aterwebsrv.importador.apoio.ConexaoFirebird.DbSater;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.seguranca.UserAuthentication;

@Service
public class SisaterCmd extends _Comando {

	@Autowired
	private ImportFacadeBo importFacadeBo;

	@Autowired
	private UnidadeOrganizacionalDao unidadeOrganizacionalDao;

	@SuppressWarnings("unchecked")
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("INICIO DA IMPORTAÇÃO");
		}

		int cont = 0;
		for (DbSater base : DbSater.values()) {
			if (base.getSigla() == null)
				continue;

			if (cont >= 1)
				break;

			if (logger.isInfoEnabled()) {
				logger.info(String.format("%s. importando base [%s]", ++cont, base.name()));
			}

			try (Connection con = ConexaoFirebird.getConnection(base)) {
				try {
					Map<Object, Object> map = new HashMap<Object, Object>();
					map.putAll(contexto);
					map.put("base", base);
					map.put("conexao", con);
					map.put("unidadeOrganizacional", unidadeOrganizacionalDao.findOneByPessoaJuridicaAndSigla((PessoaJuridica) contexto.get("emater"), base.getSigla()));
					importFacadeBo.sisater(new UserAuthentication((Usuario) contexto.get("ematerUsuario")), map);
					if (!con.isClosed()) {
						con.commit();
						if (logger.isDebugEnabled()) {
							logger.debug(String.format("[%s] Commit NO SISATER", base.name()));
						}
					}
				} catch (Exception e) {
					try {
						if (!con.isClosed()) {
							con.rollback();
							logger.error("Rollback NO SISATER");
						}
					} catch (SQLException e1) {
					}
					throw e;
				}
			}
		}
		if (logger.isInfoEnabled()) {
			logger.info("FIM DA IMPORTAÇÃO");
		}
		return false;
	}

}