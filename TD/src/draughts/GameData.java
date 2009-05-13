package draughts;

import java.util.ArrayList;
import java.util.List;

/*
 * Information about game played.
 */
public class GameData {

	public GameData() {
		evaluationFunctionFeatures = new ArrayList<double[]>();
		m_board = new ArrayList<CheckerModel[][]>();
	}

	/**
	 * Holds evaluation function features values for every time moment t =
	 * 1,2.....T.
	 */
	public List<double[]> evaluationFunctionFeatures;

	/**
	 * Holds board situations after each move.
	 */
	public List<CheckerModel[][]> m_board;

	/**
	 * Holds information what kind of checkers was the algorithm using.
	 */
	public Author playerCheckersSort;

	/**
	 * Specifies the kind of Checkers which started the game
	 */
	public Author startingCheckersSort;
}
