package it.nave.math.service;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.TextInputControl;

public class ScomponiService {
	private static String TEMPLATE = "%d^%d";
	private static String TEMPLATE_SHORT = "%d";

	private long n;
	private TextInputControl output;

	public ScomponiService(long n, TextInputControl output) {
		this.n = n;
		this.output = output;
	}

	public void scomponi() {
		if (n == 0 || n == 1) {
			output.setText(Long.toString(n));
		} else {
			Map<Long, Integer> factors = new HashMap<>();
			while (n != 1) {
				long divisor = findFirstDivisor(n);
				int count = factors.getOrDefault(divisor, 0);
				factors.put(divisor, ++count);
				n /= divisor;
			}
			writeResult(factors);
		}
	}

	private long findFirstDivisor(long x) {
		long limit = (int) Math.sqrt(x);
		for (long i = 2; i <= limit; i++) {
			if (x % i == 0) {
				return i;
			}
		}
		return x;
	}

	private void writeResult(Map<Long, Integer> factors) {
		output.setText("");
		factors.entrySet().stream().sorted((a, b) -> a.getKey().compareTo(b.getKey())).map(
				e -> String.format((e.getValue() > 1) ? TEMPLATE : TEMPLATE_SHORT, e.getKey(), e.getValue()) + "\n")
				.forEach(output::appendText);
	}

}
