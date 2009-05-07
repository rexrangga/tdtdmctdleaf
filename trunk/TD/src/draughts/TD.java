package draughts;

/*
 * Interface used by every TD implementation
 */
public abstract class TD implements ITD{

	protected double[] weights = null;
	private double a = 0;
	private double b = 0;

	public TD(double[] initialWeights, double a, double b) {
		weights = initialWeights;
		this.a = a;
		this.b = b;
	}

	/*
	 * Calculates value of the evaluation function for given evaluation function
	 * features;
	 */
	public double calculateEvaluationFunction(double[] features) {
		if (features == null || features.length != weights.length)
			return 0;

		double sum = 0.0;

		for (int i = 0; i < weights.length; i++) {
			sum += features[i] * weights[i];
		}

		return a * Math.tanh(b * sum);
	}

	/*
	 * Provides single game data, that should be used in the learning process.
	 */
	public abstract void updateWeights(GameData gameData);
}
