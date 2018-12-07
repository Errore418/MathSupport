package it.nave.math.controller;

import java.io.IOException;

import it.nave.math.support.Tool;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class HomeController {
	@FXML
	private ToggleGroup homeGroup;

	@FXML
	private void homeButtonAction() throws IOException {
		RadioButton selected = (RadioButton) homeGroup.getSelectedToggle();
		switch (selected.getId()) {
		case "euclide":
			startNewStage("Euclide.fxml", "Algoritmo di Euclide", selected);
			break;
		case "eratostene":
			startNewStage("Eratostene.fxml", "Crivello di Eratostene", selected);
			break;
		case "scomposizione":
			startNewStage("Scomposizione.fxml", "Scomposizione in fattori primi", selected);
			break;
		case "MCDmcm":
			startNewStage("MCDmcm.fxml", "MCD e mcm", selected);
			break;
		}
	}

	@FXML
	private void initialize() {
	}

	private void startNewStage(String fxml, String title, Node selected) throws IOException {
		Stage stage = Tool.setStandardStage(new Stage(), fxml, title);
		stage.show();
		Tool.closeStage(selected);
	}

}
