package com.coherent.repository;

import com.coherent.common.models.Catalog;
import com.coherent.common.models.Category;
import com.coherent.common.models.Product;
import com.coherent.repository.databaseservice.DBConnector;
import org.junit.jupiter.api.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ShopRepositoryH2ImplTest {
    private DBConnector dbConnector = new DBConnector("jdbc:h2:./src/main/resources/Database/MyShopForTesting");
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;

    @BeforeAll
    static void setUp() throws SQLException, FileNotFoundException {
        String jdbcURLForTest = "jdbc:h2:" + "./src/main/resources/Database/MyShopForTesting";
        DBConnector dbConnector = new DBConnector(jdbcURLForTest);
        Connection connection = DriverManager.getConnection(jdbcURLForTest, "sa", "1234");
        Statement statement = connection.createStatement();
        statement.executeUpdate("create table catalogs (id int primary key auto_increment, catalog_name varchar(50))");
        statement.executeUpdate("create table categories " +
                "(id int primary key auto_increment, " +
                "category_name varchar(50), " +
                "catalog_id int, " +
                "constraint fk_categories_catalog foreign key (catalog_id) references catalogs(id))");
        statement.executeUpdate("create table products " +
                "(id int primary key auto_increment, " +
                "product_name varchar(50), " +
                "price double, " +
                "stock int, " +
                "category_id int, " +
                "constraint fk_products_catagory foreign key (category_id) references categories(id))");
        statement.executeUpdate("insert into catalogs (catalog_name) values ('MyShop')");
        statement.executeUpdate("insert into categories (category_name, catalog_id) " +
                "values " +
                "('groceries', 1), " +
                "('electronics',1), " +
                "('toys',1), " +
                "('cosmetics',1), " +
                "('beverages',1)");
        statement.executeUpdate("insert into products (product_name, price, stock, category_id) " +
                "values " +
                "('carrot', 15, 33,1), " +
                "('bananas', 33, 50,1), " +
                "('ananas', 25, 30,1), " +
                "('apple', 25, 30,1), " +
                "('onion', 12, 40,1), " +
                "('oranges', 30, 21,1), " +
                "('potatoes', 18, 50,1), " +
                "('train', 80, 20,3), " +
                "('car', 80, 15,3), " +
                "('lego', 50, 30,3), " +
                "('dinosaur', 80, 30,3), " +
                "('teddyBear', 50,30,3), " +
                "('barbi', 20,90,3), " +
                "('Coca Cola', 5, 105,5), " +
                "('Pepsi', 4.5, 99,5), " +
                "('fanta', 5, 80,5), " +
                "('stella artois', 9, 50,5)");
        connection.close();
    }

    @AfterAll
    static void tearDown() {
        File file = new File("./src/main/resources/Database/MyShopForTesting.mv.db");
        File file2 = new File("./src/main/resources/Database/MyShopForTesting.trace.db");
        file.deleteOnExit();
        file2.deleteOnExit();
        System.out.println("Testes finished");
    }

    @BeforeEach
    void beforeEach() {
        categoryRepository = new CategoryRepositoryH2Impl(dbConnector);
        productRepository = new ProductRepositoryH2Impl(dbConnector);
    }

    @Test
    @DisplayName("Testing the .getCatalog() method")
    @Order(1)
    void getShopCatalog() throws SQLException, FileNotFoundException {
        Catalog catalog = new Catalog();
        assertEquals(catalog.getCategories().size(), 0);

        catalog = productRepository.getShopCatalog();

        int numberOfCategoriesExpected = catalog.getCategories().size();
        assertEquals(numberOfCategoriesExpected, 5);

        int numberOfProducts = catalog.getCategories().get(0).getProducts().size();
        assertEquals(numberOfProducts, 7);
    }

    @Test
    @DisplayName("Add new Product that doesn't exist in Shop; Happy Flow")
    @Order(2)
    void addNewProductThatDoesNotExistsInDatabase() throws SQLException, ParserConfigurationException, TransformerException {
        Product product = new Product();
        product.setName("laptop");
        product.setPrice(999);
        product.setStock(10);

        Optional<Product> productByName = productRepository.findProductByName(product.getName());
        assertEquals(false, productByName.isPresent());

        productRepository.addProductsToCategory(product, "electronics");
        productByName = productRepository.findProductByName(product.getName());
        assertEquals(true, productByName.isPresent());
        assertEquals("laptop", productByName.get().getName());
        assertEquals(999, productByName.get().getPrice());
        assertEquals(10, productByName.get().getStock());
    }

    @Test
    @DisplayName("Product exist in Database, but in a different category!")
    @Order(3)
    void addProductThatExistsInDatabaseButOnDifferentCategory() throws SQLException {
        Product product = new Product();
        product.setName("ananas");
        product.setPrice(25);
        product.setStock(10);

        assertThrows(
                RuntimeException.class,
                () -> productRepository.addProductsToCategory(product, "electronics"),
                "Product exist in Database, but in a different category!");
    }

    @Test
    @DisplayName("Product is present in DB => we update the price and increase the stock")
    @Order(4)
    void updatingPriceAndStockForExistingProduct() throws SQLException, ParserConfigurationException, TransformerException {
        Optional<Product> existingProduct = productRepository.findProductByName("dinosaur");
        assertEquals(existingProduct.get().getPrice(), 80);
        assertEquals(existingProduct.get().getStock(), 30);

        Product product = new Product();
        product.setName("dinosaur");
        product.setPrice(90);
        product.setStock(7);

        productRepository.addProductsToCategory(product, "toys");

        Optional<Product> updatedProduct = productRepository.findProductByName("dinosaur");
        assertEquals(updatedProduct.get().getPrice(), 90);
        assertEquals(updatedProduct.get().getStock(), 37);
    }

    @Test
    @DisplayName("Trying to remove a product that is not present in Database")
    @Order(5)
    void tryingToRemoveAProductThatIsNotPresentInDatabase() {
        assertThrows(
                RuntimeException.class,
                () -> productRepository.removeProduct("something"),
                "Product something is not present in Database");
    }

    @Test
    @DisplayName("Removing a product; Happy Flow")
    @Order(6)
    void removeProductHappyFlow() throws SQLException, ParserConfigurationException, TransformerException {
        Optional<Product> legoProduct = productRepository.findProductByName("lego");
        assertEquals("lego", legoProduct.get().getName());

        productRepository.removeProduct("lego");

        Optional<Product> removedLegoProduct = productRepository.findProductByName("lego");
        assertEquals(false, removedLegoProduct.isPresent());
    }

    @Test
    @DisplayName("Add an existing category to shop; we expect Runtime Exception throw")
    @Order(7)
    void addAnAlreadyExistingCategory() throws SQLException {
        Category category = new Category();
        category.setCategoryName("cosmetics");

        assertThrows(
                RuntimeException.class,
                () -> categoryRepository.addCategory(category),
                "cosmetics is already present in DB");
    }

    @Test
    @DisplayName("Add a category to shop; Happy Flow")
    @Order(8)
    void addCategoryHappyFlow() throws SQLException, ParserConfigurationException, TransformerException {
        Category category = new Category();
        category.setCategoryName("food");

        Optional<Category> foodCategory = categoryRepository.findCategoryByName("food");
        assertEquals(false, foodCategory.isPresent());

        categoryRepository.addCategory(category);

        foodCategory = categoryRepository.findCategoryByName("food");
        assertEquals(true, foodCategory.isPresent());
        assertEquals("food", foodCategory.get().getCategoryName());
    }

    @Test
    @DisplayName("Trying to remove a category that is not present in Database")
    @Order(9)
    void tryingToRemoveACategoryThatIsNotPresentInDatabase() {
        assertThrows(
                RuntimeException.class,
                () -> categoryRepository.removeCategory("missingCategory"),
                "Category missingCategory doesn't exist in database");
    }

    @Test
    @DisplayName("Removing a category; Happy Flow")
    @Order(10)
    void removeCategoryHappyFlow() throws SQLException, ParserConfigurationException, TransformerException {
        Catalog catalog = categoryRepository.getShopCatalog();

        Optional<Category> beverages = categoryRepository.findCategoryByName("beverages");
        assertEquals(true, beverages.isPresent());
        assertEquals("beverages", beverages.get().getCategoryName());
        assertEquals(4, catalog.getCategories().get(4).getProducts().size());

        categoryRepository.removeCategory("beverages");

        beverages = categoryRepository.findCategoryByName("beverages");
        Optional<Product> cocaCola = productRepository.findProductByName("Coca Cola");
        Optional<Product> pepsi = productRepository.findProductByName("Pepsi");
        Optional<Product> fanta = productRepository.findProductByName("fanta");
        Optional<Product> stellaArtois = productRepository.findProductByName("stella artois");
        assertEquals(false, beverages.isPresent());
        assertEquals(false, cocaCola.isPresent());
        assertEquals(false, pepsi.isPresent());
        assertEquals(false, fanta.isPresent());
        assertEquals(false, stellaArtois.isPresent());
    }
}