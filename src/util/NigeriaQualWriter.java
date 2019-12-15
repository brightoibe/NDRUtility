/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import model.datapump.Concept;
import model.datapump.Demographics;
import model.datapump.Form;
import model.datapump.Obs;
import model.datapump.PatientRegimen;
import model.nigeriaqual.CareAndSupportAssessmentRecord;
import model.nigeriaqual.DataARTAdherence;
import model.nigeriaqual.DataARTRecord;
import model.nigeriaqual.DataBaselineParameters;
import model.nigeriaqual.DataClinicalEvaluationInReviewPeriod;
import model.nigeriaqual.DataCotrimoxazole;
import model.nigeriaqual.DataHepatitisB;
import model.nigeriaqual.DataPatientDemographics;
import model.nigeriaqual.DataPatientMonitoringReviewPeriod;
import model.nigeriaqual.DataPatientStatusReviewPeriod;
import model.nigeriaqual.DataRegimenDuringReview;
import model.nigeriaqual.DataTuberculosisRecord;
import model.nigeriaqual.DataViralLoadTestingReviewPeriod;
import model.nigeriaqual.PedPatientDemographics;
import model.nigeriaqual.PediatricARTAdherence;
import model.nigeriaqual.PediatricARTRegimenSinceStartingTreatment;
import model.nigeriaqual.PediatricBaselineParameters;
import model.nigeriaqual.PediatricClinicalEvaluationInReviewPeriod;
import model.nigeriaqual.PediatricCotrimoxazole;
import model.nigeriaqual.PediatricEducation;
import model.nigeriaqual.PediatricLinkage;
import model.nigeriaqual.PediatricPatientMonitoringDuringReviewPeriod;
import model.nigeriaqual.PediatricPatientStatus;
import model.nigeriaqual.PediatricPatientType;
import model.nigeriaqual.PediatricTuberculosis;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.Years;

/**
 *
 * @author brightoibe
 */
public class NigeriaQualWriter {

    private LocationMap locationMap;
    private EncryptorDecryptor encryptor;
    private HashMap<Integer, Concept> conceptDictionary;
    private final static int REVIEW_PERIOD_ID = 5;

    public NigeriaQualWriter() throws InvalidKeyException, UnsupportedEncodingException, InvalidKeySpecException, NoSuchAlgorithmException {
        encryptor = EncryptorDecryptor.getInstance();

    }

    public void setConceptDictionary(HashMap<Integer, Concept> dictionary, LocationMap locMap) {
        conceptDictionary = dictionary;
        this.locationMap = locMap;
    }

    /*private void loadLocationMap() {
        locationMap = new HashMap<Integer, Integer>();
        locationMap.put(41, 1823110072);
        locationMap.put(72, 1015210009);
        locationMap.put(74, 1019210015);
        locationMap.put(42, 1008210001);
        locationMap.put(68, 1005210006);
        locationMap.put(63, 723220054);
        locationMap.put(65, 712310066);
        locationMap.put(56, 376310353);
        locationMap.put(36, 376310001);
        locationMap.put(38, 376220002);
        //locationMap.put(104, 376210354);// Clash Gwarimpa GH and Mbagen
        locationMap.put(104, 70411043);// Clash Gwarimpa GH and Mbagen
        locationMap.put(154, 376210063);
        locationMap.put(50, 376210027);
        locationMap.put(2, 376210026);
        locationMap.put(37, 376110052);
        locationMap.put(39, 373310001);
        locationMap.put(52, 373110086);
        locationMap.put(51, 373110002);
        locationMap.put(108, 372210034);
        locationMap.put(54, 372210034);
        locationMap.put(139, 271131036);
        locationMap.put(137, 271021054);
        locationMap.put(140, 270521034);
        locationMap.put(44, 270231018);
        locationMap.put(14, 250811101);
        locationMap.put(7, 250721013);
        locationMap.put(4, 250631001);
        locationMap.put(12, 250421065);
        locationMap.put(8, 250421001);
        locationMap.put(48, 202131034);
        locationMap.put(99, 202121011);
        locationMap.put(98, 202021017);
        locationMap.put(95, 201421032);
        locationMap.put(66, 71331010);
        locationMap.put(110, 70522002);
        locationMap.put(90, 20412131);
        locationMap.put(91, 19422127);
        locationMap.put(49, 19383102);
        locationMap.put(88, 19372135);
        locationMap.put(87, 19311228);
        locationMap.put(83, 19162101);
        locationMap.put(92, 19052101);
        locationMap.put(43, 1015310001);
        locationMap.put(45, 292731001);
        locationMap.put(47, 291331001);
        locationMap.put(64, 70512099);
        locationMap.put(55, 376210355);
        locationMap.put(3, 250831001);
        locationMap.put(46, 271831024);
        locationMap.put(6, 250321001);
        /*
        41,1823110072
43,1015310001
72,1015210009
42,1008210001
68,1005210006
63,723220054
65,712310066
56,376310353
36,376310001
38,376220002
104,376210354
154,376210063
50,376210027*
        
2,376210026
37,376110052
39,373310001
52,373110086
51,373110002
108,372210034
54,372210034
139,271131036
137,271021054
140,270521034
44,270231018
14,250811101
7,250721013
4,250631001
12,250421065
8,250421001
48,202131034
99,202121011
98,202021017
95,201421032
66,71331010****
110,70522002
90,20412131
91,19422127
49,19383102
88,19372135
87,19311228
83,19162101
92,19052101

        

    } */

 /*public void loadDictionaries() {
        loadLocationMap();
    }*/
    public String encrypt(String str) {
        return encryptor.encrypt(str);
    }

    public DataPatientDemographics createDataPatientDemographics(Demographics dgr, ArrayList<Obs> obsList, ArrayList<Integer> hospitalizedPatients, ArrayList<Integer> clinicVisitReviewPeriod, ArrayList<Integer> clinicVisit3MonthsReportingPeriod, HashMap<Integer, Date> firstVisitDateDictionary, model.datapump.Location loc) {
        DataPatientDemographics demo = new DataPatientDemographics();
        //demo.setFirstName(encrypt(dgr.getFirstName()));
        //demo.setLastName(encrypt(dgr.getLastName()));
        demo.setFirstName("");
        demo.setLastName("");
        if (clinicVisitReviewPeriod.contains(dgr.getPatientID())) {
            demo.setClinicVisitReviewPeriod("YES");
        } else {
            demo.setClinicVisitReviewPeriod("NO");
        }
        if (clinicVisit3MonthsReportingPeriod.contains(dgr.getPatientID())) {
            demo.setClinicVisit3MonthsReviewPeriod("YES");
        } else {
            demo.setClinicVisit3MonthsReviewPeriod("NO");
        }
        if (hospitalizedPatients.contains(dgr.getPatientID())) {
            demo.setHosptalAdmissionReviewPeriod("YES");
        } else {
            demo.setHosptalAdmissionReviewPeriod("NO");
        }
        demo.setHospID(dgr.getHospID());
        demo.setRnl(String.valueOf(dgr.getPatientID()));
        String gender = dgr.getGender();
        if (gender.equals("F")) {
            demo.setGender("FEMALE");
        } else if (gender.equals("M")) {
            demo.setGender("MALE");
        }
        demo.setDob(dgr.getDateOfBirth());
        demo.setAge(dgr.getAge());
        demo.setStateOfResidence(dgr.getAddress_state());
        demo.setLgaOfResidence(dgr.getAddress_lga());
        demo.setWardVillageTown(dgr.getAddress2());
        demo.setFacilityID(locationMap.getNigeriaQualID(loc));
        demo.setPatientID(dgr.getPepfarID());
        //demo.setPatientID(String.valueOf(dgr.getPatientID()));
        Date enrollDt = null, adultEnrollmentDt = dgr.getAdultEnrollmentDt(), peadEnrollmentDt = dgr.getPeadEnrollmentDt();
        if (adultEnrollmentDt != null) {
            enrollDt = adultEnrollmentDt;
        }
        if (adultEnrollmentDt == null && peadEnrollmentDt != null) {
            enrollDt = peadEnrollmentDt;
        } else if (adultEnrollmentDt == null && peadEnrollmentDt == null) {
            enrollDt = firstVisitDateDictionary.get(dgr.getPatientID());
        }
        demo.setDateEnrolled(enrollDt);
        demo.setRecordCompletionPosition(15);
        demo.setWebUploadFlag("No");
        demo.setReviewPeriodID(REVIEW_PERIOD_ID);
        int conceptID = 0;
        int valueCoded = 0;
        Date valueDate = null;
        Concept c = null;
        for (Obs obs : obsList) {
            if (obs.getPatientID() == dgr.getPatientID()) {
                conceptID = obs.getConceptID();
                switch (conceptID) {
                    case 1075:
                        //c = conceptDictionary.get(obs.getValueCoded());
                        valueCoded = obs.getValueCoded();
                        String maritalStatus = "";
                        switch (valueCoded) {
                            case 348:
                                maritalStatus = "SINGLE";
                                demo.setMaritalStatus(maritalStatus);
                                break;
                            case 350:
                                maritalStatus = "MARRIED";
                                demo.setMaritalStatus(maritalStatus);
                                break;
                            case 351:
                                maritalStatus = "DIVORCED";
                                demo.setMaritalStatus(maritalStatus);
                                break;
                            case 1522:
                                maritalStatus = "SEPERATED";
                                demo.setMaritalStatus(maritalStatus);
                                break;
                            case 349:
                                maritalStatus = "WIDOWED";
                                demo.setMaritalStatus(maritalStatus);
                                break;
                            default:
                                break;
                        }
                        break;
                    case 859:
                        valueDate = obs.getValueDate();
                        demo.setDateConfirmedPositive(valueDate);
                        break;
                    case 1077:
                        valueCoded = obs.getValueCoded();
                        String occupationalStatus = "";
                        switch (valueCoded) {
                            case 1520:
                                occupationalStatus = "STUDENT";
                                demo.setOccupation(occupationalStatus);
                                break;
                            case 1076:
                                occupationalStatus = "EMPLOYED";
                                demo.setOccupation(occupationalStatus);
                                break;
                            case 906:
                                occupationalStatus = "UNEMPLOYED";
                                demo.setOccupation(occupationalStatus);
                                break;
                            case 1521:
                                occupationalStatus = "RETIRED";
                                demo.setOccupation(occupationalStatus);
                                break;
                            case 789:
                                occupationalStatus = "N/A";
                                demo.setOccupation(occupationalStatus);
                                break;
                            default:
                                break;
                        }

                        break;
                    case 1079:
                        valueCoded = obs.getValueCoded();
                        String educationalLevel = "";
                        switch (valueCoded) {
                            case 28:
                                educationalLevel = "NONE";
                                demo.setEducation(educationalLevel);
                                break;
                            case 1078:
                                educationalLevel = "QUARANIC";
                                demo.setEducation(educationalLevel);
                                break;
                            case 1525:
                                educationalLevel = "PRIMARY";
                                demo.setEducation(educationalLevel);
                                break;
                            case 518:
                                educationalLevel = "SENIOR SECONDARY";
                                demo.setEducation(educationalLevel);
                                break;
                            case 1523:
                                educationalLevel = "POST SECONDARY";
                                demo.setEducation(educationalLevel);
                                break;
                            default:
                                break;
                        }

                        break;

                }
            }
        }

        return demo;
    }

