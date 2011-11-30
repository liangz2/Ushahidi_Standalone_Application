/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app_contorller;

/**
 *
 * @author Zhengyi
 */
public class TimerController extends Thread {
    private int cycle;
    private IncidentHarvester incidents;
    private enum State {AUTO, MANUAL};
    private State state;
    public TimerController(int cycle, IncidentHarvester incidents) {
        super();
        this.cycle = cycle;
        this.incidents = incidents;
        state = State.AUTO;
    }
    
    /**
     * sets the cycle of the update
     * @param cycle 
     */
    public void setCycle(int cycle) {
        if (cycle == 0)
            state = State.MANUAL;
        else
            this.cycle = cycle;
    }
    
    /**
     * update incidents automatically in each specified cycle
     */
    @Override
    public void run() {
        while(state == State.AUTO) {
            incidents.updateIndicents();
            try {
                sleep(cycle * 60000);
            } catch (InterruptedException ex) {
                return;
            }
        }
    }
}
