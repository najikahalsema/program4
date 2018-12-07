import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.imageio.*;
import java.io.*;
import java.util.ArrayList;
import java.lang.Math;

public class GridManager {
    public GridImage grid1;
    public GridImage grid2;
    public MouseListener listen1;
    public MouseMotionListener motion1;
    public MouseListener listen2;
    public MouseMotionListener motion2;
    public String file1;
    public String file2;
    private GridPoint point = null;

    public GridManager() {
        listen1 = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {
                point = grid1.GetClickedPoint(e);

                if (point != null) {
                    grid1.SetDragging(true);
                    point.SetColor(Color.RED);
                    //Set corresponding point
                    int row = point.GetRow();
                    int column = point.GetColumn();
                    GridPoint match = grid2.GetMatchingPoint(row, column);

                    if (match != null) {
                        match.SetColor(Color.RED);
                    }
                }
                grid1.Redraw();
                grid2.Redraw();
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                grid1.SetDragging(false);
                grid2.SetDragging(false);
                grid1.SetLock(false);
                grid2.SetLock(false);

                if (point != null) {
                    //point.SetColor(Color.BLACK);
                    int row = point.GetRow();
                    int column = point.GetColumn();

                    GridPoint point1 = grid1.GetMatchingPoint(row, column);
                    GridPoint point2 = grid2.GetMatchingPoint(row, column);

                    point1.SetColor(Color.BLACK);
                    point2.SetColor(Color.BLACK);
                }
                point = null;
            }
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        };

        motion1 = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (grid1.IsDragging()) {
                    if (grid1.IsLocked() == false) {
                        point = grid1.GetClickedPoint(e);
                        grid1.SetLock(true);
                    }
                    if (point != null && point.IsOnEdge() == false) {
                        point.SetXY(e.getX(), e.getY());
                        grid1.Redraw();
                    }
                }
            }
            @Override
            public void mouseMoved(MouseEvent e) {}
        };

        listen2 = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {
                point = grid2.GetClickedPoint(e);

                if (point != null) {
                    grid2.SetDragging(true);
                    point.SetColor(Color.RED);
                    //Set corresponding point
                    int row = point.GetRow();
                    int column = point.GetColumn();
                    GridPoint match = grid1.GetMatchingPoint(row, column);

                    // set point to active colour red if we find a corresponding point
                    if (match != null) {
                        match.SetColor(Color.RED);
                    }
                }
                grid1.Redraw();
                grid2.Redraw();
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                grid1.SetDragging(false);
                grid2.SetDragging(false);
                grid1.SetLock(false);
                grid2.SetLock(false);

                if (point != null) {
                    //point.SetColor(Color.BLACK);
                    int row = point.GetRow();
                    int column = point.GetColumn();

                    GridPoint point1 = grid1.GetMatchingPoint(row, column);
                    GridPoint point2 = grid2.GetMatchingPoint(row, column);

                    point1.SetColor(Color.BLACK);
                    point2.SetColor(Color.BLACK);
                }
                point = null;
            }
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        };

        motion2 = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (grid2.IsDragging()) {
                    if (grid2.IsLocked() == false) {
                        point = grid2.GetClickedPoint(e);
                        grid2.SetLock(true);
                    }
                    if (point != null && point.IsOnEdge() == false) {
                        point.SetXY(e.getX(), e.getY());
                        grid2.Redraw();
                    }
                }
            }
            @Override
            public void mouseMoved(MouseEvent e) {}
        };
        grid1 = new GridImage(listen1, motion1, "/home/firstlane/CS335/Program4/ship.jpg");
        grid2 = new GridImage(listen2, motion2, "/home/firstlane/CS335/Program4/ship.jpg");
    }
}
