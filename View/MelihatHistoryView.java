package View;

import Controller.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MelihatHistoryView {

    private JFrame historyFrame;
    private JTable historyTable;
    private JButton backButton;

    public MelihatHistoryView() {
        showHistoryView();
    }

    public void showHistoryView() {
        historyFrame = new JFrame("Lihat History Pengiriman");
        historyFrame.setLayout(new BorderLayout());
        historyFrame.setSize(800, 400);
        historyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        historyFrame.setLocationRelativeTo(null);

        historyTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(historyTable);
        historyFrame.add(scrollPane, BorderLayout.CENTER);

        backButton = new JButton("Kembali");
        backButton.setBackground(new Color(163, 124, 31));
        backButton.setForeground(Color.WHITE);
        historyFrame.add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener(e -> {
            historyFrame.dispose();
            new MenuLain();
        });

        MelihatHistoryController controller = new MelihatHistoryController(this);
        controller.loadHistoryData(); 

        historyFrame.setVisible(true);
    }

    public void setTableData(Object[][] data, String[] columns) {
        DefaultTableModel model = new DefaultTableModel(data, columns);
        historyTable.setModel(model);
    }

    public JFrame getFrame() {
        return historyFrame;
    }

    public JTable getHistoryTable() {
        return historyTable;
    }
}
