/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import static com.inductivehealth.ndr.client.Client.getXmlDate;
import com.inductivehealth.ndr.schema.CodedSimpleType;
import com.inductivehealth.ndr.schema.RegimenType;
import static daos.DataPumpDao.sortRegimen;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.datatype.DatatypeConfigurationException;
import model.datapump.Demographics;
import model.datapump.Obs;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Weeks;
import util.NDRCommonUtills;

/**
 *
 * @author The Bright
 */
public class NDRPharmacyDictionary {

    private Map<String, String> mapRegimenToCodeDictionary = new HashMap<String, String>();
    private Map<String, String> localDrugCodeMapping = new HashMap<String, String>();
    private Map<String, String> arvDrugCodingMapping = new HashMap<String, String>();
    public Map<Integer, String> ndrCodedValues = new HashMap<Integer, String>();
    private final static String COTRIMOXAZOLE_REGIMEN_TYPE_CODE = "CTX";
    private final static String INH_REGIMEN_TYPE_CODE = "TB";

    public NDRPharmacyDictionary() {
        loadDictionaries();
    }

    private void loadDictionaries() {
        loadNDRCodedValues();
        loadLocalCodingDictionary();
        loadRegimenToCodeDictionary();
        loadARVDrugCoding();
    }

    private void loadNDRCodedValues() {
        //ndrCodedValues.put(102, "1");
        ndrCodedValues.put(102, "1");
        ndrCodedValues.put(165048, "2");
        ndrCodedValues.put(160559, "3");
        ndrCodedValues.put(160567, "4");
        ndrCodedValues.put(160561, "5");
        ndrCodedValues.put(159834, "6");
        ndrCodedValues.put(163523, "7");
        ndrCodedValues.put(160566, "8");
        ndrCodedValues.put(160569, "9");
        ndrCodedValues.put(165303, "ART");
        ndrCodedValues.put(165941, "NONART");
        //ndrCodedValues.put(	,CTX);
        ndrCodedValues.put(165942, "PEP");
        ndrCodedValues.put(165943, "PEPNONOCC");
        ndrCodedValues.put(165685, "PMTCT");
        //Regimen Concepts
        ndrCodedValues.put(7777815, "1k");//	TDF/3TC 
        ndrCodedValues.put(7777816, "2g");//	FTC/ATV/r 
        ndrCodedValues.put(7777817, "2g");//	LPV/r 
        ndrCodedValues.put(7777818, "2f");//	AZT/3TC/ATVr 
        ndrCodedValues.put(7777811, "1a");//	AZT/3TC/EFV 
        ndrCodedValues.put(7777810, "1b");//	AZT/3TC/NVP 
        ndrCodedValues.put(1227, "1e");//	TDF/3TC/EFV 
        ndrCodedValues.put(7777812, "1f");//	TDF/3TC/NVP 
        ndrCodedValues.put(7777813, "1g");//	AZT/3TC/ABC 
        ndrCodedValues.put(7777814, "1h");//	AZT/3TC/TDF 
        ndrCodedValues.put(7777880, "5b");//	AZT/3TC/LPVr
        ndrCodedValues.put(7777916, "4d");//	ABC/3TC/NVP
        ndrCodedValues.put(7777922, "5d");//	d4T/3TC/NVP
        ndrCodedValues.put(7777919, "5a");//	ABC/3TC/LPV/r 
        ndrCodedValues.put(7777918, "5d");//	ddi/3TC/NVP 
        ndrCodedValues.put(7777920, "5e");//	ABC/3TC/ddi 
        ndrCodedValues.put(7777921, "5c");//	d4T/3TC/LPV/r 

        // Adult 1st-Line Regimens (7778706)
        ndrCodedValues.put(7778704, "1m");//TDF-3TC-DTG 
        ndrCodedValues.put(7778705, "1e");//TDF-3TC-EFV400 
        ndrCodedValues.put(7778609, "1a");//AZT-3TC-EFV duplicate
        ndrCodedValues.put(7778610, "1b");//AZT-3TC-NVP duplicate
        ndrCodedValues.put(7778611, "1e");//TDF-3TC-EFV 
        // Adult 2nd-Line Regimens (7778707)
        ndrCodedValues.put(7778616, "2f");//AZT-3TC-ATVr 
        ndrCodedValues.put(7778709, "6e");//AZT-3TC-DRVr 
        ndrCodedValues.put(7778617, "2g");//AZT-TDF-3TC-LPVr 
        ndrCodedValues.put(7778614, "2d");//TDF-3TC-ATVr 
        ndrCodedValues.put(7778708, "6g");//TDF-3TC-DRVr 
        ndrCodedValues.put(7778613, "2b");//TDF-3TC-LPVr 
        //Adult 3rd-Line Regimens (7778838)
        ndrCodedValues.put(7778710, "6c");//	 DRVr + DTG +/- 1-2 NRTIs 
        ndrCodedValues.put(7778712, "6a");//	 DRVr-2NTRIs+/-NNRTI 
        ndrCodedValues.put(7778749, "6d");//	 DRVr-RAL-/+1-2NRTIs
        //Peadiatric 1st-Line Regimens (7778731)
        ndrCodedValues.put(7778732, "4d");//	ABC-3TC-NVP 
        ndrCodedValues.put(7778733, "1f");//	TDF-3TC-NVP 
        ndrCodedValues.put(7778734, "1o");//	ABC-3TC-DTG 
        ndrCodedValues.put(7778735, "5a");//	ABC-3TC-LPVr 
        ndrCodedValues.put(7778615, "5b");//	AZT-3TC-LPVr 
        //Peadiatric 2nd-Line Regimen (7778741)
        ndrCodedValues.put(7778736, "5k");//TDF-3TC-RAL 
        ndrCodedValues.put(7778738, "5i");//AZT-3TC-RAL 
        ndrCodedValues.put(7778836, "5a");//ABC-3TC-LPV/r 
        //Peadiatric 3rd-Line Regimens (7778742)
        ndrCodedValues.put(7778739, "6k");//DTG+2NRTIs
        ndrCodedValues.put(7778740, "6k");//DRVr+2NRTIs

    }

