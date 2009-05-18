package draughts.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import draughts.Author;
import draughts.Checker;
import draughts.CheckerModel;
import draughts.ITD;
import draughts.MoveMessage;
import draughts.Player;
import draughts.Sort;
import draughts.TDLambda;
import draughts.moves.BoardUtils;
import draughts.moves.Mtd;

public class HeadlessGame {

	private static final int GAMES_COUNT = 2;
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");

	public void play() {
		ITD whitePlayer = new TDLambda(fillRandomWeights(), 0, 0, 0, 0, 0);
		ITD blackPlayer = new TDLambda(fillRandomWeights(), 0, 0, 0, 0, 0);
		learningMode(GAMES_COUNT, whitePlayer, blackPlayer);
	}

	private double[] fillRandomWeights() {
		double[] weights = new double[12];
		for (int i = 0; i < 12; i++)
			weights[i] = new Random().nextDouble();
		return weights;
	}

	public void learningMode(int gamesCount, ITD whitePlayer, ITD blackPlayer) {
		try {
			String name = formatter.format(new Date()) + ".game";
			System.out.println("name:" + name);
			File file = new File(name);
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			boolean whiteWins;
			out.write("Wektor gracza -> " + whitePlayer.getWeigths());
			out.write("Wektor przeciwnika -> " + blackPlayer.getWeigths());
			for (int i = 0; i < gamesCount / 2; i++) {
				whiteWins = playGame(whitePlayer, blackPlayer, true);
				if (!whiteWins) {
					out.write("Wygral przeciwnik. Uczymy gracza...");
					out.write("Wektor po nauce -> " + whitePlayer.getWeigths());
				} else {
					out.write("Wygral uczen. Nie uczymy ...");
				}
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Checker[][] createStartingBoard() {
		Checker[][] checkersArray = new Checker[10][10];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				checkersArray[i][j] = new Checker(i, j);
				if ((i + j) % 2 != 0) {
					if (i < 4) {
						checkersArray[i][j].setKind(Sort.fullBlack);
					} else if (i >= 6) {
						checkersArray[i][j].setKind(Sort.fullWhite);
					} else {
						checkersArray[i][j].setKind(Sort.blankBlack);
					}
				} else {
					checkersArray[i][j].setKind(Sort.blankWhite);
				}
				// setCheckerIcon(checkersArray[i][j],
				// checkersArray[i][j].getKind());
			}
		}
		return checkersArray;
	}

	public boolean playGame(ITD whitePlayer, ITD blackPlayer, boolean learningWhite) {
		Checker[][] bboard = createStartingBoard();
		CheckerModel[][] board = BoardUtils.fromChecker(bboard);

		Mtd whiteMtd = new Mtd(Author.owner);
		Mtd blackMtd = new Mtd(Author.opponent);
		Player white = new Player("white bot", Author.owner);
		Player black = new Player("black bot", Author.opponent);

		boolean finished = false;
		boolean whiteWins = false;
		while (!finished) {
			List<MoveMessage> list = whiteMtd.getBestMove(board, 0, 5, white, whitePlayer);
			if (list == null) {
				finished = true;
				whiteWins = false;
			}
			board = BoardUtils.performMoves(board, list);
			if (!finished) {
				list = blackMtd.getBestMove(board, 0, 5, black, blackPlayer);
				if (list == null) {
					finished = true;
					whiteWins = true;
				}
				board = BoardUtils.performMoves(board, list);
			}
		}
		if (whiteWins && !learningWhite) {
			blackPlayer.updateWeights(blackMtd.getGameData());
		} else if (!whiteWins && learningWhite) {
			whitePlayer.updateWeights(whiteMtd.getGameData());
		}

		return whiteWins;
	}
}
