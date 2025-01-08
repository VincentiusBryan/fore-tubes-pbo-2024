package View;

import javax.swing.*;
import java.awt.*;

public class UserMenuView {
    public UserMenuView() {
        showUserMenu();
    }

    private void showUserMenu() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        JFrame menuFrame = new JFrame("User Menu");
        menuFrame.setLayout(null);
        menuFrame.setSize(400, 300);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int x = (screenSize.width - menuFrame.getWidth()) / 2;
        int y = (screenSize.height - menuFrame.getHeight()) / 2;
        menuFrame.setLocation(x, y);

        JLabel title = new JLabel("User Menu");
        title.setBounds(140, 20, 200, 30);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        menuFrame.add(title);

        // Order Button
        JButton orderButton = new JButton("Order");
        orderButton.setBounds(100, 80, 200, 40);
        menuFrame.add(orderButton);

        // Buy Membership Button
        JButton membershipButton = new JButton("Buy Membership");
        membershipButton.setBounds(100, 140, 200, 40);
        menuFrame.add(membershipButton);

        // Logout Button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(100, 200, 200, 40);
        menuFrame.add(logoutButton);

        // Action Listeners
        orderButton.addActionListener(e -> {
            menuFrame.dispose();
            new OrderView();
        });

        membershipButton.addActionListener(e -> {
            menuFrame.dispose();
            new MembershipView();
        });

        logoutButton.addActionListener(e -> {
            menuFrame.dispose();
            new LoginView();
        });

        menuFrame.setVisible(true);
    }
}