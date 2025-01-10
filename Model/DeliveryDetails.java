package Model;

import java.time.LocalDateTime;

public class DeliveryDetails {
    private int detail_id;
    private Transaction transaction_id;
    private Status status;
    private String current_position;
    private String evidence;
    private LocalDateTime date;
    private String updated_by;

    public DeliveryDetails(int detail_id, Transaction transaction_id, Status status, String current_position,
                           String evidence, LocalDateTime date, String updated_by) {
        this.detail_id = detail_id;
        this.transaction_id = transaction_id;
        this.status = status;
        this.current_position = current_position;
        this.evidence = evidence;
        this.date = date;
        this.updated_by = updated_by;
    }

    public int getDetail_id() {
        return detail_id;
    }

    public void setDetail_id(int detail_id) {
        this.detail_id = detail_id;
    }

    public Transaction getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(Transaction transaction_id) {
        this.transaction_id = transaction_id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCurrent_position() {
        return current_position;
    }

    public void setCurrent_position(String current_position) {
        this.current_position = current_position;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public enum Status {
        PENDING("pending"),
        IN_PROGRESS("in_progress"),
        ON_DELIVERY("on_delivery"),
        ARRIVED("arrived");

        private final String status;

        Status(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

        public static Status fromString(String status) {
            for (Status s : Status.values()) {
                if (s.status.equalsIgnoreCase(status)) {
                    return s;
                }
            }
            throw new IllegalArgumentException("Invalid: " + status);
        }
    }
}