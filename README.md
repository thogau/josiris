# Josiris

Java [Osiris](https://github.com/siric-osiris/OSIRIS) pivot files importer.

## System requirements
- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or later.
- [git](https://git-scm.com/) client.

## Target database
- By default, patients are persisted in [HSQLDB](https://hsqldb.org/) in memory database.
- To persist data in [MySQL](https://www.mysql.com/) or [PostgreSQL](https://www.postgresql.org/) :
	- Install a MySQL/PostgreSQL server.
	- Create a database called `josiris`.
	- Comment/uncomment accordingly in `src/main/resources/application.properties`.

## Running the Application Locally
- Open command line.
- Clone the project on your computer typing `git clone https://github.com/thogau/josiris`.
- Navigate to `josiris` directory.
- type `java -version` and make sure you are using [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or later.
- Run the project typing `./mvnw`.
- Open http://localhost:8080/josiris in your browser.
- there is an example set of pivot files that you can import in `pivot` directory.

The project is a standard Maven project. You can import it to Eclipse as you would with any Maven project.

## Deploying to Production
To create a production build, call `./mvnw clean package -Pproduction`.
This will build a JAR file with all the dependencies and front-end resources,
ready to be deployed. The file can be found in the `target` folder after the build completes.

Once the JAR file is built, you can run it using
`java -jar target/josiris-1.0-SNAPSHOT.jar`
