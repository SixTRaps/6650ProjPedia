import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LoginFrame implements ActionListener {
    JFrame frame = new JFrame("ProjPedia");
    JPanel panel = new JPanel();

    JLabel welcomeLabel = new JLabel("Welcome to ProjectPedia!");
    JLabel userLabel = new JLabel("Username:");
    JLabel passwordLabel = new JLabel("Password:");
    JLabel portLabel = new JLabel("Server Port:");

    JTextField userText = new JTextField(20);
    JPasswordField passwordText = new JPasswordField(20);
    JTextField portText = new JTextField(20);

    JButton loginButton = new JButton("submit");

    public LoginFrame() {
        frame.setSize(700, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        welcomeLabel.setFont(new Font(welcomeLabel.getFont().getName(), welcomeLabel.getFont().getStyle(), 30));
        welcomeLabel.setBounds(180, 100, 400, 40);
        userLabel.setBounds(230,170,80,25);
        userText.setBounds(320,170,165,25);
        passwordLabel.setBounds(230,200,80,25);
        passwordText.setBounds(320,200,165,25);
        portLabel.setBounds(230,230,80,25);
        portText.setBounds(320,230,165,25);
        loginButton.setBounds(330, 280, 80, 25);

        loginButton.addActionListener(this);

        panel.setLayout(null);
        panel.add(welcomeLabel);
        panel.add(userLabel);
        panel.add(userText);
        panel.add(passwordLabel);
        panel.add(passwordText);
        panel.add(portLabel);
        panel.add(portText);
        panel.add(loginButton);
        frame.add(panel);

        frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (userText.getText() == null || passwordText.getPassword() == null || portText.getText() == null) {
            JOptionPane.showMessageDialog(null, "Field should not be empty", "alert", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int port = -1;
        try {
            port = Integer.parseInt(portText.getText());
        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, "Please enter correct port number", "alert", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (port < 0) {
            JOptionPane.showMessageDialog(null, "Please enter correct port number", "alert", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String hostname = "127.0.0.1";
        Registry registry = null;
        RemoteUserInterface stub = null;
        String username = userText.getText();
        String password = String.valueOf(passwordText.getPassword());
        try {
            registry = LocateRegistry.getRegistry(hostname, port);
            stub = (RemoteUserInterface) registry.lookup("PROJPEDIA");
            String loginResponse = stub.login(username, password);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Server connection failed", "alert", JOptionPane.ERROR_MESSAGE);
            return;
        }

    }
}