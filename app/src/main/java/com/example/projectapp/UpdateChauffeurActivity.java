package com.example.projectapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateChauffeurActivity extends AppCompatActivity {

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;
    private EditText etAvailability;
    private Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_chauffeur);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etAvailability = findViewById(R.id.etAvailability);
        btnUpdate = findViewById(R.id.btnUpdate);

        // Retrieve chauffeur ID from intent
        long chauffeurId = getIntent().getLongExtra("chauffeurId", 0);

        // Implement Retrofit call to fetch chauffeur details by ID and populate EditText fields
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<Chauffeur> call = apiService.getChauffeurById(chauffeurId);
        call.enqueue(new Callback<Chauffeur>() {
            @Override
            public void onResponse(Call<Chauffeur> call, Response<Chauffeur> response) {
                if (response.isSuccessful()) {
                    Chauffeur chauffeur = response.body();
                    if (chauffeur != null) {
                        // Populate EditText fields with chauffeur details
                        etFirstName.setText(chauffeur.getFirstName());
                        etLastName.setText(chauffeur.getLastName());
                        etEmail.setText(chauffeur.getEmail());
                        etAvailability.setText(String.valueOf(chauffeur.isDispo())); // Convert boolean to String
                    } else {
                        Toast.makeText(UpdateChauffeurActivity.this, "Chauffeur details not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UpdateChauffeurActivity.this, "Failed to fetch chauffeur details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Chauffeur> call, Throwable t) {
                // Handle network errors or exceptions
                Toast.makeText(UpdateChauffeurActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Implement update button click listener
        btnUpdate.setOnClickListener(v -> {
            // Retrieve updated values from EditText fields
            String updatedFirstName = etFirstName.getText().toString();
            String updatedLastName = etLastName.getText().toString();
            String updatedEmail = etEmail.getText().toString();
            boolean updatedAvailability = Boolean.parseBoolean(etAvailability.getText().toString());

            // Create a Chauffeur object with updated values
            Chauffeur updatedChauffeur = new Chauffeur();
            updatedChauffeur.setFirstName(updatedFirstName);
            updatedChauffeur.setLastName(updatedLastName);
            updatedChauffeur.setEmail(updatedEmail);
            updatedChauffeur.setDispo(updatedAvailability);

            // Implement Retrofit call to update chauffeur details
            Call<Chauffeur> updateCall = apiService.updateChauffeur(chauffeurId, updatedChauffeur);
            updateCall.enqueue(new Callback<Chauffeur>() {
                @Override
                public void onResponse(Call<Chauffeur> call, Response<Chauffeur> response) {
                    if (response.isSuccessful()) {
                        // Handle successful update
                        Toast.makeText(UpdateChauffeurActivity.this, "Chauffeur updated successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateChauffeurActivity.this, AllChauffeurActivity.class);
                        startActivity(intent);
                        finish(); // Close the activity after updating
                    } else {
                        // Handle unsuccessful update
                        Toast.makeText(UpdateChauffeurActivity.this, "Failed to update chauffeur", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Chauffeur> call, Throwable t) {
                    // Handle network errors or exceptions
                    Toast.makeText(UpdateChauffeurActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}

