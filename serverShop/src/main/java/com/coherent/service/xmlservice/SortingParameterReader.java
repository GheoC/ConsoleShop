package com.coherent.service.xmlservice;

import com.coherent.service.sortingservices.SortingParameter;
import lombok.RequiredArgsConstructor;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
@RequiredArgsConstructor
public class SortingParameterReader {
    private final File sortingConfigFile;

    public List<SortingParameter> readParametersConfigFile() throws ParserConfigurationException, IOException, SAXException {
        Document document = getDocumentFrom(sortingConfigFile);
        List<SortingParameter> parameters = new ArrayList<>();
        NodeList configParameters = document.getElementsByTagName("parameter");

        return IntStream.range(0, configParameters.getLength())
                .mapToObj(configParameters::item)
                .map(item->
                {
                    String parameterField = item.getAttributes().getNamedItem("parameterField").getNodeValue();
                    String strategy = item.getAttributes().getNamedItem("strategy").getNodeValue();
                    SortingParameter sortingParameter = new SortingParameter(parameterField, strategy);
                    return sortingParameter;
                })
                .collect(Collectors.toList());
    }

    private Document getDocumentFrom(File file) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(file);
        return document;
    }
}
