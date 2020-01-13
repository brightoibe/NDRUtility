/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import static com.inductivehealth.ndr.client.Client.getXmlDate;
import com.inductivehealth.ndr.schema.CodedSimpleType;
import com.inductivehealth.ndr.schema.HIVEncounterType;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.datatype.DatatypeConfigurationException;
import model.datapump.DrugOrder;
import model.datapump.Drugs;
import model.datapump.Obs;
import model.datapump.PatientRegimen;
import model.datapump.Visit;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import util.NDRCommonUtills;

/**
 *
 * @author The Bright
 */
public class NDRClinicalDictionary {

    private Map<Integer, String> hivEncounterTypeDictionary = new HashMap<Integer, String>();
    private NDRPharmacyDictionary pharmacyDictionary;

    public NDRClinicalDictionary() {
        pharmacyDictionary = new NDRPharmacyDictionary();
        loadDictionary();
    }

    private void loadDictionary() {
        loadHIVEncounterDictionary();
    }

    public void loadHIVEncounterDictionary() {
        hivEncounterTypeDictionary = new HashMap<Integer, String>();

        /* EDD AND PMTCT LINK    */
        hivEncounterTypeDictionary.put(47, "P");//Pregnant
        hivEncounterTypeDictionary.put(1259, "PMTCT");//ON PMTCT
        hivEncounterTypeDictionary.put(7777870, "NP");// Not Pregnant
        hivEncounterTypeDictionary.put(13, "NK");// Unknown

        /*  FAMILY PLANNING CODE      */
        hivEncounterTypeDictionary.put(1739, "FP");// True
        hivEncounterTypeDictionary.put(1740, "NOFP");// False

        /*  FAMILY PLANNING METHOD */
        hivEncounterTypeDictionary.put(280, "FP1");// Condoms
        hivEncounterTypeDictionary.put(311, "FP2");// Oral Contraceptives pills
        hivEncounterTypeDictionary.put(405, "FP3");// Injectable/Implantable
        hivEncounterTypeDictionary.put(408, "FP4");// Diaphram
        hivEncounterTypeDictionary.put(407, "FP5"); // Intrauterine device
        hivEncounterTypeDictionary.put(7777863, "FP6");// Vasectomy
        hivEncounterTypeDictionary.put(33, "FP7");// Others

        /* FUNCTIONAL STATUS */
        hivEncounterTypeDictionary.put(1009, "W");
        hivEncounterTypeDictionary.put(1010, "A");
        hivEncounterTypeDictionary.put(1012, "B");

        /* DEVELOPMENTAL STATUS */
        //hivEncounterTypeDictionary.put(1114, "W");
        //hivEncounterTypeDictionary.put(1115, "A");
        //hivEncounterTypeDictionary.put(1089, "B");

        /* WHO STAGING */
        hivEncounterTypeDictionary.put(7777928, "1");
        hivEncounterTypeDictionary.put(7777925, "2");
        hivEncounterTypeDictionary.put(7777927, "3");
        hivEncounterTypeDictionary.put(7777926, "4");

        /* TB STATUS */
        hivEncounterTypeDictionary.put(871, "1");//No sign or symptoms of disease
        hivEncounterTypeDictionary.put(870, "2");//Disease suspected
        hivEncounterTypeDictionary.put(868, "3");//Currently on INH Prophylaxis
        hivEncounterTypeDictionary.put(872, "4");// On treatment for disease
        hivEncounterTypeDictionary.put(7777924, "5");// TB Positive not on drugs

        /* OTHER OI PROBLEMS */
        hivEncounterTypeDictionary.put(1019, "1");//Herpes Zoster
        hivEncounterTypeDictionary.put(1030, "2");//Pneumonia
        hivEncounterTypeDictionary.put(1046, "3");//Dementia
        hivEncounterTypeDictionary.put(7777838, "4");//Thrush
        hivEncounterTypeDictionary.put(201, "5");//Fever
        hivEncounterTypeDictionary.put(194, "6");//Cough

        /* NOTED SIDE EFFECTS */
        hivEncounterTypeDictionary.put(706, "1");//Nausea and Vomiting
        hivEncounterTypeDictionary.put(196, "2");//Headache/confusion
        hivEncounterTypeDictionary.put(1714, "3");//Insomnia/Bad dreams
        hivEncounterTypeDictionary.put(573, "4");//Weakness
        hivEncounterTypeDictionary.put(557, "5");//Bleeding
        hivEncounterTypeDictionary.put(181, "6");//Rash
        hivEncounterTypeDictionary.put(7777791, "7");//Fat accumulation or loss
        hivEncounterTypeDictionary.put(671, "8");// Anemia
        hivEncounterTypeDictionary.put(7777872, "9");//Drainage of liquor
        hivEncounterTypeDictionary.put(1715, "10");// Steven Johnson syndrome
        hivEncounterTypeDictionary.put(7777787, "11");// Hyperglycemia

        /* DRUG ADHERENCE (ARV,CTX,INH)*/
        hivEncounterTypeDictionary.put(1398, "G");//Good
        hivEncounterTypeDictionary.put(1485, "F");//Fair
        hivEncounterTypeDictionary.put(1397, "P");//Poor

        /* WHY POOR FAIR DRUG ADHERENCE */
        hivEncounterTypeDictionary.put(1420, "17");//Afraid of/affected by drug side effects 
        hivEncounterTypeDictionary.put(1417, "5");//Became pregnant 
        hivEncounterTypeDictionary.put(1416, "4");//Busy/working/at school 
        hivEncounterTypeDictionary.put(48, "9");//	 Cannot afford treatment 
        hivEncounterTypeDictionary.put(1415, "3");//	 Change in routine/away from home 
        hivEncounterTypeDictionary.put(1422, "13");//	 Did not understand how to take medication 
        hivEncounterTypeDictionary.put(1424, "16");//	 Did not want to take medication 
        hivEncounterTypeDictionary.put(7777776, "8");//	 Drugs not available 
        hivEncounterTypeDictionary.put(1414, "2");//	 Fell asleep/slept through dose 
        hivEncounterTypeDictionary.put(900, "10");//	 Felt better 
        hivEncounterTypeDictionary.put(1421, "12");//Felt overwhelmed/depressed 
        hivEncounterTypeDictionary.put(1419, "11");//	 Felt sick/bad 
        hivEncounterTypeDictionary.put(899, "1");//	 Patient forgot 
        hivEncounterTypeDictionary.put(983, "6");//	 Patient moved 
        hivEncounterTypeDictionary.put(7777775, "7");//	 Ran out of medication 
        hivEncounterTypeDictionary.put(1423, "14");//	 Stigma 
        hivEncounterTypeDictionary.put(1425, "15");//	 Too many pills 

        /* COTRIM DOSE */
        hivEncounterTypeDictionary.put(7778519, "CTX960");// od Freq
        hivEncounterTypeDictionary.put(7778520, "CTX480"); // bd Freq
        hivEncounterTypeDictionary.put(7778522, "CTX240");// qds Freq need further consultation
        hivEncounterTypeDictionary.put(7778521, "CTX240");// tds Freq need futher consultation

 /* INH DOSE */
       /*hivEncounterTypeDictionary.put(1546, "H"); // Verify
        hivEncounterTypeDictionary.put(1543, "H"); // Verify*/

 /* DRUG NAME */
 /* hivEncounterTypeDictionary.put(1594, "H");
        hivEncounterTypeDictionary.put(35, "H");*/
    }

