package Model;
public abstract class User {
    private String id_user;
    private String nama;
    private String password;
    
    public User(String id_user, String nama, String password) {
        this.id_user = id_user;
        this.nama = nama;
        this.password = password;
    }


    public String getId_user() {    
        return id_user;
    }
    public void setId_user(String id_user) {
        this.id_user = id_user;
    }
    public String getNama() {
        return nama;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    

}
