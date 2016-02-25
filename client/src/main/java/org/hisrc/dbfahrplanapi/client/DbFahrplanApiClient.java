package org.hisrc.dbfahrplanapi.client;

import java.util.List;

import org.hisrc.dbfahrplanapi.client.model.DepartureOrArrival;
import org.hisrc.dbfahrplanapi.client.model.JourneyDetail;
import org.hisrc.dbfahrplanapi.client.model.JourneyDetailRef;
import org.hisrc.dbfahrplanapi.client.model.StopLocation;
import org.joda.time.LocalDateTime;

public interface DbFahrplanApiClient {

	public List<DepartureOrArrival> arrivalBoard(String stopId, LocalDateTime dateTime) throws DbFahrplanApiException;

	public List<DepartureOrArrival> arrivalBoard(String stopId, LocalDateTime dateTime, String lang)
			throws DbFahrplanApiException;

	public List<DepartureOrArrival> departureBoard(String stopId, LocalDateTime dateTime) throws DbFahrplanApiException;

	public List<DepartureOrArrival> departureBoard(String stopId, LocalDateTime dateTime, String lang)
			throws DbFahrplanApiException;

	public List<StopLocation> locationName(String input) throws DbFahrplanApiException;

	public List<StopLocation> locationName(String input, String lang) throws DbFahrplanApiException;

	public JourneyDetail journeyDetail(JourneyDetailRef journeyDetailRef) throws DbFahrplanApiException;

	public JourneyDetail journeyDetail(JourneyDetailRef journeyDetailRef, String lang) throws DbFahrplanApiException;
}
