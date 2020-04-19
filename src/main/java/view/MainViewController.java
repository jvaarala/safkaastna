package view;

import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.shapes.Circle;
import com.mysql.jdbc.StringUtils;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.MainApp;
import model.Restaurant;

import java.net.URL;
import java.util.*;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;

import model.SearchLogic;
import netscape.javascript.JSObject;

/**
 * MainViewController Class controls the listView of restaurants and embedded google maps view.
 */
public class MainViewController implements Initializable, MapComponentInitializedListener {

    @FXML
    private ListView<String> listViewNames;
    @FXML
    private ObservableList<String> items = FXCollections.observableArrayList();
    @FXML
    private AnchorPane mapContainer;
    @FXML
    private TextField searchTextBox;
    @FXML
    private Button searchButton;
    @FXML
    private ToggleButton filterToggleButton;
    @FXML
    private GoogleMapView mapView = new GoogleMapView();
    @FXML
    private Button nearestButton;
    private GoogleMap map;
    private SearchLogic search = new SearchLogic();
    private String textInSearchField;
    private Restaurant nearest;

    private MainApp mainApp;

    public void setGoogleMapStuff(GoogleMapView mapView, GoogleMap map) {
        this.mapView = mapView;
        this.map = map;
        System.out.println(map);
        System.out.println(mapView);
    }

    /**
     * Used to give a reference to the mainApp for this controller.
     * Should be done after controller initialisation, before using any of its functions.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Google maps api key is written on a separate, local file
     * Dotenv handles retrieving apikey and it is stored on a variable
     */
    private Dotenv dotenv = Dotenv
            .configure()
            .ignoreIfMissing()
            .load();
    private String api = dotenv.get("APIKEY");

    /**
     * Initial setup for application.
     * sets content to observable list,
     * adds map to mapcontainer and sets api key for google api calls
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("initialize");
        mapView.setKey(api);
        mapContainer.getChildren().add(mapView);
        mapView.addMapInializedListener(this);
        mapView.setPrefSize(0, 0);
    }

    /**
     * mapInitializer for GmapsFX library
     * Sets desired map options and creates GoogleMap object
     * Focuses map on a predestined location (Helsinki)
     */
    @Override
    public void mapInitialized() {
        System.out.println("mapInitialized");
        // Set the initial properties of the map.
        MapOptions mapOptions = new MapOptions();

        mapOptions.overviewMapControl(false).panControl(false).rotateControl(false).scaleControl(false)
                .streetViewControl(false).zoomControl(false).zoom(12).mapTypeControl(false);
        AnchorPane.setTopAnchor(mapView, 0.0);
        AnchorPane.setRightAnchor(mapView, 0.0);
        AnchorPane.setBottomAnchor(mapView, 0.0);
        AnchorPane.setLeftAnchor(mapView, 0.0);
        map = mapView.createMap(mapOptions);

/*        map.addUIEventHandler(UIEventType.click, (JSObject obj) -> {
            LatLong ll = new LatLong((JSObject) obj.getMember("latLng"));
			System.out.println("lat: " + ll.getLatitude() + " lon: " + ll.getLongitude());
        });*/

        map.setCenter(new LatLong(60.192059, 24.945831));
        updateView(mainApp.getRestaurants());
    }

    /**
     * Update ListView and map elements according to a list of restaurants
     *
     * @param restaurants - List of restaurants to be iterated through
     *                    Names are set on ListView and Markers are set on map on restaurants location
     */
    public void updateView(List<Restaurant> restaurants) {
        System.out.println("updateView");
        updateListView(restaurants);
        updateMarkers(restaurants);

        /// nämä pois täältä
        mainApp.sidebarOff();

        // asettaa tekstin hakunapille
        searchButton.setText(mainApp.getBundle().getString("search"));

        // asettaa tekstin filter napille joka voi olla päällä tai pois päältä
        if (filterToggleButton.isSelected()) {
            filterToggleButton.setText(mainApp.getBundle().getString("filtering"));
        }else {
            filterToggleButton.setText(mainApp.getBundle().getString("filterToggle"));
        }

        nearestButton.setText(mainApp.getBundle().getString("nearest"));
        mainApp.getOptionsControl().updateButtons();

    }

