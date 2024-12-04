package Model;
public class Admin extends User {
    private String role;

    public Admin(String id_user, String nama, String password, String role) {
        super(id_user, nama, password);
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
