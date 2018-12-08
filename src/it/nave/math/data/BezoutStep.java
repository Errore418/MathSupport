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

	private void copyFromEuclide(EuclideStep es) {
		this.MCD = es.getRest();
		this.big = es.getBig();
		this.small = es.getSmall().multiply(-1);
	}

	public void addStep(EuclideStep step) {
		if (!inizialized) {
			copyFromEuclide(step);
			inizialized = true;
		} else {
			step.getBig().multiply(small.getCoefficient());
			step.getSmall().multiply(-small.getCoefficient());
			small = big.add(step.getSmall());
			big = step.getBig();
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
