package Model;

import java.util.Date;

public class Promo {
    private int id;
    private String promoName;
    private String description;
    private double discountPercentage;
    private Date startDate;
    private Date endDate;
    private boolean isActive;

    public Promo(int id, String promoName, String description, double discountPercentage, Date startDate, Date endDate, boolean isActive) {
        this.id = id;
        this.promoName = promoName;
        this.description = description;
        this.discountPercentage = discountPercentage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPromoName() {
        return promoName;
    }

    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return promoName + " (" + discountPercentage + "% off)";
    }
}

// INSERT INTO promos (id, promo_name, description, discount_percentage, start_date, end_date, is_active)
// VALUES
// (1, 'Christmas Sale', 'Diskon spesial untuk Natal, dapatkan potongan hingga 50% untuk semua makanan dan minuman.', 50.0, '2024-12-01', '2024-12-25', TRUE),
// (2, 'New Year Offer', 'Rayakan Tahun Baru dengan diskon 30% untuk semua jenis kopi.', 30.0, '2024-12-25', '2025-01-01', TRUE),
// (3, 'Spring Promo', 'Nikmati promo spesial musim semi dengan diskon 20% untuk semua kue dan donut.', 20.0, '2024-03-01', '2024-03-31', TRUE),
// (4, 'Weekend Deal', 'Dapatkan 10% potongan harga setiap weekend untuk menu minuman', 10.0, '2024-12-01', '2024-12-31', TRUE),
// (5, 'Member Special', 'Diskon 15% untuk semua member yang melakukan pembelian di atas Rp 200.000', 15.0, '2024-11-01', '2024-11-30', FALSE);