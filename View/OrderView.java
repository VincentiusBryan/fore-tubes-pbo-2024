package View;

import Connection.DBConnection;
import Controller.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class OrderView {

    private DefaultListModel<String> cartModel;
    private int beverageQuantity = 1;
    private int foodQuantity = 1;
    private double totalPrice = 0;

    private Map<String, Map<String, Double>> beveragePrices = new HashMap<>();
    private Map<String, Double> foodPrices = new HashMap<>();

    private DBConnection dbConnection;

    public OrderView() {

        if (!SessionManager.isUserLoggedIn()) {
            JOptionPane.showMessageDialog(null, "Please log in first!", "Access Denied", JOptionPane.ERROR_MESSAGE);
            return;
        }

        dbConnection = new DBConnection();
        cartModel = new DefaultListModel<>();
        initializePrices();
        showOrderMenu();
    }

    private void initializePrices() {
        Connection connection = dbConnection.connect();
        if (connection != null) {
            try {
                // Ngambil harga beverages dari database
                Statement statement = connection.createStatement();
                ResultSet rsBeverages = statement.executeQuery("SELECT * FROM beverages");
                while (rsBeverages.next()) {
                    String beverageName = rsBeverages.getString("name");
                    String size = rsBeverages.getString("size");
                    double price = rsBeverages.getDouble("price");

                    beveragePrices.putIfAbsent(beverageName, new HashMap<>());
                    beveragePrices.get(beverageName).put(size, price);
                }

                // Ngambil harga food dari database
                ResultSet rsFood = statement.executeQuery("SELECT * FROM foods");
                while (rsFood.next()) {
                    String foodName = rsFood.getString("name");
                    double price = rsFood.getDouble("price");

                    foodPrices.put(foodName, price);
                }

                rsBeverages.close();
                rsFood.close();
                statement.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dbConnection.closeConnection(connection);
            }
        }
    }

    public void showOrderMenu() {
        JFrame orderFrame = new JFrame("Order Menu");
        orderFrame.setSize(600, 600);
        orderFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - orderFrame.getWidth()) / 2;
        int y = (screenSize.height - orderFrame.getHeight()) / 2;
        orderFrame.setLocation(x, y);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);

        JLabel titleLabel = new JLabel("Menu");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        titleLabel.setBounds(260, 14, 200, 30);
        mainPanel.add(titleLabel);

        // Beverage Section
        JLabel typeOfBeverageLabel = new JLabel("Tipe Minuman:");
        typeOfBeverageLabel.setBounds(30, 70, 100, 25);
        mainPanel.add(typeOfBeverageLabel);

        String[] beverageTypes = { "None", "Coffee", "Non-Coffee", "Tea" };
        JComboBox<String> typeOfBeverageCombo = new JComboBox<>(beverageTypes);
        typeOfBeverageCombo.setBounds(130, 70, 150, 25);
        mainPanel.add(typeOfBeverageCombo);

        JLabel beverageLabel = new JLabel("Minuman:");
        beverageLabel.setBounds(30, 110, 100, 25);
        mainPanel.add(beverageLabel);

        JComboBox<String> beverageCombo = new JComboBox<>();
        beverageCombo.setBounds(130, 110, 150, 25);
        mainPanel.add(beverageCombo);

        JLabel sizeLabel = new JLabel("Ukuran:");
        sizeLabel.setBounds(30, 150, 100, 25);
        mainPanel.add(sizeLabel);

        String[] sizes = { "None", "Medium", "Large" };
        JComboBox<String> sizeCombo = new JComboBox<>(sizes);
        sizeCombo.setBounds(130, 150, 150, 25);
        mainPanel.add(sizeCombo);

        typeOfBeverageCombo.addActionListener(e -> {
            String selectedType = (String) typeOfBeverageCombo.getSelectedItem();

            beverageCombo.removeAllItems();
            if ("None".equals(selectedType)) {
                return;
            }

            switch (selectedType) {
                case "Coffee":
                    beverageCombo.addItem("Iced Americano");
                    beverageCombo.addItem("Espresso");
                    beverageCombo.addItem("Cappuccino");
                    break;
                case "Non-Coffee":
                    beverageCombo.addItem("Iced Matcha Latte");
                    beverageCombo.addItem("Hot Matcha Latte");
                    beverageCombo.addItem("Iced Chocolate");
                    break;
                case "Tea":
                    beverageCombo.addItem("Oolong");
                    beverageCombo.addItem("Green Peach");
                    beverageCombo.addItem("Earl Grey");
                    break;
            }
        });

        // Quantity dan buttons buat beverages
        JLabel beverageQuantityLabel = new JLabel("Quantity:");
        beverageQuantityLabel.setBounds(30, 190, 100, 25);
        mainPanel.add(beverageQuantityLabel);

        JLabel beverageQuantityValueLabel = new JLabel(String.valueOf(beverageQuantity));
        beverageQuantityValueLabel.setBounds(175, 190, 50, 25);
        beverageQuantityValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(beverageQuantityValueLabel);

        JButton beveragePlusButton = new JButton("+");
        beveragePlusButton.setBounds(230, 190, 45, 25);
        mainPanel.add(beveragePlusButton);

        JButton beverageMinusButton = new JButton("-");
        beverageMinusButton.setBounds(130, 190, 45, 25);
        mainPanel.add(beverageMinusButton);

        beveragePlusButton.addActionListener(e -> {
            beverageQuantity++;
            beverageQuantityValueLabel.setText(String.valueOf(beverageQuantity));
        });

        beverageMinusButton.addActionListener(e -> {
            if (beverageQuantity > 1) {
                beverageQuantity--;
                beverageQuantityValueLabel.setText(String.valueOf(beverageQuantity));
            }
        });

        // Food Section
        JLabel foodTypeLabel = new JLabel("Tipe Makanan:");
        foodTypeLabel.setBounds(30, 230, 100, 25);
        mainPanel.add(foodTypeLabel);

        String[] foodTypes = { "None", "Cake", "Donut", "Churros", "Croissant" };
        JComboBox<String> foodTypeCombo = new JComboBox<>(foodTypes);
        foodTypeCombo.setBounds(130, 230, 150, 25);
        mainPanel.add(foodTypeCombo);

        JLabel foodLabel = new JLabel("Makanan:");
        foodLabel.setBounds(30, 270, 100, 25);
        mainPanel.add(foodLabel);

        JComboBox<String> foodCombo = new JComboBox<>();
        foodCombo.setBounds(130, 270, 150, 25);
        mainPanel.add(foodCombo);

        JLabel foodQuantityLabel = new JLabel("Quantity:");
        foodQuantityLabel.setBounds(30, 310, 100, 25);
        mainPanel.add(foodQuantityLabel);

        JLabel foodQuantityValueLabel = new JLabel(String.valueOf(foodQuantity));
        foodQuantityValueLabel.setBounds(175, 310, 50, 25);
        foodQuantityValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(foodQuantityValueLabel);

        JButton foodPlusButton = new JButton("+");
        foodPlusButton.setBounds(230, 310, 45, 25);
        mainPanel.add(foodPlusButton);

        JButton foodMinusButton = new JButton("-");
        foodMinusButton.setBounds(130, 310, 45, 25);
        mainPanel.add(foodMinusButton);

        foodPlusButton.addActionListener(e -> {
            foodQuantity++;
            foodQuantityValueLabel.setText(String.valueOf(foodQuantity));
        });

        foodMinusButton.addActionListener(e -> {
            if (foodQuantity > 1) {
                foodQuantity--;
                foodQuantityValueLabel.setText(String.valueOf(foodQuantity));
            }
        });

        foodTypeCombo.addActionListener(e -> {
            String selectedFoodType = (String) foodTypeCombo.getSelectedItem();
            foodCombo.removeAllItems();
            if ("None".equals(selectedFoodType)) {
                return;
            }

            switch (selectedFoodType) {
                case "Cake":
                    foodCombo.addItem("Red Velvet");
                    foodCombo.addItem("Tiramisu");
                    foodCombo.addItem("Black Forest");
                    break;
                case "Donut":
                    foodCombo.addItem("Glazed Donut");
                    foodCombo.addItem("Chocolate Donut");
                    foodCombo.addItem("Strawberry Donut");
                    break;
                case "Churros":
                    foodCombo.addItem("Churros Classic");
                    foodCombo.addItem("Churros with Chocolate");
                    break;
                case "Croissant":
                    foodCombo.addItem("Butter Croissant");
                    foodCombo.addItem("Chocolate Croissant");
                    break;
            }
            System.out.println("Updated foodCombo with item for: " + selectedFoodType);
        });

        // Cart Section
        JLabel cartLabel = new JLabel("Cart:");
        cartLabel.setBounds(30, 360, 100, 25);
        mainPanel.add(cartLabel);

        JList<String> cartList = new JList<>(cartModel);
        JScrollPane cartScrollPane = new JScrollPane(cartList);
        cartScrollPane.setBounds(130, 360, 250, 100);
        mainPanel.add(cartScrollPane);

        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.setBounds(400, 360, 120, 30);
        mainPanel.add(addToCartButton);

        JButton clearCartButton = new JButton("Clear Cart");
        clearCartButton.setBounds(400, 400, 120, 30);
        mainPanel.add(clearCartButton);

        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.setBounds(400, 440, 120, 30);
        mainPanel.add(checkoutButton);

        addToCartButton.addActionListener(e -> {
            String selectedBeverage = (String) beverageCombo.getSelectedItem();
            String selectedFood = (String) foodCombo.getSelectedItem();
        
            if (selectedBeverage != null && !"None".equals(selectedBeverage)) {
                String selectedSize = (String) sizeCombo.getSelectedItem();
                if (beveragePrices.containsKey(selectedBeverage)) {
                    Map<String, Double> size = beveragePrices.get(selectedBeverage);
                    if (size.containsKey(selectedSize)) {
                        double price = size.get(selectedSize) * beverageQuantity;
                        cartModel.addElement(
                                selectedBeverage + " " + selectedSize + " x" + beverageQuantity + " - Rp" + price);
                    }
                }
            }
        
            if (selectedFood != null && !"None".equals(selectedFood)) {
                if (foodPrices.containsKey(selectedFood)) {
                    double price = foodPrices.get(selectedFood) * foodQuantity;
                    cartModel.addElement(selectedFood + " x" + foodQuantity + " - Rp" + price);
                } else {
                    System.out.println("Food not found in foodPrices: " + selectedFood);
                }
            }
        });
        

        clearCartButton.addActionListener(e -> cartModel.clear());

