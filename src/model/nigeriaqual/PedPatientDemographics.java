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
public class PedPatientDemographics {
    private String patientID;
    private String lastName;
    private String firstName;
    private String hospitalNo;
    private String gender;
    private Date dateOfBirth;
    private int age;
    private String unitOfAgeMeasure;
    private Date dateEnrolledInCare;
    private String clinicVisit6MonthsPriorToReviewPeriod;
    private String deliveryLocation;
    private String primaryCareGiver;
    private String stateOfResidence;
    private String lgaOfResidence;
    private String stateOfOrigin;
    private String tribe;
    private Date dateOfLastVisit;
    private String admissionDuringReviewPeriod;
    private String rnlSerialNo;
    private String careGiverOccupation;
    private int facilityID;
    private int recordCompletionPosition;
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
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the hospitalNo
     */
    public String getHospitalNo() {
        return hospitalNo;
    }

    /**
     * @param hospitalNo the hospitalNo to set
     */
    public void setHospitalNo(String hospitalNo) {
        this.hospitalNo = hospitalNo;
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the dateOfBirth
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * @param dateOfBirth the dateOfBirth to set
     */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * @return the unitOfAgeMeasure
     */
    public String getUnitOfAgeMeasure() {
        return unitOfAgeMeasure;
    }

    /**
     * @param unitOfAgeMeasure the unitOfAgeMeasure to set
     */
    public void setUnitOfAgeMeasure(String unitOfAgeMeasure) {
        this.unitOfAgeMeasure = unitOfAgeMeasure;
    }

    /**
     * @return the dateEnrolledInCare
     */
    public Date getDateEnrolledInCare() {
        return dateEnrolledInCare;
    }

    /**
     * @param dateEnrolledInCare the dateEnrolledInCare to set
     */
    public void setDateEnrolledInCare(Date dateEnrolledInCare) {
        this.dateEnrolledInCare = dateEnrolledInCare;
    }

    /**
     * @return the clinicVisit6MonthsPriorToReviewPeriod
     */
    public String getClinicVisit6MonthsPriorToReviewPeriod() {
        return clinicVisit6MonthsPriorToReviewPeriod;
    }

    /**
     * @param clinicVisit6MonthsPriorToReviewPeriod the clinicVisit6MonthsPriorToReviewPeriod to set
     */
    public void setClinicVisit6MonthsPriorToReviewPeriod(String clinicVisit6MonthsPriorToReviewPeriod) {
        this.clinicVisit6MonthsPriorToReviewPeriod = clinicVisit6MonthsPriorToReviewPeriod;
    }

    /**
     * @return the deliveryLocation
     */
    public String getDeliveryLocation() {
        return deliveryLocation;
    }

    /**
     * @param deliveryLocation the deliveryLocation to set
     */
    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    /**
     * @return the stateOfResidence
     */
    public String getStateOfResidence() {
        return stateOfResidence;
    }

    /**
     * @param stateOfResidence the stateOfResidence to set
     */
    public void setStateOfResidence(String stateOfResidence) {
        this.stateOfResidence = stateOfResidence;
    }

    /**
     * @return the tribe
     */
    public String getTribe() {
        return tribe;
    }

    /**
     * @param tribe the tribe to set
     */
    public void setTribe(String tribe) {
        this.tribe = tribe;
    }

    /**
     * @return the dateOfLastVisit
     */
    public Date getDateOfLastVisit() {
        return dateOfLastVisit;
    }

    /**
     * @param dateOfLastVisit the dateOfLastVisit to set
     */
    public void setDateOfLastVisit(Date dateOfLastVisit) {
        this.dateOfLastVisit = dateOfLastVisit;
    }

    /**
     * @return the admissionDuringReviewPeriod
     */
    public String getAdmissionDuringReviewPeriod() {
        return admissionDuringReviewPeriod;
    }

    /**
     * @param admissionDuringReviewPeriod the admissionDuringReviewPeriod to set
     */
    public void setAdmissionDuringReviewPeriod(String admissionDuringReviewPeriod) {
        this.admissionDuringReviewPeriod = admissionDuringReviewPeriod;
    }

    /**
     * @return the rnlSerialNo
     */
    public String getRnlSerialNo() {
        return rnlSerialNo;
    }

    /**
     * @param rnlSerialNo the rnlSerialNo to set
     */
    public void setRnlSerialNo(String rnlSerialNo) {
        this.rnlSerialNo = rnlSerialNo;
    }

    /**
     * @return the careGiverOccupation
     */
    public String getCareGiverOccupation() {
        return careGiverOccupation;
    }

    /**
     * @param careGiverOccupation the careGiverOccupation to set
     */
    public void setCareGiverOccupation(String careGiverOccupation) {
        this.careGiverOccupation = careGiverOccupation;
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
     * @return the recordCompletionPosition
     */
    public int getRecordCompletionPosition() {
        return recordCompletionPosition;
    }

    /**
     * @param recordCompletionPosition the recordCompletionPosition to set
     */
    public void setRecordCompletionPosition(int recordCompletionPosition) {
        this.recordCompletionPosition = recordCompletionPosition;
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
     * @return the primaryCareGiver
     */
    public String getPrimaryCareGiver() {
        return primaryCareGiver;
    }

    /**
     * @param primaryCareGiver the primaryCareGiver to set
     */
    public void setPrimaryCareGiver(String primaryCareGiver) {
        this.primaryCareGiver = primaryCareGiver;
    }

    /**
     * @return the lgaOfResidence
     */
    public String getLgaOfResidence() {
        return lgaOfResidence;
    }

    /**
     * @param lgaOfResidence the lgaOfResidence to set
     */
    public void setLgaOfResidence(String lgaOfResidence) {
        this.lgaOfResidence = lgaOfResidence;
    }

    /**
     * @return the stateOfOrigin
     */
    public String getStateOfOrigin() {
        return stateOfOrigin;
    }

    /**
     * @param stateOfOrigin the stateOfOrigin to set
     */
    public void setStateOfOrigin(String stateOfOrigin) {
        this.stateOfOrigin = stateOfOrigin;
    }

    
}
