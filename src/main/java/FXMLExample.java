import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
// import com.lynden.gmapsfx.javascript.object.MapType;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import model.Restaurant;
import model.RestaurantDAO;


public class FXMLExample extends Application {

    public static void main(String[] args) {
        Application.launch(FXMLExample.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
//        fixCoordinates();
        Parent root = FXMLLoader.load(getClass().getResource("fxml_example.fxml"));

        stage.setTitle("FXML Welcome");
        stage.setScene(new Scene(root));
        stage.show();
    }

    private boolean fixCoordinates() {
        boolean success = false;
        RestaurantDAO dao = new RestaurantDAO();

        // korjattavat id 49, 106, 110, 142, 161, 285
        Restaurant r = new Restaurant(285, 22.279710);
        dao.updateRestaurant(r);
        
        return success;
    }
}