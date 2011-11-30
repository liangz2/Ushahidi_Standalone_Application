/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app_model;

/**
 *
 * @author Zhengyi
 */
public class Category implements Comparable {
    private String id;
    private String parentId;
    private String description;
    private String title;
    private String color;
    private String position;
    
    public Category (String id, String title) {
        this.id = id;
        this.title = title;
    }
    
    public Category(String id, String parentId, String title, String description, String color, String position) {
        this.id = id;
        this.parentId = parentId;
        this.description = description;
        this.title = title;
        this.color = color;
        this.position = position;
    }

    public String getColor() {
        return color;
    }

    public String getDescription() {
        return description;
    }

    public String getParentId() {
        return parentId;
    }

    public String getPosition() {
        return position;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
    
    @Override
    public String toString() {
        return title;
    }

    @Override
    public int compareTo(Object o) {
        return title.compareTo(o.toString());
    }
}
