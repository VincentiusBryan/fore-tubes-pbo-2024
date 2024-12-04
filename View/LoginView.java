package View;



import javax.swing.*;
import java.awt.*;

public class LoginView {
    public LoginView() {
        showLogin();
    }

    public void showLogin() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        JFrame loginFrame = new JFrame("Login");
        loginFrame.setLayout(null);
        loginFrame.setSize(400, 300);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int x = (screenSize.width - loginFrame.getWidth()) / 2;
        int y = (screenSize.height - loginFrame.getHeight()) / 2;
        loginFrame.setLocation(x, y);

        JLabel title = new JLabel("Login");
        title.setBounds(150, 20, 100, 30);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        loginFrame.add(title);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 80, 100, 25);
        loginFrame.add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(150, 80, 200, 25);
        loginFrame.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 120, 100, 25);
        loginFrame.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(150, 120, 200, 25);
        loginFrame.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 160, 90, 30);
        loginFrame.add(loginButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(260, 160, 90, 30);
        loginFrame.add(backButton);

        backButton.addActionListener(e -> {
            loginFrame.dispose();
            new MainMenu(); // back to mainmenu
        });

        loginFrame.setVisible(true);
    }
}
