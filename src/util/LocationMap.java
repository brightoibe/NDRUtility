/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import model.datapump.Location;


/**
 *
 * @author Bright
 */
public class LocationMap {
    private HashMap<String,Integer> nigeriaQualMap;
    private HashMap<String,String> ndrMap;
    private FileManager mgr;
    
    public LocationMap(){
        nigeriaQualMap=new HashMap<String,Integer>();
        ndrMap=new HashMap<String,String>();
        mgr=new FileManager();
    }
    public void loadLocation(File file) throws IOException{
       List<String[]> data=mgr.loadDataFromFile(file);
        loadNDRLocationDictionary(data);
        loadNigeriaQualLocationDictionary(data);
       
    }
    public void loadNDRLocationDictionary(List<String[]> data){
        for(String[] ele: data){
            if(StringUtils.isNoneEmpty(ele[1])&& StringUtils.isNoneEmpty(ele[3])){
                ndrMap.put(StringUtils.trim(ele[1]), StringUtils.trim(ele[3]));
            }
        }
    }
    public void loadNigeriaQualLocationDictionary(List<String[]> data){
        for(String[] ele: data){
            if(StringUtils.isNoneEmpty(ele[1]) && StringUtils.isNoneEmpty(ele[4])){
                if(!ele[4].equalsIgnoreCase("NigeriaQualID")){
                    nigeriaQualMap.put(StringUtils.trim(ele[1]), Integer.parseInt(StringUtils.trim(ele[4])));
                    System.out.println("UUID "+ele[1]+" DATIM ID: "+ele[4]);
                }
                
            }
        }
    }
    public boolean hasNDRDatimID(Location loc){
        boolean ans=false;
        String uuid=loc.getUuid();
        if(ndrMap.containsKey(uuid)){
            ans=true;
        }
        return ans;
    }
    public boolean hasNigeriaQualID(Location loc){
        boolean ans=false;
        String uuid=loc.getUuid();
        if(nigeriaQualMap.containsKey(uuid)){
            ans=true;
        }
        return ans;
    }
    public int getNigeriaQualID(model.datapump.Location location){
        String uuid=location.getUuid();
        int id=nigeriaQualMap.get(uuid);
        return id;
    }
    public String getNDRID(model.datapump.Location location){
        String uuid=location.getUuid();
        String id=ndrMap.get(uuid);
        return id;
    }
    
    
    
}
