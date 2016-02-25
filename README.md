# Deutsche Bahn Fahrplan API

This projects provides a [Swagger](http://swagger.io/specification/)/[OpenAPI](https://github.com/OAI/OpenAPI-Specification) specification for the Deutsche Bahn Fahrplan API:

* [db-fahrplan-api-specification.yaml](https://github.com/highsource/db-fahrplan-api/blob/master/db-fahrplan-api-specification.yaml)

Additionally, the project also provides a simple Java Client generated using [Swagger Codegen](https://github.com/swagger-api/swagger-codegen) and slightly customized.

## Usage

### Adding the client to your project

#### Maven

Add the following dependency to your project:

```xml
<dependency>
	<groupId>org.hisrc.db-fahrplan-api</groupId>
	<artifactId>db-fahrplan-api-client</artifactId>
	<version>...</version>
</dependency>
```

### Using the client in your Java code

#### Creating the client

```
DbFahrplanApiClient client = new DefaultDbFahrplanApiClient(authKey);
```

#### Querying locations

```
List<StopLocation> stopLocations = client.locationName("Frankfurt Hbf");
```

#### Querying departures or arrivals

```
List<DepartureOrArrival> arrivals = client.arrivalBoard("008000105", LocalDateTime.now());
List<DepartureOrArrival> departures = client.departureBoard("008000105", LocalDateTime.now());
```

#### Querying journey details

```
List<DepartureOrArrival> arrivals = client.arrivalBoard("008000105", LocalDateTime.now());
JourneyDetailRef journeyDetailRef = arrivals.get(0).getJourneyDetailRef();
JourneyDetail journeyDetail = client.journeyDetail(journeyDetailRef);
```