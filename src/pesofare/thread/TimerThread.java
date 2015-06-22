/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pesofare.thread;

import java.awt.AWTException;
import java.awt.KeyboardFocusManager;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import pesofare.com.CommConnector;
import pesofare.core.AppCore;
import pesofare.globals.GlobalVariables;

/**
 *
 * @author Abner
 */
public class TimerThread extends Thread{

    private int interval = Integer.valueOf(GlobalVariables.system.getString("time_interval"));
    private final CommConnector com = CommConnector.getInstance();
    private Robot robot;

    public TimerThread()
    {
         try {
            robot = new Robot();
        } catch (AWTException ex) {
            AppCore.getFileLogger().log(ex.getMessage());
        }
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            if(GlobalVariables.START_TIMER && !interrupted())
            {                
                try
                {
                    Thread.sleep(interval);
                } catch (InterruptedException ex)
                {
                    AppCore.getFileLogger().log(ex.getMessage());
                }
                if(GlobalVariables.SECONDS >= 0)
                {                   
                    if(GlobalVariables.SECONDS > 0)
                      GlobalVariables.SECONDS--;
                    else if(GlobalVariables.SECONDS == 0)
                    {
                       if(GlobalVariables.MINUTES > 0)
                       {
                          GlobalVariables.MINUTES--;
                          GlobalVariables.SECONDS = 59;
                       }
                       else if(GlobalVariables.MINUTES == 0 && GlobalVariables.HOURS > 0)
                       {
                           GlobalVariables.HOURS--;
                           GlobalVariables.MINUTES = 59;
                           GlobalVariables.SECONDS = 59;
                       }
                    }
                }
                else if(GlobalVariables.MINUTES >= 0)
                {
                   if(GlobalVariables.MINUTES > 0)
                      GlobalVariables.MINUTES--;
                   else if(GlobalVariables.MINUTES == 0)
                   {
                       if(GlobalVariables.HOURS > 0)
                       {
                          GlobalVariables.HOURS--;
                          GlobalVariables.MINUTES = 59;
                       }
                   }
                }       
                if(GlobalVariables.HOURS == 0 && GlobalVariables.MINUTES == 0 && GlobalVariables.SECONDS == 0)
                {
                    GlobalVariables.COIN_INSERTED = false;
                    AppCore.getAppFrame().updateTime(GlobalVariables.config.getString("default_display"));
                    com.write(GlobalVariables.TIME_OUT);
                    if(!GlobalVariables.SCREEN_LOCKED)
                    {
                        AppCore.getAppFrame().getLockFrame().lockScreen();
                    }
                    else
                    {
                        if(JFrame.ICONIFIED == AppCore.getAppFrame().getLockFrame().getState())
                        {
                            AppCore.getAppFrame().getLockFrame().setState(JFrame.NORMAL);
                        }

                        if(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() == null)
                        {
                          //  robot.keyPress(KeyEvent.VK_CONTROL);
                       //     robot.keyPress(KeyEvent.VK_ESCAPE);
                            AppCore.getAppFrame().getLockFrame().requestFocus();
                        }                       
                    }
                }
                else
                {
                    AppCore.getAppFrame().updateTime("");
                }

                if(GlobalVariables.HOURS == 0 && GlobalVariables.MINUTES == 0 && GlobalVariables.SECONDS < Integer.parseInt(GlobalVariables.system.getString("alert_time")) && GlobalVariables.SECONDS !=0 )
                {
                    AppCore.playTimer();
                }
               
            }            
        }
    }    

    public void setInterval(int ms)
    {
        interval = ms;
    }

}
