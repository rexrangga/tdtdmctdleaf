package draughts;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MovesFinder {

	private static final int BOARD_SIZE = 10;
	private final Checker[][] m_board;
	private final Author author;

	/**
	 * Creates a new instance of MovesFinder which will return possible moves for specified player in specified board
	 * position.
	 * 
	 * @param board
	 *            checkers positions. This class makes a copy of constructors argument so <code>board</code> will not be
	 *            modified by it and can be kept by the caller
	 * @param author
	 *            author equal to <code>Author.owner</code> means that current player is playing white, otherwise
	 *            current player is playing black
	 */
	public MovesFinder(Checker[][] board, Author author) {
		if (board == null || author == null) {
			throw new IllegalArgumentException();
		}
		this.m_board = makeCopy(board);
		this.author = author;
	}

	/**
	 * Returns set of possible moves in given board situation. Each element of the set describes one sequence of moves.
	 * A sequence may consist of one or more single moves. A sequence consists of more than one move if and only if each
	 * of its moves is a beating. This method implements the following checkers rules:
	 * <ul>
	 * <li>beatings are obligatory
	 * <li>if there is more than one sequence of beatings, sequence with most beatings must be chosen
	 * <li>if several sequences of beating are tied for most number of beatings, any of them may be chosen
	 * </ul>
	 * If there are no legal moves for a player in given position, this method returns an empty set.
	 * 
	 * @return
	 */
	public Set<List<MoveMessage>> getPossibleMoves() {
		return getPossibleMoves(true);
	}

	/**
	 * Returns set of possible moves in given board situation. Each element of the set describes one sequence of moves.
	 * A sequence may consist of one or more single moves. A sequence consists of more than one moves if and only if
	 * each of its moves is a beating. This method implements the following checkers rules:
	 * <ul>
	 * <li>beatings are obligatory
	 * <li>if there is more than one sequence of beatings, sequence with most beatings must be chosen
	 * <li>if several sequences of beating are tied for most number of beatings, any of them may be chosen
	 * </ul>
	 * Also, this method will return non-beating moves if and only if parameter <code>lookForNonBeatings</code> is true.
	 * If there are no legal moves for a player in given position, this method returns an empty set.
	 * 
	 * @param lookForNonBeatings
	 * @return
	 */
	public Set<List<MoveMessage>> getPossibleMoves(boolean lookForNonBeatings) {
		Sort myRegular = Author.owner.equals(author) ? Sort.fullWhite : Sort.fullBlack;
		Sort myQueen = Author.owner.equals(author) ? Sort.queenWhite : Sort.queenBlack;
		EnumSet<Sort> mySorts = EnumSet.of(myQueen, myRegular);

		boolean foundBeating = false;
		int maxLength = 0;
		Set<List<MoveMessage>> result = new HashSet<List<MoveMessage>>();

		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				Checker currChecker = m_board[i][j];
				if (mySorts.contains(currChecker)) {
					Checker[][] board = makeCopy(m_board);
					List<Checker> beatings = checkBeating(currChecker, board);
					if ((beatings == null || beatings.isEmpty()) && !foundBeating && lookForNonBeatings) {
						List<Checker> nonBeatings = checkFree(currChecker, board);
						for (Checker nonBeating : nonBeatings) {
							MoveMessage mm = new MoveMessage(new Checker(currChecker), new Checker(nonBeating), author,
									true);
							List<MoveMessage> newResult = new ArrayList<MoveMessage>(1);
							newResult.add(mm);
							maxLength = updateResult(result, maxLength, newResult);
						}
					} else {
						if (!foundBeating) {
							foundBeating = true;
							result.clear();
						}
						for (Checker beating : beatings) {
							MoveMessage mm = new MoveMessage(new Checker(currChecker), new Checker(beating), author,
									false);
							int iBeat = (currChecker.getI() + beating.getI()) / 2;
							int jBeat = (currChecker.getJ() + beating.getJ()) / 2;
							Checker[][] copy = makeCopy(board);
							copy[iBeat][jBeat].setKind(Sort.blankBlack);
							MovesFinder helper = new MovesFinder(copy, author);
							Set<List<MoveMessage>> helpResult = helper.getPossibleMoves(false);
							if (helpResult == null || helpResult.isEmpty()) {
								mm.setEndsTurn(true);
								List<MoveMessage> newResult = new ArrayList<MoveMessage>(1);
								newResult.add(mm);
								maxLength = updateResult(result, maxLength, newResult);
							} else {
								for (List<MoveMessage> list : helpResult) {
									List<MoveMessage> newResult = new ArrayList<MoveMessage>(list.size() + 1);
									MoveMessage newMessage = new MoveMessage(new Checker(mm.getFirst()), new Checker(
											mm.getSecond()), mm.getMAuthor(), mm.isEndsTurn());
									newResult.add(newMessage);
									newResult.addAll(list);
									maxLength = updateResult(result, maxLength, newResult);
								}
							}
						}
					}
				}
			}
		}
		return result;
	}

	private int updateResult(Set<List<MoveMessage>> results, int maxLength, List<MoveMessage> newResult) {
		if (maxLength > newResult.size()) {
			return maxLength;
		}
		if (maxLength == newResult.size()) {
			results.add(newResult);
			return maxLength;
		}
		// we found a longer result (longer beating sequence)
		results.clear();
		results.add(newResult);
		return results.size();
	}

	private Checker[][] makeCopy(Checker[][] board) {
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

	private ArrayList<Checker> checkBeating(Checker jb, Checker[][] board) {
		ArrayList<Checker> beatingMoves = new ArrayList<Checker>();
		int i = jb.getI();
		int j = jb.getJ();
		Sort opponent1, opponent2, supporter1, supporter2;

		if (jb.getKind() == Sort.fullWhite || jb.getKind() == Sort.queenWhite) {
			opponent1 = Sort.fullBlack;
			opponent2 = Sort.queenBlack;
			supporter1 = Sort.fullWhite;
			supporter2 = Sort.queenWhite;
		} else {
			opponent1 = Sort.fullWhite;
			opponent2 = Sort.queenWhite;
			supporter1 = Sort.fullBlack;
			supporter2 = Sort.queenBlack;
		}

		if (jb.getKind() == Sort.queenWhite || jb.getKind() == Sort.queenBlack) {
			for (int p = 2; p < 10; p++) {
				if (i + p < 10 && j + p < 10) {
					if (board[i + p - 1][j + p - 1].getKind() == supporter1
							|| board[i + p - 1][j + p - 1].getKind() == supporter2) {
						break;
					}
					if (board[i + p - 1][j + p - 1].getKind() == opponent1
							|| board[i + p - 1][j + p - 1].getKind() == opponent2) {
						if (board[i + p][j + p].getKind() != Sort.blankBlack) {
							break;
						}
						beatingMoves.add(board[i + p][j + p]);
						int q = 1;
						while (i + p + q < 10 && j + p + q < 10
								&& board[i + p + q][j + p + q].getKind() == Sort.blankBlack) {
							beatingMoves.add(board[i + p + q][j + p + q]);
							q++;
						}
						break;
					}
				}
			}
			for (int p = 2; p < 10; p++) {
				if (i + p < 10 && j - p >= 0) {
					if (board[i + p - 1][j - p + 1].getKind() == supporter1
							|| board[i + p - 1][j - p + 1].getKind() == supporter2) {
						break;
					}
					if (board[i + p - 1][j - p + 1].getKind() == opponent1
							|| board[i + p - 1][j - p + 1].getKind() == opponent2) {
						if (board[i + p][j - p].getKind() != Sort.blankBlack) {
							break;
						}
						beatingMoves.add(board[i + p][j - p]);
						int q = 1;
						while (i + p + q < 10 && j - p - q >= 0
								&& board[i + p + q][j - p - q].getKind() == Sort.blankBlack) {
							beatingMoves.add(board[i + p + q][j - p - q]);
							q++;
						}
						break;
					}
				}
			}
			for (int p = 2; p < 10; p++) {
				if (i - p >= 0 && j + p < 10) {
					if (board[i - p + 1][j + p - 1].getKind() == supporter1
							|| board[i - p + 1][j + p - 1].getKind() == supporter2) {
						break;
					}
					if (board[i - p + 1][j + p - 1].getKind() == opponent1
							|| board[i - p + 1][j + p - 1].getKind() == opponent2) {
						if (board[i - p][j + p].getKind() != Sort.blankBlack) {
							break;
						}
						beatingMoves.add(board[i - p][j + p]);
						int q = 1;
						while (i - p - q >= 0 && j + p + q < 10
								&& board[i - p - q][j + p + q].getKind() == Sort.blankBlack) {
							beatingMoves.add(board[i - p - q][j + p + q]);
							q++;
						}
						break;
					}
				}
			}
			for (int p = 2; p < 10; p++) {
				if (i - p >= 0 && j - p >= 0) {
					if (board[i - p + 1][j - p + 1].getKind() == supporter1
							|| board[i - p + 1][j - p + 1].getKind() == supporter2) {
						break;
					}
					if (board[i - p + 1][j - p + 1].getKind() == opponent1
							|| board[i - p + 1][j - p + 1].getKind() == opponent2) {
						if (board[i - p][j - p].getKind() != Sort.blankBlack) {
							break;
						}
						beatingMoves.add(board[i - p][j - p]);
						int q = 1;
						while (i - p - q >= 0 && j - p - q >= 0
								&& board[i - p - q][j - p - q].getKind() == Sort.blankBlack) {
							beatingMoves.add(board[i - p - q][j - p - q]);
							q++;
						}
						break;
					}
				}
			}
		} else {
			if (i - 2 >= 0 && j - 2 >= 0) {
				if ((board[i - 1][j - 1].getKind() == opponent1 || board[i - 1][j - 1].getKind() == opponent2)
						&& board[i - 2][j - 2].getKind() == Sort.blankBlack) {
					beatingMoves.add(board[i - 2][j - 2]);
				}

			}
			if (i - 2 >= 0 && j + 2 < 10) {
				if ((board[i - 1][j + 1].getKind() == opponent1 || board[i - 1][j + 1].getKind() == opponent2)
						&& board[i - 2][j + 2].getKind() == Sort.blankBlack) {
					beatingMoves.add(board[i - 2][j + 2]);
				}

			}
			if (i + 2 < 10 && j - 2 >= 0) {
				if ((board[i + 1][j - 1].getKind() == opponent1 || board[i + 1][j - 1].getKind() == opponent2)
						&& board[i + 2][j - 2].getKind() == Sort.blankBlack) {
					beatingMoves.add(board[i + 2][j - 2]);
				}

			}
			if (i + 2 < 10 && j + 2 < 10) {
				if ((board[i + 1][j + 1].getKind() == opponent1 || board[i + 1][j + 1].getKind() == opponent2)
						&& board[i + 2][j + 2].getKind() == Sort.blankBlack) {
					beatingMoves.add(board[i + 2][j + 2]);
				}

			}
		}
		return beatingMoves;
	}

	/**
	 * Returns a list of board fields on which specified checker can move without beating opponents checker.
	 * 
	 * @param jb
	 * @return
	 */
	private List<Checker> checkFree(Checker jb, Checker[][] board) {

		List<Checker> freeMoves = new ArrayList<Checker>();
		// freeMoves.clear();
		int i = jb.getI();
		int j = jb.getJ();
		if (jb.getKind() == Sort.fullWhite) {
			if (i - 1 >= 0 && j - 1 >= 0) {
				if (board[i - 1][j - 1].getKind() == Sort.blankBlack) {
					freeMoves.add(board[i - 1][j - 1]);
				}
			}
			if (i - 1 >= 0 && j + 1 < 10) {
				if (board[i - 1][j + 1].getKind() == Sort.blankBlack) {
					freeMoves.add(board[i - 1][j + 1]);
				}
			}
		} else if (jb.getKind() == Sort.fullBlack) {
			if (i + 1 < 10 && j - 1 >= 0) {
				if (board[i + 1][j - 1].getKind() == Sort.blankBlack) {
					freeMoves.add(board[i + 1][j - 1]);
				}
			}
			if (i + 1 < 10 && j + 1 < 10) {
				if (board[i + 1][j + 1].getKind() == Sort.blankBlack) {
					freeMoves.add(board[i + 1][j + 1]);
				}
			}
		} else if (jb.getKind() == Sort.queenWhite || jb.getKind() == Sort.queenBlack) {
			for (int p = 1; p < 10; p++) {
				if (i + p < 10 && j + p < 10) {
					if (board[i + p][j + p].getKind() == Sort.blankBlack) {
						freeMoves.add(board[i + p][j + p]);
					} else {
						break;
					}
				}
			}
			for (int p = 1; p < 10; p++) {
				if (i + p < 10 && j - p >= 0) {
					if (board[i + p][j - p].getKind() == Sort.blankBlack) {
						freeMoves.add(board[i + p][j - p]);
					} else {
						break;
					}
				}
			}
			for (int p = 1; p < 10; p++) {
				if (i - p >= 0 && j + p < 10) {
					if (board[i - p][j + p].getKind() == Sort.blankBlack) {
						freeMoves.add(board[i - p][j + p]);
					} else {
						break;
					}
				}
			}
			for (int p = 1; p < 10; p++) {
				if (i - p >= 0 && j - p >= 0) {
					if (board[i - p][j - p].getKind() == Sort.blankBlack) {
						freeMoves.add(board[i - p][j - p]);
					} else {
						break;
					}
				}
			}
		}
		return freeMoves;
	}
}
