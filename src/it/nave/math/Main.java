package it.nave.math;

import java.io.IOException;
import java.net.URL;

import it.nave.math.support.Tool;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws IOException {
		Tool.setStandardStage(primaryStage, "Home.fxml", "Welcome");
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
