/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pesofare.listeners.key;


import java.awt.KeyEventDispatcher;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import pesofare.constants.StringConstants;
import pesofare.core.AppCore;
import pesofare.globals.GlobalVariables;

/**
 *
 * @author Abner
 */
public class KeyboardListener implements KeyEventDispatcher{

    Robot robot;
    public KeyboardListener()
    {
        try {
            robot = new Robot();
         } catch (Exception ex) {
            AppCore.getFileLogger().log(ex.getMessage());
         }
    }

    public boolean dispatchKeyEvent(KeyEvent e) {

        if(GlobalVariables.SCREEN_LOCKED)
        {
            if (e.getID() == KeyEvent.KEY_PRESSED)
            {                
                if(e.isAltDown() || e.isControlDown() || e.getKeyCode() == KeyEvent.VK_WINDOWS)
                {
                    robot.keyRelease(KeyEvent.VK_WINDOWS);
                    robot.keyRelease(KeyEvent.VK_CONTROL);
                    robot.keyRelease(KeyEvent.VK_ALT);
                }
            }
            else if (e.getID() == KeyEvent.KEY_RELEASED)
            {
                if(e.getKeyCode() == KeyEvent.VK_P && e.isShiftDown())
                {
                    JTextField username = new JTextField();
                    JPasswordField password = new JPasswordField();
                    final JComponent[] inputs = new JComponent[] {
                                new JLabel(StringConstants.USERNAME),
                                username,
                                new JLabel(StringConstants.PASSWORD),
                                password
                            };

                    JOptionPane.showMessageDialog(AppCore.getAppFrame().getLockFrame().getContainer(), inputs, StringConstants.LOGIN, JOptionPane.PLAIN_MESSAGE);
                    if(AppCore.checkCredentials(username.getText(), password.getText()))
                    {
                        GlobalVariables.HOURS = Integer.parseInt(GlobalVariables.system.getString("login_time"));
                        AppCore.getAppFrame().getLockFrame().unlockScreen();
                    }
                }
            } 
        }

        return false;
    }

}
