/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.datapump;

/**
 *
 * @author brightoibe
 */
public class Location implements Comparable<Location>{

    /**
     * @return the defaultLoacation
     */
    public String getDefaultLoacation() {
        return defaultLoacation;
    }

    /**
     * @param defaultLoacation the defaultLoacation to set
     */
    public void setDefaultLoacation(String defaultLoacation) {
        this.defaultLoacation = defaultLoacation;
    }

    /**
     * @return the datimID
     */
    public String getDatimID() {
        return datimID;
    }

    /**
     * @param datimID the datimID to set
     */
    public void setDatimID(String datimID) {
        this.datimID = datimID;
    }

    /**
     * @return the nigeriaQualID
     */
    public String getNigeriaQualID() {
        return nigeriaQualID;
    }

    /**
     * @param nigeriaQualID the nigeriaQualID to set
     */
    public void setNigeriaQualID(String nigeriaQualID) {
        this.nigeriaQualID = nigeriaQualID;
    }
    private String locationName;
    private int locationID;
    private String uuid;
    private String defaultLoacation;
    private String datimID;
    private String nigeriaQualID;
   

    /**
     * @return the locationName
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * @param locationName the locationName to set
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    /**
     * @return the locationID
     */
    public int getLocationID() {
        return locationID;
    }

    /**
     * @param locationID the locationID to set
     */
    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }
    
    @Override
    public String toString(){
        return locationName;
    }

    /**
     * @return the uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @param uuid the uuid to set
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public int compareTo(Location o) {
        return this.getLocationName().compareToIgnoreCase(o.getLocationName());
    }

   
}
