import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Date;
import java.util.List;

public class Main {
    static int userCounter = 1;
    static Scanner scan = new Scanner(System.in);

    static List<Customer> customers = new ArrayList<>();

    // dummy
    static Admin admin1 = new Admin("admin01", "Asep", "123", "Admin");

    static Beverages kopisusuL = new Beverages("kopi susu", 15000, 10, DrinkSize.L);
    static Beverages kopisusuM = new Beverages("kopi susu", 12000, 10, DrinkSize.M);
    static Foods rotiKeju = new Foods("Roti Keju", 25000, 10, FoodsType.BREAD);

    public static void main(String[] args) {

        signUp();
        
        rotiKeju.printMenu();
        kopisusuL.printMenu();

    }

    public static void signUp() {
        System.out.println("=== Sign Up ===");

        
        
        System.out.print("Enter Name: ");
        String nama = scan.nextLine();
        System.out.print("Enter Password: ");
        String password = scan.nextLine();
        System.out.print("Enter Phone Number: ");
        String phoneNumber = scan.nextLine();
        
        String id_user = generateUserId();
        Customer newCustomer = new Customer(id_user, nama, password, phoneNumber);
        customers.add(newCustomer);
        
        System.out.println("Your User ID: " + id_user);
        System.out.println("Sign-up successful! Silahkan Sign in, " + nama + "!");

        signIn();


    }

    private static String generateUserId() {
        return String.format("User_%02d", userCounter++);
    }


    public static void signIn(){
        System.out.println("=== Sign in");
        String nama = scan.nextLine();
    }


}