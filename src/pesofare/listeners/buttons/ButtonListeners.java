/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pesofare.listeners.buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import pesofare.constants.StringConstants;
import pesofare.core.AppCore;
import pesofare.globals.GlobalVariables;

/**
 *
 * @author Abner
 */
public class ButtonListeners implements ActionListener{

    private static final ButtonListeners instance = new ButtonListeners();
    private JOptionPane confirmPane = null;
    private JDialog confirmDialog = null;

    private enum ButtonActions
    {
        Timeout;
    }
    
    public void actionPerformed(ActionEvent e) {
        
        ButtonActions button = ButtonActions.valueOf(e.getActionCommand());

        switch(button)
        {
            case Timeout:
                confirmPane = new JOptionPane(StringConstants.TIMEOUT_CONFIRM, JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
                confirmDialog = confirmPane.createDialog(AppCore.getAppFrame(), e.getActionCommand());  
                //confirmDialog.setIconImage(AppCore.getSystemImage());
                confirmDialog.setVisible(true);

                Object value = confirmPane.getValue();
                if(value != null)
                {
                    if(value.toString().equals("0"))
                    {
                        GlobalVariables.COIN_INSERTED = false;
                        GlobalVariables.HOURS = 0;
                        GlobalVariables.MINUTES = 0;
                        GlobalVariables.SECONDS = 0;

                        if(!GlobalVariables.SCREEN_LOCKED)
                        {
                            AppCore.getAppFrame().getLockFrame().lockScreen();
                        }
                    }
                }
                break;
         }
    }

    public static ButtonListeners getInstance()
    {
        return instance;
    }

}
