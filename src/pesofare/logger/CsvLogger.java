/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pesofare.logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import pesofare.constants.StringConstants;
import pesofare.core.AppCore;
import pesofare.globals.GlobalVariables;

/**
 *
 * @author Abner
 */
public class CsvLogger extends LoggerCore{

    private String header = "time_stamp, computer_name, mac_addres, coin_value";
    
    public CsvLogger()
    {
        super();
        this.loggerType = "csv";
        setHeader();
    }

    public void setHeader()
    {
        file = new File(this.logdir);
        boolean headerFound = false;
        if(file != null)
        {
            BufferedReader br;           
            try {
                br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                   if(line.startsWith(header))
                   {
                       headerFound = true;
                       break;
                   }
                }
                br.close();
            }
            catch (Exception ex) {
               AppCore.getFileLogger().log(ex.getMessage());
            }
        }

        if(!headerFound)
        {
            this.log(header);
        }
    }
    
    public String getFullPath() {
        
        String path = "";
        
        File dir = new File(GlobalVariables.config.getString("csv_log_dir"));
        if(dir != null && !dir.isAbsolute())
        {
            path = System.getProperty("user.dir")+ GlobalVariables.config.getString("csv_log_dir") + getFileName() + StringConstants.csvExt;
            dir = new File(System.getProperty("user.dir")+ GlobalVariables.config.getString("csv_log_dir"));
        }
        else
        {
            path = GlobalVariables.config.getString("csv_log_dir") + getFileName() + StringConstants.csvExt;
        }

        if(dir != null && !dir.exists())
        {
            dir.mkdir();
        }

        return path;
    }

    @Override
    public void checkFile(String path){
        File f = new File(path);
        if(f !=null && !f.exists())
        {
            try {
                f.createNewFile();
            } catch (IOException ex) {
                if(null != this.loggerType)
                {
                    AppCore.getFileLogger().log(ex.getMessage());
                }
            }
        }
    }
}
