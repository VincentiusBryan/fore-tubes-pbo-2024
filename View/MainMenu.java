package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu {
    public MainMenu() {
        showMenu();
    }

    public void showMenu() {
        Toolkit toolkit = Toolkit.getDefaultToolkit(); // INIT TOOLKIT
        Dimension screenSize = toolkit.getScreenSize(); // get screensize

        JFrame mainMenu = new JFrame("Main Menu");
        mainMenu.setLayout(null);
        mainMenu.setSize(500, 400);
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int x = (screenSize.width - mainMenu.getWidth()) / 2;  // get x 
        int y = (screenSize.height - mainMenu.getHeight()) / 2; // get y
        mainMenu.setLocation(x, y);

        JLabel label = new JLabel("Selamat Datang di Menu Utama");
        label.setBounds(150, 60, 200, 30);
        mainMenu.add(label);

        JLabel title2 = new JLabel("Fore");
        title2.setBounds(200, 15, 100, 50);
        title2.setFont(new Font("SansSerif", Font.BOLD, 36));
        mainMenu.add(title2);

        // Button Login
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 150, 200, 40);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        loginButton.setBackground(new Color(88, 24, 69));
        loginButton.setForeground(Color.WHITE);
        mainMenu.add(loginButton);

        // Button Register
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(150, 210, 200, 40);
        registerButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        registerButton.setBackground(new Color(58, 129, 89));
        registerButton.setForeground(Color.WHITE);
        mainMenu.add(registerButton);

        // Login Button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenu.dispose(); // close main menu
                new LoginView();   // masuk ke LoginView.java
            }
        });

        // register button action
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenu.dispose(); // close mainmenu
                new RegisterView(); // masuk ke RegisterView.java
            }
        });

        mainMenu.setVisible(true);
    }
}
