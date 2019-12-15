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
    private Map<Integer,String> hivEncounterTypeDictionary=new HashMap<Integer,String>();
    private NDRPharmacyDictionary pharmacyDictionary;
    public NDRClinicalDictionary(){
        pharmacyDictionary=new NDRPharmacyDictionary();
        loadDictionary();
    }
    private void loadDictionary(){
        loadHIVEncounterDictionary();
    }
     public void loadHIVEncounterDictionary() {
        hivEncounterTypeDictionary = new HashMap<Integer, String>();

        /* EDD AND PMTCT LINK    */
        hivEncounterTypeDictionary.put(165048, "P");
        hivEncounterTypeDictionary.put(165049, "PMTCT");
        hivEncounterTypeDictionary.put(165047, "NP");
        //hivEncounterTypeDictionary.put(13, "NK");

        /*  FAMILY PLANNING CODE      */
        hivEncounterTypeDictionary.put(1, "FP");// True
        hivEncounterTypeDictionary.put(0, "NOFP");// False

        /*  FAMILY PLANNING METHOD */
        hivEncounterTypeDictionary.put(190, "FP1");// Condoms
        hivEncounterTypeDictionary.put(311, "FP2");// Oral Contraceptives pills
        hivEncounterTypeDictionary.put(405, "FP3");// Injectable/Implantable
        //hivEncounterTypeDictionary.put(408, "FP4");// Cervical Cap
        hivEncounterTypeDictionary.put(5279, "FP5"); // Intrauterine device
        hivEncounterTypeDictionary.put(1489, "FP6");// Vasectomy
        hivEncounterTypeDictionary.put(5622, "FP7");// Others

        /* FUNCTIONAL STATUS */
        hivEncounterTypeDictionary.put(162750, "W");
        hivEncounterTypeDictionary.put(160026, "A");
        hivEncounterTypeDictionary.put(162752, "B");
        /* DEVELOPMENTAL STATUS */
        //hivEncounterTypeDictionary.put(1114, "W");
        //hivEncounterTypeDictionary.put(1115, "A");
        //hivEncounterTypeDictionary.put(1089, "B");

        /* WHO STAGING */
        hivEncounterTypeDictionary.put(1204, "1");
        hivEncounterTypeDictionary.put(1205, "2");
        hivEncounterTypeDictionary.put(1206, "3");
        hivEncounterTypeDictionary.put(1207, "4");

        /* TB STATUS */
        hivEncounterTypeDictionary.put(1660, "1");//No sign or symptoms of disease
        hivEncounterTypeDictionary.put(142177, "2");//Disease suspected
        hivEncounterTypeDictionary.put(166042, "3");//Currently on INH Prophylaxis
        hivEncounterTypeDictionary.put(1662, "4");// On treatment for disease
        hivEncounterTypeDictionary.put(1661, "5");// TB Positive not on drugs

        /* OTHER OI PROBLEMS */
        hivEncounterTypeDictionary.put(117543, "1");//Herpes Zoster
        hivEncounterTypeDictionary.put(114100, "2");//Pneumonia
        hivEncounterTypeDictionary.put(119566, "3");//Dementia
        hivEncounterTypeDictionary.put(5340, "4");//Thrush
        hivEncounterTypeDictionary.put(140238, "5");//Fever
        hivEncounterTypeDictionary.put(143264, "6");//Cough

        /* NOTED SIDE EFFECTS */
        hivEncounterTypeDictionary.put(133473, "1");//Nausea and Vomiting
        hivEncounterTypeDictionary.put(139084, "2");//Headache/confusion
        hivEncounterTypeDictionary.put(116743, "3");//Insomnia/Bad dreams
        hivEncounterTypeDictionary.put(5226, "4");//Weakness
        hivEncounterTypeDictionary.put(165767, "5");//Bleeding
        hivEncounterTypeDictionary.put(512, "6");//Rash
        hivEncounterTypeDictionary.put(165052, "7");//Fat accumulation or loss
        hivEncounterTypeDictionary.put(121629, "8");// Anemia
        hivEncounterTypeDictionary.put(165053, "9");//Drainage of liquor
        hivEncounterTypeDictionary.put(125886, "10");// Steven Johnson syndrome
        hivEncounterTypeDictionary.put(138291, "11");// Hyperglycemia

        /* DRUG ADHERENCE */
        hivEncounterTypeDictionary.put(165287, "G");
        hivEncounterTypeDictionary.put(165289, "F");
        hivEncounterTypeDictionary.put(165288, "P");

        /* WHY POOR FAIR DRUG ADHERENCE */
 /*hivEncounterTypeDictionary.put(899, "1");
        hivEncounterTypeDictionary.put(1414, "2");
        hivEncounterTypeDictionary.put(1415, "3");
        hivEncounterTypeDictionary.put(1416, "4");
        hivEncounterTypeDictionary.put(1417, "5");
        hivEncounterTypeDictionary.put(983, "6");
        hivEncounterTypeDictionary.put(7777775, "7");
        hivEncounterTypeDictionary.put(44, "8");
        hivEncounterTypeDictionary.put(48, "9");
        hivEncounterTypeDictionary.put(900, "10");
        hivEncounterTypeDictionary.put(1419, "11");
        hivEncounterTypeDictionary.put(1421, "12");
        hivEncounterTypeDictionary.put(1422, "13");
        hivEncounterTypeDictionary.put(1423, "14");
        hivEncounterTypeDictionary.put(1425, "15");*/
        // code 16 was not availaible on the care card (VERIFY!)
        //hivEncounterTypeDictionary.put(1424, "17");

        /* COTRIM DOSE */
 /*hivEncounterTypeDictionary.put(1552, "CTX960");
        hivEncounterTypeDictionary.put(1547, "CTX480");
        hivEncounterTypeDictionary.put(7778548, "CTX240");// Mapped to Cotrim dispersible 120mg*/

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
            String signsAndSymptoms = extractConceptCodes(160170, obsList, hivEncounterTypeDictionary);//528
            if (StringUtils.isNoneEmpty(signsAndSymptoms)) {
                hivEncType.setOtherOIOtherProblems(signsAndSymptoms);
            }
            String notedSideEffects = extractConceptCodes(159935, obsList, hivEncounterTypeDictionary);//1607
            if (StringUtils.isNoneEmpty(notedSideEffects)) {
                hivEncType.setNotedSideEffects(notedSideEffects);
            }

            //String reasonForPoorARVAdh=extractConceptCodes(7778453, obsList, hivEncounterTypeDictionary);
            //hivEncType.setWhyPoorFairARVDrugAdherence(reasonForPoorARVAdh);
            //String reasonForPoorCTXAdh=extractConceptCodes(7778454, obsList, hivEncounterTypeDictionary);
            //hivEncType.setWhyPoorFairCotrimoxazoleDrugAdherence(reasonForPoorCTXAdh);
            //String reasonForPoorINHAdh=extractConceptCodes(7778455, obsList, hivEncounterTypeDictionary);
            //hivEncType.setWhyPoorFairINHDrugAdherence(reasonForPoorINHAdh);
            String systolic = "", diastolic = "", bp = "";
            Obs systolicObs = NDRCommonUtills.extractConcept(5085, obsList);//84
            if (systolicObs != null) {
                systolic = systolicObs.getVariableValue();
            }
            Obs diastolicObs = NDRCommonUtills.extractConcept(5086, obsList);//568
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
                    case 5089://1734
                        value_numeric = (int) Math.round(obs.getValueNumeric());
                        hivEncType.setWeight(value_numeric);
                        break;
                    case 5090:
                        value_numeric = (int) Math.round(obs.getValueNumeric());
                        hivEncType.setChildHeight(value_numeric);
                        break;
                    case 5497:
                        value_numeric = (int) Math.round(obs.getValueNumeric());
                        hivEncType.setCD4(value_numeric);
                        hivEncType.setCD4TestDate(getXmlDate(obs.getVisitDate()));
                        break;
                    case 85:// investigate
                        value_numeric = (int) Math.round(obs.getValueNumeric());
                        hivEncType.setWeight(value_numeric);
                        break;

                    case 571:// Investigate
                        value_numeric = (int) Math.round(obs.getValueNumeric());
                        hivEncType.setChildHeight(value_numeric);
                        break;
                    case 165050:
                        value_coded = obs.getValueCoded();
                        code = hivEncounterTypeDictionary.get(value_coded);
                        if (StringUtils.isNotEmpty(code)) {
                            hivEncType.setEDDandPMTCTLink(code);
                        }
                        break;
                    case 165945:
                        value_coded = obs.getValueCoded();
                        if (value_coded == 165685) {
                            hivEncType.setEDDandPMTCTLink("PMTCT");
                        }
                        break;
                    case 1434:
                        value_coded = obs.getValueCoded();
                        if (value_coded == 1) {
                            hivEncType.setEDDandPMTCTLink("P");
                        } else if (value_coded == 0) {
                            hivEncType.setEDDandPMTCTLink("NP");
                        }
                        break;
                    case 5271://1741
                        value_coded = obs.getValueCoded();
                        if (value_coded == 1) {
                            hivEncType.setPatientFamilyPlanningCode("FP");
                        } else if (value_coded == 0) {
                            hivEncType.setPatientFamilyPlanningCode("NOFP");
                        }
                        break;
                    case 374:
                        value_coded = obs.getValueCoded();
                        hivEncType.setPatientFamilyPlanningMethodCode(hivEncounterTypeDictionary.get(value_coded));
                        break;
                    case 165039:
                        value_coded = obs.getValueCoded();
                        hivEncType.setFunctionalStatus(hivEncounterTypeDictionary.get(value_coded));
                        break;
                    case 5356:
                        value_coded = obs.getValueCoded();
                        hivEncType.setWHOClinicalStage(hivEncounterTypeDictionary.get(value_coded));
                        break;
                    case 1659:
                        value_coded = obs.getValueCoded();
                        hivEncType.setTBStatus(hivEncounterTypeDictionary.get(value_coded));
                        break;
                    /*case 528:
                        value_coded = obs.getValueCoded();
                        hivEncType.setOtherOIOtherProblems(hivEncounterTypeDictionary.get(value_coded));
                        break;
                    case 1607:
                        value_coded = obs.getValueCoded();
                        hivEncType.setNotedSideEffects(hivEncounterTypeDictionary.get(value_coded));
                        break;*/
 /*                 case 88:
                        value_numeric = (int) obs.getValueNumeric();
                        hivEncType.setCD4(value_numeric);
                        hivEncType.setCD4TestDate(getXmlDate(obs.getVisitDate()));
                        break;*/
                    case 164506: // Adult 1st line ARV regimen
                        regimen = obs.getVariableValue();
                        cst = new CodedSimpleType();
                        cst.setCode(pharmacyDictionary.getRegimenCode(regimen, 164506));
                        cst.setCodeDescTxt(pharmacyDictionary.getCareCardRegimen(regimen));
                        hivEncType.setARVDrugRegimen(cst);
                        break;
                    case 164513:// Adult 2nd line ARV regimen
                        regimen = obs.getVariableValue();
                        cst = new CodedSimpleType();
                        cst.setCode(pharmacyDictionary.getRegimenCode(regimen, 164513));
                        //cst.setCode(getRegimenCode(regimen));
                        cst.setCodeDescTxt(pharmacyDictionary.getCareCardRegimen(regimen));
                        hivEncType.setARVDrugRegimen(cst);
                        break;
                    case 165702:// Adult 3rd Line ARV Regimen
                        regimen = obs.getVariableValue();
                        cst = new CodedSimpleType();
                        cst.setCode(pharmacyDictionary.getRegimenCode(regimen, 164513));
                        //cst.setCode(getRegimenCode(regimen));
                        cst.setCodeDescTxt(pharmacyDictionary.getCareCardRegimen(regimen));
                        hivEncType.setARVDrugRegimen(cst);
                        break;
                    case 164507: // Child 1st line ARV regimen
                        regimen = obs.getVariableValue();
                        cst = new CodedSimpleType();
                        cst.setCode(pharmacyDictionary.getRegimenCode(regimen, 164507));
                        //cst.setCode(getRegimenCode(regimen));
                        cst.setCodeDescTxt(pharmacyDictionary.getCareCardRegimen(regimen));
                        hivEncType.setARVDrugRegimen(cst);
                        break;
                    case 164514: // Child 2nd line ARV regimen
                        regimen = obs.getVariableValue();
                        cst = new CodedSimpleType();
                        cst.setCode(pharmacyDictionary.getRegimenCode(regimen, 164514));
                        //cst.setCode(getRegimenCode(regimen));
                        cst.setCodeDescTxt(pharmacyDictionary.getCareCardRegimen(regimen));
                        hivEncType.setARVDrugRegimen(cst);
                        break;
                    case 165703: //Child 3rd Line ARV Regimen
                        regimen = obs.getVariableValue();
                        cst = new CodedSimpleType();
                        cst.setCode(pharmacyDictionary.getRegimenCode(regimen, 164514));
                        //cst.setCode(getRegimenCode(regimen));
                        cst.setCodeDescTxt(pharmacyDictionary.getCareCardRegimen(regimen));
                        hivEncType.setARVDrugRegimen(cst);
                        break;
                    case 165290:
                        value_coded = obs.getValueCoded();
                        hivEncType.setARVDrugAdherence(hivEncounterTypeDictionary.get(value_coded));
                        break;
                    /*case 7778453:
                        value_coded = obs.getValueCoded();
                        hivEncType.setWhyPoorFairARVDrugAdherence(hivEncounterTypeDictionary.get(value_coded));
                        break;*/
 /*                   case 7778203:
                        value_coded = obs.getValueCoded();
                        cst = new CodedSimpleType();
                        cst.setCode(hivEncounterTypeDictionary.get(value_coded));
                        cst.setCodeDescTxt("Cotrimoxazole " + obs.getVariableValue());
                        hivEncType.setCotrimoxazoleDose(cst);
                        break;*/
                    case 161625:
                        value_coded = obs.getValueCoded();
                        hivEncType.setCotrimoxazoleAdherence(hivEncounterTypeDictionary.get(value_coded));
                        break;
                    /*case 7778454:
                        value_coded = obs.getValueCoded();
                        hivEncType.setWhyPoorFairCotrimoxazoleDrugAdherence(hivEncounterTypeDictionary.get(value_coded));
                        break;*/
 /*                  case 7778202:
                        value_coded = obs.getValueCoded();
                        cst = new CodedSimpleType();
                        cst.setCode(hivEncounterTypeDictionary.get(value_coded));
                        cst.setCodeDescTxt("Isoniazid " + obs.getVariableValue());
                        hivEncType.setINHDose(cst);
                        break;*/
                    case 161653:
                        value_coded = obs.getValueCoded();
                        hivEncType.setINHAdherence(hivEncounterTypeDictionary.get(value_coded));
                        break;
                    /*case 7778455:
                        value_coded = obs.getValueCoded();
                        hivEncType.setWhyPoorFairINHDrugAdherence(hivEncounterTypeDictionary.get(value_coded));
                        break;*/
                    case 5096:
                        value_datetime = obs.getValueDate();
                        hivEncType.setNextAppointmentDate(getXmlDate(value_datetime));
                        break;
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

            PatientRegimen rgm = null;
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
            }
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
