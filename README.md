# Spring-Security
Contains all the file for learning the Spring Security concepts.

# springsecsection_2
Implemented the concept that which of the APIs are public and which of the APIs are need to be secured for only authorized access as Spring Security used to secure each and every APIs secure by default. (request.ResquestMatcher).

# springsecsection_3
Contains the concept of logging of multiple users at the same time by storing the user credentials in memory of the application using InMemoryUserDetailsManager.

# springsecsection_4
docker run --name springsecurity -e MYSQL_ROOT_PASSWORD=##### -e MYSQL_DATABASE=eazybank -p 3306:3306 -d mysql:latest
6f4c41d74cb3a7fb4e9bc4dee094b19658087e11204e557f372fbed1175ae5ad

# springsecsection_4_b
1. Inserted the data(user credentials) in the customized/preferred database.
2. Created a RestAPI which supports new users using code instead of inserting the data mannually to the database.
3. Building the required classes for the JPA framework to interact with the new table created so far.
4. Created Customer.class (customer implementation), CustomerRepository Interface (To invoke CRUD related methods and to fire the Query on Customer).
5. Creating our own Customers implementation of UserDetailsService.
