/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app_contorller;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Zhengyi
 */
public class XMLParser {
    private DocumentBuilderFactory data_factory;
    private DocumentBuilder data_builder;
    
    public XMLParser() {
        data_factory = DocumentBuilderFactory.newInstance();
        try {
            data_builder = data_factory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Take in the address where the XML lies and download it in a Document format
     * @param address
     * @return Document with the download file
     */
    public Object parseXML(String address) {
        Document doc = null;
        try {
            doc = data_builder.parse(address);
            doc.getDocumentElement().normalize();
        } catch (SAXException ex) {
            return null;
        } catch (IOException ex) {
            return null;            
        }
        
        return doc;
    }
    
    /**
     * 
     * @param stream
     * @return 
     */
    public Object parseXML(InputStream stream) {
        Document doc = null;
        try {
            doc = data_builder.parse(stream);
            doc.getDocumentElement().normalize();
        } catch (SAXException ex) {
            Logger.getLogger(IncidentHarvester.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException ex) {
            Logger.getLogger(IncidentHarvester.class.getName()).log(Level.SEVERE, null, ex);
            return null;            
        }
        
        return doc;
    }
    
    
    /**
     * gets the info from a Document type of object
     * @param data
     * @param node
     * @return NodeList
     */
    public NodeList getInfo(Document data, String node) {
        try {
            NodeList nl = data.getElementsByTagName(node);
            return nl;
        }
        catch (NullPointerException ex){
            return null;
        }
    }
    
    /**
     * gets the info from a Element type of object
     * @param data
     * @param node
     * @return String
     */
    public String getInfo(Element data, String node) {
        try {
            String value = data.getElementsByTagName(node).item(0).getChildNodes().item(0).getNodeValue();
            return value;
        }
        catch (NullPointerException ex) {
            return "";
        }
    }
        
    /**
     * gets the info from a Element type of object
     * @param data
     * @param node
     * @return NodeList
     */
    public NodeList getChildNodes(Element data, String node) {
        try {
            NodeList nl = data.getElementsByTagName(node);
            return nl;
        }
        catch (NullPointerException ex){
            return null;
        }
    }
}
