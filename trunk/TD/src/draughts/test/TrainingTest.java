package draughts.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.concurrent.TimeUnit;

import draughts.ITD;
import draughts.test.PlayerUtils.PlayerKind;

public class TrainingTest {

	public static String historyFilename = "history.txt";

	public static void main(String[] args) throws Exception {
		try {
			TimeUnit.MILLISECONDS.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ITD trainedPlayer = null;
		if (args.length != 3 && args.length != 5 && args.length != 7) {
			System.out
					.println("USAGE: ... beforeVectorFilename afterVectorFilename gamesCount [learningPlayerFilename learningPlayerMethod{0,1,2} [opponentPlayerFilename opponentPlayerMethod{0,1,2}]]");
			return;
		}
		String beforeVectorFilename = args[0];
		String afterVectorFilename = args[1];
		int gamesCount = Integer.parseInt(args[2]);

		if (args.length == 5 || args.length == 7)
			trainedPlayer = PlayerUtils.loadPlayer(args[3], PlayerUtils.getKind(Integer.parseInt(args[4])));
		else
			trainedPlayer = PlayerUtils.createRandomPlayer(PlayerKind.TD);
		PlayerUtils.savePlayer(trainedPlayer, beforeVectorFilename);
		ITD opponent = null;
		if (args.length == 7)
			trainedPlayer = PlayerUtils.loadPlayer(args[5], PlayerUtils.getKind(Integer.parseInt(args[6])));
		else
			opponent = PlayerUtils.createRandomPlayer(PlayerKind.TD);
		HeadlessTraining training = new HeadlessTraining();

		File file = new File(historyFilename);
		BufferedWriter out = new BufferedWriter(new FileWriter(file));		
		training.train(trainedPlayer, opponent, 10, out);
		PlayerUtils.savePlayer(trainedPlayer, afterVectorFilename);
		out.close();
	}

}
