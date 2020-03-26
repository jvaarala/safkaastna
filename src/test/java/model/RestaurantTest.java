package model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {
    private Restaurant r;

    @BeforeEach
    void beforeEach() {
        r = new Restaurant(
                666, "name",
                "address",
                "00000",
                "city",
                "www",
                "admin",
                "adminwww",
                62.8787878,
                23.3876387);
    }

    @Test
    void getName() {
        assertEquals("name", r.getName(), "getName does not work");
    }

    @Test
    void setName() {
        r.setName("New name");
        assertEquals("New name", r.getName(), "setName does not work");
    }

    @Test
    void getAddress() {
        assertEquals("address", r.getAddress(), "getAddress does not work");
    }

    @Test
    void setAddress() {
        r.setAddress("new address");
        assertEquals("new address", r.getAddress(), "setAddress does not work");
    }

    @Test
    void getPostal_code() {
        assertEquals("00000", r.getPostal_code(), "getPostal_code does not work");
    }

    @Test
    void setPostal_code() {
        r.setPostal_code("99999");
        assertEquals("99999", r.getPostal_code(), "setPostal_code does not work");
    }

    @Test
    void getCity() {
        assertEquals("city", r.getCity(), "getCity does not work");
    }

    @Test
    void setCity() {
        r.setCity("new city");
        assertEquals("new city", r.getCity(), "setCity does not work");
    }

    @Test
    void getWww() {
        assertEquals("www", r.getWww(), "getWww does not work");
    }

    @Test
    void setWww() {
        r.setWww("new www");
        assertEquals("new www", r.getWww(), "setWww does not work");
    }

    @Test
    void getAdmin() {
        assertEquals("admin", r.getAdmin(), "getAdmin does not work");
    }

    @Test
    void setAdmin() {
        r.setAdmin("new admin");
        assertEquals("new admin", r.getAdmin(), "setAdmin does not work");
    }

    @Test
    void getAdmin_www() {
        assertEquals("adminwww", r.getAdmin_www(), "getAdmin_www does not work");
    }

    @Test
    void setAdmin_www() {
        r.setAdmin_www("new www");
        assertEquals("new www", r.getAdmin_www(), "getAdmin_www does not work");
    }

    @Test
    void getId() {
        assertEquals(666, r.getId(), "getId does not work");
    }

    @Test
    void setId() {
        r.setId(1000);
        assertEquals(1000, r.getId(), "setId does not work");
    }

    @Test
    void getLat() {
        assertEquals(62.8787878, r.getLat(), "getLat does not work");
    }

    @Test
    void setLat() {
        r.setLat(63.05);
        assertEquals(63.05, r.getLat(), "setLat does not work");
    }

    @Test
    void getLng() {
        assertEquals(23.3876387, r.getLng(), "getLng does not work");
    }

    @Test
    void setLng() {
        r.setLng(23.05);
        assertEquals(23.05, r.getLng(), "setLng does not work");
    }
    
    @Test
    void toStringTest() {
    	String correctText = "Restaurant{id=666, name='name', address='address', postal_code=00000, city='city', www='www', admin='admin', admin_www='adminwww', lat=62.8787878, lng=23.3876387}";
    	assertEquals(correctText, r.toString(), "toString failure. Check for changes");
    }
}