package View;

import javax.swing.*;

import Controller.SessionManager;

import java.awt.*;

public class UserMenuView {
    public UserMenuView() {
        showUserMenu();
    }

    private void showUserMenu() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        // OH YES SINGLETON
        int userId = SessionManager.getLoggedInUserId();
        String userName = SessionManager.getUserNameById(userId);


        JFrame menuFrame = new JFrame("User Menu");

        menuFrame.setLayout(null);
        menuFrame.setSize(400, 450);  
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int x = (screenSize.width - menuFrame.getWidth()) / 2;
        int y = (screenSize.height - menuFrame.getHeight()) / 2;
        menuFrame.setLocation(x, y);


        //SINGLETON
        JLabel title = new JLabel("Welcome to User Menu "+ userName);
        title.setBounds(20, 20, 1000, 30);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        menuFrame.add(title);

        JButton orderButton = new JButton("Order");
        orderButton.setBounds(100, 80, 200, 40);
        menuFrame.add(orderButton);

        JButton membershipButton = new JButton("Buy Membership");
        membershipButton.setBounds(100, 140, 200, 40);
        menuFrame.add(membershipButton);

        JButton viewMembershipButton = new JButton("View Membership Status");
        viewMembershipButton.setBounds(100, 200, 200, 40);
        menuFrame.add(viewMembershipButton);

        JButton pointShopButton = new JButton("Point Shop");
        pointShopButton.setBounds(100, 260, 200, 40);
        menuFrame.add(pointShopButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(100, 320, 200, 40);
        menuFrame.add(logoutButton);

        orderButton.addActionListener(e -> {
            menuFrame.dispose();
            new OrderView();
        });

        membershipButton.addActionListener(e -> {
            menuFrame.dispose();
            new MembershipView();
        });

        viewMembershipButton.addActionListener(e -> {
            menuFrame.dispose();
            new MembershipStatusView();
        });

        logoutButton.addActionListener(e -> {
            menuFrame.dispose();
            new LoginView();
        });

        pointShopButton.addActionListener(e -> {
            menuFrame.dispose();
            new PointShopView();
        });

        menuFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new UserMenuView();
    }
}