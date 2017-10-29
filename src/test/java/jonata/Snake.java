package jonata;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.paim.commons.BinaryImage;
import org.paim.commons.Image;
import org.paim.commons.ImageConverter;
import org.paim.commons.ImageFactory;
import org.paim.pdi.SnakeProcess;

/**
 *
 * @author JonataBecker
 */
public class Snake {

    public static void main(String[] args) throws IOException {

        Image image = ImageFactory.buildRGBImage(ImageIO.read(new File("/home/jonatabecker/Desktop/binary.jpg")));
        SnakeProcess process = new SnakeProcess(image, 1000, 1, 1, 1);
        process.process();

        BinaryImage binaryImage = process.getOutput();
        for (int x = 0; x < binaryImage.getWidth(); x++) {
            for (int y = 0; y < binaryImage.getHeight(); y++) {
                if (binaryImage.get(x, y)) {
                    image.set(Image.CHANNEL_RED, x, y, 255);
                    image.set(Image.CHANNEL_GREEN, x, y, 0);
                    image.set(Image.CHANNEL_BLUE, x, y, 0);
                }
            }
        }

        JFrame frame = new JFrame();
        frame.setSize(new Dimension(800, 600));
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon(ImageConverter.toBufferedImage(image)));
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(label, BorderLayout.CENTER);
        frame.add(panel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
