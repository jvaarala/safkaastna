import com.lynden.gmapsfx.javascript.event.GMapMouseEvent;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import io.github.cdimascio.dotenv.Dotenv;
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
// import com.lynden.gmapsfx.javascript.object.MapType;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import netscape.javascript.JSObject;

public class FXMLExampleController implements Initializable, MapComponentInitializedListener {

    private RestaurantDAO restaurantDAO = new RestaurantDAO();
    private List<Restaurant> restaurantsFromDb;
    Dotenv dotenv = Dotenv.load();
    String api = dotenv.get("APIKEY");

    @FXML
    private ListView<String> listViewNames;

    @FXML
    private ObservableList<String> items = FXCollections.observableArrayList();

    @FXML
    private GoogleMapView mapView = new GoogleMapView();

    private GoogleMap map;


    @FXML
    protected void handleEsimButtonAction(ActionEvent event) {
        // esimerkki napin handlerista
        System.out.println("nappia painettu");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Haetaan Restaurant-oliot tietokannasta
        restaurantsFromDb = restaurantDAO.readRestaurants();

        listViewNames.setItems(items);

        mapView.addMapInializedListener(this);
        mapView.setKey(api);


        listViewNames.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    Restaurant restaurantToFind = new Restaurant();
                    System.out.println(newValue);
                    for(int i = 0; i < restaurantsFromDb.size(); i++) {
                        if (restaurantsFromDb.get(i).getName().equals(newValue)) {
                            restaurantToFind = restaurantsFromDb.get(i);
                            break;
                        }
                    }
                    showRestaurantDetails(restaurantToFind);
                }
        );
    }

    @Override
    public void mapInitialized() {
        //Set the initial properties of the map.
        MapOptions mapOptions = new MapOptions();

        mapOptions
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

        // clickin lat long tulostuu, kun klikkaa kohtaa kartalta (ei markeria)
        map.addUIEventHandler(UIEventType.click, (JSObject obj) -> {
            LatLong ll = new LatLong((JSObject) obj.getMember("latLng"));
            System.out.println("lat: " + ll.getLatitude() + " lon: " + ll.getLongitude());
        });

        updateListView(restaurantsFromDb);
        map.setCenter(new LatLong(60.192059, 24.945831));
    }

    private void updateListView(List<Restaurant> restaurants) {
        // Tyhjennetään lista
        listViewNames.getItems().clear();
        List<Marker> restaurantMarkers = new ArrayList<>();

        // Lisätään ravintoloiden nimet ObservableListiin
        for (Restaurant restaurant : restaurants) {
            items.add(restaurant.getName());
            LatLong tempLatLong = new LatLong(restaurant.getLat(), restaurant.getLng());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(tempLatLong);
            Marker tempMarker = new Marker(markerOptions);

/*          Tämä toteutus vaikuttaa melko hitaalta, keksi parempi

            InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
            infoWindowOptions.content(restaurant.getName());
            InfoWindow infoWindow = new InfoWindow(infoWindowOptions);
            infoWindow.open(map, tempMarker);*/

            restaurantMarkers.add(tempMarker);
        }
        map.addMarkers(restaurantMarkers);


        // Asetetaan ObservableList ListViewiin
        listViewNames.setItems(items);
    }

    private void showRestaurantDetails(Restaurant restaurant) {
        if (restaurant != null) {
            mapView.setCenter(restaurant.getLat(), restaurant.getLng());
            mapView.setZoom(15);
        }
    }
}
