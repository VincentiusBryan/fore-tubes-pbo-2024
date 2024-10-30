

public abstract class ListMenu{
    protected String namaMenu;
    protected int harga;
    protected int stok;

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


    public int getStok() {
        return stok;
    }


    public void setStok(int stok) {
        this.stok = stok;
    }


    public ListMenu(String namaMenu, int harga, int stok) {
        this.namaMenu = namaMenu;
        this.harga = harga;
        this.stok = stok;
    }


    public void printMenu(){
        System.out.println("Nama Menu : "+namaMenu);
        System.out.println("Harga : "+ harga);
        System.out.println("Stok : "+stok);
    }


}