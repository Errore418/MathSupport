package it.nave.math.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import it.nave.math.support.Tool;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class EratosteneController {
	private static final int LABEL_SIZE = 50;
	private static final int COLUMNS_NUM = 10;
	private static final String STYLE = "-fx-border-style: solid inside;" + "-fx-border-insets: 1;"
			+ "-fx-border-radius: 5;" + "-fx-border-color: black;";
	private static final String STYLE_PRIME = STYLE + "-fx-background-color: green;";
	private static final String STYLE_DELETE = STYLE + "-fx-background-color: red;";
	private static final int DEFAULT_NUM = 50;
	private static final double BUILD_FRAME_DURATION_DEFAULT = 50;
	private static final double SETACCIO_FRAME_DURATION_DEFAULT = 500;
	private static final int PRIME_PER_ROW = 10;

	@FXML
	private GridPane gridpane;
	@FXML
	private ScrollPane scrollpane;
	@FXML
	private TextField input;
	@FXML
	private TextArea output;
	@FXML
	private Button setaccia;
	@FXML
	private Button indietro;

	private int n;
	private int outputRow = 1;

	private Set<Integer> deletes = new HashSet<>();

	@FXML
	private void initialize() {
		scrollpane.setPrefViewportWidth(COLUMNS_NUM * LABEL_SIZE);
		populateGridPane().play();
	}

	@FXML
	private void backToHome() throws IOException {
		Tool.startNewStage("Home.fxml", "Welcome", gridpane);
	}

	@FXML
	private void generaGriglia() {
		populateGridPane().play();
	}

	@FXML
	private void setaccia() {
		Timeline populate = null;
		if (parseTextInputControl(input) != gridpane.getChildren().size()) {
			populate = populateGridPane();
		}
		Timeline setaccio = buildSetaccioTimeline();
		if (populate != null) {
			populate.setOnFinished(e -> {
				setaccio.play();
			});
			populate.play();
		} else {
			setaccio.play();
		}
	}

	private Timeline buildSetaccioTimeline() {
		double setaccioFrameDuration = (n > DEFAULT_NUM) ? SETACCIO_FRAME_DURATION_DEFAULT * DEFAULT_NUM / n
				: SETACCIO_FRAME_DURATION_DEFAULT;
		long countFrame = 0;
		Timeline timeline = new Timeline();
		addKeyFrame(timeline, countFrame++, setaccioFrameDuration, e -> {
			setaccia.setDisable(true);
			indietro.setDisable(true);
			clean();
		});

		deletes.clear();
		outputRow = 1;
		int num = 0;
		while ((num = searchFirstNum()) != -1) {
			final int numFinal = num;
			addKeyFrame(timeline, countFrame++, setaccioFrameDuration, e -> {
				ensureVisible(numFinal);
				markAsPrime(numFinal);
			});

			if (num <= Math.sqrt(n)) {
				for (int i = num + 1; i <= n; i++) {
					if (i % num == 0 && !deletes.contains(i)) {
						final int iFinal = i;
						addKeyFrame(timeline, countFrame++, setaccioFrameDuration, e -> {
							ensureVisible(iFinal);
							markAsDelete(iFinal);
						});
						deletes.add(i);
					}
				}
			}
			deletes.add(num++);
		}

		addKeyFrame(timeline, countFrame++, setaccioFrameDuration, e -> {
			setaccia.setDisable(false);
			indietro.setDisable(false);
		});
		return timeline;
	}

	private void clean() {
		gridpane.getChildren().stream().forEach(c -> c.setStyle(STYLE));
		output.clear();
	}

	private int searchFirstNum() {
		for (int i = 2; i <= n; i++) {
			if (!deletes.contains(i)) {
				return i;
			}
		}
		return -1;
	}

	private void markAsPrime(int x) {
		boolean newLine = false;
		if (output.getText().chars().filter(ch -> ch == ',').count() == (PRIME_PER_ROW - 1) * outputRow) {
			output.appendText("\n");
			newLine = true;
			outputRow++;
		}
		if (!output.getText().isEmpty() && !newLine) {
			output.appendText(", ");
		}
		output.appendText(Integer.toString(x));
		Label label = (Label) gridpane.getChildren().get(x - 1);
		label.setStyle(STYLE_PRIME);
	}

	private void markAsDelete(int x) {
		Label label = (Label) gridpane.getChildren().get(x - 1);
		label.setStyle(STYLE_DELETE);
	}

	private Timeline populateGridPane() {
		n = parseTextInputControl(input);
		double buildFrameDuration = (n > DEFAULT_NUM) ? BUILD_FRAME_DURATION_DEFAULT * DEFAULT_NUM / n
				: BUILD_FRAME_DURATION_DEFAULT;
		long countFrame = 0;
		Timeline timeline = new Timeline();
		addKeyFrame(timeline, countFrame++, buildFrameDuration, e -> {
			setaccia.setDisable(true);
			indietro.setDisable(true);
		});
		addKeyFrame(timeline, countFrame++, buildFrameDuration, e -> gridpane.getChildren().clear());
		VBox useless = new VBox();
		useless.setStyle(STYLE);
		useless.setMinSize(LABEL_SIZE, LABEL_SIZE);
		useless.setPrefSize(LABEL_SIZE, LABEL_SIZE);
		useless.setMaxSize(LABEL_SIZE, LABEL_SIZE);
		addKeyFrame(timeline, countFrame++, buildFrameDuration, e -> {
			gridpane.add(useless, 0, 0);
		});
		int column = 1;
		int row = 0;
		for (long i = 2; i <= n; i++) {
			Label label = new Label(Long.toString(i));
			label.setMinSize(LABEL_SIZE, LABEL_SIZE);
			label.setPrefSize(LABEL_SIZE, LABEL_SIZE);
			label.setMaxSize(LABEL_SIZE, LABEL_SIZE);
			label.setAlignment(Pos.CENTER);
			label.setStyle(STYLE);
			final int columnFinal = column;
			final int rowFinal = row;
			addKeyFrame(timeline, countFrame++, buildFrameDuration, e -> {
				gridpane.add(label, columnFinal, rowFinal);
			});
			column++;
			if (column % COLUMNS_NUM == 0) {
				column = 0;
				row++;
			}
		}
		addKeyFrame(timeline, countFrame++, buildFrameDuration, e -> {
			setaccia.setDisable(false);
			indietro.setDisable(false);
		});
		return timeline;
	}

	private void ensureVisible(int num) {
		Node node = gridpane.getChildren().get(num - 1);
		double width = scrollpane.getContent().getBoundsInLocal().getWidth();
		double height = scrollpane.getContent().getBoundsInLocal().getHeight();
		double x = (num < n / 2) ? node.getBoundsInParent().getMinX() : node.getBoundsInParent().getMaxX();
		double y = (num < n / 2) ? node.getBoundsInParent().getMinY() : node.getBoundsInParent().getMaxY();
		scrollpane.setVvalue(y / height);
		scrollpane.setHvalue(x / width);
	}

	private void addKeyFrame(Timeline timeline, long numFrame, double millis, EventHandler<ActionEvent> e) {
		KeyFrame frame = buildKeyFrame(numFrame, e, millis);
		timeline.getKeyFrames().add(frame);
	}

	private KeyFrame buildKeyFrame(long numFrame, EventHandler<ActionEvent> e, double millis) {
		return new KeyFrame(Duration.millis(millis * numFrame), e);
	}

	private int parseTextInputControl(TextInputControl input) {
		int result = 0;
		try {
			result = Integer.parseInt(input.getText());
			if (result <= 0) {
				inputNotValid();
			}
		} catch (NumberFormatException e) {
			inputNotValid();
		}
		return result;
	}

	private void inputNotValid() {
		Alert alert = new Alert(AlertType.ERROR, "La stringa inserita non è un intero positivo valido");
		alert.setTitle("ERRORE");
		alert.setHeaderText("Errore nel parsing");
		alert.showAndWait();
	}

}
