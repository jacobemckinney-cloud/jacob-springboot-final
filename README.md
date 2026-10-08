# Java Spring Boot Final

Welcome to the final project for the Java Spring Boot unit.  In this project,
you will be working with a Spring Boot application that includes a database
for a web store and an already implemented authentication and authorization
system.  You will be responsible for implementing the REST endpoints that
allow users to interact with the store, and securing those endpoints using
Spring Boot's security annotations.

## Getting Started

To get started, open the project file located in
`java-spring-boot-final/pom.xml` in IntelliJ.

## Project Overview 

This project contains a Spring Boot application with the necessary dependencies
to create a REST API backed by a mysql database with authentication and
authorization.  The application is already configured to use the database
and the authentication and authorization system is already implemented.
However, the REST endpoints that allow users to interact with the rest of the
database are not yet implemented.

The application source contains the following directories:

- `src/main/java/org.example/models` - Contains the model classes that represent
  the data in the database.  *You will not need to create any new model classes.*
- `src/main/java/org.example/daos` - Contains the data access objects that
  interact with the database.  Currently, there is only one DAO class, `UserDao`,
  which interacts with the `users` table.
- `src/main/java/org.example/services` - Contains the service classes that
  provide services that are not directly related to the database.  The only
  service class currently is `CustomUserDetailsService`, which provides
  the authentication system and `auth/login` endpoint.  *You will not need to
  create any new service classes for this project.*
- `src/main/java/org.example/controllers` - Contains the controller classes
  that provide the REST endpoints for the application.  Currently, there are
  two controller classes, `UserController` and `ProfileController`.  The
  `UserController` class provides methods for ADMIN users to create, read,
  update, and delete users.  The `ProfileController` class provides methods
  for users to read and update their own profiles.
- `src/main/java/org.example/exceptions` - Contains the exception classes that
  are thrown by the application.  Currently, there is only one exception class,
  `DAOException`, which is thrown when an error occurs in the DAO classes.
  *You will not need to create any new exception classes for this project.*

## Initial Setup and Verification

### Creating the Database

To create the database, you will need to run the `create-database.sql` script
located in the `sql` directory.  You can run this script by opening MySql
Workbench and opening the script file.  Then, you can execute the script by
clicking on the lightning bolt icon in the toolbar.

### Setting Database Connection Properties

To configure the application to connect to the database, you will need to
modify the `application.properties` file located in the `src/main/resources`
directory.  You will need to set the following properties:

- `spring.datasource.username` - The username for your mysql server.
- `spring.datasource.password` - The password for your mysql server.

### Running the Application

To run the application, you can right-click on the `SpringBootApplication`
class in IntelliJ and select "Run WebStoreApplication".  This will start the
application and you should see output in the console indicating that the
application has started.

If you see an error message in the console, you may need to check the
`application.properties` file to make sure that the database connection
properties are set correctly, and make sure that you have created the database
using the `create-database.sql` script.

### Loading the Postman Collection

To help you test the REST endpoints, a Postman collection has been provided
that contains a set of requests that you can use to interact with the REST
endpoints.  You can find the Postman collection in the `postman` directory.

To load the Postman collection, open Postman and click on the "Import" button
in the top left corner.  Then, click on the "Choose Files" button and select
the `java-spring-boot-final/postman/web-store.postman_collection.json`

Once you have imported the collection, you should see a new collection called
"Web Store" in the left sidebar.  You can expand the collection to see the
requests that are available.

Make sure that the application is running before you try to execute the
requests in Postman.

You should first execute the `login` request to authenticate as an admin user.
Then, you can execute the other requests to interact with the REST endpoints.

The requests in the `Profile` folder and the `User` folder are already
implemented and should work correctly as long as you run the `login` request
first to authenticate as an admin user.  The requests in the `Products`, 
`Orders`, and `OrderItems` folders are not yet implemented and will not work
until you implement the REST endpoints.

> [!Note]
> You should not need to modify the postman collection, but you may change
> the request bodies or parameters if you want to test different scenarios.

## Exercise

Your task is to implement the REST endpoints that allow users to interact with
the store.  You will need to create a DAO class and a controller
class for each of the tables in the database.

Stop the application and follow the steps below to complete the exercise.

### Step 1: Review the Model Classes

In the `models` folder, notice that there is a class for each of the tables in
the database.  You will need to use these classes to interact with the database
in the DAO classes and controller classes.

### Step 2: Create the DAO Classes

In the `daos` folder, create a new DAO class for each of the tables in the
database (other than the `users` and `roles` tables).  Each DAO class should
contain methods for creating, reading, updating, and deleting items in the
table:

- `getAll` - Retrieves all items from the table.
- `getById` - Retrieves an item by its id.
- `create` - Creates a new item in the table.
- `update` - Updates an existing item in the table.
- `delete` - Deletes an item from the table.

You can refer to the existing `UserDao` class for an example of what these
classes should look like.  However, note that the `UserDao` class contains an
injected `PasswordEncoder` bean that is used to encode passwords.  You will not
need to use this bean in your DAO classes.

> [!Note]
> Each DAO class should be annotated with `@Component` annotation so
> that it can be injected into the controller classes.

As in the `UserDao` class, each DAO class should contain a `JdbcTemplate`
object that is created from a `DataSource` object that is injected through the
constructor.

For example:

```java
@Component
public class ProductDao {
    private JdbcTemplate jdbcTemplate;

    public ProductDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // ...
}
```

