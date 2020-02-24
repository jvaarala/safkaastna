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

import javafx.scene.control.Button;
import model.SearchLogic;
import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

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
	@FXML private CheckBox checkBox;


	@FXML
	public void handleSearchBar(KeyEvent keyEvent) {
		String textInSearchField = searchTextBox.getText();
		List<Restaurant> foundRestaurants = search.Search(mainApp.getRestaurants(), textInSearchField);
		updateListView(foundRestaurants);
	}

	@FXML protected void handleCheckbox(ActionEvent event) {
		String textInSearchField = searchTextBox.getText();
		if (checkBox.isSelected()) {
			LatLong ll = fetchNormalJava(textInSearchField);
			focusMapOnCoordinate(ll);
		}
	}

	@FXML
	protected void handleEsimButtonAction(ActionEvent event) {
		// esimerkki napin handlerista
		System.out.println("nappia painettu");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("perkl init start");
		listViewNames.setItems(items);

		mapContainer.getChildren().add(mapView);

		mapView.addMapInializedListener(this);
		mapView.setKey(api);
		System.out.println("perkl init end");
	}

	@Override
	public void mapInitialized() {
		// Set the initial properties of the map.
		System.out.println("perkl1");
		MapOptions mapOptions = new MapOptions();

		mapOptions.overviewMapControl(false).panControl(false).rotateControl(false).scaleControl(false)
				.streetViewControl(false).zoomControl(false).zoom(12);

		map = mapView.createMap(mapOptions);
		/*
		 * //Add markers to the map LatLong joeSmithLocation = new LatLong(47.6197,
		 * -122.3231); MarkerOptions markerOptions1 = new MarkerOptions();
		 * markerOptions1.position(joeSmithLocation); Marker joeSmithMarker = new
		 * Marker(markerOptions1); map.addMarker( joeSmithMarker );
		 */

		// clickin lat long tulostuu, kun klikkaa kohtaa kartalta (ei markeria)
		map.addUIEventHandler(UIEventType.click, (JSObject obj) -> {
			LatLong ll = new LatLong((JSObject) obj.getMember("latLng"));
			System.out.println("lat: " + ll.getLatitude() + " lon: " + ll.getLongitude());
		});

		map.setCenter(new LatLong(60.192059, 24.945831));
		this.mainApp.updateMap();
	}

	public void updateListView(List<Restaurant> restaurants) {
		// Tyhjennetään lista
		System.out.println("UPDATE IN PROGRESS FOR MAPS");
		listViewNames.getItems().clear();
		List<Marker> restaurantMarkers = new ArrayList<>();
		map.clearMarkers();

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
				showRestaurantDetails(restaurantToFind);
			}
		});

		// Lisätään ravintoloiden nimet ObservableListiin
		for (Restaurant restaurant : restaurants) {
			items.add(restaurant.getName());
			LatLong tempLatLong = new LatLong(restaurant.getLat(), restaurant.getLng());
			MarkerOptions markerOptions = new MarkerOptions();
			markerOptions.position(tempLatLong);
			Marker tempMarker = new Marker(markerOptions);

			InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
			infoWindowOptions.content(restaurant.getName());
			InfoWindow infoWindow = new InfoWindow(infoWindowOptions);

			map.addUIEventHandler(tempMarker, UIEventType.click, (JSObject obj) -> {
				infoWindow.open(map, tempMarker);
			});

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

	public LatLong fetchNormalJava(String s) {
		System.out.println("FetchJava alku");
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

	private void focusMapOnCoordinate(LatLong ll) {
		System.out.printf("focusOnCoordinate alku");
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(ll);
		// TODO tähän markerille uusi ulkonäkö
		markerOptions.label("Hakemasi osoite");
		Marker tempMarker = new Marker(markerOptions);
		map.addMarkers(Collections.singletonList(tempMarker));
		mapView.setCenter(ll.getLatitude(), ll.getLongitude());
		mapView.setZoom(15);
		System.out.println("focusOnCoordinate loppu");
	}
}
