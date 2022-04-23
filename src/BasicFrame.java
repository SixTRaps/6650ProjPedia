import javax.swing.*;
import java.net.URL;

public class BasicFrame extends JFrame {
    public BasicFrame() {
        super();
        this.setTitle("ProjPedia");
        this.setBounds(100, 100, 700, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(null);
    }

    public void addBackground() {
        JLabel lblBackground = new JLabel(); // Initialize
        URL resource = this.getClass().getResource("./background.jpg"); // Image path
        ImageIcon icon = new ImageIcon(resource); // Create image object
        lblBackground.setIcon(icon); // Set image
        lblBackground.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight()); // Position and size
        this.getContentPane().add(lblBackground); // Add components
    }
}
