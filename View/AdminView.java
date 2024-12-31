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
        String[] menuNames = {"All Customer", "Edit Menu", "Menu 3", "Menu 4", "Menu 5", "Menu 6", "Menu 7","Menu 8"};
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
                case "Edit Menu":
                    editMenu();
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


    private void editMenu() {
        // Define column names for beverages and foods
        String[] beverageColumns = {"Name", "Size", "Price"};
        String[] foodColumns = {"Name", "Price"};

        // Create DefaultTableModels with the column names
        DefaultTableModel beverageModel = new DefaultTableModel(beverageColumns, 0);
        DefaultTableModel foodModel = new DefaultTableModel(foodColumns, 0);

        // Create an instance of DBConnection to get the connection
        DBConnection dbConnection = new DBConnection();

        // Retrieve beverage data from the database
        String beverageQuery = "SELECT * FROM beverages";
        try (Connection conn = dbConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(beverageQuery);
             ResultSet rs = stmt.executeQuery()) {

            // Add beverage data to the beverage model
            while (rs.next()) {
                String name = rs.getString("name");
                String size = rs.getString("size");
                double price = rs.getDouble("price");
                Object[] row = {name, size, price};
                beverageModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Retrieve food data from the database
        String foodQuery = "SELECT * FROM foods";
        try (Connection conn = dbConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(foodQuery);
             ResultSet rs = stmt.executeQuery()) {

            // Add food data to the food model
            while (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                Object[] row = {name, price};
                foodModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create JTables with the models
        JTable beverageTable = new JTable(beverageModel);
        JTable foodTable = new JTable(foodModel);

        // Add the tables to scroll panes
        JScrollPane beverageScrollPane = new JScrollPane(beverageTable);
        JScrollPane foodScrollPane = new JScrollPane(foodTable);

        // Create labels for the sections with larger font and centered text
        JLabel beverageLabel = new JLabel("BEVERAGES", JLabel.CENTER);
        JLabel foodLabel = new JLabel("FOODS", JLabel.CENTER);

        // Set font size for labels
        beverageLabel.setFont(new Font("Arial", Font.BOLD, 18));
        foodLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // Create Delete button
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            // Get the selected row from beverage or food table
            int selectedRow = beverageTable.getSelectedRow();
            if (selectedRow != -1) {
                // If a row in beverage table is selected, remove it from the model and database
                String name = (String) beverageTable.getValueAt(selectedRow, 0);
                deleteItemFromDatabase("beverages", name);
                beverageModel.removeRow(selectedRow);
            } else {
                selectedRow = foodTable.getSelectedRow();
                if (selectedRow != -1) {
                    // If a row in food table is selected, remove it from the model and database
                    String name = (String) foodTable.getValueAt(selectedRow, 0);
                    deleteItemFromDatabase("foods", name);
                    foodModel.removeRow(selectedRow);
                }
            }
        });

        // Create Edit button
        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> {
            // Get the selected row from beverage or food table
            int selectedRow = beverageTable.getSelectedRow();
            if (selectedRow != -1) {
                // If a row in beverage table is selected, open edit dialog
                String name = (String) beverageTable.getValueAt(selectedRow, 0);
                String size = (String) beverageTable.getValueAt(selectedRow, 1);
                double price = (Double) beverageTable.getValueAt(selectedRow, 2);
                // Prompt for editing
                String newName = JOptionPane.showInputDialog("Edit Name:", name);
                String newSize = JOptionPane.showInputDialog("Edit Size:", size);
                String newPrice = JOptionPane.showInputDialog("Edit Price:", price);
                if (newName != null && newSize != null && newPrice != null) {
                    // Update the model and database
                    updateItemInDatabase("beverages", name, newName, newSize, Double.parseDouble(newPrice));
                    beverageModel.setValueAt(newName, selectedRow, 0);
                    beverageModel.setValueAt(newSize, selectedRow, 1);
                    beverageModel.setValueAt(Double.parseDouble(newPrice), selectedRow, 2);
                }
            } else {
                selectedRow = foodTable.getSelectedRow();
                if (selectedRow != -1) {
                    // If a row in food table is selected, open edit dialog
                    String name = (String) foodTable.getValueAt(selectedRow, 0);
                    double price = (Double) foodTable.getValueAt(selectedRow, 1);
                    // Prompt for editing
                    String newName = JOptionPane.showInputDialog("Edit Name:", name);
                    String newPrice = JOptionPane.showInputDialog("Edit Price:", price);
                    if (newName != null && newPrice != null) {
                        // Update the model and database
                        updateItemInDatabase("foods", name, newName, Double.parseDouble(newPrice));
                        foodModel.setValueAt(newName, selectedRow, 0);
                        foodModel.setValueAt(Double.parseDouble(newPrice), selectedRow, 1);
                    }
                }
            }
        });

        // Create Add button
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            // Ask user if they want to add a food or beverage
            String[] options = {"Food", "Beverage"};
            int choice = JOptionPane.showOptionDialog(null, "Choose the type of item to add", 
                    "Add Menu", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, 
                    null, options, options[0]);

            if (choice == 0) {
                // Food - ask for name and price
                String name = JOptionPane.showInputDialog("Enter Food Name:");
                String priceStr = JOptionPane.showInputDialog("Enter Food Price:");
                if (name != null && priceStr != null) {
                    double price = Double.parseDouble(priceStr);
                    // Add food to database and update model
                    addItemToDatabase("foods", name, price);
                    foodModel.addRow(new Object[]{name, price});
                }
            } else if (choice == 1) {
                // Beverage - ask for name, size, and price
                String name = JOptionPane.showInputDialog("Enter Beverage Name:");
                String size = JOptionPane.showInputDialog("Enter Beverage Size:");
                String priceStr = JOptionPane.showInputDialog("Enter Beverage Price:");
                if (name != null && size != null && priceStr != null) {
                    double price = Double.parseDouble(priceStr);
                    // Add beverage to database and update model
                    addItemToDatabase("beverages", name, size, price);
                    beverageModel.addRow(new Object[]{name, size, price});
                }
            }
        });

        // Set a smaller size for the buttons
        deleteButton.setPreferredSize(new Dimension(100, 30));
        editButton.setPreferredSize(new Dimension(100, 30));
        addButton.setPreferredSize(new Dimension(100, 30));

        // Create a panel to hold the tables, labels, and buttons using FlowLayout
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        tablePanel.add(beverageLabel);
        tablePanel.add(beverageScrollPane);
        tablePanel.add(foodLabel);
        tablePanel.add(foodScrollPane);
        tablePanel.add(deleteButton);
        tablePanel.add(editButton);
        tablePanel.add(addButton);

        // Clear the current content panel
        contentPanel.removeAll();

        // Add the table panel to the content panel
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        // Revalidate and repaint the content panel to update the display
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // Method to delete an item from the database (for foods and beverages)
    private void deleteItemFromDatabase(String table, String name) {
        String deleteQuery = "DELETE FROM " + table + " WHERE name = ?";
        try (Connection conn = new DBConnection().connect();
             PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update an item in the database (for foods)
    private void updateItemInDatabase(String table, String oldName, String newName, double newPrice) {
        String updateQuery = "UPDATE " + table + " SET name = ?, price = ? WHERE name = ?";
        try (Connection conn = new DBConnection().connect();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setString(1, newName);
            stmt.setDouble(2, newPrice);
            stmt.setString(3, oldName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update an item in the database (for beverages)
    private void updateItemInDatabase(String table, String oldName, String newName, String newSize, double newPrice) {
        String updateQuery = "UPDATE " + table + " SET name = ?, size = ?, price = ? WHERE name = ?";
        try (Connection conn = new DBConnection().connect();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setString(1, newName);
            stmt.setString(2, newSize);
            stmt.setDouble(3, newPrice);
            stmt.setString(4, oldName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to add an item to the database (for foods and beverages)
    private void addItemToDatabase(String table, String name, double price) {
        String insertQuery = "INSERT INTO " + table + " (name, price) VALUES (?, ?)";
        try (Connection conn = new DBConnection().connect();
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to add an item to the database (for beverages)
    private void addItemToDatabase(String table, String name, String size, double price) {
        String insertQuery = "INSERT INTO " + table + " (name, size, price) VALUES (?, ?, ?)";
        try (Connection conn = new DBConnection().connect();
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            stmt.setString(1, name);
            stmt.setString(2, size);
            stmt.setDouble(3, price);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public JPanel getContentPanel() {
        return contentPanel;
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







    public static void main(String[] args) {
        new AdminView();
    }
}
