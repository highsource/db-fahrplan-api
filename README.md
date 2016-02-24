# LH Public API Java Client

This projects provides a Java Client for [LH Public API](https://developer.lufthansa.com/docs).

This client is based of the [Swagger](http://swagger.io/specification/)/[OpenAPI](https://github.com/OAI/OpenAPI-Specification) specifications for the LH Public API from the [lhapi-specification](https://github.com/highsource/lhapi-specification) project.

* [lhapi-specification](https://github.com/highsource/lhapi-specification/blob/master/lhapi-specification.yaml)

## Usage

### Adding the client to your project

#### Maven

Add the following dependency to your project:

```xml
<dependency>
	<groupId>org.hisrc.lhapi</groupId>
	<artifactId>lhapi-client</artifactId>
	<version>...</version>
</dependency>
```

### Using the client in your Java code

### Creating the client

```
LhApiClient client = new AuthenticatingLhApiClient(clientId, clientSecret);
```

### Getting the flight status

Flight status of `LO379` (today):

```
FlightStatusResponse departuresStatus = client.flightStatus("LO379", LocalDate.now());
```

### Getting departures

Departures from DME +/- one hour from now:

```
FlightsStatusResponse departuresStatus = client.departuresStatus(
	"DME",
	LocalDateTime.now().minusHours(1),
	LocalDateTime.now().plusHours(1));
```

### Getting arrivals

Arrivals to FRA +/- one hour from now:

```
FlightsStatusResponse arrivalsStatus = client.arrivalsStatus(
	"FRA",
	LocalDateTime.now().minusHours(1),
	LocalDateTime.now().plusHours(1));
```

## License

Please note that this code is currently under the [GPL-3.0](https://opensource.org/licenses/GPL-3.0) license. We plan to switch to MIT or BSD or dual licensing in the future.