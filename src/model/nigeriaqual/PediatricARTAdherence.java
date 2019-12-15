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
public class PediatricARTAdherence {
    private String artAdherenceAssessmentPerformedDuringLast3Months;
    private Date lastDateOfAssessment;
    private int facilityID;
    private String uploaderID;
    private Date uploadDate;
    private String patientID;
    private String webUploadFlag;
    private int reviewPeriodID;

    /**
     * @return the artAdherenceAssessmentPerformedDuringLast3Months
     */
    public String getArtAdherenceAssessmentPerformedDuringLast3Months() {
        return artAdherenceAssessmentPerformedDuringLast3Months;
    }

    /**
     * @param artAdherenceAssessmentPerformedDuringLast3Months the artAdherenceAssessmentPerformedDuringLast3Months to set
     */
    public void setArtAdherenceAssessmentPerformedDuringLast3Months(String artAdherenceAssessmentPerformedDuringLast3Months) {
        this.artAdherenceAssessmentPerformedDuringLast3Months = artAdherenceAssessmentPerformedDuringLast3Months;
    }

    /**
     * @return the lastDateOfAssessment
     */
    public Date getLastDateOfAssessment() {
        return lastDateOfAssessment;
    }

    /**
     * @param lastDateOfAssessment the lastDateOfAssessment to set
     */
    public void setLastDateOfAssessment(Date lastDateOfAssessment) {
        this.lastDateOfAssessment = lastDateOfAssessment;
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
