package View;

import Controller.SessionManager;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class MembershipStatusView {
    private JFrame frame;
    private Connection conn;

    public MembershipStatusView() {
        if (!SessionManager.isUserLoggedIn()) {
            JOptionPane.showMessageDialog(null, 
                "No user logged in",
                "Session Error", 
                JOptionPane.ERROR_MESSAGE);
            new LoginView();
            return;
        }
        initializeConnection();
        initializeComponents();
        loadMembershipData();
    }

    private void initializeConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/tubesForeDemo";
            String username = "root";
            String password = "";
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected successfully");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, 
                "Database driver not found: " + e.getMessage(),
                "Connection Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Database connection error: " + e.getMessage(),
                "Connection Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void loadMembershipData() {
        try {
            int currentUserId = SessionManager.getLoggedInUserId();
            System.out.println("Current User ID from SessionManager: " + currentUserId); // Debug print
            
            // First, check if user has a membership
            String checkQuery = """
                SELECT id_membership 
                FROM users 
                WHERE id_user = ?
            """;
            
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, currentUserId);
                ResultSet checkRs = checkStmt.executeQuery();
                
                if (checkRs.next()) {
                    Integer membershipId = checkRs.getObject("id_membership", Integer.class);
                    System.out.println("Found membership ID: " + membershipId); // Debug print
                    
                    if (membershipId == null) {
                        System.out.println("User has no membership (null id_membership)");
                        showNoMembershipMessage();
                        return;
                    }
                } else {
                    System.out.println("User not found in database");
                    showNoMembershipMessage();
                    return;
                }
            }
            
            // If we get here, user has a membership, so load the details
            String query = """
                SELECT u.start_date, u.end_date, u.status_aktif_membership, 
                       m.duration, m.deskripsi, m.harga
                FROM users u
                JOIN membership m ON u.id_membership = m.id_membership
                WHERE u.id_user = ?
            """;

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, currentUserId);
                System.out.println("Executing query: " + stmt.toString()); // Debug print
                
                ResultSet rs = stmt.executeQuery();
                System.out.println("Query executed successfully"); // Debug print

                if (rs.next()) {
                    System.out.println("Found membership data"); // Debug print
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
                    
                    // Debug prints for all values
                    Date startDate = rs.getDate("start_date");
                    Date endDate = rs.getDate("end_date");
                    boolean isActive = rs.getBoolean("status_aktif_membership");
                    int duration = rs.getInt("duration");
                    String description = rs.getString("deskripsi");
                    
                    System.out.println("Start Date: " + startDate);
                    System.out.println("End Date: " + endDate);
                    System.out.println("Status: " + isActive);
                    System.out.println("Duration: " + duration);
                    System.out.println("Description: " + description);
                    
                    if (startDate != null && endDate != null) {
                        updateMembershipDisplay(
                            dateFormat.format(startDate),
                            dateFormat.format(endDate),
                            isActive,
                            duration,
                            description
                        );
                    } else {
                        System.out.println("Start date or end date is null");
                        showNoMembershipMessage();
                    }
                } else {
                    System.out.println("No membership data found in join query"); // Debug print
                    showNoMembershipMessage();
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage()); // Debug print
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, 
                "Error loading membership data: " + e.getMessage(),
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateMembershipDisplay(String startDate, String endDate, 
                                       boolean isActive, int duration, 
                                       String description) {
        JPanel contentPanel = (JPanel) ((JPanel) frame.getContentPane()
                             .getComponent(0)).getComponent(2);
        contentPanel.removeAll();

        // Add membership details
        contentPanel.add(new JLabel("Status:"));
        contentPanel.add(new JLabel(isActive ? "Active" : "Inactive", 
                        SwingConstants.RIGHT));
        
        contentPanel.add(new JLabel("Membership Type:"));
        contentPanel.add(new JLabel(duration + " months", SwingConstants.RIGHT));
        
        contentPanel.add(new JLabel("Description:"));
        contentPanel.add(new JLabel(description, SwingConstants.RIGHT));
        
        contentPanel.add(new JLabel("Start Date:"));
        contentPanel.add(new JLabel(startDate, SwingConstants.RIGHT));
        
        contentPanel.add(new JLabel("End Date:"));
        contentPanel.add(new JLabel(endDate, SwingConstants.RIGHT));

        frame.revalidate();
        frame.repaint();
    }

    private void showNoMembershipMessage() {
        JPanel contentPanel = (JPanel) ((JPanel) frame.getContentPane()
                             .getComponent(0)).getComponent(2);
        contentPanel.removeAll();
        
        JLabel noMembershipLabel = new JLabel("No active membership found");
        noMembershipLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        noMembershipLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(noMembershipLabel);

        frame.revalidate();
        frame.repaint();
    }

    // Clean up resources when the window is closed
    private void cleanup() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeComponents() {
        // ... (same as before)
        frame = new JFrame("Membership Status");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // Center the frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((screenSize.width - frame.getWidth()) / 2, 
                         (screenSize.height - frame.getHeight()) / 2);

        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Your Membership Status");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Content panel for membership details
        JPanel contentPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        contentPanel.setBorder(BorderFactory.createEtchedBorder());
        mainPanel.add(contentPanel);

        // Back button
        JButton backButton = new JButton("Back to Menu");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> {
            cleanup();
            frame.dispose();
            new UserMenuView();
        });

        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(backButton);

        frame.add(mainPanel);
        frame.setVisible(true);
    }
}