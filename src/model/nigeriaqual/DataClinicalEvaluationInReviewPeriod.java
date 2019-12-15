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
public class DataClinicalEvaluationInReviewPeriod {
    private String patientID;
    private Date visit1;
    private Date visit2;
    private Date visit3;
    private Date visit4;
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
     * @return the visit1
     */
    public Date getVisit1() {
        return visit1;
    }

    /**
     * @param visit1 the visit1 to set
     */
    public void setVisit1(Date visit1) {
        this.visit1 = visit1;
    }

    /**
     * @return the visit2
     */
    public Date getVisit2() {
        return visit2;
    }

    /**
     * @param visit2 the visit2 to set
     */
    public void setVisit2(Date visit2) {
        this.visit2 = visit2;
    }

    /**
     * @return the visit3
     */
    public Date getVisit3() {
        return visit3;
    }

    /**
     * @param visit3 the visit3 to set
     */
    public void setVisit3(Date visit3) {
        this.visit3 = visit3;
    }

    /**
     * @return the visit4
     */
    public Date getVisit4() {
        return visit4;
    }

    /**
     * @param visit4 the visit4 to set
     */
    public void setVisit4(Date visit4) {
        this.visit4 = visit4;
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
