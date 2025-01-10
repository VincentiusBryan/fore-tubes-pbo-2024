package Model;

import java.time.LocalDateTime;

public class Transaction {
    private int transaction_id;
    private int customer_id; 
    private String delivery_type;
    private int expected_weight;
    private int total_cost;
    private LocalDateTime created_at;
    private String receipt_name;
    private String receipt_address;
    private String receipt_phone;

    public Transaction(int transaction_id, int customer_id, String delivery_type, int expected_weight,
                       int total_cost, LocalDateTime created_at, String receipt_name, String receipt_address, String receipt_phone) {
        this.transaction_id = transaction_id;
        this.customer_id = customer_id;
        this.delivery_type = delivery_type;
        this.expected_weight = expected_weight;
        this.total_cost = total_cost;
        this.created_at = created_at;
        this.receipt_name = receipt_name;
        this.receipt_address = receipt_address;
        this.receipt_phone = receipt_phone;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getDelivery_type() {
        return delivery_type;
    }

    public void setDelivery_type(String delivery_type) {
        this.delivery_type = delivery_type;
    }

    public int getExpected_weight() {
        return expected_weight;
    }

    public void setExpected_weight(int expected_weight) {
        this.expected_weight = expected_weight;
    }

    public int getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(int total_cost) {
        this.total_cost = total_cost;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public String getReceipt_name() {
        return receipt_name;
    }

    public void setReceipt_name(String receipt_name) {
        this.receipt_name = receipt_name;
    }

    public String getReceipt_address() {
        return receipt_address;
    }

    public void setReceipt_address(String receipt_address) {
        this.receipt_address = receipt_address;
    }

    public String getReceipt_phone() {
        return receipt_phone;
    }

    public void setReceipt_phone(String receipt_phone) {
        this.receipt_phone = receipt_phone;
    }
}
