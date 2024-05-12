package com.example.projectapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    private TextView eventCountTextView;
    private TextView clientCountTextView;
    private TextView employeeCountTextView;
    private TextView mostSavedAddressesTextView;
    private TextView departmentsMostEmployeesTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        departmentsMostEmployeesTextView = findViewById(R.id.departmentsMostEmployeesTextView);

        mostSavedAddressesTextView = findViewById(R.id.mostSavedAddressesTextView);


        // Initialize TextViews
        eventCountTextView = findViewById(R.id.eventCountTextView);
        clientCountTextView = findViewById(R.id.clientCountTextView);
        employeeCountTextView = findViewById(R.id.employeeCountTextView);

        Button btnChauffeurList = findViewById(R.id.btnChauffeurList);
        Button btnUserDetails = findViewById(R.id.btnUserDetails);

        btnChauffeurList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the chauffeur list activity
                Intent intent = new Intent(DashboardActivity.this, AllChauffeurActivity.class);
                startActivity(intent);
            }
        });

        btnUserDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the user details activity
                Intent intent = new Intent(DashboardActivity.this, UserInfoActivity.class);
                startActivity(intent);
            }
        });


        fetchMostSavedAddresses();
        fetchmostDepartment();

        // Make network request to fetch statistics
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<Map<String, Long>> call = apiService.getStatisticsCounts();
        call.enqueue(new Callback<Map<String, Long>>() {
            @Override
            public void onResponse(Call<Map<String, Long>> call, Response<Map<String, Long>> response) {
                if (response.isSuccessful()) {
                    Map<String, Long> statisticsMap = response.body();
                    if (statisticsMap != null) {
                        // Update UI with statistics counts
                        Long eventCount = statisticsMap.get("eventCount");
                        Long clientCount = statisticsMap.get("clientCount");
                        Long employeeCount = statisticsMap.get("employeeCount");

                        eventCountTextView.setText("Events: " + eventCount);
                        clientCountTextView.setText("Clients: " + clientCount);
                        employeeCountTextView.setText("Employees: " + employeeCount);
                    }
                } else {
                    Toast.makeText(DashboardActivity.this, "Failed to fetch statistics", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Long>> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void fetchMostSavedAddresses() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Object[]>> call = apiService.getMostSavedAddresses();
        call.enqueue(new Callback<List<Object[]>>() {
            @Override
            public void onResponse(Call<List<Object[]>> call, Response<List<Object[]>> response) {
                if (response.isSuccessful()) {
                    List<Object[]> mostSavedAddresses = response.body();
                    if (mostSavedAddresses != null && !mostSavedAddresses.isEmpty()) {
                        // Convert list to a string representation
                        StringBuilder addressesStringBuilder = new StringBuilder();
                        for (Object[] address : mostSavedAddresses) {
                            String addressString = address[0].toString();
                            addressesStringBuilder.append(addressString).append("\n");
                        }
                        // Set the addresses string to the TextView
                        mostSavedAddressesTextView.setText(addressesStringBuilder.toString());
                    } else {
                        Toast.makeText(DashboardActivity.this, "No saved addresses found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DashboardActivity.this, "Failed to fetch saved addresses", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Object[]>> call, Throwable t) {
                // Handle network errors or exceptions
                Toast.makeText(DashboardActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void fetchmostDepartment() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Object[]>> call = apiService.getDepartmentsByMostEmployees();
        call.enqueue(new Callback<List<Object[]>>() {
            @Override
            public void onResponse(Call<List<Object[]>> call, Response<List<Object[]>> response) {
                if (response.isSuccessful()) {
                    List<Object[]> departmentsMostEmployees = response.body();
                    if (departmentsMostEmployees != null && !departmentsMostEmployees.isEmpty()) {
                        List<String> departmentNames = new ArrayList<>();
                        // Extract department names from the response
                        for (Object[] department : departmentsMostEmployees) {
                            String departmentName = department[0].toString();
                            departmentNames.add(departmentName);
                        }
                        // Pass the list of department names to a method for further processing
                        displayDepartmentNames(departmentNames);
                    } else {
                        Toast.makeText(DashboardActivity.this, "No departments found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DashboardActivity.this, "Failed to fetch departments", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Object[]>> call, Throwable t) {
                // Handle network errors or exceptions
                Toast.makeText(DashboardActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayDepartmentNames(List<String> departmentNames) {
        // Initialize a StringBuilder to concatenate department names
        StringBuilder departmentsStringBuilder = new StringBuilder();

        // Loop through the list of department names and append each name to the StringBuilder
        for (String departmentName : departmentNames) {
            departmentsStringBuilder.append(departmentName).append("\n");
        }

        // Set the concatenated department names string to the TextView
        departmentsMostEmployeesTextView.setText(departmentsStringBuilder.toString());
    }



}

