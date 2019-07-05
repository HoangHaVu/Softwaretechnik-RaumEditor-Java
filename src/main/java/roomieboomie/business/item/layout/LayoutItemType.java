package roomieboomie.business.item.layout;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * Typ eines LayoutItems mit abrufbarer Textur
 */
public enum LayoutItemType {
    WALL, DOOR, WINDOW;

    private String texture;

    /**
     * @return Textur des LayoutItemTypes
     */
    public String getTexture() {
<<<<<<< HEAD
=======
        //TODO gibt svg gewandelt als Pfad zurueck
>>>>>>> 065a9cf6dbe355cc8f0acd0471f766f0b6a62e23
        return texture;
    }

    public void setTexture(String filename){
        texture = svgToPath(filename);
    }

    public static String svgToPath(String filename){
        String d = "abc";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder =  factory.newDocumentBuilder();
            Document doc = builder.parse("src/main/resources/iconsandtextures/"+ filename + ".svg");
            NodeList elemente = doc.getElementsByTagName("path");
            Element element = (Element) elemente.item(0);

            return element.getAttribute("d");

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return d;
    }

}
