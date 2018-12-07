/* This class draws the 10x10 grid and the image. It also handles the animation and
 * movement of the grid's points.
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.ArrayList;
import java.lang.Math;

public class GridImage extends JPanel {
    private GridPoint[][] points;   //i = x, j = y
    private ArrayList<Edge> edges;
    private boolean isDragging = false;
    private GridPoint point = null;
    private boolean lock_selection = false;
    private int count = 1;
    Timer timer;

    public BufferedImage bim = null;
    // marking for intensity shift
    public BufferedImage filteredbim = null;
    private boolean showfiltered = false;

    // base constructor
    public GridImage(String file) {
        super();

        BufferedImage img = readImage(file);
        bim = img;
        filteredbim = new BufferedImage(
                bim.getWidth(), bim.getHeight(), BufferedImage.TYPE_INT_ARGB);

        //this.setSize(new Dimension(bim.getWidth(), bim.getHeight()));
        points = new GridPoint[10][10];

        //Create 10x10 grid of points. i = rows, j = columns
        for (int i = 0; i < 500; i += 50) {
            for (int j = 0; j < 500; j+= 50) {
                GridPoint point = new GridPoint(i, j, i / 50, j / 50);
                points[i / 50][j / 50] = point;
            }
        }
        filteredbim = resize(filteredbim);
        bim = resize(bim);
        this.setPreferredSize(new Dimension(bim.getWidth(), bim.getHeight()));

        ConnectPoints();
        repaint();
        revalidate();
    }

    // constructor
    public GridImage(MouseListener listener, MouseMotionListener motion, String file) {
        super();

        BufferedImage img = readImage(file);
        bim = img;
        filteredbim = new BufferedImage(
                bim.getWidth(), bim.getHeight(), BufferedImage.TYPE_INT_ARGB);

        //this.setSize(new Dimension(bim.getWidth(), bim.getHeight()));
        points = new GridPoint[10][10];

        //Create 10x10 grid of points. i = rows, j = columns
        for (int i = 0; i < 500; i += 50) {
            for (int j = 0; j < 500; j += 50) {
                GridPoint point = new GridPoint(i, j, i / 50, j / 50);
                points[i / 50][j / 50] = point;
            }
        }
        filteredbim = resize(filteredbim);
        bim = resize(bim);
        this.setPreferredSize(new Dimension(bim.getWidth(), bim.getHeight()));

        ConnectPoints();
        this.repaint();
        revalidate();

        this.addMouseListener(listener);
        this.addMouseMotionListener(motion);
    }

    public GridPoint GetActivePoint() {
        return point;
    }

    public void SetActivePoint(GridPoint correspond_pnt) {
        points[correspond_pnt.GetRow()][correspond_pnt.GetColumn()].SetColor(Color.RED);
    }

    public void SetDragging(boolean value) {
        isDragging = value;
    }

    public boolean IsDragging() {
        return isDragging;
    }

    public void SetLock(boolean value) {
        lock_selection = value;
    }

    public boolean IsLocked() {
        return lock_selection;
    }

    public void Redraw() {
        repaint();
    }

    //gets the points on each graph that correspond to each other when selected
    public GridPoint GetMatchingPoint(int row, int column) {
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                if (points[i][j].GetRow() == row && points[i][j].GetColumn() == column) {
                    return points[i][j];
                }
            }
        }
        return null;
    }

    public void ConnectPoints() {
        edges = new ArrayList<Edge>();
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                if (i > 0) {
                    //Connect left
                    Edge left = new Edge(points[i][j], points[i-1][j]);
                    edges.add(left);
                }
                if (j < points[i].length - 1 && i < points.length - 1) {
                    //Connect bottom-right
                    Edge diagonal = new Edge(points[i][j], points[i+1][j+1]);
                    edges.add(diagonal);
                }
                if (j < points[i].length - 1) {
                    //Connect down
                    Edge down = new Edge(points[i][j], points[i][j+1]);
                    edges.add(down);
                }
            }
        }
    }

    public boolean ExistsClickedPoint(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                int pntx = points[i][j].GetX();
                int pnty = points[i][j].GetY();

                if (Math.abs(x - pntx) <= 5 && Math.abs(y - pnty) <= 5) {
                    return true;
                }
            }
        }
        return false;
    }

    public GridPoint GetClickedPoint(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                int pntx = points[i][j].GetX();
                int pnty = points[i][j].GetY();

                if (Math.abs(x - pntx) <= 5 && Math.abs(y - pnty) <= 5) {
                    return points[i][j];
                }
            }
        }
        return null;
    }

    // controls the animation of the graph and its points
    public void Morph(GridImage start, GridImage end, int fps, int seconds) {
        ActionListener event = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(count);

                if (count != (fps*seconds) - 1) {
                    for (int i = 0; i < start.points.length; i++) {
                        for (int j = 0; j < start.points[i].length; j++) {
                            float dx = end.points[i][j].GetX() - start.points[i][j].GetX();
                            float dy = end.points[i][j].GetY() - start.points[i][j].GetY();

                            int start_x = start.points[i][j].GetX();
                            int start_y = start.points[i][j].GetY();

                            points[i][j].SetXY(start_x + Math.round((dx / (fps * seconds))*count), start_y + Math.round((dy / (fps * seconds))*count));

                            if (count == (fps*seconds) - 1) {
                                points[i][j].SetXY(end.points[i][j].GetX(), end.points[i][j].GetY());
                            }
                        }
                    }
                    repaint();
                    revalidate();
                    count++;
                }
                else {
                    timer.stop();
                    repaint();
                    revalidate();
                    count = 1;
                }
            }
        };
        int interval = (int)(((float)seconds/(float)fps)*1000);
        System.out.println(interval);

        timer = new Timer(interval, event);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D big = (Graphics2D) g;
        // draw the image behind the grid
        if (showfiltered) {
            big.drawImage(filteredbim, 0, 0, this);
        }
        else {
            big.drawImage(bim, 0, 0, this);
        }
        // draw the grid
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                g.setColor(points[i][j].GetColor());
                g.fillOval(points[i][j].GetX() - 5, points[i][j].GetY() - 5, 10, 10);
            }
        }

        for (int i = 0; i < edges.size(); i++) {
            g.drawLine(edges.get(i).GetPoint1().GetX(), edges.get(i).GetPoint1().GetY(),
                       edges.get(i).GetPoint2().GetX(), edges.get(i).GetPoint2().GetY());
        }
    }
    // change the image by resetting what's stored
    public void setImage(BufferedImage img) {
        if (img == null) {
            return;
        }
        bim = img;
        filteredbim = new BufferedImage(
                bim.getWidth(), bim.getHeight(), BufferedImage.TYPE_INT_ARGB);

        filteredbim = resize(filteredbim);
        bim = resize(bim);
        setPreferredSize(new Dimension(bim.getWidth(), bim.getHeight()));

        showfiltered = false;
        this.repaint();
    }
    // get the stored image
    public BufferedImage getImage() {
        return bim;
    }

    public void showImage() {
        if (bim == null) {
            return;
        }
        showfiltered = false;
        this.repaint();
    }

    public boolean isShowfiltered() {
        return showfiltered;
    }
    // resizing the image so that it is the same size as the grid
    public BufferedImage resize(BufferedImage img) {
        Image temp = img.getScaledInstance(450, 450, Image.SCALE_SMOOTH);
        BufferedImage rimg = new BufferedImage(450, 450, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = rimg.createGraphics();
        g.drawImage(temp, 0, 0, null);
        g.dispose();

        return rimg;
    }
    // brightens the image according to user specifications
    // currently deletes the image when the slider is used... but why?
    // using the threshold function for now as a placeholder
    public void brighten(int value) {
        if (bim == null) return;
        int i;
        byte thresh[] = new byte[256];
        if ((value < 0) || (value > 100))
            value = 128;
        for (i = 0; i < value; i++)
            thresh[i] = 0;
        for (int j = i; j < 255; j++)
            thresh[j] = (byte)255;
        ByteLookupTable blut = new ByteLookupTable (0, thresh);
        // RescaleOp rop = new RescaleOp(float, 0, null);
        // rop.filter(newbim, filteredbim);
        LookupOp lop = new LookupOp (blut, null);
        lop.filter (bim, filteredbim);
        showfiltered=true;
        this.repaint();
    }
    public BufferedImage readImage(String file) {
        MediaTracker tracker = new MediaTracker(new Component() {});
        Image image = Toolkit.getDefaultToolkit().getImage(file);
        tracker.addImage(image, 0);
        try {
            tracker.waitForID(0);
        } catch(InterruptedException e) {}

        BufferedImage bim = new BufferedImage(
                image.getWidth(this), image.getHeight(this), BufferedImage.TYPE_INT_ARGB);
        Graphics2D big = bim.createGraphics();
        big.drawImage(image, 0, 0, this);

        return bim;
    }

    /*public static void main(String[] args) {
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.LINE_AXIS));
        GridTest grid1 = new GridTest("3.jpg");
        GridTest grid2 = new GridTest("3.jpg");
        frame.getContentPane().add(grid1);
        frame.getContentPane().add(grid2);
        //frame.pack();
        frame.setVisible(true);
    }*/
}
