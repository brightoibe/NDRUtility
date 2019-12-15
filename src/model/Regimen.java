/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;

/**
 *
 * @author openmrsdev
 */
public class Regimen {
    private int patientID;
    private int formID;
    private int encounterID;
    private int locationID;
    private int providerID;
    private int creatorID;
    private String pepfarID;
    private String hospID;
    private Date visitDate;
    private String location;
    private String pmmForm;
    private String visitType;
    private String pickupReason;
    private String treatmentType;
    
    
    private String regimenLine;
    private String firstLine;
    private String secondLine;
    private int regimenCode;
    private String otherLine;
    
    private String orderedBy;
    private Date dateOrdered;
    private Date dateCounseled;
    private String counseledBy;
    
    private String provider;
    private String enteredBy;
    private Date dateEntered;
    private Date dateChanged;
    private String uuid;

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
     * @return the encounterID
     */
    public int getEncounterID() {
        return encounterID;
    }

    /**
     * @param encounterID the encounterID to set
     */
    public void setEncounterID(int encounterID) {
        this.encounterID = encounterID;
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
     * @return the providerID
     */
    public int getProviderID() {
        return providerID;
    }

    /**
     * @param providerID the providerID to set
     */
    public void setProviderID(int providerID) {
        this.providerID = providerID;
    }

    /**
     * @return the creatoriD
     */
    public int getCreatoriD() {
        return creatorID;
    }

    /**
     * @param creatoriD the creatoriD to set
     */
    public void setCreatoriD(int creatoriD) {
        this.creatorID = creatoriD;
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
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the pmmForm
     */
    public String getPmmForm() {
        return pmmForm;
    }

    /**
     * @param pmmForm the pmmForm to set
     */
    public void setPmmForm(String pmmForm) {
        this.pmmForm = pmmForm;
    }

    /**
     * @return the visitType
     */
    public String getVisitType() {
        return visitType;
    }

    /**
     * @param visitType the visitType to set
     */
    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    /**
     * @return the regimenLine
     */
    public String getRegimenLine() {
        return regimenLine;
    }

    /**
     * @param regimenLine the regimenLine to set
     */
    public void setRegimenLine(String regimenLine) {
        this.regimenLine = regimenLine;
    }

    /**
     * @return the firstLine
     */
    public String getFirstLine() {
        return firstLine;
    }

    /**
     * @param firstLine the firstLine to set
     */
    public void setFirstLine(String firstLine) {
        this.firstLine = firstLine;
    }

    /**
     * @return the secondLine
     */
    public String getSecondLine() {
        return secondLine;
    }

    /**
     * @param secondLine the secondLine to set
     */
    public void setSecondLine(String secondLine) {
        this.secondLine = secondLine;
    }

    /**
     * @return the otherLine
     */
    public String getOtherLine() {
        return otherLine;
    }

    /**
     * @param otherLine the otherLine to set
     */
    public void setOtherLine(String otherLine) {
        this.otherLine = otherLine;
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
     * @return the provider
     */
    public String getProvider() {
        return provider;
    }

    /**
     * @param provider the provider to set
     */
    public void setProvider(String provider) {
        this.provider = provider;
    }

    /**
     * @return the regimenCode
     */
    public int getRegimenCode() {
        return regimenCode;
    }

    /**
     * @param regimenCode the regimenCode to set
     */
    public void setRegimenCode(int regimenCode) {
        this.regimenCode = regimenCode;
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
     * @return the pickupReason
     */
    public String getPickupReason() {
        return pickupReason;
    }

    /**
     * @param pickupReason the pickupReason to set
     */
    public void setPickupReason(String pickupReason) {
        this.pickupReason = pickupReason;
    }

    /**
     * @return the treatmentType
     */
    public String getTreatmentType() {
        return treatmentType;
    }

    /**
     * @param treatmentType the treatmentType to set
     */
    public void setTreatmentType(String treatmentType) {
        this.treatmentType = treatmentType;
    }

    /**
     * @return the orderedBy
     */
    public String getOrderedBy() {
        return orderedBy;
    }

    /**
     * @param orderedBy the orderedBy to set
     */
    public void setOrderedBy(String orderedBy) {
        this.orderedBy = orderedBy;
    }

    /**
     * @return the dateOrdered
     */
    public Date getDateOrdered() {
        return dateOrdered;
    }

    /**
     * @param dateOrdered the dateOrdered to set
     */
    public void setDateOrdered(Date dateOrdered) {
        this.dateOrdered = dateOrdered;
    }

    /**
     * @return the dateCounseled
     */
    public Date getDateCounseled() {
        return dateCounseled;
    }

    /**
     * @param dateCounseled the dateCounseled to set
     */
    public void setDateCounseled(Date dateCounseled) {
        this.dateCounseled = dateCounseled;
    }

    /**
     * @return the counseledBy
     */
    public String getCounseledBy() {
        return counseledBy;
    }

    /**
     * @param counseledBy the counseledBy to set
     */
    public void setCounseledBy(String counseledBy) {
        this.counseledBy = counseledBy;
    }
    
}
