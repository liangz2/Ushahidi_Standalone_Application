/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app_contorller;
import app_model.Category;
import app_model.CategoryList;
import app_model.Incident;
import app_model.IncidentList;
import app_model.Location;
import app_model.MediaItem;
import app_view.Viewer;
import app_view.LoadingScreen;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Zhengyi
 */
public class IncidentHarvester implements Runnable {
    private IncidentList incidentList;
    private Viewer viewer;
    private LoadingScreen loadingScreen;
    private XMLParser parser;
    private CategoryList categoryList;
    private int incidentNumber = 0;
    private String incidentURL = "http://192.168.56.50/~jharvard/Ushahidi_Web/index.php/api?task=incidents&resp=xml";
    private final String incidentCountURL = "http://192.168.56.50/~jharvard/Ushahidi_Web/index.php/api?task=incidentcount&resp=xml";
    private final String categoryURL = "http://192.168.56.50/~jharvard/Ushahidi_Web/index.php/api?task=categories&resp=xml";
    
    public IncidentHarvester (IncidentList incidents, Viewer viewer, LoadingScreen loadingScreen) {
        this.incidentList = incidents;
        this.viewer = viewer;
        this.loadingScreen = loadingScreen;
        parser = new XMLParser();
    }

    /**
     * 
     * @param data
     * @return Category
     */
    protected Category[] getCategory(Element data) {
        Category[] categoryArray = null;
        try {
            NodeList categories = parser.getChildNodes(data, "categories");
            NodeList category = parser.getChildNodes((Element) categories.item(0), "category");
            int itemCount = category.getLength();
            categoryArray = new Category[itemCount];
            for (int i = 0; i < itemCount; i++) {
                String id = parser.getInfo((Element) category.item(i), "id");
                String title = parser.getInfo((Element) category.item(i), "title");
                categoryArray[i] = categoryList.searchCateogry(id);
                if (categoryArray[i] == null)
                    categoryArray[i] = new Category(id, title);
            }
            return categoryArray;
        }
        catch (NullPointerException ex) {
            return null;
        }
    }
    
    /**
     * 
     * @param data
     * @return Location
     */
    protected Location getLocation(Element data) {
        try {
            NodeList local = parser.getChildNodes(data, "location");
            String id = parser.getInfo((Element) local.item(0), "id");
            String name = parser.getInfo((Element) local.item(0), "name");
            double latitude = Double.parseDouble(parser.getInfo((Element) local.item(0), "latitude"));
            double longitude = Double.parseDouble(parser.getInfo((Element) local.item(0), "longitude"));

            return new Location(id, name, latitude, longitude);        
        }
        catch (NullPointerException ex) {
            return null;
        }
    }
    
    /**
     * get the media items out of the xml file
     * @param data
     * @return 
     */
    protected MediaItem[] getMediaItems(Element data) {
        NodeList items = parser.getChildNodes(data, "mediaItems");
        Element media = null;
        MediaItem[] mediaItems;
        try {
            NodeList medias = parser.getChildNodes((Element) items.item(0), "media");
            int itemCount = medias.getLength();
            mediaItems = new MediaItem[itemCount];
            for (int i = 0; i < itemCount; i++) {
                media = (Element) medias.item(i);
                String id = parser.getInfo(media, "id");
                String type = parser.getInfo(media, "type");
                String link = parser.getInfo(media, "link");
                
                mediaItems[i] = new MediaItem(id, type, link);
            }
            
            return mediaItems;
        }
        catch (NullPointerException ex) {
            return null;
        }
    }

    /**
     * get the number of incidents available
     * @return 
     */
    private String getIncidentCount() {
        String incidentCount = "";
        Object doc = null;
        if ((doc = parser.parseXML(incidentCountURL)) != null) {
            if (doc instanceof Document) {
                NodeList temp = parser.getInfo((Document) doc, "count");
                Element elem = (Element) temp.item(0);
                incidentCount = parser.getInfo(elem, "count");
                if (loadingScreen != null)
                    loadingScreen.setProgressLabel("Retrieving " + incidentCount + " Incidents...");
            }
        }
        else
            internetDown("Unable to retrieve incident count");
        
        return incidentCount;
    }
    
