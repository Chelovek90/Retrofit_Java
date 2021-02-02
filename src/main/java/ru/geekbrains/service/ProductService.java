package ru.geekbrains.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import ru.geekbrains.dto.Product;
import ru.geekbrains.dto.ProductPriceFloat;
import ru.geekbrains.dto.ProductPriceString;

public interface ProductService {

    @GET("products")
    Call<Product[]> getProduct();

    @POST("products")
    Call<Product> createProduct(@Body Product createProductRequest);

    @POST("products")
    Call<ProductPriceString> createProductPriceString(@Body ProductPriceString createProductRequest);

    @POST("products")
    Call<ProductPriceFloat> createProductPriceFloat(@Body ProductPriceFloat createProductRequest);

    @PUT("products")
    Call<Product> modifyProduct(@Body Product modifyProductRequest);

    @GET("products/{id}")
    Call<Product> getProductById(@Path("id") int id);

    @DELETE("products/{id}")
    Call<ResponseBody> deleteProduct(@Path("id") int id);
}
