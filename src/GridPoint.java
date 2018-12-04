/* Class which draws the points in the grid.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

public class GridPoint {
    private int x;
    private int y;
    private int row;
    private int column;
    private Color color = Color.BLACK;

    // Constructor
    public GridPoint(int x, int y, int row, int column) {
        this.x = x;
        this.y = y;
        this.row = row;
        this.column = column;
    }

    public int GetX() {
        return x;
    }
    public int GetY() {
        return y;
    }

    public void SetX(int x) {
        this.x = x;
    }
    public void SetY(int y) {
        this.y = y;
    }

    public void SetXY(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Color GetColor() {
        return color;
    }
    public void SetColor(Color newcolor) {
        color = newcolor;
    }

    public int GetRow() {
        return row;
    }
    public int GetColumn() {
        return column;
    }
}
