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
public class DataBaselineParameters {
    private double cd4Count;
    private Date cd4CountDate;
    private double weight;
    private Date weightDate;
    private int whoStaging;
    private Date whoStagingDate;
    private String patientID;
    private int facilityID;
    private String uploaderID;
    private Date uploadDate;
    private String webUploadFlag;
    private int reviewPeriodID;

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
     * @return the whoStaging
     */
    public int getWhoStaging() {
        return whoStaging;
    }

    /**
     * @param whoStaging the whoStaging to set
     */
    public void setWhoStaging(int whoStaging) {
        this.whoStaging = whoStaging;
    }

    /**
     * @return the whoStagingDate
     */
    public Date getWhoStagingDate() {
        return whoStagingDate;
    }

    /**
     * @param whoStagingDate the whoStagingDate to set
     */
    public void setWhoStagingDate(Date whoStagingDate) {
        this.whoStagingDate = whoStagingDate;
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
