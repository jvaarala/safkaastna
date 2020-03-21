# safkaastna

2020 Kevät - OTP
Ryhmä 9
Oliver Martikainen, Katri Rasio, Christoffer Tverin, Jesse Väärälä

Safkaastna is a school project to map restaurants who offer Kela subsidized meals for students in Finland.
Project is built with Maven so all dependencies can be found on pom.xml.
Map view in program is provided with GmapsFX ( https://github.com/rterp/GMapsFX ), database connections are handled with Hibernate and the chosen framework for UI is JavaFX.

HOW TO SETUP?

If you are a student at Metropolia University of Applied Sciences:
Follow this instruction: http://users.metropolia.fi/~hakka/OTP1/Et%C3%A4yhteysOhje.pdf
The credentials provided in hibernate.cfg.xml are read only.
Database connections are available until Tuesday, May 19, 2020. After this follow instructions below.

If you are NOT a student at Metropolia University of Applied Sciences:

Original database can accessed only from educational private network of Metropolia University of Applied Sciences. 


1. Clone project


2. Set up SQL database and modify src/main/resources/hibernate.cfg.xml file to address your own SQL-database:

```     
<property name="hibernate.connection.url">jdbc:mysql://YOUR_DATABASE_ADDRESS</property>
<property name="hibernate.connection.username">YOUR USERNAME</property>
<property name="hibernate.connection.password">YOUR PASSWORD</property>
```


3. Raw data can be found in restaurantRawData210320.json - file can be added to your database with code found below:
  
```
java private static void readJSON() throws Exception {
        RestaurantDAO dao = new RestaurantDAO();
        File file = new File("restaurantRawData210320.json");
        String content = FileUtils.readFileToString(file, "utf-8");

        // Convert JSON string to JSONObject
        JSONObject restJSON = new JSONObject(content);

        JSONArray restArray = restJSON.getJSONArray("restaurants");
        for (int i = 0; i < restArray.length(); i++) {
            Restaurant restaurant = new Restaurant();
            JSONObject obj = restArray.getJSONObject(i);
            restaurant.setName(obj.getString("name"));
            restaurant.setAddress(obj.getString("address"));
            restaurant.setPostal_code(obj.getInt("postal_code"));
            restaurant.setCity(obj.getString("city"));
            restaurant.setWww(obj.getString("www"));
            restaurant.setAdmin(obj.getString("admin"));
            restaurant.setAdmin_www(obj.getString("admin_www"));
            try {
                restaurant.setLat(obj.getDouble("lat"));
                restaurant.setLng(obj.getDouble("lng"));
            } catch (Exception e) {
                System.out.println(e);
            }

            dao.createRestaurant(restaurant);

            System.out.println(restaurant);
        }

    }
```


4. You need Google Maps API key from https://developers.google.com/maps/documentation/javascript/get-api-key and .env file on project root with following content:

```
APIKEY='YOUR GOOGLE MAPS API KEY'
```

You need to give environment variable as a parameter, when you run jar-file
for example:

```
APIKEY='your api key' java -jar 'yourJarFile.jar'
```


5. ENJOY!



