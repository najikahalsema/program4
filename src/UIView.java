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
    private BufferedImage rightImage;
    GridManager manage = new GridManager();

    JFrame frame = new JFrame("JMorph");
    private JLabel FpsLabel;
    private JSlider FpsSlider;
    private JLabel filterLabelL;
    private JSlider filterSliderL;
    private JLabel filterLabelR;
    private JSlider filterSliderR;

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
                            manage.grid1.setImage(leftImage);
                            manage.grid1.showImage();
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
                        manage.grid2.setImage(rightImage);
                        manage.grid2.showImage();
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
        preview = new JButton("Preview");
        reset = new JButton("Reset");

        // build the FPS slider
        FpsLabel = new JLabel("Frames per second: 30");
        FpsSlider = new JSlider(SwingConstants.VERTICAL, 1, 60, 30);

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
        // build the Filter Sliders
        // Left image slider
        filterLabelL = new JLabel("Brightness Level: --");
        filterSliderL = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 0);

        filterSliderL.setMajorTickSpacing(10);
        filterSliderL.setPaintTicks(true);
        filterSliderL.addChangeListener(
                new ChangeListener() {
                    public void stateChanged(ChangeEvent e) {
                        manage.grid1.brighten(filterSliderL.getValue());
                        filterLabelL.setText("Brightness Level: " +
                        Integer.toString(filterSliderL.getValue()));
                    }
                }
        );
        // right image slider
        filterLabelR = new JLabel("Brightness Level: --");
        filterSliderR = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 0);

        filterSliderR.setMajorTickSpacing(10);
        filterSliderR.setPaintTicks(true);
        filterSliderR.addChangeListener(
                new ChangeListener() {
                    public void stateChanged(ChangeEvent e) {
                        manage.grid2.brighten(filterSliderR.getValue());
                        filterLabelR.setText("Brightness Level: " +
                                Integer.toString(filterSliderR.getValue()));
                    }
                }
        );
        // shows the animation in a new popup window
        preview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GridImage preview_grid = new GridImage("3.jpg");
                JFrame popup = new JFrame("Preview");
                //JOptionPane.showMessageDialog(null, preview_gr, "Preview", JOptionPane.OK_CANCEL_OPTION);
                popup.setSize(500,500);
                popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                popup.add(preview_grid);
                popup.setVisible(true);

                preview_grid.Morph(manage.grid1, manage.grid2, FpsSlider.getValue(), 1);
            }
        });

        // setting up image panel
        /*JPanel imgPanel = new JPanel();
        JPanel imgs = new JPanel();
        JPanel sliderPanel = new JPanel();
        JPanel labelPanel = new JPanel();

        imgPanel.setLayout(new BoxLayout(imgPanel, BoxLayout.PAGE_AXIS));

        //imgs.add(manage.grid1);
        //imgs.add(manage.grid2);
        sliderPanel.add(filterSliderL);
        sliderPanel.add(filterSliderR);
        labelPanel.add(filterLabelL);
        labelPanel.add(filterLabelR);

        //imgPanel.add(imgs);
        imgPanel.add(sliderPanel);
        imgPanel.add(labelPanel);
*/
        frame.setSize(1500,500);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.LINE_AXIS));

        // adding elements to the JFrame
        frame.getContentPane().add(FpsLabel);
        frame.getContentPane().add(Box.createRigidArea(new Dimension(10,0)));
        frame.getContentPane().add(FpsSlider);
        frame.getContentPane().add(Box.createRigidArea(new Dimension(5,0)));
        frame.getContentPane().add(manage.grid1);
        //frame.getContentPane().add(imgPanel);
        //frame.getContentPane().add(Box.createRigidArea(new Dimension(5,0)));
        frame.getContentPane().add(manage.grid2);
        frame.getContentPane().add(preview);
        frame.getContentPane().add(reset);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public static void main(String[] argv) {
        JFrame frame = new UIView();
    }
}