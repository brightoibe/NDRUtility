/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.datapump;

import java.util.Date;

/**
 *
 * @author brightoibe
 */
public class Form implements Comparable<Form> {
    private int formID;
    private int patientID;
    private int creatorID;
    private String formName;
    private Date visitDate;
    private Date dateEntered;
    private Date dateChanged;
    private String enteredBy;
    private String pepfarID;
    private String hospID;

    /**
     * @return the formID
     */
    public int getFormID() {
        return formID;
    }

    /**
     * @param formID the formID to set
     */
    public void setFormID(int formID) {
        this.formID = formID;
    }

    /**
     * @return the formName
     */
    public String getFormName() {
        return formName;
    }

    /**
     * @param formName the formName to set
     */
    public void setFormName(String formName) {
        this.formName = formName;
    }

    /**
     * @return the visitDate
     */
    public Date getVisitDate() {
        return visitDate;
    }

    /**
     * @param visitDate the visitDate to set
     */
    public void setVisitDate(Date visitDate) {
        this.visitDate = visitDate;
    }

    /**
     * @return the dateEntered
     */
    public Date getDateEntered() {
        return dateEntered;
    }

    /**
     * @param dateEntered the dateEntered to set
     */
    public void setDateEntered(Date dateEntered) {
        this.dateEntered = dateEntered;
    }

    /**
     * @return the enteredBy
     */
    public String getEnteredBy() {
        return enteredBy;
    }

    /**
     * @param enteredBy the enteredBy to set
     */
    public void setEnteredBy(String enteredBy) {
        this.enteredBy = enteredBy;
    }

    /**
     * @return the pepfarID
     */
    public String getPepfarID() {
        return pepfarID;
    }

    /**
     * @param pepfarID the pepfarID to set
     */
    public void setPepfarID(String pepfarID) {
        this.pepfarID = pepfarID;
    }

    /**
     * @return the hospID
     */
    public String getHospID() {
        return hospID;
    }

    /**
     * @param hospID the hospID to set
     */
    public void setHospID(String hospID) {
        this.hospID = hospID;
    }

    @Override
    public int compareTo(Form o) {
        return this.getVisitDate().compareTo(o.getVisitDate());
    }
    
    @Override
    public boolean equals(Object obj){
        boolean ans=false;
        if(!(obj instanceof Form)){
            return false;
        }
        Form o=(Form)obj;
        if(this.patientID==o.patientID && this.visitDate==o.visitDate && this.formID==o.formID){
            ans=true;
        }
        return ans;
    }
    
    @Override
    public int hashCode(){
        return this.patientID*this.formID;
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
     * @return the patientID
     */
    public int getPatientID() {
        return patientID;
    }

    /**
     * @param patientID the patientID to set
     */
    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    /**
     * @return the creatorID
     */
    public int getCreatorID() {
        return creatorID;
    }

    /**
     * @param creatorID the creatorID to set
     */
    public void setCreatorID(int creatorID) {
        this.creatorID = creatorID;
    }
    
}
