package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ater.ComunidadeDao;
import br.gov.df.emater.aterwebsrv.dao.ater.PropriedadeRuralDao;
import br.gov.df.emater.aterwebsrv.dao.funcional.UnidadeOrganizacionalDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoDao;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.ater.Comunidade;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoForma;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoFormaComposicao;

@Service("IndiceProducaoSalvarPrincipalCmd")
public class SalvarPrincipalCmd extends _Comando {

	@Autowired
	private  ComunidadeDao comunidadeDao;

	@Autowired
	private ProducaoDao dao;

	@Autowired
	private FacadeBo facadeBo;
	
	@Autowired
	private EntityManager em;
	
	@Autowired
	private  PropriedadeRuralDao propriedadeRuralDao;
	
	@Autowired
	private  UnidadeOrganizacionalDao unidadeOrganizacionalDao;
	
	public SalvarPrincipalCmd() {
	}
	
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Integer id = (Integer) contexto.getResposta();
		Producao producao = dao.findOne(id);

		// se a producao que acabaou de ser salva nao for a principal, ou seja,
		// estimativa da região
		if (producao.getUnidadeOrganizacional() == null) {
			// verificar se existe a producao principal da regiao esperada
			PropriedadeRural pr = propriedadeRuralDao.findOne(producao.getPropriedadeRural().getId());
			Comunidade cm = comunidadeDao.findOne(pr.getComunidade().getId());
			UnidadeOrganizacional uo = unidadeOrganizacionalDao.findOne(cm.getUnidadeOrganizacional().getId());
			Producao principal = dao.findOneByAnoAndBemAndUnidadeOrganizacionalAndPublicoAlvoIsNullAndPropriedadeRuralIsNull(producao.getAno(), producao.getBem(), uo);
			if (principal == null) {
				// se nao existe entao criar com base no registro atual
				principal = new Producao(producao);
				principal.setPropriedadeRural(null);
				principal.setPublicoAlvo(null);
				principal.setUnidadeOrganizacional(uo);

			} else {
				// se existir, verificar se todas as formas de produção foram
				// previstas
				List<ProducaoForma> novo = null;
				for (ProducaoForma producaoForma : producao.getProducaoFormaList()) {
					boolean encontrou = false;
					String composicao = getComposicao(producaoForma);
					if (principal.getProducaoFormaList() != null) {
						for (ProducaoForma producaoFormaPrincipal : principal.getProducaoFormaList()) {
							if (composicao.equals(getComposicao(producaoFormaPrincipal))) {
								encontrou = true;
								break;
							}
						}
					}
					if (!encontrou) {
						if (novo == null) {
							novo = new ArrayList<ProducaoForma>();
						}
						novo.add(new ProducaoForma(producaoForma));
					}
				}
				// se houveram novas formas de produção
				if (novo != null) {
					// inserir na producao principal
					for (ProducaoForma pf: principal.getProducaoFormaList()) {
						novo.add(pf);
					}
					principal.setProducaoFormaList(novo);
				}
			}
			em.detach(principal);
			facadeBo.indiceProducaoSalvar(contexto.getUsuario(), principal);
		}

		return false;
	}

	private String getComposicao(ProducaoForma producaoForma) {
		List<Integer> idList = new ArrayList<Integer>();
		for (ProducaoFormaComposicao composicao : producaoForma.getProducaoFormaComposicaoList()) {
			idList.add(composicao.getFormaProducaoValor().getId());
		}
		Collections.sort(idList);
		return UtilitarioString.collectionToString(idList);
	}

}