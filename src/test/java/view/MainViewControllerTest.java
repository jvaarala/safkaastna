package view;

import javafx.scene.control.ListView;
import model.Restaurant;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class MainViewControllerTest extends ApplicationTest {

    @BeforeAll
    public static void setup() {
        MainViewController mainViewController = new MainViewController();
        Restaurant mockRestaurant = mock(Restaurant.class);
        List<Restaurant> restaurants = Arrays.asList(mockRestaurant);
        ListView<String> listViewNames = new ListView<>();
        String restaurantName = "Test name";
        mainViewController.updateView(restaurants);


    }

    @Test
    void updateView() {

    }

}