    public PedPatientDemographics createPedPatientDemographics(Demographics dgr, ArrayList<Obs> obsList, ArrayList<Integer> hospitalizedPatients, ArrayList<Integer> clinicVisitReviewPeriod, HashMap<Integer, Date> firstVisitDateDictionary, HashMap<Integer, Date> lastVisitMap, model.datapump.Location loc) {
        PedPatientDemographics demo = new PedPatientDemographics();
        //demo.setFirstName(encrypt(dgr.getFirstName()));
        //demo.setLastName(encrypt(dgr.getLastName()));
        demo.setFirstName("");
        demo.setLastName("");
        demo.setDateOfLastVisit(lastVisitMap.get(dgr.getPatientID()));

        if (clinicVisitReviewPeriod.contains(dgr.getPatientID())) {
            demo.setClinicVisit6MonthsPriorToReviewPeriod("YES");
        } else {
            demo.setClinicVisit6MonthsPriorToReviewPeriod("NO");
        }
        if (hospitalizedPatients.contains(dgr.getPatientID())) {
            demo.setAdmissionDuringReviewPeriod("YES");
        } else {
            demo.setAdmissionDuringReviewPeriod("NO");
        }
        demo.setHospitalNo(dgr.getHospID());
        demo.setRnlSerialNo(String.valueOf(dgr.getPatientID()));
        String gender = dgr.getGender();
        if (gender.equals("F")) {
            demo.setGender("FEMALE");
        } else if (gender.equals("M")) {
            demo.setGender("MALE");
        }
        demo.setDateOfBirth(dgr.getDateOfBirth());
        demo.setAge(dgr.getAge());
        demo.setUnitOfAgeMeasure("YEARS");
        demo.setStateOfResidence(dgr.getAddress_state());
        demo.setLgaOfResidence(dgr.getAddress_lga());
        //demo.setWardVillageTown(dgr.getAddress2());
        demo.setFacilityID(locationMap.getNigeriaQualID(loc));
        demo.setPatientID(dgr.getPepfarID());
        //demo.setPatientID(String.valueOf(dgr.getPatientID()));
        Date enrollDt = null, adultEnrollmentDt = dgr.getAdultEnrollmentDt(), peadEnrollmentDt = dgr.getPeadEnrollmentDt();
        if (adultEnrollmentDt != null) {
            enrollDt = adultEnrollmentDt;
        }
        if (adultEnrollmentDt == null && peadEnrollmentDt != null) {
            enrollDt = peadEnrollmentDt;
        } else if (adultEnrollmentDt == null && peadEnrollmentDt == null) {
            enrollDt = firstVisitDateDictionary.get(dgr.getPatientID());
        }
        demo.setDateEnrolledInCare(enrollDt);
        demo.setRecordCompletionPosition(15);
        demo.setWebUploadFlag("No");
        demo.setReviewPeriodID(REVIEW_PERIOD_ID);
        int conceptID = 0;
        int valueCoded = 0;
        Concept c = null;
        String careGiver = "";
        for (Obs obs : obsList) {
            if (obs.getPatientID() == dgr.getPatientID()) {
                conceptID = obs.getConceptID();
                switch (conceptID) {
                    case 1073:
                        careGiver = obs.getVariableValue();
                        demo.setPrimaryCareGiver(careGiver);
                        break;
                    case 1070:
                        careGiver = obs.getVariableValue();
                        demo.setPrimaryCareGiver(careGiver);
                        break;
                    case 1077:
                        valueCoded = obs.getValueCoded();
                        String occupationalStatus = "";
                        switch (valueCoded) {
                            case 1520:
                                occupationalStatus = "STUDENT";
                                demo.setCareGiverOccupation(occupationalStatus);
                                break;
                            case 1076:
                                occupationalStatus = "EMPLOYED";
                                demo.setCareGiverOccupation(occupationalStatus);
                                break;
                            case 906:
                                occupationalStatus = "UNEMPLOYED";
                                demo.setCareGiverOccupation(occupationalStatus);
                                break;
                            case 1521:
                                occupationalStatus = "RETIRED";
                                demo.setCareGiverOccupation(occupationalStatus);
                                break;
                            case 789:
                                occupationalStatus = "N/A";
                                demo.setCareGiverOccupation(occupationalStatus);
                                break;
                            default:
                                break;
                        }

                        break;

                }
            }
        }

        return demo;
    }

    public boolean containsPatient(int patientID, ArrayList<Obs> obsList) {
        boolean ans = false;
        for (Obs obs : obsList) {
            if (obs.getPatientID() == patientID) {
                ans = true;
            }
        }
        return ans;
    }

    public int getHepatitisBResult(int patientID, ArrayList<Obs> obsList) {
        int ans = 0;
        for (Obs obs : obsList) {
            if (obs.getPatientID() == patientID) {
                ans = obs.getValueCoded();
            }
        }
        return ans;
    }

    public DataHepatitisB createDataHepatitisB(ArrayList<Obs> obsList, Set<Integer> clinicalEvalFormLastVisitList, model.datapump.Location loc, int patientID, String pepfarID) {
        DataHepatitisB hbp = new DataHepatitisB();
        hbp.setPatientID(pepfarID);
        //hbp.setPatientID(String.valueOf(patientID));
        if (containsPatient(patientID, obsList)) {
            hbp.setHepatitisBAssayEverDoneForPatient("Yes");
            int code = getHepatitisBResult(patientID, obsList);
            if (code == 8) {
                hbp.setResultOfHepatitisBAssay("Negative");
            } else if (code == 2) {
                hbp.setResultOfHepatitisBAssay("Positive");
            }
        } else {
            hbp.setHepatitisBAssayEverDoneForPatient("No");
        }
        if (clinicalEvalFormLastVisitList.contains(patientID)) {
            hbp.setClinicalEvaluationARTFormFilledAtLastVisit("Yes");
        } else {
            hbp.setClinicalEvaluationARTFormFilledAtLastVisit("No");
        }
        hbp.setFacilityID(locationMap.getNigeriaQualID(loc));
        hbp.setWebUploadFlag("No");
        hbp.setReviewPeriodID(REVIEW_PERIOD_ID);
        return hbp;

    }

    public DataTuberculosisRecord createDataTBRecord(Set<Integer> ptsScreenSet, HashMap<Integer, Obs> obsTBStatusMap, HashMap<Integer, Obs> obsTBScrn, Set<Integer> ptsTbTreatmentStartDtSet, Set<Integer> csrSet, int patientID, String pepfarID, model.datapump.Location loc) {
        DataTuberculosisRecord dtb = new DataTuberculosisRecord();
        dtb.setPatientID(pepfarID);
        //dtb.setPatientID(String.valueOf(patientID));
        if (ptsTbTreatmentStartDtSet.contains(patientID)) {
            dtb.setPatientOnTBTreatmentAtStartOfReviewPeriod("Yes");
        } else {
            dtb.setPatientOnTBTreatmentAtStartOfReviewPeriod("No");
        }
        //if (obsTBStatusMap.keySet().contains(patientID) || obsTBScrn.keySet().contains(patientID)) {
        if (ptsScreenSet.contains(patientID)) {
            dtb.setPatientClinicallyScreenForTBDuringReviewPeriod("Yes");
        } else {
            dtb.setPatientClinicallyScreenForTBDuringReviewPeriod("No");
        }
        if (obsTBScrn.keySet().contains(patientID)) {
            Obs obs = obsTBScrn.get(patientID);
            int valueCoded = obs.getValueCoded();
            switch (valueCoded) {
                case 540:
                    dtb.setTBClinicalScreeningCriteria("Cough");
                    dtb.setTBScreeningCriteria_CurrentCough("Yes");
                    break;
                case 201:
                    dtb.setTBClinicalScreeningCriteria("Fever");
                    break;
                case 204:
                    dtb.setTBClinicalScreeningCriteria("Night sweat");
                    break;
                case 193:
                    dtb.setTBClinicalScreeningCriteria("Weight loss");
                    dtb.setTBScreeningCriteria_PoorWeightGain("Yes");
                    break;
                default:
                    dtb.setTBScreeningCriteria_CurrentCough("No");
                    dtb.setTBScreeningCriteria_PoorWeightGain("No");
                    break;
            }
        } else {
            dtb.setTBScreeningCriteria_CurrentCough("No");
            dtb.setTBScreeningCriteria_PoorWeightGain("No");
        }
        if (obsTBStatusMap.keySet().contains(patientID)) {
            Obs obs = obsTBStatusMap.get(patientID);
            int valueCoded = obs.getValueCoded();
            switch (valueCoded) {
                case 872:
                    dtb.setPatientStartTBTreatment("Yes");
                    dtb.setPatientDiagnosedOfTBInReviewPeriod("Yes");
                    break;
                case 870:
                    dtb.setBasedOnScreeningWasPatientedSuspectedToHaveTB("Yes");
                    break;
                case 410:
                    dtb.setTBScreeningCriteria_CurrentCough("Yes");
                    break;
                default:
                    dtb.setPatientStartTBTreatment("No");
                    dtb.setPatientDiagnosedOfTBInReviewPeriod("No");
                    dtb.setBasedOnScreeningWasPatientedSuspectedToHaveTB("No");
                    break;
            }
        } else {
            dtb.setPatientStartTBTreatment("No");
            dtb.setPatientDiagnosedOfTBInReviewPeriod("No");
            dtb.setBasedOnScreeningWasPatientedSuspectedToHaveTB("No");
        }
        if (csrSet.contains(patientID)) {
            dtb.setPatientHaveCRXPerformedDuringReviewPeriod("Yes");
        } else {
            dtb.setPatientHaveCRXPerformedDuringReviewPeriod("No");
        }
        dtb.setWebUploadFlag("Yes");
        dtb.setFacilityID(locationMap.getNigeriaQualID(loc));
        dtb.setReviewPeriodID(REVIEW_PERIOD_ID);
        return dtb;
    }

