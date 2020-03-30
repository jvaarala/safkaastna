package view;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import com.sun.swing.internal.plaf.synth.resources.synth_sv;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import main.MainApp;
import model.RestaurantDAO;
import model.Restaurant;

/**
 * OptionsBarController controls connection and help functions, which are used by connection button and help button.
 */
public class OptionsBarController {

    /**
     * RestaurantsDAO is the database service class Connect and Help buttons.
     */
    private RestaurantDAO db_data;
    private List<Restaurant> restaurantsFromDb;
    private MainApp mainApp;
    String languange = "ENG";
    private String helpEN = "doesn´t work";
    private String helpFI = "ei toimi";
    private String helpSE = "fungerar inte";
    Properties props = new Properties(settings());

    @FXML
	private void mapLocation() {

	}

    private Properties settings() {
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream("./src/main/resources/settings/language.properties.txt")) {

            prop.load(input);
            helpEN = prop.getProperty("helpEN");
            helpFI = prop.getProperty("helpFI");
            helpSE = prop.getProperty("helpSE");
            System.out.println(prop.getProperty("helpEN"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }


/*
	private String helpTextENG = "SafkaaSTNA is a Java Desktop application desingned for students searching for student restaurants. \n"
			+ " \n"
			+ "With the this appliaction you can:\n"
			+ "- See all available student restaurants in Finland:\n"
			+ "		- Default view on app start\n"
			+ "- Search for restaurants near your or any address\n"
			+ "		- Turn \"Restaurant Filter\" off (default)\n"
			+ "		- Write address, then press \"Find\" \n"
			+ "- Search for restaurants by name \n"
			+ "		- Turn \"Restaurant Filter\" on\n"
			+ "		- Write restaurant name\n"
			+ "- See additional information on each restaurant \n"
			+ "		- Click a restaurants red marker";
*/
/*
	private String helpTextFI = "SafkaaSTNA on Java Työpöytä applikaatio suuniteltu opiskelijoille jotka etsivät opiskelija ravintolaa. \n"
			+ " \n"
			+ "With the this appliaction you can:\n"
			+ "- See all available student restaurants in Finland:\n"
			+ "		- Default view on app start\n"
			+ "- Search for restaurants near your or any address\n"
			+ "		- Turn \"Restaurant Filter\" off (default)\n"
			+ "		- Write address, then press \"Find\" \n"
			+ "- Search for restaurants by name \n"
			+ "		- Turn \"Restaurant Filter\" on\n"
			+ "		- Write restaurant name\n"
			+ "- See additional information on each restaurant \n"
			+ "		- Click a restaurants red marker";
*/
	/*
	private String helpTextSWE = "SafkaaSTNA är en Java Desktop applikation desingnad för studerande som lätar efter  studie restauranger. \n"
			+ " \n"
			+ "Med denna applikation kan du:\n"
			+ "- Se all studie restauranger tillängliga i Finland:\n"
			+ "		- Välja  utsikten när applikationen startas\n";

*/


    /**
     * Used for mockit JUnit tests.
     *
     * @param db_data
     */
    public void setDB_DATA(RestaurantDAO db_data) {
        this.db_data = db_data;
    }

    /**
     * Pop-up template for problem events
     *
     * @param text
     * @param title
     */
    private void popupAlert(String text, String title) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Error occured while fetching data");
        alert.setContentText(text);
        alert.show();
    }

    /**
     * Pop-up template for help/information dialogs
     *
     * @param text
     * @param title
     */
    private void popupInfo(String text, String title) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.show();
    }

    /**
     * Initiates a fresh connection
     */
    public void initConnection() {
        this.db_data = new RestaurantDAO();
    }

    /**
     * Fetches restaurant data from database and gives it to MainApp.
     *
     * @return boolean - true if fetch succeeded, false if not.
     */
    public boolean getData() {
        try {
            restaurantsFromDb = db_data.readRestaurants();
            mainApp.setRestaurants(restaurantsFromDb);
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }


    /**
     * Initiates a fresh connection and gets all restaurants from database
     *
     * @return boolean - true if restaurants updated, false if failed
     */
    @FXML
    public boolean getRestaurants() {
        initConnection();
        boolean success = getData();
        if (!success) {
            popupAlert("NO CONNECTION TO DATABASE, FAILED TO FETCH RESTAURANTS", "CONNECTION ERROR");
            return false;
        }
        return true;

    }

    /**
     * Initiates new connection and updates rendered map
     *
     * @return boolean - true if succeeded, false if failed
     */
    @FXML
    public boolean refresh() {
        boolean gotRestaurants = getRestaurants();
        if (!gotRestaurants) {
            return false;
        }

        mainApp.updateMap();
        return true;
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
     * Help button function that pops help dialog with instructions.
     */
    @FXML
    public void help() {
        if (languange.equals("SWE")) {
            popupInfo(helpSE, "SafkaaSTNA Hjälp");
        } else if (languange.equals("FI")) {
            popupInfo(helpFI, "SafkaaSTNA Apu");
        } else {
            popupInfo(helpEN, "SafkaaSTNA Help");
        }
    }


    @FXML
    public void changeFI() {
        languange = "FI";

    }

    @FXML
    public void changeENG() {
        languange = "ENG";

    }

    @FXML
    public void changeSWE() {
        languange = "SWE";

    }
}
