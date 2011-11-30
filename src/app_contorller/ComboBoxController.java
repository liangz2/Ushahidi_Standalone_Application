/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app_contorller;

import app_view.Viewer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *
 * @author Zhengyi
 */
public class ComboBoxController implements ActionListener {
    private Viewer viewer;
    public ComboBoxController(Viewer viewer) {
        this.viewer = viewer;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("categoryTitle")) {
            viewer.setCategoryIdIndex();
        }
        else if (e.getActionCommand().equals("categoryId")) {
            viewer.setCategoryTitleIndex();
        }
    }
}
