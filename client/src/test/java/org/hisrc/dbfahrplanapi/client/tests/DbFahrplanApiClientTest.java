package org.hisrc.dbfahrplanapi.client.tests;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Properties;

import org.hisrc.dbfahrplanapi.client.DbFahrplanApiClient;
import org.hisrc.dbfahrplanapi.client.DefaultDbFahrplanApiClient;
import org.hisrc.dbfahrplanapi.client.invoker.ApiException;
import org.hisrc.dbfahrplanapi.client.model.DepartureOrArrival;
import org.hisrc.dbfahrplanapi.client.model.JourneyDetail;
import org.hisrc.dbfahrplanapi.client.model.JourneyDetailRef;
import org.hisrc.dbfahrplanapi.client.model.StopLocation;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DbFahrplanApiClientTest {

	private static final String AUTH_KEY_PROPERTY_KEY = "authKey";

	private static final String DB_FAHRPLAN_API_PROPERTIES_RESOURCE_NAME = "db-fahrplan-api.properties";

	private DbFahrplanApiClient sut;

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
		sut = new DefaultDbFahrplanApiClient(authKey);
	}

	@Test
	public void returnsLocationResponse() throws ApiException {
		final List<StopLocation> stopLocations = sut.locationName("frankfurt hbf");
		Assert.assertNotNull(stopLocations);
		Assert.assertTrue(stopLocations.size() > 1);
	}

	@Test
	public void returnsDepartureBoardResponse() throws ApiException {
		List<DepartureOrArrival> departures = sut.departureBoard("008000105", LocalDateTime.now().withTime(15, 01, 0, 0));
		Assert.assertTrue(departures.size() > 1);
	}

	@Test
	public void returnsArrivalBoardResponse() throws ApiException {
		List<DepartureOrArrival> arrivals = sut.arrivalBoard("008000105", LocalDateTime.now().withTime(15, 01, 0, 0));
		Assert.assertTrue(arrivals.size() > 1);
	}

	@Test
	public void returnsJourneyDetailResponse() throws ApiException, UnsupportedEncodingException {
		List<DepartureOrArrival> arrivals = sut.arrivalBoard("008000105", LocalDateTime.now().withTime(15, 01, 0, 0));
		final JourneyDetailRef journeyDetailRef = arrivals.get(0).getJourneyDetailRef();
		final JourneyDetail journeyDetail = sut.journeyDetail(journeyDetailRef);
		Assert.assertNotNull(journeyDetail);
	}
}