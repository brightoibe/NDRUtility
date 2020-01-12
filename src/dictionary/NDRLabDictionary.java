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
        labTestUnits.put(88, "48");//CD4 COUNT
        labTestUnits.put(315, "60");// Viral Load
        labTestUnits.put(1153, "398"); // CD4 %
        labTestUnits.put(365, "311");// Na+
        labTestUnits.put(331, "25");// WBC
        labTestUnits.put(1717, "48");// Lymphocytes
        labTestUnits.put(1168, "398");// Lymphocytes %
        labTestUnits.put(366, "311");// Serum Chloride
        labTestUnits.put(1169, "48");// Monocytes
        labTestUnits.put(1718, "398");// Monocytes %
        labTestUnits.put(367, "311");// HC03 Serum Carbon Dioxide
        labTestUnits.put(1716, "48");// Neutrophils
        labTestUnits.put(1170, "398");// Neutrophils %
        labTestUnits.put(1531, "311");// Urea
        labTestUnits.put(1719, "48");// Eosinophils
        labTestUnits.put(1156, "398");//Eosinophils %
        labTestUnits.put(451, "257");//Serum Glucose
        labTestUnits.put(1150, "48");//Basophils
        labTestUnits.put(7777906, "398");//Basophils %
        labTestUnits.put(1528, "257");//T.Bilirubin
        labTestUnits.put(7777907, "398");//Hb(PCV)%
        labTestUnits.put(228, "136");//Hemoglobin(HB/PCV)
        labTestUnits.put(1529, "93"); // Amylase
        labTestUnits.put(375, "25"); // Platelets count
        labTestUnits.put(1175, "311"); // Total Cholesterol
        labTestUnits.put(1159, "311"); // HDL
        labTestUnits.put(313, "257");// Creatinine
        labTestUnits.put(308, "93"); // AST
        labTestUnits.put(309, "93"); // ALT/SGPT
        labTestUnits.put(1176, "311");// Triglycerides
        labTestUnits.put(1530, "93");// Alk.Phosphatase
        //labTestUnits.put(329, "398");// 
        labTestUnits.put(332, "25");//RBC
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
        Obs sampleCollectionDateObs=NDRCommonUtills.extractConcept(99, obsList);//Specimen collection date
        if(sampleCollectionDateObs!=null){
            labReport.setCollectionDate(getXmlDate(sampleCollectionDateObs.getValueDate()));
        }else{
            labReport.setCollectionDate(getXmlDate(visitDate));
        }
        
        //Obs labIDObs = extractConcept(7777905, obsList);
        //if (labIDObs != null) {
        //   labReport.setLaboratoryTestIdentifier(labIDObs.getVariableValue());
        //}
        labReport.setLaboratoryTestIdentifier(visit.getVisitID());
        Obs reportedByObs = NDRCommonUtills.extractConcept(7777905, obsList);//Laboratory Registration No
        if (reportedByObs != null) {
            labReport.setReportedBy(reportedByObs.getVariableValue());
        }
        Obs checkedByObs = NDRCommonUtills.extractConcept(7778306, obsList);// Checked by
        if (checkedByObs != null) {
            labReport.setCheckedBy(checkedByObs.getVariableValue());
        }
        Obs baselineFlag = NDRCommonUtills.extractConcept(1537, obsList);// Visit Type
        if (baselineFlag != null) {
            value_coded = baselineFlag.getValueCoded();
            if (value_coded == 1535) {// Initial
                labReport.setBaselineRepeatCode("B");
            } else if (value_coded == 1536) { // Follow-up
                labReport.setBaselineRepeatCode("R");
            }
        }
        /*Obs obsOrderedDate = NDRCommonUtills.extractConcept(164989, obsList);
        if (obsOrderedDate != null) {
            orderedDate = obsOrderedDate.getValueDate();
        }*/
        orderedDate=visitDate; // Ordered Date does not exist as a concept
        
        /*Obs obsReportedDate = NDRCommonUtills.extractConcept(165414, obsList);
        if (obsReportedDate != null) {
            reportedDate = obsReportedDate.getValueDate();
        }*/
        reportedDate=visitDate;// Reported Date same as Visit Date does not exist as a conccept
        
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

        labTestDictionary.put(309, "2");//ALT/SGPT
        //labTestDictionary.put(1529, "3");OpenMRS
        //labTestDictionary.put(1531, "8");OpenMRS
        labTestDictionary.put(308, "4");//AST
        labTestDictionary.put(1528, "7");//T.Bilirubin
        labTestDictionary.put(88, "11");//CD4 COUNT
        labTestDictionary.put(1717, "12");//LYMPHOCYTES
        labTestDictionary.put(1716, "13");//Neutrophils
        labTestDictionary.put(366, "16");//Serum Chloride
        labTestDictionary.put(1175, "17");//Total Cholesterol
        labTestDictionary.put(1159, "18");//HDL
        labTestDictionary.put(1167, "19");//LDL
        labTestDictionary.put(313, "21");//Creatinine
        labTestDictionary.put(317, "31");//Serum Glucose
        //labTestDictionary.put(805, "32");//Urine glucose dipstick
        labTestDictionary.put(228, "34");//HB/PCV
        labTestDictionary.put(7777907, "35");//HB (PCV)%
        //labTestDictionary.put(391, "35");//HIV Test
        labTestDictionary.put(1157, "42");//Hepatitis B Test - Qualitative
        labTestDictionary.put(1158, "43");//Hepatitis C Test - Qualitative
        //labTestDictionary.put(338, "53"); //HIV DNA Polymerase Chain Reaction, Qualitative
        labTestDictionary.put(1530, "54");//Alk.Phosphatase
        labTestDictionary.put(368, "57");//Serum Potassium
        //labTestDictionary.put(45, "58");//Urine Pregnancy Test
        //labTestDictionary.put(165926, "59");//Total Protein
        labTestDictionary.put(365, "65");//Soduim
        //labTestDictionary.put(361, "71");//RPR
        labTestDictionary.put(1176, "74");//Triglycerides
        labTestDictionary.put(315, "80");//Viral Load
        labTestDictionary.put(331, "81");//WBC
        //labTestDictionary.put(391, "44");//HIV Test
        // labTestDictionary.put(451, "31");

    }

     

}
