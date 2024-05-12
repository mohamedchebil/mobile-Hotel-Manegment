package com.example.projectapp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("auth/login")
    Call<ResponseBody> checkUser (@Body User user);

    @GET("admin/UserInfo")
    Call<User> getUserInfo( @Query("id") Long id);


    @PUT("admin/user/{id}")
    Call<User> updateUser(@Path("id") Long id, @Body User updatedUser);


    @GET("admin/allchauffeur")
    Call<List<Chauffeur>> getAllChauffeurs();

    @PUT("admin/updatechauffeur/{id}")
    Call<Chauffeur> updateChauffeur(@Path("id") long id, @Body Chauffeur chauffeur);


        @GET("admin/getonechauffeur/{id}")
        Call<Chauffeur> getChauffeurById(@Path("id") long id);

    @DELETE("admin/deletechauffeur/{id}")
    Call<HashMap<String, String>> deleteChauffeur(@Path("id") long id);

    @GET("admin/stats/counts")
    Call<Map<String, Long>> getStatisticsCounts();

    @GET("admin/stats/most-saved-client-adrs")
    Call<List<Object[]>> getMostSavedAddresses();

    @GET("admin/stats/dept-most-employees")
    Call<List<Object[]>> getDepartmentsByMostEmployees();

}


