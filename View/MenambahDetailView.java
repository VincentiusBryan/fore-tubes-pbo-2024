package View;

import javax.swing.*;

import Controller.MenambahDetailController;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenambahDetailView {

    private JFrame addDetailFrame;

    public MenambahDetailView() {
        showAddDetailForm();
    }

    public void showAddDetailForm() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        addDetailFrame = new JFrame("Menambah Detail Transaksi");
        addDetailFrame.setLayout(null);
        addDetailFrame.setSize(500, 400);
        addDetailFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int x = (screenSize.width - addDetailFrame.getWidth()) / 2;
        int y = (screenSize.height - addDetailFrame.getHeight()) / 2;
        addDetailFrame.setLocation(x, y);

        JLabel transactionIdLabel = new JLabel("Transaction ID:");
        transactionIdLabel.setBounds(30, 50, 150, 30);
        addDetailFrame.add(transactionIdLabel);
        JTextField transactionIdField = new JTextField();
        transactionIdField.setBounds(180, 50, 200, 30);
        addDetailFrame.add(transactionIdField);

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setBounds(30, 100, 150, 30);
        addDetailFrame.add(statusLabel);
        JComboBox<String> statusComboBox = new JComboBox<>(
                new String[] { "PENDING", "IN_PROGRESS", "ON_DELIVERY", "ARRIVED" });
        statusComboBox.setBounds(180, 100, 200, 30);
        addDetailFrame.add(statusComboBox);

        JLabel currentPositionLabel = new JLabel("Current Position:");
        currentPositionLabel.setBounds(30, 150, 150, 30);
        addDetailFrame.add(currentPositionLabel);
        JTextField currentPositionField = new JTextField();
        currentPositionField.setBounds(180, 150, 200, 30);
        addDetailFrame.add(currentPositionField);

        JLabel evidenceLabel = new JLabel("Evidence:");
        evidenceLabel.setBounds(30, 200, 150, 30);
        addDetailFrame.add(evidenceLabel);
        JTextArea evidenceArea = new JTextArea();
        evidenceArea.setBounds(180, 200, 200, 60);
        addDetailFrame.add(evidenceArea);

        JLabel updatedByLabel = new JLabel("Updated By:");
        updatedByLabel.setBounds(30, 280, 150, 30);
        addDetailFrame.add(updatedByLabel);
        JTextField updatedByField = new JTextField();
        updatedByField.setBounds(180, 280, 200, 30);
        addDetailFrame.add(updatedByField);

        JButton saveButton = new JButton("Simpan");
        saveButton.setBounds(100, 320, 150, 40);
        saveButton.setBackground(new Color(0, 153, 76));
        saveButton.setForeground(Color.WHITE);
        addDetailFrame.add(saveButton);

        JButton backButton = new JButton("Kembali");
        backButton.setBounds(270, 320, 150, 40);
        backButton.setBackground(new Color(163, 124, 31));
        backButton.setForeground(Color.WHITE);
        addDetailFrame.add(backButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String transactionId = transactionIdField.getText();
                String status = (String) statusComboBox.getSelectedItem();
                String currentPosition = currentPositionField.getText();
                String evidence = evidenceArea.getText();
                String updatedBy = updatedByField.getText();
        
                if (transactionId.isEmpty() || status.isEmpty() || currentPosition.isEmpty() ||
                        evidence.isEmpty() || updatedBy.isEmpty()) {
                    JOptionPane.showMessageDialog(addDetailFrame, "Semua field harus diisi!");
                } else {
                    MenambahDetailController controller = new MenambahDetailController(new MenambahDetailView());
                    controller.saveDeliveryDetail(transactionId, status, currentPosition, evidence, updatedBy);
                }
            }
        });
        

        backButton.addActionListener(e -> {
            addDetailFrame.dispose();
            new MenuLain();
        });

        addDetailFrame.setVisible(true);
    }

    public JFrame getFrame() {
        return addDetailFrame;
    }

    public static void main(String[] args) {
        new MenambahDetailView();
    }
}
