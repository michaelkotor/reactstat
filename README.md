# ReactStat

#### The project provides async API to collect metrics in you app

### Endpoints

 - `localhost:8000/event?id=9`

Creates a new Event. It contains id of the post, ip adress and date.

 - `localhost:8000/events`
 
Generates a report with three params: total number of visits by period, 
number of uniq visits for the period and number of favorite visitors.

It takes two params: `start` and `end`. If they are omitted, `end` is now, but `start` is somewhere in June 2020. 

### Redis is chosen as a database

It provides a reactive starter by Spring, so API can handle lots requests.
Two libraries are used: ***starter from Spring*** and ***lettuce-io***
THe first one provides a complex reactive way, but the second simplifies lots work.

Each event stores in format of `id!host!date`. I found it simple to parse
into Event class to string to parse.  

### RUN

```mvn clean install && docker-compose up --build``` 
