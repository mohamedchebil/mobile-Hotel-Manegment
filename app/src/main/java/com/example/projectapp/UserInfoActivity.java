package com.example.projectapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoActivity extends AppCompatActivity {

    private TextView tvUsername;
    private TextView nom ;

    Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        tvUsername = findViewById(R.id.textViewUsername);
        nom = findViewById(R.id.textViewnom);
        btnUpdate = findViewById(R.id.btnUpdate);

        // Assuming you have obtained the user ID or token after successful login
        String userId = "1";
        String token = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("token", "");

        String intent=getIntent().getStringExtra("token");

        System.out.println( "tooooookken :"+intent);

        // Decode the JWT token


        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call <User> call =apiService.getUserInfo(1L);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try {
                    //System.out.println(response.body().getEmail());

                if (response.isSuccessful()) {
                    System.out.println(token);


                        // Update UI with user information
                        tvUsername.setText(response.body().getEmail());
                        nom.setText(response.body().getUsername());

                    // Update other TextViews with additional user info fields if needed

                } else {
                    System.out.println(token);
                    // Handle unsuccessful response
                    Toast.makeText(UserInfoActivity.this, "Failed to fetch user information", Toast.LENGTH_SHORT).show();
                }
                }catch (Exception ex){
                    ex.getMessage();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Handle network errors or exceptions
                System.out.println(t.getMessage());
                Toast.makeText(UserInfoActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });




    }
    public void openUpdateUserActivity(View view) {
        Intent updateUserIntent = new Intent(this, UpdateUserActivity.class);
        startActivity(updateUserIntent);
    }
}
