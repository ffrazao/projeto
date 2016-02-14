package br.gov.df.emater.aterwebsrv.bo.atividade;

import java.util.Calendar;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.modelo.atividade.Atividade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadeFinalidade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadeFormato;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadeNatureza;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadePrioridade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadeSituacao;

@Service("AtividadeNovoCmd")
public class NovoCmd extends _Comando {

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Atividade result = (Atividade) contexto.getRequisicao();

		if (result == null) {
			result = new Atividade();
		}

		result.setCodigo("ABCD.EFGH-3");
		result.setFormato(AtividadeFormato.E);
		result.setPrioridade(AtividadePrioridade.N);
		result.setFinalidade(AtividadeFinalidade.O);
		result.setNatureza(AtividadeNatureza.D);
		result.setSituacao(AtividadeSituacao.C);
		Calendar hoje = Calendar.getInstance();
		result.setInicio(hoje);
		result.setPrevisaoConclusao(hoje);
		result.setConclusao(hoje);
		result.setDuracaoEstimada(0);
		result.setDuracaoReal(0);

		contexto.setResposta(result);

		return true;
	}

}