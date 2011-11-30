/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app_view;

import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author Zhengyi
 */
public class TimerLabel extends JLabel implements Runnable {
    private Calendar date;
    private String day;
    private String month;
    private String year;
    private String hour;
    private String minute;
    private int second;
    private String AMPM;
    
    public TimerLabel () {
        super();
    }
    
    /**
     * returns the current date
     * @return 
     */
    public String getDate() {
        return month + "/" + day + "/" + year;
    }
    
    /**
     * returns the current hour
     * @return 
     */
    public String getHour() {
        return hour;
    }
    
    /**
     * returns the current minute
     * @return 
     */
    public String getMinute() {
        return minute;
    }
    
    /**
     * returns the current am pm
     * @return 
     */
    public String getAMPM() {
        return AMPM;
    }
    
    @Override
    public void run() {
        // infinite loop to keep the time updated
        while (true) {
            try {
                date = Calendar.getInstance();
                second = date.get(Calendar.SECOND);
                if (second < 1 || day == null) {
                    // get the dates
                    day = Integer.toString(date.get(Calendar.DAY_OF_MONTH));
                    month = Integer.toString(date.get(Calendar.MONTH) + 1);
                    year = Integer.toString(date.get(Calendar.YEAR));
                    // end of getting dates

                    // get the current time
                    hour = Integer.toString(date.get(Calendar.HOUR));
                    minute = Integer.toString(date.get(Calendar.MINUTE));
                    AMPM = (date.get(Calendar.AM_PM) > 0) ? "PM" : "AM";
                    // end of getting time
                }
                // setting the fiels
                setText(month + "/" + day + "/" + year + " " 
                        + hour + ":" + minute + ":" + Integer.toString(second) + " "
                        + AMPM);
                // end of setting fiels
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(TimerLabel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
