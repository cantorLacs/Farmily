package model;

import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.type.DateTime;


public class Sale {
    private String date;
    private String product;
    private int quantity;
    private double totalPrice;
    private String buyer;
    private String seller;
    private double taxes;

    public Sale() {
    }

    public Sale(String date, String product, int quantity, double totalPrice, String buyer, String seller, double taxes) {
        this.date = date;
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.buyer = buyer;
        this.seller = seller;
        this.taxes = taxes;
    }

    public String getDate() {
        return date;
    }

    public String getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getBuyer() {
        return buyer;
    }

    public String getSeller() {
        return seller;
    }

    public double getTaxes() {
        return taxes;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "date='" + date + '\'' +
                ", product='" + product + '\'' +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                ", buyer='" + buyer + '\'' +
                ", seller='" + seller + '\'' +
                ", taxes=" + taxes +
                '}';
    }

    public double calculateTotalWithTaxes() {
        return totalPrice + taxes;
    }
}