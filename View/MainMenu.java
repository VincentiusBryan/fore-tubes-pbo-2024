package View;

import javax.swing.*;
import Connection.DBConnection;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainMenu {

    private int statusToko;
    private DBConnection dbConnection;

    public MainMenu() {
        // Menggunakan getInstance() untuk mendapatkan instance DBConnection
        dbConnection = DBConnection.getInstance();
        showMenu();
    }

    public int getStatusToko() {
        String query = "SELECT status FROM statustoko WHERE id = 1";
        
        // Menggunakan getConnection() untuk mendapatkan koneksi
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
    
            if (rs.next()) {
                statusToko = rs.getInt("status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return statusToko;
    }

    public void showMenu() {
        // Kode showMenu() tetap sama
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        JFrame mainMenu = new JFrame("Main Menu");
        mainMenu.setLayout(null);
        mainMenu.setSize(500, 400);
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int x = (screenSize.width - mainMenu.getWidth()) / 2;
        int y = (screenSize.height - mainMenu.getHeight()) / 2;
        mainMenu.setLocation(x, y);

        // ... rest of the showMenu code remains the same ...
        
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

        adminButton.addActionListener(e -> {
            mainMenu.dispose();
            new LoginView();
        });

        customerButton.addActionListener(e -> {
            mainMenu.dispose();
            customer();
        });

        mainMenu.setVisible(true);
    }

    public void customer() {
        int statusToko = getStatusToko();
        System.out.println("Status Toko: " + statusToko);
    
        if (statusToko == 0) {
            JOptionPane.showMessageDialog(null, "Toko sedang tutup. Silakan coba lagi nanti.");
            return;
        }

        // Kode customer() tetap sama
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        JFrame mainMenu = new JFrame("Main Menu Customer");
        mainMenu.setLayout(null);
        mainMenu.setSize(500, 400);
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int x = (screenSize.width - mainMenu.getWidth()) / 2;
        int y = (screenSize.height - mainMenu.getHeight()) / 2;
        mainMenu.setLocation(x, y);

        // ... rest of the customer code remains the same ...
        
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

        loginButton.addActionListener(e -> {
            mainMenu.dispose();
            new LoginView();
        });

        registerButton.addActionListener(e -> {
            mainMenu.dispose();
            new RegisterView();
        });

        mainMenu.setVisible(true);
    }
}