
import java.util.logging.Level;
import java.util.logging.Logger;
import pesofare.com.CommConnector;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author abner
 */
public class Test {

    public static void main(String[] args)
    {
          CommConnector comm = CommConnector.getInstance();
          byte[] data = {0x02};
          byte[] read = new byte[5];
          while(true)
          {
            try {
                comm.write(data);
                int len = comm.read(read);
               
                    System.out.println(data);
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
    }

}
