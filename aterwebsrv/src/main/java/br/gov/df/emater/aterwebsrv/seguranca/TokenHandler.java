package br.gov.df.emater.aterwebsrv.seguranca;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.security.authentication.CredentialsExpiredException;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

public final class TokenHandler {

	private static final String HMAC_ALGO = "HmacSHA256";
	private static final String SEPARATOR = ".";
	private static final String SEPARATOR_SPLITTER = "\\.";

	private final Mac hmac;

	public TokenHandler(byte[] secretKey) {
		try {
			hmac = Mac.getInstance(HMAC_ALGO);
			hmac.init(new SecretKeySpec(secretKey, HMAC_ALGO));
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			throw new IllegalStateException("failed to initialize HMAC: " + e.getMessage(), e);
		}
	}

	// synchronized to guard internal hmac object
	private synchronized byte[] createHmac(byte[] content) {
		return hmac.doFinal(content);
	}
	/*
	 * public static void main(String[] args) { Date start = new Date(); byte[]
	 * secret = new byte[70]; new
	 * java.security.SecureRandom().nextBytes(secret);
	 * 
	 * TokenHandler tokenHandler = new TokenHandler(secret); for (int i = 0; i <
	 * 1000; i++) { final User user = new
	 * User(java.util.UUID.randomUUID().toString().substring(0, 8), new Date(
	 * new Date().getTime() + 10000)); user.grantRole(UserRole.ADMIN); final
	 * String token = tokenHandler.createTokenForUser(user); final User
	 * parsedUser = tokenHandler.parseUserFromToken(token); if (parsedUser ==
	 * null || parsedUser.getUsername() == null) { System.out.println("error");
	 * } } System.out.println(System.currentTimeMillis() - start.getTime()); }
	 */

	private ObjectMapper createObjectMapper() {
		ObjectMapper result = new ObjectMapper();
		result.setSerializationInclusion(Include.NON_EMPTY);
		result.setSerializationInclusion(Include.NON_NULL);
		return result;
	}

	public String createTokenForUser(Usuario user) {
		byte[] userBytes = toJSON(user);
		System.out.println(new String(userBytes));
		byte[] hash = createHmac(userBytes);
		final StringBuilder sb = new StringBuilder(170);
		sb.append(toBase64(userBytes));
		sb.append(SEPARATOR);
		sb.append(toBase64(hash));
		return sb.toString();
	}

	private byte[] fromBase64(String content) {
		return DatatypeConverter.parseBase64Binary(content);
	}

	private Usuario fromJSON(final byte[] userBytes) {
		try {
			ObjectMapper objectMapper = createObjectMapper();
			synchronized (objectMapper) {
				return objectMapper.readValue(new ByteArrayInputStream(userBytes), Usuario.class);
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public Usuario parseUserFromToken(String token) {
		final String[] parts = token.split(SEPARATOR_SPLITTER);
		if (parts.length == 2 && parts[0].length() > 0 && parts[1].length() > 0) {
			try {
				final byte[] userBytes = fromBase64(parts[0]);
				final byte[] hash = fromBase64(parts[1]);

				boolean validHash = Arrays.equals(createHmac(userBytes), hash);
				if (validHash) {
					final Usuario user = fromJSON(userBytes);
					if (new Date().getTime() < user.getExpires()) {
						return user;
					} else {
						throw new CredentialsExpiredException("Sessão expirada!");
					}
				}
			} catch (IllegalArgumentException e) {
				// log tempering attempt here
			}
		}
		return null;
	}

	private String toBase64(byte[] content) {
		return DatatypeConverter.printBase64Binary(content);
	}

	private byte[] toJSON(Usuario user) {
		try {
			ObjectMapper objectMapper = createObjectMapper();
			synchronized (objectMapper) {
				return objectMapper.writeValueAsBytes(user);
			}
		} catch (JsonProcessingException e) {
			throw new IllegalStateException(e);
		}
	}
}
