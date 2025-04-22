package com.example.farmily;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.type.DateTime;
import java.time.LocalDate;
import java.util.Date;
import java.text.DecimalFormat;


import model.Cart;
import model.Listing;
import model.Sale;

public class OrderConfirmationActivity extends AppCompatActivity {

    private DatabaseReference salesDatabase;
    private TextView textViewTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_confirmation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initialize();
    }

    private void initialize() {
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice);
        Button buttonDone = findViewById(R.id.buttonDone);

        // Initialize Firebase Database reference
        salesDatabase = FirebaseDatabase.getInstance().getReference("Sales");

        // Get the cart object from the intent
        Cart cart = (Cart) getIntent().getSerializableExtra("CART");

        if (cart != null) {
            displayOrderDetails(cart);
        }

        // Set up button click listener to return to ProductListActivity
        buttonDone.setOnClickListener(v -> {
            Intent intent = new Intent(OrderConfirmationActivity.this, ProductListActivity.class);
            startActivity(intent);
            finish(); // Optional: finish the current activity
        });
    }

    private void registerOrder(Sale sale) {
        String key = salesDatabase.push().getKey();
        if (key != null) { // Ensure key is not null
            salesDatabase.child(key).setValue(sale).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Order Created Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error: " + task.getException(), Toast.LENGTH_LONG).show();
                    Log.e("FirebaseError", "Upload failed", task.getException());
                }
            });
        } else {
            Log.e("OrderConfirmation", "Failed to get a unique key for the sale.");
        }
    }

    @SuppressLint("SetTextI18n")
    private void displayOrderDetails(Cart cart) {
        LinearLayout orderItemsContainer = findViewById(R.id.orderItemsContainer);
        orderItemsContainer.removeAllViews(); // Clear previous items

        float totalPrice = cart.getTotalPrice();

        // Loop through the product listings in the cart
        for (Listing listing : cart.getProductList()) {
            // Create a new TextView for each item
            TextView itemView = new TextView(this);
            itemView.setText("Item: " + listing.getTitle() +
                    "\nQuantity: " + listing.getQuantity() +
                    "\nPrice: $" + listing.getPrice());
            itemView.setTextSize(16);
            itemView.setPadding(0, 8, 0, 8); // Add some padding for spacing
            itemView.setBackgroundResource(R.drawable.item_background); // Optional: add a background for better visuals

            // Add the TextView to the container
            orderItemsContainer.addView(itemView);

            Sale sale = new Sale(
                    listing.getTitle(),
                    listing.getQuantity(),
                    listing.getPrice(),
                    LocalDate.now().toString());

            registerOrder(sale);
        }

        // Set the total price
        DecimalFormat df = new DecimalFormat("#.00");
        textViewTotalPrice.setText("Total: $" + df.format(totalPrice));
    }
}