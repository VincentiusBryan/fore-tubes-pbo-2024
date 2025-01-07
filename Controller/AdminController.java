package Controller;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.DriverManager;
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









    //PROMO 

    public void deletePromoFromDatabase(String promoName) {
        String deleteQuery = "DELETE FROM promos WHERE promo_name = ?";
        try (Connection conn = new DBConnection().connect();
             PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
    
            // Set parameters
            stmt.setString(1, promoName);
    
            // Execute delete
            int rowsDeleted = stmt.executeUpdate();
    
            // Commit changes if necessary
            conn.commit();
    
            // Log result
            if (rowsDeleted > 0) {
                System.out.println("Promo deleted successfully: " + promoName);
            } else {
                System.err.println("Failed to delete promo: " + promoName);
            }
        } catch (SQLException e) {
            System.err.println("Error deleting promo: " + e.getMessage());
            e.printStackTrace();
        }
    }



    
public void updatePromoInDatabase(String oldName, String newName, String newDescription, double newDiscount, String newStartDate, String newEndDate, boolean isActive) {
    String updateQuery = "UPDATE promos SET promo_name = ?, description = ?, discount_percentage = ?, start_date = ?, end_date = ?, is_active = ? WHERE promo_name = ?";
    try (Connection conn = new DBConnection().connect();
         PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

        // Nonaktifkan autoCommit
        conn.setAutoCommit(false);

        // Set parameters
        stmt.setString(1, newName);
        stmt.setString(2, newDescription);
        stmt.setDouble(3, newDiscount);
        stmt.setString(4, newStartDate);
        stmt.setString(5, newEndDate);
        stmt.setBoolean(6, isActive);
        stmt.setString(7, oldName);

        // Execute update
        int rowsUpdated = stmt.executeUpdate();

        // Commit changes
        if (rowsUpdated > 0) {
            conn.commit();
            System.out.println("Promo updated successfully: " + oldName + " -> " + newName);
        } else {
            System.err.println("Failed to update promo: " + oldName);
        }

        // Aktifkan kembali autoCommit
        conn.setAutoCommit(true);
    } catch (SQLException e) {
        System.err.println("Error updating promo: " + e.getMessage());
        e.printStackTrace();
    }
}



    // Add promo to database
    public void addPromoToDatabase(String promoName, String description, double discount, String startDate, String endDate, boolean isActive) {
        String insertQuery = "INSERT INTO promos (promo_name, description, discount_percentage, start_date, end_date, is_active) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = new DBConnection().connect();
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
    
            // Set parameters
            stmt.setString(1, promoName);
            stmt.setString(2, description);
            stmt.setDouble(3, discount);
            stmt.setString(4, startDate);
            stmt.setString(5, endDate);
            stmt.setBoolean(6, isActive);
    
            // Execute update
            int rowsInserted = stmt.executeUpdate();
    
            // Commit changes if necessary
            conn.commit();
    
            // Log result
            if (rowsInserted > 0) {
                System.out.println("Promo added successfully: " + promoName);
            } else {
                System.err.println("Failed to add promo: " + promoName);
            }
        } catch (SQLException e) {
            System.err.println("Error inserting promo: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    



















    // // MENU 5 - View Orders
    public void showAllOrders(DefaultTableModel tableModel) {
    tableModel.setRowCount(0);  // Membersihkan tabel

    String query = "SELECT t.id_transaksi, u.email, f.name AS food_name, b.name AS beverage_name, t.tipe_item, t.ukuran, t.jumlah, t.total_harga, t.waktu_transaksi, pt.promo_name "
                 + "FROM transaksi t "
                 + "JOIN users u ON t.id_user = u.id_user "
                 + "LEFT JOIN foods f ON t.nama_item = f.name "
                 + "LEFT JOIN beverages b ON t.nama_item = b.name "
                 + "LEFT JOIN promos pt ON t.id_promo = pt.id_promo";
    try (Connection connection = dbConnection.connect();
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int idTransaksi = resultSet.getInt("id_transaksi");
            String email = resultSet.getString("email");
            String foodName = resultSet.getString("food_name");
            String beverageName = resultSet.getString("beverage_name");
            String tipeItem = resultSet.getString("tipe_item");
            String ukuran = resultSet.getString("ukuran");
            int jumlah = resultSet.getInt("jumlah");
            double totalHarga = resultSet.getDouble("total_harga");
            Timestamp waktuTransaksi = resultSet.getTimestamp("waktu_transaksi");
            String promoName = resultSet.getString("promo_name");

            tableModel.addRow(new Object[]{idTransaksi, email, foodName, beverageName, tipeItem, ukuran, jumlah, totalHarga, waktuTransaksi, promoName});
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    public void updateOrderStatus(int idTransaksi, boolean status) {
        String query = "UPDATE transaksi SET selesai = ? WHERE id_transaksi = ?";
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



















// Menampilkan data karyawan
public void showAllKaryawan(DefaultTableModel tableModel) {
    String query = "SELECT id, nama, peran, jam_kerja, gaji FROM karyawan";
    try (Connection connection = dbConnection.connect();
         PreparedStatement statement = connection.prepareStatement(query);
         ResultSet resultSet = statement.executeQuery()) {

        // Membersihkan tableModel sebelum diisi ulang
        tableModel.setRowCount(0);

        // Iterasi hasil query
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String nama = resultSet.getString("nama");
            String peran = resultSet.getString("peran");
            String jamKerja = resultSet.getString("jam_kerja");
            double gaji = resultSet.getDouble("gaji");

            // Tambahkan data ke tableModel
            tableModel.addRow(new Object[]{id, nama, peran, jamKerja, gaji});
        }

        System.out.println("Data karyawan berhasil diambil.");
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Gagal mengambil data karyawan.");
    }
}

public void addKaryawan(String nama, String peran, String jamKerja, double gaji) {
    String query = "INSERT INTO karyawan (nama, peran, jam_kerja, gaji) VALUES (?, ?, ?, ?)";
    try (Connection connection = dbConnection.connect();
         PreparedStatement statement = connection.prepareStatement(query)) {

        statement.setString(1, nama);
        statement.setString(2, peran);
        statement.setString(3, jamKerja);
        statement.setDouble(4, gaji);

        statement.executeUpdate();
        System.out.println("Karyawan berhasil ditambahkan.");
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Gagal menambahkan karyawan.");
    }
}


public void editKaryawan(int id, String nama, String peran, String jamKerja, double gaji) {
    String query = "UPDATE karyawan SET nama = ?, peran = ?, jam_kerja = ?, gaji = ? WHERE id = ?";
    try (Connection connection = dbConnection.connect();
         PreparedStatement statement = connection.prepareStatement(query)) {

        statement.setString(1, nama);
        statement.setString(2, peran);
        statement.setString(3, jamKerja);
        statement.setDouble(4, gaji);
        statement.setInt(5, id);

        statement.executeUpdate();
        System.out.println("Karyawan berhasil diperbarui.");
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Gagal memperbarui karyawan.");
    }
}


public void deleteKaryawan(int id) {
    String query = "DELETE FROM karyawan WHERE id = ?";
    try (Connection connection = dbConnection.connect();
         PreparedStatement statement = connection.prepareStatement(query)) {

        statement.setInt(1, id);

        statement.executeUpdate();
        System.out.println("Karyawan berhasil dihapus.");
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Gagal menghapus karyawan.");
    }
}



}
