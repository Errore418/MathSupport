package it.nave.math.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import it.nave.math.support.Tool;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class EratosteneController {
	private static final int LABEL_SIZE = 50;
	private static final int MIN_LABEL_SIZE = 25;
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
		int current = 0;
		try {
			current = Tool.parseIntTextInputControl(input, 0, Tool.MESSAGE_POSITIVE);
		} catch (NumberFormatException e) {
			Tool.inputNotValid(Tool.MESSAGE_POSITIVE);
			return;
		}
		if (current != gridpane.getChildren().size()) {
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
		Tool.addKeyFrame(timeline, countFrame++, setaccioFrameDuration, e -> {
			setaccia.setDisable(true);
			indietro.setDisable(true);
			clean();
		});

		deletes.clear();
		outputRow = 1;
		int num = 0;
		while ((num = searchFirstNum()) != -1) {
			final int numFinal = num;
			Tool.addKeyFrame(timeline, countFrame++, setaccioFrameDuration, e -> {
				ensureVisible(numFinal);
				markAsPrime(numFinal);
			});

			if (num <= Math.sqrt(n)) {
				for (int i = num + 1; i <= n; i++) {
					if (i % num == 0 && !deletes.contains(i)) {
						final int iFinal = i;
						Tool.addKeyFrame(timeline, countFrame++, setaccioFrameDuration, e -> {
							ensureVisible(iFinal);
							markAsDelete(iFinal);
						});
						deletes.add(i);
					}
				}
			}
			deletes.add(num++);
		}

		Tool.addKeyFrame(timeline, countFrame++, setaccioFrameDuration, e -> {
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
		VBox vbox = (VBox) gridpane.getChildren().get(x - 1);
		vbox.setStyle(STYLE_PRIME);
	}

	private void markAsDelete(int x) {
		VBox vbox = (VBox) gridpane.getChildren().get(x - 1);
		vbox.setStyle(STYLE_DELETE);
	}

	private Timeline populateGridPane() {
		n = Tool.parseIntTextInputControl(input, 0, Tool.MESSAGE_POSITIVE);
		double buildFrameDuration = (n > DEFAULT_NUM) ? BUILD_FRAME_DURATION_DEFAULT * DEFAULT_NUM / n
				: BUILD_FRAME_DURATION_DEFAULT;
		long countFrame = 0;
		Timeline timeline = new Timeline();
		Tool.addKeyFrame(timeline, countFrame++, buildFrameDuration, e -> {
			setaccia.setDisable(true);
			indietro.setDisable(true);
		});
		Tool.addKeyFrame(timeline, countFrame++, buildFrameDuration, e -> gridpane.getChildren().clear());
		VBox useless = buildVBox(false, -1);
		Tool.addKeyFrame(timeline, countFrame++, buildFrameDuration, e -> {
			gridpane.add(useless, 0, 0);
		});
		int column = 1;
		int row = 0;
		for (long i = 2; i <= n; i++) {
			VBox vbox = buildVBox(true, i);
			final int columnFinal = column;
			final int rowFinal = row;
			Tool.addKeyFrame(timeline, countFrame++, buildFrameDuration, e -> {
				gridpane.add(vbox, columnFinal, rowFinal);
			});
			column++;
			if (column % COLUMNS_NUM == 0) {
				column = 0;
				row++;
			}
		}
		Tool.addKeyFrame(timeline, countFrame++, buildFrameDuration, e -> {
			setaccia.setDisable(false);
			indietro.setDisable(false);
		});
		return timeline;
	}

	private VBox buildVBox(boolean withLabel, long i) {		
		VBox result = new VBox();
		result.setStyle(STYLE);
		result.setMinSize(MIN_LABEL_SIZE, MIN_LABEL_SIZE);
		result.setPrefSize(Region.USE_COMPUTED_SIZE, LABEL_SIZE);
		result.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
		if (withLabel) {
			Label label = new Label(Long.toString(i));
			label.setAlignment(Pos.CENTER);
			result.getChildren().add(label);
			result.setAlignment(Pos.CENTER);
		}
		return result;
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

}
