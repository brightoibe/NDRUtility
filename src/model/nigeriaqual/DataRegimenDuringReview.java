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
public class DataRegimenDuringReview {
    private String patientID;
    private String patientOnARTFirstDayOfReviewPeriod;
    private String PatientOnARTAnytimeDuringReviewPeriod;
    private String firstRegimen;
    private Date firstRegimenStartDate;
    private Date firstRegimenChangeDate;
    private String secondRegimen;
    private Date secondRegimenStartDate;
    private Date secondRegimenChangeDate;
    private String thirdRegimen;
    private Date thirdRegimenStartDate;
    private Date thirdRegimenChangeDate;
    private String otherRegimenSpecify;
    private Date dateOfLastARTPickup;
    private int durationOfMedicationCoverageInMonths;
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
     * @return the patientOnARTFirstDayOfReviewPeriod
     */
    public String getPatientOnARTFirstDayOfReviewPeriod() {
        return patientOnARTFirstDayOfReviewPeriod;
    }

    /**
     * @param patientOnARTFirstDayOfReviewPeriod the patientOnARTFirstDayOfReviewPeriod to set
     */
    public void setPatientOnARTFirstDayOfReviewPeriod(String patientOnARTFirstDayOfReviewPeriod) {
        this.patientOnARTFirstDayOfReviewPeriod = patientOnARTFirstDayOfReviewPeriod;
    }

    /**
     * @return the PatientOnARTAnytimeDuringReviewPeriod
     */
    public String getPatientOnARTAnytimeDuringReviewPeriod() {
        return PatientOnARTAnytimeDuringReviewPeriod;
    }

    /**
     * @param PatientOnARTAnytimeDuringReviewPeriod the PatientOnARTAnytimeDuringReviewPeriod to set
     */
    public void setPatientOnARTAnytimeDuringReviewPeriod(String PatientOnARTAnytimeDuringReviewPeriod) {
        this.PatientOnARTAnytimeDuringReviewPeriod = PatientOnARTAnytimeDuringReviewPeriod;
    }

    /**
     * @return the firstRegimen
     */
    public String getFirstRegimen() {
        return firstRegimen;
    }

    /**
     * @param firstRegimen the firstRegimen to set
     */
    public void setFirstRegimen(String firstRegimen) {
        this.firstRegimen = firstRegimen;
    }

    /**
     * @return the firstRegimenChangeDate
     */
    public Date getFirstRegimenChangeDate() {
        return firstRegimenChangeDate;
    }

    /**
     * @param firstRegimenChangeDate the firstRegimenChangeDate to set
     */
    public void setFirstRegimenChangeDate(Date firstRegimenChangeDate) {
        this.firstRegimenChangeDate = firstRegimenChangeDate;
    }

    /**
     * @return the secondRegimen
     */
    public String getSecondRegimen() {
        return secondRegimen;
    }

    /**
     * @param secondRegimen the secondRegimen to set
     */
    public void setSecondRegimen(String secondRegimen) {
        this.secondRegimen = secondRegimen;
    }

    /**
     * @return the secondRegimenChangeDate
     */
    public Date getSecondRegimenChangeDate() {
        return secondRegimenChangeDate;
    }

    /**
     * @param secondRegimenChangeDate the secondRegimenChangeDate to set
     */
    public void setSecondRegimenChangeDate(Date secondRegimenChangeDate) {
        this.secondRegimenChangeDate = secondRegimenChangeDate;
    }

    /**
     * @return the thirdRegimen
     */
    public String getThirdRegimen() {
        return thirdRegimen;
    }

    /**
     * @param thirdRegimen the thirdRegimen to set
     */
    public void setThirdRegimen(String thirdRegimen) {
        this.thirdRegimen = thirdRegimen;
    }

    /**
     * @return the thirdRegimenChangeDate
     */
    public Date getThirdRegimenChangeDate() {
        return thirdRegimenChangeDate;
    }

    /**
     * @param thirdRegimenChangeDate the thirdRegimenChangeDate to set
     */
    public void setThirdRegimenChangeDate(Date thirdRegimenChangeDate) {
        this.thirdRegimenChangeDate = thirdRegimenChangeDate;
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
     * @return the dateOfLastARTPickup
     */
    public Date getDateOfLastARTPickup() {
        return dateOfLastARTPickup;
    }

    /**
     * @param dateOfLastARTPickup the dateOfLastARTPickup to set
     */
    public void setDateOfLastARTPickup(Date dateOfLastARTPickup) {
        this.dateOfLastARTPickup = dateOfLastARTPickup;
    }

    /**
     * @return the durationOfMedicationCoverageInMonths
     */
    public int getDurationOfMedicationCoverageInMonths() {
        return durationOfMedicationCoverageInMonths;
    }

    /**
     * @param durationOfMedicationCoverageInMonths the durationOfMedicationCoverageInMonths to set
     */
    public void setDurationOfMedicationCoverageInMonths(int durationOfMedicationCoverageInMonths) {
        this.durationOfMedicationCoverageInMonths = durationOfMedicationCoverageInMonths;
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

    /**
     * @return the firstRegimenStartDate
     */
    public Date getFirstRegimenStartDate() {
        return firstRegimenStartDate;
    }

    /**
     * @param firstRegimenStartDate the firstRegimenStartDate to set
     */
    public void setFirstRegimenStartDate(Date firstRegimenStartDate) {
        this.firstRegimenStartDate = firstRegimenStartDate;
    }

    /**
     * @return the secondRegimenStartDate
     */
    public Date getSecondRegimenStartDate() {
        return secondRegimenStartDate;
    }

    /**
     * @param secondRegimenStartDate the secondRegimenStartDate to set
     */
    public void setSecondRegimenStartDate(Date secondRegimenStartDate) {
        this.secondRegimenStartDate = secondRegimenStartDate;
    }

    /**
     * @return the thirdRegimenStartDate
     */
    public Date getThirdRegimenStartDate() {
        return thirdRegimenStartDate;
    }

    /**
     * @param thirdRegimenStartDate the thirdRegimenStartDate to set
     */
    public void setThirdRegimenStartDate(Date thirdRegimenStartDate) {
        this.thirdRegimenStartDate = thirdRegimenStartDate;
    }
   


}
