package com.coherent.repository;

import com.coherent.service.xmlservice.XMLParser;
import com.coherent.common.models.Catalog;
import com.coherent.common.models.Shop;
import lombok.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;

@RequiredArgsConstructor
public class ShopRepositoryXMLImpl {
    protected final Shop shop;
    private final XMLParser xmlParser;

    public Catalog getShopCatalog() {
        return shop.getCatalog();
    }

    public void saveData() throws ParserConfigurationException, TransformerException {
        {
            File xmlDatabase = new File("src/main/resources/shopDataBase.xml");
            xmlParser.saveDataToDBFileFrom(getShopCatalog(), xmlDatabase);
        }
    }
}