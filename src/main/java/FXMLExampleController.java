import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.service.geocoding.GeocoderStatus;
import com.lynden.gmapsfx.service.geocoding.GeocodingResult;
import com.lynden.gmapsfx.service.geocoding.GeocodingService;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
//import model.RestaurantDAO;
import model.Restaurant;
import model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
// import com.lynden.gmapsfx.javascript.object.MapType;

import model.SearchLogic;
import netscape.javascript.JSObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

public class FXMLExampleController implements Initializable, MapComponentInitializedListener {

    Dotenv dotenv = Dotenv.load();
    String api = dotenv.get("APIKEY");

    private RestaurantDAO restaurantDAO = new RestaurantDAO();
    private List<Restaurant> restaurantsFromDb;
    private SearchLogic search = new SearchLogic();

    @FXML private ListView<String> listViewNames;
    @FXML private ObservableList<String> items = FXCollections.observableArrayList();
    @FXML private GoogleMapView mapView = new GoogleMapView();
    @FXML private TextField searchTextBox;

    private GoogleMap map;
    private GeocodingService geocodingService;
    private StringProperty address = new SimpleStringProperty();


    @FXML
    public void handleSearchBar(KeyEvent keyEvent) {
        String textInSearchField = searchTextBox.getText();
        List<Restaurant> foundRestaurants = search.Search(restaurantsFromDb, textInSearchField);
        updateListView(foundRestaurants);
//        fetchWithGmapsFX();
        if (foundRestaurants.size() == 0) {
            fetchNormalJava(textInSearchField);
        }
    }

/*
https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,
+Mountain+View,+CA&key=YOUR_API_KEY

 */

    public void fetchNormalJava(String s) {
//        String api2 = dotenv.get("APIKEY2");

        String sWithoutSpaces = s.replace(" ", "+");
        String httpsUrl = "https://maps.googleapis.com/maps/api/geocode/json?address="+ sWithoutSpaces +
                "&key=" + api;
        URL url;

        try {
            url = new URL(httpsUrl);
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            print_https_cert(con);
            print_content(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void print_content(HttpsURLConnection con){
        if(con!=null){

            try {

                System.out.println("****** Content of the URL ********");
                BufferedReader br =
                        new BufferedReader(
                                new InputStreamReader(con.getInputStream()));

                String input;

                while ((input = br.readLine()) != null){
                    System.out.println(input);
                }
                br.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void print_https_cert(HttpsURLConnection con){

        if(con!=null){

            try {

                System.out.println("Response Code : " + con.getResponseCode());
                System.out.println("Cipher Suite : " + con.getCipherSuite());
                System.out.println("\n");

                Certificate[] certs = con.getServerCertificates();
                for(Certificate cert : certs){
                    System.out.println("Cert Type : " + cert.getType());
                    System.out.println("Cert Hash Code : " + cert.hashCode());
                    System.out.println("Cert Public Key Algorithm : "
                            + cert.getPublicKey().getAlgorithm());
                    System.out.println("Cert Public Key Format : "
                            + cert.getPublicKey().getFormat());
                    System.out.println("\n");
                }

            } catch (SSLPeerUnverifiedException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }

        }

    }

    public void fetchWithGmapsFX() {
        System.out.println("HAKUSANA " + searchTextBox.getText());
        System.out.println(address);
        geocodingService.geocode(address.get(), (GeocodingResult[] results, GeocoderStatus status) -> {
            System.out.println("Haun results " + results);
            System.out.println("status " + status);
            LatLong latLong = null;

            if( status == GeocoderStatus.ZERO_RESULTS) {
                System.out.println("IF");
                Alert alert = new Alert(Alert.AlertType.ERROR, "No matching address found");
                alert.show();
                return;
            } else if( results.length > 1 ) {
                System.out.println("ELSE IF");
                Alert alert = new Alert(Alert.AlertType.WARNING, "Multiple results found, showing the first one.");
                alert.show();
                latLong = new LatLong(results[0].getGeometry().getLocation().getLatitude(), results[0].getGeometry().getLocation().getLongitude());
            } else {
                System.out.println("ELSE");
                latLong = new LatLong(results[0].getGeometry().getLocation().getLatitude(), results[0].getGeometry().getLocation().getLongitude());
            }

            System.out.println("rivi 94: LatLong on" + latLong);
            if (latLong == null) {

                System.out.println("LATLONG NULL");
            }
            map.setCenter(latLong);

        });
    }


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
        address.bind(searchTextBox.textProperty());         // TODO KAKE

    }

    @Override
    public void mapInitialized() {
        //Set the initial properties of the map.
        geocodingService = new GeocodingService();          // TODO KAKE
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
//        map.setCenter(new LatLong(60.192059, 24.945831));
    }

    private void updateListView(List<Restaurant> restaurants) {
        listViewNames.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    Restaurant restaurantToFind = new Restaurant();
                    System.out.println("listView click listener" + newValue);
                    for(int i = 0; i < restaurantsFromDb.size(); i++) {
                        if (restaurantsFromDb.get(i).getName().equals(newValue)) {
                            restaurantToFind = restaurantsFromDb.get(i);
                            break;
                        }
                    }
                    showRestaurantDetails(restaurantToFind);
                }
        );
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

            if (restaurants.size() < 10) {
                //          Tämä toteutus vaikuttaa melko hitaalta, keksi parempi

                InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
                infoWindowOptions.content(restaurant.getName());
                InfoWindow infoWindow = new InfoWindow(infoWindowOptions);
                infoWindow.open(map, tempMarker);
            }

            restaurantMarkers.add(tempMarker);
        }
        map.addMarkers(restaurantMarkers);


        // Asetetaan ObservableList ListViewiin
        listViewNames.setItems(items);

        // Karttanäkymän tarkennus listasisällön mukaan
        // jos täysi lista, mennään oletussijaintiin -> tähän voi muokata oletuskaupungin random koordinaattien sijaan!
        if (restaurants.size() == 310) {
            mapView.setCenter(60.192059, 24.945831);
            mapView.setZoom(12);
        }
        // jos listan pituus on jotain muuta, mutta ei nolla, fokusoidaan listan ensimmäiseen ravintolaan?
        else if (restaurants.size() > 0 ) {
            Restaurant restaurantToShow = restaurants.get(0);
            showRestaurantDetails(restaurantToShow);
        }
    }

    private void showRestaurantDetails(Restaurant restaurant) {
        System.out.println("Focus on " + restaurant.getName() + " " + restaurant.getCity());
        if (restaurant != null) {
            mapView.setCenter(restaurant.getLat(), restaurant.getLng());
            mapView.setZoom(15);
        }
    }

}
