package View;

import Connection.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import Controller.SessionManager;
import Controller.MembershipController;

public class PaymentMemberView {
    private JFrame paymentFrame;
    private double totalPrice;
    private int selectedDuration;
    private int selectedMembershipId;
    private MembershipController membershipController;

    public PaymentMemberView(int membershipId, String packageName, double price, int duration) {
        this.totalPrice = price;
        this.selectedDuration = duration;
        this.selectedMembershipId = membershipId;
        this.membershipController = new MembershipController();
        showPaymentOptions(packageName);
    }

    private void showPaymentOptions(String packageName) {
        paymentFrame = new JFrame("Membership Payment");
        paymentFrame.setSize(500, 400);
        paymentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - paymentFrame.getWidth()) / 2;
        int y = (screenSize.height - paymentFrame.getHeight()) / 2;
        paymentFrame.setLocation(x, y);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));

        // Title Panel
        JLabel titleLabel = new JLabel("Membership Payment", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Details Panel
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridLayout(3, 1, 5, 5));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel packageLabel = new JLabel("Package: " + packageName);
        JLabel durationLabel = new JLabel("Duration: " + selectedDuration + " month(s)");
        
        java.text.NumberFormat currencyFormat = java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("id", "ID"));
        String formattedPrice = currencyFormat.format(totalPrice);
        JLabel priceLabel = new JLabel("Price: " + formattedPrice);

        packageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        durationLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        detailsPanel.add(packageLabel);
        detailsPanel.add(durationLabel);
        detailsPanel.add(priceLabel);

        mainPanel.add(detailsPanel, BorderLayout.CENTER);

        // Payment Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton qrisButton = new JButton("Pay with QRIS");
        JButton gopayButton = new JButton("Pay with GoPay");
        JButton ovoButton = new JButton("Pay with OVO");

        qrisButton.addActionListener(e -> handlePayment("QRIS"));
        gopayButton.addActionListener(e -> handlePayment("GoPay"));
        ovoButton.addActionListener(e -> handlePayment("Ovo"));

        buttonPanel.add(qrisButton);
        buttonPanel.add(gopayButton);
        buttonPanel.add(ovoButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        paymentFrame.add(mainPanel);
        paymentFrame.setVisible(true);
    }

    private void handlePayment(String method) {
        if (method.equals("GoPay") || method.equals("Ovo")) {
            String phoneNumber = JOptionPane.showInputDialog(paymentFrame,
                    "Enter your phone number for " + method + " payment:",
                    "Phone Number",
                    JOptionPane.QUESTION_MESSAGE);

            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                processPayment(method);
            } else {
                JOptionPane.showMessageDialog(paymentFrame,
                        "Phone number is required to complete the payment.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // QRIS Payment
            JPanel imagePanel = new JPanel();
            JLabel qrImageLabel = new JLabel();

            ImageIcon qrImageIcon = new ImageIcon("View/QRIS_Fore.png");
            qrImageLabel.setIcon(qrImageIcon);
            imagePanel.add(qrImageLabel);

            JFrame qrFrame = new JFrame("QRIS Payment");
            qrFrame.setSize(400, 430);
            qrFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            centerFrame(qrFrame);

            qrFrame.add(imagePanel);
            qrFrame.setVisible(true);

            qrFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent we) {
                    processPayment("QRIS");
                    qrFrame.dispose();
                }
            });
        }
    }

    private void processPayment(String paymentMethod) {
        int userId = SessionManager.getLoggedInUserId();
        boolean success = membershipController.createMembershipTransaction(
            userId, 
            selectedMembershipId, 
            totalPrice, 
            paymentMethod
        );

        if (success) {
            JOptionPane.showMessageDialog(paymentFrame,
                "Membership payment successful! Your membership is now active.",
                "Payment Success",
                JOptionPane.INFORMATION_MESSAGE);
            paymentFrame.dispose();
            new UserMenuView();
        } else {
            JOptionPane.showMessageDialog(paymentFrame,
                "Payment failed. Please try again.",
                "Payment Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void centerFrame(JFrame frame) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - frame.getWidth()) / 2;
        int y = (screenSize.height - frame.getHeight()) / 2;
        frame.setLocation(x, y);
    }
}