/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.datapump;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Visit {

    private Date visitDate;
    private Set<String> formNames;
    private Set<Integer> encounterIDs;
    private HashMap<Integer, Obs> conceptMap;
    private int patientID;
    private int locationID;
    private String locationName;
    private PatientRegimen regimen;
    private Demographics demo;
    private Date firstFormDate;
    private Date lastVisitDate;
    private Date lastARTPickupDate;
    private Date artStartDate;
    private String pepfarID;
    private String visitID;
    private String hospID;

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
     * @return the formNames
     */
    public Set<String> getFormNames() {
        return formNames;
    }

    /**
     * @param formNames the formNames to set
     */
    public void setFormNames(Set<String> formNames) {
        this.formNames = formNames;
    }

    /**
     * @return the encounterIDs
     */
    public Set<Integer> getEncounterIDs() {
        return encounterIDs;
    }

    /**
     * @param encounterIDs the encounterIDs to set
     */
    public void setEncounterIDs(Set<Integer> encounterIDs) {
        this.encounterIDs = encounterIDs;
    }

    /**
     * @return the conceptMap
     */
    public HashMap<Integer, Obs> getConceptMap() {
        return conceptMap;
    }

    /**
     * @param conceptMap the conceptMap to set
     */
    public void setConceptMap(HashMap<Integer, Obs> conceptMap) {
        this.conceptMap = conceptMap;
    }

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

    public static String getFormNameString(Visit visit) {
        String forms = "";
        Set<String> formNames = visit.getFormNames();
        if (formNames != null) {
            for (String ele : formNames) {
                forms += ele + ",";
            }
        }
        return forms;
    }

    public static String codeGender(String gender) {
        String code = null;
        if (gender != null && !gender.isEmpty()) {
            if (gender.equalsIgnoreCase("M")) {
                code = "Male";
            } else if (gender.equalsIgnoreCase("F")) {
                code = "Female";
            }
        }
        return code;
    }

    public static String codeARTStatus(String artStatus) {
        String status = "";

        if (artStatus.equals("ON ART")) {
            status = "ART";
        } else if (artStatus.equals("NOT ON ART")) {
            status = "Non ART";
        } else if (artStatus.equals("ON PMTCT")) {
            status = "PMTCT";
        }
        return status;
    }

    public static String codeServicesReceived(HashMap<Integer, Obs> map, PatientRegimen rgmn) {
        String service = "";
        String value = "";
        Obs tbStatus = map.get(862);
        if (tbStatus != null) {
            service += "TB_Scrn ";
            if (tbStatus.getValueCoded() == 872) {
                service += "TB_Tx ";
            }

        }
        if (rgmn != null && rgmn.getDrugName().toUpperCase().contains("COTRI")) {
            service += "Rcvd_CTX ";
        }

        return service;
    }

    public static String codeLVOutcome(HashMap<Integer, Obs> map) {
        String outcome = "";
        Obs obs = null;
        int valueCoded = 0;
        obs = map.get(977);
        if (obs != null) {
            valueCoded = obs.getValueCoded();
            switch (valueCoded) {
                case 974:
                    outcome = "LTFU";
                    break;
                case 975:
                    outcome = "Dead";
                    break;
                case 977:
                    outcome = "Transferred Out";
                    break;
                case 976:
                    outcome = "Stopped";
                    break;
                default:
                    break;
            }

        }
        return outcome;
    }

    public static String codeTBScreen(HashMap<Integer, Obs> map) {
        String code = "0";
        Obs obs = map.get(862);
        int valueCoded = 0;
        if (obs != null) {
            code = "1";
        }
        return code;
    }

    public static String codeTBScreenDate(HashMap<Integer, Obs> map) {
        String date = "";
        Obs obs = map.get(862);
        int valueCoded = 0;
        if (obs != null) {
            date = formatDate2(obs.getVisitDate());
        }
        return date;
    }

    public static String codeTBTx(HashMap<Integer, Obs> map) {
        String code = "0";
        Obs obs = map.get(862);
        int valueCoded = 0;
        if (obs != null) {
            valueCoded = obs.getValueCoded();
            if (valueCoded == 872) {
                code = "1";
            }
        }
        return code;
    }

    public static String codeNutriSpprt(HashMap<Integer, Obs> map) {
        String code = "";
        Obs obs = map.get(1269);
        int valueCoded = 0;
        if (obs != null) {
            valueCoded = obs.getValueCoded();
            if (valueCoded == 258) {
                code = "Nutr_spprt";
            }
        }
        return code;
    }

    public static String codeNutriSpprtDate(HashMap<Integer, Obs> map) {
        String date = "";
        Obs obs = map.get(1269);
        int valueCoded = 0;
        if (obs != null) {
            valueCoded = obs.getValueCoded();
            if (valueCoded == 258) {
                date = formatDate2(obs.getVisitDate());
            }
        }
        return date;
    }

    public static String[] getVisitHeaders() {
        String[] headers = {
            "PEPFAR_ID",
            "HOSP_ID",
            "GENDER",
            "AGE_YRS",
            "AGE_MNT",
            "DOB",
            "FIRST_NAME",
            "LAST_NAME",
            "MIDDLE_NAME",
            "regimen",
            "CRNT_ART_STAT",
            "FIRST_VISIT_DT",
            "ART_INITIATION_DT",
            "LST_CLN_VST_DT",
            "LST_ART_PICKUP_DT",
            "SERVICES_RCVD",
            "LV_OUTCOME",
            "TB_SCRN_DT",
            "TB_scrn",
            "TB_Tx",
            "RCVD_NUT_SPPRT_DT",
            "Nutri_spprt",
            "RCVD_CTX_DT",
            "Rcvd_CTX",
            "Patients_Tel_No",
            "CD4_TEST_DT",
            "CD4_COUNT",
            "PTS_ADDRESS",
            "PTS_weight",
            "PTS_MUAC",
            "PTS_HEIGHT",
            "PMM_FORMS",
            "RGMN_LN",
            "TB_STATUS",
            "HBC_SRVC_RCVD",
            "TRANS_IN_ID",
            "FRST_FORM_DT",
            "LST_FORM_DT",
            "DRUGS",
            "LST_DRUPICKUP_DT",
            "PMTCT_ENROLL_DT",
            "HEI_ENROLL_DT",
            "REGIMEN_NAME"
        };
        return headers;
    }

    public static String[] toArray(Visit visit) {//, ) {
        String artStartDate = formatDate2(visit.getArtStartDate());
        String firstFormDate = formatDate2(visit.getFirstFormDate());
        String lastVisitDate = formatDate2(visit.getLastVisitDate());
        String lastARTPickupDate = formatDate2(visit.getLastARTPickupDate());
        Demographics demo = visit.getDemo();
        String pepfarID = demo.getPepfarID();
        String hospID = demo.getHospID();
        String gender = demo.getGender();
        String dob = formatDate2(demo.getDateOfBirth());
        String firstName = demo.getFirstName();
        String lastName = demo.getLastName();
        String middleName = demo.getMiddleName();
        String address = demo.getAddress2() + " " + demo.getAddress1() + " " + demo.getAddress_lga() + " " + demo.getAddress_state();
        address=address.trim();
        String firstVisitDate = formatDate2(demo.getEnrollDate());
        String ageYrs = String.valueOf(demo.getAge());
        String ageMnt = "";
        String transID = demo.getOtherID();
        if (demo.getAge() < 5) {
            ageMnt = String.valueOf(demo.getAgeMnt());
        }

        ArrayList<String> dataList = new ArrayList<String>(45);

        PatientRegimen regimen = visit.getRegimen();
        Obs obs = null;
        dataList.add(0, pepfarID);
        dataList.add(1, hospID);
        dataList.add(2, codeGender(gender));
        dataList.add(3, ageYrs);
        dataList.add(4, ageMnt);
        dataList.add(5, dob);
        dataList.add(6, firstName);
        dataList.add(7, lastName);
        dataList.add(8, middleName);
        if (regimen != null && !regimen.getRegimenName().isEmpty()) {
            dataList.add(9, regimen.getCode());
            //dataList.add(14, formatDate2(regimen.getStartDate()));
        } else {
            dataList.add(9, "");
        }
        obs = visit.getConceptMap().get(1256);
        String artStatus="";
        if (obs != null) {
            //artStatus=codeARTStatus(obs.getVariableValue());
            dataList.add(10, codeARTStatus(obs.getVariableValue()));
        } else if(regimen!=null && !regimen.getRegimenName().isEmpty()) {
            dataList.add(10, "ART");
        }else{
            dataList.add(10,"Non ART");
        }

        dataList.add(11, firstVisitDate);
        dataList.add(12, artStartDate);
        dataList.add(13, formatDate2(visit.getVisitDate()));
        if (regimen != null && !regimen.getRegimenName().isEmpty() && !lastARTPickupDate.isEmpty()) {
           
            dataList.add(14, formatDate2(regimen.getStartDate()));
        }else{
            dataList.add(14, "");
        }
       
        dataList.add(15, codeServicesReceived(visit.getConceptMap(), visit.getRegimen()));
        dataList.add(16, codeLVOutcome(visit.getConceptMap()));
        dataList.add(17, codeTBScreenDate(visit.getConceptMap()));
        dataList.add(18, codeTBScreen(visit.getConceptMap()));
        dataList.add(19, codeTBTx(visit.getConceptMap()));
        dataList.add(20, codeNutriSpprtDate(visit.getConceptMap()));
        dataList.add(21, codeNutriSpprt(visit.getConceptMap()));
        if (regimen != null && regimen.getDrugName().toUpperCase().contains("COTRI")) {
            dataList.add(22, formatDate2(regimen.getStartDate()));
            dataList.add(23, "1");
        } else {
            dataList.add(22, "");
            dataList.add(23, "");
        }
        obs = visit.getConceptMap().get(97);
        if (obs != null) {
            dataList.add(24, obs.getValueText());
        } else {
            dataList.add(24, "");

        }
        obs = visit.getConceptMap().get(88);
        if (obs != null) {
            dataList.add(25, formatDate2(obs.getVisitDate()));
            dataList.add(26, String.valueOf(obs.getValueNumeric()));
        } else {
            dataList.add(25, "");
            dataList.add(26, "");
        }
        dataList.add(27, address);
        obs = visit.getConceptMap().get(85);
        if (obs != null) {
            dataList.add(28, String.valueOf(obs.getValueNumeric()));
        } else {
            dataList.add(28, "");
        }
        obs = visit.getConceptMap().get(387);
        if (obs != null) {
            dataList.add(29, String.valueOf(obs.getValueNumeric()));
        } else {
            dataList.add(29, "");
        }
        obs = visit.getConceptMap().get(571);
        if (obs != null) {
            dataList.add(30, obs.getVariableValue());
        } else {
            dataList.add(30, "");
        }
        dataList.add(31, getFormNameString(visit));
        if (regimen != null) {
            dataList.add(32, regimen.getRegimenLine());
        } else {
            dataList.add(32, "");
        }
        obs = visit.getConceptMap().get(862);
        if (obs != null) {
            dataList.add(33, obs.getVariableValue());
        } else {
            dataList.add(33, "");
        }

        obs = visit.getConceptMap().get(1269);
        if (obs != null) {
            dataList.add(34, obs.getVariableValue());
        } else {
            dataList.add(34, "");
        }
        dataList.add(35, transID);
        dataList.add(36, firstFormDate);
        dataList.add(37, lastVisitDate);
        if (regimen != null) {
            dataList.add(38, regimen.getDrugName());
            // dataList.add(43,regimen.getRegimenName());
        } else {
            dataList.add(38, "");
        }
        dataList.add(39, lastARTPickupDate);
        dataList.add(40, formatDate2(demo.getPmtctEnrollmentDt()));
        dataList.add(41, formatDate2(demo.getHeiEnrollmentDt()));
        if (regimen != null) {
            //dataList.add(38,regimen.getDrugName());
            dataList.add(42, regimen.getRegimenName());
        } else {
            dataList.add(42, "");
        }
        //dataList.add(43, ageMnt);
        //dataList.add(42,formatDate2(demo.getHeiEnrollmentDt()));
        String[] data = new String[43];
        for (int i = 0; i < dataList.size(); i++) {
            data[i] = dataList.get(i);
        }

        return data;
    }

    public static Visit createVisit(Demographics demo, Date visitDate, HashMap<Integer, Obs> map, PatientRegimen regimen, Date artStartDate, Date firstFormDate, Date lastVisitDate, Date lastARTPickupDate) {
        Visit visit = new Visit();
        visit.setDemo(demo);
        visit.setPatientID(demo.getPatientID());
        visit.setVisitDate(visitDate);
        visit.setConceptMap(map);
        visit.setRegimen(regimen);
        visit.setArtStartDate(artStartDate);
        visit.setFirstFormDate(firstFormDate);
        visit.setLastARTPickupDate(lastARTPickupDate);
        visit.setLastVisitDate(lastVisitDate);
        Set<String> formNames = new HashSet<String>();
        Set<Integer> encounterSet = new HashSet<Integer>();
        if (!map.isEmpty()) {
            Set<Integer> keySet = map.keySet();
            Obs obs = null;
            for (Integer ele : keySet) {
                obs = map.get(ele);
                if (obs != null) {
                    formNames.add(obs.getFormName());
                    encounterSet.add(obs.getEncounterID());
                }
            }
            visit.setFormNames(formNames);
            visit.setEncounterIDs(encounterSet);
        }
        visit.setLocationID(demo.getLocationID());
        visit.setLocationName(demo.getLocationName());
        return visit;
    }

    public static String formatDate2(Date date) {
        String dateString = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            dateString = df.format(date);
        }
        return dateString;

    }

    /**
     * @return the regimen
     */
    public PatientRegimen getRegimen() {
        return regimen;
    }

    /**
     * @param regimen the regimen to set
     */
    public void setRegimen(PatientRegimen regimen) {
        this.regimen = regimen;
    }

    /**
     * @return the locationName
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * @param locationName the locationName to set
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    /**
     * @return the demo
     */
    public Demographics getDemo() {
        return demo;
    }

    /**
     * @param demo the demo to set
     */
    public void setDemo(Demographics demo) {
        this.demo = demo;
    }

    /**
     * @return the firstFormDate
     */
    public Date getFirstFormDate() {
        return firstFormDate;
    }

    /**
     * @param firstFormDate the firstFormDate to set
     */
    public void setFirstFormDate(Date firstFormDate) {
        this.firstFormDate = firstFormDate;
    }

    /**
     * @return the lastVisitDate
     */
    public Date getLastVisitDate() {
        return lastVisitDate;
    }

    /**
     * @param lastVisitDate the lastVisitDate to set
     */
    public void setLastVisitDate(Date lastVisitDate) {
        this.lastVisitDate = lastVisitDate;
    }

    /**
     * @return the lastARTPickupDate
     */
    public Date getLastARTPickupDate() {
        return lastARTPickupDate;
    }

    /**
     * @param lastARTPickupDate the lastARTPickupDate to set
     */
    public void setLastARTPickupDate(Date lastARTPickupDate) {
        this.lastARTPickupDate = lastARTPickupDate;
    }

    /**
     * @return the artStartDate
     */
    public Date getArtStartDate() {
        return artStartDate;
    }

    /**
     * @param artStartDate the artStartDate to set
     */
    public void setArtStartDate(Date artStartDate) {
        this.artStartDate = artStartDate;
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
     * @return the visitID
     */
    public String getVisitID() {
        return visitID;
    }

    /**
     * @param visitID the visitID to set
     */
    public void setVisitID(String visitID) {
        this.visitID = visitID;
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

}
