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
public class Drug {
    private int patientID;
    private int encounterID;
    private int formID;
    private int providerID;
    private int creator;
    private int locationID;
    private String location;
    private String uuid;
    private String pepfarID;
    private String hospID;
    private Date dispensedDate;
    private String pmmForm;
    private String dispensedBy;
    private String enteredBy;
    private Date dateEntered;
    private String drugName;
    private int conceptID;
    private String drugStrength;
    private String otherDrugStrength;
    private double singleDosePrescriptionValue;
    private String singleDosePrescriptionUnit;
    private String drugFrequency;
    private double drugDurationValue;
    private String drugDurationUnit;
    private double quantityDispensedValue;
    private String quantityDispensedUnit;
    private double quantityPrescribedValue;
    private String quantityPrescribedUnit;

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
     * @return the creator
     */
    public int getCreator() {
        return creator;
    }

    /**
     * @param creator the creator to set
     */
    public void setCreator(int creator) {
        this.creator = creator;
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
     * @return the drugStrength
     */
    public String getDrugStrength() {
        return drugStrength;
    }

    /**
     * @param drugStrength the drugStrength to set
     */
    public void setDrugStrength(String drugStrength) {
        this.drugStrength = drugStrength;
    }

    /**
     * @return the otherDrugStrength
     */
    public String getOtherDrugStrength() {
        return otherDrugStrength;
    }

    /**
     * @param otherDrugStrength the otherDrugStrength to set
     */
    public void setOtherDrugStrength(String otherDrugStrength) {
        this.otherDrugStrength = otherDrugStrength;
    }

    /**
     * @return the singleDosePrescriptionValue
     */
    public double getSingleDosePrescriptionValue() {
        return singleDosePrescriptionValue;
    }

    /**
     * @param singleDosePrescriptionValue the singleDosePrescriptionValue to set
     */
    public void setSingleDosePrescriptionValue(double singleDosePrescriptionValue) {
        this.singleDosePrescriptionValue = singleDosePrescriptionValue;
    }

    /**
     * @return the singleDosePrescriptionUnit
     */
    public String getSingleDosePrescriptionUnit() {
        return singleDosePrescriptionUnit;
    }

    /**
     * @param singleDosePrescriptionUnit the singleDosePrescriptionUnit to set
     */
    public void setSingleDosePrescriptionUnit(String singleDosePrescriptionUnit) {
        this.singleDosePrescriptionUnit = singleDosePrescriptionUnit;
    }

    /**
     * @return the drugFrequency
     */
    public String getDrugFrequency() {
        return drugFrequency;
    }

    /**
     * @param drugFrequency the drugFrequency to set
     */
    public void setDrugFrequency(String drugFrequency) {
        this.drugFrequency = drugFrequency;
    }

    /**
     * @return the drugDurationValue
     */
    public double getDrugDurationValue() {
        return drugDurationValue;
    }

    /**
     * @param drugDurationValue the drugDurationValue to set
     */
    public void setDrugDurationValue(double drugDurationValue) {
        this.drugDurationValue = drugDurationValue;
    }

    /**
     * @return the drugDurationUnit
     */
    public String getDrugDurationUnit() {
        return drugDurationUnit;
    }

    /**
     * @param drugDurationUnit the drugDurationUnit to set
     */
    public void setDrugDurationUnit(String drugDurationUnit) {
        this.drugDurationUnit = drugDurationUnit;
    }

    /**
     * @return the quantityDispensedValue
     */
    public double getQuantityDispensedValue() {
        return quantityDispensedValue;
    }

    /**
     * @param quantityDispensedValue the quantityDispensedValue to set
     */
    public void setQuantityDispensedValue(double quantityDispensedValue) {
        this.quantityDispensedValue = quantityDispensedValue;
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
     * @return the quantityPrescribedValue
     */
    public double getQuantityPrescribedValue() {
        return quantityPrescribedValue;
    }

    /**
     * @param quantityPrescribedValue the quantityPrescribedValue to set
     */
    public void setQuantityPrescribedValue(double quantityPrescribedValue) {
        this.quantityPrescribedValue = quantityPrescribedValue;
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
    
    
    
}
