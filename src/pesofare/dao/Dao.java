/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pesofare.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import pesofare.core.AppCore;
import pesofare.globals.GlobalVariables;

/**
 *
 * @author Abner
 */
public class Dao {

    private Connection connect;
    private Statement statement;

    private String driver = GlobalVariables.system.getString("driver");
    private String user = GlobalVariables.system.getString("db_user");
    private String password = GlobalVariables.system.getString("db_pass");
    private String host = GlobalVariables.system.getString("host");
    private String db = GlobalVariables.system.getString("database");
    private String port = GlobalVariables.system.getString("port");

    public boolean connect()
    {
         boolean result = true;
         try
         {
            Class.forName(driver);                        
            connect = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db, user, password);
         }
         catch (Exception e)
         {
            AppCore.getFileLogger().log(e.getMessage());
            result = false;
         }
         return result;
    }

    public boolean log(PesoFareData data)
    {
        return insertData(data);
    }

    public boolean insertData(PesoFareData data)
    {
        boolean result = false;
        String sql = null;

        if(connect())
        {
            try
            {
                statement = connect.createStatement();
                sql = autoGenerateInsertSql(data);
                if(0 != statement.executeUpdate(sql))
                {
                    result = true;
                }             
            } 
            catch (SQLException ex)
            {
                AppCore.getFileLogger().log(ex.getMessage());
            }           
        }

        return result;
    }

    public String autoGenerateInsertSql(PesoFareData data)
    {
        String sql = "INSERT INTO pesofare_profit (date_created,computer_name,computer_mac_address,value) VALUES(";
        
        sql += quoteSmart(data.getDateCreated()) + ", ";
        sql += quoteSmart(data.getComputerName()) + ", ";
        sql += quoteSmart(data.getMacAddress()) + ", ";
        sql += quoteSmart(data.getValue()) + ") ";
        
        return sql;
    }

    protected String quoteSmart(String value)
    {
        return "'" + value + "'";
    }
}