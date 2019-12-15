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
public class DataARTRecord {
    private String patientID;
    private String patientEverStartedOnART;
    private Date artStartDate;
    private String treatmentPrepationCompletedBeforeARTStart;
    private int facilityID;
    private String uploaderID;
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
     * @return the patientEverStartedOnART
     */
    public String getPatientEverStartedOnART() {
        return patientEverStartedOnART;
    }

    /**
     * @param patientEverStartedOnART the patientEverStartedOnART to set
     */
    public void setPatientEverStartedOnART(String patientEverStartedOnART) {
        this.patientEverStartedOnART = patientEverStartedOnART;
    }

    /**
     * @return the artStartDate
     */
    public Date getArtStartDate() {
        return artStartDate;
    }

    /**
     * @param artStartDate the artStartDate to set
     */
    public void setArtStartDate(Date artStartDate) {
        this.artStartDate = artStartDate;
    }

    /**
     * @return the treatmentPrepationCompletedBeforeARTStart
     */
    public String getTreatmentPrepationCompletedBeforeARTStart() {
        return treatmentPrepationCompletedBeforeARTStart;
    }

    /**
     * @param treatmentPrepationCompletedBeforeARTStart the treatmentPrepationCompletedBeforeARTStart to set
     */
    public void setTreatmentPrepationCompletedBeforeARTStart(String treatmentPrepationCompletedBeforeARTStart) {
        this.treatmentPrepationCompletedBeforeARTStart = treatmentPrepationCompletedBeforeARTStart;
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
     * @return the uploaderID
     */
    public String getUploaderID() {
        return uploaderID;
    }

    /**
     * @param uploaderID the uploaderID to set
     */
    public void setUploaderID(String uploaderID) {
        this.uploaderID = uploaderID;
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
