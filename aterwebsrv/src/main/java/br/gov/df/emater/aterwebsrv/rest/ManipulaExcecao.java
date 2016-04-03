package br.gov.df.emater.aterwebsrv.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ManipulaExcecao {

	// @Autowired
	// private MessageSource messageSource;

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Resposta handleAllException(HttpServletRequest req, Exception ex) {
		// Locale locale = LocaleContextHolder.getLocale();
		// String mensagem = messageSource.getMessage("error.no.smartphone.id",
		// null, locale);

		// Resposta resposta = null;
		// if (ex instanceof TransactionSystemException && ex.getCause()
		// instanceof RollbackException) {
		// ex.printStackTrace();
		// } else {
		// resposta = new Resposta(ex);
		// }
		Resposta resposta;
		if (ex instanceof TransactionSystemException) {
			resposta = new Resposta(((TransactionSystemException) ex).getApplicationException() != null ? ((TransactionSystemException) ex).getApplicationException() : new Exception(ex));
		} else if (ex instanceof RuntimeException) {
			resposta = new Resposta(new Exception(ex));
		} else {
			resposta = new Resposta(ex);
		}
		return resposta;
	}
}