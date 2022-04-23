import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

public class MainPage implements ActionListener {
    BasicFrame frameMain = new BasicFrame();
    BasicFrame frameEntryList = new BasicFrame();
    BasicFrame frameAddNew = new BasicFrame();
    BasicFrame frameEdit = new BasicFrame();

    JPanel panel = new JPanel();

    JLabel welcomeLabel = new JLabel();
    JLabel inquiryLabel = new JLabel();
    JLabel addNewLabel = new JLabel();

    JLabel newEntryKeyLabel = new JLabel();
    JTextField newEntryKeyField = new JTextField();
    JLabel newEntryValueLabel = new JLabel();
    JTextArea newEntryValueArea = new JTextArea();

    JButton entriesButton = new JButton();
    JButton addNewButton = new JButton();
    JButton editButton = new JButton();
    JButton deleteButton = new JButton();
    JButton logoutButton = new JButton();

    JButton submitButton = new JButton();
    JButton backButton = new JButton();

    private String username;
    private RemoteUserInterface stub;

    public MainPage(String username, RemoteUserInterface stub) throws IllegalArgumentException {
        this.username = username;
        this.stub = stub;
        if (username == null || username.length() == 0) {
            throw new IllegalArgumentException("Invalid username");
        }

        welcomeLabel.setText("Welcome to ProjPedia, " + this.username + "!");
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 40));
        welcomeLabel.setBounds(130, 120, 600, 40);
        welcomeLabel.setForeground(Color.white);

        inquiryLabel.setText(this.username + ", what do you want to do today?");
        inquiryLabel.setFont(new Font("Arial", Font.ITALIC, 20));
        inquiryLabel.setBounds(220, 180, 400, 30);
        inquiryLabel.setForeground(Color.white);

        entriesButton.setText("View All Entries");
        entriesButton.setBounds(260, 270, 200, 20);
        entriesButton.addActionListener(this);

        addNewButton.setText("Create New Entry");
        addNewButton.setBounds(260, 300, 200, 20);
        addNewButton.addActionListener(this);

        editButton.setText("Edit Entry");
        editButton.setBounds(260, 330, 200, 20);
        editButton.addActionListener(this);

        deleteButton.setText("Delete Entry");
        deleteButton.setBounds(260, 360, 200, 20);
        deleteButton.addActionListener(this);

        logoutButton.setText("Log Out");
        logoutButton.setBounds(260, 390, 200, 20);
        logoutButton.addActionListener(this);

        submitButton.setText("Submit");

        backButton.setText("Back");

        panel.setLayout(null);
        panel.add(welcomeLabel);
        panel.add(inquiryLabel);

        panel.add(entriesButton);
        panel.add(addNewButton);
        panel.add(editButton);
        panel.add(deleteButton);
        panel.add(logoutButton);
        frameMain.setContentPane(panel);

        frameMain.addBackground();
        frameMain.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String response = null;
        if (e.getSource() == entriesButton) {
            JPanel listPanel = new JPanel();
            JPanel titlePanel = new JPanel();
            JPanel bottomPanel = new JPanel();

            JLabel titleLabel = new JLabel("Existing Entries");
            titlePanel.add(titleLabel);
            backButton.addActionListener(e1 -> {
                frameEntryList.dispose();
                new MainPage(username, stub);
            });
            bottomPanel.add(backButton);
            frameEntryList.getContentPane().setLayout(new BorderLayout());
            try {
                Set<String> keySet = stub.getEntryKeyList();
                for (String key : keySet) {
                    JButton btn = new JButton(key);
                    System.out.println(key);
                    btn.addActionListener(event -> {
                        String getResp = null;
                        try {
                            getResp = stub.get(key);
                            if (getResp.equals("none") || getResp == null) {
                                JOptionPane.showMessageDialog(null, "Entry not exists. Try again.", "alert", JOptionPane.ERROR_MESSAGE);
                            }
                            else {
                                JOptionPane.showMessageDialog(null, getResp, "Entry Value", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Delete entry failed", "alert", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    });
                    listPanel.add(btn);
                }
                titlePanel.setBackground(Color.white);
                bottomPanel.setBackground(Color.white);
                frameEntryList.add(titlePanel, BorderLayout.NORTH);
                frameEntryList.add(new JScrollPane(listPanel), BorderLayout.CENTER);
                frameEntryList.add(bottomPanel, BorderLayout.SOUTH);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Retrieve entry list failed", "alert", JOptionPane.ERROR_MESSAGE);
                return;
            }
            frameMain.dispose();
            frameEntryList.setVisible(true);
        }
        else if (e.getSource() == addNewButton) {
            addNewLabel.setText("Please enter the entry key and value");
            addNewLabel.setForeground(Color.white);
            addNewLabel.setFont(new Font("Serif", Font.BOLD, 30));
            addNewLabel.setBounds(40, 80, 600, 40);
            frameAddNew.getContentPane().add(addNewLabel);

            newEntryKeyLabel.setText("Name:");
            newEntryKeyLabel.setForeground(Color.white);
            newEntryKeyLabel.setBounds(40, 150,  120, 25);
            newEntryKeyField.setBounds(140, 150, 500, 25);
            frameAddNew.getContentPane().add(newEntryKeyLabel);
            frameAddNew.getContentPane().add(newEntryKeyField);

            newEntryValueLabel.setText("Content:");
            newEntryValueLabel.setForeground(Color.white);
            newEntryValueLabel.setBounds(40, 200, 120, 25);
            newEntryValueArea.setBounds(140, 200, 500, 200);
            frameAddNew.getContentPane().add(newEntryValueLabel);
            frameAddNew.getContentPane().add(newEntryValueArea);

            submitButton.setBounds(240, 420, 100, 25);
            submitButton.addActionListener(e1 -> {
                String key = newEntryKeyField.getText();
                String value = newEntryValueArea.getText();
                if (key == null || key.trim().length() == 0 || value == null || value.trim().length() == 0) {
                    JOptionPane.showMessageDialog(null, "Field should not be empty", "alert", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    String getResponse = stub.get(key);
//                    if (getResponse.equalsIgnoreCase() )
                    String submitResponse = stub.put(key, value);
                    if (submitResponse.equals("Fail")) {
                        JOptionPane.showMessageDialog(null, "Adding new entry failed", "alert", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Adding new entry Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                        frameAddNew.dispose();
                        frameEdit.dispose();
                        new MainPage(username, stub);
                        return;
                    }
                } catch (Exception err) {
                    JOptionPane.showMessageDialog(null, "Adding new entry failed", "alert", JOptionPane.ERROR_MESSAGE);
                    err.printStackTrace();
                }
            });
            frameAddNew.getContentPane().add(submitButton);

            backButton.setBounds(360, 420, 100, 24);
            backButton.addActionListener(e1 -> {
                frameAddNew.dispose();
                new MainPage(username, stub);
            });
            frameAddNew.getContentPane().add(backButton);

            frameAddNew.addBackground();
            frameMain.dispose();
            frameAddNew.setVisible(true);
        }
        else if (e.getSource() == editButton) {
            String key = JOptionPane.showInputDialog(null, "The key of entry you want to edit");
            JLabel editTitle = new JLabel();
            JLabel editValueLabel = new JLabel("Content");
            editValueLabel.setForeground(Color.white);
            JTextArea editValueArea = new JTextArea();
            try {
                response = stub.get(key);
                if (response.equalsIgnoreCase("None")) {
                    JOptionPane.showMessageDialog(null, "Entry not exists", "alert", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else {
                    editTitle.setText(key);
                    editTitle.setForeground(Color.white);
                    editTitle.setFont(new Font("Serif", Font.BOLD, 30));
                    editTitle.setBounds(40, 80, 600, 40);
                    frameEdit.add(editTitle);

                    editValueLabel.setBounds(40, 200, 120, 25);
                    frameEdit.add(editValueLabel);

                    editValueArea.setText(response);
                    editValueArea.setBounds(140, 200, 500, 200);
                    frameEdit.add(editValueArea);

                    submitButton.setBounds(240, 420, 100, 25);
                    submitButton.addActionListener(e1 -> {
                        String value = editValueArea.getText();
                        if (value == null || value.trim().length() == 0) {
                            JOptionPane.showMessageDialog(null, "Field should not be empty", "alert", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        try {
                            String submitResponse = stub.put(key, value);
                            if (submitResponse.equals("Fail")) {
                                JOptionPane.showMessageDialog(null, "Edit entry failed", "alert", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            else {
                                JOptionPane.showMessageDialog(null, "Edit entry Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                                frameAddNew.dispose();
                                frameEdit.dispose();
                                new MainPage(username, stub);
                                return;
                            }
                        } catch (Exception err) {
                            JOptionPane.showMessageDialog(null, "Edit entry failed", "alert", JOptionPane.ERROR_MESSAGE);
                            err.printStackTrace();
                        }
                    });
                    frameEdit.getContentPane().add(submitButton);

                    backButton.setBounds(360, 420, 100, 24);
                    backButton.addActionListener(e1 -> {
                        frameEdit.dispose();
                        new MainPage(username, stub);
                    });
                    frameEdit.getContentPane().add(backButton);
                }
            } catch (Exception err) {
                JOptionPane.showMessageDialog(null, "Fail to edit entry", "alert", JOptionPane.ERROR_MESSAGE);
                err.printStackTrace();
                return;
            }
            frameMain.dispose();
            frameEdit.addBackground();
            frameEdit.setVisible(true);
        }
        else if (e.getSource() == deleteButton) {
            String[] options = {"PROCEED", "CANCEL"};
            String key = JOptionPane.showInputDialog(null, "The key of entry you want to delete");
            int decision = JOptionPane.showOptionDialog(null, "Are you sure you want to delete the entry: " + key + "?", "Warning",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
            if (decision == JOptionPane.OK_OPTION) {
                try {
                    response = stub.delete(key);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Delete entry failed", "alert", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (response.equals("Fail")) {
                    JOptionPane.showMessageDialog(null, "Due to unknown reason, fail to delete the entry: " + key, "alert", JOptionPane.ERROR_MESSAGE);
                }
                else if (response.equals("Success")) {
                    JOptionPane.showMessageDialog(null, "Successfully deleted the entry: " + key, "alert", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        else if (e.getSource() == logoutButton) {
            try {
                response = stub.logout(username);
                if (response.equalsIgnoreCase("Logout successfully")) {
                    frameMain.dispose();
                    new WelcomePage();
                }
                else {
                    JOptionPane.showMessageDialog(null, "Due to unknown reason, fail to log out.", "alert", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Delete entry failed", "alert", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }

}