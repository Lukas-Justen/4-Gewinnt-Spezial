package de.thbingen.movs.lukas.a4gewinntspezial.game;

import android.graphics.Point;

/**
 * @author Lukas Justen lukas.justen@th-bingen.de
 * @version 1.0
 *          <p>
 *          Created on 17.06.2017
 *          <p>
 *          Die Klasse Game erfüllt alle Aufgaben und Funktionen zur Einhaltung der Spielregeln.
 *          Zusätzlich werden alle Informationen wie zum Beispiel Belegung des Spielfelds oder
 *          welcher Spieler gerade an der Reihe ist gespeichert. Im Falle eines Siegers kann
 *          zusätzlich die Position der Spielsteine mit der der Spieler gewonnen hat ausgegeben und
 *          angezeigt werden.
 */
class Game {

    private enum Player {P1, P2}

    private final int COLUMNS;
    private final int ROWS;
    private Player turn = Player.P1;
    private Player[][] positions;
    private Point[] winPositions = new Point[4];

    public Game() {
        this(7, 6);
    }

    public Game(int columns, int rows) {
        this.COLUMNS = columns;
        this.ROWS = rows;
        positions = new Player[COLUMNS][ROWS];
    }

    /**
     * Überprüft, ob der Spieler den Stein in die angegebene Spalte überhaupt einwerfen kann.
     *
     * @param column Die Spalte in die man den Spielstein wirft.
     * @return -1, wenn der Spielstein nicht eingeworfen werden kann, sonst die Höhe von 0 an.
     */
    int insert(int column) {
        int row = 0;
        while (row + 1 < ROWS && positions[column][row + 1] == null)
            row++;

        if (positions[column][row] == null) {
            positions[column][row] = turn;
            return row;
        }

        return -1;
    }

    /**
     * Überprüft, ob der aktuelle Spieler bereits gewonnen hat.
     *
     * @return true, wenn einer gewonnen hat, sonst false
     */
    boolean checkWin() {
        // Horizontal
        for(int i = 0; i < positions[0].length; i++)
        {
            int c = 0;
            for(int x = 0; x < 7; x++)
            {
                Player[] p = positions[x];
                if( p[i] == turn )
                {
                    winPositions[c] = new Point(x,i);
                    c++;
                }
                else
                    c = 0;

                if( c >= 4 )
                    return true;
            }
        }

        // Vertikal
        for(int x = 0; x < 7; x++)
        {
            Player[] pc = positions[x];
            int c = 0;
            for(int y = 0; y < 6; y++)
            {
                Player p = pc[y];
                if( p == turn )
                {
                    winPositions[c] = new Point(x, y);
                    c++;
                }
                else
                    c = 0;

                if( c >= 4 )
                    return true;
            }
        }
        // Diagonal - top left to bottom right
        for(int i = 0; i < 3; i++)
        {
            int c = 0;
            for(int x = 0; x <= 5-i; x++)
            {
                int y = i+x;
                if( positions[x][y] == turn )
                {
                    winPositions[c] = new Point(x, y);
                    c++;
                }
                else
                    c = 0;

                if( c >= 4 )
                    return true;
            }
        }
        for(int i = 1; i <= 3; i++)
        {
            int c = 0;
            for(int y = 0; y <= 6-i; y++)
            {
                int x = i+y;
                if( positions[x][y] == turn )
                {
                    winPositions[c] = new Point(x, y);
                    c++;
                }
                else
                    c = 0;

                if( c >= 4 )
                    return true;
            }
        }
        // Diagonal - bottom left to top right
        for(int i = 0; i < 3; i++)
        {
            int c = 0;
            for(int x = 0; x <= 5-i; x++)
            {
                int y = i+x;
                if( positions[x][5-y] == turn )
                {
                    winPositions[c] = new Point(x, 5-y);
                    c++;
                }
                else
                    c = 0;

                if( c >= 4 )
                    return true;
            }
        }
        for(int i = 1; i <= 3; i++)
        {
            int c = 0;
            for(int y = 0; y <= 6-i; y++)
            {
                int x = i+y;
                if( positions[x][5-y] == turn )
                {
                    winPositions[c] = new Point(x, 5-y);
                    c++;
                }
                else
                    c = 0;

                if( c >= 4 )
                    return true;
            }
        }
        return false;
    }

    /**
     * Lässt den nächsten Spieler an die Reihe kommen.
     */
    void nextPlayer() {
        turn = turn == Player.P1 ? Player.P2 : Player.P1;
    }

    Player getPlayerTurn() {
        return turn;
    }

    public Point[] getWinPositions()
    {
        return winPositions;
    }
}