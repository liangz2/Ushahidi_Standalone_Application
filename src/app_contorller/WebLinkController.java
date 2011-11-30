/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app_contorller;

import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 *
 * @author Zhengyi
 */
public class WebLinkController extends MouseAdapter implements HyperlinkListener {
    private Desktop desktop;
    private boolean desktopSupport;
    
    public WebLinkController() {
        desktopSupport = Desktop.isDesktopSupported();
    }
    
    /**
     * controls the mouse clicks on the logo
     * @param e 
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if(desktopSupport) {
            desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI("http://192.168.56.50/~jharvard/Ushahidi_Web/index.php"));
            } catch (URISyntaxException ex) {
                Logger.getLogger(WebLinkController.class.getName()).log(Level.SEVERE, null, ex);
            }catch (IOException ex) {
                    Logger.getLogger(WebLinkController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * controls all hyperlinks in the detail panel
     * @param e 
     */
    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
            if (desktopSupport) {
                desktop = Desktop.getDesktop();
                try {
                    desktop.browse(new URI(e.getDescription()));
                } catch (URISyntaxException ex) {
                    JOptionPane.showMessageDialog(null, "Unable to open web page: \n" + e.getDescription(), "Failure", JOptionPane.ERROR_MESSAGE);
                }catch (IOException ex) {
                        Logger.getLogger(WebLinkController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
