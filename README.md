# Stock Portfolio Dashboard

# Overview
Stock Portfolio Dashboard provides tools for investors to quickly analyze an investment portfolio to help inform 
potential investment decisions (e.g., asset allocation based on some common investment patterns such as sector based 
allocation, market capitalization based allocation, and portfolio tilt).

The product is available at http://portfoliosight.com

# Features
Stock Portfolio Dashboard features:

Stock Breakdown
* Stock Breakdown uses IEX Cloud's `latestPrice` attribute to calculate the total market value for each symbol in the 
  portfolio and then builds market value based allocation visualizations. The `latestPrice` refers to the latest 
  relevant price of the security which is derived from multiple sources. IEX Cloud first looks for an IEX based real 
  time price. If an IEX real time price is older than 15 minutes, the 15-minute delayed price is used. If a 15-minute 
  delayed price is not available, then the current day close price is used. Otherwise, the last available closing price 
  will be used.
  
Sector Breakdown
* Sector breakdown uses IEX Cloud's `sector` attribute to build sector allocation visualizations. The `sector` refers 
  to the sector a security belongs to.
  
Market Cap Breakdown
* Market cap breakdown uses IEX Cloud's `marketCap` attribute to build market cap allocation visualizations for each 
  company in the portfolio. The `marketCap` of a security is calculated as shares outstanding * previous day close.

Each feature `GETS` data from IEX Cloud's `/stock` endpoint using a batch call. Batch calls can return data on up to 
one hundred symbols per request, significantly reducing network traffic.

# Frameworks, Libraries, and Tools
Spring Cloud:
* Spring Data JPA provides abstraction over the Data Access Layer using Java Persistence API and Hibernate as the ORM.
* Spring Security provides a powerful and highly customizable framework authentication, authorization, and protection 
  against common exploits (e.g. Cross-Site Request Forgery).
* Spring Web module provides basic web-oriented integration features such as multipart file upload functionality and 
  the initialization of the IoC container using Servlet listeners and a web-oriented application context. It also 
  contains an HTTP client, and the web-related parts of Spring’s remoting support.
  
AWS
* AWS Elastic Beanstalk is an easy-to-use service for deploying and scaling web applications. The service handles all 
the deployment needs for this application including provisioning, load balancing, auto-scaling, and health monitoring.
* AWS RDS is a distributed relational database service designed to simplify the setup, operation, and scaling of a 
relational database. Stock Portfolio Dashboard uses a MySQL database engine.

Data Processing
* Jackson-Databind converts JSON data from IEX Cloud into POJOs needed by the application. 

IEX Cloud
* IEX Cloud is a financial data infrastructure platform that connects this application to financial data creators. This 
application uses the IEX Cloud batch calls for its real-time and historical market data needs.
  
This application is deployed on AWS and is available at http://portfoliosight.com/login. However, developers interested 
in compiling the program need to establish an IEX Cloud account to make API calls. IEX Cloud offers both free and paid 
accounts. After establishing an account, navigate to the `console/tokens` endpoint and copy and paste the token into a 
`secrets.properties` file. The key-value pair should be `IexCloudApiKey=YOUR_IEX_CLOUD_TOKEN`
  
Thymeleaf
* Thymeleaf is a Java template engine for processing and creating HTML, JavaScript, and CSS that is integrated with 
  Spring MVC to serve the View Layer.

# How to compile the project

We use Apache Maven to compile and run this project.

You need to install Apache Maven (https://maven.apache.org/) on your system.

Type on the command line:

```bash
mvn clean compile
```

# How to create a binary runnable package

```bash
mvn clean package
```

# How to deploy

This application runs on AWS Elastic Beanstalk. New versions should be uploaded to Elastic Beanstalk environment and 
deployed into production.

# Run unit and integration tests

```bash
mvn clean compile test spotbugs:check
```

The check goal runs analysis like spotbugs goal, and will make the build fail if any bugs are found.

For more info see
https://spotbugs.readthedocs.io/en/latest/maven.html

SpotBugs https://spotbugs.github.io/ is the spiritual successor of FindBugs.