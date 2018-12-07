import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.ArrayList;

public class ImgView extends JLabel {
    public BufferedImage bim = null;
    // marking for intensity shift
    public BufferedImage filteredbim = null;
    private boolean showfiltered = false;

    // default constructor
    public ImgView() {}

    // This constructor stores a buffered image stored as a parameter
    public ImgView(BufferedImage img) {
        bim = img;
        filteredbim = new BufferedImage(
                bim.getWidth(), bim.getHeight(), BufferedImage.TYPE_INT_RGB);
        // might need to change this later to 300x300 to match the Grid dimensions
        // setPreferredSize(new Dimension(bim.getWidth(), bim.getHeight()));
        setPreferredSize(new Dimension(300, 300));
        this.repaint();
    }
    // change the image by resetting what's stored
    public void setImage(BufferedImage img) {
        if (img == null) {
            return;
        }
        bim = img;
        filteredbim = new BufferedImage(
                bim.getWidth(), bim.getHeight(), BufferedImage.TYPE_INT_RGB);
        setPreferredSize(new Dimension(300, 300));
        showfiltered = false;
        this.repaint();
    }
    // get the stored image
    public BufferedImage getImage() {
        return bim;
    }
    /* preview the transformation of the image from Left to Right
    public void PreviewLR() {
        if (bim == null) {
            return;
        }
        // create the temporary JFrame to preview the transformation
        JFrame frame = new JFrame("Preview");
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        JPanel preview = new JPanel();
        preview.add(this);
        //showfiltered = false;

        frame.add(preview);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }*/
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
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D big = (Graphics2D) g;
        if (showfiltered) {
            big.drawImage(filteredbim, 0, 0, this);
        }
        else {
            big.drawImage(bim, 0, 0, this);
        }
    }
}
