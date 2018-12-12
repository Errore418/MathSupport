package it.nave.math.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import it.nave.math.service.ScomponiService;
import it.nave.math.support.Tool;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MCDmcmController {
	@FXML
	private TextField input1;
	@FXML
	private TextField input2;
	@FXML
	private TextArea scomposizione1;
	@FXML
	private TextArea scomposizione2;
	@FXML
	private TextArea scomposizioneMCD;
	@FXML
	private TextArea scomposizionemcm;
	@FXML
	private TextField MCD;
	@FXML
	private TextField mcm;

	@FXML
	private void initialize() {
	}

	@FXML
	private void backToHome() throws IOException {
		Tool.startNewStage("Home.fxml", "Welcome", input1);
	}

	@FXML
	private void trova() {
		long n1 = 0;
		long n2 = 0;
		try {
			n1 = Tool.parseLongTextInputControl(input1, 0, Tool.MESSAGE_POSITIVE);
			n2 = Tool.parseLongTextInputControl(input2, 0, Tool.MESSAGE_POSITIVE);
		} catch (NumberFormatException e) {
			Tool.inputNotValid(Tool.MESSAGE_POSITIVE);
			scomposizione1.clear();
			scomposizione2.clear();
			scomposizioneMCD.clear();
			scomposizionemcm.clear();
			MCD.clear();
			mcm.clear();
			return;
		}
		ScomponiService scomponi1 = new ScomponiService(n1, scomposizione1);
		ScomponiService scomponi2 = new ScomponiService(n2, scomposizione2);
		Map<Long, Integer> factors1 = scomponi1.scomponi();
		Map<Long, Integer> factors2 = scomponi2.scomponi();
		Map<Long, Integer> factorsMCD = Tool.calculateMCD(factors1, factors2);
		Map<Long, Integer> factorsmcm = Tool.calculatemcm(factors1, factors2);
		Tool.writeFactors(factorsMCD, scomposizioneMCD);
		Tool.writeFactors(factorsmcm, scomposizionemcm);
		Tool.calculateAndWrite(factorsMCD, Optional.of(MCD));
		Tool.calculateAndWrite(factorsmcm, Optional.of(mcm));
	}
}
