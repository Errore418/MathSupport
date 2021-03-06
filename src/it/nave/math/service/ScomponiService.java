package it.nave.math.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import it.nave.math.support.Tool;
import javafx.scene.control.TextInputControl;

public class ScomponiService {

	private long n;
	private Optional<TextInputControl> output;

	public ScomponiService(long n, TextInputControl output) {
		this.n = n;
		this.output = Optional.ofNullable(output);
	}

	public Map<Long, Integer> scomponi() {
		Map<Long, Integer> factors = new HashMap<>();
		if (n == 0 || n == 1) {
			factors.put(n, 1);
		} else {
			while (n != 1) {
				long divisor = findFirstDivisor(n);
				int count = factors.getOrDefault(divisor, 0);
				factors.put(divisor, ++count);
				n /= divisor;
			}
		}
		if (output.isPresent()) {
			Tool.writeFactors(factors, output.get());
		}
		return factors;
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

}
