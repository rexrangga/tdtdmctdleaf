package draughts.moves;

import java.util.List;
import java.util.Set;

import draughts.Checker;
import draughts.CheckerModel;
import draughts.ITD;
import draughts.MoveMessage;
import draughts.MovesFinder;
import draughts.Player;

public class Mtd {

	private static final int F=0;
	private static final int MAX_DEPTH=5;
	
	private AlphaBetaWithMemory alphaBetaWithMemory = new AlphaBetaWithMemory();

	public List<MoveMessage> getBestMove(Checker[][] bboard, int f, int maxDepth, Player me, ITD td) {
		CheckerModel[][] board = BoardUtils.fromChecker(bboard);
		System.out.println("getBestMove");
		MovesFinder mf = new MovesFinder(board, me.getMAuthor());
		Set<List<MoveMessage>> legalMoves = mf.getLegalMoves();
		List<MoveMessage> bestMove = null;
		int bestValue = Integer.MIN_VALUE;
		for (List<MoveMessage> move : legalMoves) {
			int currentValue = evaluate(board, f, maxDepth, me, td);
			if (currentValue > bestValue) {
				bestMove = move;
				bestValue = currentValue;
			}
		}
		return bestMove;
	}
	
	public List<MoveMessage> getBestMoveDefault(Checker[][] board, Player me, ITD td){
		return getBestMove(board, F, MAX_DEPTH, me, td );
	}

	private int evaluate(CheckerModel[][] board, int f, int maxDepth, Player me, ITD td) {
		int g = f;
		int upperBound = Integer.MAX_VALUE;
		int lowerBound = Integer.MIN_VALUE;
		while (lowerBound < upperBound) {
			int beta;
			if (g == lowerBound) {
				beta = g + 1;
			} else {
				beta = g;
			}
			Node node = new Node(board);
			g = alphaBetaWithMemory.evaluate(node, beta - 1, beta, maxDepth, true, me, td);
			if (g < beta) {
				upperBound = g;
			} else {
				lowerBound = g;
			}
		}
		return g;
	}
}
