/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pesofare.constants;

/**
 *
 * @author Abner
 */
public class StringConstants {

    public static final String COM_SUCCESS = "Serial port connected.";
    public static final String COM_NOT_FOUND_ERR = "Serial port not found.";
    public static final String COM_CONNECT_ERR = "Serial port connection failed.";
    public static final String PORT_USE_ERR = "Serial port in use";
    public static final String PORT_NOT_SUPPORTED_ERR = "Serial port not supported";


    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String LOGS_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String logExt = ".log";
    public static final String csvExt = ".csv";

    public static final String TIMEOUT_CONFIRM = "Are you sure you want to timeout?";
    public static final String SHUTDOWN_CONFIRM = "Are you sure you want to shutdown?";
    public static final String RESTART_CONFIRM = "Are you sure you want to restart?";

    public static final String SHUTDOWN_CMD = "shutdown -s -t 0";
    public static final String RESTART_CMD = "shutdown -r";
    public static final String SHUTDOWN = "Shutdown";
    public static final String USERNAME = "Username";
    public static final String PASSWORD = "Password";
    public static final String LOGIN = "Login";
    public static final String RESTART = "Restart";
    public static final String TO_DO = "What do you want to do?";
    public static final String ACTION = "Action";
    public static final String CANCEL = "Cancel";

    public static final String TIMEOUT_CMD = "Timeout";
    public static final String REMAINING_TIME = "Warning";
    public static final String WARNING_TIME = "      1 minute remaining.";
}
