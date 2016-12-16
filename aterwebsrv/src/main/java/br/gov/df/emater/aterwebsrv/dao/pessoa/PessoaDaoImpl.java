package br.gov.df.emater.aterwebsrv.dao.pessoa;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import br.gov.df.emater.aterwebsrv.dto.TagDto;
import br.gov.df.emater.aterwebsrv.dto.pessoa.PessoaCadFiltroDto;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ConfirmacaoDap;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaGenero;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaGeracao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaSituacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaTipo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PublicoAlvoCategoria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PublicoAlvoSegmento;

public class PessoaDaoImpl implements PessoaDaoCustom {

	private String comunidadeQryStr;

	private String dapQryStr;

	@Autowired
	private EntityManager em;

	private String emailQryStr;

	private String telefoneQryStr;

	private String pendenciaQryStr;

	public PessoaDaoImpl() {
		StringBuilder sql;

		// construção do sql
		sql = new StringBuilder();
		sql.append("select   p.finalidade").append("\n");
		sql.append("       , p.tipo").append("\n");
		sql.append("       , t.numero").append("\n");
		sql.append("from     pessoa.pessoa_telefone p").append("\n");
		sql.append("join     pessoa.telefone t").append("\n");
		sql.append("on       p.telefone_id = t.id").append("\n");
		sql.append("where    p.pessoa_id = ?1").append("\n");
		sql.append("and      FIND_IN_SET('C', p.finalidade) > 0").append("\n");
		sql.append("order by p.ordem").append("\n");
		telefoneQryStr = sql.toString();

		sql = new StringBuilder();
		sql.append("select   p.finalidade").append("\n");
		sql.append("       , e.endereco").append("\n");
		sql.append("from     pessoa.pessoa_email p").append("\n");
		sql.append("join     pessoa.email e").append("\n");
		sql.append("on       p.email_id = e.id").append("\n");
		sql.append("where    p.pessoa_id = ?1").append("\n");
		sql.append("and      FIND_IN_SET('C', p.finalidade) > 0").append("\n");
		sql.append("order by p.ordem").append("\n");
		emailQryStr = sql.toString();

		sql = new StringBuilder();
		sql.append("select   distinct").append("\n");
		sql.append("         p.tipo_vinculo").append("\n");
		sql.append("       , c.nome").append("\n");
		sql.append("from     ater.publico_alvo_propriedade_rural p ").append("\n");
		sql.append("join     ater.comunidade c").append("\n");
		sql.append("on       p.comunidade_id = c.id").append("\n");
		sql.append("where    publico_alvo_id = ?1").append("\n");
		sql.append("and      (p.inicio is null or p.inicio < now())").append("\n");
		sql.append("and      (p.termino is null or p.termino > now())").append("\n");
		sql.append("order by c.nome").append("\n");
		comunidadeQryStr = sql.toString();

		sql = new StringBuilder();
		sql.append("select  pa.dap_situacao, pa.dap_validade").append("\n");
		sql.append("from    pessoa.pessoa p").append("\n");
		sql.append("join    ater.publico_alvo pa").append("\n");
		sql.append("on      pa.pessoa_id = p.id").append("\n");
		sql.append("where   p.id = ?1").append("\n");
		sql.append("and     p.publico_alvo = 'S'").append("\n");
		sql.append("and     pa.categoria = 'E'").append("\n");
		sql.append("and     pa.segmento = 'F'").append("\n");
		dapQryStr = sql.toString();

		sql = new StringBuilder();
		sql.append("select  (select   count(*)").append("\n");
		sql.append("         from     pessoa.pessoa_pendencia p").append("\n");
		sql.append("         where    p.pessoa_id = ?1").append("\n");
		sql.append("         and      p.tipo = 'E') as erro,").append("\n");
		sql.append("        (select   count(*)").append("\n");
		sql.append("         from     pessoa.pessoa_pendencia p").append("\n");
		sql.append("         where    p.pessoa_id = ?1").append("\n");
		sql.append("         and      p.tipo = 'A') as aviso").append("\n");
		pendenciaQryStr = sql.toString();
	}

