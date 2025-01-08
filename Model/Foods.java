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