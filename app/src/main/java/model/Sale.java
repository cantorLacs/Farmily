package model;

import java.util.Date;

public class Sale {
    private String buyer;
    private String  date;
    private double price;
    private String product;
    private int quantity;
    private String seller;
    private double taxes;



    private double totalPrice;

    public Sale() {
        this.buyer = "User";
        this.date = "";
        this.seller = "User";
        this.taxes = 0.0f;
    }

    public Sale(String product, int quantity, double price,  String date) {
        this.buyer = "User";
        this.date = date;
        this.seller = "User";
        this.taxes = 0.0f;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = price * quantity;

    }
    public Sale(String buyer, String date, String product, int quantity, String seller, double taxes, double price, double totalPrice) {
        this.buyer = buyer;
        this.date = date;
        this.product = product;
        this.quantity = quantity;
        this.seller = seller;
        this.taxes = taxes;
        this.price = price;
        this.totalPrice = totalPrice;
    }

    // Getters Setters
    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public double getTaxes() {
        return taxes;
    }

    public void setTaxes(double taxes) {
        this.taxes = taxes;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

}

