# Description
This microservice was created for the PAF test exercise to simulate a Tournament service
The service is written in Kotlin with the latest stable Java version and the latest stable SpringBoot version
Java 21 and SpringBoot 3.2.2

# DB
There are two DB tables Tournament and Player. One tournament can hold several players.
Tournament DB holds a list of Players so there is a foreign key from Player to the Tournament table

The interaction between Kotlin and DB is done with SpringBoot JPA library which uses Hibernate implementation by default

Since Kotlin data classes and Spring JPA is not working well together perhaps a better solution would have been
to use the Spring data JDBC starter instead alternatively use Kotlin class instead of data class. 

By not using data class some of the functions like copy, equals, hashCode, toString will be lost 

# API
There are endpoints to ADD, UPDATE, GET and REMOVE a tournament as per the scope
There are two GET endpoints, one to get all the tournaments and one to get a specific tournament and all the players

As for the Player API there are endpoints to ADD, UPDATE and GET a player
When fetching players per tournament only a list of players for that tournament is returned and no data about the tournament
I was thinking about having an endpoint to delete a player but perhaps we would need some history of all players
that have been registered for a tournament I decided to add an enum and inactivate a player rather than deleting 
Perhaps PAF needs to report to some authority which players have been registered so deletion is not a good option

# Error handling
The handling of errors is done in a global error handler where the different exceptions is caught, logged and converted 
to proper API responses

Since this is probably a back office service leaking information about which player or tournament does not exist is not an issue
This is why some of the API endpoints throw a bad request error in order for the calling services or app to update their systems
and keep track of which players or tournaments don't exist

Had this been an exposed API leakage of which player or tournament could have been vulnerable to Phishing attacks 

# Testing
Unit tests are using Kotlin Mockk and due to lack of time there is not 100% test coverage

For testing the controller WebMVC is used to isolate the controller and not spin up bit SpringBootTest
When testing controller we are only interested in this specific controller and mocking service classes
Only one of two controllers are tested due to lack of time

Component/IT tests are testing end to end within the microservice. 
In this test we are starting a complete SpringBoot application to be able to test the complete service all the way to the DB tables

One test class handles the happy cases where the complete chain of calls is made to the APIs. 
Since there were issues with Kotlin data class and JPA I had to create test functions where each field is asserted instead of comparing two objects
assertTournament, assertFirstPlayer, assertSecondPlayer would have been able to test with one assert if these issues were resolved

Another class was created for the unhappy cases which are the error cases
The reason for the division is the DB tables create sequences for primary keys and when an endpoint has an error 
the transactional annotation will revert the commit but the sequence will not be reverted so the primary key ID will not be reset
This makes some of the test fail in the happy case and the Json files holding the request and response are not dynamical so the division seemed best at the moment
There is probably some way to fix the sequence but since I lack time I had to prioritize
I tried to reset the sequence after each test with sql script and a @BeforeEach setup that did not work

# Improvements

    1. Unit tests for Player service and controller as done for Tournament
    2. Unit tests for the mappers in the Dtos
    3. Perhaps some unit tests for the validation specifically , we are testing it in the controller
    4. Improve error handling
    5. Perhaps use inheritence for AddTournamentRequest and UpdateTournamentRequest but since there are only two variable this might not be needed?
    6. Make sure tournamentId in AddPlayerRequest should throw exceptoion when null since Jackson will serialize it as a zero
    7. Improve the error response documentation in swagger
    8. A fix for the Kotlin data class and JPA issue
    9. A fix for the frontend part to work as in the original exercise project

# Issues
There are some issues when using kotlin data classes and Spring Data JPA in comparing two objects
Circular references and stackoverflow errors can occur. This is why I added a custom toString method in the Player entity
I probably need to add it for the Tournament Entity as well. The more I tried to fix the issue the more
issues I encountered and the more I read about it the more I realized I was not alone dealing with these issues

Again due to lack of time I will leave this as is but in the future I will seek alternatives to using data class
and JPA

# Frontend
Since I created a new microservice I have not had the time to fix the frontend part so the way to test is to use swagger link

http://localhost:8080/exercise/swagger-ui/index.html#/

Or use Postman, Curl or any other tool. The frontend app at http://localhost:8080/ will unfortunately not work

