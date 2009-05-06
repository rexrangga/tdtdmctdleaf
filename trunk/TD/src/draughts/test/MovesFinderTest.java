package draughts.test;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Assert;

import draughts.Author;
import draughts.Checker;
import draughts.MoveMessage;
import draughts.MovesFinder;
import draughts.Sort;

public class MovesFinderTest {

	private static final int BOARD_SIZE = 10;

	private static Checker[][] createEmptyBoard() {
		Checker[][] board = new Checker[BOARD_SIZE][];
		for (int i = 0; i < BOARD_SIZE; i++) {
			Checker[] temp = new Checker[BOARD_SIZE];
			for (int j = 0; j < BOARD_SIZE; j++) {
				temp[j] = new Checker(i, j);
				if ((i + j) % 2 != 0) {
					temp[j].setKind(Sort.blankBlack);
				} else {
					temp[j].setKind(Sort.blankWhite);
				}
			}
			board[i] = temp;
		}
		return board;
	}

	private static void testEmpty() {
		Checker[][] board = createEmptyBoard();
		Set<?> s = new MovesFinder(board, Author.owner).getLegalMoves();
		Assert.assertTrue(s.isEmpty());
		System.out.println("+ success");
	}

	/**
	 * A checker on empty board - one possible move
	 */
	private static void testSimple() {
		Checker[][] board = createEmptyBoard();
		board[9][0].setKind(Sort.fullWhite);
		Set<List<MoveMessage>> s = new MovesFinder(board, Author.owner).getLegalMoves();
		Assert.assertNotNull(s);
		Assert.assertTrue(s.size() == 1);
		List<MoveMessage> list = s.iterator().next();
		Assert.assertNotNull(list);
		Assert.assertTrue(list.size() == 1);
		MoveMessage mm = list.get(0);
		Assert.assertTrue(mm.getFirst().getI() == 9);
		Assert.assertTrue(mm.getFirst().getJ() == 0);
		Assert.assertTrue(mm.getSecond().getI() == 8);
		Assert.assertTrue(mm.getSecond().getJ() == 1);
		System.out.println("+ success");
	}

	private static void testSimple2() {
		Checker[][] board = createEmptyBoard();
		board[9][2].setKind(Sort.fullWhite);
		Set<List<MoveMessage>> s = new MovesFinder(board, Author.owner).getLegalMoves();
		Assert.assertNotNull(s);
		Assert.assertTrue(s.size() == 2);
		Iterator<List<MoveMessage>> it = s.iterator();
		List<MoveMessage> list = it.next();
		Assert.assertNotNull(list);
		Assert.assertTrue(list.size() == 1);
		list = it.next();
		Assert.assertNotNull(list);
		Assert.assertTrue(list.size() == 1);
		System.out.println("+ success");
	}

	private static void testBeating() {
		Checker[][] board = createEmptyBoard();
		board[9][2].setKind(Sort.fullWhite);
		board[8][3].setKind(Sort.fullBlack);
		Set<List<MoveMessage>> s = new MovesFinder(board, Author.owner).getLegalMoves();
		Assert.assertNotNull(s);
		Assert.assertTrue(s.size() == 1);
		Iterator<List<MoveMessage>> it = s.iterator();
		List<MoveMessage> list = it.next();
		Assert.assertNotNull(list);
		Assert.assertTrue(list.size() == 1);
		MoveMessage mm = list.get(0);
		Assert.assertEquals(mm.getFirst().getI(), 9);
		Assert.assertEquals(mm.getFirst().getJ(), 2);
		Assert.assertEquals(mm.getSecond().getI(), 7);
		Assert.assertEquals(mm.getSecond().getJ(), 4);
		System.out.println("+ success");
	}

	private static void testTwoBeatings() {
		Checker[][] board = createEmptyBoard();
		board[9][2].setKind(Sort.fullWhite);
		board[8][3].setKind(Sort.fullBlack);
		board[8][1].setKind(Sort.fullBlack);
		board[6][5].setKind(Sort.fullBlack);
		Set<List<MoveMessage>> s = new MovesFinder(board, Author.owner).getLegalMoves();
		Assert.assertNotNull(s);
		Assert.assertTrue(s.size() == 1);
		Iterator<List<MoveMessage>> it = s.iterator();
		List<MoveMessage> list = it.next();
		Assert.assertNotNull(list);
		Assert.assertTrue(list.size() == 2);
		MoveMessage mm = list.get(0);
		Assert.assertEquals(9, mm.getFirst().getI());
		Assert.assertEquals(2, mm.getFirst().getJ());
		Assert.assertEquals(7, mm.getSecond().getI());
		Assert.assertEquals(4, mm.getSecond().getJ());
		mm = list.get(1);
		Assert.assertEquals(7, mm.getFirst().getI());
		Assert.assertEquals(4, mm.getFirst().getJ());
		Assert.assertEquals(5, mm.getSecond().getI());
		Assert.assertEquals(6, mm.getSecond().getJ());
		System.out.println("+ success");
	}

	public static void main(String[] args) {
		testEmpty();
		testSimple();
		testSimple2();
		testBeating();
		testTwoBeatings();
	}
}
