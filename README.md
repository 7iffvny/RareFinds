# Bookstore System Eclipse
RareFinds Website by:
* Jane Odum
* Tiffany Nguyen
* Jorrin Thacker
* Sammy Vo

## Bookstore System
Sprint 2 projects for online Bookstore system using Spring boot and MySQL Database.
user credentials stored in MySQL database. Tokens (persistent-logins) are stored in the database as well. Passwords are encrypted with the BCrypt algorithm.
A Spring Boot web app with the following features:
Login and registration pages
Password reset workflows
Edit profile page
Thymeleaf templates

## Getting Started
To install this application, run the following commands:

git clone https://github.com/janeodum/BookstoreSystemEclipse.git

## Required Technologies
* Eclipse EE
* Spring Boot Tools
* Mysql
* Mysql Workbench.

## Libraries used:
* Spring boot
* Spring Data JPA and Hibernate
* MYSQL (for database management)
* Spring security

## Database description:
Below are the main tables that will be used in the authentication/authorization process:
- Users table: Contains basic user details (email and password) used for the user to login. Here BCrypt encrypted passwords are stored and also user_type(admin or user) used for the user privileges.
- featured_books table: Contains books details displayed on the home page.
- Payment_information table: Contains user payments details it also stores the user’s email which serves as a foreign key in the payment table.
- Persistent_token table: Contains remember-me access tokens for authenticated users.
- Address_information table: Contains information about user address details.
- Promotion_information: Contains information about users who have subscribed for promotional email. It stores the user’s email address and phone number as a foreign key.

NOTE:
You would need to create a Database called Bookstore in your MYSQL Workbench and add the connection details in the app.properties file. Since the requirement states that the admin does not need to register to have an account open, you would need to manually enter admin information in the database after the user table has been created. To do so simply enter “admin” in the user_type column after the user account has been created and when the user is authenticated, based on their type they would be redirected to the appropriate page. 
 
## Project configurations - application.properties:
Database configuration: You can modify database-related configurations in the application.properties.
For email SMTP I used Gmail for this project. 
Note that once you add your Gmail and run the code you might encounter an SMTP error depending on your Gmail security configuration. To correct this simply turn on Less secure app setting on your Gmail account. If you also have a two-factor authentication you would need to turn off that too. Or as I advise, open a new Gmail account for the purpose of testing this application.

 
## How to run this project:
To run the project, if you cloned my repo on GitHub, open the command-line at the project's root directory, and run this command: mvnw spring-boot:run. The proram runs in Java 11 so adjust the Java version if needed. It will automatically create the database, required tables, and insert preliminary data. Or simply open your eclipse IDLE and click on FILE → OpenFile and open the file folder where you have it saved. You can also use IntelliJ IDEA to import it as a Maven project. Right-click on the Bookstoresystem file and click Run As spring boot application. Once you have had the spring boot application ran, note that the featured books table has not been populated with book details because the admin page has not been implemented so for now we would need to run an SQL script to insert book details in the table. To populate the featured books table open the database folder under the resources folder and copy the script. Run the script in your MySQL workbench.
Now open your browser and go to localhost:8080/ to access the homepage of our website! Enjoy!
 
 
 

