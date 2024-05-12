package com.example.projectapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllChauffeurActivity extends AppCompatActivity implements ChauffeurAdapter.OnUpdateClickListener ,ChauffeurAdapter.OnDeleteClickListener{

    private RecyclerView recyclerView;
    private ChauffeurAdapter adapter;
    private List<Chauffeur> chauffeurList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_chauffeur);

        recyclerView = findViewById(R.id.recyclerViewChauffeurs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChauffeurAdapter();
        recyclerView.setAdapter(adapter);



        // Make network request to fetch all chauffeurs
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Chauffeur>> call = apiService.getAllChauffeurs();
        call.enqueue(new Callback<List<Chauffeur>>() {
            @Override
            public void onResponse(Call<List<Chauffeur>> call, Response<List<Chauffeur>> response) {
                if (response.isSuccessful()) {
                    List<Chauffeur> chauffeurs = response.body();


                    if (chauffeurs != null) {
                        setupRecyclerView(chauffeurs); // Move this line here
                    } else {
                        Toast.makeText(AllChauffeurActivity.this, "No chauffeurs found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AllChauffeurActivity.this, "Failed to fetch chauffeurs", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Chauffeur>> call, Throwable t) {
                // Handle network errors or exceptions
                Log.e("API Request", "Failed to fetch chauffeurs", t);
                Toast.makeText(AllChauffeurActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }
    private void setupRecyclerView(List<Chauffeur> chauffeurs) {
        this.chauffeurList = chauffeurs; // Assign the chauffeurList
        adapter = new ChauffeurAdapter(chauffeurs); // Pass the chauffeurList to the adapter
        adapter.setOnUpdateClickListener(this);
        adapter.setOnDeleteClickListener(this); // Set the delete click listener

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // Handle update button click
    @Override
    public void onUpdateClick(int position) {
        if (chauffeurList != null && position < chauffeurList.size()) {
            // Get the clicked chauffeur
            Chauffeur clickedChauffeur = chauffeurList.get(position);

            // Get the ID of the clicked chauffeur
            long chauffeurId = clickedChauffeur.getId();

            // Navigate to the update activity with the chauffeur ID
            Intent intent = new Intent(AllChauffeurActivity.this, UpdateChauffeurActivity.class);
            intent.putExtra("chauffeurId", chauffeurId);
            System.out.println(("chaufrei"+chauffeurId));
            startActivity(intent);
        } else {
            Log.e("Chauffeur Click", "Invalid position or chauffeur list is null");
        }
    }

    @Override
    public void onDeleteClick(int position) {
        Chauffeur clickedChauffeur = chauffeurList.get(position);
        long chauffeurId = clickedChauffeur.getId();
        System.out.println(("chaufrei"+chauffeurId));
        System.out.println(("position"+position));



        Log.d("Chauffeur Delete", "Deleting chauffeur with ID: " + chauffeurId);

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<HashMap<String, String>> call = apiService.deleteChauffeur(chauffeurId);
        call.enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                if (response.isSuccessful()) {
                    Log.d("Chauffeur Delete", "Delete request successful");
                    HashMap<String, String> message = response.body();
                    String state = message != null ? message.get("etat") : "Unknown";
                    Toast.makeText(AllChauffeurActivity.this, "Chauffeur deleted: " + state, Toast.LENGTH_SHORT).show();

                    // Remove the deleted chauffeur from the list
                    chauffeurList.remove(position);
                    adapter.notifyItemRemoved(position);
                } else {
                    Log.e("Chauffeur Delete", "Failed to delete chauffeur: " + response.message());
                    Toast.makeText(AllChauffeurActivity.this, "Failed to delete chauffeur", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                Log.e("Chauffeur Delete", "Error deleting chauffeur: " + t.getMessage());
                Toast.makeText(AllChauffeurActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}