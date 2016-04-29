package br.gov.df.emater.aterwebsrv.bo.seguranca;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.modelo.sistema.LogAcao;

@Service("SegurancaLogCmd")
public class LogCmd extends _Comando {

	@Autowired
	private FacadeBo facade;

	@Override
	public boolean executar(_Contexto context) throws Exception {
		System.out.format("Log acao[%s], erro[%s], requisicao[%s], resposta[%s]\n", context.getAcao(), context.getErro(), context.getRequisicao(), context.getResposta());
		ObjectMapper mapper = new ObjectMapper();
		LogAcao log = new LogAcao();
		log.setComandoCodigo(context.getAcao());
		log.setData(Calendar.getInstance());
		if (context.getUsuario() != null) {
			log.setNomeUsuario(context.getUsuario().getName());
		}
		log.setRequisicao(mapper.writeValueAsString(context.getRequisicao()));
		log.setResposta(mapper.writeValueAsString(context.getResposta()));
		log.setErro(mapper.writeValueAsString(context.getErro()));

		facade._logAcaoSalvar(log);

		return false;
	}

}