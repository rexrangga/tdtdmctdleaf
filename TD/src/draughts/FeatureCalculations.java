package draughts;

public class FeatureCalculations {

	private Checker[][] checkersArray;
	private Player player;
	
	private int getMyPossibleBeats(){
		return 0;
	}
	
	public FeatureCalculations(Player player){
		this.player=player;
	}
	
	public double[] getEvaluationFunctionFeatures(Checker[][] checkersArray){
		this.checkersArray=checkersArray;
		
		double[] features=new double[12];
		int iAmWhite=player.getMAuthor()==Author.owner?1:0;
		
		for(int i=0;i<12;i++)
			features[i]=0;
		
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<10;j++)
			{
				if(checkersArray[i][j].getKind()==Sort.fullBlack)
				{
					features[iAmWhite-1]++;
				}
				else if(checkersArray[i][j].getKind()==Sort.fullWhite)
				{
					features[iAmWhite]++;
				}
				else if(checkersArray[i][j].getKind()==Sort.queenBlack)
				{
					features[2+iAmWhite]++;
				}
				else if(checkersArray[i][j].getKind()==Sort.queenWhite)
				{
					features[2+iAmWhite]++;
				}
			}
		}
		
		
		
		return new double[0];
	}
	
}
