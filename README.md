# Bdecisive-be

## Architecture Diagram
![SoftwareArchitectureUpdated.drawio.svg](SoftwareArchitectureUpdated.drawio.svg)

## Descripton 

### Layers
Presentation Layer (View): Web Application

Business Layer: Controller - Service 

Persistence Layer: – Repository Interface – Model(entity)

Database Layer: Database

#### Overview

This diagram shows the architecture of a web application using the Spring Boot framework. 
It is divided into three main parts (or layers): the Presentation Layer, the Business Layer, Persistence Layer, and the Database Layer. Each layer has a different role in how the application works. 

### Presentation Layer
The presentation layer is the top layer of the spring boot architecture. 
It consists of Views. i.e., the front-end part of the application. It handles the HTTP requests and performs authentication. 
It is responsible for converting the JSON field’s parameter to Java Objects and vice-versa. 
Once it performs the authentication of the request it passes it to the next layer. i.e., the business layer.

### Business Layer
The business layer contains all the business logic. It consists of service and controller classes. 
It is responsible for validation and authorization. Controllers and Services will send requests 
to each other. For example, the InfluencerController and CategoryController will communicate with 
the InfluencersService and CategoryController. The Controllers also communicate with the 
presentation layer, specifically the client, web application. 

### Persistence Layer
The persistence layer contains all the database storage logic. 
It is responsible for converting business objects to the database row and vice-versa.
It also consists of the Model classes that communicate with other Model classes and the Service classes
that are in the Business Layer.
For example, the Product Model class has a relation with the Product Service, as well as communicating with Review,
FavoriteProduct and Category Model classes. 

### Database Layer
The database layer contains all the databases such as Mysql. This layer can contain multiple databases. 
It is responsible for performing the CRUD operations. Model classes will send data from the MySQL database,
and the service classes within the business layer will send and receive data from the MySQL database within 
the database layer as well. 

#### Full Example
For example, the user goes to the web application and searches a product. The web application sends this request to the controller. 
The Controller receives the request and the user wants to search for a product. 
Then, it calls the Service that contains the logic for searching a product. 
The Service checks the logic for searching a product. To get the list of related products, 
the service calls the Repository Interface, which sends a query to the database layer. 
The Database Layer retrieves the list of the products 
