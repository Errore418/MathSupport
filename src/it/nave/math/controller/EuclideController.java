package it.nave.math.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.nave.math.data.BezoutStep;
import it.nave.math.data.EuclideStep;
import it.nave.math.data.Num;
import it.nave.math.support.Tool;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

public class EuclideController {
	private static String EUCLIDE_STEP_TEMPLATE = "%s = %s + %s";
	private static final double STEP_FRAME_DURATION = 500;

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
	private Button calcola;
	@FXML
	private Button indietro;

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
		Timeline euclide = new Timeline();
		List<EuclideStep> euclideSteps = executeEuclide(n1, n2, euclide);
		Timeline bezout = executeBezout(euclideSteps);
		euclide.setOnFinished(e -> bezout.play());
		euclide.play();
	}

	private List<EuclideStep> executeEuclide(long n1, long n2, Timeline timeline) {
		long countFrame = 0;
		Tool.addKeyFrame(timeline, countFrame++, STEP_FRAME_DURATION, e -> {
			calcola.setDisable(true);
			indietro.setDisable(true);
			euclide.clear();
			MCD.clear();
			bezout.clear();
			MCD2.clear();
		});
		long a = Math.max(n1, n2);
		long b = Math.min(n1, n2);
		List<EuclideStep> steps = new ArrayList<>();
		while (b != 0) {
			EuclideStep step = new EuclideStep(a, b);
			Tool.addKeyFrame(timeline, countFrame++, STEP_FRAME_DURATION, e -> writeEuclideStep(step));
			a = b;
			b = step.getRest().toLong();
			steps.add(step);
		}
		final long aFinal = a;
		Tool.addKeyFrame(timeline, countFrame++, STEP_FRAME_DURATION, e -> MCD.setText(Long.toString(aFinal)));
		return steps;
	}

	private Timeline executeBezout(List<EuclideStep> euclideSteps) {
		Timeline timeline = new Timeline();
		long countFrame = 1;
		if (euclideSteps.size() == 1) {
			String bezoutFirst = euclideSteps.get(0).getSmall().getValue() + " = ";
			String bezoutSecond = "0 * " + euclideSteps.get(0).getBig() + " + "
					+ euclideSteps.get(0).getSmall().getValue();
			Tool.addKeyFrame(timeline, countFrame++, STEP_FRAME_DURATION,
					e -> bezout.setText(bezoutFirst + bezoutSecond));
			Tool.addKeyFrame(timeline, countFrame++, STEP_FRAME_DURATION, e -> MCD2.setText(bezoutSecond));
		} else {
			BezoutStep step = new BezoutStep();
			for (int i = euclideSteps.size() - 2; i >= 0; i--) {
				BezoutStep buffer = step.addStep(euclideSteps.get(i));
				Tool.addKeyFrame(timeline, countFrame++, STEP_FRAME_DURATION, e -> writeBezoutStep(buffer));
				step = buffer;
			}
			String finalBezout = buildBezoutSecond(step);
			Tool.addKeyFrame(timeline, countFrame++, STEP_FRAME_DURATION, e -> MCD2.setText(finalBezout));
		}
		Tool.addKeyFrame(timeline, countFrame++, STEP_FRAME_DURATION, e -> {
			calcola.setDisable(false);
			indietro.setDisable(false);
		});
		return timeline;
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
