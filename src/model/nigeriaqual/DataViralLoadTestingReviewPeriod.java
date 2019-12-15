/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.nigeriaqual;

import java.util.Date;

/**
 *
 * @author brightoibe
 */
public class DataViralLoadTestingReviewPeriod {
     private String patientID;
     private String hasPatientReceivedVLTesting;
     private Date vlTestingDate;
     private double vlResult;
     private String facilityID;
     private int uploadID;
     private Date uploadDate;
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
     * @return the hasPatientReceivedVLTesting
     */
    public String getHasPatientReceivedVLTesting() {
        return hasPatientReceivedVLTesting;
    }

    /**
     * @param hasPatientReceivedVLTesting the hasPatientReceivedVLTesting to set
     */
    public void setHasPatientReceivedVLTesting(String hasPatientReceivedVLTesting) {
        this.hasPatientReceivedVLTesting = hasPatientReceivedVLTesting;
    }

    /**
     * @return the vlTestingDate
     */
    public Date getVlTestingDate() {
        return vlTestingDate;
    }

    /**
     * @param vlTestingDate the vlTestingDate to set
     */
    public void setVlTestingDate(Date vlTestingDate) {
        this.vlTestingDate = vlTestingDate;
    }

    /**
     * @return the vlResult
     */
    public double getVlResult() {
        return vlResult;
    }

    /**
     * @param vlResult the vlResult to set
     */
    public void setVlResult(double vlResult) {
        this.vlResult = vlResult;
    }

    /**
     * @return the facilityID
     */
    public String getFacilityID() {
        return facilityID;
    }

    /**
     * @param facilityID the facilityID to set
     */
    public void setFacilityID(String facilityID) {
        this.facilityID = facilityID;
    }

    /**
     * @return the uploadID
     */
    public int getUploadID() {
        return uploadID;
    }

    /**
     * @param uploadID the uploadID to set
     */
    public void setUploadID(int uploadID) {
        this.uploadID = uploadID;
    }

    /**
     * @return the uploadDate
     */
    public Date getUploadDate() {
        return uploadDate;
    }

    /**
     * @param uploadDate the uploadDate to set
     */
    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
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
