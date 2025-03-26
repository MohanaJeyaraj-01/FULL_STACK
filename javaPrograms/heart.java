import java.awt.*;
import javax.swing.*;

public class heart extends JFrame {

    public heart() {
        setTitle("Red Heart Drawing");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        // Set the color to red
        g2d.setColor(Color.RED);

        // Create the heart shape using a combination of arcs and a triangle
        int[] xPoints = {200, 150, 250};
        int[] yPoints = {200, 350, 350};

        // Draw left half of the heart (arc)
        g2d.fillArc(100, 100, 100, 100, 0, 180); // x, y, width, height, start angle, arc angle

        // Draw right half of the heart (arc)
        g2d.fillArc(200, 100, 100, 100, 0, 180);

        // Draw bottom triangle connecting the arcs
        g2d.fillPolygon(xPoints, yPoints, 3);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            heart h = new heart();
            h.setVisible(true);
        });
    }
}
