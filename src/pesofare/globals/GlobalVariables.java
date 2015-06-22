/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pesofare.globals;

import pesofare.com.property.PropertyFile;

/**
 *
 * @author Abner
 */
public class GlobalVariables {

     // Flags for Serial port reading
    public static boolean START_COM_WRITE = false;
    public static boolean START_COM_READ = false;
    public static boolean START_TIMER = false;
    public static boolean DATA_AVAILABLE = false;
    public static boolean DEVICE_RESPONDING = false;
    public static boolean PASS_KEY_PRESSED = false;
    public static boolean DISPLAY_WARNING = false;

    public static boolean COIN_INSERTED = false;
    public static boolean SCREEN_LOCKED = false;

    public static int MINUTES = 0;
    public static int SECONDS = 0;
    public static int HOURS = 0;

    public static byte[] TIME_OUT = {0x0B};

   // public static final ResourceBundle config = ResourceBundle.getBundle("configs.pesofare",Locale.getDefault());
   // public static final ResourceBundle system = ResourceBundle.getBundle("configs.system",Locale.getDefault());

    public static final PropertyFile config = new PropertyFile(System.getProperty("user.dir")+"/configs/pesofare.properties");
    public static final PropertyFile system = new PropertyFile(System.getProperty("user.dir")+"/configs/system.properties");

}
