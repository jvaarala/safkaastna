package view;

import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import com.mysql.jdbc.StringUtils;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import main.MainApp;
import model.Restaurant;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;

import model.SearchLogic;
import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

public class MapController implements Initializable, MapComponentInitializedListener {

	private MainApp mainApp;

	/**
	 * Google maps api key is written on a separate, local file
	 * Dotenv handles retrieving apikey and it is stored on a variable
	 */
	private Dotenv dotenv = Dotenv.load();
	private String api = dotenv.get("APIKEY");

	@FXML private ListView<String> listViewNames;
	@FXML private ObservableList<String> items = FXCollections.observableArrayList();
	@FXML private AnchorPane mapContainer;
	@FXML private TextField searchTextBox;
	@FXML private CheckBox checkBox;
	@FXML private GoogleMapView mapView = new GoogleMapView();
	private GoogleMap map;
	private SearchLogic search = new SearchLogic();

	/**
	 * FXML method for handling search box
	 * @param keyEvent - Listens to every keystroke on input field
	 */
	@FXML
	protected void handleSearchBar(KeyEvent keyEvent) {
		String textInSearchField = searchTextBox.getText();
		List<Restaurant> foundRestaurants = search.Search(mainApp.getRestaurants(), textInSearchField);
		updateView(foundRestaurants);
	}

	/**
	 * FXML method for handling the 'Searching for an address'-checkbox
	 * @param event Listens to every click on checkbox
	 */
	@FXML protected void handleCheckbox(ActionEvent event) {
		String textInSearchField = searchTextBox.getText();
		if (checkBox.isSelected()) {
			LatLong ll = fetchGoogleCoordinates(textInSearchField);
			focusMapOnCoordinate(ll, textInSearchField);
		}
	}

	@FXML
	protected void handleEsimButtonAction(ActionEvent event) {
		// esimerkki napin handlerista
		System.out.println("nappia painettu");
	}

	/**
	 * // TODO EN OSAA KUVATA TÄTÄ!
	 * @param location
	 * @param resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("perkl init start");
		listViewNames.setItems(items);

		mapContainer.getChildren().add(mapView);

		mapView.addMapInializedListener(this);
		mapView.setKey(api);
		System.out.println("perkl init end");
	}

	/**
	 * mapInitializer for GmapsFX library
	 * Sets desired map options and creates GoogleMap object
	 * Focuses map on a predestined location (Helsinki)
	 */
	@Override
	public void mapInitialized() {
		// Set the initial properties of the map.
		System.out.println("perkl1");
		MapOptions mapOptions = new MapOptions();

		mapOptions.overviewMapControl(false).panControl(false).rotateControl(false).scaleControl(false)
				.streetViewControl(false).zoomControl(false).zoom(12);

		map = mapView.createMap(mapOptions);

		// Prints LatLong according to map click to console
		map.addUIEventHandler(UIEventType.click, (JSObject obj) -> {
			LatLong ll = new LatLong((JSObject) obj.getMember("latLng"));
			System.out.println("lat: " + ll.getLatitude() + " lon: " + ll.getLongitude());
		});

		map.setCenter(new LatLong(60.192059, 24.945831));
		this.mainApp.updateMap();
	}

