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
    public Grid grid1;
    public Grid grid2;
    public MouseListener listen1;
    public MouseMotionListener motion1;
    public MouseListener listen2;
    public MouseMotionListener motion2;

    private GridPoint point = null;
    private JPanel panel;
    public ImgView leftView;
    public BufferedImage leftImage;
    public ImgView rightView;
    public BufferedImage rightImage;

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
                    if (point != null) {
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
                    if (point != null) {
                        point.SetXY(e.getX(), e.getY());
                        grid2.Redraw();
                    }
                }
            }
            @Override
            public void mouseMoved(MouseEvent e) {}
        };
        panel = new JPanel();
        grid1 = new Grid(listen1, motion1);
        grid2 = new Grid(listen2, motion2);
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("JMorph");
        GridManager manage = new GridManager();

        final JFileChooser fc = new JFileChooser(".");
        JMenuBar bar = new JMenuBar();
        frame.setJMenuBar(bar);

        JMenu fileMenu = new JMenu("File");
        JMenuItem openLeft = new JMenuItem("Open Left Image");
        JMenuItem openRight = new JMenuItem("Open Right Image");
        JMenuItem fileExit = new JMenuItem("Exit");

        // action listener to change the left image
        // not implementable yet
        /* openLeft.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        int returnVal = fc.showOpenDialog(GridManager.this);

                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            File file = fc.getSelectedFile();
                            try {
                                leftImage = ImageIO.read(file);
                            } catch(IOException e1) {};
                            leftView.setImage(leftImage);
                            leftView.showImage();
                        }
                    }
                }
        );
        openRight.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int returnVal = fc.showOpenDialog(GridManager.this);

                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        try {
                            rightImage = ImageIO.read(file);
                        } catch (IOException e1) {
                        }
                        ;
                        rightView.setImage(rightImage);
                        rightView.showImage();
                    }
                }
            }
        );*/
        fileExit.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                }
        );

        fileMenu.add(openLeft);
        fileMenu.add(openRight);
        fileMenu.add(fileExit);
        bar.add(fileMenu);
        // The following creates the slider to adjust the FPS
        JLabel FpsLabel = new JLabel("Frames per second: 30");
        JSlider FpsSlider = new JSlider(SwingConstants.VERTICAL, 1, 60, 30);

        FpsSlider.setMajorTickSpacing(5);
        FpsSlider.setPaintTicks(true);
        FpsSlider.addChangeListener(
                new ChangeListener() {
                    public void stateChanged(ChangeEvent e) {
                        FpsLabel.setText("Frames per Second: " +
                                Integer.toString(FpsSlider.getValue()));
                    }
                }
        );

        JButton preview = new JButton("Preview");
        JButton reset = new JButton("Reset");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500,500);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.LINE_AXIS));

        // adding elements to the JFrame
        frame.getContentPane().add(FpsLabel);
        frame.getContentPane().add(Box.createRigidArea(new Dimension(10,0)));
        frame.getContentPane().add(FpsSlider);
        frame.getContentPane().add(Box.createRigidArea(new Dimension(5,0)));
        frame.getContentPane().add(manage.grid1);
        frame.getContentPane().add(Box.createRigidArea(new Dimension(5,0)));
        frame.getContentPane().add(manage.grid2);
        frame.getContentPane().add(preview);
        frame.getContentPane().add(reset);
        frame.setVisible(true);

        // shows the animation in a new popup window
        preview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Grid preview_grid = new Grid();
                JFrame popup = new JFrame("Preview");
                //JOptionPane.showMessageDialog(null, preview_gr, "Preview", JOptionPane.OK_CANCEL_OPTION);
                popup.setSize(400,400);
                popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                popup.add(preview_grid);
                popup.setVisible(true);

                preview_grid.Morph(manage.grid1, manage.grid2, FpsSlider.getValue(), 1);
            }
        });
        // resets the right grid
        reset.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               manage.grid2 = new Grid();
               //manage.grid2 = new Grid();
            }
        });
    }
}
