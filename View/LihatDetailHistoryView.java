package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import Connection.DBConnection;

public class LihatDetailHistoryView {

    public LihatDetailHistoryView(String transactionId) {
        showDetailHistory(transactionId);
    }

    public void showDetailHistory(String transactionId) {
        JFrame detailFrame = new JFrame("Detail History Pengiriman");
        detailFrame.setLayout(null);
        detailFrame.setSize(600, 500);
        detailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - detailFrame.getWidth()) / 2;
        int y = (screenSize.height - detailFrame.getHeight()) / 2;
        detailFrame.setLocation(x, y);

        JLabel titleLabel = new JLabel("Detail History Pengiriman");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setBounds(180, 30, 300, 30);
        detailFrame.add(titleLabel);

        JButton backButton = new JButton("Back");
        backButton.setBounds(20, 20, 100, 30);
        backButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        backButton.setBackground(new Color(88, 24, 69));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> {
            detailFrame.dispose();
            new MelihatHistoryView();
        });
        detailFrame.add(backButton);

        JTable historyTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(historyTable);
        tableScrollPane.setBounds(20, 80, 550, 200);
        detailFrame.add(tableScrollPane);

        DBConnection dbConnection = new DBConnection();
        String query = "SELECT status, evidence, date, updated_by FROM DeliveryDetails WHERE transaction_id = ? ORDER BY date DESC";

        try (Connection con = dbConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, transactionId);
            ResultSet rs = stmt.executeQuery();

            DefaultTableModel model = new DefaultTableModel(new Object[]{"Status", "Evidence", "Date", "Updated By"}, 0);
            while (rs.next()) {
                String status = rs.getString("status");
                String evidence = rs.getString("evidence");
                Timestamp date = rs.getTimestamp("date");
                String updatedBy = rs.getString("updated_by");

                model.addRow(new Object[]{status, evidence, date != null ? date.toLocalDateTime() : null, updatedBy});
            }
            historyTable.setModel(model);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(detailFrame, "Error loading data: " + e.getMessage());
        }

        JLabel transactionIdLabel = new JLabel("Transaction ID: " + transactionId);
        transactionIdLabel.setBounds(20, 300, 250, 30);
        detailFrame.add(transactionIdLabel);
        
        detailFrame.setVisible(true);
    }
}
