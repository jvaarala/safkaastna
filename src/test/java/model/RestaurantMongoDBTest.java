package model;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RestaurantMongoDBTest {
    Restaurant rMock;
    Restaurant r1;
    Restaurant r2;
    List<Restaurant> restaurantList;
    RestaurantMongoDB dao;


    @BeforeAll
    void init() {
        rMock = mock(Restaurant.class);
        r1  = new Restaurant(1, "Name1", "Address1", "00100", "City1", "www", "admin", "adminwww", 62, 24 );
        r2  = new Restaurant(2, "Name2", "Address2", "00100", "City2", "www2", "admin2", "adminwww2", 64, 20 );
        restaurantList = (Arrays.asList(r1, r1));
    }


    @Test
    void uploadRestaurantsAdmin() {
    	RestaurantMongoDB db = new RestaurantMongoDB("admin_test");
    	    	
        try {
            boolean result = db.uploadRestaurants(restaurantList);
            assertTrue(result, "Connection failed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    void uploadRestaurantsUser() {
    	RestaurantMongoDB db = new RestaurantMongoDB();
    	db = new RestaurantMongoDB("user_test");
        try {
            boolean result = db.uploadRestaurants(restaurantList);
            assertFalse(result, "Returned true even as user");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void downloadRestaurants() {
    	RestaurantMongoDB db = new RestaurantMongoDB();
    	db = new RestaurantMongoDB("user_test");
        try {
        	List<Restaurant> res = db.downloadRestaurants();
        	
        	boolean result = res.get(0).getName().equals(r1.getName()); 
        	
        	assertTrue(result, "Incorrect data in test database");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}