checkoutButton.addActionListener(e -> {
    totalPrice = 0;
    StringBuilder orderSummary = new StringBuilder();
    Connection connection = dbConnection.connect();

    if (connection != null) {
        try {
            int userId = SessionManager.getLoggedInUserId();
            if (userId == -1) {
                JOptionPane.showMessageDialog(orderFrame, "User not logged in!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (int i = 0; i < cartModel.size(); i++) {
                String cartItem = cartModel.get(i);
                String[] itemParts = cartItem.split(" - Rp");
                if (itemParts.length > 1) {
                    totalPrice += Double.parseDouble(itemParts[1]);
                    orderSummary.append(cartItem).append("\n");

                    // Parse item details
                    String[] details = itemParts[0].split(" x");
                    String itemName = details[0].trim();
                    int quantity = Integer.parseInt(details[1].trim());
                    double pricePerItem = Double.parseDouble(itemParts[1]) / quantity;

                    String itemType = itemName.contains(" ") ? "Minuman" : "Makanan"; // Check if it's food or drink
                    String size = itemType.equals("Minuman") ? itemName.split(" ")[1].trim() : null;

                    // Insert into transaksi table
                    String query = "INSERT INTO transaksi (id_user, nama_item, tipe_item, ukuran, jumlah, harga_per_item, total_harga) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement ps = connection.prepareStatement(query)) {
                        ps.setInt(1, userId);
                        ps.setString(2, itemName);
                        ps.setString(3, itemType);
                        ps.setString(4, size);
                        ps.setInt(5, quantity);
                        ps.setDouble(6, pricePerItem);
                        ps.setDouble(7, pricePerItem * quantity);
                        ps.executeUpdate();
                    }
                }
            }

            orderSummary.append("Total: Rp").append(totalPrice);
            JOptionPane.showMessageDialog(orderFrame, "Total: Rp" + totalPrice);

            // Navigate to PaymentView
            new PaymentView(orderSummary.toString(), totalPrice);
            orderFrame.dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            dbConnection.closeConnection(connection);
        }
    }
});


        orderFrame.add(mainPanel);
        orderFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new OrderView();
    }
}
