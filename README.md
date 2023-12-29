# Akka Challenge IDAaaS

This project is is an implementation of a master-slave architecture under the Akka toolkit.

## Requirements

You will need to have or install all the following software packages:

* [Maven](https://maven.apache.org/) 
* (optional) [Eclipse](https://eclipseide.org/) 


## Build the program

For building the program, just type the following command in the main directory:

```
mvn package
```

## Check the program

For checking the program by test, just type the following command in the main directory:

```
mvn test
```

## Run the program 

For running the program, just type the following command in the main directory by specifying the role "master" or "slave":

```
java -jar target/IDAaaS-0.0.1-SNAPSHOT-jar-with-dependencies.jar <role>
```

## Clean the program

For cleaning the program, just type the following command in the main directory:

```
mvn clean
```

## Documentation

Use the following command to run the automated documentation for this project:

```
mvn javadoc:javadoc
```

Source code documentation is available in `target/site/apidocs/index.html`.

## Licence

Apache License Version 2.0

## Version

2.0

## Author

* **Noureddine Mouhoub** - (*GitHub:* [NoureddineMouhoub](https://github.com/nmouhoub))