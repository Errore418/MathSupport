package it.nave.math.support;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Tool {

	public static Stage setStandardStage(Stage stage, String fxmlFile, String title) throws IOException {
		Parent root = FXMLLoader.load(Tool.class.getClassLoader().getResource(fxmlFile));
		stage.setTitle(title);
		stage.setScene(new Scene(root));
		stage.setResizable(false); // FIXME computazionalmente più efficiente di rendere gli elementi dinamici
		return stage;
	}

	public static void startNewStage(String fxml, String title, Node node) throws IOException {
		Stage stage = Tool.setStandardStage(new Stage(), fxml, title);
		stage.show();
		Tool.closeStage(node);
	}

	public static void closeStage(Node node) {
		Stage stage = (Stage) node.getScene().getWindow();
		stage.close();
	}

}
