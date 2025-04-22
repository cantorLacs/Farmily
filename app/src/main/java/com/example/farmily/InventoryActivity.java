package com.example.farmily;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import model.Listing;

public class InventoryActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {
    LinearLayout layoutCards;
    Button buttonReturn;

    int sunriseBlue = Color.parseColor("#D3DAD5");
    int barkBrown = Color.parseColor("#482723");
    int darkSageGreen = Color.parseColor("#514E38");
    ArrayList<Listing> listingList = new ArrayList<Listing>();
    DatabaseReference listingDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inventory);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initialize();
    }

    private void initialize() {
        layoutCards = findViewById(R.id.layoutCards);

        buttonReturn = findViewById(R.id.buttonReturn);
        buttonReturn.setOnClickListener(this);

        listingDatabase = FirebaseDatabase.getInstance().getReference("Listings");

        fillListingList();

    }

    private void fillListingList() {

        listingDatabase.addValueEventListener(this);


    }
    private void createCard(@NonNull Listing listing){

        CardView rCard = new CardView(this);
        rCard.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        rCard.setRadius(20);

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
        picture.setImageResource(R.drawable.grapes);
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

        TextView stock = new TextView(this);
        stock.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        stock.setText(String.valueOf("Stock: " + listing.getStock()));
        stock.setTextSize(15);
        stock.setTextColor(barkBrown);

        inner.addView(title);
        inner.addView(description);
        inner.addView(price);
        inner.addView(stock);
        outer.addView(picture);
        outer.addView(inner);
        rCard.addView(outer);
        layoutCards.addView(rCard);
        //rCard.setId();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonReturn) {
            Intent intent = new Intent(InventoryActivity.this, TrackingActivity.class);
            startActivity(intent);
            finish(); // Optional: finish the current activity
        }
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
