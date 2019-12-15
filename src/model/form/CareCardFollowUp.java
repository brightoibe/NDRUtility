/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Obs;

/**
 *
 * @author openmrsdev
 */
public class CareCardFollowUp {
    private int patientID;
    private int locationID;
    private int encounterID;
    private int formID;
    private int creatorID;
    private int providerID;
    private String pepfarID;
    private String hospID;
    private String location;
    private Date visitDate;
    private String provider;
    private String pmmForm;
    private String enteredBy;
    private Date dateEntered;
    /* Standard */
    private String visitType;
    private String isScheduled;
    private double weight;
    private double height;
    private double systolicBP;
    private double diastolicBP;
    private double surfaceArea;
    private double headCircumference;
    private double muac;
    private double temperature;
    private String PMTCTLink;
    private Date EDD;
    private String familyPlanning;
    private String familyPlanningMethod;
    private String functionalStatus;
    private String developmentalStatus;
    private String WHO;
    private String TBStatus;
    private String TBNo;
    private String signsAndSymptoms;
    private String otherSignsAndSymptoms;
    private String presumedARVSideEffects;
    private String regimenLine;
    private String firstLineRegimen;
    private String secondLineRegimen;
    private String otherRegimen;
    private String ARVAdherence;
    private String reasonForPoorARVAdherence;
    private String CTXDose;
    private String CTXAdherence;
    private String reasonForPoorCTXAdherence;
    private String INHDose;
    private String INHAdherence;
    private String reasonForPoorINHAdherence;
    private String otherPrescribedDrugs;
    private String testOrdered;
    private String hospitalized;
    private Date hospitalizationDate;
    private String hospitalizationReason;
    private String referrals;
    private String nextAppointment;
    private Date nextAppointmentDate;
    
    
   /*private String[] title={
       "PATIENT_ID",
       "PMM_FORM",
       "ENCOUNTER_ID",
       "PEPFAR_ID",
       "HOSP_ID",
       "VISIT_DATE",
       "PMM_FORM",
       "VisitType",
       "IsScheduledVisit",
       "Weight(Kg)",
       "Height(cm)",
       "SystolicBP",
       "DiastolicBP",
       "BodySurfaceArea",
       "HeadCircumference(cm)",
       "MUAC(cm)",
       "Temperature",
       "PMTCTLink",
       "EDD",
       "FamilyPlanningStatus",
       "FamilyPlanningMethod",
       "FunctionalStauts",
       "DevelopmentatStatus",
       "WHO",
       "TBStatus",
       "TBCardNo",
       "SignsAndSymptom",
       "OtherSignsAndSymptom",
       "PresumedARVSideEffect",
       "OtherPresumedARVSideEffect",
       "RegimenLine",
       "FirstLineRegimen",
       "SecondLineRegimen",
       "OtherRegimens",
       "OtherDrugs",
       "ARVAdherence",
       "ReasonForPoorOrFairARVAdherence",
       "OtherReasonForPoorOrFairARVAdherence",
       "ChangeTreatmentType",
       "ReasonForRegimenChange",
       "CTXDosePrescribed",
       "CTXAdherence",
       "ReasonForPoorOrFairCTXAdherence",
       "OtherReasonForPoorOrFairCTXAdherence",
       "INHDosePrescribed",
       "INHAdherence",
       "ReasonForPoorOrFairINHAdherence",
       "OtherReasonForPoorOrFairINHAdherence",
       "OtherPrescribedDrugs",
       "TestOrdered",
       "OtherLabTest",
       "Hospitalized",
       "DateOfHospitalization",
       "ReasonForHospitalization",
       "ReferralsOrdered",
       "OtherReferrals",
       "NextAppointment",
       "NextAppointmentDate"
    };*/
   /*private int[] conceptArr={
       1537,
       7778232,
       65,
       571,
       84,
       568,
       1104,
       1105,
       387,
       570,
       7777871,
       577,
       1741,
       1742,
       1013,
       1116,
       7777798,
       862,
       7778105,
       528,
       7778411,
       1607,
       7778145,
       7778111,
       7778108,
       7778109,
       7778410,
       1596,
       7777874,
       7778453,
       7778456,
       7778305,
       1725,
       7778203,
       7777875,
       7778454,
       7778202,
       7777876,
       7778455,
       7778458,
       7778231,
       382,
       7778199,
       45,
       998,
       997,
       260,
       7778345,
       7778345,
       7777821,
       7777822
       
   };*/
   private List<Obs> obsList=new ArrayList<Obs>();

