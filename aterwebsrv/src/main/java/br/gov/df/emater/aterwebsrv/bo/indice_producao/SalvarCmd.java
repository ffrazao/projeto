package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import java.util.Map;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ferramenta.UtilDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.BemDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoFormaComposicaoDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoFormaDao;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioNumero;
import br.gov.df.emater.aterwebsrv.modelo.dominio.CadastroAcao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.FormulaProduto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoForma;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoFormaComposicao;

@Service("IndiceProducaoSalvarCmd")
public class SalvarCmd extends _Comando {

	public SalvarCmd() {
	}

	@Autowired
	private ProducaoDao dao;

	@Autowired
	private UtilDao utilDao;

	@Autowired
	private BemDao bemDao;

	@Autowired
	private ProducaoFormaDao producaoFormaDao;

	@Autowired
	private ProducaoFormaComposicaoDao producaoFormaComposicaoDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Producao result = (Producao) contexto.getRequisicao();
		if (result.getId() == null) {
			result.setInclusaoUsuario(getUsuario(contexto.getUsuario().getName()));
		}
		result.setAlteracaoUsuario(getUsuario(contexto.getUsuario().getName()));
		result.setBem(bemDao.findOne(result.getBem().getId()));

		if (result.getPublicoAlvo() != null && result.getPropriedadeRural() != null) {
			result.setUnidadeOrganizacional(null);
		} else if (result.getUnidadeOrganizacional() != null) {
			result.setPublicoAlvo(null);
			result.setPropriedadeRural(null);
		}

		try {
			dao.save(result);
		} catch (DataIntegrityViolationException e) {
			if (e.getCause() instanceof ConstraintViolationException) {
				ConstraintViolationException c = (ConstraintViolationException) e.getCause();
				throw new BoException("Registro já cadastrado", c);
			}
			throw e;
		}

		// proceder a exclusao dos registros
		for (ProducaoForma producaoForma : result.getProducaoFormaList()) {
			if (producaoForma.getProducaoFormaComposicaoList() != null) {
				for (ProducaoFormaComposicao producaoFormaComposicao : producaoForma.getProducaoFormaComposicaoList()) {
					if (CadastroAcao.E.equals(producaoFormaComposicao.getCadastroAcao())) {
						producaoFormaComposicaoDao.delete(producaoFormaComposicao);
					}
				}
			}
			if (CadastroAcao.E.equals(producaoForma.getCadastroAcao())) {
				producaoFormaDao.delete(producaoForma.getId());
			}
		}

		Map<String, Object> bemClassificacaoDetalhe = utilDao.ipaBemClassificacaoDetalhes(result.getBem().getBemClassificacao());

		// salvar demais itens
		for (ProducaoForma producaoForma : result.getProducaoFormaList()) {
			// se não foi excluido
			if (CadastroAcao.E.equals(producaoForma.getCadastroAcao())) {
				continue;
			}
			producaoForma.setProducao(result);
			if (producaoForma.getId() == null) {
				producaoForma.setInclusaoUsuario(getUsuario(contexto.getUsuario().getName()));
			}
			producaoForma.setAlteracaoUsuario(getUsuario(contexto.getUsuario().getName()));

			try {
				producaoForma.setVolume(((FormulaProduto) bemClassificacaoDetalhe.get("formulaProduto")).volume(producaoForma));
			} catch (Exception e) {
				throw new BoException(e);
			}
			if (producaoForma.getVolume() != null) {
				producaoForma.setValorTotal(producaoForma.getVolume().multiply(producaoForma.getValorUnitario(), UtilitarioNumero.BIG_DECIMAL_PRECISAO));
			}

			producaoFormaDao.save(producaoForma);

			Integer ordem = 0;
			for (ProducaoFormaComposicao producaoFormaComposicao : producaoForma.getProducaoFormaComposicaoList()) {
				// se não foi excluido
				if (CadastroAcao.E.equals(producaoFormaComposicao.getCadastroAcao())) {
					continue;
				}
				producaoFormaComposicao.setProducaoForma(producaoForma);
				producaoFormaComposicao.setOrdem(++ordem);
				producaoFormaComposicaoDao.save(producaoFormaComposicao);
			}
		}

		dao.flush();

		contexto.setResposta(result.getId());
		return false;
	}

}