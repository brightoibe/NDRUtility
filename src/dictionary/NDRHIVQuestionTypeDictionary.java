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
        hivQueDictionary.put(160546, "1");//STI OUTPATIENT
        hivQueDictionary.put(160542, "2");//GOPD
        hivQueDictionary.put(160539, "3");//VCT
        hivQueDictionary.put(160543, "4");//CBO
        hivQueDictionary.put(160547, "5");//Private/Commercial Health Facility
        hivQueDictionary.put(160541, "6");//TB Outpatient
        hivQueDictionary.put(160536, "7");//Ward
        hivQueDictionary.put(5622, "8");//Casualty
        hivQueDictionary.put(160538, "9");//ANC/PMTCT
        hivQueDictionary.put(160548, "10");//IDU
        hivQueDictionary.put(160550, "11");//Sex Worker Outreach
        hivQueDictionary.put(160536, "12");//Current Clinic Patient
        hivQueDictionary.put(160551, "13");//Self Referral
        hivQueDictionary.put(160563, "14");//Pre-ART Transfer in
        hivQueDictionary.put(164949, "HIVAb");//HIV-Ab
        hivQueDictionary.put(164948, "HIVPCR");//PCR
        hivQueDictionary.put(165238, "T");//Transfered in with records
        hivQueDictionary.put(165239, "NT");//Earlier ARV but not transfered in
        hivQueDictionary.put(165240, "P");//PMTCT Only
        hivQueDictionary.put(1107, "N");//Has never received ARVs 
        /*    Reason Medically Eligible    */
 /* hivQueDictionary.put(1730, "1");
         hivQueDictionary.put(88, "2");
         hivQueDictionary.put(1153, "3");
         hivQueDictionary.put(1717, "4"); */

 /* Clinical Stage at start of ART */
 /* hivQueDictionary.put(7777925, "1");
          hivQueDictionary.put(7777926, "2");
          hivQueDictionary.put(7777927, "3");
          hivQueDictionary.put(7777928, "4");  */
 /* Functional status at ART start */
 /*hivQueDictionary.put(1009, "W");
        hivQueDictionary.put(1010, "A");
        hivQueDictionary.put(1012, "B");*/
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
        if (regimenCode != null && regimenName != null) {
            cst1 = new CodedSimpleType();
            cst1.setCode(regimenCode);
            cst1.setCodeDescTxt(pharmacyDictionary.getCareCardRegimen(regimenName));
            hiv.setFirstARTRegimen(cst1);
        }
        if (artStartDate != null) {
            hiv.setARTStartDate(getXmlDate(artStartDate));
        } else if (regimenCode != null && openmrsFirstRegimen != null) {
            hiv.setARTStartDate(getXmlDate(openmrsFirstRegimen.getStartDate()));
        }
        for (Obs obs : obsList) {
            conceptID = obs.getConceptID();
            //System.out.println("Concept ID: "+conceptID);
            switch (conceptID) {
                case 160540:
                    value_coded = obs.getValueCoded();
                    hiv.setCareEntryPoint(hivQueDictionary.get(value_coded));
                    break;
                case 160554:
                    value_datetime = obs.getValueDate();
                    if (value_datetime != null) {
                        hiv.setFirstConfirmedHIVTestDate(getXmlDate(value_datetime));
                    }
                    break;
                case 164947:
                    value_coded = obs.getValueCoded();
                    hiv.setFirstHIVTestMode(hivQueDictionary.get(value_coded));
                    break;
                /*case 7778238:
                    value_text = obs.getValueText();
                    //ft=createFacilityType(value_text);
                    hiv.setWhereFirstHIVTest(value_text);
                    break;*/
                case 165242:
                    value_coded = obs.getValueCoded();
                    hiv.setPriorArt(hivQueDictionary.get(value_coded));
                    break;
                /*case 1703:
                    value_datetime = obs.getValueDate();
                    if (value_datetime != null) {
                        hiv.setMedicallyEligibleDate(getXmlDate(value_datetime));
                    }
                    break;*/
                /*case 1731:
                    value_coded = obs.getValueCoded();
                    hiv.setReasonMedicallyEligible(hivQueDictionary.get(value_coded));
                    break;*/
                /* case 7777862:
                    value_datetime = obs.getValueDate();
                    hiv.setInitialAdherenceCounselingCompletedDate(getXmlDate(value_datetime));
                    break;*/
                case 160534:
                    form_id = obs.getFormID();
                    if (form_id == 23) {
                        value_datetime = obs.getValueDate();
                        if (value_datetime != null) {
                            hiv.setTransferredInDate(getXmlDate(value_datetime));
                        }
                    }
                    break;
                case 160535:
                    value_text = obs.getValueText();
                    FacilityType ft2 = NDRDemographicsDictionary.createFacilityType(value_text, "FAC");
                    hiv.setTransferredInFrom(ft2);
                    break;
                /*case 7778529:
                    value_coded = obs.getValueCoded();
                    hiv.setWHOClinicalStageARTStart(hivQueDictionary.get(value_coded));
                    break;
                case 1734:
                    value_numeric = obs.getValueNumeric();
                    hiv.setWeightAtARTStart((int) value_numeric);
                    break;
                case 1735:
                    value_numeric = obs.getValueNumeric();
                    hiv.setChildHeightAtARTStart((int) value_numeric);
                    break;
                case 7778530:
                    value_coded = obs.getValueCoded();
                    hiv.setFunctionalStatusStartART(hivQueDictionary.get(value_coded));
                    break;
                case 1733:
                    value_numeric = obs.getValueNumeric();
                    hiv.setCD4AtStartOfART(String.valueOf(value_numeric));
                    break;*/
                case 165470:
                    value_coded = obs.getValueCoded();
                    if (value_coded == 159492) {
                        hiv.setPatientTransferredOut(true);
                        if (onART) {
                            hiv.setTransferredOutStatus("A");
                        }
                    } else if (value_coded == 165889) {
                        hiv.setPatientHasDied(true);
                        if (onART) {
                            hiv.setStatusAtDeath("A");
                        }
                    }
                    break;
                /*case 980:
                    value_text = obs.getValueText();
                    hiv.setSourceOfDeathInformation(value_text);
                    break;*/
                default:
                    break;
            }

        }
        return hiv;
    }
}
