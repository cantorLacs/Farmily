package com.example.farmily;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private EditText emailInput, passwordInput;
    private Spinner roleSpinner;
    private Button loginButton;
    private String selectedRole = "Customer"; // Default role

    DatabaseReference userDatabase, listingDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI components
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        roleSpinner = findViewById(R.id.roleSpinner);
        loginButton = findViewById(R.id.loginButton);

        // Initialize DB
        //userDatabase = FirebaseDatabase.getInstance().getReference("Users");
        //listingDatabase = FirebaseDatabase.getInstance().getReference("Listings");


        // Set up Spinner (Dropdown) for Role Selection
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.user_roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        // Handle Role Selection
        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRole = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedRole = "Customer"; // Default
            }
        });

        // Login Button Click
        loginButton.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Basic validation
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Redirect based on selected role
        if (selectedRole.equals("Farmer")) {
            startActivity(new Intent(this, TrackingActivity.class));
            Toast.makeText(this, "Welcome Farmer!", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(this, ProductListActivity.class));
            Toast.makeText(this, "Welcome Customer!", Toast.LENGTH_SHORT).show();
        }

        finish(); // Close login activity
    }
}
