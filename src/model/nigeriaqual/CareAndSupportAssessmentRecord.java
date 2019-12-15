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
public class CareAndSupportAssessmentRecord {
    private String patientID;
    private String careAndSupportAssementFormInPatientFolder;
    private String patientReceiveCareAndSupportAssessmentInReviewPeriod;
    private String nutritionalAssessmentEverDoneForPatientSinceEnrolment;
    private String patientReceiveNutritionalAssessementInReviewPeriod;
    private String preventionGoaDocumentedInCareAndSupportForm;
    private String patientEverReceivedBasicCarePackage;
    private String patientReceiveBasicCarePackageInReviewPeriod;
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
     * @return the careAndSupportAssementFormInPatientFolder
     */
    public String getCareAndSupportAssementFormInPatientFolder() {
        return careAndSupportAssementFormInPatientFolder;
    }

    /**
     * @param careAndSupportAssementFormInPatientFolder the careAndSupportAssementFormInPatientFolder to set
     */
    public void setCareAndSupportAssementFormInPatientFolder(String careAndSupportAssementFormInPatientFolder) {
        this.careAndSupportAssementFormInPatientFolder = careAndSupportAssementFormInPatientFolder;
    }

    /**
     * @return the patientReceiveCareAndSupportAssessmentInReviewPeriod
     */
    public String getPatientReceiveCareAndSupportAssessmentInReviewPeriod() {
        return patientReceiveCareAndSupportAssessmentInReviewPeriod;
    }

    /**
     * @param patientReceiveCareAndSupportAssessmentInReviewPeriod the patientReceiveCareAndSupportAssessmentInReviewPeriod to set
     */
    public void setPatientReceiveCareAndSupportAssessmentInReviewPeriod(String patientReceiveCareAndSupportAssessmentInReviewPeriod) {
        this.patientReceiveCareAndSupportAssessmentInReviewPeriod = patientReceiveCareAndSupportAssessmentInReviewPeriod;
    }

    /**
     * @return the nutritionalAssessmentEverDoneForPatientSinceEnrolment
     */
    public String getNutritionalAssessmentEverDoneForPatientSinceEnrolment() {
        return nutritionalAssessmentEverDoneForPatientSinceEnrolment;
    }

    /**
     * @param nutritionalAssessmentEverDoneForPatientSinceEnrolment the nutritionalAssessmentEverDoneForPatientSinceEnrolment to set
     */
    public void setNutritionalAssessmentEverDoneForPatientSinceEnrolment(String nutritionalAssessmentEverDoneForPatientSinceEnrolment) {
        this.nutritionalAssessmentEverDoneForPatientSinceEnrolment = nutritionalAssessmentEverDoneForPatientSinceEnrolment;
    }

    /**
     * @return the patientReceiveNutritionalAssessementInReviewPeriod
     */
    public String getPatientReceiveNutritionalAssessementInReviewPeriod() {
        return patientReceiveNutritionalAssessementInReviewPeriod;
    }

    /**
     * @param patientReceiveNutritionalAssessementInReviewPeriod the patientReceiveNutritionalAssessementInReviewPeriod to set
     */
    public void setPatientReceiveNutritionalAssessementInReviewPeriod(String patientReceiveNutritionalAssessementInReviewPeriod) {
        this.patientReceiveNutritionalAssessementInReviewPeriod = patientReceiveNutritionalAssessementInReviewPeriod;
    }

    /**
     * @return the preventionGoaDocumentedInCareAndSupportForm
     */
    public String getPreventionGoaDocumentedInCareAndSupportForm() {
        return preventionGoaDocumentedInCareAndSupportForm;
    }

    /**
     * @param preventionGoaDocumentedInCareAndSupportForm the preventionGoaDocumentedInCareAndSupportForm to set
     */
    public void setPreventionGoaDocumentedInCareAndSupportForm(String preventionGoaDocumentedInCareAndSupportForm) {
        this.preventionGoaDocumentedInCareAndSupportForm = preventionGoaDocumentedInCareAndSupportForm;
    }

    /**
     * @return the patientEverReceivedBasicCarePackage
     */
    public String getPatientEverReceivedBasicCarePackage() {
        return patientEverReceivedBasicCarePackage;
    }

    /**
     * @param patientEverReceivedBasicCarePackage the patientEverReceivedBasicCarePackage to set
     */
    public void setPatientEverReceivedBasicCarePackage(String patientEverReceivedBasicCarePackage) {
        this.patientEverReceivedBasicCarePackage = patientEverReceivedBasicCarePackage;
    }

    /**
     * @return the patientReceiveBasicCarePackageInReviewPeriod
     */
    public String getPatientReceiveBasicCarePackageInReviewPeriod() {
        return patientReceiveBasicCarePackageInReviewPeriod;
    }

    /**
     * @param patientReceiveBasicCarePackageInReviewPeriod the patientReceiveBasicCarePackageInReviewPeriod to set
     */
    public void setPatientReceiveBasicCarePackageInReviewPeriod(String patientReceiveBasicCarePackageInReviewPeriod) {
        this.patientReceiveBasicCarePackageInReviewPeriod = patientReceiveBasicCarePackageInReviewPeriod;
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
