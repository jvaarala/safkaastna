package model;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.BeforeAll;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

class OfflineDatabaseTest {
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
     void loadRestaurants() {
    	OfflineDatabase db = new OfflineDatabase(); 
    	db = new OfflineDatabase("test");
    	db.storeRestaurants(restaurantList);
    	 try {
			List<Restaurant> res = db.loadRestaurants();
	    	 boolean result = res.get(0).getName().equals(r1.getName());
	    	 assertTrue(result, "Local file data doesnt match stored");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
    
    @Test
    void loadRestaurantsOnlineVersion() {
    	OnlineDatabase db = new RestaurantMongoDB("user_test");
    	db.setLocal(new OfflineDatabase("test"));
    	
    	try {
			List<Restaurant> res = db.loadRestaurants();
	    	 boolean result = res.get(0).getName().equals(r1.getName());
	    	 assertTrue(result, "Local file data doesnt match stored");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
}
