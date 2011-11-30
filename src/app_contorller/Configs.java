/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app_contorller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Zhengyi
 */
public class Configs implements Serializable {

    public int UPDATE_CYCLE = 5;
    public boolean AUTO_UPDATE = true;
    private Configs configs;
    /**
     * loads the config file
     * @param init 
     */
    public void loadConfig() {
        ObjectInputStream ois;
        InputStream file_reader;
        try {
            file_reader = new FileInputStream(new File("Config"));
            ois = new ObjectInputStream(file_reader);
            configs = (Configs) ois.readObject();
            if (configs == null) {
                JOptionPane.showMessageDialog(null, "Config file is corrupted, please delete the file");
                System.exit(1);
            }
            file_reader.close();
            ois.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Configs.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Config file not found. Default setting is applied");
            configs = new Configs();
            saveConfig(configs);
        }
        
        UPDATE_CYCLE = configs.UPDATE_CYCLE;
        AUTO_UPDATE = configs.AUTO_UPDATE;
    }
    
    /**
     * calls whenever settings are changed
     */
    public void saveConfig(Configs configs) {
        OutputStream  file_writer;
        ObjectOutputStream oos;
        try {
            file_writer = new FileOutputStream(new File("Config"));
            oos = new ObjectOutputStream(file_writer);
            oos.writeObject(configs);
            file_writer.close();
            oos.close();
        } catch (IOException ex) {
            Logger.getLogger(Configs.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