    public PediatricTuberculosis createPediatricTBRecord(HashMap<Integer, Obs> obsTBStatusMap, Set<Integer> ptsTBScreenSet, HashMap<Integer, Obs> obsTBScrn, Set<Integer> ptsTbTreatmentStartDtSet, Set<Integer> csrSet, int patientID, String pepfarID, model.datapump.Location loc) {
        PediatricTuberculosis dtb = new PediatricTuberculosis();
        dtb.setPatientID(pepfarID);
        //dtb.setPatientID(String.valueOf(patientID));
        /*if (ptsTbTreatmentStartDtSet.contains(patientID)) {
            dtb.setPatientOnTBTreatmentAtStartOfReviewPeriod("Yes");
        } else {
            dtb.setPatientOnTBTreatmentAtStartOfReviewPeriod("No");
        }*/
        Obs ob = obsTBStatusMap.get(patientID);

        /*if (obsTBStatusMap.keySet().contains(patientID) || obsTBScrn.keySet().contains(patientID)) {
            dtb.setPatientClinicallyScreenForTBDuringReviewPeriod("Yes");
        } else {
            dtb.setPatientClinicallyScreenForTBDuringReviewPeriod("No");
        }*/
        if (ptsTBScreenSet.contains(patientID)) {
            dtb.setPatientClinicallyScreenForTBDuringReviewPeriod("Yes");
        } else {
            dtb.setPatientClinicallyScreenForTBDuringReviewPeriod("No");
        }
        if (obsTBScrn.keySet().contains(patientID)) {
            Obs obs = obsTBScrn.get(patientID);
            int valueCoded = obs.getValueCoded();
            switch (valueCoded) {
                case 540:
                    dtb.setTbClinicalScreeningCriteria("Cough");
                    dtb.setTBScreeningCriteria_CurrentCough("Yes");
                    break;
                case 201:
                    dtb.setTbClinicalScreeningCriteria("Fever");
                    break;
                case 204:
                    dtb.setTbClinicalScreeningCriteria("Night sweat");
                    break;
                case 193:
                    dtb.setTbClinicalScreeningCriteria("Weight loss");
                    dtb.setTBScreeningCriteria_PoorWeightGain("Yes");
                    break;
                default:
                    dtb.setTBScreeningCriteria_CurrentCough("No");
                    dtb.setTBScreeningCriteria_PoorWeightGain("No");
                    break;
            }
        } else {
            dtb.setTBScreeningCriteria_CurrentCough("No");
            dtb.setTBScreeningCriteria_PoorWeightGain("No");
        }
        if (obsTBStatusMap.keySet().contains(patientID)) {
            Obs obs = obsTBStatusMap.get(patientID);
            int valueCoded = obs.getValueCoded();
            switch (valueCoded) {
                case 872:
                    dtb.setPatientStartTBTreatment("Yes");
                    dtb.setPatientOnTBTreatmentDuringReviewPeriod("Yes");
                    dtb.setPatientDiagnosedOfTBInReviewPeriod("Yes");
                    break;
                case 870:
                    dtb.setBasedOnScreeningWasPatientedSuspectedToHaveTB("Yes");
                    break;
                case 410:
                    dtb.setTBScreeningCriteria_CurrentCough("Yes");
                    break;
                default:
                    dtb.setPatientStartTBTreatment("No");
                    dtb.setPatientDiagnosedOfTBInReviewPeriod("No");
                    dtb.setPatientOnTBTreatmentDuringReviewPeriod("No");
                    dtb.setBasedOnScreeningWasPatientedSuspectedToHaveTB("No");
                    break;
            }
        } else {
            dtb.setPatientStartTBTreatment("No");
            dtb.setPatientDiagnosedOfTBInReviewPeriod("No");
            dtb.setPatientOnTBTreatmentDuringReviewPeriod("No");
            dtb.setBasedOnScreeningWasPatientedSuspectedToHaveTB("No");
        }
        if (csrSet.contains(patientID)) {
            dtb.setPatientHdChestXRay("Yes");
        } else {
            dtb.setPatientHdChestXRay("No");
        }
        dtb.setWebUploadFlag("Yes");
        dtb.setFacilityID(locationMap.getNigeriaQualID(loc));
        dtb.setReviewPeriodID(REVIEW_PERIOD_ID);
        return dtb;
    }

    public CareAndSupportAssessmentRecord createCareAndSupportRecord(Set<Integer> ptsCSForm, Set<Integer> ptsNutriAccessInReview, Set<Integer> nutriAssessmentEver, model.datapump.Location loc, int patientID, String pepfarID) {
        CareAndSupportAssessmentRecord cs = new CareAndSupportAssessmentRecord();
        cs.setPatientID(pepfarID);
        //cs.setPatientID(String.valueOf(patientID));
        if (ptsCSForm.contains(patientID)) {
            cs.setCareAndSupportAssementFormInPatientFolder("Yes");
            cs.setPatientReceiveCareAndSupportAssessmentInReviewPeriod("Yes");
        } else {
            cs.setCareAndSupportAssementFormInPatientFolder("No");
            cs.setPatientReceiveCareAndSupportAssessmentInReviewPeriod("No");
        }

        if (nutriAssessmentEver.contains(patientID)) {
            cs.setNutritionalAssessmentEverDoneForPatientSinceEnrolment("Yes");
        } else {
            cs.setNutritionalAssessmentEverDoneForPatientSinceEnrolment("No");
        }
        if (ptsNutriAccessInReview.contains(patientID)) {
            cs.setNutritionalAssessmentEverDoneForPatientSinceEnrolment("Yes");
        } else {
            cs.setNutritionalAssessmentEverDoneForPatientSinceEnrolment("No");
        }
        cs.setFacilityID(locationMap.getNigeriaQualID(loc));
        cs.setWebUploadFlag("No");
        cs.setReviewPeriodID(REVIEW_PERIOD_ID);
        return cs;
    }

    public DataCotrimoxazole createDataCotrimoxazole(PatientRegimen ptsRegimen, model.datapump.Location loc) {
        DataCotrimoxazole dc = new DataCotrimoxazole();
        dc.setPatientID(ptsRegimen.getPepfarID());
        if (ptsRegimen.getDrugName().contains("Cotri")) {
            dc.setPatientCurrentlyOnCotrimoxazoleProphylaxis("Yes");
            dc.setPatientReceiveCotrimoxazoleDuringReviewPeriod("Yes");
        }
        dc.setDateOfLastPrescription(ptsRegimen.getStartDate());
        dc.setWebUploadFlag("No");
        dc.setReviewPeriodID(REVIEW_PERIOD_ID);
        dc.setFacilityID(locationMap.getNigeriaQualID(loc));
        return dc;

    }

    public PediatricCotrimoxazole createPediatricCotrimoxazole(HashMap<Integer, Demographics> ptsMap, Integer id, HashMap<Integer, Date> ptsFirstCTXDateMap, model.datapump.Location loc) {

        PediatricCotrimoxazole dc = new PediatricCotrimoxazole();
        Demographics dmg = null;
        dmg = ptsMap.get(id);
        if (dmg != null) {
            dc.setPatientID(String.valueOf(dmg.getPepfarID()));
        }

        //dc.setPatientID(String.valueOf(ptsRegimen.getPatientID()));
        //if (ptsRegimen.getDrugName().contains("Cotri")) {
        if (ptsFirstCTXDateMap.containsKey(id)) {
            dc.setPatientCurrentlyOnCotrimoxazoleProphylaxis("Yes");
            //dc.setPatientCurrentlyOnCotrimoxazoleProphylaxis("Yes");
        }
        Demographics demo = ptsMap.get(id);
        DateTime birthDate = new DateTime(demo.getDateOfBirth());
        DateTime ctxStartDate = null;
        Years yrs = null;
        int ageOfFirstPrescription = 0;
        String unitOfAgeMeasure = "";
        if (ptsFirstCTXDateMap.containsKey(id) && ptsFirstCTXDateMap.get(id) != null) {
            ctxStartDate = new DateTime(ptsFirstCTXDateMap.get(id));
            yrs = Years.yearsBetween(birthDate, ctxStartDate);
            int years = yrs.getYears();
            if (years >= 5) {
                ageOfFirstPrescription = years;
                unitOfAgeMeasure = "YEARS";
            } else {
                ageOfFirstPrescription = 12 * years;
                unitOfAgeMeasure = "MONTHS";
            }
        }

        dc.setAgeOfFirstPrescription(ageOfFirstPrescription);
        dc.setUnitOfAgeMeasure(unitOfAgeMeasure);

        if (ctxStartDate != null) {
            dc.setDateOfFirstPrescription(ctxStartDate.toDate());
        }

        dc.setWebUploadFlag("No");
        dc.setReviewPeriodID(REVIEW_PERIOD_ID);
        dc.setFacilityID(locationMap.getNigeriaQualID(loc));
        return dc;

    }

