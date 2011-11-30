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
public class CategoryList extends ArrayList<Category> {

    public Category searchCateogry(String id) {
        for (Category category: this)
            if (id.equals(category.getId()))
                return category;
        return null;
    }
    /**
     * returns the id array
     * @return 
     */
    public Object[] getIdArray() {
        String[] ids = new String[size()];
        for (int i = 0; i < size(); i ++)
            ids[i] = get(i).getId();
        return ids;
    }
}
