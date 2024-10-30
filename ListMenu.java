import java.util.logging.Handler;

public abstract class ListMenu{
    protected String namaMenu;
    protected int harga;
    protected boolean stok;

    public ListMenu(String namaMenu, int harga, boolean stok) {
        this.namaMenu = namaMenu;
        this.harga = harga;
        this.stok = stok;
    }

    public String getNamaMenu() {
        return namaMenu;
    }
    public void setNamaMenu(String namaMenu) {
        this.namaMenu = namaMenu;
    }
    public int getHarga() {
        return harga;
    }
    public void setHarga(int harga) {
        this.harga = harga;
    }
    public boolean isStok() {
        return stok;
    }
    public void setStok(boolean stok) {
        this.stok = stok;
    }

    public void printMenu(){
        System.out.println("Nama Menu : "+namaMenu);
        System.out.println("Harga : "+ harga);
        System.out.println("Stok : "+stok);
    }


}