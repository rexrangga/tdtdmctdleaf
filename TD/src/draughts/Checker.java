package draughts;



import javax.swing.JButton;

import draughts.Sort;


/**
 * Definiuje pole w grze.
 */
public class Checker extends JButton {

    private Sort kind;
    private int i;
    private int j;

    /**
     * Tworzy pole bez ustalonej pozycji i typu.
     */
    public Checker() {
        super();
    }

    /**
     * Tworzy pole o danych pozycjach.
     * @param i Wiersz pola.
     * @param j Kolumna pola.
     */
    public Checker(int i, int j) {
        super();
        this.i = i;
        this.j = j;
    }

    /**
     * Tworzy pole o pozycji i typie pola danego.
     * @param x Pole, z którego powielamy dane.
     */
    public Checker(Checker x) {
        super();
        this.i = x.i;
        this.j = x.j;
        this.kind = x.kind;
    }

    /**
     * Określa czy obiekt ma te same położenie co obiekt przekazany jako argument.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Checker) {
            return (i == ((Checker) obj).i && j == ((Checker) obj).j);
        } else {
            return false;
        }
    }

    /**
     * Zwraca hash kod obiektu.
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.i;
        hash = 97 * hash + this.j;
        return hash;
    }

    /**
     * Zwraca wiersz. Numery wierszy rosną w dół i są w zakresie <0,7>.
     * @return Wiersz pola.
     */
    public int getI() {
        return i;
    }

    /**
     * Ustawia wiersz. Numery wierszy rosną w dół i są w zakresie <0,7>.
     * @param i Wiersz pola.
     */
    public void setI(int i) {
        this.i = i;
    }

    /**
     * Zwraca kolumnę. Numery kolumn rosną w prawo i są w zakresie <0,7>.
     * @return Kolumna pola.
     */
    public int getJ() {
        return j;
    }

    /**
     * Ustawia kolumnę. Numery kolumn rosną w prawo i są w zakresie <0,7>.
     * @param j Kolumna pola.
     */
    public void setJ(int j) {
        this.j = j;
    }

    /**
     * Zwraca typ.
     * @return Typ pola.
     */
    public Sort getKind() {
        return kind;
    }

    /**
     * Ustawia typ.
     * @param kind Typ pola.
     */
    public void setKind(Sort kind) {
        this.kind = kind;
    }
}
