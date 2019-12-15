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
public class PediatricPatientStatus {
    /*
       PatientID
Status
DateOfStatusChange
Reason
ID
FacilityID
UploaderId
UploadDt
webUploadFlag
Transferred_Out
Death
Discontinued_Care
Transferred_Out_Date
Death_Date
Discontinued_Care_Date
Discontinued_Care_Reason
Discontinued_Care_Reason_Other
ReviewPeriodID

     */
    private String patientID;
    private String status;
    private Date dateOfStatusChange;
    private String reason;
    private String ID;
    private int facilityID;
    private String uploaderId;
    private Date uploadDt;
    private String webUploadFlag;
    private String transferredOut;
    private String death;
    private String discontinuedCare;
    private Date transferredOutDate;
    private Date deathDate;
    private Date discontinuedCareDate;
    private String discontinuedCareReason;
    private String discontinuedCareReasonOther;
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
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the dateOfStatusChange
     */
    public Date getDateOfStatusChange() {
        return dateOfStatusChange;
    }

    /**
     * @param dateOfStatusChange the dateOfStatusChange to set
     */
    public void setDateOfStatusChange(Date dateOfStatusChange) {
        this.dateOfStatusChange = dateOfStatusChange;
    }

    /**
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * @return the ID
     */
    public String getID() {
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(String ID) {
        this.ID = ID;
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
     * @return the transferredOut
     */
    public String getTransferredOut() {
        return transferredOut;
    }

    /**
     * @param transferredOut the transferredOut to set
     */
    public void setTransferredOut(String transferredOut) {
        this.transferredOut = transferredOut;
    }

    /**
     * @return the death
     */
    public String getDeath() {
        return death;
    }

    /**
     * @param death the death to set
     */
    public void setDeath(String death) {
        this.death = death;
    }

    /**
     * @return the discontinuedDate
     */
    public String getDiscontinued() {
        return discontinuedCare;
    }

    /**
     * @param discontinuedDate the discontinuedDate to set
     */
    public void setDiscontinued(String discontinuedDate) {
        this.discontinuedCare = discontinuedDate;
    }

    /**
     * @return the transferredOutDate
     */
    public Date getTransferredOutDate() {
        return transferredOutDate;
    }

    /**
     * @param transferredOutDate the transferredOutDate to set
     */
    public void setTransferredOutDate(Date transferredOutDate) {
        this.transferredOutDate = transferredOutDate;
    }

    /**
     * @return the deathDate
     */
    public Date getDeathDate() {
        return deathDate;
    }

    /**
     * @param deathDate the deathDate to set
     */
    public void setDeathDate(Date deathDate) {
        this.deathDate = deathDate;
    }

    /**
     * @return the discontinuedCareDate
     */
    public Date getDiscontinuedCareDate() {
        return discontinuedCareDate;
    }

    /**
     * @param discontinuedCareDate the discontinuedCareDate to set
     */
    public void setDiscontinuedCareDate(Date discontinuedCareDate) {
        this.discontinuedCareDate = discontinuedCareDate;
    }

    /**
     * @return the discontinuedCareReason
     */
    public String getDiscontinuedCareReason() {
        return discontinuedCareReason;
    }

    /**
     * @param discontinuedCareReason the discontinuedCareReason to set
     */
    public void setDiscontinuedCareReason(String discontinuedCareReason) {
        this.discontinuedCareReason = discontinuedCareReason;
    }

    /**
     * @return the discontinuedCareReasonOther
     */
    public String getDiscontinuedCareReasonOther() {
        return discontinuedCareReasonOther;
    }

    /**
     * @param discontinuedCareReasonOther the discontinuedCareReasonOther to set
     */
    public void setDiscontinuedCareReasonOther(String discontinuedCareReasonOther) {
        this.discontinuedCareReasonOther = discontinuedCareReasonOther;
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
