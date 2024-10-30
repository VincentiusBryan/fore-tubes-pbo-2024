import java.util.Arrays;
import java.util.Date;
public class Main{
    public static void main(String[] args) {
        Foods rotiKeju= new Foods("Roti Keju", 25000, 10, FoodsType.BREAD);
        rotiKeju.printMenu();
        Beverages kopisusuL = new Beverages("kopi susu", 15000, 10, DrinkSize.L);
        Beverages kopisusuM = new Beverages("kopi susu", 12000, 10, DrinkSize.M);
        
    }
}