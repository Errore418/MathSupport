package it.nave.math.controller;

import java.io.IOException;

import it.nave.math.service.ScomponiService;
import it.nave.math.support.Tool;
import javafx.fxml.FXML;
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
			n = Tool.parseLongTextInputControl(input, -1, Tool.MESSAGE_GREATER_ZERO);
		} catch (NumberFormatException e) {
			Tool.inputNotValid(Tool.MESSAGE_GREATER_ZERO);
			result.clear();
			return;
		}
		ScomponiService service = new ScomponiService(n, result);
		service.scomponi();
	}

}
