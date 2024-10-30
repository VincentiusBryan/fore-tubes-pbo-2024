public class Beverages extends ListMenu {
    private SizeDrink size;

    public Beverages(String namaMenu, int harga, boolean stok, SizeDrink size) {
        super(namaMenu, harga, stok);
        this.size = size;
    }

    public SizeDrink getSize() {
        return size;
    }

    public void setSize(SizeDrink size) {
        this.size = size;
    }


    public void printMenu(){
        
    }

}
