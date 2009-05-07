package draughts;


import javax.swing.SwingUtilities;
import javax.swing.UIManager;


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
     * @param args Argumenty wywołania.
     */
    public static void main(String[] args) {

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
