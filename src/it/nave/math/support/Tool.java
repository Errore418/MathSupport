package it.nave.math.support;

import java.io.IOException;
import java.util.Map;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Tool {
	public static final String MESSAGE_POSITIVE = "La stringa inserita non � un intero positivo valido";
	public static final String MESSAGE_GREATER_ZERO = "La stringa inserita non � un intero naturale valido";

	private static final String FACTORS_TEMPLATE = "%d^%d";
	private static final String FACTORS_TEMPLATE_SHORT = "%d";

	public static Stage setStandardStage(Stage stage, String fxmlFile, String title) throws IOException {
		Parent root = FXMLLoader.load(Tool.class.getClassLoader().getResource(fxmlFile));
		stage.setTitle(title);
		stage.setScene(new Scene(root));
//		stage.setResizable(false); // FIXME computazionalmente pi� efficiente di rendere gli elementi dinamici
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
		output.clear();
		factors.entrySet().stream().sorted((a, b) -> a.getKey().compareTo(b.getKey()))
				.map(e -> String.format((e.getValue() > 1) ? FACTORS_TEMPLATE : FACTORS_TEMPLATE_SHORT, e.getKey(),
						e.getValue()) + "\n")
				.forEach(output::appendText);
	}

	public static void addKeyFrame(Timeline timeline, long numFrame, double millis, EventHandler<ActionEvent> e) {
		KeyFrame frame = buildKeyFrame(numFrame, e, millis);
		timeline.getKeyFrames().add(frame);
	}

	public static KeyFrame buildKeyFrame(long numFrame, EventHandler<ActionEvent> e, double millis) {
		return new KeyFrame(Duration.millis(millis * numFrame), e);
	}

	public static long parseLongTextInputControl(TextInputControl input, int min, String msg) {
		long result = 0;
		result = Long.parseLong(input.getText());
		if (result <= min) {
			throw new NumberFormatException();
		}
		return result;
	}

	public static int parseIntTextInputControl(TextInputControl input, int min, String msg) {
		int result = 0;
		result = Integer.parseInt(input.getText());
		if (result <= min) {
			throw new NumberFormatException();
		}
		return result;
	}

	public static void inputNotValid(String msg) {
		Alert alert = new Alert(AlertType.ERROR, msg);
		alert.setTitle("ERRORE");
		alert.setHeaderText("Errore nel parsing");
		alert.showAndWait();
	}

}
