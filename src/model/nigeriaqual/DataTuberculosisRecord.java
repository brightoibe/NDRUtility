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
public class DataTuberculosisRecord {
    private String patientID;
    private String patientOnTBTreatmentAtStartOfReviewPeriod;
    private String patientClinicallyScreenForTBDuringReviewPeriod;
    private String TBClinicalScreeningCriteria;
    private String BasedOnScreeningWasPatientedSuspectedToHaveTB;
    private String PatientHaveCRXPerformedDuringReviewPeriod;
    private String PatientReferredToDOTsClinic ;
    private String PatientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture;
    private String PatientDiagnosedOfTBInReviewPeriod;
    private Date TBDiagnosis_Date;
    private String PatientStartTBTreatment;
    private Date TB_TreatmentStartDate;
    private int facilityID;
    private String uploaderID;
    private Date uploadDate;
    private String webUploadFlag;
    private int reviewPeriodID;
    private String TBScreeningCriteria_CurrentCough;
    private String TBScreeningCriteria_ContactHistoryWithTBCase;
    private String TBScreeningCriteria_PoorWeightGain;

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
     * @return the patientOnTBTreatmentAtStartOfReviewPeriod
     */
    public String getPatientOnTBTreatmentAtStartOfReviewPeriod() {
        return patientOnTBTreatmentAtStartOfReviewPeriod;
    }

    /**
     * @param patientOnTBTreatmentAtStartOfReviewPeriod the patientOnTBTreatmentAtStartOfReviewPeriod to set
     */
    public void setPatientOnTBTreatmentAtStartOfReviewPeriod(String patientOnTBTreatmentAtStartOfReviewPeriod) {
        this.patientOnTBTreatmentAtStartOfReviewPeriod = patientOnTBTreatmentAtStartOfReviewPeriod;
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
     * @return the TBClinicalScreeningCriteria
     */
    public String getTBClinicalScreeningCriteria() {
        return TBClinicalScreeningCriteria;
    }

    /**
     * @param TBClinicalScreeningCriteria the TBClinicalScreeningCriteria to set
     */
    public void setTBClinicalScreeningCriteria(String TBClinicalScreeningCriteria) {
        this.TBClinicalScreeningCriteria = TBClinicalScreeningCriteria;
    }

    /**
     * @return the BasedOnScreeningWasPatientedSuspectedToHaveTB
     */
    public String getBasedOnScreeningWasPatientedSuspectedToHaveTB() {
        return BasedOnScreeningWasPatientedSuspectedToHaveTB;
    }

    /**
     * @param BasedOnScreeningWasPatientedSuspectedToHaveTB the BasedOnScreeningWasPatientedSuspectedToHaveTB to set
     */
    public void setBasedOnScreeningWasPatientedSuspectedToHaveTB(String BasedOnScreeningWasPatientedSuspectedToHaveTB) {
        this.BasedOnScreeningWasPatientedSuspectedToHaveTB = BasedOnScreeningWasPatientedSuspectedToHaveTB;
    }

    /**
     * @return the PatientHaveCRXPerformedDuringReviewPeriod
     */
    public String getPatientHaveCRXPerformedDuringReviewPeriod() {
        return PatientHaveCRXPerformedDuringReviewPeriod;
    }

    /**
     * @param PatientHaveCRXPerformedDuringReviewPeriod the PatientHaveCRXPerformedDuringReviewPeriod to set
     */
    public void setPatientHaveCRXPerformedDuringReviewPeriod(String PatientHaveCRXPerformedDuringReviewPeriod) {
        this.PatientHaveCRXPerformedDuringReviewPeriod = PatientHaveCRXPerformedDuringReviewPeriod;
    }

    /**
     * @return the PatientReferredToDOTsClinic
     */
    public String getPatientReferredToDOTsClinic() {
        return PatientReferredToDOTsClinic;
    }

    /**
     * @param PatientReferredToDOTsClinic the PatientReferredToDOTsClinic to set
     */
    public void setPatientReferredToDOTsClinic(String PatientReferredToDOTsClinic) {
        this.PatientReferredToDOTsClinic = PatientReferredToDOTsClinic;
    }

    /**
     * @return the PatientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture
     */
    public String getPatientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture() {
        return PatientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture;
    }

    /**
     * @param PatientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture the PatientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture to set
     */
    public void setPatientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture(String PatientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture) {
        this.PatientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture = PatientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture;
    }

    /**
     * @return the PatientDiagnosedOfTBInReviewPeriod
     */
    public String getPatientDiagnosedOfTBInReviewPeriod() {
        return PatientDiagnosedOfTBInReviewPeriod;
    }

    /**
     * @param PatientDiagnosedOfTBInReviewPeriod the PatientDiagnosedOfTBInReviewPeriod to set
     */
    public void setPatientDiagnosedOfTBInReviewPeriod(String PatientDiagnosedOfTBInReviewPeriod) {
        this.PatientDiagnosedOfTBInReviewPeriod = PatientDiagnosedOfTBInReviewPeriod;
    }

    /**
     * @return the TBDiagnosis_Date
     */
    public Date getTBDiagnosis_Date() {
        return TBDiagnosis_Date;
    }

    /**
     * @param TBDiagnosis_Date the TBDiagnosis_Date to set
     */
    public void setTBDiagnosis_Date(Date TBDiagnosis_Date) {
        this.TBDiagnosis_Date = TBDiagnosis_Date;
    }

    /**
     * @return the PatientStartTBTreatment
     */
    public String getPatientStartTBTreatment() {
        return PatientStartTBTreatment;
    }

    /**
     * @param PatientStartTBTreatment the PatientStartTBTreatment to set
     */
    public void setPatientStartTBTreatment(String PatientStartTBTreatment) {
        this.PatientStartTBTreatment = PatientStartTBTreatment;
    }

    /**
     * @return the TB_TreatmentStartDate
     */
    public Date getTB_TreatmentStartDate() {
        return TB_TreatmentStartDate;
    }

    /**
     * @param TB_TreatmentStartDate the TB_TreatmentStartDate to set
     */
    public void setTB_TreatmentStartDate(Date TB_TreatmentStartDate) {
        this.TB_TreatmentStartDate = TB_TreatmentStartDate;
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
     * @return the TBScreeningCriteria_ContactHistoryWithTBCase
     */
    public String getTBScreeningCriteria_ContactHistoryWithTBCase() {
        return TBScreeningCriteria_ContactHistoryWithTBCase;
    }

    /**
     * @param TBScreeningCriteria_ContactHistoryWithTBCase the TBScreeningCriteria_ContactHistoryWithTBCase to set
     */
    public void setTBScreeningCriteria_ContactHistoryWithTBCase(String TBScreeningCriteria_ContactHistoryWithTBCase) {
        this.TBScreeningCriteria_ContactHistoryWithTBCase = TBScreeningCriteria_ContactHistoryWithTBCase;
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
}
