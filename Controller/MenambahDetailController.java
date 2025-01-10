package Controller;

import Model.DeliveryDetails;
import View.MenambahDetailView;
import Connection.DBConnection;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class MenambahDetailController {

    private MenambahDetailView view;
    private DBConnection dbConnection;

    public MenambahDetailController(MenambahDetailView view) {
        this.view = view;
        this.dbConnection = new DBConnection();
    }

    public boolean isTransactionIdValid(String transactionId) {
        try (Connection con = dbConnection.getConnection()) {
            String query = "SELECT COUNT(*) FROM `Transaction` WHERE `transaction_id` = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, Integer.parseInt(transactionId));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void saveDeliveryDetail(String transactionId, String status, String currentPosition, String evidence, String updatedBy) {
        if (!isTransactionIdValid(transactionId)) {
            JOptionPane.showMessageDialog(view.getFrame(), "Transaction ID tidak valid atau tidak ada di database.");
            return;
        }

        try (Connection con = dbConnection.getConnection()) {
            String query = "INSERT INTO DeliveryDetails (transaction_id, status, current_position, evidence, date, updated_by) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, Integer.parseInt(transactionId));
            stmt.setString(2, status);
            stmt.setString(3, currentPosition);
            stmt.setString(4, evidence);
            stmt.setObject(5, LocalDateTime.now());
            stmt.setString(6, updatedBy);

            int rowsAffected = stmt.executeUpdate();

            SwingUtilities.invokeLater(() -> {
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(view.getFrame(), "Data berhasil disimpan!");
                } else {
                    JOptionPane.showMessageDialog(view.getFrame(), "Gagal menyimpan data.");
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();

            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(view.getFrame(), "Terjadi kesalahan saat menyimpan data: " + e.getMessage());
            });
        }
    }
}