    public void loadARVDrugCoding() {
        arvDrugCodingMapping.put("NEVIRAPINE", "NVP");
        arvDrugCodingMapping.put("LAMIVUDINE", "3TC");
        arvDrugCodingMapping.put("ABACAVIR", "ABC");
        arvDrugCodingMapping.put("ZIDOVUDINE", "AZT");
        arvDrugCodingMapping.put("INDINAVIR", "IDVr");
        arvDrugCodingMapping.put("NELFINAVIR", "NFVr");
        arvDrugCodingMapping.put("RITONAVIR", "r");
        arvDrugCodingMapping.put("EFAVIRENZ", "EFV");
        arvDrugCodingMapping.put("TENOFOVIR", "TDF");
        arvDrugCodingMapping.put("ATAZANAVIR", "ATVr");
        arvDrugCodingMapping.put("KALETRA", "LPVr");
        arvDrugCodingMapping.put("LOPINAVIR", "LPVr");
        arvDrugCodingMapping.put("EMTRICITABINE", "FTC");
        arvDrugCodingMapping.put("DIDANOSINE", "DDI");
        arvDrugCodingMapping.put("STAVUDINE", "D4T");
        arvDrugCodingMapping.put("SAQUINAVIR", "SQVr");
        arvDrugCodingMapping.put("Dolutegravir", "DTG");

    }

    public void loadLocalCodingDictionary() {
        localDrugCodeMapping = new HashMap<String, String>();
        localDrugCodeMapping.put("ACTION MEAL PRESCRIBED", "ACM");
        localDrugCodeMapping.put("ACYCLOVIR", "ACY");
        localDrugCodeMapping.put("ALBENDAZOLE", "ALB");
        localDrugCodeMapping.put("AMOXICILLIN", "AMX");
        localDrugCodeMapping.put("AMOXICILLIN AND CLAVULANIC ACID", "AMX");
        localDrugCodeMapping.put("AMITRIPTYLINE", "AMT");
        localDrugCodeMapping.put("ARTEMETER", "ATM");
        localDrugCodeMapping.put("ARTEMETHER-LUMEFANTRINE", "ALU");
        localDrugCodeMapping.put("AZITHROMYCIN", "AZI");
        localDrugCodeMapping.put("PENICILLIN G, BENZATHINE", "BEP");
        localDrugCodeMapping.put("PENICILLIN G BENZATHINE / PENICILLIN G PROCAINE", "BEP");//
        localDrugCodeMapping.put("BENZOIC ACID/SALICYLIC ACID CREAM", "BES");
        localDrugCodeMapping.put("CEFTRIAXONE IV", "CFT");
        localDrugCodeMapping.put("CEFTRIAXONE", "CFT");//
        localDrugCodeMapping.put("CHLORPHENIRAMINE MALEATE", "CHP");
        localDrugCodeMapping.put("CIPROFLOXACIN", "CPF");//
        localDrugCodeMapping.put("CLOTRIMAZOLE VAG. PRESSARY", "CLX");
        localDrugCodeMapping.put("CTX", "CTX960");
        localDrugCodeMapping.put("CTX prophylaxis", "CTX960");
        //localCodeMapping.put("CTX", "CTX240");
        //localCodeMapping.put("Cotrimoxazole (TMP/SMX) Tablet", "CTX960");
        localDrugCodeMapping.put("DAPSONE", "DDS");// DOXYCYCLINE
        localDrugCodeMapping.put("DOXYCYCLINE", "DXY");// 
        localDrugCodeMapping.put("ETHAMBUTOL", "E");// 
        localDrugCodeMapping.put("Erythromycin", "ERN");
        localDrugCodeMapping.put("FANSIDAR", "FAN");
        localDrugCodeMapping.put("FERROUS GLUCONATE", "FEG");
        localDrugCodeMapping.put("FERROUS SULPHATE", "FES");
        localDrugCodeMapping.put("FERROUS SULFATE", "FES");
        localDrugCodeMapping.put("FLUCONAZOLE", "FLUC");//
        localDrugCodeMapping.put("FLUOXETINE", "FLUX");
        localDrugCodeMapping.put("FOLIC ACID", "FOA");
        localDrugCodeMapping.put("HYDROCORTISONE ACETATE", "HYA");
        localDrugCodeMapping.put("HYDROCORTISONE", "HYA");
        localDrugCodeMapping.put("IBUPROFEN", "IBP");
        localDrugCodeMapping.put("INH", "H");
        localDrugCodeMapping.put("INH/B6", "H");//ISONIAZID PROPHYLAXIS
        localDrugCodeMapping.put("ISONIAZID PROPHYLAXIS", "H");//
        localDrugCodeMapping.put("LOPERAMIDE", "LOP");
        //localCodeMapping.put("LORATADINE", "LRS");
        localDrugCodeMapping.put("LORATADINE", "LOR");
        localDrugCodeMapping.put("METOCLOPRAMIDE", "MEP");
        localDrugCodeMapping.put("METRONIDAZOLE", "MET");
        localDrugCodeMapping.put("MULTIVITAMIN", "MUV");
        // localDrugCodeMapping.put("Multivitamin Drops", "MUV");
        //localCodeMapping.put("Multivitamin Syrup", "MUV");
        // localDrugCodeMapping.put("Multivite Tab", "MUV");
        localDrugCodeMapping.put("NYSTATIN SOLUTION", "NYS");
        localDrugCodeMapping.put("NYSTATIN", "NYS");//
        localDrugCodeMapping.put("ORAL REHYDRATION SALT", "ORS");
        localDrugCodeMapping.put("Oral Nystatin", "ORN");//
        localDrugCodeMapping.put("OFLOXACIN", "OFL");//
        localDrugCodeMapping.put("PROMETHAZINE / PSEUDOEPHEDRINE", "PRM");
        localDrugCodeMapping.put("PROMETHAZINE", "PRM");
        //localCodeMapping.put("Pyridoxine (Vitamin B6)", "PYR");
        localDrugCodeMapping.put("PYRIZINAMIDE", "Z");
        localDrugCodeMapping.put("RIFAMPICIN", "R");
        localDrugCodeMapping.put("SULFADOXINE/PYRIMETHAMINE", "SXP");
        localDrugCodeMapping.put("SULFADOXINE AND PYRIMETHAMINE", "SXP");
        localDrugCodeMapping.put("SPECTINOMYCIN", "S");//
        localDrugCodeMapping.put("STREPTOMYCIN INJECTION", "SI");
        localDrugCodeMapping.put("TRAMADOL", "TMD");
        localDrugCodeMapping.put("TRAMADOL HYDROCHLORIDE", "TMD");
        localDrugCodeMapping.put("TINIDAZOLE", "TND");
        localDrugCodeMapping.put("TETRACYCLINE", "TCL");
        localDrugCodeMapping.put("RIFAMPICIN AND ISONIAZID", "HR");
        localDrugCodeMapping.put("RIFAMPICIN ISONIAZID PYRAZINAMIDE AND ETHAMBUTOL ", "HRZE");

    }

