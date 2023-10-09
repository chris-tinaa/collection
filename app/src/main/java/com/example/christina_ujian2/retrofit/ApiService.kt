package com.example.androidstarting.retrofit

import com.example.androidstarting.model.DataOutstanding
import com.example.androidstarting.model.ResponseGetAllOutstanding
import com.example.androidstarting.model.ResponseSuccess
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @GET("all")
    fun getAll(): Call<ResponseGetAllOutstanding>

    @Multipart
    @POST("add")
    fun addData(
        @Part("nama")
        nama:RequestBody,

        @Part("alamat")
        alamat:RequestBody,

        @Part("outstanding")
        outstanding:RequestBody
    ) : Call<ResponseSuccess>

    @Multipart
    @POST("update")
    fun updateData(
        @Part("id")
        id:RequestBody,

        @Part("nama")
        nama:RequestBody,

        @Part("alamat")
        alamat:RequestBody,

        @Part("outstanding")
        outstanding:RequestBody
    ) : Call<ResponseSuccess>


    @POST("delete")
    fun deleteData(
        @Body
        outstandingItem: DataOutstanding
    ): Call<ResponseSuccess>

    @GET("all")
    fun getAllDataByFilter(
        @Query("filters[0][co][2][fl]")
        filterField: String = "nama",

        @Query("filters[0][co][2][op]")
        filterOperator: String = "equal",

        @Query("filters[0][co][2][vl]")
        filterValue: String,

        @Query("sort_order")
        sortOrder: String = "ASC"
    ) : Call <ResponseGetAllOutstanding>


}
