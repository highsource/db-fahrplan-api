package org.hisrc.dbfahrplanapi.client;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.hisrc.dbfahrplanapi.client.api.DefaultApi;
import org.hisrc.dbfahrplanapi.client.invoker.ApiException;
import org.hisrc.dbfahrplanapi.client.invoker.CustomizedApiClient;
import org.hisrc.dbfahrplanapi.client.model.ArrivalBoardResponse;
import org.hisrc.dbfahrplanapi.client.model.DepartureBoardResponse;
import org.hisrc.dbfahrplanapi.client.model.DepartureOrArrival;
import org.hisrc.dbfahrplanapi.client.model.JourneyDetail;
import org.hisrc.dbfahrplanapi.client.model.JourneyDetailRef;
import org.hisrc.dbfahrplanapi.client.model.JourneyDetailResponse;
import org.hisrc.dbfahrplanapi.client.model.LocationResponse;
import org.hisrc.dbfahrplanapi.client.model.StopLocation;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.sun.jersey.api.client.ClientHandlerException;

public class DefaultDbFahrplanApiClient implements DbFahrplanApiClient {

	private static final String FORMAT = "json";
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormat.forPattern("HH:mm");

	private final String defaultLang;
	private final DefaultApi api;

	public DefaultDbFahrplanApiClient(String authKey) {
		this(authKey, null);
	}

	public DefaultDbFahrplanApiClient(String authKey, String defaultLang) {
		Objects.requireNonNull(authKey);
		this.defaultLang = defaultLang;

		this.api = new DefaultApi(new CustomizedApiClient());
		this.api.getApiClient().setApiKey(authKey);
	}

	@Override
	public List<DepartureOrArrival> arrivalBoard(String stopId, LocalDateTime dateTime) {
		return this.arrivalBoard(stopId, dateTime, defaultLang);
	}

	@Override
	public List<DepartureOrArrival> departureBoard(String stopId, LocalDateTime dateTime) {
		return this.departureBoard(stopId, dateTime, defaultLang);
	}

	@Override
	public List<StopLocation> locationName(String input) {
		return this.locationName(input, defaultLang);
	}

	@Override
	public JourneyDetail journeyDetail(JourneyDetailRef journeyDetailRef) throws DbFahrplanApiException {
		return this.journeyDetail(journeyDetailRef, defaultLang);
	}

	@Override
	public List<StopLocation> locationName(String input, String lang) throws DbFahrplanApiException {
		Objects.requireNonNull(input);
		final LocationResponse locationResponse = execute(a -> a.locationNameGet(FORMAT, input, lang));
		if (locationResponse != null && locationResponse.getLocationList() != null
				&& locationResponse.getLocationList().getStopLocation() != null) {
			return locationResponse.getLocationList().getStopLocation();
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public List<DepartureOrArrival> arrivalBoard(String stopId, LocalDateTime dateTime, String lang)
			throws DbFahrplanApiException {
		Objects.requireNonNull(stopId);
		Objects.requireNonNull(dateTime);
		final ArrivalBoardResponse arrivalBoardResponse = execute(a -> a.arrivalBoardGet(FORMAT, stopId,
				DATE_FORMATTER.print(dateTime), TIME_FORMATTER.print(dateTime), lang));
		if (arrivalBoardResponse != null && arrivalBoardResponse.getArrivalBoard() != null
				&& arrivalBoardResponse.getArrivalBoard().getArrival() != null) {
			return arrivalBoardResponse.getArrivalBoard().getArrival();
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public List<DepartureOrArrival> departureBoard(String stopId, LocalDateTime dateTime, String lang)
			throws DbFahrplanApiException {
		Objects.requireNonNull(stopId);
		Objects.requireNonNull(dateTime);
		final DepartureBoardResponse departureBoardResponse = execute(a -> a.departureBoardGet(FORMAT, stopId,
				DATE_FORMATTER.print(dateTime), TIME_FORMATTER.print(dateTime), lang));
		if (departureBoardResponse != null && departureBoardResponse.getDepartureBoard() != null
				&& departureBoardResponse.getDepartureBoard().getDeparture() != null) {
			return departureBoardResponse.getDepartureBoard().getDeparture();
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public JourneyDetail journeyDetail(JourneyDetailRef journeyDetailRef, String lang) throws DbFahrplanApiException {
		Objects.requireNonNull(journeyDetailRef);
		final String ref = journeyDetailRef.getRef();

		String refPrefix = "ref=";
		final int refValueStartIndex = ref.indexOf(refPrefix);
		if (refValueStartIndex >= 0) {
			String refSuffix = "&";
			final int refValueEndIndex = ref.indexOf(refSuffix, refValueStartIndex + 1);
			final String refValue = refValueEndIndex >= 0
					? ref.substring(refValueStartIndex + refPrefix.length(), refValueEndIndex)
					: ref.substring(refValueStartIndex + refPrefix.length());
			final String decodedRefValue;
			try {
				decodedRefValue = URLDecoder.decode(refValue, "UTF-8");
			} catch (UnsupportedEncodingException uex) {
				throw new UnsupportedOperationException(
						MessageFormat.format("Could not decode the URL [{0}].", refValue), uex);
			}
			final JourneyDetailResponse journeyDetailResponse = execute(
					a -> a.journeyDetailGet(FORMAT, decodedRefValue, lang));
			if (journeyDetailResponse != null) {
				return journeyDetailResponse.getJourneyDetail();
			} else {
				return null;
			}
		} else {
			throw new DbFahrplanApiException("Could not read the ref parameter.", new ApiException(
					MessageFormat.format("JourneyDetailRef [{0}] does not seem to contain the parameter [ref].", ref)));
		}
	}

	private <T> T execute(ApiFunction<DefaultApi, T> operation) {
		try {
			return operation.apply(this.api);
		} catch (ApiException apiex) {
			throw new DbFahrplanApiException("Error executing the API operation.", apiex);
		} catch (ClientHandlerException chex1) {
			throw new DbFahrplanApiException("Error executing the API operation.", new ApiException(chex1));
		}
	}

	@FunctionalInterface
	private interface ApiFunction<T, R> {
		public R apply(T base) throws ApiException;
	}

}
