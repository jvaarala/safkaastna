package view;

import java.io.IOException;
import java.net.URL;

import javafx.application.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class SafkaaSaatana extends Application {
	public static void main(String[] args) {
	    launch(args);
	}
	
	@FXML
	private String perkele = "Huutava vääryys";
	
	@FXML
	private int count = 666;
	
	@FXML
	private String textTest;
	
	
	private Stage primaryStage;
	private BorderPane aaaaaa;
	
	@FXML
	private TextField kys;
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		
		FXMLLoader loader = new FXMLLoader();
		
		URL testa = SafkaaSaatana.class.getResource("SafkaaSaatana.fxml");
		System.out.println(testa);
		loader.setLocation(testa);
		try {
			aaaaaa = (BorderPane) loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		primaryStage.setScene(new Scene(aaaaaa));
        primaryStage.show();
	}
	
    //copy your old code and stuff like that. Structure and things available there without much research

	
	//this shit if you assign action through code --> just function if you do it through FXML / FXMLLoader();
	@FXML
	public EventHandler<ActionEvent> testAct() {
		EventHandler<ActionEvent> a = new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	            System.out.println("Hello World!");
	            String bertta = kys.getCharacters().toString();
	            System.out.println("Hello World! "+bertta);
	        }
		};
		return a;
	}
	/*
    btn.setOnAction(new EventHandler<ActionEvent>() {

        @Override
        public void handle(ActionEvent event) {
            System.out.println("Hello World!");
        }
    });
	*/
	@FXML
	public void funkyTest() {
		System.out.println("Saatana Saatana Saatana");
	}
	
	//Show a table with data?
	
	@FXML
	public int huutonauraa() {
		System.out.println("kyl kyl kyl kyl "+perkele);
		this.count++;
		return count;
	}
	
	@FXML void showCount() {
		System.out.println("COuntti on "+count);
	}
	
	@FXML void testest() {
		textTest = kys.getCharacters().toString();
		System.out.println("AA: "+textTest);
	}
}

