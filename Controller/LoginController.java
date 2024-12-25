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
        return email.equalsIgnoreCase("admin@example.com"); // cek admin
    }
}

