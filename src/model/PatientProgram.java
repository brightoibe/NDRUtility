/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;

/**
 *
 * @author brightoibe
 */
public class PatientProgram {
    private int patient_id;
    private int program_id;
    private String program_name;
    private Date dateEnrolled;
    private String creator;
    private Date dateCreated;
    private Date dateChanged;
    private Date dateCompleted;
    private String changedBy;
    private int locationID;
    
    
    /**
     * @return the patient_id
     */
    public int getPatient_id() {
        return patient_id;
    }

    /**
     * @param patient_id the patient_id to set
     */
    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    /**
     * @return the program_id
     */
    public int getProgram_id() {
        return program_id;
    }

    /**
     * @param program_id the program_id to set
     */
    public void setProgram_id(int program_id) {
        this.program_id = program_id;
    }

    /**
     * @return the program_name
     */
    public String getProgram_name() {
        return program_name;
    }

    /**
     * @param program_name the program_name to set
     */
    public void setProgram_name(String program_name) {
        this.program_name = program_name;
    }

    /**
     * @return the dateEnrolled
     */
    public Date getDateEnrolled() {
        return dateEnrolled;
    }

    /**
     * @param dateEnrolled the dateEnrolled to set
     */
    public void setDateEnrolled(Date dateEnrolled) {
        this.dateEnrolled = dateEnrolled;
    }

    /**
     * @return the creator
     */
    public String getCreator() {
        return creator;
    }

    /**
     * @param creator the creator to set
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * @return the dateCreated
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * @param dateCreated the dateCreated to set
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * @return the dateChanged
     */
    public Date getDateChanged() {
        return dateChanged;
    }

    /**
     * @param dateChanged the dateChanged to set
     */
    public void setDateChanged(Date dateChanged) {
        this.dateChanged = dateChanged;
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

    /**
     * @return the changedBy
     */
    public String getChangedBy() {
        return changedBy;
    }

    /**
     * @param changedBy the changedBy to set
     */
    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    /**
     * @return the dateCompleted
     */
    public Date getDateCompleted() {
        return dateCompleted;
    }

    /**
     * @param dateCompleted the dateCompleted to set
     */
    public void setDateCompleted(Date dateCompleted) {
        this.dateCompleted = dateCompleted;
    }
}
