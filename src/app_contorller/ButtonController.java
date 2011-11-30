/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app_contorller;

import app_model.IncidentList;
import app_view.LoadingScreen;
import app_view.Viewer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 *
 * @author Zhengyi
 */
public class ButtonController implements ActionListener, ChangeListener {
    private Viewer viewer;
    private IncidentList incidentList;
    private final String ushahidiPostURL = "http://192.168.56.50/~jharvard/Ushahidi_Web/index.php/api";
    private TimerController timer;
    private ExecutorService executor;
    private IncidentHarvester incidents;
    private Configs configs;
    
    // constructor
    public ButtonController(IncidentList incidentList, Viewer viewer, ExecutorService executor, TimerController timer) {
        this.viewer = viewer;
        this.incidentList = incidentList;
        this.executor = executor;
        this.timer = timer;
        configs = new Configs();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("left")) {  // left button on the title list
            incidentList.previousPage();
            viewer.modelChanged();
        }
        else if (e.getActionCommand().equals("right")) {  // right button on the title list
            incidentList.nextPage();
            viewer.modelChanged();
        }
        else if (e.getActionCommand().equals("zoomIn")) {   // zoom in button on the incident display
            viewer.incrementCharSize();
            viewer.updateDetailPane(incidentList.getSelectedIndex());
        }
        else if (e.getActionCommand().equals("zoomOut")) {  // zoom out button on the incident display
            viewer.decrementCharSize();
            viewer.updateDetailPane(incidentList.getSelectedIndex());
        }
        else if (e.getActionCommand().equals("post")) {     // post button on the post page
            String incidentParameter = viewer.getPostIncidentAttributes();
            if (incidentParameter == null) {
                viewer.displayErrorMessage("<html><body align = 'center'>Required Fields start with <font color = red size = '5'>'*'</font> are empty</body></html>");
                return;
            }
            Object[] result = submitIncident(incidentParameter);
            viewer.showPostingMessage((String) result[0], (Boolean) result[1]);
            if ((Boolean) result[1])
                if (!viewer.getResetButton().isEnabled())
                    viewer.getResetButton().setEnabled(true);
        }
        else if (e.getActionCommand().equals("reset")) {
            viewer.resetPostForm();
            viewer.getResetButton().setEnabled(false);
        }
        else if (e.getActionCommand().equals("updateList")) {
            // new loading screen
            LoadingScreen loadingScreen = new LoadingScreen(viewer, true);
            // new Initializer to update incidents
            incidents = new IncidentHarvester(incidentList, viewer, loadingScreen);
            // new thread to run the initializer
            executor = Executors.newSingleThreadExecutor();
            executor.execute(incidents);
            executor.shutdown();
            // set loading screen visibility
            loadingScreen.setVisible(true);
        }
        else if (e.getActionCommand().equals("autoUpdate")) {
            JCheckBox autoUpdate = (JCheckBox) e.getSource();   // get the source
            boolean auto = autoUpdate.isSelected();
            if (auto) {
                viewer.setAutoUpdate(true);
                // new Initializer to update incidents
                incidents = new IncidentHarvester(incidentList, viewer, null);
                timer = new TimerController(viewer.getUpdateCycle(), incidents);
                executor = Executors.newSingleThreadExecutor();
                executor.execute(timer);
                executor.shutdown();
            }
            else {
                // turn off auto update, interrupt all sleeping threads
                viewer.setAutoUpdate(false);
                executor.shutdownNow();
            }
            // set config and save
            configs.AUTO_UPDATE = auto;
            configs.UPDATE_CYCLE = viewer.getUpdateCycle();
            configs.saveConfig(configs);
        }
    }

    private Object[] submitIncident(String param) {
        try {
            // initialize result array
            Object[] result = new Object[2];
            // new URL
            URL url = new URL(ushahidiPostURL);
            // new parser
            XMLParser parser = new XMLParser();
            // open connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);     // sets DoOutput to true in order to feed in parameter for query
            conn.setDoInput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(param);
            wr.flush();

            // Get the response
            Document response = (Document) parser.parseXML(conn.getInputStream());
            NodeList seccessNode = parser.getInfo(response, "success");
            NodeList errorNode = parser.getInfo(response, "message");
            // get the seccess message
            String seccess = seccessNode.item(0).getChildNodes().item(0).getNodeValue();
            // get the error message
            String errorMessage = errorNode.item(0).getChildNodes().item(0).getNodeValue();
            
            // close the writer
            wr.close();
            
            // setup the result array
            if (seccess.equals("true")) {
                result[0] = "<html><body align = 'center'>Post Incident Seccesful <br> Please wait for your post to be approved</body></html>";
                result[1] = true;
            }
            else {
                result[0] = "<html><body align = 'center'>Post Incident Failed: <br>" + errorMessage + "</body></html>";
                result[1] = false;
            }
            return result;
            
        } catch (IOException ex) {
            return null;
        }
    }
    @Override
    public void stateChanged(ChangeEvent e) {
        // get the source of command
        JComponent source = (JComponent) e.getSource();
        // if the source is the main tabbed panel
        if (source instanceof JTabbedPane) {
            int selectedIndex = ((JTabbedPane) source).getSelectedIndex();
            viewer.resetSelectedTabTitle();
            viewer.setSelectedTabTitle(selectedIndex);
        }
        // else if the source is the cycle update spinner
        else {
            int cycle = (Integer) ((JSpinner) source).getValue();
            configs.UPDATE_CYCLE = cycle;       // sets the update cycle to what the spinner indicates
            configs.saveConfig(configs);        // save it for next startup
            timer.setCycle(cycle);          // set the timer to take effect
        }
    }
}
