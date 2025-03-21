package com.example.farmily;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class TrackingActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {


    Button btnInventory;
    Button btnSalesHistory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tracking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        initialize();
    }
    private void initialize(){

        btnInventory = findViewById(R.id.buttonInventory);
        btnSalesHistory = findViewById(R.id.buttonSalesHistory);
        btnInventory.setOnClickListener(this);
        btnSalesHistory.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent;

        if (view.getId() == R.id.buttonInventory) {
            intent = new Intent(this, InventoryActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.buttonSalesHistory) {
            intent = new Intent(this, SalesActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}