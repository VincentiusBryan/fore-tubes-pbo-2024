package View;

import Connection.DBConnection;
import Controller.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class PaymentView {
    private JFrame paymentFrame;
    private double totalPrice;
    private String selectedPromoName = "";
    private int selectedDiscount = 0;
    private int selectedPromoId = 0;

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
        DBConnection dbConnection = DBConnection.getInstance();
        Connection connection = dbConnection.getConnection();

        try {
            String membershipQuery = """
                        SELECT m.duration
                        FROM users u
                        JOIN membership m ON u.id_membership = m.id_membership
                        WHERE u.id_user = ? AND u.status_aktif_membership = 1
                    """;

            PreparedStatement membershipStmt = connection.prepareStatement(membershipQuery);
            membershipStmt.setInt(1, SessionManager.getLoggedInUserId());
            ResultSet membershipRs = membershipStmt.executeQuery();

            int membershipDuration = 0;
            if (membershipRs.next()) {
                membershipDuration = membershipRs.getInt("duration");
            }

            String promoQuery = """
                        SELECT id_promo, promo_name, description, discount_percentage,
                               CASE
                                   WHEN discount_percentage IN (10, 15, 20) THEN 'Membership'
                                   ELSE 'Regular'
                               END as promo_type
                        FROM promos
                        WHERE is_active = 1
                        ORDER BY
                            CASE
                                WHEN discount_percentage IN (10, 15, 20) THEN 0
                                ELSE 1
                            END,
                            discount_percentage DESC
                    """;

            Statement promoStmt = connection.createStatement();
            ResultSet promoRs = promoStmt.executeQuery(promoQuery);

            JLabel availablePromosLabel = new JLabel("Available Promos:", SwingConstants.CENTER);
            availablePromosLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            promoPanel.add(availablePromosLabel);

            boolean hasPromos = false;
            String currentPromoType = "";

            while (promoRs.next()) {
                hasPromos = true;
                int id = promoRs.getInt("id_promo");
                String promoName = promoRs.getString("promo_name");
                String description = promoRs.getString("description");
                int discountPercentage = promoRs.getInt("discount_percentage");
                String promoType = promoRs.getString("promo_type");

                if (!currentPromoType.equals(promoType)) {
                    currentPromoType = promoType;
                    JLabel typeLabel = new JLabel(promoType + " Promos:", SwingConstants.CENTER);
                    typeLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
                    promoPanel.add(typeLabel);
                }

                boolean isEligible = true;
                if (promoType.equals("Membership")) {
                    isEligible = (membershipDuration > 0) && ((membershipDuration == 1 && discountPercentage == 10) ||
                            (membershipDuration == 6 && discountPercentage == 15) ||
                            (membershipDuration == 12 && discountPercentage == 20));
                }

                JButton promoButton = new JButton(promoName + " - " + discountPercentage + "%");
                promoButton.setToolTipText(description);

                if (!isEligible) {
                    promoButton.setEnabled(false);
                    promoButton.setToolTipText(description + " (Requires appropriate membership level)");
                }

                promoButton.addActionListener(e -> {
                    selectedPromoId = id;
                    selectedPromoName = promoName;
                    selectedDiscount = discountPercentage;
                    JOptionPane.showMessageDialog(paymentFrame,
                            "Selected Promo: " + promoName + "\nDiscount: " + discountPercentage + "%");
                });

                promoPanel.add(promoButton);
            }

            if (!hasPromos) {
                JLabel noPromosLabel = new JLabel("No promos currently available");
                noPromosLabel.setHorizontalAlignment(SwingConstants.CENTER);
                promoPanel.add(noPromosLabel);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Failed to load promotions: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void handlePayment(String method) {
        double discountAmount = (totalPrice * selectedDiscount) / 100;
        double finalPrice = totalPrice - discountAmount;

        java.text.NumberFormat currencyFormat = java.text.NumberFormat
                .getCurrencyInstance(new java.util.Locale("id", "ID"));

        String formattedTotalPrice = currencyFormat.format(totalPrice);
        String formattedFinalPrice = currencyFormat.format(finalPrice);

        JOptionPane.showMessageDialog(paymentFrame,
                "Total Price: " + formattedTotalPrice +
                        "\nPromo: " + selectedPromoName + " - Discount: " + selectedDiscount + "%" +
                        "\nFinal Price: " + formattedFinalPrice,
                "Payment Details",
                JOptionPane.INFORMATION_MESSAGE);

        if (method.equals("GoPay") || method.equals("Ovo")) {
            String phoneNumber = JOptionPane.showInputDialog(paymentFrame,
                    "Enter your phone number for " + method + " payment:", "Phone Number",
                    JOptionPane.QUESTION_MESSAGE);

            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                JOptionPane.showMessageDialog(paymentFrame,
                        "You have successfully paid with " + method + ". Payment completed!", "Payment Success",
                        JOptionPane.INFORMATION_MESSAGE);
                updateTransactionWithPromo(selectedPromoId);
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
                    updateTransactionWithPromo(selectedPromoId);
                    continueAfterPayment(true);
                    qrFrame.dispose();
                }
            });
        }
    }

    private void updateTransactionWithPromo(int promoId) {
        DBConnection dbConnection = DBConnection.getInstance();
        Connection connection = dbConnection.getConnection();

        if (connection != null) {
            try {
                double originalPrice = totalPrice;

                String promoQuery = "SELECT discount_percentage FROM promos WHERE id_promo = ?";
                PreparedStatement promoPs = connection.prepareStatement(promoQuery);
                promoPs.setInt(1, promoId);
                ResultSet promoRs = promoPs.executeQuery();

                if (promoRs.next()) {
                    int discountPercentage = promoRs.getInt("discount_percentage");

                    double discountedPrice = originalPrice * (100 - discountPercentage) / 100.0;

                    String updateDetailQuery = "UPDATE transaksi_detail " +
                            "SET id_promo = ?, " +
                            "harga_sebelum_promo = ?, " +
                            "harga_setelah_promo = ? " +
                            "WHERE id_transaksi = (SELECT MAX(id_transaksi) FROM transaksi)";
                    PreparedStatement detailPs = connection.prepareStatement(updateDetailQuery);
                    detailPs.setInt(1, promoId);
                    detailPs.setDouble(2, originalPrice);
                    detailPs.setDouble(3, discountedPrice);
                    detailPs.executeUpdate();
                    detailPs.close();

                    String updateTransactionQuery = "UPDATE transaksi " +
                            "SET total_harga = ? " +
                            "WHERE id_transaksi = (SELECT MAX(id_transaksi) FROM transaksi)";
                    PreparedStatement transactionPs = connection.prepareStatement(updateTransactionQuery);
                    transactionPs.setDouble(1, discountedPrice);
                    transactionPs.executeUpdate();
                    transactionPs.close();
                }

                promoRs.close();
                promoPs.close();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Failed to update transaction with promo: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addPointsToUser() {
        DBConnection dbConnection = DBConnection.getInstance();
        Connection connection = dbConnection.getConnection();
        if (connection != null) {
            try {
                String updatePointsQuery = "UPDATE users SET points = points + 2 WHERE id_user = ?";
                PreparedStatement ps = connection.prepareStatement(updatePointsQuery);
                ps.setInt(1, SessionManager.getLoggedInUserId());
                ps.executeUpdate();
                ps.close();

                String getPointsQuery = "SELECT points FROM users WHERE id_user = ?";
                ps = connection.prepareStatement(getPointsQuery);
                ps.setInt(1, SessionManager.getLoggedInUserId());
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    int currentPoints = rs.getInt("points");
                    JOptionPane.showMessageDialog(paymentFrame,
                            "You earned 2 points for this transaction!\n" +
                                    "Your current points: " + currentPoints,
                            "Points Earned",
                            JOptionPane.INFORMATION_MESSAGE);
                }

                ps.close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Failed to add points: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void continueAfterPayment(boolean paymentSuccess) {
        if (paymentSuccess) {
            addPointsToUser();

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