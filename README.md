# heroesapi
API to manage heroes with their number of movies from different universes.

### Postman Documenter of the API 
https://documenter.getpostman.com/view/14879772/TzCP6nGw

### Documentacao swagger gerada pela aplicação
http://localhost:8080/swagger-ui-heroes-reactive-api.html

### Stack used
- Java8
- maven
- spring webflux
- Spring data
- lombok
- dynamodb
- junit
- assertj
- mockito
- mapstruct
- sl4j
- reactor

### Run dynamo
On the folder that dynamo's jar is downloaded: java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb
To list all tables created: aws dynamodb list-tables --endpoint-url http://localhost:8000

