package com.example.banque.api;

import com.example.banque.model.Compte;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CompteApi {
    @GET("/banque/comptes")
    Call<List<Compte>> getAllComptes(@Header("Accept") String acceptHeader);

    @GET("/banque/comptes/{id}")
    Call<Compte> getCompteById(@Path("id") Long id, @Header("Accept") String acceptHeader);

    @POST("/banque/comptes")
    Call<Compte> createCompte(@Body Compte compte, @Header("Accept") String acceptHeader);

    @PUT("/banque/comptes/{id}")
    Call<Compte> updateCompte(@Path("id") Long id, @Body Compte compte, @Header("Accept") String acceptHeader);

    @DELETE("/banque/comptes/{id}")
    Call<Void> deleteCompte(@Path("id") Long id, @Header("Accept") String acceptHeader);
}
