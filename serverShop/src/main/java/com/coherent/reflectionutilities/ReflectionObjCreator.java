package com.coherent.reflectionutilities;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectionObjCreator {

    public static Object createProduct(int id, String productName, double price, int stock) throws Exception {

        var classObj = Class.forName("com.coherent.common.models.Product");
        Object productReflectionObj = classObj.getConstructor().newInstance();

        //creating new object through fields
//        var fields = classObj.getDeclaredFields();
//        for (Field field:fields)
//        {
//            if (field.getName().equals("name"))
//            {
//                field.setAccessible(true);
//                field.set(productReflectionObj,productName);
//            }
//            if (field.getName().equals("price"))
//            {
//                field.setAccessible(true);
//                field.set(productReflectionObj, price);
//            }
//            if (field.getName().equals("stock"))
//            {
//                field.setAccessible(true);
//                field.set(productReflectionObj, stock);
//            }
//        }

        //creating new object through methods
        var methods = classObj.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals("setId")) {
                method.invoke(productReflectionObj, id);
            }
            if (method.getName().equals("setName")) {
                method.invoke(productReflectionObj, productName);
            }
            if (method.getName().equals("setPrice")) {
                method.invoke(productReflectionObj, price);
            }
            if (method.getName().equals("setStock")) {
                method.invoke(productReflectionObj, stock);
            }
        }

        return productReflectionObj;
    }

    public static Object createCategory(String categoryName) throws Exception {
        var classObj = Class.forName("com.coherent.common.models.Category");
        Object categoryReflectionObj = classObj.getConstructor().newInstance();

        var methods = classObj.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals("setCategoryName")) {
                method.invoke(categoryReflectionObj, categoryName);
            }
        }

        return categoryReflectionObj;
    }

    public static List<Object> bulkCreateCategoriesFromURIPath(File folder) throws Exception {
        List<Object> objectList = new ArrayList<>();
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                String name = file.getName();
                String newName = name.replaceAll(".java", "");
                var classObj = Class.forName("com.coherent.categoriesforcreation." + newName);

                var method = classObj.getMethod("getName");
                Object objForCatagoriesFolderPath = classObj.getConstructor().newInstance();
                String nameOfCategory = String.valueOf(method.invoke(objForCatagoriesFolderPath));

                Object categoryReflectionObj = createCategory(nameOfCategory);
                objectList.add(categoryReflectionObj);
            }
        }
        return objectList;
    }
}