    public ArrayList<DataCotrimoxazole> createDataCotrimoxazole2(HashMap<Integer, Date> ctxMap, Set<Integer> idSet, HashMap<Integer, Demographics> demoMapList, model.datapump.Location loc) {
        ArrayList<DataCotrimoxazole> ctxList = new ArrayList<DataCotrimoxazole>();
        DataCotrimoxazole dc = null;
        Demographics demo = null;
        String pepfarID = null;

        for (int ele : idSet) {
            dc = new DataCotrimoxazole();
            demo = demoMapList.get(ele);
            if (demo != null) {
                pepfarID = demo.getPepfarID();
                dc.setPatientID(pepfarID);
                //dc.setPatientID(String.valueOf(ele));
            }
            if (ctxMap.keySet().contains(ele)) {
                dc.setPatientReceiveCotrimoxazoleDuringReviewPeriod("Yes");
                dc.setPatientCurrentlyOnCotrimoxazoleProphylaxis("Yes");
                dc.setDateOfLastPrescription(ctxMap.get(ele));
            } else {
                dc.setPatientReceiveCotrimoxazoleDuringReviewPeriod("No");
                dc.setPatientCurrentlyOnCotrimoxazoleProphylaxis("No");
            }
            dc.setWebUploadFlag("No");
            dc.setReviewPeriodID(REVIEW_PERIOD_ID);
            dc.setFacilityID(locationMap.getNigeriaQualID(loc));
            ctxList.add(dc);
        }

        return ctxList;

    }

    public DataBaselineParameters createDataBaselineParameters(int patientID, ArrayList<Obs> obsList, model.datapump.Location loc) {
        DataBaselineParameters dbp = new DataBaselineParameters();
        int personID = 0;
        int conceptID = 0;
        int valueCoded = 0;
        for (Obs obs : obsList) {
            if (obs.getPatientID() == patientID) {
                dbp.setPatientID(obs.getPepfarID());
                //dbp.setPatientID(String.valueOf(obs.getPatientID()));
                dbp.setFacilityID(locationMap.getNigeriaQualID(loc));
                conceptID = obs.getConceptID();
                switch (conceptID) {
                    case 85:
                        dbp.setWeight(obs.getValueNumeric());
                        dbp.setWeightDate(obs.getVisitDate());
                        break;
                    case 88:
                        dbp.setCd4Count(obs.getValueNumeric());
                        dbp.setCd4CountDate(obs.getVisitDate());
                        break;
                    case 860:
                        valueCoded = obs.getValueCoded();
                        switch (valueCoded) {
                            case 861:
                                dbp.setWhoStaging(1);
                                dbp.setWhoStagingDate(obs.getVisitDate());
                                break;
                            case 864:
                                dbp.setWhoStaging(2);
                                dbp.setWhoStagingDate(obs.getVisitDate());
                                break;
                            case 865:
                                dbp.setWhoStaging(3);
                                dbp.setWhoStagingDate(obs.getVisitDate());
                                break;
                            case 866:
                                dbp.setWhoStaging(4);
                                dbp.setWhoStagingDate(obs.getVisitDate());
                                break;
                            case 1121:
                                dbp.setWhoStaging(1);
                                dbp.setWhoStagingDate(obs.getVisitDate());
                                break;
                            case 1122:
                                dbp.setWhoStaging(2);
                                dbp.setWhoStagingDate(obs.getVisitDate());
                                break;
                            case 1123:
                                dbp.setWhoStaging(3);
                                dbp.setWhoStagingDate(obs.getVisitDate());
                                break;
                            case 1124:
                                dbp.setWhoStaging(4);
                                dbp.setWhoStagingDate(obs.getVisitDate());
                                break;
                            default:
                                break;
                        }
                        break;
                    case 7777798:
                        valueCoded = obs.getValueCoded();
                        switch (valueCoded) {
                            case 7777925:
                                dbp.setWhoStaging(1);
                                dbp.setWhoStagingDate(obs.getVisitDate());
                                break;
                            case 7777926:
                                dbp.setWhoStaging(2);
                                dbp.setWhoStagingDate(obs.getVisitDate());
                                break;
                            case 7777927:
                                dbp.setWhoStaging(3);
                                dbp.setWhoStagingDate(obs.getVisitDate());
                                break;
                            case 7777928:
                                dbp.setWhoStaging(4);
                                dbp.setWhoStagingDate(obs.getVisitDate());
                                break;
                            default:
                                break;

                        }
                        break;
                    default:
                        break;

                }
                dbp.setReviewPeriodID(REVIEW_PERIOD_ID);
                dbp.setWebUploadFlag("No");
            }
        }

        return dbp;
    }

