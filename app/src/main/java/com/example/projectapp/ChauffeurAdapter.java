package com.example.projectapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChauffeurAdapter extends RecyclerView.Adapter<ChauffeurAdapter.ChauffeurViewHolder> {
    private List<Chauffeur> chauffeurList;



    public ChauffeurAdapter(List<Chauffeur> chauffeurList) {
        this.chauffeurList = chauffeurList;
    }
    public ChauffeurAdapter() {

    }

    @NonNull
    @Override
    public ChauffeurViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chauffeur, parent, false);
        return new ChauffeurViewHolder(view);
    }

    // Listener interface for handling update click events
    public interface OnUpdateClickListener {
        void onUpdateClick(int position);
    }

    private OnUpdateClickListener updateClickListener;

    public void setOnUpdateClickListener(OnUpdateClickListener listener) {
        this.updateClickListener = listener;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }
    private OnDeleteClickListener deleteClickListener;
    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }



    @Override
    public void onBindViewHolder(@NonNull ChauffeurViewHolder holder, int position) {
        Chauffeur chauffeur = chauffeurList.get(position);
        holder.textViewName.setText(chauffeur.getFirstName() + " " + chauffeur.getLastName());
        holder.textViewEmail.setText(chauffeur.getEmail());
        holder.textViewAvailability.setText(chauffeur.isDispo() ? "Available" : "Not Available");
        // Handle update button click
        holder.buttonUpdateChauffeur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateClickListener != null) {
                    // Pass the adapter position to the click listener
                    updateClickListener.onUpdateClick(holder.getAdapterPosition());
                }
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteClickListener != null) {
                    deleteClickListener.onDeleteClick(holder.getAdapterPosition());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return chauffeurList == null ? 0 : chauffeurList.size();
    }

    public void setChauffeurs(List<Chauffeur> chauffeurs) {
        this.chauffeurList = chauffeurs;
        notifyDataSetChanged();
    }

    public class ChauffeurViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewEmail;
        private TextView textViewAvailability;
        private Button buttonUpdateChauffeur ;
        private Button btnDelete ;

        public ChauffeurViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewChauffeurName);
            textViewEmail = itemView.findViewById(R.id.textViewChauffeurEmail);
            textViewAvailability = itemView.findViewById(R.id.textViewChauffeurAvailability);
            buttonUpdateChauffeur = itemView.findViewById(R.id.buttonUpdateChauffeur);
            btnDelete = itemView.findViewById(R.id.btnDelete); // Initialize delete button
            btnDelete.setOnClickListener(v -> {
                if (deleteClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        deleteClickListener.onDeleteClick(position);
                    }
                }
            });
        }

        }
    }


