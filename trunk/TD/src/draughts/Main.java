package draughts;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import draughts.test.HeadlessGame;

/**
 * Główna klasa aplikacji, która uruchamia okno gry.
 */
/**
 * @author Kamil
 * 
 */
public class Main {
	private static final boolean LEARNING_MODE = true;
	/**
	 * Otwiera główne okno gry.
	 * 
	 * @param args
	 *            Argumenty wywołania.
	 */

	public static void main(String[] args) {
		/*
		 * double[] weights = { 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5,
		 * 0.5, 0.5, 0.5 }; ArrayList<double[]> features = new
		 * ArrayList<double[]>(); TDMC tdmc = new TDMC(weights, 0.5, 0.1, 0.1,
		 * 0.5, 0.5); GameData gd = new GameData();
		 * 
		 * double[] features1 = { 2, 2, 2, 3, 4, 2, 2, 2, 1, -1, 0, 1 };
		 * double[] features2 = { 2, 2, 2, 3, 4, 2, 2, 2, 1, -1, 0, 1 };
		 * double[] features3 = { 2, 2, 2, 3, 4, 2, 2, 2, 1, -1, 0, 1 };
		 * double[] features4 = { 2, 2, 2, 3, 4, 2, 2, 2, 1, -1, 0, 1 };
		 * double[] features5 = { 2, 2, 2, 3, 4, 2, 2, 2, 1, -1, 0, 1 };
		 * 
		 * gd.evaluationFunctionFeatures.add(features1);
		 * gd.evaluationFunctionFeatures.add(features2);
		 * gd.evaluationFunctionFeatures.add(features3);
		 * gd.evaluationFunctionFeatures.add(features4);
		 * gd.evaluationFunctionFeatures.add(features5);
		 * 
//		 * gd.m_board = null; gd.playerCheckersSort = Author.opponent;
		 * gd.startingCheckersSort = Author.opponent;
		 * 
		 * tdmc.updateWeights(gd);
		 */
		if (LEARNING_MODE) {
			HeadlessGame headlessGame=new HeadlessGame();
			headlessGame.play();
		} else {
			java.awt.EventQueue.invokeLater(new Runnable() {

				public void run() {
					Frame f = new Frame();
					f.setVisible(true);
					try {
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						SwingUtilities.updateComponentTreeUI(f);
					} catch (Exception e) {
						System.out.println(e.toString());
					}
				}
			});
		}
	}
}
