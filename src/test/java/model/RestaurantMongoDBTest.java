package model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RestaurantMongoDBTest {
    Restaurant r1;
    Restaurant r2;
    List<Restaurant> restaurantList;


    @BeforeAll
    void init() {
        r1  = new Restaurant(1, "Name1", "Address1", "00100", "City1", "www", "admin", "adminwww", 62, 24 );
        r2  = new Restaurant(2, "Name2", "Address2", "00100", "City2", "www2", "admin2", "adminwww2", 64, 20 );
        restaurantList = (Arrays.asList(r1, r1));
    }


    @Test
    void uploadRestaurantsAdmin() {
    	OnlineDatabase db = new RestaurantMongoDB("admin_test");
    	    	
        try {
            boolean result = db.storeRestaurants(restaurantList);
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
            assertFalse(result, "Returned true even as non_admin");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void downloadRestaurants() {
    	OnlineDatabase db = new RestaurantMongoDB();
    	db = new RestaurantMongoDB("admin_test");
    	db.setLocal(new OfflineDatabase("test"));
        try {
        	List<Restaurant> res = db.loadRestaurants();
        	
        	boolean result = res.get(0).getName().equals(r1.getName()); 
        	
        	assertTrue(result, "Incorrect data in test database");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}