    public PediatricBaselineParameters createPediatricBaselineParameters(int patientID, ArrayList<Obs> obsList, model.datapump.Location loc) {
        PediatricBaselineParameters dbp = new PediatricBaselineParameters();
        int personID = 0;
        int conceptID = 0;
        for (Obs obs : obsList) {
            if (obs.getPatientID() == patientID) {
                dbp.setPatientID(obs.getPepfarID());
                //dbp.setPatientID(String.valueOf(obs.getPatientID()));
                dbp.setFacilityID(locationMap.getNigeriaQualID(loc));
                conceptID = obs.getConceptID();
                switch (conceptID) {
                    case 85:
                        dbp.setWeight(obs.getValueNumeric());
                        dbp.setWeightDate(obs.getVisitDate());
                        break;
                    case 88:
                        dbp.setCd4Count(obs.getValueNumeric());
                        dbp.setCd4CountDate(obs.getVisitDate());
                        break;
                    case 860:
                        int valueCoded = obs.getValueCoded();
                        switch (valueCoded) {
                            case 861:
                                dbp.setWhoClinicalStage(1);
                                dbp.setWhoClinicalStageDate(obs.getVisitDate());
                                break;
                            case 864:
                                dbp.setWhoClinicalStage(2);
                                dbp.setWhoClinicalStageDate(obs.getVisitDate());
                                break;
                            case 865:
                                dbp.setWhoClinicalStage(3);
                                dbp.setWhoClinicalStageDate(obs.getVisitDate());
                                break;
                            case 866:
                                dbp.setWhoClinicalStage(4);
                                dbp.setWhoClinicalStageDate(obs.getVisitDate());
                                break;
                            case 1121:
                                dbp.setWhoClinicalStage(1);
                                dbp.setWhoClinicalStageDate(obs.getVisitDate());
                                break;
                            case 1122:
                                dbp.setWhoClinicalStage(2);
                                dbp.setWhoClinicalStageDate(obs.getVisitDate());
                                break;
                            case 1123:
                                dbp.setWhoClinicalStage(3);
                                dbp.setWhoClinicalStageDate(obs.getVisitDate());
                                break;
                            case 1124:
                                dbp.setWhoClinicalStage(4);
                                dbp.setWhoClinicalStageDate(obs.getVisitDate());
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;

                }
                dbp.setReviewPeriodID(REVIEW_PERIOD_ID);
                dbp.setWebUploadFlag("No");
            }
        }

        return dbp;
    }

    public ArrayList<DataARTRecord> createDataARTRecords(ArrayList<Demographics> demoList, HashMap<Integer, Date> artStartDateDictionary, Set<Integer> artList, model.datapump.Location loc, HashMap<Integer, PatientRegimen> firstRegimenDictionary, Set<Integer> idSet) {
        DataARTRecord artRec = null;
        ArrayList<DataARTRecord> artRecList = new ArrayList<DataARTRecord>();
        Date artStartDate = null;
        //Set<Integer> idSet = firstRegimenDictionary.keySet();
        for (Demographics demo : demoList) {
            if ((artList.contains(demo.getPatientID()) && idSet.contains(demo.getPatientID())) && !demo.getPepfarID().isEmpty()) {
                artRec = new DataARTRecord();
                artRec.setPatientID(demo.getPepfarID());
                //artRec.setPatientID(String.valueOf(demo.getPatientID()));
                artRec.setPatientEverStartedOnART("YES");
                artStartDate = artStartDateDictionary.get(demo.getPatientID());
                if (artStartDate == null) {
                    artStartDate = firstRegimenDictionary.get(demo.getPatientID()).getStartDate();
                }
                artRec.setArtStartDate(artStartDate);
                artRec.setTreatmentPrepationCompletedBeforeARTStart("YES");
                artRec.setFacilityID(locationMap.getNigeriaQualID(loc));
                artRec.setWebUploadFlag("No");
                artRec.setReviewPeriodID(REVIEW_PERIOD_ID);
                artRecList.add(artRec);
            }
        }
        return artRecList;
    }

    public ArrayList<PediatricARTAdherence> createPediatricARTAdherenceList(HashMap<Integer, Demographics> demoMap, HashMap<Integer, Obs> adherenceMap, model.datapump.Location loc, Set<Integer> idSet) {
        ArrayList<PediatricARTAdherence> pedAdhList = new ArrayList<PediatricARTAdherence>();
        Set<Integer> ptsSet = demoMap.keySet();
        Set<Integer> ptsAdhSet = adherenceMap.keySet();
        PediatricARTAdherence artAdh;
        for (Integer id : idSet) {
            // if (idSet.contains(id)) {
            artAdh = new PediatricARTAdherence();
            artAdh.setPatientID(demoMap.get(id).getPepfarID());
            //artAdh.setPatientID(String.valueOf(id));
            if (adherenceMap.containsKey(id)) {
                artAdh.setArtAdherenceAssessmentPerformedDuringLast3Months("YES");
                artAdh.setLastDateOfAssessment(adherenceMap.get(id).getVisitDate());
            } else {
                artAdh.setArtAdherenceAssessmentPerformedDuringLast3Months("NO");

            }
            // Obs maxCD4Obs = maxCD4Map.get(id);
            //if (maxCD4Obs != null) {
            //  artAdh.setHighestCD4SinceARTInitiation(maxCD4Obs.getValueNumeric());
            //  artAdh.setDateOfHighestCD4Test(maxCD4Obs.getVisitDate());
            //}
            artAdh.setFacilityID(locationMap.getNigeriaQualID(loc));
            artAdh.setWebUploadFlag("No");
            artAdh.setReviewPeriodID(REVIEW_PERIOD_ID);
            pedAdhList.add(artAdh);
            //}
        }

        return pedAdhList;
    }

    public ArrayList<DataARTAdherence> createDataARTAdherenceList(HashMap<Integer, Demographics> demoMap, HashMap<Integer, Obs> maxCD4Map, HashMap<Integer, Obs> adherenceMap, model.datapump.Location loc, Set<Integer> idSet) {
        ArrayList<DataARTAdherence> artAdhList = new ArrayList<DataARTAdherence>();
        DataARTAdherence artAdh = null;
        Set<Integer> ptsSet = demoMap.keySet();
        Set<Integer> ptsAdhSet = adherenceMap.keySet();
        for (Integer id : ptsSet) {
            if (idSet.contains(id)) {
                artAdh = new DataARTAdherence();
                artAdh.setPatientID(demoMap.get(id).getPepfarID());
                //artAdh.setPatientID(String.valueOf(id));
                if (adherenceMap.containsKey(id)) {
                    artAdh.setArtAdherenceAssessmentPerformedLast3Months("YES");
                    artAdh.setLastDateOfAssessment(adherenceMap.get(id).getVisitDate());
                } else {
                    artAdh.setArtAdherenceAssessmentPerformedLast3Months("NO");

                }
                Obs maxCD4Obs = maxCD4Map.get(id);
                if (maxCD4Obs != null) {
                    artAdh.setHighestCD4SinceARTInitiation(maxCD4Obs.getValueNumeric());
                    artAdh.setDateOfHighestCD4Test(maxCD4Obs.getVisitDate());
                }
                artAdh.setFacilityID(locationMap.getNigeriaQualID(loc));
                artAdh.setWebUploadFlag("No");
                artAdh.setReviewPeriodID(REVIEW_PERIOD_ID);
                artAdhList.add(artAdh);
            }
        }
        return artAdhList;
    }

    public PediatricClinicalEvaluationInReviewPeriod createPediatricClinicalEvalReviewPeriod(ArrayList<Form> formList, int patientID, String pepfarID, model.datapump.Location loc) {
        PediatricClinicalEvaluationInReviewPeriod dc = new PediatricClinicalEvaluationInReviewPeriod();
        dc.setPatientID(pepfarID);
        //dc.setPatientID(String.valueOf(patientID));
        ArrayList<Form> formListForPatient = new ArrayList<Form>();
        for (Form frm : formList) {
            //System.out.println("Form pid: "+frm.getPatientID()+" PatientID: "+patientID);
            if (frm.getPatientID() == patientID) {
                formListForPatient.add(frm);
                //System.out.println("Match found");
            }
        }
        Collections.sort(formListForPatient);
        //System.out.println("Form list size: "+formListForPatient.size());
        Form frm1 = getElementForm(1, formListForPatient);
        if (frm1 != null) {
            dc.setVisit1(frm1.getVisitDate());
        }
        Form frm2 = getElementForm(2, formListForPatient);
        if (frm2 != null) {
            dc.setVisit2(frm2.getVisitDate());
        }
        Form frm3 = getElementForm(3, formListForPatient);
        if (frm3 != null) {
            dc.setVisit3(frm3.getVisitDate());
        }
        Form frm4 = getElementForm(4, formListForPatient);
        if (frm4 != null) {
            dc.setVisit4(frm4.getVisitDate());
        }
        dc.setWebUploadFlag("No");
        dc.setReviewPeriodID(REVIEW_PERIOD_ID);
        dc.setFacilityID(locationMap.getNigeriaQualID(loc));
        return dc;
    }

    public DataClinicalEvaluationInReviewPeriod createClinicalEvalReviewPeriod(ArrayList<Form> formList, int patientID, String pepfarID, model.datapump.Location loc) {
        DataClinicalEvaluationInReviewPeriod dc = new DataClinicalEvaluationInReviewPeriod();
        dc.setPatientID(pepfarID);
        //dc.setPatientID(String.valueOf(patientID));

        ArrayList<Form> formListForPatient = new ArrayList<Form>();
        for (Form frm : formList) {
            //System.out.println("Form pid: "+frm.getPatientID()+" PatientID: "+patientID);
            if (frm.getPatientID() == patientID) {
                formListForPatient.add(frm);
                //System.out.println("Match found");
            }
        }
        Collections.sort(formListForPatient);
        //System.out.println("Form list size: "+formListForPatient.size());
        Form frm1 = getElementForm(1, formListForPatient);
        if (frm1 != null) {
            dc.setVisit1(frm1.getVisitDate());
        }
        Form frm2 = getElementForm(2, formListForPatient);
        if (frm2 != null) {
            dc.setVisit2(frm2.getVisitDate());
        }
        Form frm3 = getElementForm(3, formListForPatient);
        if (frm3 != null) {
            dc.setVisit3(frm3.getVisitDate());
        }
        Form frm4 = getElementForm(4, formListForPatient);
        if (frm4 != null) {
            dc.setVisit4(frm4.getVisitDate());
        }
        dc.setWebUploadFlag("No");
        dc.setReviewPeriodID(REVIEW_PERIOD_ID);
        dc.setFacilityID(locationMap.getNigeriaQualID(loc));
        return dc;
    }

    public PatientRegimen getElement(int pos, ArrayList<PatientRegimen> s) {
        Iterator<PatientRegimen> it = s.iterator();
        int count = 0;
        PatientRegimen ele = null;
        while (it.hasNext() && count < pos && pos <= s.size()) {
            ele = it.next();
            count++;
        }
        return ele;
    }

    public Obs getElementObs(int pos, ArrayList<Obs> s) {
        Iterator<Obs> it = s.iterator();
        int count = 0;
        Obs ele = null;
        while (it.hasNext() && count < pos && pos <= s.size()) {
            ele = it.next();
            count++;
        }
        return ele;
    }

    public Form getElementForm(int pos, ArrayList<Form> s) {
        Iterator<Form> it = s.iterator();
        int count = 0;
        Form ele = null;
        while (it.hasNext() && count < pos && pos <= s.size()) {
            ele = it.next();
            count++;
        }
        return ele;
    }

    public ArrayList<PatientRegimen> getPatientRegimenForID(ArrayList<PatientRegimen> ptsRegimenList, int id) {
        ArrayList<PatientRegimen> regimenList = new ArrayList<PatientRegimen>();
        for (PatientRegimen ele : ptsRegimenList) {
            if (ele.getPatientID() == id) {
                regimenList.add(ele);
            }
        }
        return regimenList;
    }

    public ArrayList<DataRegimenDuringReview> createDataARTRegimen(HashMap<Integer, Demographics> ptsMap, ArrayList<PatientRegimen> ptsRegimenList, ArrayList<Integer> ptsOnARTFirstDay, ArrayList<Integer> ptsOnARTAnyTime, model.datapump.Location loc, Set<Integer> idSet) {
        ArrayList<DataRegimenDuringReview> dataRegimenReviewPeriodList = new ArrayList<DataRegimenDuringReview>();
        DataRegimenDuringReview dataRegimen = new DataRegimenDuringReview();
        ArrayList<PatientRegimen> ptsRegimen = new ArrayList<PatientRegimen>();
        Set<Integer> keySet = ptsMap.keySet();
        for (int id : keySet) {
            if (idSet.contains(id)) {
                dataRegimen = new DataRegimenDuringReview();
                dataRegimen.setPatientID(ptsMap.get(id).getPepfarID());
                //dataRegimen.setPatientID(String.valueOf(id));
                Set<PatientRegimen> set = new HashSet<PatientRegimen>();
                if (ptsOnARTFirstDay.contains(ptsMap.get(id).getPatientID())) {
                    dataRegimen.setPatientOnARTFirstDayOfReviewPeriod("YES");
                    dataRegimen.setPatientOnARTAnytimeDuringReviewPeriod("YES");
                } else {
                    dataRegimen.setPatientOnARTFirstDayOfReviewPeriod("NO");
                    if (ptsOnARTAnyTime.contains(ptsMap.get(id).getPatientID())) {
                        dataRegimen.setPatientOnARTAnytimeDuringReviewPeriod("YES");
                    } else {
                        dataRegimen.setPatientOnARTAnytimeDuringReviewPeriod("NO");
                    }
                }

                ptsRegimen = getPatientRegimenForID(ptsRegimenList, id);
                Collections.sort(ptsRegimen);
                set.addAll(ptsRegimen);

                PatientRegimen regimen = getElement(1, ptsRegimen);
                if (regimen != null) {
                    dataRegimen.setFirstRegimen(regimen.getCode());
                    dataRegimen.setFirstRegimenStartDate(regimen.getStartDate());
                    dataRegimen.setFirstRegimenChangeDate(regimen.getStopDate());
                } else {
                    dataRegimen.setFirstRegimen("0");
                }
                regimen = getElement(2, ptsRegimen);
                if (regimen != null) {
                    dataRegimen.setSecondRegimen(regimen.getCode());
                    dataRegimen.setSecondRegimenStartDate(regimen.getStartDate());
                    dataRegimen.setSecondRegimenChangeDate(regimen.getStopDate());
                } else {
                    dataRegimen.setSecondRegimen("0");
                }
                regimen = getElement(3, ptsRegimen);
                if (regimen != null) {
                    dataRegimen.setThirdRegimen(regimen.getCode());
                    dataRegimen.setThirdRegimenStartDate(regimen.getStartDate());
                    dataRegimen.setThirdRegimenChangeDate(regimen.getStopDate());
                } else {
                    dataRegimen.setThirdRegimen("0");
                }
                regimen = getElement(set.size(), ptsRegimen);
                if (regimen != null) {
                    Date startDate = regimen.getStartDate();
                    Date stopDate = regimen.getStopDate();
                    LocalDate d2 = new LocalDate(new DateTime(stopDate));
                    LocalDate d1 = new LocalDate(new DateTime(startDate));
                    int durationOfMedicationCoverageInMonths = 0;
                    if (d2.isAfter(d1) || d2.isEqual(d1)) {
                        durationOfMedicationCoverageInMonths = Months.monthsBetween(d1, d2).getMonths();
                    }
                    dataRegimen.setDateOfLastARTPickup(regimen.getStartDate());
                    if (durationOfMedicationCoverageInMonths < 6) {
                        dataRegimen.setDurationOfMedicationCoverageInMonths(durationOfMedicationCoverageInMonths);
                    } else {
                        dataRegimen.setDurationOfMedicationCoverageInMonths(0);
                    }

                }
                dataRegimen.setFacilityID(locationMap.getNigeriaQualID(loc));
                dataRegimen.setWebUploadFlag("No");
                dataRegimen.setReviewPeriodID(REVIEW_PERIOD_ID);
                dataRegimenReviewPeriodList.add(dataRegimen);
            }
        }
        return dataRegimenReviewPeriodList;
    }

    public ArrayList<DataPatientStatusReviewPeriod> createDataPatientStatusReviewPeriod(HashMap<Integer, Demographics> ptsMap, Set<Integer> idSet, ArrayList<Obs> obsList, model.datapump.Location loc) {
        ArrayList<DataPatientStatusReviewPeriod> pstatusList = new ArrayList<DataPatientStatusReviewPeriod>();
        DataPatientStatusReviewPeriod pstatus;
        Obs obs;
        int valueCoded;
        for (int pts : idSet) {
            pstatus = new DataPatientStatusReviewPeriod();
            pstatus.setId(String.valueOf(pts));
            pstatus.setPatientID(ptsMap.get(pts).getPepfarID());
            //pstatus.setPatientID(String.valueOf(pts));
            obs = getObsForPatientForConcept1(pts, 977, obsList);
            if (obs != null) {
                valueCoded = obs.getValueCoded();
                switch (valueCoded) {
                    case 211:
                        pstatus.setStatus("TRANSFERRED OUT");
                        pstatus.setDateOfStatusChange(obs.getVisitDate());
                        pstatus.setTransferredOut("YES");
                        pstatus.setTransferredOutDate(obs.getVisitDate());
                        break;
                    case 975:
                        pstatus.setStatus("DEATH");
                        pstatus.setDateOfStatusChange(obs.getVisitDate());
                        pstatus.setDeath("YES");
                        pstatus.setDeathDate(obs.getVisitDate());
                        break;
                    case 976:
                        pstatus.setStatus("DISCONTINUED CARE");
                        pstatus.setDateOfStatusChange(obs.getVisitDate());
                        pstatus.setDiscontinuedCare("YES");
                        pstatus.setDiscontinuedCareDate(obs.getVisitDate());
                        break;
                    default:
                        pstatus.setTransferredOut("NO");
                        pstatus.setDeath("NO");
                        pstatus.setDiscontinuedCare("NO");
                        break;
                }
            } else {
                pstatus.setTransferredOut("NO");
                pstatus.setDeath("NO");
                pstatus.setDiscontinuedCare("NO");
            }
            obs = getObsForPatientForConcept1(pts, 976, obsList);
            if (obs != null) {
                pstatus.setDiscontinuedCareReason(obs.getVariableValue());
            }
            pstatus.setFacilityID(locationMap.getNigeriaQualID(loc));
            pstatus.setWebUploadFlag("NO");
            pstatus.setReviewPeriodID(REVIEW_PERIOD_ID);
            pstatusList.add(pstatus);

        }
        return pstatusList;
    }

    public ArrayList<PediatricLinkage> createPediatricLinkage(HashMap<Integer, Demographics> ptsMap, Set<Integer> idSet, ArrayList<Obs> obsList, model.datapump.Location loc) {
        ArrayList<PediatricLinkage> plList = new ArrayList<PediatricLinkage>();
        PediatricLinkage pl;
        Obs obs;
        int valueCoded;
        for (Integer ele : idSet) {
            pl = new PediatricLinkage();
            pl.setPatientID(ptsMap.get(ele).getPepfarID());
            // pl.setPatientID(String.valueOf(ele));
            obs = getObsForPatientForConcept1(ele, 1269, obsList);
            if (obs != null) {
                valueCoded = obs.getValueCoded();
                if (valueCoded == 258) {
                    pl.setPatientReceivedNutritionalAssessmentInReviewPeriod("YES");
                    pl.setDidPatientReceivedNutritionSupport("YES");
                } else {
                    pl.setPatientReceivedNutritionalAssessmentInReviewPeriod("NO");
                    pl.setDidPatientReceivedNutritionSupport("NO");
                }
            }
            obs = getObsForPatientForConcept1(ele, 260, obsList);
            if (obs != null) {
                valueCoded = obs.getValueCoded();
                if (valueCoded == 258) {
                    pl.setPatientQualifyForNutritionSupporot("YES");
                } else {
                    pl.setPatientQualifyForNutritionSupporot("NO");
                }
            }
            obs = getObsForPatientForConcept1(ele, 1103, obsList);
            if (obs != null) {
                valueCoded = obs.getValueCoded();
                if (valueCoded == 80) {
                    pl.setChildImmunizationStatus("UP to date");
                } else {
                    pl.setChildImmunizationStatus("Incomplete");
                }
            }
            pl.setFacilityID(locationMap.getNigeriaQualID(loc));
            pl.setWebUploadFlag("NO");
            pl.setReviewPeriodID(REVIEW_PERIOD_ID);
            obs = getObsForPatientForConcept1(ele, 1356, obsList);
            if (obs != null) {
                valueCoded = obs.getValueCoded();
                if (valueCoded == 1355) {
                    pl.setWaterGuardReceived("YES");
                } else {
                    pl.setWaterGuardReceived("NO");
                }
            }
            obs = getObsForPatientForConcept1(ele, 1269, obsList);
            if (obs != null) {
                pl.setServiceReceivedByPatient(obs.getVariableValue());
            }
            obs = getObsForPatientForConcept1(ele, 1368, obsList);
            if (obs != null) {
                valueCoded = obs.getValueCoded();
                if (valueCoded == 80) {
                    pl.setInsecticideTreatedNetsReceived("YES");
                } else {
                    pl.setInsecticideTreatedNetsReceived("NO");
                }
            }
            plList.add(pl);
        }
        return plList;
    }

    public ArrayList<PediatricEducation> createPediatricEducation(HashMap<Integer, Demographics> ptsMap, Set<Integer> idSet, ArrayList<Obs> obsList, model.datapump.Location loc) {
        ArrayList<PediatricEducation> pedEduList = new ArrayList<PediatricEducation>();
        PediatricEducation pedEdu;
        Obs obs;
        int valueCoded;
        for (Integer ele : idSet) {
            pedEdu = new PediatricEducation();
            pedEdu.setPatientID(ptsMap.get(ele).getPepfarID());
            //pedEdu.setPatientID(String.valueOf(ele));
            obs = getObsForPatientForConcept1(ele, 1690, obsList);
            if (obs != null) {
                valueCoded = obs.getValueCoded();
                if (valueCoded == 1099) {
                    pedEdu.setMotherReceivedInfantFeedingEducation("YES");
                } else {
                    pedEdu.setMotherReceivedInfantFeedingEducation("NO");
                }
            } else {
                pedEdu.setMotherReceivedInfantFeedingEducation("NOT INDICATED");
            }
            pedEdu.setReviewPeriodID(REVIEW_PERIOD_ID);
            pedEdu.setWebUploadFlag("No");
            pedEdu.setFacilityID(locationMap.getNigeriaQualID(loc));
            pedEduList.add(pedEdu);
        }
        return pedEduList;
    }

    public ArrayList<PediatricPatientType> createPediatricPatientType(HashMap<Integer, Demographics> ptsMap, Set<Integer> idSet, model.datapump.Location loc) {
        ArrayList<PediatricPatientType> pptList = new ArrayList<PediatricPatientType>();
        Demographics demo;
        String patientType;
        PediatricPatientType ppt;
        int age;
        for (Integer ele : idSet) {
            ppt = new PediatricPatientType();
            demo = ptsMap.get(ele);
            ppt.setPatientID(demo.getPepfarID());
            age = demo.getAge();
            if (age <= 2) {
                patientType = "HIV infected infant age 0 to 24 months";
            } else {
                patientType = "HIV infected child age >24 months";
            }
            ppt.setFacilityID(locationMap.getNigeriaQualID(loc));
            ppt.setWebUploadFlag("NO");
            ppt.setReviewPeriodID(REVIEW_PERIOD_ID);

        }
        return pptList;
    }

    public ArrayList<PediatricPatientStatus> createPediatricPatientStatusReviewPeriod(HashMap<Integer, Demographics> ptsMap, Set<Integer> idSet, ArrayList<Obs> obsList, model.datapump.Location loc) {
        ArrayList<PediatricPatientStatus> pstatusList = new ArrayList<PediatricPatientStatus>();
        PediatricPatientStatus pstatus;
        Obs obs;
        int valueCoded;
        for (int pts : idSet) {
            pstatus = new PediatricPatientStatus();
            pstatus.setPatientID(ptsMap.get(pts).getPepfarID());
            //pstatus.setPatientID(String.valueOf(pts));
            obs = getObsForPatientForConcept1(pts, 977, obsList);
            if (obs != null) {
                valueCoded = obs.getValueCoded();
                switch (valueCoded) {
                    case 211:
                        pstatus.setStatus("TRANSFERRED OUT");
                        pstatus.setDateOfStatusChange(obs.getVisitDate());
                        pstatus.setTransferredOut("YES");
                        pstatus.setTransferredOutDate(obs.getVisitDate());
                        break;
                    case 975:
                        pstatus.setStatus("DEATH");
                        pstatus.setDateOfStatusChange(obs.getVisitDate());
                        pstatus.setDeath("YES");
                        pstatus.setDeathDate(obs.getVisitDate());
                        break;
                    case 976:
                        pstatus.setStatus("DISCONTINUED CARE");
                        pstatus.setDateOfStatusChange(obs.getVisitDate());
                        pstatus.setDiscontinued("YES");
                        pstatus.setDiscontinuedCareDate(obs.getVisitDate());
                        break;
                    default:
                        pstatus.setTransferredOut("NO");
                        pstatus.setDeath("NO");
                        pstatus.setDiscontinued("NO");
                        break;
                }
            } else {
                pstatus.setTransferredOut("NO");
                pstatus.setDeath("NO");
                pstatus.setDiscontinued("NO");
            }
            obs = getObsForPatientForConcept1(pts, 976, obsList);
            if (obs != null) {
                pstatus.setDiscontinuedCareReason(obs.getVariableValue());
            }
            pstatus.setFacilityID(locationMap.getNigeriaQualID(loc));
            pstatus.setWebUploadFlag("NO");
            pstatus.setReviewPeriodID(REVIEW_PERIOD_ID);
            pstatusList.add(pstatus);

        }
        return pstatusList;
    }

    public ArrayList<PediatricARTRegimenSinceStartingTreatment> createPedARTRegimen(HashMap<Integer, Demographics> ptsMap, ArrayList<PatientRegimen> ptsRegimenList, ArrayList<Integer> ptsOnARTFirstDay, ArrayList<Integer> ptsOnARTAnyTime, model.datapump.Location loc, Set<Integer> idSet) {
        ArrayList<PediatricARTRegimenSinceStartingTreatment> dataRegimenReviewPeriodList = new ArrayList<PediatricARTRegimenSinceStartingTreatment>();
        PediatricARTRegimenSinceStartingTreatment dataRegimen = new PediatricARTRegimenSinceStartingTreatment();
        ArrayList<PatientRegimen> ptsRegimen = new ArrayList<PatientRegimen>();
        Set<Integer> keySet = ptsMap.keySet();
        for (int id : keySet) {
            if (idSet.contains(id)) {
                dataRegimen = new PediatricARTRegimenSinceStartingTreatment();
                dataRegimen.setPatientID(ptsMap.get(id).getPepfarID());
                //dataRegimen.setPatientID(String.valueOf(id));
                Set<PatientRegimen> set = new HashSet<PatientRegimen>();
                /*if (ptsOnARTFirstDay.contains(ptsMap.get(id).getPatientID())) {
                    dataRegimen.setPatientOnARTFirstDayOfReviewPeriod("YES");
                } else {
                    dataRegimen.setPatientOnARTFirstDayOfReviewPeriod("NO");
                }*/
                if (ptsOnARTAnyTime.contains(ptsMap.get(id).getPatientID())) {
                    dataRegimen.setPatientOnARTAnytimeDuringReviewPeriod("Yes");
                } else {
                    dataRegimen.setPatientOnARTAnytimeDuringReviewPeriod("No");
                }
                ptsRegimen = getPatientRegimenForID(ptsRegimenList, id);
                Collections.sort(ptsRegimen);
                set.addAll(ptsRegimen);

                PatientRegimen regimen = getElement(1, ptsRegimen);
                if (regimen != null) {
                    dataRegimen.setC1stRegminen(regimen.getCode());
                    dataRegimen.setC1stRegimenStartDate(regimen.getStartDate());
                    dataRegimen.setC1stRegimenChangeDate(regimen.getStopDate());
                } else {
                    dataRegimen.setC1stRegminen("0");
                }
                regimen = getElement(2, ptsRegimen);
                if (regimen != null) {
                    dataRegimen.setC2ndRegimen(regimen.getCode());
                    dataRegimen.setC2ndRegimenStartDate(regimen.getStartDate());
                    dataRegimen.setC2ndRegimenChangeDate(regimen.getStopDate());
                } else {
                    dataRegimen.setC2ndRegimen("0");
                }
                regimen = getElement(3, ptsRegimen);
                if (regimen != null) {
                    dataRegimen.setC3rdRegimen(regimen.getCode());
                    dataRegimen.setC3rdRegimenStartDate(regimen.getStartDate());
                    dataRegimen.setC3rdRegimenChangeDate(regimen.getStopDate());
                } else {
                    dataRegimen.setC3rdRegimen("0");
                }
                regimen = getElement(set.size(), ptsRegimen);
                if (regimen != null) {
                    Date startDate = regimen.getStartDate();
                    Date stopDate = regimen.getStopDate();
                    LocalDate d2 = new LocalDate(new DateTime(stopDate));
                    LocalDate d1 = new LocalDate(new DateTime(startDate));
                    // int durationOfMedicationCoverageInMonths = 0;
                    //if (d2.isAfter(d1) || d2.isEqual(d1)) {
                    //     durationOfMedicationCoverageInMonths = Months.monthsBetween(d1, d2).getMonths();
                    //   }
                    //dataRegimen.setDateOfLastARTPickup(regimen.getStartDate());
                    //dataRegimen.setDurationOfMedicationCoverageInMonths(durationOfMedicationCoverageInMonths);

                }
                dataRegimen.setOtherRegimenSpecify("0");
                dataRegimen.setFacilityID(locationMap.getNigeriaQualID(loc));
                dataRegimen.setWebUploadFlag("No");
                dataRegimen.setReviewPeriodID(REVIEW_PERIOD_ID);
                dataRegimenReviewPeriodList.add(dataRegimen);
            }
        }
        return dataRegimenReviewPeriodList;
    }

    public DataViralLoadTestingReviewPeriod createDataViralLoadTesting(HashMap<Integer, Obs> vlMap, int patientID, String pepfarID, model.datapump.Location loc) {
        DataViralLoadTestingReviewPeriod dv = new DataViralLoadTestingReviewPeriod();
        dv.setPatientID(pepfarID);
        //dv.setPatientID(String.valueOf(patientID));
        Obs obs = null;
        if (vlMap.keySet().contains(patientID)) {
            dv.setHasPatientReceivedVLTesting("Yes");
            obs = vlMap.get(patientID);
            dv.setVlTestingDate(obs.getVisitDate());
            dv.setVlResult(obs.getValueNumeric());
        } else {
            dv.setHasPatientReceivedVLTesting("No");
        }
        dv.setFacilityID(String.valueOf(locationMap.getNigeriaQualID(loc)));
        dv.setWebUploadFlag("No");
        dv.setReviewPeriodID(REVIEW_PERIOD_ID);
        return dv;
    }

    public ArrayList<DataPatientMonitoringReviewPeriod> createDataPatientMonitoringReviewPeriod(Set<Integer> idset, HashMap<Integer, Demographics> ptsMap, ArrayList<Obs> obsList, model.datapump.Location loc) {
        DataPatientMonitoringReviewPeriod pm;
        
        ArrayList<DataPatientMonitoringReviewPeriod> pmList = new ArrayList<DataPatientMonitoringReviewPeriod>();
        ArrayList<Obs> obsForConceptList, obsForConceptList2;
        Obs obs = null;
        for (Integer ele : idset) {
            pm = new DataPatientMonitoringReviewPeriod();
            pm.setPatientID(ptsMap.get(ele).getPepfarID());
            //pm.setPatientID(String.valueOf(ele));
            obsForConceptList = getObsForPatientForConcept(ele, 88, obsList);
            Collections.sort(obsForConceptList);
            obs = getElementObs(1, obsForConceptList);
            if (obs != null) {
                pm.setCd4Value1(obs.getValueNumeric());
                pm.setCd4Value1Date(obs.getVisitDate());
            }
            obs = getElementObs(2, obsForConceptList);
            if (obs != null) {
                pm.setCd4Value2(obs.getValueNumeric());
                pm.setCd4Value2Date(obs.getVisitDate());
            }
            obs = getElementObs(3, obsForConceptList);
            if (obs != null) {
                pm.setCd4Value3(obs.getValueNumeric());
                pm.setCd4Value3Date(obs.getVisitDate());
            }
            obs = getElementObs(4, obsForConceptList);
            if (obs != null) {
                pm.setCd4Value4(obs.getValueNumeric());
                pm.setCd4Value4Date(obs.getVisitDate());
            }
            obsForConceptList = getObsForPatientForConcept(ele, 313, obsList);
            Collections.sort(obsForConceptList);
            obs = getElementObs(1, obsForConceptList);
            if (obs != null) {
                pm.setCreatinine1(obs.getValueNumeric());
                pm.setCreatinine1Date(obs.getVisitDate());
            }
            obs = getElementObs(2, obsForConceptList);
            if (obs != null) {
                pm.setCreatinine2(obs.getValueNumeric());
                pm.setCreatinine2Date(obs.getVisitDate());
            }
            obs = getElementObs(3, obsForConceptList);
            if (obs != null) {
                pm.setCreatinine3(obs.getValueNumeric());
                pm.setCreatinine3Date(obs.getVisitDate());
            }
            obs = getElementObs(4, obsForConceptList);
            if (obs != null) {
                pm.setCreatinine4(obs.getValueNumeric());
                pm.setCreatinine4Date(obs.getVisitDate());
            }
            obsForConceptList = getObsForPatientForConcept(ele, 85, obsList);
            Collections.sort(obsForConceptList);
            obs = getElementObs(1, obsForConceptList);
            if (obs != null) {
                pm.setWeight1(obs.getValueNumeric());
                pm.setWeight1Date(obs.getVisitDate());
            }
            obs = getElementObs(2, obsForConceptList);
            if (obs != null) {
                pm.setWeight2(obs.getValueNumeric());
                pm.setWeight2Date(obs.getVisitDate());
            }
            obs = getElementObs(3, obsForConceptList);
            if (obs != null) {
                pm.setWeight3(obs.getValueNumeric());
                pm.setWeight3Date(obs.getVisitDate());
            }
            obs = getElementObs(4, obsForConceptList);
            if (obs != null) {
                pm.setWeight4(obs.getValueNumeric());
                pm.setWeight4Date(obs.getVisitDate());
            }
            obsForConceptList = getObsForPatientForConcept(ele, 7777798, obsList);
            obsForConceptList2 = getObsForPatientForConcept(ele, 860, obsList);
            obsForConceptList.addAll(obsForConceptList2);
            Collections.sort(obsForConceptList);
            obs = getElementObs(1, obsForConceptList);
            if (obs != null) {
                pm.setWho1(mapWHO(obs.getValueCoded()));
                pm.setWho1Date(obs.getVisitDate());
            }
            obs = getElementObs(2, obsForConceptList);
            if (obs != null) {
                pm.setWho2(mapWHO(obs.getValueCoded()));
                pm.setWho2Date(obs.getVisitDate());
            }
            obs = getElementObs(3, obsForConceptList);
            if (obs != null) {
                pm.setWho3(mapWHO(obs.getValueCoded()));
                pm.setWho3Date(obs.getVisitDate());
            }
            obs = getElementObs(4, obsForConceptList);
            if (obs != null) {
                pm.setWho4(mapWHO(obs.getValueCoded()));
                pm.setWho4Date(obs.getVisitDate());
            }

            obsForConceptList = getObsForPatientForConcept(ele, 329, obsList);
            Collections.sort(obsForConceptList);
            obs = getElementObs(1, obsForConceptList);
            if (obs != null) {
                pm.setPctHct1(obs.getValueNumeric());
                pm.setPctHct1Date(obs.getVisitDate());
            }
            obs = getElementObs(2, obsForConceptList);
            if (obs != null) {
                pm.setPctHct2(obs.getValueNumeric());
                pm.setPctHct2Date(obs.getVisitDate());
            }
            obs = getElementObs(3, obsForConceptList);
            if (obs != null) {
                pm.setPctHct3(obs.getValueNumeric());
                pm.setPctHct3Date(obs.getVisitDate());
            }
            obs = getElementObs(4, obsForConceptList);
            if (obs != null) {
                pm.setPctHct4(obs.getValueNumeric());
                pm.setPctHct4Date(obs.getVisitDate());
            }
            obsForConceptList = getObsForPatientForConcept(ele, 309, obsList);
            Collections.sort(obsForConceptList);
            obs = getElementObs(1, obsForConceptList);
            if (obs != null) {
                pm.setAlt1(obs.getValueNumeric());
                pm.setAlt1Date(obs.getVisitDate());
            }
            obs = getElementObs(2, obsForConceptList);
            if (obs != null) {
                pm.setAlt2(obs.getValueNumeric());
                pm.setAlt2Date(obs.getVisitDate());
            }
            obs = getElementObs(3, obsForConceptList);
            if (obs != null) {
                pm.setAlt3(obs.getValueNumeric());
                pm.setAlt3Date(obs.getVisitDate());
            }
            obs = getElementObs(4, obsForConceptList);
            if (obs != null) {
                pm.setAlt4(obs.getValueNumeric());
                pm.setAlt4Date(obs.getVisitDate());
            }
            pm.setFacilityID(locationMap.getNigeriaQualID(loc));
            pm.setWebUploadFlag("NO");
            pm.setReviewPeriodID(REVIEW_PERIOD_ID);
            pmList.add(pm);
        }

        return pmList;
    }

    public ArrayList<PediatricPatientMonitoringDuringReviewPeriod> createPediatricPatientMonitoringReviewPeriod(Set<Integer> idset, HashMap<Integer, Demographics> ptsMap, ArrayList<Obs> obsList, model.datapump.Location loc, HashMap<Integer, Obs> viralLoadMap) {
        PediatricPatientMonitoringDuringReviewPeriod pm;
        ArrayList<PediatricPatientMonitoringDuringReviewPeriod> pmList = new ArrayList<PediatricPatientMonitoringDuringReviewPeriod>();
        ArrayList<Obs> obsForConceptList;
        Set<Integer> ptsSet = viralLoadMap.keySet();
        Obs obs = null;
        for (Integer ele : idset) {
            pm = new PediatricPatientMonitoringDuringReviewPeriod();
            pm.setPatientID(ptsMap.get(ele).getPepfarID());
            //pm.setPatientID(String.valueOf(ele));
            pm.setFacilityID(locationMap.getNigeriaQualID(loc));
            pm.setReviewPeriodID(5);
            if (ptsSet.contains(ele)) {
                pm.setHasThePatientReceivedViralLoadTesting("Yes");
                obs = viralLoadMap.get(ele);
                pm.setViralLoadTestDate(obs.getVisitDate());
                pm.setViralLoadTestResult((int) obs.getValueNumeric());
            } else {
                pm.setHasThePatientReceivedViralLoadTesting("No");
            }
            obsForConceptList = getObsForPatientForConcept(ele, 88, obsList);
            Collections.sort(obsForConceptList);
            obs = getElementObs(1, obsForConceptList);
            if (obs != null) {
                pm.setCd4Value1(obs.getValueNumeric());
                pm.setCd4Value1Date(obs.getVisitDate());
            }
            obs = getElementObs(2, obsForConceptList);
            if (obs != null) {
                pm.setCd4Value2(obs.getValueNumeric());
                pm.setCd4Value2Date(obs.getVisitDate());
            }
            obs = getElementObs(3, obsForConceptList);
            if (obs != null) {
                pm.setCd4Value3(obs.getValueNumeric());
                pm.setCd4Value3Date(obs.getVisitDate());
            }
            obs = getElementObs(4, obsForConceptList);
            if (obs != null) {
                pm.setCd4Value4(obs.getValueNumeric());
                pm.setCd4Value4Date(obs.getVisitDate());
            }
            obsForConceptList = getObsForPatientForConcept(ele, 1153, obsList);
            Collections.sort(obsForConceptList);
            obs = getElementObs(1, obsForConceptList);
            if (obs != null) {
                pm.setCd4Prc1(obs.getValueNumeric());
                pm.setCd4Prc1Date(obs.getVisitDate());
            }
            obs = getElementObs(2, obsForConceptList);
            if (obs != null) {
                pm.setCd4Prc2(obs.getValueNumeric());
                pm.setCd4Prc2Date(obs.getVisitDate());
            }
            obs = getElementObs(3, obsForConceptList);
            if (obs != null) {
                pm.setCd4Prc3(obs.getValueNumeric());
                pm.setCd4Prc3Date(obs.getVisitDate());
            }
            obs = getElementObs(4, obsForConceptList);
            if (obs != null) {
                pm.setCd4Prc4(obs.getValueNumeric());
                pm.setCd4Prc4Date(obs.getVisitDate());
            }
            obsForConceptList = getObsForPatientForConcept(ele, 85, obsList);
            Collections.sort(obsForConceptList);
            obs = getElementObs(1, obsForConceptList);
            if (obs != null) {
                pm.setWeight1(obs.getValueNumeric());
                pm.setWeight1Date(obs.getVisitDate());
            }
            obs = getElementObs(2, obsForConceptList);
            if (obs != null) {
                pm.setWeight2(obs.getValueNumeric());
                pm.setWeight2Date(obs.getVisitDate());
            }
            obs = getElementObs(3, obsForConceptList);
            if (obs != null) {
                pm.setWeight3(obs.getValueNumeric());
                pm.setWeight3Date(obs.getVisitDate());
            }
            obs = getElementObs(4, obsForConceptList);
            if (obs != null) {
                pm.setWeight4(obs.getValueNumeric());
                pm.setWeight4Date(obs.getVisitDate());
            }
            obsForConceptList = getObsForPatientForConcept(ele, 860, obsList);
            Collections.sort(obsForConceptList);
            obs = getElementObs(1, obsForConceptList);
            if (obs != null) {
                pm.setWho1(mapWHO(obs.getValueCoded()));
                pm.setWho1Date(obs.getVisitDate());
            }
            obs = getElementObs(2, obsForConceptList);
            if (obs != null) {
                pm.setWho2(mapWHO(obs.getValueCoded()));
                pm.setWho2Date(obs.getVisitDate());
            }
            obs = getElementObs(3, obsForConceptList);
            if (obs != null) {
                pm.setWho3(mapWHO(obs.getValueCoded()));
                pm.setWho3Date(obs.getVisitDate());
            }
            obs = getElementObs(4, obsForConceptList);
            if (obs != null) {
                pm.setWho4(mapWHO(obs.getValueCoded()));
                pm.setWho4Date(obs.getVisitDate());
            }
            obsForConceptList = getObsForPatientForConcept(ele, 329, obsList);
            Collections.sort(obsForConceptList);
            obs = getElementObs(1, obsForConceptList);
            if (obs != null) {
                pm.setPctHct1(obs.getValueNumeric());
                pm.setPctHct1Date(obs.getVisitDate());
            }
            obs = getElementObs(2, obsForConceptList);
            if (obs != null) {
                pm.setPctHct2(obs.getValueNumeric());
                pm.setPctHct2Date(obs.getVisitDate());
            }
            obs = getElementObs(3, obsForConceptList);
            if (obs != null) {
                pm.setPctHct3(obs.getValueNumeric());
                pm.setPctHct3Date(obs.getVisitDate());
            }
            obs = getElementObs(4, obsForConceptList);
            if (obs != null) {
                pm.setPctHct4(obs.getValueNumeric());
                pm.setPctHct4Date(obs.getVisitDate());
            }
            obsForConceptList = getObsForPatientForConcept(ele, 309, obsList);
            Collections.sort(obsForConceptList);
            obs = getElementObs(1, obsForConceptList);
            if (obs != null) {
                pm.setAlt1(obs.getValueNumeric());
                pm.setAlt1Date(obs.getVisitDate());
            }
            obs = getElementObs(2, obsForConceptList);
            if (obs != null) {
                pm.setAlt2(obs.getValueNumeric());
                pm.setAlt2Date(obs.getVisitDate());
            }
            obs = getElementObs(3, obsForConceptList);
            if (obs != null) {
                pm.setAlt3(obs.getValueNumeric());
                pm.setAlt3Date(obs.getVisitDate());
            }
            obs = getElementObs(4, obsForConceptList);
            if (obs != null) {
                pm.setAlt4(obs.getValueNumeric());
                pm.setAlt4Date(obs.getVisitDate());
            }
            pmList.add(pm);
        }

        return pmList;
    }

    public int mapWHO(int valueCoded) {
        int who = 0;
        switch (valueCoded) {
            case 861:
                who = 1;
                break;
            case 1121:
                who = 1;
                break;
            case 864:
                who = 2;
                break;
            case 1122:
                who = 2;
                break;
            case 865:
                who = 3;
                break;
            case 1123:
                who = 3;
                break;
            case 866:
                who = 4;
                break;
            case 1124:
                who = 4;
                break;
            case 7777925:
                who = 1;
                break;
            case 7777926:
                who = 2;
                break;
            case 7777927:
                who = 3;
                break;
            case 7777928:
                who = 4;
                break;

            default:
                break;
        }
        return who;
    }

    public ArrayList<Obs> getObsForPatientForConcept(int patientID, int conceptID, ArrayList<Obs> obsListAll) {
        ArrayList<Obs> obsListForConcept = new ArrayList<Obs>();

        for (Obs ele : obsListAll) {
            if (ele.getPatientID() == patientID) {
                if (ele.getConceptID() == conceptID) {
                    obsListForConcept.add(ele);
                }
            }
        }

        return obsListForConcept;
    }

    public Obs getObsForPatientForConcept1(int patientID, int conceptID, ArrayList<Obs> obsListAll) {
        Obs obs = null;

        for (Obs ele : obsListAll) {
            if (ele.getPatientID() == patientID) {
                if (ele.getConceptID() == conceptID) {
                    obs = ele;
                }
            }
        }

        return obs;
    }

}
