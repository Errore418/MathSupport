package it.nave.math.controller;

import java.io.IOException;

import it.nave.math.support.Tool;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class HomeController {
	@FXML
	private ToggleGroup homeGroup;

	@FXML
	private void homeButtonAction() throws IOException {
		RadioButton selected = (RadioButton) homeGroup.getSelectedToggle();
		switch (selected.getId()) {
		case "euclide":
			Tool.startNewStage("Euclide.fxml", "Algoritmo di Euclide", selected);
			break;
		case "eratostene":
			Tool.startNewStage("Eratostene.fxml", "Crivello di Eratostene", selected);
			break;
		case "scomposizione":
			Tool.startNewStage("Scomposizione.fxml", "Scomposizione in fattori primi", selected);
			break;
		case "MCDmcm":
			Tool.startNewStage("MCDmcm.fxml", "MCD e mcm", selected);
			break;
		}
	}

	@FXML
	private void initialize() {
	}

}
