/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import static com.inductivehealth.ndr.client.Client.getXmlDate;
import com.inductivehealth.ndr.schema.AnswerType;
import com.inductivehealth.ndr.schema.CodedSimpleType;
import com.inductivehealth.ndr.schema.CodedType;
import com.inductivehealth.ndr.schema.LaboratoryOrderAndResult;
import com.inductivehealth.ndr.schema.LaboratoryReportType;
import com.inductivehealth.ndr.schema.NumericType;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.datatype.DatatypeConfigurationException;
import model.datapump.Concept;
import model.datapump.Obs;
import model.datapump.Visit;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import util.NDRCommonUtills;

/**
 *
 * @author The Bright
 */
public class NDRLabDictionary {
    private Map<Integer,String> labTestDictionary=new HashMap<Integer,String>();
    private Map<Integer,String> labTestUnits=new HashMap<Integer,String>();
    private Map<String,String> labTestUnitDescription = new HashMap<String, String>();
    private Map<Integer, model.datapump.Concept> conceptDictionary;
    public NDRLabDictionary(){
        loadDictionary();
    }
    private void loadDictionary(){
        loadLabTestDictionary();
        loadLabTestUnitDictionary();
        loadLabTestUnitDescription();
    }
    public void setConceptDictionary(Map<Integer, model.datapump.Concept> conceptDictionaryRL){
        this.conceptDictionary=conceptDictionaryRL;
    }
    private void loadLabTestUnitDescription() {
        //labTestUnitDescription = new HashMap<String, String>();
        labTestUnitDescription.put("48", "CellsPerMicroLiter,cell/ul");
        labTestUnitDescription.put("60", "CopiesPerMilliLiter,copies/ml");
        labTestUnitDescription.put("398", "percent,%");
        labTestUnitDescription.put("311", "MilliMolesPerLiter,mmol/L");
        labTestUnitDescription.put("25", "BillionPerLiter,10*9/L");
        labTestUnitDescription.put("257", "MicroMole,umol");
        labTestUnitDescription.put("136", "GramsPerDeciLiter,g/dL");
        labTestUnitDescription.put("93", "GramsPerDeciLiter,U/L");
    }
    private void loadLabTestUnitDictionary() {
        labTestUnits.put(88, "48");
        labTestUnits.put(315, "60");
        labTestUnits.put(1153, "398");
        labTestUnits.put(365, "311");
        labTestUnits.put(331, "25");
        labTestUnits.put(1717, "48");
        labTestUnits.put(1168, "398");
        labTestUnits.put(366, "311");
        labTestUnits.put(1169, "48");
        labTestUnits.put(1718, "398");
        labTestUnits.put(367, "311");
        labTestUnits.put(1716, "48");
        labTestUnits.put(1170, "398");
        labTestUnits.put(1531, "311");
        labTestUnits.put(1719, "48");
        labTestUnits.put(1156, "398");
        labTestUnits.put(451, "257");
        labTestUnits.put(1150, "48");
        labTestUnits.put(7777906, "398");
        labTestUnits.put(1528, "257");
        labTestUnits.put(7777907, "398");
        labTestUnits.put(228, "136");
        labTestUnits.put(1529, "93");
        labTestUnits.put(375, "25");
        labTestUnits.put(1175, "311");
        labTestUnits.put(1159, "311");
        labTestUnits.put(313, "257");
        labTestUnits.put(308, "93");
        labTestUnits.put(309, "93");
        labTestUnits.put(1176, "311");
        labTestUnits.put(1530, "93");
        labTestUnits.put(329, "398");
        labTestUnits.put(332, "25");
    }

