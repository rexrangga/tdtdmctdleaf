package draughts.moves;

import java.util.Arrays;

import draughts.Checker;

public class Node {

	private final Checker[][] board;

	public Node(Checker[][] board) {
		this.board = BoardUtils.makeCopy(board);
	}

	public Checker[][] getBoard() {
		return board;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(board);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (!Arrays.equals(board, other.board))
			return false;
		return true;
	}
}
