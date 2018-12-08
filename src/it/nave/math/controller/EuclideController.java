package it.nave.math.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.nave.math.data.BezoutStep;
import it.nave.math.data.EuclideStep;
import it.nave.math.data.Num;
import it.nave.math.support.Tool;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

public class EuclideController {
	private static String EUCLIDE_STEP_TEMPLATE = "%s = %s + %s";

	@FXML
	private TextField input1;
	@FXML
	private TextField input2;
	@FXML
	private TextArea euclide;
	@FXML
	private TextArea bezout;
	@FXML
	private TextField MCD;
	@FXML
	private TextField MCD2;

	@FXML
	private void initialize() {
	}

	@FXML
	private void backToHome() throws IOException {
		Tool.startNewStage("Home.fxml", "Welcome", input1);
	}

	@FXML
	private void calcola() {
		long n1 = parseTextInputControl(input1);
		long n2 = parseTextInputControl(input2);
		List<EuclideStep> euclideSteps = executeEuclide(n1, n2);
		executeBezout(euclideSteps);

	}

	private List<EuclideStep> executeEuclide(long n1, long n2) {
		euclide.clear();
		long a = Math.max(n1, n2);
		long b = Math.min(n1, n2);
		List<EuclideStep> steps = new ArrayList<>();
		while (b != 0) {
			EuclideStep step = new EuclideStep(a, b);
			writeEuclideStep(step);
			a = b;
			b = step.getRest().toLong();
			steps.add(step);
		}
		MCD.setText(Long.toString(a));
		return steps;
	}

	private void executeBezout(List<EuclideStep> euclideSteps) {
		bezout.clear();
		if (euclideSteps.size() == 1) {
			executeBezoutShort(euclideSteps.get(0));
		} else {
			BezoutStep step = new BezoutStep();
			for (int i = euclideSteps.size() - 2; i >= 0; i--) {
				step.addStep(euclideSteps.get(i));
				writeBezoutStep(step);
			}
			String finalBezout = buildBezoutSecond(step);
			MCD2.setText(finalBezout);
		}
	}

	private void executeBezoutShort(EuclideStep last) {
		String bezoutFirst = last.getSmall().getValue() + " = ";
		String bezoutSecond = "0 * " + last.getBig() + " + " + last.getSmall().getValue();
		bezout.setText(bezoutFirst + bezoutSecond);
		MCD2.setText(bezoutSecond);
	}

	private void writeEuclideStep(EuclideStep step) {
		euclide.appendText(String.format(EUCLIDE_STEP_TEMPLATE, step.getBig(), step.getSmall(), step.getRest() + "\n"));
	}

	private void writeBezoutStep(BezoutStep step) {
		String bezoutFirst = step.getMCD() + " = ";
		String bezoutSecond = buildBezoutSecond(step);
		bezout.appendText(bezoutFirst + bezoutSecond + "\n");
	}

	private String buildBezoutSecond(BezoutStep step) {
		Num positive = (step.getBig().getCoefficient() > 0) ? step.getBig() : step.getSmall();
		Num negative = (step.getBig().getCoefficient() > 0) ? step.getSmall() : step.getBig();
		return positive + " " + negative;
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

	private void inputNotValid() {
		Alert alert = new Alert(AlertType.ERROR, "La stringa inserita non è un intero positivo valido");
		alert.setTitle("ERRORE");
		alert.setHeaderText("Errore nel parsing");
		alert.showAndWait();
	}
}
