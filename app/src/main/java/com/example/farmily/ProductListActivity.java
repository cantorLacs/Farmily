package com.example.farmily;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import model.Address;
import model.Cart;
import model.Listing;
import model.ListingAdapter;

public class ProductListActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {
    LinearLayout layoutCards;
    Button btnAccount,buttonFilter;
    EditText editTextCustomerLocation;
    ListView listViewCards;
    SearchView searchBar;
    int sunriseBlue = Color.parseColor("#D3DAD5");
    int barkBrown = Color.parseColor("#482723");
    int darkSageGreen = Color.parseColor("#514E38");
    ArrayList<Listing> listingList = new ArrayList<Listing>();
    DatabaseReference listingDatabase;

    ListingAdapter listingAdapter;
    private Cart cart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cart = new Cart();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initialize();
    }
    private void initialize(){
        layoutCards = findViewById(R.id.layoutCards);
        btnAccount = findViewById(R.id.buttonAccount);
        searchBar = findViewById(R.id.search);
        buttonFilter = findViewById(R.id.buttonFilter);
        EditText editTextCustomerLocation = findViewById(R.id.editTextCustomerLocation);
        listViewCards = findViewById(R.id.listViewCards);
        Button buttonResetFilter = findViewById(R.id.buttonResetFilter);

        btnAccount.setOnClickListener(this);

        listingDatabase = FirebaseDatabase.getInstance().getReference("Listings");
        listingDatabase.addValueEventListener(this);


        listingAdapter = new ListingAdapter(this, listingList);
        listViewCards.setAdapter(listingAdapter);

        Button buttonCart = findViewById(R.id.buttonCart);
        buttonCart.setOnClickListener(v -> openCartActivity());

        //searchBar.setQuery("125 Avenue du Mont-Royal Ouest",false);

        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredLocation = editTextCustomerLocation.getText().toString().trim();
                if (!enteredLocation.isEmpty()) {
                    applyFilterByLocation(enteredLocation);
                } else {
                    Toast.makeText(ProductListActivity.this, "Enter a location to filter", Toast.LENGTH_SHORT).show();
                }
            }
        });


        buttonResetFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFilter();
            }
        });

    }
    private void openCartActivity() {
        Intent intent = new Intent(this, cartActivity.class);
        intent.putExtra("CART", cart);
        startActivity(intent);
    }
    private void applyFilterByLocation(String location) {
        String keyword = location.toLowerCase().trim();
        Log.d("FILTER", "Filtering by city: " + keyword);

        listingDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listingList.clear();

                for (DataSnapshot child : snapshot.getChildren()) {
                    try {
                        Listing listing = child.getValue(Listing.class);

                        if (listing != null && listing.getDeliveryArea() != null) {
                            String city = listing.getDeliveryArea().getCity();

                            if (city != null && city.toLowerCase().contains(keyword)) {
                                createCard(listing);
                            }
                        }
                    } catch (Exception e) {
                        Log.e("FILTER", "Skipping invalid listing: " + e.getMessage());
                    }
                }

                if (listingList.isEmpty()) {
                    Toast.makeText(ProductListActivity.this, "No matching listings found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProductListActivity.this, "Filtering failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void resetFilter() {
        listingList.clear();

        listingDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    try {
                        Listing listing = child.getValue(Listing.class);
                        if (listing != null) {
                            createCard(listing);
                        }
                    } catch (Exception e) {
                        Log.e("RESET", "Skipping invalid listing: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProductListActivity.this, "Reset failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Helper to avoid null issues
    private String safe(String value) {
        return value != null ? value : "";
    }


    // This function creates a listing card for the feed
    private void createCard(@NonNull Listing listing){

        listingList.add(listing);
        listingAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {
        //Listing.findAll(v);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.exists()){

            for (DataSnapshot child: snapshot.getChildren()){
                String key = child.getKey().toString();
                Listing listing = new Listing();
                listing.setTitle(snapshot.child(key).child("title").getValue().toString());
                listing.setDescription(snapshot.child(key).child("description").getValue().toString());
                listing.setPrice( Float.parseFloat(snapshot.child(key).child("price").getValue().toString()));
                createCard(listing);
            }
//
        }
        else{
            Toast.makeText(this,
                    "The person doesn't exist",
                    Toast.LENGTH_LONG).show();

        }
    }

    public Cart getCart() {
        return cart;
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}