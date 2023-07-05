# chubbio-rest-quarkus Project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

# To run the application locally with application-local file, use below commands:

```
mvn -Dquarkus-profile=local clean install

mvn -Dquarkus-profile=local quarkus:dev
```

# At runtime if you want to pick the properties from application.yaml then use the below command 

```
 java -Dquarkus-profile=dev -jar target/quarkus-app/quarkus-run.jar
```

# Prerequisites

- Make sure you have java 11 installed in your machine.
- To start with a quarkus- karavan project, you need to install the "Karavan" extension in visual studio code.

# Generating a quarkus project

- We can generate a quarkus maven project using quarkus intializer
  (Refer: https://code.quarkus.io/)
- Required dependencies can also be added while generating.
  For example: In case of REST the following extensions can be selected

<!-- for local -->
![addextensions](/quarkus-images/extensions.PNG)

<!-- for deployment use below syntax -->
<!-- ![addextensions](https://nausp-aapp0001.aceins.com/chubb-io/chubbio-reference-implementation/blob/master/Karavan-Quarkus/REST%20Quarkus%20-%20Karavan%20Implementation/images/extensions.PNG) -->

# Creating Routes with Karavan

- Open the generated folder in visual studio code
- Create routes folder in src/main/resources/ folder
- Right click on the routes folder and select "Karavan:Create Integration" option, and provide the yaml file name (refer: rest-sample.yaml)
- Newly created file will open in the karavan palette, else right click on newly created file and select "Karavan: Open"
- Click on Create Route option and from the popup select source/from components

![addcomponents](/quarkus-images/addcomponents.PNG)

- If you have selected direct component, configure route details as below

![direct](/quarkus-images/direct.png)

- Similarly add all the needed components like log, Set Body, Marshal, etc as per the requirement by clicking the + icon as shown below. (refer: rest-sample.yaml)

![addicon](/quarkus-images/add.jpg)

- For the REST quarkus implementation, we need to define our services under REST Tab

![create-rest](/quarkus-images/create-rest.PNG)

- Click on Create REST Service -> Add Method. For adding different methods

![select-method](/quarkus-images/select-method.PNG)

- For a simple get message, configuration can be done by selecting routes from "to" dropdown

![get-call](/quarkus-images/get-call.PNG)

- For Fetching records from mongoDB. Fill out the connection bean, collection name, db name, operation & output type

![get-records](/quarkus-images/get-records.PNG)

- REST service for the above route can be configured as:

![get-records-rest](/quarkus-images/get-records-rest.PNG)

- For fetching a single record from mongoDB

![get-single-record](/quarkus-images/get-single-record.PNG)

- REST service for fetching a single record can be configured as:

![get-single-record-rest](/quarkus-images/get-single-record-rest.PNG)

- For inserting record to mongoDB

![insert-record](/quarkus-images/insert-record.PNG)

- REST service for the above route can be configured as:

![insert-record-rest](/quarkus-images/insert-record-rest.PNG)

- **Configure below property in the application.yaml file**  - Refer application.yaml file
```
quarkus:
  mongodb:
    connection-string:
```

**Note:** Also check the **classpath**, if you are running the quarkus application in your local.

1. For **Cold** Deployments

- When deploying the quarkus application, use the command:

`routes-include-pattern: "classpath:routes/*"`

2. For **Hot Local** development

- When running the quarkus application locally, use the command:

`routes-include-pattern: "classpath:routes/route-sample.yaml"`

where **route-sample.yaml** is the route file which you want to run and test.

- For running multiple route files (say 'route-sample1.yaml', 'route-sample2.yaml', 'route-sample3.yaml') at once, use the following command:

```

classpath:routes/route-sample1.yaml,routes/route-sample2.yaml,routes/route-sample3.yaml

```

# How to Test locally
- First run the application using **mvn quarkus:dev** command.
- Access your application at the port: **http://localhost:8080**.
- Say, you have created a GET API endpoint (say 'persons') which fetches the list of all the person details from the mongodb database properly configured as a REST service.
- Hit the API endpoint (say **http://localhost:8080/persons**) which you have configured, to check if its retrieving the data. (**Postman** application is recommended for testing locally)
- Similarly, test out all the other CRUD operations (API calls such as: POST, DELETE, PUT) which you have created for your application.

# Compile & Run the application using following commands

- Use command **mvn clean package** to package the source code and creates a runnable jar in target/quarkus-app directory
- Use command **mvn package -Dquarkus.package.type=uber-jar** to generate the runnable jar
- Use command **mvn quarkus:dev** to run the application with hot reload (Changing the files need not required to build again)


# Packaging and running the application

### Quarkus runs only on Java 11 or more and maven should be installed in your system

- **mvn clean package**
    - packages the source code and creates a runnable jar in target/quarkus-app directory
    - this is **NOT** a uber-jar (not a independent runnable jar), all the required dependencies are copied in the target/quarkus-app/lib/ directory
    - **java -jar target/quarkus-app/quarkus-run.jar** will run the application

- **mvn clean package -Dquarkus.package.type=uber-jar**
    - packages the sources code and the uber-jar is generated in target directory with name <project-name>-runner.jar
    - **java -jar target/<project-name>-runner.jar** will run the application

- **mvn quarkus:dev**
    - runs the application in dev mode with hot deployment enable (change in any files will reflect the changes without rebuilding)

## Creating a native executable

You can create a native executable using:
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/chubbio-rest-quarkus-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Related Guides

- REST Client Classic ([guide](https://quarkus.io/guides/rest-client)): Call REST services
- RESTEasy Classic ([guide](https://quarkus.io/guides/resteasy)): REST endpoint framework implementing JAX-RS and more

## Provided Code

### REST Client

Invoke different services through REST with JSON

[Related guide section...](https://quarkus.io/guides/rest-client)

### RESTEasy JAX-RS

Easily start your RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)
