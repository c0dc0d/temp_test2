# MyCamp

The project is built in Spring Boot technology, exposing the Rest API using to persistence data with H2 Database.

Using ActiveMQ to processing on Queue the registration of reservations.

To run the project can run:
```
mvn spring-boot:run
```

##The endpoints exposes are:

####To find a period available should use:
Get Method: 
http://localhost:8080/api/reservation-periods?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD
 - startDate: first date of the range pattern YYYY-MM-DD
 - endDate: last date of the range pattern YYYY-MM-DD

#### To create a reservation:

Post Method:
http://localhost:8080/api/reservations
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
####To cancel one reservation should use:

Put method:
http://localhost:8080/api/reservations/:numberOfReservation/cancellations
 - numberOfReservation: A number of reservation registered in data base

#### To get a status of reservations should use:

When a reservation is created and it goes to the queue to processing, this endpoint was created to know the status of the reservations on the flow.

Get method:
http://localhost:8080/api/reservations/:numberOfReservation/status

 - numberOfReservation: number of reservation registered in data base

#### To change a reservation should use:

Put method:
http://localhost:8080/api/reservations/:numberOfReservation/alterations?arrivalDate=YYYY-MM-DD&departureDate=YYYY-MM-DD
 - numberOfReservation: number of reservation registered in data base
 - arrivalDate: Date of arrival
 - departureDate: Date of departure

## Acknowledgments

In the folder /jmeter there is a script to test the queue processing and concurrency.