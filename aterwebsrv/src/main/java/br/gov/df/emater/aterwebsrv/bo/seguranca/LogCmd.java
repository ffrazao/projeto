package br.gov.df.emater.aterwebsrv.bo.seguranca;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.sistema.LogAcao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.seguranca.UserAuthentication;

@Service("SegurancaLogCmd")
public class LogCmd extends _Comando {

	@Autowired
	private FacadeBo facade;

	@Override
	public boolean executar(_Contexto context) throws Exception {
		if (1 == 1) {
			return false;
		}
		LogAcao log = new LogAcao();
		ObjectMapper mapper = new ObjectMapper();

		log.setComandoChain(context.getAcao());
		if (context.getUsuario() != null) {
			Usuario usr = ((UserAuthentication) context.getUsuario()).getDetails();
			if (usr == null) {
				throw new BoException("Erro log operação, usuário não identificado");
			}
			log.setNomeUsuario(usr.getUsername());
			log.setUnidadeOrganizacionalId(usr.getLotacaoAtual() == null ? null : usr.getLotacaoAtual().getId());
			Calendar dtLogin = Calendar.getInstance();
			if (usr.getDetails() != null) {
				dtLogin.setTime(new Date((Long) usr.getDetails().get("DATA")));
				log.setDataLogin(dtLogin);
				log.setModuloId(Integer.parseInt((String) usr.getDetails().get("MODULO")));
				log.setNumeroIp((String) usr.getDetails().get("NUMERO_IP"));
				log.setBrowser((String) usr.getDetails().get("USER_AGENT"));
				log.setEnderecoOrigem((String) usr.getDetails().get("ORIGIN"));
				log.setEnderecoReferencia((String) usr.getDetails().get("REFERER"));
			}
		}
		// captação dos dados transitados
		try {
			log.setRequisicao(UtilitarioString.limitarTextoEm(mapper.writeValueAsString(context.getRequisicao()), 16777210));
		} catch (JsonMappingException e) {
		}
		try {
			log.setResposta(UtilitarioString.limitarTextoEm(mapper.writeValueAsString(context.getResposta()), 16777210));
		} catch (JsonMappingException e) {
		}
		try {
			log.setErro(UtilitarioString.limitarTextoEm(mapper.writeValueAsString(context.getErro()), 16777210));
		} catch (JsonMappingException e) {
		}

		facade._logAcaoSalvar(log);

		return false;
	}

}