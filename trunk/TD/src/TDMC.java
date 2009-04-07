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
		double[][] evaulationFunctionGradient = computeEvaluationFunctionGradient(gameData.evaluationFunctionFeatures);
		//TODO dokoñczyæ funkcê modyfikuj¹c¹ wagi
		
		double[] rewards = calculateWinningProbabilities(gameData);
		double[] Rt = computeReturn(rewards);
		
		
		
		
		//calculate each R value
		
		//update weights.
	}
	
	/**
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
	
	/**
	 * Calculates gradient of the evaluation function for every moment of time.
	 * Return value: first dimension - time moment, second dimension - weight derrivate
	 */
	public double[][] computeEvaluationFunctionGradient(List<double[]> evaluationFunctionFeatures)
	{
		double[][] gradient = new double[evaluationFunctionFeatures.size()][weights.length];
		for(int i=0; i<evaluationFunctionFeatures.size(); i++){
			for(int j=0; j<weights.length; j++){
				gradient[i][j]=evaluationFunctionFeatures.get(i)[j];
			}
		}
		return gradient;
	}
	
	public double[] calculateWinningProbabilities(GameData gameData){
		//TODO napisaæ kod symulacji MonteCarlo
		
		return null;
	}
	
	public double[] computeReturn(double[] returns){
		double[] Rt = new double[returns.length];
		for(int i=0; i<returns.length; i++){
			double value = 0.0;
			for(int j=i; j<returns.length; j++)
				value+=Math.pow(lambdaEligibilityRate, j-1)*returns[j];
			Rt[i]=value;		
		}
		return Rt;
	}
	
	/**
	 * 
	 * @return first argument - time moment, 
	 */
	public double[][] computeNStepReturn(double[] rewards, double evaluationFunctionValues){
		double[][] Rn = new double[rewards.length][];
		for(int i=0; i<rewards.length; i++){
			Rn[i]=new double[rewards.length-i];
			
		}
		//TODO napisaæ kod obliczaj¹cy Rn
		return Rn;
	}
	
	//RTlamda
	public double[] computeLabdaReturn(double[][] Rn, double[] Rt){
		double[] RtLambda = new double[Rt.length];
		for(int i=0; i<RtLambda.length; i++){
			double value=0.0;
			for(int j=0; j<Rt.length-i; j++){
				value+=Math.pow(lambdaEligibilityRate, j)*Rn[i][j];
			}
			value+=Math.pow(lambdaEligibilityRate, Rt.length-i)*Rt[i];
			RtLambda[i]=value;
		}
		return RtLambda;
	}
}
