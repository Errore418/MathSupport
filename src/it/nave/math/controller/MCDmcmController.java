package it.nave.math.controller;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import it.nave.math.service.ScomponiService;
import it.nave.math.support.Tool;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

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
		long n1 = parseTextInputControl(input1);
		long n2 = parseTextInputControl(input2);
		ScomponiService scomponi1 = new ScomponiService(n1, scomposizione1);
		ScomponiService scomponi2 = new ScomponiService(n2, scomposizione2);
		Map<Long, Integer> factors1 = scomponi1.scomponi();
		Map<Long, Integer> factors2 = scomponi2.scomponi();
		Map<Long, Integer> factorsMCD = calculateMCD(factors1, factors2);
		Map<Long, Integer> factorsmcm = calculatemcm(factors1, factors2);
		Tool.writeFactors(factorsMCD, scomposizioneMCD);
		Tool.writeFactors(factorsmcm, scomposizionemcm);
		calculateAndWrite(factorsMCD, MCD);
		calculateAndWrite(factorsmcm, mcm);
	}

	private long parseTextInputControl(TextInputControl input) {
		long result = 0;
		try {
			result = Long.parseLong(input.getText());
			if (result <= 0) {
				inputNotValid();
			}
		} catch (NumberFormatException e) {
			inputNotValid();
		}
		return result;
	}

	private Map<Long, Integer> calculateMCD(Map<Long, Integer> factors1, Map<Long, Integer> factors2) {
		Map<Long, Integer> result = factors1.entrySet().stream().filter(e -> factors2.containsKey(e.getKey()))
				.map(e -> new SimpleEntry<>(e.getKey(), Math.min(e.getValue(), factors2.get(e.getKey()))))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		if (result.isEmpty()) {
			result.put(1l, 1);
		}
		return result;
	}

	private Map<Long, Integer> calculatemcm(Map<Long, Integer> factors1, Map<Long, Integer> factors2) {
		Map<Long, Integer> result = new HashMap<>(factors1);
		factors2.forEach((k, v) -> result.merge(k, v, Math::max));
		return result;
	}

	private void calculateAndWrite(Map<Long, Integer> factors, TextInputControl output) {
		long result = factors.entrySet().stream().map(e -> Math.pow(e.getKey(), e.getValue())).reduce((a, b) -> a * b)
				.orElse(1d).longValue();
		output.setText(Long.toString(result));
	}

	private void inputNotValid() {
		Alert alert = new Alert(AlertType.ERROR, "Le stringhe inserite non sono interi positivi validi");
		alert.setTitle("ERRORE");
		alert.setHeaderText("Errore nel parsing");
		alert.showAndWait();
	}
}
