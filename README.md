Public Holidays API

This project is a Public Holidays API implemented in Java. You can query public holidays through its
REST endpoints. It uses https://date.nager.at/api/ as the data source.

- Spring Boot Reactive Framework is used.
- Maven is used as build automation tool.
- Google Java Format is used as formatting guideline. The IntelliJ XML configuration can be found in
  the
  /external folder.
- Caffeine Cache and Redis Cache are used.
- Docker is used as the container platform.

Features

- Reactive Framework - WebClient - Non-Blocking I/O : Allows threads to work on other tasks during
  Nager API calls.
- Double Cache Service: Uses Caffeine as a local cache and Redis as a remote cache.

ENDPOINTS:

- Last Holiday Endpoints
    - /v1/holiday/last/{country}
    - /v1/holiday/last/{country}/{count}
    - Given a country, return the last celebrated 3 holidays (date and name).

- Non Weekend Holidays
    - /v1/holiday/non-weekend/{year}/{country1}/{country2}
    - Given a year and country codes, for each country return a number of public holidays not
      falling on weekends (sort in descending order).

- Common Holidays
    - /v1/holiday/common/{year}/{country1}/{country2}
    - Given a year and 2 country codes, return the deduplicated list of dates celebrated in both
      countries (date + local names)

Prerequisites

- Java 21
- Maven 3.9.9
- Docker (Version 27.5.1, compose 2.32.4)

- Building the Project run:
    - mvn clean install -DskipTests

- Running Tests run:
    - Running tests requires local Redis instance. Therefore Redis Docker container must be up
      before running the tests
    - mvn test

- Running the Application run:
  docker-compose up --build

Design Considerations:

- Separation of Concerns: Input, output, client, controllers, service, cache configs are separated
  into different classes, making the code modular and testable.
- API Versioning
- Validation: Custom validation annotations have been developed. The available country list from the
  Nager API is used for country validation.
- Exception Handling: Robust exception handling is implemented.
- Easy-to-Use Cache Service: The cache service is designed for simplicity and ease of use.
- Testability: By separating Nager API calls and business logic into dedicated services (e.g.,
  PublicHoliday, LastCelebrated), unit tests can target these components directly.
- Faster Deployments: The Docker image has been optimized for Spring applications.

Future Improvements

- First WebClient requests takes 5 seconds. As a work-around, An warm-up process has been
  implemented to run startups. Proper solution needs to check.
- Testing: Improve test coverage for service layer and cache configs
- Test containers for Redis to be able to run tests without docker