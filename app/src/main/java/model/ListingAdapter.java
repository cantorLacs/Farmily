package model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farmily.ProductListActivity;
import com.example.farmily.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ListingAdapter extends BaseAdapter {
    private ArrayList<Listing> listingArrayList;
    private Context context;
    private HashMap<Integer, Integer> quantityMap = new HashMap<>();


    public ListingAdapter(Context context, ArrayList<Listing> listingArrayList) {
        this.listingArrayList = listingArrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listingArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return listingArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        Listing listing;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.listing_card, parent, false);
        } else {
            view = convertView;
        }

        ImageView picture = view.findViewById(R.id.picture);
        TextView title = view.findViewById(R.id.title);
        TextView description = view.findViewById(R.id.description);
        TextView price = view.findViewById(R.id.price);
        TextView textCounter = view.findViewById(R.id.textCounter);
        Button buttonIncrease = view.findViewById(R.id.buttonIncrease);
        Button buttonDecrease = view.findViewById(R.id.buttonDecrease);
        Button buttonAddToCart = view.findViewById(R.id.buttonAddToCart);

        listing = (Listing) getItem(position);

        title.setText(listing.getTitle());
        description.setText(listing.getDescription());
        price.setText(String.valueOf(listing.getPrice()));

        int imageId = context.getResources().getIdentifier(listing.getImagePath(),"drawable",context.getPackageName());
        picture.setImageResource(imageId);

        quantityMap.putIfAbsent(position, 1);
        textCounter.setText(String.valueOf(quantityMap.get(position)));

        buttonIncrease.setOnClickListener(v -> {
            int currentQuantity = quantityMap.get(position);
            quantityMap.put(position, currentQuantity + 1);
            textCounter.setText(String.valueOf(quantityMap.get(position)));
        });

        buttonDecrease.setOnClickListener(v -> {
            int currentQuantity = quantityMap.get(position);
            if (currentQuantity > 1) {
                quantityMap.put(position, currentQuantity - 1);
                textCounter.setText(String.valueOf(quantityMap.get(position)));
            }
        });

        buttonAddToCart.setOnClickListener(v -> {

            int quantity = quantityMap.get(position);

            if (context instanceof ProductListActivity) {
                Cart cart = ((ProductListActivity) context).getCart();
                Listing listingCopy = listing.copy();
                listingCopy.setQuantity(quantity);
                cart.addToCart(listingCopy);
                Toast.makeText(context, "Added " + quantity + " of " + listing.getTitle() + " to cart", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
