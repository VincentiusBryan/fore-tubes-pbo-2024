package Model;
public class Keranjang {
    private String namaMenu;
    private int quantity;
    public Keranjang(String namaMenu, int quantity) {
        this.namaMenu = namaMenu;
        this.quantity = quantity;
    }
    public String getNamaMenu() {
        return namaMenu;
    }
    public void setNamaMenu(String namaMenu) {
        this.namaMenu = namaMenu;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
}
