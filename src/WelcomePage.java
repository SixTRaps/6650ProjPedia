import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class WelcomePage implements ActionListener {

    private BasicFrame frame;
    private JButton button1;
    private JButton button2;
    private JLabel label1;
    private JLabel label2;

    /**
     * Create the application.
     */
    public WelcomePage() {
        frame = new BasicFrame();

        // Projpedia label
        label1 = new JLabel("Projpedia");
        label1.setBounds(255, 20, 200, 200);
        label1.setFont(new Font("Serif", Font.BOLD, 45));
        label1.setForeground(Color.white);
        frame.getContentPane().add(label1);

        // Short introduction
        label2 = new JLabel("ProjPedia, a stupid encyclopedia");
        label2.setBounds(220, 0, 500, 500);
        label2.setFont(new Font("Arial", Font.ITALIC, 20));
        label2.setForeground(Color.white);
        frame.getContentPane().add(label2);

        // Login Button
        button1 = new JButton("Sign up");
        button1.setBounds(100, 400, 200, 40);
        button1.addActionListener(this);
        frame.getContentPane().add(button1);

        // Signup Button
        button2 = new JButton("Log in");
        button2.setBounds(400, 400, 200, 40);
        button2.addActionListener(this);
        frame.getContentPane().add(button2);

        // Background
        frame.addBackground();

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Sign up event
        if (e.getSource() == button1) {
            frame.dispose();
            new SignUpPage();
        }

        // Login event
        if (e.getSource() == button2) {
            frame.dispose();
            new LoginPage();
        }
    }


}