package draughts.test;

import draughts.ITD;

public class HeadlessTraining {

	public void train(ITD trainedPlayer, ITD opponent, int numGames) {

		HeadlessGame headlessGame = new HeadlessGame();

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
			headlessGame.playGame(whitePlayer, blackPlayer, trainingWhite);
		}
	}
}