    public boolean isEquivalent(String openmrsRegimen, String careCardRegimen) {
        boolean ans = false;
        String[] arr1 = null, arr2 = null;
        if (openmrsRegimen != null && careCardRegimen != null) {
            arr1 = openmrsRegimen.toUpperCase().split("/");
            careCardRegimen = StringUtils.replacePattern(careCardRegimen, "\\/r", "r");
            arr2 = careCardRegimen.toUpperCase().split("-");
            Arrays.sort(arr1);
            Arrays.sort(arr2);
        }
        return Arrays.equals(arr1, arr2);
    }
    public static void main(String[] arg){
        NDRPharmacyDictionary dic=new NDRPharmacyDictionary();
        System.out.println(dic.getRegimenCoding("TDF/3TC/EFV (300mg/300mg/600mg)/Cotrimoxazole (960mg)/Isoniazid INH (300mg)"));
    }
    public  String getRegimenCoding(String drugName){
        String codedDrugs=convertDrugToRegimenCode(drugName);
        String regimenLine=getRegimenLineCoding(codedDrugs);
        return getRegimenCode(codedDrugs, regimenLine);
    }
    public String getCareCardRegimen(String openmrsRegimen) {
        String regimen = null;
        Set<String> set = mapRegimenToCodeDictionary.keySet();
        for (String ele : set) {
            if (isEquivalent(openmrsRegimen, ele)) {
                regimen = ele;
                return regimen;
            }
        }
        return regimen;
    }

    public String codeDrugs(String drugName) {
        drugName = drugName.toUpperCase();
        String codedName = drugName;
        Set<String> keySet = null;
        if (drugName != null && !drugName.isEmpty()) {
            keySet = arvDrugCodingMapping.keySet();
            for (String key : keySet) {
                codedName = StringUtils.replacePattern(codedName, key, arvDrugCodingMapping.get(key));
            }
            /*if (drugName.contains("NEVIRAPINE")) {
                codedName += "NVP/";
            }
            if (drugName.contains("LAMIVUDINE")) {
                codedName += "3TC/";
            }
            if (drugName.contains("ZIDOVUDINE")) {
                codedName += "AZT/";
            }
            if (drugName.contains("EMTRICITABINE")) {
                codedName += "FTC/";
            }
            if (drugName.contains("TENOFOVIR")) {
                codedName += "TDF/";
            }
            if (drugName.contains("EFAVIRENZ")) {
                codedName += "EFV/";
            }
            if (drugName.contains("ABACAVIR")) {
                codedName += "ABC/";
            }
            if (drugName.contains("ATAZANAVIR")) {
                codedName += "ATVr/";
            }
            if (drugName.contains("KALETRA") || drugName.contains("LOPINAVIR")) {
                codedName += "LPVr/";
            }
            if (drugName.contains("DIDANOSINE")) {
                codedName += "DDI/";
            }
            if (drugName.contains("STAVUDINE")) {
                codedName += "D4T/";
            }
            if (drugName.contains("SAQUINAVIR")) {
                codedName += "SQVr/";
            }
            if (drugName.contains("INDINAVIR")) {
                codedName += "IDVr/";
            }
            if (drugName.contains("NELFINAVIR")) {
                codedName += "NFVr/";
            }*/
            if (codedName.endsWith("/")) {
                codedName = codedName.substring(0, codedName.length() - 1);

            }

        }
        codedName = StringUtils.replacePattern(codedName, "\\s+", "");
        return sortRegimen(codedName);
    }

    public boolean isPresent(String regimenList, String drugCode) {
        boolean ans = false;
        String[] drugs = regimenList.split("/");
        if (containsAll(drugs, drugCode)) {
            ans = true;
        }
        return ans;
    }

    public String getRegimenLineCoding(String codedDrug) {
        String line = "";
        if (codedDrug.contains("LPVr") || codedDrug.contains("SQVr") || codedDrug.contains("IDVr") || codedDrug.contains("DDI") || codedDrug.contains("ATVr")) {
            line = "20";
        } else {
            line = "10";
        }
        return line;
    }

    public boolean containsAll(String[] darr, String codedDrugNames) {
        boolean ans = true;
        for (String ele : darr) {
            if (!codedDrugNames.contains(ele)) {
                ans = false;
            }
        }
        return ans;

    }

    public static String sortRegimen(String code) {
        String[] codeArr = code.split("/");
        Arrays.sort(codeArr);
        return StringUtils.join(codeArr, "/");
    }

    public String convertDrugToRegimenCode(String drugName) {
        String regimenCoding = "";
        drugName = StringUtils.replacePattern(drugName, "\\/r", "r");
        drugName = StringUtils.trimToEmpty(StringUtils.replacePattern(drugName, "\\(.*?\\)", ""));
        regimenCoding = codeDrugs(drugName);
        return regimenCoding;
    }

    public String getRegimenCode(String regimen) {
        String code = null;
        Set<String> set = mapRegimenToCodeDictionary.keySet();
        for (String ele : set) {
            if (isEquivalent(regimen, ele)) {
                code = mapRegimenToCodeDictionary.get(ele);
                return code;
            }
        }
        return code;
    }
    public String getRegimenCode(String codedDrug, String regimenLineConcept) {
        String code = null;
        
        Set<String> set = mapRegimenToCodeDictionary.keySet();
        for (String ele : set) {
            if (isEquivalent(codedDrug, ele)) {
                code = mapRegimenToCodeDictionary.get(ele);
                return code;
            }
        }
        if (StringUtils.isEmpty(code) &&  StringUtils.equalsIgnoreCase(regimenLineConcept, "10")) {
            code = "1k";
        }
        if (StringUtils.isEmpty(code) && StringUtils.equalsIgnoreCase(regimenLineConcept, "20")) {
            code = "2g";
        }
        return code;
    }

