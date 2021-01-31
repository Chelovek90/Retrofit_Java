package ru.geekbrains;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.geekbrains.dto.Product;
import ru.geekbrains.service.ProductService;
import ru.geekbrains.util.RetrofitUtils;

import java.util.Random;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class GetProductTest {
    Random random = new Random();
    int countProducts;

    static ProductService productService;
    Product product;
    Faker faker = new Faker();

    int expectedId;
    String expectedTitle;
    int expectedPrice;
    String expectedCategoryTitle;

    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit().create(ProductService.class);
    }

    @SneakyThrows
    @BeforeEach
    public void setUp() {
        Response<Product[]> response = productService.getProduct().execute();
        assertThat(response.body().length, not(equalTo(0)));
        countProducts = response.body().length;
        int randomProduct = 1 + random.nextInt((response.body().length));
        product = response.body()[randomProduct];
        expectedId = product.getId();
        expectedTitle = product.getTitle();
        expectedPrice = product.getPrice();
        expectedCategoryTitle = product.getCategoryTitle();
    }

    @SneakyThrows
    @Test
    public void getProductByIdPositiveTest() {
        Response<Product> response = productService.getProductById(expectedId).execute();
        assertThat("Id doesn't match", response.body().getId(), equalTo(expectedId));
        assertThat("Title doesn't match", response.body().getTitle(), equalTo(expectedTitle));
        assertThat("Title doesn't match", response.body().getPrice(), equalTo(expectedPrice));
        assertThat("Category title doesn't match", response.body().getCategoryTitle(), equalTo(expectedCategoryTitle));
    }

    @SneakyThrows
    @Test
    public void getProductWithNotCorrectIdNegativeTest() {
        Response<Product> response = productService.getProductById(-1).execute();
        assertThat(response.code(), is(404));
    }


}
