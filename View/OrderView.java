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
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        titleLabel.setBounds(260, 14, 200, 30);
        mainPanel.add(titleLabel);

        JLabel typeOfBeverageLabel = new JLabel("Tipe Minuman:");
        typeOfBeverageLabel.setBounds(30, 70, 100, 25);
        mainPanel.add(typeOfBeverageLabel);

        String[] beverageTypes = {"None", "Coffee", "Non-Coffee", "Tea"};
        JComboBox<String> typeOfBeverageCombo = new JComboBox<>(beverageTypes);
        typeOfBeverageCombo.setBounds(130, 70, 150, 25);
        mainPanel.add(typeOfBeverageCombo);

        JLabel beverageLabel = new JLabel("Minuman:");
        beverageLabel.setBounds(30, 110, 100, 25);
        mainPanel.add(beverageLabel);

        JComboBox<String> beverageCombo = new JComboBox<>();
        beverageCombo.setBounds(130, 110, 150, 25);
        mainPanel.add(beverageCombo);

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

        JLabel sizeLabel = new JLabel("Ukuran:");
        sizeLabel.setBounds(30, 150, 100, 25);
        mainPanel.add(sizeLabel);

        String[] sizes = {"None", "Medium", "Large"};
        JComboBox<String> sizeCombo = new JComboBox<>(sizes);
        sizeCombo.setBounds(130, 150, 150, 25);
        mainPanel.add(sizeCombo);

        JLabel foodTypeLabel = new JLabel("Tipe Makanan:");
        foodTypeLabel.setBounds(30, 190, 100, 25);
        mainPanel.add(foodTypeLabel);

        String[] foodTypes = {"None", "Cake", "Donut", "Churros", "Croissant"};
        JComboBox<String> foodTypeCombo = new JComboBox<>(foodTypes);
        foodTypeCombo.setBounds(130, 190, 150, 25);
        mainPanel.add(foodTypeCombo);

        JLabel foodLabel = new JLabel("Makanan:");
        foodLabel.setBounds(30, 230, 100, 25);
        mainPanel.add(foodLabel);

        JComboBox<String> foodCombo = new JComboBox<>();
        foodCombo.setBounds(130, 230, 150, 25);
        mainPanel.add(foodCombo);

        foodTypeCombo.addActionListener(e -> {
            String selectedType = (String) foodTypeCombo.getSelectedItem();

            foodCombo.removeAllItems();
            if ("None".equals(selectedType)) {
                return;
            }

            switch (selectedType) {
                case "Cake":
                    foodCombo.addItem("Red Velvet");
                    foodCombo.addItem("Tiramisu");
                    foodCombo.addItem("Black Forest");
                    break;
                case "Donut":
                    foodCombo.addItem("Glazed");
                    foodCombo.addItem("Strawberry");
                    foodCombo.addItem("Choco Mint");
                    break;
                case "Churros":
                    foodCombo.addItem("Honey");
                    foodCombo.addItem("Taro");
                    break;
                case "Croissant":
                    foodCombo.addItem("Plain");
                    foodCombo.addItem("Smoke Beef");
                    break;
            }
        });

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setBounds(30, 270, 100, 25);
        mainPanel.add(quantityLabel);

        JLabel quantityValueLabel = new JLabel(String.valueOf(quantity));
        quantityValueLabel.setBounds(175, 270, 50, 25);
        quantityValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(quantityValueLabel);

        JButton plusButton = new JButton("+");
        plusButton.setBounds(230, 270, 45, 25);
        mainPanel.add(plusButton);

        JButton minusButton = new JButton("-");
        minusButton.setBounds(130, 270, 45, 25);
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

        JLabel cartLabel = new JLabel("Cart:");
        cartLabel.setBounds(350, 60, 100, 25);
        mainPanel.add(cartLabel);

        cartModel = new DefaultListModel<>();
        JList<String> cartList = new JList<>(cartModel);
        JScrollPane cartScrollPane = new JScrollPane(cartList);
        cartScrollPane.setBounds(350, 100, 200, 250);
        mainPanel.add(cartScrollPane);

        JButton addButton = new JButton("Add");
        addButton.setBounds(130, 320, 150, 30);
        mainPanel.add(addButton);

        addButton.addActionListener(e -> {
            String beverageType = (String) typeOfBeverageCombo.getSelectedItem();
            String beverage = (String) beverageCombo.getSelectedItem();
            String size = (String) sizeCombo.getSelectedItem();
            String foodType = (String) foodTypeCombo.getSelectedItem();
            String food = (String) foodCombo.getSelectedItem();

            if (("None".equals(beverageType) || beverage == null) && ("None".equals(foodType) || food == null)) {
                JOptionPane.showMessageDialog(orderFrame, "Please select at least one item (food or beverage).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String order;
            if ("None".equals(foodType) || food == null) {
                order = quantity + "x " + size + " " + beverageType + " " + beverage;
            } else if ("None".equals(beverageType) || beverage == null) {
                order = quantity + "x " + foodType + " " + food;
            } else {
                order = quantity + "x " + size + " " + beverageType + " " + beverage + ", " + foodType + " " + food;
            }

            cartModel.addElement(order);
        });

        JButton clearButton = new JButton("Clear");
        clearButton.setBounds(350, 410, 100, 30);
        mainPanel.add(clearButton);

        JButton resetButton = new JButton("Reset");
        resetButton.setBounds(230, 410, 100, 30);
        mainPanel.add(resetButton);

        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.setBounds(450, 410, 100, 30);
        mainPanel.add(checkoutButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(30, 410, 100, 30);
        mainPanel.add(backButton);

        backButton.addActionListener(e -> {
            orderFrame.dispose(); // close current order frame
            new LoginView(); // back to login frame
        });

        clearButton.addActionListener(e -> cartModel.clear());

        resetButton.addActionListener(e -> {
            typeOfBeverageCombo.setSelectedIndex(0);
            beverageCombo.removeAllItems();
            sizeCombo.setSelectedIndex(0);
            foodTypeCombo.setSelectedIndex(0);
            foodCombo.removeAllItems();
            quantity = 1;
            quantityValueLabel.setText(String.valueOf(quantity));
        });

        checkoutButton.addActionListener(e -> {
            if (cartModel.isEmpty()) {
                JOptionPane.showMessageDialog(orderFrame, "Your cart is empty!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                StringBuilder orderSummary = new StringBuilder("Order Summary:\n");
                for (int i = 0; i < cartModel.size(); i++) {
                    orderSummary.append(cartModel.getElementAt(i)).append("\n");
                }
        
                // Tampilkan PaymentView setelah checkout
                new PaymentView(orderSummary.toString());
            }
        });

        

        orderFrame.add(mainPanel);
        orderFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new OrderView();
    }
}
