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
public class PediatricEducation {
    private String patientID;
    private String motherReceivedInfantFeedingEducation;
    private int facilityID;
    private String uploaderID;
    private Date uploadDt;
    private String webUploadFlag;
    private int reviewPeriodID;
    /*
    PatientID
MotherReceivedInfantFeedingEducation
FacilityID
UploaderId
UploadDt
webUploadFlag
ReviewPeriodID

    */

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
     * @return the motherReceivedInfantFeedingEducation
     */
    public String getMotherReceivedInfantFeedingEducation() {
        return motherReceivedInfantFeedingEducation;
    }

    /**
     * @param motherReceivedInfantFeedingEducation the motherReceivedInfantFeedingEducation to set
     */
    public void setMotherReceivedInfantFeedingEducation(String motherReceivedInfantFeedingEducation) {
        this.motherReceivedInfantFeedingEducation = motherReceivedInfantFeedingEducation;
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
