/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.nigeriaqual;

import java.util.Date;

/**
 *
 * @author brigh
 */
public class PediatricCotrimoxazole {
    private String patientID;
private String patientCurrentlyOnCotrimoxazoleProphylaxis;
private Date dateOfFirstPrescription;
private int ageOfFirstPrescription;
private String unitOfAgeMeasure;
private int facilityID;
private String uploaderId;
private Date uploadDt;
private String webUploadFlag;
private int reviewPeriodID;

    /**
     * @return the patientID
     */
    public String getPatientID() {
        return patientID;
    }

    /**
     * @param patientID the patientID to set
     */
    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    /**
     * @return the patientCurrentlyOnCotrimoxazoleProphylaxis
     */
    public String getPatientCurrentlyOnCotrimoxazoleProphylaxis() {
        return patientCurrentlyOnCotrimoxazoleProphylaxis;
    }

    /**
     * @param patientCurrentlyOnCotrimoxazoleProphylaxis the patientCurrentlyOnCotrimoxazoleProphylaxis to set
     */
    public void setPatientCurrentlyOnCotrimoxazoleProphylaxis(String patientCurrentlyOnCotrimoxazoleProphylaxis) {
        this.patientCurrentlyOnCotrimoxazoleProphylaxis = patientCurrentlyOnCotrimoxazoleProphylaxis;
    }

    /**
     * @return the dateOfFirstPrescription
     */
    public Date getDateOfFirstPrescription() {
        return dateOfFirstPrescription;
    }

    /**
     * @param dateOfFirstPrescription the dateOfFirstPrescription to set
     */
    public void setDateOfFirstPrescription(Date dateOfFirstPrescription) {
        this.dateOfFirstPrescription = dateOfFirstPrescription;
    }

    /**
     * @return the ageOfFirstPrescription
     */
    public int getAgeOfFirstPrescription() {
        return ageOfFirstPrescription;
    }

    /**
     * @param ageOfFirstPrescription the ageOfFirstPrescription to set
     */
    public void setAgeOfFirstPrescription(int ageOfFirstPrescription) {
        this.ageOfFirstPrescription = ageOfFirstPrescription;
    }

    /**
     * @return the unitOfAgeMeasure
     */
    public String getUnitOfAgeMeasure() {
        return unitOfAgeMeasure;
    }

    /**
     * @param unitOfAgeMeasure the unitOfAgeMeasure to set
     */
    public void setUnitOfAgeMeasure(String unitOfAgeMeasure) {
        this.unitOfAgeMeasure = unitOfAgeMeasure;
    }

    /**
     * @return the facilityID
     */
    public int getFacilityID() {
        return facilityID;
    }

    /**
     * @param facilityID the facilityID to set
     */
    public void setFacilityID(int facilityID) {
        this.facilityID = facilityID;
    }

    /**
     * @return the uploaderId
     */
    public String getUploaderId() {
        return uploaderId;
    }

    /**
     * @param uploaderId the uploaderId to set
     */
    public void setUploaderId(String uploaderId) {
        this.uploaderId = uploaderId;
    }

    /**
     * @return the uploadDt
     */
    public Date getUploadDt() {
        return uploadDt;
    }

    /**
     * @param uploadDt the uploadDt to set
     */
    public void setUploadDt(Date uploadDt) {
        this.uploadDt = uploadDt;
    }

    /**
     * @return the webUploadFlag
     */
    public String getWebUploadFlag() {
        return webUploadFlag;
    }

    /**
     * @param webUploadFlag the webUploadFlag to set
     */
    public void setWebUploadFlag(String webUploadFlag) {
        this.webUploadFlag = webUploadFlag;
    }

    /**
     * @return the reviewPeriodID
     */
    public int getReviewPeriodID() {
        return reviewPeriodID;
    }

    /**
     * @param reviewPeriodID the reviewPeriodID to set
     */
    public void setReviewPeriodID(int reviewPeriodID) {
        this.reviewPeriodID = reviewPeriodID;
    }

}
