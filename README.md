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
the service, with the help of the Repository Interface, sends a query to the database layer. 
The Database Layer retrieves the list of the products and sends it back to the Service .

## Description of Classes and Packages 

### Controllers package:
The controller package includes all the controller classes including the
AdminController that works with logic for the admin, AuthController which 
works with the logic for authenticating/verifying users, CategoryController which
handles logic for approving, creating and updating categories, CommentController
which handles logic for followers to make comments, FavoriteProductController
which handles logic for a user to be able to favorite a product, FollowerController
which handles logic for creating a follower account, InfluencerController which handles
the logic for the creation of an influencer account, ProductController which handles the 
logic for creating, updating and deleting a product, the ReviewController which handles
the logic for creating reviews, the UserController which handles creating a general account
and updating profile logic and finally, the VendorController which has logic for creating 
and approving a Vendor account. 

### DTOs
The DTO package includes a classes for managing data transfer efficiently for each class. 
For example, there is a Category, CategoryRequest, CategoryResponse DTO for category,
Comment and CommentRequest DTOs for comment data, FollowerDTO for follower data, InfluencerDTO
for influencer data, Product, ProductRequest, and ProductResponse DTOs for product data. 
ProductReview and Review and ReviewRequest DTOs for product review data, User and UserResponse, and 
VerifyUser DTOs for handling general user data, Vendor, VendorRequest, and VendorResponse for
vendor data. 

### Enums
The enum package include an AppRole enum for Admin, Follower, Influencer and Vendor. 

### Exceptions
Include common exceptions that need to be thrown such as validation error response. 

### Mailing
The mailing package includes logic for email verification of users.

### Models 
The model classes represent the data of our application, 
mirroring the structure of our database tables 
or the data we're working with.
They also hold the attributes and properties of our entities, 
making it easy to access and manipulate data.
Briefly listing the model classes, there is BaseEntity,
Category, Comment, FavoriteProduct, Follower, Influencer,
PassWordRestToken, Product, Review, ReviewLike, Role,
User and Vendor. All these classes also interact with 
the database and service classes.

### Repositories 
The repositories contain logic that allows for 
mechanisms for storage, retrieval, update, delete, and searching 
and there is one for almost each model class, stated above. 

### Security 
The security package includes logic 
for security including configuration 
for web and general security, making
sure our data is safe. 

### Service 
The service class includes logic for the core
business logic of our application. 
They encapsulate the operations and processes 
that manipulate data and implement the application's functionality.
The interfaces such as CategoryService define what is to be expected
in the CategoryServiceImpl which holds the main logic for approving, 
creating and updating a category.  
