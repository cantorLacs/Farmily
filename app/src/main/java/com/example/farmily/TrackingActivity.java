package com.example.farmily;

import android.annotation.SuppressLint;
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

    Button buttonGoToListingActivity;
    Button btnInventory;
    Button btnSalesHistory;
    Button btnReturn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tracking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_traking), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        initialize();
    }

    private void initialize(){
        buttonGoToListingActivity = findViewById(R.id.buttonGoToListingActivity);
        buttonGoToListingActivity.setOnClickListener(this);
        btnInventory = findViewById(R.id.buttonInventory);
        btnSalesHistory = findViewById(R.id.buttonSalesHistory);
        btnInventory.setOnClickListener(this);
        btnSalesHistory.setOnClickListener(this);
        btnReturn = findViewById(R.id.buttonReturnHome);
        btnReturn.setOnClickListener(this);

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
        } else if (view.getId() == R.id.buttonGoToListingActivity){
            intent = new Intent(this, ListingActivity.class);
            startActivity(intent);
        }else if (view.getId() == R.id.buttonReturnHome){
            intent = new Intent(this, ProductListActivity.class);
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