    public LaboratoryReportType createLaboratoryReportType(Visit visit, ArrayList<Obs> obsList, Date artStartDate) throws DatatypeConfigurationException {
        LaboratoryReportType labReport = null;
        LaboratoryOrderAndResult result = null;
        Date orderedDate = null, reportedDate = null;
        int value_coded = 0;
        int conceptID = 0;
        int value_numeric = 0;
        int dataType = 0;
        Concept c = null;
        AnswerType answer = null;
        NumericType numeric = null;
        CodedSimpleType cst = null;
        labReport = new LaboratoryReportType();
        Date visitDate = visit.getVisitDate();
        String providerName = obsList.get(0).getProvider();
        labReport.setVisitID(visit.getVisitID());
        labReport.setVisitDate(getXmlDate(visitDate));
        //labReport.setReportedBy(providerName);
        labReport.setClinician(providerName);
        labReport.setCollectionDate(getXmlDate(visitDate));
        //Obs labIDObs = extractConcept(7777905, obsList);
        //if (labIDObs != null) {
        //   labReport.setLaboratoryTestIdentifier(labIDObs.getVariableValue());
        //}
        labReport.setLaboratoryTestIdentifier(visit.getVisitID());
        Obs reportedByObs = NDRCommonUtills.extractConcept(164982, obsList);
        if (reportedByObs != null) {
            labReport.setReportedBy(reportedByObs.getVariableValue());
        }
        Obs checkedByObs = NDRCommonUtills.extractConcept(164983, obsList);
        if (checkedByObs != null) {
            labReport.setCheckedBy(checkedByObs.getVariableValue());
        }
        Obs baselineFlag = NDRCommonUtills.extractConcept(164181, obsList);
        if (baselineFlag != null) {
            value_coded = baselineFlag.getValueCoded();
            if (value_coded == 164180) {
                labReport.setBaselineRepeatCode("B");
            } else if (value_coded == 160530) {
                labReport.setBaselineRepeatCode("R");
            }
        }
        Obs obsOrderedDate = NDRCommonUtills.extractConcept(164989, obsList);
        if (obsOrderedDate != null) {
            orderedDate = obsOrderedDate.getValueDate();
        }
        Obs obsReportedDate = NDRCommonUtills.extractConcept(165414, obsList);
        if (obsReportedDate != null) {
            reportedDate = obsReportedDate.getValueDate();
        }
        DateTime d1, d2;
        String artStatus = null;
        if (artStartDate != null) {
            d1 = new DateTime(artStartDate);
            d2 = new DateTime(visitDate);
            if (d1.isEqual(d2) || d1.isBefore(d2)) {
                artStatus = "A";
            } else if (d1.isAfter(d2)) {
                artStatus = "N";
            }
            labReport.setARTStatusCode(artStatus);

        }
        for (Obs obs : obsList) {
            conceptID = obs.getConceptID();
            c = conceptDictionary.get(conceptID);
            dataType = c.getDataType();
            if (dataType == 1 && labTestDictionary.containsKey(conceptID)) {
                cst = new CodedSimpleType();
                cst.setCode(labTestDictionary.get(conceptID));
                cst.setCodeDescTxt(c.getConceptName());
                result = new LaboratoryOrderAndResult();
                result.setLaboratoryResultedTest(cst);
                answer = new AnswerType();
                numeric = new NumericType();
                if (orderedDate != null) {
                    result.setOrderedTestDate(getXmlDate(orderedDate));
                }
                if (reportedDate != null) {
                    result.setResultedTestDate(getXmlDate(reportedDate));
                }
                numeric.setValue1((int) Math.round(obs.getValueNumeric()));
                if (labTestUnits.containsKey(conceptID)) {
                    String[] descriptionText = null;
                    CodedType ct = new CodedType();
                    ct.setCode(labTestUnits.get(conceptID));
                    descriptionText = StringUtils.split(labTestUnitDescription.get(ct.getCode()), ",");
                    if (descriptionText != null) {
                        ct.setCodeDescTxt(descriptionText[0]);
                        ct.setCodeSystemCode(StringEscapeUtils.escapeXml10(descriptionText[1]));
                    }
                    numeric.setUnit(ct);
                }
                answer.setAnswerNumeric(numeric);
                result.setLaboratoryResult(answer);
                labReport.getLaboratoryOrderAndResult().add(result);
            } else if (dataType == 2 && labTestDictionary.containsKey(conceptID)) {
                cst = new CodedSimpleType();
                value_coded = obs.getValueCoded();
                if (conceptDictionary.containsKey(value_coded)) {
                    cst.setCode(labTestDictionary.get(conceptID));
                    cst.setCodeDescTxt(c.getConceptName());
                    result = new LaboratoryOrderAndResult();
                    result.setLaboratoryResultedTest(cst);
                    answer = new AnswerType();
                    CodedType ct = new CodedType();
                    ct.setCode(conceptDictionary.get(value_coded).getConceptName());
                    ct.setCodeDescTxt(conceptDictionary.get(value_coded).getConceptName());
                    ct.setCodeSystemCode(conceptDictionary.get(value_coded).getConceptName());
                    answer.setAnswerCode(ct);
                    result.setLaboratoryResult(answer);
                    if (orderedDate != null) {
                        result.setOrderedTestDate(getXmlDate(orderedDate));
                    }
                    if (reportedDate != null) {
                        result.setResultedTestDate(getXmlDate(reportedDate));
                    }
                    labReport.getLaboratoryOrderAndResult().add(result);
                }
            }

        }
        if (labReport.getLaboratoryOrderAndResult().isEmpty()) {
            labReport = null;
        }
        return labReport;

    }
    public void loadLabTestDictionary() {
        labTestDictionary = new HashMap<Integer, String>();

        labTestDictionary.put(165923, "2");//ALT/SGPT
        //labTestDictionary.put(1529, "3");OpenMRS
        //labTestDictionary.put(1531, "8");OpenMRS
        labTestDictionary.put(654, "4");//AST
        labTestDictionary.put(655, "7");//T.Bilirubin
        labTestDictionary.put(5497, "11");//CD4 COUNT
        labTestDictionary.put(1319, "12");//LYMPHOCYTES
        labTestDictionary.put(1022, "13");//Neutrophils
        labTestDictionary.put(1134, "16");//Serum Chloride
        labTestDictionary.put(1006, "17");//Total Cholesterol
        labTestDictionary.put(1007, "18");//HDL
        labTestDictionary.put(1008, "19");//LDL
        labTestDictionary.put(164364, "21");//Creatinine
        labTestDictionary.put(160053, "31");//Serum Glucose
        //labTestDictionary.put(805, "32");//Urine glucose dipstick
        labTestDictionary.put(165920, "34");//HB/PCV
        labTestDictionary.put(165395, "35");//HB (PCV)%
        //labTestDictionary.put(391, "35");//HIV Test
        labTestDictionary.put(159430, "42");//Hepatitis B Test - Qualitative
        labTestDictionary.put(1325, "43");//Hepatitis C Test - Qualitative
        //labTestDictionary.put(338, "53"); //HIV DNA Polymerase Chain Reaction, Qualitative
        labTestDictionary.put(785, "54");//Alk.Phosphatase
        labTestDictionary.put(1133, "57");//Serum Potassium
        labTestDictionary.put(45, "58");//Urine Pregnancy Test
        labTestDictionary.put(165926, "59");//Total Protein
        labTestDictionary.put(1132, "65");//Soduim
        labTestDictionary.put(361, "71");//RPR
        labTestDictionary.put(1009, "74");//Triglycerides
        labTestDictionary.put(856, "80");//Viral Load
        labTestDictionary.put(678, "81");//WBC
        //labTestDictionary.put(391, "44");//HIV Test
        // labTestDictionary.put(451, "31");

    }

     

}
