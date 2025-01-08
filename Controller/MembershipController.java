package Controller;

import Connection.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import javax.swing.JOptionPane;

public class MembershipController {
    private DBConnection dbConnection;

    public MembershipController() {
        dbConnection = DBConnection.getInstance();
    }

    public boolean createMembershipTransaction(int userId, int membershipId, double amount, String paymentMethod) {
        Connection connection = dbConnection.getConnection();
        if (connection != null) {
            try {
                // Start transaction
                connection.setAutoCommit(false);

                try {
                    // 1. Insert into transaksi_membership table
                    String insertTransaction = "INSERT INTO transaksi_membership (id_user, id_membership, tanggal_transaksi, metode_pembayaran, total_pembayaran) VALUES (?, ?, NOW(), ?, ?)";
                    PreparedStatement transactionStmt = connection.prepareStatement(insertTransaction);
                    transactionStmt.setInt(1, userId);
                    transactionStmt.setInt(2, membershipId);
                    transactionStmt.setString(3, paymentMethod);
                    transactionStmt.setDouble(4, amount);
                    transactionStmt.executeUpdate();

                    // 2. Get membership duration
                    String getDuration = "SELECT duration FROM membership WHERE id_membership = ?";
                    PreparedStatement durationStmt = connection.prepareStatement(getDuration);
                    durationStmt.setInt(1, membershipId);
                    ResultSet rs = durationStmt.executeQuery();
                    
                    int duration = 0;
                    if (rs.next()) {
                        duration = rs.getInt("duration");
                    }

                    // 3. Update user's membership status
                    LocalDate startDate = LocalDate.now();
                    LocalDate endDate = startDate.plusMonths(duration);
                    
                    String updateUser = "UPDATE users SET id_membership = ?, start_date = ?, end_date = ?, status_aktif_membership = 1 WHERE id_user = ?";
                    PreparedStatement userStmt = connection.prepareStatement(updateUser);
                    userStmt.setInt(1, membershipId);
                    userStmt.setDate(2, Date.valueOf(startDate));
                    userStmt.setDate(3, Date.valueOf(endDate));
                    userStmt.setInt(4, userId);
                    userStmt.executeUpdate();

                    connection.commit();
                    return true;

                } catch (SQLException e) {
                    connection.rollback();
                    throw e;
                } finally {
                    connection.setAutoCommit(true);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Error processing membership transaction: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        return false;
    }

    public ResultSet getMembershipPackages() {
        Connection connection = dbConnection.getConnection();
        if (connection != null) {
            try {
                String query = "SELECT * FROM membership ORDER BY duration";
                Statement stmt = connection.createStatement();
                return stmt.executeQuery(query);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Error fetching membership packages: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        return null;
    }

    public boolean checkActiveMembership(int userId) {
        Connection connection = dbConnection.getConnection();
        if (connection != null) {
            try {
                String query = "SELECT status_aktif_membership, end_date FROM users WHERE id_user = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    boolean isActive = rs.getBoolean("status_aktif_membership");
                    Date endDate = rs.getDate("end_date");
                    
                    if (isActive && endDate != null) {
                        LocalDate now = LocalDate.now();
                        return !now.isAfter(endDate.toLocalDate());
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}