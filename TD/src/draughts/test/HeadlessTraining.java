package draughts.test;

import java.io.BufferedWriter;
import java.io.IOException;

import draughts.ITD;

public class HeadlessTraining {

	public void train(ITD trainedPlayer, ITD opponent, int numGames, BufferedWriter out) throws Exception {
		
		HeadlessGame headlessGame = new HeadlessGame();

		int trainedPlayerWinsCounter=0;
		
		for (int i = 0; i < numGames; i++) {
			ITD whitePlayer = null, blackPlayer = null;
			boolean trainingWhite = false;
			if (i % 2 == 0) {
				trainingWhite = true;
				whitePlayer = trainedPlayer;
				blackPlayer = opponent;
			} else {
				trainingWhite = false;
				whitePlayer = opponent;
				blackPlayer = trainedPlayer;
			}
			boolean whiteWins=headlessGame.playGame(whitePlayer, blackPlayer, trainingWhite,i);
			if((trainingWhite && whiteWins) || (!trainingWhite && !whiteWins))
				trainedPlayerWinsCounter++;
		}
		
		out.write("Gracz uczacy sie wygral " + trainedPlayerWinsCounter + " z " + numGames + " gier.");
	}
}
