# MyCamp

The project is built in Spring Boot technology, exposing the Rest API and H2 Database.

Using ActimeMQ to processing on Queue the registration of reservations.

To run the project can run:
```
mvn spring-boot:run
```

The endpoints expose are:

To create a period available should use:
Post Method:
### http://localhost:8080/api/reservation-periods
```
{
    "firstDateAvailable": "2019-03-01",
    "finalDateAvailable": "2019-03-30",
    "statusAvailable": "Y"
}
```
To find a period available should use:
Get Method:
###http://localhost:8080/api/reservation-periods/:numberOfReservation?startDate=YYYY-MM-DD&finalDate=YYYY-MM-DD
 - numberOfReservation:A number of a reservation registred
 - startDate: first date of the range pattern YYYY-MM-DD
 - finalDate: last date of the range pattern YYYY-MM-DD

To create a reservation:
Post Method:
### http://localhost:8080/api/reservations
Body Request example:
```
{
    "arrivalDate":"2019-03-06",
    "departureDate":"2019-03-08",
    "user":{
        "fullName": "igor Soares de Freitas",
        "email": "igorsoares.de.freitas@gmail.com"
    }
}
```
To cancel one reservation should use:
### http://localhost:8080/api/reservations/:numberOfReservation/cancellations
 - numberOfReservation: A number of reservation registred in data base

To get a status of reservations should use:
### http://localhost:8080/api/reservations/:numberOfReservation/status
 - numberOfReservation: number of reservation registred in data base

To change a reservation should use:
###http://localhost:8080/api/reservations/:numberOfReservation/alterations?startDate=YYYY-MM-DD&finalDate=YYYY-MM-DD