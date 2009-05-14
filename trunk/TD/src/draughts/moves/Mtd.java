package draughts.moves;

import java.util.List;
import java.util.Set;

import draughts.Checker;
import draughts.CheckerModel;
import draughts.GameData;
import draughts.ITD;
import draughts.MoveMessage;
import draughts.MovesFinder;
import draughts.Pair;
import draughts.Player;

public class Mtd {

	private static final int F = 0;
	private static final int MAX_DEPTH = 5;

	private Checker[][] originalBoard;
	private GameData gameData = new GameData();

	private AlphaBetaWithMemory alphaBetaWithMemory = new AlphaBetaWithMemory();

	public Mtd() {
	}

	public GameData getGameData() {
		return gameData;
	}

	public List<MoveMessage> getBestMove(Checker[][] bboard, int f, int maxDepth, Player me, ITD td) {
		this.originalBoard = bboard;
		CheckerModel[][] board = BoardUtils.fromChecker(bboard);
		System.out.println("getBestMove");
		MovesFinder mf = new MovesFinder(board, me.getMAuthor());
		Set<List<MoveMessage>> legalMoves = mf.getLegalMoves();
		List<MoveMessage> bestMove = null;
		CheckerModel[][] bestVariation = null;
		double bestValue = Double.NEGATIVE_INFINITY;
		for (List<MoveMessage> move : legalMoves) {
			Pair<Double, CheckerModel[][]> p = evaluate(board, f, maxDepth, me, td);
			double currentValue = p.getFirst();
			if (currentValue > bestValue) {
				bestMove = move;
				bestValue = currentValue;
				bestVariation = p.getSecond();
			}
		}
		gameData.m_board.add(BoardUtils.performMoves(board, bestMove));
		gameData.statesEvaluations.add(bestValue);
		gameData.principalVariation.add(bestVariation);
		return bestMove;
	}

	public List<MoveMessage> getBestMoveDefault(Checker[][] board, Player me, ITD td) {
		return getBestMove(board, F, MAX_DEPTH, me, td);
	}

	private Pair<Double, CheckerModel[][]> evaluate(CheckerModel[][] board, double f, int maxDepth, Player me, ITD td) {
		double g = f;
		CheckerModel[][] principalVariation = null;
		double upperBound = Double.POSITIVE_INFINITY;
		double lowerBound = Double.NEGATIVE_INFINITY;
		while (lowerBound < upperBound) {
			double beta;
			if (g == lowerBound) {
				beta = g + 1;
			} else {
				beta = g;
			}
			Node node = new Node(board);
			Pair<Double, CheckerModel[][]> p = alphaBetaWithMemory.evaluate(node, beta - 1, beta, maxDepth, true, me,
					td, originalBoard);
			g = p.getFirst();
			principalVariation = p.getSecond();
			if (g < beta) {
				upperBound = g;
			} else {
				lowerBound = g;
			}
		}
		return new Pair<Double, CheckerModel[][]>(g, principalVariation);
	}
}
