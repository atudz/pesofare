/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pesofare.frame;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GraphicsDevice;
import java.awt.KeyboardFocusManager;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import pesofare.constants.StringConstants;
import pesofare.core.AppCore;
import pesofare.globals.GlobalVariables;
import pesofare.listeners.key.KeyboardListener;
/**
 *
 * @author Abner
 */
public class LockScreenFrame extends JFrame implements ActionListener{

    private boolean screenIsLocked = false;
    private GraphicsDevice device;
    private Container container;
    private JOptionPane confirmPane = null;
    private JDialog confirmDialog = null;
    private JButton shutdown = null;
    private JPanel panelShutDown = null;
    private JLabel label = null;
    private Robot robot;

     Toolkit tk = Toolkit.getDefaultToolkit();
     int xSize = ((int) (tk.getScreenSize().getWidth()* 0.75) );
     int ySize = ((int) (tk.getScreenSize().getHeight() * 0.95));

    public LockScreenFrame(GraphicsDevice device)
    {
        super(device.getDefaultConfiguration());
        this.device = device;
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            AppCore.getFileLogger().log(ex.getMessage());
        }
        initComponents(getContentPane());
    }

    public LockScreenFrame()
    {
        initComponents(getContentPane());
    }
    
    public void initComponents(Container c)
    {        
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);       
        
        container = c;
        setUndecorated(true);
 		setResizable(false);
        setAlwaysOnTop(true);
        
        getContentPane().setBackground(Color.BLACK);
        
        File file = new File(System.getProperty("user.dir") + GlobalVariables.config.getString("lock_image"));
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch (IOException ex) {
            AppCore.getFileLogger().log(ex.getMessage());
        }

        setLayout(new BorderLayout());
        label = new JLabel(new ImageIcon(image));
        container.add(label, BorderLayout.CENTER);


        ImageIcon img = new ImageIcon(System.getProperty("user.dir") + GlobalVariables.config.getString("shutdown_icon"));
        shutdown = new JButton(img);
        shutdown.setActionCommand(StringConstants.SHUTDOWN);
        shutdown.setBorderPainted(false);
        shutdown.setFocusPainted(false);
        shutdown.setContentAreaFilled(false);
        shutdown.setFocusable(false);

        shutdown.addActionListener(this);
        
        panelShutDown = new JPanel(new BorderLayout(10,10));
        panelShutDown.setBackground(Color.BLACK);
        panelShutDown.add(shutdown, BorderLayout.SOUTH);
        container.add(panelShutDown, BorderLayout.EAST);

        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyboardListener());

    }

    public void lockScreen()
    {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ESCAPE);

        requestFocus();
        if(device != null)
        {
            device.setFullScreenWindow(this);
        }
        else
        {
            setVisible(true);
        }
                
        screenIsLocked = true;
        GlobalVariables.SCREEN_LOCKED = true;

        try {
             Runtime.getRuntime().exec("taskkill /F /IM explorer.exe").waitFor();
        } catch (Exception e) {
            AppCore.getFileLogger().log(e.getMessage());
        }

        System.gc();
    }

    public void unlockScreen()
    {
        screenIsLocked = false;
        GlobalVariables.SCREEN_LOCKED = false;
        setVisible(false);
        if(device != null)
        {
            device.setFullScreenWindow(null);
        }
        else
        {
            setVisible(false);
        }

        AppCore.getAppFrame().displayCenter();

        try {
             Runtime.getRuntime().exec("C:\\Windows\\explorer.exe");
        } catch (Exception e) {
            AppCore.getFileLogger().log(e.getMessage());
        }
    }

    public boolean isLocked()
    {
        return screenIsLocked;
    }


    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action.equals(StringConstants.SHUTDOWN)) {

           confirmPane = new JOptionPane(StringConstants.SHUTDOWN_CONFIRM, JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
           confirmDialog = confirmPane.createDialog(container, e.getActionCommand());
          // confirmDialog.setIconImage(AppCore.getSystemImage());
           confirmDialog.setVisible(true);

           Object value = confirmPane.getValue();
           if(value != null)
           {
               if(value.toString().equals("0"))
               {
                   AppCore.destroy();
                   Runtime runtime = Runtime.getRuntime();
                   try {
                       Process proc = runtime.exec(StringConstants.SHUTDOWN_CMD);
                   }
                   catch (IOException ex) {
                       AppCore.getFileLogger().log(ex.getMessage());
                   }
               }
           }
                    
          /*  Object[] options = {StringConstants.SHUTDOWN, StringConstants.RESTART, StringConstants.CANCEL};
            int selection = JOptionPane.showOptionDialog(container,
                                                 StringConstants.TO_DO,
                                                 StringConstants.ACTION,
                                                 JOptionPane.YES_NO_CANCEL_OPTION,
                                                 JOptionPane.QUESTION_MESSAGE,
                                                 null,     //do not use a custom Icon
                                                 options,  //the titles of buttons
                                                 options[0]); //default button title

            switch(selection)
            {
                case 0:
                    confirmPane = new JOptionPane(StringConstants.SHUTDOWN_CONFIRM, JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
                    confirmDialog = confirmPane.createDialog(container, e.getActionCommand());
                    confirmDialog.setIconImage(AppCore.getSystemImage());
                    confirmDialog.show();
                    if(confirmPane.getValue().toString().equals("0"))
                    {
                        AppCore.destroy();
                        Runtime runtime = Runtime.getRuntime();
                        try {
                            Process proc = runtime.exec(StringConstants.SHUTDOWN_CMD);
                        }
                        catch (IOException ex) {
                            AppCore.getFileLogger().log(ex.getMessage());
                        }
                        System.exit(0);
                    }
                    break;
                case 1:
                    confirmPane = new JOptionPane(StringConstants.RESTART_CONFIRM, JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
                    confirmDialog = confirmPane.createDialog(container, e.getActionCommand());
                    confirmDialog.setIconImage(AppCore.getSystemImage());
                    confirmDialog.show();
                    if(confirmPane.getValue().toString().equals("0"))
                    {
                        AppCore.destroy();
                        Runtime runtime = Runtime.getRuntime();
                        try {
                            Process proc = runtime.exec(StringConstants.RESTART_CMD);
                        }
                        catch (IOException ex) {
                            AppCore.getFileLogger().log(ex.getMessage());
                        }
                        System.exit(0);
                    }
                    break;
                case 2:
                    // Do nothing
                    break;
            }*/
        }
    }

    public Container getContainer()
    {
        return container;
    }

    public GraphicsDevice getDevice()
    {
        return device;
    }
}
