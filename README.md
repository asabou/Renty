# Renty
Rest API for RentyApp

To test the rest-endpoints using the h2 in memory database, you have to do the following steps:
1. Take a look on BootStrapData class from dao and choose a user from there
2. Encript user:password using base 64.
3. Start the Spring Boot application (make sure that the active spring profile is dev or test in application.yml)
4. Use Postman or any other client tester
5. Create a POST method and add Authorization header with the value having the username and password encripted.
6. Send the request at /login rest endpoint.
7. If the authentication is successfull, you'll receive a response with Authorization header and the token, if not, a custom unsuccessfull authentication message will be in the body of the response.
8. Use the token, for testing the rest of endpoints. Put the token on the Authorization header for every rest request.
