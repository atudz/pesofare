/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pesofare.com;

import pesofare.globals.GlobalVariables;
import pesofare.listeners.serialport.SerialPortListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import javax.comm.*;
import pesofare.constants.StringConstants;
import pesofare.core.AppCore;


/**
 *
 * @author Abner
 */
public class CommConnector {

    // Variable that holds the instance of this object
   private static final CommConnector instance = new CommConnector();

    private String driverName;
    private CommDriver commDriver;
    private OutputStream out;
    private InputStream in;
    private CommPortIdentifier portIdentifier;
    private SerialPort serialPort;

    public static final int COMM_CONNECT_SUCCESS = 1;
    public static final int COMM_CONNECT_FAILED = 2;
    public static final int COMM_CONNECT_INUSE = 3;
    public static final int COMM_CONNECT_PORT_NOT_FOUND = 4;
    public static final int COMM_CONNECT_PORT_NOT_SERIAL = 5;
    public static final int COMM_CONNECT_PORT_NOT_SUPPORTED = 6;

    private final int MAX_COMM = 10;

    private String comPort = GlobalVariables.config.getString("com_port");
    private int baudRate = Integer.valueOf(GlobalVariables.config.getString("baudrate"));
    private int dataBits = Integer.valueOf(GlobalVariables.config.getString("databit"));
    private int stopBits = Integer.valueOf(GlobalVariables.config.getString("stopbit"));
    private int parity = Integer.valueOf(GlobalVariables.config.getString("parity"));
    private int bufferSize = Integer.valueOf(GlobalVariables.config.getString("buffer_size"));

    /**
     *  Constructor
     */
    public CommConnector()
    {
        initializeClassVariables();
        initializeDriver();
        initializeConnection();
    }

    /**
     * Initialize class variables
     */
    public void initializeClassVariables()
    {
        driverName = new String();
        commDriver = null;
        out = null;
        portIdentifier = null;
        serialPort = null;
        in = null;
    }
    /**
     *  Initialize javax.comm driver settings
     */
    public void initializeDriver()
    {           
        this.driverName = GlobalVariables.config.getString("com_driver");

        try
        {
            commDriver = (CommDriver)Class.forName(this.driverName).newInstance();
            commDriver.initialize();
        }
        catch (Exception e)
        {
            AppCore.getFileLogger().log(e.getMessage());
        }

    }
    /**
     *  Initialize connection
     */
    private void initializeConnection()
    {
        int result = this.connect(comPort, baudRate, dataBits, stopBits, parity, bufferSize);
        System.out.println(result);
    }

    /**
     *  Gets comm port lists
     */
     public String[] getCommPortLists()
     {
         String[] ports = new String[this.MAX_COMM];
         int index = 0;
         CommPortIdentifier portIdentifier = null;
         Enumeration portList = CommPortIdentifier.getPortIdentifiers();        
         while(portList.hasMoreElements())
         {
             portIdentifier = (CommPortIdentifier)portList.nextElement();
             if(portIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL);
             {
                 ports[index] = (String)portIdentifier.getName();
                 index++;
             }
         }

         return ports;
     }

     /**
      *  Connect to serial port
      *  @return int Connection status result
      */

     public int connect(String comm, int baudrate, int dataBits, int stopBits, int parity, int bufferSize)
     {
         int result = CommConnector.COMM_CONNECT_SUCCESS;
         try
         {
             portIdentifier = CommPortIdentifier.getPortIdentifier(comm);
         }
         catch(NoSuchPortException e)
         {
             result = CommConnector.COMM_CONNECT_PORT_NOT_FOUND;
             AppCore.getFileLogger().log(StringConstants.COM_NOT_FOUND_ERR);
         }
         catch(Exception e)
         {
             result = CommConnector.COMM_CONNECT_FAILED;
             AppCore.getFileLogger().log(StringConstants.COM_CONNECT_ERR);
         }

         if(CommConnector.COMM_CONNECT_SUCCESS == result && portIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL)
         {
             try
             {
                  serialPort = (SerialPort)portIdentifier.open(GlobalVariables.config.getString("connection_identifier"), 1000);
                  serialPort.setSerialPortParams(baudrate, dataBits, stopBits, parity);
                  out = serialPort.getOutputStream();
                  in = serialPort.getInputStream();

                  serialPort.addEventListener(new SerialPortListener());

                  serialPort.notifyOnDataAvailable(true);
                  serialPort.notifyOnBreakInterrupt(true);
                  serialPort.notifyOnCarrierDetect(true);
                  serialPort.notifyOnCTS(true);
                  serialPort.notifyOnDataAvailable(true);
                  serialPort.notifyOnDSR(true);
                  serialPort.notifyOnFramingError(true);
                  serialPort.notifyOnOutputEmpty(true);
                  serialPort.notifyOnOverrunError(true);
                  serialPort.notifyOnParityError(true);
                  serialPort.notifyOnRingIndicator(true);
             }
             catch (PortInUseException e)
             {
                  result = CommConnector.COMM_CONNECT_INUSE;
                  AppCore.getFileLogger().log(StringConstants.PORT_USE_ERR);
             }
             catch (UnsupportedCommOperationException e)
             {
                  result = CommConnector.COMM_CONNECT_PORT_NOT_SUPPORTED;
                  AppCore.getFileLogger().log(StringConstants.PORT_NOT_SUPPORTED_ERR);
             }
             catch (TooManyListenersException e)
             {
                  result = CommConnector.COMM_CONNECT_INUSE;
                  AppCore.getFileLogger().log(StringConstants.COM_CONNECT_ERR);
             }
             catch (IOException e)
             {
                 result = CommConnector.COMM_CONNECT_FAILED;
                 AppCore.getFileLogger().log(StringConstants.COM_CONNECT_ERR);
             }
         }

         if(result == 1)
         {
             AppCore.getFileLogger().log(StringConstants.COM_SUCCESS);
         }

         return result;
     }

     /**
      * Writes data to Serial Port
      */
     public boolean write(byte[] data)
     {
         boolean result = true;
         try
         {
             out.write(data);
         }
         catch(IOException e)
         {
             result = false;
             AppCore.getFileLogger().log(e.getMessage());
         }
         return result;
     }

     /**
      * Writes data to Serial Port
      */
     public boolean write(int data)
     {
         boolean result = true;
         try
         {
             out.write(data);
         }
         catch(IOException e)
         {
             result = false;
             AppCore.getFileLogger().log(e.getMessage());
         }
         return result;
     }
     /**
      *  Reads data in Serial Port
      */
      public int read(byte[] buffer)
      {
          int length = 0;
          try
          {
             if(GlobalVariables.DATA_AVAILABLE)
             {
                 while(in.available() > 0)
                 {
                     length = in.read(buffer);
                 }
                 GlobalVariables.DATA_AVAILABLE = false;
                 GlobalVariables.DEVICE_RESPONDING = true;
             }
          }
          catch(IOException e)
          {
             AppCore.getFileLogger().log(e.getMessage());
          }
          return length;
      }

     /**
      *
      */
     public boolean close()
     {
         boolean result = true;

         try
         {
             in.close();
             out.close();
             serialPort.close();
         }
         catch(IOException e)
         {
              result = false;
              AppCore.getFileLogger().log(e.getMessage());
         }

         return result;
     }

     public CommDriver getCommDriver()
     {
         return commDriver;
     }

     public OutputStream getOutputStream()
     {
         return out;
     }

     public InputStream getInputStream()
     {
         return in;
     }

     public SerialPort getSerialPort()
     {
         return serialPort;
     }

    public static CommConnector getInstance()
    {
        return instance;
    }

}
