/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author brightoibe
 */
public class VitalSign {
    private int patientID;
    private String pepfarID;
    private String hospID;
    private String visitDate;
    private int conceptID;
    private String vitalSignName;
    private String vitalSignValue;
    private int locationID;
    private int formID;
    private int creatorID;
    private String creatorName;
    private String formName;
    private String dateCreated;
    
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
     * @return the visitDate
     */
    public String getVisitDate() {
        return visitDate;
    }

    /**
     * @param visitDate the visitDate to set
     */
    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    /**
     * @return the vitalSign
     */
    public int getVitalSign() {
        return conceptID;
    }

    /**
     * @param vitalSign the vitalSign to set
     */
    public void setVitalSign(int vitalSign) {
        this.conceptID = vitalSign;
    }

    /**
     * @return the vitalSignValue
     */
    public String getVitalSignValue() {
        return vitalSignValue;
    }

    /**
     * @param vitalSignValue the vitalSignValue to set
     */
    public void setVitalSignValue(String vitalSignValue) {
        this.vitalSignValue = vitalSignValue;
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
     * @return the vitalSignName
     */
    public String getVitalSignName() {
        return vitalSignName;
    }

    /**
     * @param vitalSignName the vitalSignName to set
     */
    public void setVitalSignName(String vitalSignName) {
        this.vitalSignName = vitalSignName;
    }

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

    /**
     * @return the creatorName
     */
    public String getCreatorName() {
        return creatorName;
    }

    /**
     * @param creatorName the creatorName to set
     */
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
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
}
