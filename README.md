# Vert.x Maven Starter

This project is a template to start your own Vert.x project using Apache Maven.

## Prerequisites

    Apache Maven

    JDK 8+

## Getting started

Create your project with:

`git clone https://github.com/Oteng/vertxstarter.git PROJECT_NAME`

Replace PROJECT_NAME with the name of your project.


## Running the project

Once you have retrieved the project, you can check that everything works with:

`mvn test exec:java`
`mvn compile vertx:run`

The command compiles the project and runs the tests, then it launches the application, so you can check by yourself. Open your browser to http://localhost:9001. You should see a Hello World message.
Anatomy of the project

The project contains:

    a pom.xml file

    a main verticle file (src/main/java/io/vertx/starter/MainVerticle.java)

    an unit test (src/main/test/io/vertx/starter/MainVerticleTest.java)


## Note
	the redeploy script are also configurable
Building the project

To build the project, just use:

`mvn clean package`

It generates a fat-jar in the target directory.
