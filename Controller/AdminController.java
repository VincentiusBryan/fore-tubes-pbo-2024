package Controller;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;

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
                int id = resultSet.getInt("id_user");
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

    // MENU 2
    public void deleteItemFromDatabase(String table, String name) {
        String deleteQuery = "DELETE FROM " + table + " WHERE name = ?";
        try (Connection conn = dbConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateItemInDatabase(String table, String oldName, String newName, double newPrice) {
        String updateQuery = "UPDATE " + table + " SET name = ?, price = ? WHERE name = ?";
        try (Connection conn = dbConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setString(1, newName);
            stmt.setDouble(2, newPrice);
            stmt.setString(3, oldName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateItemInDatabase(String table, String oldName, String newName, String newSize, double newPrice) {
        String updateQuery = "UPDATE " + table + " SET name = ?, size = ?, price = ? WHERE name = ?";
        try (Connection conn = dbConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setString(1, newName);
            stmt.setString(2, newSize);
            stmt.setDouble(3, newPrice);
            stmt.setString(4, oldName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addItemToDatabase(String table, String name, double price) {
        String insertQuery = "INSERT INTO " + table + " (name, price) VALUES (?, ?)";
        try (Connection conn = dbConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addItemToDatabase(String table, String name, String size, double price) {
        String insertQuery = "INSERT INTO " + table + " (name, size, price) VALUES (?, ?, ?)";
        try (Connection conn = dbConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            stmt.setString(1, name);
            stmt.setString(2, size);
            stmt.setDouble(3, price);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getBeverages() throws SQLException {
        String query = "SELECT * FROM beverages";
        Connection conn = dbConnection.connect();
        PreparedStatement stmt = conn.prepareStatement(query);
        return stmt.executeQuery();
    }

    public ResultSet getFoods() throws SQLException {
        String query = "SELECT * FROM foods";
        Connection conn = dbConnection.connect();
        PreparedStatement stmt = conn.prepareStatement(query);
        return stmt.executeQuery();
    }

    //MENU 3
    public void promo() {
        // Implementasi promo di sini
    }

    // MENU 5 - View Orders
    public void viewOrders(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);  // Membersihkan table

        String query = "SELECT * FROM transaksi";
        try (Connection connection = dbConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id_transaksi");
                String namaItem = resultSet.getString("nama_item");
                String ukuran = resultSet.getString("ukuran");
                int jumlah = resultSet.getInt("jumlah");
                double hargaPerItem = resultSet.getDouble("harga_per_item");
                double totalHarga = resultSet.getDouble("total_harga");
                Timestamp waktuTransaksi = resultSet.getTimestamp("waktu_transaksi");
                boolean status = resultSet.getBoolean("status");

                String formattedTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(waktuTransaksi);

                tableModel.addRow(new Object[]{id, namaItem, ukuran, jumlah, hargaPerItem, totalHarga, formattedTime, status});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateOrderStatus(int idTransaksi, boolean status) {
        String query = "UPDATE transaksi SET status = ? WHERE id_transaksi = ?";
        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setBoolean(1, status);
            statement.setInt(2, idTransaksi);
            statement.executeUpdate();

            System.out.println("Status pesanan berhasil diperbarui.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //MENU 6
    public void updateStatusToko(int status) {
        String query = "UPDATE statusToko SET status = ?, last_updated = CURRENT_TIMESTAMP WHERE id = 1";
        try (Connection connection = dbConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, status); // Mengatur status (1 untuk buka, 0 untuk tutup)
            statement.executeUpdate();

            System.out.println("Status toko berhasil diubah.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
