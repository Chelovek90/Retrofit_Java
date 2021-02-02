package ru.geekbrains;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.geekbrains.dto.Product;
import ru.geekbrains.dto.ProductPriceFloat;
import ru.geekbrains.dto.ProductPriceString;
import ru.geekbrains.service.ProductService;
import ru.geekbrains.util.RetrofitUtils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.geekbrains.baseEnums.Category.ELECTRONIC;

public class PostProductNegativeTest {

    static ProductService productService;
    Product productWithId;
    Product productWithNegativePrice;
    Product productNullData;
    ProductPriceString productPriceString;
    ProductPriceFloat productPriceFloat;
    Faker faker = new Faker();

    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit().create(ProductService.class);
    }

    @BeforeEach
    void setUp() {
        productWithId = new Product()
                .withId((int) (Math.random() * 1000))
                .withTitle(faker.app().name())
                .withCategoryTitle(ELECTRONIC.title)
                .withPrice((int) (Math.random() * 1000));
        productWithNegativePrice = new Product()
                .withTitle(faker.app().name())
                .withCategoryTitle(ELECTRONIC.title)
                .withPrice((int) (Math.random() * -1000));
        productNullData = new Product()
                .withTitle("")
                .withCategoryTitle("");
        productPriceString = new ProductPriceString()
                .withTitle(faker.app().name())
                .withCategoryTitle(ELECTRONIC.title)
                .withPrice(faker.app().name());
        productPriceFloat = new ProductPriceFloat()
                .withTitle(faker.app().name())
                .withCategoryTitle(ELECTRONIC.title)
                .withPrice(2.3f);
    }

    @SneakyThrows
    @Test
    void createProductInElectronicCategoryWithIdTest() {
        Response<Product> response = productService.createProduct(productWithId).execute();
        assertThat(response.code(), is(400));
    }

    @SneakyThrows
    @Test
    void createProductInElectronicCategoryWithNegativePriceTest() {
        Response<Product> response = productService.createProduct(productWithNegativePrice).execute();
        assertThat(response.isSuccessful(), is(false));

    }

    @SneakyThrows
    @Test
    void createProductNullDataNegativePriceTest() {
        Response<Product> response = productService.createProduct(productNullData).execute();
        assertThat(response.code(), is(500));
    }

    @SneakyThrows
    @Test
    void createProductPriceStringNegativePriceTest() {
        Response<ProductPriceString> response = productService.createProductPriceString(productPriceString).execute();
        assertThat(response.code(), is(400));
    }

    @SneakyThrows
    @Test
    void createProductPriceFloatNegativePriceTest() {
        Response<ProductPriceFloat> response = productService.createProductPriceFloat(productPriceFloat).execute();
        assertThat("Product created with not correct price", response.code(), is(400));
    }

}
