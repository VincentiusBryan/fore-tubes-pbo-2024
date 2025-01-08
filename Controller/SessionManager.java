package Controller;

public class SessionManager {
    private static int loggedInUserId = -1; // Default value for no user

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
}