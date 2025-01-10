package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuLain {

    public MenuLain() {
        showOtherMenu();
    }

    public void showOtherMenu() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        JFrame otherMenu = new JFrame("Pratama Group");
        otherMenu.setLayout(null);
        otherMenu.setSize(500, 400);
        otherMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int x = (screenSize.width - otherMenu.getWidth()) / 2;
        int y = (screenSize.height - otherMenu.getHeight()) / 2;
        otherMenu.setLocation(x, y);

        JLabel label = new JLabel("Menu Lain");
        label.setBounds(220, 70, 300, 30);
        otherMenu.add(label);

        JLabel title2 = new JLabel("Pratama Group");
        title2.setBounds(120, 30, 300, 50);
        title2.setFont(new Font("SansSerif", Font.BOLD, 36));
        otherMenu.add(title2);

        JButton addTransactionButton = new JButton("Menambahkan Transaksi Pengiriman");
        addTransactionButton.setBounds(150, 150, 200, 40);
        addTransactionButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        addTransactionButton.setBackground(new Color(42, 87, 146));
        addTransactionButton.setForeground(Color.WHITE);
        otherMenu.add(addTransactionButton);

        JButton viewHistoryButton = new JButton("Melihat History Pengiriman");
        viewHistoryButton.setBounds(150, 210, 200, 40);
        viewHistoryButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        viewHistoryButton.setBackground(new Color(163, 124, 31));
        viewHistoryButton.setForeground(Color.WHITE);
        otherMenu.add(viewHistoryButton);

        JButton addDetailTransactionButton = new JButton("Menambahkan Detail Transaksi");
        addDetailTransactionButton.setBounds(150, 270, 200, 40);
        addDetailTransactionButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        addDetailTransactionButton.setBackground(new Color(0, 153, 76));
        addDetailTransactionButton.setForeground(Color.WHITE);
        otherMenu.add(addDetailTransactionButton);

        addTransactionButton.addActionListener(e -> {
            otherMenu.dispose();
            new MenambahTransaksi();
        });

        viewHistoryButton.addActionListener(e -> {
            otherMenu.dispose();
            new MelihatHistoryView();
        });

        addDetailTransactionButton.addActionListener(e -> {
            otherMenu.dispose();
            new MenambahDetailView();
        });

        otherMenu.setVisible(true);
    }
}