	@SuppressWarnings("unchecked")
	private List<Object[]> fetch(List<Object[]> lista) {
		Query telefoneQry = em.createNativeQuery(telefoneQryStr);
		Query emailQry = em.createNativeQuery(emailQryStr);
		Query comunidadeQry = em.createNativeQuery(comunidadeQryStr);
		Query dapQry = em.createNativeQuery(dapQryStr);
		Query pendenciaQry = em.createNativeQuery(pendenciaQryStr);
		List<Object[]> result = new ArrayList<Object[]>();
		for (Object[] linha : lista) {
			List<Object> registro = new ArrayList<Object>(Arrays.asList(linha));

			telefoneQry.setParameter(1, registro.get(0));
			emailQry.setParameter(1, registro.get(0));
			comunidadeQry.setParameter(1, registro.get(10));
			dapQry.setParameter(1, registro.get(0));
			pendenciaQry.setParameter(1, registro.get(0));

			registro.add(telefoneQry.getResultList());
			registro.add(emailQry.getResultList());
			registro.add(comunidadeQry.getResultList());
			List<Object> dapRes = dapQry.getResultList();
			if (dapRes == null || dapRes.size() == 0) {
				registro.add(null);
			} else {
				Object[] dapResObj = (Object[]) dapRes.get(0);
				if (dapResObj == null || dapResObj.length != 2) {
					registro.add(null);
				} else {
					Character temp1 = (Character) dapResObj[0];
					Date temp2 = (Date) dapResObj[1];

					if (temp1 == null || temp2 == null) {
						if (temp2 == null) {
							registro.add("img/dap-vencida.png");
						} else {							
							registro.add(null);
						}
					} else {
						ConfirmacaoDap sit = ConfirmacaoDap.valueOf(temp1.toString());
						Calendar valid = new GregorianCalendar();
						valid.setTime(temp2);
						Calendar hoje = Calendar.getInstance();
						Calendar carencia = Calendar.getInstance();
						carencia.roll(Calendar.MONTH, 2);

						if (ConfirmacaoDap.S.equals(sit)) {
							if (valid.after(carencia)) {
								registro.add("img/dap-ok.png");
							} else if (valid.after(hoje)) {
								registro.add("img/dap-a-vencer.png");
							} else {
								registro.add("img/dap-vencida.png");
							}
						} else {
							registro.add(null);
						}
					}
				}
			}
			List<Object> pendenciaRes = pendenciaQry.getResultList();
			if (CollectionUtils.isEmpty(pendenciaRes)) {
				registro.add(null);
				registro.add(null);
			} else {
				registro.add(((Object[]) pendenciaRes.get(0))[0]);
				registro.add(((Object[]) pendenciaRes.get(0))[1]);
			}

			result.add(registro.toArray());
		}
		return result;
	}

