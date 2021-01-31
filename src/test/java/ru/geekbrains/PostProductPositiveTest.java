package ru.geekbrains;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.*;
import retrofit2.Response;
import ru.geekbrains.dto.Product;
import ru.geekbrains.service.ProductService;
import ru.geekbrains.util.RetrofitUtils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.geekbrains.baseEnums.Category.ELECTRONIC;
import static ru.geekbrains.baseEnums.Category.FOOD;

public class PostProductPositiveTest {

    static ProductService productService;
    Product product;
    Faker faker = new Faker();
    int id;

    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit().create(ProductService.class);
    }

    @BeforeEach
    void setUp() {
        product = new Product()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle(FOOD.title)
                .withPrice((int) (Math.random() * 1000));
    }

    @SneakyThrows
    @Test
    void createProductInFoodCategoryPositiveTest() {
        Response<Product> response = productService.createProduct(product).execute();
        id = response.body().getId();
        assertThat(response.isSuccessful(), is(true));
    }


    @SneakyThrows
    @AfterEach
    void tearDown() {
        Response<ResponseBody> response = productService.deleteProduct(id).execute();
        assertThat(response.isSuccessful(), is(true));
    }
}
