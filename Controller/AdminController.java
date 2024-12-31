package Controller;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Connection.DBConnection;

public class AdminController {

    private DBConnection dbConnection;

    public AdminController() {
        dbConnection = new DBConnection();
    }

        //MENU 1 
    public void updateTable(DefaultTableModel tableModel, String userType) {
        
        tableModel.setRowCount(0);

        
        String query = "SELECT * FROM users WHERE user_type=?";
        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, userType);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String email = resultSet.getString("email");
                String phoneNumber = resultSet.getString("phone_number");
                String type = resultSet.getString("user_type");
                String createdAt = resultSet.getString("created_at");

                tableModel.addRow(new Object[]{id, email, phoneNumber, type, createdAt});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }







    //menu 2

}
