package ru.geekbrains;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;
import ru.geekbrains.db.dao.CategoriesMapper;
import ru.geekbrains.db.dao.ProductsMapper;
import ru.geekbrains.db.model.CategoriesExample;
import ru.geekbrains.db.model.Products;
import ru.geekbrains.db.model.ProductsExample;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
public class DBMethods {

    private static String resource = "mybatis-config.xml";


    public static void main(String[] args) {
        try {
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            SqlSession session = sqlSessionFactory.openSession(true);


            CategoriesMapper categoriesMapper = session.getMapper(CategoriesMapper.class);

            log.info("categories number {}", categoriesMapper.countByExample(new CategoriesExample()));

            ProductsMapper productsMapper = session.getMapper(ProductsMapper.class);
            Products selectedProduct = productsMapper.selectByPrimaryKey(3L);
//            log.info("product with id 3 - {}", selectedProduct.getTitle());
////
////            log.info("product price id 6 - {}", productsMapper.selectByPrimaryKey(6L).getPrice());
////            Products productId6 = productsMapper.selectByPrimaryKey(6L);
////
////            Products newProduct = new Products();
////            newProduct.setPrice(3232323);
////            newProduct.setId(6L);
////            newProduct.setCategory_id(1L);
////            newProduct.setTitle("Lemon");
////
////            ProductsExample example = new ProductsExample();
////
////            example.createCriteria().andIdEqualTo(6L);
////
////            productsMapper.updateByExample(productId6, example);
////
////            log.info("product price id 6 - {}", productsMapper.selectByPrimaryKey(6L).getPrice());

            productsMapper.selectByExample(new ProductsExample()).forEach(e -> System.out.println(e.getTitle()));
            System.out.println("*******************");
            ProductsExample example = new ProductsExample();
            example.createCriteria().andTitleLike("Test%");
            List<Products> productsList = productsMapper.selectByExample(example);
            productsList.forEach(e-> System.out.println(e.getTitle()));
            System.out.println("*******************");
            productsMapper.deleteByExample(example);
            productsMapper.selectByExample(new ProductsExample()).forEach(e -> System.out.println(e.getTitle()));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