    public void updateListView(List<Restaurant> restaurants) {
        System.out.println("updateListView");

        listViewNames.getItems().clear();
        for (Restaurant restaurant : restaurants) {
            items.add(restaurant.getName());
        }

        // Click listener for ListView item clicks
        listViewNames.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                for (Restaurant restaurant : restaurants) {
                    if (restaurant.getName().equals(newValue)) {
                        focusMapOnRestaurant(restaurant);
                        mainApp.getSidebarControl().showRestaurantInfo(restaurant, mainApp.getBundle());
                        break;
                    }
                }
            }
        });
        // Set ObservableList to ListView
        listViewNames.setItems(items);
    }

    private void updateMarkers(List<Restaurant> restaurants) {
        System.out.println("updateMarkers");
        List<Marker> restaurantMarkers = new ArrayList<>();
        map.clearMarkers();
        if (mainApp.getUserLocation() != null) {

            restaurantMarkers.add(createUserLocationMarker());
        }

        for (Restaurant restaurant : restaurants) {
            LatLong tempLatLong = new LatLong(restaurant.getLat(), restaurant.getLng());
            MarkerOptions markerOptions = new MarkerOptions();
            // if nearest restaurant is known, highlight it somehow
            if (nearest != null && tempLatLong.getLatitude() == nearest.getLat() && tempLatLong.getLongitude() == nearest.getLng()) {
                markerOptions
                        .position(tempLatLong)
                        .icon("https://users.metropolia.fi/~katriras/OTP1/kela_nearest.gif")
                        ;
                System.out.println("Nearest marker should be different");
            } else {
                markerOptions.position(tempLatLong);
                markerOptions.icon("https://www.kela.fi/documents/10180/24327790/Logo_Tunnus_rgb.gif/7a417c63-36b0-4ef0-ad4c-3e29fb5196e9?t=1553685514309");
            }
            Marker tempMarker = new Marker(markerOptions);
            map.addUIEventHandler(tempMarker, UIEventType.click, (JSObject obj) -> {
                mainApp.getSidebarControl().showRestaurantInfo(restaurant, mainApp.getBundle());
                mainApp.sidebarOn();
            });
            restaurantMarkers.add(tempMarker);
        }
        map.addMarkers(restaurantMarkers);

        // Map zoom & focus options
        if (restaurants.size() < 20 && restaurants.size() != 0) {
            focusMapOnRestaurant(restaurants.get(0));
        } else if (mainApp.getUserLocation() != null && nearest != null) {
            fitBounds(mainApp.getUserLocation(), nearest);
        } else if (mainApp.getUserLocation() != null) {
            map.setCenter(mainApp.getUserLocation());
            map.setZoom(12);
        } else {
            mapView.setCenter(60.192059, 24.945831);
            mapView.setZoom(12);
        }
    }

    /**
     * FXML method for getting text content from search box
     *
     * @param keyEvent - Listens to every keystroke on input field
     */
    @FXML
    protected void handleSearchBar(KeyEvent keyEvent) {
        System.out.println("handleSearchBar");
        textInSearchField = searchTextBox.getText();
        if (filterToggleButton.isSelected()) {
            List<Restaurant> foundRestaurants = search.filter(mainApp.getRestaurants(), textInSearchField);
            updateView(foundRestaurants);
        }
    }

    /**
     * Event handler for search button
     * If toggle button for restaurant filtering is not selected, will continue to fetchCoordinates() &
     * focusMapOnCoordinate()
     *
     * @param event
     */
    @FXML
    protected void handleSearchButton(ActionEvent event) {
        System.out.println("handleSearchButton");
        if (!filterToggleButton.isSelected()) {
            map.clearMarkers();
            updateMarkers(mainApp.getRestaurants());
            mainApp.setUserLocation(search.fetchGoogleCoordinates(textInSearchField));
            createAndFocusOnUserLocationMarker(mainApp.getUserLocation());
            mainApp.getSidebarControl().setUserLocationText(formatString(textInSearchField));
        } else {
        }
    }

    /*
    TODO MOVE THIS TO YOUR NEW BUTTON, WHEN EXECUTED
     */
    @FXML
    protected void handleLocateNearestButton(ActionEvent event) {
        System.out.println("handleLocateNearestButton");
        if (mainApp.getUserLocation() == null) {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(mainApp.getPrimaryStage());
            VBox dialogVbox = new VBox(20);
            dialogVbox.getChildren().add(new Text(mainApp.getBundle().getString("nearestpopuptext")));
            TextField startAddress = new TextField(mainApp.getBundle().getString("nearestpopuphint"));
            Button searchButton = new Button(mainApp.getBundle().getString("nearestpopupbutton"));
            dialogVbox.getChildren().addAll(startAddress, searchButton);
            Scene dialogScene = new Scene(dialogVbox, 300, 200);
            dialogScene.getStylesheets().add("Styles.css");
            dialog.setScene(dialogScene);
            dialog.show();
            searchButton.setOnAction(
                    event1 -> {
                        String address = startAddress.getText();
                        mainApp.setUserLocation(search.fetchGoogleCoordinates(address));
                        dialog.close();
                        //		userLocation = new LatLong(60.240165, 24.042544);
                        createAndFocusOnUserLocationMarker(mainApp.getUserLocation());
                        nearest = findNearest(mainApp.getUserLocation());
                    });
        } else {
            nearest = this.findNearest(mainApp.getUserLocation());
        }
        System.out.println("nearest " + nearest + " updateMarkers next line");
        updateMarkers(mainApp.getRestaurants());
    }

    /**
     * Event handler for restaurant filter toggle button
     * if toggle is on, will change button text to "Restaurant filter ON"
     * and call updateView with limited restaurant list
     * if not, text set to "Restaurant filter OFF" and call updateView with list including all restaurants
     *
     * @param event - event when button is toggled
     */
    @FXML
    protected void handleFilterToggle(ActionEvent event) {
        System.out.println("handleFilterToggle");
        if (filterToggleButton.isSelected()) {
            searchButton.setDisable(true);
            filterToggleButton.setText(mainApp.getBundle().getString("filtering"));
            List<Restaurant> foundRestaurants = search.filter(mainApp.getRestaurants(), searchTextBox.getText());
            updateView(foundRestaurants);
        } else {
            searchButton.setDisable(false);
            filterToggleButton.setText(mainApp.getBundle().getString("filterToggle"));
            updateView(mainApp.getRestaurants());
        }
        searchTextBox.requestFocus();
    }

    public Restaurant findNearest(LatLong userLocation) {
        System.out.println("findNearestAndFitBounds");
        // userLocation = new LatLong(60.240165, 24.042544);
        Restaurant nearest = search.findNearestRestaurant(mainApp.getRestaurants(), userLocation);
        System.out.println(nearest);
        mainApp.getSidebarControl().showRestaurantInfo(nearest, mainApp.getBundle());
        return nearest;
    }


    public void fitBounds(LatLong userLocation, Restaurant nearest) {
        // zooming with different approach THIS WORKS BETTER!
        double distance = search.calculateDistanceToNearest(nearest, userLocation);
        Circle circle = new Circle();
        circle.setRadius(distance);
        circle.setCenter(new LatLong(nearest.getLat(), nearest.getLng()));
        map.fitBounds(circle.getBounds());
        System.out.println("zoom level " + map.getZoom());

//        map.fitBounds(new LatLongBounds(userLocation, new LatLong(nearest.getLat(), nearest.getLng())));
//        // zoom out by 1 so that markers are not hidden behind ListView
//        System.out.println(map.getZoom());
//        int zoomValue = map.getZoom();
//        if (zoomValue < 10) {
//            map.setCenter(new LatLong(nearest.getLat(), nearest.getLng()));
//            map.setZoom(15);
//        } else {
//            map.setZoom(zoomValue - 1);
//        }
    }

    public Marker createUserLocationMarker() {
        System.out.println("createUserLocationMarker");
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions
                .position(mainApp.getUserLocation())
                .icon("https://users.metropolia.fi/~katriras/OTP1/map-marker.png")
                .animation(Animation.BOUNCE)
                ;
        return new Marker(markerOptions);
    }

    /**
     * Creates a new marker for user location and focuses mapview to given marker
     *
     * @param ll User location as LatLong object
     */
    public void createAndFocusOnUserLocationMarker(LatLong ll) {
        System.out.println("createAndFocusOnUserLocationMarker");
        map.clearMarkers();
        updateMarkers(mainApp.getRestaurants());
        try {
            // add new marker to map on correct location & zoom in
            map.addMarkers(Collections.singletonList(createUserLocationMarker()));
            focusMapOnLocation(ll);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ADDRESS NOT FOUND");
            alert.setHeaderText("No search results");
            alert.setContentText("Try again! Preferred format on address is 'Streetname 1 City'");
            alert.show();
        }
    }

    /**
     * Focuses mapview to given location
     *
     * @param ll User location as LatLong object
     */
    public void focusMapOnLocation(LatLong ll) {
        System.out.println("focusMapOnLocation");
        mapView.setCenter(ll.getLatitude(), ll.getLongitude());
        mapView.setZoom(15);
    }

    /**
     * Focus map on a certain restaurant
     *
     * @param restaurant - Restaurant on which to focus the map on
     */
    public void focusMapOnRestaurant(Restaurant restaurant) {
        System.out.println("focusMapOnRestaurant");
        if (restaurant != null) {
            mapView.setCenter(restaurant.getLat(), restaurant.getLng());
            mapView.setZoom(15);
        }
    }

    public static String formatString(String s) {
        System.out.println("formatString");
        String[] words = s.replaceAll("\\s+", " ").trim().split(" ");
        String newString = "";
        for (String word : words) {
            if (StringUtils.isStrictlyNumeric(word)) {
                newString += word + " ";
                continue;
            }
            for (int i = 0; i < word.length(); i++) {
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
        newString = newString.trim();
        return newString;
    }
}
