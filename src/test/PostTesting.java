/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import app_contorller.XMLParser;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Zhengyi
 */
public class PostTesting {
    public static void doSubmit(String url, HashMap<String, String> data) throws Exception {
            URL siteUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) siteUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            DataOutputStream out = new DataOutputStream(conn.getOutputStream());

            Set keys = data.keySet();
            Iterator keyIter = keys.iterator();
            String content = "";
            for(int i=0; keyIter.hasNext(); i++) {
                    Object key = keyIter.next();
                    if(i!=0) {
                            content += "&";
                    }
                    content += key + "=" + URLEncoder.encode(data.get(key), "UTF-8");
            }
            System.out.println(content);
            out.writeBytes(content);
            out.flush();
            out.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            while((line=in.readLine())!=null) {
                    System.out.println(line);
            }
            in.close();
	}
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        HashMap<String, String> data = new HashMap<String, String>();
//        data.put("task", "report");
//        data.put("incident_title", "test");
//        data.put("incident_description", "API");
//        data.put("incident_date", "07/08/2010");
//        data.put("incident_hour", "01");
//        data.put("incident_minute", "01");
//        data.put("incident_ampm", "am");
//        data.put("incident_category", "15");
//        data.put("latitude", "53.1072");
//        try {
            // Send data
        try {
            // Construct data
            String data = "task=report&incident_title=Testing API&incident_description=API testing testing&incident_date=27/11/2010&"
                    + "incident_hour=1&incident_minute=1&incident_ampm=am&incident_category=55&latitude=53.1072&"
                    + "longitude=-1.3184&location_name=acca&resp=xml";
            XMLParser parser = new XMLParser();
            // Send data
            URL url = new URL("http://192.168.56.50/~jharvard/Ushahidi_Web/index.php/api");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            // Get the response
//            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String line;
//            while ((line = rd.readLine()) != null) {
//                System.out.println(line);
//            }
            Document response = (Document) parser.parseXML(conn.getInputStream());
            NodeList list = parser.getInfo(response, "success");
            NodeList errorMess = parser.getInfo(response, "message");
            System.out.println(list.item(0).getChildNodes().item(0).getNodeValue());
            System.out.println(errorMess.item(0).getChildNodes().item(0).getNodeValue());
            wr.close();
//            rd.close();
        } catch (IOException ex) {
        }        
    }
    /*<?xml version="1.0" encoding="UTF-8"?>
<response><payload><domain>http://192.168.56.50/~jharvard/Ushahidi_Web/</domain><success>true</success></payload><error><code>0</code><message>No Error</message></error></response>*/
}
