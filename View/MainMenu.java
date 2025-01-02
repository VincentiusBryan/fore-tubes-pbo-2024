package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu {

    private int statusToko;

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
        title2.setBounds(210, 15, 100, 50);
        title2.setFont(new Font("SansSerif", Font.BOLD, 36));
        mainMenu.add(title2);

        JButton adminButton = new JButton("Admin");
        adminButton.setBounds(150, 150, 200, 40);
        adminButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        adminButton.setBackground(new Color(208, 44, 49));
        adminButton.setForeground(Color.WHITE);
        mainMenu.add(adminButton);

        JButton customerButton = new JButton("Customer");
        customerButton.setBounds(150, 210, 200, 40);
        customerButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        customerButton.setBackground(new Color(49, 49, 50));
        customerButton.setForeground(Color.WHITE);
        mainMenu.add(customerButton);

        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenu.dispose(); // close main menu
                new LoginView(); // pastikan LoginView sudah didefinisikan
            }
        });

        customerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenu.dispose(); // close main menu
                customer(); // masuk ke Menu Customer
            }
        });

        mainMenu.setVisible(true);
    }




    public void customer() {


        //checkstatus if 0 = toko tutup   if 1 = toko buka lanjutkan kebawah


        Toolkit toolkit = Toolkit.getDefaultToolkit(); // INIT TOOLKIT
        Dimension screenSize = toolkit.getScreenSize(); // get screensize

        JFrame mainMenu = new JFrame("Main Menu Customer");
        mainMenu.setLayout(null);
        mainMenu.setSize(500, 400);
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int x = (screenSize.width - mainMenu.getWidth()) / 2;  // get x 
        int y = (screenSize.height - mainMenu.getHeight()) / 2; // get y
        mainMenu.setLocation(x, y);

        JLabel label = new JLabel("Selamat Datang di Menu Utama Customer");
        label.setBounds(125, 60, 300, 30);
        mainMenu.add(label);

        JLabel title2 = new JLabel("Fore");
        title2.setBounds(200, 15, 100, 50);
        title2.setFont(new Font("SansSerif", Font.BOLD, 36));
        mainMenu.add(title2);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 150, 200, 40);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        loginButton.setBackground(new Color(88, 24, 69));
        loginButton.setForeground(Color.WHITE);
        mainMenu.add(loginButton);

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
                new LoginView(); // pastikan LoginView sudah didefinisikan
            }
        });



        // Register Button action
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenu.dispose(); // close main menu
                new RegisterView(); // pastikan RegisterView sudah didefinisikan
            }
        });

        mainMenu.setVisible(true);
    }
}
