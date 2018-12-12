package it.nave.math.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import it.nave.math.data.Fraction;
import it.nave.math.service.ScomponiService;
import it.nave.math.support.Tool;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class FrazioniController {

	@FXML
	private TextField input;
	@FXML
	private TextField numeratore1;
	@FXML
	private TextField numeratore2;
	@FXML
	private TextField denominatore1;
	@FXML
	private TextField denominatore2;

	@FXML
	private void initialize() {
	}

	@FXML
	private void backToHome() throws IOException {
		Tool.startNewStage("Home.fxml", "Welcome", input);
	}

	@FXML
	private void genera() {
		Fraction f = new Fraction(input.getText());
		scriviFrazione(f);
	}

	private void scriviFrazione(Fraction f) {
		if (!f.getPeriodo().isPresent()) {
			String numeratore = (f.getIntera().get().matches("0+") ? "" : f.getIntera().get())
					+ f.getAntiperiodo().orElse("");
			String denominatore = "1";
			if (f.getAntiperiodo().isPresent()) {
				long zero = f.getAntiperiodo().get().chars().count();
				for (int i = 0; i < zero; i++) {
					denominatore += "0";
				}
			}
			numeratore1.setText(numeratore);
			denominatore1.setText(denominatore);
		} else {
			String all = f.getIntera().get() + f.getAntiperiodo().orElse("") + f.getPeriodo().get();
			long numeratore = Long.parseLong(all) - Long.parseLong(f.getIntera().get() + f.getAntiperiodo().orElse(""));
			String denominatore = "";
			long nine = f.getPeriodo().get().chars().count();
			for (int i = 0; i < nine; i++) {
				denominatore += "9";
			}
			if (f.getAntiperiodo().isPresent()) {
				long zero = f.getAntiperiodo().get().chars().count();
				for (int i = 0; i < zero; i++) {
					denominatore += "0";
				}
			}
			numeratore1.setText(Long.toString(numeratore));
			denominatore1.setText(denominatore);
		}
		semplifica();
	}

	private void semplifica() {
		long num = Tool.parseLongTextInputControl(numeratore1, -1, "");
		long den = Tool.parseLongTextInputControl(denominatore1, -1, "");
		Map<Long, Integer> numFacto = new ScomponiService(num, null).scomponi();
		Map<Long, Integer> denFacto = new ScomponiService(den, null).scomponi();
		long MCD = Tool.calculateAndWrite(Tool.calculateMCD(numFacto, denFacto), Optional.empty());
		num /= MCD;
		den /= MCD;
		numeratore2.setText(Long.toString(num));
		denominatore2.setText(Long.toString(den));
	}

}
