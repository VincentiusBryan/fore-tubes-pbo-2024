package View;

import javax.swing.*;
import java.awt.*;

public class PaymentView {
    private JFrame paymentFrame;

    public PaymentView(String orderSummary) {
        showPaymentOptions(orderSummary);
    }

    private void showPaymentOptions(String orderSummary) {
        paymentFrame = new JFrame("Payment Options");
        paymentFrame.setSize(400, 400);
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

    private void handlePayment(String method) {
        // Use Boolean (object) to allow modification within the windowClosing method
        final Boolean[] paymentSuccess = {false};  // Wrap boolean in an array to be effectively final
        
        if (method.equals("GoPay")) {
            // show tab input no telp
            String phoneNumber = JOptionPane.showInputDialog(paymentFrame, "Enter your phone number:", "Phone Number", JOptionPane.QUESTION_MESSAGE);
            
            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                JOptionPane.showMessageDialog(paymentFrame, "You have successfully paid with GoPay. Payment completed!", "Payment Success", JOptionPane.INFORMATION_MESSAGE);
                paymentSuccess[0] = true;
            } else {
                JOptionPane.showMessageDialog(paymentFrame, "Phone number is required to complete the payment.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (method.equals("OVO")) {
            // show tab input no telp
            String phoneNumber = JOptionPane.showInputDialog(paymentFrame, "Enter your phone number for OVO payment:", "Phone Number", JOptionPane.QUESTION_MESSAGE);
            
            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                JOptionPane.showMessageDialog(paymentFrame, "You have successfully paid with OVO. Payment completed!", "Payment Success", JOptionPane.INFORMATION_MESSAGE);
                paymentSuccess[0] = true;
            } else {
                JOptionPane.showMessageDialog(paymentFrame, "Phone number is required to complete the payment.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (method.equals("QRIS")) {
            // Show image for QRIS
            JPanel imagePanel = new JPanel();
            JLabel qrImageLabel = new JLabel();
            
            // Load the image
            ImageIcon qrImageIcon = new ImageIcon("C:\\Users\\Asus Pc\\Documents\\1123052_Jordan Emmanuelle\\SEMESTER 3\\PrakPBO\\Tubes\\fore-tubes-pbo-2024\\View\\QRIS_Fore.png");
            qrImageLabel.setIcon(qrImageIcon);
            
            // Add the image label to the panel
            imagePanel.add(qrImageLabel);
            
            // Create a new frame or dialog to show the image
            JFrame qrFrame = new JFrame("QRIS Payment");
            qrFrame.setSize(400, 430);
            qrFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            
            // Position the frame at the center of the screen
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension screenSize = toolkit.getScreenSize();
            int x = (screenSize.width - qrFrame.getWidth()) / 2;
            int y = (screenSize.height - qrFrame.getHeight()) / 2;
            qrFrame.setLocation(x, y);
            
            qrFrame.add(imagePanel);
            qrFrame.setVisible(true);
    
            // Wait for the QRIS frame to close before proceeding to next step
            qrFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent we) {
                    paymentSuccess[0] = true; // Payment is considered successful after QRIS image is closed
                    // Call the method to proceed with the next steps
                    continueAfterPayment(paymentSuccess[0]);
                    qrFrame.dispose(); // Close the QRIS frame

                }
            });
        } else {
            JOptionPane.showMessageDialog(paymentFrame, "You selected " + method + " payment.", "Payment Success", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(paymentFrame, "nanti idenya munculin QR buat discan, isinya website ada messagenya 'berhasil bayar'. SOON!", "UPDATE", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    

    private void continueAfterPayment(boolean paymentSuccess) {
        if (paymentSuccess) {
            int response = JOptionPane.showConfirmDialog(paymentFrame, "Do you want to create another order?", "Order Again", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                paymentFrame.dispose();
                new OrderView(); // Open the order view again
            } else if (response == JOptionPane.NO_OPTION) {
                int logoutResponse = JOptionPane.showConfirmDialog(paymentFrame, "Do you want to log out?", "Log Out", JOptionPane.YES_NO_OPTION);
                if (logoutResponse == JOptionPane.YES_OPTION) {
                    paymentFrame.dispose();
                    JOptionPane.showMessageDialog(null, "You have been logged out. Goodbye!", "Log Out Success", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0); // Logout
                }
            }
        }
    }
}