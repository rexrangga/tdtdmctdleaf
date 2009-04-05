import java.util.List;

/*
 * Implementation of the first request version of TDMC (as described in the article)
 */
public class TDMC implements ITD 
{
	double gammaDiscountRate=0.5;
	double lambdaEligibilityRate=0.5;
	double[] weights = null;
	
	public TDMC(double[] weights, 
				double gammaDiscountRate, 
				double lambdaEligibilityRate){
		
		this.weights = weights;
		this.gammaDiscountRate = gammaDiscountRate;
		this.lambdaEligibilityRate = lambdaEligibilityRate;
	}
	
	@Override
	public double calculateEvaluationFunction(double[] features) {
		
		if(features==null || features.length!=weights.length)
			return 0;
		
		double sum = 0.0;
		
		for(int i =0; i<weights.length; i++){
			sum+=features[i]*weights[i];
		}
		
		return sum;
	}

	@Override
	public void updateWeights(GameData gameData) {
		int numberOfMovesT=gameData.evaluationFunctionFeatures.size();
		//first step of the algorithm - calculate evaluationFunctionValues
		double[] evaluationFunctionValues = calculateEverEvaluationFunctionValue(gameData.evaluationFunctionFeatures);
		//calculate evaluation function gradient
		//first dimension - time moment, second dimension - weight derrivates
		double[][] evaulationFunctionGradient = computeEvaluationFunctionGradient(evaluationFunctionValues);
		
		//calculate monte carlo rewards for every time moment
		
		//calculate each R value
		
		//update weights.
	}
	
	/*
	 * Calculates evaluation function values for every time moment
	 */
	private double[] calculateEverEvaluationFunctionValue(List<double[]> features)
	{
		double[] functionValues = new double[features.size()];
		for(int i=0; i<features.size(); i++)
		{
			functionValues[i]= calculateEvaluationFunction(features.get(i));
			
		}
		return functionValues;
	}
	
	/*
	 * Calculates gradient of the evaluation function for every moment of time.
	 * Return value: first dimension - time moment, second dimension - weight derrivate
	 */
	public double[][] computeEvaluationFunctionGradient(double[] evaluationFunctionValues)
	{
		double[][] gradient = new double[evaluationFunctionValues.length][weights.length];
		for(int i=0; i<evaluationFunctionValues.length; i++){
			//TODO: obliczyæ gradient na ka¿dej ze wspó³rzêdnych.
		}
		return gradient;
	}
	
	
	//Rt
	public double computeReturn(){
		
		
		return 0.0;
	}
	
	//Rtn
	public double computeNStepReturn(){
		
		return 0.0;
	}
	
	//RTlamda
	public double computeLabdaReturn(){
		
		return 0.0;
	}
	
}
