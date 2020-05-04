# SafkaaSTNA

2020 Kevät - OTP
Ryhmä 9
Oliver Martikainen, Katri Rasio, Christoffer Tverin, Jesse Väärälä

SafkaaSTNA is a school project to map restaurants who offer Kela subsidized meals for students in Finland.
Project is built with Maven so all dependencies can be found on pom.xml.
Map view in program is provided with GmapsFX ( https://github.com/rterp/GMapsFX ), database connections are handled with Hibernate and the chosen framework for UI is JavaFX.

## HOW TO SETUP?

1: Clone project

2: Get Google Maps API key from https://developers.google.com/maps/documentation/javascript/get-api-key

3: Create file named _.env_ at root of the project

4: Add following content to _.env_


```
APIKEY='YOUR GOOGLE MAPS API KEY'
MONGO_USER="SafkaaSTNA_public"
MONGO_USER_PASS="1wbozMh4vZDDGsR0"
```

5: ENJOY!
