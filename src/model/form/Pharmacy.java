/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.form;

import java.util.Date;

/**
 *
 * @author openmrsdev
 */
public class Pharmacy {
    private int patientID;
    private int locationID;
    private int encounterID;
    private int formID;
    private int creatorID;
    private int providerID;
    private String uuid;
    private String pepfarID;
    private String hospID;
    private Date visitDate;
    private String pmmForm;
    private String location;
    private String provider;
    private String enteredBy;
    private Date dateEntered;
    private String visitType;
    private String treatmentType;
    private String regimenLine;
    private String firstLineRegimen;
    private String secondLineRegimen;
    private String pickupReason;
    private String drugName;
    private String drugStrength;
    private String otherDrugStrength;
    private int singleDose;
    private String singleDoseUnit;
    private String frequency;
    private int duration;
    private String durationUnit;
    private int quantityPrescribed;
    private String quantityPrescribedUnit;
    private int quantityDispensed;
    private String quantityDispensedUnit;
    private String orderedBy;
    private Date dateOrdered;
    private String counseledBy;
    private Date dateCounseled;
    private String pickupBy;
    private String nameOfErrorDrug;
    private String nameOfErrors;
    private String consequenceOfErrors;

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
     * @return the firstLineRegimen
     */
    public String getFirstLineRegimen() {
        return firstLineRegimen;
    }

    /**
     * @param firstLineRegimen the firstLineRegimen to set
     */
    public void setFirstLineRegimen(String firstLineRegimen) {
        this.firstLineRegimen = firstLineRegimen;
    }

    /**
     * @return the secondLineRegimen
     */
    public String getSecondLineRegimen() {
        return secondLineRegimen;
    }

    /**
     * @param secondLineRegimen the secondLineRegimen to set
     */
    public void setSecondLineRegimen(String secondLineRegimen) {
        this.secondLineRegimen = secondLineRegimen;
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
     * @return the singleDose
     */
    public int getSingleDose() {
        return singleDose;
    }

    /**
     * @param singleDose the singleDose to set
     */
    public void setSingleDose(int singleDose) {
        this.singleDose = singleDose;
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
     * @return the pickupBy
     */
    public String getPickupBy() {
        return pickupBy;
    }

    /**
     * @param pickupBy the pickupBy to set
     */
    public void setPickupBy(String pickupBy) {
        this.pickupBy = pickupBy;
    }

    /**
     * @return the nameOfErrorDrug
     */
    public String getNameOfErrorDrug() {
        return nameOfErrorDrug;
    }

    /**
     * @param nameOfErrorDrug the nameOfErrorDrug to set
     */
    public void setNameOfErrorDrug(String nameOfErrorDrug) {
        this.nameOfErrorDrug = nameOfErrorDrug;
    }

    /**
     * @return the nameOfErrors
     */
    public String getNameOfErrors() {
        return nameOfErrors;
    }

    /**
     * @param nameOfErrors the nameOfErrors to set
     */
    public void setNameOfErrors(String nameOfErrors) {
        this.nameOfErrors = nameOfErrors;
    }

    /**
     * @return the consequenceOfErrors
     */
    public String getConsequenceOfErrors() {
        return consequenceOfErrors;
    }

    /**
     * @param consequenceOfErrors the consequenceOfErrors to set
     */
    public void setConsequenceOfErrors(String consequenceOfErrors) {
        this.consequenceOfErrors = consequenceOfErrors;
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
    
}
