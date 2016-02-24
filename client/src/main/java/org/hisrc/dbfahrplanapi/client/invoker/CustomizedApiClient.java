package org.hisrc.dbfahrplanapi.client.invoker;

import com.fasterxml.jackson.databind.DeserializationFeature;

public class CustomizedApiClient extends ApiClient {

	public CustomizedApiClient() {
		this.getObjectMapper().enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
	}

}