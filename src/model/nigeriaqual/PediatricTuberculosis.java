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
public class PediatricTuberculosis {

    private String patientID;
    private String patientOnTBTreatmentDuringReviewPeriod;
    private String patientClinicallyScreenForTBDuringReviewPeriod;
    private String tbClinicalScreeningCriteria;
    private String basedOnScreeningWasPatientedSuspectedToHaveTB;
    private String patientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture;
    private String patientHdChestXRay;
    private String patientDiagnosedOfTBInReviewPeriod;
    private String patientStartTBTreatment;
    private Date tbTreatmentStartDate;
    private Date tbDiagnosisDate;
    //private String patientReferredToDOTsClinic;
    private int facilityID;
    private String uploaderID;
    private Date uploadDate;
    private String webUploadFlag;
    private String TBScreeningCriteria_CurrentCough;
    private String TBScreeningCriteria_ContactWithTBCase;
    private String TBScreeningCriteria_PoorWeightGain;
    private int reviewPeriodID;
    /* PatientID
PatientOnTBTreatmentDuringReviewPeriod
PatientClinicallyScreenForTBDuringReviewPeriod
TBClinicalScreeningCriteria
BasedOnScreeningWasPatientedSuspectedToHaveTB
PatientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture
PatientHdChestXRay
PatientDiagnosedOfTBInReviewPeriod
PatientStartTBTreatment
TB_TreatmentStartDate
TBDiagnosis_Date
FacilityID
UploaderId
UploadDt
webUploadFlag
TBScreeningCriteria_CurrentCough
TBScreeningCriteria_ContactWithTBCase
TBScreeningCriteria_PoorWeighGain
ReviewPeriodID*/

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
     * @return the patientOnTBTreatmentDuringReviewPeriod
     */
    public String getPatientOnTBTreatmentDuringReviewPeriod() {
        return patientOnTBTreatmentDuringReviewPeriod;
    }

    /**
     * @param patientOnTBTreatmentDuringReviewPeriod the patientOnTBTreatmentDuringReviewPeriod to set
     */
    public void setPatientOnTBTreatmentDuringReviewPeriod(String patientOnTBTreatmentDuringReviewPeriod) {
        this.patientOnTBTreatmentDuringReviewPeriod = patientOnTBTreatmentDuringReviewPeriod;
    }

    /**
     * @return the patientClinicallyScreenForTBDuringReviewPeriod
     */
    public String getPatientClinicallyScreenForTBDuringReviewPeriod() {
        return patientClinicallyScreenForTBDuringReviewPeriod;
    }

    /**
     * @param patientClinicallyScreenForTBDuringReviewPeriod the patientClinicallyScreenForTBDuringReviewPeriod to set
     */
    public void setPatientClinicallyScreenForTBDuringReviewPeriod(String patientClinicallyScreenForTBDuringReviewPeriod) {
        this.patientClinicallyScreenForTBDuringReviewPeriod = patientClinicallyScreenForTBDuringReviewPeriod;
    }

    /**
     * @return the tbClinicalScreeningCriteria
     */
    public String getTbClinicalScreeningCriteria() {
        return tbClinicalScreeningCriteria;
    }

    /**
     * @param tbClinicalScreeningCriteria the tbClinicalScreeningCriteria to set
     */
    public void setTbClinicalScreeningCriteria(String tbClinicalScreeningCriteria) {
        this.tbClinicalScreeningCriteria = tbClinicalScreeningCriteria;
    }

    /**
     * @return the basedOnScreeningWasPatientedSuspectedToHaveTB
     */
    public String getBasedOnScreeningWasPatientedSuspectedToHaveTB() {
        return basedOnScreeningWasPatientedSuspectedToHaveTB;
    }

    /**
     * @param basedOnScreeningWasPatientedSuspectedToHaveTB the basedOnScreeningWasPatientedSuspectedToHaveTB to set
     */
    public void setBasedOnScreeningWasPatientedSuspectedToHaveTB(String basedOnScreeningWasPatientedSuspectedToHaveTB) {
        this.basedOnScreeningWasPatientedSuspectedToHaveTB = basedOnScreeningWasPatientedSuspectedToHaveTB;
    }

