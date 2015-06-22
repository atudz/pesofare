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

/**
 *
 * @author Abner
 */
public abstract class LoggerCore {

    protected String logdir;
    protected BufferedWriter bwriter;
    protected File file;
    protected String loggerType;


    public LoggerCore()
    {
        logdir = getFullPath();
        checkFile(logdir);
        file = new File(logdir);
    }
    
    public abstract String getFullPath();
    
    public String getFileName() {

       return AppCore.getCurrentDateTime(StringConstants.DATE_FORMAT);
    }

    public void checkFile(String path){
        
        File f = new File(path);
        if(f != null && !f.exists())
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

    public void log(String message)
    {
        if(file != null)
        {
            try
            {
                file = new File(logdir);
                bwriter = new BufferedWriter(new FileWriter(file, true));
                bwriter.write(message, 0, message.length());
                bwriter.newLine();
                bwriter.close();
            }
            catch(IOException e)
            {
                AppCore.getFileLogger().log(e.getMessage());
            }
        }
    }
}
