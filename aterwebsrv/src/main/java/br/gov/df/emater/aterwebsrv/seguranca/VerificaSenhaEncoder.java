package br.gov.df.emater.aterwebsrv.seguranca;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

public class VerificaSenhaEncoder implements PasswordEncoder {

	private final Log logger = LogFactory.getLog(getClass());

	public String encode(CharSequence rawPassword) {
		return null;
	}

	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return true;
	}
}
