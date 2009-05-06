package draughts;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class FeatureCalculations {

	private Checker[][] checkersArray;
	private Player player;

	private Set<List<MoveMessage>> myBeatableMoves;
	private Set<List<MoveMessage>> oppBeatableMoves;
	private Set<List<MoveMessage>> myNonBeatableMoves;
	private Set<List<MoveMessage>> oppNonBeatableMoves;
	
	public FeatureCalculations(Checker[][] checkersArray, Player player){
		this.player=player;
		this.checkersArray=checkersArray;
		
		MovesFinder myMovesFinder=new MovesFinder(checkersArray,player.getMAuthor());
		this.myBeatableMoves=myMovesFinder.getAllBeatings();
		this.myNonBeatableMoves=myMovesFinder.getAllNonBeatings();
		MovesFinder oppMovesFinder=new MovesFinder(checkersArray,player.getMAuthor()==Author.owner?Author.opponent:Author.owner);
		this.oppBeatableMoves=oppMovesFinder.getAllBeatings();
		this.oppBeatableMoves=oppMovesFinder.getAllNonBeatings();
	}
	
	private int getMyPossibleBeatsCount(){
		return myBeatableMoves.size()+myNonBeatableMoves.size();
	}
	
	private int getOppPossibleBeatsCount(){
		return oppBeatableMoves.size() + oppNonBeatableMoves.size();
	}
	
	private int getLongestBeatLength(Set<List<MoveMessage>> possibleMoves){
		int max=0;
		
		Iterator<List<MoveMessage>> it=possibleMoves.iterator();
		
		while(it.hasNext()){
			List<MoveMessage> actual=it.next();
			max=(actual.size()>max)?actual.size():max;
		}
		return max;
	}
	
	public double[] getEvaluationFunctionFeatures(){
		
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
		
		features[4]=getOppPossibleBeatsCount();
		features[5]=getMyPossibleBeatsCount();
		features[6]=getLongestBeatLength(oppBeatableMoves);
		features[7]=getLongestBeatLength(myBeatableMoves);
		
		
		return new double[0];
	}
	
}
