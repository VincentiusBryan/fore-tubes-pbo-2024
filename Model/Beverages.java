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

// INSERT INTO beverages (name, size, price) VALUES
// ('Iced Americano', 'Medium', 25000.0),
// ('Iced Americano', 'Large', 30000.0),
// ('Espresso', 'Medium', 20000.0),
// ('Espresso', 'Large', 25000.0),
// ('Cappuccino', 'Medium', 30000.0),
// ('Cappuccino', 'Large', 35000.0),
// ('Iced Matcha Latte', 'Medium', 35000.0),
// ('Iced Matcha Latte', 'Large', 40000.0),
// ('Hot Matcha Latte', 'Medium', 30000.0),
// ('Hot Matcha Latte', 'Large', 35000.0),
// ('Iced Chocolate', 'Medium', 40000.0),
// ('Iced Chocolate', 'Large', 45000.0),
// ('Oolong', 'Medium', 20000.0),
// ('Oolong', 'Large', 25000.0),
// ('Green Peach', 'Medium', 25000.0),
// ('Green Peach', 'Large', 30000.0),
// ('Earl Grey', 'Medium', 22000.0),
// ('Earl Grey', 'Large', 27000.0);