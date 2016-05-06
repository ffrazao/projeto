package br.gov.df.emater.aterwebsrv.importador;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.funcional.UnidadeOrganizacionalDao;
import br.gov.df.emater.aterwebsrv.importador.apoio.ConexaoFirebird;
import br.gov.df.emater.aterwebsrv.importador.apoio.ConexaoFirebird.DbSater;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;

@Service
public class SisaterCmd extends _Comando {

	@Autowired
	private ImportFacadeBo importFacadeBo;

	@Autowired
	private UnidadeOrganizacionalDao unidadeOrganizacionalDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {

		for (DbSater base : DbSater.values()) {
			if (base.getSigla() == null)
				continue;

			try (Connection con = ConexaoFirebird.getConnection(base)) {
				try {
					Map<Object, Object> map = new HashMap<Object, Object>();
					map.put("base", base);
					map.put("conexao", con);
					map.put("unidadeOrganizacional", unidadeOrganizacionalDao.findOneByPessoaJuridicaAndSigla((PessoaJuridica) contexto.get("emater"), base.getSigla()));
					importFacadeBo.sisater(contexto.getUsuario(), map);
					if (!con.isClosed()) {
						con.commit();
					}
				} catch (Exception e) {
					try {
						if (!con.isClosed()) {
							con.rollback();
						}
					} catch (SQLException e1) {
					}
					throw e;
				}
			}
		}
		return false;
	}

}