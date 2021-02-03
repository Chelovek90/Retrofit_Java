package ru.geekbrains;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.AfterAll;
import ru.geekbrains.db.dao.ProductsMapper;
import ru.geekbrains.db.model.Products;
import ru.geekbrains.db.model.ProductsExample;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CleanTestDataBase {
    
    static ProductsMapper productsMapper;
    
    public static ProductsMapper getProductsMapper(String resource) throws IOException {
        SqlSession session = getSqlSession(resource);
        return session.getMapper(ProductsMapper.class);
    }

    private static SqlSession getSqlSession(String resource) throws IOException {
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        return sqlSessionFactory.openSession(true);
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
