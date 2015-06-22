/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pesofare.dao;

import pesofare.constants.StringConstants;
import pesofare.core.AppCore;
import pesofare.globals.GlobalVariables;

/**
 *
 * @author Abner
 */
public class PesoFareData {

    private String dateCreated;
    private String computerName;
    private String macAddress;
    private String value;
    private String userId;

    public PesoFareData()
    {
        dateCreated = AppCore.getCurrentDateTime(StringConstants.LOGS_DATE_FORMAT);
        computerName = GlobalVariables.config.getString("computer_name");
        macAddress = AppCore.getMacAddress();
        userId="0";
        value="";
    }

    /**
     * @return the dateCreated
     */
    public String getDateCreated() {
        return dateCreated;
    }

    /**
     * @param dateCreated the dateCreated to set
     */
    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * @return the computerName
     */
    public String getComputerName() {
        return computerName;
    }

    /**
     * @param computerName the computerName to set
     */
    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    /**
     * @return the macAddress
     */
    public String getMacAddress() {
        return macAddress;
    }

    /**
     * @param macAddress the macAddress to set
     */
    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getInserSQL(String tableName)
    {
        String sql;

        sql = "INSERT INTO " + tableName + "";
        return sql;
    }
}
