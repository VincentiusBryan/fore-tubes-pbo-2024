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
    // private JFrame frame;
    // private JButton openButton;
    // private JButton closeButton;
    // private MainMenu mainMenu;
    private AdminController controller;
    private JFrame adminFrame;
    private JPanel contentPanel;
    private DBConnection dbConnection;
    private MainMenu mainMenu;

    public AdminView() {
        controller = new AdminController(); // Inisialisasi AdminController
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
        String[] menuNames = {"All Customer", "Edit Menu", "Show Promo", "Status Toko", "View Order", "View Karyawan", "Menu 7","Menu 8"};
        for (String menuName : menuNames) {
            JButton menuButton = new JButton(menuName);
            menuButton.addActionListener(new MenuButtonListener(menuName));
            menuPanel.add(menuButton);
        }
    
        // Create the content panel
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
    
        // Create the Back button to return to MainMenu
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        backButton.setBackground(new Color(44, 62, 80));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminFrame.dispose(); // Close the Admin Menu
                new MainMenu(); // Show the MainMenu again
            }
        });
    
        // Add back button to the content panel
        contentPanel.add(backButton, BorderLayout.SOUTH);
    
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
                case "Show Promo":
                    showPromos();
                    break;
                case "Status Toko":
                    statusToko();
                    break;
                case "View Order":
                    showAllOrders();
                    break;
                case "View Karyawan":
                    showAllKaryawan();
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





    // SHOW ALL CUSTOMER



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













    
// EDIT MENU

    private void editMenu() {
        AdminController controller= new AdminController();
        // Define column names for beverages and foods
        String[] beverageColumns = {"Name", "Size", "Price"};
        String[] foodColumns = {"Name", "Price"};

        // Create DefaultTableModels with the column names
        DefaultTableModel beverageModel = new DefaultTableModel(beverageColumns, 0);
        DefaultTableModel foodModel = new DefaultTableModel(foodColumns, 0);

        // Retrieve beverage data from the database
        try (ResultSet rs = controller.getBeverages()) {
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
        try (ResultSet rs = controller.getFoods()) {
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
                String name = (String) beverageTable.getValueAt(selectedRow, 0);
                controller.deleteItemFromDatabase("beverages", name);
                beverageModel.removeRow(selectedRow);
            } else {
                selectedRow = foodTable.getSelectedRow();
                if (selectedRow != -1) {
                    String name = (String) foodTable.getValueAt(selectedRow, 0);
                    controller.deleteItemFromDatabase("foods", name);
                    foodModel.removeRow(selectedRow);
                }
            }
        });

        // Create Edit button
        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> {
            int selectedRow = beverageTable.getSelectedRow();
            if (selectedRow != -1) {
                String name = (String) beverageTable.getValueAt(selectedRow, 0);
                String size = (String) beverageTable.getValueAt(selectedRow, 1);
                double price = (Double) beverageTable.getValueAt(selectedRow, 2);
                String newName = JOptionPane.showInputDialog("Edit Name:", name);
                String newSize = JOptionPane.showInputDialog("Edit Size:", size);
                String newPrice = JOptionPane.showInputDialog("Edit Price:", price);
                if (newName != null && newSize != null && newPrice != null) {
                    controller.updateItemInDatabase("beverages", name, newName, newSize, Double.parseDouble(newPrice));
                    beverageModel.setValueAt(newName, selectedRow, 0);
                    beverageModel.setValueAt(newSize, selectedRow, 1);
                    beverageModel.setValueAt(Double.parseDouble(newPrice), selectedRow, 2);
                }
            } else {
                selectedRow = foodTable.getSelectedRow();
                if (selectedRow != -1) {
                    String name = (String) foodTable.getValueAt(selectedRow, 0);
                    double price = (Double) foodTable.getValueAt(selectedRow, 1);
                    String newName = JOptionPane.showInputDialog("Edit Name:", name);
                    String newPrice = JOptionPane.showInputDialog("Edit Price:", price);
                    if (newName != null && newPrice != null) {
                        controller.updateItemInDatabase("foods", name, newName, Double.parseDouble(newPrice));
                        foodModel.setValueAt(newName, selectedRow, 0);
                        foodModel.setValueAt(Double.parseDouble(newPrice), selectedRow, 1);
                    }
                }
            }
        });

        // Create Add button
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            String[] options = {"Food", "Beverage"};
            int choice = JOptionPane.showOptionDialog(null, "Choose the type of item to add", 
                    "Add Menu", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, 
                    null, options, options[0]);

            if (choice == 0) {
                String name = JOptionPane.showInputDialog("Enter Food Name:");
                String priceStr = JOptionPane.showInputDialog("Enter Food Price:");
                if (name != null && priceStr != null) {
                    double price = Double.parseDouble(priceStr);
                    controller.addItemToDatabase("foods", name, price);
                    foodModel.addRow(new Object[]{name, price});
                }
            } else if (choice == 1) {
                String name = JOptionPane.showInputDialog("Enter Beverage Name:");
                String size = JOptionPane.showInputDialog("Enter Beverage Size:");
                String priceStr = JOptionPane.showInputDialog("Enter Beverage Price:");
                if (name != null && size != null && priceStr != null) {
                    double price = Double.parseDouble(priceStr);
                    controller.addItemToDatabase("beverages", name, size, price);
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

    public JPanel getContentPanel() {
        return contentPanel;
    }






















    //menu 4


    private void showPromos() {
        // Define columns for the promos table
        String[] promoColumns = {"Promo Name", "Description", "Discount %", "Start Date", "End Date", "Active"};

        // Create DefaultTableModel for promos
        DefaultTableModel promoModel = new DefaultTableModel(promoColumns, 0);

        // Retrieve promo data from the database
        String promoQuery = "SELECT * FROM promos";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(promoQuery);
             ResultSet rs = stmt.executeQuery()) {

            // Populate the promo model with database data
            while (rs.next()) {
                String promoName = rs.getString("promo_name");
                String description = rs.getString("description");
                double discount = rs.getDouble("discount_percentage");
                String startDate = rs.getString("start_date");
                String endDate = rs.getString("end_date");
                boolean isActive = rs.getBoolean("is_active");
                Object[] row = {promoName, description, discount, startDate, endDate, isActive};
                promoModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create JTable for promos
        JTable promoTable = new JTable(promoModel);

        // Add the table to a scroll pane
        JScrollPane promoScrollPane = new JScrollPane(promoTable);

        // Create labels for the section
        JLabel promoLabel = new JLabel("PROMOS", JLabel.CENTER);
        promoLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // Create Delete button
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            // Get the selected row
            int selectedRow = promoTable.getSelectedRow();
            if (selectedRow != -1) {
                // Remove the selected row from the model and database
                String promoName = (String) promoTable.getValueAt(selectedRow, 0);
                controller.deletePromoFromDatabase(promoName);
                promoModel.removeRow(selectedRow);
            }
        });

        // Create Edit button
        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> {
            // Get the selected row
            int selectedRow = promoTable.getSelectedRow();
            if (selectedRow != -1) {
                // Get current promo details
                String promoName = (String) promoTable.getValueAt(selectedRow, 0);
                String description = (String) promoTable.getValueAt(selectedRow, 1);
                double discount = (Double) promoTable.getValueAt(selectedRow, 2);
                String startDate = (String) promoTable.getValueAt(selectedRow, 3);
                String endDate = (String) promoTable.getValueAt(selectedRow, 4);
                boolean isActive = (Boolean) promoTable.getValueAt(selectedRow, 5);

                // Prompt for new values
                String newPromoName = JOptionPane.showInputDialog("Edit Promo Name:", promoName);
                String newDescription = JOptionPane.showInputDialog("Edit Description:", description);
                String newDiscountStr = JOptionPane.showInputDialog("Edit Discount (%):", discount);
                String newStartDate = JOptionPane.showInputDialog("Edit Start Date (YYYY-MM-DD):", startDate);
                String newEndDate = JOptionPane.showInputDialog("Edit End Date (YYYY-MM-DD):", endDate);
                int newIsActive = JOptionPane.showConfirmDialog(null, "Is this promo active?", "Edit Active Status", JOptionPane.YES_NO_OPTION);

                if (newPromoName != null && newDescription != null && newDiscountStr != null && newStartDate != null && newEndDate != null) {
                    // Update database and table model
                    controller.updatePromoInDatabase(
                            promoName,
                            newPromoName,
                            newDescription,
                            Double.parseDouble(newDiscountStr),
                            newStartDate,
                            newEndDate,
                            newIsActive == JOptionPane.YES_OPTION
                    );
                    promoModel.setValueAt(newPromoName, selectedRow, 0);
                    promoModel.setValueAt(newDescription, selectedRow, 1);
                    promoModel.setValueAt(Double.parseDouble(newDiscountStr), selectedRow, 2);
                    promoModel.setValueAt(newStartDate, selectedRow, 3);
                    promoModel.setValueAt(newEndDate, selectedRow, 4);
                    promoModel.setValueAt(newIsActive == JOptionPane.YES_OPTION, selectedRow, 5);
                }
            }
        });

        // Create Add button
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            // Prompt for new promo details
            String promoName = JOptionPane.showInputDialog("Enter Promo Name:");
            String description = JOptionPane.showInputDialog("Enter Description:");
            String discountStr = JOptionPane.showInputDialog("Enter Discount (%):");
            String startDate = JOptionPane.showInputDialog("Enter Start Date (YYYY-MM-DD):");
            String endDate = JOptionPane.showInputDialog("Enter End Date (YYYY-MM-DD):");
            int isActive = JOptionPane.showConfirmDialog(null, "Is this promo active?", "Add Active Status", JOptionPane.YES_NO_OPTION);

            if (promoName != null && description != null && discountStr != null && startDate != null && endDate != null) {
                // Add promo to database and update table model
                controller.addPromoToDatabase(
                        promoName,
                        description,
                        Double.parseDouble(discountStr),
                        startDate,
                        endDate,
                        isActive == JOptionPane.YES_OPTION
                );
                promoModel.addRow(new Object[]{
                        promoName,
                        description,
                        Double.parseDouble(discountStr),
                        startDate,
                        endDate,
                        isActive == JOptionPane.YES_OPTION
                });
            }
        });

        // Create a panel to hold the table and buttons
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        tablePanel.add(promoLabel);
        tablePanel.add(promoScrollPane);
        tablePanel.add(deleteButton);
        tablePanel.add(editButton);
        tablePanel.add(addButton);

        // Clear the current content panel and add the new table panel
        contentPanel.removeAll();
        contentPanel.add(tablePanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    

    


    

    
    private void statusToko() {
    // Clear existing content
    contentPanel.removeAll();
    
    // Create main panel for status toko
    JPanel statusPanel = new JPanel();
    statusPanel.setLayout(new BorderLayout());
    statusPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
    // Create and style the status label
    JLabel statusLabel = new JLabel("Status Toko: Tidak Diketahui", JLabel.CENTER);
    statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
    statusPanel.add(statusLabel, BorderLayout.CENTER);
    
    // Create button panel
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
    
    // Create and style the open button
    JButton openButton = new JButton("Buka Toko");
    openButton.setPreferredSize(new Dimension(120, 40));
    openButton.setFont(new Font("Arial", Font.PLAIN, 14));
    openButton.addActionListener(e -> {
        controller.updateStatusToko(1);
        statusLabel.setText("Status Toko: Buka");
    });
    
    // Create and style the close button
    JButton closeButton = new JButton("Tutup Toko");
    closeButton.setPreferredSize(new Dimension(120, 40));
    closeButton.setFont(new Font("Arial", Font.PLAIN, 14));
    closeButton.addActionListener(e -> {
        controller.updateStatusToko(0);
        statusLabel.setText("Status Toko: Tutup");
    });
    
    // Add buttons to button panel
    buttonPanel.add(openButton);
    buttonPanel.add(closeButton);
    
    // Add button panel to status panel
    statusPanel.add(buttonPanel, BorderLayout.SOUTH);
    
    // Add status panel to content panel
    contentPanel.add(statusPanel, BorderLayout.CENTER);
    
    // Refresh the display
    contentPanel.revalidate();
    contentPanel.repaint();
}
    
    // MENU 5
    private void showAllOrders() {
        contentPanel.removeAll();
        JPanel ordersPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("View Orders", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        ordersPanel.add(titleLabel, BorderLayout.NORTH);
        String[] columnNames = {"ID Transaksi", "Email", "Nama Makanan", "Nama Minuman", 
                              "Tipe Item", "Ukuran", "Jumlah", "Total Harga", 
                              "Waktu Transaksi", "Promo", "Selesai"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        controller.showAllOrders(tableModel);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        table.getColumnModel().getColumn(10).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        table.getModel().addTableModelListener(e -> {
            if (e.getColumn() == 10) {
                int selectedRow = table.getSelectedRow();
                int idTransaksi = (int) table.getValueAt(selectedRow, 0);
                boolean status = (boolean) table.getValueAt(selectedRow, 10);
                controller.updateOrderStatus(idTransaksi, status);
            }
        });
        ordersPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(ordersPanel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }




















    public void showAllKaryawan(){
        contentPanel.removeAll();
JPanel karyawanPanel = new JPanel(new BorderLayout());
JLabel titleLabel = new JLabel("View Karyawan", JLabel.CENTER);
titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
karyawanPanel.add(titleLabel, BorderLayout.NORTH);

// Definisikan kolom tabel
String[] columnNames = {"ID", "Nama", "Peran", "Jam Kerja", "Gaji"};
DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

// Ambil data karyawan dari controller
controller.showAllKaryawan(tableModel);

// Tampilkan tabel
JTable table = new JTable(tableModel);
JScrollPane scrollPane = new JScrollPane(table);
karyawanPanel.add(scrollPane, BorderLayout.CENTER);

// Tambahkan panel ke contentPanel
contentPanel.add(karyawanPanel);
contentPanel.revalidate();
contentPanel.repaint();


JPanel actionPanel = new JPanel(new FlowLayout());

JButton addButton = new JButton("Add");
addButton.addActionListener(e -> {
    String nama = JOptionPane.showInputDialog("Masukkan Nama:");
    String peran = JOptionPane.showInputDialog("Masukkan Peran:");
    String jamKerja = JOptionPane.showInputDialog("Masukkan Jam Kerja (contoh: Senin–Jumat 09:00–17:00):");
    double gaji = Double.parseDouble(JOptionPane.showInputDialog("Masukkan Gaji:"));
    controller.addKaryawan(nama, peran, jamKerja, gaji);
    controller.showAllKaryawan(tableModel); // Refresh tabel
});

JButton editButton = new JButton("Edit");
editButton.addActionListener(e -> {
    int selectedRow = table.getSelectedRow();
    if (selectedRow >= 0) {
        int id = (int) table.getValueAt(selectedRow, 0);
        String nama = JOptionPane.showInputDialog("Edit Nama:", table.getValueAt(selectedRow, 1));
        String peran = JOptionPane.showInputDialog("Edit Peran:", table.getValueAt(selectedRow, 2));
        String jamKerja = JOptionPane.showInputDialog("Edit Jam Kerja:", table.getValueAt(selectedRow, 3));
        double gaji = Double.parseDouble(JOptionPane.showInputDialog("Edit Gaji:", table.getValueAt(selectedRow, 4)));
        controller.editKaryawan(id, nama, peran, jamKerja, gaji);
        controller.showAllKaryawan(tableModel); // Refresh tabel
    } else {
        JOptionPane.showMessageDialog(null, "Pilih baris untuk diedit.");
    }
});

JButton deleteButton = new JButton("Delete");
deleteButton.addActionListener(e -> {
    int selectedRow = table.getSelectedRow();
    if (selectedRow >= 0) {
        int id = (int) table.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(null, "Yakin ingin menghapus karyawan?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            controller.deleteKaryawan(id);
            controller.showAllKaryawan(tableModel); // Refresh tabel
        }
    } else {
        JOptionPane.showMessageDialog(null, "Pilih baris untuk dihapus.");
    }
});

// Tambahkan tombol ke panel
actionPanel.add(addButton);
actionPanel.add(editButton);
actionPanel.add(deleteButton);
contentPanel.add(actionPanel, BorderLayout.SOUTH);


    }
    


public static void main(String[] args) {
  new AdminView();
  
}

  
}