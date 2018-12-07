package it.nave.math.controller;

import java.io.IOException;

import it.nave.math.service.ScomponiService;
import it.nave.math.support.Tool;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ScomponiController {
	@FXML
	private TextField input;
	@FXML
	private TextArea result;

	@FXML
	private void initialize() {
	}

	@FXML
	private void backToHome() throws IOException {
		Tool.startNewStage("Home.fxml", "Welcome", input);
	}

	@FXML
	private void scomponi() throws IOException {
		long n = 0;
		try {
			n = Long.parseLong(input.getText());
			if (n < 0) {
				inputNotValid();
			}
		} catch (NumberFormatException e) {
			inputNotValid();
		}
		ScomponiService service = new ScomponiService(n, result);
		service.scomponi();
	}

	private void inputNotValid() {
		Alert alert = new Alert(AlertType.ERROR, "La stringa inserita non è un intero naturale valido");
		alert.setTitle("ERRORE");
		alert.setHeaderText("Errore nel parsing");
		alert.showAndWait();
	}

}
