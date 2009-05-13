package draughts.moves;

import java.awt.Color;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;

import draughts.Author;
import draughts.Checker;
import draughts.FeatureCalculations;
import draughts.ITD;
import draughts.MoveMessage;
import draughts.MovesFinder;
import draughts.Player;

public class AlphaBetaWithMemory {

	private LookupTable lookup = new LookupTable();

	public int evaluate1(Node node, int alpha, int beta, int depth, boolean myMove, Player me, ITD td,
			Checker[][] originalBoard) {
		return evaluate(node, alpha, beta, depth, myMove, me, td, originalBoard);
	}

	public int evaluate(Node node, int alpha, int beta, int depth, boolean myMove, Player me, ITD td,
			Checker[][] originalBoard) {

		// System.out.println("evaluate, depth = " + depth);
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
		if (isTerminal || depth <= 0) {
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
				MoveMessage mm = list.get(list.size() - 1);
				originalBoard[mm.getSecond().getI()][mm.getSecond().getJ()].setBorder(BorderFactory.createLineBorder(Color.red));
				Node newNode = new Node(BoardUtils.performMoves(node.getBoard(), list));
				// int newDepth = BoardUtils.isBeating(list) ? depth : depth - 1;
				int newDepth = depth - 1;
				if (newDepth == 0 && BoardUtils.isBeating(list, node.getBoard())) {
					newDepth = 1;
					g = Math.max(g, evaluate1(newNode, a, beta, newDepth, !myMove, me, td, originalBoard));
				} else {
					g = Math.max(g, evaluate(newNode, a, beta, newDepth, !myMove, me, td, originalBoard));
				}
				a = Math.max(a, g);
				originalBoard[mm.getSecond().getI()][mm.getSecond().getJ()].setBorder(null);
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
				MoveMessage mm = list.get(list.size() - 1);
				Node newNode = new Node(BoardUtils.performMoves(node.getBoard(), list));
				originalBoard[mm.getSecond().getI()][mm.getSecond().getJ()].setBorder(BorderFactory.createLineBorder(Color.red));
				// int newDepth = BoardUtils.isBeating(list) ? depth : depth - 1;
				int newDepth = depth - 1;
				if (newDepth == 0 && BoardUtils.isBeating(list, node.getBoard())) {
					newDepth = 1;
					g = Math.min(g, evaluate1(newNode, alpha, b, newDepth, !myMove, me, td, originalBoard));
				} else {
					g = Math.min(g, evaluate(newNode, alpha, b, newDepth, !myMove, me, td, originalBoard));
				}
				b = Math.min(b, g);
				originalBoard[mm.getSecond().getI()][mm.getSecond().getJ()].setBorder(null);
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
