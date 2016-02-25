package org.hisrc.dbfahrplanapi.client;

import org.hisrc.dbfahrplanapi.client.invoker.ApiException;

public class DbFahrplanApiException extends RuntimeException {
	private static final long serialVersionUID = 8301313499805392414L;

	public DbFahrplanApiException(String message, ApiException cause) {
		super(message, cause);
	}

	@Override
	public synchronized ApiException getCause() {
		return (ApiException) super.getCause();
	}
}
