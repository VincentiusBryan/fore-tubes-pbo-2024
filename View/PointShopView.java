package View;

import Connection.DBConnection;
import Controller.SessionManager;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PointShopView {
    private JFrame frame;
    private Connection conn;
    private int userPoints;
    private List<DrinkReward> availableRewards;

    // Inner class untuk menyimpan informasi reward
    private static class DrinkReward {
        int id;
        String name;
        int pointCost;
        
        DrinkReward(int id, String name, int pointCost) {
            this.id = id;
            this.name = name;
            this.pointCost = pointCost;
        }
        
        @Override
        public String toString() {
            return name + " (" + pointCost + " points)";
        }
    }

    public PointShopView() {
        initializeConnection();
        loadUserPoints();
        loadAvailableRewards();
        initializeComponents();
    }

    private void initializeConnection() {
        try {
            DBConnection dbConnection = DBConnection.getInstance();
            conn = dbConnection.getConnection();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Database connection error: " + e.getMessage(),
                "Connection Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void loadUserPoints() {
        try {
            String query = "SELECT points FROM users WHERE id_user = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, SessionManager.getLoggedInUserId());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                userPoints = rs.getInt("points");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, 
                "Error loading user points: " + e.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAvailableRewards() {
        availableRewards = new ArrayList<>();
        try {
            String query = "SELECT id_beverage, nama_minuman, point_cost FROM point_rewards";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                availableRewards.add(new DrinkReward(
                    rs.getInt("id_beverage"),
                    rs.getString("nama_minuman"),
                    rs.getInt("point_cost")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, 
                "Error loading rewards: " + e.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initializeComponents() {
        frame = new JFrame("Point Shop");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Center the frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(
            (screenSize.width - frame.getWidth()) / 2,
            (screenSize.height - frame.getHeight()) / 2
        );

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Points display
        JLabel pointsLabel = new JLabel("Your Points: " + userPoints);
        pointsLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        mainPanel.add(pointsLabel, BorderLayout.NORTH);

        // Rewards list
        JPanel rewardsPanel = new JPanel();
        rewardsPanel.setLayout(new GridLayout(0, 1, 10, 10));
        
        for (DrinkReward reward : availableRewards) {
            JButton rewardButton = new JButton(reward.toString());
            rewardButton.addActionListener(e -> handleRewardSelection(reward));
            rewardsPanel.add(rewardButton);
        }

        JScrollPane scrollPane = new JScrollPane(rewardsPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Back button
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> {
            frame.dispose();
            new UserMenuView();
        });
        mainPanel.add(backButton, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void handleRewardSelection(DrinkReward reward) {
        if (userPoints >= reward.pointCost) {
            int confirm = JOptionPane.showConfirmDialog(frame,
                "Do you want to redeem " + reward.name + " for " + reward.pointCost + " points?",
                "Confirm Redemption",
                JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                redeemReward(reward);
            }
        } else {
            JOptionPane.showMessageDialog(frame,
                "You don't have enough points. You need " + 
                (reward.pointCost - userPoints) + " more points.",
                "Insufficient Points",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void redeemReward(DrinkReward reward) {
        try {
            conn.setAutoCommit(false);
            
            // Update user points
            String updatePointsQuery = "UPDATE users SET points = points - ? WHERE id_user = ?";
            PreparedStatement updatePoints = conn.prepareStatement(updatePointsQuery);
            updatePoints.setInt(1, reward.pointCost);
            updatePoints.setInt(2, SessionManager.getLoggedInUserId());
            updatePoints.executeUpdate();

            // Record redemption
            String insertRedemptionQuery = 
                "INSERT INTO point_redemptions (id_user, id_beverage, points_used, redemption_date) " +
                "VALUES (?, ?, ?, NOW())";
            PreparedStatement insertRedemption = conn.prepareStatement(insertRedemptionQuery);
            insertRedemption.setInt(1, SessionManager.getLoggedInUserId());
            insertRedemption.setInt(2, reward.id);
            insertRedemption.setInt(3, reward.pointCost);
            insertRedemption.executeUpdate();

            conn.commit();
            
            // Update local points and refresh display
            userPoints -= reward.pointCost;
            JOptionPane.showMessageDialog(frame,
                "Successfully redeemed " + reward.name + "!\n" +
                "Your remaining points: " + userPoints,
                "Redemption Successful",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Refresh the frame
            frame.dispose();
            new PointShopView();
            
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame,
                "Error processing redemption: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}