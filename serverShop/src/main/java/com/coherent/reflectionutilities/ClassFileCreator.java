package com.coherent.reflectionutilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ClassFileCreator
{
    public static void createCategoryClassFile(String categoryName) throws IOException {
        String className = categoryName.substring(0,1).toUpperCase() + categoryName.substring(1);
        className = className.replaceAll("\\s","");

        File file = new File("src/main/java/com/coherent/CategoriesForCreation/"+className+".java");

        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        bufferedWriter.write("package com.coherent.CategoriesForCreation;");
        bufferedWriter.write("public class "+className);
        bufferedWriter.write("{");
        bufferedWriter.write("public String name = \""+categoryName+"\";");
        bufferedWriter.write(" public String getName() {return name;}");
        bufferedWriter.write("}");
        bufferedWriter.close();
    }
}
