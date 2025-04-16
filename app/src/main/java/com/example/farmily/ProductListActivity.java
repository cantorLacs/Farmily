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
import model.Listing;

public class ProductListActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {
    LinearLayout layoutCards;
    Button btnAccount,buttonFilter;
    EditText editTextCustomerLocation;

    SearchView searchBar;
    int sunriseBlue = Color.parseColor("#D3DAD5");
    int barkBrown = Color.parseColor("#482723");
    int darkSageGreen = Color.parseColor("#514E38");
    ArrayList<Listing> listingList = new ArrayList<Listing>();
    DatabaseReference listingDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        btnAccount.setOnClickListener(this);

        listingDatabase = FirebaseDatabase.getInstance().getReference("Listings");

        fillListingList();
        buttonFilter = findViewById(R.id.buttonFilter);
        EditText editTextCustomerLocation = findViewById(R.id.editTextCustomerLocation);

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
        Button buttonResetFilter = findViewById(R.id.buttonResetFilter);
        buttonResetFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFilter();
            }
        });

    }
    private void applyFilterByLocation(String location) {
        String keyword = location.toLowerCase().trim();
        Log.d("FILTER", "Filtering by city: " + keyword);

        listingDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                layoutCards.removeAllViews();
                listingList.clear();

                for (DataSnapshot child : snapshot.getChildren()) {
                    try {
                        Listing listing = child.getValue(Listing.class);

                        if (listing != null && listing.getDeliveryArea() != null) {
                            String city = listing.getDeliveryArea().getCity();

                            if (city != null && city.toLowerCase().contains(keyword)) {
                                listingList.add(listing);
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
        layoutCards.removeAllViews();
        listingList.clear();

        listingDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    try {
                        Listing listing = child.getValue(Listing.class);
                        if (listing != null) {
                            listingList.add(listing);
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



    private void fillListingList(){

        listingDatabase.addValueEventListener(this);


    }

    // This function creates a listing card for the feed
    private void createCard(@NonNull Listing listing){

        CardView rCard = new CardView(this);
        rCard.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        rCard.setRadius(20);

        ViewGroup.MarginLayoutParams cardViewMarginParams = (ViewGroup.MarginLayoutParams) rCard.getLayoutParams();cardViewMarginParams.setMargins(0, 30, 0, 30);
        rCard.requestLayout();


        LinearLayout outer = new LinearLayout(this);
        outer.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        outer.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout inner = new LinearLayout(this);
        inner.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        inner.setOrientation(LinearLayout.VERTICAL);
        inner.setPadding(30,30,30,30);


        ImageView picture = new ImageView(this);
        picture.setLayoutParams(new LinearLayout.LayoutParams(300,300));
        picture.setImageResource(listing.getTitle().equals("Grapes")?R.drawable.grapes:R.drawable.bananas);
        picture.setPadding(30,30,30,30);


        TextView title = new TextView(this);
        title.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        title.setText(listing.getTitle());
        title.setTextSize(25);
        title.setTextColor(barkBrown);


        TextView description = new TextView(this);
        description.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        description.setText(listing.getDescription());
        description.setTextSize(15);
        description.setTextColor(barkBrown);


        TextView price = new TextView(this);
        price.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        price.setText(String.valueOf(listing.getPrice()));
        price.setTextSize(15);
        price.setTextColor(barkBrown);

        inner.addView(title);
        inner.addView(description);
        inner.addView(price);
        outer.addView(picture);
        outer.addView(inner);
        rCard.addView(outer);
        layoutCards.addView(rCard);
        //rCard.setId();
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

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}