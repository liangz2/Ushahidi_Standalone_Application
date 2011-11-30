/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app_contorller;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Zhengyi
 */
public class TextFieldListener extends KeyAdapter {

    /**
     * defines what to do when a key stroke is down
     * @param e 
     */
    @Override
    public void keyReleased(KeyEvent e) {
        JComponent source = (JComponent) e.getSource();     // gets the source component
        // action performed according to whether
        if (!(source instanceof JTextArea)) {
            JTextField textField = (JTextField) source;     // if the area is a text field
            JLabel label = ((JLabel) textField.getAccessibleContext().getAccessibleParent());
            String title = label.getText();
            if (textField.getText().isEmpty() && label.getForeground().equals(Color.black)) {
                label.setText("* " + title);
                label.setForeground(Color.red);
            }
            else if (!textField.getText().isEmpty() && label.getForeground().equals(Color.red)) {
                label.setText(title.substring(2));
                label.setForeground(Color.black);
            }
        }
        else {
            JTextArea textArea = (JTextArea) source;        // if the area is a text area
            // obtain the title and color
            JScrollPane scroller = (JScrollPane) textArea.getAccessibleContext().getAccessibleParent().getAccessibleContext().getAccessibleParent();
            TitledBorder border = (TitledBorder) scroller.getBorder();
            Color titleColor = border.getTitleColor();
            String title = border.getTitle();
            // end of obtaining title and color
            
            // modify the title accordingly
            if (textArea.getText().isEmpty() && titleColor.equals(Color.black)) {       // area is empty
                border.setTitle("* " + title);
                border.setTitleColor(Color.red);
                scroller.repaint();
            }
            else if (!textArea.getText().isEmpty() && titleColor.equals(Color.red)) {       // area is not empty
                border.setTitle(title.substring(2));
                border.setTitleColor(Color.black);
                scroller.repaint();
            }
        }
        // end of action
    }
}
