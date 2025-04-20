package model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.farmily.R;

import java.util.ArrayList;

public class ListingAdapter extends BaseAdapter {
    private ArrayList<Listing> listingArrayList;
    private Context context;

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
        ImageView picture;
        TextView title;
        TextView description;
        TextView price;

        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.listing_card,parent,false);

        picture = view.findViewById(R.id.picture);
        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);
        price = view.findViewById(R.id.price);
        listing = (Listing)getItem(position);

        title.setText(listing.getTitle());
        description.setText(listing.getDescription());
        price.setText(String.valueOf(listing.getPrice()));

        int imageRes = R.drawable.bananas;
        switch (listing.getTitle()) {
            case "Grapes":
                imageRes = R.drawable.grapes;
                break;
            case "Bread":
                imageRes = R.drawable.bread;
                break;
            case "Berries":
                imageRes = R.drawable.berries;
                break;
        }
        picture.setImageResource(imageRes);

        return view;
    }
}
