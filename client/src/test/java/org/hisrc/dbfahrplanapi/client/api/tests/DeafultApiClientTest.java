package org.hisrc.dbfahrplanapi.client.api.tests;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.Properties;

import org.hisrc.dbfahrplanapi.client.api.DefaultApi;
import org.hisrc.dbfahrplanapi.client.invoker.ApiException;
import org.hisrc.dbfahrplanapi.client.invoker.CustomizedApiClient;
import org.hisrc.dbfahrplanapi.client.model.ArrivalBoardResponse;
import org.hisrc.dbfahrplanapi.client.model.DepartureBoardResponse;
import org.hisrc.dbfahrplanapi.client.model.DepartureOrArrival;
import org.hisrc.dbfahrplanapi.client.model.JourneyDetailResponse;
import org.hisrc.dbfahrplanapi.client.model.LocationResponse;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DeafultApiClientTest {

	private static final String AUTH_KEY_PROPERTY_KEY = "authKey";

	private static final String DB_FAHRPLAN_API_PROPERTIES_RESOURCE_NAME = "db-fahrplan-api.properties";

	private DefaultApi sut;

	@Before
	public void createDefaultApi() throws IOException {
		final Properties properties = new Properties();
		final InputStream is = getClass().getClassLoader()
				.getResourceAsStream(DB_FAHRPLAN_API_PROPERTIES_RESOURCE_NAME);
		Assert.assertNotNull(MessageFormat.format(
				"Could not find the [{0}] resource. For tests, please create src/test/resources/{0} with authKey=<authKey> property.",
				DB_FAHRPLAN_API_PROPERTIES_RESOURCE_NAME));
		properties.load(is);
		final String authKey = properties.getProperty(AUTH_KEY_PROPERTY_KEY);
		sut = new DefaultApi(new CustomizedApiClient());
		sut.getApiClient().setApiKey(authKey);
	}

	@Test
	public void returnsLocationResponse() throws ApiException {
		final LocationResponse locationResponse = sut.locationNameGet("json", "frankfurt hbf", "de");
		Assert.assertNotNull(locationResponse);
		Assert.assertTrue(locationResponse.getLocationList().getStopLocation().size() > 1);
	}

	@Test
	public void returnsDepartureBoardResponse() throws ApiException {
		DepartureBoardResponse departureBoardGet = sut.departureBoardGet("json", "8000105",
				LocalDate.now().toString("yyyy-MM-dd"), "15:01", "en");
		Assert.assertTrue(departureBoardGet.getDepartureBoard().getDeparture().size() > 1);
	}

	@Test
	public void returnsArrivalBoardResponse() throws ApiException {
		ArrivalBoardResponse arrivalBoardGet = sut.arrivalBoardGet("json", "8000105",
				LocalDate.now().toString("yyyy-MM-dd"), "15:01", "en");
		Assert.assertTrue(arrivalBoardGet.getArrivalBoard().getArrival().size() > 1);
	}

	@Test
	public void returnsJourneyDetailResponse() throws ApiException, UnsupportedEncodingException {
		DepartureBoardResponse departureBoardGet = sut.departureBoardGet("json", "8000105",
				LocalDate.now().toString("yyyy-MM-dd"), "15:01", "en");
		final DepartureOrArrival departureOrArrival = departureBoardGet.getDepartureBoard().getDeparture().get(0);
		String ref = departureOrArrival.getJourneyDetailRef().getRef();
		String refValue = ref.substring(ref.indexOf("?ref=") + 5);
		String decodedRefvalue = URLDecoder.decode(refValue, "UTF-8");
		final JourneyDetailResponse journeyDetailResponse = sut.journeyDetailGet("json", decodedRefvalue, "en");
		Assert.assertTrue(journeyDetailResponse.getJourneyDetail().getStops().getStop().size() > 1);
	}
}
