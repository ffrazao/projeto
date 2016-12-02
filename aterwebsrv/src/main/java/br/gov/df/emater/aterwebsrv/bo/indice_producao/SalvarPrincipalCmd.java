package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import static br.gov.df.emater.aterwebsrv.bo.indice_producao.IndiceProducaoUtil.getComposicaoValorId;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ater.ComunidadeDao;
import br.gov.df.emater.aterwebsrv.dao.ater.PropriedadeRuralDao;
import br.gov.df.emater.aterwebsrv.dao.funcional.UnidadeOrganizacionalDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoProprietarioDao;
import br.gov.df.emater.aterwebsrv.modelo.ater.Comunidade;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoProprietario;

@Service("IndiceProducaoSalvarPrincipalCmd")
public class SalvarPrincipalCmd extends _Comando {

	@Autowired
	private ComunidadeDao comunidadeDao;

	@Autowired
	private ProducaoProprietarioDao dao;

	@Autowired
	private ProducaoDao producaoDao;

	@Autowired
	private EntityManager em;

	@Autowired
	private FacadeBo facadeBo;

	@Autowired
	private PropriedadeRuralDao propriedadeRuralDao;

	@Autowired
	private UnidadeOrganizacionalDao unidadeOrganizacionalDao;

	public SalvarPrincipalCmd() {
	}

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Integer id = (Integer) contexto.getResposta();
		ProducaoProprietario producaoProprietario = dao.findOne(id);

		// se a producao que acabaou de ser salva nao for a principal, ou seja,
		// estimativa da região
		if (producaoProprietario.getUnidadeOrganizacional() == null && producaoProprietario.getPropriedadeRural() != null) {
			// verificar se existe a producao principal da regiao esperada
			PropriedadeRural pr = propriedadeRuralDao.findOne(producaoProprietario.getPropriedadeRural().getId());
			if (pr.getComunidade() == null) {
				return false;
			}
			Comunidade comunidade = comunidadeDao.findOne(pr.getComunidade().getId());
			UnidadeOrganizacional unidadeOrganizacional = unidadeOrganizacionalDao.findOne(comunidade.getUnidadeOrganizacional().getId());
			ProducaoProprietario producaoProprietarioPrincipal = dao.findOneByAnoAndBemClassificadoAndUnidadeOrganizacionalAndPublicoAlvoIsNullAndPropriedadeRuralIsNull(producaoProprietario.getAno(), producaoProprietario.getBemClassificado(), unidadeOrganizacional);
			if (producaoProprietarioPrincipal == null) {
				// se nao existe entao criar com base no registro atual
				producaoProprietarioPrincipal = new ProducaoProprietario(producaoProprietario);
				producaoProprietarioPrincipal.setUnidadeOrganizacional(unidadeOrganizacional);
				producaoProprietarioPrincipal.setPropriedadeRural(null);
				producaoProprietarioPrincipal.setPublicoAlvo(null);
			} else {
				// se existir, verificar se todas as formas de produção foram
				// previstas
				final List<Producao> producaoNovoList = new ArrayList<>();
				// restaurar producoes
				producaoProprietario.setProducaoList(producaoDao.findByProducaoProprietario(producaoProprietario));
				producaoProprietarioPrincipal.setProducaoList(producaoDao.findByProducaoProprietario(producaoProprietarioPrincipal));
				if (producaoProprietario.getProducaoList() != null) {					
					for (Producao producao : producaoProprietario.getProducaoList()) {
						boolean encontrou = false;
						final String composicao = getComposicaoValorId(producao);
						if (!CollectionUtils.isEmpty(producaoProprietarioPrincipal.getProducaoList())) {
							for (Producao producaoPrincipal : producaoProprietarioPrincipal.getProducaoList()) {
								if (getComposicaoValorId(producaoPrincipal).equals(composicao)) {
									encontrou = true;
									break;
								}
							}
						}
						if (!encontrou) {
							producaoNovoList.add(new Producao(producao));
						}
					}
				}
				// se houveram novas formas de produção
				if (!CollectionUtils.isEmpty(producaoNovoList)) {
					// inserir na producao principal
					if (producaoProprietarioPrincipal.getProducaoList() == null) {
						producaoProprietarioPrincipal.setProducaoList(new ArrayList<>());
					}
					producaoNovoList.addAll(producaoProprietarioPrincipal.getProducaoList());
					producaoProprietarioPrincipal.setProducaoList(producaoNovoList);
//					producaoPrincipal.getProducaoList().forEach((producao)->producaoNovoList.add(producao));
//					producaoPrincipal.setProducaoList(producaoNovoList);
				}
			}
			em.detach(producaoProprietarioPrincipal);
			facadeBo.indiceProducaoSalvar(contexto.getUsuario(), producaoProprietarioPrincipal);
		}
		
		contexto.setResposta(id);

		return false;
	}

}