    public String getRegimenCode(String regimen, int regimenLineConcept) {
        String code = null;
        int[] firstLineArr = {164506, 164507};
        int[] secondLineArr = {164513, 164514};
        Arrays.sort(firstLineArr);
        Arrays.sort(secondLineArr);

        Set<String> set = mapRegimenToCodeDictionary.keySet();
        for (String ele : set) {
            if (isEquivalent(regimen, ele)) {
                code = mapRegimenToCodeDictionary.get(ele);
                return code;
            }
        }
        if (StringUtils.isEmpty(code) && (Arrays.binarySearch(firstLineArr, regimenLineConcept) >= 0)) {
            code = "1k";
        }
        if (StringUtils.isEmpty(code) && (Arrays.binarySearch(secondLineArr, regimenLineConcept) >= 0)) {
            code = "2g";
        }
        return code;
    }

    public void loadRegimenToCodeDictionary() {
        //mapRegimenToCodeDictionary = new HashMap<String, String>();
        mapRegimenToCodeDictionary.put("3TC-D4T-EFV", "1k");
        mapRegimenToCodeDictionary.put("TDF-3TC-DTG", "1m");
        mapRegimenToCodeDictionary.put("TDF-FTC-DTG", "1n");
        mapRegimenToCodeDictionary.put("ABC-3TC-DTG", "1o");
        mapRegimenToCodeDictionary.put("ABC-FTC-DTG", "1p");
        mapRegimenToCodeDictionary.put("Other first line", "1k");
        mapRegimenToCodeDictionary.put("AZT-3TC-EFV", "1a");
        mapRegimenToCodeDictionary.put("AZT-3TC-NVP", "1b");
        mapRegimenToCodeDictionary.put("TDF-FTC-EFV", "1c");
        mapRegimenToCodeDictionary.put("TDF-FTC-NVP", "1d");
        mapRegimenToCodeDictionary.put("TDF-3TC-EFV", "1e");
        mapRegimenToCodeDictionary.put("TDF-3TC-NVP", "1f");
        mapRegimenToCodeDictionary.put("AZT-3TC-ABC", "1g");
        mapRegimenToCodeDictionary.put("AZT-3TC-TDF", "1h");
        mapRegimenToCodeDictionary.put("TDF-FTC-LPV/r", "2a");
        mapRegimenToCodeDictionary.put("TDF-3TC-LPVr", "2b");
        mapRegimenToCodeDictionary.put("TDF-FTC-ATVr", "2c");
        mapRegimenToCodeDictionary.put("TDF-3TC-ATVr", "2d");
        mapRegimenToCodeDictionary.put("AZT-3TC-LPVr", "2e");
        mapRegimenToCodeDictionary.put("AZT-3TC-ATVr", "2f");
        mapRegimenToCodeDictionary.put("AZT-3TC-EFV", "4a");
        mapRegimenToCodeDictionary.put("AZT-3TC-NVP", "4b");
        mapRegimenToCodeDictionary.put("ABC-3TC-EFV", "4c");
        mapRegimenToCodeDictionary.put("ABC-3TC-NVP", "4d");
        mapRegimenToCodeDictionary.put("AZT-3TC-ABC", "4e");
        mapRegimenToCodeDictionary.put("d4T-3TC-NVP", "4f");
        mapRegimenToCodeDictionary.put("ABC-3TC-LPVr", "5a");
        mapRegimenToCodeDictionary.put("AZT-3TC-LPVr", "5b");
        mapRegimenToCodeDictionary.put("d4T-3TC-LPVr", "5c");
        mapRegimenToCodeDictionary.put("ABC-3TC-ddi", "5e");
        mapRegimenToCodeDictionary.put("AZT", "9a");
        mapRegimenToCodeDictionary.put("3TC", "9b");
        mapRegimenToCodeDictionary.put("NVP", "9c");
        mapRegimenToCodeDictionary.put("AZT-3TC", "9d");
        mapRegimenToCodeDictionary.put("AZT-NVP", "9e");
        mapRegimenToCodeDictionary.put("FTC-TDF", "9f");
        mapRegimenToCodeDictionary.put("3TC-d4T", "9g");
        mapRegimenToCodeDictionary.put("3TC-d4T", "9h");
        mapRegimenToCodeDictionary.put("AZT-FTC-LPV/r-TDF", "2g");
        mapRegimenToCodeDictionary.put("3TC-AZT-LPV/r-TDF", "2g");
        mapRegimenToCodeDictionary.put("IDV/r-AZT-3TC", "2g");
        mapRegimenToCodeDictionary.put("IDV/r-AZT-FTC", "2g");
        mapRegimenToCodeDictionary.put("IDV/r-AZT-3TC", "2g");
        mapRegimenToCodeDictionary.put("IDV/r-3TC-D4T", "2g");
        mapRegimenToCodeDictionary.put("AZT-FTC-ATV/r", "2g");
        mapRegimenToCodeDictionary.put("AZT-FTC-LPV/r", "2g");
        mapRegimenToCodeDictionary.put("ABC-AZT-3TC-LPV/r", "2g");
        mapRegimenToCodeDictionary.put("3TC-ABC-AZT-ATV/r", "2g");
        mapRegimenToCodeDictionary.put("ATV/r-AZT-3TC-TDF", "2g");
        mapRegimenToCodeDictionary.put("ATV/r-AZT-FTC-TDF", "2g");
        mapRegimenToCodeDictionary.put("3TC-ABC-ATV/r", "2g");
        mapRegimenToCodeDictionary.put("3TC-D4T-SQV/r", "2g");
        mapRegimenToCodeDictionary.put("ABC-SQV/r-TDF", "2g");
        mapRegimenToCodeDictionary.put("ABC-DDI-LPV/r", "2g");
        mapRegimenToCodeDictionary.put("TDF-FTC-SQV/r", "2g");
        mapRegimenToCodeDictionary.put("3TC-AZT-SQV/r", "2g");
        mapRegimenToCodeDictionary.put("3TC-IDV/r-TDF", "2g");
        mapRegimenToCodeDictionary.put("FTC-IDV/r-TDF", "2g");
        mapRegimenToCodeDictionary.put("3TC-AZT-SQV/r-TDF", "2g");
        mapRegimenToCodeDictionary.put("3TC-SQV/r-TDF", "2g");
        mapRegimenToCodeDictionary.put("ABC-AZT-LPV/r", "2g");
        mapRegimenToCodeDictionary.put("DDI-IDV/r-TDF", "2g");
        mapRegimenToCodeDictionary.put("DDI-SQV/r-TDF", "2g");
        mapRegimenToCodeDictionary.put("Other second line", "2g");

    }

