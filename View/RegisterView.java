package View;

import javax.swing.*;
import Controller.RegisterController;
import java.awt.*;

public class RegisterView {
    public RegisterView() {
        showRegister();
    }

    public void showRegister() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        JFrame registerFrame = new JFrame("Register");
        registerFrame.setLayout(null);
        registerFrame.setSize(400, 350);
        registerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int x = (screenSize.width - registerFrame.getWidth()) / 2;
        int y = (screenSize.height - registerFrame.getHeight()) / 2;
        registerFrame.setLocation(x, y);

        JLabel title = new JLabel("Register");
        title.setBounds(150, 20, 100, 30);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        registerFrame.add(title);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 80, 100, 25);
        registerFrame.add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setBounds(150, 80, 200, 25);
        registerFrame.add(emailField);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(50, 120, 100, 25);
        registerFrame.add(phoneLabel);

        JTextField phoneField = new JTextField();
        phoneField.setBounds(150, 120, 200, 25);
        registerFrame.add(phoneField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 160, 100, 25);
        registerFrame.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(150, 160, 200, 25);
        registerFrame.add(passwordField);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(150, 200, 90, 30);
        registerFrame.add(registerButton);

        registerButton.addActionListener(e -> {
            String email = emailField.getText();
            String phone = phoneField.getText();
            String password = new String(passwordField.getPassword());

            if (email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(registerFrame, "Email, Phone, or Password cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!phone.matches("\\d{10,12}")) {
                JOptionPane.showMessageDialog(registerFrame, "Invalid phone number format!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                RegisterController controller = new RegisterController();
                boolean success = controller.registerUser(email, password, phone);

                if (success) {
                    JOptionPane.showMessageDialog(registerFrame, "User registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    registerFrame.dispose();
                    new MainMenu();
                } else {
                    JOptionPane.showMessageDialog(registerFrame, "Failed to register user. Try again!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton backButton = new JButton("Back");
        backButton.setBounds(260, 200, 90, 30);
        registerFrame.add(backButton);

        backButton.addActionListener(e -> {
            registerFrame.dispose();
            new MainMenu();
        });

        registerFrame.setVisible(true);
    }
}
