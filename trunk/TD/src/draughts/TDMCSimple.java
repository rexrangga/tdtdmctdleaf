package draughts;

public class TDMCSimple extends TD {

	int simNumber = 5000;

	public TDMCSimple(double[] initialWeights, double a, double b) {
		super(initialWeights, a, b);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateWeights(GameData gameData) {
		// first step of the algorithm - calculate evaluationFunctionValues
		double[] evaluationFunctionValues = calculateEvaluationFunctionValue(gameData.evaluationFunctionFeatures);
		double[] rewards = calculateMonteCarloWinningProbabilities(gameData,
				simNumber);

	}
	
	@Override
	public double[] getWeigths() {
		return super.weights;
	}
}
