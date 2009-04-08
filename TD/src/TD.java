
/*
 * Interface used by every TD implementation
 */
public abstract class TD {
	
	protected double[] weights = null;
	
	public TD(double[] initialWeights){
		weights=initialWeights;	
	}
	
	/*
	 * Calculates value of the evaluation function for given evaluation function features;
	 */
	public double calculateEvaluationFunction(double[] features){
		if(features==null || features.length!=weights.length)
			return 0;
		
		double sum = 0.0;
		
		for(int i =0; i<weights.length; i++){
			sum+=features[i]*weights[i];
		}
		
		return sum;
	}
	
	/*
	 * Provides single game data, that should be used in the learning process.
	 */
	public abstract void updateWeights(GameData gameData);
}


