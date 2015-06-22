/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pesofare.thread;

import pesofare.com.CommConnector;
import pesofare.constants.StringConstants;
import pesofare.core.AppCore;
import pesofare.dao.PesoFareData;
import pesofare.globals.GlobalVariables;

/**
 *
 * @author Abner
 */
public class ComListenerThread extends Thread{

     private final CommConnector com = CommConnector.getInstance();
     private int coin_5_time_val = Integer.valueOf(GlobalVariables.system.getString("five_coin"));
     private int coin_1_time_val = Integer.valueOf(GlobalVariables.system.getString("one_coin"));

    /* private String comPort = GlobalVariables.config.getString("com_port");
     private int baudRate = Integer.valueOf(GlobalVariables.config.getString("baudrate"));
     private int dataBits = Integer.valueOf(GlobalVariables.config.getString("databit"));
     private int stopBits = Integer.valueOf(GlobalVariables.config.getString("stopbit"));
     private int parity = Integer.valueOf(GlobalVariables.config.getString("parity"));
     private int bufferSize = Integer.valueOf(GlobalVariables.config.getString("buffer_size"));*/

     public ComListenerThread()
     {
        // int result = com.connect(comPort, baudRate, dataBits, stopBits, parity, bufferSize);
     }

     @Override
     public void run()
     {
         int length = 0;
         int coinValue = 0;
         byte[] serialData = new byte[1];
         boolean res = false;
         
         while(true)
         {             
             if(GlobalVariables.START_COM_READ && !interrupted())
             {
                 if(GlobalVariables.DATA_AVAILABLE)
                 {
                    GlobalVariables.COIN_INSERTED = true;
                    length = com.read(serialData);
                    coinValue = new Byte(serialData[0]).intValue();
                    System.out.println("DATA: " + (int)coinValue);

                    switch(coinValue)
                    {
                        case 1:                             
                             if(GlobalVariables.MINUTES + coin_1_time_val < 60)
                             {
                                GlobalVariables.MINUTES = GlobalVariables.MINUTES + coin_1_time_val;
                             }
                             else
                             {
                                 if(((GlobalVariables.MINUTES + coin_1_time_val)/60) + GlobalVariables.HOURS > 24)
                                 {
                                    GlobalVariables.HOURS = 24;
                                    GlobalVariables.MINUTES = 0;
                                 }
                                 else
                                 {
                                    GlobalVariables.HOURS = ((GlobalVariables.MINUTES + coin_1_time_val)/60) + GlobalVariables.HOURS;
                                    GlobalVariables.MINUTES = (GlobalVariables.MINUTES + coin_1_time_val)%60;
                                 }
                             }
                             AppCore.getAppFrame().updateTime("");
                             if(GlobalVariables.SCREEN_LOCKED)
                             {
                                 AppCore.getAppFrame().getLockFrame().unlockScreen();
                             }
                             AppCore.playInserCoin();
                             logEvent(coinValue);
                             break;
                        case 5:                             
                             if(GlobalVariables.MINUTES + coin_5_time_val < 60)
                             {
                                GlobalVariables.MINUTES = GlobalVariables.MINUTES + coin_5_time_val;
                             }
                             else
                             {
                                 if(((GlobalVariables.MINUTES + coin_5_time_val)/60) + GlobalVariables.HOURS > 24)
                                 {
                                    GlobalVariables.HOURS = 24;
                                    GlobalVariables.MINUTES = 0;
                                 }
                                 else
                                 {
                                    GlobalVariables.HOURS = ((GlobalVariables.MINUTES + coin_5_time_val)/60) + GlobalVariables.HOURS;
                                    GlobalVariables.MINUTES = (GlobalVariables.MINUTES + coin_5_time_val)%60;
                                 }
                             }
                             AppCore.getAppFrame().updateTime("");
                             if(GlobalVariables.SCREEN_LOCKED)
                             {
                                 AppCore.getAppFrame().getLockFrame().unlockScreen();
                             }
                             AppCore.playInserCoin();
                             logEvent(coinValue);
                             break;
                    }
                 }
             }
             
             try
             {
                Thread.sleep(10);
             }
             catch (InterruptedException ex)
             {
                AppCore.getFileLogger().log(ex.getMessage());
             }
         }
     }

     public void logEvent(int coinValue)
     {
         if(Boolean.valueOf(GlobalVariables.config.getString("monitor_to_csv")))
         {
            AppCore.getCsvLogger().log(AppCore.getCurrentDateTime(StringConstants.LOGS_DATE_FORMAT)+","+GlobalVariables.config.getString("computer_name")+","+AppCore.getMacAddress()+","+coinValue);
         }

         if(Boolean.valueOf(GlobalVariables.config.getString("monitor_to_db")))
         {
            PesoFareData data = new PesoFareData();
            data.setValue(String.valueOf(coinValue));
            AppCore.getDBLogger().log(data);
         }
     }

}
