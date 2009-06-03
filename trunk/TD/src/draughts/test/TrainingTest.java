package draughts.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import draughts.ITD;
import draughts.test.HeadlessTraining.OpponentCreateStrategy;
import draughts.test.PlayerUtils.PlayerKind;

public class TrainingTest {

	private int depth;
	private double alpha, gamma, lambda;
	private int gamesCount;
	private String logFilePath;
	private String firstPlayerFile;
	private String secondPlayerFile;

	public TrainingTest(String[] args) throws IOException {
		depth = Integer.parseInt(args[0]);
		alpha = Double.parseDouble(args[1]);
		gamma = Double.parseDouble(args[2]);
		lambda = Double.parseDouble(args[3]);
		gamesCount = Integer.parseInt(args[4]);
		logFilePath = args[5];

		ITD firstPlayer = null;

		if (args.length > 7) {
			PlayerKind firstKind = PlayerUtils.getKind(Integer.parseInt(args[6]));
			firstPlayer = PlayerUtils.loadPlayer(args[7], firstKind, alpha, gamma, lambda);
			firstPlayerFile = args[7];
		} else {
			PlayerKind firstKind = PlayerUtils.getKind(Integer.parseInt(args[6]));
			firstPlayer = PlayerUtils.createRandomPlayer(firstKind, alpha, gamma, lambda);
			firstPlayerFile = "learning" + System.currentTimeMillis() + ".txt";
		}

		OpponentCreateStrategy strategy = null;
		boolean saveSecondPlayer = false;
		ITD secondPlayer = null;
		if (args.length > 9) {
			secondPlayerFile = args[9];
			PlayerKind secondKind = PlayerUtils.getKind(Integer.parseInt(args[8]));
			secondPlayer = PlayerUtils.loadPlayer(args[9], secondKind, alpha, gamma, lambda);
			final ITD p = secondPlayer;
			strategy = new OpponentCreateStrategy() {
				public ITD getOpponent() {
					return p;
				}
			};
			saveSecondPlayer = true;
		} else {
			strategy = HeadlessTraining.RANDOM_OPPONENT;
		}

		HeadlessTraining training = new HeadlessTraining();

		File file = new File(logFilePath);
		BufferedWriter out = new BufferedWriter(new FileWriter(file));
		training.train(firstPlayer, gamesCount, out, strategy, depth);
		PlayerUtils.savePlayer(firstPlayer, firstPlayerFile.substring(0, firstPlayerFile.length() - 4) + "_after.txt");
		if (saveSecondPlayer) {
			PlayerUtils.savePlayer(secondPlayer, secondPlayerFile.substring(0, secondPlayerFile.length() - 4)
					+ "_after.txt");
		}
		out.close();
	}

	public static void main(String[] args) throws Exception {
		new TrainingTest(args);
		// try {
		// TimeUnit.MILLISECONDS.sleep(1000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// ITD trainedPlayer = null;
		// if (args.length != 3 && args.length != 5 && args.length != 7) {
		// System.out.println("USAGE: ... beforeVectorFilename afterVectorFilename gamesCount [learningPlayerFilename learningPlayerMethod{0,1,2} [opponentPlayerFilename opponentPlayerMethod{0,1,2}]]");
		// return;
		// }
		// String beforeVectorFilename = args[0];
		// String afterVectorFilename = args[1];
		// int gamesCount = Integer.parseInt(args[2]);
		//
		// if (args.length == 5 || args.length == 7)
		// trainedPlayer = PlayerUtils.loadPlayer(args[3], PlayerUtils.getKind(Integer.parseInt(args[4])));
		// else
		// trainedPlayer = PlayerUtils.createRandomPlayer(PlayerKind.TDMC);
		//
		// PlayerUtils.savePlayer(trainedPlayer, beforeVectorFilename);
		// // ITD opponent = null;
		// // if (args.length == 7)
		// // trainedPlayer = PlayerUtils.loadPlayer(args[5], PlayerUtils
		// // .getKind(Integer.parseInt(args[6])));
		// // else
		// // opponent = PlayerUtils.createRandomPlayer(PlayerKind.TDMC);
		//
		// OpponentCreateStrategy strategy = null;
		// if (args.length == 7) {
		// final ITD opponent = PlayerUtils.loadPlayer(args[5], PlayerUtils.getKind(Integer.parseInt(args[6])));
		// strategy = new OpponentCreateStrategy() {
		// public ITD getOpponent() {
		// return opponent;
		// }
		// };
		// } else {
		// strategy = HeadlessTraining.RANDOM_OPPONENT;
		// }
		//
		// HeadlessTraining training = new HeadlessTraining();
		//
		// File file = new File(historyFilename);
		// BufferedWriter out = new BufferedWriter(new FileWriter(file));
		// training.train(trainedPlayer, gamesCount, out, strategy);
		// PlayerUtils.savePlayer(trainedPlayer, afterVectorFilename);
		// out.close();
	}
}
