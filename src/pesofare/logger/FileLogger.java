/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pesofare.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import pesofare.constants.StringConstants;
import pesofare.core.AppCore;
import pesofare.globals.GlobalVariables;

/**
 *
 * @author Abner
 */
public class FileLogger extends LoggerCore{
    
    public FileLogger()
    {        
        super();
        this.loggerType = "file";
    }

    @Override
    public void log(String message)
    {
        if(file != null)
        {
           // System.out.println(message);
            message = AppCore.getCurrentDateTime(StringConstants.LOGS_DATE_FORMAT) + " " + message;

            try
            {
                bwriter = new BufferedWriter(new FileWriter(file, true));
                bwriter.write(message, 0, message.length());
                bwriter.newLine();
                bwriter.close();
            }
            catch(IOException e)
            {
               // System.out.println(e.getMessage());
               //AppCore.getFileLogger().log(e.getMessage());
            }
        }
    }

    public String getFullPath()
    {        
        String path = "";

        File dir = new File(GlobalVariables.config.getString("log_dir"));
        if(dir != null && !dir.isAbsolute())
        {
            path = System.getProperty("user.dir")+ GlobalVariables.config.getString("log_dir") + getFileName() + StringConstants.logExt;
            dir = new File(System.getProperty("user.dir")+ GlobalVariables.config.getString("log_dir"));
        }
        else
        {
            path = GlobalVariables.config.getString("log_dir") + getFileName() + StringConstants.logExt;            
        }

        if(dir != null && !dir.exists())
        {
            dir.mkdir();
        }

        return path;
    }

}
