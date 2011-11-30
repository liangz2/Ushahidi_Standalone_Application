/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app_contorller;

import app_model.IncidentList;
import app_view.LoadingScreen;
import app_view.Viewer;
import java.awt.Point;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.openstreetmap.gui.jmapviewer.DefaultMapController;

/**
 *
 * @author Zhengyi
 */
public class Controller {
    private Viewer viewer;
    private IncidentList list;
    private LoadingScreen loadingScreen;
    private ExecutorService timerThread;
    private Configs configs;
    // constructor
    public Controller(IncidentList list, Viewer viewer, LoadingScreen loadingScreen) {
        this.viewer = viewer;
        this.list = list;
        this.loadingScreen = loadingScreen;
    }
    
    /**
     * personalize the map controller
     */
    public void setMapControl() {
        // setup the map in the posting page
        DefaultMapController postMapController = new DefaultMapController(viewer.getPostMap(), viewer.getPostLatitude(), viewer.getPostLongitude());
        postMapController.setDoubleClickZoomEnabled(false);
        postMapController.setDoubleClickSetMarkerEnabled(true);
        postMapController.setRightClickRemoveMakerEnabled(true);
        viewer.getPostMap().setDisplayPositionByLatLon(0.0, 0.0, 1);
        
        // setup the map in the vewing page
        new DefaultMapController(viewer.getViewMap());
        viewer.getViewMap().setZoom(5);
    }
    /**
     * 
     */
    public void startProgram() {
        TimerController autoUpdate = null;
        IncidentHarvester initialize = new IncidentHarvester(list, viewer, loadingScreen);
        // load up the config from previous run
        configs = new Configs();
        configs.loadConfig();
        // setup the program with config file
        viewer.setAutoUpdate(configs.AUTO_UPDATE);
        viewer.setUpdateCycle(configs.UPDATE_CYCLE);
        if (configs.AUTO_UPDATE) {
            autoUpdate = new TimerController(configs.UPDATE_CYCLE, initialize);
            timerThread = Executors.newSingleThreadExecutor();
            timerThread.execute(autoUpdate);
            timerThread.shutdown();
        }
        else {
            timerThread = Executors.newSingleThreadExecutor();
            timerThread.execute(initialize);
            timerThread.shutdown();
        }
        
        setMapControl();
        
        // add listeners
        viewer.addHyperlinkListener(new TitlelinkListener(list, viewer));
        viewer.addButtonActionListener(new ButtonController(list, viewer, timerThread, autoUpdate));
        viewer.addComboxListener(new ComboBoxController(viewer));
        viewer.addTextFieldListener(new TextFieldListener());
        viewer.addLogoLabelListener(new WebLinkController());
        viewer.addDetailPaneHyperLinkListener(new WebLinkController());
        // end of adding listeners
    }
}
