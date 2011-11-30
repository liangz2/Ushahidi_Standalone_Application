/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app_model;

import java.util.ArrayList;

/**
 *
 * @author Zhengyi
 */
public class IncidentList {
    private ArrayList<Incident> list;
    private int currentPage = 1;
    private int incidentNumberPerPage = 20;
    private int selectedIndex = -1;

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    /**
     * 
     * @return 
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * 
     * @return 
     */
    public int getIncidentNumberPerPage() {
        return incidentNumberPerPage;
    }
    
    /**
     * 
     * @param currentPage 
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    
    /**
     * go to next page
     */
    public void nextPage() {
        currentPage++;
    }
    
    /**
     * go back to previous page
     */
    public void previousPage() {
        currentPage--;
    }
    /**
     * 
     * @param incidentNumberPerPage 
     */
    public void setIncidentNumberPerPage(int incidentNumberPerPage) {
        this.incidentNumberPerPage = incidentNumberPerPage;
    }
    
    /**
     * 
     */
    public IncidentList() {
        list = new ArrayList<Incident>();
    }
    
    /**
     * 
     * @param incident 
     */
    public void addIncident(Incident incident) {
        list.add(incident);
    }
    
    /**
     * 
     * @param index
     * @return 
     */
    public Incident getIncident(int index) {
        return list.get(index);
    }
    
    /**
     * 
     * @return 
     */
    public int getSize() {
        return list.size();
    }
    
    /**
     * 
     * @return 
     */
    public int getMaxPage() {
        return (int) Math.ceil((float) list.size() / incidentNumberPerPage);
    }
    
    public void resetList() {
        list.clear();
    }
}