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
public class PediatricBaselineParameters {

    private String patientID;
    private double cd4Count;
    private Date cd4CountDate;
    private double weight;
    private Date weightDate;
    private int whoClinicalStage;
    private Date whoClinicalStageDate;
    private String cd4NotRecorded;
    private String weightNotRecorded;
    private String whoClinicalStageNotRecorded;
    private int facilityID;
    private String patientEverStartedOnART;
    private Date artStartDate;
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
     * @return the cd4Count
     */
    public double getCd4Count() {
        return cd4Count;
    }

    /**
     * @param cd4Count the cd4Count to set
     */
    public void setCd4Count(double cd4Count) {
        this.cd4Count = cd4Count;
    }

    /**
     * @return the cd4CountDate
     */
    public Date getCd4CountDate() {
        return cd4CountDate;
    }

    /**
     * @param cd4CountDate the cd4CountDate to set
     */
    public void setCd4CountDate(Date cd4CountDate) {
        this.cd4CountDate = cd4CountDate;
    }

    /**
     * @return the weight
     */
    public double getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * @return the weightDate
     */
    public Date getWeightDate() {
        return weightDate;
    }

    /**
     * @param weightDate the weightDate to set
     */
    public void setWeightDate(Date weightDate) {
        this.weightDate = weightDate;
    }

    /**
     * @return the whoClinicalStage
     */
    public int getWhoClinicalStage() {
        return whoClinicalStage;
    }

    /**
     * @param whoClinicalStage the whoClinicalStage to set
     */
    public void setWhoClinicalStage(int whoClinicalStage) {
        this.whoClinicalStage = whoClinicalStage;
    }

    /**
     * @return the whoClinicalStageDate
     */
    public Date getWhoClinicalStageDate() {
        return whoClinicalStageDate;
    }

    /**
     * @param whoClinicalStageDate the whoClinicalStageDate to set
     */
    public void setWhoClinicalStageDate(Date whoClinicalStageDate) {
        this.whoClinicalStageDate = whoClinicalStageDate;
    }

    /**
     * @return the cd4NotRecorded
     */
    public String getCd4NotRecorded() {
        return cd4NotRecorded;
    }

    /**
     * @param cd4NotRecorded the cd4NotRecorded to set
     */
    public void setCd4NotRecorded(String cd4NotRecorded) {
        this.cd4NotRecorded = cd4NotRecorded;
    }

    /**
     * @return the weightNotRecorded
     */
    public String getWeightNotRecorded() {
        return weightNotRecorded;
    }

    /**
     * @param weightNotRecorded the weightNotRecorded to set
     */
    public void setWeightNotRecorded(String weightNotRecorded) {
        this.weightNotRecorded = weightNotRecorded;
    }

    /**
     * @return the whoClinicalStageNotRecorded
     */
    public String getWhoClinicalStageNotRecorded() {
        return whoClinicalStageNotRecorded;
    }

    /**
     * @param whoClinicalStageNotRecorded the whoClinicalStageNotRecorded to set
     */
    public void setWhoClinicalStageNotRecorded(String whoClinicalStageNotRecorded) {
        this.whoClinicalStageNotRecorded = whoClinicalStageNotRecorded;
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
