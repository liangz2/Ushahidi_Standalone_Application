/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app_view;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;

/**
 *
 * @author Zhengyi
 */
public class CountDownButton extends JButton implements Runnable {
    private int reUseCD = 10;
    private String title;
    public CountDownButton(String title) {
        super(title);
        this.title = title;
    }
    @Override
    public void run() {
        try {
            setEnabled(false);
            while (reUseCD > 0) {
                setText(Integer.toString(reUseCD));
                Thread.sleep(1000);
                reUseCD--;
            }
            reUseCD = 10;
            setText(title);
            setEnabled(true);
        } catch (InterruptedException ex) {
            Logger.getLogger(CountDownButton.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
