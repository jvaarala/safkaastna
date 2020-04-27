package view;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
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
import javafx.util.Callback;
import main.MainApp;
import model.Restaurant;
import model.SearchLogic;
import netscape.javascript.JSObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

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
    private List<String> cities;
    private double[] defaultCity;

    private MainApp mainApp;

    /**
     * Used to give reference to GoogleMapView and GoogleMap (as mocks)
     * Used only in tests
     * @param mapView
     * @param map
     */
    void setGoogleMapStuff(GoogleMapView mapView, GoogleMap map) {
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
     * Adds map to mapcontainer and sets api key for google api calls.
     * This is overridden method from FXMLLoader.
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
     *
     * Calls @see #updateMainView(List<Restaurant> restaurants) at end
     */
    @Override
    public void mapInitialized() {
        String city = mainApp.getDefaultBundle().getString("Default");
        defaultCity = search.stringToDouble(city);

        MapOptions mapOptions = new MapOptions();
        mapOptions.overviewMapControl(false).panControl(false).rotateControl(false).scaleControl(false)
                .streetViewControl(false).zoomControl(false).zoom(12).mapTypeControl(false);
        AnchorPane.setTopAnchor(mapView, 0.0);
        AnchorPane.setRightAnchor(mapView, 0.0);
        AnchorPane.setBottomAnchor(mapView, 0.0);
        AnchorPane.setLeftAnchor(mapView, 0.0);
        map = mapView.createMap(mapOptions);
        map.setCenter(new LatLong(60.192059, 24.945831));

        updateMainView(mainApp.getRestaurants());
    }

    /**
     * Update ListView and map elements according to a list of restaurants
     *
     * @param restaurants - List of restaurants to be iterated through
     *                    Names are set on ListView and Markers are set on map on restaurants location
     */
    void updateMainView(List<Restaurant> restaurants) {
        updateListView(restaurants);
        updateMarkers(restaurants);
    }


    /**
     * Update Main Screen ListView according to a list of restaurants
     * Used to update list when filter value is used to filter restaurants
     * Always called together with {@link #updateMarkers(List)}
     *
     * @param restaurants List of restaurants (may filtered with filter value)
     */
    private void updateListView(List<Restaurant> restaurants) {
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
                        mainApp.getSidebarControl().showRestaurantInfo(restaurant);
                        break;
                    }
                }
            }
        });

        listViewNames.setCellFactory(list -> new RestaurantNameCell());
        listViewNames.setItems(items);
    }

    /**
     * Used to update Markers on map according to a list of restaurants
     * Used to update list when filter value is used to filter restaurants markers on map
     * Always called together with {@link #updateListView(List)}
     *
     * @param restaurants
     */
    private void updateMarkers(List<Restaurant> restaurants) {
        List<Marker> restaurantMarkers = new ArrayList<>();
        map.clearMarkers();
        if (mainApp.getUserLocation() != null) {
            restaurantMarkers.add(createUserLocationMarker());
        }

        for (Restaurant restaurant : restaurants) {
            LatLong tempLatLong = new LatLong(restaurant.getLat(), restaurant.getLng());
            MarkerOptions markerOptions = new MarkerOptions();
            // if nearest restaurant is known, use different marker icon
            if (nearest != null && tempLatLong.getLatitude() == nearest.getLat() && tempLatLong.getLongitude() == nearest.getLng()) {
                markerOptions
                        .position(tempLatLong)
                        .icon("https://users.metropolia.fi/~katriras/OTP1/kela_nearest.gif")
                ;
            } else {
                markerOptions.position(tempLatLong);
                markerOptions.icon("https://www.kela.fi/documents/10180/24327790/Logo_Tunnus_rgb.gif/7a417c63-36b0-4ef0-ad4c-3e29fb5196e9?t=1553685514309");
            }
            Marker tempMarker = new Marker(markerOptions);
            map.addUIEventHandler(tempMarker, UIEventType.click, (JSObject obj) -> {
                mainApp.getSidebarControl().showRestaurantInfo(restaurant);
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
            // Default cordinates read from file
            mapView.setCenter(defaultCity[0], defaultCity[1]);
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
            updateMainView(foundRestaurants);
        }
    }

    /**
     * Event handler for search button
     * If toggle button for restaurant filtering is not selected, will continue to fetchCoordinates() &
     * focusMapOnCoordinate()
     *
     * @param event Button click
     */
    @FXML
    protected void handleSearchButton(ActionEvent event) {
        if (!filterToggleButton.isSelected()) {
            map.clearMarkers();
            updateMarkers(mainApp.getRestaurants());
            mainApp.setUserLocation(search.fetchGoogleCoordinates(textInSearchField));
            focusMapOnLocation(mainApp.getUserLocation());
//            createAndFocusOnUserLocationMarker(mainApp.getUserLocation());
            mainApp.getSidebarControl().setUserLocationText(formatString(textInSearchField));
        }
    }

    /**
     * Event handler for searching the nearest restaurant
     * If userLocation is not set, modal pop up window is created to ask for user location
     * When user location is known, calls to {@link #findNearest(LatLong)}
     * and {@link #updateMarkers(List)} to change the marker icon of nearest restaurant
     *
     * @param event Button click
     */
    @FXML
    protected void handleLocateNearestButton(ActionEvent event) {
        // if user location is not set, but find nearest button is pressed, ask for user location
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
//                        createAndFocusOnUserLocationMarker(mainApp.getUserLocation());
                        nearest = findNearest(mainApp.getUserLocation());
                        updateMarkers(mainApp.getRestaurants());
                    });
        } else {
            nearest = this.findNearest(mainApp.getUserLocation());
        }
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
        if (filterToggleButton.isSelected()) {
            searchButton.setDisable(true);
            filterToggleButton.setText(mainApp.getBundle().getString("filtering"));
            List<Restaurant> foundRestaurants = search.filter(mainApp.getRestaurants(), searchTextBox.getText());
            updateMainView(foundRestaurants);
        } else {
            searchButton.setDisable(false);
            filterToggleButton.setText(mainApp.getBundle().getString("filterToggle"));
            updateMainView(mainApp.getRestaurants());
        }
        searchTextBox.requestFocus();
    }

    /**
     * Used to find nearest restaurant to user location
     *
     * @param userLocation user location
     * @return Restaurant that is nearest to user location
     */
    private Restaurant findNearest(LatLong userLocation) {
        Restaurant nearest = search.findNearestRestaurant(mainApp.getRestaurants(), userLocation);
        System.out.println(nearest);
        mainApp.getSidebarControl().showRestaurantInfo(nearest);
        return nearest;
    }

    /**
     * Used to zoom map according user location and nearest restaurant
     *
     * @param userLocation LatLong object indicating user location
     * @param nearest Restaurant object that is nearest to userLocation
     */

    private void fitBounds(LatLong userLocation, Restaurant nearest) {
        double distance = search.calculateDistanceToNearest(nearest, userLocation);
        Circle circle = new Circle();
        circle.setRadius(distance);
        circle.setCenter(new LatLong(nearest.getLat(), nearest.getLng()));
        map.fitBounds(circle.getBounds());
        System.out.println("zoom level " + map.getZoom());
    }

    /**
     * Used to create marker for user location with custom icon.
     * Called from {@link #updateMarkers(List)}
     *
     * @return new marker to be added to marker list
     */

    public Marker createUserLocationMarker() {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions
                .position(mainApp.getUserLocation())
                .icon("https://users.metropolia.fi/~katriras/OTP1/map-marker.png")
                .animation(Animation.BOUNCE)
        ;
        return new Marker(markerOptions);
    }

    /**
     * Focuses Map View to given location
     * if latLong is null, alert is given
     *
     * @param ll User location as LatLong object
     */
    void focusMapOnLocation(LatLong ll) {
        if (ll != null) {
            updateMarkers(mainApp.getRestaurants());
            System.out.println("focusMapOnLocation");
            mapView.setCenter(ll.getLatitude(), ll.getLongitude());
            mapView.setZoom(15);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("PLACE NOT FOUND");
            alert.setHeaderText("No search results");
            alert.setContentText("Try again! Preferred format on address is 'Streetname 1 City'");
            alert.show();
        }
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

    /**
     * Used to auto capitalize user input address (for example "streEt name 1 ciTY")
     *
     * @param s user input (address or place name)
     * @return modified string for example "Street name 1 City"
     */
    static String formatString(String s) {
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

    public void setTexts(ResourceBundle bundle) {
        searchButton.setText(bundle.getString("search"));
        nearestButton.setText(bundle.getString("nearest"));
        if (filterToggleButton.isSelected()) {
            filterToggleButton.setText(bundle.getString("filtering"));
        } else {
            filterToggleButton.setText(bundle.getString("filterToggle"));
        }
    }

    public static class RestaurantNameCell extends ListCell<String> {
        RestaurantNameCell() {
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            int listViewNameLengthMax = 24;
            super.updateItem(item, empty);
            if (item != null && item.length() > listViewNameLengthMax) {
                setText(item.substring(0, listViewNameLengthMax - 3) + "...");
            } else setText(item);
        }
    }
}
