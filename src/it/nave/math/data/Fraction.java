package it.nave.math.data;

import java.util.Optional;

public class Fraction {
	private Optional<String> intera = Optional.empty();
	private Optional<String> antiperiodo = Optional.empty();
	private Optional<String> periodo = Optional.empty();

	public Fraction(String n) {
		n = n.trim().replace(",", ".").replace("(", "[").replace(")", "]");
		if (n.matches("^\\d+$")) {
			intera = Optional.of(n);
		} else if (n.matches("^\\d+\\.\\d+$")) {
			String split[] = n.split("\\.");
			intera = Optional.of(split[0]);
			antiperiodo = Optional.of(split[1]);
			periodo = Optional.empty();
		} else if (n.matches("^\\d+\\.\\[\\d+\\]$")) {
			String split1[] = n.split("\\.");
			intera = Optional.of(split1[0]);
			String split2[] = split1[1].split("\\[");
			antiperiodo = Optional.empty();
			periodo = Optional.of(split2[1].replaceAll("\\]", ""));
		} else if (n.matches("^\\d+\\.\\d+\\[\\d+\\]$")) {
			String split1[] = n.split("\\.");
			intera = Optional.of(split1[0]);
			String split2[] = split1[1].split("\\[");
			antiperiodo = Optional.of(split2[0]);
			String temp = split2[1].replaceAll("\\]", "");
			periodo = (temp.matches("0+")) ? Optional.empty() : Optional.of(temp);
		} else {
			throw new IllegalArgumentException();
		}
	}

	public Optional<String> getIntera() {
		return intera;
	}

	public Optional<String> getAntiperiodo() {
		return antiperiodo;
	}

	public Optional<String> getPeriodo() {
		return periodo;
	}

}
