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

    public boolean registerUser(String phone, String name, String address, String password) {
        Connection connection = dbConnection.logOn();
        
        if (connection != null) {
            try {
                String query = "INSERT INTO Customer (phone, name, address, password) VALUES (?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, phone);
                statement.setString(2, name);
                statement.setString(3, address);
                statement.setString(4, password);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("User registered!");
                    return true;
                } else {
                    System.out.println("Gagal insert data ke database.");
                }
            } catch (SQLException e) {
                System.out.println("Error di regis.");
                e.printStackTrace();
            } finally {
                dbConnection.logOff();
            }
        } else {
            System.out.println("Database ga konek.");
        }
        return false;
    }
}