    /**
     * get all the information of categories
     * @return 
     */
    private CategoryList getCategories() {
        CategoryList tempList = null;
        Object doc = null;
        if ((doc = parser.parseXML(categoryURL)) != null) {
            tempList = new CategoryList();
            // get category tag
            NodeList temp = parser.getInfo((Document) doc, "category");
            // if there is multiple tags, loop through it
            for (int i = 0; i < temp.getLength(); i++) {
                Element cat = (Element) temp.item(i);
                String id = parser.getInfo(cat, "id");
                String parentId = parser.getInfo(cat, "parent_id");
                String title = parser.getInfo(cat, "title");
                String description = parser.getInfo(cat, "description");
                String color = parser.getInfo(cat, "color");
                String position = parser.getInfo(cat, "position");
                if (color.equals("ffffff"))
                    color = "00ff00";
                tempList.add(new Category(id, parentId, title, description, color, position));
            }
        }
        else 
            internetDown("Unable to retrieve categories");
        
        return sortCategoryName(tempList);
    }
    
    /**
     * display an error message and close the program
     * @param message 
     */
    private void internetDown(String message) {
        if (loadingScreen != null)
            loadingScreen.setProgressLabel(message);
        try {
            Thread.sleep(4500);
        } catch (InterruptedException ex) {
            Logger.getLogger(IncidentHarvester.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(1);
    }
    /**
     * Simple bubble sort algorithm 
     * @param categories
     * @return 
     */
    private CategoryList sortCategoryName(CategoryList categories) {
        if (categories == null)
            return null;
        Category first, second;
        int size  = categories.size();
        for (int i = 0; i < size; i++) {
        first = categories.get(i);
            for (int j = i + 1; j < size; j++) {
                second = categories.get(j);
                if (first.compareTo(second) > 0) {
                    categories.set(i, second);
                    categories.set(j, first);
                    i--;
                    break;
                }
            }
        }
        return categories;
    }
    
    /**
     * get all incidents and store them into a list
     * @param incidentCount 
     */
    protected void getIncidents(String incidentCount) {
        Object doc = null;
        incidentList.resetList();
        incidentURL = "http://192.168.56.50/~jharvard/Ushahidi_Web/index.php/api?task=incidents&limit=" + incidentCount + "&resp=xml";
        // make sure the returned object is not null
        if ((doc = parser.parseXML(incidentURL)) != null) {
            if (loadingScreen != null)
                loadingScreen.setProgressLabel("Completed, Parsing Incidents...");
            NodeList temp = parser.getInfo((Document) doc, "zhengyi_incident");
            for (int i = 0; i < temp.getLength(); i++) {
                Element incident = (Element) temp.item(i);
                String title = parser.getInfo(incident, "title");
                String id = parser.getInfo(incident, "id");
                String description = parser.getInfo(incident, "description");
                String date = parser.getInfo(incident, "date");
                boolean active = parser.getInfo(incident, "active").equals("0") ? false : true;
                boolean verified = parser.getInfo(incident, "verified").equals("0") ? false : true;
                String mode = parser.getInfo(incident, "mode");
                Location location = getLocation(incident);
                Category[] category = getCategory(incident);
                MediaItem[] medias = getMediaItems(incident);
                
                Incident newIncident = new Incident(id, title, description, date, mode, active, verified, location, category, medias);
                incidentList.addIncident(newIncident);
            }
            if (loadingScreen != null)
                loadingScreen.setProgressLabel("Parsing Completed, Displaying...");
        }
    }
    
    /**
     * get ready to display them
     */
    protected void updateIndicents() {
        String incidentCount = getIncidentCount();  // get the number of incidents
        int incidCount = Integer.parseInt(incidentCount);
        String programTitle = viewer.getProgramName();
        viewer.setTitle(programTitle + " - Checking for updates");
        // compare if there the list has changed
        if (incidentNumber != incidCount) {
            viewer.setTitle(programTitle + " - List has changed, updating");
            incidentNumber = incidCount;
            categoryList = getCategories();
            viewer.fillCategoryTitleCombo(categoryList.toArray());
            viewer.fillCategoryIdCombo(categoryList.getIdArray());

            getIncidents(incidentCount);  // initialize the incidents
            
            viewer.setDetailPanelVisible(false);
            viewer.modelChanged();
            if (loadingScreen != null)
                loadingScreen.dispose();
            
            // inform the user the process of the application
            viewer.setTitle(programTitle + " - Update completed");
            viewer.getAutoUpdateBox().setEnabled(false);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
            }
            viewer.setTitle(programTitle);
            viewer.getAutoUpdateBox().setEnabled(true);
        }
        else {
            viewer.setTitle(programTitle + " - No updates availeble");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
            }
            viewer.setTitle(programTitle);
        }
    }

    @Override
    public void run() {
        updateIndicents();
    }
}
