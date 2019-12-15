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
public class ARTCommence {
    private int patientID;
    private int encounterID;
    private int formID;
    private int creatorID;
    private int providerID;
    private int locationID;
    private String uuid;
    private String pepfarID;
    private String hospID;
    private Date visitDate;
    private String pmmForm;
    private String location;
    private String enteredBy;
    private Date dateEntered;
    private String providerName;
    private Date dateMedicallyEligible;
    private String whyEligible;
    private Date dateInitialAdherenceCouncelingCompleted;
    private Date dateTransferedIn;
    private String facilityTransferredFrom;
    private String firstRegimenLine;
    private String firstLineRegimen;
    private String secondLineRegimen;
    private String otherRegimen;
    private Date dateARTStarted;
    private int ageARTStart;
    private String clinicalStageAtARTStart;
    private double weight;
    private double height;
    private String functionalStatus;
    private double cd4CountAtARTStart;
    private String artRegisterSerialNo;

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
     * @return the providerName
     */
    public String getProviderName() {
        return providerName;
    }

    /**
     * @param providerName the providerName to set
     */
    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    /**
     * @return the dateMedicallyEligible
     */
    public Date getDateMedicallyEligible() {
        return dateMedicallyEligible;
    }

    /**
     * @param dateMedicallyEligible the dateMedicallyEligible to set
     */
    public void setDateMedicallyEligible(Date dateMedicallyEligible) {
        this.dateMedicallyEligible = dateMedicallyEligible;
    }

    /**
     * @return the whyEligible
     */
    public String getWhyEligible() {
        return whyEligible;
    }

    /**
     * @param whyEligible the whyEligible to set
     */
    public void setWhyEligible(String whyEligible) {
        this.whyEligible = whyEligible;
    }

    /**
     * @return the dateInitialAdherenceCouncelingCompleted
     */
    public Date getDateInitialAdherenceCouncelingCompleted() {
        return dateInitialAdherenceCouncelingCompleted;
    }

    /**
     * @param dateInitialAdherenceCouncelingCompleted the dateInitialAdherenceCouncelingCompleted to set
     */
    public void setDateInitialAdherenceCouncelingCompleted(Date dateInitialAdherenceCouncelingCompleted) {
        this.dateInitialAdherenceCouncelingCompleted = dateInitialAdherenceCouncelingCompleted;
    }

    /**
     * @return the dateTransferedIn
     */
    public Date getDateTransferedIn() {
        return dateTransferedIn;
    }

    /**
     * @param dateTransferedIn the dateTransferedIn to set
     */
    public void setDateTransferedIn(Date dateTransferedIn) {
        this.dateTransferedIn = dateTransferedIn;
    }

    /**
     * @return the facilityTransferredFrom
     */
    public String getFacilityTransferredFrom() {
        return facilityTransferredFrom;
    }

    /**
     * @param facilityTransferredFrom the facilityTransferredFrom to set
     */
    public void setFacilityTransferredFrom(String facilityTransferredFrom) {
        this.facilityTransferredFrom = facilityTransferredFrom;
    }

    /**
     * @return the firstRegimenLine
     */
    public String getFirstRegimenLine() {
        return firstRegimenLine;
    }

    /**
     * @param firstRegimenLine the firstRegimenLine to set
     */
    public void setFirstRegimenLine(String firstRegimenLine) {
        this.firstRegimenLine = firstRegimenLine;
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
     * @return the dateARTStarted
     */
    public Date getDateARTStarted() {
        return dateARTStarted;
    }

    /**
     * @param dateARTStarted the dateARTStarted to set
     */
    public void setDateARTStarted(Date dateARTStarted) {
        this.dateARTStarted = dateARTStarted;
    }

    /**
     * @return the clinicalStageAtARTStart
     */
    public String getClinicalStageAtARTStart() {
        return clinicalStageAtARTStart;
    }

    /**
     * @param clinicalStageAtARTStart the clinicalStageAtARTStart to set
     */
    public void setClinicalStageAtARTStart(String clinicalStageAtARTStart) {
        this.clinicalStageAtARTStart = clinicalStageAtARTStart;
    }

    /**
     * @return the weight
     */
    public double getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * @return the height
     */
    public double getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * @return the functionalStatus
     */
    public String getFunctionalStatus() {
        return functionalStatus;
    }

    /**
     * @param functionalStatus the functionalStatus to set
     */
    public void setFunctionalStatus(String functionalStatus) {
        this.functionalStatus = functionalStatus;
    }

    /**
     * @return the cd4CountAtARTStart
     */
    public double getCd4CountAtARTStart() {
        return cd4CountAtARTStart;
    }

    /**
     * @param cd4CountAtARTStart the cd4CountAtARTStart to set
     */
    public void setCd4CountAtARTStart(double cd4CountAtARTStart) {
        this.cd4CountAtARTStart = cd4CountAtARTStart;
    }

    /**
     * @return the artRegisterSerialNo
     */
    public String getArtRegisterSerialNo() {
        return artRegisterSerialNo;
    }

    /**
     * @param artRegisterSerialNo the artRegisterSerialNo to set
     */
    public void setArtRegisterSerialNo(String artRegisterSerialNo) {
        this.artRegisterSerialNo = artRegisterSerialNo;
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
     * @return the otherRegimenLine
     */
    public String getOtherRegimen() {
        return otherRegimen;
    }

    /**
     * @param otherRegimenLine the otherRegimenLine to set
     */
    public void setOtherRegimen(String otherRegimenLine) {
        this.otherRegimen = otherRegimenLine;
    }

    /**
     * @return the ageARTStart
     */
    public int getAgeARTStart() {
        return ageARTStart;
    }

    /**
     * @param ageARTStart the ageARTStart to set
     */
    public void setAgeARTStart(int ageARTStart) {
        this.ageARTStart = ageARTStart;
    }
   
    
    
}
