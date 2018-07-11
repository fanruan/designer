package com.fr.design.extra.ucenter;

import com.fr.base.FRContext;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;

/**
 * @author lp
 * @date 2016/9/9
 */
public class XMLHelper {

    public static LinkedList<String> ucUnserialize(String input) {

        LinkedList<String> result = new LinkedList<String>();
        DOMParser parser = new DOMParser();
        try {
            parser.parse(new InputSource(new StringReader(input)));
            Document doc = parser.getDocument();
            NodeList nl = doc.getChildNodes().item(0).getChildNodes();
            int length = nl.getLength();
            for (int i = 0; i < length; i++) {
                if (nl.item(i).getNodeType() == Document.ELEMENT_NODE) {
                    result.add(nl.item(i).getTextContent());
                }
            }
        } catch (SAXException e) {
            FRContext.getLogger().info(e.getMessage());
        } catch (IOException e1) {
            FRContext.getLogger().info(e1.getLocalizedMessage());
        }
        return result;
    }
}