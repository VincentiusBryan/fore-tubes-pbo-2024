package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Connection.DBConnection;
import Controller.AdminController;


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
        JLabel titleLabel = new JLabel("All Customers", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        menu1Panel.add(titleLabel, BorderLayout.NORTH);

        // Panel untuk tombol filter
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton adminButton = new JButton("Admin");
        JButton userButton = new JButton("User");

        buttonPanel.add(adminButton);
        buttonPanel.add(userButton);
        menu1Panel.add(buttonPanel, BorderLayout.NORTH);

        // Panel untuk tabel
        JPanel tablePanel = new JPanel(new BorderLayout());
        String[] columnNames = {"ID", "Email", "Phone Number", "User Type", "Created At"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Tambahkan tablePanel ke menu1Panel
        menu1Panel.add(tablePanel, BorderLayout.CENTER);

        // Tambahkan menu1Panel ke contentPanel
        contentPanel.add(menu1Panel, BorderLayout.CENTER);

        // Event Listener untuk tombol
        adminButton.addActionListener(e -> updateTable(tableModel, "Admin"));
        userButton.addActionListener(e -> updateTable(tableModel, "User"));

        contentPanel.revalidate();
        contentPanel.repaint();
    }



private void updateTable(DefaultTableModel tableModel, String userType) {
    AdminController controller = new AdminController();
    controller.updateTable(tableModel, userType);
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
