package view;

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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import main.MainApp;
import model.Restaurant;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;

import javafx.scene.control.Button;
import model.SearchLogic;
import netscape.javascript.JSObject;

public class FXMLExampleController implements Initializable, MapComponentInitializedListener {

    private MainApp mainApp;

    Dotenv dotenv = Dotenv.load();
    String api = dotenv.get("APIKEY");

    SearchLogic search = new SearchLogic();


    @FXML
    private ListView<String> listViewNames;

    @FXML
    private ObservableList<String> items = FXCollections.observableArrayList();

    @FXML
    private GoogleMapView mapView = new GoogleMapView();

    private GoogleMap map;
    
    @FXML
    private AnchorPane mapContainer;

    @FXML
    private TextField searchTextBox;

    @FXML
    public void handleSearchBar(KeyEvent keyEvent) {
        String textInSearchField = searchTextBox.getText();
       List<Restaurant> foundRestaurants = search.Search(mainApp.getRestaurants(), textInSearchField);
        updateListView(foundRestaurants);
    }




    @FXML
    protected void handleEsimButtonAction(ActionEvent event) {
        // esimerkki napin handlerista
        System.out.println("nappia painettu");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listViewNames.setItems(items);
        System.out.println("WE DID THIS");
        mapContainer.getChildren().add(mapView);
        
        mapView.addMapInializedListener(this);
        mapView.setKey(api);

        /*
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
        */
        
    }

    @Override
    public void mapInitialized() {
        //Set the initial properties of the map.
    	System.out.println("mapp init");
        MapOptions mapOptions = new MapOptions();

        mapOptions
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .zoom(12);

        map = mapView.createMap(mapOptions);
        System.out.println("mappp 2");
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

        map.setCenter(new LatLong(60.192059, 24.945831));
        System.out.println("mappp 3");
    }

    public void updateListView(List<Restaurant> restaurants) {
        // Tyhjennetään lista
    	System.out.println("UPDATE IN PROGRESS FOR MAPS");
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
        System.out.println("UPDATE MAPS DONE");
    }
    
    public void setMainApp(MainApp mainApp) {
    	this.mainApp = mainApp;
    }

    private void showRestaurantDetails(Restaurant restaurant) {
        if (restaurant != null) {
            mapView.setCenter(restaurant.getLat(), restaurant.getLng());
            mapView.setZoom(15);
        }
    }

}
