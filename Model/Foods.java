package Model;

public class Foods extends ListMenu {
    private FoodsType foodsType;

    public Foods(String namaMenu, int harga, int stok, FoodsType foodsType) {
        super(namaMenu, harga, stok);
        this.foodsType = foodsType;
    }

    public FoodsType getFoodsType() {
        return foodsType;
    }

    public void setFoodsType(FoodsType foodsType) {
        this.foodsType = foodsType;
    }

    @Override
    public void printMenu() {
        super.printMenu();
        System.out.println("Food Type: " + foodsType);
    }

    @Override
    public String toString() {
        return namaMenu + " (" + foodsType + ")";
    }
}

// INSERT INTO foods (name, price) VALUES
// ('Red Velvet', 25000.0),
// ('Tiramisu', 30000.0),
// ('Black Forest', 28000.0),
// ('Glazed', 15000.0),
// ('Strawberry', 18000.0),
// ('Choco Mint', 20000.0),
// ('Honey', 18000.0),
// ('Taro', 22000.0),
// ('Plain', 12000.0),
// ('Smoke Beef', 20000.0);