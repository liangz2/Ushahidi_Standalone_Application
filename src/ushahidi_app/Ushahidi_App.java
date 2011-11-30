/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ushahidi_app;

import app_contorller.Controller;
import app_model.IncidentList;
import app_view.Viewer;
import app_view.LoadingScreen;

/**
 *
 * @author Zhengyi
 */
public class Ushahidi_App {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Ushahidi_App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ushahidi_App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ushahidi_App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ushahidi_App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        IncidentList incidents = new IncidentList();
        Viewer viewer = new Viewer(incidents);
        LoadingScreen loadingScreen = new LoadingScreen(viewer, true);
        
        Controller incidents_harvester = new Controller(incidents, viewer, loadingScreen);
        incidents_harvester.startProgram();
        
        loadingScreen.setVisible(true);
        viewer.setVisible(true);
    }
}
