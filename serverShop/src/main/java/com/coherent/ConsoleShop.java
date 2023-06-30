package com.coherent;

import com.coherent.common.models.Shop;
import com.coherent.menu.Menu;
import com.coherent.repository.*;
import com.coherent.repository.databaseservice.DBConnector;
import com.coherent.service.OrderService;
import com.coherent.service.xmlservice.SortingParameterReader;
import com.coherent.service.CategoryService;
import com.coherent.service.ProductService;
import com.coherent.service.xmlservice.XMLParser;
import com.coherent.service.sortingservices.ProductSortingService;
import com.coherent.views.CategoryView;
import com.coherent.views.OrderView;
import com.coherent.views.ProductView;

import java.io.File;

public class ConsoleShop {

    public static void main(String[] args) throws Exception {

        File xmlDatabasePath = new File("src/main/resources/shopDataBase.xml");
        File CategoriesForCreationFolder = new File("src/main/java/com/coherent/categoriesForCreation");
        File sortingConfigFile = new File("src/main/resources/parametersConfig.xml");
        String jdbcURL = "jdbc:h2:" + "./src/main/resources/Database/MyShop";
        DBConnector dbConnector = new DBConnector(jdbcURL);
        XMLParser xmlParser = new XMLParser();
        Shop shop = xmlParser.loadDataToShop(xmlDatabasePath, CategoriesForCreationFolder);

//        ProductRepository productRepository = new ProductRepositoryXMLImpl(shop, xmlParser);
//        CategoryRepository categoryRepository = new CategoryRepositoryXMLImpl(shop, xmlParser);
        ProductRepository productRepository = new ProductRepositoryH2Impl(dbConnector);
        CategoryRepository categoryRepository = new CategoryRepositoryH2Impl(dbConnector);
        OrderRepository orderRepository= new OrderRepositoryH2Impl(dbConnector);

        ProductService productService = new ProductService(productRepository);
        SortingParameterReader sortingParameterReader = new SortingParameterReader(sortingConfigFile);
        ProductSortingService productSortingService = new ProductSortingService(productRepository);
        CategoryService categoryService = new CategoryService(categoryRepository, productSortingService, sortingParameterReader);
        OrderService orderService = new OrderService(orderRepository);

        CategoryView categoryView = new CategoryView(categoryService);
        ProductView productView = new ProductView(productService);
        OrderView orderView = new OrderView(productService, orderService);

        new Menu(productService,
                categoryService,
                categoryView,
                productView,
                orderView).run();
    }
}
