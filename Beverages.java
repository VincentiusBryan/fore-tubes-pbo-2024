public class Beverages extends ListMenu{
    private DrinkSize size;

    public Beverages(String namaMenu, int harga, int stok, DrinkSize size) {
        super(namaMenu, harga, stok);
        this.size = size;
    }

    public DrinkSize getSize() {
        return size;
    }

    public void setSize(DrinkSize size) {
        this.size = size;
    }

    @Override
    public void printMenu() {
        super.printMenu();
        System.out.println("Drink Size : "+size);
    }

}