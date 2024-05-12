package com.example.projectapp;

import androidx.appcompat.app.AppCompatActivity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });


    }


    private void loginUser() {
        final String userName = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        User user = new User();
        user.setEmail(userName);
        user.setPassword(password);

        if (userName.isEmpty()) {
            etUsername.setError("Username is required");
            etUsername.requestFocus();
            return;
        } else if (password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call <ResponseBody> call =apiService.checkUser(user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String token = response.body().string();
                        System.out.println(token);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token", token);
                        Intent intent=new Intent(LoginActivity.this, DashboardActivity.class);

         editor.apply();
                        startActivity(intent);

                        Toast.makeText(LoginActivity.this, "logged in", Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(LoginActivity.this, ""+t, Toast.LENGTH_SHORT).show();
                throw new RuntimeException(t);

            }
        });


    }
}





