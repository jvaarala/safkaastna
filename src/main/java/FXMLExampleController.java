import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import model.RestaurantDAO;
import model.Restaurant;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FXMLExampleController implements Initializable {

    RestaurantDAO restaurantDAO = new RestaurantDAO();

    @FXML
    private ListView<String> listViewNames;

    @FXML
    private ObservableList<String> items = FXCollections.observableArrayList();


    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) {
        // Tyhjennetään lista
        listViewNames.getItems().clear();

        // Haetaan Restaurant-oliot tietokannasta
        List<Restaurant> restaurantsFromDb = new ArrayList<>();
        restaurantsFromDb = restaurantDAO.readRestaurants();

        // Lisätään ravintoloiden nimet ObservableListiin
        for (int i = 0; i < restaurantsFromDb.size(); i++) {
            items.add(restaurantsFromDb.get(i).getName());
        }

        // Asetetaan ObservableList ListViewiin
        listViewNames.setItems(items);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        items.add("placeholder 1");
        items.add("placeholder 2");
        items.add("placeholder 3");

        listViewNames.setItems(items);
    }
}
