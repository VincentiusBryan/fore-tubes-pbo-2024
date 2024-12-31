package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Connection.DBConnection;


public class AdminView {

    private JFrame adminFrame;
    private JPanel contentPanel;
    private DBConnection dbConnection;

    public AdminView() {
        dbConnection= new DBConnection();
        showAdminView();
    }

    public void showAdminView() {
        // Setup the main frame
        adminFrame = new JFrame("Admin Menu");
        adminFrame.setSize(600, 550);
        adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        adminFrame.setLayout(new BorderLayout());

        // Create the menu panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(1, 6));

        // Add menu buttons
        String[] menuNames = {"All Customer", "Menu 2", "Menu 3", "Menu 4", "Menu 5", "Menu 6", "Menu 7","Menu 8"};
        for (String menuName : menuNames) {
            JButton menuButton = new JButton(menuName);
            menuButton.addActionListener(new MenuButtonListener(menuName));
            menuPanel.add(menuButton);
        }

        // Create the content panel
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        // Add panels to the frame
        adminFrame.add(menuPanel, BorderLayout.NORTH);
        adminFrame.add(contentPanel, BorderLayout.CENTER);

        // Set frame visibility
        adminFrame.setVisible(true);
    }



private void showAllCustomer() {
    contentPanel.removeAll();

    // Membuat panel utama
    JPanel menu1Panel = new JPanel();
    menu1Panel.setLayout(new BorderLayout());

    // Label judul
    JLabel titleLabel = new JLabel("MENU 1", JLabel.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
    menu1Panel.add(titleLabel, BorderLayout.NORTH);

    // Panel untuk tombol filter
    JPanel buttonPanel = new JPanel(new FlowLayout());
    JButton adminButton = new JButton("Admin");
    JButton userButton = new JButton("User");

    buttonPanel.add(adminButton);
    buttonPanel.add(userButton);
    menu1Panel.add(buttonPanel, BorderLayout.CENTER);

    // Menambahkan panel ke contentPanel
    contentPanel.add(menu1Panel);
    contentPanel.revalidate();
    contentPanel.repaint();

    // Event Listener untuk tombol
    adminButton.addActionListener(e -> filterUsers("Admin"));
    userButton.addActionListener(e -> filterUsers("User"));
}



// Fungsi untuk menampilkan pengguna berdasarkan filter
private void filterUsers(String userType) {
    contentPanel.removeAll();

    // Membuat tabel untuk menampilkan data
    String[] columnNames = {"id", "email", "phone_number", "user_type","created_at"};
    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
    JTable userTable = new JTable(tableModel);

    // Query database
    String query = "SELECT * FROM users WHERE user_type=?";
    try (Connection connection = dbConnection.connect();
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

        preparedStatement.setString(1, userType);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String email = resultSet.getString("email");
            String phone_number= resultSet.getString("phone_number");
            String type = resultSet.getString("user_type");
            String createdAt = resultSet.getString("created_at");

            tableModel.addRow(new Object[]{id, phone_number, email, type, createdAt});
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    
    JScrollPane scrollPane = new JScrollPane(userTable);
    contentPanel.add(scrollPane, BorderLayout.CENTER);
    contentPanel.revalidate();
    contentPanel.repaint();
}







    private class MenuButtonListener implements ActionListener {
        private final String menuName;

        public MenuButtonListener(String menuName) {
            this.menuName = menuName;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (menuName) {
                case "All Customer":
                    showAllCustomer();
                    break;
                case "Menu 2":
                    // showMenu2();
                    break;
                
                default:
                    contentPanel.removeAll();
                    contentPanel.add(new JLabel(menuName + " content goes here.", JLabel.CENTER));
                    contentPanel.revalidate();
                    contentPanel.repaint();
                    break;
            }
        }
    }

    public static void main(String[] args) {
        new AdminView();
    }
}