    public HIVEncounterType createHIVEncounter(Visit visit, Date artStartDate, List<Obs> obsList, ArrayList<DrugOrder> orders, ArrayList<Drugs> drugList) throws DatatypeConfigurationException {
        HIVEncounterType hivEncType = new HIVEncounterType();
        Date visitDate = visit.getVisitDate();
        LocalDate d2 = new LocalDate(new DateTime(visitDate));
        LocalDate d1 = new LocalDate(new DateTime(artStartDate));
        int monthOnART = 0;
        if (artStartDate != null && (d2.isAfter(d1) || d2.isEqual(d1))) {
            monthOnART = Months.monthsBetween(d1, d2).getMonths();
            hivEncType.setDurationOnArt(monthOnART);
        }

        hivEncType.setVisitDate(getXmlDate(visitDate));
        hivEncType.setVisitID(visit.getVisitID());

        int conceptID = 0;
        int value_numeric = 0;
        String value_text = "";
        Date value_datetime = null;
        String code = "";
        String description = "";
        String regimen = "";
        CodedSimpleType cst = null;
        int value_coded = 0;
        if (!obsList.isEmpty()) {
            String signsAndSymptoms = extractConceptCodes(528, obsList, hivEncounterTypeDictionary);//SIGN/SYMPTOM NAME
            if (StringUtils.isNoneEmpty(signsAndSymptoms)) {
                hivEncType.setOtherOIOtherProblems(signsAndSymptoms);
            }
            String notedSideEffects = extractConceptCodes(7777873, obsList, hivEncounterTypeDictionary);//Noted side effects
            if (StringUtils.isNoneEmpty(notedSideEffects)) {
                hivEncType.setNotedSideEffects(notedSideEffects);
            }

            //String reasonForPoorARVAdh = extractConceptCodes(7778843, obsList, hivEncounterTypeDictionary);// Reason for poor ARV
            //hivEncType.setWhyPoorFairARVDrugAdherence(reasonForPoorARVAdh);
            //String reasonForPoorCTXAdh = extractConceptCodes(7778844, obsList, hivEncounterTypeDictionary);// Reason for poor Cotrimoxazole
            //hivEncType.setWhyPoorFairCotrimoxazoleDrugAdherence(reasonForPoorCTXAdh);
            //String reasonForPoorINHAdh = extractConceptCodes(7778845, obsList, hivEncounterTypeDictionary);// Reason for poor INH
            //hivEncType.setWhyPoorFairINHDrugAdherence(reasonForPoorINHAdh);
            String systolic = "", diastolic = "", bp = "";
            Obs systolicObs = NDRCommonUtills.extractConcept(84, obsList);//Systolic Blood Pressure
            if (systolicObs != null) {
                systolic = systolicObs.getVariableValue();
            }
            Obs diastolicObs = NDRCommonUtills.extractConcept(568, obsList);//Diastolic Blood Pressure
            if (diastolicObs != null) {
                diastolic = diastolicObs.getVariableValue();
            }
            if (!StringUtils.isEmpty(systolic) && !StringUtils.isEmpty(diastolic)) {
                bp = systolic + "/" + diastolic;
                hivEncType.setBloodPressure(StringEscapeUtils.escapeXml10(bp));
            }
            for (Obs obs : obsList) {
                conceptID = obs.getConceptID();
                switch (conceptID) {
                    case 85:// Weight (Kg) from Vital Signs Form (6)
                        value_numeric = (int) Math.round(obs.getValueNumeric());
                        hivEncType.setWeight(value_numeric);
                        break;
                    case 571: // Height (cm) from Vital Signs Form (6)
                        value_numeric = (int) Math.round(obs.getValueNumeric());
                        hivEncType.setChildHeight(value_numeric);
                        break;
                    case 88:// CD4 Count from Laboratory Results Form (5)
                        value_numeric = (int) Math.round(obs.getValueNumeric());
                        hivEncType.setCD4(value_numeric);
                        hivEncType.setCD4TestDate(getXmlDate(obs.getVisitDate()));
                        break;
                    case 7777871:// PMTCT Link from Care/ART Card(8)
                        value_coded = obs.getValueCoded();
                        code = hivEncounterTypeDictionary.get(value_coded);
                        if (StringUtils.isNotEmpty(code)) {
                            hivEncType.setEDDandPMTCTLink(code);
                        }
                        break;
                    /*case 165945:
                        value_coded = obs.getValueCoded();
                        if (value_coded == 165685) {
                            hivEncType.setEDDandPMTCTLink("PMTCT");
                        }
                        break;*/
                    case 575:// Pregnancy Status (For female patients from Adult/Ped Initial Clinical Form)
                        value_coded = obs.getValueCoded();
                        if (value_coded == 80) {
                            hivEncType.setEDDandPMTCTLink("P");
                        } else if (value_coded == 81) {
                            hivEncType.setEDDandPMTCTLink("NP");
                        }
                        break;
                    case 1741://Family Planning Status
                        value_coded = obs.getValueCoded();
                        if (value_coded == 1739) {
                            hivEncType.setPatientFamilyPlanningCode("FP");
                        } else if (value_coded == 1740) {
                            hivEncType.setPatientFamilyPlanningCode("NOFP");
                        }
                        break;
                    case 1742:// Family Planning used
                        value_coded = obs.getValueCoded();
                        hivEncType.setPatientFamilyPlanningMethodCode(hivEncounterTypeDictionary.get(value_coded));
                        break;
                    case 1013:// Functional Status
                        value_coded = obs.getValueCoded();
                        hivEncType.setFunctionalStatus(hivEncounterTypeDictionary.get(value_coded));
                        break;
                    case 7777798:// WHO Stage
                        value_coded = obs.getValueCoded();
                        hivEncType.setWHOClinicalStage(hivEncounterTypeDictionary.get(value_coded));
                        break;
                    case 862:// TUBERCULOSIS DISEASE STATUS
                        value_coded = obs.getValueCoded();
                        hivEncType.setTBStatus(hivEncounterTypeDictionary.get(value_coded));
                        break;
                    /*
                        -- These two concepts were commented out because they can have multiple values (checkboxes)
                   case 528:
                        value_coded = obs.getValueCoded();
                        hivEncType.setOtherOIOtherProblems(hivEncounterTypeDictionary.get(value_coded));
                        break;
                    case 1607:
                        value_coded = obs.getValueCoded();
                        hivEncType.setNotedSideEffects(hivEncounterTypeDictionary.get(value_coded));
                        break;
                     */
                    case 7778706: // Adult 1st line ARV regimen
                        regimen = obs.getVariableValue();
                        value_coded = obs.getValueCoded();
                        cst = new CodedSimpleType();
                        //cst.setCode(pharmacyDictionary.getRegimenCode(regimen, 164506));
                        cst.setCode(pharmacyDictionary.getRegimenConceptNDRCode(value_coded));
                        cst.setCodeDescTxt(regimen);
                        hivEncType.setARVDrugRegimen(cst);
                        break;
                    case 7778707:// Adult 2nd line ARV regimen
                        regimen = obs.getVariableValue();
                        value_coded = obs.getValueCoded();
                        cst = new CodedSimpleType();
                        //cst.setCode(pharmacyDictionary.getRegimenCode(regimen, 164513));
                        cst.setCode(pharmacyDictionary.getRegimenConceptNDRCode(value_coded));
                        //cst.setCode(getRegimenCode(regimen));
                        cst.setCodeDescTxt(regimen);
                        hivEncType.setARVDrugRegimen(cst);
                        break;
                    case 7778838:// Adult 3rd Line ARV Regimen
                        regimen = obs.getVariableValue();
                        value_coded = obs.getValueCoded();
                        cst = new CodedSimpleType();
                        //cst.setCode(pharmacyDictionary.getRegimenCode(regimen, 164513));
                        cst.setCode(pharmacyDictionary.getRegimenConceptNDRCode(value_coded));
                        //cst.setCode(getRegimenCode(regimen));
                        //cst.setCodeDescTxt(pharmacyDictionary.getCareCardRegimen(regimen));
                        cst.setCodeDescTxt(regimen);
                        hivEncType.setARVDrugRegimen(cst);
                        break;
                    case 7778731: // Child 1st line ARV regimen
                        regimen = obs.getVariableValue();
                        value_coded = obs.getValueCoded();
                        cst = new CodedSimpleType();
                        //cst.setCode(pharmacyDictionary.getRegimenCode(regimen, 164507));
                        //cst.setCode(getRegimenCode(regimen));
                        //cst.setCodeDescTxt(pharmacyDictionary.getCareCardRegimen(regimen));
                        cst.setCode(pharmacyDictionary.getRegimenConceptNDRCode(value_coded));
                        cst.setCodeDescTxt(regimen);
                        hivEncType.setARVDrugRegimen(cst);
                        break;
                    case 7778741: // Child 2nd line ARV regimen
                        regimen = obs.getVariableValue();
                        value_coded = obs.getValueCoded();
                        cst = new CodedSimpleType();
                        //cst.setCode(pharmacyDictionary.getRegimenCode(regimen, 164514));
                        //cst.setCode(getRegimenCode(regimen));
                        //cst.setCodeDescTxt(pharmacyDictionary.getCareCardRegimen(regimen));
                        cst.setCode(pharmacyDictionary.getRegimenConceptNDRCode(value_coded));
                        cst.setCodeDescTxt(regimen);
                        hivEncType.setARVDrugRegimen(cst);
                        break;
                    case 7778742: //Child 3rd Line ARV Regimen
                        regimen = obs.getVariableValue();
                        value_coded = obs.getValueCoded();
                        cst = new CodedSimpleType();
                        //cst.setCode(pharmacyDictionary.getRegimenCode(regimen, 164514));
                        //cst.setCode(getRegimenCode(regimen));
                        //cst.setCodeDescTxt(pharmacyDictionary.getCareCardRegimen(regimen));
                        cst.setCode(pharmacyDictionary.getRegimenConceptNDRCode(value_coded));
                        cst.setCodeDescTxt(regimen);
                        hivEncType.setARVDrugRegimen(cst);
                        break;
                    case 7777874:
                        value_coded = obs.getValueCoded();
                        hivEncType.setARVDrugAdherence(hivEncounterTypeDictionary.get(value_coded));
                        break;
                    case 7778846:
                        value_coded = obs.getValueCoded();
                        cst = new CodedSimpleType();
                        cst.setCode(hivEncounterTypeDictionary.get(value_coded));
                        cst.setCodeDescTxt(hivEncounterTypeDictionary.get(value_coded));
                        hivEncType.setCotrimoxazoleDose(cst);
                        break;
                    case 7777875:
                        value_coded = obs.getValueCoded();
                        hivEncType.setCotrimoxazoleAdherence(hivEncounterTypeDictionary.get(value_coded));
                        break;
                    case 7778844:
                        value_coded = obs.getValueCoded();
                        hivEncType.setWhyPoorFairCotrimoxazoleDrugAdherence(hivEncounterTypeDictionary.get(value_coded));
                        break;
                   case  7778847:
                        value_coded = obs.getValueCoded();
                        cst = new CodedSimpleType();
                        cst.setCode(hivEncounterTypeDictionary.get(value_coded));
                        cst.setCodeDescTxt("Isoniazid");
                        hivEncType.setINHDose(cst);
                        break;
                    case 7777876:
                        value_coded = obs.getValueCoded();
                        hivEncType.setINHAdherence(hivEncounterTypeDictionary.get(value_coded));
                        break;
                    case 7778845:
                        value_coded = obs.getValueCoded();
                        hivEncType.setWhyPoorFairINHDrugAdherence(hivEncounterTypeDictionary.get(value_coded));
                        break;
 /*case 5096:
                        value_datetime = obs.getValueDate();
                        hivEncType.setNextAppointmentDate(getXmlDate(value_datetime));
                        break;*/
 /*case 7777821:
                        value_coded = obs.getValueCoded();
                        if (hivEncType.getNextAppointmentDate() == null) {
                            hivEncType.setNextAppointmentDate(getXmlDate(getNextAppointmentDate(obs.getVisitDate(), value_coded)));
                        }
                        break;*/
 /*                  case 77778364:
                        value_coded = obs.getValueCoded();
                        if (hivEncType.getINHDose() == null) {
                            cst = new CodedSimpleType();
                            cst.setCode(hivEncounterTypeDictionary.get(value_coded));
                            cst.setCodeDescTxt(obs.getVariableValue());
                            hivEncType.setINHDose(cst);
                        }*/

                    default:
                        break;
                }

            }

            /* PatientRegimen rgm = null;
            String openmrsARVRegimen = "";
            String cotrimDose = "";
            String inhDose = "";
            String drugName = "";
            String strength = "";
            String otherStrength = "";

            String regimenCode = "";
            for (DrugOrder ord : orders) {
                rgm = (PatientRegimen) ord;
                if (rgm.getStartDate() != null && rgm.getStartDate().equals(visitDate)) {
                    openmrsARVRegimen = rgm.getRegimenName();
                    regimenCode = rgm.getCode();
                    if (regimenCode!=null && openmrsARVRegimen!=null && !openmrsARVRegimen.isEmpty() && !regimenCode.equalsIgnoreCase("-1") && pharmacyDictionary.getRegimenCode(openmrsARVRegimen) != null) {
                        cst = new CodedSimpleType();
                        cst.setCode(pharmacyDictionary.getRegimenCode(openmrsARVRegimen));
                        cst.setCodeDescTxt(openmrsARVRegimen);
                        hivEncType.setARVDrugRegimen(cst);
                    }
                }
            }*/
 /*for (Drugs drg : drugList) {
                drugName = drg.getDrugName();
                strength = drg.getStrength();
                otherStrength = drg.getOtherStrength();
                if (drg.getDispensedDate() != null && drg.getDispensedDate().equals(visitDate)) {
                    if (hivEncType.getCotrimoxazoleDose() == null) {
                        if (StringUtils.equalsIgnoreCase(drugName, "CTX")) {
                            if (StringUtils.equalsIgnoreCase("960mg", strength) || StringUtils.equalsIgnoreCase("960mg", otherStrength)) {
                                code = "CTX960";
                                description = "Cotrimoxazole 960mg";
                                cst = new CodedSimpleType();
                                cst.setCode(code);
                                cst.setCodeDescTxt(description);
                                hivEncType.setCotrimoxazoleDose(cst);
                            }
                            if (StringUtils.equalsIgnoreCase("120mg", strength) || StringUtils.equalsIgnoreCase("120mg", otherStrength)) {
                                code = "CTX240";
                                description = "Cotrimoxazole 120mg";
                                cst = new CodedSimpleType();
                                cst.setCode(code);
                                cst.setCodeDescTxt(description);
                                hivEncType.setCotrimoxazoleDose(cst);
                            }
                            if (StringUtils.equalsIgnoreCase("480mg", strength) || StringUtils.equalsIgnoreCase("480mg", otherStrength)) {
                                code = "CTX480";
                                description = "Cotrimoxazole 480mg";
                                cst = new CodedSimpleType();
                                cst.setCode(code);
                                cst.setCodeDescTxt(description);
                                hivEncType.setCotrimoxazoleDose(cst);
                            }
                        }
                    }
                    if (hivEncType.getINHDose() == null) {
                        if (StringUtils.contains(drugName, "INH")) {
                            code = "H";
                            description = "Isoniazid " + strength + otherStrength;
                            cst = new CodedSimpleType();
                            cst.setCode(code);
                            cst.setCodeDescTxt(description);
                            hivEncType.setINHDose(cst);
                        }
                    }
                }

            }*/
        }

        return hivEncType;
    }

    public String extractConceptCodes(int conceptID, List<Obs> obsList, Map<Integer, String> answerMap) {
        String codedAnswer = "";
        String val = "";
        ArrayList<String> valList = new ArrayList<String>();
        for (Obs obs : obsList) {
            if (obs.getConceptID() == conceptID) {
                if (answerMap.containsKey(obs.getValueCoded())) {
                    val = answerMap.get(obs.getValueCoded());
                    valList.add(val);
                }
            }
        }
        codedAnswer = StringUtils.join(valList, "|");
        return codedAnswer;
    }

}
