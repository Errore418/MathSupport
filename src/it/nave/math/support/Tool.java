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
import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Tool {
	private static String FACTORS_TEMPLATE = "%d^%d";
	private static String FACTORS_TEMPLATE_SHORT = "%d";

	public static Stage setStandardStage(Stage stage, String fxmlFile, String title) throws IOException {
		Parent root = FXMLLoader.load(Tool.class.getClassLoader().getResource(fxmlFile));
		stage.setTitle(title);
		stage.setScene(new Scene(root));
//		stage.setResizable(false); // FIXME computazionalmente più efficiente di rendere gli elementi dinamici
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

}
