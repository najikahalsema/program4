import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.ArrayList;

public class ImgView extends JPanel {
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
                bim.getWidth(), bim.getHeight(), BufferedImage.TYPE_INT_ARGB);
        // might need to change this later to 300x300 to match the Grid dimensions
        filteredbim = resize(filteredbim);
        bim = resize(bim);
        setPreferredSize(new Dimension(bim.getWidth(), bim.getHeight()));

        this.repaint();
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

    // resizing the image so that it is the same size as the grid
    public BufferedImage resize(BufferedImage img) {
        Image temp = img.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        BufferedImage rimg = new BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB);

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
}
