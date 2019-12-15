/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import static com.inductivehealth.ndr.client.Client.getXmlDate;
import com.inductivehealth.ndr.schema.CommonQuestionsType;
import java.util.Date;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import model.datapump.Obs;

/**
 *
 * @author The Bright
 */
public class NDRHIVCommonQuestionsDictionary {
    
     public NDRHIVCommonQuestionsDictionary(){
         
     }
     public CommonQuestionsType createCommonQuestionType(String hospID, List<Obs> obsList, Date firstVisitDate, Date lastVisitDate, boolean patientdiedfromillness, int age, String gender) throws DatatypeConfigurationException {
        CommonQuestionsType common = new CommonQuestionsType();
        common.setPatientDieFromThisIllness(patientdiedfromillness);
        common.setPatientAge(age);
        common.setHospitalNumber(hospID);
        if (firstVisitDate != null) {
            common.setDateOfFirstReport(getXmlDate(firstVisitDate));
        }
        if (lastVisitDate != null) {
            common.setDateOfLastReport(getXmlDate(lastVisitDate));
        }
        int value_coded = 0;
        Date value_datetime = null;
        int conceptID = 0;
        for (Obs obs : obsList) {
            conceptID = obs.getConceptID();
            switch (conceptID) {
                case 165050:
                    value_coded = obs.getValueCoded();
                    if (gender.equals("F")) {
                        switch (value_coded) {
                            case 165048:
                                common.setPatientPregnancyStatusCode("P");
                                break;
                            case 165047:
                                common.setPatientPregnancyStatusCode("NP");
                                break;
                            /*case 13:
                                common.setPatientPregnancyStatusCode("NK");
                                break;*/
                            case 165049:
                                common.setPatientPregnancyStatusCode("PMTCT");
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                case 5596:
                    value_datetime = obs.getValueDate();
                    if (gender.equals("F")) {
                        common.setEstimatedDeliveryDate(getXmlDate(value_datetime));
                    }
                    break;
                case 160554:
                    value_datetime = obs.getValueDate();
                    common.setDiagnosisDate(getXmlDate(value_datetime));
                    break;
                case 165470:
                    value_coded = obs.getValueCoded();
                    if (value_coded == 165889) {
                        common.setPatientDieFromThisIllness(true);
                    }
                    break;

                default:
                    break;
            }
        }
        return common;
    }
}
