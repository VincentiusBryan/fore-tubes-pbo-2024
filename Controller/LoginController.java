package Controller;

import Connection.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    private DBConnection dbConnection;

    public LoginController() {
        dbConnection = new DBConnection();
    }

    public boolean loginUser(String phoneNumber, String password) {
        Connection connection = dbConnection.logOn();
        
        if (connection != null) {
            try {
                String query = "SELECT customer_id, password FROM customer WHERE phone = ? AND password = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, phoneNumber);
                statement.setString(2, password);
                
                ResultSet resultSet = statement.executeQuery();
                
                if (resultSet.next()) {
                    int userId = resultSet.getInt("customer_id");
                    SessionManager.setLoggedInUserId(userId); 
                    return true;
                }
            } catch (SQLException e) {
                System.out.println("Error : " + e.getMessage());
                e.printStackTrace();
            } finally {
                dbConnection.logOff();
            }
        }
        return false;
    }

    public boolean isAdmin(String phoneNumber) {
        Connection connection = dbConnection.logOn();
        if (connection != null) {
            try {
                String query = "SELECT role FROM customer WHERE phone = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, phoneNumber);
                
                ResultSet resultSet = statement.executeQuery();
                
                if (resultSet.next()) {
                    String role = resultSet.getString("role");
                    return "Admin".equals(role);
                }
            } catch (SQLException e) {
                System.out.println("Error : " + e.getMessage());
                e.printStackTrace();
            } finally {
                dbConnection.logOff();
            }
        }
        return false;
    }
}
