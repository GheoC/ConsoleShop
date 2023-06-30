package com.coherent.service.xmlservice;

import com.coherent.common.models.Catalog;
import com.coherent.common.models.Category;
import com.coherent.common.models.Product;
import com.coherent.common.models.Shop;
import com.coherent.reflectionutilities.ReflectionObjCreator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class XMLParser {
    public Shop loadDataToShop(File file, File categoryFolder) throws Exception {
        Shop shop = new Shop();

        if (isDatabaseFileEmpty(file)) {
            Catalog catalog = new Catalog();
            catalog.setCatalogId(1);
            catalog.setCatalogName("MyShop");
            shop.addCatalog(catalog);
            return null;
        }
        Catalog catalog = new Catalog();
        Document document = getDocumentFrom(file);

        NodeList catalogNodeList = document.getElementsByTagName("catalog");
        int catalogId = Integer.parseInt(catalogNodeList.item(0).getAttributes().getNamedItem("id").getNodeValue());
        String catalogName = catalogNodeList.item(0).getAttributes().getNamedItem("name").getNodeValue();

        catalog.setCatalogId(catalogId);
        catalog.setCatalogName(catalogName);

        Element catalogElement = (Element) catalogNodeList.item(0);
        NodeList categoriesNodeList = catalogElement.getElementsByTagName("category");

        List<Category> categories =
                IntStream.range(0, categoriesNodeList.getLength())
                        .boxed()
                        .map(categoriesNodeList::item)
                        .map(categoryNode ->
                        {
                            int categoryId = Integer.parseInt(categoryNode.getAttributes().getNamedItem("id").getNodeValue());
                            String categoryName = categoryNode.getAttributes().getNamedItem("name").getNodeValue();
                            Category category = new Category();
                            category.setCategoryId(categoryId);
                            category.setCategoryName(categoryName);
                            Element categoryElement = (Element) categoryNode;
                            NodeList productNodeList = categoryElement.getElementsByTagName("product");

                            List<Product> products =
                                    IntStream.range(0, productNodeList.getLength())
                                            .boxed()
                                            .map(productNodeList::item)
                                            .map(productNode ->
                                            {
                                                int id = Integer.parseInt(productNode.getAttributes().getNamedItem("id").getNodeValue());
                                                String productName = productNode.getAttributes().getNamedItem("name").getNodeValue();
                                                double productPrice = Double.parseDouble(productNode.getAttributes().getNamedItem("price").getNodeValue());
                                                int productStock = Integer.parseInt(productNode.getAttributes().getNamedItem("stock").getNodeValue());

                                                Product product = new Product(id, productName, productPrice, productStock);
                                                return product;

                                            }).collect(Collectors.toList());
                            category.addProductsToCategory(products);
                            return category;
                        }).collect(Collectors.toList());
        catalog.addCategoriesToCatalog(categories);
        shop.addCatalog(catalog);

        addCategoriesFromFolderToTheShop(shop,categoryFolder);

        return shop;
    }

    public void saveDataToDBFileFrom(Catalog catalog, File file) throws TransformerException, ParserConfigurationException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        Element rootTag = document.createElement("catalog");
        rootTag.setAttribute("id", String.valueOf(catalog.getCatalogId()));
        rootTag.setAttribute("name", catalog.getCatalogName());

        List<Category> categoryList = catalog.getCategories();
        for (Category category : categoryList) {
            Element categoryLevel = document.createElement("category");
            categoryLevel.setAttribute("id", String.valueOf(category.getCategoryId()));
            categoryLevel.setAttribute("name", category.getCategoryName());

            List<Product> productList = category.getProducts();
            for (Product product : productList) {
                Element productLevel = document.createElement("product");
                productLevel.setAttribute("id", String.valueOf(product.getId()));
                productLevel.setAttribute("name", product.getName());
                productLevel.setAttribute("price", String.valueOf(product.getPrice()));
                productLevel.setAttribute("stock", String.valueOf(product.getStock()));

                categoryLevel.appendChild(productLevel);
            }
            rootTag.appendChild(categoryLevel);
        }
        document.appendChild(rootTag);

        writeFromDocumentIntoDatabase(document, file);
    }

    public void addCategoriesFromFolderToTheShop(Shop shop, File folderPath) throws Exception {
        //Creating categories by scanning the folder: CategoriesForCreation
        for (Object category : ReflectionObjCreator.bulkCreateCategoriesFromURIPath(folderPath)) {
            String nameOfCategory = ((Category) category).getCategoryName();
            if (!shop.getCatalog().hasCategory(nameOfCategory)) {
                shop.getCatalog().addCategoryToCatalog((Category) category);
            }
        }
    }
    private static boolean isDatabaseFileEmpty(File file) {
        if (file.length() == 0) {
            return true;
        }
        return false;
    }

    private static Document getDocumentFrom(File file) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(file);
        document.getDocumentElement();
        return document;
    }

    private static void writeFromDocumentIntoDatabase(Document document, File file) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(file);
        transformer.transform(domSource, streamResult);
    }
}
