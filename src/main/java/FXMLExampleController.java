import com.lynden.gmapsfx.javascript.event.GMapMouseEvent;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import jdk.nashorn.internal.parser.JSONParser;
import model.RestaurantDAO;
import model.Restaurant;

import java.io.*;
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
import model.SearchLogic;
import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

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
    @FXML private CheckBox checkBox;

    private GoogleMap map;

    @FXML
    public void handleSearchBar(KeyEvent keyEvent) {
        System.out.println("handleSearchBar alku");
        String textInSearchField = searchTextBox.getText();
        if(checkBox.isSelected()) {
            LatLong ll = fetchNormalJava(textInSearchField);
            focusMapOnCoordinate(ll);
        }
        List<Restaurant> foundRestaurants = search.Search(restaurantsFromDb, textInSearchField);
        updateListView(foundRestaurants);
        System.out.println("handleSearchBar loppu");
    }

    @FXML
    protected void handleEsimButtonAction(ActionEvent event) {
        // esimerkki napin handlerista
        System.out.println("nappia painettu");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("initialize alku");
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
        System.out.println("initialize loppu");
    }

    @Override
    public void mapInitialized() {
        System.out.println("mapInitialized alku");
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
        System.out.println("mapInitialized alku");
    }

    private void updateListView(List<Restaurant> restaurants) {
        System.out.println("updateListview alku");
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

        System.out.println("updateListview loppu");
    }

    private void showRestaurantDetails(Restaurant restaurant) {
        System.out.println("showRestaurantDetails alku");
        if (restaurant != null) {
            mapView.setCenter(restaurant.getLat(), restaurant.getLng());
            mapView.setZoom(15);
        }
        System.out.println("showRestaurantDetails loppu");

    }

    private void focusMapOnCoordinate(LatLong ll) {
        System.out.printf("focusMapOnCoordinates alku");
        mapView.setCenter(ll.getLatitude(), ll.getLongitude());
        mapView.setZoom(15);
        System.out.printf("focusMapOnCoordinates loppu");
    }

    public LatLong fetchNormalJava(String s) {
        System.out.println("FETCHING STUFF alku");

        String sWithoutSpaces = s.replace(" ", "+");
        String httpsUrl = "https://maps.googleapis.com/maps/api/geocode/json?address="+ sWithoutSpaces +
                "&key=" + api;
        URL url;
        HttpsURLConnection con = null;
        StringBuilder result = new StringBuilder();
        InputStream in = null;
        BufferedReader reader = null;

        try {
            url = new URL(httpsUrl);
            con = (HttpsURLConnection)url.openConnection();
            in = new BufferedInputStream(con.getInputStream());
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

//            print_https_cert(con);
//            print_content(con);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            if (con != null) {
                con.disconnect();
            }
        }

        LatLong ll = null;
        JSONObject resultJSON = new JSONObject(result.toString());
        JSONArray resultArray = resultJSON.getJSONArray("results");
        for (int i = 0; i < resultArray.length(); i++) {
            JSONObject results = resultArray.getJSONObject(i);
            JSONObject geometry = results.getJSONObject("geometry");
            JSONObject location = geometry.getJSONObject("location");
            ll = new LatLong(location.getDouble("lat"), location.getDouble("lng"));
        }
        System.out.println(ll);
        System.out.println("FETCHING STUFF loppu");
        return ll;
    }
}


