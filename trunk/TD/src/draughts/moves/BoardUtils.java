package draughts.moves;

import java.util.List;

import draughts.Checker;
import draughts.CheckerModel;
import draughts.MoveMessage;
import draughts.Sort;

public class BoardUtils {

	public static final int BOARD_SIZE = 10;

	public static CheckerModel[][] makeCopy(CheckerModel[][] board) {
		CheckerModel[][] result = new CheckerModel[BOARD_SIZE][];
		for (int i = 0; i < BOARD_SIZE; i++) {
			CheckerModel[] temp = new CheckerModel[BOARD_SIZE];
			for (int j = 0; j < BOARD_SIZE; j++) {
				temp[j] = new CheckerModel(board[i][j]);
			}
			result[i] = temp;
		}
		return result;
	}

	public static CheckerModel[][] fromChecker(Checker[][] board) {
		CheckerModel[][] result = new CheckerModel[BOARD_SIZE][];
		for (int i = 0; i < BOARD_SIZE; i++) {
			CheckerModel[] temp = new CheckerModel[BOARD_SIZE];
			for (int j = 0; j < BOARD_SIZE; j++) {
				temp[j] = new CheckerModel(board[i][j].getI(), board[i][j].getJ(), board[i][j].getKind());
			}
			result[i] = temp;
		}
		return result;
	}
	
	public static CheckerModel[][] performMoves(CheckerModel[][] board, List<MoveMessage> moves) {
		CheckerModel[][] result = makeCopy(board);
		for (MoveMessage mm : moves) {
			int dx = Math.abs(mm.getFirst().getI() - mm.getSecond().getI());
			int dy = Math.abs(mm.getFirst().getJ() - mm.getSecond().getJ());
			if (dx == dy && dx > 1) {
				for (int i = 1; i < dx; i++) {
					int directionX = (mm.getFirst().getI() - mm.getSecond().getI()) / dx;
					int directionY = (mm.getFirst().getJ() - mm.getSecond().getJ()) / dy;
					int iIdx = mm.getFirst().getI() + directionX * i;
					int jIdx = mm.getFirst().getJ() + directionY * i;
					board[iIdx][jIdx].setKind(Sort.blankBlack);
				}
				board[mm.getFirst().getI()][mm.getFirst().getJ()].setKind(Sort.blankBlack);
				board[mm.getSecond().getI()][mm.getSecond().getJ()].setKind(mm.getFirst().getKind());
			} else if (dx == 1 && dy == 1) {
				board[mm.getFirst().getI()][mm.getFirst().getJ()].setKind(Sort.blankBlack);
				board[mm.getSecond().getI()][mm.getSecond().getJ()].setKind(mm.getFirst().getKind());
			} else {
				throw new RuntimeException("first [i = " + mm.getFirst().getI() + ", j = " + mm.getFirst().getJ()
						+ "] second [i = " + mm.getSecond().getI() + ", j = " + mm.getSecond().getJ() + "]");
			}
			if (mm.isEndsTurn()) {
				if (mm.getFirst().getKind().equals(Sort.fullWhite) && mm.getSecond().getI() == 0) {
					board[mm.getSecond().getI()][mm.getSecond().getJ()].setKind(Sort.queenWhite);
				}
				if (mm.getFirst().getKind().equals(Sort.fullBlack) && mm.getSecond().getI() == 9) {
					board[mm.getSecond().getI()][mm.getSecond().getJ()].setKind(Sort.queenBlack);
				}
			}
		}
		return result;
	}

	public static boolean isBeating(List<MoveMessage> list) {
		MoveMessage mm = list.get(0);
		int dx = Math.abs(mm.getFirst().getI() - mm.getSecond().getI());
		int dy = Math.abs(mm.getFirst().getJ() - mm.getSecond().getJ());
		return dx == dy && dx > 1;
	}
}
