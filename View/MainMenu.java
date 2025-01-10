package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu {

    public MainMenu() {
        showCustomerMenu();
    }

    public void showCustomerMenu() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        JFrame customerMenu = new JFrame("Pratama Group");
        customerMenu.setLayout(null);
        customerMenu.setSize(500, 400);
        customerMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int x = (screenSize.width - customerMenu.getWidth()) / 2;
        int y = (screenSize.height - customerMenu.getHeight()) / 2;
        customerMenu.setLocation(x, y);

        JLabel label = new JLabel("Selamat Datang");
        label.setBounds(200, 70, 300, 30);
        customerMenu.add(label);

        JLabel title2 = new JLabel("Pratama Group");
        title2.setBounds(120, 30, 300, 50);
        title2.setFont(new Font("SansSerif", Font.BOLD, 36));
        customerMenu.add(title2);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 150, 200, 40);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        loginButton.setBackground(new Color(88, 24, 69));
        loginButton.setForeground(Color.WHITE);
        customerMenu.add(loginButton);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(150, 210, 200, 40);
        registerButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        registerButton.setBackground(new Color(58, 129, 89));
        registerButton.setForeground(Color.WHITE);
        customerMenu.add(registerButton);

        loginButton.addActionListener(e -> {
            customerMenu.dispose();
            new LoginView();
        });

        registerButton.addActionListener(e -> {
            customerMenu.dispose();
            new RegisterView();
        });

        customerMenu.setVisible(true);
    }
}
