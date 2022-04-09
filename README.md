# Guess my number with Kafka

### Intro

This is a small **Java Spring Boot** application **demonstration how to use Kafka** in your applications. 
The Kafka broker is set up for local development by using corresponding **docker containers**.

This application implements the **"Guess my number" game**. 
Clients are collaborating to find the number as quick as possible. Communication is done via Kafka events/topics.

### Setup Docker/Kafka

 * Make sure you have **installed Docker**; e.g. install Docker Desktop: https://www.docker.com/products/docker-desktop/
 * **Start the Docker containers** for *Zookeeper* and *Kafka*  by going in the folder `./docker` and starting `docker compose up -d` 
   * For redundancy two containers are started for each service (2x Zookeeper + 2x Kafka) 
   * Background information regarding Docker and Kafka can be found here: [kafka-docker-setup](https://www.baeldung.com/ops/kafka-docker-setup)
   * Test access to the Kafka container. For example using the *Big Data Tools* from IntelliJ connecting to `localhost:29092,localhost:39092`
   * Test that Zookeeper is running on `port 22181` and `port 32181`; e.g. use another IntelliJ Plugin like *zoolytic*
