package draughts.test;

import java.io.IOException;

import draughts.ITD;
import draughts.test.PlayerUtils.PlayerKind;

public class TrainingTest {

	public static void main(String[] args) throws IOException {
		ITD trainedPlayer = PlayerUtils.createRandomPlayer(PlayerKind.TD);
		PlayerUtils.savePlayer(trainedPlayer, "before.txt");
		ITD opponent = PlayerUtils.createRandomPlayer(PlayerKind.TD);
		HeadlessTraining training = new HeadlessTraining();
		training.train(trainedPlayer, opponent, 10);
		PlayerUtils.savePlayer(trainedPlayer, "result.txt");
	}
}
