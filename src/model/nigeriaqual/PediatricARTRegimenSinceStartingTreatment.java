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
public class PediatricARTRegimenSinceStartingTreatment {

    private String patientID;
    private String patientOnARTAnytimeDuringReviewPeriod;
    private String c1stRegminen;
    private Date c1stRegimenStartDate;
    private Date c1stRegimenChangeDate;
    private String c2ndRegimen;
    private Date c2ndRegimenStartDate;
    private Date c2ndRegimenChangeDate;
    private String c3rdRegimen;
    private Date c3rdRegimenStartDate;
    private Date c3rdRegimenChangeDate;
    private String otherRegimenSpecify;
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
     * @return the patientOnARTAnytimeDuringReviewPeriod
     */
    public String getPatientOnARTAnytimeDuringReviewPeriod() {
        return patientOnARTAnytimeDuringReviewPeriod;
    }

    /**
     * @param patientOnARTAnytimeDuringReviewPeriod the patientOnARTAnytimeDuringReviewPeriod to set
     */
    public void setPatientOnARTAnytimeDuringReviewPeriod(String patientOnARTAnytimeDuringReviewPeriod) {
        this.patientOnARTAnytimeDuringReviewPeriod = patientOnARTAnytimeDuringReviewPeriod;
    }

    /**
     * @return the c1stRegminen
     */
    public String getC1stRegminen() {
        return c1stRegminen;
    }

    /**
     * @param c1stRegminen the c1stRegminen to set
     */
    public void setC1stRegminen(String c1stRegminen) {
        this.c1stRegminen = c1stRegminen;
    }

    /**
     * @return the c1stRegimenStartDate
     */
    public Date getC1stRegimenStartDate() {
        return c1stRegimenStartDate;
    }

    /**
     * @param c1stRegimenStartDate the c1stRegimenStartDate to set
     */
    public void setC1stRegimenStartDate(Date c1stRegimenStartDate) {
        this.c1stRegimenStartDate = c1stRegimenStartDate;
    }

    /**
     * @return the c1stRegimenChangeDate
     */
    public Date getC1stRegimenChangeDate() {
        return c1stRegimenChangeDate;
    }

    /**
     * @param c1stRegimenChangeDate the c1stRegimenChangeDate to set
     */
    public void setC1stRegimenChangeDate(Date c1stRegimenChangeDate) {
        this.c1stRegimenChangeDate = c1stRegimenChangeDate;
    }

    /**
     * @return the c2ndRegimen
     */
    public String getC2ndRegimen() {
        return c2ndRegimen;
    }

    /**
     * @param c2ndRegimen the c2ndRegimen to set
     */
    public void setC2ndRegimen(String c2ndRegimen) {
        this.c2ndRegimen = c2ndRegimen;
    }

    /**
     * @return the c2ndRegimenStartDate
     */
    public Date getC2ndRegimenStartDate() {
        return c2ndRegimenStartDate;
    }

    /**
     * @param c2ndRegimenStartDate the c2ndRegimenStartDate to set
     */
    public void setC2ndRegimenStartDate(Date c2ndRegimenStartDate) {
        this.c2ndRegimenStartDate = c2ndRegimenStartDate;
    }

    /**
     * @return the c2ndRegimenChangeDate
     */
    public Date getC2ndRegimenChangeDate() {
        return c2ndRegimenChangeDate;
    }

    /**
     * @param c2ndRegimenChangeDate the c2ndRegimenChangeDate to set
     */
    public void setC2ndRegimenChangeDate(Date c2ndRegimenChangeDate) {
        this.c2ndRegimenChangeDate = c2ndRegimenChangeDate;
    }

    /**
     * @return the c3rdRegimen
     */
    public String getC3rdRegimen() {
        return c3rdRegimen;
    }

    /**
     * @param c3rdRegimen the c3rdRegimen to set
     */
    public void setC3rdRegimen(String c3rdRegimen) {
        this.c3rdRegimen = c3rdRegimen;
    }

    /**
     * @return the c3rdRegimenStartDate
     */
    public Date getC3rdRegimenStartDate() {
        return c3rdRegimenStartDate;
    }

    /**
     * @param c3rdRegimenStartDate the c3rdRegimenStartDate to set
     */
    public void setC3rdRegimenStartDate(Date c3rdRegimenStartDate) {
        this.c3rdRegimenStartDate = c3rdRegimenStartDate;
    }

    /**
     * @return the c3rdRegimenChangeDate
     */
    public Date getC3rdRegimenChangeDate() {
        return c3rdRegimenChangeDate;
    }

    /**
     * @param c3rdRegimenChangeDate the c3rdRegimenChangeDate to set
     */
    public void setC3rdRegimenChangeDate(Date c3rdRegimenChangeDate) {
        this.c3rdRegimenChangeDate = c3rdRegimenChangeDate;
    }

    /**
     * @return the otherRegimenSpecify
     */
    public String getOtherRegimenSpecify() {
        return otherRegimenSpecify;
    }

    /**
     * @param otherRegimenSpecify the otherRegimenSpecify to set
     */
    public void setOtherRegimenSpecify(String otherRegimenSpecify) {
        this.otherRegimenSpecify = otherRegimenSpecify;
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
