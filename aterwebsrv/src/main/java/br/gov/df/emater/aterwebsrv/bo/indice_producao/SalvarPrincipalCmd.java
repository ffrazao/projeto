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
import br.gov.df.emater.aterwebsrv.modelo.ater.Comunidade;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoForma;

@Service("IndiceProducaoSalvarPrincipalCmd")
public class SalvarPrincipalCmd extends _Comando {

	@Autowired
	private ComunidadeDao comunidadeDao;

	@Autowired
	private ProducaoDao dao;

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
		Producao producao = dao.findOne(id);

		// se a producao que acabaou de ser salva nao for a principal, ou seja,
		// estimativa da região
		if (producao.getUnidadeOrganizacional() == null && producao.getPropriedadeRural() != null) {
			// verificar se existe a producao principal da regiao esperada
			PropriedadeRural pr = propriedadeRuralDao.findOne(producao.getPropriedadeRural().getId());
			if (pr.getComunidade() == null) {
				return false;
			}
			Comunidade comunidade = comunidadeDao.findOne(pr.getComunidade().getId());
			UnidadeOrganizacional unidadeOrganizacional = unidadeOrganizacionalDao.findOne(comunidade.getUnidadeOrganizacional().getId());
			Producao producaoPrincipal = dao.findOneByAnoAndBemAndUnidadeOrganizacionalAndPublicoAlvoIsNullAndPropriedadeRuralIsNull(producao.getAno(), producao.getBem(), unidadeOrganizacional);
			if (producaoPrincipal == null) {
				// se nao existe entao criar com base no registro atual
				producaoPrincipal = new Producao(producao);
				producaoPrincipal.setUnidadeOrganizacional(unidadeOrganizacional);
				producaoPrincipal.setPropriedadeRural(null);
				producaoPrincipal.setPublicoAlvo(null);
			} else {
				// se existir, verificar se todas as formas de produção foram
				// previstas
				final List<ProducaoForma> producaoFormaNovoList = new ArrayList<>();
				for (ProducaoForma producaoForma : producao.getProducaoFormaList()) {
					boolean encontrou = false;
					final String composicao = getComposicaoValorId(producaoForma);
					if (!CollectionUtils.isEmpty(producaoPrincipal.getProducaoFormaList())) {
						for (ProducaoForma producaoFormaPrincipal : producaoPrincipal.getProducaoFormaList()) {
							if (getComposicaoValorId(producaoFormaPrincipal).equals(composicao)) {
								encontrou = true;
								break;
							}
						}
					}
					if (!encontrou) {
						producaoFormaNovoList.add(new ProducaoForma(producaoForma));
					}
				}
				// se houveram novas formas de produção
				if (!CollectionUtils.isEmpty(producaoFormaNovoList)) {
					// inserir na producao principal
					if (producaoPrincipal.getProducaoFormaList() == null) {
						producaoPrincipal.setProducaoFormaList(new ArrayList<>());
					}
					producaoFormaNovoList.addAll(producaoPrincipal.getProducaoFormaList());
					producaoPrincipal.setProducaoFormaList(producaoFormaNovoList);
//					producaoPrincipal.getProducaoFormaList().forEach((producaoForma)->producaoFormaNovoList.add(producaoForma));
//					producaoPrincipal.setProducaoFormaList(producaoFormaNovoList);
				}
			}
			em.detach(producaoPrincipal);
			facadeBo.indiceProducaoSalvar(contexto.getUsuario(), producaoPrincipal);
		}
		
		contexto.setResposta(id);

		return false;
	}

}