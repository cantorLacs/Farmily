package com.example.farmily;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import model.Sale;

public class SalesActivity extends AppCompatActivity {
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sales);

        tableLayout = findViewById(R.id.tableLayout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadSales();
    }

    private void loadSales() {

        FirebaseDatabase.getInstance().getReference("Sales")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (tableLayout != null) {
                            tableLayout.removeAllViews();
                        } else {
                            Log.e("SalesActivity", "TableLayout is null");
                        }
                        addTableHeader();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Sale sale = snapshot.getValue(Sale.class);
                            addTableRow(sale);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("InventoryActivity", "Error", databaseError.toException());
                    }
                });
    }

    private void addTableHeader() {
        TableRow header = new TableRow(this);
        TextView dateHeader = new TextView(this);
        dateHeader.setText("Date");
        TextView productHeader = new TextView(this);
        productHeader.setText("Product");
        TextView quantityHeader = new TextView(this);
        quantityHeader.setText("Quantity");
        TextView totalPriceHeader = new TextView(this);
        totalPriceHeader.setText("U. Price");
        TextView priceHeader = new TextView(this);
        priceHeader.setText("Total");



        header.addView(dateHeader);
        header.addView(productHeader);
        header.addView(quantityHeader);
        header.addView(totalPriceHeader);
        header.addView(priceHeader);
        tableLayout.addView(header);
    }

    private void addTableRow(Sale sale) {
        TableRow row = new TableRow(this);

        TextView dateTextView = new TextView(this);
        dateTextView.setText(sale.getDate());
        dateTextView.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView productTextView = new TextView(this);
        productTextView.setText(sale.getProduct());
        productTextView.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView quantityTextView = new TextView(this);
        quantityTextView.setText(String.valueOf(sale.getQuantity()));
        quantityTextView.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView totalPriceTextView = new TextView(this);
        totalPriceTextView.setText(String.valueOf(sale.getTotalPrice()));
        totalPriceTextView.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView priceTextView = new TextView(this);
        priceTextView.setText(String.valueOf(sale.getPrice()));
        priceTextView.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        row.addView(dateTextView);
        row.addView(productTextView);
        row.addView(quantityTextView);
        row.addView(totalPriceTextView);
        row.addView(priceTextView);


        tableLayout.addView(row);
    }
}