package View;

import javax.swing.JFrame;

public class AdminView {
    public AdminView() {
        showAdminView();
    }

    public void showAdminView() {
        JFrame adminFrame = new JFrame("Admin Menu");
        adminFrame.setSize(600, 550);
        adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
