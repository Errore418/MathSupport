package it.nave.math.data;

public class BezoutStep {
	private Num MCD;
	private Num big;
	private Num small;
	private boolean inizialized;

	public BezoutStep() {
		inizialized = false;
	}

	public BezoutStep(EuclideStep start) {
		copyFromEuclide(start);
		this.inizialized = true;
	}

	private BezoutStep(Num MCD, Num big, Num small) {
		this.MCD = MCD;
		this.big = big;
		this.small = small;
		this.inizialized = true;
	}

	private void copyFromEuclide(EuclideStep es) {
		this.MCD = es.getRest().multiply(1);
		this.big = es.getBig().multiply(1);
		this.small = es.getSmall().multiply(-1);
	}

	public BezoutStep addStep(EuclideStep step) {
		if (!inizialized) {
			copyFromEuclide(step);
			inizialized = true;
			return this;
		} else {
			Num newSmall = big.add(step.getSmall().multiply(-small.getCoefficient()));
			Num newBig = step.getBig().multiply(small.getCoefficient());
			return new BezoutStep(MCD.multiply(1), newBig, newSmall);
		}
	}

	public Num getMCD() {
		return MCD;
	}

	public Num getBig() {
		return big;
	}

	public Num getSmall() {
		return small;
	}

}
