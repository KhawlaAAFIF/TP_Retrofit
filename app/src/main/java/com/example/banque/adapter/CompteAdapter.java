package com.example.banque.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banque.R;
import com.example.banque.model.Compte;

import java.util.List;

public class CompteAdapter extends RecyclerView.Adapter<CompteAdapter.ViewHolder> {

    private List<Compte> comptes;
    private OnUpdateListener updateListener;
    private OnDeleteListener deleteListener;
    private Context context;

    public interface OnUpdateListener {
        void onUpdate(Compte compte);
    }

    public interface OnDeleteListener {
        void onDelete(Long id);
    }



    public CompteAdapter(List<Compte> comptes, OnUpdateListener updateListener, OnDeleteListener deleteListener) {
        this.comptes = comptes;
        this.updateListener = updateListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_compte, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Compte compte = comptes.get(position);

        holder.idTextView.setText(String.valueOf(compte.getId()));
        holder.soldeTextView.setText(String.format("Solde: %f", compte.getSolde()));
        holder.typeTextView.setText(String.format("Type: %s", compte.getTypeCompte()));

        holder.updateButton.setOnClickListener(v -> {
            if (updateListener != null) {
                updateListener.onUpdate(compte);
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(compte.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return comptes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView idTextView;
        TextView soldeTextView;
        TextView typeTextView;
        AppCompatImageButton updateButton;
        AppCompatImageButton  deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.idTextView);
            soldeTextView = itemView.findViewById(R.id.soldeTextView);
            typeTextView = itemView.findViewById(R.id.typeTextView);
            updateButton =(AppCompatImageButton) itemView.findViewById(R.id.updateButton);
            deleteButton =(AppCompatImageButton) itemView.findViewById(R.id.deleteButton);
        }
    }
}
