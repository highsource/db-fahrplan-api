package org.hisrc.dbfahrplanapi.client.model.tests;

import java.io.IOException;
import java.io.InputStream;

import org.hisrc.dbfahrplanapi.client.invoker.CustomizedApiClient;
import org.hisrc.dbfahrplanapi.client.model.DepartureBoardResponse;
import org.hisrc.dbfahrplanapi.client.model.DepartureOrArrival;
import org.hisrc.dbfahrplanapi.client.model.JourneyDetailResponse;
import org.hisrc.dbfahrplanapi.client.model.LocationResponse;
import org.hisrc.dbfahrplanapi.client.model.Stop;
import org.hisrc.dbfahrplanapi.client.model.StopLocation;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiClientMapperTest {

	private ObjectMapper sut = new CustomizedApiClient().getObjectMapper();

	@Test
	public void parsesDeparture() throws IOException {
		try (InputStream is = getClass().getResourceAsStream("departure.json")) {
			DepartureBoardResponse response = sut.readValue(is, DepartureBoardResponse.class);
			Assert.assertEquals(8, response.getDepartureBoard().getDeparture().size());
			final DepartureOrArrival departure = response.getDepartureBoard().getDeparture().get(0);
			Assert.assertEquals("RE 15306", departure.getName());
			Assert.assertEquals("RE", departure.getType());
			Assert.assertEquals(8000105, departure.getStopid().intValue());
			Assert.assertEquals("Frankfurt(Main)Hbf", departure.getStop());
			Assert.assertEquals(LocalTime.parse("15:01"), departure.getTime());
			Assert.assertEquals(LocalDate.parse("2016-02-22"), departure.getDate());
			Assert.assertEquals("Limburg(Lahn)", departure.getDirection());
			Assert.assertEquals("3", departure.getTrack());}
	}
	@Test
	public void parsesJourneyDetails() throws IOException {
		try (InputStream is = getClass().getResourceAsStream("journeyDetails.json")) {
			JourneyDetailResponse response = sut.readValue(is, JourneyDetailResponse.class);
			Assert.assertEquals(6, response.getJourneyDetail().getStops().getStop().size());
			Assert.assertEquals(1, response.getJourneyDetail().getNames().getName().size());
			Assert.assertEquals(1, response.getJourneyDetail().getTypes().getType().size());
			Assert.assertEquals(1, response.getJourneyDetail().getOperators().getOperator().size());
			Assert.assertEquals(1, response.getJourneyDetail().getNotes().getNote().size());
			final Stop stop = response.getJourneyDetail().getStops().getStop().get(0);
			Assert.assertEquals("Frankfurt(Main)Hbf", stop.getName());
			Assert.assertEquals(8000105, stop.getId().intValue());
			Assert.assertEquals(8.663785, stop.getLon(), 0.000001);
			Assert.assertEquals(50.107149, stop.getLat(), 0.000001);
			Assert.assertEquals(LocalTime.parse("15:02"), stop.getDepTime());
			Assert.assertEquals(LocalDate.parse("2016-02-22"), stop.getDepDate());
			Assert.assertEquals(0, stop.getRouteIdx().intValue());
			Assert.assertEquals("13", stop.getTrack());
		}
	}
	@Test
	public void parsesLocation() throws IOException {
		try (InputStream is = getClass().getResourceAsStream("location.json")) {
			LocationResponse response = sut.readValue(is, LocationResponse.class);
			Assert.assertEquals(50, response.getLocationList().getStopLocation().size());
			final StopLocation stopLocation = response.getLocationList().getStopLocation().get(0);
			Assert.assertEquals(8000105, stopLocation.getId().intValue());
			Assert.assertEquals("Frankfurt(Main)Hbf", stopLocation.getName());
			Assert.assertEquals(8.663785, stopLocation.getLon(), 0.000001);
			Assert.assertEquals(50.107149, stopLocation.getLat(), 0.000001);
		}
	}

}
