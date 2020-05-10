# Sims and Customers Management Application



### Introduction



This is a Spring-boot 2 SIM Card and Customer Management application which can run in parallel in a distributed cluster.

For this purpose Spring Eureka was used to manage the different instances and is configured on port 8761. 
Hence the Eureka server application needs to be started first. 
When all instances are active, they will register with the server 
application since they are discovered because of the 
@EnableDiscoveryClient annotation.



### Load balancing



For Client side load balancing, there are several ways how this can be achieved. 
The most straight forward using 
Spring Netflix Cloud Ribbon or a separate loadbalancer like NGinx. 
However the instances can be dockerised and run
as a Swarm or otherwise Kubernetes can be used. 
Ideally this setup would be as infrastructure as code with tools
such as Chef or Puppet for easier automation. 
If a cloud provider is used there are cloud provider agnostic languages
 like Terraform that can be used.


The port settings (default is 8081) for different instances need to be set from file application.yml. 
It is important
that scheduling (default is ON) is switched on only on one of the instances otherwise 
the customers will be sent duplicate 
birthday emails and there will be multiple exports of birthday customers 
with their SIMS to file.

 

Ideally all configuration will be in Spring Cloud Config. 
Logging can be centralised for example using Zipkin.

 
For emails to be sent, SMTP settings need to be configured in application.yml file.




### Requirements



* Java 8

* Gradle version 6 or later




### Running the application



From root folder type:

    ./gradlew build         (For Windows type: gradlew build)



then to run it type:

    ./gradle bootRun        (For Windows type: gradle bootRun)





### Database Storage



For ease of development H2 database was used, however this can be changed from application.yml and the 
DB driver
dependency from build.gradle file. The H2 web-console can be found on the following url:

    

http://localhost:8081/h2-console/




### Testing



As regards testing there are 23 tests in all as follows:

 
* unit tests of controllers
 
* unit tests of services including the backend email and export operations as required
 
* integration test that create SIM Cards with and without customers

To see the test results, 

JaCoCo was set up to provide a neat web interface. To see this please go to:

    

build/reports/tests/test/index.html



and open the file in your favourite browser.

