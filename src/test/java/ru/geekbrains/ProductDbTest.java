package ru.geekbrains;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.geekbrains.baseEnums.Category;
import ru.geekbrains.db.dao.CategoriesMapper;
import ru.geekbrains.db.dao.ProductsMapper;
import ru.geekbrains.db.model.Products;
import ru.geekbrains.db.model.ProductsExample;
import ru.geekbrains.dto.Product;
import ru.geekbrains.service.ProductService;
import ru.geekbrains.util.RetrofitUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Slf4j
public class ProductDbTest extends CleanTestDataBase{
    Integer productId;
    static ProductsMapper productsMapper;
    static ProductService productService;
    static ProductsExample example;
    private Product product;
    Faker faker = new Faker();
    private static String resource = "mybatis-config.xml";

    public static CategoriesMapper getCategoryMapper(String resource) throws IOException{
        SqlSession session = getSqlSession(resource);
        return session.getMapper(CategoriesMapper.class);
    }

    public static ProductsMapper getProductsMapper(String resource) throws IOException{
        SqlSession session = getSqlSession(resource);
        return session.getMapper(ProductsMapper.class);
    }

    private static SqlSession getSqlSession(String resource) throws IOException {
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        return sqlSessionFactory.openSession(true);
    }


    @BeforeAll
    static void beforeAll() throws IOException {
        productsMapper = getProductsMapper(resource);
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);
        example = new ProductsExample();
    }

    @BeforeEach
    @SneakyThrows
    void setUp() {
        String title = "Test_" + faker.food().ingredient();
        product = new Product()
                .withCategoryTitle(Category.FOOD.title)
                .withTitle(title)
                .withPrice((int) (Math.random() * 10000));
        productId = Objects.requireNonNull(
                productService.createProduct(product)
                        .execute()
                        .body())
                .getId();
    }

    @Test
    void deleteProductThroughDbTest() {
        Integer sizeBefore = productsMapper.selectByExample(new ProductsExample()).size();
        productsMapper.deleteByPrimaryKey(productId.longValue());
        assertThat(productsMapper.selectByExample(new ProductsExample()).size(), equalTo(sizeBefore-1));
    }

    @AfterAll
    static void deleteTestProductThroughDbTest() {
        ProductsExample example = new ProductsExample();
        example.createCriteria().andTitleLike("Test%");
        List<Products> productsList = productsMapper.selectByExample(example);
        productsList.forEach(e-> System.out.println(e.getTitle()));
        productsMapper.deleteByExample(example);
    }
}
