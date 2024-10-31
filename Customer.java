import java.util.List;
public class Customer extends User  {
    private String phoneNumber;
    private List<Keranjang> keranjangKu;


    public Customer(String id_user, String nama, String password, String phoneNumber) {
        super(id_user, nama, password);
        this.phoneNumber = phoneNumber;
    }
    

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
