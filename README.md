# Josiris

Java [Osiris](https://github.com/siric-osiris/OSIRIS) pivot files importer.

## System requirements
- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or later.

## Target database
- By default, patients are persisted in [HSQLDB](https://hsqldb.org/) in memory database.
- To persist data in [MySQL](https://www.mysql.com/) or [PostgreSQL](https://www.postgresql.org/) :
		- Install a MySQL/PostgreSQL server
		- Create a josiris database
		- Comment/uncomment accordingly in `src/main/resources/application.properties`

## Running the Application Locally
The project is a standard Maven project. To run it from the command line, type `./mvnw`, then open http://localhost:8080/josiris in your browser.
You can also import the project to Eclipse as you would with any Maven project.

## Deploying to Production

To create a production build, call `./mvnw clean package -Pproduction`.
This will build a JAR file with all the dependencies and front-end resources,
ready to be deployed. The file can be found in the `target` folder after the build completes.

Once the JAR file is built, you can run it using
`java -jar target/josiris-1.0-SNAPSHOT.jar`
