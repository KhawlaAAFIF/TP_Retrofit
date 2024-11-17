package com.example.banque.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Compte {
    private Long id;
    private double solde;

    @SerializedName("dateCreation")
    private Date dateCreation;

    @SerializedName("typeCompte")
    private String typeCompte;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getTypeCompte() {
        return typeCompte;
    }

    public void setTypeCompte(String typeCompte) {
        this.typeCompte = typeCompte;
    }
}

