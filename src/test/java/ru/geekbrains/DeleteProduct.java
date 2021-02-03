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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.geekbrains.baseEnums.Category.FOOD;

public class DeleteProduct extends CleanTestDataBase {
    static ProductService productService;
    Product product;
    Faker faker = new Faker();
    int id;

    @SneakyThrows
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
        Response<Product> response = productService.createProduct(product).execute();
        id = response.body().getId();
    }

    @SneakyThrows
    @Test
    void deleteProductNotCorrectId() {
        Response<ResponseBody> response = productService.deleteProduct(26).execute();
        assertThat(response.code(), is(500));
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        Response<ResponseBody> response = productService.deleteProduct(id).execute();
        assertThat(response.isSuccessful(), is(true));
    }
}
