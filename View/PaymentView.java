package View;

import Connection.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class PaymentView {
    private JFrame paymentFrame;
    private double totalPrice; // Menyimpan total harga dari pesanan
    private String selectedPromoName = ""; // Nama promo yang dipilih
    private int selectedDiscount = 0; // Persentase diskonnya
    private int selectedPromoId = 0; // Tambahkan ini di bagian instance variables

    public PaymentView(String orderSummary, double totalPrice) {
        this.totalPrice = totalPrice;
        showPaymentOptions(orderSummary);
    }

    private void showPaymentOptions(String orderSummary) {
        paymentFrame = new JFrame("Payment Options");
        paymentFrame.setSize(600, 600);
        paymentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - paymentFrame.getWidth()) / 2;
        int y = (screenSize.height - paymentFrame.getHeight()) / 2;
        paymentFrame.setLocation(x, y);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Payment Options", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JTextArea orderDetailsArea = new JTextArea(orderSummary);
        orderDetailsArea.setEditable(false);
        JScrollPane orderScrollPane = new JScrollPane(orderDetailsArea);
        mainPanel.add(orderScrollPane, BorderLayout.CENTER);

        // Promo button
        JPanel promoPanel = new JPanel();
        promoPanel.setLayout(new GridLayout(0, 1, 10, 10));
        JLabel promoLabel = new JLabel("Promo", SwingConstants.CENTER);
        promoLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        promoPanel.add(promoLabel);

        loadPromos(promoPanel);

        JScrollPane promoScrollPane = new JScrollPane(promoPanel);
        mainPanel.add(promoScrollPane, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton cashButton = new JButton("Pay with QRIS");
        JButton cardButton = new JButton("Pay with GoPay");
        JButton eWalletButton = new JButton("Pay with Ovo");

        cashButton.addActionListener(e -> handlePayment("QRIS"));
        cardButton.addActionListener(e -> handlePayment("GoPay"));
        eWalletButton.addActionListener(e -> handlePayment("Ovo"));

        buttonPanel.add(cashButton);
        buttonPanel.add(cardButton);
        buttonPanel.add(eWalletButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        paymentFrame.add(mainPanel);
        paymentFrame.setVisible(true);
    }

    private void loadPromos(JPanel promoPanel) {
        // Koneksi ke database
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.connect();

        String query = "SELECT id_promo, promo_name, description, discount_percentage, start_date, end_date FROM promos WHERE is_active = 1";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id_promo");
                String promoName = resultSet.getString("promo_name");
                String description = resultSet.getString("description");
                int discountPercentage = resultSet.getInt("discount_percentage");
                String startDate = resultSet.getString("start_date");
                String endDate = resultSet.getString("end_date");

                // Di dalam method loadPromos, ubah bagian pembuatan promoButton:
                JButton promoButton = new JButton(promoName + " - " + discountPercentage + "%");
                promoButton.addActionListener(e -> {
                    selectedPromoId = id; // Tambahkan ini
                    selectedPromoName = promoName;
                    selectedDiscount = discountPercentage;
                    JOptionPane.showMessageDialog(paymentFrame,
                            "Selected Promo: " + promoName + "\nDiscount: " + discountPercentage + "%");
                });
                promoPanel.add(promoButton);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to load promotions: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            dbConnection.closeConnection(connection);
        }
    }

    private void handlePayment(String method) {
        // Menghitung diskon berdasarkan promo yang dipilih
        double discountAmount = (totalPrice * selectedDiscount) / 100;
        double finalPrice = totalPrice - discountAmount;

        java.text.NumberFormat currencyFormat = java.text.NumberFormat
                .getCurrencyInstance(new java.util.Locale("id", "ID"));

        String formattedTotalPrice = currencyFormat.format(totalPrice);
        String formattedFinalPrice = currencyFormat.format(finalPrice);

        // Menampilkan info harga
        JOptionPane.showMessageDialog(paymentFrame,
                "Total Price: " + formattedTotalPrice +
                        "\nPromo: " + selectedPromoName + " - Discount: " + selectedDiscount + "%" +
                        "\nFinal Price: " + formattedFinalPrice,
                "Payment Details",
                JOptionPane.INFORMATION_MESSAGE);

        // Proses pembayaran
        if (method.equals("GoPay") || method.equals("Ovo")) {
            String phoneNumber = JOptionPane.showInputDialog(paymentFrame,
                    "Enter your phone number for " + method + " payment:", "Phone Number",
                    JOptionPane.QUESTION_MESSAGE);

            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                JOptionPane.showMessageDialog(paymentFrame,
                        "You have successfully paid with " + method + ". Payment completed!", "Payment Success",
                        JOptionPane.INFORMATION_MESSAGE);
                updateTransactionWithPromo(selectedPromoId); // Update transaksi dengan promo
                continueAfterPayment(true);
            } else {
                JOptionPane.showMessageDialog(paymentFrame, "Phone number is required to complete the payment.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JPanel imagePanel = new JPanel();
            JLabel qrImageLabel = new JLabel();

            ImageIcon qrImageIcon = new ImageIcon(
                    "C:\\Users\\Asus Pc\\Documents\\1123052_Jordan Emmanuelle\\SEMESTER 3\\PrakPBO\\Tubes\\fore-tubes-pbo-2024\\View\\QRIS_Fore.png");
            qrImageLabel.setIcon(qrImageIcon);

            imagePanel.add(qrImageLabel);

            JFrame qrFrame = new JFrame("QRIS Payment");
            qrFrame.setSize(400, 430);
            qrFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension screenSize = toolkit.getScreenSize();
            int x = (screenSize.width - qrFrame.getWidth()) / 2;
            int y = (screenSize.height - qrFrame.getHeight()) / 2;
            qrFrame.setLocation(x, y);

            qrFrame.add(imagePanel);
            qrFrame.setVisible(true);

            qrFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent we) {
                    updateTransactionWithPromo(selectedPromoId); // Update transaksi dengan promo
                    continueAfterPayment(true);
                    qrFrame.dispose();
                }
            });
        }
    }

    private void updateTransactionWithPromo(int promoId) {
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.connect();

        if (connection != null) {
            try {
                // First, get the discount percentage from the promo
                String promoQuery = "SELECT discount_percentage FROM promos WHERE id_promo = ?";
                PreparedStatement promoPs = connection.prepareStatement(promoQuery);
                promoPs.setInt(1, promoId);
                ResultSet promoRs = promoPs.executeQuery();

                if (promoRs.next()) {
                    int discountPercentage = promoRs.getInt("discount_percentage");

                    // Calculate the discounted price
                    double discountMultiplier = (100 - discountPercentage) / 100.0;

                    // Update the transaction with both original and discounted prices
                    String updateQuery = "UPDATE transaksi " +
                            "SET id_promo = ?, " +
                            "harga_setelah_promo = harga_sebelum_promo * ? " +
                            "WHERE id_transaksi = (SELECT MAX(id_transaksi) FROM transaksi as t2)";

                    PreparedStatement ps = connection.prepareStatement(updateQuery);
                    ps.setInt(1, promoId);
                    ps.setDouble(2, discountMultiplier);
                    ps.executeUpdate();
                }

                promoRs.close();
                promoPs.close();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Failed to update transaction with promo: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                dbConnection.closeConnection(connection);
            }
        }
    }

    private void continueAfterPayment(boolean paymentSuccess) {
        if (paymentSuccess) {
            int response = JOptionPane.showConfirmDialog(paymentFrame, "Do you want to create another order?",
                    "Order Again", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                paymentFrame.dispose();
                new OrderView();
            } else if (response == JOptionPane.NO_OPTION) {
                int logoutResponse = JOptionPane.showConfirmDialog(paymentFrame, "Do you want to log out?", "Log Out",
                        JOptionPane.YES_NO_OPTION);
                if (logoutResponse == JOptionPane.YES_OPTION) {
                    paymentFrame.dispose();
                    JOptionPane.showMessageDialog(null, "You have been logged out. Goodbye!", "Log Out Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }
            }
        }
    }
}