    public RegimenType createARVRegimenTypeFromObsList(ArrayList<Obs> obsListFromPharmacy, Date visitDate, String pepfarID) {
        RegimenType regimenType = null;
        String prescribedRegimenCode = "";
        Obs obsPin = null;
        int valueCoded = 0;
        CodedSimpleType cst = null;
        String visitID = NDRCommonUtills.formatDateYYYYMMDD(visitDate) + "-" + pepfarID;
        //Get the prescribed regimen line from the list 165708 (Current Regimen Line)
        obsPin = NDRCommonUtills.getObsForConceptForForm(165708, obsListFromPharmacy, visitDate);
        if (obsPin != null) {
            regimenType = new RegimenType();
            valueCoded = obsPin.getValueCoded();
            if (valueCoded == 164506 || valueCoded == 164507) {
                prescribedRegimenCode = "10";
                cst = new CodedSimpleType();
                cst.setCode(prescribedRegimenCode);
                cst.setCodeDescTxt(obsPin.getVariableValue());
            } else if (valueCoded == 164513 || valueCoded == 164514) {
                prescribedRegimenCode = "20";
                cst = new CodedSimpleType();
                cst.setCode(prescribedRegimenCode);
                cst.setCodeDescTxt(obsPin.getVariableValue());
            }

        }

        return regimenType;
    }

    public List<RegimenType> constructRegimenTypeList(Demographics pts, List<Obs> pharmacyObsList) throws DatatypeConfigurationException {
        List<RegimenType> regimenTypeList = new ArrayList<RegimenType>();
        Set<Date> visitSet = NDRCommonUtills.getAllVisitsFromObsList(pharmacyObsList);
        RegimenType regimenType = null;
        List<Obs> obsList = null;
        for (Date vstDate : visitSet) {
            obsList = NDRCommonUtills.getAllObsForDate(vstDate, pharmacyObsList);
            regimenTypeList.add(extractARVRegimens(obsList, pts, vstDate));
            regimenTypeList.addAll(extractOIRegimens(obsList, vstDate, pts));
        }
        return regimenTypeList;
    }

    public List<RegimenType> constructRegimenTypeListOIs(Demographics pts, List<model.datapump.DrugOrder> drugOrderList) throws DatatypeConfigurationException {
        List<RegimenType> regimenTypeList = new ArrayList<RegimenType>();
        RegimenType regimenType = null;

        /*
           RegimenType
           -VisitID
           -VisitDate
           -ReasonForRegimenSwitchSubs
           -PrescribedRegimen
           -PrescribedRegimenTypeCode
           -PrescribedRegimenLineCode
           -PrescribedRegimenDuration
           -PrescribedRegimenDispensedDate
           -DateRegimenStarted
           -DateRegimenStartedDD
           -DateRegimenStartedMM
           -DateRegimenStartedYYYY
           -DateRegimenEnded
           -DateRegimenEndedDD
           -DateRegimenEndedMM
           -DateRegimenEndedYYYY
           -PrescribedRegimenInitialIndicator
           -PrescribedRegimenCurrentIndicator
           -TypeOfPreviousExposureCode
           -PoorAdherenceIndicator
           -ReasonForPoorAdherence
           -ReasonRegimenEndedCode
           -SubstitutionIndicator
           -SwitchIndicator
           
           
         */
        for (model.datapump.DrugOrder order : drugOrderList) {
            if (order.getConceptID() == 963) {// Is this Cotrimoxazole
                regimenType = createRegimenTypeOI(order);
                regimenTypeList.add(regimenType);
            }
            if (order.getConceptID() == 1594) {// Is this INH
                regimenType = createRegimenTypeOI(order);
                regimenTypeList.add(regimenType);
            }
        }
        return regimenTypeList;
    }

    public Date calculateRegimenStopDate(Date visitDate, String frequency) {
        DateTime startDateTime = new DateTime(visitDate);
        int durationDays = 90;
        if (StringUtils.isNoneBlank(frequency)) {
            if (StringUtils.contains("7 days/week", frequency)) {
                durationDays = 7;
            } else if (StringUtils.contains("3 Months", frequency)) {
                durationDays = 90;
            } else if (StringUtils.contains("6 Months", frequency)) {
                durationDays = 180;
            } else if (StringUtils.contains("2 Months", frequency)) {
                durationDays = 60;
            } else if (StringUtils.contains("1 Month", frequency)) {
                durationDays = 30;
            } else if (StringUtils.contains("2 days/week", frequency)) {
                durationDays = 14;
            } else if (StringUtils.contains("1 Month1", frequency)) {
                durationDays = 30;
            }
        }
        DateTime endDateTime = startDateTime.plusDays(durationDays);
        return endDateTime.toDate();

        //DateTime endDateTime = new DateTime(stopDate);
    }

    public RegimenType createRegimenTypeOI(model.datapump.DrugOrder order) throws DatatypeConfigurationException {
        RegimenType regimenType = null;
        String regimenCoding = "", regimenTypeCoding = "", description = "";
        String pepfarID = "";
        Date visitDate = null, stopDate = null;
        if (order.getConceptID() == 963) {// COTRIMOXAZOLE CONCEPT ID
            regimenCoding = "CTX960";
            regimenTypeCoding = COTRIMOXAZOLE_REGIMEN_TYPE_CODE;
            visitDate = order.getStartDate();
            stopDate = order.getStopDate();
            pepfarID = order.getPepfarID();
            description = order.getDrugName();
            regimenType = createRegimenType(pepfarID, visitDate, regimenCoding, description, stopDate, regimenTypeCoding);
        }
        if (order.getConceptID() == 1594) { // INH CONCEPT ID
            regimenCoding = "H";
            regimenTypeCoding = INH_REGIMEN_TYPE_CODE;
            visitDate = order.getStartDate();
            stopDate = order.getStopDate();
            pepfarID = order.getPepfarID();
            description = order.getDrugName();
            regimenType = createRegimenType(pepfarID, visitDate, regimenCoding, description, stopDate, regimenTypeCoding);
        }
        return regimenType;
    }

