/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pesofare.core;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import pesofare.dao.Dao;
import pesofare.frame.AppFrame;
import pesofare.globals.GlobalVariables;
import pesofare.logger.CsvLogger;
import pesofare.logger.FileLogger;

/**
 *
 * @author Abner
 */
public class AppCore {

    private static FileLogger fileLogger;
    private static CsvLogger csvLogger;
    private static AppFrame appFrame;
    private static Dao daoLogger;

    
    private final static AppCore app = new AppCore();
    private static ImageIcon systemIcon;
    private static BufferedImage systemImage = null;

    private static String macAddress = "";
    private static String ipAddress = "";

    private static File insertCoinFile;
    private static File timerFile;

    public AppCore()
    {
        fileLogger = new FileLogger();
        csvLogger = new CsvLogger();
        appFrame = new AppFrame();
        daoLogger = new Dao();
        systemIcon = new ImageIcon(System.getProperty("user.dir") + GlobalVariables.config.getString("system_icon"));

        insertCoinFile = new File(System.getProperty("user.dir") +GlobalVariables.config.getString("inser_coin")).getAbsoluteFile();
        timerFile = new File(System.getProperty("user.dir") +GlobalVariables.config.getString("timer_dir")).getAbsoluteFile();
    }

    public static FileLogger getFileLogger()
    {
        return fileLogger;
    }

     public static CsvLogger getCsvLogger()
    {
        return csvLogger;
    }

    public static AppFrame getAppFrame()
    {
        return appFrame;
    }

    public static AppCore getInstance()
    {
        return app;
    }

    public void launch()
    {
        appFrame.launch();
    }

    public static String getCurrentDateTime(String format)
    {
        DateFormat dateFormat;
        dateFormat = new SimpleDateFormat(format);

        return dateFormat.format(new Date()).toString();
    }


    public static void destroy()
    {
        appFrame.getLockFrame().dispose();
        appFrame.dispose();
    }

    public static ImageIcon getSystemIcon()
    {
        return systemIcon;
    }

    public static Image getSystemImage()
    {
        if(null == systemImage)
        {
            try
            {
                systemImage = ImageIO.read(new File(System.getProperty("user.dir") + GlobalVariables.config.getString("system_icon")));
            }
            catch (IOException ex)
            {
                fileLogger.log(ex.getMessage());
            }
        }
        return systemImage;
    }

    public static String getIpAddress() 
    {
        if(ipAddress.isEmpty())
        {
            try
            {
                InetAddress ip = InetAddress.getLocalHost();
                ipAddress = ip.getHostAddress();
            } 
            catch (UnknownHostException ex)
            {
                fileLogger.log(ex.getMessage());            
            }        
        }
        return ipAddress;
    }
    
    public static String getMacAddress()
    {
        if(macAddress.isEmpty())
        {
            try
            {
                InetAddress ip = InetAddress.getLocalHost();
                NetworkInterface network = NetworkInterface.getByInetAddress(ip);
                byte[] mac = network.getHardwareAddress();
                if(mac != null)
                {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++)
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    macAddress = sb.toString();
                }
            }
            catch (Exception ex)
            {
                fileLogger.log(ex.getMessage());
            }
        }
        return macAddress;
    }

    public static Dao getDBLogger()
    {
        return daoLogger;
    }

    public static boolean checkCredentials(String username, String password)
    {
        boolean valid = false;
        
        if(username.equals(GlobalVariables.system.getString("username"))
           && password.equals(GlobalVariables.system.getString("password")))
        {
            valid = true;
        }

        return valid;
    }

    public static void playTimer() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(timerFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch(Exception ex) {
            fileLogger.log(ex.getMessage());
        }
    }

    public static void playInserCoin() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(insertCoinFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch(Exception ex) {
            fileLogger.log(ex.getMessage());
        }
    }
}