    /**
     * @return the patientID
     */
    public int getPatientID() {
        return patientID;
    }

    /**
     * @param patientID the patientID to set
     */
    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    /**
     * @return the locationID
     */
    public int getLocationID() {
        return locationID;
    }

    /**
     * @param locationID the locationID to set
     */
    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    /**
     * @return the encounterID
     */
    public int getEncounterID() {
        return encounterID;
    }

    /**
     * @param encounterID the encounterID to set
     */
    public void setEncounterID(int encounterID) {
        this.encounterID = encounterID;
    }

    /**
     * @return the formID
     */
    public int getFormID() {
        return formID;
    }

    /**
     * @param formID the formID to set
     */
    public void setFormID(int formID) {
        this.formID = formID;
    }

    /**
     * @return the creatorID
     */
    public int getCreatorID() {
        return creatorID;
    }

    /**
     * @param creatorID the creatorID to set
     */
    public void setCreatorID(int creatorID) {
        this.creatorID = creatorID;
    }

    /**
     * @return the providerID
     */
    public int getProviderID() {
        return providerID;
    }

    /**
     * @param providerID the providerID to set
     */
    public void setProviderID(int providerID) {
        this.providerID = providerID;
    }

    /**
     * @return the pepfarID
     */
    public String getPepfarID() {
        return pepfarID;
    }

    /**
     * @param pepfarID the pepfarID to set
     */
    public void setPepfarID(String pepfarID) {
        this.pepfarID = pepfarID;
    }

    /**
     * @return the hospID
     */
    public String getHospID() {
        return hospID;
    }

