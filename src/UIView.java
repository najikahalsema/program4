/* The main UI Controller: houses UI components and builds the display
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.imageio.*;
import javax.swing.event.*;
import java.io.*;
import java.util.ArrayList;

public class UIView extends JFrame {
    private BufferedImage leftImage;
    private ImgView leftView;
    private BufferedImage rightImage;
    private ImgView rightView;
    GridManager manage = new GridManager();

    JFrame frame = new JFrame("JMorph");
    private JLabel FpsLabel;
    private JSlider FpsSlider;
    private JButton preview;
    private JButton reset;

    // constructor
    public UIView() {
        super();

        this.buildMenus();
        this.buildDisplay();
    }

    // builds the menu and handles opening files
    private void buildMenus() {
        final JFileChooser fc = new JFileChooser(".");
        JMenuBar bar = new JMenuBar();
        frame.setJMenuBar(bar);

        JMenu fileMenu = new JMenu("File");
        JMenuItem openLeft = new JMenuItem("Open Left Image");
        JMenuItem openRight = new JMenuItem("Open Right Image");
        JMenuItem fileSave = new JMenuItem("Save");
        JMenuItem fileLoad = new JMenuItem("Load");
        JMenuItem fileExit = new JMenuItem("Exit");

        // action listener to change the left image
        openLeft.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        int returnVal = fc.showOpenDialog(UIView.this);

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
                    int returnVal = fc.showOpenDialog(UIView.this);

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
        );
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
    }

    // builds the main display
    private void buildDisplay() {
        leftView = new ImgView(readImage("3.jpg"));
        rightView = new ImgView(readImage("3.jpg"));

        preview = new JButton("Preview");
        reset = new JButton("Reset");

        // build the FPS slider
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
        JPanel imgPanel = new JPanel();

        imgPanel.add(leftView);
        imgPanel.add(rightView);

        frame.setSize(1500,500);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.LINE_AXIS));

        // adding elements to the JFrame
        frame.getContentPane().add(FpsLabel);
        frame.getContentPane().add(Box.createRigidArea(new Dimension(10,0)));
        frame.getContentPane().add(FpsSlider);
        frame.getContentPane().add(Box.createRigidArea(new Dimension(5,0)));
        frame.getContentPane().add(manage.grid1);
        frame.getContentPane().add(imgPanel);
        frame.getContentPane().add(Box.createRigidArea(new Dimension(5,0)));
        frame.getContentPane().add(manage.grid2);
        frame.getContentPane().add(preview);
        frame.getContentPane().add(reset);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    // reads an Image object from a file and returns a converted
    // BufferedImage object
    public BufferedImage readImage(String file) {
        MediaTracker tracker = new MediaTracker(new Component() {});
        Image image = Toolkit.getDefaultToolkit().getImage(file);
        tracker.addImage(image, 0);
        try {
            tracker.waitForID(0);
        } catch(InterruptedException e) {}

        BufferedImage bim = new BufferedImage(
                image.getWidth(this), image.getHeight(this), BufferedImage.TYPE_INT_RGB);
        Graphics2D big = bim.createGraphics();
        big.drawImage(image, 0, 0, this);

        return bim;
    }

    public static void main(String[] argv) {
        JFrame frame = new UIView();
    }
}