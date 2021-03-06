package br.gov.df.emater.aterwebsrv.bo.util;

import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;

@Service("UtilEmailEnviarCmd")
public class EmailEnviarCmd extends _Comando {

	@Autowired
	private JavaMailSender javaMailSender;

	@SuppressWarnings("unchecked")
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Map<String, Object> requisicao = (Map<String, Object>) contexto.getRequisicao();
		String para = (String) requisicao.get("para");
		String copiaPara = (String) requisicao.get("copiaPara");
		String remetente = (String) requisicao.get("remetente");
		String assunto = (String) requisicao.get("assunto");
		String texto = (String) requisicao.get("texto");

		MimeMessage mail = javaMailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(mail, false, "UTF-8");
		if (para != null) {
			helper.setTo(para);
		}
		if (copiaPara != null) {
			helper.setReplyTo(copiaPara);
		}
		if (remetente != null) {
			helper.setFrom(remetente);
		}
		if (assunto != null) {
			helper.setSubject(assunto);
		}
		if (texto != null) {
			helper.setText(texto);
		}

		javaMailSender.send(mail);

		return false;
	}

}
