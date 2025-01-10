package View;

import Model.*;
import Connection.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDateTime;

public class MenambahTransaksi {
    private DBConnection dbConnection;
    private JComboBox<String> deliveryTypeComboBox;
    private JTextField nameField, addressField, phoneField, weightField;
    private JFrame frame;

    private static final String[] DELIVERY_TYPES = {
        "Building Materials",
        "House Moving",
        "Instant Delivery"
    };
    
    private static final int[] DELIVERY_FEES = {
        1000,  
        2000,
        3000
    };

    public MenambahTransaksi() {
        dbConnection = new DBConnection();
        initialize();
    }

    public void initialize() {
        frame = new JFrame("Menambah Transaksi Pengiriman");
        frame.setLayout(null);
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setupFramePosition();
        setupComponents();
        loadDeliveryTypes();
        
        frame.setVisible(true);
    }

    private void setupFramePosition() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - frame.getWidth()) / 2;
        int y = (screenSize.height - frame.getHeight()) / 2;
        frame.setLocation(x, y);
    }

    private void setupComponents() {
        JLabel title = new JLabel("Menambah Transaksi Pengiriman");
        title.setBounds(100, 30, 300, 30);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        frame.add(title);

        setupFormFields();

        setupButtons();
    }

    private void setupFormFields() {
        JLabel nameLabel = new JLabel("Nama Penerima:");
        nameLabel.setBounds(50, 70, 150, 30);
        frame.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(200, 70, 200, 30);
        frame.add(nameField);

        JLabel addressLabel = new JLabel("Alamat Penerima:");
        addressLabel.setBounds(50, 110, 150, 30);
        frame.add(addressLabel);

        addressField = new JTextField();
        addressField.setBounds(200, 110, 200, 30);
        frame.add(addressField);

        JLabel phoneLabel = new JLabel("No. Telepon Penerima:");
        phoneLabel.setBounds(50, 150, 150, 30);
        frame.add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setBounds(200, 150, 200, 30);
        frame.add(phoneField);

        JLabel weightLabel = new JLabel("Berat Pengiriman (kg):");
        weightLabel.setBounds(50, 190, 150, 30);
        frame.add(weightLabel);

        weightField = new JTextField();
        weightField.setBounds(200, 190, 200, 30);
        frame.add(weightField);

        JLabel deliveryTypeLabel = new JLabel("Tipe Pengiriman:");
        deliveryTypeLabel.setBounds(50, 230, 150, 30);
        frame.add(deliveryTypeLabel);

        deliveryTypeComboBox = new JComboBox<>(DELIVERY_TYPES);
        deliveryTypeComboBox.setBounds(200, 230, 200, 30);
        frame.add(deliveryTypeComboBox);
    }

    private void setupButtons() {
        JButton saveButton = new JButton("Simpan");
        saveButton.setBounds(100, 280, 150, 40);
        saveButton.setBackground(new Color(58, 129, 89));
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> saveTransaction());
        frame.add(saveButton);

        JButton backButton = new JButton("Kembali");
        backButton.setBounds(250, 280, 150, 40);
        backButton.setBackground(new Color(88, 24, 69));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> {
            frame.dispose();
            new MenuLain();
        });
        frame.add(backButton);
    }

    private void loadDeliveryTypes() {
        deliveryTypeComboBox.removeAllItems();
        for (String type : DELIVERY_TYPES) {
            deliveryTypeComboBox.addItem(type);
        }
    }

    private void saveTransaction() {
        try {
            validateInput();
            
            int weight = validateWeight();
            
            String deliveryType = (String) deliveryTypeComboBox.getSelectedItem();
            int totalCost = calculateCost(deliveryType, weight);
            
            saveTransactionToDatabase(deliveryType, weight, totalCost);
            
            clearForm();
            JOptionPane.showMessageDialog(frame, "Transaksi berhasil disimpan.", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            handleDatabaseError("Error saving transaction", e);
        }
    }

    private void validateInput() {
        if (nameField.getText().trim().isEmpty() || 
            addressField.getText().trim().isEmpty() || 
            phoneField.getText().trim().isEmpty() || 
            weightField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Semua field harus diisi.");
        }
    }

    private int validateWeight() {
        try {
            int weight = Integer.parseInt(weightField.getText().trim());
            if (weight <= 0) {
                throw new IllegalArgumentException("Berat pengiriman tidak boleh 0 atau negatif.");
            }
            return weight;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Berat pengiriman harus berupa angka bulat.");
        }
    }

    private int calculateCost(String deliveryType, int weight) {
        int index = -1;
        for (int i = 0; i < DELIVERY_TYPES.length; i++) {
            if (DELIVERY_TYPES[i].equals(deliveryType)) {
                index = i;
                break;
            }
        }
        
        if (index == -1) {
            throw new IllegalArgumentException("Tipe pengiriman tidak valid.");
        }
        
        return DELIVERY_FEES[index] * weight;
    }

    private void saveTransactionToDatabase(String deliveryType, int weight, int totalCost) throws SQLException {
        Connection con = null;
        try {
            con = dbConnection.getConnection();
            con.setAutoCommit(false);

            String query = "INSERT INTO Transaction (customer_id, delivery_type, expected_weight, " +
                          "total_cost, created_at, receipt_name, receipt_address, receipt_phone) " +
                          "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                          
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setInt(1, 1);
                stmt.setString(2, deliveryType);
                stmt.setInt(3, weight);
                stmt.setInt(4, totalCost);
                stmt.setObject(5, LocalDateTime.now());
                stmt.setString(6, nameField.getText().trim());
                stmt.setString(7, addressField.getText().trim());
                stmt.setString(8, phoneField.getText().trim());
                
                int rowTerpengaruh = stmt.executeUpdate();
                if (rowTerpengaruh == 0) {
                    throw new SQLException("Failed to insert transaction");
                }
            }

            con.commit();
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleDatabaseError(String message, SQLException e) {
        e.printStackTrace();
        String errorMessage = "Database error: " + e.getMessage();
        if (e.getSQLState() != null) {
            errorMessage += "\nSQL State: " + e.getSQLState();
        }
        JOptionPane.showMessageDialog(frame, message + "\n\n" + errorMessage,
            "Database Error", JOptionPane.ERROR_MESSAGE);
    }

    private void clearForm() {
        nameField.setText("");
        addressField.setText("");
        phoneField.setText("");
        weightField.setText("");
        deliveryTypeComboBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenambahTransaksi());
    }
}