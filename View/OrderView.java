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
        orderFrame.setSize(600, 550);
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

        JLabel beverageLabel = new JLabel("Minuman: ");
        beverageLabel.setBounds(30, 60, 100, 25);
        mainPanel.add(beverageLabel);

        String[] beverages = {"None", "Iced Latte", "Iced Americano", "Ice Cappucinno", "Hot Latte", "Hot Americano", "Hot Cappucinno", "Chai Latte", "Matcha Latter"};
        JComboBox<String> beverageCombo = new JComboBox<>(beverages);
        beverageCombo.setBounds(130, 60, 150, 25);
        mainPanel.add(beverageCombo);

        JLabel sizeLabel = new JLabel("Ukuran:");
        sizeLabel.setBounds(30, 100, 100, 25);
        mainPanel.add(sizeLabel);

        String[] sizes = {"None", "Medium", "Large"};
        JComboBox<String> sizeCombo = new JComboBox<>(sizes);
        sizeCombo.setBounds(130, 100, 150, 25);
        mainPanel.add(sizeCombo);

        JLabel foodTypeLabel = new JLabel("Tipe Makanan:");
        foodTypeLabel.setBounds(30, 140, 100, 25); 
        mainPanel.add(foodTypeLabel);

        String[] foodTypes = {"None", "Cake", "Donut", "Churros", "Croissant"};
        JComboBox<String> foodTypeCombo = new JComboBox<>(foodTypes);
        foodTypeCombo.setBounds(130, 140, 150, 25); 
        mainPanel.add(foodTypeCombo);

        JLabel foodLabel = new JLabel("Makanan:");
        foodLabel.setBounds(30, 180, 100, 25);
        mainPanel.add(foodLabel);

        JComboBox<String> foodCombo = new JComboBox<>();
        foodCombo.setBounds(130, 180, 150, 25);
        mainPanel.add(foodCombo);

        foodTypeCombo.addActionListener(e -> {
            String selectedType = (String) foodTypeCombo.getSelectedItem();
            foodCombo.removeAllItems();

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
                    foodCombo.addItem("Blueberry");
                    break;
                case "Churos":
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
        quantityLabel.setBounds(30, 220, 100, 25);
        mainPanel.add(quantityLabel);

        JLabel quantityValueLabel = new JLabel(String.valueOf(quantity));
        quantityValueLabel.setBounds(150, 220, 50, 25);
        quantityValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(quantityValueLabel);

        JButton plusButton = new JButton("+");
        plusButton.setBounds(200, 220, 45, 25);
        mainPanel.add(plusButton);

        JButton minusButton = new JButton("-");
        minusButton.setBounds(100, 220, 45, 25);
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
        addButton.setBounds(130, 260, 150, 30);
        mainPanel.add(addButton);

        JButton clearButton = new JButton("Clear");
        clearButton.setBounds(350, 350, 100, 30);
        mainPanel.add(clearButton);

        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.setBounds(450, 350, 100, 30);
        mainPanel.add(checkoutButton);

        addButton.addActionListener(e -> {
            String beverage = (String) beverageCombo.getSelectedItem();
            String size = (String) sizeCombo.getSelectedItem();
            String foodType = (String) foodTypeCombo.getSelectedItem();
            String food = (String) foodCombo.getSelectedItem();

            if (foodType.equals("None") && beverage.equals("None")) {
                JOptionPane.showMessageDialog(orderFrame, "Please select at least one item (food or beverage).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        
            String order;
            if (beverage.equals("None") && !foodType.equals("None") && !food.equals("None")) {
                order = quantity + "x " + foodType + " " + food; // Hanya makanan
            } else if (foodType.equals("None") || food.equals("None")) {
                order = quantity + "x " + size + " " + beverage;
            } else {
                order = quantity + "x " + size + " " + beverage + ", " + food;
            }
            cartModel.addElement(order);
        });
        
        
        

        clearButton.addActionListener(e -> cartModel.clear());

        checkoutButton.addActionListener(e -> {
            if (cartModel.getSize() == 0) {
                JOptionPane.showMessageDialog(orderFrame, "Your cart is empty!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                StringBuilder orderSummary = new StringBuilder("Order Summary:\n");
                for (int i = 0; i < cartModel.getSize(); i++) {
                    orderSummary.append(cartModel.get(i)).append("\n");
                }
                JOptionPane.showMessageDialog(orderFrame, orderSummary.toString(), "Checkout", JOptionPane.INFORMATION_MESSAGE);
                cartModel.clear();
            }
        });

        orderFrame.add(mainPanel);
        orderFrame.setVisible(true);
    }
}
