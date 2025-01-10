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
        controller = new AdminController();
        showAdminView();
    }

    public void showAdminView() {
        adminFrame = new JFrame("Admin Menu");
        adminFrame.setSize(600, 550);
        adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        adminFrame.setLayout(new BorderLayout());

        adminFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(1, 6));

        String[] menuNames = { "All Users", "Edit Menu", "Show Promo", "Status Toko", "View Order", "View Karyawan",
                "Laporan Penjualan", "Membership", "Back" };
        for (String menuName : menuNames) {
            JButton menuButton = new JButton(menuName);
            menuButton.addActionListener(new MenuButtonListener(menuName));
            menuPanel.add(menuButton);
        }

        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        backButton.setBackground(new Color(44, 62, 80));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminFrame.dispose();
                new MainMenu();
            }
        });

        contentPanel.add(backButton, BorderLayout.SOUTH);

        adminFrame.add(menuPanel, BorderLayout.NORTH);
        adminFrame.add(contentPanel, BorderLayout.CENTER);

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
                case "All Users":
                    showAllUsers();
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
                case "Laporan Penjualan":
                    showSalesReport();
                    break;
                case "Membership":
                    showMembership();
                    break;
                case "Back":
                    new MainMenu();
                    adminFrame.dispose();
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

    private void showAllUsers() {
        contentPanel.removeAll();

        JPanel menu1Panel = new JPanel();
        menu1Panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("All Customers", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        menu1Panel.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton adminButton = new JButton("Admin");
        JButton userButton = new JButton("User");

        JButton deleteButton = new JButton("Delete");
        JButton editButton = new JButton("Edit");

        buttonPanel.add(adminButton);
        buttonPanel.add(userButton);
        // buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        menu1Panel.add(buttonPanel, BorderLayout.NORTH);

        JPanel tablePanel = new JPanel(new BorderLayout());
        String[] columnNames = { "ID", "Email", "Phone Number", "User Type", "Created At", "Points", "id_membership",
                "Status Aktif Membership" };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        menu1Panel.add(tablePanel, BorderLayout.CENTER);

        contentPanel.add(menu1Panel, BorderLayout.CENTER);

        adminButton.addActionListener(e -> updateTable(tableModel, "Admin"));
        userButton.addActionListener(e -> updateTable(tableModel, "User"));
        // addButton.addActionListener(e -> showAddUserDialog());
        deleteButton.addActionListener(e -> deleteUser(userTable, tableModel));
        editButton.addActionListener(e -> editUser(userTable, tableModel));

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // private void showAddUserDialog() {

    // String email = JOptionPane.showInputDialog("Enter Email:");
    // String phoneNumber = JOptionPane.showInputDialog("Enter Phone Number:");
    // String userType = JOptionPane.showInputDialog("Enter User Type:");
    // String createdAt = JOptionPane.showInputDialog("Enter Created At:");
    // int points = Integer.parseInt(JOptionPane.showInputDialog("Enter Points:"));
    // int idMembership = Integer.parseInt(JOptionPane.showInputDialog("Enter
    // Membership ID:"));
    // int statusMembership = Integer.parseInt(JOptionPane.showInputDialog("Enter
    // Membership Status:"));

    // AdminController controller = new AdminController();
    // controller.addUser(email, phoneNumber, userType, createdAt, points,
    // idMembership, statusMembership);
    // }

    private void deleteUser(JTable userTable, DefaultTableModel tableModel) {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1) {
            int userId = (int) tableModel.getValueAt(selectedRow, 0);
            AdminController controller = new AdminController();
            controller.deleteUser(userId);
            tableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(null, "Please select a user to delete.");
        }
    }

    private void editUser(JTable userTable, DefaultTableModel tableModel) {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1) {
            int userId = (int) tableModel.getValueAt(selectedRow, 0);
            String email = (String) tableModel.getValueAt(selectedRow, 1);
            String phoneNumber = (String) tableModel.getValueAt(selectedRow, 2);
            String userType = (String) tableModel.getValueAt(selectedRow, 3);
            String createdAt = (String) tableModel.getValueAt(selectedRow, 4);
            int points = (int) tableModel.getValueAt(selectedRow, 5);
            int idMembership = (int) tableModel.getValueAt(selectedRow, 6);
            int statusMembership = (int) tableModel.getValueAt(selectedRow, 7);

            email = JOptionPane.showInputDialog("Edit Email:", email);
            phoneNumber = JOptionPane.showInputDialog("Edit Phone Number:", phoneNumber);
            userType = JOptionPane.showInputDialog("Edit User Type:", userType);
            createdAt = JOptionPane.showInputDialog("Edit Created At:", createdAt);

            idMembership = Integer.parseInt(JOptionPane.showInputDialog("Edit Membership ID:", idMembership));
            points = Integer.parseInt(JOptionPane.showInputDialog("Edit Points:", points));
            statusMembership = Integer
                    .parseInt(JOptionPane.showInputDialog("Edit Membership Status:", statusMembership));

            AdminController controller = new AdminController();
            controller.updateUser(userId, email, phoneNumber, userType, createdAt, points, idMembership,
                    statusMembership);

            tableModel.setValueAt(email, selectedRow, 1);
            tableModel.setValueAt(phoneNumber, selectedRow, 2);
            tableModel.setValueAt(userType, selectedRow, 3);
            tableModel.setValueAt(createdAt, selectedRow, 4);
            tableModel.setValueAt(points, selectedRow, 5);
            tableModel.setValueAt(idMembership, selectedRow, 6);
            tableModel.setValueAt(statusMembership, selectedRow, 7);
        } else {
            JOptionPane.showMessageDialog(null, "Please select a user to edit.");
        }
    }

    private void updateTable(DefaultTableModel tableModel, String userType) {
        AdminController controller = new AdminController();
        controller.updateTableShowUsers(tableModel, userType);
    }

    // EDIT MENU

    private void editMenu() {
        AdminController controller = new AdminController();
        String[] beverageColumns = { "Name", "Size", "Price" };
        String[] foodColumns = { "Name", "Price" };

        DefaultTableModel beverageModel = new DefaultTableModel(beverageColumns, 0);
        DefaultTableModel foodModel = new DefaultTableModel(foodColumns, 0);

        try (ResultSet rs = controller.getBeverages()) {
            while (rs.next()) {
                String name = rs.getString("name");
                String size = rs.getString("size");
                double price = rs.getDouble("price");
                Object[] row = { name, size, price };
                beverageModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (ResultSet rs = controller.getFoods()) {
            while (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                Object[] row = { name, price };
                foodModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JTable beverageTable = new JTable(beverageModel);
        JTable foodTable = new JTable(foodModel);

        JScrollPane beverageScrollPane = new JScrollPane(beverageTable);
        JScrollPane foodScrollPane = new JScrollPane(foodTable);

        JLabel beverageLabel = new JLabel("BEVERAGES", JLabel.CENTER);
        JLabel foodLabel = new JLabel("FOODS", JLabel.CENTER);

        beverageLabel.setFont(new Font("Arial", Font.BOLD, 18));
        foodLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
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

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            String[] options = { "Food", "Beverage" };
            int choice = JOptionPane.showOptionDialog(null, "Choose the type of item to add",
                    "Add Menu", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, options, options[0]);

            if (choice == 0) {
                String name = JOptionPane.showInputDialog("Enter Food Name:");
                String priceStr = JOptionPane.showInputDialog("Enter Food Price:");
                if (name != null && priceStr != null) {
                    double price = Double.parseDouble(priceStr);
                    controller.addItemToDatabase("foods", name, price);
                    foodModel.addRow(new Object[] { name, price });
                }
            } else if (choice == 1) {
                String name = JOptionPane.showInputDialog("Enter Beverage Name:");
                String size = JOptionPane.showInputDialog("Enter Beverage Size:");
                String priceStr = JOptionPane.showInputDialog("Enter Beverage Price:");
                if (name != null && size != null && priceStr != null) {
                    double price = Double.parseDouble(priceStr);
                    controller.addItemToDatabase("beverages", name, size, price);
                    beverageModel.addRow(new Object[] { name, size, price });
                }
            }
        });

        deleteButton.setPreferredSize(new Dimension(100, 30));
        editButton.setPreferredSize(new Dimension(100, 30));
        addButton.setPreferredSize(new Dimension(100, 30));

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        tablePanel.add(beverageLabel);
        tablePanel.add(beverageScrollPane);
        tablePanel.add(foodLabel);
        tablePanel.add(foodScrollPane);
        tablePanel.add(deleteButton);
        tablePanel.add(editButton);
        tablePanel.add(addButton);

        contentPanel.removeAll();

        contentPanel.add(tablePanel, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    // menu 4

    private void showPromos() {
        String[] promoColumns = { "Promo Name", "Description", "Discount %", "Start Date", "End Date", "Active" };

        DefaultTableModel promoModel = new DefaultTableModel(promoColumns, 0);

        String promoQuery = "SELECT * FROM promos";
        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(promoQuery);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String promoName = rs.getString("promo_name");
                String description = rs.getString("description");
                double discount = rs.getDouble("discount_percentage");
                String startDate = rs.getString("start_date");
                String endDate = rs.getString("end_date");
                boolean isActive = rs.getBoolean("is_active");
                Object[] row = { promoName, description, discount, startDate, endDate, isActive };
                promoModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JTable promoTable = new JTable(promoModel);

        JScrollPane promoScrollPane = new JScrollPane(promoTable);

        JLabel promoLabel = new JLabel("PROMOS", JLabel.CENTER);
        promoLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            int selectedRow = promoTable.getSelectedRow();
            if (selectedRow != -1) {
                String promoName = (String) promoTable.getValueAt(selectedRow, 0);
                controller.deletePromoFromDatabase(promoName);
                promoModel.removeRow(selectedRow);
            }
        });

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> {

            int selectedRow = promoTable.getSelectedRow();
            if (selectedRow != -1) {
                String promoName = (String) promoTable.getValueAt(selectedRow, 0);
                String description = (String) promoTable.getValueAt(selectedRow, 1);
                double discount = (Double) promoTable.getValueAt(selectedRow, 2);
                String startDate = (String) promoTable.getValueAt(selectedRow, 3);
                String endDate = (String) promoTable.getValueAt(selectedRow, 4);
                boolean isActive = (Boolean) promoTable.getValueAt(selectedRow, 5);

                String newPromoName = JOptionPane.showInputDialog("Edit Promo Name:", promoName);
                String newDescription = JOptionPane.showInputDialog("Edit Description:", description);
                String newDiscountStr = JOptionPane.showInputDialog("Edit Discount (%):", discount);
                String newStartDate = JOptionPane.showInputDialog("Edit Start Date (YYYY-MM-DD):", startDate);
                String newEndDate = JOptionPane.showInputDialog("Edit End Date (YYYY-MM-DD):", endDate);
                int newIsActive = JOptionPane.showConfirmDialog(null, "Is this promo active?", "Edit Active Status",
                        JOptionPane.YES_NO_OPTION);

                if (newPromoName != null && newDescription != null && newDiscountStr != null && newStartDate != null
                        && newEndDate != null) {
                    controller.updatePromoInDatabase(
                            promoName,
                            newPromoName,
                            newDescription,
                            Double.parseDouble(newDiscountStr),
                            newStartDate,
                            newEndDate,
                            newIsActive == JOptionPane.YES_OPTION);
                    promoModel.setValueAt(newPromoName, selectedRow, 0);
                    promoModel.setValueAt(newDescription, selectedRow, 1);
                    promoModel.setValueAt(Double.parseDouble(newDiscountStr), selectedRow, 2);
                    promoModel.setValueAt(newStartDate, selectedRow, 3);
                    promoModel.setValueAt(newEndDate, selectedRow, 4);
                    promoModel.setValueAt(newIsActive == JOptionPane.YES_OPTION, selectedRow, 5);
                }
            }
        });

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            String promoName = JOptionPane.showInputDialog("Enter Promo Name:");
            String description = JOptionPane.showInputDialog("Enter Description:");
            String discountStr = JOptionPane.showInputDialog("Enter Discount (%):");
            String startDate = JOptionPane.showInputDialog("Enter Start Date (YYYY-MM-DD):");
            String endDate = JOptionPane.showInputDialog("Enter End Date (YYYY-MM-DD):");
            int isActive = JOptionPane.showConfirmDialog(null, "Is this promo active?", "Add Active Status",
                    JOptionPane.YES_NO_OPTION);

            if (promoName != null && description != null && discountStr != null && startDate != null
                    && endDate != null) {

                controller.addPromoToDatabase(
                        promoName,
                        description,
                        Double.parseDouble(discountStr),
                        startDate,
                        endDate,
                        isActive == JOptionPane.YES_OPTION);
                promoModel.addRow(new Object[] {
                        promoName,
                        description,
                        Double.parseDouble(discountStr),
                        startDate,
                        endDate,
                        isActive == JOptionPane.YES_OPTION
                });
            }
        });

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        tablePanel.add(promoLabel);
        tablePanel.add(promoScrollPane);
        tablePanel.add(deleteButton);
        tablePanel.add(editButton);
        tablePanel.add(addButton);

        contentPanel.removeAll();
        contentPanel.add(tablePanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void statusToko() {

        contentPanel.removeAll();

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel statusLabel = new JLabel("Status Toko: Tidak Diketahui", JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        statusPanel.add(statusLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton openButton = new JButton("Buka Toko");
        openButton.setPreferredSize(new Dimension(120, 40));
        openButton.setFont(new Font("Arial", Font.PLAIN, 14));
        openButton.addActionListener(e -> {
            controller.updateStatusToko(1);
            statusLabel.setText("Status Toko: Buka");
        });

        JButton closeButton = new JButton("Tutup Toko");
        closeButton.setPreferredSize(new Dimension(120, 40));
        closeButton.setFont(new Font("Arial", Font.PLAIN, 14));
        closeButton.addActionListener(e -> {
            controller.updateStatusToko(0);
            statusLabel.setText("Status Toko: Tutup");
        });

        buttonPanel.add(openButton);
        buttonPanel.add(closeButton);

        statusPanel.add(buttonPanel, BorderLayout.SOUTH);

        contentPanel.add(statusPanel, BorderLayout.CENTER);

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

        String[] columnNames = {
                "ID Detail", "ID Transaksi", "Email Customer", "Nama Item",
                "Tipe Item", "Ukuran", "Jumlah", "Harga/Item", "Total Harga",
                "Lokasi", "Alamat Delivery", "Keterangan",
                "Tanggal Transaksi"
        };

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        controller.showAllOrders(tableModel);

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        table.getColumnModel().getColumn(0).setPreferredWidth(60); // ID Detail
        table.getColumnModel().getColumn(1).setPreferredWidth(60); // ID Transaksi
        table.getColumnModel().getColumn(2).setPreferredWidth(150); // Email
        table.getColumnModel().getColumn(9).setPreferredWidth(80); // Lokasi
        table.getColumnModel().getColumn(10).setPreferredWidth(200); // Alamat
        table.getColumnModel().getColumn(11).setPreferredWidth(150); // Keterangan
        table.getColumnModel().getColumn(12).setPreferredWidth(120); // Tanggal

        ordersPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(ordersPanel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void showAllKaryawan() {
        contentPanel.removeAll();
        JPanel karyawanPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("View Karyawan", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        karyawanPanel.add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = { "ID", "Nama", "Peran", "Jam Kerja", "Gaji" };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        controller.showAllKaryawan(tableModel);

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        karyawanPanel.add(scrollPane, BorderLayout.CENTER);

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
            controller.showAllKaryawan(tableModel);
        });

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) table.getValueAt(selectedRow, 0);
                String nama = JOptionPane.showInputDialog("Edit Nama:", table.getValueAt(selectedRow, 1));
                String peran = JOptionPane.showInputDialog("Edit Peran:", table.getValueAt(selectedRow, 2));
                String jamKerja = JOptionPane.showInputDialog("Edit Jam Kerja:", table.getValueAt(selectedRow, 3));
                double gaji = Double
                        .parseDouble(JOptionPane.showInputDialog("Edit Gaji:", table.getValueAt(selectedRow, 4)));
                controller.editKaryawan(id, nama, peran, jamKerja, gaji);
                controller.showAllKaryawan(tableModel);
            } else {
                JOptionPane.showMessageDialog(null, "Pilih baris untuk diedit.");
            }
        });

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) table.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(null, "Yakin ingin menghapus karyawan?", "Konfirmasi",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    controller.deleteKaryawan(id);
                    controller.showAllKaryawan(tableModel);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Pilih baris untuk dihapus.");
            }
        });

        actionPanel.add(addButton);
        actionPanel.add(editButton);
        actionPanel.add(deleteButton);
        contentPanel.add(actionPanel, BorderLayout.SOUTH);

    }

    private void showSalesReport() {
        contentPanel.removeAll();

        JPanel reportPanel = new JPanel();
        reportPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Laporan Penjualan", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        reportPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton makananButton = new JButton("Makanan");
        JButton minumanButton = new JButton("Minuman");
        JButton semuaButton = new JButton("Semua");

        JTextField dateField = new JTextField(10);
        JButton filterButton = new JButton("Filter Tanggal");

        buttonPanel.add(new JLabel("Filter Tipe:"));
        buttonPanel.add(makananButton);
        buttonPanel.add(minumanButton);
        buttonPanel.add(semuaButton);
        buttonPanel.add(new JLabel("Tanggal (YYYY-MM-DD):"));
        buttonPanel.add(dateField);
        buttonPanel.add(filterButton);

        reportPanel.add(buttonPanel, BorderLayout.NORTH);

        JPanel tablePanel = new JPanel(new BorderLayout());
        String[] columnNames = { "Tanggal", "Nama Item", "Tipe Item", "Jumlah", "Harga Per Item", "Total Harga" };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable salesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(salesTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel totalPanel = new JPanel();
        JLabel totalLabel = new JLabel("Total Pendapatan: Rp 0");
        totalPanel.add(totalLabel);
        reportPanel.add(totalPanel, BorderLayout.SOUTH);

        reportPanel.add(tablePanel, BorderLayout.CENTER);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 32));

        contentPanel.add(reportPanel, BorderLayout.CENTER);

        semuaButton.addActionListener(e -> controller.updateSalesTable(tableModel, "Semua", null, totalLabel));
        makananButton.addActionListener(e -> controller.updateSalesTable(tableModel, "Makanan", null, totalLabel));
        minumanButton.addActionListener(e -> controller.updateSalesTable(tableModel, "Minuman", null, totalLabel));
        filterButton.addActionListener(e -> {
            String selectedDate = dateField.getText();
            controller.updateSalesTable(tableModel, "Semua", selectedDate, totalLabel);
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showMembership() {
        contentPanel.removeAll();

        JPanel membershipPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("MEMBERSHIP MANAGEMENT", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        membershipPanel.add(titleLabel, BorderLayout.NORTH);

        String[] membershipColumns = { "ID Membership", "Duration (Months)", "Description", "Price" };

        DefaultTableModel membershipModel = new DefaultTableModel(membershipColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable membershipTable = new JTable(membershipModel);
        JScrollPane scrollPane = new JScrollPane(membershipTable);
        membershipPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            try {
                String durationStr = JOptionPane.showInputDialog("Enter Duration (months):");
                String description = JOptionPane.showInputDialog("Enter Description:");
                String priceStr = JOptionPane.showInputDialog("Enter Price:");

                if (durationStr != null && description != null && priceStr != null) {
                    int duration = Integer.parseInt(durationStr);
                    int price = Integer.parseInt(priceStr);

                    try (Connection conn = DBConnection.getInstance().getConnection();
                            PreparedStatement stmt = conn.prepareStatement(
                                    "INSERT INTO membership (duration, deskripsi, harga) VALUES (?, ?, ?)")) {

                        stmt.setInt(1, duration);
                        stmt.setString(2, description);
                        stmt.setInt(3, price);
                        stmt.executeUpdate();

                        refreshMembershipTable(membershipModel);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter valid numbers for duration and price");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error adding membership: " + ex.getMessage());
            }
        });

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> {
            int selectedRow = membershipTable.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    int id = (int) membershipTable.getValueAt(selectedRow, 0);
                    String durationStr = JOptionPane.showInputDialog("Enter new Duration (months):",
                            membershipTable.getValueAt(selectedRow, 1));
                    String description = JOptionPane.showInputDialog("Enter new Description:",
                            membershipTable.getValueAt(selectedRow, 2));
                    String priceStr = JOptionPane.showInputDialog("Enter new Price:",
                            membershipTable.getValueAt(selectedRow, 3));

                    if (durationStr != null && description != null && priceStr != null) {
                        int duration = Integer.parseInt(durationStr);
                        int price = Integer.parseInt(priceStr);

                        try (Connection conn = DBConnection.getInstance().getConnection();
                                PreparedStatement stmt = conn.prepareStatement(
                                        "UPDATE membership SET duration=?, deskripsi=?, harga=? WHERE id_membership=?")) {

                            stmt.setInt(1, duration);
                            stmt.setString(2, description);
                            stmt.setInt(3, price);
                            stmt.setInt(4, id);
                            stmt.executeUpdate();

                            refreshMembershipTable(membershipModel);
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter valid numbers for duration and price");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error updating membership: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a membership to edit");
            }
        });

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            int selectedRow = membershipTable.getSelectedRow();
            if (selectedRow != -1) {
                int confirm = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete this membership?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        int id = (int) membershipTable.getValueAt(selectedRow, 0);
                        try (Connection conn = DBConnection.getInstance().getConnection();
                                PreparedStatement stmt = conn.prepareStatement(
                                        "DELETE FROM membership WHERE id_membership=?")) {

                            stmt.setInt(1, id);
                            stmt.executeUpdate();

                            refreshMembershipTable(membershipModel);
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Error deleting membership: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a membership to delete");
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        membershipPanel.add(buttonPanel, BorderLayout.SOUTH);

        contentPanel.add(membershipPanel);

        refreshMembershipTable(membershipModel);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void refreshMembershipTable(DefaultTableModel model) {
        model.setRowCount(0);

        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM membership");
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id_membership"),
                        rs.getInt("duration"),
                        rs.getString("deskripsi"),
                        rs.getInt("harga")
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error refreshing membership table: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new AdminView();

    }

}