    public boolean isValidARV(String conceptStr) {
        boolean ans = false;
        Integer[] arvConceptIDs = {953, 956, 952, 955, 960, 1186, 959, 1213, 1187, 1188, 957, 7777942, 7778040, 23, 1184,
            1219, 7778238, 7778239, 7777817, 1211, 7778240, 7778159, 1226, 1224, 1223, 7778241,
            1232, 1233, 7778242, 7778243, 7778244, 7778245, 7778246, 7778247, 7777880, 7777919,
            7777918, 7777921, 7777920, 1225, 1220, 17, 1222, 7778502, 7778503, 1533, 598, 7778849,
            1191, 7778830};
        String[] arvConceptIDStr = new String[arvConceptIDs.length];
        for (int i = 0; i < arvConceptIDs.length; i++) {
            arvConceptIDStr[i] = String.valueOf(arvConceptIDs[i]);
        }
        for(String ele: arvConceptIDStr){
            if(StringUtils.contains(ele, conceptStr)){
                ans=true;
            }
        }
        return ans;
    }

    public RegimenType createRegimenTypeARV(model.datapump.DrugOrder order) {
        RegimenType regimenType = null;
        String codedDrug="";
        String regimenCoding="";
        codedDrug=codeDrugs(order.getDrugName());
        /*
           RegimenType
           -VisitID
           -VisitDate
           -ReasonForRegimenSwitchSubs
           -PrescribedRegimen
           -PrescribedRegimenTypeCode
           -PrescribedRegimenLineCode
           -PrescribedRegimenDuration
           -PrescribedRegimenDispensedDate
           -DateRegimenStarted
           -DateRegimenStartedDD
           -DateRegimenStartedMM
           -DateRegimenStartedYYYY
           -DateRegimenEnded
           -DateRegimenEndedDD
           -DateRegimenEndedMM
           -DateRegimenEndedYYYY
           -PrescribedRegimenInitialIndicator
           -PrescribedRegimenCurrentIndicator
           -TypeOfPreviousExposureCode
           -PoorAdherenceIndicator
           -ReasonForPoorAdherence
           -ReasonRegimenEndedCode
           -SubstitutionIndicator
           -SwitchIndicator
         */
        return regimenType;
    }

    public List<RegimenType> constructRegimenTypeListARV(Demographics pts, List<model.datapump.DrugOrder> drugOrderList) throws DatatypeConfigurationException {
        List<RegimenType> regimenTypeList = new ArrayList<RegimenType>();
        RegimenType regimenType=null;
        for(model.datapump.DrugOrder order : drugOrderList){
            if(isValidARV(order.getDrugName())){
                regimenType=createRegimenTypeARV(order);
                regimenTypeList.add(regimenType);
            }
        }
        return regimenTypeList;
    }

    public String codeConceptString(String conceptString) {
        String code = "";
        if (StringUtils.isNotEmpty(conceptString)) {
            if (StringUtils.contains(code, code)) {
                
            }
        }
        return code;
    }

    public RegimenType extractARVRegimens(List<model.datapump.Obs> obsList, Demographics pts, Date visitDate) throws DatatypeConfigurationException {
        //List<RegimenType> regimenTypeList = new ArrayList<RegimenType>();
        String visitID = NDRCommonUtills.formatDateYYYYMMDD(visitDate) + "-" + pts.getPepfarID();
        String prescribedRegimenLineCode = "";
        Obs obsPin = null;
        int valueCoded = 0, obsID = 0, duration = 0;
        CodedSimpleType prescribedRegimenCst = null;
        RegimenType regimenType = null;
        obsPin = NDRCommonUtills.getObsForConceptFromList(165708, obsList);
        Calendar cal = Calendar.getInstance();
        String month = "", year = "", day = "", durationUnit = "", regimenCode = "";
        Date stopDate = null;
        if (obsPin != null) {
            regimenType = new RegimenType();
            valueCoded = obsPin.getValueCoded();
            Obs regimenObs = null;
            if (valueCoded == 164506 || valueCoded == 164507) {
                prescribedRegimenLineCode = "10";
                regimenObs = NDRCommonUtills.getObsForConceptFromList(valueCoded, obsList);

                if (regimenObs != null && getRegimenCode(regimenObs.getVariableValue(), valueCoded) != null) {
                    prescribedRegimenCst = new CodedSimpleType();
                    prescribedRegimenCst.setCode(getRegimenCode(regimenObs.getVariableValue(), valueCoded));
                    prescribedRegimenCst.setCodeDescTxt(regimenObs.getVariableValue());
                }
            } else if (valueCoded == 164513 || valueCoded == 164514) {
                prescribedRegimenLineCode = "20";
                regimenObs = NDRCommonUtills.getObsForConceptFromList(valueCoded, obsList);
                //regimenCode = getRegimenCode(regimenObs.getVariableValue(), valueCoded);
                if (regimenObs != null && getRegimenCode(regimenObs.getVariableValue(), valueCoded) != null) {
                    prescribedRegimenCst = new CodedSimpleType();
                    regimenObs = NDRCommonUtills.getObsForConceptFromList(valueCoded, obsList);
                    prescribedRegimenCst.setCode(getRegimenCode(regimenObs.getVariableValue(), valueCoded));
                    prescribedRegimenCst.setCodeDescTxt(regimenObs.getVariableValue());
                }
            }
            regimenType.setVisitID(visitID);
            regimenType.setVisitDate(getXmlDate(visitDate));
            regimenType.setPrescribedRegimenLineCode(prescribedRegimenLineCode);
            regimenType.setPrescribedRegimen(prescribedRegimenCst);
            regimenType.setDateRegimenStarted(getXmlDate(visitDate));
            cal.setTime(visitDate);
            month = StringUtils.leftPad(String.valueOf(cal.get(Calendar.MONTH) + 1), 2, "0");
            year = String.valueOf(cal.get(Calendar.YEAR));
            day = StringUtils.leftPad(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)), 2, "0");
            regimenType.setDateRegimenStarted(getXmlDate(visitDate));
            regimenType.setDateRegimenStartedDD(day);
            regimenType.setDateRegimenStartedMM(month);
            regimenType.setDateRegimenStartedYYYY(year);
            regimenType.setPrescribedRegimenDispensedDate(getXmlDate(visitDate));
            obsPin = NDRCommonUtills.getObsForConceptFromList(165056, obsList);// Retrieve Reason for Substitution/Switch from Care Card
            if (obsPin != null) {
                valueCoded = obsPin.getValueCoded();
                regimenType.setReasonForRegimenSwitchSubs(ndrCodedValues.get(valueCoded));
            }
            obsPin = NDRCommonUtills.getObsForConceptFromList(165945, obsList);// Treatment Type
            if (obsPin != null) {
                valueCoded = obsPin.getValueCoded();
                regimenType.setPrescribedRegimenTypeCode(ndrCodedValues.get(valueCoded));
            }
            obsPin = NDRCommonUtills.getObsForConceptFromList(162240, obsList);//Human immunodeficiency virus treatment regimen grouping concept for ARV 

