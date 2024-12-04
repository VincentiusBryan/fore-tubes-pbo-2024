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
                // Query untuk menambah user baru
                String query = "INSERT INTO users (email, phone_number, password, user_type, created_at) VALUES (?, ?, ?, ?, NOW())";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, email);
                statement.setString(2, phone);
                statement.setString(3, password);
                statement.setString(4, "User");

                // Eksekusi query
                int rowsInserted = statement.executeUpdate();
                
                // Jika berhasil memasukkan data
                if (rowsInserted > 0) {
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                dbConnection.closeConnection(connection);
            }
        }
        return false; // Jika registrasi gagal
    }
}
