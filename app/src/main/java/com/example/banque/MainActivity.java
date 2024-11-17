package com.example.banque;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.banque.adapter.CompteAdapter;
import com.example.banque.api.ApiClient;
import com.example.banque.api.CompteApi;
import com.example.banque.model.Compte;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CompteAdapter adapter;
    private Spinner formatSpinner;
    private Button addButton;
    private CompteApi compteApi;
    private String[] format = {"application/json"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind views
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        formatSpinner = findViewById(R.id.formatSpinner);
        addButton = findViewById(R.id.addButton);

        compteApi = ApiClient.getRetrofitInstance(format[0]).create(CompteApi.class);

        formatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, android.view.View selectedItemView, int position, long id) {
                format[0] = position == 0 ? "application/json" : "application/xml";
                fetchComptes();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                format[0] = "application/json";
            }
        });
        addButton.setOnClickListener(v -> showCompteDialog(null));
        fetchComptes();
    }

    private void fetchComptes() {
        Retrofit retrofitInstance = ApiClient.getRetrofitInstance(format[0]);
        CompteApi compteApi = retrofitInstance.create(CompteApi.class);

        Call<List<Compte>> call = compteApi.getAllComptes(format[0]);
        call.enqueue(new Callback<List<Compte>>() {
            @Override
            public void onResponse(Call<List<Compte>> call, Response<List<Compte>> response) {
                if (response.isSuccessful()) {
                    List<Compte> comptes = response.body();
                    adapter = new CompteAdapter(comptes, MainActivity.this::showCompteDialog, MainActivity.this::deleteCompte);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(MainActivity.this, "Erreur de récupération des comptes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Compte>> call, Throwable t) {
                Log.e("API_ERROR", "Erreur réseau: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCompteDialog(Compte compte) {
        // Create a custom dialog
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_form);

        EditText soldeEditText = dialog.findViewById(R.id.soldeEditText);
        Spinner typeCompteSpinner = dialog.findViewById(R.id.typeCompteSpinner);
        Button saveButton = dialog.findViewById(R.id.saveButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.type_compte_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeCompteSpinner.setAdapter(adapter);

        if (compte != null) {
            soldeEditText.setText(String.valueOf(compte.getSolde()));
            int spinnerPosition = adapter.getPosition(compte.getTypeCompte());
            typeCompteSpinner.setSelection(spinnerPosition);
        }

        saveButton.setOnClickListener(v -> {
            String soldeStr = soldeEditText.getText().toString();
            String selectedTypeCompte = typeCompteSpinner.getSelectedItem().toString();

            if (!soldeStr.isEmpty() && !selectedTypeCompte.isEmpty()) {
                double solde = Double.parseDouble(soldeStr);

                if (compte == null) {
                    createCompte(solde, selectedTypeCompte);
                } else {
                    updateCompte(compte.getId(), solde, selectedTypeCompte);
                }
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }


    private void createCompte(double solde, String typeCompte) {
        Compte newCompte = new Compte();
        newCompte.setSolde(solde);
        newCompte.setTypeCompte(typeCompte);

        compteApi.createCompte(newCompte, format[0]).enqueue(new Callback<Compte>() {
            @Override
            public void onResponse(Call<Compte> call, Response<Compte> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Compte créé avec succès", Toast.LENGTH_SHORT).show();
                    fetchComptes();
                } else {
                    Toast.makeText(MainActivity.this, "Erreur de création du compte", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Compte> call, Throwable t) {
                Log.e("API_ERROR", "Erreur réseau: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateCompte(Long id, double solde, String typeCompte) {
        Compte updatedCompte = new Compte();
        updatedCompte.setSolde(solde);
        updatedCompte.setTypeCompte(typeCompte);

        compteApi.updateCompte(id, updatedCompte, format[0]).enqueue(new Callback<Compte>() {
            @Override
            public void onResponse(Call<Compte> call, Response<Compte> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Compte mis à jour", Toast.LENGTH_SHORT).show();
                    fetchComptes();
                } else {
                    Toast.makeText(MainActivity.this, "Erreur de mise à jour", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Compte> call, Throwable t) {
                Log.e("API_ERROR", "Erreur réseau: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteCompte(Long id) {
        compteApi.deleteCompte(id, format[0]).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Compte supprimé", Toast.LENGTH_SHORT).show();
                    fetchComptes();
                } else {
                    Toast.makeText(MainActivity.this, "Erreur de suppression", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API_ERROR", "Erreur réseau: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
