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




    public boolean loginUser(String email, String password) {
        Connection connection = dbConnection.connect();

        if (connection != null) {
            try {
                String query = "SELECT * FROM users WHERE email = ? AND password = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, email);
                statement.setString(2, password);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    return true; // login sukses
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                dbConnection.closeConnection(connection);
            }
        }
        return false; 
    }


    
    public boolean isAdmin(String email) {
    
            Connection connection = dbConnection.connect();
            boolean isAdmin = false;
    
            String query = "SELECT user_type FROM users WHERE email = ?";
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                ResultSet resultSet = preparedStatement.executeQuery();
                
                if (resultSet.next()) {
                    String userType = resultSet.getString("user_type");
                    isAdmin = "Admin".equalsIgnoreCase(userType);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
    
            return isAdmin;
        }
}

