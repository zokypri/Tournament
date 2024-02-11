# Improvements

    1. Unit tests for Player service and controller as done for Tournament
    2. Unit tests for the mappers in the Dtos
    3. Perhaps some unit tests for the validation specifically , we are testing it in the controller
    4. Improve error handling
    5. Perhaps use inheritence for AddTournamentRequest and UpdateTournamentRequest but since there are only two variable this might not be needed?
    6. Make sure tournamentId in AddPlayerRequest should throw exceptoion when null since Jackson will serialize it as a zero
    7. Improve the error response documentation in swagger

# Testing

I have skipped some tests due to lack of time to write them all
The controllers lack a few of the error handling tests due to this lack of time

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

