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
public class DataHepatitisB {
    private String patientID;
    private String hepatitisBAssayEverDoneForPatient;
    private String resultOfHepatitisBAssay;
    private String clinicalEvaluationARTFormFilledAtLastVisit;
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
     * @return the hepatitisBAssayEverDoneForPatient
     */
    public String getHepatitisBAssayEverDoneForPatient() {
        return hepatitisBAssayEverDoneForPatient;
    }

    /**
     * @param hepatitisBAssayEverDoneForPatient the hepatitisBAssayEverDoneForPatient to set
     */
    public void setHepatitisBAssayEverDoneForPatient(String hepatitisBAssayEverDoneForPatient) {
        this.hepatitisBAssayEverDoneForPatient = hepatitisBAssayEverDoneForPatient;
    }

    /**
     * @return the resultOfHepatitisBAssay
     */
    public String getResultOfHepatitisBAssay() {
        return resultOfHepatitisBAssay;
    }

    /**
     * @param resultOfHepatitisBAssay the resultOfHepatitisBAssay to set
     */
    public void setResultOfHepatitisBAssay(String resultOfHepatitisBAssay) {
        this.resultOfHepatitisBAssay = resultOfHepatitisBAssay;
    }

    /**
     * @return the clinicalEvaluationARTFormFilledAtLastVisit
     */
    public String getClinicalEvaluationARTFormFilledAtLastVisit() {
        return clinicalEvaluationARTFormFilledAtLastVisit;
    }

    /**
     * @param clinicalEvaluationARTFormFilledAtLastVisit the clinicalEvaluationARTFormFilledAtLastVisit to set
     */
    public void setClinicalEvaluationARTFormFilledAtLastVisit(String clinicalEvaluationARTFormFilledAtLastVisit) {
        this.clinicalEvaluationARTFormFilledAtLastVisit = clinicalEvaluationARTFormFilledAtLastVisit;
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
