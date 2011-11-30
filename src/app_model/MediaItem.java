/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app_model;

/**
 *
 * @author Zhengyi
 */
public class MediaItem {
    private String id;
    private String type;
    private String link;

    public MediaItem(String id, String type, String link) {
        this.id = id;
        this.type = type;
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public String getType() {
        return type;
    }
    
    
}