	@Override
	public List<Object[]> filtrar(PessoaCadFiltroDto filtro) {
		// objetos de trabalho
		List<Object[]> result = null;
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql, sqlTemp;

		// construção do sql
		sql = new StringBuilder();
		sql.append("select distinct p.id").append("\n");
		sql.append("     , p.nome").append("\n");
		sql.append("     , p.apelidoSigla").append("\n");
		sql.append("     , p.pessoaTipo").append("\n");
		sql.append("     , p.cpf").append("\n");
		sql.append("     , p.cnpj").append("\n");
		sql.append("     , p.publicoAlvoConfirmacao").append("\n");
		sql.append("     , perfil.md5").append("\n");
		sql.append("     , p.nascimento").append("\n");
		sql.append("     , p.genero").append("\n");
		sql.append("     , alvo.id").append("\n");
		sql.append("     , p.chaveSisater").append("\n");
		sql.append("     , p.situacao").append("\n");
		sql.append("from Pessoa p").append("\n");
		sql.append("left join p.publicoAlvo alvo").append("\n");
		sql.append("left join p.perfilArquivo perfil").append("\n");
		if (filtrarPublicoAlvo(filtro)) {
			if (filtro.getPublicoAlvoSetor() != null) {
				sql.append("left join alvo.publicoAlvoSetorList paSetor").append("\n");
			}
			if (!CollectionUtils.isEmpty(filtro.getEmpresaList()) || !CollectionUtils.isEmpty(filtro.getUnidadeOrganizacionalList()) || !CollectionUtils.isEmpty(filtro.getComunidadeList())) {
				sql.append("left join alvo.publicoAlvoPropriedadeRuralList paPropriedadeRural").append("\n");
			}
		}
		if (!CollectionUtils.isEmpty(filtro.getPendencia())) {
			sql.append("join p.pendenciaList pendencia").append("\n");
		}
		sql.append("where (1 = 1)").append("\n");
		if (filtrarPublicoAlvo(filtro)) {
			sql.append("and   p.publicoAlvoConfirmacao = 'S'").append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getNomeList())) {
			sql.append("and (").append("\n");
			sqlTemp = new StringBuilder();
			for (TagDto nome : filtro.getNomeList()) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append(" or ");
				}
				String n = nome.getText().replaceAll("\\s", "%");
				params.add(String.format("%%%s%%", n));
				sqlTemp.append(" (p.nome like ?").append(params.size());
				params.add(String.format("%%%s%%", n));
				sqlTemp.append(" or p.apelidoSigla like ?").append(params.size()).append(")").append("\n");
			}
			sql.append(sqlTemp);
			sql.append(" )").append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getChaveSisaterList())) {
			sql.append("and (").append("\n");
			sqlTemp = new StringBuilder();
			for (TagDto chaveSisater : filtro.getChaveSisaterList()) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append(" or ");
				}
				String n = chaveSisater.getText().replaceAll("\\s", "%");
				params.add(String.format("%%%s%%", n));
				sqlTemp.append(" (p.chaveSisater like ?").append(params.size()).append(")").append("\n");
			}
			sql.append(sqlTemp);
			sql.append(" )").append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getPessoaTipoList()) && (PessoaTipo.values().length != (filtro.getPessoaTipoList().size()))) {
			params.add(filtro.getPessoaTipoList());
			sql.append("and p.pessoaTipo in ?").append(params.size()).append("\n");
		}
		if (!StringUtils.isEmpty(filtro.getCpf())) {
			params.add(UtilitarioString.formataCpf(filtro.getCpf()));
			sql.append("and p.cpf = ?").append(params.size()).append("\n");
		}
		if (!StringUtils.isEmpty(filtro.getCnpj())) {
			params.add(UtilitarioString.formataCnpj(filtro.getCnpj()));
			sql.append("and p.cnpj = ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getPessoaGenero()) && (PessoaGenero.values().length != (filtro.getPessoaGenero().size()))) {
			params.add(filtro.getPessoaGenero());
			sql.append("and p.pessoaGenero in ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getPessoaGeracao()) && (PessoaGeracao.values().length != (filtro.getPessoaGeracao().size()))) {
			sql.append("and (").append("\n");
			sqlTemp = new StringBuilder();
			for (PessoaGeracao pg : filtro.getPessoaGeracao()) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append(" or ");
				}
				params.add(pg.getIdadeInicio());
				sqlTemp.append("TIMESTAMPDIFF(YEAR, p.nascimento, CURDATE()) between ?").append(params.size());
				params.add(pg.getIdadeFim());
				sqlTemp.append(" and ?").append(params.size()).append("\n");
			}
			sql.append(sqlTemp).append("	)").append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getPessoaSituacao()) && (PessoaSituacao.values().length != (filtro.getPessoaSituacao().size()))) {
			params.add(filtro.getPessoaSituacao());
			sql.append("and p.situacao in ?").append(params.size()).append("\n");
		}
		// filtro de publico alvo
		if (!CollectionUtils.isEmpty(filtro.getPublicoAlvoSegmento()) && (PublicoAlvoSegmento.values().length != (filtro.getPublicoAlvoSegmento().size()))) {
			params.add(filtro.getPublicoAlvoSegmento());
			sql.append("and alvo.segmento in ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getPublicoAlvoCategoria()) && (PublicoAlvoCategoria.values().length != (filtro.getPublicoAlvoCategoria().size()))) {
			params.add(filtro.getPublicoAlvoCategoria());
			sql.append("and alvo.categoria in ?").append(params.size()).append("\n");
		}
		if (!StringUtils.isEmpty(filtro.getPublicoAlvoSetor())) {
			params.add(filtro.getPublicoAlvoSetor());
			sql.append("and paSetor.setor.id = ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getComunidadeList())) {
			params.add(filtro.getComunidadeList());
			sql.append("and paPropriedadeRural.comunidade in ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getUnidadeOrganizacionalList())) {
			params.add(filtro.getUnidadeOrganizacionalList());
			sql.append("and paPropriedadeRural.comunidade.unidadeOrganizacional in ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getEmpresaList())) {
			params.add(filtro.getEmpresaList());
			sql.append("and paPropriedadeRural.comunidade.unidadeOrganizacional.pessoaJuridica in ?").append(params.size()).append("\n");
		}
		if (!StringUtils.isEmpty(filtro.getPublicoAlvoPropriedadeUtilizacaoEspacoRural())) {
			// TODO Este campo nao foi implementado no BD - resolver
			// params.add(filtro.getPublicoAlvoPropriedadeUtilizacaoEspacoRural().getId());
			// sql.append("and paSetor.setor.id =
			// ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getPendencia())) {
			params.add(filtro.getPendencia());
			sql.append("and pendencia.tipo in ?").append(params.size()).append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getPublicoAlvoDap())) {
			params.add(filtro.getPublicoAlvoDap());
			sql.append("and alvo.dapSituacao in ?").append(params.size()).append("\n");
		}
		sql.append("order by p.nome, p.apelidoSigla").append("\n");

		// criar a query
		TypedQuery<Object[]> query = em.createQuery(sql.toString(), Object[].class);

		// inserir os parametros
		for (int i = 1; i <= params.size(); i++) {
			query.setParameter(i, params.get(i - 1));
		}

		// definir a pagina a ser consultada
		filtro.configuraPaginacao(query);

		// executar a consulta
		result = query.getResultList();

		if (result != null) {
			result = fetch(result);
		}

		// retornar
		return result;
	}

	private boolean filtrarPublicoAlvo(PessoaCadFiltroDto filtro) {
		return (!CollectionUtils.isEmpty(filtro.getPublicoAlvoSegmento()) && (PublicoAlvoSegmento.values().length != (filtro.getPublicoAlvoSegmento().size())))
				|| (!CollectionUtils.isEmpty(filtro.getPublicoAlvoCategoria()) && (PublicoAlvoCategoria.values().length != (filtro.getPublicoAlvoCategoria().size()))) || (filtro.getPublicoAlvoSetor() != null) || (filtro.getPublicoAlvoPropriedadeUtilizacaoEspacoRural() != null)
				|| (!CollectionUtils.isEmpty(filtro.getEmpresaList())) || (!CollectionUtils.isEmpty(filtro.getUnidadeOrganizacionalList())) || (!CollectionUtils.isEmpty(filtro.getComunidadeList()));
	}

}