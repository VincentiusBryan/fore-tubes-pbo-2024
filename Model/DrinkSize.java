package Model;

public enum DrinkSize {
    M(20000),
    L(25000);

    private final int price;

    DrinkSize(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}
