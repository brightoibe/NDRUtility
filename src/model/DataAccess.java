/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author brightoibe
 */
public interface DataAccess {
    public boolean connect(DBConnection con);
    
    public void runReport(Date startDate, Date endDate, int location, String encrypt, File file,String reportType);
    
    public ArrayList<Location> getLocations();
    
    public void setDisplay(DisplayScreen screen);
    
    public void closeConnections();
        
    
    
        
    
}