            if (obsPin != null) {
                obsID = obsPin.getObsID();
                obsPin = NDRCommonUtills.getObsForConceptFromListWithGroupID(159368, obsList, obsID);//Medication Duration Concept 
                //if (obsPin != null) {
                //System.out.println(obsPin.getValueNumeric() + " = Duration");
                //}
                if (obsPin != null) {
                    //System.out.println("Obs ID group ID is: " + obsPin.getObsID());
                    duration = (int) obsPin.getValueNumeric();
                    durationUnit = "DAY(S)";
                    stopDate = NDRCommonUtills.calculateStopDate(visitDate, duration, durationUnit);
                }
            }
            if (stopDate == null) {
                obsPin = NDRCommonUtills.getObsForConceptFromList(5096, obsList);//Return Visit Date
                if (obsPin != null) {
                    System.out.println("Next Appointment Date value is " + obsPin.getValueDate());
                    stopDate = obsPin.getValueDate();

                    //stopDate = NDRCommonUtills.calculateStopDate(visitDate, duration, durationUnit);
                }

            }
            if (stopDate == null) {
                duration = 30;
                durationUnit = "DAY(S)";
                stopDate = NDRCommonUtills.calculateStopDate(visitDate, duration, durationUnit);
            }
            regimenType.setDateRegimenEnded(getXmlDate(stopDate));
            cal = Calendar.getInstance();
            cal.setTime(stopDate);
            month = StringUtils.leftPad(String.valueOf(cal.get(Calendar.MONTH) + 1), 2, "0");
            year = String.valueOf(cal.get(Calendar.YEAR));
            day = StringUtils.leftPad(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)), 2, "0");
            regimenType.setDateRegimenEndedDD(day);
            regimenType.setDateRegimenEndedMM(month);
            regimenType.setDateRegimenEndedYYYY(year);
            DateTime startDateTime = new DateTime(visitDate);
            DateTime endDateTime = new DateTime(stopDate);
            Days days = Days.daysBetween(startDateTime, endDateTime);
            int daysVal = days.getDays();
            String regimenDuration = String.valueOf(daysVal);
            regimenType.setPrescribedRegimenDuration(regimenDuration);
        }
        return regimenType;
    }

    public RegimenType converterToOIRegimenType(List<Obs> obsListForOI, Date visitDate, Demographics pts, List<Obs> obsListForVisit) throws DatatypeConfigurationException {
        RegimenType regimenType = null;
        Obs obsPin = null;
        int obsID = 0, duration = 0, conceptID = 0, valueCoded = 0;

        Calendar cal = Calendar.getInstance();
        String pepfarID = "", drugName = "", coding = "", description = "", regimenTypeCode = "NONART";
        Date stopDate = null;
        obsPin = NDRCommonUtills.getObsForConceptFromList(165726, obsListForVisit);//OI drugs Concept ID
        if (obsPin != null) {
            obsID = obsPin.getObsID();
            obsPin = NDRCommonUtills.getObsForConceptFromListWithGroupID(165727, obsListForVisit, obsID);// OI drugs
            if (obsPin != null && obsPin.getValueCoded() == 165257) {
                description = obsPin.getVariableValue();
                obsPin = NDRCommonUtills.getObsForConceptFromListWithGroupID(165725, obsListForVisit, obsID);// ARV Drug Strength
                if (obsPin != null) {
                    valueCoded = obsPin.getValueCoded();
                    switch (valueCoded) {
                        case 165062: //960mg 
                            coding = "CTX960";
                            break;
                        case 165060: //480mg
                            coding = "CTX480";
                            break;
                        case 166095: //240mg
                            coding = "CTX240";
                            break;
                        case 165068: //120mg 
                            coding = "CTX120";
                            break;
                        default: // Case anything else
                            coding = "CTX960";
                            break;
                    }

                }
                stopDate = getRegimenStopDate(obsListForVisit, visitDate, obsID);
                regimenType = createRegimenType(pts.getPepfarID(), visitDate, coding, description, stopDate, duration, regimenTypeCode);
                return regimenType;
            }
            obsPin = NDRCommonUtills.getObsForConceptFromList(165726, obsListForVisit);//OI drugs Concept ID
            if (obsPin != null) {
                obsID = obsPin.getObsID();
                obsPin = NDRCommonUtills.getObsForConceptFromListWithGroupID(165727, obsListForVisit, obsID);// OI drugs
                if (obsPin != null && isValidOIDrug(obsPin.getVariableValue())) {
                    drugName = obsPin.getVariableValue();
                    coding = getLocalCodingOtherDrugs(drugName);
                    description = drugName;
                    stopDate = getRegimenStopDate(obsListForOI, visitDate, obsID);
                    regimenType = createRegimenType(pepfarID, visitDate, coding, description, stopDate, duration, regimenTypeCode);
                    return regimenType;
                }
            }

        }
        return regimenType;
    }

    public Date getRegimenStopDate(List<Obs> obsListForVisit, Date visitDate, int obsID) {
        Obs obsPin = null;
        int duration = 0;
        String durationUnit = "DAY(S)";
        Date stopDate = null;
        //if (stopDate == null) {
        obsPin = NDRCommonUtills.getObsForConceptFromListWithGroupID(159368, obsListForVisit, obsID);//Medication duration
        if (obsPin != null) {
            duration = (int) obsPin.getValueNumeric();
            stopDate = NDRCommonUtills.calculateStopDate(visitDate, duration, durationUnit);
        }
        //}
        if (stopDate == null) {
            obsPin = NDRCommonUtills.getObsForConceptFromList(5096, obsListForVisit);// Next Appointment Date
            if (obsPin != null) {
                stopDate = obsPin.getValueDate();
            }
        }
        if (stopDate == null) {
            duration = 30;
            stopDate = NDRCommonUtills.calculateStopDate(visitDate, duration, durationUnit);
        }
        return stopDate;
    }

    public RegimenType createRegimenType(String pepfarID, Date visitDate, String regimenCoding, String description, Date stopDate, String regimenTypeCode) throws DatatypeConfigurationException {
        Calendar cal = Calendar.getInstance();
        String month = "", year = "", day = "";
        RegimenType regimenType = new RegimenType();
        String visitID = NDRCommonUtills.formatDateYYYYMMDD(visitDate) + "-" + pepfarID;
        regimenType.setVisitID(visitID);
        regimenType.setVisitDate(getXmlDate(visitDate));
        regimenType.setPrescribedRegimenDispensedDate(getXmlDate(visitDate));
        regimenType.setPrescribedRegimenTypeCode(regimenTypeCode);
        CodedSimpleType cst = new CodedSimpleType();
        cst.setCode(regimenCoding);
        cst.setCodeDescTxt(description);
        regimenType.setPrescribedRegimen(cst);
        cal.setTime(visitDate);
        month = StringUtils.leftPad(String.valueOf(cal.get(Calendar.MONTH) + 1), 2, "0");
        year = String.valueOf(cal.get(Calendar.YEAR));
        day = StringUtils.leftPad(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)), 2, "0");
        regimenType.setDateRegimenStarted(getXmlDate(visitDate));
        regimenType.setDateRegimenStartedDD(day);
        regimenType.setDateRegimenStartedMM(month);
        regimenType.setDateRegimenStartedYYYY(year);
        regimenType.setDateRegimenEnded(getXmlDate(stopDate));
        cal = Calendar.getInstance();
        cal.setTime(stopDate);
        month = StringUtils.leftPad(String.valueOf(cal.get(Calendar.MONTH) + 1), 2, "0");
        year = String.valueOf(cal.get(Calendar.YEAR));
        day = StringUtils.leftPad(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)), 2, "0");
        regimenType.setDateRegimenEndedDD(day);
        regimenType.setDateRegimenEndedMM(month);
        regimenType.setDateRegimenEndedYYYY(year);
        DateTime startDateTime = new DateTime(visitDate);
        DateTime endDateTime = new DateTime(stopDate);
        Days days = Days.daysBetween(startDateTime, endDateTime);
        int daysVal = days.getDays();
        String regimenDuration = String.valueOf(daysVal);
        regimenType.setPrescribedRegimenDuration(regimenDuration);
        return regimenType;

    }

    public RegimenType createRegimenType(String pepfarID, Date visitDate, String regimenCoding, String description, Date stopDate, int durationDays, String regimenTypeCode) throws DatatypeConfigurationException {
        Calendar cal = Calendar.getInstance();
        String month = "", year = "", day = "";
        RegimenType regimenType = new RegimenType();
        String visitID = NDRCommonUtills.formatDateYYYYMMDD(visitDate) + "-" + pepfarID;
        regimenType.setVisitID(visitID);
        regimenType.setVisitDate(getXmlDate(visitDate));
        regimenType.setPrescribedRegimenDispensedDate(getXmlDate(visitDate));
        regimenType.setPrescribedRegimenTypeCode(regimenTypeCode);
        CodedSimpleType cst = new CodedSimpleType();
        cst.setCode(regimenCoding);
        cst.setCodeDescTxt(description);
        regimenType.setPrescribedRegimen(cst);
        cal.setTime(visitDate);
        month = StringUtils.leftPad(String.valueOf(cal.get(Calendar.MONTH) + 1), 2, "0");
        year = String.valueOf(cal.get(Calendar.YEAR));
        day = StringUtils.leftPad(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)), 2, "0");
        regimenType.setDateRegimenStarted(getXmlDate(visitDate));
        regimenType.setDateRegimenStartedDD(day);
        regimenType.setDateRegimenStartedMM(month);
        regimenType.setDateRegimenStartedYYYY(year);
        regimenType.setDateRegimenEnded(getXmlDate(stopDate));
        cal = Calendar.getInstance();
        cal.setTime(stopDate);
        month = StringUtils.leftPad(String.valueOf(cal.get(Calendar.MONTH) + 1), 2, "0");
        year = String.valueOf(cal.get(Calendar.YEAR));
        day = StringUtils.leftPad(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)), 2, "0");
        regimenType.setDateRegimenEndedDD(day);
        regimenType.setDateRegimenEndedMM(month);
        regimenType.setDateRegimenEndedYYYY(year);
        DateTime startDateTime = new DateTime(visitDate);
        DateTime endDateTime = new DateTime(stopDate);
        Days days = Days.daysBetween(startDateTime, endDateTime);
        int daysVal = days.getDays();
        String regimenDuration = String.valueOf(daysVal);
        regimenType.setPrescribedRegimenDuration(regimenDuration);
        return regimenType;

    }

    public List<RegimenType> extractOIRegimens(List<model.datapump.Obs> obsList, Date visitDate, Demographics pts) throws DatatypeConfigurationException {
        List<RegimenType> regimenTypeList = new ArrayList<RegimenType>();
        RegimenType regimenType = null;
        List<Obs> oiDrugsObsList = null;
        List<Obs> obsListForOIProphylaxis = NDRCommonUtills.getAllObsWithConceptForList(165726, visitDate, obsList);// Retrieve all Group concept obs (Drugs for OI Prophylaxis)
        Set<Integer> obsIDSet = new HashSet<Integer>();
        for (Obs ele : obsListForOIProphylaxis) {
            obsIDSet.add(ele.getObsID());
        }
        for (Integer eleInt : obsIDSet) {
            oiDrugsObsList = NDRCommonUtills.getAllObsForConceptFromListWithGroupID(obsList, eleInt);
            regimenType = converterToOIRegimenType(oiDrugsObsList, visitDate, pts, obsList);
            regimenTypeList.add(regimenType);
        }

        return regimenTypeList;
    }

    public String getLocalCodingOtherDrugs(String drugName) {
        String coding = "";
        if (drugName != null && !drugName.isEmpty()) {
            Set<String> keySet = localDrugCodeMapping.keySet();
            for (String key : keySet) {
                if (drugName.contains(key) && !localDrugCodeMapping.get(key).contains("CTX")) {
                    coding += localDrugCodeMapping.get(key) + "-";
                }

            }
            if (coding.endsWith("-")) {
                coding = coding.substring(0, coding.length() - 1);
            }
            String[] codingArray = coding.split("-");
            Arrays.sort(codingArray);
            coding = StringUtils.join(codingArray, "-");
        }
        return coding;
    }

    public String getRegimenConceptNDRCode(int valueCoded) {
        String code = ndrCodedValues.get(valueCoded);
        return code;
    }

    public boolean isValidOIDrug(String drugName) {
        boolean ans = false;
        if (localDrugCodeMapping.containsKey(drugName)) {
            ans = true;
        }
        return ans;
    }

}
