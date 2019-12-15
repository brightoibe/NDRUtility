/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.datapump;

import java.util.Date;

/**
 *
 * @author Bright
 */
public class Drugs {
    protected int patientID;
    protected int encounterID;
    protected int formID;
    protected int providerID;
    protected int creatorID;
    protected int locationID;
    private int orderID;
    protected String location;
    protected String pepfarID;
    protected String hospID;
    protected String pmmForm;
    protected Date dispensedDate;
    protected String dispensedBy;
    protected String enteredBy;
    protected Date dateEntered;
    protected String drugName;
    protected int conceptID;
    protected String strength;
    protected String otherStrength;
    protected double singleDoseValue;
    protected String singleDoseUnit;
    protected String frequency;
    protected int duration;
    protected String durationUnit;
    protected int quantityPrescribed;
    protected String quantityPrescribedUnit;
    protected int quantityDispensed;
    protected String quantityDispensedUnit;
    private Date stopDate;
    protected String uuid;

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
     * @return the dispensedDate
     */
    public Date getDispensedDate() {
        return dispensedDate;
    }

    /**
     * @param dispensedDate the dispensedDate to set
     */
    public void setDispensedDate(Date dispensedDate) {
        this.dispensedDate = dispensedDate;
    }

    /**
     * @return the dispensedBy
     */
    public String getDispensedBy() {
        return dispensedBy;
    }

    /**
     * @param dispensedBy the dispensedBy to set
     */
    public void setDispensedBy(String dispensedBy) {
        this.dispensedBy = dispensedBy;
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
     * @return the drugName
     */
    public String getDrugName() {
        return drugName;
    }

    /**
     * @param drugName the drugName to set
     */
    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    /**
     * @return the conceptID
     */
    public int getConceptID() {
        return conceptID;
    }

    /**
     * @param conceptID the conceptID to set
     */
    public void setConceptID(int conceptID) {
        this.conceptID = conceptID;
    }

    /**
     * @return the strength
     */
    public String getStrength() {
        return strength;
    }

    /**
     * @param strength the strength to set
     */
    public void setStrength(String strength) {
        this.strength = strength;
    }

    /**
     * @return the otherStrength
     */
    public String getOtherStrength() {
        return otherStrength;
    }

    /**
     * @param otherStrength the otherStrength to set
     */
    public void setOtherStrength(String otherStrength) {
        this.otherStrength = otherStrength;
    }

    /**
     * @return the singleDoseValue
     */
    public double getSingleDoseValue() {
        return singleDoseValue;
    }

    /**
     * @param singleDoseValue the singleDoseValue to set
     */
    public void setSingleDoseValue(double singleDoseValue) {
        this.singleDoseValue = singleDoseValue;
    }

    /**
     * @return the frequency
     */
    public String getFrequency() {
        return frequency;
    }

    /**
     * @param frequency the frequency to set
     */
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    /**
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * @return the durationUnit
     */
    public String getDurationUnit() {
        return durationUnit;
    }

    /**
     * @param durationUnit the durationUnit to set
     */
    public void setDurationUnit(String durationUnit) {
        this.durationUnit = durationUnit;
    }

    /**
     * @return the quantityPrescribed
     */
    public int getQuantityPrescribed() {
        return quantityPrescribed;
    }

    /**
     * @param quantityPrescribed the quantityPrescribed to set
     */
    public void setQuantityPrescribed(int quantityPrescribed) {
        this.quantityPrescribed = quantityPrescribed;
    }

    /**
     * @return the quantityPrescribedUnit
     */
    public String getQuantityPrescribedUnit() {
        return quantityPrescribedUnit;
    }

    /**
     * @param quantityPrescribedUnit the quantityPrescribedUnit to set
     */
    public void setQuantityPrescribedUnit(String quantityPrescribedUnit) {
        this.quantityPrescribedUnit = quantityPrescribedUnit;
    }

    /**
     * @return the quantityDispensed
     */
    public int getQuantityDispensed() {
        return quantityDispensed;
    }

    /**
     * @param quantityDispensed the quantityDispensed to set
     */
    public void setQuantityDispensed(int quantityDispensed) {
        this.quantityDispensed = quantityDispensed;
    }

    /**
     * @return the quantityDispensedUnit
     */
    public String getQuantityDispensedUnit() {
        return quantityDispensedUnit;
    }

    /**
     * @param quantityDispensedUnit the quantityDispensedUnit to set
     */
    public void setQuantityDispensedUnit(String quantityDispensedUnit) {
        this.quantityDispensedUnit = quantityDispensedUnit;
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
     * @return the singleDoseUnit
     */
    public String getSingleDoseUnit() {
        return singleDoseUnit;
    }

    /**
     * @param singleDoseUnit the singleDoseUnit to set
     */
    public void setSingleDoseUnit(String singleDoseUnit) {
        this.singleDoseUnit = singleDoseUnit;
    }

    /**
     * @return the orderID
     */
    public int getOrderID() {
        return orderID;
    }

    /**
     * @param orderID the orderID to set
     */
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    /**
     * @return the stopDate
     */
    public Date getStopDate() {
        return stopDate;
    }

    /**
     * @param stopDate the stopDate to set
     */
    public void setStopDate(Date stopDate) {
        this.stopDate = stopDate;
    }
}
