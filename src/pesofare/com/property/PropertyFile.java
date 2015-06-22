/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pesofare.com.property;

import java.io.FileInputStream;
import java.util.Properties;
import pesofare.core.AppCore;

/**
 *
 * @author Abner
 */
public class PropertyFile {

    Properties properties = new Properties();
    public PropertyFile(String path)
    {
        try {
          properties.load(new FileInputStream(path));
        } catch (Exception e) {
          AppCore.getFileLogger().log(e.getMessage());
        }
    }

    public String getString(String propName)
    {
        if(properties!=null)
        {
            return properties.getProperty(propName);
        }

        return "";
    }
}
