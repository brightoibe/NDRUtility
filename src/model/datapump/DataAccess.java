/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.datapump;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author brightoibe
 */
public interface DataAccess {
    public boolean connect(DBConnection con);
    
    public void runReport(Date startDate, Date endDate, model.datapump.Location loc, String encrypt, File file,String reportType,File datimIDFile);
    
    public ArrayList<Location> getLocations();
    
    public void setDisplay(DisplayScreen screen);
    
    public void closeConnections();
}
