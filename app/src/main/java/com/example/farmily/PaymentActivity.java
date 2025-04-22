package com.example.farmily;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

import model.Cart;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextCardHolderName;
    private EditText editTextCardNumber;
    private EditText editTextExpiryDate;
    private EditText editTextCVV;
    private TextView textViewTotalPriceValue;
    private Button buttonPay;
    private Button buttonReturn;
    private Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Initialize();
    }

    private void Initialize() {
        // Initialize UI elements
        editTextCardHolderName = findViewById(R.id.cardHolderName);
        editTextCardNumber = findViewById(R.id.cardNumber);
        editTextExpiryDate = findViewById(R.id.expiryDate);
        editTextCVV = findViewById(R.id.cvv);
        buttonPay = findViewById(R.id.btnPay);
        buttonPay.setOnClickListener(this);
        buttonReturn = findViewById(R.id.buttonReturn);
        buttonReturn.setOnClickListener(this);

        // Get the total price from the intent
        float totalPrice = getIntent().getFloatExtra("TOTAL_PRICE", 0.00F);
        textViewTotalPriceValue = findViewById(R.id.totalPriceValue);
        textViewTotalPriceValue.setText(String.format("$%.2f", totalPrice));
    }

    private void processPayment() {
        String cardHolderName = editTextCardHolderName.getText().toString().trim();
        String cardNumber = editTextCardNumber.getText().toString().trim();
        String expiryDate = editTextExpiryDate.getText().toString().trim();
        String cvv = editTextCVV.getText().toString().trim();

        if (validateInputs(cardHolderName, cardNumber, expiryDate, cvv)) {
            // Simulate payment processing
            boolean paymentApproved = true; // Change this based on your payment logic

            if (paymentApproved) {
                Cart cart = (Cart) getIntent().getSerializableExtra("CART");

                // Start PaymentConfirmationActivity
                Intent intent = new Intent(PaymentActivity.this, OrderConfirmationActivity.class);

                intent.putExtra("CART", cart); // Pass the cart object
                startActivity(intent);
                finish(); // Optional: finish the current activity
            } else {
                // Handle payment failure
                showToast("Payment failed. Please try again.");
            }
        }
    }

    private boolean validateInputs(String cardHolderName, String cardNumber, String expiryDate, String cvv) {
        if (TextUtils.isEmpty(cardHolderName)) {
            showToast("Please enter the card holder's name");
            return false;
        }

        if (TextUtils.isEmpty(cardNumber) || !isValidCardNumber(cardNumber)) {
            showToast("Please enter a valid card number");
            return false;
        }

        if (TextUtils.isEmpty(expiryDate) || !isValidExpiryDate(expiryDate)) {
            showToast("Please enter a valid expiry date (MM/YY)");
            return false;
        }

        if (TextUtils.isEmpty(cvv) || !isValidCVV(cvv)) {
            showToast("Please enter a valid CVV");
            return false;
        }

        return true;
    }

    private boolean isValidCardNumber(String cardNumber) {
        // Simple validation for card number length (e.g., 16 digits)
        return cardNumber.length() == 16 && TextUtils.isDigitsOnly(cardNumber);
    }

    private boolean isValidExpiryDate(String expiryDate) {
        // Simple validation for expiry date format (MM/YY)
        String[] parts = expiryDate.split("/");
        if (parts.length != 2) return false;

        String month = parts[0];
        String year = parts[1];

        // Check if month and year are valid digits and have correct lengths
        if (month.length() != 2 || year.length() != 2 || !TextUtils.isDigitsOnly(month) || !TextUtils.isDigitsOnly(year)) {
            return false;
        }

        try {
            // Parse the month and year
            int expMonth = Integer.parseInt(month);
            int expYear = Integer.parseInt(year) + 2000; // Assuming YY format for years

            // Get the current date
            Calendar current = Calendar.getInstance();
            int currentYear = current.get(Calendar.YEAR);
            int currentMonth = current.get(Calendar.MONTH) + 1; // Months are zero-based in Calendar

            // Check if the expiry date is in the past
            if (expYear < currentYear || (expYear == currentYear && expMonth < currentMonth)) {
                showToast("Expiry date cannot be in the past");
                return false;
            }
        } catch (NumberFormatException e) {
            return false; // In case of invalid number format
        }

        return true;
    }

    private boolean isValidCVV(String cvv) {
        // Simple validation for CVV (3 or 4 digits)
        return (cvv.length() == 3 || cvv.length() == 4) && TextUtils.isDigitsOnly(cvv);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnPay) {
            processPayment();
        }
        if (v.getId() == R.id.buttonReturn) {
            Intent intent = new Intent(PaymentActivity.this, ProductListActivity.class);
            intent.putExtra("CART", cart);
            startActivity(intent);
            finish(); // Optional: finish the current activity
        }
    }
}