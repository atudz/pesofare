/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pesofare.listeners.serialport;

import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import pesofare.core.AppCore;
import pesofare.globals.GlobalVariables;

/**
 *
 * @author Abner
 */
public class SerialPortListener implements SerialPortEventListener{

    public void serialEvent(SerialPortEvent event)
     {
         switch(event.getEventType())
         {
            case SerialPortEvent.BI:
                 AppCore.getFileLogger().log("SerialPortEvent.BI occurred");
            case SerialPortEvent.OE:
                AppCore.getFileLogger().log("SerialPortEvent.OE occurred");
            case SerialPortEvent.FE:
                AppCore.getFileLogger().log("SerialPortEvent.FE occurred");
            case SerialPortEvent.PE:
                AppCore.getFileLogger().log("SerialPortEvent.PE occurred");
            case SerialPortEvent.CD:
                AppCore.getFileLogger().log("SerialPortEvent.CD occurred");
            case SerialPortEvent.CTS:
                AppCore.getFileLogger().log("SerialPortEvent.CTS occurred");
            case SerialPortEvent.DSR:
                AppCore.getFileLogger().log("SerialPortEvent.DSR occurred");
            case SerialPortEvent.RI:
                AppCore.getFileLogger().log("SerialPortEvent.RI occurred");
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                AppCore.getFileLogger().log("SerialPortEvent.OUTPUT_BUFFER_EMPTY occurred");
                break;
            case SerialPortEvent.DATA_AVAILABLE:
                AppCore.getFileLogger().log("SerialPortEvent.DATA_AVAILABLE occurred");
                GlobalVariables.DATA_AVAILABLE = true;
                break;
        }
     }

}
