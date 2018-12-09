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
		Map<Long, Integer> factorsMCD = calculateMCD(factors1, factors2);
		Map<Long, Integer> factorsmcm = calculatemcm(factors1, factors2);
		Tool.writeFactors(factorsMCD, scomposizioneMCD);
		Tool.writeFactors(factorsmcm, scomposizionemcm);
		calculateAndWrite(factorsMCD, MCD);
		calculateAndWrite(factorsmcm, mcm);
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
}
