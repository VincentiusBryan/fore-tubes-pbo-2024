package Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class DBConnection {

    public Connection con;
    private String driver = "com.mysql.cj.jdbc.Driver";
    private String url = "jdbc:mysql://localhost:3306/db_uas_1123018";
    private String username = "root";
    private String password = "";

    public Connection logOn() {

        try {
            Class.forName(driver).getDeclaredConstructor().newInstance();
            con = DriverManager.getConnection(url, username, password);
        } catch (Exception ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getLocalizedMessage());
            JOptionPane.showMessageDialog(null, "Error occurred when logging in: " + ex);
        }
        return con;
    }

    public void logOff() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error occurred when logging off: " + ex);
        }
    }

    public Connection getConnection() {
        return logOn();
    }
}
