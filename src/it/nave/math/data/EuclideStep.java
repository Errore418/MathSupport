package it.nave.math.data;

public class EuclideStep {
	private Num big;
	private Num small;
	private Num rest;

	public EuclideStep(long big, long small) {
		this.big = new Num(big, 1);
		this.small = new Num(small, big / small);
		this.rest = new Num(big % small, 1);
	}

	public Num getBig() {
		return big;
	}

	public Num getSmall() {
		return small;
	}

	public Num getRest() {
		return rest;
	}
}
