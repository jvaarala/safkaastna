package view;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import model.Restaurant;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MapControllerTest extends ApplicationTest {

    @BeforeAll
    public static void setup() {
        MapController mapController = new MapController();
        Restaurant mockRestaurant = mock(Restaurant.class);
        List<Restaurant> restaurants = Arrays.asList(mockRestaurant);
        ListView<String> listViewNames = new ListView<>();
        String restaurantName = "Test name";
        mapController.updateView(restaurants);


    }

    @Test
    void updateView() {

    }

}