import com.lynden.gmapsfx.javascript.event.GMapMouseEvent;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
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
        List<Marker> restaurantMarkers = new ArrayList<>();
        restaurantsFromDb = restaurantDAO.readRestaurants();


        // Lisätään ravintoloiden nimet ObservableListiin
        for (Restaurant restaurant : restaurantsFromDb) {
            items.add(restaurant.getName());
            LatLong tempLatLong = new LatLong(restaurant.getLat(), restaurant.getLng());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(tempLatLong);
            Marker tempMarker = new Marker(markerOptions);

            InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
            infoWindowOptions.content(restaurant.getName());
            infoWindowOptions.disableAutoPan(false);
            InfoWindow infoWindow = new InfoWindow(infoWindowOptions);
            infoWindow.open(map, tempMarker);

            restaurantMarkers.add(tempMarker);
        }

        map.addMarkers(restaurantMarkers);

        // clickin lat long tulostuu, kun klikkaa kohtaa kartalta (ei markeria)
        map.addUIEventHandler(UIEventType.click, (JSObject obj) -> {
            LatLong ll = new LatLong((JSObject) obj.getMember("latLng"));
            System.out.println("lat: " + ll.getLatitude() + " lon: " + ll.getLongitude());
        });
        // tämä toimii ihan vastaavasti kun ylläoleva
//        map.addMouseEventHandler(UIEventType.click, (GMapMouseEvent e) -> {
//            System.out.println(e);
//            LatLong latLong = e.getLatLong();
//            System.out.println("Lat: " + latLong.getLatitude());
//            System.out.println("Long " + latLong.getLongitude());
//        });

        // Asetetaan ObservableList ListViewiin
        listViewNames.setItems(items);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        items.add("Hae painamalla nappia");

        listViewNames.setItems(items);

        mapView.addMapInializedListener(this);
        listViewNames.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    int id = -1;
                    System.out.println(newValue);
                    for(int i = 0; i < restaurantsFromDb.size(); i++) {
                        if (restaurantsFromDb.get(i).getName().equals(newValue)) {
                            id = i;
                            break;
                        }
                    }
                    Restaurant restaurantToFind = restaurantsFromDb.get(id);
                    System.out.println(restaurantToFind.toString());
                    showRestaurantDetails(restaurantToFind);
                }
        );
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

    private void showRestaurantDetails(Restaurant restaurant) {
        if (restaurant != null) {
            System.out.println("Täällä");
//            MapOptions mapOptions = new MapOptions();
//            mapOptions.center(new LatLong(restaurant.getLat(), restaurant.getLng()))
//                .overviewMapControl(false)
//                    .panControl(false)
//                    .rotateControl(false)
//                    .scaleControl(false)
//                    .streetViewControl(false)
//                    .zoomControl(false)
//                    .zoom(12);
            mapView.setCenter(restaurant.getLat(), restaurant.getLng());
        } else {
            System.out.println("Ravintolaa ei valittu");
        }
    }
}
