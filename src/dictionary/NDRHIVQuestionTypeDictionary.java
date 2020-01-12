/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import static com.inductivehealth.ndr.client.Client.getXmlDate;
import com.inductivehealth.ndr.schema.CodedSimpleType;
import com.inductivehealth.ndr.schema.FacilityType;
import com.inductivehealth.ndr.schema.HIVQuestionsType;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import model.datapump.Obs;
import model.datapump.PatientRegimen;

/**
 *
 * @author The Bright
 */
public class NDRHIVQuestionTypeDictionary {

    private HashMap<Integer, String> hivQueDictionary = new HashMap<Integer, String>();
    private NDRPharmacyDictionary pharmacyDictionary = new NDRPharmacyDictionary();

    public NDRHIVQuestionTypeDictionary() {
        loadDictionary();
    }

    private void loadDictionary() {
        loadHIVQuestionMapDictionary();
    }

    public void loadHIVQuestionMapDictionary() {
        //hivQueDictionary = new HashMap<Integer, String>();
        //Care Entry Point
        hivQueDictionary.put(1056, "1");//	STI
        hivQueDictionary.put(1066, "2");//	OPD
        hivQueDictionary.put(243, "3");//	HCT
        hivQueDictionary.put(7778232, "4");//	CBO
        hivQueDictionary.put(1057, "5");//	Private
        hivQueDictionary.put(1055, "6");//	TB
        hivQueDictionary.put(7778233, "7");//	Ward
        hivQueDictionary.put(7778234, "8");//	Casualty
        hivQueDictionary.put(1053, "9");//	ANC/PMTCT
        hivQueDictionary.put(1058, "11");//	Sex workers Outreach
        hivQueDictionary.put(7778235, "12");//	Current clinic patient
        hivQueDictionary.put(1059, "13");//	Self-referral
        hivQueDictionary.put(7778236, "14");//	Pre-ART Transfer in
        //Mode of HIV Test
        hivQueDictionary.put(7777948, "HIVAb");//HIV-Ab
        hivQueDictionary.put(1661, "HIVPCR");//PCR
        //Prior ART
        hivQueDictionary.put(7777764, "T");//Transfer in with records
        hivQueDictionary.put(7777766, "NT");//Earlier ARV but not a transfer in
        hivQueDictionary.put(7777765, "P");//PMTCT only
        hivQueDictionary.put(7777767, "N");//None
        /*    Reason Medically Eligible    */
        hivQueDictionary.put(1730, "1");//Clinically only
        hivQueDictionary.put(88, "2");//CD4
        hivQueDictionary.put(1153, "3");//CD4%
        hivQueDictionary.put(1717, "4");//TLC

        /* Clinical Stage at start of ART */
        hivQueDictionary.put(861, "1");//WHO STAGE 1 ADULT (861)
        hivQueDictionary.put(864, "2");//WHO STAGE 2 ADULT (864)
        hivQueDictionary.put(865, "3");//WHO STAGE 3 ADULT (865)
        hivQueDictionary.put(866, "4");//WHO STAGE 4 ADULT (866)
        hivQueDictionary.put(1121, "1");//WHO STAGE 1 PEDS (1121)
        hivQueDictionary.put(1122, "2");//WHO STAGE 2 PEDS (1122)
        hivQueDictionary.put(1123, "3");//WHO STAGE 3 PEDS (1123)
        hivQueDictionary.put(1124, "4");//WHO STAGE 4 PEDS (1124)
        /* Functional status at ART start */
        hivQueDictionary.put(1009, "W");// Working
        hivQueDictionary.put(1010, "A");// Ambulatory
        hivQueDictionary.put(1012, "B");// Bedridden
        /* Initial TB Status */
        hivQueDictionary.put(871, "1");//No signs or symptoms of TB
        hivQueDictionary.put(873, "2");//TB suspected and referred for evaluation
        hivQueDictionary.put(868, "3");//Currently on INH prophylaxis (IPT)
        hivQueDictionary.put(872, "4");//Currently on TB treatment
        hivQueDictionary.put(7777924, "5");//TB Positive =not on TB drugs
        /* Cause of Death HIV Related */
        hivQueDictionary.put(80, "Y");//Yes
        hivQueDictionary.put(81, "N");//No
        hivQueDictionary.put(13, "U");//Unknown
        // First Regimen
        

    }

