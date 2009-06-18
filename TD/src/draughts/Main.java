package draughts;

import java.io.IOException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import draughts.test.PlayerUtils;
import draughts.test.PlayerUtils.PlayerKind;

/**
 * Główna klasa aplikacji, która uruchamia okno gry.
 */
/**
 * @author Kamil
 * 
 */
public class Main {

	/**
	 * Otwiera główne okno gry.
	 * 
	 * @param args
	 *            Argumenty wywołania.
	 */

	public static void main(String[] args) {
		/*
		 * double[] weights = { 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5 }; ArrayList<double[]>
		 * features = new ArrayList<double[]>(); TDMC tdmc = new TDMC(weights, 0.5, 0.1, 0.1, 0.5, 0.5); GameData gd =
		 * new GameData();
		 * 
		 * double[] features1 = { 2, 2, 2, 3, 4, 2, 2, 2, 1, -1, 0, 1 }; double[] features2 = { 2, 2, 2, 3, 4, 2, 2, 2,
		 * 1, -1, 0, 1 }; double[] features3 = { 2, 2, 2, 3, 4, 2, 2, 2, 1, -1, 0, 1 }; double[] features4 = { 2, 2, 2,
		 * 3, 4, 2, 2, 2, 1, -1, 0, 1 }; double[] features5 = { 2, 2, 2, 3, 4, 2, 2, 2, 1, -1, 0, 1 };
		 * 
		 * gd.evaluationFunctionFeatures.add(features1); gd.evaluationFunctionFeatures.add(features2);
		 * gd.evaluationFunctionFeatures.add(features3); gd.evaluationFunctionFeatures.add(features4);
		 * gd.evaluationFunctionFeatures.add(features5);
		 * 
		 * // gd.m_board = null; gd.playerCheckersSort = Author.opponent; gd.startingCheckersSort = Author.opponent;
		 * 
		 * tdmc.updateWeights(gd);
		 */
		final String[] finalArgs = args;
		java.awt.EventQueue.invokeLater(new Runnable() {

			public void run() {
				Frame f = new Frame();
				f.setVisible(true);
				if (finalArgs.length > 0 && finalArgs[0].trim().length() > 0) {
					f.setArtificialGame(true);

					try {
						f.setItd(PlayerUtils.loadPlayer(finalArgs[0], PlayerKind.NO_LEARNING));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
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
