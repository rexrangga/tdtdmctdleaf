package draughts.test;

import java.io.BufferedWriter;
import java.io.IOException;

import draughts.ITD;
import draughts.test.PlayerUtils.PlayerKind;

public class HeadlessTraining {

	public void train(ITD trainedPlayer, int numGames, BufferedWriter out) throws IOException {
		HeadlessGame headlessGame = new HeadlessGame();

		int trainedPlayerWinsCounter = 0;
		int drawCounter = 0;

		for (int i = 0; i < numGames; i++) {
			ITD opponent = PlayerUtils.createRandomPlayer(PlayerKind.TD);
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
			Boolean whiteWins = headlessGame.playGame(whitePlayer, blackPlayer, trainingWhite, i);
			if (whiteWins == null) {
				out.write("\nPartia nr " + i + " zakonczona remisem.");
				drawCounter++;
			} else if ((trainingWhite && whiteWins) || (!trainingWhite && !whiteWins)) {
				trainedPlayerWinsCounter++;
				out.write("\nPartie nr " + i + " wygral gracz uczacy sie.");
			} else {
				out.write("\nPartie nr " + i + " wygral przeciwnik.");
				out.write(" Wektor po nauce: ");
				ITD playingPlayer = trainingWhite ? whitePlayer : blackPlayer;
				for (int j = 0; j < playingPlayer.getWeigths().length; j++) {
					out.write(String.valueOf(playingPlayer.getWeigths()[j]));
					out.write(" ");
				}
			}
			out.flush();
		}

		out.write("\nGracz uczacy sie wygral " + trainedPlayerWinsCounter + " i zremisowal " + drawCounter + " z "
				+ numGames + " gier.");
	}

	/**
	 * Parameter <code>opponent</code> is ignored. Use method
	 * <code>train(ITD trainedPlayer, int numGames, BufferedWriter out)</code> instead.
	 * 
	 * @param trainedPlayer
	 * @param opponent
	 * @param numGames
	 * @param out
	 * @throws Exception
	 */
	@Deprecated
	public void train(ITD trainedPlayer, ITD opponent, int numGames, BufferedWriter out) throws Exception {
		train(trainedPlayer, numGames, out);
	}
}
