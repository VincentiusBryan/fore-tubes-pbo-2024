package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Connection.DBConnection;

public class SessionManager {
    public SessionManager() {
        dbConnection = DBConnection.getInstance();
    }

    private static int loggedInUserId = -1;
    private static boolean isLoggedIn = false;
    private DBConnection dbConnection;

    public static void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public static boolean isLoggedIn() {
        return isLoggedIn;
    }

    public static void setLoggedInUserId(int userId) {
        loggedInUserId = userId;
    }

    public static int getLoggedInUserId() {
        return loggedInUserId;
    }

    public static boolean isUserLoggedIn() {
        return loggedInUserId != -1;
    }

    public static void clearSession() {
        loggedInUserId = -1;
    }

    public static String getUserNameById(int userId) {
        Connection connection = DBConnection.getInstance().getConnection();

        if (connection != null) {
            try {
                String query = "SELECT email FROM users WHERE id_user = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, userId);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    return resultSet.getString("email");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
