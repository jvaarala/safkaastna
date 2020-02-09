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

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
// import com.lynden.gmapsfx.javascript.object.MapType;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class FXMLExampleController implements Initializable, MapComponentInitializedListener {

    private RestaurantDAO restaurantDAO = new RestaurantDAO();

    @FXML
    private ListView<String> listViewNames;

    @FXML
    private ObservableList<String> items = FXCollections.observableArrayList();

    @FXML
    private GoogleMapView mapView = new GoogleMapView();

    private GoogleMap map;


    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) {
        // Tyhjennetään lista
        listViewNames.getItems().clear();

        // Haetaan Restaurant-oliot tietokannasta
        List<Restaurant> restaurantsFromDb;
        List<Marker> restaurantMarkers = new ArrayList<>();
        restaurantsFromDb = restaurantDAO.readRestaurants();

        // Lisätään ravintoloiden nimet ObservableListiin
        for (Restaurant restaurant : restaurantsFromDb) {
            items.add(restaurant.getName());
            LatLong tempLatLong = new LatLong(restaurant.getLat(), restaurant.getLng());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(tempLatLong);
            Marker tempMarker = new Marker(markerOptions);
            restaurantMarkers.add(tempMarker);
        }

        map.addMarkers(restaurantMarkers);

        // Asetetaan ObservableList ListViewiin
        listViewNames.setItems(items);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        items.add("Hae painamalla nappia");

        listViewNames.setItems(items);

        mapView.addMapInializedListener(this);

    }

    @Override
    public void mapInitialized() {
        //Set the initial properties of the map.
        MapOptions mapOptions = new MapOptions();

        mapOptions.center(new LatLong(60.192059, 24.945831))
//                .mapType(MapType.ROADMAP)
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .zoom(12);

        map = mapView.createMap(mapOptions);

/*        //Add markers to the map
        LatLong joeSmithLocation = new LatLong(47.6197, -122.3231);
        MarkerOptions markerOptions1 = new MarkerOptions();
        markerOptions1.position(joeSmithLocation);
        Marker joeSmithMarker = new Marker(markerOptions1);
        map.addMarker( joeSmithMarker );
        */
    }
}
