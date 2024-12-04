import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import Model.Admin;
import Model.Beverages;
import Model.Customer;
import Model.DrinkSize;
import Model.Foods;
import Model.FoodsType;
import Model.ListMenu;

import java.util.Date;
import java.util.List;

    
    public class Order {
    static int userCounter = 1;
    static Scanner scan = new Scanner(System.in);


    static List<Customer> customers = new ArrayList<>();

    // DUMMY ADMIN
    static Admin admin1 = new Admin("admin01", "Asep", "123", "Admin");

    //DUMMY MENU
    static Beverages kopisusuL = new Beverages("kopi susu", 15000, 10, DrinkSize.L);
    static Beverages kopisusuM = new Beverages("kopi susu", 12000, 10, DrinkSize.M);
    static Foods rotiKeju = new Foods("Roti Keju", 25000, 10, FoodsType.BREAD);

    static ListMenu[] listMenu = {kopisusuL, kopisusuM, rotiKeju};


    public static void main(String[] args) {

        signUp();
        
        // rotiKeju.printMenu();
        // kopisusuL.printMenu();

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
        System.out.println("=== Sign in ===");
        System.out.println("nama : ");
        String nama = scan.nextLine();
        System.out.println("password : ");
        String password = scan.nextLine();
        
        if(customers.size() > 0 && nama.equals(customers.get(0).getNama())&&password.equals(customers.get(0).getPassword())){
            System.out.println("Sign in berhasil , silahkan Order \n\n\n\n\n");
            order(customers.get(0));
        }else{
            System.out.println("Sign in gagal, silahkan ulangi");
            signIn();
        }
    }

    public static void order (Customer customers){
        int x=1;
        System.out.println(" = = = = = ORDER = = = = =");
        for (ListMenu menuItem : listMenu) {
            String sizeInfo = menuItem instanceof Beverages ? "( "+((Beverages)menuItem).getSize() + " )" : "";
            System.out.println(x + ". " + menuItem.getNamaMenu() + sizeInfo + " - Rp : " + menuItem.getHarga());
            x++;
        }

        System.out.print("Pilih nomor menu yang ingin ditambahkan ke keranjang: ");
        int pilihan = scan.nextInt();
        System.out.print("Masukkan jumlah: ");
        int quantity = scan.nextInt();
        scan.nextLine();

        int index= pilihan-1;               // static ListMenu[] listMenu = {kopisusuL, kopisusuM, rotiKeju}; ini dimulai dari index 0, makanya pilihan-1

        if(index>=0 && index<listMenu.length){
            ListMenu menu= listMenu[index];
            customers.addToKeranjang(menu.getNamaMenu(), quantity);
            System.out.println(menu.getNamaMenu() + " berhasil ditambahkan"+ "qty : "+quantity);
        }
            
            while (true) {
                System.out.println("\nPilih opsi:");
                System.out.println("1. Lihat Keranjang");
                System.out.println("2. Lanjut Beli");
                System.out.print("Pilihan: ");
                
                int choice = scan.nextInt();
                scan.nextLine();
        
                switch (choice) {
                    case 1:
                        cekKeranjang(customers);
                        break;
                    case 2:
                        order(customers);
                        return; 
                    default:
                        System.out.println("Pilihan invalid, coba lagi");
                        break;
                }
            }
            
    }
    public static void cekKeranjang(Customer customers){
        customers.printKeranjang(listMenu);
    }
}

