package Model;

public class Beverages extends ListMenu {
    private DrinkSize size;

    public Beverages(String namaMenu, int stok, DrinkSize size) {
        super(namaMenu, 0, stok);
        this.size = size;
        this.harga = size.getPrice();
    }

    public DrinkSize getSize() {
        return size;
    }

    public void setSize(DrinkSize size) {
        this.size = size;
        this.harga = size.getPrice();
    }

    @Override
    public void printMenu() {
        super.printMenu();
        System.out.println("Drink Size: " + size);
    }

    @Override
    public String toString() {
        return namaMenu + " (" + size + ")";
    }
}