    /**
     * @return the patientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture
     */
    public String getPatientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture() {
        return patientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture;
    }

    /**
     * @param patientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture the patientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture to set
     */
    public void setPatientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture(String patientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture) {
        this.patientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture = patientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture;
    }

    /**
     * @return the patientHdChestXRay
     */
    public String getPatientHdChestXRay() {
        return patientHdChestXRay;
    }

    /**
     * @param patientHdChestXRay the patientHdChestXRay to set
     */
    public void setPatientHdChestXRay(String patientHdChestXRay) {
        this.patientHdChestXRay = patientHdChestXRay;
    }

    /**
     * @return the patientDiagnosedOfTBInReviewPeriod
     */
    public String getPatientDiagnosedOfTBInReviewPeriod() {
        return patientDiagnosedOfTBInReviewPeriod;
    }

    /**
     * @param patientDiagnosedOfTBInReviewPeriod the patientDiagnosedOfTBInReviewPeriod to set
     */
    public void setPatientDiagnosedOfTBInReviewPeriod(String patientDiagnosedOfTBInReviewPeriod) {
        this.patientDiagnosedOfTBInReviewPeriod = patientDiagnosedOfTBInReviewPeriod;
    }

    /**
     * @return the patientStartTBTreatment
     */
    public String getPatientStartTBTreatment() {
        return patientStartTBTreatment;
    }

    /**
     * @param patientStartTBTreatment the patientStartTBTreatment to set
     */
    public void setPatientStartTBTreatment(String patientStartTBTreatment) {
        this.patientStartTBTreatment = patientStartTBTreatment;
    }

    /**
     * @return the tbTreatmentStartDate
     */
    public Date getTbTreatmentStartDate() {
        return tbTreatmentStartDate;
    }

    /**
     * @param tbTreatmentStartDate the tbTreatmentStartDate to set
     */
    public void setTbTreatmentStartDate(Date tbTreatmentStartDate) {
        this.tbTreatmentStartDate = tbTreatmentStartDate;
    }

    /**
     * @return the tbDiagnosisDate
     */
    public Date getTbDiagnosisDate() {
        return tbDiagnosisDate;
    }

    /**
     * @param tbDiagnosisDate the tbDiagnosisDate to set
     */
    public void setTbDiagnosisDate(Date tbDiagnosisDate) {
        this.tbDiagnosisDate = tbDiagnosisDate;
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
     * @return the TBScreeningCriteria_CurrentCough
     */
    public String getTBScreeningCriteria_CurrentCough() {
        return TBScreeningCriteria_CurrentCough;
    }

    /**
     * @param TBScreeningCriteria_CurrentCough the TBScreeningCriteria_CurrentCough to set
     */
    public void setTBScreeningCriteria_CurrentCough(String TBScreeningCriteria_CurrentCough) {
        this.TBScreeningCriteria_CurrentCough = TBScreeningCriteria_CurrentCough;
    }

    /**
     * @return the TBScreeningCriteria_ContactWithTBCase
     */
    public String getTBScreeningCriteria_ContactWithTBCase() {
        return TBScreeningCriteria_ContactWithTBCase;
    }

    /**
     * @param TBScreeningCriteria_ContactWithTBCase the TBScreeningCriteria_ContactWithTBCase to set
     */
    public void setTBScreeningCriteria_ContactWithTBCase(String TBScreeningCriteria_ContactWithTBCase) {
        this.TBScreeningCriteria_ContactWithTBCase = TBScreeningCriteria_ContactWithTBCase;
    }

    /**
     * @return the TBScreeningCriteria_PoorWeightGain
     */
    public String getTBScreeningCriteria_PoorWeightGain() {
        return TBScreeningCriteria_PoorWeightGain;
    }

    /**
     * @param TBScreeningCriteria_PoorWeightGain the TBScreeningCriteria_PoorWeightGain to set
     */
    public void setTBScreeningCriteria_PoorWeightGain(String TBScreeningCriteria_PoorWeightGain) {
        this.TBScreeningCriteria_PoorWeightGain = TBScreeningCriteria_PoorWeightGain;
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