    /**
     * @param hospID the hospID to set
     */
    public void setHospID(String hospID) {
        this.hospID = hospID;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the visitDate
     */
    public Date getVisitDate() {
        return visitDate;
    }

    /**
     * @param visitDate the visitDate to set
     */
    public void setVisitDate(Date visitDate) {
        this.visitDate = visitDate;
    }

    /**
     * @return the provider
     */
    public String getProvider() {
        return provider;
    }

    /**
     * @param provider the provider to set
     */
    public void setProvider(String provider) {
        this.provider = provider;
    }

    /**
     * @return the pmmForm
     */
    public String getPmmForm() {
        return pmmForm;
    }

    /**
     * @param pmmForm the pmmForm to set
     */
    public void setPmmForm(String pmmForm) {
        this.pmmForm = pmmForm;
    }

    /**
     * @return the enteredBy
     */
    public String getEnteredBy() {
        return enteredBy;
    }

    /**
     * @param enteredBy the enteredBy to set
     */
    public void setEnteredBy(String enteredBy) {
        this.enteredBy = enteredBy;
    }

    /**
     * @return the dateEntered
     */
    public Date getDateEntered() {
        return dateEntered;
    }

    /**
     * @param dateEntered the dateEntered to set
     */
    public void setDateEntered(Date dateEntered) {
        this.dateEntered = dateEntered;
    }

    /**
     * @return the visitType
     */
    public String getVisitType() {
        return visitType;
    }

    /**
     * @param visitType the visitType to set
     */
    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    /**
     * @return the isScheduled
     */
    public String getIsScheduled() {
        return isScheduled;
    }

    /**
     * @param isScheduled the isScheduled to set
     */
    public void setIsScheduled(String isScheduled) {
        this.isScheduled = isScheduled;
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
     * @return the height
     */
    public double getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * @return the systolicBP
     */
    public double getSystolicBP() {
        return systolicBP;
    }

    /**
     * @param systolicBP the systolicBP to set
     */
    public void setSystolicBP(double systolicBP) {
        this.systolicBP = systolicBP;
    }

    /**
     * @return the diastolicBP
     */
    public double getDiastolicBP() {
        return diastolicBP;
    }

    /**
     * @param diastolicBP the diastolicBP to set
     */
    public void setDiastolicBP(double diastolicBP) {
        this.diastolicBP = diastolicBP;
    }

    /**
     * @return the surfaceArea
     */
    public double getSurfaceArea() {
        return surfaceArea;
    }

    /**
     * @param surfaceArea the surfaceArea to set
     */
    public void setSurfaceArea(double surfaceArea) {
        this.surfaceArea = surfaceArea;
    }

    /**
     * @return the headCircumference
     */
    public double getHeadCircumference() {
        return headCircumference;
    }

    /**
     * @param headCircumference the headCircumference to set
     */
    public void setHeadCircumference(double headCircumference) {
        this.headCircumference = headCircumference;
    }

    /**
     * @return the muac
     */
    public double getMuac() {
        return muac;
    }

    /**
     * @param muac the muac to set
     */
    public void setMuac(double muac) {
        this.muac = muac;
    }

    /**
     * @return the temperature
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * @param temperature the temperature to set
     */
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    /**
     * @return the PMTCTLink
     */
    public String getPMTCTLink() {
        return PMTCTLink;
    }

    /**
     * @param PMTCTLink the PMTCTLink to set
     */
    public void setPMTCTLink(String PMTCTLink) {
        this.PMTCTLink = PMTCTLink;
    }

    /**
     * @return the EDD
     */
    public Date getEDD() {
        return EDD;
    }

    /**
     * @param EDD the EDD to set
     */
    public void setEDD(Date EDD) {
        this.EDD = EDD;
    }

    /**
     * @return the familyPlanning
     */
    public String getFamilyPlanning() {
        return familyPlanning;
    }

    /**
     * @param familyPlanning the familyPlanning to set
     */
    public void setFamilyPlanning(String familyPlanning) {
        this.familyPlanning = familyPlanning;
    }

    /**
     * @return the familyPlanningMethod
     */
    public String getFamilyPlanningMethod() {
        return familyPlanningMethod;
    }

    /**
     * @param familyPlanningMethod the familyPlanningMethod to set
     */
    public void setFamilyPlanningMethod(String familyPlanningMethod) {
        this.familyPlanningMethod = familyPlanningMethod;
    }

    /**
     * @return the functionalStatus
     */
    public String getFunctionalStatus() {
        return functionalStatus;
    }

    /**
     * @param functionalStatus the functionalStatus to set
     */
    public void setFunctionalStatus(String functionalStatus) {
        this.functionalStatus = functionalStatus;
    }

    /**
     * @return the developmentalStatus
     */
    public String getDevelopmentalStatus() {
        return developmentalStatus;
    }

    /**
     * @param developmentalStatus the developmentalStatus to set
     */
    public void setDevelopmentalStatus(String developmentalStatus) {
        this.developmentalStatus = developmentalStatus;
    }

    /**
     * @return the WHO
     */
    public String getWHO() {
        return WHO;
    }

    /**
     * @param WHO the WHO to set
     */
    public void setWHO(String WHO) {
        this.WHO = WHO;
    }

    /**
     * @return the TBStatus
     */
    public String getTBStatus() {
        return TBStatus;
    }

    /**
     * @param TBStatus the TBStatus to set
     */
    public void setTBStatus(String TBStatus) {
        this.TBStatus = TBStatus;
    }

    /**
     * @return the TBNo
     */
    public String getTBNo() {
        return TBNo;
    }

    /**
     * @param TBNo the TBNo to set
     */
    public void setTBNo(String TBNo) {
        this.TBNo = TBNo;
    }

    /**
     * @return the signsAndSymptoms
     */
    public String getSignsAndSymptoms() {
        return signsAndSymptoms;
    }

    /**
     * @param signsAndSymptoms the signsAndSymptoms to set
     */
    public void setSignsAndSymptoms(String signsAndSymptoms) {
        this.signsAndSymptoms = signsAndSymptoms;
    }

    /**
     * @return the otherSignsAndSymptoms
     */
    public String getOtherSignsAndSymptoms() {
        return otherSignsAndSymptoms;
    }

    /**
     * @param otherSignsAndSymptoms the otherSignsAndSymptoms to set
     */
    public void setOtherSignsAndSymptoms(String otherSignsAndSymptoms) {
        this.otherSignsAndSymptoms = otherSignsAndSymptoms;
    }

    /**
     * @return the presumedARVSideEffects
     */
    public String getPresumedARVSideEffects() {
        return presumedARVSideEffects;
    }

    /**
     * @param presumedARVSideEffects the presumedARVSideEffects to set
     */
    public void setPresumedARVSideEffects(String presumedARVSideEffects) {
        this.presumedARVSideEffects = presumedARVSideEffects;
    }

    /**
     * @return the regimenLine
     */
    public String getRegimenLine() {
        return regimenLine;
    }

    /**
     * @param regimenLine the regimenLine to set
     */
    public void setRegimenLine(String regimenLine) {
        this.regimenLine = regimenLine;
    }

    /**
     * @return the firstLineRegimen
     */
    public String getFirstLineRegimen() {
        return firstLineRegimen;
    }

    /**
     * @param firstLineRegimen the firstLineRegimen to set
     */
    public void setFirstLineRegimen(String firstLineRegimen) {
        this.firstLineRegimen = firstLineRegimen;
    }

    /**
     * @return the secondLineRegimen
     */
    public String getSecondLineRegimen() {
        return secondLineRegimen;
    }

    /**
     * @param secondLineRegimen the secondLineRegimen to set
     */
    public void setSecondLineRegimen(String secondLineRegimen) {
        this.secondLineRegimen = secondLineRegimen;
    }

    /**
     * @return the otherRegimen
     */
    public String getOtherRegimen() {
        return otherRegimen;
    }

    /**
     * @param otherRegimen the otherRegimen to set
     */
    public void setOtherRegimen(String otherRegimen) {
        this.otherRegimen = otherRegimen;
    }

    /**
     * @return the ARVAdherence
     */
    public String getARVAdherence() {
        return ARVAdherence;
    }

    /**
     * @param ARVAdherence the ARVAdherence to set
     */
    public void setARVAdherence(String ARVAdherence) {
        this.ARVAdherence = ARVAdherence;
    }

    /**
     * @return the reasonForPoorARVAdherence
     */
    public String getReasonForPoorARVAdherence() {
        return reasonForPoorARVAdherence;
    }

    /**
     * @param reasonForPoorARVAdherence the reasonForPoorARVAdherence to set
     */
    public void setReasonForPoorARVAdherence(String reasonForPoorARVAdherence) {
        this.reasonForPoorARVAdherence = reasonForPoorARVAdherence;
    }

    /**
     * @return the INHDose
     */
    public String getINHDose() {
        return INHDose;
    }

    /**
     * @param INHDose the INHDose to set
     */
    public void setINHDose(String INHDose) {
        this.INHDose = INHDose;
    }

    /**
     * @return the INHAdherence
     */
    public String getINHAdherence() {
        return INHAdherence;
    }

    /**
     * @param INHAdherence the INHAdherence to set
     */
    public void setINHAdherence(String INHAdherence) {
        this.INHAdherence = INHAdherence;
    }

    /**
     * @return the reasonForPoorINHAdherence
     */
    public String getReasonForPoorINHAdherence() {
        return reasonForPoorINHAdherence;
    }

    /**
     * @param reasonForPoorINHAdherence the reasonForPoorINHAdherence to set
     */
    public void setReasonForPoorINHAdherence(String reasonForPoorINHAdherence) {
        this.reasonForPoorINHAdherence = reasonForPoorINHAdherence;
    }

    /**
     * @return the otherPrescribedDrugs
     */
    public String getOtherPrescribedDrugs() {
        return otherPrescribedDrugs;
    }

    /**
     * @param otherPrescribedDrugs the otherPrescribedDrugs to set
     */
    public void setOtherPrescribedDrugs(String otherPrescribedDrugs) {
        this.otherPrescribedDrugs = otherPrescribedDrugs;
    }

    /**
     * @return the testOrdered
     */
    public String getTestOrdered() {
        return testOrdered;
    }

    /**
     * @param testOrdered the testOrdered to set
     */
    public void setTestOrdered(String testOrdered) {
        this.testOrdered = testOrdered;
    }

    /**
     * @return the hospitalized
     */
    public String getHospitalized() {
        return hospitalized;
    }

    /**
     * @param hospitalized the hospitalized to set
     */
    public void setHospitalized(String hospitalized) {
        this.hospitalized = hospitalized;
    }

    /**
     * @return the hospitalizationReason
     */
    public String getHospitalizationReason() {
        return hospitalizationReason;
    }

    /**
     * @param hospitalizationReason the hospitalizationReason to set
     */
    public void setHospitalizationReason(String hospitalizationReason) {
        this.hospitalizationReason = hospitalizationReason;
    }

    /**
     * @return the referrals
     */
    public String getReferrals() {
        return referrals;
    }

    /**
     * @param referrals the referrals to set
     */
    public void setReferrals(String referrals) {
        this.referrals = referrals;
    }

    /**
     * @return the nextAppointment
     */
    public String getNextAppointment() {
        return nextAppointment;
    }

    /**
     * @param nextAppointment the nextAppointment to set
     */
    public void setNextAppointment(String nextAppointment) {
        this.nextAppointment = nextAppointment;
    }

    /**
     * @return the nextAppointmentDate
     */
    public Date getNextAppointmentDate() {
        return nextAppointmentDate;
    }

    /**
     * @param nextAppointmentDate the nextAppointmentDate to set
     */
    public void setNextAppointmentDate(Date nextAppointmentDate) {
        this.nextAppointmentDate = nextAppointmentDate;
    }

    /**
     * @return the obsList
     */
    public List<Obs> getObsList() {
        return obsList;
    }

    /**
     * @param obsList the obsList to set
     */
    public void setObsList(List<Obs> obsList) {
        this.obsList = obsList;
    }

    /**
     * @return the CTXDose
     */
    public String getCTXDose() {
        return CTXDose;
    }

    /**
     * @param CTXDose the CTXDose to set
     */
    public void setCTXDose(String CTXDose) {
        this.CTXDose = CTXDose;
    }

    /**
     * @return the CTXAdherence
     */
    public String getCTXAdherence() {
        return CTXAdherence;
    }

    /**
     * @param CTXAdherence the CTXAdherence to set
     */
    public void setCTXAdherence(String CTXAdherence) {
        this.CTXAdherence = CTXAdherence;
    }

    /**
     * @return the reasonForPoorCTXAdherence
     */
    public String getReasonForPoorCTXAdherence() {
        return reasonForPoorCTXAdherence;
    }

    /**
     * @param reasonForPoorCTXAdherence the reasonForPoorCTXAdherence to set
     */
    public void setReasonForPoorCTXAdherence(String reasonForPoorCTXAdherence) {
        this.reasonForPoorCTXAdherence = reasonForPoorCTXAdherence;
    }

    /**
     * @return the hospitalizationDate
     */
    public Date getHospitalizationDate() {
        return hospitalizationDate;
    }

    /**
     * @param hospitalizationDate the hospitalizationDate to set
     */
    public void setHospitalizationDate(Date hospitalizationDate) {
        this.hospitalizationDate = hospitalizationDate;
    }
}
