package br.gov.df.emater.aterwebsrv.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ManipulaExcecao {

//	@Autowired
//	private MessageSource messageSource;

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Resposta handleAllException(HttpServletRequest req, Exception ex) {
//		Locale locale = LocaleContextHolder.getLocale();
//		String mensagem = messageSource.getMessage("error.no.smartphone.id", null, locale);

		Resposta resposta = new Resposta(ex);
		return resposta;
	}
}