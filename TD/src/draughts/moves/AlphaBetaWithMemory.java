package draughts.moves;

import java.util.List;
import java.util.Set;

import draughts.Author;
import draughts.MoveMessage;
import draughts.MovesFinder;

public class AlphaBetaWithMemory {

	private LookupTable lookup = new LookupTable();

	public int evaluate(Node node, int alpha, int beta, int depth, boolean myMove, Author whoAmI) {

		Author opponent = Author.owner.equals(whoAmI) ? Author.opponent : Author.owner;

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
		boolean isTerminal = false;
		// TODO howto check if node is terminal
		if (isTerminal || depth == 0) {
			// TODO g = evaluate
		} else if (myMove) {
			g = Integer.MIN_VALUE;
			int a = alpha;
			MovesFinder mf = new MovesFinder(node.getBoard(), myMove ? whoAmI : opponent);
			Set<List<MoveMessage>> s = mf.getLegalMoves();
			for (List<MoveMessage> list : s) {
				if (g >= beta) {
					break;
				}
				Node newNode = new Node(BoardUtils.performMoves(node.getBoard(), list));
				g = Math.max(g, evaluate(newNode, a, beta, depth - 1, !myMove, whoAmI));
				a = Math.max(a, g);
			}

		} else {
			g = Integer.MAX_VALUE;
			int b = beta;
			MovesFinder mf = new MovesFinder(node.getBoard(), myMove ? whoAmI : opponent);
			Set<List<MoveMessage>> s = mf.getLegalMoves();
			for (List<MoveMessage> list : s) {
				if (g <= alpha) {
					break;
				}
				Node newNode = new Node(BoardUtils.performMoves(node.getBoard(), list));
				g = Math.min(g, evaluate(newNode, alpha, b, depth - 1, !myMove, whoAmI));
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
