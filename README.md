<p align="center">
    <img 
        alt="Online Book Store" 
        title="Online Book Store" 
        src="https://i.imgur.com/u9TwkfW.png">
</p>
<h1 align="center"> Online Book Store </h1> <br>
<p align="center">
    Free API prototype
</p>

## Table of Contents

- [Introduction](#introduction)
- [Technologies](#technologies)
- [Functionalities](#functionalities)
- [Installation](#Installation)

Welcome to the Online Book Store API project! This API is designed 
to serve as the backend for an online bookstore, providing essential 
functionalities for managing books, categories, and user interactions.

## Introduction

The inspiration behind this project is to create a basic API prototype 
for an online bookstore, catering to the needs of both customers 
and administrators. The goal is to offer a seamless and efficient 
experience for data filling, browsing, searching, and purchasing books.

## Technologies

The project was made through <b>Java</b> using the following technologies:
- <b>Spring Boot</b>: The project is built using the Spring Boot framework 
for easy development and deployment of Java-based applications.
- <b>Spring Security</b>: Ensures secure authentication and authorization 
for various API endpoints.
- <b>Spring Data</b>: Simplifies database operations, allowing seamless 
integration with relational databases.
- <b>Liquibase</b>: Database schema change management solution that enables 
you to revise and release database changes faster and safer from development 
to production.
- <b>Swagger</b>: API documentation is powered by Swagger, making it easy 
for developers to understand and explore the available endpoints.
- <b>MapStruct</b>: Code generator which simplifies the implementation 
of mappings between Java bean types by generating mapping code at compile time, 
following a convention-over-configuration approach.
- <b>Docker</b>: Software platform that allows you to build, test, 
and deploy applications quickly using containers.

## Functionalities

<h3>Book Controller</h3>

The BookController provides the following functionalities:
- <b>Get All Books</b>: Retrieve a list of all books available in the bookstore.
    <pre>GET /api/books</pre>
- <b>Get Book by ID</b>: Retrieve details of a specific book using its unique identifier.
    <pre>GET /api/books/{<var>id</var>}</pre>
- <b>Add New Book</b>: Add a new book to the bookstore catalog. Only for admins.
    <pre>POST /api/books</pre>
- <b>Update Book</b>: Modify the details of an existing book. Only for admins.
    <pre>PUT /api/books/{<var>id</var>}</pre>
- <b>Delete Book</b>: Remove a book from the catalog. Only for admins.
    <pre>DELETE /api/books/{<var>id</var>}</pre>
- <b>Find books</b>: Find books by specified criteria.
    <pre>GET /api/books/search</pre>

<h3>Category Controller</h3>

The CategoryController provides the following functionalities:
- <b>Get all categories</b>: Retrieve a list of all book categories.
    <pre>GET /api/categories</pre>
- <b>Get Category by ID</b>: Retrieve details of a specific category.
    <pre>GET /api/categories/{<var>id</var>}</pre>
- <b>Get Books by Category</b>: Get all books by a specific category.
    <pre>GET /api/categories/{<var>id</var>}/books</pre>
- <b>Add New Category</b>: Add a new category for books. Only for admins.
    <pre>POST /api/categories</pre>
- <b>Update Category</b>: Modify the details of an existing category. Only for admins.
    <pre>PUT /api/categories/{<var>id</var>}</pre>
- <b>Delete Category</b>: Remove a category. Only for admins.
    <pre>DELETE /api/categories/{<var>id</var>}</pre>

<h3>Authentication Controller</h3>

The AuthenticationController provides the following functionalities:
- <b>Authenticate User</b>: Authenticate a user and generate an authentication token.
    <pre>POST /api/auth/login</pre>
- <b>Register New User</b>: Register a new user in the system.
    <pre>POST /api/auth/registration</pre>

<h3>Shopping Cart Controller</h3>

The ShoppingCartController provides the following functionalities:
- <b>Get User's Shopping Cart</b>: Retrieve the shopping cart for the authenticated user.
  <pre>GET /api/cart</pre>
- <b>Add Book to Shopping Cart</b>: Add a book to the user's shopping cart.
  <pre>POST /api/cart</pre>
- <b>Update Shopping Cart Item</b>: Update an item in the user's shopping cart.
  <pre>PUT /api/cart/items/{<var>id</var>}</pre>
- <b>Remove Item from Shopping Cart</b>: Remove an item from the user's shopping cart.
  <pre>DELETE /api/cart/items/{<var>id</var>}</pre>

<h3>Order Controller</h3>

The OrderController provides the following functionalities:
- <b>Place Order</b>: Place a new order based on the contents of the user's shopping cart.
  <pre>POST /api/orders</pre>
- <b>Get User's Orders</b>: Retrieve a list of orders placed by the authenticated user.
  <pre>GET /api/orders</pre>
- <b>Get Order by ID</b>: Retrieve details of a specific order by its unique identifier.
  <pre>GET /api/orders/{<var>id</var>}</pre>
- <b>Get Order Items</b>: Retrieve a specific order items.
  <pre>GET /api/orders/{<var>id</var>}/items</pre>
- <b>Get Order Item</b>: Retrieve a specific order item by order and item unique identifiers.
  <pre>GET /api/orders/{<var>orderId</var>}/items/{<var>itemId</var>}</pre>
- <b>Update Order</b>: Update status of a specific order by its unique identifier. Only for admins.
  <pre>PATCH /api/orders/{<var>id</var>}</pre>

## Installation

To set up and run the Online Book Store API on your local machine:

- Clone this repository:

        git clone https://github.com/VoitseshukV/bookstore.git <local_directory>
- Navigate to the project directory:
        
        cd <local_directory>
- Add custom <code>.env</code> file with the necessary parameters:

        MYSQL_DATABASE=<db_name>
        MYSQL_USER=<db_user_name>
        MYSQL_PASSWORD=<db_user_password>
        MYSQL_ROOT_PASSWORD=<db_root_password>
        MYSQL_LOCAL_PORT=<external_dbms_port>
        MYSQL_DOCKER_PORT=<internal_dbms_port>
        SPRING_LOCAL_PORT=<external_api_port>
        SPRING_DOCKER_PORT=<internal_api_port>
        DEBUG_PORT=<debug_port>
        JWT_SECRET=<secret_key_string_min_32_symbols>
- Builds, creates, starts, and attaches to containers for a service:

        docker-compose up