Each DAO class will also need a mapping method that maps a `ResultSet` object
to a model object so that your `SELECT` queries can map the results to the 
appropriate model objects. You can refer to the `UserDao` class for an example
of what this method should look like.

### Step 3: Create the Controller Classes

Create a new controller class for each of the tables in the database (other
than the `users` and `roles` tables).  Each controller class should contain
methods providing REST endpoints for creating, reading, updating, and deleting
items in the table.  These endpoints are detailed below.

You can refer to the existing `UserController` and `ProfileController` classes
for an example of what these classes should look like.

Note that each controller class injects its corresponding DAO class through
an `@Autowired` class member.  You will need to do the same for your controller
classes.

For example:

```java
@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductDao productDao;

    // ...
}
```
> [!Note]
> Your "Get by Id" methods should return a 404 status code if the item is not
> found in the database.  You can do this by throwing a `ResponseStatusException`
> with a `HttpStatus.NOT_FOUND` status code. (Refer to the `UserController` class
> for an example of how to do this.)

When you are finished, you should have implemented all of the REST endpoints
in the Postman collection, and they should all function properly and return
appropriate data.

### Product Endpoints

- `GET /products` - Retrieves all products.
- `GET /products/{id}` - Retrieves a product by the id in the path, return a 404 NOT FOUND status code if the product is not found.
- `POST /products` - Creates a new product from the request body and returns the created product with a 201 CREATED http status code.
- `PUT /products/{id}` - Updates an existing product from the request body and returns the updated product, return a 404 NOT FOUND status code if the product is not found.
- `DELETE /products/{id}` - Deletes a product by the id in the path and returns the number of rows affected, return a 404 NOT FOUND status code if the product is not found.

### Order Endpoints

- `GET /orders` - Retrieves all orders.
- `GET /orders/{id}` - Retrieves an order by the id in the path, return a 404 NOT FOUND status code if the order is not found.
- `POST /orders` - Creates a new order from the request body and returns the created order with a 201 CREATED http status code.
- `PUT /orders/{id}` - Updates an existing order from the request body and returns the updated order, return a 404 NOT FOUND status code if the order is not found.
- `DELETE /orders/{id}` - Deletes an order by the id in the path and returns the number of rows affected, return a 404 NOT FOUND status code if the order is not found.

### Order Item Endpoints

- `GET /order-items` - Retrieves all order items.
- `GET /order-items/{id}` - Retrieves an order item by the id in the path, return a 404 NOT FOUND status code if the order item is not found.
- `POST /order-items` - Creates a new order item from the request body and returns the created order item with a CREATED http 201 status code.
- `PUT /order-items/{id}` - Updates an existing order item from the request body and returns the updated order item, return a 404 NOT FOUND status code if the order item is not found.
- `DELETE /order-items/{id}` - Deletes an order item by the id in the path and returns the number of rows affected, return a 404 NOT FOUND status code if the order item is not found.

## Testing with Postman 

You can verify that the application is working correctly by running the
application and then launching Postman to test the REST endpoints.  You should
be able to create, read, update, and delete items in the database using the
REST endpoints that you created.

## Testing with Included Unit Tests

You can also verify that the application is working correctly by running the
included unit tests.  The unit tests are located in the `src/test/java` directory.
To run the unit tests, right-click on the `src/test/java` directory and select
"Run All Tests".

> [!warning]
> DO NOT modify the unit tests.  Your project will be evaluated based on the
> unit tests all passing.  Modifying the unit tests will result in a failing
> grade.

## Evaluation

Your project will be evaluated based on the unit tests all passing.

## Bonus Steps

### Bonus Step 1: Secure all REST endpoints by requiring authentication

Add the `@PreAuthorize` annotation to all of the REST endpoints in the
controller classes to require that users be authenticated in order to access
the endpoints.  You can use the `isAuthenticated()` expression to require that
users be authenticated.

### Bonus Step 2: Constrain Order Creation and Update using `Principal`

Add a `Principal` argument to the create and update endpoints for the
`Order` controller that will allow you to get the username of the user and
overwrite the `username` field in the passed `Order` object.  This will
guaranatee that the `username` field is always set to the username of the
user that created or updated the order.

For example:

```java
@PostMapping
public void createOrder(@RequestBody Order order, Principal principal) {
    String username = principal.getName();
    order.setUsername(username);

    // ...
}
```

### Bonus Step 3: Add optional query parameters to the `Orders` GET endpoint

Add optional `user` query parameters to the `Orders` GET endpoint that will
allow users to only retrieve orders that belong to a specific user.  If the
`user` query parameter is not provided, the endpoint should return all orders.

```java
@GetMapping
public List<Order> listOrders(@RequestParam(required = false) String username) {
    if (username != null) {
        return orderDao.getOrdersByUsername(username);
    } else {
        return orderDao.getOrders();
    }
}
```

> [!Note]
> You will need to create a new method in the `OrderDao` class that retrieves
> orders by username.

### Bonus Step 4: Add optional query parameters to the `OrderItems` GET endpoint

Add optional `orderId` query parameters to the `OrderItems` GET endpoint that
will allow users to only retrieve order items that belong to a specific order.
If the `orderId` query parameter is not provided, the endpoint should return
all order items.

```java
@GetMapping
public List<OrderItem> listOrderItems(@RequestParam(required = false) Long orderId) {
    if (orderId != null) {
        return orderItemDao.getOrderItemsByOrderId(orderId);
    } else {
        return orderItemDao.getOrderItems();
    }
}
```

> [!Note]
> You will need to create a new method in the `OrderItemDao` class that retrieves
> order items by order id.

