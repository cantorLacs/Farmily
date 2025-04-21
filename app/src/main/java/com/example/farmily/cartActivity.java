package com.example.farmily;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;
import model.Cart;
import model.Listing;

public class cartActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private Cart cart;
    private TextView totalPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tableLayout = findViewById(R.id.tableLayout);
        Button buttonAccount = findViewById(R.id.buttonAccount);
        totalPriceTextView = findViewById(R.id.totalPriceTextView); // Inicializa el TextView del total

        cart = (Cart) getIntent().getSerializableExtra("CART"); // Asegúrate de que Cart implemente Serializable

        if (cart != null) {
            displayCartItems();
        } else {
            Toast.makeText(this, "No items in the cart", Toast.LENGTH_SHORT).show();
        }

        buttonAccount.setOnClickListener(v -> finish()); // Regresar a la actividad anterior
    }

    private void displayCartItems() {
        ArrayList<Listing> productList = cart.getProductList();
        tableLayout.removeAllViews(); // Limpiar la tabla antes de agregar

        float totalPrice = 0;

        // Agregar encabezados de la tabla
        TableRow headerRow = new TableRow(this);
        headerRow.addView(createTextView("Item"));
        headerRow.addView(createTextView("Quantity"));
        headerRow.addView(createTextView("Price"));
        tableLayout.addView(headerRow);

        for (Listing listing : productList) {
            TableRow row = new TableRow(this);
            TextView title = createTextView(listing.getTitle());
            TextView quantity = createTextView(String.valueOf(listing.getQuantity()));
            TextView price = createTextView(String.valueOf(listing.getPrice()));

            row.addView(title);
            row.addView(quantity);
            row.addView(price);
            tableLayout.addView(row);

            // Calcular el precio total (cantidad * precio)
            totalPrice += listing.getPrice() * listing.getQuantity();
        }

        // Mostrar el precio total
        totalPriceTextView.setText("Total: $" + totalPrice);
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(16, 16, 16, 16);
        return textView;
    }
}