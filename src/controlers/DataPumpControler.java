/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlers;

import daos.DataPumpDao;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import model.datapump.DBConnection;
import model.datapump.DisplayScreen;
import model.datapump.Location;

/**
 *
 * @author brightoibe
 */
public class DataPumpControler {
    private DisplayScreen screen;
    private DataPumpDao dao;
    DBConnection con=new DBConnection();
    public DataPumpControler(){
        dao=new DataPumpDao();
    }
    
    public DefaultComboBoxModel getExportTypeModel() {
        DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
        /*String[] export_types = {
            "DATA WAREHOUSE XML",
            "MONTHLY EXPORT",
            "INDICATOR EXPORT",
            "NDR",
            "ADULTNIGERIAQUAL",
            "PEDNIGERIAQUAL",
            "DISPENSED DRUGS",
            "PATIENT REGIMEN",
            "APPOINTMENTS",
            "LAB",
        };*/
        String[] export_types = {
            "NDR", };
        
        for (String ele : export_types) {
            comboBoxModel.addElement(ele);
        }
        return comboBoxModel;
    }
    public DefaultComboBoxModel getEncryptionTypeModel(){
         DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
        String[] export_types = {
            "ENCRYPTED EXPORT",
            "PLAIN EXPORT",
            };
        for (String ele : export_types) {
            comboBoxModel.addElement(ele);
        }
        return comboBoxModel;
    }
    public void setDisplay(model.datapump.DisplayScreen screen){
        this.screen=screen;
        dao.setDisplay(screen);
    }
    public boolean connect(String username,String pass, String port, String host){
         boolean ans=false;
         con.setUsername(username);
         con.setPassword(pass);
         con.setMysqlPort(port);
         con.setHostName(host);
         if(dao.loadDriver()){
             if(dao.connect(con)){
                 screen.updateStatus("Connection successfull");
                 ans=true;
             }
              
         }
          
         return ans;
         
    }
    public void runDataPump(Date startDate, Date endDate, Location loc,String reportType,String encrypt,File file,File datimIDFile){
        dao.runReport(startDate, endDate, loc, encrypt, file, reportType,datimIDFile);
    }
    public DefaultComboBoxModel getlocationComboModel(){
        DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
        ArrayList<Location> locList=dao.getLocations();
        for(Location ele: locList){
            comboBoxModel.addElement(ele);
        }
        return comboBoxModel;
    }
    public void closeAllResources(){
        dao.closeConnections();
    }
    
}
