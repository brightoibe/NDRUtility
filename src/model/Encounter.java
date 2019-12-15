/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;

/**
 *
 * @author The Bright
 */
public class Encounter {
    private int encounterID;
    private int patientID;
    private int formID;
    private int encounterType;
    private int providerID;
    private int locationID;
    private Date encounterDate;
    private int creator;
    private Date dateCreated;
    private int voided;
    private int voidedBy;
    private Date dateVoided;
    private String voidedReason;
    private String uuid;
    private int changedBy;
    private Date dateChanged;

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
     * @return the encounterType
     */
    public int getEncounterType() {
        return encounterType;
    }

    /**
     * @param encounterType the encounterType to set
     */
    public void setEncounterType(int encounterType) {
        this.encounterType = encounterType;
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
     * @return the encounterDate
     */
    public Date getEncounterDate() {
        return encounterDate;
    }

    /**
     * @param encounterDate the encounterDate to set
     */
    public void setEncounterDate(Date encounterDate) {
        this.encounterDate = encounterDate;
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
     * @return the voided
     */
    public int getVoided() {
        return voided;
    }

    /**
     * @param voided the voided to set
     */
    public void setVoided(int voided) {
        this.voided = voided;
    }

    /**
     * @return the voidedBy
     */
    public int getVoidedBy() {
        return voidedBy;
    }

    /**
     * @param voidedBy the voidedBy to set
     */
    public void setVoidedBy(int voidedBy) {
        this.voidedBy = voidedBy;
    }

    /**
     * @return the dateVoided
     */
    public Date getDateVoided() {
        return dateVoided;
    }

    /**
     * @param dateVoided the dateVoided to set
     */
    public void setDateVoided(Date dateVoided) {
        this.dateVoided = dateVoided;
    }

    /**
     * @return the voidedReason
     */
    public String getVoidedReason() {
        return voidedReason;
    }

    /**
     * @param voidedReason the voidedReason to set
     */
    public void setVoidedReason(String voidedReason) {
        this.voidedReason = voidedReason;
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
     * @return the changedBy
     */
    public int getChangedBy() {
        return changedBy;
    }

    /**
     * @param changedBy the changedBy to set
     */
    public void setChangedBy(int changedBy) {
        this.changedBy = changedBy;
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
}
