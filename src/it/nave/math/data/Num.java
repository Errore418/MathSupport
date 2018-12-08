package it.nave.math.data;

public class Num {
	private static final String STRING_TEMPLATE = "%d * %d";
	private static final String STRING_TEMPLATE_SHORT = "%d";

	private long value;
	private long coefficient;

	public Num(long value, long coefficient) {
		this.value = value;
		this.coefficient = coefficient;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public long getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(long coefficient) {
		this.coefficient = coefficient;
	}

	public boolean isSameValue(Num other) {
		return this.value == other.getValue();
	}

	@Override
	public String toString() {
		String string = null;
		if (coefficient == 1 || coefficient == 0 || coefficient == -1) {
			string = String.format(STRING_TEMPLATE_SHORT, value);
		} else {
			string = String.format(STRING_TEMPLATE, value, Math.abs(coefficient));
		}
		return (coefficient > 0) ? string : "- " + string;
	}

	public long toLong() {
		return value * coefficient;
	}

	public Num multiply(long x) {
		coefficient *= x;
		return this;
	}

	public Num add(long x) {
		coefficient += x;
		return this;
	}

	public Num add(Num o) {
		assert (value == o.getValue());
		coefficient += o.getCoefficient();
		return this;
	}

}