    public HIVQuestionsType createHIVQuestionType(PatientRegimen openmrsFirstRegimen, Date artStartDate, Date enrollmentDt, boolean onART, List<Obs> obsList) throws DatatypeConfigurationException {
        HIVQuestionsType hiv = new HIVQuestionsType();
        if (enrollmentDt != null) {
            hiv.setEnrolledInHIVCareDate(getXmlDate(enrollmentDt));
        }
        String regimenCode = null;
        String regimenName = null;
        if (openmrsFirstRegimen != null) {
            regimenName = openmrsFirstRegimen.getRegimenName();
            regimenCode = pharmacyDictionary.getRegimenCode(openmrsFirstRegimen.getRegimenName());
        }

        CodedSimpleType cst1 = null;
        int conceptID = 0;
        int form_id = 0;
        double value_numeric = 0.0;
        int value_coded = 0;
        String value_text = "";
        Date value_datetime = null;
        FacilityType ft = null;
        /*if (regimenCode != null && regimenName != null) {
            cst1 = new CodedSimpleType();
            cst1.setCode(regimenCode);
            cst1.setCodeDescTxt(pharmacyDictionary.getCareCardRegimen(regimenName));
            hiv.setFirstARTRegimen(cst1);
        }*/
        if (artStartDate != null) {
            hiv.setARTStartDate(getXmlDate(artStartDate));
        } else if (regimenCode != null && openmrsFirstRegimen != null) {
            hiv.setARTStartDate(getXmlDate(openmrsFirstRegimen.getStartDate()));
        }
        for (Obs obs : obsList) {
            conceptID = obs.getConceptID();
            //System.out.println("Concept ID: "+conceptID);
            switch (conceptID) {
                case 1052:// Care Entry Point
                    value_coded = obs.getValueCoded();
                    hiv.setCareEntryPoint(hivQueDictionary.get(value_coded));
                    break;
                case 859: // Date Confirmed Positive
                    value_datetime = obs.getValueDate();
                    if (value_datetime != null) {
                        hiv.setFirstConfirmedHIVTestDate(getXmlDate(value_datetime));
                    }
                    break;
                case 7777949: // First HIV Test Mode
                    value_coded = obs.getValueCoded();
                    hiv.setFirstHIVTestMode(hivQueDictionary.get(value_coded));
                    break;
                case 7778237: // Where First HIV Test
                    value_text = obs.getValueText();
                    ft = NDRDemographicsDictionary.createFacilityType(value_text, "FAC");
                    hiv.setWhereFirstHIVTest(value_text);
                    break;
                case 7777768:// Prior ART Exposure
                    value_coded = obs.getValueCoded();
                    hiv.setPriorArt(hivQueDictionary.get(value_coded));
                    break;
                case 1703:// Medically Eligible Date
                    value_datetime = obs.getValueDate();
                    if (value_datetime != null) {
                        hiv.setMedicallyEligibleDate(getXmlDate(value_datetime));
                    }
                    break;
                case 1731: // Reason Medically Eligible
                    value_coded = obs.getValueCoded();
                    hiv.setReasonMedicallyEligible(hivQueDictionary.get(value_coded));
                    break;
                case 7777862:// Initial Adherence Counseling
                    value_datetime = obs.getValueDate();
                    hiv.setInitialAdherenceCounselingCompletedDate(getXmlDate(value_datetime));
                    break;
                case 978: //Date Trasferred
                    form_id = obs.getFormID();// 
                    if (form_id == 22) {// Check if it is coming from Care Card Front Page
                        value_datetime = obs.getValueDate();
                        if (value_datetime != null) {
                            hiv.setTransferredInDate(getXmlDate(value_datetime));
                        }
                    }
                    break;
                case 1732: //Transferred in From
                    value_text = obs.getValueText();
                    FacilityType ft2 = NDRDemographicsDictionary.createFacilityType(value_text, "FAC");
                    hiv.setTransferredInFrom(ft2);
                    break;
                case 860: // WHO Clinical Stage at ART Start
                    value_coded = obs.getValueCoded();
                    hiv.setWHOClinicalStageARTStart(hivQueDictionary.get(value_coded));
                    break;
                case 85: //Weight At ART Start
                    value_numeric = obs.getValueNumeric();
                    hiv.setWeightAtARTStart((int) value_numeric);
                    break;
                case 571: // Child Height At ART Start
                    value_numeric = obs.getValueNumeric();
                    hiv.setChildHeightAtARTStart((int) value_numeric);
                    break;
                case 1013: // Functional Status at ART Start
                    value_coded = obs.getValueCoded();
                    hiv.setFunctionalStatusStartART(hivQueDictionary.get(value_coded));
                    break;
                case 88: // CD4 At ART Start
                    value_numeric = obs.getValueNumeric();
                    hiv.setCD4AtStartOfART(String.valueOf(value_numeric));
                    break;
                case 977:
                    value_coded = obs.getValueCoded();
                    if (value_coded == 211) {
                        hiv.setPatientTransferredOut(true);
                        if (onART) {
                            hiv.setTransferredOutStatus("A");
                        }
                    } else if (value_coded == 975) {
                        hiv.setPatientHasDied(true);
                        if (onART) {
                            hiv.setStatusAtDeath("A");
                        }
                    }
                    break;
                case 7778447: // Patient Has Died
                    value_coded = obs.getValueCoded();
                    if (value_coded == 80) {
                        hiv.setPatientHasDied(true);
                    }
                    if (onART) {
                        hiv.setStatusAtDeath("A");
                    }
                    break;
                case 980://Source of Death Information
                    value_text = obs.getValueText();
                    hiv.setSourceOfDeathInformation(value_text);
                    break;
                case 862: // TB Status
                    value_coded = obs.getValueCoded();
                    hiv.setInitialTBStatus(hivQueDictionary.get(value_coded));
                    break;
                case 7777869:// Cause of Death HIV Realated
                    value_coded = obs.getValueCoded();
                    hiv.setCauseOfDeathHIVRelated(hivQueDictionary.get(value_coded));
                    break;
                case 7777864: // First ART Regimen
                    value_coded=obs.getValueCoded();
                    cst1=new CodedSimpleType();
                    cst1.setCodeDescTxt(obs.getVariableValue());
                    cst1.setCode(pharmacyDictionary.getRegimenConceptNDRCode(value_coded));
                    hiv.setFirstARTRegimen(cst1);
                    break;
                case 7777915://Paed 1st Line
                    value_coded=obs.getValueCoded();
                    cst1=new CodedSimpleType();
                    cst1.setCodeDescTxt(obs.getVariableValue());
                    cst1.setCode(pharmacyDictionary.getRegimenConceptNDRCode(value_coded));
                    hiv.setFirstARTRegimen(cst1);
                    break;
                case 7777917: //Pead Alternate Line
                    value_coded=obs.getValueCoded();
                    cst1=new CodedSimpleType();
                    cst1.setCodeDescTxt(obs.getVariableValue());
                    cst1.setCode(pharmacyDictionary.getRegimenConceptNDRCode(value_coded));
                    hiv.setFirstARTRegimen(cst1);
                    break;
                case 7777923: //Pead 2nd line
                    value_coded=obs.getValueCoded();
                    cst1=new CodedSimpleType();
                    cst1.setCodeDescTxt(obs.getVariableValue());
                    cst1.setCode(pharmacyDictionary.getRegimenConceptNDRCode(value_coded));
                    hiv.setFirstARTRegimen(cst1);
                    break;
                default:
                    break;
            }

        }
        return hiv;
    }
}
