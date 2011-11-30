/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app_model;

/**
 *
 * @author Zhengyi
 */
public class Incident {
    private String id;
    private String title;
    private String description;
    private String date;
    private String mode;
    private boolean active;
    private boolean verified;
    private Location location;
    private MediaItem[] mediaItems;
    private Category[] categories;

    public Incident(String id, String title, String description, String date, String mode, boolean active, boolean verified, Location location, Category[] categories, MediaItem[] mediaItems) {
        this.categories = categories;
        this.title = title;
        this.description = description;
        this.date = date;
        this.location = location;
        this.mediaItems = mediaItems;
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public Category[] getCategory() {
        return categories;
    }

    public MediaItem[] getMediaItems() {
        return mediaItems;
    }

    public String getMode() {
        return mode;
    }

    public boolean isVerified() {
        return verified;
    }

    public String getId() {
        return id;
    }
    
    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public Location getLocation() {
        return location;
    }
    
}
