package it.nave.math.support;

import java.io.IOException;
import java.util.Map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;

public class Tool {
	private static String TEMPLATE = "%d^%d";
	private static String TEMPLATE_SHORT = "%d";

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

	public static void writeFactors(Map<Long, Integer> factors, TextInputControl output) {
		output.setText("");
		factors.entrySet().stream().sorted((a, b) -> a.getKey().compareTo(b.getKey())).map(
				e -> String.format((e.getValue() > 1) ? TEMPLATE : TEMPLATE_SHORT, e.getKey(), e.getValue()) + "\n")
				.forEach(output::appendText);
	}

}
