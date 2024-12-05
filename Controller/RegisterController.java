package Controller;

import Connection.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterController {

    private DBConnection dbConnection;

    public RegisterController() {
        dbConnection = new DBConnection();
    }

    public boolean registerUser(String email, String password, String phone) {
        Connection connection = dbConnection.connect();
        
        if (connection != null) {
            try {
                String query = "INSERT INTO users (email, phone_number, password, user_type, created_at) VALUES (?, ?, ?, ?, NOW())";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, email);
                statement.setString(2, phone);
                statement.setString(3, password);
                statement.setString(4, "User");

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("User registered successfully.");
                    return true; // Registrasi berhasil
                } else {
                    System.out.println("Failed to insert data into database.");
                }
            } catch (SQLException e) {
                System.out.println("Error occurred during registration.");
                e.printStackTrace();
            } finally {
                dbConnection.closeConnection(connection);
            }
        } else {
            System.out.println("Database connection failed.");
        }
        return false; // Jika gagal
    }
}
