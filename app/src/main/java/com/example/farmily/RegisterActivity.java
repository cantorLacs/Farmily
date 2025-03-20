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

public class RegisterActivity extends AppCompatActivity {
    private EditText nameInput, emailInput, passwordInput;
    private Spinner roleSpinner;
    private Button registerButton;
    private String selectedRole = "Customer"; // Default role

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize UI components
        nameInput = findViewById(R.id.name);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        roleSpinner = findViewById(R.id.roleSpinner);
        registerButton = findViewById(R.id.registerButton);

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

        // Register Button Click
        registerButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Basic Validation
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simulating a successful registration
        Toast.makeText(this, "Registered as " + selectedRole, Toast.LENGTH_SHORT).show();

        // Redirecting to LoginActivity
        Intent intent = new Intent(this, ProductListActivity.class);
        startActivity(intent);
        finish(); // Close Registration Page
    }
}
