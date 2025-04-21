package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Cart implements Serializable {
    private ArrayList<Listing> productListingList;
    private float totalPrice;
    private String status;

    public Cart() {
        this.productListingList = new ArrayList<Listing>();
        totalPrice = 0;
        status = "pending";
    }

    public ArrayList<Listing> getProductList() {
        return productListingList;
    }
    public void setProductList(ArrayList<Listing> productList) {
        this.productListingList = productList;
    }
    public float getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public void addToCart(Listing productListingId){
        productListingList.add(productListingId);
        totalPrice += productListingId.getPrice();
    }

    public void removeFromCart(Listing productListing){

        productListingList.remove(productListing);
        totalPrice -= productListing.getPrice();

    }

    public void clearCart(){
        if (status.equals("paid") || status.equals("cancelled")){
            productListingList.clear();
            totalPrice = 0;
        }
    }
}