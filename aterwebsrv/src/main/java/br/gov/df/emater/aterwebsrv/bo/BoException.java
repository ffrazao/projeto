package br.gov.df.emater.aterwebsrv.bo;

public class BoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BoException() {
		super();
	}

	public BoException(String message) {
		super(message);
	}

	public BoException(String message, Throwable cause) {
		super(message, cause);
	}

	public BoException(Throwable cause) {
		super(cause);
	}
}