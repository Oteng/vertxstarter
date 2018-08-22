# Vert.x Maven Starter

This project is a template to start your own Vert.x project using Apache Maven. It has 
been structured to resemble normal project structure you have been used to in other 
frameworks.

## Structure
    +-starter
    |  +-MainVerticle.java
    |  |  +-controllers
    |  |  |  +-IndexController.java
    |  |  +-models
    |  |  | +-User.java
    |  |  +-routes
    |  |  |  +-Routes.java
    |  |  +-util
    |  |  |  +-BaseController.java
    |  |  |  +-BaseModel.java
    |  |  |  +-Config.java
    |  |  |  +-Constant.java
    |  |  |  +-CORS.java
    

### `starter` Package
This package is the root package of the application. It contains the MainVerticle.java 
file. This file services as the entry point for the webserver. It is not meant to contain 
business logic but as a glue for the rest of the application. 

### `controllers` Package
This package contains all the controllers of the application. This was intended to be 
one class for one endpoint. However you can do whatever you want :)

### `models` Package
This package contains classes who's lower case names corresponds to tables in the database. 
The class inherit BaseModel which provides methods for CRUD operation. 

### `routes`Package
This package contains only one file, the Routes.java file. This is where all the endpoints 
of the application is defined and it is also the point where all the controllers are 
imported and used.
__TODO:__ *find a way to automatically load controllers and bind them to their routes*

### `util` Package
This package contains stuff that other files of the application uses. 

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
## Building the project

To build the project, just use:

`mvn clean package`

It generates a fat-jar in the target directory.
