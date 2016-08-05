package br.gov.df.emater.aterwebsrv.bo.projeto_credito_rural;

import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaList;
import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaReg;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.projeto_credito_rural.ProjetoCreditoRuralDao;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoPropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.atividade.Atividade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.MetodoCodigo;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRural;

@Service("ProjetoCreditoRuralVisualizarCmd")
public class VisualizarCmd extends _Comando {

	@Autowired
	private ProjetoCreditoRuralDao dao;

	@Autowired
	private EntityManager em;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Atividade atividade = (Atividade) contexto.getResposta();

		// verificar se o método corresponde a este comando
		if (atividade.getMetodo() == null || !MetodoCodigo.PROJETO_CREDITO_RURAL.equals(atividade.getMetodo().getCodigo())) {
			return false;
		}

		// restaurar
		ProjetoCreditoRural result = dao.findOneByAtividade(atividade);
		if (result == null) {
			throw new BoException("Registro não localizado");
		}

		// ajustar dados de retorno
		List<PublicoAlvoPropriedadeRural> publicoAlvoPropriedadeRuralList = null;
		if (result.getPublicoAlvo() != null && result.getPublicoAlvo().getPublicoAlvoPropriedadeRuralList() != null) {
			publicoAlvoPropriedadeRuralList = infoBasicaList(result.getPublicoAlvo().getPublicoAlvoPropriedadeRuralList());
		}
		result = infoBasicaReg(result);
		if (result.getPublicoAlvo() != null) {
			result.getPublicoAlvo().setPublicoAlvoPropriedadeRuralList(publicoAlvoPropriedadeRuralList);
		}

		em.detach(result);

		// ATENÇÃO! este tipo de comando não altera o campo resposta do
		// contexto, mas sim o objeto da resposta
		atividade.setProjetoCreditoRural(result);

		return false;
	}

}