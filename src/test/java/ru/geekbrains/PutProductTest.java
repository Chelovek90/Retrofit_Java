package ru.geekbrains;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.AfterEach;
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
import static ru.geekbrains.baseEnums.Category.ELECTRONIC;
import static ru.geekbrains.baseEnums.Category.FOOD;

public class PutProductTest {

    static ProductService productService;
    Product product;
    Random random = new Random();
    Product modifyProduct;
    Product modifyNotCorrectIdProduct;
    Product modifyWithoutDataProduct;
    Faker faker = new Faker();
    int id;

    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit().create(ProductService.class);
    }

    @SneakyThrows
    @BeforeEach
    void setUp() {
        product = new Product()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle(FOOD.title)
                .withPrice((int) (Math.random() * 1000));
        productService.createProduct(product);
        Response<Product> response = productService.createProduct(product).execute();
        id = response.body().getId();

        modifyProduct = new Product()
                .withId(id)
                .withTitle(faker.food().ingredient())
                .withCategoryTitle(ELECTRONIC.title)
                .withPrice((int) (Math.random() * 1000));

        modifyNotCorrectIdProduct = new Product()
                .withId(-1)
                .withTitle(faker.food().ingredient())
                .withCategoryTitle(ELECTRONIC.title)
                .withPrice((int) (Math.random() * 1000));

        modifyWithoutDataProduct = new Product()
                .withId(id);
    }

    @SneakyThrows
    @Test
    void modifyProductPositiveTest() {
        Response<Product> response = productService.modifyProduct(modifyProduct).execute();
        assertThat(response.isSuccessful(), is(true));
    }

    @SneakyThrows
    @Test
    void modifyProductNotIdNegativeTest() {
        Response<Product> response = productService.modifyProduct(product).execute();
        assertThat(response.isSuccessful(), is(false));
    }

    @SneakyThrows
    @Test
    void modifyProductNotCorrectIdNegativeTest() {
        Response<Product> response = productService.modifyProduct(modifyNotCorrectIdProduct).execute();
        assertThat(response.code(), is(400));
    }

    @SneakyThrows
    @Test
    void modifyProductWithoutDataNegativeTest() {
        Response<Product> response = productService.modifyProduct(modifyWithoutDataProduct).execute();
        assertThat(response.code(), is(500));
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        Response<ResponseBody> response = productService.deleteProduct(id).execute();
        assertThat(response.isSuccessful(), is(true));
    }
}
