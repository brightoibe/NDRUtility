/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.datapump;

import java.util.Date;

/**
 *
 * @author The Bright
 */
public class BiometricInfo {

    /**
     * @return the biometricInfoID
     */
    public int getBiometricInfoID() {
        return biometricInfoID;
    }

    /**
     * @param biometricInfoID the biometricInfoID to set
     */
    public void setBiometricInfoID(int biometricInfoID) {
        this.biometricInfoID = biometricInfoID;
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
     * @return the template
     */
    public String getTemplate() {
        return template;
    }

    /**
     * @param template the template to set
     */
    public void setTemplate(String template) {
        this.template = template;
    }

    /**
     * @return the fingerPosition
     */
    public String getFingerPosition() {
        return fingerPosition;
    }

    /**
     * @param fingerPosition the fingerPosition to set
     */
    public void setFingerPosition(String fingerPosition) {
        this.fingerPosition = fingerPosition;
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
    private int biometricInfoID;
    private int patientID;
    private String template;
    private String fingerPosition;
    private Date dateCreated;
    private int creator;
}
