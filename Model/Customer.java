package Model;
import java.util.ArrayList;
import java.util.List;
public class Customer extends User  {
    private String phoneNumber;
    private List<Keranjang> keranjangKu;
    

    

    public Customer(String id_user, String nama, String password, String phoneNumber) {
        super(id_user, nama, password);
        this.phoneNumber = phoneNumber;
        this.keranjangKu =new ArrayList<>();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void addToKeranjang(String namaMenu, int quantity){
        Keranjang itemKeranjangku = new Keranjang(namaMenu,quantity);
        keranjangKu.add(itemKeranjangku);
    }

    public void printKeranjang(ListMenu[] list){
        System.out.println("\n\nKeranjang kamu:");
        int x = 1;
        int totalHarga = 0; 
        for (Keranjang item : keranjangKu) {
            String namaMenu = item.getNamaMenu();
            int quantity = item.getQuantity();
            int hargaMenu = 0;
            
            for (ListMenu menuItem : list) {
                if (menuItem.getNamaMenu().equals(namaMenu)) {
                    hargaMenu = menuItem.getHarga();
                    break;
                }
            }
            int subtotal = hargaMenu * quantity;
            totalHarga += subtotal; 

            System.out.println(x + ". " + namaMenu+ " Qty: " + quantity + " - Total Harga: Rp " + subtotal);
            x++;
        }

        System.out.println("Sub total :  Rp " + totalHarga); 
    }
    }

    
