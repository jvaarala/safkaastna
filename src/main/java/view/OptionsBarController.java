package view;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import main.MainApp;

/**
 * OptionsBarController controls connection and help functions, which are used by connection button and help button.
 */
public class OptionsBarController {

    /**
     * RestaurantsDAO is the database service class Connect and Help buttons.
     */
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
     * Used to give a reference to the mainApp for this controller.
     * Should be done after controller initialisation, before using any of its functions.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
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
     * Help button function that pops help dialog with instructions.
     */
    @FXML
    public void Help() {
        if (languange.equals("SWE")) {
            popupInfo(helpSE, "SafkaaSTNA Hjälp");
        } else if (languange.equals("FI")) {
            popupInfo(helpFI, "SafkaaSTNA Apu");
        } else {
            popupInfo(helpEN, "SafkaaSTNA Help");
        }
    }

    @FXML
    public void Refresh(ActionEvent actionEvent) {
        mainApp.updateRestaurantsFromDb();
        mainApp.getMainViewControl().updateView(mainApp.getRestaurants());

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
