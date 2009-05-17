package draughts;

public class TDLambda extends TD {

	private double alpha;
	private double gamma;
	private double lambda;

	public TDLambda(double[] initialWeights, double a, double b, double alphaLearningRate,
			double gammaDiscountParameter, double lambdaDecayParameter) {
		super(initialWeights, a, b);
		this.alpha = alphaLearningRate;
		this.gamma = gammaDiscountParameter;
		this.lambda = lambdaDecayParameter;
	}

	@Override
	public void updateWeights(GameData gameData) {

		for (int t = 0; t < gameData.evaluationFunctionFeatures.size() - 1; t++) {
			double delta = gamma * gameData.statesEvaluations.get(t + 1) - gameData.statesEvaluations.get(t);
			double[] eligibility = calculateEligibilityVector(gameData, t);
			for (int i = 0; i < weights.length; i++) {
				weights[i] += alpha * delta * eligibility[i];
			}
		}
	}

	private double[] calculateEligibilityVector(GameData gameData, int t) {

		double[] result = new double[weights.length];
		if (t == 0) {
			for (int i = 0; i < result.length; i++) {
				result[i] = 0;
			}
			return result;
		}

		result = calculateEligibilityVector(gameData, t - 1);
		double[] currFeatures = gameData.evaluationFunctionFeatures.get(t);
		for (int i = 0; i < result.length; i++) {
			result[i] *= gamma;
			result[i] *= lambda;
			result[i] += currFeatures[i];
		}
		return result;
	}
}
