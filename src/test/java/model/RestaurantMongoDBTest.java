package model;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import controllers.RestaurantMongoDB;

import org.bson.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@Disabled // KESKEN
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
        dao = new RestaurantMongoDB("admin");
    }


    @Test
    void uploadRestaurants() {
        MongoClient mongoClient = mock(MongoClient.class);
        when(MongoClients.create()).thenReturn(mongoClient);
        MongoDatabase database = mock(MongoDatabase.class);
        when(mongoClient.getDatabase("SafkaaSTNA")).thenReturn(database);
        MongoCollection<Document> mockCollection = mock(MongoCollection.class);
        when(database.getCollection("Restaurants")).thenReturn(mockCollection);

        try {
            dao.uploadRestaurants(restaurantList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    void downloadRestaurants() {
    }

    @Test
    void createRestaurantsJson() {
    }

    @Test
    void createRestaurantsList() {
    }

    @Test
    void storeRestaurantsToFile() {
    }

    @Test
    void readRestaurantsFromFile() {
    }
}