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
public class PediatricLinkage {
    private String patientID;
    private String patientReceivedNutritionalAssessmentInReviewPeriod;
    private String patientQualifyForNutritionSupporot;
    private String didPatientReceivedNutritionSupport;
    private String childImmunizationStatus;
   
    private int facilityID;
    private String uploaderId;
    private Date uploadDt;
    
    private String serviceReceivedByPatient;
    
    private String webUploadFlag;
    
    private String waterGuardReceived;
    
    private String insecticideTreatedNetsReceived;
    
    private String notIndicated;
    
    private String noneReceived;
    
    private int reviewPeriodID;
    
    
    
    /*
       PatientID
PatientReceivedNutritionalAssessmentInReviewPeriod
PatientQualifyForNutritionSupporot
DidPatientReceivedNutritionSupport
ChildImmunizationStatus
FacilityID
UploaderId
UploadDt
ServicesReceivedByPatient
webUploadFlag
WaterguardReceived
InsecticideTreatedNetsReceived
NotIndicated
NoneReceived
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
     * @return the patientReceivedNutritionalAssessmentInReviewPeriod
     */
    public String getPatientReceivedNutritionalAssessmentInReviewPeriod() {
        return patientReceivedNutritionalAssessmentInReviewPeriod;
    }

    /**
     * @param patientReceivedNutritionalAssessmentInReviewPeriod the patientReceivedNutritionalAssessmentInReviewPeriod to set
     */
    public void setPatientReceivedNutritionalAssessmentInReviewPeriod(String patientReceivedNutritionalAssessmentInReviewPeriod) {
        this.patientReceivedNutritionalAssessmentInReviewPeriod = patientReceivedNutritionalAssessmentInReviewPeriod;
    }

    /**
     * @return the patientQualifyForNutritionSupporot
     */
    public String getPatientQualifyForNutritionSupporot() {
        return patientQualifyForNutritionSupporot;
    }

    /**
     * @param patientQualifyForNutritionSupporot the patientQualifyForNutritionSupporot to set
     */
    public void setPatientQualifyForNutritionSupporot(String patientQualifyForNutritionSupporot) {
        this.patientQualifyForNutritionSupporot = patientQualifyForNutritionSupporot;
    }

    /**
     * @return the didPatientReceivedNutritionSupport
     */
    public String getDidPatientReceivedNutritionSupport() {
        return didPatientReceivedNutritionSupport;
    }

    /**
     * @param didPatientReceivedNutritionSupport the didPatientReceivedNutritionSupport to set
     */
    public void setDidPatientReceivedNutritionSupport(String didPatientReceivedNutritionSupport) {
        this.didPatientReceivedNutritionSupport = didPatientReceivedNutritionSupport;
    }

    /**
     * @return the childImmunizationStatus
     */
    public String getChildImmunizationStatus() {
        return childImmunizationStatus;
    }

    /**
     * @param childImmunizationStatus the childImmunizationStatus to set
     */
    public void setChildImmunizationStatus(String childImmunizationStatus) {
        this.childImmunizationStatus = childImmunizationStatus;
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
     * @return the serviceReceivedByPatient
     */
    public String getServiceReceivedByPatient() {
        return serviceReceivedByPatient;
    }

    /**
     * @param serviceReceivedByPatient the serviceReceivedByPatient to set
     */
    public void setServiceReceivedByPatient(String serviceReceivedByPatient) {
        this.serviceReceivedByPatient = serviceReceivedByPatient;
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
     * @return the waterGuardReceived
     */
    public String getWaterGuardReceived() {
        return waterGuardReceived;
    }

    /**
     * @param waterGuardReceived the waterGuardReceived to set
     */
    public void setWaterGuardReceived(String waterGuardReceived) {
        this.waterGuardReceived = waterGuardReceived;
    }

    /**
     * @return the insecticideTreatedNetsReceived
     */
    public String getInsecticideTreatedNetsReceived() {
        return insecticideTreatedNetsReceived;
    }

    /**
     * @param insecticideTreatedNetsReceived the insecticideTreatedNetsReceived to set
     */
    public void setInsecticideTreatedNetsReceived(String insecticideTreatedNetsReceived) {
        this.insecticideTreatedNetsReceived = insecticideTreatedNetsReceived;
    }

    /**
     * @return the notIndicated
     */
    public String getNotIndicated() {
        return notIndicated;
    }

    /**
     * @param notIndicated the notIndicated to set
     */
    public void setNotIndicated(String notIndicated) {
        this.notIndicated = notIndicated;
    }

    /**
     * @return the noneReceived
     */
    public String getNoneReceived() {
        return noneReceived;
    }

    /**
     * @param noneReceived the noneReceived to set
     */
    public void setNoneReceived(String noneReceived) {
        this.noneReceived = noneReceived;
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
