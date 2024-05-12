package com.example.projectapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.mindrot.jbcrypt.BCrypt;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword;
    private Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnUpdate = findViewById(R.id.btnUpdate);

        // Assuming you have obtained the user ID or token after successful login
        String token = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("token", "");

        // Call the API to get the user's information based on the user ID
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<User> call = apiService.getUserInfo(1L);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        // Populate the EditText fields with the user's information
                        etUsername.setText(user.getUsername());
                        etEmail.setText(user.getEmail());
                        // You can choose whether to display the password or not
                        // etPassword.setText(user.getPassword());
                    }
                } else {
                    Toast.makeText(UpdateUserActivity.this, "Failed to fetch user information", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(UpdateUserActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the updated user information from EditText fields
                String updatedUsername = etUsername.getText().toString().trim();
                String updatedEmail = etEmail.getText().toString().trim();
                String updatedPassword = etPassword.getText().toString().trim(); // You may want to validate or encrypt the password
                String hashedPassword = BCrypt.hashpw(updatedPassword, BCrypt.gensalt());

                // Create a User object with the updated information
                User updatedUser = new User();
                updatedUser.setUsername(updatedUsername);
                updatedUser.setEmail(updatedEmail);
                updatedUser.setPassword(hashedPassword);

                // Call the API to update the user's information
                Call<User> updateCall = apiService.updateUser(1L, updatedUser);
                updateCall.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {


                            // Handle successful update
                            Toast.makeText(UpdateUserActivity.this, "User updated successfully", Toast.LENGTH_SHORT).show();
                            // Refresh user information in UserInfoActivity
                            Intent intent = new Intent(UpdateUserActivity.this, UserInfoActivity.class);
                            startActivity(intent);
                            finish(); // Close the current activity

                        } else {
                            // Handle unsuccessful update
                            Toast.makeText(UpdateUserActivity.this, "Failed to update user", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        // Handle network errors or exceptions
                        Toast.makeText(UpdateUserActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}


