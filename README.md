<h1 align="center">Places API</h1>

<p align="center">
  <a href="https://github.com/magrininicolas/placesAPIMVC/blob/main/LICENSE">
    <img src="https://img.shields.io/npm/l/react" alt="NPM License" />
  </a>
  <a href="https://www.linkedin.com/in/nicolasgmpereira">
    <img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" alt="LinkedIn" />
  </a>
</p>

API for managing places (CRUD) that is part of [this challenge](https://github.com/RocketBus/quero-ser-clickbus/tree/master/testes/backend-developer) for backend developers applying to ClickBus.

I make this project to practice what I've been studying in backend development with Java/Spring Boot.

## Technologies Used

- [Java](https://docs.oracle.com/en/java/)
- [Spring Boot](https://spring.io/projects/spring-boot/)
- [Spring MVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Doc Open API 3](https://springdoc.org)
- [JUnit 5](https://junit.org/junit5/docs/current/user-guide/)
- [PostgreSQL](https://www.postgresql.org)
- [Slugify](https://github.com/slugify/slugify)
- [Lombok](https://projectlombok.org)

## Adopted practices

- SOLID
- DRY
- Automated Testing
- DTOs in the API
- Swagger automatically generated with Open API 3
- Slugs automatically generated with Slugify
- Dependency Injection
- Audit trail for entity creation and updates

# How to execute

## Locally

- Clone git repository
- Build project

```
./mvnw clean package
```

- Run

```
java -jar apilugaresmvc/target/apilugaresmvc-0.0.1-SNAPSHOT.jar
```

The API can be accessed at [localhost:8080](http://localhost:8080).

The Swagger can be visualized at [localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Using docker

- Clone git repository
- Build project:

```
./mvnw clean package
```

- Build image

```
./mvnw spring-boot:build-image
```

- Run container

```
docker run --name api-lugares-mvc -p 8080:8080 -d apilugaresmvc:0.0.1-SNAPSHOT
```

The API can be accessed at [localhost:8080](http://localhost:8080).

The Swagger can be visualized at [localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## API endpoints

The HTTP requests below were made using [Postman](https://www.postman.com/downloads/)

- POST /places
![POST Mapping](https://github.com/magrininicolas/placesAPIMVC/blob/main/src/main/resources/imgs/post.png)
```
URL: http://localhost:8080/places
Method: POST

Body(raw):
{
    "name": "Name",
    "state": "State",
    "city": "City"
}

Response:
200 OK
Response size: 316B

Response Body:
{
    "name": "Name",
    "slug": "name",
    "city": "City",
    "state": "State",
    "createdAt": "2023-07-26T22:35:41.720042631",
    "updatedAt": "2023-07-26T22:35:41.720042631"
}
```

- GET /places/{id}
![GET by ID Mapping](https://github.com/magrininicolas/placesAPIMVC/blob/main/src/main/resources/imgs/getid.png)
```
URL: http://localhost:8080/places/e79b4afe-efd0-4d9a-ace1-4a02744b2810
Method: GET

Response:
302 Found
Response size: 308B

Response Body:
{
    "name": "Name",
    "slug": "name",
    "city": "City",
    "state": "State",
    "createdAt": "2023-07-26T22:35:41.720043",
    "updatedAt": "2023-07-26T22:35:41.720043"
}
```

- GET /places/{page}/{qtyUsers}
![GET by Page Mapping](https://github.com/magrininicolas/placesAPIMVC/blob/main/src/main/resources/imgs/getpage.png)
```
URL: http://localhost:8080/places/1/3
Method: GET

Response:
302 Found
Response size: 684B

Response Body:
[
    {
        "name": "Av Pedro Bottesi",
        "slug": "av-pedro-bottesi",
        "city": "Mogi Mirim",
        "state": "São Paulo",
        "createdAt": "2023-07-16T16:37:14.847064",
        "updatedAt": "2023-07-16T16:37:14.847064"
    },
    {
        "name": "Av Padre Jaime",
        "slug": "av-padre-jaime",
        "city": "Mogi Mirim",
        "state": "São Paulo",
        "createdAt": "2023-07-25T15:05:49.999586",
        "updatedAt": "2023-07-25T15:05:49.999586"
    },
    {
        "name": "Av Norte Sul",
        "slug": "av-norte-sul",
        "city": "Campinas",
        "state": "São Paulo",
        "createdAt": "2023-07-25T15:15:42.465428",
        "updatedAt": "2023-07-26T20:00:50.71238"
    }
]
```

- GET /places
![GET all Mapping](https://github.com/magrininicolas/placesAPIMVC/blob/main/src/main/resources/imgs/getall.png)
```
URL: http://localhost:8080/places
Method: GET

This method simply return all places included in database.
```

- GET /places/name?name=?
![GET by Name Mapping](https://github.com/magrininicolas/placesAPIMVC/blob/main/src/main/resources/imgs/getname.png)
```
URL: http://localhost:8080/places/name?name=name
Method: GET

Response:
302 Found
Response size: 310B

Response Body:
[
    {
        "name": "Name",
        "slug": "name",
        "city": "City",
        "state": "State",
        "createdAt": "2023-07-26T22:35:41.720043",
        "updatedAt": "2023-07-26T22:35:41.720043"
    }
]
```

- PATCH /places/{id}
![PATCH Mapping](https://github.com/magrininicolas/placesAPIMVC/blob/main/src/main/resources/imgs/patch.png)
```
URL: http://localhost:8080/places/e79b4afe-efd0-4d9a-ace1-4a02744b2810
Method: PATCH

Body:
{
    "name": "New Name",
    "state": "New State",
    "city": "New City"
}

Response:
200 OK
Response size: 324B

Response Body:
{
    "name": "New Name",
    "slug": "new-name",
    "city": "New City",
    "state": "New State",
    "createdAt": "2023-07-26T22:35:41.720043",
    "updatedAt": "2023-07-26T22:48:52.487206527"
}
```

- DELETE /places/{id}
![DELET Mapping](https://github.com/magrininicolas/placesAPIMVC/blob/main/src/main/resources/imgs/delete.png)
```
URL: http://localhost:8080/places/e79b4afe-efd0-4d9a-ace1-4a02744b2810
Method: DELETE

Response:
200 OK
Response size: 190B

Response Body: Place deleted successfully
```
