package com.zjhcsoft.rule.common.util;

import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by XuanLubin on 2014/4/3. 13:12
 */
public class SqlFromXml {

    private static final String sqlDefineXmlPath = "config/sqlDefine.xml";

    private static String classPath = ClassPathUtil.getClassPath();

    public static Map<String, String> getSqlMap(String groupName) throws XPathExpressionException {
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        InputSource inputsource = new InputSource(classPath + sqlDefineXmlPath);
        NodeList list = (NodeList) xpath.evaluate("/sqlDefine/group[@name='" + groupName + "']/sql", inputsource, XPathConstants.NODESET);
        Map<String, String> sqlMap = new HashMap<>();
        for (int i = 0; i < list.getLength(); i++) {
            String name = list.item(i).getAttributes().getNamedItem("name").getNodeValue();
            String tempSql = list.item(i).getTextContent().trim().replaceAll("(\r\n|\r|\n|\n\r)", " ").replaceAll("[\\s]{2,}", " ");
            sqlMap.put(name, tempSql);
        }
        return sqlMap;
    }
}
