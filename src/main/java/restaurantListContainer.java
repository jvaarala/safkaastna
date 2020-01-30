import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.Restaurant;
import model.RestaurantDAO;

import java.util.ArrayList;
import java.util.List;

public class restaurantListContainer {
    private StackPane root = new StackPane();

    public restaurantListContainer() {
        VBox vbox = new VBox();

        Label title = new Label("All restaurants");
        ListView<String> listView = new ListView<String>();
        List<Restaurant> restaurants = getRestaurants();
        // filling observableList
        ObservableList<String> itemsForListView = FXCollections.observableArrayList();
        for (int i = 0; i < restaurants.size(); i++) {
//                    itemsForListView.add(new Restaurant(
//                            restaurants.get(i).getId(),
//                            restaurants.get(i).getName(),
//                            restaurants.get(i).getAddress(),
//                            restaurants.get(i).getPostal_code(),
//                            restaurants.get(i).getCity(),
//                            restaurants.get(i).getWww(),
//                            restaurants.get(i).getAdmin(),
//                            restaurants.get(i).getAdmin_www(),
//                            restaurants.get(i).getLat(),
//                            restaurants.get(i).getLng()
//                    ));
            itemsForListView.add(restaurants.get(i).getName());
        }
        listView.setItems(itemsForListView);
        vbox.getChildren().addAll(title, listView);

        root.getChildren().add(vbox);
    }

    private List<Restaurant> getRestaurants() {
        RestaurantDAO dao = new RestaurantDAO();
        List<Restaurant> restaurantsFromDb = new ArrayList<>();

        restaurantsFromDb = dao.readRestaurants();
//        System.out.println(restaurantsFromDb.get(10));
        return restaurantsFromDb;
    }

    public StackPane getRoot() {
        return root;
    }
}