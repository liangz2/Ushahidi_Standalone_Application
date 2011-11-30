/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app_contorller;

import app_model.Incident;
import app_model.IncidentList;
import app_view.Viewer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;

/**
 *
 * @author Zhengyi
 */
public class TitlelinkListener implements HyperlinkListener {
    private Viewer viewer;
    private int selectedIndex = -1;
    private int oldIndex = -1;
    private IncidentList incidentList;
    private Incident incident;
    private double latitude;
    private double longitude;
    private MapMarkerDot marker;
    private JMapViewer viewMap;
    private HTMLDocument htmlDoc;
    private HTMLEditorKit htmlEditor;
    private Element select;
    private String originalText = "";   // the original text of the selected item before being clicked
    private int previousItemStart;      // the start offset of the privious selected item
    private int previousItemEnd;        // the end offset of the privious selected item
    private int previousPage;       // page number
    private boolean isSelected;     // determining if any title is selected
    
    public TitlelinkListener(IncidentList incidentList, Viewer viewer) {
        this.viewer = viewer;
        viewMap = viewer.getViewMap();
        this.incidentList = incidentList;
        htmlDoc = (HTMLDocument) viewer.getTitleDocument();
        htmlEditor = (HTMLEditorKit) viewer.getTitleEditorKit();
        isSelected = false;
    }
    
    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        try {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                selectedIndex = Integer.parseInt(e.getDescription()); // get the index of the incidents in the arraylist
                
                int currentPage = incidentList.getCurrentPage(); // get the current page number
                
                if (selectedIndex != oldIndex && select != null && previousPage == currentPage && isSelected) {
                    int length = previousItemEnd - previousItemStart;
                    String normal = "";
                    normal = "<font size = '4'><a href = '" + oldIndex + "'>"
                                                + originalText + "</a></font>";
                    htmlDoc.remove(previousItemStart, length + 1);
                    htmlEditor.insertHTML(htmlDoc, previousItemStart, normal, 0, 0, null);
                } // change back to how it looked like before
                
                select = e.getSourceElement();
                int start = select.getStartOffset();
                int end = select.getEndOffset();
                int length = end - start;
                String title = "";
                // variables
                
                
                // select and deselect titles
                if (selectedIndex == oldIndex && isSelected) {
                    viewer.setDetailPanelVisible(false);
                    title = "<font size = '4'><a href = '" + selectedIndex + "'>"
                                            + select.getDocument().getText(start, length) + "</a></font>";
                    viewer.updateDetailPane(-1);
                    isSelected = false;
                    
                }
                else {
                    viewer.setDetailPanelVisible(true);
                    isSelected = true;
                    previousPage = incidentList.getCurrentPage();
                    incidentList.setSelectedIndex(selectedIndex);
                    oldIndex = selectedIndex;
                    previousItemStart = start;
                    previousItemEnd = end;
                    originalText = select.getDocument().getText(start, length);
                    title = "<font size = '4' color = 'blue'><b><a href = '" + selectedIndex + "'>"
                                                + originalText + "</a></b></font>";
                    viewer.updateDetailPane(selectedIndex);
                    
                    // get the selected incident
                    incident = incidentList.getIncident(selectedIndex);
                    
                    // obtain location
                    latitude = incident.getLocation().getLatitude();
                    longitude = incident.getLocation().getLongitude();
                    
                    // delete the previous marker
                    if (marker != null)
                        viewMap.removeMapMarker(marker);
                    
                    // add the current marker in
                    marker = new MapMarkerDot(latitude, longitude);
                    viewMap.addMapMarker(marker);
                    // zoom in the map
                    viewMap.setDisplayPositionByLatLon(latitude, longitude, viewMap.getZoom());
                }  // open up a new incident
                
                htmlDoc.remove(start, length + 1);
                htmlEditor.insertHTML(htmlDoc, start, title, 0, 0, null);
                // end of select and deselect
            }
        } catch (IOException ex) {
            Logger.getLogger(TitlelinkListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadLocationException ex) {            
            Logger.getLogger(TitlelinkListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
