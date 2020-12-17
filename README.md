# CO<sub>2</sub> Sensor API

The application that can be used to store CO<sub>2</sub> measurements form multiple sensors through REST API. Written in Java with use of an in-memory storage (H2), taking advantage of Kafka's ability to deal with data streams.

### Prerequisites

To run the application locally please ensure:

- Java 10+
- Apache Kafka (`localhost:9092`)

### Building application

In order to build application locally please execute the following Maven command:

`mvn clean install`

### Running application

In order to run application on your local machine please execute the following command:

`mvn spring-boot:run`

### REST API docs

Once app is up and running, uou can find REST endpoints specification under the following link: 

http://localhost:8081/swagger-ui.html

### Implementation details

Please refer to `SensorController` class as it is the entry point to the application.