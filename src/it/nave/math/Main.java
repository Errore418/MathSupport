package it.nave.math;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Home.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Welcome");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false); //FIXME computazionalmente più efficiente di rendere gli elementi dinamici kappa
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
