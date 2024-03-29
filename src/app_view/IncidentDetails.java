/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * IncidentDetails.java
 *
 * Created on Nov 6, 2011, 1:44:30 AM
 */
package app_view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author Zhengyi
 */
public class IncidentDetails extends javax.swing.JDialog {
    /** Creates new form IncidentDetails */
    public IncidentDetails(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        viewer.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                System.out.println(viewer.getPosition(e.getX(), e.getY()));
            }
        });
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane = new javax.swing.JScrollPane();
        viewer = new org.openstreetmap.gui.jmapviewer.JMapViewer();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        scrollPane.setViewportView(viewer);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 692, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    
    public static void main(String[] args) {
        IncidentDetails details = new IncidentDetails(null, false);
        details.setVisible(true);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane scrollPane;
    private org.openstreetmap.gui.jmapviewer.JMapViewer viewer;
    // End of variables declaration//GEN-END:variables
}
