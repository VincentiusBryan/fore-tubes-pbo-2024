package View;

import Connection.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MembershipView {
    private JFrame membershipFrame;

    public MembershipView() {
        showMembershipPackages();
    }

    private void showMembershipPackages() {
        membershipFrame = new JFrame("Buy Membership");
        membershipFrame.setLayout(null);
        membershipFrame.setSize(600, 500);
        membershipFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Center the frame
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - membershipFrame.getWidth()) / 2;
        int y = (screenSize.height - membershipFrame.getHeight()) / 2;
        membershipFrame.setLocation(x, y);

        JLabel title = new JLabel("Select Membership Package");
        title.setBounds(150, 20, 300, 30);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        membershipFrame.add(title);

        // Get packages from database
        loadMembershipPackages();

        // Back button
        JButton backButton = new JButton("Back to Menu");
        backButton.setBounds(225, 420, 150, 30);
        backButton.addActionListener(e -> {
            membershipFrame.dispose();
            new UserMenuView();
        });
        membershipFrame.add(backButton);

        membershipFrame.setVisible(true);
    }

    private void loadMembershipPackages() {
        Connection conn = DBConnection.getInstance().getConnection();
        if (conn != null) {
            try {
                String query = "SELECT * FROM membership ORDER BY duration";
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                int yPosition = 80;
                while (rs.next()) {
                    createPackagePanel(
                        rs.getInt("id_membership"),
                        rs.getString("deskripsi"),
                        rs.getDouble("harga"),
                        rs.getInt("duration"),
                        yPosition
                    );
                    yPosition += 110;
                }

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(membershipFrame,
                    "Error loading membership packages: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void createPackagePanel(int membershipId, String description, double price, int duration, int yPosition) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(50, yPosition, 500, 100);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Package duration
        JLabel durationLabel = new JLabel(duration + " Month" + (duration > 1 ? "s" : ""));
        durationLabel.setBounds(20, 10, 200, 25);
        durationLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(durationLabel);

        // Price
        java.text.NumberFormat currencyFormat = java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("id", "ID"));
        JLabel priceLabel = new JLabel(currencyFormat.format(price));
        priceLabel.setBounds(20, 35, 200, 25);
        panel.add(priceLabel);

        // Description
        JLabel descLabel = new JLabel("<html><body style='width: 250px'>" + description + "</body></html>");
        descLabel.setBounds(20, 60, 300, 25);
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        panel.add(descLabel);

        // Buy button
        JButton buyButton = new JButton("Purchase");
        buyButton.setBounds(350, 35, 100, 30);
        buyButton.addActionListener(e -> handlePurchase(membershipId, duration + " Month Package", price, duration));
        panel.add(buyButton);

        membershipFrame.add(panel);
    }

    private void handlePurchase(int membershipId, String packageName, double price, int duration) {
        int confirm = JOptionPane.showConfirmDialog(
            membershipFrame,
            String.format("Confirm purchase of %s package for %s?", 
                packageName, 
                java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("id", "ID")).format(price)),
            "Confirm Purchase",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            membershipFrame.dispose();
            new PaymentMemberView(membershipId, packageName, price, duration);
        }
    }
}