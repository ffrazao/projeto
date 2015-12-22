package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.BemClassificacaoDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoFormaComposicaoDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoFormaDao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.CadastroAcao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.FormulaProduto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificacao;
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
	private BemClassificacaoDao bemClassificacaoDao;

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
		dao.save(result);
		
		// proceder a exclusao dos registros
		for (ProducaoForma producaoForma : result.getProducaoFormaList()) {
			if (producaoForma.getProducaoFormaComposicaoList() != null) {
				for (ProducaoFormaComposicao producaoFormaComposicao: producaoForma.getProducaoFormaComposicaoList()) {
					if (CadastroAcao.E.equals(producaoFormaComposicao.getCadastroAcao())) {
						producaoFormaComposicaoDao.delete(producaoFormaComposicao);
					}
				}
			}
			if (CadastroAcao.E.equals(producaoForma.getCadastroAcao())) {
				producaoFormaDao.delete(producaoForma.getId());
			}
		}
		
		FormulaProduto formulaProduto = pegaFormula(bemClassificacaoDao.findOne(result.getBem().getBemClassificacao().getId()));
		
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
			producaoForma.setVolume(formulaProduto.volume(producaoForma));

			producaoFormaDao.save(producaoForma);
			
			Integer ordem = 0;
			for (ProducaoFormaComposicao producaoFormaComposicao: producaoForma.getProducaoFormaComposicaoList()) {
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

	private FormulaProduto pegaFormula(BemClassificacao bemClassificacao) {
		if (bemClassificacao == null) {
			return null;
		} else if (bemClassificacao.getFormula() != null) {
			return bemClassificacao.getFormula();
		} else {
			return pegaFormula(bemClassificacao.getBemClassificacao());
		}
	}

}