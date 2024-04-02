# Introduction
This Java application is a Stock Quote Application that allows a single user to view/buy/sell stocks using real time data. 

# Quick Start
To run the application, follow the below steps:
```bash
# STEP 1: navigate to the correct directory #
cd core_java/jdbc

# STEP 2: run application #
docker_user=tharunii
docker_path=${docker_user}/jdbc
docker pull ${docker_path}

docker run ${docker_path}
```

# Implementation 
Here's a brief summary of the key components and functionality of the application: <br>


# Test
Two logger files tells us the flow of the application: an info file to tell us what methods are running and an  error file that flags whenever a problem occurs. Junit and Mockito is used to run unit tests on specific methods and integration tests to ensure all methods are working together as one. 
The below command will allow the Junit tests to run:
```bash
mvn run...
```

The below lines of input was inputted into the main process for testing purposes and was compared against data on the Alpha Vantage API:
```bash
view new MSFT
view new GOOG
```

# Deployment
Maven was used to build the Java project, Docker was used to containerize the application, and GitHub for source code management. When running the application for the first time, make sure to pull the image from DockerHub:
```bash
docker pull ${docker_user}/jdbc
```

# Improvement 
1. Improve sell: currently you must sell all shares of a stock. Improve so you can choose a certain amount to sell. Also show the profits made during the sale.
2. Improve GUI: Use Swing in Java to create a more user friendly UI.
3. Fix limitation: Alpha Vantage is Freemium and only allows a certain amount of API calls. Potentially find another source or upgrade plan to fix this.