	/**
	 * Update ListView & map elements according to a list of restaurants
	 * @param restaurants - List of restaurants to be iterated through
	 *                    Names are set on ListView and Markers are set on map on restaurants location
	 */
	public void updateView(List<Restaurant> restaurants) {
		// Empty list & clear markers
		System.out.println("UPDATE IN PROGRESS FOR MAPS");
		listViewNames.getItems().clear();
		List<Marker> restaurantMarkers = new ArrayList<>();
		map.clearMarkers();

		// Click listener for ListView item clicks
		listViewNames.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				Restaurant restaurantToFind = new Restaurant();
				System.out.println(newValue);
				for (int i = 0; i < restaurants.size(); i++) {
					if (restaurants.get(i).getName().equals(newValue)) {
						restaurantToFind = restaurants.get(i);
						break;
					}
				}
				focusMapOnRestaurant(restaurantToFind);
			}
		});

		// Add restaurants to ObservableList
		for (Restaurant restaurant : restaurants) {
			items.add(restaurant.getName());
			LatLong tempLatLong = new LatLong(restaurant.getLat(), restaurant.getLng());
			MarkerOptions markerOptions = new MarkerOptions();
			markerOptions.position(tempLatLong);
			Marker tempMarker = new Marker(markerOptions);

			// Create InfoWindow for each marker
			InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
			infoWindowOptions.content(restaurant.getName());
			InfoWindow infoWindow = new InfoWindow(infoWindowOptions);

			// ClickListener for InfoWindow
			map.addUIEventHandler(tempMarker, UIEventType.click, (JSObject obj) -> {
				infoWindow.open(map, tempMarker);
			});

			restaurantMarkers.add(tempMarker);
		}
		map.addMarkers(restaurantMarkers);

		// Set ObservableList to ListView
		listViewNames.setItems(items);
		System.out.println("UPDATE MAPS DONE");
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	/**
	 * Focus map on a certain restaurant
	 * @param restaurant - Restaurant on which to focus the map on
	 */
	private void focusMapOnRestaurant(Restaurant restaurant) {
		if (restaurant != null) {
			mapView.setCenter(restaurant.getLat(), restaurant.getLng());
			mapView.setZoom(15);
		}
	}

	/**
	 * Search Google Maps api for coordinates with address
	 * @param s User input String (address) to be searched from Google maps api
	 * @return LatLong object to be placed on map
	 */
	private LatLong fetchGoogleCoordinates(String s) {

		// Format string to be usable as a part of search url
		String sWithoutSpaces = s
				.replace(",", "")
				.replace(" ", "+");
		String httpsUrl = "https://maps.googleapis.com/maps/api/geocode/json?address="+ sWithoutSpaces +
				"&key=" + api;
		URL url;
		HttpsURLConnection con = null;
		StringBuilder result = new StringBuilder();
		InputStream in = null;
		BufferedReader reader = null;

		// Create String from api response
		try {
			url = new URL(httpsUrl);
			con = (HttpsURLConnection)url.openConnection();
			in = new BufferedInputStream(con.getInputStream());
			reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null) {
				result.append(line);
			}
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

		// Modify api response String to a LatLong Object
		LatLong ll = null;
		JSONObject resultJSON = new JSONObject(result.toString());
		JSONArray resultArray = resultJSON.getJSONArray("results");
		for (int i = 0; i < resultArray.length(); i++) {
			JSONObject results = resultArray.getJSONObject(i);
			JSONObject geometry = results.getJSONObject("geometry");
			JSONObject location = geometry.getJSONObject("location");
			ll = new LatLong(location.getDouble("lat"), location.getDouble("lng"));
		}
		System.out.println("fetch loppu");
		return ll;
	}

	/**
	 * Focus map to a specific coordinate & string formatting for showing user input on a markers info window
	 * @param ll User input address converted to LatLong object
	 * @param s User input address as a string to be used on markers infoWindow feature
	 */

	private void focusMapOnCoordinate(LatLong ll, String s) {
		try {
			MarkerOptions markerOptions = new MarkerOptions();
			markerOptions.position(ll);
			markerOptions.icon("https://users.metropolia.fi/~katriras/OTP1/map-marker.png");
			Marker tempMarker = new Marker(markerOptions);
			InfoWindowOptions infoWindowOptions = new InfoWindowOptions();

			// User input (address) formatting
			String words[] = s.replaceAll("\\s+", " ").trim().split(" ");
			String newString = "";
			for (String word : words) {
				if (StringUtils.isStrictlyNumeric(word)) {
					newString += word + " ";
					continue;
				}
				for (int i = 0; i < word.length(); i++) {
					char c = word.charAt(i);
					if (i == 0) {
						newString = newString + word.substring(i, i + 1).toUpperCase();
					} else {
						if (i != word.length() - 1) {
							newString += word.substring(i, i + 1).toLowerCase();
						} else {
							newString += word.substring(i, i + 1).toLowerCase() + " ";
						}
					}
				}
			}
			infoWindowOptions.content(newString);
			InfoWindow infoWindow = new InfoWindow(infoWindowOptions);
			map.addUIEventHandler(tempMarker, UIEventType.click, (JSObject obj) -> {
				infoWindow.open(map, tempMarker);
			});
			// add new marker to map on correct location & zoom in
			map.addMarkers(Collections.singletonList(tempMarker));
			mapView.setCenter(ll.getLatitude(), ll.getLongitude());
			mapView.setZoom(15);
		} catch (Exception e ) {
//			e.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("ADDRESS NOT FOUND");
			alert.setHeaderText("No search results");
			alert.setContentText("Try again! Preferred format on address is 'Streetname 1 City'");
			alert.show();
		}
	}
}
