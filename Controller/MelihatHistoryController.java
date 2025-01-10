package Controller;

import View.MelihatHistoryView;
import View.LihatDetailHistoryView;
import Connection.DBConnection;
import javax.swing.*;
import java.sql.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MelihatHistoryController {

    private MelihatHistoryView view;
    private DBConnection dbConnection;

    public MelihatHistoryController(MelihatHistoryView view) {
        this.view = view;
        this.dbConnection = new DBConnection();
    }

    public void loadHistoryData() {
        String query = "SELECT t.transaction_id, t.delivery_type, t.total_cost, t.created_at, " +
                       "MAX(d.date) AS updated_at " +
                       "FROM `Transaction` t " +
                       "JOIN `DeliveryDetails` d ON t.transaction_id = d.transaction_id " +
                       "GROUP BY t.transaction_id " +
                       "ORDER BY t.created_at DESC";

        try (Connection con = dbConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            Object[][] data = new Object[50][6];
            int i = 0;
            while (rs.next()) {
                String transactionId = rs.getString("transaction_id");
                String deliveryType = rs.getString("delivery_type");
                int totalCost = rs.getInt("total_cost");
                Timestamp createdAt = rs.getTimestamp("created_at");
                Timestamp updatedAt = rs.getTimestamp("updated_at");

                data[i][0] = transactionId;
                data[i][1] = deliveryType;
                data[i][2] = totalCost;
                data[i][3] = createdAt != null ? createdAt.toLocalDateTime() : null;
                data[i][4] = updatedAt != null ? updatedAt.toLocalDateTime() : null;
                data[i][5] = "Lihat Detail";

                i++;
            }

            String[] columns = {"Transaction ID", "Delivery Type", "Total Cost", "Created At", "Updated At", "Action"};
            view.setTableData(data, columns);

            view.getHistoryTable().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent evt) {
                    int row = view.getHistoryTable().rowAtPoint(evt.getPoint());
                    if (row >= 0) {
                        String transactionId = (String) view.getHistoryTable().getValueAt(row, 0);
                        new LihatDetailHistoryView(transactionId); // Open the detail view
                    }
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view.getFrame(), "Error loading data: " + e.getMessage());
        }
    }
}
