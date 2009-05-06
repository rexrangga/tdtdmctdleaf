package draughts.moves;

import java.util.List;

import draughts.Checker;
import draughts.MoveMessage;

public class BoardUtils {

	public static final int BOARD_SIZE = 10;

	public static Checker[][] makeCopy(Checker[][] board) {
		Checker[][] result = new Checker[BOARD_SIZE][];
		for (int i = 0; i < BOARD_SIZE; i++) {
			Checker[] temp = new Checker[BOARD_SIZE];
			for (int j = 0; j < BOARD_SIZE; j++) {
				temp[j] = new Checker(board[i][j]);
			}
			result[i] = temp;
		}
		return result;
	}

	public static Checker[][] performMoves(Checker[][] board, List<MoveMessage> moves) {
		Checker[][] result = makeCopy(board);
		// TODO
		return result;
	}
}
