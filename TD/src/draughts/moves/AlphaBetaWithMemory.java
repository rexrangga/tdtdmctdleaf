package draughts.moves;

import java.util.List;
import java.util.Set;


import draughts.Author;
import draughts.FeatureCalculations;
import draughts.ITD;
import draughts.MoveMessage;
import draughts.MovesFinder;
import draughts.Player;

public class AlphaBetaWithMemory {

	private LookupTable lookup = new LookupTable();

	public int evaluate(Node node, int alpha, int beta, int depth, boolean myMove, Player me, ITD td) {
		// System.out.println("evaluate");
		Author opponent = Author.owner.equals(me.getMAuthor()) ? Author.opponent : Author.owner;

		LookupTable.Data data = lookup.lookup(node, depth);
		if (data != null) {
			if (data.getLower() != null && data.getLower() >= beta) {
				return data.getLower();
			}
			if (data.getUpper() != null && data.getUpper() <= alpha) {
				return data.getUpper();
			}
			if (data.getLower() != null) {
				alpha = Math.max(alpha, data.getLower());
			}
			if (data.getUpper() != null) {
				beta = Math.min(beta, data.getUpper());
			}
		}

		int g = 0;
		MovesFinder mf = new MovesFinder(node.getBoard(), myMove ? me.getMAuthor() : opponent);
		Set<List<MoveMessage>> legalMoves = mf.getLegalMoves();
		boolean isTerminal = legalMoves.isEmpty();
		if (isTerminal || depth == 0) {
			FeatureCalculations featuresCalculator = new FeatureCalculations(node.getBoard(), me);
			return (int) td.calculateEvaluationFunction(featuresCalculator.getEvaluationFunctionFeatures());
		} else if (myMove) {
			g = Integer.MIN_VALUE;
			int a = alpha;
			// MovesFinder mf = new MovesFinder(node.getBoard(), myMove ? me.getMAuthor() : opponent);
			// Set<List<MoveMessage>> s = mf.getLegalMoves();
			for (List<MoveMessage> list : legalMoves) {
				if (g >= beta) {
					break;
				}
				Node newNode = new Node(BoardUtils.performMoves(node.getBoard(), list));
				// int newDepth = BoardUtils.isBeating(list) ? depth : depth - 1;
				int newDepth = depth - 1;
				if (newDepth == 0 && BoardUtils.isBeating(list)) {
					newDepth = 1;
				}
				g = Math.max(g, evaluate(newNode, a, beta, newDepth, !myMove, me, td));
				a = Math.max(a, g);
			}

		} else {
			g = Integer.MAX_VALUE;
			int b = beta;
			// MovesFinder mf = new MovesFinder(node.getBoard(), myMove ? me.getMAuthor() : opponent);
			// Set<List<MoveMessage>> s = mf.getLegalMoves();
			for (List<MoveMessage> list : legalMoves) {
				if (g <= alpha) {
					break;
				}
				Node newNode = new Node(BoardUtils.performMoves(node.getBoard(), list));
				// int newDepth = BoardUtils.isBeating(list) ? depth : depth - 1;
				int newDepth = depth - 1;
				if (newDepth == 0 && BoardUtils.isBeating(list)) {
					newDepth = 1;
				}
				g = Math.min(g, evaluate(newNode, alpha, b, newDepth, !myMove, me, td));
				b = Math.min(b, g);
			}
		}

		if (g <= alpha) {
			lookup.storeUpper(node, depth, g);
		} else if (g > alpha && g < beta) {
			throw new RuntimeException("err");
		} else {
			lookup.storeLower(node, depth, g);
		}

		return g;
	}
}
