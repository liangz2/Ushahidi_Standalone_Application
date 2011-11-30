/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app_model;

/**
 *
 * @author Zhengyi
 */
public class Location {
    String id;
    String name = "";
    double longitude;
    double latitude;
    
    public Location(String id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }
}
