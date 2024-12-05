package View;

import javax.swing.*;
import java.awt.*;

public class OrderView {
    private DefaultListModel<String> cartModel;
    private int quantity = 1; // default qty

    public OrderView() {
        showOrderMenu();
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
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setBounds(200, 10, 200, 30);
        mainPanel.add(titleLabel);

        JLabel typeOfBeverageLabel = new JLabel("Tipe Minuman:");
        typeOfBeverageLabel.setBounds(30, 60, 100, 25);
        mainPanel.add(typeOfBeverageLabel);

        String[] beverageTypes = {"None", "Coffee", "Non-Coffee", "Tea"};
        JComboBox<String> typeOfBeverageCombo = new JComboBox<>(beverageTypes);
        typeOfBeverageCombo.setBounds(130, 60, 150, 25);
        mainPanel.add(typeOfBeverageCombo);

        JLabel beverageLabel = new JLabel("Minuman:");
        beverageLabel.setBounds(30, 100, 100, 25);
        mainPanel.add(beverageLabel);

        JComboBox<String> beverageCombo = new JComboBox<>();
        beverageCombo.setBounds(130, 100, 150, 25);
        mainPanel.add(beverageCombo);

        typeOfBeverageCombo.addActionListener(e -> {
            String selectedType = (String) typeOfBeverageCombo.getSelectedItem();
            beverageCombo.removeAllItems();

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
                    beverageCombo.addItem("Hot Chocolate");
                    beverageCombo.addItem("Iced Chai Latte");
                    break;
                case "Tea":
                    beverageCombo.addItem("Oolong");
                    beverageCombo.addItem("Green Peach");
                    beverageCombo.addItem("Earl Grey");
                    beverageCombo.addItem("Chamomile");
                    break;
                default:
                    beverageCombo.addItem("None");
                    break;
            }
        });

        JLabel sizeLabel = new JLabel("Ukuran:");
        sizeLabel.setBounds(30, 140, 100, 25);
        mainPanel.add(sizeLabel);

        String[] sizes = {"None", "Medium", "Large"};
        JComboBox<String> sizeCombo = new JComboBox<>(sizes);
        sizeCombo.setBounds(130, 140, 150, 25);
        mainPanel.add(sizeCombo);

        JLabel foodTypeLabel = new JLabel("Tipe Makanan:");
        foodTypeLabel.setBounds(30, 180, 100, 25); 
        mainPanel.add(foodTypeLabel);

        String[] foodTypes = {"None", "Cake", "Donut", "Churros", "Croissant"};
        JComboBox<String> foodTypeCombo = new JComboBox<>(foodTypes);
        foodTypeCombo.setBounds(130, 180, 150, 25); 
        mainPanel.add(foodTypeCombo);

        JLabel foodLabel = new JLabel("Makanan:");
        foodLabel.setBounds(30, 220, 100, 25);
        mainPanel.add(foodLabel);

        JComboBox<String> foodCombo = new JComboBox<>();
        foodCombo.setBounds(130, 220, 150, 25);
        mainPanel.add(foodCombo);

        foodTypeCombo.addActionListener(e -> {
            String selectedType = (String) foodTypeCombo.getSelectedItem();
            foodCombo.removeAllItems();

            switch (selectedType) {
                case "Cake":
                    foodCombo.addItem("Red Velvet");
                    foodCombo.addItem("Tiramisu");
                    foodCombo.addItem("Black Forest");
                    foodCombo.addItem("Choco Lava");
                    break;
                case "Donut":
                    foodCombo.addItem("Glazed");
                    foodCombo.addItem("Strawberry");
                    foodCombo.addItem("Choco Mint");
                    foodCombo.addItem("Blueberry");
                    break;
                case "Churros":
                    foodCombo.addItem("Honey");
                    foodCombo.addItem("Taro");
                    foodCombo.addItem("Dark Chocolate");
                    foodCombo.addItem("Cookies & Cream");
                    break;
                case "Croissant":
                    foodCombo.addItem("Plain");
                    foodCombo.addItem("Smoke Beef");
                    foodCombo.addItem("Double Cheese");
                    foodCombo.addItem("Cheese");
                    break;
                default:
                    foodCombo.addItem("None"); // set default
                    break;
            }
        });

        JLabel quantityLabel = new JLabel("Jumlah:");
        quantityLabel.setBounds(30, 260, 100, 25);
        mainPanel.add(quantityLabel);

        JLabel quantityValueLabel = new JLabel(String.valueOf(quantity));
        quantityValueLabel.setBounds(150, 260, 50, 25);
        quantityValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(quantityValueLabel);

        JButton plusButton = new JButton("+");
        plusButton.setBounds(200, 260, 45, 25);
        mainPanel.add(plusButton);

        JButton minusButton = new JButton("-");
        minusButton.setBounds(100, 260, 45, 25);
        mainPanel.add(minusButton);

        plusButton.addActionListener(e -> {
            quantity++;
            quantityValueLabel.setText(String.valueOf(quantity));
        });

        minusButton.addActionListener(e -> {
            if (quantity > 1) {
                quantity--;
                quantityValueLabel.setText(String.valueOf(quantity));
            }
        });

        JLabel cartLabel = new JLabel("Keranjang:");
        cartLabel.setBounds(350, 60, 100, 25);
        mainPanel.add(cartLabel);

        cartModel = new DefaultListModel<>();
        JList<String> cartList = new JList<>(cartModel);
        JScrollPane cartScrollPane = new JScrollPane(cartList);
        cartScrollPane.setBounds(350, 90, 200, 250);
        mainPanel.add(cartScrollPane);

        JButton addButton = new JButton("Tambah");
        addButton.setBounds(130, 310, 150, 30);
        mainPanel.add(addButton);

        addButton.addActionListener(e -> {
            String beverageType = (String) typeOfBeverageCombo.getSelectedItem();
            String beverage = (String) beverageCombo.getSelectedItem();
            String size = (String) sizeCombo.getSelectedItem();
            String foodType = (String) foodTypeCombo.getSelectedItem();
            String food = (String) foodCombo.getSelectedItem();

            if ((beverageType.equals("None") || beverage.equals("None")) && (foodType.equals("None") || food.equals("None"))) {
                JOptionPane.showMessageDialog(orderFrame, "Please select at least one item (food or beverage).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String order;
            if (foodType.equals("None") || food.equals("None")) {
                order = quantity + "x " + size + " " + beverageType + " " + beverage; // Hanya minuman
            } else if (beverageType.equals("None") || beverage.equals("None")) {
                order = quantity + "x " + foodType + " " + food; // Hanya makanan
            } else {
                order = quantity + "x " + size + " " + beverageType + " " + beverage + ", " + foodType + " " + food;
            }

            cartModel.addElement(order);
        });

        JButton clearButton = new JButton("Clear");
        clearButton.setBounds(350, 400, 100, 30);
        mainPanel.add(clearButton);

        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.setBounds(450, 400, 100, 30);
        mainPanel.add(checkoutButton);

        clearButton.addActionListener(e -> cartModel.clear());

        checkoutButton.addActionListener(e -> {
            if (cartModel.getSize() == 0) {
                JOptionPane.showMessageDialog(orderFrame, "Your cart is empty!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                StringBuilder orderSummary = new StringBuilder("Order Summary:\n");
                for (int i = 0; i < cartModel.size(); i++) {
                    orderSummary.append(cartModel.getElementAt(i)).append("\n");
                }
                JOptionPane.showMessageDialog(orderFrame, orderSummary.toString(), "Checkout", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        orderFrame.add(mainPanel);
        orderFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new OrderView();
    }
}
