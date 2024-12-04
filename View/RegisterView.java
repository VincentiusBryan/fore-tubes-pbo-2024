package View;

import javax.swing.*;
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
        registerFrame.setSize(400, 300);
        registerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int x = (screenSize.width - registerFrame.getWidth()) / 2;
        int y = (screenSize.height - registerFrame.getHeight()) / 2;
        registerFrame.setLocation(x, y);

        JLabel title = new JLabel("Register");
        title.setBounds(150, 20, 100, 30);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        registerFrame.add(title);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 80, 100, 25);
        registerFrame.add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(150, 80, 200, 25);
        registerFrame.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 120, 100, 25);
        registerFrame.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(150, 120, 200, 25);
        registerFrame.add(passwordField);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(150, 160, 90, 30);
        registerFrame.add(registerButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(260, 160, 90, 30);
        registerFrame.add(backButton);

        backButton.addActionListener(e -> {
            registerFrame.dispose();
            new MainMenu(); // back ke mainmenu
        });

        registerFrame.setVisible(true);
    }
}
