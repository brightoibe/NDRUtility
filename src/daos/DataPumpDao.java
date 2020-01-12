/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import com.inductivehealth.ndr.schema.CommonQuestionsType;
import com.inductivehealth.ndr.schema.ConditionSpecificQuestionsType;
import com.inductivehealth.ndr.schema.ConditionType;
import com.inductivehealth.ndr.schema.Container;
import com.inductivehealth.ndr.schema.EncountersType;
import com.inductivehealth.ndr.schema.FingerPrintType;
import com.inductivehealth.ndr.schema.HIVEncounterType;
import com.inductivehealth.ndr.schema.HIVQuestionsType;
import com.inductivehealth.ndr.schema.IndividualReportType;
import com.inductivehealth.ndr.schema.LaboratoryOrderAndResult;
import com.inductivehealth.ndr.schema.LaboratoryReportType;
import com.inductivehealth.ndr.schema.PatientDemographicsType;
import com.inductivehealth.ndr.schema.ProgramAreaType;
import com.inductivehealth.ndr.schema.RegimenType;
import com.inductivehealth.ndr.schema.RightHandType;
import dictionary.NDRMasterDictionary;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.zip.ZipOutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.stream.XMLStreamException;
import model.Appointment;
import model.Concept;
import model.Demographics;
import model.Drug;
import model.DrugOrder;
import model.Encounter;
import model.Location;
//import model.Obs;
import model.datapump.Obs;
import model.PatientAddress;
import model.PatientProgram;
import model.datapump.PatientRegimen;
import model.Person;
import model.PersonName;
import model.Regimen;
import model.Relationship;
import model.User;
import model.Visit;
import model.datapump.BiometricInfo;
import model.datapump.Drugs;
import model.datapump.Form;
import model.form.ARTCommence;
import model.form.CareCardFollowUp;
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
import model.nigeriaqual.PediatricTuberculosis;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Weeks;
import org.joda.time.Years;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import util.CohortBuilder;
import util.CompressUtil;
import util.CustomErrorHandler;
import util.FileManager;
import util.LocationMap;
import util.NDRWriter;
import util.NigeriaQualWriter;

/**
 *
 * @author brightoibe
 */
public class DataPumpDao implements model.datapump.DataAccess {

    int progress;
    private Connection connection;
    private model.datapump.DisplayScreen screen;
    private FileManager mgr;
    private Map<Integer, String> pepfarDictionary, hospIDDictionary,
            locationDictionary, otherIDDictionary, ehnidDictionary,
            drugDictionary, idLocationDictionary, valueCodedDictionary,
            formNamesDictionary;
    private HashMap<Integer, PatientRegimen> firstRegimenDictionary;
    private ArrayList<model.datapump.Demographics> patientDemoList2;
    private HashMap<Integer, Person> personDictionary;
    private HashMap<Integer, model.datapump.Concept> conceptDictionary;
    private HashMap<Integer, PersonName> namesDictionary;
    private HashMap<Integer, PatientAddress> addressDictionary;
    private HashMap<Integer, Date> lastVisitDateDictionary, firstVisitDateDictionary, artStartDateDictionary, lastARTPickupDateDictionary;
    private HashMap<Integer, User> userDictionary;
    private HashMap<Integer, PatientProgram> programDictionary,
            adultARTPatientProgram, peadPatientProgram,
            pmtctPatientProgram, pepPatientEnrollment, heiEnrollment, ancEnrollment;
    private ArrayList<String> zipFileEntryNames;
    private HashMap<String, Integer> map1;
    private HashMap<String, Integer> map2;
    //private HashMap<Integer, Demographics> patientDemoMap;
    private HashMap<Integer, model.datapump.Demographics> patientDemoMap;
    private ArrayList<model.datapump.Demographics> patientDemoList;
    private Set<Integer> ageLess1, age1To4, age5To9, age10To14, age15To19, age20To24, age25To49, age50;
    private Set<Integer> male, female;
    private Set<Integer> careNew, onART, careCurr, careCumm, txNew, exit, tbScrn, tbTxt, txCurr, ccVisit;
    private NDRWriter ndrWriter;
    private CustomErrorHandler errorHandler;
    private HashMap<Integer, model.datapump.Concept> conceptDictionaryRL;
    private Map<String, String> locationNDRMap;
    private NigeriaQualWriter nigeriaQualWriter;
    private final static String NAMES_SPACE = "http://www.w3.org/2001/XMLSchema-instance";
    private final static String COMMON_HEADER = "Data-set";
    private LocationMap locMap = new LocationMap();
    private DateFormat formatter;
    private Map<String, String> propertyMap;

    public DataPumpDao() {
        zipFileEntryNames = new ArrayList<String>();
        mgr = new FileManager();

    }

    public boolean loadDriver() {
        boolean ans;

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            ans = true;
            screen.updateStatus("Mysql jdbc driver loaded......");
        } catch (ClassNotFoundException e) {
            ans = false;
            screen.updateStatus(e.getMessage());
        } catch (InstantiationException ie) {
            System.err.println(ie.getMessage());
            ans = false;
            screen.updateStatus(ie.getMessage());
        } catch (IllegalAccessException iae) {
            System.err.println(iae.getMessage());
            screen.updateStatus(iae.getMessage());
            ans = false;
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            screen.updateStatus(ex.getMessage());
            ans = false;
        }
        return ans;
    }

    @Override
    public boolean connect(model.datapump.DBConnection con) {
        boolean ans;
        try {
            String conString = "jdbc:mysql://" + con.getHostName() + ":" + con.getMysqlPort() + "/" + con.getDatabase() + "?user=" + con.getUsername() + "&password=" + con.getPassword();
            connection = DriverManager.getConnection(conString);
            ans = true;
            connection.setAutoCommit(false);
        } catch (SQLException ex) {
            displayErrors(ex);
            ans = false;
        }
        return ans;
    }

    public Map<String, String> getGlobalProperties() {
        Map<String, String> propertyMap = new HashMap<String, String>();
        String sql_text = "select property,property_value from global_property where property in('Facility_Name','facility_datim_code','nigeriaqual_id','partner_full_name','partner_short_name')";
        PreparedStatement ps = prepareQuery(sql_text);
        ResultSet rs = null;
        try {
            rs = ps.executeQuery();
            while (rs.next()) {
                propertyMap.put(rs.getString("property"), rs.getString("property_value"));
            }
            cleanUp(rs, ps);
        } catch (SQLException ex) {
            displayErrors(ex);
        } finally {
            cleanUp(rs, ps);
        }
        return propertyMap;
    }

    @Override
    public void runReport(Date startDate, Date endDate, model.datapump.Location location, String encrypt, File file, String reportType, File datimIDFile) {
        zipFileEntryNames.clear();
        screen.updateProgress(0);
        loadDictionaries();
        int location_id = location.getLocationID();
        propertyMap = getGlobalProperties();
        String datim_id = propertyMap.get("facility_datim_code");
        String default_facility = propertyMap.get("Facility_Name");
        String nigeriaqual_id = propertyMap.get("nigeriaqual_id");
        location.setDatimID(datim_id);
        location.setDefaultLoacation(default_facility);
        location.setNigeriaQualID(nigeriaqual_id);
        //loadPatientDemographics(location_id);
        loadDemographics(location_id);

        /*try {
            if (datimIDFile != null) {
                locMap.loadLocation(datimIDFile);
                
            }
        } catch (IOException ex) {
            handleException(ex);
        } catch (Exception ex) {
            handleException(ex);
        }*/
        if (reportType.equals("DATA WAREHOUSE XML")) {
            runDWHExport(startDate, endDate, file, location_id);
        } else if (reportType.equals("PATIENT REGIMEN")) {
            exportPatientRegimenCSV(startDate, endDate, location_id, file);
        } else if (reportType.equals("APPOINTMENTS")) {
            runAppointmentExport(startDate, endDate, location_id, file);
        } else if (reportType.equals("DISPENSED DRUGS")) {
            runDispensedDrugs(startDate, endDate, location_id, file);
        } else if (reportType.equals("LAB")) {
            runLabExport(startDate, endDate, location_id, file);
        } else if (reportType.equalsIgnoreCase("MONTHLY EXPORT")) {
            runMonthlyExport(startDate, endDate, location_id, encrypt, file, reportType);
        } else if (reportType.equalsIgnoreCase("INDICATOR EXPORT")) {
            runIndicatorReport(startDate, endDate, file, location_id);
        } else if (reportType.equalsIgnoreCase("NDR")) {
            //if (locMap.hasNDRDatimID(location)) {
            if (!StringUtils.isEmpty(location.getDatimID())) {
                if (!StringUtils.isEmpty(location.getDefaultLoacation())) {
                    runNDRExport2(startDate, endDate, file, location);
                } else {
                    screen.showError("add default_location to global property");
                }
            } else {
                screen.showError("add datim_id to global property");
            }
        } else if (reportType.equalsIgnoreCase("ADULTNIGERIAQUAL")) {
            //if (locMap.hasNigeriaQualID(location)) {
            if (!StringUtils.isEmpty(location.getNigeriaQualID())) {
                runNigeriaQualExport(startDate, endDate, file, location);// datimIDFile);
            } else {
                screen.showError("add nigeriaqual_id to global property");
            }
        } else if (reportType.equals("PEDNIGERIAQUAL")) {
            //if (locMap.hasNigeriaQualID(location)) {
            if (!StringUtils.isEmpty(location.getNigeriaQualID())) {
                runPedNigeriaQualExport(startDate, endDate, file, location);// datimIDFile);
            } else {
                screen.showError("add nigeriaqual_id to global property");
            }

        }
    }

    public void runPedNigeriaQualExport(Date startDate, Date endDate, File file, model.datapump.Location loc) {// File datimIDFile) {
        long startTime = System.currentTimeMillis();
        screen.updateMinMaxProgress(0, 13);
        try {
            nigeriaQualWriter = new NigeriaQualWriter();
        } catch (Exception ex) {
            handleException(ex);
        }
        int count = 0;
        nigeriaQualWriter.setConceptDictionary(conceptDictionaryRL, locMap);
        //nigeriaQualWriter.loadDictionaries();
        int location_id = loc.getLocationID();
        count++;
        screen.updateProgress(count);
        Set<Integer> idSet = getSamplePedPatients(startDate, endDate, location_id);
        loadFirstRegimens(idSet);
        count++;
        screen.updateProgress(count);
        runPediatricPatientDemographics(startDate, endDate, loc, file, idSet);
        count++;
        screen.updateProgress(count);
        runPediatricARTAdherence(startDate, endDate, loc, file, idSet);
        count++;
        screen.updateProgress(count);
        runPedRegimenDuringReviewPeriod(startDate, endDate, loc, file, idSet);
        count++;
        screen.updateProgress(count);
        runPediatricBaselineParameterExport(startDate, endDate, loc, file, idSet);
        count++;
        screen.updateProgress(count);
        runPedClinicalEvalExport(startDate, endDate, loc, file, idSet);
        count++;
        screen.updateProgress(count);
        runPediatricCotrimoxazole(startDate, endDate, loc, file, idSet);
        count++;
        screen.updateProgress(count);
        runPediatricPatientMonitoringDuringReviewPeriod(startDate, endDate, loc, file, idSet);
        count++;
        screen.updateProgress(count);
        runPediatricTuberculosisRecord(startDate, endDate, loc, file, idSet);
        count++;
        screen.updateProgress(count);
        runPediatricPatientStatusReviewPeriod(startDate, endDate, loc, file, idSet);
        count++;
        screen.updateProgress(count);
        runPediatricEducation(startDate, endDate, loc, file, idSet);
        count++;
        screen.updateProgress(count);
        runPediatricLinkage(startDate, endDate, loc, file, idSet);
        count++;
        screen.updateProgress(count);
        addToSpecialZip(zipFileEntryNames, file.getAbsolutePath());
        count++;
        screen.updateProgress(13);
        long duration = calculateDuration(startTime);
        screen.updateStatus("Writing Pediatric Export Completed " + duration + " secs");

    }

    public void runPediatricTuberculosisRecord(Date startDate, Date endDate, model.datapump.Location loc, File file, Set<Integer> idSet) {
        screen.updateStatus("Writing DataTuberculosis.xml...");
        HashMap<Integer, model.datapump.Obs> obsTBStatusMap = null;
        HashMap<Integer, model.datapump.Obs> obsTBScrn = null;
        Set<Integer> ptsTbTreatmentStartDtSet = null;
        Set<Integer> csrSet = null;
        Set<Integer> ptsTBScreenSet = null;
        PediatricTuberculosis dtr = null;
        int count = 0;
        int locationID = loc.getLocationID();
        String pepfarID = null;
        int conceptID = 862;
        obsTBStatusMap = getLastOfObsForPatients(startDate, endDate, locationID, idSet, conceptID);
        conceptID = 1045;
        obsTBScrn = getLastOfObsForPatients(startDate, endDate, locationID, idSet, conceptID);
        ptsTBScreenSet = getAllPatientsWithClinicalEvalFormsFilled(idSet, startDate, endDate);
        ptsTbTreatmentStartDtSet = getPatientsOnTBTreatment6MonthsBeforeRP(startDate, endDate, locationID, idSet);
        csrSet = getPatientCXRPerformed(startDate, endDate, locationID, idSet);
        String fileName = "PediatricTuberculosis.xml";
        model.datapump.Demographics demo = null;
        zipFileEntryNames.add(fileName);
        try {
            mgr.createXMLWriter(fileName);
            mgr.openXMLDocument();
            mgr.writeXMLHeader(COMMON_HEADER, NAMES_SPACE);
            for (int ele : idSet) {
                demo = patientDemoMap.get(ele);
                if (demo != null) {
                    pepfarID = demo.getPepfarID();
                }
                dtr = nigeriaQualWriter.createPediatricTBRecord(obsTBStatusMap, ptsTBScreenSet, obsTBScrn, ptsTbTreatmentStartDtSet, csrSet, ele, pepfarID, loc);
                mgr.writeToXML(dtr);
                screen.updateStatus("PediatricTuberculosis.xml..." + count);
                count++;
            }
            mgr.endXMLDocument();
            mgr.closeXMLWriter();

        } catch (XMLStreamException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void runPediatricPatientStatusReviewPeriod(Date startDate, Date endDate, model.datapump.Location loc, File file, Set<Integer> idSet) {
        screen.updateStatus("Writing PediatricPatientStatusReviewPeriod.xml...");
        Integer[] formIDArr = {29};
        Set<Integer> formIDSet = new HashSet<Integer>(Arrays.asList(formIDArr));
        ArrayList<model.datapump.Obs> obsList = getAllObsForForm(formIDSet, idSet, startDate, endDate);
        String fileName = "PediatricPatientStatusReviewPeriod.xml";
        ArrayList<PediatricPatientStatus> pstatusList = nigeriaQualWriter.createPediatricPatientStatusReviewPeriod(patientDemoMap, idSet, obsList, loc);
        zipFileEntryNames.add(fileName);
        int count = 0;
        try {
            mgr.createXMLWriter(fileName);
            mgr.openXMLDocument();
            mgr.writeXMLHeader(COMMON_HEADER, NAMES_SPACE);
            for (PediatricPatientStatus pm : pstatusList) {
                mgr.writeToXML(pm);
                count++;
                screen.updateStatus("Writing PediatricPatientStatusReviewPeriod.xml..." + count);
            }
            mgr.endXMLDocument();
            mgr.closeXMLWriter();
        } catch (XMLStreamException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void runPediatricLinkage(Date startDate, Date endDate, model.datapump.Location loc, File file, Set<Integer> idSet) {
        screen.updateStatus("Writing PediatricLinkage.xml...");
        Integer[] formIDArr = {30, 51};
        Set<Integer> formIDSet = new HashSet<Integer>(Arrays.asList(formIDArr));
        ArrayList<model.datapump.Obs> obsList = getAllObsForForm(formIDSet, idSet, startDate, endDate);
        String fileName = "PediatricLinkage.xml";
        ArrayList<PediatricLinkage> plList = nigeriaQualWriter.createPediatricLinkage(patientDemoMap, idSet, obsList, loc);
        zipFileEntryNames.add(fileName);
        int count = 0;
        try {
            mgr.createXMLWriter(fileName);
            mgr.openXMLDocument();
            mgr.writeXMLHeader(COMMON_HEADER, NAMES_SPACE);
            for (PediatricLinkage pm : plList) {
                mgr.writeToXML(pm);
                count++;
                screen.updateStatus("Writing PediatricLinkage.xml..." + count);
            }
            mgr.endXMLDocument();
            mgr.closeXMLWriter();
        } catch (XMLStreamException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void runPediatricEducation(Date startDate, Date endDate, model.datapump.Location loc, File file, Set<Integer> idSet) {
        screen.updateStatus("Writing PediatricEducation.xml...");
        Integer[] formIDArr = {30, 51};
        Set<Integer> formIDSet = new HashSet<Integer>(Arrays.asList(formIDArr));
        ArrayList<model.datapump.Obs> obsList = getAllObsForConcepts(formIDSet, idSet, startDate, endDate);
        String fileName = "PediatricEducation.xml";
        ArrayList<PediatricEducation> peList = nigeriaQualWriter.createPediatricEducation(patientDemoMap, idSet, obsList, loc);
        zipFileEntryNames.add(fileName);
        int count = 0;
        try {
            mgr.createXMLWriter(fileName);
            mgr.openXMLDocument();
            mgr.writeXMLHeader(COMMON_HEADER, NAMES_SPACE);
            for (PediatricEducation pm : peList) {
                mgr.writeToXML(pm);
                count++;
                screen.updateStatus("Writing PediatricEducation.xml..." + count);
            }
            mgr.endXMLDocument();
            mgr.closeXMLWriter();
        } catch (XMLStreamException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public HashMap<Integer, Obs> getLastOfObsForPatients(Date endDate, int locationID, Set<Integer> idSet, int conceptID) {
        HashMap<Integer, Obs> obsList = new HashMap<Integer, Obs>();
        /*String sql_text="SELECT * from obs \n" +
"inner join (select PATIENT_ID,CONCEPT_ID,MAX(VISIT_DATE) LST_DT FROM obs \n" +
"WHERE CONCEPT_ID=? AND VISIT_DATE <= ? AND PATIENT_ID IN ("+buildString(idSet)+") AND LOCATION_ID=? GROUP BY PATIENT_ID,CONCEPT_ID) sinner on(sinner.PATIENT_ID=obs.PATIENT_ID AND sinner.CONCEPT_ID=obs.CONCEPT_ID AND sinner.LST_DT=obs.VISIT_DATE)";*/
        String sql_text = "select \n"
                + "    `obs`.`obs_id`,\n"
                + "    `obs`.`person_id`,\n"
                + "    `obs`.`obs_datetime`,\n"
                + "    `obs`.`concept_id`,\n"
                + "    `obs`.`value_numeric`,\n"
                + "     obs.value_coded,\n"
                + "     obs.value_text,\n"
                + "     obs.value_datetime,\n"
                + "     obs.value_boolean,\n"
                + "     cast(coalesce(obs.value_coded,`obs`.`value_numeric`,`obs`.`value_datetime`,`obs`.`value_text`) as char charset utf8) AS `variable_value`,\n"
                + "     `obs`.`date_created`,\n"
                + "      encounter.date_changed,\n"
                + "      encounter.date_voided,\n"
                + "      encounter.changed_by,\n"
                + "      obs.voided,\n"
                + "      encounter.voided_by,\n"
                + "     `obs`.`creator` AS `creator`,\n"
                + "     `obs`.`encounter_id` AS `encounter_id`,\n"
                + "     `obs`.`obs_group_id` AS `obs_group_id`,\n"
                + "     `obs`.`uuid`,\n"
                + "     `encounter`.`form_id` AS `form_id`,\n"
                + "     `encounter`.`provider_id` AS `provider_id`,\n"
                + "     `obs`.`location_id` AS `location_id` \n"
                + "     from `obs` \n"
                + "	inner join `patient` on(`patient`.`patient_id` = `obs`.`person_id`)\n"
                + "     inner join `encounter` on(`encounter`.`encounter_id` = `obs`.`encounter_id`)\n"
                + "     inner join (select \n"
                + "	obs.person_id,\n"
                + "	obs.concept_id,\n"
                + "	MAX(obs.obs_datetime) as date_obs\n"
                + "     from obs where obs.voided=0 and obs.concept_id=? and obs.person_id in(" + buildString(idSet) + ") AND obs.obs_datetime <=? GROUP BY obs.person_id, obs.concept_id) \n"
                + "	sinner on(sinner.person_id=obs.person_id and sinner.concept_id=obs.concept_id and encounter.encounter_datetime=sinner.date_obs)\n"
                + "	where obs.concept_id=? and encounter.voided=0 and encounter.encounter_datetime<=? AND encounter.patient_id in (" + buildString(idSet) + ") order by encounter.patient_id";

        PreparedStatement ps = null;
        ResultSet rs = null;
        Obs obs = null;
        try {
            ps = prepareQuery(sql_text);
            ps.setInt(1, conceptID);
            //ps.setDate(2,convertToSQLDate(startDate));
            ps.setDate(2, convertToSQLDate(endDate));
            ps.setInt(3, conceptID);
            ps.setDate(4, convertToSQLDate(endDate));
            //ps.setInt(3,locationID);
            rs = ps.executeQuery();
            while (rs.next()) {
                obs = constructObs2(rs);
                obsList.put(obs.getPatientID(), obs);
                //System.out.println("Patient ID Obs: "+obs.getVariableName()+" Value: "+obs.getVariableValue());
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }
        return obsList;
    }

    public void runPediatricPatientMonitoringDuringReviewPeriod(Date startDate, Date endDate, model.datapump.Location loc, File file, Set<Integer> idSet) {
        screen.updateStatus("Writing PediatricPatientMonitoringReviewPeriod.xml...");
        Integer[] conceptArr = {88, 1153, 85, 860, 313, 329, 309};
        int location_id = loc.getLocationID();
        Set<Integer> conceptSet = new HashSet<Integer>(Arrays.asList(conceptArr));
        ArrayList<model.datapump.Obs> obsList = getAllObsForConcepts(conceptSet, idSet, startDate, endDate);
        HashMap<Integer, Obs> viralLoadMap = getLastOfObsForPatients(endDate, location_id, idSet, 315);
        ArrayList<PediatricPatientMonitoringDuringReviewPeriod> pmList = nigeriaQualWriter.createPediatricPatientMonitoringReviewPeriod(idSet, patientDemoMap, obsList, loc, viralLoadMap);
        String fileName = "PediatricPatientMonitoringReviewPeriod.xml";
        zipFileEntryNames.add(fileName);
        int count = 0;
        try {
            mgr.createXMLWriter(fileName);
            mgr.openXMLDocument();
            mgr.writeXMLHeader(COMMON_HEADER, NAMES_SPACE);
            for (PediatricPatientMonitoringDuringReviewPeriod pm : pmList) {
                mgr.writeToXML(pm);
                count++;
                screen.updateStatus("Writing PediatricPatientMonitoringReviewPeriod.xml..." + count);
            }
            mgr.endXMLDocument();
            mgr.closeXMLWriter();
        } catch (XMLStreamException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void runPediatricCotrimoxazole(Date startDate, Date endDate, model.datapump.Location loc, File file, Set<Integer> idSet) {
        screen.updateStatus("Writing PediatricCotrimoxazoleReportingPeriod.xml...");
        //ArrayList<model.datapump.PatientRegimen> ptsCotrimRegimenList = getFirstReceivedCotrimReviewPeriod(startDate, endDate, location_id, idSet);
        int location_id = loc.getLocationID();
        HashMap<Integer, Date> ptsFirstCTXDate = getFirstReceivedCotrimReviewPeriod(startDate, endDate, location_id, idSet);
        PediatricCotrimoxazole dc = null;
        String fileName = "PediatricCotrimoxazoleReportingPeriod.xml";
        zipFileEntryNames.add(fileName);
        int count = 0;
        try {
            mgr.createXMLWriter(fileName);
            mgr.openXMLDocument();
            mgr.writeXMLHeader(COMMON_HEADER, NAMES_SPACE);
            for (Integer ele : idSet) {
                dc = nigeriaQualWriter.createPediatricCotrimoxazole(patientDemoMap, ele, ptsFirstCTXDate, loc);
                mgr.writeToXML(dc);
                screen.updateStatus("Writing PediatricCotrimoxazoleReportingPeriod.xml..." + count);
                count++;
            }
            mgr.endXMLDocument();
            mgr.closeXMLWriter();
        } catch (XMLStreamException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public HashMap<Integer, Date> getFirstReceivedCotrimReviewPeriod(Date startDate, Date endDate, int locationID, Set<Integer> idSet) {
        /*String sql_text="SELECT * FROM regimen\n" +
"INNER JOIN \n" +
"(SELECT PATIENT_ID,MIN(VISIT_DATE) FST_CTX_DT FROM regimen WHERE DRUGS LIKE '%Cotri%' AND VISIT_DATE BETWEEN ? AND ? AND PATIENT_ID IN("+buildString(idSet)+") GROUP BY PATIENT_ID) SINNER\n" +
"ON(SINNER.PATIENT_ID=regimen.PATIENT_ID AND SINNER.FST_CTX_DT=regimen.VISIT_DATE AND regimen.DRUGS LIKE '%Cotri%')";*/
        HashMap<Integer, Date> ptsFirstDateMap = new HashMap<Integer, Date>();
        String sql_text = "select \n"
                + "obs.person_id,\n"
                + "obs.concept_id,\n"
                + "MIN(obs.obs_datetime) as first_ctx_dt\n"
                + "from\n"
                + "obs \n"
                + "where \n"
                + "obs.concept_id in (7778364,7778203) \n"
                + "and \n"
                + "obs.value_coded in (963,1547,1552,7778548)\n"
                + "and\n"
                + "obs.voided=0\n"
                + "and \n"
                + "obs.person_id in(" + buildString(idSet) + ")\n"
                + "and \n"
                + "obs.obs_datetime<=? \n"
                + "\n"
                + "\n"
                + "group by obs.person_id,obs.concept_id";
        PreparedStatement ps = null;
        ResultSet rs = null;
        //model.datapump.PatientRegimen ptsRegimen = null;
        //ArrayList<model.datapump.PatientRegimen> ptsCotrimRegimen = new ArrayList<model.datapump.PatientRegimen>();
        try {
            ps = prepareQuery(sql_text);
            //ps.setDate(1, convertToSQLDate(startDate));
            ps.setDate(1, convertToSQLDate(endDate));
            rs = ps.executeQuery();
            while (rs.next()) {
                ptsFirstDateMap.put(rs.getInt("person_id"), rs.getDate("first_ctx_dt"));
                //ptsRegimen = constructRegimen(rs);
                //ptsCotrimRegimen.add(ptsRegimen);
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }
        return ptsFirstDateMap;
    }

    public ArrayList<model.datapump.PatientRegimen> getFirstReceivedCotrimReviewPeriod2(Date startDate, Date endDate, int locationID, Set<Integer> idSet) {
        /*String sql_text="SELECT * FROM regimen\n" +
"INNER JOIN \n" +
"(SELECT PATIENT_ID,MIN(VISIT_DATE) FST_CTX_DT FROM regimen WHERE DRUGS LIKE '%Cotri%' AND VISIT_DATE BETWEEN ? AND ? AND PATIENT_ID IN("+buildString(idSet)+") GROUP BY PATIENT_ID) SINNER\n" +
"ON(SINNER.PATIENT_ID=regimen.PATIENT_ID AND SINNER.FST_CTX_DT=regimen.VISIT_DATE AND regimen.DRUGS LIKE '%Cotri%')";*/
        HashMap<Integer, Date> ptsFirstDateMap = new HashMap<Integer, Date>();
        String sql_text = "select \n"
                + "obs.person_id,\n"
                + "obs.concept_id,\n"
                + "MIN(obs.obs_datetime) as first_ctx_dt\n"
                + "from\n"
                + "obs \n"
                + "where \n"
                + "obs.concept_id=7778364 \n"
                + "and \n"
                + "obs.value_coded=963\n"
                + "and\n"
                + "obs.voided=0\n"
                + "and \n"
                + "obs.person_id in(" + buildString(idSet) + ")\n"
                + "\n"
                + "\n"
                + "group by obs.person_id,obs.concept_id";
        PreparedStatement ps = null;
        ResultSet rs = null;
        model.datapump.PatientRegimen ptsRegimen = null;
        ArrayList<model.datapump.PatientRegimen> ptsCotrimRegimen = new ArrayList<model.datapump.PatientRegimen>();
        try {
            ps = prepareQuery(sql_text);
            ps.setDate(1, convertToSQLDate(startDate));
            ps.setDate(2, convertToSQLDate(endDate));
            rs = ps.executeQuery();
            while (rs.next()) {
                ptsRegimen = constructRegimen(rs);
                ptsCotrimRegimen.add(ptsRegimen);
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }
        return ptsCotrimRegimen;
    }

    public void runPedClinicalEvalExport(Date startDate, Date endDate, model.datapump.Location loc, File file, Set<Integer> idSet) {
        screen.updateStatus("Writing Pediatric_ClinicalEvaluationInReviewPeriod.xml...");
        Integer[] formArr = {18, 24, 27, 20, 56};
        Set<Integer> formSet = new HashSet<Integer>(Arrays.asList(formArr));
        PediatricClinicalEvaluationInReviewPeriod dc = null;
        int locationID = loc.getLocationID();
        ArrayList<Form> formList = getFormForPatient(idSet, formSet, startDate, endDate, locationID);
        System.out.println(formList.size());
        String pepfarID = "";
        model.datapump.Demographics demo = null;
        String fileName = "Pediatric_ClinicalEvaluationInReviewPeriod.xml";
        zipFileEntryNames.add(fileName);
        int count = 0;
        try {
            mgr.createXMLWriter(fileName);
            mgr.openXMLDocument();
            mgr.writeXMLHeader(COMMON_HEADER, NAMES_SPACE);
            for (int pid : idSet) {
                demo = patientDemoMap.get(pid);
                pepfarID = demo.getPepfarID();
                dc = nigeriaQualWriter.createPediatricClinicalEvalReviewPeriod(formList, pid, pepfarID, loc);
                screen.updateStatus("Writing Pediatric_ClinicalEvaluationInReviewPeriod.xml..." + count);
                mgr.writeToXML(dc);
                count++;
            }
            mgr.endXMLDocument();
            mgr.closeXMLWriter();
        } catch (XMLStreamException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void runPedRegimenDuringReviewPeriod(Date startDate, Date endDate, model.datapump.Location loc, File file, Set<Integer> idSet) {
        screen.updateStatus("Writing Pediatric_ARTRegimenSinceStartingTreatment.xml...Please wait");
        ArrayList<Integer> patientOnARTFirstDayList = null, patientOnARTAnytimeList = null;
        ArrayList<model.datapump.PatientRegimen> ptsRegimenList = null;
        int location_id = loc.getLocationID();
        ArrayList<PediatricARTRegimenSinceStartingTreatment> dataRegimenDuringReviewList = null;
        patientOnARTFirstDayList = getPatientsOnARTFirstDayOfReview(startDate, endDate, location_id);
        patientOnARTAnytimeList = getPatientsOnARTAnytimeOfReviewPeriod(startDate, endDate, location_id);
        ptsRegimenList = getAllPatientRegimen(startDate, endDate, location_id, idSet);
        dataRegimenDuringReviewList = nigeriaQualWriter.createPedARTRegimen(patientDemoMap, ptsRegimenList, patientOnARTFirstDayList, patientOnARTAnytimeList, loc, idSet);//DataARTRegimenDuringReviewPeriod
        String fileName = "Pediatric_ARTRegimenSinceStartingTreatment.xml";
        zipFileEntryNames.add(fileName);
        int count = 0;
        try {
            mgr.createXMLWriter(fileName);
            mgr.openXMLDocument();
            mgr.writeXMLHeader(COMMON_HEADER, NAMES_SPACE);

            for (PediatricARTRegimenSinceStartingTreatment drr : dataRegimenDuringReviewList) {
                mgr.writeToXML(drr);
                count++;
                screen.updateStatus("Writing Pediatric_ARTRegimenSinceStartingTreatment.xml...Please wait " + count);
            }
            mgr.endXMLDocument();
            mgr.closeXMLWriter();
        } catch (XMLStreamException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void runPediatricBaselineParameterExport(Date startDate, Date endDate, model.datapump.Location loc, File file, Set<Integer> idSet) {
        screen.updateStatus("Writing Pediatric_BaselineParameters.xml file...Please wait");
        long startTime = System.currentTimeMillis();
        ArrayList<PediatricBaselineParameters> dataBaselineParaList = new ArrayList<PediatricBaselineParameters>();
        ArrayList<model.datapump.Obs> obsList = null;
        PediatricBaselineParameters dataBaselinePara = null;
        Set<Integer> ptsSet = patientDemoMap.keySet();
        obsList = getAllBaselines(idSet);
        int count = 0;
        Date artStartDate = null;
        model.datapump.PatientRegimen firstRegimen = null;
        for (int ele : ptsSet) {
            if (idSet.contains(ele)) {
                dataBaselinePara = nigeriaQualWriter.createPediatricBaselineParameters(ele, obsList, loc);
                if (onART.contains(ele)) {
                    dataBaselinePara.setPatientEverStartedOnART("YES");
                    artStartDate = artStartDateDictionary.get(ele);
                    if (artStartDate == null) {
                        firstRegimen = firstRegimenDictionary.get(ele);
                        if (firstRegimen != null) {
                            artStartDate = firstRegimen.getStartDate();
                        }
                    }
                    dataBaselinePara.setArtStartDate(artStartDate);
                } else {
                    dataBaselinePara.setPatientEverStartedOnART("NO");
                }
                dataBaselineParaList.add(dataBaselinePara);
            }

        }
        String fileName = "Pediatric_BaselineParameters.xml";
        try {
            mgr.createXMLWriter(fileName);
            mgr.openXMLDocument();
            mgr.writeXMLHeader(COMMON_HEADER, NAMES_SPACE);
            zipFileEntryNames.add(fileName);
            for (PediatricBaselineParameters dbp : dataBaselineParaList) {
                mgr.writeToXML(dbp);
                count++;
                screen.updateStatus("Writing Pediatric_BaselineParameters.xml file... " + count + " records written");
            }
            mgr.endXMLDocument();
            mgr.closeXMLWriter();
            long duration = calculateDuration(startTime);
            screen.updateStatus("Pediatric_BaselineParameters.xml file completed in " + duration + " secs ...Wait");
        } catch (IOException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } catch (XMLStreamException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void loadFirstRegimens(Set<Integer> idSet) {
        ResultSet rs = null;
        Statement stmt = null;
        firstRegimenDictionary = new HashMap<Integer, model.datapump.PatientRegimen>();
        screen.updateStatus("Loading first regimens...");

        /* String sql_text="select regimen.*,COALESCE(regimen.first_line,regimen.second_line) as regimen,s1.drug_name,s1.duration,s1.duration_unit from regimen \n" +
"inner join (select drug.patient_id, drug.dispensed_date,group_concat(drug.drug_name) as drug_name,drug.duration,drug.duration_unit \n" +
"from drug GROUP BY drug.patient_id,drug.dispensed_date) s1 on(s1.patient_id=regimen.patient_id and s1.dispensed_date=regimen.visit_date)\n" +
"inner join (select regimen.patient_id,MIN(regimen.visit_date) as first_regimen_dt from regimen where regimen.first_line<>'' || regimen.second_line<>'' group by regimen.patient_id) s2\n" +
"on(s2.patient_id=regimen.patient_id and s2.first_regimen_dt=regimen.visit_date)";*/
 /*String sql_text = "select \n"
                + "    `obs`.`obs_id`,\n"
                + "    `obs`.`person_id`,\n"
                + "    `obs`.`obs_datetime`,\n"
                + "    `obs`.`concept_id`,\n"
                + "    `obs`.`value_numeric`,\n"
                + "     obs.value_coded,\n"
                + "     obs.value_text,\n"
                + "     obs.value_datetime,\n"
                + "     cast(coalesce(obs.value_coded,`obs`.`value_numeric`,`obs`.`value_datetime`,`obs`.`value_text`) as char charset utf8) AS `variable_value`,\n"
                + "     `obs`.`date_created`,\n"
                + "      encounter.date_changed,\n"
                + "      encounter.date_voided,\n"
                + "      encounter.changed_by,\n"
                + "      obs.voided,\n"
                + "      encounter.voided_by,\n"
                + "     `obs`.`creator` AS `creator`,\n"
                + "     `obs`.`encounter_id` AS `encounter_id`,\n"
                + "     `obs`.`obs_group_id` AS `obs_group_id`,\n"
                + "     `obs`.`uuid`,\n"
                + "     `encounter`.`form_id` AS `form_id`,\n"
                + "     `encounter_provider`.`provider_id` AS `provider_id`,\n"
                + "     `obs`.`location_id` AS `location_id` \n"
                + "     from `encounter` \n"
                //+ "     inner join `encounter` on(`encounter`.`encounter_id` = `obs`.`encounter_id`)\n"
                + "       inner join `obs` on(`encounter`.`encounter_id` = `obs`.`encounter_id`)\n"
                + "       inner join `encounter_provider` on(`encounter_provider`.`encounter_id` = `encounter`.`encounter_id`)\n"
                + "     inner join (select \n"
                + "	obs.person_id,\n"
                + "	obs.concept_id,\n"
                + "	DATE(MIN(obs.obs_datetime)) as date_obs\n"
                + "     from obs where obs.voided=0 and obs.concept_id in(7778111,7778531) and obs.person_id in(" + buildString(idSet) + ") GROUP BY obs.person_id) \n"
                + "	sinner on(sinner.person_id=obs.person_id and sinner.concept_id=obs.concept_id and DATE(encounter.encounter_datetime)=sinner.date_obs)\n"
                + "	where obs.concept_id in(7778111,7778531) and encounter.patient_id in(" + buildString(idSet) + ") and encounter.voided=0 order by encounter.patient_id;";*/
        String sql_text = "select \n"
                + "orders.patient_id,\n"
                + "pid1 .identifier as pepfar_id,\n"
                + "pid2 .identifier as hosp_id,\n"
                + " orders.start_date as visit_date,\n"
                + " GROUP_CONCAT(drug.name SEPARATOR '/') as drug_name,\n"
                + " orders.start_date as FirstPickupDate, \n"
                + "orders.discontinued_date as stop_date, \n"
                + "drug_order.frequency,\n"
                + "drug_order.quantity,\n"
                + "orders.date_created as date_entered,\n"
                + "orders.creator as creator_id,\n"
                + "drug_order.dose \n"
                + "from orders\n"
                + "left join drug_order on(drug_order.order_id=orders.order_id and orders.voided=0)\n"
                + "left join drug on(drug.drug_id=drug_order.drug_inventory_id )\n"
                + "left join patient_identifier pid1 on(orders.patient_id=pid1 .patient_id and pid1 .identifier_type=3)\n"
                + "left join patient_identifier pid2 on(orders.patient_id=pid1 .patient_id and pid2 .identifier_type=6)\n"
                + "left join (\n"
                + " select orders.patient_id, MIN(orders.start_date) as firstRegimenDate from orders where \n"
                + "orders.voided=0  and orders.concept_id \n"
                + "IN (953,956,952,955,960,1186,959,1213,1187,\n"
                + "1188,957,7777942,7778040,23,1184,1219,\n"
                + "7778238,7778239,7777817,1211,7778240,\n"
                + "7778159,1226,1224,1223,7778241,1232,\n"
                + "1233,7778242,7778243,7778244,7778245,\n"
                + "7778246,7778247,7777880,7777919,7777918,\n"
                + "7777921,7777920,1225,1220,17,1222,7778502,\n"
                + "7778503,1533,598,7778849,1191,7778830)\n"
                + "GROUP BY orders.patient_id\n"
                + ") as smin on(smin.patient_id=orders.patient_id and smin.firstRegimenDate=orders.start_date)\n"
                + "where orders.voided=0   and orders.concept_id \n"
                + "IN (953,956,952,955,960,1186,959,1213,1187,\n"
                + "1188,957,7777942,7778040,23,1184,1219,\n"
                + "7778238,7778239,7777817,1211,7778240,\n"
                + "7778159,1226,1224,1223,7778241,1232,\n"
                + "1233,7778242,7778243,7778244,7778245,\n"
                + "7778246,7778247,7777880,7777919,7777918,\n"
                + "7777921,7777920,1225,1220,17,1222,7778502,\n"
                + "7778503,1533,598,7778849,1191,7778830) GROUP BY orders.patient_id,orders.start_date";
        int count = 0;
        Obs obs = null;
        ArrayList<Obs> obsList = null;
        model.datapump.PatientRegimen ptsRegimen = null;
        /*HashMap<Integer, ArrayList<Obs>> firstRegimenMap = new HashMap<Integer, ArrayList<Obs>>();
        for (Integer ele : idSet) {
            obsList = new ArrayList<Obs>();
            firstRegimenMap.put(ele, obsList);
        }*/
        try {
            stmt = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            rs = stmt.executeQuery(sql_text);

            //ps = prepareQuery(sql_text);
            //rs = ps.executeQuery();
            while (rs.next()) {
                //firstRegimenDictionary.put(rs.getInt("patient_id"), constructRegimen(rs));
                ptsRegimen = constructRegimen2(rs);//uctObs2(rs);
                firstRegimenDictionary.put(ptsRegimen.getPatientID(), ptsRegimen);
                //obsList.add(obs);
                //firstRegimenMap.get(obs.getPatientID()).add(obs);
                screen.updateStatus("first regimens..." + count);
                count++;
            }
            rs.close();
            stmt.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            screen.updateStatus(ex.getMessage());
        } finally {
            cleanUp(rs, stmt);
        }
        /*PatientRegimen ptsRegimen = null;
        for (Integer id : idSet) {
            if (!firstRegimenMap.get(id).isEmpty()) {
                ptsRegimen = extractPatientRegimen(firstRegimenMap.get(id), id);
                if (StringUtils.isNotEmpty(ptsRegimen.getRegimenLine())) {
                    firstRegimenDictionary.put(id, ptsRegimen);
                }

            }

        }*/
        //firstRegimenDictionary=firstRegimenMap;
    }

    public void loadONARTPatients() {
        //String sql_text="SELECT DISTINCT PATIENT_ID FROM regimen WHERE REGIMEN_CODE is not null";
        String sql_text = "select DISTINCT obs.person_id from obs where concept_id in(164506,164513,165702,165708,164507,164514,165703) and obs.voided=0";
        Statement stmt = null;
        ResultSet rs = null;
        onART = new HashSet<Integer>();
        try {
            stmt = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            rs = stmt.executeQuery(sql_text);
            screen.updateStatus("Loading ON ART patients.....");
            while (rs.next()) {
                onART.add(rs.getInt("person_id"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            cleanUp(rs, stmt);
        }
    }

    public void runPediatricARTAdherence(Date startDate, Date endDate, model.datapump.Location loc, File file, Set<Integer> idSet) {
        screen.updateStatus("Writing Pediatric_ART_Adherence.xml...Please wait");
        long startTime = System.currentTimeMillis();
        int location_id = loc.getLocationID();
        //HashMap<Integer,Obs> maxCD4Map=getAllMaxCD4Count(location_id);
        HashMap<Integer, model.datapump.Obs> adherenceMap = getAdherenceLast3Months(startDate, endDate, location_id, idSet);
        ArrayList<PediatricARTAdherence> dataARTAdhList = nigeriaQualWriter.createPediatricARTAdherenceList(patientDemoMap, adherenceMap, loc, idSet);
        int count = 0;
        String fileName = "Pediatric_ART_Adherence.xml";
        zipFileEntryNames.add(fileName);
        try {
            mgr.createXMLWriter(fileName);
            mgr.openXMLDocument();
            mgr.writeXMLHeader(COMMON_HEADER, NAMES_SPACE);
            for (PediatricARTAdherence ele : dataARTAdhList) {
                mgr.writeToXML(ele);
                count++;
                screen.updateStatus("Writing Pediatric_ART_Adherence.xml...Please wait " + count);
            }
            mgr.endXMLDocument();
            mgr.closeXMLWriter();
            long duration = calculateDuration(startTime);
            screen.updateStatus("Pediatric_ART_Adherence.xml Completed " + duration + " secs");

        } catch (XMLStreamException ex) {
            ex.printStackTrace();
            screen.updateStatus(ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
            screen.updateStatus(ex.getMessage());
        }
    }

    public void runPediatricPatientDemographics(Date startDate, Date endDate, model.datapump.Location loc, File file, Set<Integer> idSet) {
        screen.updateStatus("Writing PediatricPatientDemographics.xml file...Please wait");
        long startTime = System.currentTimeMillis();
        int location_id = loc.getLocationID();
        ArrayList<model.datapump.Obs> personalHistoryList = getAllPersonalHistoryObs(location_id, idSet);
        ArrayList<Integer> clinicVisitReportingPeriod = getClinicVisitReportingPeriod(startDate, endDate, location_id);
        ArrayList<Integer> hospitalizedReportingPeriod = getHospitalizedReportingPeriod(startDate, endDate, location_id);
        //HashMap<Integer,Date> lastVisitMap=getDateOfLastVisit(startDate, endDate, idSet);
        PedPatientDemographics dataPatientDemo = null;
        ArrayList<PedPatientDemographics> pedPatientDemoList = new ArrayList<PedPatientDemographics>();
        for (model.datapump.Demographics ele : patientDemoList) {
            if (idSet.contains(ele.getPatientID()) && !(ele.getPepfarID().isEmpty())) {
                dataPatientDemo = nigeriaQualWriter.createPedPatientDemographics(ele, personalHistoryList, hospitalizedReportingPeriod, clinicVisitReportingPeriod, firstVisitDateDictionary, lastVisitDateDictionary, loc);
                pedPatientDemoList.add(dataPatientDemo);
            }
        }
        String fileName = "PediatricPatientDemographics.xml";
        int count = 0;
        zipFileEntryNames.add(fileName);

        try {
            mgr.createXMLWriter(fileName);
            mgr.openXMLDocument();
            mgr.writeXMLHeader(COMMON_HEADER, NAMES_SPACE);
            for (PedPatientDemographics ele : pedPatientDemoList) {
                mgr.writeToXML(ele);
                count++;
                screen.updateStatus("Writing PediatricPatientDemographics.xml file.." + count + " files written");

            }
            mgr.endXMLDocument();
            mgr.closeXMLWriter();
            long duration = calculateDuration(startTime);
            screen.updateStatus("PediatricPatientDemographics.xml completed in " + duration + " secs");
        } catch (XMLStreamException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public Set<Integer> getSamplePedPatients(Date startDate, Date endDate, int locationID) {
        Set<Integer> idSet = new HashSet<Integer>();
        Set<Integer> sampleSet = new HashSet<Integer>();
        /*String sql_text = "select patient.person_source_pk as patient_id from patient where timestampdiff(YEAR,dob,curdate())<15 and pepfar_id is not null\n"
                + "and dob is not null and coalesce(adult_enrollment_dt,pead_enrollment_dt) is not null\n"
                + "and (person_source_pk IN(\n"
                + "select obs.PATIENT_ID from obs where TIMESTAMPDIFF(MONTH,VISIT_DATE,?)<=6 and obs.VOIDED=0 and obs.location_id=?)\n"
                + "OR\n"
                + "person_source_pk IN(select patient_id from regimen where regimen is not null and regimen_code<>-1 \n"
                + "and TIMESTAMPDIFF(YEAR, VISIT_DATE,?)<=3)); ";*/
        String sql_text = "select \n"
                + "patient.patient_id\n"
                + "from patient\n"
                + "inner join person on(person.person_id=patient.patient_id)\n"
                + "left join patient_program on(patient_program.patient_id=patient.patient_id and patient_program.program_id=3)\n"
                + "left join encounter on(encounter.patient_id=patient.patient_id)\n"
                + "left join patient_identifier \n"
                + "on(patient_identifier.patient_id=patient.patient_id and patient_identifier.identifier_type=4)\n"
                + "where\n"
                + "TIMESTAMPDIFF(YEAR,person.birthdate,curdate())<15\n"
                + "and\n"
                + "TIMESTAMPDIFF(MONTH,encounter.encounter_datetime,?)<=6\n"
                + "and \n"
                + "patient_identifier.identifier_type=4\n"
                + "and\n"
                + "patient_identifier.identifier is not null\n"
                + "and\n"
                + "patient_program.program_id=3\n"
                + "and\n"
                + "patient_program.date_enrolled is not null\n"
                + "and\n"
                + "patient.voided=0\n"
                + "and\n"
                + "encounter.voided=0\n"
                + "and \n"
                + "person.birthdate is not null";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = prepareQuery(sql_text);
            ps.setDate(1, convertToSQLDate(startDate));
            //ps.setInt(2, locationID);
            //ps.setDate(3, convertToSQLDate(startDate));
            rs = ps.executeQuery();
            while (rs.next()) {
                idSet.add(rs.getInt("patient_id"));
            }
            rs.close();
            ps.close();
            int sampleSize = getSampleSize(idSet.size());
            System.out.println("Sample size: " + sampleSize);
            System.out.println("Set size: " + idSet.size());
            /*Iterator<Integer> it = idSet.iterator();
            int count = 0;
            while (it.hasNext() && count <= idSet.size() && count <= sampleSize) {
                /*if(count==sampleSize){
                    break;
                }*/
 /* int pid = getRandom(idSet);
                if (!sampleSet.contains(pid)) {
                    sampleSet.add(pid);
                    System.out.println("Patient ID added: " + pid);
                }
                count++;
            }*/
            sampleSet = getRandomPatients(idSet, sampleSize);
            rs.close();
            ps.close();

        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }
        return sampleSet;
    }

    public void runNDRExport2(Date startDate, Date endDate, File file, model.datapump.Location loc) {
        JAXBContext jaxbContext = null;
        Container container = null;
        IndividualReportType individual = null;
        PatientDemographicsType patientDemographicType = null;
        ConditionType conditionType = null;
        ProgramAreaType pa = null;
        CommonQuestionsType common = null;
        ConditionSpecificQuestionsType disease = null;
        HIVQuestionsType hivQuestionType = null;
        RegimenType regimenType = null;
        EncountersType encounterType = null;
        ArrayList<model.datapump.DrugOrder> orders = null;
        HIVEncounterType hivEncounterType = null;
        LaboratoryReportType labReportType = null;
        LaboratoryOrderAndResult labOrderAndResult = null;
        Visit visit = null;
        String fileName = file.getAbsolutePath();
        NDRMasterDictionary NDRDictionary = null;
        errorHandler = new CustomErrorHandler();
        List<RegimenType> regimenTypeList = new ArrayList<RegimenType>();
        Date date = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        SimpleDateFormat dateForma2t = new SimpleDateFormat("HHmmss.ms");
        File file2 = null;
        int count = 0, count2 = 0, size = 0;
        ArrayList<model.datapump.Demographics> patients = null;
        List<Obs> commonQuestionObsList, phamarcyObsList, clinicalObsList, ptsObsList, obsList;
        List<BiometricInfo> ptsBiometricInfo = null;
        ArrayList<model.datapump.Drugs> drugList = null;
        ArrayList<model.datapump.Visit> allVisitList = null;
        ArrayList<model.datapump.Visit> ptsVisitList = null;
        ArrayList<model.datapump.Obs> labObsList = null;
        FingerPrintType fingerPrintType = null;
        String ipName = "", ipCode = "";
        try {
            NDRDictionary = new NDRMasterDictionary();
            NDRDictionary.initializeLogFile();
            jaxbContext = NDRDictionary.createJAXBContext();
            Marshaller jaxbMarshaller = NDRDictionary.createMarshaller(jaxbContext);
            // Get all patients with changes in encounter or demographics
            Set<Integer> idSet = getAllPatientsInDBWithChange(startDate, endDate);
            loadFirstRegimens(idSet); // Load the first regimen of all identified patients
            patients = getAllPatientsInDB(loc.getLocationID(), idSet); // Load Demographics Information
            commonQuestionObsList = getCommonQuestionObs(idSet);
            size = patients.size();
            NDRDictionary.setConceptDictionary(conceptDictionaryRL);
            screen.updateMinMaxProgress(0, patients.size());
            allVisitList = loadAllVisit(idSet);
            for (model.datapump.Demographics pts : patients) {
                int patientID = pts.getPatientID();
                if (!pts.getPepfarID().isEmpty() && !pts.getGender().isEmpty() && pts.getDateOfBirth() != null) {
                    //Get IP Name and IP Code from global properties
                    ipName = propertyMap.get("partner_full_name");
                    ipCode = propertyMap.get("partner_short_name");
                    container = NDRDictionary.createContainer(ipName, ipCode, "UPDATED");
                    //ptsBiometricInfo = getBiometricInfoForPatient(patientID);
                    //if (!ptsBiometricInfo.isEmpty()) {
                    //fingerPrintType = NDRDictionary.createFingerPrintType(ptsBiometricInfo);
                    //}
                    individual = NDRDictionary.createIndividualReport();
                    ptsObsList = getCommonQuestionsForPatient(patientID, commonQuestionObsList);
                    obsList = getPersonalHistoryObs(pts.getPatientID(), loc.getLocationID());
                    patientDemographicType = NDRDictionary.createPatientDemographics(pts, loc, obsList, locMap);
                    //patientDemographicType.setFingerPrints(fingerPrintType);
                    individual.setPatientDemographics(patientDemographicType);
                    conditionType = NDRDictionary.createConiditionTypeWithProgramArea(pts);
                    Boolean deaseased = patientDemographicType.isPatientDeceasedIndicator();
                    if (deaseased == null) {
                        deaseased = false;
                    }
                    common = NDRDictionary.createCommonQuestionType(pts.getHospID(), ptsObsList, firstVisitDateDictionary.get(pts.getPatientID()), lastVisitDateDictionary.get(pts.getPatientID()), deaseased, pts.getAge(), pts.getGender());
                    conditionType.setCommonQuestions(common);
                    Date enrollDate = NDRDictionary.getEnrollmentDateForPatient(pts, firstVisitDateDictionary);
                    disease = NDRDictionary.createConditionSpecificQuestionType();
                    hivQuestionType = NDRDictionary.createHIVQuestionType(firstRegimenDictionary.get(pts.getPatientID()), artStartDateDictionary.get(pts.getPatientID()), enrollDate, isOnART(pts.getPatientID()), obsList);
                    disease.setHIVQuestions(hivQuestionType);
                    conditionType.setConditionSpecificQuestions(disease);
                    phamarcyObsList = getObsFromPharmacyForPatient(patientID);
                    orders = getDrugOrderForPatient(pts.getPatientID(), phamarcyObsList);
                    regimenTypeList = NDRDictionary.createRegimenTypeList(pts, phamarcyObsList);
                    encounterType = NDRDictionary.createEncounterType();
                    ptsVisitList = extractVisit(pts.getPatientID(), allVisitList);
                    Date artStartDate = null;
                    labObsList = new ArrayList<model.datapump.Obs>();
                    ArrayList<DrugOrder> ptsOrders = null, ctxInhARVList;
                    for (model.datapump.Visit ele : ptsVisitList) {
                        obsList = getAllObsForVisit(ele);
                        labObsList = extractLabObs(obsList);
                        if ((obsList != null && !obsList.isEmpty()) || (!orders.isEmpty())) {
                            model.datapump.PatientRegimen ptsRgm = firstRegimenDictionary.get(pts.getPatientID());
                            artStartDate = artStartDateDictionary.get(pts.getPatientID());
                            if (artStartDate == null && ptsRgm != null) {
                                artStartDate = ptsRgm.getStartDate();
                            }
                            clinicalObsList = extractClinicalObs(obsList);
                            if (obsList != null && !obsList.isEmpty()) {
                                hivEncounterType = NDRDictionary.createHIVEncounter(ele, artStartDate, obsList, orders, drugList);
                                encounterType.getHIVEncounter().add(hivEncounterType);
                            }
                        }
                        if (!labObsList.isEmpty()) {
                            labReportType = NDRDictionary.createLaboratoryReportType(ele, labObsList, artStartDate);
                            conditionType.getLaboratoryReport().add(labReportType);
                        }
                    }

                    conditionType.setEncounters(encounterType);
                    conditionType.getRegimen().addAll(regimenTypeList);
                    individual.getCondition().add(conditionType);
                    container.setIndividualReport(individual);
                    screen.updateProgress(count);

                    date = new Date();
                    file.mkdir();
                    file2 = new File(file.getAbsoluteFile() + "/" + file.getName() + dateFormat.format(date) + "_" + dateForma2t.format(date) + "_" + StringUtils.replace(pts.getPepfarID().toUpperCase(), "/", "").replaceAll("[^a-zA-Z0-9]", "") + ".xml");
                    NDRDictionary.writeFile(jaxbMarshaller, container, file2, errorHandler);
                    screen.updateStatus("Writing xml...Please wait " + count2 + " of " + patients.size());
                    count2++;

                }
                count++;
            }
            screen.updateStatus("Zipping xml files...");
            date = new Date();

            //CompressUtil.zip(file.getAbsolutePath(), file.getParent() + "/" + loc.getDefaultLoacation()+ formatDate3(date) + dateForma2t.format(date).replaceAll("[^a-zA-Z0-9]", "") + ".zip", false, null);
            //CompressUtil.zip(file.getAbsolutePath(), file.getParent() + "/" + loc.getDefaultLoacation()+ formatDate3(date) + ".zip", false, null);
            //FileUtils.deleteDirectory(file);
            errorHandler.closeMgr();
            screen.updateStatus("XML file completed for " + count2 + " out of " + count + " patients");
            screen.updateProgress(size);
        } catch (DatatypeConfigurationException ex) {
            NDRDictionary.logError(ex, screen);
            handleException(ex);
        } catch (JAXBException ex) {
            NDRDictionary.logError(ex, screen);
            handleException(ex);
        } catch (SAXParseException ex) {
            NDRDictionary.logError(file2, ex, screen);
            handleException(ex);
        } catch (SAXException ex) {
            NDRDictionary.logError(file2, ex, screen);
            handleException(ex);
        } catch (IOException ex) {
            NDRDictionary.logError(ex, screen);
            handleException(ex);
        }
    }

    public void runNDRExport(Date startDate, Date endDate, File file, model.datapump.Location loc) {//File datimIDFile) {
        ndrWriter = new NDRWriter();
        errorHandler = new CustomErrorHandler();
        //loadDictionaries();
        int count = 0;
        int count2 = 0;
        ArrayList<model.datapump.Demographics> patients = null;
        ArrayList<model.datapump.Obs> obsList = null, clinicalObsList = null;
        ArrayList<model.datapump.Obs> commonQuestionObsList = null;
        ArrayList<model.datapump.Obs> cd4ObsList = null;
        ArrayList<model.datapump.Obs> ptsObsList = null;
        ArrayList<model.datapump.DrugOrder> orders = null;
        ArrayList<Integer> encounterList = null;
        ArrayList<RegimenType> regimenTypeList = null;
        ArrayList<model.datapump.Drugs> drugList = null;
        ArrayList<Container> containers = null;
        ArrayList<model.datapump.Visit> allVisitList = null;
        ArrayList<model.datapump.Visit> ptsVisitList = null;
        ArrayList<model.datapump.Obs> labObsList = null;
        String messageID = "";
        String schemaVersion = "1.2";
        JAXBContext jaxbContext = null;
        Container container = null;
        IndividualReportType individual = null;
        PatientDemographicsType patientDemographicType = null;
        ConditionType conditionType = null;
        ProgramAreaType pa = null;
        CommonQuestionsType common = null;
        ConditionSpecificQuestionsType disease = null;
        HIVQuestionsType hivQuestionType = null;
        RegimenType regimenType = null;
        EncountersType encounterType = null;
        HIVEncounterType hivEncounterType = null;
        LaboratoryReportType labReportType = null;
        LaboratoryOrderAndResult labOrderAndResult = null;
        Visit visit = null;
        String fileName = file.getAbsolutePath();
        Date date = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        SimpleDateFormat dateForma2t = new SimpleDateFormat("HHmmss.ms");
        FileManager mgr = new FileManager();
        File file2 = null;
        ArrayList<model.datapump.Obs> phamarcyObsList = new ArrayList<model.datapump.Obs>();

        try {
            mgr.createCSVWriter("errorlog.csv");
            String[] headers = {"ErrorFileName", "ErrorLine", "ErrorPosition", "ErrorPosition", "ErrorMessage"};
            mgr.writeHeader(headers);
            jaxbContext = JAXBContext.newInstance("com.inductivehealth.ndr.schema");
            Marshaller jaxbMarshaller = ndrWriter.createMarshaller(jaxbContext);
            ndrWriter.loadNDRToOMRSDictionary();
            Set<Integer> idSet = getAllPatientsInDBWithChange(startDate, endDate);
            loadFirstRegimens(idSet);
            patients = getAllPatientsInDB(loc.getLocationID(), idSet);
            commonQuestionObsList = getCommonQuestionObs(idSet);
            //loc = locationDictionary.get(location_id);
            //loc.setLocationID(location_id);
            //loc.setLocationName(locationDictionary.get(location_id).getLocationName());
            int size = patients.size();
            ndrWriter.setConceptDictionary(conceptDictionaryRL);
            screen.updateMinMaxProgress(0, patients.size());
            allVisitList = loadAllVisit(idSet);
            for (model.datapump.Demographics pts : patients) {
                int patientID = pts.getPatientID();
                if (!pts.getPepfarID().isEmpty() && !pts.getGender().isEmpty() && pts.getDateOfBirth() != null) {//&& StringUtils.contains(pts.getPepfarID(),"FCT1516")) {
                    messageID = UUID.randomUUID().toString();
                    container = ndrWriter.createContainer("UPDATED", messageID, schemaVersion);
                    individual = ndrWriter.createIndividualReport();
                    ptsObsList = getCommonQuestionsForPatient(patientID, commonQuestionObsList);
                    obsList = getPersonalHistoryObs(pts.getPatientID(), loc.getLocationID());
                    patientDemographicType = ndrWriter.createPatientDemographics(pts, loc, obsList, locMap);

                    individual.setPatientDemographics(patientDemographicType);
                    conditionType = ndrWriter.createConditionType("86406008");
                    conditionType.setPatientAddress(ndrWriter.createAddressType(pts));
                    pa = ndrWriter.createProgramAreaType("HIV");
                    conditionType.setProgramArea(pa);

                    Boolean deaseased = patientDemographicType.isPatientDeceasedIndicator();
                    if (deaseased == null) {
                        deaseased = false;
                    }
                    // if(!obsList.isEmpty()){
                    common = ndrWriter.createCommonQuestionType(pts.getHospID(), ptsObsList, firstVisitDateDictionary.get(pts.getPatientID()), lastVisitDateDictionary.get(pts.getPatientID()), deaseased, pts.getAge(), pts.getGender());
                    conditionType.setCommonQuestions(common);
                    // }
                    Date adultEnrollDate = pts.getAdultEnrollmentDt();
                    // Date peadEnrollDate = pts.getPeadEnrollmentDt();
                    Date enrollDate = null;

                    if (adultEnrollDate != null) {
                        enrollDate = adultEnrollDate;
                    } else {
                        enrollDate = firstVisitDateDictionary.get(pts.getPatientID());
                    }
                    disease = ndrWriter.createConditionSpecificQuestionType();
                    //obsList = getPersonalHistoryObs(pts.getPatientID(), location_id);
                    hivQuestionType = ndrWriter.createHIVQuestionType(firstRegimenDictionary.get(pts.getPatientID()), artStartDateDictionary.get(pts.getPatientID()), enrollDate, isOnART(pts.getPatientID()), obsList);
                    disease.setHIVQuestions(hivQuestionType);
                    conditionType.setConditionSpecificQuestions(disease);
                    phamarcyObsList = getObsFromPharmacyForPatient(patientID);
                    orders = getDrugOrderForPatient(pts.getPatientID(), phamarcyObsList);// extract all regimens
                    drugList = getAllDrugsForPatient(pts.getPatientID(), phamarcyObsList);// extracts individual drugs especially OIs
                    regimenTypeList = ndrWriter.createRegimenTypeList(orders, pts);
                    conditionType.getRegimen().addAll(regimenTypeList);
                    regimenTypeList = ndrWriter.createRegimenTypeListFromDrugs(drugList, pts);
                    conditionType.getRegimen().addAll(regimenTypeList);
                    encounterType = ndrWriter.createEncounterType();

                    ptsVisitList = extractVisit(pts.getPatientID(), allVisitList);
                    Date artStartDate = null;

                    labObsList = new ArrayList<model.datapump.Obs>();
                    ArrayList<DrugOrder> ptsOrders = null, ctxInhARVList;

                    for (model.datapump.Visit ele : ptsVisitList) {
                        obsList = getAllObsForVisit(ele);
                        labObsList = extractLabObs(obsList);
                        //ptsOrders=extractDrugOrderForVisit(ele, orders);
                        //labObsList.addAll(tempObsList);
                        if ((obsList != null && !obsList.isEmpty()) || (!orders.isEmpty())) {
                            model.datapump.PatientRegimen ptsRgm = firstRegimenDictionary.get(pts.getPatientID());
                            artStartDate = artStartDateDictionary.get(pts.getPatientID());
                            if (artStartDate == null && ptsRgm != null) {
                                artStartDate = ptsRgm.getStartDate();
                            }
                            clinicalObsList = extractClinicalObs(obsList);
                            //ctxInhARVList=extractCTXINHARV(orders);
                            //if(!clinicalObsList.isEmpty() ){//|| !ctxInhARVList.isEmpty()){
                            if (obsList != null && !obsList.isEmpty()) {
                                hivEncounterType = ndrWriter.createHIVEncounter(ele, artStartDate, obsList, orders, drugList);
                                encounterType.getHIVEncounter().add(hivEncounterType);
                            }
                        }
                        if (!labObsList.isEmpty()) {
                            labReportType = ndrWriter.createLaboratoryReportType(ele, labObsList, artStartDate);
                            conditionType.getLaboratoryReport().add(labReportType);
                        }
                    }
                    conditionType.setEncounters(encounterType);
                    individual.getCondition().add(conditionType);
                    container.setIndividualReport(individual);
                    screen.updateProgress(count);

                    date = new Date();
                    file.mkdir();
                    file2 = new File(file.getAbsoluteFile() + "/" + file.getName() + dateFormat.format(date) + "_" + dateForma2t.format(date) + "_" + StringUtils.replace(pts.getPepfarID().toUpperCase(), "/", "").replaceAll("[^a-zA-Z0-9]", "") + ".xml");
                    ndrWriter.writeFile(jaxbMarshaller, container, file2, errorHandler);

                    screen.updateStatus("Writing xml...Please wait " + count2 + " of " + patients.size());
                    count2++;
                }
                count++;

            }
            screen.updateStatus("Zipping xml files...");
            date = new Date();

            //CompressUtil.zip(file.getAbsolutePath(), file.getParent() + "/" + loc.getDefaultLoacation()+ formatDate3(date) + dateForma2t.format(date).replaceAll("[^a-zA-Z0-9]", "") + ".zip", false, null);
            //CompressUtil.zip(file.getAbsolutePath(), file.getParent() + "/" + loc.getDefaultLoacation()+ formatDate3(date) + ".zip", false, null);
            //FileUtils.deleteDirectory(file);
            errorHandler.closeMgr();
            screen.updateStatus("XML file completed for " + count2 + " out of " + count + " patients");
            screen.updateProgress(size);

        } catch (DatatypeConfigurationException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } catch (JAXBException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } catch (SAXParseException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
            String[] errorArr = new String[4];
            if (file2 != null) {
                errorArr[0] = file2.getName();
                errorArr[1] = String.valueOf(ex.getLineNumber());
                errorArr[2] = String.valueOf(ex.getColumnNumber());
                errorArr[3] = ex.getMessage();
                mgr.writeHeader(errorArr);
            }
        } catch (SAXException ex) {
            SAXParseException ex2 = (SAXParseException) ex;
            String[] errorArr = new String[4];
            if (file2 != null) {
                errorArr[0] = file2.getName();
                errorArr[1] = String.valueOf(ex2.getLineNumber());
                errorArr[2] = String.valueOf(ex2.getColumnNumber());
                errorArr[3] = ex.getMessage();
                mgr.writeHeader(errorArr);
            }
            screen.updateStatus(ex2.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public ArrayList<model.datapump.Obs> extractLabObs(List<model.datapump.Obs> obsList) {
        ArrayList<model.datapump.Obs> labObsList = new ArrayList<model.datapump.Obs>();
        for (model.datapump.Obs ele : obsList) {
            if (ele.getFormID() == 5 || ele.getFormID() == 3) {// Lab Request Form ID (3), Lab Results Form ID(5)
                labObsList.add(ele);
            }
        }
        return labObsList;
    }

    public String formatDate3(Date date) {
        String strDate = "";
        formatter = new SimpleDateFormat("dd-MMMM-yyyy");
        strDate = formatter.format(date);
        return strDate;
    }

    public boolean isOnART(int patient_id) {
        boolean ans = false;
        if (firstRegimenDictionary.containsKey(patient_id)) {
            ans = true;
        }
        return ans;
    }

    public ArrayList<model.datapump.Obs> getAllObsForVisit(model.datapump.Visit visit) {
        ArrayList<model.datapump.Obs> obsList = new ArrayList<model.datapump.Obs>();
        //String sql_text = "select * from obs where VISIT_DATE=? AND PATIENT_ID=? AND FORM_ID IN(24,18,46,56,72,27,20,53,67,47,1) AND VOIDED=0";

        String sql_text = "select \n"
                + "    `obs`.`obs_id`,\n"
                + "    `obs`.`person_id`,\n"
                + "    `obs`.`obs_datetime`,\n"
                + "    `obs`.`concept_id`,\n"
                + "    `obs`.`value_numeric`,\n"
                + "     obs.value_coded,\n"
                + "     obs.value_text,\n"
                + "     obs.value_datetime,\n"
                + "     cast(coalesce(obs.value_coded,`obs`.`value_numeric`,`obs`.`value_datetime`,`obs`.`value_text`) as char charset utf8) AS `variable_value`,\n"
                + "     `obs`.`date_created`,\n"
                + "      encounter.date_changed,\n"
                + "      encounter.date_voided,\n"
                + "      encounter.changed_by,\n"
                + "      obs.voided,\n"
                + "      encounter.voided_by,\n"
                + "     `obs`.`creator` AS `creator`,\n"
                + "     `obs`.`encounter_id` AS `encounter_id`,\n"
                + "     `obs`.`obs_group_id` AS `obs_group_id`,\n"
                + "     `obs`.`uuid`,\n"
                + "     `encounter`.`form_id` AS `form_id`,\n"
                + "     `encounter_provider`.`provider_id` AS `provider_id`,\n"
                + "     `obs`.`location_id` AS `location_id` \n"
                + "     from `obs` \n"
                + "	inner join `patient` on(`patient`.`patient_id` = `obs`.`person_id`)\n"
                + "     inner join `encounter` on(`encounter`.`encounter_id` = `obs`.`encounter_id`)\n"
                + "     inner join `encounter_provider` on(`encounter_provider`.encounter_id=encounter.encounter_id and encounter.voided=0)\n"
                + "	where encounter.form_id in(22,14,21,23,20,27) and CAST(encounter.encounter_datetime AS DATE)=? and  encounter.patient_id=? and encounter.voided=0   order by obs.person_id\n"
                + "     	 ";
        PreparedStatement ps = null;
        ResultSet rs = null;
        model.datapump.Obs obs = null;
        try {
            ps = prepareQuery(sql_text);
            ps.setDate(1, convertToSQLDate(visit.getVisitDate()));
            ps.setInt(2, visit.getPatientID());
            rs = ps.executeQuery();
            while (rs.next()) {
                obs = constructObs2(rs);
                obsList.add(obs);
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            screen.updateStatus(ex.getMessage());
        } finally {
            cleanUp(rs, ps);
        }
        return obsList;
    }

    public ArrayList<model.datapump.Obs> extractClinicalObs(List<model.datapump.Obs> obsList) {
        ArrayList<model.datapump.Obs> clinicalObsList = new ArrayList<model.datapump.Obs>();
        //int[] cidArr={1734,1735,1733,85,571,84,568,575,265,1256,1741,1742,1013,860,862,528,861,864,865,866,1121,1122,1123,1124,88};
        // 8 - Care/ART Card Follow-up Page
        // 1 - Adult Initial Clinical Evaluation Form
        // 2 - Paediatric Initial Clinical Evaluation Form
        // 5 - Laboratory Results Form
        // 6 - Vital Signs
        int[] fidArr = {8, 1, 2, 5, 6};
        for (model.datapump.Obs obs : obsList) {
            if (Arrays.binarySearch(fidArr, obs.getFormID()) != -1) {
                clinicalObsList.add(obs);
            }
        }
        return clinicalObsList;

    }

    public ArrayList<model.datapump.Visit> extractVisit(int patientID, ArrayList<model.datapump.Visit> allVisitList) {
        ArrayList<model.datapump.Visit> visitList = new ArrayList<model.datapump.Visit>();
        for (model.datapump.Visit vs : allVisitList) {
            if (vs.getPatientID() == patientID) {
                visitList.add(vs);
            }
        }
        return visitList;
    }

    public ArrayList<Drugs> getAllDrugsForPatient(ArrayList<model.datapump.DrugOrder> drugOrderList) {
        ArrayList<Drugs> drugList = new ArrayList<Drugs>();
        for (model.datapump.DrugOrder ele : drugOrderList) {
            if (StringUtils.isNoneEmpty(ele.getDrugName())) {
                drugList.add(ele);
            }

        }
        //Drugs drg = null;
        /*String sql_text = "select * from drug where patient_id = ?";
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;
        try {
            ps = prepareQuery(sql_text);
            ps.setInt(1, patientID);
            rs = ps.executeQuery();
            while (rs.next()) {
                drg = constructDrug(rs);
                drugList.add(drg);
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            screen.updateStatus(ex.getMessage());
        } finally {
            cleanUp(rs, ps);
        }
        return drugList;*/
        return drugList;
    }

    public ArrayList<Drugs> getAllDrugsForPatient(int patientID, ArrayList<model.datapump.Obs> pharmObsList) {
        ArrayList<Drugs> drugList = new ArrayList<Drugs>();
        Drugs drg = null;
        model.datapump.DrugOrder ptsRegimen = null;
        Set<Date> visitDateSet = getAllVisitsFromObsList(pharmObsList);
        Set<Integer> obsGroupConceptSet = null;
        for (Date ele : visitDateSet) {
            obsGroupConceptSet = getAllObsGroupIDsFromList(pharmObsList, ele);
            for (Integer id : obsGroupConceptSet) {
                drg = getDrugObsParameters(id, pharmObsList, ele);
                //drg = (model.datapump.Drugs) ptsRegimen;
                drugList.add(drg);
            }
        }
        /*String sql_text = "select * from drug where patient_id = ?";
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;
        try {
            ps = prepareQuery(sql_text);
            ps.setInt(1, patientID);
            rs = ps.executeQuery();
            while (rs.next()) {
                drg = constructDrug(rs);
                drugList.add(drg);
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            screen.updateStatus(ex.getMessage());
        } finally {
            cleanUp(rs, ps);
        }
        return drugList;*/
        return drugList;
    }

    public Drugs constructDrug(ResultSet rs) throws SQLException {
        Drugs drg = new Drugs();
        drg.setPatientID(rs.getInt("patient_id"));
        drg.setEncounterID(rs.getInt("encounter_id"));
        drg.setFormID(rs.getInt("form_id"));
        drg.setProviderID(rs.getInt("provider_id"));
        drg.setCreatorID(rs.getInt("creator_id"));
        drg.setLocationID(rs.getInt("location_id"));
        drg.setLocation(rs.getString("location"));
        drg.setPepfarID(rs.getString("pepfar_id"));
        drg.setHospID(rs.getString("hosp_id"));
        drg.setPmmForm(rs.getString("pmm_form"));
        drg.setDispensedDate(rs.getDate("dispensed_date"));
        drg.setDispensedBy(rs.getString("dispensed_by"));
        drg.setEnteredBy(rs.getString("entered_by"));
        drg.setDateEntered(rs.getDate("date_entered"));
        drg.setDrugName(rs.getString("drug_name"));
        drg.setConceptID(rs.getInt("concept_id"));
        drg.setStrength(rs.getString("strength"));
        drg.setOtherStrength(rs.getString("other_strength"));
        drg.setSingleDoseValue(rs.getDouble("single_dose_value"));
        drg.setSingleDoseUnit(rs.getString("single_dose_unit"));
        drg.setFrequency(rs.getString("frequency"));
        drg.setDuration(rs.getInt("duration"));
        drg.setDurationUnit(rs.getString("duration_unit"));
        drg.setQuantityPrescribed(rs.getInt("quantity_prescribed"));
        drg.setQuantityPrescribedUnit(rs.getString("quantity_prescribed_unit"));
        drg.setQuantityDispensed(rs.getInt("quantity_dispensed"));
        drg.setQuantityDispensedUnit(rs.getString("quantity_dispensed_unit"));
        drg.setUuid(rs.getString("uuid"));
        return drg;
    }

    public ArrayList<model.datapump.DrugOrder> getDrugOrderForPatient(int patientID, List<model.datapump.Obs> obsList) {

        model.datapump.DrugOrder patientRegimen = null;
        ArrayList<model.datapump.DrugOrder> patientRegimenList = new ArrayList<model.datapump.DrugOrder>();

        Set<Date> visitDateSet = null;
        Set<Integer> obsGroupIDSet = null;

        visitDateSet = getAllVisitsFromObsList(obsList);

        for (Date ele : visitDateSet) {
            patientRegimen = getRegimenObsParameters(obsList, ele);
            patientRegimenList.add(patientRegimen);
        }

        return patientRegimenList;
    }

    public List<model.datapump.DrugOrder> getDrugOrderForPatient3(int patientID) {
        ArrayList<model.datapump.DrugOrder> drugOrderList = new ArrayList<model.datapump.DrugOrder>();
        String sql_text = "select \n"
                + "patient.patient_id,\n"
                + "pid1.identifier as hosp_id,\n"
                + "pid2.identifier as pepfar_id,\n"
                + "orders.encounter_id,\n"
                + "orders.start_date,\n"
                + "orders.discontinued_date,\n"
                + "orders.concept_id,\n"
                + "drug.`name` as drug_name,\n"
                + "drug_order.dose,\n"
                + "drug_order.quantity,\n"
                + "drug_order.frequency,\n"
                + "drug_order.equivalent_daily_dose,\n"
                + "drug_order.units,\n"
                + "drug_order.drug_inventory_id,\n"
                + "orders.creator,\n"
                + "orders.discontinued_reason,\n"
                + "orders.date_created,\n"
                + "orders.uuid\n"
                + "from orders \n"
                + "inner join patient on(orders.patient_id=patient.patient_id and orders.voided=0 and patient.voided=0)\n"
                + "inner join drug_order on(drug_order.order_id=orders.order_id)\n"
                + "inner join drug on(drug.drug_id=drug_order.drug_inventory_id)\n"
                + "left join patient_identifier pid1 on(pid1.patient_id=orders.patient_id and pid1.identifier_type=6 and pid1.voided=0)\n"
                + "left join patient_identifier pid2 on(pid2.patient_id=orders.patient_id and pid2.identifier_type=3 and pid2.voided=0)\n"
                + "where orders.voided=0 and orders.patient_id=?\n"
                + "\n"
                + "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        model.datapump.DrugOrder order = null;
        try {
            ps = prepareQuery(sql_text);
            ps.setInt(1, patientID);
            rs = ps.executeQuery();
            while(rs.next()){
                order=new model.datapump.DrugOrder();
                order.setPatientID(rs.getInt("patient_id"));
                
            }
        }catch(SQLException ex){
            handleException(ex);
        }finally{
            cleanUp(rs, ps);
        }
        return drugOrderList;
    }

    public ArrayList<model.datapump.DrugOrder> getDrugOrderForPatient2(int patientID) {

        ArrayList<model.datapump.DrugOrder> drugOrderList = new ArrayList<model.datapump.DrugOrder>();
        String sql_text = "select regimen.*,COALESCE(regimen.first_line,regimen.second_line) as regimen,s1.drug_name,s1.duration,s1.duration_unit from regimen \n"
                + "inner join (select drug.patient_id, drug.dispensed_date,group_concat(drug.drug_name) as drug_name,drug.duration,drug.duration_unit \n"
                + "from drug GROUP BY drug.patient_id,drug.dispensed_date) s1 on(s1.patient_id=regimen.patient_id and s1.dispensed_date=regimen.visit_date) where regimen.patient_id=?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        model.datapump.PatientRegimen order = null;
        try {
            ps = prepareQuery(sql_text);
            ps.setInt(1, patientID);
            rs = ps.executeQuery();
            int duration = 0;
            String unit = "";
            Date stopDate = null;
            while (rs.next()) {
                order = new model.datapump.PatientRegimen();
                order.setPatientID(rs.getInt("patient_id"));
                order.setPepfarID(rs.getString("pepfar_id"));
                order.setHospID(rs.getString("hosp_id"));
                order.setStartDate(rs.getDate("visit_date"));
                duration = rs.getInt("duration");
                unit = rs.getString("duration_unit");
                stopDate = calculateStopDate(order.getStartDate(), duration, unit);
                order.setStopDate(stopDate);
                order.setDrugName(rs.getString("drug_name"));
                order.setRegimenName(rs.getString("regimen"));
                order.setCode(String.valueOf(rs.getInt("regimen_code")));
                order.setRegimenLine(rs.getString("regimen_line"));
                order.setEnteredBy(rs.getString("entered_by"));
                order.setDateEntered(rs.getDate("date_entered"));
                order.setCreator(rs.getInt("creator_id"));
                drugOrderList.add(order);
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }
        return drugOrderList;
    }

    public ArrayList<model.datapump.Obs> getObsForPatientForVisit(int patientID, Date endDate) {

        // ArrayList<model.datapump.DrugOrder> drugOrderList = new ArrayList<model.datapump.DrugOrder>();
        /*String sql_text = "select regimen.*,COALESCE(regimen.first_line,regimen.second_line) as regimen,s1.drug_name,s1.duration,s1.duration_unit from regimen \n"
                + "inner join (select drug.patient_id, drug.dispensed_date,group_concat(drug.drug_name) as drug_name,drug.duration,drug.duration_unit \n"
                + "from drug GROUP BY drug.patient_id,drug.dispensed_date) s1 on(s1.patient_id=regimen.patient_id and s1.dispensed_date=regimen.visit_date) where regimen.patient_id=?";*/
        String sql_text = "select \n"
                + "    `obs`.`obs_id`,\n"
                + "    `obs`.`person_id`,\n"
                + "    `obs`.`obs_datetime`,\n"
                + "    `obs`.`concept_id`,\n"
                + "    `obs`.`value_numeric`,\n"
                + "     obs.value_coded,\n"
                + "     obs.value_text,\n"
                + "     obs.value_datetime,\n"
                + "     cast(coalesce(obs.value_coded,`obs`.`value_numeric`,`obs`.`value_datetime`,`obs`.`value_text`) as char charset utf8) AS `variable_value`,\n"
                + "     `obs`.`date_created`,\n"
                + "      encounter.date_changed,\n"
                + "      encounter.date_voided,\n"
                + "      encounter.changed_by,\n"
                + "      obs.voided,\n"
                + "      encounter.voided_by,\n"
                + "     `obs`.`creator` AS `creator`,\n"
                + "     `obs`.`encounter_id` AS `encounter_id`,\n"
                + "     `obs`.`obs_group_id` AS `obs_group_id`,\n"
                + "     `obs`.`uuid`,\n"
                + "     `encounter`.`form_id` AS `form_id`,\n"
                + "     `encounter_provider`.`provider_id` AS `provider_id`,\n"
                + "     `obs`.`location_id` AS `location_id` \n"
                + "     from `obs` \n"
                + "	inner join `patient` on(`patient`.`patient_id` = `obs`.`person_id`)\n"
                + "     inner join `encounter` on(`encounter`.`encounter_id` = `obs`.`encounter_id`)\n"
                + "     inner join `encounter_provider` on(`encounter_provider`.`encounter_id` = `encounter`.`encounter_id`)\n"
                + "     where encounter.form_id in(14,22,27,14,20,1,23,21) and encounter.patient_id=? and encounter.voided=0 and encounter.encounter_datetime=? order by encounter.patient_id";

        PreparedStatement ps = null;
        ResultSet rs = null;
        model.datapump.PatientRegimen order = null;
        ArrayList<model.datapump.Obs> obsList = new ArrayList<model.datapump.Obs>();
        model.datapump.Obs obs = null;
        try {
            ps = prepareQuery(sql_text);
            ps.setInt(1, patientID);
            ps.setDate(2, convertToSQLDate(endDate));
            rs = ps.executeQuery();
            //int duration = 0;
            //String unit = "";
            //Date stopDate = null;
            while (rs.next()) {
                obs = constructObs2(rs);
                obsList.add(obs);
                /*order = new model.datapump.PatientRegimen();
                order.setPatientID(rs.getInt("patient_id"));
                order.setPepfarID(rs.getString("pepfar_id"));
                order.setHospID(rs.getString("hosp_id"));
                order.setStartDate(rs.getDate("visit_date"));
                duration = rs.getInt("duration");
                unit = rs.getString("duration_unit");
                stopDate = calculateStopDate(order.getStartDate(), duration, unit);
                order.setStopDate(stopDate);
                order.setDrugName(rs.getString("drug_name"));
                order.setRegimenName(rs.getString("regimen"));
                order.setCode(String.valueOf(rs.getInt("regimen_code")));
                order.setRegimenLine(rs.getString("regimen_line"));
                order.setEnteredBy(rs.getString("entered_by"));
                order.setDateEntered(rs.getDate("date_entered"));
                order.setCreator(rs.getInt("creator_id"));
                drugOrderList.add(order);*/
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }
        return obsList;
    }

    public ArrayList<model.datapump.Obs> getObsFromPharmacyForPatientForVisit(int patientID, Date visitDate) {

        // ArrayList<model.datapump.DrugOrder> drugOrderList = new ArrayList<model.datapump.DrugOrder>();
        /*String sql_text = "select regimen.*,COALESCE(regimen.first_line,regimen.second_line) as regimen,s1.drug_name,s1.duration,s1.duration_unit from regimen \n"
                + "inner join (select drug.patient_id, drug.dispensed_date,group_concat(drug.drug_name) as drug_name,drug.duration,drug.duration_unit \n"
                + "from drug GROUP BY drug.patient_id,drug.dispensed_date) s1 on(s1.patient_id=regimen.patient_id and s1.dispensed_date=regimen.visit_date) where regimen.patient_id=?";*/
        String sql_text = "select \n"
                + "    `obs`.`obs_id`,\n"
                + "    `obs`.`person_id`,\n"
                + "    `obs`.`obs_datetime`,\n"
                + "    `obs`.`concept_id`,\n"
                + "    `obs`.`value_numeric`,\n"
                + "     obs.value_coded,\n"
                + "     obs.value_text,\n"
                + "     obs.value_datetime,\n"
                + "     obs.value_boolean,\n"
                + "     cast(coalesce(obs.value_coded,`obs`.`value_numeric`,`obs`.`value_datetime`,`obs`.`value_text`) as char charset utf8) AS `variable_value`,\n"
                + "     `obs`.`date_created`,\n"
                + "      encounter.date_changed,\n"
                + "      encounter.date_voided,\n"
                + "      encounter.changed_by,\n"
                + "      obs.voided,\n"
                + "      encounter.voided_by,\n"
                + "     `obs`.`creator` AS `creator`,\n"
                + "     `obs`.`encounter_id` AS `encounter_id`,\n"
                + "     `obs`.`obs_group_id` AS `obs_group_id`,\n"
                + "     `obs`.`uuid`,\n"
                + "     `encounter`.`form_id` AS `form_id`,\n"
                + "     `encounter`.`provider_id` AS `provider_id`,\n"
                + "     `obs`.`location_id` AS `location_id` \n"
                + "     from `obs` \n"
                + "	inner join `patient` on(`patient`.`patient_id` = `obs`.`person_id`)\n"
                + "     inner join `encounter` on(`encounter`.`encounter_id` = `obs`.`encounter_id`)\n"
                + "     where encounter.form_id in(46,53,86) and encounter.patient_id=? and encounter.voided=0 and encounter.encounter_datetime=? order by encounter.patient_id";

        PreparedStatement ps = null;
        ResultSet rs = null;
        model.datapump.PatientRegimen order = null;
        ArrayList<model.datapump.Obs> obsList = new ArrayList<model.datapump.Obs>();
        model.datapump.Obs obs = null;
        try {
            ps = prepareQuery(sql_text);
            ps.setInt(1, patientID);
            ps.setDate(2, convertToSQLDate(visitDate));
            rs = ps.executeQuery();
            //int duration = 0;
            //String unit = "";
            //Date stopDate = null;
            while (rs.next()) {
                obs = constructObs2(rs);
                obsList.add(obs);
                /*order = new model.datapump.PatientRegimen();
                order.setPatientID(rs.getInt("patient_id"));
                order.setPepfarID(rs.getString("pepfar_id"));
                order.setHospID(rs.getString("hosp_id"));
                order.setStartDate(rs.getDate("visit_date"));
                duration = rs.getInt("duration");
                unit = rs.getString("duration_unit");
                stopDate = calculateStopDate(order.getStartDate(), duration, unit);
                order.setStopDate(stopDate);
                order.setDrugName(rs.getString("drug_name"));
                order.setRegimenName(rs.getString("regimen"));
                order.setCode(String.valueOf(rs.getInt("regimen_code")));
                order.setRegimenLine(rs.getString("regimen_line"));
                order.setEnteredBy(rs.getString("entered_by"));
                order.setDateEntered(rs.getDate("date_entered"));
                order.setCreator(rs.getInt("creator_id"));
                drugOrderList.add(order);*/
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }
        return obsList;
    }

    public ArrayList<model.datapump.Obs> getObsFromPharmacyForPatient(int patientID) {

        // ArrayList<model.datapump.DrugOrder> drugOrderList = new ArrayList<model.datapump.DrugOrder>();
        /*String sql_text = "select regimen.*,COALESCE(regimen.first_line,regimen.second_line) as regimen,s1.drug_name,s1.duration,s1.duration_unit from regimen \n"
                + "inner join (select drug.patient_id, drug.dispensed_date,group_concat(drug.drug_name) as drug_name,drug.duration,drug.duration_unit \n"
                + "from drug GROUP BY drug.patient_id,drug.dispensed_date) s1 on(s1.patient_id=regimen.patient_id and s1.dispensed_date=regimen.visit_date) where regimen.patient_id=?";*/
        String sql_text = "select \n"
                + "    `obs`.`obs_id`,\n"
                + "    `obs`.`person_id`,\n"
                + "    `obs`.`obs_datetime`,\n"
                + "    `obs`.`concept_id`,\n"
                + "    `obs`.`value_numeric`,\n"
                + "     obs.value_coded,\n"
                + "     obs.value_text,\n"
                + "     obs.value_datetime,\n"
                + "     cast(coalesce(obs.value_coded,`obs`.`value_numeric`,`obs`.`value_datetime`,`obs`.`value_text`) as char charset utf8) AS `variable_value`,\n"
                + "     `obs`.`date_created`,\n"
                + "      encounter.date_changed,\n"
                + "      encounter.date_voided,\n"
                + "      encounter.changed_by,\n"
                + "      obs.voided,\n"
                + "      encounter.voided_by,\n"
                + "     `obs`.`creator` AS `creator`,\n"
                + "     `obs`.`encounter_id` AS `encounter_id`,\n"
                + "     `obs`.`obs_group_id` AS `obs_group_id`,\n"
                + "     `obs`.`uuid`,\n"
                + "     `encounter`.`form_id` AS `form_id`,\n"
                + "     `encounter_provider`.`provider_id` AS `provider_id`,\n"
                + "     `obs`.`location_id` AS `location_id` \n"
                + "     from `obs` \n"
                + "	inner join `patient` on(`patient`.`patient_id` = `obs`.`person_id`)\n"
                + "     inner join `encounter` on(`encounter`.`encounter_id` = `obs`.`encounter_id`)\n"
                + "     inner join `encounter_provider` on(`encounter_provider`.encounter_id=encounter.encounter_id and encounter.voided=0)\n"
                + "     where encounter.form_id in(27,14) and encounter.patient_id=? and obs.voided=0 order by encounter.patient_id,encounter.encounter_datetime";

        PreparedStatement ps = null;
        ResultSet rs = null;
        model.datapump.PatientRegimen order = null;
        ArrayList<model.datapump.Obs> obsList = new ArrayList<model.datapump.Obs>();
        model.datapump.Obs obs = null;
        try {
            ps = prepareQuery(sql_text);
            ps.setInt(1, patientID);
            //ps.setInt(2, patientID);
            rs = ps.executeQuery();
            //int duration = 0;
            //String unit = "";
            //Date stopDate = null;
            while (rs.next()) {
                obs = constructObs2(rs);

                obsList.add(obs);
                //System.out.println("obs group id "+obs.getObsGroupID());
                /*order = new model.datapump.PatientRegimen();
                order.setPatientID(rs.getInt("patient_id"));
                order.setPepfarID(rs.getString("pepfar_id"));
                order.setHospID(rs.getString("hosp_id"));
                order.setStartDate(rs.getDate("visit_date"));
                duration = rs.getInt("duration");
                unit = rs.getString("duration_unit");
                stopDate = calculateStopDate(order.getStartDate(), duration, unit);
                order.setStopDate(stopDate);
                order.setDrugName(rs.getString("drug_name"));
                order.setRegimenName(rs.getString("regimen"));
                order.setCode(String.valueOf(rs.getInt("regimen_code")));
                order.setRegimenLine(rs.getString("regimen_line"));
                order.setEnteredBy(rs.getString("entered_by"));
                order.setDateEntered(rs.getDate("date_entered"));
                order.setCreator(rs.getInt("creator_id"));
                drugOrderList.add(order);*/
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }
        return obsList;
    }

    public Set<Integer> getAllFormsFromObsList(ArrayList<model.datapump.Obs> obsList) {
        Set<Integer> formIDSet = new HashSet<Integer>();
        int formID = 0;
        for (model.datapump.Obs ele : obsList) {
            formID = ele.getFormID();
            formIDSet.add(formID);
        }
        return formIDSet;
    }

    public Set<Date> getAllVisitsFromObsList(List<model.datapump.Obs> obsList) {
        Set<Date> visitDateSet = new HashSet<Date>();
        Date visitDate = null;
        for (model.datapump.Obs ele : obsList) {
            visitDate = ele.getVisitDate();
            visitDateSet.add(visitDate);
        }
        return visitDateSet;
    }

    public Set<Integer> getAllObsGroupIDsFromList(ArrayList<model.datapump.Obs> obsList, int formID) {
        Set<Integer> obsGroupIDSet = new HashSet<Integer>();
        int obsGroupID = 0;
        for (model.datapump.Obs ele : obsList) {
            if (ele.getFormID() == formID) {
                if (ele.getObsGroupID() != 0) {
                    obsGroupIDSet.add(ele.getObsGroupID());
                }
            }
        }
        return obsGroupIDSet;
    }

    public Set<Integer> getAllObsGroupIDsFromList(ArrayList<model.datapump.Obs> obsList, Date visitDate) {
        Set<Integer> obsGroupIDSet = new HashSet<Integer>();
        int obsGroupID = 0;
        for (model.datapump.Obs ele : obsList) {
            if (ele.getVisitDate().equals(visitDate)) {
                if (ele.getObsGroupID() != 0) {
                    obsGroupIDSet.add(ele.getObsGroupID());
                }
            }
        }
        return obsGroupIDSet;
    }

    public model.datapump.Obs getConceptForForm(int conceptID, int formID, List<model.datapump.Obs> obsList) {
        model.datapump.Obs obs = null;
        for (model.datapump.Obs ele : obsList) {
            if (ele.getConceptID() == conceptID && ele.getFormID() == formID) {
                obs = ele;
            }
        }
        return obs;
    }

    public model.datapump.Obs getConceptForForm(int conceptID, int formID, List<model.datapump.Obs> obsList, Date visitDate) {
        model.datapump.Obs obs = null;
        for (model.datapump.Obs ele : obsList) {
            if (ele.getConceptID() == conceptID && DateUtils.isSameDay(ele.getVisitDate(), visitDate) && ele.getFormID() == formID) {
                obs = ele;
            }
        }
        return obs;
    }

    public model.datapump.Obs getConceptForFormInGroup(int conceptID, int formID, List<model.datapump.Obs> obsList, Date visitDate, int obsID) {
        model.datapump.Obs obs = null;
        //DateTime t1,t2;
        //t1=new DateTime(visitDate);
        for (model.datapump.Obs ele : obsList) {
            //t2=new DateTime(ele.getVisitDate());
            //if (ele.getConceptID() == conceptID && DateUtils.isSameDay(visitDate, ele.getVisitDate()) && ele.getObsGroupID() == obsID) {
            if (ele.getConceptID() == conceptID && ele.getObsGroupID() == obsID) {
                obs = ele;
                //System.out.println("Test Concept ID "+conceptID+" obs group id of ele: "+ele.getObsGroupID()+ " Concept ID: "+ele.getConceptID());
            }
        }
        return obs;
    }

    public int getObsIDOfConceptInList(int conceptID, int formID, List<model.datapump.Obs> obsList, Date visitDate) {
        Obs obs = getConceptForForm(conceptID, formID, obsList);
        //Obs obs=getConceptForForm(conceptID, formID, obsList, visitDate);
        int obsGroupID = 0;
        if (obs != null) {
            obsGroupID = obs.getObsGroupID();
        }
        return obsGroupID;

    }

    public model.datapump.Obs getConceptForVisit(int conceptID, Date visitDate, ArrayList<model.datapump.Obs> obsList) {
        model.datapump.Obs obs = null;
        for (model.datapump.Obs ele : obsList) {
            if (ele.getConceptID() == conceptID && ele.getVisitDate().equals(visitDate)) {
                obs = ele;

            }
        }
        return obs;
    }

    public model.datapump.Obs getConceptForVisit(int conceptID, ArrayList<model.datapump.Obs> obsList) {
        model.datapump.Obs obs = null;
        for (model.datapump.Obs ele : obsList) {
            if (ele.getConceptID() == conceptID) {
                obs = ele;
            }
        }
        return obs;
    }

    public model.datapump.PatientRegimen extractPatientRegimen(ArrayList<Obs> obsList, int pid) {
        int duration = 0;
        int duration_unit = 0;
        int valueCoded = 0;
        String durationUnitStr = "";
        String regimenName = "";
        String regimenLine = "";
        Date stopDate = null;
        Date visitDate = null;
        model.datapump.PatientRegimen order = new model.datapump.PatientRegimen();
        Obs obsPin = null;
        order.setPatientID(pid);
        order.setPepfarID(pepfarDictionary.get(pid));
        order.setHospID(hospIDDictionary.get(pid));
        // order.setStartDate(visitDate);
        //getting regimen line
        obsPin = getConceptForVisit(7778111, obsList);
        if (obsPin != null) {
            regimenLine = obsPin.getVariableValue();
            order.setRegimenLine(regimenLine);
            order.setEnteredBy(obsPin.getEnteredBy());
            order.setDateEntered(obsPin.getDateEntered());
            order.setCreator(obsPin.getCreator());
            valueCoded = obsPin.getValueCoded();
            order.setStartDate(obsPin.getVisitDate());
            if (valueCoded == 7778108) {
                //getting the first line regimen name
                obsPin = getConceptForVisit(7778108, obsList);
                if (obsPin != null) {
                    regimenName = obsPin.getVariableValue();
                    order.setRegimenName(regimenName);
                    //order.setEnteredBy(obs);
                }
            } else if (valueCoded == 7778109) {
                //getting the second line regimen name
                obsPin = getConceptForVisit(7778109, obsList);
                if (obsPin != null) {
                    regimenName = obsPin.getVariableValue();
                    order.setRegimenName(regimenName);
                }
            }
            visitDate = order.getStartDate();
        } else {
            obsPin = getConceptForVisit(7778531, obsList);
            if (obsPin != null) {
                regimenLine = obsPin.getVariableValue();
                order.setRegimenLine(regimenLine);
                order.setEnteredBy(obsPin.getEnteredBy());
                order.setDateEntered(obsPin.getDateEntered());
                order.setCreator(obsPin.getCreator());
                valueCoded = obsPin.getValueCoded();
                order.setStartDate(obsPin.getVisitDate());
                if (valueCoded == 7778108) {
                    //getting the first line regimen name
                    obsPin = getConceptForVisit(7778108, obsList);
                    if (obsPin != null) {
                        regimenName = obsPin.getVariableValue();
                        order.setRegimenName(regimenName);
                        //order.setEnteredBy(obs);
                    }
                } else if (valueCoded == 7778109) {
                    //getting the second line regimen name
                    obsPin = getConceptForVisit(7778109, obsList);
                    if (obsPin != null) {
                        regimenName = obsPin.getVariableValue();
                        order.setRegimenName(regimenName);
                    }
                }
                visitDate = order.getStartDate();
            }
        }

        duration = estimateDurationWeeks(visitDate, obsList);
        durationUnitStr = "WEEK(S)";
        /* // getting the duration
        obsPin = getConceptForVisit(7778370, visitDate, obsList);
        if (obsPin != null) {
            duration = (int) obsPin.getValueNumeric();
            order.setDuration(duration);
        } else {
            duration = 2;
            order.setDuration(duration);
        }
        // getting duration unit concept
        obsPin = getConceptForVisit(7778371, visitDate, obsList);
        if (obsPin != null) {
            durationUnitStr = obsPin.getVariableValue();
            order.setDurationUnit(durationUnitStr);
        } else {
            durationUnitStr = "MONTH(S)";
            order.setDurationUnit(durationUnitStr);
        }*/

        //duration = rs.getInt("duration");
        // unit = rs.getString("duration_unit");
        stopDate = calculateStopDate(order.getStartDate(), duration, durationUnitStr);
        order.setStopDate(stopDate);

        //order.setDrugName(rs.getString("drug_name"));
        //order.setRegimenName(rs.getString("regimen"));
        order.setCode(String.valueOf(getCode(regimenName)));
        //order.setCode(String.valueOf(rs.getInt("regimen_code")));
        //order.setRegimenLine(rs.getString("regimen_line"));

        //order.setEnteredBy(rs.getString("entered_by"));
        //order.setDateEntered(rs.getDate("date_entered"));
        //order.setCreator(rs.getInt("creator_id"));
        return order;
    }

    public model.datapump.PatientRegimen extractPatientRegimen(ArrayList<Obs> obsList, int pid, Date visitDate) {
        int duration = 0;
        int duration_unit = 0;
        int valueCoded = 0;
        String durationUnitStr = "";
        String regimenName = "";
        String regimenLine = "";
        Date stopDate = null;
        model.datapump.PatientRegimen order = new model.datapump.PatientRegimen();
        Obs obsPin = null;
        order.setPatientID(pid);
        order.setPepfarID(pepfarDictionary.get(pid));
        order.setHospID(hospIDDictionary.get(pid));
        order.setStartDate(visitDate);
        //getting regimen line
        obsPin = getConceptForVisit(7778111, visitDate, obsList);
        if (obsPin != null) {
            regimenLine = obsPin.getVariableValue();
            order.setRegimenLine(regimenLine);
            order.setEnteredBy(obsPin.getEnteredBy());
            order.setDateEntered(obsPin.getDateEntered());
            order.setCreator(obsPin.getCreator());
            valueCoded = obsPin.getValueCoded();
            if (valueCoded == 7778108) {
                //getting the first line regimen name
                obsPin = getConceptForVisit(7778108, visitDate, obsList);
                if (obsPin != null) {
                    regimenName = obsPin.getVariableValue();
                    order.setRegimenName(regimenName);
                    order.setCode(String.valueOf(getCode(regimenName)));

                    //order.setEnteredBy(obs);
                }
            } else if (valueCoded == 7778109) {
                //getting the second line regimen name
                obsPin = getConceptForVisit(7778109, visitDate, obsList);
                if (obsPin != null) {
                    regimenName = obsPin.getVariableValue();
                    order.setRegimenName(regimenName);
                    order.setCode(String.valueOf(getCode(regimenName)));
                }
            }
        } else {
            obsPin = getConceptForVisit(7778531, obsList);
            if (obsPin != null) {
                regimenLine = obsPin.getVariableValue();
                order.setRegimenLine(regimenLine);
                order.setEnteredBy(obsPin.getEnteredBy());
                order.setDateEntered(obsPin.getDateEntered());
                order.setCreator(obsPin.getCreator());
                valueCoded = obsPin.getValueCoded();
                order.setStartDate(obsPin.getVisitDate());
                if (valueCoded == 7778108) {
                    //getting the first line regimen name
                    obsPin = getConceptForVisit(7778108, obsList);
                    if (obsPin != null) {
                        regimenName = obsPin.getVariableValue();
                        order.setRegimenName(regimenName);
                        order.setCode(String.valueOf(getCode(regimenName)));
                        //order.setEnteredBy(obs);
                    }
                } else if (valueCoded == 7778109) {
                    //getting the second line regimen name
                    obsPin = getConceptForVisit(7778109, obsList);
                    if (obsPin != null) {
                        regimenName = obsPin.getVariableValue();
                        order.setRegimenName(regimenName);
                        order.setCode(String.valueOf(getCode(regimenName)));
                    }
                }
                visitDate = order.getStartDate();
            }
        }
        duration = estimateDurationWeeks(visitDate, obsList);
        durationUnitStr = "WEEK(S)";
        /* // getting the duration
        obsPin = getConceptForVisit(7778370, visitDate, obsList);
        if (obsPin != null) {
            duration = (int) obsPin.getValueNumeric();
            order.setDuration(duration);
        } else {
            duration = 2;
            order.setDuration(duration);
        }
        // getting duration unit concept
        obsPin = getConceptForVisit(7778371, visitDate, obsList);
        if (obsPin != null) {
            durationUnitStr = obsPin.getVariableValue();
            order.setDurationUnit(durationUnitStr);
        } else {
            durationUnitStr = "MONTH(S)";
            order.setDurationUnit(durationUnitStr);
        }*/

        //duration = rs.getInt("duration");
        // unit = rs.getString("duration_unit");
        stopDate = calculateStopDate(order.getStartDate(), duration, durationUnitStr);
        order.setStopDate(stopDate);

        //order.setDrugName(rs.getString("drug_name"));
        //order.setRegimenName(rs.getString("regimen"));
        //order.setCode(String.valueOf(rs.getInt("regimen_code")));
        //order.setRegimenLine(rs.getString("regimen_line"));
        //order.setEnteredBy(rs.getString("entered_by"));
        //order.setDateEntered(rs.getDate("date_entered"));
        //order.setCreator(rs.getInt("creator_id"));
        return order;
    }

    public int estimateDurationWeeks(Date visitDate, ArrayList<Obs> obsList) {
        int duration = 0;
        Obs obsPin = null;
        double durationNumber = 0.0;
        int durationUnit = 0;
        int nextAppointmentDuration = 0;
        Date nextAppointmentDate = null;

        //Check if duration number and duration unit exists
        obsPin = getConceptForVisit(7778370, visitDate, obsList);
        if (obsPin != null) {
            durationNumber = obsPin.getValueNumeric();
        }
        obsPin = getConceptForVisit(7778371, visitDate, obsList);
        if (obsPin != null) {
            durationUnit = obsPin.getValueCoded();
        }
        obsPin = getConceptForVisit(7777821, visitDate, obsList);
        if (obsPin != null) {
            nextAppointmentDuration = obsPin.getValueCoded();
        }
        obsPin = getConceptForVisit(7777822, visitDate, obsList);
        if (obsPin != null) {
            nextAppointmentDate = obsPin.getVisitDate();
        }
        if (nextAppointmentDuration != 0) {
            switch (nextAppointmentDuration) {
                case 1570:
                    duration = 1;
                    break;
                case 1571:
                    duration = 2;
                    break;
                case 1628:
                    duration = 4;
                    break;
                case 1574:
                    duration = 8;
                    break;
                case 1575:
                    duration = 3 * 4;
                    break;
                case 1576:
                    duration = 6 * 4;
                    break;
                case 7778597:
                    duration = 12 * 4;
                    break;
                default:
                    break;

            }
        } else if (nextAppointmentDate != null) {
            DateTime aptDate = new DateTime(nextAppointmentDate);
            DateTime visitDateTime = new DateTime(visitDate);
            Weeks wks = Weeks.weeksBetween(visitDateTime, aptDate);
            duration = wks.getWeeks();
        } else if (durationUnit != 0 && durationNumber != 0.0) {
            duration = convertToWeeks(durationUnit, durationNumber);
            if (duration > 24) {
                duration = 12;
            }
        } else {
            duration = 3 * 4;
        }

        return duration;
    }

    public int convertToWeeks(int durationUnit, double durationValue) {
        int durationWeeks = 0;
        switch (durationUnit) {
            case 523:
                durationWeeks = (int) (durationValue / 4);
                break;
            case 524:
                durationWeeks = (int) durationValue * 4;
                break;
            case 520:
                durationWeeks = (int) durationValue * 1;
                break;
            default:
                break;
        }
        return durationWeeks;
    }

    public model.datapump.Obs getConceptForForm(int conceptID, int obsGroupID, int formID, ArrayList<model.datapump.Obs> obsList) {
        model.datapump.Obs obs = null;
        for (model.datapump.Obs ele : obsList) {
            if (ele.getConceptID() == conceptID && ele.getObsGroupID() == obsGroupID) {
                obs = ele;
            }
        }
        return obs;
    }

    public model.datapump.Obs getConceptForForm(int conceptID, int obsGroupID, int formID, ArrayList<model.datapump.Obs> obsList, Date visitDate) {
        model.datapump.Obs obs = null;
        for (model.datapump.Obs ele : obsList) {
            if (ele.getConceptID() == conceptID && ele.getFormID() == formID && ele.getObsGroupID() == obsGroupID && ele.getVisitDate().equals(visitDate)) {
                obs = ele;
            }
        }
        return obs;
    }

    public model.datapump.DrugOrder getRegimenObsParameters(int obsGroupID, ArrayList<model.datapump.Obs> obsList) {
        model.datapump.PatientRegimen order = new model.datapump.PatientRegimen();
        int patientID = 0;
        String pepfarID = "";
        String hospID = "";
        Date startDate = null;
        int duration = 0;
        String durationUnit = "";
        Date stopDate = null;
        String drugName = "";
        String regimenName = "";
        String strength = "";
        String otherStrength = "";
        String dose = "";
        String frequency = "";
        int quantity = 0;
        String regimenCode = "";
        String regimenLine = "";
        String enteredBy = "";
        Date dateEntered = null;
        int creatorID = 0;
        int formID = 0;
        int drugConceptID = 0;
        model.datapump.Obs obsPin = null;
        int valueCoded = 0;
        int encounterID = 0;
        int providerID = 0;
        for (model.datapump.Obs ele : obsList) {
            if (ele.getObsGroupID() == obsGroupID) {
                formID = ele.getFormID();
                patientID = ele.getPatientID();
                pepfarID = ele.getPepfarID();
                hospID = ele.getHospID();
                encounterID = ele.getEncounterID();
                providerID = ele.getProviderID();
                startDate = ele.getVisitDate();
                obsPin = getConceptForForm(7778370, obsGroupID, formID, obsList);
                if (obsPin != null) {
                    duration = (int) obsPin.getValueNumeric();
                } else {
                    duration = 2;
                }
                obsPin = getConceptForForm(7778371, obsGroupID, formID, obsList);
                if (obsPin != null) {
                    durationUnit = obsPin.getVariableValue();
                } else {
                    durationUnit = "MONTH(S)";
                }
                stopDate = calculateStopDate(order.getStartDate(), duration, durationUnit);
                obsPin = getConceptForForm(7778364, obsGroupID, formID, obsList);
                if (obsPin != null) {
                    drugName = obsPin.getVariableValue();
                    drugConceptID = obsPin.getConceptID();
                }
                String firstLine = "", secondLine = "";
                obsPin = getConceptForForm(7778111, formID, obsList);
                if (obsPin != null) {
                    regimenLine = obsPin.getVariableValue();
                    int code = obsPin.getValueCoded();
                    if (code == 7778108) {
                        obsPin = getConceptForForm(7778108, formID, obsList);
                        if (obsPin != null) {
                            firstLine = obsPin.getVariableValue();
                            regimenName = firstLine;
                        }
                    } else if (code == 7778109) {
                        obsPin = getConceptForForm(7778109, formID, obsList);
                        if (obsPin != null) {
                            secondLine = obsPin.getVariableValue();
                            regimenName = secondLine;
                        }
                    }
                }
                obsPin = getConceptForForm(7778365, obsGroupID, formID, obsList);
                if (obsPin != null) {
                    strength = obsPin.getVariableValue();
                }
                obsPin = getConceptForForm(7778407, obsGroupID, formID, obsList);
                if (obsPin != null) {
                    frequency = obsPin.getVariableValue();
                }

                enteredBy = ele.getEnteredBy();
                dateEntered = ele.getDateEntered();
                creatorID = ele.getCreator();
                order.setPatientID(patientID);
                order.setEncounterID(encounterID);

                order.setPepfarID(pepfarID);
                order.setHospID(hospID);
                order.setStartDate(startDate);
                order.setStopDate(stopDate);
                order.setDrugName(drugName);
                //order.setDrugName(drugName);
                order.setFrequency(frequency);
                order.setConceptID(drugConceptID);
                order.setRegimenName(regimenName);
                order.setCode(String.valueOf(getCode(regimenName)));
                order.setRegimenLine(regimenLine);
                order.setEnteredBy(enteredBy);
                order.setDateEntered(dateEntered);
                order.setCreator(creatorID);

            }
        }
        return order;
    }

    public model.datapump.DrugOrder getRegimenObsParameters(List<model.datapump.Obs> obsList, Date visitDate) {
        model.datapump.PatientRegimen order = new model.datapump.PatientRegimen();
        int patientID = 0;
        String pepfarID = "";
        String hospID = "";
        Date startDate = null;
        int duration = 0;
        String durationUnit = "";
        Date stopDate = null;
        String drugName = "";
        String regimenName = "";
        String strength = "";
        String otherStrength = "";
        String dose = "";
        String frequency = "";
        int quantity = 0;
        String regimenCode = "";
        String regimenLine = "";
        String enteredBy = "";
        Date dateEntered = null;
        int creatorID = 0;
        int formID = 0;
        int drugConceptID = 0;
        model.datapump.Obs obsPin = null;
        int valueCoded = 0;
        int encounterID = 0;
        int providerID = 0;

        startDate = visitDate;
        String firstLine = "", secondLine = "";

        obsPin = getConceptForForm(165708, formID, obsList, visitDate);
        if (obsPin != null) {
            regimenLine = obsPin.getVariableValue();
            int code = obsPin.getValueCoded();
            pepfarID = obsPin.getPepfarID();
            hospID = obsPin.getHospID();
            if (code == 164506 || code == 164507) {
                obsPin = getConceptForForm(code, formID, obsList, visitDate);
                if (obsPin != null) {
                    firstLine = obsPin.getVariableValue();
                    regimenName = firstLine;
                }
            } else if (code == 164514 || code == 164513) {
                obsPin = getConceptForForm(code, formID, obsList, visitDate);
                if (obsPin != null) {
                    secondLine = obsPin.getVariableValue();
                    regimenName = secondLine;
                }
            }

        }
        /*
                     This block of code extracts duration; durationUnit;
                     from the Obs List.
         */
        int obsGroupID = getObsIDOfConceptInList(165724, 27, obsList, visitDate);
        System.out.println("Obs ID of ARV Medication: " + obsGroupID);
        obsPin = getConceptForFormInGroup(159368, 27, obsList, visitDate, obsGroupID);
        //System.out.println("Prescribed regimen duration: "+obsPin.getValueNumeric());
        if (obsPin != null) {
            duration = (int) obsPin.getValueNumeric();
            durationUnit = "DAY(S)";
            /*switch (valueCoded) {
                case 1570:
                    duration = 1;
                    durationUnit = "WEEK(S)";
                    break;
                case 1571:
                    duration = 2;
                    durationUnit = "WEEK(S)";
                    break;
                case 1628:
                    duration = 4;
                    durationUnit = "WEEK(S)";
                    break;
                case 1574:
                    duration = 2;
                    durationUnit = "MONTH(S)";
                    break;
                case 1575:
                    duration = 3;
                    durationUnit = "MONTH(S)";
                    break;
                case 1576:
                    duration = 6;
                    durationUnit = "MONTH(S)";
                    break;
                default:
                    break;
            }*/
 /*if (StringUtils.isEmpty(pepfarID)) {
                pepfarID = obsPin.getPepfarID();
            }*/
        } else {
            /*obsPin = getConceptForForm(5096, 27, obsList, visitDate);
            Date nextAppointmentDate = null;
            if (obsPin != null) {
                nextAppointmentDate = obsPin.getValueDate();
                DateTime visitDateTime = new DateTime(visitDate);
                DateTime nextAppoinDateTime = new DateTime(nextAppointmentDate);
                Weeks wks = Weeks.weeksBetween(visitDateTime, nextAppoinDateTime);
                duration = wks.getWeeks();
                durationUnit = "WEEK(S)";
            }*/

        }
        /*if (StringUtils.isEmpty(durationUnit) || duration == 0 || calculateDayValue(duration, durationUnit) > 120) {
            obsPin = getConceptForForm(7778371, formID, obsList, visitDate); // Drug Duration unit
            if (obsPin != null) {
                durationUnit = obsPin.getVariableValue();
            }
            obsPin = getConceptForForm(7778370, formID, obsList, visitDate); // Drug Duration number
            if (obsPin != null) {
                duration = (int) obsPin.getValueNumeric();
            }
            
        }*/
 /*
            This calculates the stopDate;
         */
        stopDate = calculateStopDate(visitDate, duration, durationUnit);

        /* obsPin = getConceptForForm(7778364, formID, obsList);// Drug name
        if (obsPin != null) {
            drugName = obsPin.getVariableValue();
            drugConceptID = obsPin.getValueCoded();//ConceptID();
        } else {
            obsPin = getConceptForForm(7778203, formID, obsList, visitDate);
            if (obsPin != null) {
                valueCoded = obsPin.getValueCoded();
                switch (valueCoded) {
                    case 1547:
                        drugName = "CTX";
                        strength = "480mg";
                        break;
                    case 1552:
                        drugName = "CTX";
                        strength = "960mg";
                        break;
                    case 1592:
                        drugName = "CTX";
                        strength = "8mg";
                        break;
                    case 7778548:
                        drugName = "CTX";
                        strength = "120mg";
                        break;
                    default:
                        break;
                }
            }
        }

        obsPin = getConceptForForm(7778365, obsGroupID, formID, obsList);
        if (obsPin != null) {
            strength = obsPin.getVariableValue();
        }
        obsPin = getConceptForForm(7778407, obsGroupID, formID, obsList);
        if (obsPin != null) {
            frequency = obsPin.getVariableValue();
        }*/

 /*enteredBy = ele.getEnteredBy();
        dateEntered = ele.getDateEntered();
        creatorID = ele.getCreator();*/
        order.setPatientID(patientID);
        order.setEncounterID(encounterID);

        order.setPepfarID(pepfarID);
        order.setHospID(hospID);
        order.setStartDate(startDate);
        order.setStopDate(stopDate);
        //order.setDrugName(drugName);
        //order.setDrugName(drugName);
        //order.setFrequency(frequency);
        //order.setConceptID(drugConceptID);
        order.setRegimenName(regimenName);
        order.setCode(String.valueOf(getCode(regimenName)));
        //order.setCode();
        order.setRegimenLine(regimenLine);
        //order.setEnteredBy(enteredBy);
        //order.setDateEntered(dateEntered);
        //order.setCreator(creatorID);

        // }
        //}
        return order;
    }

    public model.datapump.Drugs getDrugObsParameters(int obsGroupID, ArrayList<model.datapump.Obs> obsList, Date visitDate) {
        model.datapump.Drugs drg = new model.datapump.Drugs();
        int patientID = 0;
        String pepfarID = "";
        String hospID = "";
        Date startDate = null;
        int duration = 0;
        String durationUnit = "";
        Date stopDate = null;
        String drugName = "";
        String regimenName = "";
        String strength = "";
        String otherStrength = "";
        String dose = "";
        String frequency = "";
        int quantity = 0;
        String regimenCode = "";
        String regimenLine = "";
        String enteredBy = "";
        Date dateEntered = null;
        int creatorID = 0;
        int formID = 0;
        int drugConceptID = 0;
        model.datapump.Obs obsPin = null;
        int valueCoded = 0;
        int encounterID = 0;
        int providerID = 0;

        /*
            Extract drugName
         */
        obsPin = getConceptForForm(165724, obsGroupID, formID, obsList);// Drug name
        if (obsPin != null) {
            drugName = obsPin.getVariableValue();
            drugConceptID = obsPin.getValueCoded();//ConceptID();
            patientID = obsPin.getPatientID();
            pepfarID = obsPin.getPepfarID();
            hospID = obsPin.getHospID();
            formID = obsPin.getFormID();
        } else {
            obsPin = getConceptForForm(165727, formID, obsList, visitDate);
            if (obsPin != null) {
                drugName = "CTX";
            }
        }

        /*
            Extract the strength; frequency
         */
        obsPin = getConceptForForm(165725, obsGroupID, formID, obsList);
        if (obsPin != null) {
            strength = obsPin.getVariableValue();
        } else if (StringUtils.equalsIgnoreCase(drugName, "CTX")) {
            obsPin = getConceptForForm(165727, formID, obsList, visitDate);
            if (obsPin != null) {
                valueCoded = obsPin.getValueCoded();
                switch (valueCoded) {
                    case 165060:
                        //drugName = "CTX";
                        strength = "480mg";
                        break;
                    case 165062:
                        //drugName = "CTX";
                        strength = "960mg";
                        break;
                    case 166095:
                        //drugName = "CTX";
                        strength = "240mg";
                        break;
                    case 165068:
                        //drugName = "CTX";
                        strength = "120mg";
                        break;
                    default:
                        break;
                }
            }
        }
        /*
           Extract otherStrength
         */
 /*obsPin = getConceptForForm(7778390, obsGroupID, formID, obsList);
        if (obsPin != null) {
            otherStrength = obsPin.getValueText();
        }*/
 /*
            Extracts the frequency
         */
        obsPin = getConceptForForm(165723, obsGroupID, formID, obsList);
        if (obsPin != null) {
            frequency = obsPin.getVariableValue();
        }

        /*
           Extract duration and durationUnit
         */
        obsPin = getConceptForForm(159368, 27, obsList, visitDate);
        if (obsPin != null) {
            duration = (int) obsPin.getValueNumeric();
            durationUnit = "DAY(S)";
            /*valueCoded = obsPin.getValueCoded();
            switch (valueCoded) {
                case 1570:
                    duration = 1;
                    durationUnit = "WEEK(S)";
                    break;
                case 1571:
                    duration = 2;
                    durationUnit = "WEEK(S)";
                    break;
                case 1628:
                    duration = 4;
                    durationUnit = "WEEK(S)";
                    break;
                case 1574:
                    duration = 2;
                    durationUnit = "MONTH(S)";
                    break;
                case 1575:
                    duration = 3;
                    durationUnit = "MONTH(S)";
                    break;
                case 1576:
                    duration = 6;
                    durationUnit = "MONTH(S)";
                    break;
                default:
                    break;
            }*/
        } else {
            obsPin = getConceptForForm(5096, 27, obsList, visitDate);
            Date nextAppointmentDate = null;
            if (obsPin != null) {
                nextAppointmentDate = obsPin.getValueDate();
                DateTime visitDateTime = new DateTime(visitDate);
                DateTime nextAppoinDateTime = new DateTime(nextAppointmentDate);
                Weeks wks = Weeks.weeksBetween(visitDateTime, nextAppoinDateTime);
                duration = wks.getWeeks();
                durationUnit = "WEEK(S)";
            }

        }
        /*if (StringUtils.isEmpty(durationUnit) || duration == 0 || calculateDayValue(duration, durationUnit) > 120) {
            obsPin = getConceptForForm(7778371, obsGroupID, formID, obsList); // Drug Duration unit
            if (obsPin != null) {
                durationUnit = obsPin.getVariableValue();
            }
            obsPin = getConceptForForm(7778370, obsGroupID, formID, obsList);// Drug Duration number
            if (obsPin != null) {
                duration = (int) obsPin.getValueNumeric();
            }
            
        }*/
 /* if (StringUtils.isEmpty(durationUnit) || duration == 0 || calculateDayValue(duration, durationUnit) > 120) {
            duration = 2;
            durationUnit = "MONTH(S)";
        }*/
 /*
            Extract startDate and stopDate
         */
        startDate = visitDate;
        stopDate = calculateStopDate(visitDate, duration, durationUnit);

        //for (model.datapump.Obs ele : obsList) {
        //if (ele.getObsGroupID() == obsGroupID) {
        //formID = ele.getFormID();
        //patientID = ele.getPatientID();
        //pepfarID = ele.getPepfarID();
        //hospID = ele.getHospID();
        //encounterID = ele.getEncounterID();
        //providerID = ele.getProviderID();
        obsPin = getConceptForForm(7778364, obsGroupID, formID, obsList);// Drug name
        if (obsPin != null) {
            drugName = obsPin.getVariableValue();
            drugConceptID = obsPin.getValueCoded();//ConceptID();
        } else {
            /*obsPin = getConceptForForm(7778203, formID, obsList, visitDate);
            if (obsPin != null) {
                valueCoded = obsPin.getValueCoded();
                switch (valueCoded) {
                    case 1547:
                        drugName = "CTX";
                        strength = "480mg";
                        break;
                    case 1552:
                        drugName = "CTX";
                        strength = "960mg";
                        break;
                    case 1592:
                        drugName = "CTX";
                        strength = "8mg";
                        break;
                    case 7778548:
                        drugName = "CTX";
                        strength = "120mg";
                        break;
                    default:
                        break;
                }
            }*/
        }
        String firstLine = "", secondLine = "";
        obsPin = getConceptForForm(165708, formID, obsList, visitDate);
        if (obsPin != null) {
            regimenLine = obsPin.getVariableValue();
            int code = obsPin.getValueCoded();
            if (code == 164506 || code == 164507) {
                obsPin = getConceptForForm(code, formID, obsList, visitDate);
                if (obsPin != null) {
                    firstLine = obsPin.getVariableValue();
                    regimenName = firstLine;
                }
            } else if (code == 164513 || code == 164514) {
                obsPin = getConceptForForm(code, formID, obsList, visitDate);
                if (obsPin != null) {
                    secondLine = obsPin.getVariableValue();
                    regimenName = secondLine;
                }
            }
        }

        //enteredBy = ele.getEnteredBy();
        //dateEntered = ele.getDateEntered();
        //creatorID = ele.getCreator();
        drg.setPatientID(patientID);
        drg.setEncounterID(encounterID);
        drg.setDispensedDate(startDate);
        drg.setPepfarID(pepfarID);
        drg.setStrength(strength);
        drg.setOtherStrength(otherStrength);
        drg.setHospID(hospID);
        drg.setDuration(duration);
        drg.setDurationUnit(durationUnit);
        drg.setStopDate(stopDate);
        //drg.setDrugName(drugName);
        //drg.setStartDate(startDate);
        //drg.setStopDate(stopDate);
        drg.setDrugName(drugName);
        //order.setDrugName(drugName);
        drg.setFrequency(frequency);
        drg.setConceptID(drugConceptID);
        //drg.setRegimenName(regimenName);
        //drg.setCode(String.valueOf(getCode(regimenName)));
        //drg.setRegimenLine(regimenLine);
        drg.setEnteredBy(enteredBy);
        drg.setDateEntered(dateEntered);
        //drg.setCreator(creatorID);

        //}
        //}
        return drg;
    }

    public model.datapump.DrugOrder getRegimenObsParameters(int obsGroupID, ArrayList<model.datapump.Obs> obsList, Date visitDate) {
        model.datapump.PatientRegimen order = new model.datapump.PatientRegimen();
        int patientID = 0;
        String pepfarID = "";
        String hospID = "";
        Date startDate = null;
        int duration = 0;
        String durationUnit = "";
        Date stopDate = null;
        String drugName = "";
        String regimenName = "";
        String strength = "";
        String otherStrength = "";
        String dose = "";
        String frequency = "";
        int quantity = 0;
        String regimenCode = "";
        String regimenLine = "";
        String enteredBy = "";
        Date dateEntered = null;
        int creatorID = 0;
        int formID = 0;
        int drugConceptID = 0;
        model.datapump.Obs obsPin = null;
        int valueCoded = 0;
        int encounterID = 0;
        int providerID = 0;
        for (model.datapump.Obs ele : obsList) {
            if (ele.getObsGroupID() == obsGroupID) {
                formID = ele.getFormID();
                patientID = ele.getPatientID();
                pepfarID = ele.getPepfarID();
                hospID = ele.getHospID();
                encounterID = ele.getEncounterID();
                providerID = ele.getProviderID();
                startDate = ele.getVisitDate();
                obsPin = getConceptForForm(7778371, obsGroupID, formID, obsList); // Drug Duration unit
                if (obsPin != null) {
                    durationUnit = obsPin.getVariableValue();
                }
                obsPin = getConceptForForm(7778370, obsGroupID, formID, obsList);// Drug Duration number
                if (obsPin != null) {
                    duration = (int) obsPin.getValueNumeric();
                }
                if (StringUtils.isEmpty(durationUnit) || duration == 0 || calculateDayValue(duration, durationUnit) > 120) {
                    obsPin = getConceptForForm(7777821, 56, obsList, visitDate);
                    if (obsPin != null) {
                        valueCoded = obsPin.getValueCoded();
                        switch (valueCoded) {
                            case 1570:
                                duration = 1;
                                durationUnit = "WEEK(S)";
                                break;
                            case 1571:
                                duration = 2;
                                durationUnit = "WEEK(S)";
                                break;
                            case 1628:
                                duration = 4;
                                durationUnit = "WEEK(S)";
                                break;
                            case 1574:
                                duration = 2;
                                durationUnit = "MONTH(S)";
                                break;
                            case 1575:
                                duration = 3;
                                durationUnit = "MONTH(S)";
                                break;
                            case 1576:
                                duration = 6;
                                durationUnit = "MONTH(S)";
                                break;
                            default:
                                break;
                        }
                    } else {
                        obsPin = getConceptForForm(7777822, 56, obsList, visitDate);
                        Date nextAppointmentDate = null;
                        if (obsPin != null) {
                            nextAppointmentDate = obsPin.getValueDate();
                            DateTime visitDateTime = new DateTime(visitDate);
                            DateTime nextAppoinDateTime = new DateTime(nextAppointmentDate);
                            Weeks wks = Weeks.weeksBetween(visitDateTime, nextAppoinDateTime);
                            duration = wks.getWeeks();
                            durationUnit = "WEEK(S)";
                        } else {
                            duration = 2;
                            durationUnit = "MONTH(S)";
                        }

                    }
                    //duration = 2;
                }

                stopDate = calculateStopDate(order.getStartDate(), duration, durationUnit);
                obsPin = getConceptForForm(7778364, obsGroupID, formID, obsList);// Drug name
                if (obsPin != null) {
                    drugName = obsPin.getVariableValue();
                    drugConceptID = obsPin.getValueCoded();//ConceptID();
                } else {
                    obsPin = getConceptForForm(7778203, formID, obsList, visitDate);
                    if (obsPin != null) {
                        valueCoded = obsPin.getValueCoded();
                        switch (valueCoded) {
                            case 1547:
                                drugName = "CTX";
                                strength = "480mg";
                                break;
                            case 1552:
                                drugName = "CTX";
                                strength = "960mg";
                                break;
                            case 1592:
                                drugName = "CTX";
                                strength = "8mg";
                                break;
                            case 7778548:
                                drugName = "CTX";
                                strength = "120mg";
                                break;
                            default:
                                break;
                        }
                    }
                }
                String firstLine = "", secondLine = "";
                obsPin = getConceptForForm(7778111, formID, obsList, visitDate);
                if (obsPin != null) {
                    regimenLine = obsPin.getVariableValue();
                    int code = obsPin.getValueCoded();
                    if (code == 7778108) {
                        obsPin = getConceptForForm(7778108, formID, obsList, visitDate);
                        if (obsPin != null) {
                            firstLine = obsPin.getVariableValue();
                            regimenName = firstLine;
                        }
                    } else if (code == 7778109) {
                        obsPin = getConceptForForm(7778109, formID, obsList, visitDate);
                        if (obsPin != null) {
                            secondLine = obsPin.getVariableValue();
                            regimenName = secondLine;
                        }
                    }
                }
                obsPin = getConceptForForm(7778365, obsGroupID, formID, obsList);
                if (obsPin != null) {
                    strength = obsPin.getVariableValue();
                }
                obsPin = getConceptForForm(7778407, obsGroupID, formID, obsList);
                if (obsPin != null) {
                    frequency = obsPin.getVariableValue();
                }

                enteredBy = ele.getEnteredBy();
                dateEntered = ele.getDateEntered();
                creatorID = ele.getCreator();
                order.setPatientID(patientID);
                order.setEncounterID(encounterID);

                order.setPepfarID(pepfarID);
                order.setHospID(hospID);
                order.setStartDate(startDate);
                order.setStopDate(stopDate);
                order.setDrugName(drugName);
                //order.setDrugName(drugName);
                order.setFrequency(frequency);
                order.setConceptID(drugConceptID);
                order.setRegimenName(regimenName);
                order.setCode(String.valueOf(getCode(regimenName)));
                order.setRegimenLine(regimenLine);
                order.setEnteredBy(enteredBy);
                order.setDateEntered(dateEntered);
                order.setCreator(creatorID);

            }
        }
        return order;
    }

    public ArrayList<model.datapump.Obs> getPersonalHistoryObs(int patient_id, int location_id) {
        ArrayList<model.datapump.Obs> obsList = new ArrayList<model.datapump.Obs>();
        //String sql_text = "select DISTINCT * from obs where form_id in(18,19,45,65,20,29,71,1) and PATIENT_ID=? and VOIDED=0  AND VISIT_DATE>='2001-01-01' GROUP BY PATIENT_ID, CONCEPT_ID ORDER BY DATE_CREATED DESC ";
        String sql_text = "select DISTINCT \n"
                + "    `obs`.`obs_id`,\n"
                + "    `obs`.`person_id`,\n"
                + "    `obs`.`obs_datetime`,\n"
                + "    `obs`.`concept_id`,\n"
                + "    `obs`.`value_numeric`,\n"
                + "     obs.value_coded,\n"
                + "     obs.value_text,\n"
                + "     obs.value_datetime,\n"
                + "     cast(coalesce(obs.value_coded,`obs`.`value_numeric`,`obs`.`value_datetime`,`obs`.`value_text`) as char charset utf8) AS `variable_value`,\n"
                + "     `obs`.`date_created`,\n"
                + "      encounter.date_changed,\n"
                + "      encounter.date_voided,\n"
                + "      encounter.changed_by,\n"
                + "      obs.voided,\n"
                + "      encounter.voided_by,\n"
                + "     `obs`.`creator` AS `creator`,\n"
                + "     `obs`.`encounter_id` AS `encounter_id`,\n"
                + "     `obs`.`obs_group_id` AS `obs_group_id`,\n"
                + "     `obs`.`uuid`,\n"
                + "     `encounter`.`form_id` AS `form_id`,\n"
                + "     `encounter_provider`.`provider_id` AS `provider_id`,\n"
                + "     `obs`.`location_id` AS `location_id` \n"
                + "     from `obs` \n"
                + "	inner join `patient` on(`patient`.`patient_id` = `obs`.`person_id`)\n"
                + "     inner join `encounter` on(`encounter`.`encounter_id` = `obs`.`encounter_id`)\n"
                + "     inner join `encounter_provider` on(`encounter_provider`.encounter_id=encounter.encounter_id and encounter.voided=0)\n"
                + "	where encounter.form_id in(22,10,13,1,2,46,4) and encounter.voided=0 and encounter.patient_id=? and encounter.encounter_datetime>'2001-01-01'"
                + "     GROUP BY encounter.patient_id,obs.concept_id order by encounter.date_created";

        PreparedStatement ps = null;
        ResultSet rs = null;
        model.datapump.Obs obs = null;
        try {
            ps = prepareQuery(sql_text);
            ps.setInt(1, patient_id);
            //ps.setInt(2, location_id);
            rs = ps.executeQuery();
            while (rs.next()) {
                obs = constructObs2(rs);
                obsList.add(obs);
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            screen.updateStatus(ex.getMessage());
        } finally {
            cleanUp(rs, ps);
        }
        return obsList;
    }

    public Date calculateStopDate(Date startDate, int duration, String unit) {
        Date stopDate = null;
        int dayVal = calculateDayValue(duration, unit);
        /*int dayVal = 30;
        if (StringUtils.isNotBlank(unit)) {
            if (StringUtils.equalsIgnoreCase(unit, "MONTH(S)")) {
                dayVal = duration * 30;
            } else if (StringUtils.equalsIgnoreCase(unit, "DAY(S)")) {
                dayVal = duration;
            } else if (StringUtils.equalsIgnoreCase(unit, "WEEK(S)")) {
                dayVal = duration * 7;
            }

            //if (dayVal > 120) {
                //dayVal = 30;
            //}
        }*/
        System.out.println("Days val: " + dayVal);
        DateTime startDateTime = new DateTime(startDate);
        DateTime stopDateTime = startDateTime.plusDays(dayVal);
        stopDate = stopDateTime.toDate();
        return stopDate;
    }

    public int calculateDayValue(int duration, String unit) {
        int dayVal = 0;
        if (StringUtils.isNotBlank(unit)) {
            if (StringUtils.equalsIgnoreCase(unit, "MONTH(S)")) {
                dayVal = duration * 30;
            } else if (StringUtils.equalsIgnoreCase(unit, "DAY(S)")) {
                dayVal = duration;
            } else if (StringUtils.equalsIgnoreCase(unit, "WEEK(S)")) {
                dayVal = duration * 7;
            }
        }
        return dayVal;
    }

    public model.datapump.Obs constructObs(ResultSet rs) throws SQLException {
        model.datapump.Obs obs = new model.datapump.Obs();
        obs.setPatientID(rs.getInt("PATIENT_ID"));
        obs.setEncounterID(rs.getInt("ENCOUNTER_ID"));
        obs.setPepfarID(rs.getString("PEPFAR_ID"));
        obs.setHospID(rs.getString("HOSP_ID"));
        obs.setVisitDate(rs.getDate("VISIT_DATE"));
        obs.setFormName(rs.getString("PMM_FORM"));
        obs.setFormID(rs.getInt("FORM_ID"));
        obs.setConceptID(rs.getInt("CONCEPT_ID"));
        obs.setVariableName(rs.getString("VARIABLE_NAME"));
        obs.setVariableValue(rs.getString("VARIABLE_VALUE"));
        obs.setEnteredBy(rs.getString("ENTERED_BY"));
        obs.setDateEntered(rs.getDate("DATE_CREATED"));
        obs.setDateChanged(rs.getDate("DATE_CHANGED"));
        obs.setProvider(rs.getString("PROVIDER"));
        obs.setUuid(rs.getString("UUID"));
        obs.setLocationName(rs.getString("LOCATION"));
        obs.setLocationID(rs.getInt("LOCATION_ID"));
        obs.setCreator(rs.getInt("CREATOR_ID"));
        obs.setProviderID(rs.getInt("PROVIDER_ID"));
        obs.setValueNumeric(rs.getDouble("VALUE_NUMERIC"));
        obs.setValueCoded(rs.getInt("VALUE_CODED"));
        obs.setValueDate(rs.getDate("VALUE_DATETIME"));
        obs.setValueText(rs.getString("VALUE_TEXT"));
        obs.setValueBoolean(rs.getBoolean("VALUE_BOOL"));
        obs.setObsGroupID(rs.getInt("OBS_GROUP_ID"));
        obs.setVoided(rs.getInt("VOIDED"));
        obs.setDateVoided(rs.getDate("DATE_VOIDED"));
        obs.setVoidedBy(rs.getInt("VOIDED_BY"));
        return obs;
    }

    public ArrayList<model.datapump.Obs> getCommonQuestionsForPatient(int patientID, List<model.datapump.Obs> obsList) {
        ArrayList<model.datapump.Obs> ptsObsList = new ArrayList<model.datapump.Obs>();
        for (model.datapump.Obs obs : obsList) {
            if (obs.getPatientID() == patientID) {
                ptsObsList.add(obs);
            }
        }
        return ptsObsList;
    }

    public ArrayList<model.datapump.Visit> loadAllVisit() {
        ArrayList<model.datapump.Visit> visitList = new ArrayList<model.datapump.Visit>();
        /*String sql_text="select * from (select PATIENT_ID, PEPFAR_ID,HOSP_ID,VISIT_DATE from obs where VOIDED=0 \n" +
"UNION\n" +
"select PATIENT_ID,PEPFAR_ID,HOSP_ID,VISIT_DATE from REGIMEN) as sinner\n" +
"order by PEPFAR_ID ASC, VISIT_DATE ASC";*/
 /*String sql_text = "select DISTINCT obs.PATIENT_ID,obs.PEPFAR_ID,obs.HOSP_ID,"
                + "obs.VISIT_DATE FROM obs WHERE VOIDED=0 GROUP BY obs.PATIENT_ID,obs.VISIT_DATE";*/
        String sql_text = "select\n"
                + "DISTINCT\n"
                + "encounter.patient_id as PATIENT_ID,\n"
                + "encounter.encounter_datetime as VISIT_DATE,\n"
                + "pid1.identifier as PEPFAR_ID,\n"
                + "pid2.identifier as HOSP_ID\n"
                + "from\n"
                + "encounter \n"
                + "left join patient_identifier pid1 \n"
                + "on(encounter.patient_id=pid1.patient_id and pid1.identifier_type=4 and encounter.voided=0)\n"
                + "left join patient_identifier pid2 \n"
                + "on(encounter.patient_id=pid2.patient_id and pid2.identifier_type=3 and encounter.voided=0)\n"
                + "GROUP BY encounter.patient_id,encounter.encounter_datetime;";
        Statement stmt = null;
        ResultSet rs = null;
        model.datapump.Visit visit = null;
        try {
            stmt = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            rs = stmt.executeQuery(sql_text);
            while (rs.next()) {
                visit = constructVisit(rs);
                if (NDRWriter.isValidDate(visit.getVisitDate())) {
                    visitList.add(visit);
                }
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            screen.updateStatus(ex.getMessage());
        } finally {
            cleanUp(rs, stmt);
        }

        return visitList;
    }

    public ArrayList<model.datapump.Visit> loadAllVisit(Set<Integer> idSet) {
        ArrayList<model.datapump.Visit> visitList = new ArrayList<model.datapump.Visit>();
        screen.updateStatus("Loading visits ...");
        /*String sql_text="select * from (select PATIENT_ID, PEPFAR_ID,HOSP_ID,VISIT_DATE from obs where VOIDED=0 \n" +
"UNION\n" +
"select PATIENT_ID,PEPFAR_ID,HOSP_ID,VISIT_DATE from REGIMEN) as sinner\n" +
"order by PEPFAR_ID ASC, VISIT_DATE ASC";*/
 /*String sql_text = "select DISTINCT obs.PATIENT_ID,obs.PEPFAR_ID,obs.HOSP_ID,"
                + "obs.VISIT_DATE FROM obs WHERE VOIDED=0 GROUP BY obs.PATIENT_ID,obs.VISIT_DATE";*/
 /*String sql_text = "select\n"
                + "DISTINCT\n"
                + "encounter.patient_id as PATIENT_ID,\n"
                + "CAST(encounter.encounter_datetime AS DATE) as VISIT_DATE,\n"
                + "pid1.identifier as PEPFAR_ID,\n"
                + "pid2.identifier as HOSP_ID\n"
                + "from\n"
                + "encounter \n"
                + "left join patient_identifier pid1 \n"
                + "on(encounter.patient_id=pid1.patient_id and pid1.identifier_type=4 and encounter.voided=0)\n"
                + "left join patient_identifier pid2 \n"
                + "on(encounter.patient_id=pid2.patient_id and pid2.identifier_type=3 and encounter.voided=0)\n"
                + "where encounter.patient_id in (" + buildString(idSet) + ") GROUP BY encounter.patient_id,CAST(encounter.encounter_datetime AS DATE);";*/
        String sql_text = "select DISTINCT encounter.patient_id as PATIENT_ID,CAST(encounter.encounter_datetime AS DATE) as VISIT_DATE from encounter where "
                + "encounter.patient_id in (" + buildString(idSet) + ") and encounter.voided=0 GROUP BY encounter.patient_id,CAST(encounter.encounter_datetime AS DATE) ORDER BY encounter.patient_id,encounter.encounter_datetime  ";

        Statement stmt = null;
        ResultSet rs = null;
        model.datapump.Visit visit = null;
        try {
            stmt = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            rs = stmt.executeQuery(sql_text);
            while (rs.next()) {
                visit = constructVisit2(rs);
                if (NDRWriter.isValidDate(visit.getVisitDate())) {
                    visitList.add(visit);
                }
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            screen.updateStatus(ex.getMessage());
        } finally {
            cleanUp(rs, stmt);
        }

        return visitList;
    }

    public model.datapump.Visit constructVisit(ResultSet rs) throws SQLException {
        model.datapump.Visit vs = new model.datapump.Visit();
        String visitID = "";
        vs.setPatientID(rs.getInt("PATIENT_ID"));
        vs.setPepfarID(rs.getString("PEPFAR_ID"));
        vs.setHospID(rs.getString("HOSP_ID"));
        vs.setVisitDate(rs.getDate("VISIT_DATE"));
        visitID = formatDate(vs.getVisitDate()) + "-" + vs.getPepfarID();
        vs.setVisitID(visitID);
        return vs;
    }

    public model.datapump.Visit constructVisit2(ResultSet rs) throws SQLException {
        model.datapump.Visit vs = new model.datapump.Visit();
        String visitID = "", pepfarID = "", hospID = "";
        vs.setPatientID(rs.getInt("PATIENT_ID"));
        pepfarID = pepfarDictionary.get(rs.getInt("PATIENT_ID"));
        hospID = hospIDDictionary.get(rs.getInt("PATIENT_ID"));
        if (!StringUtils.isEmpty(pepfarID)) {
            vs.setPepfarID(pepfarID);
        } else {
            vs.setPepfarID(hospID);
        }

        vs.setHospID(hospIDDictionary.get(rs.getInt("PATIENT_ID")));
        vs.setVisitDate(rs.getDate("VISIT_DATE"));
        visitID = formatDate2(vs.getVisitDate()) + "-" + vs.getPepfarID();
        vs.setVisitID(visitID);
        return vs;
    }

    public ArrayList<model.datapump.Obs> getCommonQuestionObs() {
        ArrayList<model.datapump.Obs> obsList = new ArrayList<model.datapump.Obs>();
        screen.updateStatus("Loading common question obs...Pls wait");

        String sql_text = "select obs.* from obs \n"
                + "inner join(select obs.patient_id,obs.CONCEPT_ID,MAX(obs.VISIT_DATE) as lst_obs_dt from obs\n"
                + "where obs.CONCEPT_ID in(859,7777871,577,859) and obs.voided=0 group by patient_id,CONCEPT_ID) sinner\n"
                + "on(sinner.patient_id=obs.PATIENT_ID and sinner.concept_id=obs.CONCEPT_ID and obs.VISIT_DATE=sinner.lst_obs_dt)\n"
                + "where obs.VOIDED=0";

        Statement stmt = null;
        ResultSet rs = null;
        model.datapump.Obs obs = null;
        try {
            stmt = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            rs = stmt.executeQuery(sql_text);
            while (rs.next()) {
                obs = constructObs(rs);
                obsList.add(obs);
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            cleanUp(rs, stmt);
        }
        return obsList;
    }

    public ArrayList<model.datapump.Obs> getCommonQuestionObs(Set<Integer> idSet) {
        ArrayList<model.datapump.Obs> obsList = new ArrayList<model.datapump.Obs>();
        screen.updateStatus("Loading common question obs...Pls wait");

        /*String sql_text = "select obs.* from obs \n"
                + "inner join(select obs.patient_id,obs.CONCEPT_ID,MAX(obs.VISIT_DATE) as lst_obs_dt from obs\n"
                + "where obs.CONCEPT_ID in(859,7777871,577) and obs.voided=0 group by patient_id,CONCEPT_ID) sinner\n"
                + "on(sinner.patient_id=obs.PATIENT_ID and sinner.concept_id=obs.CONCEPT_ID and obs.VISIT_DATE=sinner.lst_obs_dt)\n"
                + "where obs.VOIDED=0";*/
        String sql_text = "select \n"
                + "    `obs`.`obs_id`,\n"
                + "    `obs`.`person_id`,\n"
                + "    `obs`.`obs_datetime`,\n"
                + "    `obs`.`concept_id`,\n"
                + "    `obs`.`value_numeric`,\n"
                + "     obs.value_coded,\n"
                + "     obs.value_text,\n"
                + "     obs.value_datetime,\n"
                + "     cast(coalesce(obs.value_coded,`obs`.`value_numeric`,`obs`.`value_datetime`,`obs`.`value_text`) as char charset utf8) AS `variable_value`,\n"
                + "     `obs`.`date_created`,\n"
                + "      encounter.date_changed,\n"
                + "      encounter.date_voided,\n"
                + "      encounter.changed_by,\n"
                + "      obs.voided,\n"
                + "      encounter.voided_by,\n"
                + "     `obs`.`creator` AS `creator`,\n"
                + "     `obs`.`encounter_id` AS `encounter_id`,\n"
                + "     `obs`.`obs_group_id` AS `obs_group_id`,\n"
                + "     `obs`.`uuid`,\n"
                + "     `encounter`.`form_id` AS `form_id`,\n"
                + "     `encounter_provider`.`provider_id` AS `provider_id`,\n"
                + "     `obs`.`location_id` AS `location_id` \n"
                + "     from `obs` \n"
                + "	inner join `patient` on(`patient`.`patient_id` = `obs`.`person_id`)\n"
                + "     inner join `encounter` on(`encounter`.`encounter_id` = `obs`.`encounter_id`)\n"
                + "     inner join `encounter_provider` on(encounter_provider.encounter_id=encounter.encounter_id and encounter_provider.voided=0 and encounter_provider.encounter_role_id=1)\n"
                + "	where encounter.form_id in(22,10,13,1,2,46,4) and obs.voided=0 and obs.person_id in(" + buildString(idSet) + ") order by obs.person_id";

        Statement stmt = null;
        ResultSet rs = null;
        model.datapump.Obs obs = null;
        try {
            stmt = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            rs = stmt.executeQuery(sql_text);
            while (rs.next()) {
                obs = constructObs2(rs);
                obsList.add(obs);
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            cleanUp(rs, stmt);
        }
        return obsList;
    }

    public ArrayList<model.datapump.Demographics> getAllPatientsInDB2(Set<Integer> idSet) {
        ArrayList<model.datapump.Demographics> patients = new ArrayList<model.datapump.Demographics>();
        screen.updateStatus("Getting patient demographics...");
        // HashMap<Integer,Demographics> demoMap=new HashMap<Integer,Demographics>();
        //String sql_text = "select *,TIMESTAMPDIFF(YEAR,dob,curdate()) as age2 from patient where pepfar_id is not null and dob is not null and first_name is not null and person_source_pk in( " + buildString(idSet) + " ) order by person_source_pk";
        String sql_text = "select \n"
                + "patient.patient_id as person_source_pk,\n"
                + "person.uuid as person_uuid,\n"
                + "pid1.identifier as pepfar_id,\n"
                + "pid2.identifier as hosp_id,\n"
                + "pid3.identifier as ehnid,\n"
                + "pid4.identifier as other_id,\n"
                + "person_name.given_name as first_name,\n"
                + "person_name.family_name as last_name,\n"
                + "person_name.middle_name as middle_name,\n"
                + "pprg1.date_enrolled as adult_enrollment_dt,\n"
                + "null as pead_enrollment_dt,\n"
                + "pprg3.date_enrolled as pmtct_enrollment_dt,\n"
                + "pprg4.date_enrolled as hei_enrollment_dt,\n"
                + "pprg5.date_enrolled as pep_enrollment_dt,\n"
                + "person.birthdate as dob,\n"
                + "TIMESTAMPDIFF(YEAR,person.birthdate,curdate()) as age2,\n"
                + "person.gender as gender,\n"
                + "person_address.address1,\n"
                + "person_address.address2,\n"
                + "person_address.city_village as address_lga,\n"
                + "person_address.state_province as address_state,\n"
                + "patient.creator as creator_id,\n"
                + "patient.date_created,\n"
                + "patient.date_changed,\n"
                + "pid1.location_id,\n"
                + "location.name as location,\n"
                + "CONCAT(pn1.given_name,' ',pn1.family_name) as creator\n"
                + "from patient\n"
                + "inner join person on(person.person_id=patient.patient_id and patient.voided=0)\n"
                + "left join patient_identifier pid1 on(pid1.patient_id=patient.patient_id and pid1.identifier_type=4)\n"
                + "left join patient_identifier pid2 on(pid2.patient_id=patient.patient_id and pid2.identifier_type=3)\n"
                + "left join patient_identifier pid3 on(pid3.patient_id=patient.patient_id and pid3.identifier_type=5)\n"
                + "left join patient_identifier pid4 on(pid4.patient_id=patient.patient_id and pid4.identifier_type=9)\n"
                + "left join person_name on(person_name.person_id=patient.patient_id)\n"
                + "left join patient_program pprg1 on(pprg1.patient_id=patient.patient_id and pprg1.program_id=3)\n"
                + "left join patient_program pprg3 on(pprg3.patient_id=patient.patient_id and pprg3.program_id=1)\n"
                + "left join patient_program pprg4 on(pprg4.patient_id=patient.patient_id and pprg4.program_id=9)\n"
                + "left join patient_program pprg5 on(pprg5.patient_id=patient.patient_id and pprg5.program_id=10)\n"
                + "left join person_address on(person_address.person_id=patient.patient_id and person_address.voided=0)\n"
                + "left join users on(users.user_id=patient.creator)\n"
                + "left join person_name pn1 on(pn1.person_id=users.person_id)\n"
                + "left join location on(location.location_id=pid1.location_id)\n"
                + "where patient.patient_id in(" + buildString(idSet) + ")";

        PreparedStatement ps = null;
        ResultSet rs = null;
        model.datapump.Demographics demo = null;
        try {
            ps = prepareQuery(sql_text);
            rs = ps.executeQuery();
            while (rs.next()) {
                demo = new model.datapump.Demographics();
                demo.setPatientID(rs.getInt("person_source_pk"));
                demo.setPatientUUID(rs.getString("person_uuid"));
                demo.setPepfarID(rs.getString("pepfar_id"));
                demo.setHospID(rs.getString("hosp_id"));
                demo.seteHNID(rs.getString("ehnid"));
                demo.setOtherID(rs.getString("other_id"));
                demo.setFirstName(rs.getString("first_name"));
                demo.setLastName(rs.getString("last_name"));
                demo.setMiddleName(rs.getString("middle_name"));
                demo.setAdultEnrollmentDt(rs.getDate("adult_enrollment_dt"));
                demo.setPeadEnrollmentDt(rs.getDate("pead_enrollment_dt"));
                demo.setPmtctEnrollmentDt(rs.getDate("pmtct_enrollment_dt"));
                demo.setHeiEnrollmentDt(rs.getDate("hei_enrollment_dt"));
                demo.setPepEnrollmentDt(rs.getDate("pep_enrollment_dt"));
                demo.setDateOfBirth(rs.getDate("dob"));
                demo.setAge(rs.getInt("age2"));
                demo.setGender(rs.getString("gender"));
                demo.setAddress1(rs.getString("address1"));
                demo.setAddress2(rs.getString("address2"));
                demo.setAddress_lga(rs.getString("address_lga"));
                demo.setAddress_state(rs.getString("address_state"));
                demo.setCreatorID(rs.getInt("creator_id"));
                demo.setDateCreated(rs.getDate("date_created"));
                demo.setDateChanged(rs.getDate("date_changed"));
                demo.setLocationID(rs.getInt("location_id"));
                demo.setCreatorName(rs.getString("creator"));
                demo.setLocationName(rs.getString("location"));
                patients.add(demo);
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            screen.updateStatus(ex.getMessage());
        } finally {
            cleanUp(rs, ps);
        }

        return patients;
    }

    public StringBuilder buildString(Set<Integer> ids) {
        StringBuilder sbuilder = new StringBuilder();
        for (int ele : ids) {
            if (sbuilder.length() > 0) {
                sbuilder.append(",");
            }
            sbuilder.append(ele);
        }
        return sbuilder;
    }

    public void runNigeriaQualExport(Date startDate, Date endDate, File file, model.datapump.Location loc) {
        screen.updateMinMaxProgress(0, 15);
        int count = 0;
        int location_id = loc.getLocationID();
        try {
            nigeriaQualWriter = new NigeriaQualWriter();
        } catch (Exception ex) {
            handleException(ex);
        }
        nigeriaQualWriter.setConceptDictionary(conceptDictionaryRL, locMap);
        //nigeriaQualWriter.loadDictionaries();
        count++;
        screen.updateProgress(count);
        Set<Integer> idSet = getSamplePatients(startDate, endDate, loc);
        loadFirstRegimens(idSet);
        System.out.print(idSet.size());
        count++;
        screen.updateProgress(count);
        runDataPatientDemographicExport(startDate, endDate, loc, file, idSet);
        count++;
        screen.updateProgress(count);
        runDataBaselineParameterExport(startDate, endDate, loc, file, idSet);
        count++;
        screen.updateProgress(count);
        runDataARTRecordExport(startDate, endDate, loc, file, idSet);
        count++;
        screen.updateProgress(count);
        runDataRegimenDuringReviewPeriod(startDate, endDate, loc, file, idSet);
        count++;
        screen.updateProgress(count);
        runDataARTAdherenceExport(startDate, endDate, loc, file, idSet);
        count++;
        screen.updateProgress(count);
        runViralLoadTestingExport(startDate, endDate, loc, file, idSet);
        count++;
        screen.updateProgress(count);
        runDataCotrimoxazoleExport2(startDate, endDate, loc, file, idSet);
        count++;
        screen.updateProgress(count);
        runClinicalEvalExport(startDate, endDate, loc, file, idSet);
        count++;
        screen.updateProgress(count);
        runDataTuberculosisRecord(startDate, endDate, loc, file, idSet);
        count++;
        screen.updateProgress(count);
        runCareAndSupportExport(startDate, endDate, loc, file, idSet);
        count++;
        screen.updateProgress(count);
        runDataHepatitisB(startDate, endDate, loc, file, idSet);
        count++;
        screen.updateProgress(count);
        runDataPatientMonitoringDuringReviewPeriod(startDate, endDate, loc, file, idSet);
        count++;
        screen.updateProgress(count);
        runDataPatientStatusReviewPeriod(startDate, endDate, loc, file, idSet);
        count++;
        screen.updateProgress(count);
        addToSpecialZip(zipFileEntryNames, file.getAbsolutePath());
        count++;
        screen.updateProgress(count);
        screen.updateProgress(15);
        screen.updateStatus("Export completed thank you");
    }

    public void runDataPatientDemographicExport(Date startDate, Date endDate, model.datapump.Location loc, File file, Set<Integer> idSet) {
        screen.updateStatus("Writing DataPatientDemographics.xml file...Please wait");
        long startTime = System.currentTimeMillis();
        int location_id = loc.getLocationID();
        ArrayList<model.datapump.Obs> personalHistoryList = getAllPersonalHistoryObs(location_id, idSet);
        ArrayList<Integer> clinicVisitReportingPeriod = getClinicVisitReportingPeriod(startDate, endDate, location_id);
        ArrayList<Integer> patientHadClinicVisit3MonthReviewPeriod = getClinicVisit3MonthsReportingPeriod(startDate, endDate, location_id);
        ArrayList<Integer> hospitalizedReportingPeriod = getHospitalizedReportingPeriod(startDate, endDate, location_id);
        ArrayList<DataPatientDemographics> dataPatientDemographicList = new ArrayList<DataPatientDemographics>();
        DataPatientDemographics dataPatientDemo = null;
        for (model.datapump.Demographics ele : patientDemoList) {
            if (idSet.contains(ele.getPatientID()) && !(ele.getPepfarID().isEmpty())) {
                dataPatientDemo = nigeriaQualWriter.createDataPatientDemographics(ele, personalHistoryList, hospitalizedReportingPeriod, clinicVisitReportingPeriod, patientHadClinicVisit3MonthReviewPeriod, firstVisitDateDictionary, loc);
                dataPatientDemographicList.add(dataPatientDemo);
            }
        }
        String fileName = "Data PatientDemographics.xml";
        int count = 0;
        zipFileEntryNames.add(fileName);

        try {
            mgr.createXMLWriter(fileName);
            mgr.openXMLDocument();
            mgr.writeXMLHeader(COMMON_HEADER, NAMES_SPACE);
            for (DataPatientDemographics ele : dataPatientDemographicList) {
                mgr.writeToXML(ele);
                count++;
                screen.updateStatus("Writing DataPatientDemographics.xml file.." + count + " files written");
            }
            mgr.endXMLDocument();
            mgr.closeXMLWriter();
            long duration = calculateDuration(startTime);
            screen.updateStatus("DataPatientDemographics.xml completed in " + duration + " secs...Pls wait");
        } catch (XMLStreamException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public ArrayList<Integer> getClinicVisit3MonthsReportingPeriod(Date startDate, Date endDate, int location_id) {
        ArrayList<Integer> patients = new ArrayList<Integer>();
        String sql_text = "select DISTINCT encounter.patient_id PATIENT_ID from encounter where TIMESTAMPDIFF(MONTH,encounter.encounter_datetime,?)<=3 AND location_id=? AND VOIDED=0";
        PreparedStatement ps = null;
        ResultSet rs = null;
        int patientID = 0;
        try {
            ps = prepareQuery(sql_text);
            ps.setDate(1, convertToSQLDate(startDate));
            //ps.setDate(2, convertToSQLDate(endDate));
            ps.setInt(2, location_id);
            rs = ps.executeQuery();
            while (rs.next()) {
                patientID = rs.getInt("PATIENT_ID");
                patients.add(patientID);
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            screen.updateStatus(ex.getMessage());
        } finally {
            cleanUp(rs, ps);
        }
        return patients;
    }

    public ArrayList<Integer> getClinicVisitReportingPeriod(Date startDate, Date endDate, int location_id) {
        ArrayList<Integer> patients = new ArrayList<Integer>();
        //String sql_text = "select DISTINCT PATIENT_ID from obs where VISIT_DATE BETWEEN ? AND ? AND LOCATION_ID=? AND VOIDED=0";
        String sql_text = "select distinct encounter.patient_id from encounter where encounter_datetime between ? and ? and voided=0 and encounter.location_id=?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        int patientID = 0;
        try {
            ps = prepareQuery(sql_text);
            ps.setDate(1, convertToSQLDate(startDate));
            ps.setDate(2, convertToSQLDate(endDate));
            ps.setInt(3, location_id);
            rs = ps.executeQuery();
            while (rs.next()) {
                patientID = rs.getInt("patient_id");
                patients.add(patientID);
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            screen.updateStatus(ex.getMessage());
        } finally {
            cleanUp(rs, ps);
        }
        return patients;
    }

    public ArrayList<Integer> getHospitalizedReportingPeriod(Date startDate, Date endDate, int location_id) {
        ArrayList<Integer> patients = new ArrayList<Integer>();
        //String sql_text = "select DISTINCT PATIENT_ID from obs where VISIT_DATE BETWEEN ? AND ? AND LOCATION_ID=? AND CONCEPT_ID=45 AND VALUE_CODED=80 AND VOIDED=0";
        String sql_text = "select DISTINCT encounter.patient_id from encounter inner join obs on(encounter.encounter_id=obs.encounter_id and encounter.voided=0) where obs_datetime BETWEEN ? AND ? AND concept_id=45 AND value_coded=80 AND encounter.voided=0 and encounter.location_id=?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        int patientID = 0;
        try {
            ps = prepareQuery(sql_text);
            ps.setDate(1, convertToSQLDate(startDate));
            ps.setDate(2, convertToSQLDate(endDate));
            ps.setInt(3, location_id);
            rs = ps.executeQuery();
            while (rs.next()) {
                patientID = rs.getInt("patient_id");
                patients.add(patientID);
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            screen.updateStatus(ex.getMessage());
        } finally {
            cleanUp(rs, ps);
        }
        return patients;
    }

    public ArrayList<model.datapump.Obs> getAllPersonalHistoryObs(int location_id, Set<Integer> idSet) {
        ArrayList<model.datapump.Obs> obsList = new ArrayList<model.datapump.Obs>();
        //String sql_text = "select DISTINCT * from obs where form_id in(19,28,29,17,1) and VOIDED=0 and LOCATION_ID=" + location_id + " AND obs.patient_id in(" + buildString(idSet) + ") and patient_id is not null GROUP BY PATIENT_ID, CONCEPT_ID ORDER BY DATE_CREATED DESC ";
        String sql_text = "select \n"
                + "                `obs`.`obs_id`,\n"
                + "                `obs`.`person_id`,\n"
                + "                `obs`.`obs_datetime`,\n"
                + "                `obs`.`concept_id`,\n"
                + "                 obs.value_numeric,\n"
                + "                 obs.value_coded,\n"
                + "                 obs.value_text,\n"
                + "                 obs.value_datetime,\n"
                + "                 obs.value_boolean,\n"
                + "                 cast(coalesce(obs.value_coded,`obs`.`value_numeric`,`obs`.`value_datetime`,`obs`.`value_text`) as char charset utf8) AS `variable_value`,\n"
                + "                `obs`.`date_created`,\n"
                + "                 encounter.date_changed,\n"
                + "                 encounter.date_voided,\n"
                + "                 encounter.changed_by,\n"
                + "                 obs.voided,\n"
                + "                 encounter.voided_by,\n"
                + "                `obs`.`creator` AS `creator`,\n"
                + "                `obs`.`encounter_id` AS `encounter_id`,\n"
                + "                `obs`.`obs_group_id` AS `obs_group_id`,\n"
                + "                `obs`.`uuid`,\n"
                + "                `encounter`.`form_id` AS `form_id`,\n"
                + "                `encounter`.`provider_id` AS `provider_id`,\n"
                + "                `obs`.`location_id` AS `location_id` \n"
                + "                from `obs` inner join `patient` on(`patient`.`patient_id` = `obs`.`person_id`)\n"
                + "                inner join `encounter` on(`encounter`.`encounter_id` = `obs`.`encounter_id`) \n"
                + "				where encounter.form_id in(19,28,29,17,1)\n"
                + "				and encounter.voided=0\n"
                + "				and encounter.patient_id in(" + buildString(idSet) + ") and encounter.patient_id is not null \n"
                + "				GROUP BY obs.person_id, obs.concept_id ORDER BY DATE_CREATED DESC\n";

        Statement stmt = null;
        ResultSet rs = null;
        model.datapump.Obs obs = null;
        try {
            stmt = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            rs = stmt.executeQuery(sql_text);
            //ps = prepareQuery(sql_text);
            //ps.setInt(1, location_id);
            // rs = ps.executeQuery();
            while (rs.next()) {
                obs = constructObs2(rs);
                obsList.add(obs);
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            screen.updateStatus(ex.getMessage());
        } finally {
            cleanUp(rs, stmt);
        }
        return obsList;
    }
    public model.datapump.DrugOrder constructDrugOrder(ResultSet rs) throws SQLException{
        model.datapump.DrugOrder order=new model.datapump.DrugOrder();
        order.setPatientID(rs.getInt("patient_id"));
        order.setHospID(rs.getString("hosp_id"));
        order.setPepfarID(rs.getString("pepfar_id"));
        order.setEncounterID(rs.getInt("encounter_id"));
        order.setStartDate(rs.getDate("start_date"));
        order.setStopDate(rs.getDate("discontinued_date"));
        
        return order;
    }
    public model.datapump.Obs constructObs2(ResultSet rs) throws SQLException {
        model.datapump.Obs obs = new model.datapump.Obs();
        int concept_id = 0, value_coded = 0;
        User enteredByUser = null;
        PersonName name = null;
        obs.setObsID(rs.getInt("obs_id"));
        obs.setPatientID(rs.getInt("person_id"));
        obs.setEncounterID(rs.getInt("encounter_id"));
        obs.setPepfarID(pepfarDictionary.get(rs.getInt("person_id")));
        obs.setHospID(hospIDDictionary.get(rs.getInt("person_id")));
        obs.setVisitDate(rs.getDate("obs_datetime"));
        obs.setFormName(formNamesDictionary.get(rs.getInt("form_id")));
        //obs.setFormID(rs.getInt("form_id"));
        obs.setConceptID(rs.getInt("concept_id"));
        obs.setVariableName(conceptDictionary.get(rs.getInt("concept_id")).getConceptName());
        value_coded = rs.getInt("value_coded");
        obs.setValueCoded(value_coded);
        if (isValueCodedConcept(obs.getConceptID()) && conceptDictionary.get(rs.getInt("value_coded")) != null) {
            obs.setVariableValue(conceptDictionary.get(rs.getInt("value_coded")).getConceptName());
        } else {
            obs.setVariableValue(rs.getString("variable_value"));
        }
        enteredByUser = userDictionary.get(rs.getInt("creator"));

        if (enteredByUser != null) {
            obs.setEnteredBy(enteredByUser.getFullName());
        }
        obs.setDateEntered(rs.getDate("date_created"));
        obs.setDateChanged(rs.getDate("date_changed"));
        name = namesDictionary.get(rs.getInt("provider_id"));
        if (name != null) {
            obs.setProvider(name.getFullName());
        }
        obs.setUuid(rs.getString("uuid"));
        obs.setLocationName(locationDictionary.get(rs.getInt("location_id")));
        obs.setLocationID(rs.getInt("location_id"));
        obs.setValueText(rs.getString("value_text"));
        obs.setValueDate(rs.getDate("value_datetime"));
        //obs.setValueBoolean(rs.getBoolean("value_boolean"));
        obs.setValueNumeric(rs.getDouble("value_numeric"));
        obs.setValueCoded(rs.getInt("value_coded"));
        obs.setObsGroupID(rs.getInt("obs_group_id"));
        obs.setVoided(rs.getInt("voided"));
        obs.setVoidedBy(rs.getInt("voided_by"));
        obs.setDateVoided(rs.getDate("date_voided"));
        obs.setProviderID(rs.getInt("provider_id"));
        obs.setCreator(rs.getInt("creator"));
        obs.setFormID(rs.getInt("form_id"));
        return obs;
    }

    public void runDataRegimenDuringReviewPeriod(Date startDate, Date endDate, model.datapump.Location loc, File file, Set<Integer> idSet) {
        screen.updateStatus("Writing Data ARTRegimenDuringReviewPeriod.xml...Please wait");
        ArrayList<Integer> patientOnARTFirstDayList = null, patientOnARTAnytimeList = null;
        ArrayList<model.datapump.PatientRegimen> ptsRegimenList = null;
        ArrayList<DataRegimenDuringReview> dataRegimenDuringReviewList = null;
        int location_id = loc.getLocationID();
        patientOnARTFirstDayList = getPatientsOnARTFirstDayOfReview(startDate, endDate, location_id);
        patientOnARTAnytimeList = getPatientsOnARTAnytimeOfReviewPeriod(startDate, endDate, location_id);
        ptsRegimenList = getAllPatientRegimen(startDate, endDate, location_id, idSet);
        dataRegimenDuringReviewList = nigeriaQualWriter.createDataARTRegimen(patientDemoMap, ptsRegimenList, patientOnARTFirstDayList, patientOnARTAnytimeList, loc, idSet);//DataARTRegimenDuringReviewPeriod
        String fileName = "Data ARTRegimenDuringReviewPeriod.xml";
        zipFileEntryNames.add(fileName);
        int count = 0;
        try {
            mgr.createXMLWriter(fileName);
            mgr.openXMLDocument();
            mgr.writeXMLHeader(COMMON_HEADER, NAMES_SPACE);

            for (DataRegimenDuringReview drr : dataRegimenDuringReviewList) {
                mgr.writeToXML(drr);
                count++;
                screen.updateStatus("Writing Data ARTRegimenDuringReviewPeriod.xml...Please wait " + count);
            }
            mgr.endXMLDocument();
            mgr.closeXMLWriter();
        } catch (XMLStreamException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        }

    }

    public ArrayList<Date> extractVisits(int pid, List<model.datapump.Obs> obsList) {
        ArrayList<Date> dateList = new ArrayList<Date>();
        Set<Date> dateSet = new HashSet<Date>();
        for (Obs ele : obsList) {
            if (ele.getPatientID() == pid && ele.getVisitDate() != null) {
                dateSet.add(ele.getVisitDate());
            }

        }
        dateList.addAll(dateSet);
        return dateList;
    }

    public PatientRegimen extractRegimenForVisit(Date visitDate, int pid, List<Obs> obsList) {
        PatientRegimen regimen = new PatientRegimen();
        for (Obs obs : obsList) {
            if (obs.getPatientID() == pid && obs.getVisitDate().equals(visitDate)) {

            }
        }
        return regimen;
    }

    public ArrayList<Date> getVisitDatesForPatientForForms(Set<Integer> formIDSet, int patientID, Date endDate) {
        ArrayList<Date> visitDateList = new ArrayList<Date>();
        String sql_text = "select distinct encounter.encounter_datetime as visit_date "
                + " from encounter where encounter.patient_id=? and encounter.form_id in (" + buildString(formIDSet) + ") and encounter.voided=0 and encounter.encounter_datetime<=?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = prepareQuery(sql_text);
            ps.setInt(1, patientID);
            ps.setDate(2, convertToSQLDate(endDate));
            rs = ps.executeQuery();
            while (rs.next()) {
                visitDateList.add(rs.getDate("visit_date"));
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            screen.updateStatus(ex.getMessage());
        } finally {
            cleanUp(rs, ps);
        }
        return visitDateList;
    }

    public ArrayList<model.datapump.PatientRegimen> getAllPatientRegimen(Date startDate, Date endDate, int location_id, Set<Integer> idSet) {
        screen.updateStatus("Loading patient regimen... Please wait");

        ArrayList<model.datapump.PatientRegimen> ptsRegimenList = new ArrayList<model.datapump.PatientRegimen>();
        List<model.datapump.Obs> pharmacyFormObsList = null;
        ArrayList<Date> ptsVisitDates = null;
        Integer[] formArr = {86, 53, 46, 56, 18, 20};
        ArrayList<Obs> ptsObsVisitList = null;
        model.datapump.PatientRegimen ptsRegimen = null;
        Set<Integer> formIDSet = new HashSet<Integer>(Arrays.asList(formArr));
        int count = 0;
        for (int pid : idSet) {
            ptsVisitDates = getVisitDatesForPatientForForms(formIDSet, pid, endDate);
            for (Date dele : ptsVisitDates) {
                ptsObsVisitList = getObsForPatientForVisit(pid, dele);
                ptsRegimen = extractPatientRegimen(ptsObsVisitList, pid, dele);
                if (StringUtils.isNotEmpty(ptsRegimen.getRegimenName())) {
                    ptsRegimenList.add(ptsRegimen);
                    screen.updateStatus("Regimen... " + count + " " + ptsRegimen.getRegimenName());
                    count++;
                }

            }
        }
        //List<Obs> pharmacyFormObsList=getObsFromPharmacyForPatient(location_id, endDate)

        //ArrayList<model.datapump.PatientRegimen> ptsRegimenList = new ArrayList<model.datapump.PatientRegimen>();
        /* Statement stmt = null;
        ResultSet rs = null;
        int count = 0;
        try {
            stmt = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            rs = stmt.executeQuery(sql_text);
            while (rs.next()) {
                ptsRegimen = constructRegimen(rs);
                ptsRegimenList.add(ptsRegimen);
                count++;
                screen.updateStatus("Loading patient regimen... Please wait " + count);
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            screen.updateStatus(ex.getMessage());
        } finally {
            cleanUp(rs, stmt);
        }*/
        return ptsRegimenList;
    }

    public model.datapump.PatientRegimen constructRegimen(ResultSet rs) throws SQLException {
        model.datapump.PatientRegimen order = new model.datapump.PatientRegimen();
        int duration = 0;
        String duration_unit = "";
        Date stopDate = null;
        order = new model.datapump.PatientRegimen();
        order.setPatientID(rs.getInt("patient_id"));
        order.setPepfarID(rs.getString("pepfar_id"));
        order.setHospID(rs.getString("hosp_id"));
        order.setStartDate(rs.getDate("visit_date"));
        duration = rs.getInt("duration");
        duration_unit = rs.getString("duration_unit");
        stopDate = calculateStopDate(order.getStartDate(), duration, duration_unit);
        //order.setStopDate(rs.getDate("STOP_DATE"));
        order.setDrugName(rs.getString("drug_name"));

        order.setRegimenName(rs.getString("regimen"));
        order.setCode(String.valueOf(rs.getInt("regimen_code")));
        order.setRegimenLine(rs.getString("regimen_line"));
        order.setEnteredBy(rs.getString("entered_by"));
        order.setDateEntered(rs.getDate("date_entered"));
        order.setCreator(rs.getInt("creator_id"));
        return order;
    }

    public model.datapump.PatientRegimen constructRegimen2(ResultSet rs) throws SQLException {
        model.datapump.PatientRegimen order = new model.datapump.PatientRegimen();
        int duration = 0;
        String duration_unit = "";
        Date stopDate = null;
        order = new model.datapump.PatientRegimen();
        order.setPatientID(rs.getInt("patient_id"));
        order.setPepfarID(rs.getString("pepfar_id"));
        order.setHospID(rs.getString("hosp_id"));
        order.setStartDate(rs.getDate("visit_date"));
        duration = rs.getInt("duration");
        //duration_unit = rs.getString("duration_unit");
        //stopDate = calculateStopDate(order.getStartDate(), duration, duration_unit);
        order.setStopDate(rs.getDate("stop_date"));
        order.setDrugName(rs.getString("drug_name"));

        //order.setRegimenName(rs.getString("regimen"));
        //order.setCode(String.valueOf(rs.getInt("regimen_code")));
        //order.setRegimenLine(rs.getString("regimen_line"));
        order.setEnteredBy(rs.getString("entered_by"));
        order.setDateEntered(rs.getDate("date_entered"));
        order.setCreator(rs.getInt("creator_id"));
        return order;
    }

    public ArrayList<Integer> getPatientsOnARTAnytimeOfReviewPeriod(Date startDate, Date endDate, int location_id) {
        ArrayList<Integer> patientList = new ArrayList<Integer>();
        //String sql_text = "SELECT PATIENT_ID FROM regimen \n"
        // + "where REGIMEN_CODE is not null  AND  VISIT_DATE <= ?";
        String sql_text = "select DISTINCT encounter.patient_id from encounter inner join obs"
                + " on(encounter.encounter_id=obs.encounter_id and encounter.voided=0) where concept_id in(7778111,7778531) and encounter.voided=0 and encounter.encounter_datetime >=? GROUP BY encounter.patient_id";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = prepareQuery(sql_text);
            //ps.setDate(1, convertToSQLDate(startDate));
            ps.setDate(1, convertToSQLDate(endDate));
            rs = ps.executeQuery();
            while (rs.next()) {
                patientList.add(rs.getInt("patient_id"));
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            screen.updateStatus(ex.getMessage());
        }
        return patientList;
    }

    public ArrayList<Integer> getPatientsOnARTFirstDayOfReview(Date startDate, Date endDate, int location_id) {
        ArrayList<Integer> patientList = new ArrayList<Integer>();
        /*String sql_text = "SELECT PATIENT_ID FROM regimen \n"
                + "where regimen is not null  AND  VISIT_DATE<= ?";*/
        String sql_text = "select DISTINCT encounter.patient_id from encounter inner join obs"
                + " on(encounter.encounter_id=obs.encounter_id and encounter.voided=0) where concept_id in(7778111,7778531) and obs.voided=0  and encounter.encounter_datetime <=? GROUP BY encounter.patient_id";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = prepareQuery(sql_text);
            ps.setDate(1, convertToSQLDate(startDate));
            rs = ps.executeQuery();
            while (rs.next()) {
                patientList.add(rs.getInt("patient_id"));
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            screen.updateStatus(ex.getMessage());
        }
        return patientList;
    }

    public void runDataCotrimoxazoleExport2(Date startDate, Date endDate, model.datapump.Location loc, File file, Set<Integer> idSet) {
        screen.updateStatus("Writing DataCotrimoxazoleReportingPeriod.xml...");
        //ArrayList<PatientRegimen> ptsCotrimRegimenList=getLastReceivedCotrimReviewPeriod(startDate, endDate, locationID, idSet);
        HashMap<Integer, Date> ctxMap = null;
        int locationID = loc.getLocationID();
        ctxMap = getLastReceivedCotrimReviewPeriod2(startDate, endDate, locationID, idSet);
        ArrayList<DataCotrimoxazole> ctxList = nigeriaQualWriter.createDataCotrimoxazole2(ctxMap, idSet, patientDemoMap, loc);
        DataCotrimoxazole dc = null;
        String fileName = "DataCotrimoxazoleReportingPeriod.xml";
        zipFileEntryNames.add(fileName);
        int count = 0;
        try {
            mgr.createXMLWriter(fileName);
            mgr.openXMLDocument();
            mgr.writeXMLHeader(COMMON_HEADER, NAMES_SPACE);
            for (DataCotrimoxazole ele : ctxList) {
                //dc=nigeriaQualWriter.createDataCotrimoxazole(ele, locationID);
                mgr.writeToXML(ele);
                screen.updateStatus("Writing DataCotrimoxazoleReportingPeriod.xml..." + count);
                count++;
            }
            mgr.endXMLDocument();
            mgr.closeXMLWriter();
        } catch (XMLStreamException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public HashMap<Integer, Date> getLastReceivedCotrimReviewPeriod2(Date startDate, Date endDate, int locationID, Set<Integer> idSet) {
        /* String sql_text = "select patient.person_source_pk pid,LST_CTX_DT dt from patient\n"
                + "inner join\n"
                + "(select drug.PATIENT_ID,MAX(drug.START_DATE) LST_CTX_DT\n"
                + " from drug where drug.DRUG LIKE '%Cotri%' AND START_DATE <=? GROUP BY PATIENT_ID) sinner\n"
                + " ON(sinner.patient_id=patient.person_source_pk)";*/
        String sql_text = "select\n"
                + "obs.person_id pid,\n"
                + "MAX(obs.obs_datetime) as dt,\n"
                + "obs.concept_id,\n"
                + "obs.value_coded\n"
                + "from obs \n"
                + "where concept_id in(7778364,7778203)\n"
                + "and value_coded in(963,1547,1552,7778548)\n"
                + "and obs.obs_datetime <= ?\n"
                + "and obs.voided=0 and obs.person_id in(" + buildString(idSet) + ") group by obs.person_id,obs.concept_id";
        PreparedStatement ps = null;
        ResultSet rs = null;
        model.datapump.PatientRegimen ptsRegimen = null;
        HashMap<Integer, Date> lstCTXMap = new HashMap<Integer, Date>();

        try {
            ps = prepareQuery(sql_text);
            ps.setDate(1, convertToSQLDate(endDate));
            rs = ps.executeQuery();
            while (rs.next()) {
                lstCTXMap.put(rs.getInt("pid"), rs.getDate("dt"));
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }
        return lstCTXMap;
    }

    public void runClinicalEvalExport(Date startDate, Date endDate, model.datapump.Location loc, File file, Set<Integer> idSet) {
        screen.updateStatus("Writing DataClinicalEvaluation.xml...");
        Integer[] formArr = {18, 24, 27, 20, 56};
        int locationID = loc.getLocationID();
        Set<Integer> formSet = new HashSet<Integer>(Arrays.asList(formArr));
        DataClinicalEvaluationInReviewPeriod dc = null;
        ArrayList<Form> formList = getFormForPatient(idSet, formSet, startDate, endDate, locationID);
        System.out.println(formList.size());
        String pepfarID = "";
        model.datapump.Demographics demo = null;
        String fileName = "DataClinicalEval.xml";
        zipFileEntryNames.add(fileName);
        int count = 0;
        try {
            mgr.createXMLWriter(fileName);
            mgr.openXMLDocument();
            mgr.writeXMLHeader(COMMON_HEADER, NAMES_SPACE);
            for (int pid : idSet) {
                demo = patientDemoMap.get(pid);
                pepfarID = demo.getPepfarID();
                dc = nigeriaQualWriter.createClinicalEvalReviewPeriod(formList, pid, pepfarID, loc);
                screen.updateStatus("Writing DataClinicalEvaluation.xml..." + count);
                mgr.writeToXML(dc);
                count++;

            }
            mgr.endXMLDocument();
            mgr.closeXMLWriter();
        } catch (XMLStreamException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<Form> getFormForPatient(Set<Integer> idSet, Set<Integer> formidSet, Date startDate, Date endDate, int locationID) {
        ArrayList<Form> formList = new ArrayList<Form>();
        /*String sql_text = "select \n"
                + "DISTINCT\n"
                + "PATIENT_ID,\n"
                + "PEPFAR_ID,\n"
                + "PMM_FORM,\n"
                + "VISIT_DATE,\n"
                + "DATE_CREATED,\n"
                + "DATE_CHANGED,\n"
                + "CREATOR_ID,\n"
                + "FORM_ID\n"
                + "FROM obs WHERE PATIENT_ID IN(" + buildString(idSet) + ") AND FORM_ID IN(" + buildString(formidSet) + ") AND VISIT_DATE BETWEEN ? AND ? AND LOCATION_ID=? GROUP BY PATIENT_ID,FORM_ID,VISIT_DATE";*/
        String sql_text = "select \n"
                + "DISTINCT\n"
                + "encounter.patient_id,\n"
                + "encounter.form_id,\n"
                + "encounter.encounter_datetime,\n"
                + "encounter.date_created,\n"
                + "encounter.date_changed,\n"
                + "encounter.creator\n"
                + "from encounter \n"
                + "where encounter.patient_id in(" + buildString(idSet) + ")\n"
                + "and encounter.form_id in(" + buildString(formidSet) + ")\n"
                + "and encounter.encounter_datetime BETWEEN ? AND ?\n"
                + "GROUP BY encounter.patient_id,encounter.form_id,encounter.encounter_datetime;";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Form frm = null;
        try {
            ps = prepareQuery(sql_text);
            ps.setDate(1, convertToSQLDate(startDate));
            ps.setDate(2, convertToSQLDate(endDate));
            //ps.setInt(3, locationID);
            rs = ps.executeQuery();
            while (rs.next()) {
                frm = constructForm(rs);
                formList.add(frm);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }

        return formList;
    }

    public Form constructForm(ResultSet rs) throws SQLException {
        Form form = new Form();
        form.setFormID(rs.getInt("form_id"));
        form.setFormName(formNamesDictionary.get(rs.getInt("form_id")));
        form.setPatientID(rs.getInt("patient_id"));
        form.setVisitDate(rs.getDate("encounter_datetime"));
        form.setDateEntered(rs.getDate("date_created"));
        form.setDateChanged(rs.getDate("date_changed"));
        form.setCreatorID(rs.getInt("creator"));
        form.setPepfarID(pepfarDictionary.get(rs.getInt("patient_id")));
        return form;
    }

    public Form constructForm2(ResultSet rs) throws SQLException {
        Form form = new Form();
        form.setFormID(rs.getInt("FORM_ID"));
        form.setFormName(rs.getString("PMM_FORM"));
        form.setPatientID(rs.getInt("PATIENT_ID"));
        form.setVisitDate(rs.getDate("VISIT_DATE"));
        form.setDateEntered(rs.getDate("DATE_CREATED"));
        form.setDateChanged(rs.getDate("DATE_CHANGED"));
        form.setCreatorID(rs.getInt("CREATOR_ID"));
        form.setPepfarID(rs.getString("PEPFAR_ID"));
        return form;
    }

    public void runDataTuberculosisRecord(Date startDate, Date endDate, model.datapump.Location loc, File file, Set<Integer> idSet) {
        screen.updateStatus("Writing DataTuberculosis.xml...");
        HashMap<Integer, model.datapump.Obs> obsTBStatusMap = null;
        HashMap<Integer, model.datapump.Obs> obsTBScrn = null;
        Set<Integer> ptsTbTreatmentStartDtSet = null;
        Set<Integer> csrSet = null;
        Set<Integer> ptsTBScreenSet = null;
        DataTuberculosisRecord dtr = null;
        int count = 0;
        String pepfarID = null;
        int conceptID = 862;
        int locationID = loc.getLocationID();
        obsTBStatusMap = getLastOfObsForPatients(startDate, endDate, locationID, idSet, conceptID);
        conceptID = 1045;
        obsTBScrn = getLastOfObsForPatients(startDate, endDate, locationID, idSet, conceptID);
        ptsTBScreenSet = getAllPatientsWithClinicalEvalFormsFilled(idSet, startDate, endDate);
        ptsTbTreatmentStartDtSet = getPatientsOnTBTreatment6MonthsBeforeRP(startDate, endDate, locationID, idSet);
        csrSet = getPatientCXRPerformed(startDate, endDate, locationID, idSet);
        String fileName = "DataTuberculosis.xml";
        model.datapump.Demographics demo = null;
        zipFileEntryNames.add(fileName);
        try {
            mgr.createXMLWriter(fileName);
            mgr.openXMLDocument();
            mgr.writeXMLHeader(COMMON_HEADER, NAMES_SPACE);
            for (int ele : idSet) {
                demo = patientDemoMap.get(ele);
                if (demo != null) {
                    pepfarID = demo.getPepfarID();
                }
                dtr = nigeriaQualWriter.createDataTBRecord(ptsTBScreenSet, obsTBStatusMap, obsTBScrn, ptsTbTreatmentStartDtSet, csrSet, ele, pepfarID, loc);
                mgr.writeToXML(dtr);
                screen.updateStatus("DataTuberculosis.xml..." + count);
                count++;
            }
            mgr.endXMLDocument();
            mgr.closeXMLWriter();

        } catch (XMLStreamException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Set<Integer> getPatientsOnTBTreatment6MonthsBeforeRP(Date startDate, Date endDate, int locationID, Set<Integer> idSet) {
        Set<Integer> tbSet = new HashSet<Integer>();
        //String sql_text = "select obs.PATIENT_ID from obs where TIMESTAMPDIFF(MONTH,VISIT_DATE,?)<=6 AND obs.CONCEPT_ID=862 AND obs.VALUE_CODED=872 AND obs.LOCATION_ID=? AND obs.PATIENT_ID IN(" + buildString(idSet) + ")";
        String sql_text = "select \n"
                + "DISTINCT encounter.patient_id\n"
                + "from \n"
                + "encounter \n"
                + "inner join obs on(obs.encounter_id=encounter.encounter_id)\n"
                + "where\n"
                + "obs.concept_id=862\n"
                + "and\n"
                + "obs.value_coded=872\n"
                + "and \n"
                + "encounter.patient_id in(" + buildString(idSet) + ")\n"
                + "and \n"
                + "TIMESTAMPDIFF(MONTH,encounter.encounter_datetime,?)<=6\n"
                + "and \n"
                + "encounter.voided=0";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = prepareQuery(sql_text);
            ps.setDate(1, convertToSQLDate(startDate));
            //ps.setInt(2, locationID);
            int pid = 0;
            rs = ps.executeQuery();
            while (rs.next()) {
                pid = rs.getInt("patient_id");
                tbSet.add(pid);
                //System.out.println("TBTreat: "+pid);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }
        return tbSet;
    }

    public Set<Integer> getPatientCXRPerformed(Date startDate, Date endDate, int locationID, Set<Integer> idSet) {
        Set<Integer> ptsSet = new HashSet<Integer>();
        //String sql_text = "select obs.PATIENT_ID from obs where CONCEPT_ID=1608 AND VALUE_CODED=227 AND obs.VISIT_DATE BETWEEN ? AND ? AND obs.LOCATION_ID=? AND PATIENT_ID IN(" + buildString(idSet) + ")";
        String sql_text = "select DISTINCT encounter.patient_id from encounter inner join obs on(obs.encounter_id=encounter.encounter_id)\n"
                + "where obs.concept_id=862 and obs.value_coded=870 and encounter.patient_id in(" + buildString(idSet) + ")\n"
                + "and encounter.voided=0 and encounter.encounter_datetime between ? and ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        int pid = 0;
        try {
            ps = prepareQuery(sql_text);
            ps.setDate(1, convertToSQLDate(startDate));
            ps.setDate(2, convertToSQLDate(endDate));
            //ps.setInt(3, locationID);
            rs = ps.executeQuery();
            while (rs.next()) {
                pid = rs.getInt("patient_id");
                ptsSet.add(pid);
                //System.out.println("Chest X-ray: "+pid);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }
        return ptsSet;
    }

    public Set<Integer> getAllPatientsWithClinicalEvalFormsFilled(Set<Integer> idSet, Date startDate, Date endDate) {
        Set<Integer> ptsWithClinicalEvalFormSet = new HashSet<Integer>();
        /*String sql_text = "select DISTINCT obs.PATIENT_ID from obs WHERE FORM_ID IN "
                + "(24,18,27,20,47,1,6) AND obs.VISIT_DATE BETWEEN ? AND ?";*/

        String sql_text = "select \n"
                + "DISTINCT encounter.patient_id\n"
                + "from \n"
                + "encounter\n"
                + "where \n"
                + "encounter.encounter_datetime BETWEEN ? AND ?\n"
                + "and\n"
                + "encounter.form_id in(24,18,27,20,47,1,6,56)\n"
                + "and \n"
                + "encounter.voided=0 and encounter.patient_id in (" + buildString(idSet) + ")";

        PreparedStatement ps = prepareQuery(sql_text);
        ResultSet rs = null;
        try {
            ps.setDate(1, convertToSQLDate(startDate));
            ps.setDate(2, convertToSQLDate(endDate));
            rs = ps.executeQuery();
            while (rs.next()) {
                ptsWithClinicalEvalFormSet.add(rs.getInt("patient_id"));
            }
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }
        return ptsWithClinicalEvalFormSet;
    }

    public HashMap<Integer, model.datapump.Obs> getLastOfObsForPatients(Date startDate, Date endDate, int locationID, Set<Integer> idSet, int conceptID) {
        HashMap<Integer, model.datapump.Obs> obsList = new HashMap<Integer, model.datapump.Obs>();
        /*String sql_text = "SELECT * from obs \n"
                + "inner join (select PATIENT_ID,CONCEPT_ID,MAX(VISIT_DATE) LST_DT FROM obs \n"
                + "WHERE CONCEPT_ID=? AND VISIT_DATE BETWEEN ? AND ? AND PATIENT_ID IN (" + buildString(idSet) + ") AND LOCATION_ID=? GROUP BY PATIENT_ID,CONCEPT_ID) sinner on(sinner.PATIENT_ID=obs.PATIENT_ID AND sinner.CONCEPT_ID=obs.CONCEPT_ID AND sinner.LST_DT=obs.VISIT_DATE)";*/
        String sql_text = "select \n"
                + "    `obs`.`obs_id`,\n"
                + "    `obs`.`person_id`,\n"
                + "    `obs`.`obs_datetime`,\n"
                + "    `obs`.`concept_id`,\n"
                + "    `obs`.`value_numeric`,\n"
                + "     obs.value_coded,\n"
                + "     obs.value_text,\n"
                + "     obs.value_datetime,\n"
                + "     obs.value_boolean,\n"
                + "     cast(coalesce(obs.value_coded,`obs`.`value_numeric`,`obs`.`value_datetime`,`obs`.`value_text`) as char charset utf8) AS `variable_value`,\n"
                + "     `obs`.`date_created`,\n"
                + "      encounter.date_changed,\n"
                + "      encounter.date_voided,\n"
                + "      encounter.changed_by,\n"
                + "      obs.voided,\n"
                + "      encounter.voided_by,\n"
                + "     `obs`.`creator` AS `creator`,\n"
                + "     `obs`.`encounter_id` AS `encounter_id`,\n"
                + "     `obs`.`obs_group_id` AS `obs_group_id`,\n"
                + "     `obs`.`uuid`,\n"
                + "     `encounter`.`form_id` AS `form_id`,\n"
                + "     `encounter`.`provider_id` AS `provider_id`,\n"
                + "     `obs`.`location_id` AS `location_id` \n"
                + "     from `obs` \n"
                + "	 inner join `patient` on(`patient`.`patient_id` = `obs`.`person_id`)\n"
                + "     inner join `encounter` on(`encounter`.`encounter_id` = `obs`.`encounter_id`)\n"
                + "     inner join (\n"
                + "	 select \n"
                + "	 obs.person_id,\n"
                + "	 obs.concept_id,\n"
                + "	 MAX(obs.obs_datetime) as date_obs\n"
                + "     from obs\n"
                + "	 where obs.voided=0\n"
                + "	 and \n"
                + "	 obs.concept_id=? \n"
                + "	 and\n"
                + "	 obs.obs_datetime BETWEEN ? AND ?\n"
                + "	 and\n"
                + "	 obs.person_id in (" + buildString(idSet) + ")\n"
                + "	 GROUP BY obs.person_id, obs.concept_id\n"
                + "	 ) \n"
                + "	 sinner on(sinner.person_id=obs.person_id and sinner.concept_id=obs.concept_id and encounter.encounter_datetime=sinner.date_obs)\n"
                + "	 where  encounter.voided=0 and encounter.patient_id in(" + buildString(idSet) + ") order by encounter.patient_id";
        PreparedStatement ps = null;
        ResultSet rs = null;
        model.datapump.Obs obs = null;
        try {
            ps = prepareQuery(sql_text);
            ps.setInt(1, conceptID);
            ps.setDate(2, convertToSQLDate(startDate));
            ps.setDate(3, convertToSQLDate(endDate));
            //ps.setInt(4, locationID);
            rs = ps.executeQuery();
            while (rs.next()) {
                obs = constructObs2(rs);
                obsList.put(obs.getPatientID(), obs);
                //System.out.println("Patient ID Obs: "+obs.getVariableName()+" Value: "+obs.getVariableValue());
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }
        return obsList;
    }

    public void runCareAndSupportExport(Date startDate, Date endDate, model.datapump.Location loc, File file, Set<Integer> idSet) {
        screen.updateStatus("Writing DataCareAndSupport.xml...");
        Set<Integer> ptsHasCandSFormSet, ptsRcvdNutriAccessRp, ptsEverRcvdNutriAccess;
        int locationID = loc.getLocationID();
        ptsHasCandSFormSet = getPatientWithCandSForms(startDate, endDate, locationID, idSet);
        ptsRcvdNutriAccessRp = getPatientsWithNutritionalAssessmentReportingPeriod(startDate, endDate, locationID, idSet);
        ptsEverRcvdNutriAccess = getPatientsWithNutritionalAssessmentEver(startDate, endDate, locationID, idSet);
        String pepfarID = null;
        model.datapump.Demographics demo = null;
        CareAndSupportAssessmentRecord csRec = null;
        String fileName = "DataCareAndSupport.xml";
        zipFileEntryNames.add(fileName);
        int count = 0;
        try {
            mgr.createXMLWriter(fileName);
            mgr.openXMLDocument();
            mgr.writeXMLHeader(COMMON_HEADER, NAMES_SPACE);

            for (int ele : idSet) {
                demo = patientDemoMap.get(ele);
                pepfarID = demo.getPepfarID();
                csRec = nigeriaQualWriter.createCareAndSupportRecord(ptsHasCandSFormSet, ptsRcvdNutriAccessRp, ptsEverRcvdNutriAccess, loc, ele, pepfarID);
                mgr.writeToXML(csRec);
                screen.updateStatus("Writing DataCareAndSupport.xml..." + count);
                count++;
            }
            mgr.endXMLDocument();
            mgr.closeXMLWriter();
        } catch (XMLStreamException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Set<Integer> getPatientsWithNutritionalAssessmentReportingPeriod(Date startDate, Date endDate, int locationID, Set<Integer> idSet) {
        //String sql_text = "select obs.PATIENT_ID from obs where CONCEPT_ID IN (85,571) AND  obs.VISIT_DATE BETWEEN ? AND ? AND LOCATION_ID=? AND PATIENT_ID IN(" + buildString(idSet) + ")";
        String sql_text = "select encounter.patient_id from encounter\n"
                + "inner join obs on(obs.encounter_id=encounter.encounter_id)\n"
                + "where encounter_datetime between ? and ? and encounter.voided=0 and obs.concept_id in(85,571)";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Set<Integer> ptsSet = new HashSet<Integer>();
        try {
            ps = prepareQuery(sql_text);
            ps.setDate(1, convertToSQLDate(startDate));
            ps.setDate(2, convertToSQLDate(endDate));
            //ps.setInt(3, locationID);
            rs = ps.executeQuery();
            while (rs.next()) {
                ptsSet.add(rs.getInt("PATIENT_ID"));
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }
        return ptsSet;
    }

    public Set<Integer> getPatientsWithNutritionalAssessmentEver(Date startDate, Date endDate, int locationID, Set<Integer> idSet) {
        Set<Integer> ptsSet = new HashSet<Integer>();
        //String sql_text = "select obs.PATIENT_ID from obs where CONCEPT_ID IN (85,571) AND  obs.VISIT_DATE <=? AND LOCATION_ID=? AND PATIENT_ID IN(" + buildString(idSet) + ")";
        String sql_text = "select encounter.patient_id from encounter\n"
                + "inner join obs on(obs.encounter_id=encounter.encounter_id)\n"
                + "where \n"
                + "encounter_datetime<=? \n"
                + "and \n"
                + "encounter.voided=0 \n"
                + "and \n"
                + "obs.concept_id in(85,571) and encounter.patient_id in(" + buildString(idSet) + ")";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = prepareQuery(sql_text);
            ps.setDate(1, convertToSQLDate(endDate));
            //ps.setInt(2, locationID);
            rs = ps.executeQuery();
            while (rs.next()) {
                ptsSet.add(rs.getInt("patient_id"));
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }
        return ptsSet;

    }

    public Set<Integer> getPatientWithCandSForms(Date startDate, Date endDate, int locationID, Set<Integer> idSet) {
        Set<Integer> ptsSet = new HashSet<Integer>();
        //String sql_text = "select obs.PATIENT_ID FROM obs WHERE FORM_ID IN (30,51) AND obs.VISIT_DATE BETWEEN ? AND ? AND obs.LOCATION_ID=? AND obs.PATIENT_ID IN(" + buildString(idSet) + ")";
        String sql_text = "select encounter.patient_id from encounter\n"
                + "where encounter_datetime between ? and ? and encounter.form_id in(30,59,70,51) and encounter.patient_id in (" + buildString(idSet) + ") and encounter.voided=0";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = prepareQuery(sql_text);
            ps.setDate(1, convertToSQLDate(startDate));
            ps.setDate(2, convertToSQLDate(endDate));
            //ps.setInt(3, locationID);
            rs = ps.executeQuery();
            while (rs.next()) {
                ptsSet.add(rs.getInt("patient_id"));
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }
        return ptsSet;
    }

    public void runDataHepatitisB(Date startDate, Date endDate, model.datapump.Location loc, File file, Set<Integer> idSet) {
        screen.updateStatus("Writing DataHepatitisB.xml....");
        int locationID = loc.getLocationID();
        ArrayList<model.datapump.Obs> obsList = getHepatitisBTestReportingPeriod(startDate, endDate, locationID, file, idSet);
        Set<Integer> cevalLastVisit = getPatientsWithClinicalEvalFilledLastVisit(startDate, endDate, locationID, idSet);
        DataHepatitisB dbp = null;
        int count = 0;
        String fileName = "DataHepatitisB.xml";
        zipFileEntryNames.add(fileName);
        model.datapump.Demographics demo = null;
        String pepfarID = null;
        try {

            mgr.createXMLWriter(fileName);
            mgr.openXMLDocument();
            mgr.writeXMLHeader(COMMON_HEADER, NAMES_SPACE);
            for (int ele : idSet) {
                demo = patientDemoMap.get(ele);
                if (demo != null) {
                    pepfarID = demo.getPepfarID();
                }
                dbp = nigeriaQualWriter.createDataHepatitisB(obsList, cevalLastVisit, loc, ele, pepfarID);
                mgr.writeToXML(dbp);
                count++;
                screen.updateStatus("Writing DataHepatitisB.xml..." + count);
            }
            mgr.endXMLDocument();
            mgr.closeXMLWriter();
        } catch (XMLStreamException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Set<Integer> getPatientsWithClinicalEvalFilledLastVisit(Date startDate, Date endDate, int locationID, Set<Integer> idSet) {
        Set<Integer> idPtsSet = new HashSet<Integer>();
        /* String sql_text = "select obs.PATIENT_ID from obs\n"
                + "inner join\n"
                + "( \n"
                + "select sinnerinner.PATIENT_ID, MAX(sinnerinner.VISIT_DATE) LST_VST\n"
                + "FROM \n"
                + "(select obs.PATIENT_ID,obs.VISIT_DATE from obs where VISIT_DATE BETWEEN ? AND ? AND obs.LOCATION_ID=? \n"
                + "UNION \n"
                + "select regimen.PATIENT_ID,regimen.VISIT_DATE from regimen WHERE regimen.VISIT_DATE BETWEEN ? AND ?) sinnerinner\n"
                + "GROUP BY sinnerinner.patient_id\n"
                + ") sinner\n"
                + "ON(obs.PATIENT_ID=sinner.PATIENT_ID AND obs.VISIT_DATE=sinner.LST_VST and obs.PMM_FORM LIKE '%Clinical%' and obs.PATIENT_ID IN (" + buildString(idSet) + "))";*/
        String sql_text = "select encounter.patient_id from encounter\n"
                + "inner join\n"
                + "(\n"
                + "select encounter.patient_id,\n"
                + "MAX(encounter.encounter_datetime) as lst_visit_date\n"
                + "from encounter where encounter.voided=0 and encounter.encounter_datetime<=? group by encounter.patient_id\n"
                + ")\n"
                + "sinner on(sinner.patient_id=encounter.patient_id and encounter.encounter_datetime=sinner.lst_visit_date)\n"
                + "where encounter.form_id in(24,18,56,27,20,47) and encounter.patient_id in(" + buildString(idSet) + ") and encounter.voided=0 and encounter.encounter_datetime <=?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = prepareQuery(sql_text);
            //ps.setDate(1, convertToSQLDate(startDate));
            ps.setDate(1, convertToSQLDate(endDate));
            //ps.setInt(3, locationID);
            //ps.setDate(4, convertToSQLDate(startDate));
            ps.setDate(2, convertToSQLDate(endDate));
            rs = ps.executeQuery();
            while (rs.next()) {
                idPtsSet.add(rs.getInt("patient_id"));
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }
        return idPtsSet;
    }

    public ArrayList<model.datapump.Obs> getHepatitisBTestReportingPeriod(Date startDate, Date endDate, int locationID, File file, Set<Integer> idSet) {
        ArrayList<model.datapump.Obs> obsList = new ArrayList<model.datapump.Obs>();
        /*String sql_text = "select * from obs \n"
                + "inner join\n"
                + "(select patient_id,max(visit_date) lst_dt,concept_id from obs where concept_id=1157 and patient_id in(" + buildString(idSet) + ") and visit_date between ? and ? and location_id=? group by patient_id, concept_id)\n"
                + "sinner on(sinner.patient_id=obs.patient_id and sinner.concept_id=obs.concept_id and sinner.lst_dt=obs.visit_date)";*/
        String sql_text = "select\n"
                + "obs.obs_id,\n"
                + "obs.person_id,\n"
                + "obs.obs_datetime,\n"
                + "obs.concept_id,\n"
                + "obs.value_numeric,\n"
                + "obs.value_coded,\n"
                + "obs.value_text,\n"
                + "obs.value_datetime,\n"
                + "obs.value_boolean,\n"
                + "cast(coalesce(obs.value_coded,obs.value_numeric,obs.value_datetime,obs.value_text) as char charset utf8) AS `variable_value`,\n"
                + "obs.date_created,\n"
                + "encounter.date_changed,\n"
                + "encounter.date_voided,\n"
                + "encounter.changed_by,\n"
                + "obs.voided,\n"
                + "encounter.voided_by,\n"
                + "obs.creator as creator,\n"
                + "obs.encounter_id as encounter_id,\n"
                + "obs.obs_group_id as obs_group_id,\n"
                + "obs.uuid,\n"
                + "encounter.form_id as form_id,\n"
                + "encounter.provider_id as provider_id,\n"
                + "obs.location_id as location_id\n"
                + "from obs\n"
                + "inner join patient on(patient.patient_id =obs.person_id)\n"
                + "inner join encounter on(encounter.encounter_id=obs.encounter_id)\n"
                + "inner join (select\n"
                + "obs.person_id,\n"
                + "obs.concept_id,\n"
                + "MAX(obs.obs_datetime) as date_obs\n"
                + "from obs where obs.voided=0 and obs.concept_id=1157 and obs.person_id in (" + buildString(idSet) + ") GROUP BY obs.person_id, obs.concept_id)\n"
                + "sinner on(sinner.person_id=obs.person_id and sinner.concept_id=obs.concept_id and encounter.encounter_datetime=sinner.date_obs)\n"
                + "where obs.concept_id=1157 and obs.voided=0 and encounter.encounter_datetime between ? and ?  order by encounter.patient_id;";

        PreparedStatement ps = null;
        ResultSet rs = null;
        model.datapump.Obs obs = null;
        try {
            ps = prepareQuery(sql_text);
            ps.setDate(1, convertToSQLDate(startDate));
            ps.setDate(2, convertToSQLDate(endDate));
            //ps.setInt(3, locationID);
            rs = ps.executeQuery();
            while (rs.next()) {
                obs = constructObs2(rs);
                obsList.add(obs);
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }
        return obsList;
    }

    public void runDataPatientMonitoringDuringReviewPeriod(Date startDate, Date endDate, model.datapump.Location loc, File file, Set<Integer> idSet) {
        screen.updateStatus("Writing DataPatientMonitoringReviewPeriod.xml...");
        Integer[] conceptArr = {88, 1153, 85, 860, 313, 329, 309, 7777798};
        int locationID = loc.getLocationID();
        Set<Integer> conceptSet = new HashSet<Integer>(Arrays.asList(conceptArr));
        ArrayList<model.datapump.Obs> obsList = getAllObsForConcepts(conceptSet, idSet, startDate, endDate);
        ArrayList<DataPatientMonitoringReviewPeriod> pmList = nigeriaQualWriter.createDataPatientMonitoringReviewPeriod(idSet, patientDemoMap, obsList, loc);
        String fileName = "DataPatientMonitoringReviewPeriod.xml";
        zipFileEntryNames.add(fileName);
        int count = 0;
        try {
            mgr.createXMLWriter(fileName);
            mgr.openXMLDocument();
            mgr.writeXMLHeader(COMMON_HEADER, NAMES_SPACE);
            for (DataPatientMonitoringReviewPeriod pm : pmList) {
                mgr.writeToXML(pm);
                count++;
                screen.updateStatus("Writing DataPatientMonitoringReviewPeriod.xml..." + count);
            }
            mgr.endXMLDocument();
            mgr.closeXMLWriter();
        } catch (XMLStreamException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<model.datapump.Obs> getAllObsForConcepts(Set<Integer> conceptSet, Set<Integer> idSet, Date startDate, Date endDate) {
        //String sql_text = "select * from obs where PATIENT_ID IN(" + buildString(idSet) + ") and CONCEPT_ID IN (" + buildString(conceptSet) + ") AND obs.VISIT_DATE BETWEEN ? AND ?";
        String sql_text = "select \n"
                + "    `obs`.`obs_id`,\n"
                + "    `obs`.`person_id`,\n"
                + "    `obs`.`obs_datetime`,\n"
                + "    `obs`.`concept_id`,\n"
                + "    `obs`.`value_numeric`,\n"
                + "     obs.value_coded,\n"
                + "     obs.value_text,\n"
                + "     obs.value_datetime,\n"
                + "     obs.value_boolean,\n"
                + "     cast(coalesce(obs.value_coded,`obs`.`value_numeric`,`obs`.`value_datetime`,`obs`.`value_text`) as char charset utf8) AS `variable_value`,\n"
                + "     `obs`.`date_created`,\n"
                + "      encounter.date_changed,\n"
                + "      encounter.date_voided,\n"
                + "      encounter.changed_by,\n"
                + "      obs.voided,\n"
                + "      encounter.voided_by,\n"
                + "     `obs`.`creator` AS `creator`,\n"
                + "     `obs`.`encounter_id` AS `encounter_id`,\n"
                + "     `obs`.`obs_group_id` AS `obs_group_id`,\n"
                + "     `obs`.`uuid`,\n"
                + "     `encounter`.`form_id` AS `form_id`,\n"
                + "     `encounter`.`provider_id` AS `provider_id`,\n"
                + "     `obs`.`location_id` AS `location_id` \n"
                + "     from `obs` \n"
                + "	inner join `patient` on(`patient`.`patient_id` = `obs`.`person_id`)\n"
                + "     inner join `encounter` on(`encounter`.`encounter_id` = `obs`.`encounter_id`)\n"
                + "	where obs.concept_id in(" + buildString(conceptSet) + ") and obs.voided=0 and encounter.patient_id in (" + buildString(idSet) + ") and encounter.encounter_datetime BETWEEN ? and ? order by encounter.patient_id";
        PreparedStatement ps = null;
        ArrayList<model.datapump.Obs> obsList = new ArrayList<model.datapump.Obs>();
        model.datapump.Obs obs = null;
        ResultSet rs = null;
        ps = prepareQuery(sql_text);
        try {
            ps.setDate(1, convertToSQLDate(startDate));
            ps.setDate(2, convertToSQLDate(endDate));
            rs = ps.executeQuery();
            while (rs.next()) {
                obs = constructObs2(rs);
                obsList.add(obs);
            }

        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }
        return obsList;
    }

    public void runDataPatientStatusReviewPeriod(Date startDate, Date endDate, model.datapump.Location loc, File file, Set<Integer> idSet) {
        screen.updateStatus("Writing DataPatientStatusReviewPeriod.xml...");
        Integer[] formIDArr = {29, 71};
        Set<Integer> formIDSet = new HashSet<Integer>(Arrays.asList(formIDArr));
        ArrayList<model.datapump.Obs> obsList = getAllObsForForm(formIDSet, idSet, startDate, endDate);
        String fileName = "DataPatientStatusReviewPeriod.xml";
        ArrayList<DataPatientStatusReviewPeriod> pstatusList = nigeriaQualWriter.createDataPatientStatusReviewPeriod(patientDemoMap, idSet, obsList, loc);
        zipFileEntryNames.add(fileName);
        int count = 0;
        try {
            mgr.createXMLWriter(fileName);
            mgr.openXMLDocument();
            mgr.writeXMLHeader(COMMON_HEADER, NAMES_SPACE);
            for (DataPatientStatusReviewPeriod pm : pstatusList) {
                mgr.writeToXML(pm);
                count++;
                screen.updateStatus("Writing DataPatientStatusReviewPeriod.xml..." + count);
            }
            mgr.endXMLDocument();
            mgr.closeXMLWriter();
        } catch (XMLStreamException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<model.datapump.Obs> getAllObsForForm(Set<Integer> formIDSet, Set<Integer> idSet, Date startDate, Date endDate) {
        ArrayList<model.datapump.Obs> obsList = new ArrayList<model.datapump.Obs>();
        //String sql_text = "select * from obs where FORM_ID IN (" + buildString(formIDSet) + ") AND PATIENT_ID IN(" + buildString(idSet) + ") AND VISIT_DATE BETWEEN ? AND ?";
        String sql_text = "select \n"
                + "    `obs`.`obs_id`,\n"
                + "    `obs`.`person_id`,\n"
                + "    `obs`.`obs_datetime`,\n"
                + "    `obs`.`concept_id`,\n"
                + "    `obs`.`value_numeric`,\n"
                + "     obs.value_coded,\n"
                + "     obs.value_text,\n"
                + "     obs.value_datetime,\n"
                + "     obs.value_boolean,\n"
                + "     cast(coalesce(obs.value_coded,`obs`.`value_numeric`,`obs`.`value_datetime`,`obs`.`value_text`) as char charset utf8) AS `variable_value`,\n"
                + "     `obs`.`date_created`,\n"
                + "      encounter.date_changed,\n"
                + "      encounter.date_voided,\n"
                + "      encounter.changed_by,\n"
                + "      obs.voided,\n"
                + "      encounter.voided_by,\n"
                + "     `obs`.`creator` AS `creator`,\n"
                + "     `obs`.`encounter_id` AS `encounter_id`,\n"
                + "     `obs`.`obs_group_id` AS `obs_group_id`,\n"
                + "     `obs`.`uuid`,\n"
                + "     `encounter`.`form_id` AS `form_id`,\n"
                + "     `encounter`.`provider_id` AS `provider_id`,\n"
                + "     `obs`.`location_id` AS `location_id` \n"
                + "     from `obs` \n"
                + "	inner join `patient` on(`patient`.`patient_id` = `obs`.`person_id`)\n"
                + "     inner join `encounter` on(`encounter`.`encounter_id` = `obs`.`encounter_id`)\n"
                + "	where encounter.form_id in(" + buildString(formIDSet) + ") and obs.voided=0 and encounter.patient_id in (" + buildString(idSet) + ") and encounter.encounter_datetime BETWEEN ? and ? order by encounter.patient_id";
        PreparedStatement ps = null;
        ResultSet rs = null;
        model.datapump.Obs obs = null;
        try {
            ps = prepareQuery(sql_text);
            ps.setDate(1, convertToSQLDate(startDate));
            ps.setDate(2, convertToSQLDate(endDate));
            rs = ps.executeQuery();
            while (rs.next()) {
                obs = constructObs2(rs);
                obsList.add(obs);
            }

        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }
        return obsList;
    }

    public HashMap<Integer, model.datapump.Obs> getAdherenceLast3Months(Date startDate, Date endDate, int locationID, Set<Integer> idSet) {
        HashMap<Integer, model.datapump.Obs> adhMap = new HashMap<Integer, model.datapump.Obs>();
        /* String sql_text="select DISTINCT obs.* from obs \n" +
"inner join (select patient_id, max(visit_date) as max_date from obs where obs.voided=0 and ((CONCEPT_ID=240 and VALUE_CODED=80) OR (FORM_ID IN(16,24,18,27,47,25,1))) and obs.visit_date between ? and ? and obs.location_id=? GROUP BY PATIENT_ID) as sinner \n" +
"on(obs.patient_id=sinner.patient_id and obs.visit_date=sinner.max_date)\n" +
"where obs.voided=0 and ((obs.concept_id=240 and obs.value_coded=80) or (form_id=16)) and obs.visit_date between ? and ? and obs.location_id=? group by obs.patient_id";*/
 /*String sql_text = "select DISTINCT obs.* from obs\n"
                + "inner join (select patient_id, max(visit_date) as max_date from obs where obs.voided=0 and ((CONCEPT_ID=240 and VALUE_CODED=80) OR (FORM_ID IN(16,24,18,27,47,25,1,6))) and TIMESTAMPDIFF(MONTH,obs.visit_date,?)<=3 and obs.location_id=? GROUP BY PATIENT_ID) as sinner\n"
                + "on(obs.patient_id=sinner.patient_id and obs.visit_date=sinner.max_date)\n"
                + "where obs.voided=0 and ((obs.concept_id=240 and obs.value_coded=80) or (FORM_ID IN(16,24,18,27,47,25,1,6))) and TIMESTAMPDIFF(MONTH,obs.visit_date,?)<=3 and obs.location_id=? group by obs.patient_id;";*/
        String sql_text = "select\n"
                + "obs.obs_id,\n"
                + "obs.person_id,\n"
                + "obs.obs_datetime,\n"
                + "obs.concept_id,\n"
                + "obs.value_numeric,\n"
                + "obs.value_coded,\n"
                + "obs.value_text,\n"
                + "obs.value_datetime,\n"
                + "obs.value_boolean,\n"
                + "cast(coalesce(obs.value_coded,obs.value_numeric,obs.value_datetime,obs.value_text) as char charset utf8) AS `variable_value`,\n"
                + "obs.date_created,\n"
                + "encounter.date_changed,\n"
                + "encounter.date_voided,\n"
                + "encounter.changed_by,\n"
                + "obs.voided,\n"
                + "encounter.voided_by,\n"
                + "obs.creator AS creator,\n"
                + "obs.encounter_id AS encounter_id,\n"
                + "obs.obs_group_id AS obs_group_id,\n"
                + "obs.uuid,\n"
                + "encounter.form_id AS form_id,\n"
                + "encounter.provider_id AS provider_id,\n"
                + "obs.location_id AS location_id\n"
                + "from obs\n"
                + "inner join patient on(patient.patient_id =obs.person_id)\n"
                + "inner join encounter on(encounter.encounter_id=obs.encounter_id)\n"
                + "inner join (select\n"
                + "obs.person_id,\n"
                + "obs.concept_id,\n"
                + "MAX(obs.obs_datetime) as date_obs\n"
                + "from obs where obs.voided=0 and obs.person_id in (" + buildString(idSet) + ") and obs.concept_id in (7777874,240) and obs.value_coded in (80,1398,1485,1397) and TIMESTAMPDIFF(MONTH, obs.obs_datetime,?)<=3 GROUP BY obs.person_id, obs.concept_id)\n"
                + "sinner on(sinner.person_id=obs.person_id and sinner.concept_id=obs.concept_id and obs.obs_datetime=sinner.date_obs)\n"
                + "where obs.person_id in (" + buildString(idSet) + ") and obs.concept_id in (7777874,240) and obs.value_coded in (80,1398,1485,1397) and obs.voided=0 and TIMESTAMPDIFF(MONTH,obs.obs_datetime,?)<=3 order by encounter.patient_id;";
        PreparedStatement ps = null;
        ResultSet rs = null;
        model.datapump.Obs obs = null;
        try {
            ps = prepareQuery(sql_text);
            //ps.setDate(1, convertToSQLDate(startDate));
            ps.setDate(1, convertToSQLDate(endDate));
            //ps.setInt(2, locationID);
            //ps.setDate(4, convertToSQLDate(startDate));
            ps.setDate(2, convertToSQLDate(endDate));
            //ps.setInt(4, locationID);
            rs = ps.executeQuery();
            while (rs.next()) {
                obs = constructObs2(rs);
                adhMap.put(obs.getPatientID(), obs);
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        }
        return adhMap;
    }

    public void runDataARTAdherenceExport(Date startDate, Date endDate, model.datapump.Location loc, File file, Set<Integer> idSet) {
        screen.updateStatus("Writing Data ART_Adherence.xml...Please wait");
        long startTime = System.currentTimeMillis();
        int location_id = loc.getLocationID();
        HashMap<Integer, model.datapump.Obs> maxCD4Map = getAllMaxCD4Count(idSet);
        HashMap<Integer, model.datapump.Obs> adherenceMap = getAdherenceLast3Months(startDate, endDate, location_id, idSet);
        ArrayList<DataARTAdherence> dataARTAdhList = nigeriaQualWriter.createDataARTAdherenceList(patientDemoMap, maxCD4Map, adherenceMap, loc, idSet);
        int count = 0;
        String fileName = "Data ART_Adherence.xml";
        zipFileEntryNames.add(fileName);
        try {
            mgr.createXMLWriter(fileName);
            mgr.openXMLDocument();
            mgr.writeXMLHeader(COMMON_HEADER, NAMES_SPACE);
            for (DataARTAdherence ele : dataARTAdhList) {
                mgr.writeToXML(ele);
                count++;
                screen.updateStatus("Writing Data ART_Adherence.xml...Please wait " + count);
            }
            mgr.endXMLDocument();
            mgr.closeXMLWriter();
            long duration = calculateDuration(startTime);
            screen.updateStatus("Writing Data ART_Adherence.xml Completed " + duration + " secs");

        } catch (XMLStreamException ex) {
            ex.printStackTrace();
            screen.updateStatus(ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
            screen.updateStatus(ex.getMessage());
        }

    }

    public HashMap<Integer, model.datapump.Obs> getAllMaxCD4Count(Set<Integer> idSet) {
        HashMap<Integer, model.datapump.Obs> obsMapList = new HashMap<Integer, model.datapump.Obs>();
        /*String sql_text = "SELECT \n"
                + "obs.*\n"
                + "FROM obs inner join \n"
                + "(select \n"
                + "    obs.PATIENT_ID,\n"
                + "    obs.CONCEPT_ID,\n"
                + "    MAX(value_numeric) AS max_cd4\n"
                + "    FROM obs WHERE VOIDED=0 AND CONCEPT_ID =88 GROUP BY PATIENT_ID, CONCEPT_ID ) sinner\n"
                + "    on(sinner.PATIENT_ID=obs.PATIENT_ID AND obs.CONCEPT_ID=sinner.CONCEPT_ID AND obs.value_numeric=sinner.max_cd4)\n"
                + "    where obs.concept_id =88 and obs.voided=0 order by obs.patient_id";*/
        String sql_text = "select \n"
                + "    `obs`.`obs_id`,\n"
                + "    `obs`.`person_id`,\n"
                + "    `obs`.`obs_datetime`,\n"
                + "    `obs`.`concept_id`,\n"
                + "    `obs`.`value_numeric`,\n"
                + "     obs.value_coded,\n"
                + "     obs.value_text,\n"
                + "     obs.value_datetime,\n"
                + "     obs.value_boolean,\n"
                + "     cast(coalesce(obs.value_coded,`obs`.`value_numeric`,`obs`.`value_datetime`,`obs`.`value_text`) as char charset utf8) AS `variable_value`,\n"
                + "     `obs`.`date_created`,\n"
                + "      encounter.date_changed,\n"
                + "      encounter.date_voided,\n"
                + "      encounter.changed_by,\n"
                + "      obs.voided,\n"
                + "      encounter.voided_by,\n"
                + "     `obs`.`creator` AS `creator`,\n"
                + "     `obs`.`encounter_id` AS `encounter_id`,\n"
                + "     `obs`.`obs_group_id` AS `obs_group_id`,\n"
                + "     `obs`.`uuid`,\n"
                + "     `encounter`.`form_id` AS `form_id`,\n"
                + "     `encounter`.`provider_id` AS `provider_id`,\n"
                + "     `obs`.`location_id` AS `location_id` \n"
                + "     from `obs` \n"
                + "	inner join `patient` on(`patient`.`patient_id` = `obs`.`person_id`)\n"
                + "     inner join `encounter` on(`encounter`.`encounter_id` = `obs`.`encounter_id`)\n"
                + "     inner join (select \n"
                + "	 obs.person_id,\n"
                + "	 obs.concept_id,\n"
                + "      obs.obs_datetime,\n"
                + "	 MAX(obs.value_numeric) as max_cd4\n"
                + "      from obs where obs.voided=0 and obs.concept_id=88 and obs.person_id in (" + buildString(idSet) + ") GROUP BY obs.person_id, obs.concept_id) \n"
                + "	 sinner on(sinner.person_id=obs.person_id and sinner.concept_id=obs.concept_id and sinner.obs_datetime=encounter.encounter_datetime)\n"
                + "	 where  encounter.voided=0 and encounter.patient_id in (" + buildString(idSet) + ") and obs.concept_id=88 order by encounter.patient_id";
        PreparedStatement ps = null;
        ResultSet rs = null;
        model.datapump.Obs obs = null;
        try {
            ps = prepareQuery(sql_text);
            rs = ps.executeQuery();
            while (rs.next()) {
                obs = constructObs2(rs);
                obsMapList.put(obs.getPatientID(), obs);
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            screen.updateStatus(ex.getMessage());
        } finally {
            cleanUp(rs, ps);
        }
        return obsMapList;

    }

    public void runViralLoadTestingExport(Date startDate, Date endDate, model.datapump.Location loc, File file, Set<Integer> idSet) {
        screen.updateStatus("Writing DataViralLoadTesting.xml...");
        HashMap<Integer, model.datapump.Obs> vlMap = getAllViralLoadTestingReportingPeriod(startDate, endDate, loc, idSet);
        DataViralLoadTestingReviewPeriod dv = null;
        String fileName = "DataViralLoadTesting.xml";
        int count = 0;
        zipFileEntryNames.add(fileName);

        try {
            mgr.createXMLWriter(fileName);
            mgr.openXMLDocument();
            mgr.writeXMLHeader(COMMON_HEADER, NAMES_SPACE);
            for (int ele : idSet) {
                dv = nigeriaQualWriter.createDataViralLoadTesting(vlMap, ele, patientDemoMap.get(ele).getPepfarID(), loc);
                mgr.writeToXML(dv);
                screen.updateStatus("Writing DataViralLoadTesting.xml..." + count);
                count++;
            }
            mgr.endXMLDocument();
            mgr.closeXMLWriter();
        } catch (XMLStreamException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public HashMap<Integer, model.datapump.Obs> getAllViralLoadTestingReportingPeriod(Date startDate, Date endDate, model.datapump.Location loc, Set<Integer> idSet) {
        /*String sql_text = "SELECT * from obs \n"
                + "inner join (select PATIENT_ID,CONCEPT_ID,MAX(VISIT_DATE) LST_DT FROM obs \n"
                + "WHERE CONCEPT_ID=315 AND VISIT_DATE <=? AND LOCATION_ID=? AND PATIENT_ID IN(" + buildString(idSet) + ")GROUP BY PATIENT_ID,CONCEPT_ID) sinner on(sinner.PATIENT_ID=obs.PATIENT_ID AND sinner.CONCEPT_ID=obs.CONCEPT_ID AND sinner.LST_DT=obs.VISIT_DATE)";*/
        String sql_text = "select\n"
                + "obs.obs_id,\n"
                + "obs.person_id,\n"
                + "obs.obs_datetime,\n"
                + "obs.concept_id,\n"
                + "obs.value_numeric,\n"
                + "obs.value_coded,\n"
                + "obs.value_text,\n"
                + "obs.value_datetime,\n"
                + "obs.value_boolean,\n"
                + "cast(coalesce(obs.value_coded,obs.value_numeric,obs.value_datetime,obs.value_text) as char charset utf8) AS `variable_value`,\n"
                + "obs.date_created,\n"
                + "encounter.date_changed,\n"
                + "encounter.date_voided,\n"
                + "encounter.changed_by,\n"
                + "obs.voided,\n"
                + "encounter.voided_by,\n"
                + "obs.creator as creator,\n"
                + "obs.encounter_id as encounter_id,\n"
                + "obs.obs_group_id as obs_group_id,\n"
                + "obs.uuid,\n"
                + "encounter.form_id as form_id,\n"
                + "encounter.provider_id as provider_id,\n"
                + "obs.location_id as location_id\n"
                + "from obs\n"
                + "inner join patient on(patient.patient_id =obs.person_id)\n"
                + "inner join encounter on(encounter.encounter_id=obs.encounter_id)\n"
                + "inner join (select\n"
                + "obs.person_id,\n"
                + "obs.concept_id,\n"
                + "MAX(obs.obs_datetime) as date_obs\n"
                + "from obs where obs.voided=0 and obs.person_id in(" + buildString(idSet) + ") and obs.concept_id=315 GROUP BY obs.person_id, obs.concept_id)\n"
                + "sinner on(sinner.person_id=obs.person_id and sinner.concept_id=obs.concept_id and encounter.encounter_datetime=sinner.date_obs)\n"
                + "where obs.concept_id=315 and obs.voided=0 and encounter.encounter_datetime<=? and encounter.patient_id in (" + buildString(idSet) + ") order by encounter.patient_id;";
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap<Integer, model.datapump.Obs> obsMap = new HashMap<Integer, model.datapump.Obs>();
        try {
            ps = prepareQuery(sql_text);
            ps.setDate(1, convertToSQLDate(endDate));
            //ps.setInt(2, locationID);
            rs = ps.executeQuery();
            model.datapump.Obs obs = null;
            while (rs.next()) {
                obs = constructObs2(rs);
                obsMap.put(obs.getPatientID(), obs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }
        return obsMap;
    }

    public void runDataARTRecordExport(Date startDate, Date endDate, model.datapump.Location loc, File file, Set<Integer> idSet) {
        screen.updateStatus("Writing Data ARTRecord.xml...Please wait");
        long startTime = System.currentTimeMillis();
        int count = 0;
        // Set<Integer> idSet=patientDemoMap.keySet();
        ArrayList<DataARTRecord> dataARTRecordsList = new ArrayList<DataARTRecord>();
        dataARTRecordsList = nigeriaQualWriter.createDataARTRecords(patientDemoList, artStartDateDictionary, firstRegimenDictionary.keySet(), loc, firstRegimenDictionary, idSet);//DataARTRecord
        String fileName = "Data ARTRecord.xml";
        try {
            mgr.createXMLWriter(fileName);
            mgr.openXMLDocument();
            mgr.writeXMLHeader(COMMON_HEADER, NAMES_SPACE);
            zipFileEntryNames.add(fileName);
            for (DataARTRecord ele : dataARTRecordsList) {
                mgr.writeToXML(ele);
                count++;
                screen.updateStatus("Writing Data ARTRecord.xml..." + count);
            }
            mgr.endXMLDocument();
            mgr.closeXMLWriter();
        } catch (XMLStreamException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        }
        long duration = calculateDuration(startTime);
        screen.updateStatus("Data ARTRecord.xml completed in " + duration + " sec...Wait");

    }

    public void runDataBaselineParameterExport(Date startDate, Date endDate, model.datapump.Location loc, File file, Set<Integer> idSet) {
        screen.updateStatus("Writing DataBaselineParameters.xml file...Please wait");
        long startTime = System.currentTimeMillis();
        ArrayList<DataBaselineParameters> dataBaselineParaList = new ArrayList<DataBaselineParameters>();
        ArrayList<model.datapump.Obs> obsList = null;
        DataBaselineParameters dataBaselinePara = null;
        Set<Integer> ptsSet = patientDemoMap.keySet();
        obsList = getAllBaselines(idSet);
        int count = 0;
        /* for (int ele : ptsSet) {
            if (ptsSet.contains(ele)) {
                dataBaselinePara = nigeriaQualWriter.createDataBaselineParameters(ele, obsList, loc);
                dataBaselineParaList.add(dataBaselinePara);
            }
        }*/
        for (int ele : idSet) {
            //if (idSet.contains(ele)) {
            dataBaselinePara = nigeriaQualWriter.createDataBaselineParameters(ele, obsList, loc);
            dataBaselineParaList.add(dataBaselinePara);
            // }
        }
        String fileName = "Data BaselineParameters.xml";
        try {
            mgr.createXMLWriter(fileName);
            mgr.openXMLDocument();
            mgr.writeXMLHeader(COMMON_HEADER, NAMES_SPACE);
            zipFileEntryNames.add(fileName);
            for (DataBaselineParameters dbp : dataBaselineParaList) {
                mgr.writeToXML(dbp);
                count++;
                screen.updateStatus("Writing DataBaselineParameters.xml file... " + count + " records written");
            }
            mgr.endXMLDocument();
            mgr.closeXMLWriter();
            long duration = calculateDuration(startTime);
            screen.updateStatus("DataBaselineParameters.xml file completed in " + duration + " secs ...Wait");
        } catch (IOException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } catch (XMLStreamException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public ArrayList<model.datapump.Obs> getAllBaselines(Set<Integer> idSet) {
        ArrayList<model.datapump.Obs> obsList = new ArrayList<model.datapump.Obs>();
        long startTime = System.currentTimeMillis();
        screen.updateStatus("Exporting baselines....Please wait");
        /*String sql_text = "SELECT \n"
                + "obs.*\n"
                + "FROM obs inner join \n"
                + "(select \n"
                + "    obs.PATIENT_ID,\n"
                + "    obs.CONCEPT_ID,\n"
                + "    MIN(obs.VISIT_DATE) AS DATE_OBS\n"
                + "    FROM obs WHERE VOIDED=0 AND CONCEPT_ID in (88,85,860) GROUP BY PATIENT_ID, CONCEPT_ID ) sinner\n"
                + "    on(sinner.PATIENT_ID=obs.PATIENT_ID AND obs.CONCEPT_ID=sinner.CONCEPT_ID AND obs.VISIT_DATE=sinner.DATE_OBS)\n"
                + "    where obs.concept_id in(88,85,860) and obs.voided=0 order by obs.patient_id";*/
        String sql_text = "select \n"
                + "    `obs`.`obs_id`,\n"
                + "    `obs`.`person_id`,\n"
                + "    `obs`.`obs_datetime`,\n"
                + "    `obs`.`concept_id`,\n"
                + "    `obs`.`value_numeric`,\n"
                + "     obs.value_coded,\n"
                + "     obs.value_text,\n"
                + "     obs.value_datetime,\n"
                + "     obs.value_boolean,\n"
                + "     cast(coalesce(obs.value_coded,`obs`.`value_numeric`,`obs`.`value_datetime`,`obs`.`value_text`) as char charset utf8) AS `variable_value`,\n"
                + "     `obs`.`date_created`,\n"
                + "      encounter.date_changed,\n"
                + "      encounter.date_voided,\n"
                + "      encounter.changed_by,\n"
                + "      obs.voided,\n"
                + "      encounter.voided_by,\n"
                + "     `obs`.`creator` AS `creator`,\n"
                + "     `obs`.`encounter_id` AS `encounter_id`,\n"
                + "     `obs`.`obs_group_id` AS `obs_group_id`,\n"
                + "     `obs`.`uuid`,\n"
                + "     `encounter`.`form_id` AS `form_id`,\n"
                + "     `encounter`.`provider_id` AS `provider_id`,\n"
                + "     `obs`.`location_id` AS `location_id` \n"
                + "     from `obs` \n"
                + "	inner join `patient` on(`patient`.`patient_id` = `obs`.`person_id`)\n"
                + "     inner join `encounter` on(`encounter`.`encounter_id` = `obs`.`encounter_id`)\n"
                + "     inner join (select \n"
                + "	 obs.person_id,\n"
                + "	 obs.concept_id,\n"
                + "	 MIN(obs.obs_datetime) as date_obs\n"
                + "     from obs where obs.voided=0 and obs.concept_id in(88,85,7777798,860) and obs.person_id in(" + buildString(idSet) + ") GROUP BY obs.person_id, obs.concept_id) \n"
                + "	 sinner on(sinner.person_id=obs.person_id and sinner.concept_id=obs.concept_id and encounter.encounter_datetime=sinner.date_obs)\n"
                + "	 where obs.concept_id in(88,85,7777798,860) and encounter.voided=0 and encounter.patient_id in (" + buildString(idSet) + ") order by encounter.patient_id";
        Statement stmt = null;
        ResultSet rs = null;
        model.datapump.Obs obs = null;
        long duration = 0l;
        try {
            stmt = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            rs = stmt.executeQuery(sql_text);
            while (rs.next()) {
                obs = constructObs2(rs);
                obsList.add(obs);
            }
            rs.close();
            stmt.close();
            duration = calculateDuration(startTime);
        } catch (SQLException ex) {
            ex.printStackTrace();
            screen.updateStatus(ex.getMessage());
        } finally {
            cleanUp(rs, stmt);
        }
        screen.updateStatus("Baseline export completed in " + duration + "secs");
        return obsList;
    }

    public Set<Integer> getSamplePatients(Date startDate, Date endDate, model.datapump.Location loc) {
        Set<Integer> idSet = new HashSet<Integer>();
        Set<Integer> sampleSet = new HashSet<Integer>();
        /*String sql_text="select patient.person_source_pk as patient_id from patient where timestampdiff(YEAR,dob,curdate())>=15 and pepfar_id is not null\n" +
"and dob is not null and coalesce(adult_enrollment_dt,pead_enrollment_dt) is not null\n" +
"and (person_source_pk IN(\n" +
"select obs.PATIENT_ID from obs where TIMESTAMPDIFF(MONTH,VISIT_DATE,?)<=6 and obs.VOIDED=0 and obs.location_id=?)\n" +
"OR\n" +
"person_source_pk IN(select patient_id from regimen where regimen is not null and regimen_code<>-1 \n" +
"and TIMESTAMPDIFF(YEAR, VISIT_DATE,?)<=3)); ";*/
        String sql_text = "select  DISTINCT patient.patient_id from patient\n"
                + "                 left join person on(patient.patient_id=person.person_id and patient.voided=0)\n"
                + "                 left join patient_program on(patient.patient_id=patient_program.patient_id and patient_program.program_id=3 and patient_program.voided=0)\n"
                + "                 left join encounter on(encounter.patient_id=patient.patient_id and encounter.voided=0)\n"
                + "                 where \n"
                + "                 person.birthdate is not null\n"
                + "                 and\n"
                + "                 patient_program.date_enrolled is not null\n"
                + "                 and \n"
                + "                 patient.voided=0\n"
                + "                 and\n"
                + "                 patient_program.program_id=3\n"
                + "                 and\n"
                + "                 patient_program.voided=0\n"
                + "                 and\n"
                + "                 TIMESTAMPDIFF(YEAR,person.birthdate,curdate())>=15\n"
                + "                 and\n"
                + "                 TIMESTAMPDIFF(MONTH,encounter.encounter_datetime,?)<=6 AND encounter.patient_id is not null and encounter.voided=0";
        PreparedStatement ps = null;
        ResultSet rs = null;
        //Statement stmt=null;
        try {
            /*stmt = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            rs = stmt.executeQuery(sql_text);*/
            ps = prepareQuery(sql_text);
            ps.setDate(1, convertToSQLDate(startDate));
            //ps.setInt(2, locationID);
            //ps.setDate(3, convertToSQLDate(startDate));
            rs = ps.executeQuery();
            while (rs.next()) {
                idSet.add(rs.getInt("patient_id"));
            }
            rs.close();
            ps.close();
            int sampleSize = getSampleSize(idSet.size());
            sampleSet = getRandomPatients(idSet, sampleSize);
            /*Iterator<Integer> it = idSet.iterator();
            int count = 0;
            while (it.hasNext() && count < sampleSize) {
                int pid = getRandom(idSet);
                if (!sampleSet.contains(pid)) {
                    sampleSet.add(pid);
                    count++;
                    screen.updateStatus("Random patients "+count);
                }
            }
            rs.close();
            ps.close();*/

        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }
        return sampleSet;
    }

    public Set<Integer> getRandomPatients(Set<Integer> idSet, int n) {
        Set<Integer> sampleSet = new HashSet<Integer>();
        ArrayList<Integer> sampleList = new ArrayList<Integer>();
        sampleList.addAll(idSet);
        int size = idSet.size();
        if (size <= n) {
            n = size;
        }
        Collections.shuffle(sampleList);
        int sele = 0;
        for (int i = 0; i < n; i++) {
            sele = sampleList.get(i);
            sampleSet.add(sele);
        }
        return sampleSet;
    }

    public int getSampleSize(int patientLoad) {
        int size = 0;
        if (patientLoad <= 20) {
            size = 20;
        } else if (patientLoad >= 5000) {
            size = 150;
        } else if (patientLoad >= 1000 && patientLoad <= 4999) {
            size = 146;
        } else if (patientLoad >= 750 && patientLoad <= 999) {
            size = 131;
        } else if (patientLoad >= 500 && patientLoad <= 749) {
            size = 127;
        } else if (patientLoad >= 450 && patientLoad <= 499) {
            size = 116;
        } else if (patientLoad >= 400 && patientLoad <= 449) {
            size = 113;
        } else if (patientLoad >= 300 && patientLoad <= 349) {
            size = 106;
        } else if (patientLoad >= 250 && patientLoad <= 299) {
            size = 101;
        } else if (patientLoad >= 200 && patientLoad <= 249) {
            size = 94;
        } else if (patientLoad >= 180 && patientLoad <= 199) {
            size = 86;
        } else if (patientLoad >= 160 && patientLoad <= 179) {
            size = 82;
        } else if (patientLoad >= 140 && patientLoad <= 159) {
            size = 78;
        } else if (patientLoad >= 120 && patientLoad <= 139) {
            size = 73;
        } else if (patientLoad >= 101 && patientLoad <= 119) {
            size = 67;
        } else if (patientLoad <= 20) {
            size = 20;
        } else {
            size = (int) Math.round(patientLoad * 0.7);
        }
        return size;
    }

    public static int getRandom(Set<Integer> idSet) {
        ArrayList<Integer> idList = new ArrayList<Integer>();
        idList.addAll(idSet);
        int rnd = new Random().nextInt(idList.size());
        return idList.get(rnd);
    }

    public Set<Integer> getAllPatientsInDBWithChange(Date startDate, Date endDate) {
        Set<Integer> ptsWithChange = new HashSet<Integer>();
        int count = 0;
        /* String sql_text = "select patient.person_source_pk from patient where (patient.date_created between ? and ? OR patient.date_changed between ? and ?) and pepfar_id is not null and dob is not null and first_name is not null\n"
                + "UNION\n"
                + "select obs.PATIENT_ID from obs where (obs.VISIT_DATE between ? and ? OR obs.DATE_CREATED BETWEEN ? AND ? OR obs.DATE_CHANGED BETWEEN ? AND ?) AND obs.VOIDED=0\n";*/
 /*"UNION\n" +
"select regimen.PATIENT_ID from regimen where (regimen.VISIT_DATE BETWEEN ? AND ? OR regimen.DATE_ENTERED BETWEEN ? AND ?)";*/
        String sql_text = "select \n"
                + "DISTINCT patient.patient_id\n"
                + "from patient \n"
                + "LEFT JOIN encounter on(encounter.patient_id=patient.patient_id and encounter.voided=0 and patient.voided=0)\n"
                + "LEFT JOIN person on(person.person_id=patient.patient_id and person.voided=0)\n"
                + "LEFT JOIN orders on(orders.patient_id=patient.patient_id and patient.voided=0)\n"
                + "LEFT JOIN patient_program on(patient_program.patient_id=patient.patient_id and patient_program.voided=0)\n"
                + "LEFT JOIN patient_identifier on(patient_identifier.patient_id=patient.patient_id and patient_identifier.identifier_type=3 and patient_identifier.voided=0)\n"
                + "LEFT JOIN person_address on(person_address.person_id=patient.patient_id and person_address.voided=0)\n"
                + "where \n"
                + "(encounter.encounter_datetime BETWEEN ? AND ?\n"
                + "OR\n"
                + "encounter.date_created BETWEEN ? AND ?\n"
                + "OR \n"
                + "encounter.date_changed BETWEEN ? AND ?\n"
                + "OR \n"
                + "patient.date_created BETWEEN ? AND ?\n"
                + "OR \n"
                + "person.date_created BETWEEN ? AND ?\n"
                + "OR\n"
                + "person.date_changed BETWEEN ? AND ?\n"
                + "OR\n"
                + "patient_program.date_created BETWEEN ? AND ?\n"
                + "OR\n"
                + "patient_program.date_changed BETWEEN ? AND ?\n"
                + "OR\n"
                + "person_address.date_created BETWEEN ? AND ?\n"
                + "OR\n"
                + "orders.date_created BETWEEN ? AND ?\n"
                + "OR\n"
                + "patient_identifier.date_created BETWEEN ? AND ?)\n"
                + "AND\n"
                + "patient.voided=0\n"
                + "AND\n"
                + "encounter.voided=0\n"
                + "AND \n"
                + "person.voided=0\n"
                + "AND\n"
                + "person.birthdate is not null\n"
                + "AND\n"
                + "person.gender is not null\n"
                + "AND \n"
                + "patient_identifier.voided=0\n"
                + "AND\n"
                + "patient_identifier.identifier_type=5\n"
                + "AND\n"
                + "patient_identifier.identifier is not null\n"
                + "AND\n"
                + "patient_program.voided=0\n"
                + "AND\n"
                + "orders.voided=0\n"
                + "AND\n"
                + "patient_program.date_enrolled is not null\n"
                + "AND\n"
                + "patient_program.program_id=1\n"
                + "GROUP BY patient.patient_id\n";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = prepareQuery(sql_text);
            ps.setDate(1, convertToSQLDate(startDate));
            ps.setDate(2, convertToSQLDate(endDate));
            ps.setDate(3, convertToSQLDate(startDate));
            ps.setDate(4, convertToSQLDate(endDate));
            ps.setDate(5, convertToSQLDate(startDate));
            ps.setDate(6, convertToSQLDate(endDate));
            ps.setDate(7, convertToSQLDate(startDate));
            ps.setDate(8, convertToSQLDate(endDate));
            ps.setDate(9, convertToSQLDate(startDate));
            ps.setDate(10, convertToSQLDate(endDate));
            ps.setDate(11, convertToSQLDate(startDate));
            ps.setDate(12, convertToSQLDate(endDate));
            ps.setDate(13, convertToSQLDate(startDate));
            ps.setDate(14, convertToSQLDate(endDate));
            ps.setDate(15, convertToSQLDate(startDate));
            ps.setDate(16, convertToSQLDate(endDate));
            ps.setDate(17, convertToSQLDate(startDate));
            ps.setDate(18, convertToSQLDate(endDate));
            ps.setDate(19, convertToSQLDate(startDate));
            ps.setDate(20, convertToSQLDate(endDate));
            //ps.setDate(11, convertToSQLDate(startDate));
            //ps.setDate(12, convertToSQLDate(endDate));
            //ps.setDate(13, convertToSQLDate(startDate));
            //ps.setDate(14, convertToSQLDate(endDate));
            rs = ps.executeQuery();
            while (rs.next()) {
                ptsWithChange.add(rs.getInt("patient_id"));
                count++;
                screen.updateStatus("Loading patients with change " + count);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            screen.updateStatus(ex.getMessage());
        } finally {
            cleanUp(rs, ps);
        }
        return ptsWithChange;
    }

    public void runIndicatorReport(Date startDate, Date endDate, File file, int location_id) {
        FileManager mgr = new FileManager();
        int progress = 0;
        screen.updateMinMaxProgress(0, 4);
        patientDemoList = getAllPatientsInDB(location_id);
        screen.updateStatus("Patients loaded...Please wait");
        progress++;
        screen.updateProgress(progress);

        loadCohorts(startDate, endDate, location_id);
        screen.updateStatus("Cohorts loaded...Please wait");
        progress++;
        screen.updateProgress(progress);
        CohortBuilder.init();
        CohortBuilder.loadMap(patientDemoList, lastVisitDateDictionary);
        screen.updateStatus("Dimensions loaded...Please wait");
        progress++;
        screen.updateProgress(progress);
        String[] headers = {
            "Indicator " + formatDate(startDate) + " To " + formatDate(endDate),
            "Male<1",
            "Male1-4",
            "Male5-9",
            "Male10-14",
            "Male15-19",
            "Male20-24",
            "Male25-49",
            "Male>50",
            "Female<1",
            "Female1-4",
            "Female5-9",
            "Female10-14",
            "Female15-19",
            "Female20-24",
            "Female25-49",
            "Female>50",
            "<1 Total",
            "1-4 Total",
            "5-9 Total",
            "10-14 Total",
            "15-19 Total",
            "20-24 Total",
            "25-49 Total",
            ">50 Total",
            "Male Total",
            "Female Total",
            "Total"
        };
        String[] data = null;
        File file2 = null;
        String fileName = file.getAbsolutePath();

        try {
            if (fileName.contains(".")) {
                fileName = fileName.substring(0, fileName.indexOf("."));
            }
            fileName += new SimpleDateFormat("yyyyMMddhhmm'.csv'").format(new Date());
            file2 = new File(fileName);
            //file.renameTo(file2);
            mgr.createCSVWriter(file2.getAbsolutePath());
            data = createIndicatorData(careCurr, "CARE_CURR");

            mgr.writeHeader(headers);
            mgr.writeToCSV(data);
            data = createIndicatorData(careNew, "CARE_NEW");
            mgr.writeToCSV(data);
            data = createIndicatorData(careCumm, "CARE_CUMM");
            mgr.writeToCSV(data);
            data = createIndicatorData(txCurr, "TX_CURR");
            mgr.writeToCSV(data);
            data = createIndicatorData(exit, "CARE_EXIT");
            mgr.writeToCSV(data);
            data = createIndicatorData(tbScrn, "TB_SCRN");
            mgr.writeToCSV(data);
            data = createIndicatorData(tbTxt, "TB_TX");
            mgr.writeToCSV(data);
            data = createIndicatorData(ccVisit, "CLINIC_VISIT");
            mgr.writeToCSV(data);
            progress++;
            screen.updateProgress(progress);
            screen.updateStatus("Indicators written to csv");
            mgr.closeCSVWriter();
        } catch (IOException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        }

    }

    private String[] createIndicatorData(Set<Integer> cohort, String name) {
        String[] dataArr = new String[28];
        dataArr[0] = name;
        for (int i = 1; i <= 27; i++) {
            dataArr[i] = String.valueOf(CohortBuilder.getDim(i, cohort).size());
        }
        return dataArr;
    }

    private void loadCareCurrent(Date startDate, Date endDate, int locationID) {
        String sql_text = "select  distinct obs.person_id from obs where concept_id in (88,860,315) and obs.voided=0 and obs.location_id=? and obs.obs_datetime between ? and ?";
        PreparedStatement ps = null;
        careCurr = new HashSet<Integer>();
        ResultSet rs = null;
        try {
            ps = prepareQuery(sql_text);
            ps.setInt(1, locationID);
            ps.setDate(2, convertToSQLDate(startDate));
            ps.setDate(3, convertToSQLDate(endDate));
            rs = ps.executeQuery();
            while (rs.next()) {
                careCurr.add(rs.getInt("person_id"));
            }
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }
    }

    private void loadCareNew(Date startDate, Date endDate, int locationID) {
        Set<Integer> ptsSet = firstVisitDateDictionary.keySet();
        careNew = new HashSet<Integer>();
        DateTime enrollDate = null;
        DateTime startDateTime = new DateTime(startDate);
        DateTime endDateTime = new DateTime(endDate);
        for (Integer ele : ptsSet) {
            enrollDate = new DateTime(firstVisitDateDictionary.get(ele));

            if ((enrollDate.isEqual(startDateTime) || enrollDate.isAfter(startDateTime)) && (enrollDate.isEqual(endDateTime) || enrollDate.isBefore(endDateTime))) {
                careNew.add(ele);
            }

        }
    }

    private void loadCareCummulative(Date startDate, Date endDate, int locationID) {
        careCumm = new HashSet<Integer>();
        for (model.datapump.Demographics ele : patientDemoList) {
            careCumm.add(ele.getPatientID());
        }
    }

    private void loadTBScrn(Date startDate, Date endDate, int locationID) {
        tbScrn = new HashSet<Integer>();
        String sql_text = "select distinct obs.person_id from obs where location_id=? and concept_id=862 and voided=0 and obs_datetime between ? and ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = prepareQuery(sql_text);
            ps.setInt(1, locationID);
            ps.setDate(2, convertToSQLDate(startDate));
            ps.setDate(3, convertToSQLDate(endDate));
            rs = ps.executeQuery();
            while (rs.next()) {
                tbScrn.add(rs.getInt("person_id"));
            }
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }
    }

    public void loadClinicVisit(Date startDate, Date endDate, int locationID) {
        ccVisit = new HashSet<Integer>();
        String sql_text = "select distinct obs.person_id from obs where location_id=? and voided=0 and obs_datetime between ? and ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = prepareQuery(sql_text);
            ps.setInt(1, locationID);
            ps.setDate(2, convertToSQLDate(startDate));
            ps.setDate(3, convertToSQLDate(endDate));
            rs = ps.executeQuery();
            while (rs.next()) {
                ccVisit.add(rs.getInt("person_id"));
            }
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }
    }

    private void loadTBTx(Date startDate, Date endDate, int locationID) {
        tbTxt = new HashSet<Integer>();
        String sql_text = "select distinct obs.person_id from obs where location_id=? and concept_id=862 and value_coded=872 and voided=0 and obs_datetime between ? and ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = prepareQuery(sql_text);
            ps.setInt(1, locationID);
            ps.setDate(2, convertToSQLDate(startDate));
            ps.setDate(3, convertToSQLDate(endDate));
            rs = ps.executeQuery();
            while (rs.next()) {
                tbTxt.add(rs.getInt("person_id"));
            }
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }
    }

    private void loadExits(Date startDate, Date endDate, int locationID) {
        exit = new HashSet<Integer>();
        String sql_text = "select obs.person_id from obs where concept_id=977 and location_id=? and voided=0 and obs_datetime between ? and ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = prepareQuery(sql_text);
            ps.setInt(1, locationID);
            ps.setDate(2, convertToSQLDate(startDate));
            ps.setDate(3, convertToSQLDate(endDate));
            rs = ps.executeQuery();
            while (rs.next()) {
                exit.add(rs.getInt("person_id"));
            }
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }

    }

    private void loadTxCurr(Date startDate, Date endDate, int locationID) {
        txCurr = new HashSet<Integer>();
        /*String sql_text = "select orders.patient_id, max(orders.start_date) from orders\n"
                + "where  orders.voided=0 and orders.concept_id in(\n"
                + "17,18,23,952,953,954,955,956,962,961,\n"
                + "957,958,959,960,1184,1185,1186,1187,\n"
                + "1188,1189,1190,1191,1213,1219,1220,1221,\n"
                + "1222,1223,1224,1225,1226,1227,1228,\n"
                + "1229,1230,1231,1232,1233,1234,1533,\n"
                + "1235,1236,1237,1238,1528,7777747,\n"
                + "7777748,7777749,7777750,7777751) \n"
                + "GROUP BY orders.patient_id  having TIMESTAMPDIFF(MONTH,max(orders.start_date),?)<=6";*/
        String sql_text = "select encounter.patient_id,max(encounter.encounter_datetime) from encounter inner join obs on(obs.encounter_id=encounter.encounter_id)\n"
                + "                inner join concept_set on(obs.value_coded=concept_set.concept_id and concept_set.concept_set=27) where encounter.voided=0 and obs.voided=0 and form_id in(46,53) and obs.concept_id=7778364 \n"
                + "                GROUP BY encounter.patient_id having TIMESTAMPDIFF(MONTH,max(encounter.encounter_datetime),?)<=4 ;";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = prepareQuery(sql_text);
            ps.setDate(1, convertToSQLDate(endDate));

            rs = ps.executeQuery();
            while (rs.next()) {
                txCurr.add(rs.getInt("patient_id"));
            }
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }

    }

    private void loadCohorts(Date startDate, Date endDate, int locationID) {
        loadCareCurrent(startDate, endDate, locationID);
        loadCareCummulative(startDate, endDate, locationID);
        loadCareNew(startDate, endDate, locationID);
        loadTxCurr(startDate, endDate, locationID);
        loadExits(startDate, endDate, locationID);
        loadTBScrn(startDate, endDate, locationID);
        loadTBTx(startDate, endDate, locationID);
        loadClinicVisit(startDate, endDate, locationID);

    }

    public void runDWHExport(Date startDate, Date endDate, File file, int location_id) {
        screen.updateMinMaxProgress(0, 9);
        long starttime = System.currentTimeMillis();

        screen.updateStatus("DWH Export started..... Please wait...");

        /*String[] filesNames = {
            FileUtils.toFile(getClass().getClassLoader().getResource("schema/appointment.xsd")).getAbsolutePath(),
            FileUtils.toFile(getClass().getClassLoader().getResource("schema/concept.xsd")).getAbsolutePath(),
            FileUtils.toFile(getClass().getClassLoader().getResource("schema/drug.xsd")).getAbsolutePath(),
            FileUtils.toFile(getClass().getClassLoader().getResource("schema/obs.xsd")).getAbsolutePath(),
            FileUtils.toFile(getClass().getClassLoader().getResource("schema/patient.xsd")).getAbsolutePath(),
            FileUtils.toFile(getClass().getClassLoader().getResource("schema/user.xsd")).getAbsolutePath(),
            FileUtils.toFile(getClass().getClassLoader().getResource("schema/art.xsd")).getAbsolutePath(),
            FileUtils.toFile(getClass().getClassLoader().getResource("schema/regimens.xsd")).getAbsolutePath()};*/
        //String[] filesNames = {"schema/appointment.xsd", "schema/concept.xsd", "schema/regimens.xsd", "schema/art.xsd", "schema/user.xsd", "schema/patients.xsd", "schema/obs.xsd", "schema/drugs.xsd"};
        String[] filesNames = {"schema"};
        FileOutputStream fos = null;

        ZipOutputStream zos = null;
        File file2 = null;
        String fileName = file.getAbsolutePath();
        try {
            if (fileName.contains(".")) {
                fileName = fileName.substring(0, fileName.indexOf("."));
            }
            fileName += new SimpleDateFormat("yyyyMMddhhmm'.zip'").format(new Date());
            file2 = new File(fileName);
            file.renameTo(file2);
            int progress = 0;
            runPatientRegimenDWH(startDate, endDate);
            progress++;
            screen.updateProgress(progress);
            runDWHDemographics(file, location_id);
            progress++;
            screen.updateProgress(progress);
            runDWHAppointments(file);
            progress++;
            screen.updateProgress(progress);
            runDWHDrugs(file);
            progress++;
            screen.updateProgress(progress);
            runDWHFormByForm(file, startDate, endDate);
            progress++;
            screen.updateProgress(progress);
            runDWHUsers(file);
            progress++;
            screen.updateProgress(progress);
            //runDictionaryDWHExport();
            progress++;
            screen.updateProgress(progress);
            //runRelationshipExport();
            progress++;
            screen.updateProgress(progress);
            runARTCommencementExport(file, startDate, endDate);
            progress++;
            screen.updateProgress(progress);
            zipFileEntryNames.addAll(Arrays.asList(filesNames));
            this.addToSpecialZipEncrypt(zipFileEntryNames, fileName);
            clearFiles(zipFileEntryNames, Arrays.asList(filesNames));
        } catch (Exception e) {
            //screen.updateStatus(e.getMessage());
            handleException(e);
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
                handleException(ioe);
            }

        }

        long duration = calculateDuration(starttime);
        screen.updateStatus("<html><body>DWH Export completed at " + duration + "(" + (Math.round(duration / 60)) + " mins) <br> Export file: " + fileName + "</html></body>");
    }

    public ARTCommence constructARTCommencement(Encounter enc, HashMap<Integer, Obs> obsMap) {
        ARTCommence art = new ARTCommence();
        Obs obs = null;
        art.setPatientID(enc.getPatientID());
        art.setEncounterID(enc.getEncounterID());
        art.setFormID(enc.getFormID());
        art.setCreatorID(enc.getCreator());
        art.setProviderID(enc.getProviderID());
        art.setLocationID(enc.getLocationID());
        art.setUuid(enc.getUuid());
        art.setPepfarID(pepfarDictionary.get(enc.getPatientID()));
        art.setHospID(hospIDDictionary.get(enc.getPatientID()));
        art.setVisitDate(enc.getEncounterDate());
        art.setPmmForm(formNamesDictionary.get(enc.getFormID()));
        art.setLocation(locationDictionary.get(enc.getLocationID()));
        User usr = userDictionary.get(enc.getCreator());
        if (usr != null) {
            art.setEnteredBy(usr.getFullName());
            art.setDateEntered(enc.getDateCreated());
        }
        PersonName personName = namesDictionary.get(enc.getProviderID());
        if (personName != null) {
            art.setProviderName(personName.getFullName());
        }

        art.setDateMedicallyEligible(getValueDate(1703, obsMap));
        art.setWhyEligible(getValueStringOrCoded(1731, obsMap));
        art.setDateInitialAdherenceCouncelingCompleted(getValueDate(7777862, obsMap));
        art.setDateTransferedIn(getValueDate(978, obsMap));
        art.setFacilityTransferredFrom(getValueStringOrCoded(1732, obsMap));
        art.setFirstRegimenLine(getValueStringOrCoded(7778531, obsMap));
        art.setFirstLineRegimen(getValueStringOrCoded(7778532, obsMap));
        art.setSecondLineRegimen(getValueStringOrCoded(7778533, obsMap));
        art.setOtherRegimen(getValueStringOrCoded(7778534, obsMap));
        art.setDateARTStarted(getValueDate(863, obsMap));
        Person person = personDictionary.get(enc.getPatientID());
        Date dob = null;
        if (person != null && art.getDateARTStarted() != null) {
            dob = person.getBirthDate();
            DateTime d1 = new DateTime(dob);
            DateTime d2 = new DateTime(art.getDateARTStarted());
            Years yrs = Years.yearsBetween(d1, d2);
            art.setAgeARTStart(yrs.getYears());
        }
        art.setClinicalStageAtARTStart(getValueStringOrCoded(7778529, obsMap));
        art.setWeight(getValueNumeric(1734, obsMap));
        art.setHeight(getValueNumeric(1735, obsMap));
        art.setFunctionalStatus(getValueStringOrCoded(7778530, obsMap));
        art.setCd4CountAtARTStart(getValueNumeric(1735, obsMap));
        art.setArtRegisterSerialNo(getValueStringOrCoded(7778291, obsMap));
        return art;
    }

    public void runARTCommencementExport(File file, Date startDate, Date endDate) {
        /*String sql_text = "select \n"
                + "   obs.person_id as patient_id,\n"
                + "   pid1.identifier as pepfar_id,\n"
                + "   pid2.identifier as hosp_id,\n"
                + "   encounter.encounter_datetime as visit_date,\n"
                + "   encounter.encounter_id as encounter_id,\n"
                + "   encounter.form_id as form_id,\n"
                + "   encounter.creator as creator_id,\n"
                + "   encounter.provider_id as provider_id,\n"
                + "   encounter.location_id as location_id,\n"
                + "   encounter.uuid as uuid,\n"
                + "   location.name as location,\n"
                + "   form.name as pmm_form,\n"
                + "   CONCAT(pn1.given_name,' ',pn1.family_name) as entered_by,\n"
                + "   CONCAT(pn2.given_name,' ',pn2.family_name) as provider,\n"
                + "   encounter.date_created as date_created\n"
                + "   ,MAX(IF(obs.concept_id= 1703, obs.value_datetime, NULL)) as  `date_clinically_eligible`\n"
                + "   ,GROUP_CONCAT(IF(obs.concept_id= 1731, cn1.name, NULL)) as  `why_eligible`\n"
                + "   ,MAX(IF(obs.concept_id= 7777862, obs.value_datetime, NULL)) as  `date_initial_adh_counseling`\n"
                + "   ,MAX(IF(obs.concept_id=978, obs.value_datetime, NULL)) as  `date_transfered_in`\n"
                + "   ,MAX(IF(obs.concept_id=1732, obs.value_text, NULL)) as  `facility_transfered_from`\n"
                + "  ,MAX(IF(obs.concept_id= 7778531, cn1.name, NULL)) as  `regimen_line_at_art_start`\n"
                + "  ,MAX(IF(obs.concept_id= 7778532, cn1.name, NULL)) as  `first_line_regimen`\n"
                + "  ,MAX(IF(obs.concept_id= 7778533, cn1.name, NULL)) as  `second_line_regimen`\n"
                + "  ,MAX(IF(obs.concept_id= 7778534, cn1.name, NULL)) as  `other_regimen`\n"
                + "  ,MAX(IF(obs.concept_id= 863, obs.value_datetime, NULL)) as  `art_start_date`\n"
                + "  ,MAX(IF(obs.concept_id= 863, TIMESTAMPDIFF(YEAR,person.birthdate,obs.value_datetime), NULL)) as  `age_art_start`\n"
                + "  ,MAX(IF(obs.concept_id= 7778529, cn1.name, NULL)) as  `who_at_art_start`\n"
                + "  ,MAX(IF(obs.concept_id= 1734, obs.value_numeric, NULL)) as  `weight_at_art_start`\n"
                + "  ,MAX(IF(obs.concept_id= 1735, obs.value_numeric, NULL)) as  `height_at_art_start`\n"
                + "  ,MAX(IF(obs.concept_id= 7778530, cn1.name, NULL)) as  `fs_at_art_start`\n"
                + "  ,MAX(IF(obs.concept_id= 1733,obs.value_numeric, NULL)) as  `cd4_at_art_start`\n"
                + "  ,MAX(IF(obs.concept_id= 7778291, obs.value_text, NULL)) as  `art_register_serial_no`\n"
                + "  FROM obs \n"
                + "left join patient_identifier pid1 on(pid1.patient_id=obs.person_id and pid1.identifier_type=4 and obs.voided=0 and pid1.voided=0)\n"
                + "left join patient_identifier pid2 on(pid2.patient_id=obs.person_id and pid2.identifier_type=3 and obs.voided=0 and pid2.voided=0)\n"
                + "left join encounter on(encounter.encounter_id=obs.encounter_id and encounter.voided=0)\n"
                + "left join location on(location.location_id=encounter.location_id)\n"
                + "left join users on(users.user_id=encounter.creator)\n"
                + "left join patient on(patient.patient_id=encounter.patient_id)\n"
                + "left join person on(patient.patient_id=person.person_id)\n"
                + "left join person_name pn1 on(pn1.person_id=users.person_id)\n"
                + "left join person_name pn2 on(pn2.person_id=encounter.provider_id)\n"
                + "left join form on(form.form_id=encounter.form_id)\n"
                + "left join concept_name cn1 on(cn1.concept_id=obs.value_coded and cn1.locale='en' and cn1.locale_preferred=1)\n"
                + "where encounter.form_id=1 and encounter.voided=0 and patient.voided=0\n"
                + "GROUP BY  obs.person_id, obs.encounter_id";*/
        screen.updateStatus("Writing art.xml....Please wait");
        Integer[] formIDArr = {1};
        Set<Integer> idSet = new HashSet<Integer>(Arrays.asList(formIDArr));
        ArrayList<Encounter> encList = getEncounterForDate(startDate, endDate, idSet);
        HashMap<Integer, Obs> obsMap = new HashMap<Integer, Obs>();

        screen.updateStatus("Writing art.xml....Please wait");

        Statement stmt = null;
        ResultSet rs = null;
        ARTCommence art = null;
        String fileName = "art.xml";
        PersonName pn1 = null;
        int count = 0;
        zipFileEntryNames.add(fileName);

        try {
            mgr.createXMLWriter(fileName);
            mgr.openXMLDocument();
            mgr.writeXMLHeader("artcommencement", "schema/art.xsd");
            for (Encounter enc : encList) {
                obsMap = getObsForEncounter(enc.getEncounterID());
                art = constructARTCommencement(enc, obsMap);
                mgr.writeToXML(art);
                count++;
                screen.updateStatus("Writing art.xml...Please wait " + count);
            }
            //mgr.writeHeaderXML("artcommencement", "schema/art.xsd");
            /*stmt = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            rs = stmt.executeQuery(sql_text);
            while (rs.next()) {
                art = new ARTCommence();
                art.setPatientID(rs.getInt("patient_id"));
                art.setEncounterID(rs.getInt("encounter_id"));
                art.setFormID(rs.getInt("form_id"));
                art.setCreatorID(rs.getInt("creator_id"));
                art.setProviderID(rs.getInt("provider_id"));
                art.setLocationID(rs.getInt("location_id"));
                art.setUuid(rs.getString("uuid"));
                art.setPepfarID(rs.getString("pepfar_id"));
                art.setHospID(rs.getString("hosp_id"));
                art.setVisitDate(rs.getDate("visit_date"));
                art.setPmmForm(rs.getString("pmm_form"));
                art.setLocation(rs.getString("location"));
                art.setEnteredBy(rs.getString("entered_by"));
                art.setDateEntered(rs.getDate("date_created"));
                art.setProviderName(rs.getString("provider"));
                art.setDateMedicallyEligible(rs.getDate("date_clinically_eligible"));
                art.setWhyEligible(rs.getString("why_eligible"));
                art.setDateInitialAdherenceCouncelingCompleted(rs.getDate("date_initial_adh_counseling"));
                art.setDateTransferedIn(rs.getDate("date_transfered_in"));
                art.setFacilityTransferredFrom(rs.getString("facility_transfered_from"));
                art.setFirstRegimenLine(rs.getString("regimen_line_at_art_start"));
                art.setFirstLineRegimen(rs.getString("first_line_regimen"));
                art.setSecondLineRegimen(rs.getString("second_line_regimen"));
                art.setOtherRegimen(rs.getString("other_regimen"));
                art.setDateARTStarted(rs.getDate("art_start_date"));
                art.setAgeARTStart(rs.getInt("age_art_start"));
                art.setClinicalStageAtARTStart(rs.getString("who_at_art_start"));
                art.setWeight(rs.getDouble("weight_at_art_start"));
                art.setHeight(rs.getDouble("height_at_art_start"));
                art.setFunctionalStatus(rs.getString("fs_at_art_start"));
                art.setCd4CountAtARTStart(rs.getDouble("cd4_at_art_start"));
                art.setArtRegisterSerialNo(rs.getString("art_register_serial_no"));
                mgr.writeToXML(art);
                count++;
                screen.updateStatus("Writing drugs.xml...Please wait " + count);
            }*/
            mgr.endXMLDocument();
            zipFileEntryNames.add(fileName);
            screen.updateStatus("art.xml file complete.... please wait");
            //rs.close();
            //stmt.close();
            mgr.closeXMLWriter();
        } catch (XMLStreamException xe) {
            handleException(xe);
        } catch (IOException ioe) {
            handleException(ioe);
        }

    }

    public void runCareCardFollowUpExport(File file) {
        String sql_text = "select \n"
                + "   obs.person_id as patientID,\n"
                + "   obs.encounter_id as encounterID,\n"
                + "   encounter.form_id as formID,\n"
                + "   encounter.location_id as locationID,\n"
                + "  encounter.provider_id as providerID,\n"
                + "  encounter.creator as creatorID,\n"
                + "   pid1.identifier as PepfarID,\n"
                + "   pid2.identifier as HospID,\n"
                + "   encounter.encounter_datetime as VisitDate,\n"
                + "   location.name as Location,\n"
                + "   form.name as PMMForm,\n"
                + "   CONCAT(pn1.given_name,' ',pn1.family_name) as EnteredBy,\n"
                + "   CONCAT(pn2.given_name,' ',pn2.family_name) as Provider,\n"
                + "   encounter.date_created as DateCreated\n"
                + "   ,MAX(IF(obs.concept_id= 1537, cn1.name, NULL)) as  `VisitType`\n"
                + "   ,MAX(IF(obs.concept_id= 7778232, cn1.name, NULL)) as  `Scheduled`\n"
                + "   ,MAX(IF(obs.concept_id= 85, obs.value_numeric, NULL)) as  `Weight(KG)`\n"
                + "   ,MAX(IF(obs.concept_id= 571,obs.value_numeric, NULL)) as  `Height(CM)`\n"
                + "   ,MAX(IF(obs.concept_id= 84, obs.value_numeric, NULL)) as  `Systolic_BP`\n"
                + "  ,MAX(IF(obs.concept_id= 568, obs.value_numeric, NULL)) as  `Diastolic_BP`\n"
                + "  ,MAX(IF(obs.concept_id= 1104, obs.value_numeric, NULL)) as  `SurfaceArea`\n"
                + "  ,MAX(IF(obs.concept_id= 1105, obs.value_numeric, NULL)) as  `HeadCircumference`\n"
                + "  ,MAX(IF(obs.concept_id= 387, obs.value_numeric, NULL)) as  `MUAC`\n"
                + "  ,MAX(IF(obs.concept_id= 570, obs.value_numeric, NULL)) as  `Temperature`\n"
                + "  ,MAX(IF(obs.concept_id= 7777871, cn1.name, NULL)) as  `PMTCTLink`\n"
                + "  ,MAX(IF(obs.concept_id= 577, obs.value_datetime, NULL)) as  `EDD`\n"
                + "  ,MAX(IF(obs.concept_id= 1741, cn1.name, NULL)) as  `FamilyPlanning`\n"
                + "  ,MAX(IF(obs.concept_id= 1742, cn1.name, NULL)) as  `FamilyPlanningMethod`\n"
                + "  ,MAX(IF(obs.concept_id= 1013, cn1.name, NULL)) as  `FunctionalStatus`\n"
                + "  ,MAX(IF(obs.concept_id= 1116, cn1.name, NULL)) as  `DevelopmentStatus`\n"
                + "  ,MAX(IF(obs.concept_id= 7777798, cn1.name, NULL)) as  `WHOStaging`\n"
                + "  ,MAX(IF(obs.concept_id= 862, cn1.name, NULL)) as  `TBStatus`\n"
                + "  ,MAX(IF(obs.concept_id= 7778105, obs.value_text, NULL)) as  `TBNo`\n"
                + "  ,MAX(IF(obs.concept_id= 528,cn1.name, NULL)) as  `Sign/Symptom`\n"
                + " ,MAX(IF(obs.concept_id= 7778411, cn1.name, NULL)) as  `OtherSignsAndSymptoms`\n"
                + " ,MAX(IF(obs.concept_id= 1607, cn1.name, NULL)) as  `PresumedARVSideEffect`\n"
                + "  ,MAX(IF(obs.concept_id= 7778111, cn1.name, NULL)) as  `RegimenLine`\n"
                + "  ,MAX(IF(obs.concept_id= 7778108, cn1.name, NULL)) as  `FirstLine`\n"
                + "  ,MAX(IF(obs.concept_id= 7778109, cn1.name, NULL)) as  `SecondLine`\n"
                + "   ,MAX(IF(obs.concept_id= 7778410, cn1.name, NULL)) as  `OtherRegimen`\n"
                + "   ,MAX(IF(obs.concept_id= 7777874, cn1.name, NULL)) as  `ARVAdherence`\n"
                + "    ,MAX(IF(obs.concept_id= 7778453, cn1.name, NULL)) as  `ReasonForPoorARVAdherence`\n"
                + "   ,MAX(IF(obs.concept_id= 7778203, cn1.name, NULL)) as  `CTX`\n"
                + "   ,MAX(IF(obs.concept_id= 7777875, cn1.name, NULL)) as  `CTXAdherence`\n"
                + " ,MAX(IF(obs.concept_id= 7778454, cn1.name, NULL)) as  `ReasonForPoorCTXAdherence`\n"
                + "   ,MAX(IF(obs.concept_id= 7778202, cn1.name , NULL)) as  `INHDose`\n"
                + "   ,MAX(IF(obs.concept_id= 7777876, cn1.name, NULL)) as  `INHAdherence`\n"
                + " ,MAX(IF(obs.concept_id= 7778455, cn1.name, NULL)) as  `ReasonForPoorINHAdherence`\n"
                + " ,MAX(IF(obs.concept_id= 7778231, cn1.name, NULL)) as  `OtherPrescribedDrugs`\n"
                + " ,MAX(IF(obs.concept_id= 382, cn1.name, NULL)) as  `TestOrdered`\n"
                + "  ,MAX(IF(obs.concept_id= 178, cn1.name, NULL)) as  `Hospitalized`\n"
                + "  ,MAX(IF(obs.concept_id= 998, obs.value_datetime, NULL)) as  `HospitalizationDate`\n"
                + " ,MAX(IF(obs.concept_id= 997, obs.value_text, NULL)) as  `HospitalizationReason`\n"
                + " ,MAX(IF(obs.concept_id= 260, cn1.name, NULL)) as  `ReferralsOrdered`\n"
                + " ,MAX(IF(obs.concept_id= 7777821, cn1.name, NULL)) as  `NextAppointment`\n"
                + " ,MAX(IF(obs.concept_id= 7777822, obs.value_datetime, NULL)) as  `NextAppointmentDate`\n"
                + "FROM obs \n"
                + "left join patient_identifier pid1 on(pid1.patient_id=obs.person_id and pid1.identifier_type=4 and obs.voided=0 and pid1.voided=0)\n"
                + "left join patient_identifier pid2 on(pid2.patient_id=obs.person_id and pid2.identifier_type=3 and obs.voided=0 and pid2.voided=0)\n"
                + "left join encounter on(encounter.encounter_id=obs.encounter_id and encounter.voided=0)\n"
                + "left join location on(location.location_id=encounter.location_id)\n"
                + "left join users on(users.user_id=encounter.creator)\n"
                + "left join person_name pn1 on(pn1.person_id=users.person_id)\n"
                + "left join person_name pn2 on(pn2.person_id=encounter.provider_id)\n"
                + "left join form on(form.form_id=encounter.form_id)\n"
                + "left join concept_name cn1 on(cn1.concept_id=obs.value_coded and cn1.locale='en' and cn1.locale_preferred=1)\n"
                + "where encounter.form_id in (56,24,18,27,20,47) and encounter.voided=0\n"
                + "GROUP BY  obs.person_id, obs.encounter_id order by obs.person_id asc,encounter.encounter_datetime desc;";

        screen.updateStatus("Writing carecardfollowup.xml....Please wait");

        Statement stmt = null;
        ResultSet rs = null;
        CareCardFollowUp card = null;
        String fileName = "carecardfollowup.xml";
        PersonName pn1 = null;
        int count = 0;
        zipFileEntryNames.add(fileName);
        try {
            mgr.createXMLWriter(fileName);
            mgr.openXMLDocument();
            mgr.writeXMLHeader("carecardfollowup");
            stmt = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            rs = stmt.executeQuery(sql_text);
            while (rs.next()) {
                card = new CareCardFollowUp();
                card.setPatientID(rs.getInt("patientID"));
                card.setEncounterID(rs.getInt("encounterID"));
                card.setFormID(rs.getInt("formID"));
                card.setProviderID(rs.getInt("providerID"));
                card.setCreatorID(rs.getInt("creatorID"));
                card.setPepfarID(rs.getString("PepfarID"));
                card.setHospID(rs.getString("HospID"));
                card.setVisitDate(rs.getDate("VisitDate"));
                card.setLocation(rs.getString("Location"));
                card.setLocationID(rs.getInt("locationID"));
                card.setPmmForm(rs.getString("PMMForm"));
                card.setEnteredBy(rs.getString("EnteredBy"));
                card.setProvider(rs.getString("Provider"));
                card.setDateEntered(rs.getDate("DateCreated"));
                card.setVisitType(rs.getString("VisitType"));
                card.setIsScheduled(rs.getString("Scheduled"));
                card.setWeight(rs.getDouble("weight"));
                card.setHeight(rs.getDouble("height"));
                card.setSystolicBP(rs.getDouble("Systolic_BP"));
                card.setDiastolicBP(rs.getDouble("Diastolic_BP"));
                card.setSurfaceArea(rs.getDouble("SurfaceArea"));
                card.setHeadCircumference(rs.getDouble("HeadCircumference"));
                card.setMuac(rs.getDouble("MUAC"));
                card.setTemperature(rs.getDouble("Temperature"));
                card.setPMTCTLink(rs.getString("PMTCTLink"));
                card.setEDD(rs.getDate("EDD"));
                card.setFamilyPlanning(rs.getString("FamilyPlanning"));
                card.setFamilyPlanningMethod(rs.getString("FamilyPlanningMethod"));
                card.setFunctionalStatus(rs.getString("FunctionalStatus"));
                card.setDevelopmentalStatus(rs.getString("DevelopmentalStatus"));
                card.setWHO(rs.getString("WHOStaging"));
                card.setTBStatus(rs.getString("TBStatus"));
                card.setTBNo(rs.getString("TBNo"));
                card.setSignsAndSymptoms(rs.getString("Sign/Symptom"));
                card.setOtherSignsAndSymptoms(rs.getString("OtherSignsAndSymptom"));
                card.setPresumedARVSideEffects(rs.getString("PresumedARVSideEffect"));
                card.setRegimenLine(rs.getString("RegimenLine"));
                card.setFirstLineRegimen(rs.getString("FirstLine"));
                card.setSecondLineRegimen(rs.getString("SecondLine"));
                card.setOtherRegimen(rs.getString("OtherRegimen"));
                card.setARVAdherence(rs.getString("ARVAdherence"));
                card.setReasonForPoorARVAdherence(rs.getString("ReasonForPoorARVAdherence"));
                card.setCTXDose(rs.getString("CTX"));
                card.setCTXAdherence(rs.getString("CTXAdherence"));
                card.setReasonForPoorCTXAdherence(rs.getString("ReasonForPoorCTXAdherence"));
                card.setINHDose(rs.getString("INHDose"));
                card.setINHAdherence(rs.getString("INHAdherence"));
                card.setReasonForPoorINHAdherence(rs.getString("ReasonForPoorINHAdherence"));
                card.setOtherPrescribedDrugs(rs.getString("OtherPrescribedDrugs"));
                card.setTestOrdered(rs.getString("TestOrdered"));
                card.setHospitalized(rs.getString("Hospitalized"));
                card.setHospitalizationDate(rs.getDate("HospitalizationDate"));
                card.setHospitalizationReason(rs.getString("HospitalizationReason"));
                card.setReferrals(rs.getString("ReferralsOrdered"));
                card.setNextAppointment(rs.getString("NextAppointment"));
                card.setNextAppointmentDate(rs.getDate("NextAppointmentDate"));
                mgr.writeToXML(card);
                count++;
                screen.updateStatus("Carecard records...." + count);
            }
            cleanUp(rs, stmt);
        } catch (SQLException ex) {
            handleException(ex);
        } catch (XMLStreamException ex) {
            handleException(ex);
        } catch (IOException ioe) {
            handleException(ioe);
        }

    }

    public void runPharmacyExport(File file) {
        String sql_text = "";
    }

    public void handleException(Exception e) {
        screen.updateStatus(e.getMessage());
        e.printStackTrace();
    }

    public void addToSpecialZipEncrypt(ArrayList<String> fileNames, String zipfile) {
        ArrayList fileList = new ArrayList();
        for (String f : fileNames) {
            CompressUtil.zip(f, zipfile, "HMISPass1word");
            screen.updateStatus(f + " has been added to zip");
        }

    }

    public void clearFiles(ArrayList<String> fileNames, List<String> exceptNames) {
        File fe;
        for (String fileName : fileNames) {
            if (!exceptNames.contains(fileName)) {
                fe = new File(fileName);
                if (fe.delete()) {
                    screen.updateStatus(fileName + " has been cleared");
                }
            }
        }
        fileNames.clear();
    }

    public void addToSpecialZip(ArrayList<String> fileNames, String zipfile) {
        ArrayList fileList = new ArrayList();
        for (String f : fileNames) {
            CompressUtil.zip(f, zipfile, null);
            screen.updateStatus(f + " has been added to zip");
        }
        File fe;
        for (String fileName : fileNames) {
            fe = new File(fileName);
            if (fe.delete()) {
                screen.updateStatus(fileName + " has been cleared");
            }
        }
        fileNames.clear();

    }

    public void runRelationshipExport() {
        screen.updateStatus("Writing relationship.xml....Please wait");
        String sql_text = "select \n"
                + "relationship.relationship_id,\n"
                + "relationship.person_a,\n"
                + "relationship.person_b,\n"
                + "relationship_type.a_is_to_b,\n"
                + "relationship_type.b_is_to_a,\n"
                + "relationship.creator,\n"
                + "relationship.date_created,\n"
                + "relationship.relationship,\n"
                + "relationship.creator,\n"
                + "relationship.voided,\n"
                + "relationship.date_voided,\n"
                + "relationship.uuid\n"
                + " from relationship INNER JOIN relationship_type on(relationship_type.relationship_type_id=relationship.relationship)";
        Statement stmt = null;
        ResultSet rs = null;
        int person_id = -1;
        int creator_id = -1;
        int count = 0;
        Relationship rel = null;
        User usr = null;
        String fileName = "relationship.xml";
        PersonName pn1 = null;
        zipFileEntryNames.add(fileName);

        try {
            mgr.createXMLWriter(fileName);
            mgr.openXMLDocument();
            mgr.writeXMLHeader("relationships", "schema/relationship.xsd");
            stmt = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            rs = stmt.executeQuery(sql_text);
            while (rs.next()) {
                rel = new Relationship();
                rel.setRelationshipID(rs.getInt("relationship_id"));
                person_id = rs.getInt("person_a");
                rel.setPersonAID(person_id);
                pn1 = namesDictionary.get(person_id);
                if (pn1 != null) {
                    rel.setPersonAName(pn1.getFullName());
                }
                person_id = rs.getInt("person_b");
                pn1 = namesDictionary.get(person_id);
                if (pn1 != null) {
                    rel.setPersonBName(pn1.getFullName());
                }
                rel.setPersonBID(person_id);
                rel.setaIsToB(rs.getString("a_is_to_b"));
                rel.setbIsToA(rs.getString("b_is_to_a"));
                creator_id = rs.getInt("creator");
                rel.setCreatorID(creator_id);
                usr = userDictionary.get(creator_id);
                if (usr != null) {
                    rel.setEnteredBy(usr.getFullName());
                }
                rel.setDateEntered(rs.getDate("date_created"));
                rel.setRelationshipTypeID(rs.getInt("relationship"));
                rel.setVoided(rs.getInt("voided"));
                rel.setDateVoided(rs.getDate("date_voided"));
                rel.setUuid(rs.getString("uuid"));
                mgr.writeToXML(rel);
                count++;
                screen.updateStatus("Writing relationship.xml...Please wait " + count);
            }
            mgr.endXMLDocument();
            mgr.closeXMLWriter();
            stmt.close();
            rs.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (XMLStreamException ex) {
            ex.printStackTrace();
            screen.updateStatus(ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
            screen.updateStatus(ex.getMessage());
        } finally {
            try {
                cleanUp(rs, stmt);
                mgr.closeWriters();
            } catch (XMLStreamException ex) {
                ex.printStackTrace();
                screen.updateStatus(ex.getMessage());
            }
        }

    }

    public void runDWHDemographics(File file, int location_id) {
        String sql_text = "select \n"
                + "                         patient.patient_id, person.person_id person_source_pk ,\n"
                + "                     person.uuid person_uuid,\n"
                + "                         person.creator creator_id,\n"
                + "                      DATE_FORMAT(person.birthdate,'%Y-%m-%d') dob,\n"
                + "                       TIMESTAMPDIFF(YEAR,birthdate,curdate()) age,\n"
                + "                       person.gender,\n"
                + "                         DATE_FORMAT(person.date_created,'%Y-%m-%d %H:%i:%s') date_created,\n"
                + "                   person.voided,\n"
                + "                        DATE_FORMAT(person.date_changed,'%Y-%m-%d %H:%i:%s') date_changed \n"
                + "                         from patient inner join person on(patient.patient_id=person.person_id and patient.voided=0)  \n"
                + "                        order by person_source_pk asc";

        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            rs = stmt.executeQuery(sql_text);

            int person_id = -1;
            int creator_id = -1;
            int count = 0;
            PersonName pn = null;
            PatientProgram pprg = null;
            PatientAddress padd = null;
            User usr = null;
            String[] title_arr = {
                "person_source_pk",
                "person_uuid",
                "pepfar_id",
                "hosp_id",
                "ehnid",
                "other_id",
                "first_name",
                "last_name",
                "middle_name",
                "adult_enrollment_dt",
                "pead_enrollment_dt",
                "pmtct_enrollment_dt",
                "dob",
                "gender",
                "address1",
                "address2",
                "address_lga",
                "address_state",
                "creator_id",
                "date_created",
                "voided",
                "date_changed",
                "location_id",
                "creator",
                "location"
            };

            String demofileName = "demographics.xml";
            File demofile = new File(demofileName);
            Demographics pts = null;
            mgr.createXMLWriter(demofileName);
            mgr.openXMLDocument();
            mgr.writeXMLHeader("patients", "schema/patients.xsd");
            while (rs.next()) {
                pts = new Demographics();
                person_id = rs.getInt("person_source_pk");
                pts.setPatientID(person_id);
                pts.setPatientUUID(rs.getString("person_uuid"));
                pts.setPepfarID(pepfarDictionary.get(person_id));
                pts.setHospID(hospIDDictionary.get(person_id));
                pts.seteHNID(ehnidDictionary.get(person_id));
                pts.setOtherID(otherIDDictionary.get(person_id));
                pn = namesDictionary.get(person_id);
                if (pn != null) {
                    pts.setFirstName(pn.getFirstName());
                    pts.setLastName(pn.getLastName());
                    pts.setMiddleName(pn.getMiddleName());
                } else {
                    pts.setFirstName("");
                    pts.setLastName("");
                    pts.setMiddleName("");
                }
                pprg = adultARTPatientProgram.get(person_id);
                if (pprg != null) {
                    pts.setAdultEnrollmentDt(pprg.getDateEnrolled());
                }
                pprg = peadPatientProgram.get(person_id);
                if (pprg != null) {
                    pts.setPeadEnrollmentDt(pprg.getDateEnrolled());
                }
                pprg = pmtctPatientProgram.get(person_id);
                if (pprg != null) {
                    pts.setPmtctEnrollmentDt(pprg.getDateEnrolled());
                }
                pprg = heiEnrollment.get(person_id);
                if (pprg != null) {
                    pts.setHeiEnrollmentDt(pprg.getDateEnrolled());
                }
                pprg = pepPatientEnrollment.get(person_id);
                if (pprg != null) {
                    pts.setPepEnrollmentDt(pprg.getDateEnrolled());
                }
                pts.setDateOfBirth(rs.getDate("dob"));
                pts.setAge(rs.getInt("age"));
                pts.setGender(rs.getString("gender"));
                padd = addressDictionary.get(person_id);
                if (padd != null) {
                    pts.setAddress1(padd.getAddress1());
                    pts.setAddress2(padd.getAddress2());
                    pts.setAddress_lga(padd.getCityVillage());
                    pts.setAddress_state(padd.getStateProvince());
                }
                pts.setCreatorID(rs.getInt("creator_id"));
                pts.setDateCreated(rs.getDate("date_created"));
                pts.setVoided(rs.getInt("voided"));
                pts.setDateChanged(rs.getDate("date_changed"));
                pts.setLocationID(location_id);
                pts.setLocationName(String.valueOf(idLocationDictionary.get(person_id)));

                creator_id = rs.getInt("creator_id");
                usr = userDictionary.get(creator_id);
                if (usr != null) {
                    int pid = usr.getPerson_id();
                    pn = namesDictionary.get(pid);
                    pts.setCreatorName(pn.getFullName());
                }
                mgr.writeToXML(pts);
                count++;
                screen.updateStatus("Writing demographics.xml...Please wait " + count);
            }
            mgr.endXMLDocument();
            zipFileEntryNames.add(demofileName);
            screen.updateStatus("Exported Demographics records... Please wait");
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ioe) {
            screen.updateStatus(ioe.getMessage());
            ioe.printStackTrace();
        } catch (XMLStreamException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rs != null) {
                    rs.close();
                };
                mgr.closeXMLWriter();
            } catch (Exception ex) {

                ex.printStackTrace();
            }
        }

    }

    public void loadLastARTPickupDate2() {
        lastARTPickupDateDictionary = new HashMap<Integer, Date>();
        String sql_text = "select orders.patient_id,max(orders.start_date) as lst_art_dt from orders where orders.concept_id in"
                + "(17,18,23,952,953,954,955,956,957,958,959,960,1184,1185,1186,"
                + "1187,1188,1189,1190,1191,1213,1219,1220,1221,1222,1223,1224,"
                + "1225,1226,1227,1228,1229,1230,1231,1232,1233,1234,1235,1236,1237,"
                + "1238,1528,7777747,7777748,7777749,7777751) and orders.voided=0 group by orders.patient_id";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            rs = stmt.executeQuery(sql_text);
            while (rs.next()) {
                lastARTPickupDateDictionary.put(rs.getInt("patient_id"), rs.getDate("lst_art_dt"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            cleanUp(rs, stmt);
        }
    }

    public void loadLastARTPickupDate() {
        lastARTPickupDateDictionary = new HashMap<Integer, Date>();
        String sql_text = "select obs.person_id, max(obs.obs_datetime) as lst_art_dt from obs where obs.concept_id=7778111 and obs.value_coded in (7778108,7778109) and obs.voided=0 GROUP BY obs.person_id";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            rs = stmt.executeQuery(sql_text);
            while (rs.next()) {
                lastARTPickupDateDictionary.put(rs.getInt("person_id"), rs.getDate("lst_art_dt"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            cleanUp(rs, stmt);
        }
    }

    public void runDictionaryDWHExport() {
        screen.updateStatus("Exporting data dictionary....");

        Concept concept = null;
        String fileName = "concept.xml";
        int count = 0;
        ResultSet rs = null;
        String query = "select concept_name.concept_id, concept_name.name, concept.datatype_id\n"
                + " from concept_name inner join concept on(concept.concept_id=concept_name.concept_id) \n"
                + "where \n"
                + "locale='en' and locale_preferred=1";
        PreparedStatement ps = prepareQuery(query);
        try {
            mgr.createXMLWriter(fileName);
            mgr.openXMLDocument();

            mgr.writeXMLHeader("concepts", "schema/concept.xsd");
            zipFileEntryNames.add(fileName);
            rs = ps.executeQuery();
            int size = getResultSetSize(rs);
            screen.updateMinMaxProgress(0, size);
            while (rs.next()) {
                //map.put(rs.getInt("concept_id"), rs.getString("name"));
                concept = new Concept();
                concept.setConceptID(rs.getInt("concept_id"));
                concept.setConceptName(rs.getString("name"));
                concept.setDataType(rs.getInt("datatype_id"));
                mgr.writeToXML(concept);
                count++;
                screen.updateStatus("Exporting concept.xml " + count);

            }
            //screen.updateProgress(size);
            mgr.endXMLDocument();
            mgr.closeXMLWriter();
            rs.close();
            ps.close();
            //screen.updateMinMaxProgress(0,100);
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
        } catch (IOException ex) {
            screen.updateStatus(ex.getMessage());
        } catch (XMLStreamException ex) {
            screen.updateStatus(ex.getMessage());
        } finally {
            cleanUp(rs, ps);
        }

    }

    public void runDWHDrugs2(File file) {
        screen.updateStatus("drugs.xml export started...Please wait");
        String sql_text = "select\n"
                + "orders.order_id,\n"
                + "orders.patient_id,\n"
                + "drug_order.drug_inventory_id,\n"
                + "orders.concept_id,\n"
                + "orders.start_date ,\n"
                + "orders.discontinued_date ,\n"
                + "drug_order.dose,\n"
                + "drug_order.units,\n"
                + "drug_order.frequency,\n"
                + "drug_order.quantity,\n"
                + "orders.orderer,\n"
                + "orders.creator,\n"
                + "orders.date_created,\n"
                + "orders.voided,\n"
                + "orders.date_voided,\n"
                + "orders.uuid,\n"
                + "orders.voided_by\n"
                + "from\n"
                + "orders\n"
                + "inner join drug_order on(drug_order.order_id=orders.order_id )\n"
                + "inner join drug on(drug.drug_id=drug_order.drug_inventory_id)\n"
                + "inner join patient on(patient.patient_id=orders.patient_id)\n"
                + "ORDER BY patient_id asc ,orders.start_date asc;";

        Statement stmt = null;
        ResultSet rs = null;
        DrugOrder ord = null;
        User usr = null;
        int patient_id = 0;
        int count = 0;
        String drugfileName = "drugs.xml";
        File drugfile = new File(drugfileName);

        try {
            stmt = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            rs = stmt.executeQuery(sql_text);
            mgr.createXMLWriter(drugfileName);
            mgr.openXMLDocument();
            mgr.writeXMLHeader("drugs");
            while (rs.next()) {
                ord = new DrugOrder();
                ord.setOrderID(rs.getInt("order_id"));
                patient_id = rs.getInt("patient_id");
                ord.setPepfarID(pepfarDictionary.get(patient_id));
                ord.setHospID(hospIDDictionary.get(patient_id));
                ord.setConceptID(rs.getInt("concept_id"));
                ord.setDrugDose(String.valueOf(rs.getDouble("dose")));
                ord.setDrugName(drugDictionary.get(rs.getInt("drug_inventory_id")));
                ord.setFrequency(rs.getString("frequency"));
                ord.setPatientID(rs.getInt("patient_id"));
                ord.setStartDate(rs.getDate("start_date"));
                ord.setStopDate(rs.getDate("discontinued_date"));
                ord.setCreator(rs.getInt("creator"));
                ord.setOrderer(rs.getInt("orderer"));
                ord.setQuantity(rs.getDouble("quantity"));
                ord.setUuid(rs.getString("uuid"));
                ord.setVoided(rs.getInt("voided"));
                ord.setDateVoided(rs.getDate("date_voided"));
                ord.setVoidedBy(rs.getInt("voided_by"));
                usr = userDictionary.get(rs.getInt("creator"));
                if (usr != null) {
                    ord.setEnteredBy(namesDictionary.get(usr.getPerson_id()).getFullName());
                }
                ord.setDateEntered(rs.getDate("date_created"));
                count++;
                mgr.writeToXML(ord);
                screen.updateStatus("Writing drugs.xml...Please wait " + count);
            }
            mgr.endXMLDocument();
            zipFileEntryNames.add(drugfileName);
            screen.updateStatus("drugs.xml file complete.... please wait");
            rs.close();
            stmt.close();
            mgr.closeXMLWriter();

        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
        } catch (XMLStreamException ex) {
            screen.updateStatus(ex.getMessage());
        } catch (IOException ex) {
            screen.updateStatus(ex.getMessage());
        } finally {
            cleanUp(rs, stmt);
        }
    }

    public void runDWHDrugs(File file) {
        screen.updateStatus("drugs.xml export started...Please wait");
        //System.out.println("i don start");
        String sql_text = "select \n"
                + "obs.person_id as patient_id,\n"
                + "encounter.encounter_id,\n"
                + "encounter.form_id,\n"
                + "encounter.provider_id,\n"
                + "encounter.creator,\n"
                + "encounter.location_id,\n"
                + "encounter.uuid,\n"
                + "pid1.identifier as pepfar_id,\n"
                + "pid2.identifier as hosp_id,\n"
                + "\n"
                + "encounter.encounter_datetime as dispense_date,\n"
                + "\n"
                + "form.name as pmm_form,\n"
                + "\n"
                + "CONCAT(pn2.given_name,' ',pn2.family_name) as dispensed_by,\n"
                + "\n"
                + "CONCAT(pn1.given_name,' ',pn1.family_name) as entered_by,\n"
                + "\n"
                + "encounter.date_created as date_entered\n"
                + "\n"
                + ",MAX(IF(obs.concept_id=7778364,cn1.name, NULL)) as  `drug_name`\n"
                + "\n"
                + ",MAX(IF(obs.concept_id=7778364,obs.value_coded, NULL)) as  `concept_id`\n"
                + "\n"
                + ",MAX(IF(obs.concept_id=7778365,cn1.name, NULL)) as  `drug_strength`\n"
                + "\n"
                + ",MAX(IF(obs.concept_id=7778390,obs.value_text, NULL)) as  `other_drug_strength`\n"
                + "\n"
                + ",MAX(IF(obs.concept_id=7778366,obs.value_numeric, NULL)) as  `single_dose_prescription_value`\n"
                + "\n"
                + ",MAX(IF(obs.concept_id=7778367,cn1.name, NULL)) as  `single_dose_prescription_unit`\n"
                + "\n"
                + ",MAX(IF(obs.concept_id=7778407,cn1.name, NULL)) as  `drug_frequency`\n"
                + "\n"
                + ",MAX(IF(obs.concept_id=7778370,obs.value_numeric, NULL)) as  `drug_duration_value`\n"
                + "\n"
                + ",MAX(IF(obs.concept_id=7778371,cn1.name, NULL)) as  `drug_duration_unit`\n"
                + "\n"
                + ",MAX(IF(obs.concept_id=7778374,obs.value_numeric, NULL)) as  `drug_quantity_prescribed_value`\n"
                + "\n"
                + ",MAX(IF(obs.concept_id=7778375,cn1.name, NULL)) as  `drug_quantity_prescribed_unit`\n"
                + "\n"
                + ",MAX(IF(obs.concept_id=7778372,obs.value_numeric, NULL)) as  `drug_quantity_dispensed_value`\n"
                + "\n"
                + ",MAX(IF(obs.concept_id=7778373,cn1.name, NULL)) as  `drug_quantity_dispensed_unit`\n"
                + ",encounter.date_created\n"
                + "\n"
                + "\n"
                + "from obs \n"
                + "\n"
                + "left join patient_identifier pid1 on(pid1.patient_id=obs.person_id and pid1.identifier_type=4 and pid1.voided=0)\n"
                + "\n"
                + "left join patient_identifier pid2 on(pid2.patient_id=obs.person_id and pid2.identifier_type=3 and pid2.voided=0)\n"
                + "\n"
                + "left join encounter on(encounter.encounter_id=obs.encounter_id and encounter.voided=0)\n"
                + "\n"
                + "left join users usr1 on(usr1.user_id=encounter.creator and encounter.voided=0)\n"
                + "\n"
                + "left join person_name pn1 on(usr1.person_id=pn1.person_id and pn1.voided=0)\n"
                + "\n"
                + "left join person_name pn2 on(encounter.provider_id=pn2.person_id and pn2.voided=0)\n"
                + "\n"
                + "left join form on(form.form_id=encounter.form_id and encounter.voided=0)\n"
                + "\n"
                + "left join concept_name cn1 on(obs.value_coded=cn1.concept_id and cn1.locale='en' and cn1.locale_preferred=1)\n"
                + "\n"
                + "where encounter.form_id in(46,53,86)  and obs_group_id is not null\n"
                + "\n"
                + "GROUP BY obs.person_id,encounter.encounter_id,obs.obs_group_id ";

        Statement stmt = null;
        ResultSet rs = null;
        Drug ord = null;
        User usr = null;
        int patient_id = 0;
        int count = 0;
        String drugfileName = "drugs.xml";
        File drugfile = new File(drugfileName);

        try {
            stmt = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            rs = stmt.executeQuery(sql_text);
            mgr.createXMLWriter(drugfileName);
            mgr.openXMLDocument();
            mgr.writeXMLHeader("drugs", "schema/drugs.xsd");
            while (rs.next()) {
                ord = new Drug();
                patient_id = rs.getInt("patient_id");
                ord.setPatientID(rs.getInt("patient_id"));
                ord.setEncounterID(rs.getInt("encounter_id"));
                ord.setFormID(rs.getInt("form_id"));
                ord.setProviderID(rs.getInt("provider_id"));
                ord.setCreator(rs.getInt("creator"));
                ord.setLocationID(rs.getInt("location_id"));
                ord.setLocation(locationDictionary.get(rs.getInt("location_id")));
                ord.setUuid(rs.getString("uuid"));
                ord.setPepfarID(pepfarDictionary.get(patient_id));
                ord.setHospID(hospIDDictionary.get(patient_id));
                ord.setPmmForm(rs.getString("pmm_form"));
                ord.setDispensedDate(rs.getDate("dispense_date"));
                ord.setDispensedBy(rs.getString("dispensed_by"));
                ord.setEnteredBy(rs.getString("entered_by"));
                ord.setDateEntered(rs.getDate("date_entered"));
                ord.setDrugName(rs.getString("drug_name"));
                ord.setConceptID(rs.getInt("concept_id"));
                ord.setDrugStrength(rs.getString("drug_strength"));
                ord.setOtherDrugStrength(rs.getString("other_drug_strength"));
                ord.setSingleDosePrescriptionValue(rs.getDouble("single_dose_prescription_value"));
                ord.setSingleDosePrescriptionUnit(rs.getString("single_dose_prescription_unit"));
                ord.setDrugFrequency(rs.getString("drug_frequency"));
                ord.setDrugDurationValue(rs.getInt("drug_duration_value"));
                ord.setDrugDurationUnit(rs.getString("drug_duration_unit"));
                ord.setQuantityPrescribedValue(rs.getDouble("drug_quantity_prescribed_value"));
                ord.setQuantityPrescribedUnit(rs.getString("drug_quantity_prescribed_unit"));
                ord.setQuantityDispensedValue(rs.getDouble("drug_quantity_dispensed_value"));
                ord.setQuantityDispensedUnit(rs.getString("drug_quantity_dispensed_unit"));
                count++;
                mgr.writeToXML(ord);
                screen.updateStatus("Writing drugs.xml...Please wait " + count);
            }
            mgr.endXMLDocument();
            zipFileEntryNames.add(drugfileName);
            screen.updateStatus("drugs.xml file complete.... please wait");
            rs.close();
            stmt.close();
            mgr.closeXMLWriter();

        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } catch (XMLStreamException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            cleanUp(rs, stmt);
        }
        //System.out.println("i don finish");
    }

    public void runLabExport(Date startDate, Date endDate, int locationID, File file) {
        screen.updateStatus("Writing " + file.getName() + " file.....Please wait");
        String sql_text = "select \n"
                + "`obs`.`obs_id`,\n"
                + "`obs`.`person_id`,\n"
                + "`obs`.`obs_datetime`,\n"
                + " encounter.form_id,\n"
                + "`obs`.`concept_id`,\n"
                + " cast(coalesce(obs.value_coded,`obs`.`value_numeric`,`obs`.`value_datetime`,`obs`.`value_text`) as char charset utf8) AS `variable_value`,\n"
                + " obs.value_coded,\n"
                + "`obs`.`date_created`,\n"
                + "`obs`.`creator`,\n"
                + "`obs`.`uuid`,\n"
                + "`obs`.`voided`,\n"
                + "`encounter`.`form_id`,\n"
                + "`encounter`.`provider_id` ,\n"
                + "`encounter`.`encounter_id` ,\n"
                + "`obs`.`location_id`  \n"
                + "from `obs`\n"
                + "inner join `patient` on(`patient`.`patient_id` = `obs`.`person_id`)\n"
                + "inner join `encounter` on(`encounter`.`encounter_id` = `obs`.`encounter_id` and `encounter`.`voided` = 0  and `obs`.`voided` = 0) \n"
                + "where  `obs`.`voided` = 0 and encounter.form_id in (67,21) and obs.obs_datetime between  Date('" + formatDate2(convertToSQLDate(startDate)) + "')  and  Date('" + formatDate2(convertToSQLDate(endDate)) + "') and obs.location_id=" + locationID + "\n"
                + "order by `patient`.`patient_id`, encounter.encounter_datetime,encounter.encounter_id,`encounter`.`form_id`";
        Statement stmt = null;
        ResultSet rs = null;
        int count = 0;
        Obs obs = null;
        int patient_id = 0;
        Concept c = null;
        User usr = null;
        Person prs = null;
        try {
            stmt = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            rs = stmt.executeQuery(sql_text);
            String[] headers = {"PEPFAR_ID", "HOSP_ID", "VISIT_DATE", "PMM_FORM", "TEST_NAME", "RESULT", "ENTERED_BY", "DATE_ENTERED", "PROVIDER", "UUID", "LOCATION", "OBS_ID", "PATIENT_ID", "ENCOUNTER_ID", "CONCEPT_ID", "LOCATION_ID", "CREATOR"};
            mgr.createCSVWriter(file.getAbsolutePath());
            mgr.writeCSVHeaders(headers);
            //screen.updateMinMaxProgress(0, 300000);
            screen.setState(true);
            while (rs.next()) {
                obs = new Obs();
                patient_id = rs.getInt("person_id");
                obs.setObsID(rs.getInt("obs_id"));
                obs.setPatientID(patient_id);
                obs.setPepfarID(pepfarDictionary.get(patient_id));
                obs.setHospID(hospIDDictionary.get(patient_id));
                obs.setVisitDate(rs.getDate("obs_datetime"));
                obs.setFormID(rs.getInt("form_id"));
                obs.setFormName(formNamesDictionary.get(rs.getInt("form_id")));
                obs.setEncounterID(rs.getInt("encounter_id"));
                obs.setConceptID(rs.getInt("concept_id"));
                c = conceptDictionary.get(rs.getInt("concept_id"));
                if (c != null) {
                    obs.setVariableName(c.getConceptName());
                }
                if (isValueCodedConcept(rs.getInt("concept_id"))) {
                    c = conceptDictionary.get(rs.getInt("value_coded"));
                    if (c != null) {
                        obs.setVariableValue(c.getConceptName());
                    }

                } else {
                    obs.setVariableValue(rs.getString("variable_value"));
                }
                obs.setCreator(rs.getInt("creator"));
                usr = userDictionary.get(rs.getInt("creator"));
                if (usr != null) {
                    obs.setEnteredBy(usr.getFullName());
                }
                obs.setProviderID(rs.getInt("provider_id"));
                prs = personDictionary.get(rs.getInt("provider_id"));
                //usr=userDictionary.get(prs)
                if (prs != null) {
                    obs.setProvider(namesDictionary.get(prs.getPerson_id()).getFullName());
                }
                obs.setDateEntered(rs.getDate("date_created"));

                obs.setUuid(rs.getString("uuid"));
                obs.setLocationID(rs.getInt("location_id"));
                obs.setLocationName(locationDictionary.get(rs.getInt("location_id")));
                obs.setVoided((rs.getInt("voided")));
                mgr.writeToLab(obs);
                count++;
                screen.updateStatus("Writing lab records.....Please wait " + count);
                screen.updateProgress(count);
            }
            screen.updateStatus("Lab records completed " + count);
            screen.setState(false);
            mgr.closeCSVWriter();
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            cleanUp(rs, stmt);
        }

    }

    public void runDWHAppointments(File file) {
        screen.updateStatus("Writing Appointment xml file.....Please wait.");
        String sql_text = "select \n"
                + "appointment_id,\n"
                + "patient_id,\n"
                + "location_id,\n"
                + "provider_id,\n"
                + "DATE_FORMAT(appointment_date,'%Y-%m-%d') appointment_date,\n"
                + "phone_number,\n"
                + "email,\n"
                + "other_contact_phone,\n"
                + "reason,\n"
                + "note,\n"
                + "attended,\n"
                + "voided\n"
                + "from appointment;";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            rs = stmt.executeQuery(sql_text);
            //String[] data = new String[12];
            //String aptfileName = file.getAbsolutePath() + "AppointmentDWH" + new SimpleDateFormat("yyyyMMddhhmm'.xml'").format(new Date());
            String aptfileName = "appointment.xml";
            File aptfile = new File(aptfileName);
            //csvWriter = new CSVWriter(new FileWriter(aptfile));
            zipFileEntryNames.add(aptfileName);
            String[] headers = {
                "appointment_id",
                "patient_id",
                "location_id",
                "provider_id",
                "appointment_date",
                "phone_number",
                "email",
                "other_contact_phone",
                "reason",
                "note",
                "attended",
                "voided"
            };
            mgr.createXMLWriter(aptfileName);
            mgr.openXMLDocument();
            //mgr.writeXMLHeader("appointments");
            mgr.writeXMLHeader("appointments", "schema/appointment.xsd");
            int count = 0;
            Appointment apt = null;
            int patient_id = 0;
            int provider_id = 0;
            int location_id = 0;
            PersonName pn = null;
            while (rs.next()) {
                apt = new Appointment();
                apt.setAppointmentID(rs.getInt("appointment_id"));
                patient_id = rs.getInt("patient_id");
                apt.setPatientID(patient_id);
                apt.setPepfarID(pepfarDictionary.get(patient_id));
                apt.setHospID(hospIDDictionary.get(patient_id));
                location_id = rs.getInt("location_id");
                provider_id = rs.getInt("provider_id");
                apt.setLocationID(location_id);
                apt.setProviderID(provider_id);
                apt.setLocationName(locationDictionary.get(location_id));
                pn = namesDictionary.get(provider_id);
                if (pn != null) {
                    apt.setProviderName(pn.getFullName());
                }

                apt.setAppointmentDate(rs.getDate("appointment_date"));
                apt.setPhoneNumber(rs.getString("phone_number"));
                apt.setEmail(rs.getString("email"));
                apt.setOtherPhoneNumbers(rs.getString("other_contact_phone"));
                apt.setReason(rs.getString("reason"));
                apt.setNote(rs.getString("note"));
                apt.setAttended(rs.getString("attended"));
                apt.setVoided(rs.getInt("voided"));
                count++;
                mgr.writeToXML(apt);
                screen.updateStatus("Writing Appointment.xml....Please wait " + count);

            }
            screen.updateStatus("Appointment.xml file completed");
            mgr.endXMLDocument();
            mgr.closeXMLWriter();
            rs.close();
            stmt.close();

        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } catch (XMLStreamException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            cleanUp(rs, stmt);
        }
    }

    public void cleanUp(ResultSet rs, Statement stmt) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    private void loadARTStartDates() {
        artStartDateDictionary = new HashMap<Integer, Date>();
        screen.updateStatus("Loading ART Date Dictionary...Please wait");
        /*String sql_text = "select  obs.person_id, obs.value_datetime from obs left join encounter on(obs.encounter_id=encounter.encounter_id) where concept_id=863 and obs.voided=0 and encounter.voided=0 and encounter.form_id=1 \n"
                + "order by person_id ASC, obs.obs_datetime DESC ;";*/
        String sql_text = "select encounter.patient_id,obs.value_datetime from encounter inner join obs on(obs.encounter_id=encounter.encounter_id and encounter.voided=0 and obs.concept_id=863 and encounter.form_id IN (22,46))";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = prepareQuery(sql_text);
            rs = ps.executeQuery();
            while (rs.next()) {
                artStartDateDictionary.put(rs.getInt("patient_id"), rs.getDate("value_datetime"));
            }
            //rs.close();
            //ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            screen.updateStatus(ex.getMessage());
        } finally {
            cleanUp(rs, ps);
        }
    }

    public String codeDrugs(String drugName) {
        String codedName = "";
        drugName = drugName.toUpperCase();
        if (drugName != null && !drugName.isEmpty()) {
            if (drugName.contains("NEVIRAPINE")) {
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
            }
            if (codedName.endsWith("/")) {
                codedName = codedName.substring(0, codedName.length() - 1);
            }

        }
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

    public static boolean isEquivalent(String code1, String code2) {
        boolean ans = false;
        String[] arr1 = code1.split("/");
        String[] arr2 = StringUtils.replace(StringUtils.trim(code2), "/", "").split("-");
        Arrays.sort(arr1);
        Arrays.sort(arr2);
        return Arrays.equals(arr1, arr2);

    }

    public static String sortRegimen(String code) {
        String[] codeArr = code.split("/");
        Arrays.sort(codeArr);
        return StringUtils.join(codeArr, "/");
    }

    public void loadMaps() {
        map1 = new HashMap<String, Integer>();
        map1.put("NVP/AZT/3TC", 1);
        map1.put("NVP/TDF/FTC", 2);
        map1.put("NVP/TDF/3TC", 2);
        map1.put("NVP/D4T/3TC", 3);
        map1.put("NVP/ABC/3TC", 4);
        map1.put("EFV/AZT/3TC", 5);
        map1.put("EFV/TDF/FTC", 6);
        map1.put("EFV/TDF/3TC", 6);
        map1.put("EFV/D4T/3TC", 7);
        map1.put("EFV/ABC/3TC", 8);
        map1.put("ABC/AZT/3TC", 9);
        map1.put("LPVr/TDF/FTC", 11);
        map1.put("LPVr/TDF/3TC", 11);
        map1.put("LPVr/AZT/3TC", 12);
        map1.put("LPVr/AZT/FTC", 12);
        map1.put("LPVr/TDF/AZT/FTC", 13);
        map1.put("LPVr/TDF/AZT/3TC", 13);
        map1.put("LPVr/D4T/3TC", 14);
        map1.put("LPVr/D4T/FTC", 14);
        map1.put("LPVr/ABC/3TC", 15);
        map1.put("LPVr/ABC/FTC", 15);
        map1.put("SQVr/TDF/FTC", 16);
        map1.put("SQVr/TDF/3TC", 16);
        map1.put("SQVr/AZT/3TC", 17);
        map1.put("SQVr/AZT/FTC", 17);
        map1.put("SQVr/TDF/AZT/FTC", 18);
        map1.put("SQVr/TDF/AZT/3TC", 18);
        map1.put("IDVr/TDF/FTC", 19);
        map1.put("IDVr/TDF/3TC", 19);
        map1.put("IDVr/AZT/3TC", 20);
        map1.put("IDVr/AZT/FTC", 20);
        map1.put("IDVr/TDF/AZT/3TC", 21);
        map1.put("IDVr/TDF/AZT/FTC", 21);
        map1.put("DDI/SQVr/TDF", 22);
        map1.put("DDI/IDVr/TDF", 22);
        map1.put("ABC/DDI/LPVr", 22);
        map1.put("LPVr/AZT/ABC", 23);
        map1.put("ATVr/AZT/3TC", 24);
        map1.put("ATVr/AZT/FTC", 24);
        map1.put("ATVr/TDF/3TC", 25);
        map1.put("ATVr/TDF/FTC", 25);
        map1.put("3TC/ABC/ATVr", 22);
        map1.put("ATVr/AZT/FTC/TDF", 26);
        map1.put("ATVr/AZT/3TC/TDF", 26);
        map1.put("3TC/TDF/AZT/ATVr", 26);
        map1.put("IDVr/3TC/D4T", 26);
        map1.put("IDVr/AZT/3TC", 20);
        map1.put("AZT/3TC/TDF", 10);
        map1.put("SQVr/D4T/3TC", 26);
        map1.put("SQVr/ABC/TDF", 26);

    }

    public int getCode(String codedDrug) {
        int code = -1;
        if (StringUtils.isNotEmpty(codedDrug)) {
            Set<String> set = map1.keySet();
            for (String ele : set) {
                if (isEquivalent(ele, codedDrug)) {
                    code = map1.get(ele);
                }
            }
        }

        return code;
    }

    public boolean isARV(String drugName) {
        boolean ans = false;
        int[] arvConceptArr = {
            17, 18, 23, 952, 953, 954, 955,
            956, 957, 958, 959, 960, 1184,
            1185, 1186, 1187, 1188, 1189,
            1190, 1191, 1213, 1219, 1220,
            1221, 1222, 1223, 1224, 1225,
            1226, 1227, 1228, 1229, 1230,
            1231, 1232, 1233, 1234, 1235,
            1237, 1238, 1528, 7777748, 7777749,
            7777751};
        String name = "";
        for (int ele : arvConceptArr) {
            name = drugDictionary.get(ele);
            if (name != null && drugName.toUpperCase().contains(name.toUpperCase())) {
                ans = true;
                return ans;
            }
        }
        return ans;
    }

    public String getRegimenLine(String codedDrug) {
        String line = "";
        if (codedDrug.contains("LPVr") || codedDrug.contains("SQVr") || codedDrug.contains("IDVr") || codedDrug.contains("DDI") || codedDrug.contains("ATVr")) {
            line = "2nd line";
        } else {
            line = "1st line";
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

    public List<BiometricInfo> getBiometricInfoForPatient(int patientID) {
        String sql_text = "select\n"
                + "     biometricInfo_Id,\n"
                + "	patient_Id,\n"
                + "	template,\n"
                + "	fingerPosition,\n"
                + "	creator,\n"
                + "	date_created\n"
                + "	from biometricinfo \n"
                + "	where patient_Id=?";
        PreparedStatement ps = prepareQuery(sql_text);
        ResultSet rs = null;
        List<BiometricInfo> biometricInfoList = new ArrayList<BiometricInfo>();
        try {
            ps.setInt(1, patientID);
            rs = ps.executeQuery();
            while (rs.next()) {
                biometricInfoList.add(createBiometricInfo(rs));
            }
            cleanUp(rs, ps);
        } catch (SQLException ex) {
            handleException(ex);
        } finally {
            cleanUp(rs, ps);
        }
        return biometricInfoList;
    }

    public BiometricInfo createBiometricInfo(ResultSet rs) throws SQLException {
        BiometricInfo biometricInfo = new BiometricInfo();
        biometricInfo.setBiometricInfoID(rs.getInt("biometricInfo_Id"));
        biometricInfo.setPatientID(rs.getInt("patient_Id"));
        biometricInfo.setTemplate(rs.getString("template"));
        biometricInfo.setFingerPosition(rs.getString("fingerPosition"));
        biometricInfo.setCreator(rs.getInt("creator"));
        biometricInfo.setDateCreated(rs.getDate("date_created"));
        return biometricInfo;
    }

    public int getPatientRegimenSize(Date startDate, Date endDate) {
        String sql_text = "select  count(distinct  start_date,patient_id)  as num_rows from orders"
                + " where voided=0 and orders.start_date between ? and ? "
                + "and orders.concept_id in(\n"
                + "17,18,23,952,953,954,955,956,962,961,\n"
                + "957,958,959,960,1184,1185,1186,1187,\n"
                + "1188,1189,1190,1191,1219,1220,1221,\n"
                + "1222,1223,1224,1225,1226,1227,1228,\n"
                + "1229,1230,1231,1232,1233,1234,1533,\n"
                + "1235,1236,1237,1238,1528,7777747,\n"
                + "7777748,7777749,7777750,7777751) \n"
                + "or orders.concept_id in (select concept_id from concept_set where concept_set=27 and concept_id is not null)\n"
                + "or orders.concept_id in (select answer_concept from concept_answer where concept_id=1606 and answer_concept is not null);";
        ResultSet rs = null;
        int size = 0;
        PreparedStatement ps = prepareQuery(sql_text);
        try {
            ps.setDate(1, convertToSQLDate(startDate));
            ps.setDate(2, convertToSQLDate(endDate));
            rs = ps.executeQuery();

            while (rs.next()) {
                size = rs.getInt("num_rows");
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return size;

    }

    public double getValueNumeric(int conceptID, HashMap<Integer, Obs> obsMap) {
        double ans = 0.0;
        Obs obs = null;
        obs = obsMap.get(conceptID);
        if (obs != null) {
            ans = obs.getValueNumeric();
        }
        return ans;
    }

    public Date getValueDate(int conceptID, HashMap<Integer, Obs> obsMap) {
        Date dateVal = null;
        Obs obs = null;
        obs = obsMap.get(conceptID);
        if (obs != null) {
            dateVal = obs.getValueDate();
        }
        return dateVal;
    }

    public String getValueStringOrCoded(int conceptID, HashMap<Integer, Obs> obsMap) {
        String val = null;
        Obs obs = null;
        obs = obsMap.get(conceptID);
        if (obs != null) {
            val = obs.getVariableValue();
        }
        return val;
    }

    public Regimen constructRegimen(Encounter enc, HashMap<Integer, Obs> obsMap) {
        Regimen regimen = new Regimen();
        regimen.setPatientID(enc.getPatientID());
        regimen.setFormID(enc.getFormID());
        regimen.setEncounterID(enc.getEncounterID());
        regimen.setCreatoriD(enc.getCreator());
        regimen.setProviderID(enc.getProviderID());
        regimen.setLocationID(enc.getLocationID());
        regimen.setPepfarID(pepfarDictionary.get(enc.getPatientID()));
        regimen.setHospID(hospIDDictionary.get(enc.getPatientID()));
        regimen.setVisitDate(enc.getEncounterDate());
        regimen.setLocation(locationDictionary.get(enc.getLocationID()));
        regimen.setPmmForm(formNamesDictionary.get(enc.getFormID()));
        regimen.setEnteredBy(userDictionary.get(enc.getCreator()).getFullName());
        regimen.setUuid(enc.getUuid());
        User usr = userDictionary.get(enc.getProviderID());
        String providerName = "";
        if (usr != null) {
            providerName = usr.getFullName();
            regimen.setProvider(providerName);
        }

        regimen.setDateEntered(enc.getDateCreated());
        regimen.setDateChanged(enc.getDateChanged());
        regimen.setVisitType(getValueStringOrCoded(1537, obsMap));
        regimen.setPickupReason(getValueStringOrCoded(1542, obsMap));
        regimen.setTreatmentType(getValueStringOrCoded(1256, obsMap));
        regimen.setRegimenLine(getValueStringOrCoded(7778111, obsMap));
        regimen.setFirstLine(getValueStringOrCoded(7778108, obsMap));
        regimen.setSecondLine(getValueStringOrCoded(7778109, obsMap));
        regimen.setOtherLine(getValueStringOrCoded(7778410, obsMap));
        regimen.setOrderedBy(getValueStringOrCoded(7778009, obsMap));
        regimen.setDateOrdered(getValueDate(7778015, obsMap));
        regimen.setDateCounseled(getValueDate(7778443, obsMap));
        regimen.setCounseledBy(getValueStringOrCoded(7778442, obsMap));
        String regimenStr = "";
        if (regimen.getFirstLine() != null && !regimen.getFirstLine().isEmpty()) {
            regimenStr = regimen.getFirstLine();
        } else if (regimen.getSecondLine() != null && !regimen.getSecondLine().isEmpty()) {
            regimenStr = regimen.getSecondLine();
        }
        regimen.setRegimenCode(getCode(regimenStr));
        return regimen;
    }

    public void runPatientRegimenDWH(Date startDate, Date endDate) {
        screen.updateStatus("Writing regimens.xml....Please wait");
        Integer[] formIDArr = {46, 53, 86};
        Set<Integer> idSet = new HashSet<Integer>(Arrays.asList(formIDArr));
        ArrayList<Encounter> encList = getEncounterForDate(startDate, endDate, idSet);
        HashMap<Integer, Obs> obsMap = new HashMap<Integer, Obs>();
        int count = 0;
        Regimen regimen = null;
        try {
            String fileName = "regimens.xml";
            mgr.createXMLWriter(fileName);
            mgr.openXMLDocument();
            mgr.writeXMLHeader("regimens", "schema/regimens.xsd");
            zipFileEntryNames.add(fileName);
            for (Encounter enc : encList) {
                obsMap = getObsForEncounter(enc.getEncounterID());
                regimen = constructRegimen(enc, obsMap);
                mgr.writeToXML(regimen);
                screen.updateStatus("regimen.xml...Please wait" + count);
                count++;
            }
            mgr.endXMLDocument();
            mgr.closeXMLWriter();
        } catch (IOException ex) {
            ex.printStackTrace();
            screen.showError(ex.getMessage());
        } catch (XMLStreamException ex) {
            ex.printStackTrace();
            screen.showError(ex.getMessage());
        } finally {
            try {
                mgr.closeXMLWriter();
            } catch (XMLStreamException xes) {
                handleException(xes);
            }
        }
        /*String sql_text = "select \n"
                + "   obs.person_id as patient_id,\n"
                + "   encounter.form_id as form_id,\n"
                + "   encounter.encounter_id,\n"
                + "   encounter.creator,\n"
                + "   encounter.provider_id,\n"
                + "   encounter.location_id,\n"
                + "   encounter.uuid,\n"
                + "   encounter.date_changed,\n"
                + "   pid1.identifier as pepfar_id,\n"
                + "   pid2.identifier as hosp_id,\n"
                + "   encounter.encounter_datetime as visit_date,\n"
                + "   location.name as location,\n"
                + "   form.name as pmm_form,\n"
                + "  CONCAT(pn1.given_name,'  ',pn1.family_name) as enteredBy,\n"
                + "  CONCAT(pn2.given_name,'  ',pn2.family_name) as provider,\n"
                + "  encounter.date_created as dateCreated\n"
                + " ,MAX(IF(obs.concept_id= 1537, cn1.name, NULL)) as  `visitType`\n"
                + " ,MAX(IF(obs.concept_id= 1542, cn1.name, NULL)) as  `pickupReason`\n"
                + " ,MAX(IF(obs.concept_id= 1256, cn1.name, NULL)) as  `treatmentType`\n"
                + " ,MAX(IF(obs.concept_id= 7778111, cn1.name, NULL)) as  `regimenLine`\n"
                + " ,MAX(IF(obs.concept_id= 7778108, cn1.name, NULL)) as  `firstLine`\n"
                + " ,MAX(IF(obs.concept_id= 7778109, cn1.name, NULL)) as  `secondLine`\n"
                + " ,MAX(IF(obs.concept_id= 7778410, cn1.name, NULL)) as  `otherRegimen`\n"
                + " ,MAX(IF(obs.concept_id= 7778009, obs.value_text, NULL)) as  `orderedBy`\n"
                + " ,MAX(IF(obs.concept_id= 7778015, obs.value_datetime, NULL)) as  `dateOrdered`\n"
                + " ,MAX(IF(obs.concept_id= 7778442, obs.value_text, NULL)) as  `counseledBy`\n"
                + " ,MAX(IF(obs.concept_id= 7778444, obs.value_datetime, NULL)) as  `dateCounseled`\n"
                + "FROM obs \n"
                + "left join patient_identifier pid1 on(pid1.patient_id=obs.person_id and pid1.identifier_type=4 and pid1.voided=0)\n"
                + "left join patient_identifier pid2 on(pid2.patient_id=obs.person_id and pid2.identifier_type=3 and pid2.voided=0)\n"
                + "left join encounter on(encounter.encounter_id=obs.encounter_id and encounter.voided=0)\n"
                + "left join location on(location.location_id=encounter.location_id)\n"
                + "left join users on(users.user_id=encounter.creator)\n"
                + "left join person_name pn1 on(pn1.person_id=users.person_id)\n"
                + "left join person_name pn2 on(pn2.person_id=encounter.provider_id)\n"
                + "left join form on(form.form_id=encounter.form_id)\n"
                + "left join concept_name cn1 on(cn1.concept_id=obs.value_coded and cn1.locale='en' and cn1.locale_preferred=1)\n"
                + "where encounter.form_id in (46,53) and encounter.voided=0 and obs.voided=0\n"
                + "GROUP BY  obs.person_id,obs.encounter_id";*/
 /*Statement stmt = null;
        ResultSet rs = null;
        Regimen regimen = null;
        int patient_id = 0;
        int count = 0;
        String[] headers = {"PATIENT_ID", "PEPFAR_ID", "HOSP_ID", "START_DATE", "STOP_DATE", "DRUGS", "REGIMEN", "REGIMENLINE", "ENTERED_BY", "DATE_ENTERED", "CREATOR_ID"};
        try {

            String fileName = "regimens.xml";
            mgr.createXMLWriter(fileName);
            mgr.openXMLDocument();
            mgr.writeXMLHeader("regimens", "schema/regimens.xsd");
            zipFileEntryNames.add(fileName);
            stmt = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            rs = stmt.executeQuery(sql_text);
            while (rs.next()) {
                regimen = new Regimen();
                patient_id = rs.getInt("patient_id");
                regimen.setPatientID(patient_id);
                regimen.setFormID(rs.getInt("form_id"));
                regimen.setEncounterID(rs.getInt("encounter_id"));
                regimen.setCreatoriD(rs.getInt("creator"));
                regimen.setProviderID(rs.getInt("provider_id"));
                regimen.setLocationID(rs.getInt("location_id"));
                regimen.setPepfarID(pepfarDictionary.get(patient_id));
                regimen.setHospID(hospIDDictionary.get(patient_id));
                regimen.setVisitDate(rs.getDate("visit_date"));
                regimen.setLocation(rs.getString("location"));
                regimen.setPmmForm(rs.getString("pmm_form"));
                regimen.setEnteredBy(rs.getString("enteredBy"));
                regimen.setUuid(rs.getString("uuid"));
                regimen.setProvider(rs.getString("provider"));
                regimen.setDateEntered(rs.getDate("dateCreated"));
                regimen.setDateChanged(rs.getDate("date_changed"));
                regimen.setVisitType(rs.getString("visitType"));
                regimen.setPickupReason(rs.getString("pickupReason"));
                regimen.setTreatmentType(rs.getString("treatmentType"));
                regimen.setRegimenLine(rs.getString("regimenLine"));
                regimen.setFirstLine(rs.getString("firstLine"));
                regimen.setSecondLine(rs.getString("secondLine"));
                regimen.setOtherLine(rs.getString("otherRegimen"));
                regimen.setOrderedBy(rs.getString("orderedBy"));
                regimen.setDateOrdered(rs.getDate("dateOrdered"));
                regimen.setDateCounseled(rs.getDate("dateCounseled"));
                regimen.setCounseledBy(rs.getString("counseledBy"));

                String regimenStr = "";
                if (regimen.getFirstLine() != null && !regimen.getFirstLine().isEmpty()) {
                    regimenStr = regimen.getFirstLine();
                } else if (regimen.getSecondLine() != null && !regimen.getSecondLine().isEmpty()) {
                    regimenStr = regimen.getSecondLine();
                }
                regimen.setRegimenCode(getCode(regimenStr));
                count++;
                screen.updateStatus("Writing regimen.xml...Please wait " + count);
                mgr.writeToXML(regimen);
            }

            mgr.endXMLDocument();
            mgr.closeXMLWriter();

        } catch (SQLException ex) {
            ex.printStackTrace();
            screen.showError(ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
            screen.showError(ex.getMessage());
        } catch (XMLStreamException ex) {
            ex.printStackTrace();
            screen.showError(ex.getMessage());
        } finally {
            cleanUp(rs, stmt);
        }*/

    }

    public void runPatientRegimenDWH2() {
        screen.updateStatus("Writing regimens.xml....Please wait");

        String sql_text = "select\n"
                + "                orders.order_id,\n"
                + "                orders.patient_id,\n"
                + "                GROUP_CONCAT(drug.`name` SEPARATOR '/') as regimen_name,\n"
                + "                orders.start_date ,\n"
                + "                CAST(GROUP_CONCAT(orders.concept_id SEPARATOR ',') AS CHAR) as concept_ids,\n"
                + "                orders.discontinued_date ,\n"
                + "                orders.concept_id,\n"
                + "                orders.creator,\n"
                + "                orders.date_created,\n"
                + "                orders.voided\n"
                + "                from\n"
                + "                orders\n"
                + "                inner join drug_order on(drug_order.order_id=orders.order_id and orders.voided=0)\n"
                + "                left join drug on(drug.drug_id=drug_order.drug_inventory_id)\n"
                + "                inner join patient on(patient.patient_id=orders.patient_id and patient.voided=0)\n"
                + "                where orders.voided=0\n"
                + "                GROUP BY orders.patient_id,orders.start_date ORDER BY patient_id asc ,orders.start_date\n"
                + "";
        Statement stmt = null;
        ResultSet rs = null;
        PatientRegimen order = null;
        User usr = null;
        int patient_id = 0;
        int creator_id = 0;
        int count = 0;
        int num_rows = 0;
        String drugName = "";
        String regimenCode = "";
        String[] headers = {"PATIENT_ID", "PEPFAR_ID", "HOSP_ID", "START_DATE", "STOP_DATE", "DRUGS", "REGIMEN", "REGIMENLINE", "ENTERED_BY", "DATE_ENTERED", "CREATOR_ID"};
        try {

            String fileName = "regimen.xml";
            mgr.createXMLWriter(fileName);
            mgr.openXMLDocument();

            mgr.writeXMLHeader("regimens");

            zipFileEntryNames.add(fileName);

            PreparedStatement ps = prepareQuery(sql_text);
            rs = ps.executeQuery();

            while (rs.next()) {
                order = new PatientRegimen();
                patient_id = rs.getInt("patient_id");
                order.setPatientID(patient_id);
                //order.setOrderID(rs.getInt("order_id"));
                order.setPepfarID(pepfarDictionary.get(patient_id));
                order.setHospID(hospIDDictionary.get(patient_id));
                order.setStartDate(rs.getDate("start_date"));
                order.setStopDate(rs.getDate("discontinued_date"));
                drugName = rs.getString("regimen_name");

                if (drugName != null && !drugName.isEmpty()) {

                    regimenCode = codeDrugs(drugName);
                    order.setRegimenName(regimenCode);
                    order.setDrugName(drugName);
                    if (regimenCode != null && !regimenCode.isEmpty()) {
                        order.setCode(String.valueOf(getCode(regimenCode)));
                        order.setRegimenLine(getRegimenLine(regimenCode));
                    }
                }
                creator_id = rs.getInt("creator");
                usr = userDictionary.get(creator_id);
                if (usr != null) {
                    order.setEnteredBy(usr.getFullName());
                }
                order.setDateEntered(rs.getDate("date_created"));

                order.setCreator(creator_id);

                //mgr.writeToXML(order);
                count++;
                screen.updateStatus("Writing regimen.xml...Please wait " + count);

            }

            ps.close();
            rs.close();
            mgr.endXMLDocument();
            mgr.closeXMLWriter();
            //mgr.closeCSVWriter();
        } catch (SQLException ex) {
            ex.printStackTrace();
            screen.showError(ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
            screen.showError(ex.getMessage());
        } catch (XMLStreamException ex) {
            ex.printStackTrace();
            screen.showError(ex.getMessage());
        } finally {
            cleanUp(rs, stmt);
        }

    }

    public String getConceptNames(String ids) {
        String names = "";
        String[] idNoStr = null;
        int[] idNo = null;
        String conceptName = "";
        if (!ids.isEmpty()) {
            idNoStr = ids.split(",");
            idNo = new int[idNoStr.length];
            for (int i = 0; i < idNoStr.length; i++) {
                idNo[i] = Integer.parseInt(idNoStr[i]);
            }

            for (int ele : idNo) {
                conceptName = conceptDictionary.get(ele).getConceptName();
                names += conceptName + "/";
            }
            if (names.endsWith("/")) {
                names = names.substring(0, names.length() - 1);
            }
        }
        return names;
    }

    public void exportPatientRegimenCSV(Date startDate, Date endDate, int location_id, File file) {
        screen.updateStatus("Writing " + file.getName() + " ...Please wait");

        String sql_text = "select\n"
                + "orders.order_id,\n"
                + "orders.patient_id,\n"
                + "GROUP_CONCAT(drug.`name` SEPARATOR '/') as regimen_name,\n"
                + "orders.start_date ,\n"
                + "orders.discontinued_date ,\n"
                + "orders.creator,\n"
                + "orders.date_created,\n"
                + "orders.voided\n"
                + "from\n"
                + "orders\n"
                + "inner join drug_order on(drug_order.order_id=orders.order_id and orders.voided=0)\n"
                + "inner join drug on(drug.drug_id=drug_order.drug_inventory_id)\n"
                + "inner join patient on(patient.patient_id=orders.patient_id and patient.voided=0)\n"
                + "where orders.voided=0 and orders.start_date between Date('" + formatDate2(convertToSQLDate(startDate)) + "') and Date('" + formatDate2(convertToSQLDate(endDate)) + "') \n"
                + "GROUP BY orders.patient_id, orders.start_date ORDER BY patient_id asc ,orders.start_date ;";

        Statement stmt = null;
        ResultSet rs = null;
        PatientRegimen order = null;
        User usr = null;
        int patient_id = 0;
        int creator_id = 0;
        int count = 0;
        int num_rows = 0;
        String drugConceptIDs = "";

        String drugName = "";
        String regimenCode = "";
        String[] headers = {"ORDER_ID", "PATIENT_ID", "PEPFAR_ID", "HOSP_ID", "START_DATE", "STOP_DATE", "DRUGS", "REGIMEN", "REGIMEN_CODE", "REGIMENLINE", "ENTERED_BY", "DATE_ENTERED", "CREATOR_ID"};
        try {
            stmt = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);

            mgr.createCSVWriter(file.getAbsolutePath());
            mgr.writeCSVHeaders(headers);

            rs = stmt.executeQuery(sql_text);

            screen.setState(true);
            while (rs.next()) {
                order = new PatientRegimen();
                patient_id = rs.getInt("patient_id");
                order.setOrderID(rs.getInt("order_id"));
                order.setPatientID(patient_id);
                order.setPepfarID(pepfarDictionary.get(patient_id));
                order.setHospID(hospIDDictionary.get(patient_id));
                order.setStartDate(rs.getDate("start_date"));
                order.setStopDate(rs.getDate("discontinued_date"));
                drugName = rs.getString("regimen_name");
                if (drugName != null && !drugName.isEmpty()) {
                    order.setDrugName(drugName);
                    regimenCode = codeDrugs(drugName);
                    order.setRegimenName(regimenCode);
                    if (!regimenCode.isEmpty()) {
                        order.setCode(String.valueOf(getCode(regimenCode)));
                    }
                    order.setRegimenLine(getRegimenLine(regimenCode));
                }
                creator_id = rs.getInt("creator");
                usr = userDictionary.get(creator_id);
                if (usr != null) {
                    order.setEnteredBy(usr.getFullName());
                }
                order.setDateEntered(rs.getDate("date_created"));

                order.setCreator(creator_id);

                //mgr.writeToCSV(order);
                count++;
                screen.updateStatus("Writing " + file.getName() + "...Please wait " + count);

            }

            screen.setState(false);
            screen.updateProgress(100);
            stmt.close();
            rs.close();

            mgr.closeCSVWriter();
            screen.updateStatus("Export to " + file.getName() + " Completed thanks");
        } catch (SQLException ex) {
            ex.printStackTrace();
            screen.showError(ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
            screen.showError(ex.getMessage());
        } finally {
            cleanUp(rs, stmt);
        }

    }

    public java.sql.Date convertToSQLDate(java.util.Date olddate) {
        java.sql.Date sqlDate = new java.sql.Date(olddate.getTime());
        return sqlDate;
    }

    public void runDWHFormByForm(File file, Date startDate, Date endDate) {
        screen.updateStatus("Form by form xml export started...please wait");
        String sql_text = " select \n"
                + "`obs`.`obs_id`,\n"
                + "`obs`.`person_id`,\n"
                + "`obs`.`obs_datetime`,\n"
                + "`obs`.`concept_id`,\n"
                + "obs.value_numeric,\n"
                + "obs.value_coded,\n"
                + "obs.value_text,\n"
                + "obs.value_datetime,\n"
                + "obs.value_boolean,\n"
                + "cast(coalesce(obs.value_coded,`obs`.`value_numeric`,`obs`.`value_datetime`,`obs`.`value_text`) as char charset utf8) AS `variable_value`,\n"
                + "`obs`.`date_created`,\n"
                + "encounter.date_changed,\n"
                + "encounter.date_voided,\n"
                + "encounter.changed_by,\n"
                + "obs.voided,\n"
                + "encounter.voided_by,\n"
                + "`obs`.`creator` AS `creator`,\n"
                + "`obs`.`encounter_id` AS `encounter_id`,\n"
                + "`obs`.`obs_group_id` AS `obs_group_id`,\n"
                + "`obs`.`uuid`,\n"
                + "`encounter`.`form_id` AS `form_id`,\n"
                + "`encounter`.`provider_id` AS `provider_id`,\n"
                + "`obs`.`location_id` AS `location_id` \n"
                + "from `obs` inner join `patient` on(`patient`.`patient_id` = `obs`.`person_id`)\n"
                + " inner join `encounter` on(`encounter`.`encounter_id` = `obs`.`encounter_id`) where (encounter.encounter_datetime BETWEEN '" + convertToSQLDate(startDate) + "' AND '" + convertToSQLDate(endDate) + "') OR (encounter.date_created BETWEEN '" + convertToSQLDate(startDate) + "' AND '" + convertToSQLDate(endDate) + "')";

        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);

            String[] data = new String[17];
            String obsfileName = "obs.xml";
            File obsfile = new File(obsfileName);
            mgr.createXMLWriter(obsfileName);
            mgr.openXMLDocument();
            zipFileEntryNames.add(obsfileName);
            String[] headers = {
                "OBS_ID",
                "PATIENT_ID",
                "ENCOUNTER_ID",
                "PEPFAR_ID",
                "HOSP_ID",
                "VISIT_DATE",
                "PMM_FORM",
                "CONCEPT_ID",
                "VARIABLE_NAME",
                "VARIABLE_VALUE",
                "ENTERED_BY",
                "DATE_CREATED",
                "DATE_CHANGED",
                "PROVIDER",
                "UUID",
                "LOCATION",
                "LOCATION_ID",
                "CREATOR_ID",
                "PROVIDER_ID",
                "VALUE_NUMERIC",
                "VALUE_DATETIME",
                "VALUE_CODED",
                "VALUE_TEXT",
                "VALUE_BOOL",
                "OBS_GROUP_ID",
                "VOIDED",
                "DATE_VOIDED",
                "VOIDED_BY",
                "CHANGED_BY",
                "FORM_ID",};

            int count = 0;
            int concept_id = -1;
            User enteredByUser = null;
            User providerUser = null;
            Obs obs = null;
            PersonName name = null;
            int value_coded = -9;
            double value_numeric = 0.0;
            String value_text = null;
            Date value_date = null;
            rs = stmt.executeQuery(sql_text);
            mgr.writeXMLHeader("variables", "schema/obs.xsd");
            while (rs.next()) {
                obs = new Obs();
                obs.setObsID(rs.getInt("obs_id"));//obs_id
                obs.setPatientID(rs.getInt("person_id"));//person_id
                obs.setEncounterID(rs.getInt("encounter_id"));//encounter_id
                obs.setPepfarID(pepfarDictionary.get(rs.getInt("person_id")));
                obs.setHospID(hospIDDictionary.get(rs.getInt("person_id")));
                obs.setVisitDate(rs.getDate("obs_datetime"));
                obs.setFormName(formNamesDictionary.get(rs.getInt("form_id")));
                concept_id = rs.getInt("concept_id");
                obs.setConceptID(concept_id);
                obs.setVariableName(conceptDictionary.get(rs.getInt("concept_id")).getConceptName());
                value_coded = rs.getInt("value_coded");
                obs.setValueCoded(value_coded);
                if (isValueCodedConcept(concept_id) && (value_coded != 0)) {
                    obs.setVariableValue(conceptDictionary.get(rs.getInt("value_coded")).getConceptName());
                } else {
                    obs.setVariableValue(rs.getString("variable_value"));
                }
                enteredByUser = userDictionary.get(rs.getInt("creator"));
                if (enteredByUser != null) {
                    obs.setEnteredBy(enteredByUser.getFullName());
                }
                obs.setDateEntered(rs.getDate("date_created"));
                obs.setDateChanged(rs.getDate("date_changed"));
                name = namesDictionary.get(rs.getInt("provider_id"));
                if (name != null) {
                    obs.setProvider(name.getFullName());
                }
                obs.setUuid(rs.getString("uuid"));
                obs.setLocationName(locationDictionary.get(rs.getInt("location_id")));
                obs.setLocationID(rs.getInt("location_id"));
                obs.setValueText(rs.getString("value_text"));
                obs.setValueDate(rs.getDate("value_datetime"));
                obs.setValueBoolean(rs.getBoolean("value_boolean"));
                obs.setValueNumeric(rs.getDouble("value_numeric"));
                obs.setValueCoded(rs.getInt("value_coded"));
                obs.setObsGroupID(rs.getInt("obs_group_id"));
                obs.setVoided(rs.getInt("voided"));
                obs.setVoidedBy(rs.getInt("voided_by"));
                obs.setDateVoided(rs.getDate("date_voided"));
                obs.setProviderID(rs.getInt("provider_id"));
                obs.setCreator(rs.getInt("creator"));
                obs.setFormID(rs.getInt("form_id"));
                count++;
                mgr.writeToXML(obs);
                screen.updateStatus("Writing obs.xml....Please wait " + count);
            }
            mgr.endXMLDocument();
            rs.close();
            stmt.close();
            screen.updateStatus("obs.xml file completed....Please wait");
            mgr.closeXMLWriter();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } catch (XMLStreamException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            cleanUp(rs, stmt);

        }
    }

    static public boolean isNotNullInteger(ResultSet rs, String strColName) throws SQLException {
        boolean ans = true;
        int nValue = rs.getInt(strColName);
        if (rs.wasNull()) {
            ans = false;
        }

        return ans;
    }

    public void runDWHUsers(File file) {
        screen.updateStatus("Writing users.xml file.....Please wait");
        String sql_text = "select \n"
                + "users.user_id,\n"
                + "users.person_id,\n"
                + "users.username,\n"
                + "person_name.given_name,\n"
                + "person_name.family_name,\n"
                + "person.gender,\n"
                + "users.date_created,\n"
                + "users.creator,\n"
                + "users.uuid, \n"
                + "users.date_changed,\n"
                + "users.retired,\n"
                + "users.retired_by\n"
                + "from users inner join person on(users.person_id=person.person_id and users.retired=0)\n"
                + "inner join person_name on(person_name.person_id=users.person_id and person_name.voided=0)";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            rs = stmt.executeQuery(sql_text);

            String userfileName = "users.xml";
            File userfile = new File(userfileName);
            mgr.createXMLWriter(userfileName);
            mgr.openXMLDocument();
            zipFileEntryNames.add(userfileName);
            mgr.writeXMLHeader("users", "schema/user.xsd");
            String[] headers = {
                "user_id",
                "person_id",
                "username",
                "given_name",
                "family_name",
                "gender",
                "date_created",
                "creator",
                "uuid",
                "date_changed",
                "retired",
                "retired_by"
            };

            int count = 0;
            User usr = null;
            while (rs.next()) {
                usr = new User();
                usr.setUser_id(rs.getInt("user_id"));
                usr.setPerson_id(rs.getInt("person_id"));
                usr.setUserName(rs.getString("username"));
                usr.setFirstName(rs.getString("given_name"));
                usr.setLastName(rs.getString("family_name"));
                usr.setGender(rs.getString("gender"));
                usr.setDateCreated(rs.getDate("date_created"));
                usr.setCreator(rs.getInt("creator"));
                usr.setUuid(rs.getString("uuid"));
                usr.setDateChanged(rs.getDate("date_changed"));
                usr.setRetired(rs.getInt("retired"));
                usr.setRetiredBy(rs.getInt("retired_by"));
                usr.setFullName(usr.getFirstName() + " " + usr.getLastName());
                count++;
                screen.updateStatus("Writing users.xml file...Please wait " + count);
                mgr.writeToXML(usr);

            }
            mgr.endXMLDocument();
            rs.close();
            stmt.close();
            screen.updateStatus("User export completed.....Please wait");
            mgr.closeXMLWriter();

        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } catch (XMLStreamException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            cleanUp(rs, stmt);
        }
    }

    public Set<Date> getVisitsForPatient(int patient_id, Date startDate, Date endDate, int locationID) {
        Set<Date> dateSet = new HashSet<Date>();
        String sql_text = "select distinct sinner.patient_id, sinner.visit_date from (\n"
                + "select encounter.patient_id, encounter.encounter_datetime as visit_date from encounter where encounter.voided=0 and encounter.patient_id=? and encounter.location_id=? and encounter.encounter_datetime between ? and ?\n"
                + "UNION\n"
                + "select orders.patient_id,orders.start_date as visit_date from orders where orders.voided=0 and orders.patient_id=? and orders.start_date between ? and ?) sinner group by sinner.patient_id,sinner.visit_date ORDER BY sinner.patient_id asc, sinner.visit_date asc";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = prepareQuery(sql_text);
            ps.setInt(1, patient_id);
            ps.setInt(2, locationID);
            ps.setDate(3, convertToSQLDate(startDate));
            ps.setDate(4, convertToSQLDate(endDate));
            ps.setInt(5, patient_id);
            //ps.setInt(2, locationID);
            ps.setDate(6, convertToSQLDate(startDate));
            ps.setDate(7, convertToSQLDate(endDate));
            rs = ps.executeQuery();
            while (rs.next()) {
                dateSet.add(rs.getDate("visit_date"));
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            cleanUp(rs, ps);
        }
        return dateSet;
    }

    public Encounter constructEncounter(ResultSet rs) throws SQLException {
        Encounter enc = new Encounter();
        enc.setEncounterID(rs.getInt("encounter_id"));
        enc.setEncounterType(rs.getInt("encounter_type"));
        enc.setPatientID(rs.getInt("patient_id"));
        enc.setProviderID(rs.getInt("provider_id"));
        enc.setLocationID(rs.getInt("location_id"));
        enc.setFormID(rs.getInt("form_id"));
        enc.setEncounterDate(rs.getDate("encounter_datetime"));
        enc.setCreator(rs.getInt("creator"));
        enc.setDateCreated(rs.getDate("date_created"));
        enc.setVoided(rs.getInt("voided"));
        enc.setVoidedBy(rs.getInt("voided_by"));
        enc.setDateVoided(rs.getDate("date_voided"));
        enc.setVoidedReason(rs.getString("void_reason"));
        enc.setUuid(rs.getString("uuid"));
        enc.setChangedBy(rs.getInt("changed_by"));
        enc.setDateChanged(rs.getDate("date_changed"));

        return enc;
    }

    public HashMap<Integer, Obs> getObsForEncounter(int encounterID) {
        HashMap<Integer, Obs> obsMap = new HashMap<Integer, Obs>();
        String sql_text = " select \n"
                + "`obs`.`obs_id`,\n"
                + "`obs`.`person_id`,\n"
                + "`obs`.`obs_datetime`,\n"
                + "`obs`.`concept_id`,\n"
                + "obs.value_numeric,\n"
                + "obs.value_coded,\n"
                + "obs.value_text,\n"
                + "obs.value_datetime,\n"
                + "obs.value_boolean,\n"
                + "cast(coalesce(obs.value_coded,`obs`.`value_numeric`,`obs`.`value_datetime`,`obs`.`value_text`) as char charset utf8) AS `variable_value`,\n"
                + "`obs`.`date_created`,\n"
                + "encounter.date_changed,\n"
                + "encounter.date_voided,\n"
                + "encounter.changed_by,\n"
                + "obs.voided,\n"
                + "encounter.voided_by,\n"
                + "`obs`.`creator` AS `creator`,\n"
                + "`obs`.`encounter_id` AS `encounter_id`,\n"
                + "`obs`.`obs_group_id` AS `obs_group_id`,\n"
                + "`obs`.`uuid`,\n"
                + "`encounter`.`form_id` AS `form_id`,\n"
                + "`encounter`.`provider_id` AS `provider_id`,\n"
                + "`obs`.`location_id` AS `location_id` \n"
                + "from `obs` inner join `patient` on(`patient`.`patient_id` = `obs`.`person_id`)\n"
                + " inner join `encounter` on(`encounter`.`encounter_id` = `obs`.`encounter_id` and `encounter`.`voided` = 0  and `obs`.`voided` = 0) \n"
                + "where  `obs`.`voided` = 0  and obs.encounter_id='" + encounterID + "'\n"
                + " order by `patient`.`patient_id`,encounter.encounter_datetime,encounter.encounter_id,`obs`.`obs_group_id`,`encounter`.`form_id`";

        Statement stmt = null;
        ResultSet rs = null;
        Obs obs = null;
        try {
            stmt = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            rs = stmt.executeQuery(sql_text);
            while (rs.next()) {
                obs = constructObs2(rs);
                obsMap.put(obs.getConceptID(), obs);
                //System.out.println("ConceptID "+obs.getConceptID());
            }
            rs.close();
            stmt.close();

        } catch (SQLException ex) {
            handleException(ex);
        } finally {
            cleanUp(rs, stmt);
        }
        return obsMap;
    }

    public ArrayList<model.Encounter> getEncounterForDate(Date startDate, Date endDate, Set<Integer> formIDs) {
        ArrayList<Encounter> encounterList = new ArrayList<Encounter>();
        String sql_text = "select * from encounter where form_id in(" + buildString(formIDs) + ") and voided=0";

        Statement stmt = null;
        ResultSet rs = null;
        Encounter enc = null;
        try {
            stmt = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            //ps.setDate(1, convertToSQLDate(startDate));
            //ps.setDate(2, convertToSQLDate(endDate));
            stmt.setFetchSize(Integer.MIN_VALUE);
            rs = stmt.executeQuery(sql_text);
            while (rs.next()) {
                enc = constructEncounter(rs);
                encounterList.add(enc);
                System.out.println("EncounterID " + enc.getEncounterID());
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            handleException(ex);
        } finally {
            cleanUp(rs, stmt);
        }
        return encounterList;
    }

    public HashMap<Integer, Obs> getObsForPatientForDate(int patientID, Date visitDate, int locationID) {
        HashMap<Integer, Obs> map = new HashMap<Integer, Obs>();

        String sql_text = " select \n"
                + "`obs`.`obs_id`,\n"
                + "`obs`.`person_id`,\n"
                + "`obs`.`obs_datetime`,\n"
                + "`obs`.`concept_id`,\n"
                + "obs.value_numeric,\n"
                + "obs.value_coded,\n"
                + "obs.value_text,\n"
                + "obs.value_datetime,\n"
                + "obs.value_boolean,\n"
                + "cast(coalesce(obs.value_coded,`obs`.`value_numeric`,`obs`.`value_datetime`,`obs`.`value_text`) as char charset utf8) AS `variable_value`,\n"
                + "`obs`.`date_created`,\n"
                + "encounter.date_changed,\n"
                + "encounter.date_voided,\n"
                + "encounter.changed_by,\n"
                + "obs.voided,\n"
                + "encounter.voided_by,\n"
                + "`obs`.`creator` AS `creator`,\n"
                + "`obs`.`encounter_id` AS `encounter_id`,\n"
                + "`obs`.`obs_group_id` AS `obs_group_id`,\n"
                + "`obs`.`uuid`,\n"
                + "`encounter`.`form_id` AS `form_id`,\n"
                + "`encounter`.`provider_id` AS `provider_id`,\n"
                + "`obs`.`location_id` AS `location_id` \n"
                + "from `obs` inner join `patient` on(`patient`.`patient_id` = `obs`.`person_id`)\n"
                + " inner join `encounter` on(`encounter`.`encounter_id` = `obs`.`encounter_id` and `encounter`.`voided` = 0  and `obs`.`voided` = 0) \n"
                + "where  `obs`.`voided` = 0 and obs.person_id=? and obs.obs_datetime=? and obs.location_id=? \n"
                + " order by `patient`.`patient_id`,encounter.encounter_datetime,encounter.encounter_id,`obs`.`obs_group_id`,`encounter`.`form_id`";

        PreparedStatement ps = null;
        ResultSet rs = null;
        Obs obs = null;
        int concept_id = 0;
        int value_coded = 0;
        User enteredByUser = null;
        PersonName name = null;
        try {
            ps = prepareQuery(sql_text);
            ps.setInt(1, patientID);
            ps.setDate(2, convertToSQLDate(visitDate));
            ps.setInt(3, locationID);
            rs = ps.executeQuery();
            while (rs.next()) {
                obs = new Obs();
                obs.setObsID(rs.getInt("obs_id"));//obs_id
                obs.setPatientID(rs.getInt("person_id"));//person_id
                obs.setEncounterID(rs.getInt("encounter_id"));//encounter_id
                obs.setPepfarID(pepfarDictionary.get(rs.getInt("person_id")));
                obs.setHospID(hospIDDictionary.get(rs.getInt("person_id")));
                obs.setVisitDate(rs.getDate("obs_datetime"));
                obs.setFormName(formNamesDictionary.get(rs.getInt("form_id")));
                concept_id = rs.getInt("concept_id");
                obs.setConceptID(concept_id);
                obs.setVariableName(conceptDictionary.get(rs.getInt("concept_id")).getConceptName());
                value_coded = rs.getInt("value_coded");
                obs.setValueCoded(value_coded);
                if (isValueCodedConcept(concept_id) && (value_coded != 0)) {
                    obs.setVariableValue(conceptDictionary.get(rs.getInt("value_coded")).getConceptName());
                } else {
                    obs.setVariableValue(rs.getString("variable_value"));
                }
                enteredByUser = userDictionary.get(rs.getInt("creator"));
                if (enteredByUser != null) {
                    obs.setEnteredBy(enteredByUser.getFullName());
                }
                obs.setDateEntered(rs.getDate("date_created"));
                obs.setDateChanged(rs.getDate("date_changed"));
                name = namesDictionary.get(rs.getInt("provider_id"));
                if (name != null) {
                    obs.setProvider(name.getFullName());
                }
                obs.setUuid(rs.getString("uuid"));
                obs.setLocationName(locationDictionary.get(rs.getInt("location_id")));
                obs.setLocationID(rs.getInt("location_id"));
                obs.setValueText(rs.getString("value_text"));
                obs.setValueDate(rs.getDate("value_datetime"));
                obs.setValueBoolean(rs.getBoolean("value_boolean"));
                obs.setValueNumeric(rs.getDouble("value_numeric"));
                obs.setValueCoded(rs.getInt("value_coded"));
                obs.setObsGroupID(rs.getInt("obs_group_id"));
                obs.setVoided(rs.getInt("voided"));
                obs.setVoidedBy(rs.getInt("voided_by"));
                obs.setDateVoided(rs.getDate("date_voided"));
                obs.setProviderID(rs.getInt("provider_id"));
                obs.setCreator(rs.getInt("creator"));
                obs.setFormID(rs.getInt("form_id"));
                map.put(obs.getConceptID(), obs);
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            screen.updateStatus(ex.getMessage());
        } finally {
            cleanUp(rs, ps);
        }
        return map;
    }

    public String getRegimenForPatientForVisit(int patientID, Date visitDate) {
        /*String sql_text = "select\n"
                + "                orders.order_id,\n"
                + "                orders.patient_id,\n"
                + "                GROUP_CONCAT(drug.`name` SEPARATOR '/') as regimen_name,\n"
                + "                orders.start_date ,\n"
                + "                CAST(GROUP_CONCAT(orders.concept_id SEPARATOR ',') AS CHAR) as concept_ids,\n"
                + "                orders.discontinued_date ,\n"
                + "                orders.concept_id,\n"
                + "                orders.creator,\n"
                + "                orders.date_created,\n"
                + "                orders.voided\n"
                + "                from\n"
                + "                orders\n"
                + "                inner join drug_order on(drug_order.order_id=orders.order_id and orders.voided=0)\n"
                + "                left join drug on(drug.drug_id=drug_order.drug_inventory_id)\n"
                + "                inner join patient on(patient.patient_id=orders.patient_id and patient.voided=0)\n"
                + "                where orders.voided=0 and orders.patient_id=? and orders.start_date=? \n"
                + "                GROUP BY orders.patient_id,orders.start_date ORDER BY patient_id asc ,orders.start_date\n"
                + "";*/
        String sql_text = "select \n"
                + "obs.person_id as patient_id,\n"
                + "obs.obs_datetime as visit_date,\n"
                + "encounter.form_id,\n"
                + "GROUP_CONCAT((IF(obs.concept_id=7778364,cn1.name, NULL))) as  `drug_name`\n"
                + "from obs\n"
                + "left join concept_name cn1 on(obs.value_coded=cn1.concept_id and cn1.locale='en' and cn1.locale_preferred=1 and cn1.voided=0)\n"
                + "left join encounter on(encounter.encounter_id=obs.encounter_id and encounter.voided=0)\n"
                + "left join patient on(obs.person_id=patient.patient_id and patient.voided=0)\n"
                + "where encounter.form_id in(46,53)  and person_id=? and encounter_datetime=?\n"
                + "GROUP BY obs.person_id,encounter.encounter_datetime;";
        PreparedStatement ps = null;
        ResultSet rs = null;
        PatientRegimen order = null;
        User usr = null;
        int patient_id = 0;
        int creator_id = 0;
        int count = 0;

        String drugName = "";
        String regimenCode = "";

        try {
            ps = prepareQuery(sql_text);
            ps.setInt(1, patientID);
            ps.setDate(2, convertToSQLDate(visitDate));
            rs = ps.executeQuery();
            while (rs.next()) {
                drugName = rs.getString("drug_name");
                /*order = new PatientRegimen();
                patient_id = rs.getInt("patient_id");
                order.setPatientID(patient_id);
                order.setOrderID(rs.getInt("order_id"));
                order.setPepfarID(pepfarDictionary.get(patient_id));
                order.setHospID(hospIDDictionary.get(patient_id));
                order.setStartDate(rs.getDate("start_date"));
                order.setStopDate(rs.getDate("discontinued_date"));
                drugName = rs.getString("regimen_name");
                if (drugName != null && !drugName.isEmpty()) {
                    regimenCode = codeDrugs(drugName);
                    order.setRegimenName(regimenCode);
                    order.setDrugName(drugName);
                    if (regimenCode != null && !regimenCode.isEmpty()) {
                        order.setCode(String.valueOf(getCode(regimenCode)));
                        order.setRegimenLine(getRegimenLine(regimenCode));
                    }
                }
                creator_id = rs.getInt("creator");
                usr = userDictionary.get(creator_id);
                if (usr != null) {
                    order.setEnteredBy(usr.getFullName());
                }
                order.setDateEntered(rs.getDate("date_created"));
                order.setCreator(creator_id);
                count++;*/

            }
            ps.close();
            rs.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            screen.showError(ex.getMessage());
        } finally {
            cleanUp(rs, ps);
        }
        return drugName;

    }

    public void runMonthlyExport(Date startDate, Date endDate, int location, String encrypt, File file, String reportType) {
        screen.updateStatus("Loading patients... Please wait");
        long startTime = System.currentTimeMillis();
        ArrayList<model.datapump.Demographics> ptsList = getAllPatientsInDB(location);
        screen.updateStatus("Writing " + file.getName() + " ... Please wait");
        Set<Date> visitDates = null;
        PatientRegimen regimen = null;
        String drugName = "";
        HashMap<Integer, Obs> map = null;
        Visit visit = null;
        Location loc = null;
        Date artStartDate = null;
        Date firstFormDate = null;
        Date lastARTPickupDate = null;
        Date lastVisitDate = null;
        int count = 0;
        int size = ptsList.size();
        screen.updateMinMaxProgress(count, size);
        String fileName = file.getAbsolutePath();
        if (!fileName.endsWith(".csv")) {
            fileName += ".csv";
        }
        File file2 = null;
        File file3 = null;
        file2 = new File(fileName);

        try {
            if (fileName.contains(".")) {
                fileName = fileName.substring(0, fileName.indexOf("."));
            }
            fileName += new SimpleDateFormat("yyyyMMddhhmm'.zip'").format(new Date());

            file3 = new File(fileName);

            mgr.createCSVWriter(file2.getAbsolutePath());
            mgr.writeCSVHeaders(Visit.getVisitHeaders());
            for (model.datapump.Demographics ele : ptsList) {
                visitDates = getVisitsForPatient(ele.getPatientID(), startDate, endDate, location);
                for (Date vdate : visitDates) {
                    map = getObsForPatientForDate(ele.getPatientID(), vdate, location);
                    drugName = getRegimenForPatientForVisit(ele.getPatientID(), vdate);
                    //regimen = getRegimenForPatient(map);
                    //if(regimen!=null){
                    // regimen.setDrugName(drugName);
                    //}

                    //if (regimen != null && regimen.getRegimenName().isEmpty()) {
                    artStartDate = artStartDateDictionary.get(ele.getPatientID());
                    //}
                    firstFormDate = firstVisitDateDictionary.get(ele.getPatientID());
                    lastARTPickupDate = lastARTPickupDateDictionary.get(ele.getPatientID());
                    lastVisitDate = lastVisitDateDictionary.get(ele.getPatientID());
                    visit = Visit.createVisit(ele, vdate, map, artStartDate, drugName, firstFormDate, lastVisitDate, lastARTPickupDate);
                    mgr.writeToCSV(visit);

                }
                count++;
                screen.updateStatus("Writing " + file.getName() + "... Please wait " + count);
                screen.updateProgress(count);
            }

            mgr.closeCSVWriter();
            zipFileEntryNames.add(file2.getAbsolutePath());
            this.addToSpecialZipEncrypt(zipFileEntryNames, file3.getAbsolutePath());
            long duration = calculateDuration(startTime);
            screen.updateStatus("<html><body>Export Completed<br/> File Name: " + file.getName() + "<br/>Time(sec): " + duration + "<br/>Total Patients: " + count + "</body></html>");
        } catch (IOException ex) {
            ex.printStackTrace();
            screen.updateStatus(ex.getMessage());
        }

    }

    public PatientRegimen getRegimenForPatient(HashMap<Integer, Obs> map) {
        PatientRegimen regimen = new PatientRegimen();
        Obs obs = null;
        String regimenLine = "";
        String regimenName = "";
        obs = map.get(7778111);
        if (obs != null) {
            regimenLine = obs.getVariableValue();
            regimen.setPatientID(obs.getPatientID());
            regimen.setHospID(obs.getHospID());
            regimen.setPepfarID(obs.getPepfarID());
            regimen.setStartDate(obs.getVisitDate());
            regimen.setRegimenLine(regimenLine);

        }
        obs = map.get(7778108);
        if (obs != null) {
            regimenName = obs.getVariableValue();
            regimen.setCode(String.valueOf(getCode(StringUtils.replace(regimenName, "-", "/"))));
            regimen.setRegimenName(regimenName);
        } else {
            obs = map.get(7778109);
            if (obs != null) {
                regimenName = obs.getVariableValue();
                regimen.setCode(String.valueOf(getCode(StringUtils.replace(regimenName, "-", "/"))));
                regimen.setRegimenName(regimenName);
            }
        }
        return regimen;
    }

    @Override
    public ArrayList<model.datapump.Location> getLocations() {
        ArrayList<model.datapump.Location> locationList = new ArrayList<model.datapump.Location>();
        try {
            String query = "select location_id, TRIM(UPPER(name)) location_name,uuid from location order by location_name desc";
            PreparedStatement ps = prepareQuery(query);
            ResultSet result = ps.executeQuery();
            model.datapump.Location loc = null;
            String locationName = null;
            int location_id = 0;
            while (result.next()) {
                location_id = result.getInt("location_id");
                locationName = result.getString("location_name");
                loc = new model.datapump.Location();
                loc.setLocationID(location_id);
                loc.setLocationName(locationName);
                loc.setUuid(result.getString("uuid"));
                locationList.add(loc);
            }

            result.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return locationList;
    }

    public void displayErrors(SQLException ex) {
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
        ex.printStackTrace();
    }

    @Override
    public void setDisplay(model.datapump.DisplayScreen screen) {
        this.screen = screen;
    }

    @Override
    public void closeConnections() {
        try {
            if (connection != null) {
                connection.close();
            }
            mgr.closeCSVWriter();
        } catch (IOException ex) {
            screen.showError(ex.getMessage());
        } catch (SQLException ex) {
            screen.showError(ex.getMessage());
        }
    }

    public HashMap<Integer, String> getAllPatientPepfarIDs() {
        ResultSet rs = null;
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        String query = "select patient_id,identifier from patient_identifier where identifier_type=3 and voided=0";
        PreparedStatement ps = prepareQuery(query);
        try {
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getInt("patient_id"), rs.getString("identifier"));
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        }
        return map;
    }

    public HashMap<Integer, PatientAddress> getAllPatientAddresses() {
        ResultSet rs = null;
        HashMap<Integer, PatientAddress> map = new HashMap<Integer, PatientAddress>();
        String query = "SELECT  person_id, ifnull(address1,\"\") address1, ifnull(address2,\"\") address2, \n"
                + "              ifnull(city_village,\"\") city_village, ifnull(state_province,\"\") state_province,\n"
                + "              ifnull (country,\"\")  country, voided FROM person_address where voided=0";
        PreparedStatement ps = prepareQuery(query);
        try {
            rs = ps.executeQuery();
            PatientAddress pa = null;
            while (rs.next()) {
                pa = new PatientAddress();
                pa.setPatient_id(rs.getInt("person_id"));
                pa.setAddress1(rs.getString("address1"));
                pa.setAddress2(rs.getString("address2"));
                pa.setCityVillage(rs.getString("city_village"));
                pa.setStateProvince(rs.getString("state_province"));
                pa.setCountry(rs.getString("country"));
                map.put(pa.getPatient_id(), pa);
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
        }
        return map;
    }

    public HashMap<Integer, PatientProgram> getAllPatientProgram() {
        HashMap<Integer, PatientProgram> map = new HashMap<Integer, PatientProgram>();
        ResultSet rs = null;
        String query = "SELECT pp.patient_id, pp.program_id, pp.date_enrolled, prg.name program_name FROM patient_program pp inner join program prg on(prg.program_id=pp.program_id) and pp.voided=0";
        PreparedStatement ps = prepareQuery(query);
        try {
            rs = ps.executeQuery();
            PatientProgram pprg = null;
            while (rs.next()) {
                pprg = new PatientProgram();
                pprg.setPatient_id(rs.getInt("patient_id"));
                pprg.setDateEnrolled(rs.getDate("date_enrolled"));
                pprg.setProgram_name(rs.getString("program_name"));
                pprg.setProgram_id(rs.getInt("program_id"));
                map.put(rs.getInt("patient_id"), pprg);
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        }
        return map;
    }

    private HashMap<Integer, PatientProgram> getAllHEIEnrollment() {
        HashMap<Integer, PatientProgram> map = new HashMap<Integer, PatientProgram>();
        ResultSet rs = null;
        String query = "select \n"
                + "patient_id,\n"
                + "patient_program.program_id,\n"
                + "DATE_FORMAT(date_enrolled,'%Y-%m-%d') date_enrolled,\n"
                + "name program_name,\n"
                + "DATE_FORMAT(date_completed,'%Y-%m-%d') date_completed,\n"
                + "patient_program.creator,\n"
                + "DATE_FORMAT(patient_program.date_created,'%Y-%m-%d') date_created,\n"
                + "patient_program.changed_by,\n"
                + "DATE_FORMAT(patient_program.date_changed,'%Y-%m-%d') date_changed,\n"
                + "patient_program.location_id\n"
                + "from patient_program\n"
                + "inner join\n"
                + "program on(program.program_id=patient_program.program_id)\n"
                + "where voided=0 and patient_program.program_id=9 order by date_enrolled desc";
        PreparedStatement ps = prepareQuery(query);
        PatientProgram pprg = null;
        try {
            rs = ps.executeQuery();
            int patient_id = -1;
            int count = 0;
            while (rs.next()) {
                pprg = new PatientProgram();
                patient_id = rs.getInt("patient_id");
                pprg.setPatient_id(patient_id);
                pprg.setProgram_id(rs.getInt("program_id"));
                pprg.setDateEnrolled(rs.getDate("date_enrolled"));
                pprg.setProgram_name(rs.getString("program_name"));
                pprg.setChangedBy(rs.getString("changed_by"));
                pprg.setDateChanged(rs.getDate("date_changed"));
                pprg.setCreator(rs.getString("creator"));
                pprg.setDateCreated(rs.getDate("date_created"));
                pprg.setDateCompleted(rs.getDate("date_completed"));
                pprg.setLocationID(rs.getInt("location_id"));
                map.put(patient_id, pprg);
                count++;
                screen.updateStatus(count + " patient program records loaded....");
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
        }
        return map;

    }

    private HashMap<Integer, PatientProgram> getAllPepEnrollment() {
        HashMap<Integer, PatientProgram> map = new HashMap<Integer, PatientProgram>();
        ResultSet rs = null;
        String query = "select \n"
                + "patient_id,\n"
                + "patient_program.program_id,\n"
                + "DATE_FORMAT(date_enrolled,'%Y-%m-%d') date_enrolled,\n"
                + "name program_name,\n"
                + "DATE_FORMAT(date_completed,'%Y-%m-%d') date_completed,\n"
                + "patient_program.creator,\n"
                + "DATE_FORMAT(patient_program.date_created,'%Y-%m-%d') date_created,\n"
                + "patient_program.changed_by,\n"
                + "DATE_FORMAT(patient_program.date_changed,'%Y-%m-%d') date_changed,\n"
                + "patient_program.location_id\n"
                + "from patient_program\n"
                + "inner join\n"
                + "program on(program.program_id=patient_program.program_id)\n"
                + "where voided=0 and patient_program.program_id=10 order by date_enrolled desc";
        PreparedStatement ps = prepareQuery(query);
        PatientProgram pprg = null;
        try {
            rs = ps.executeQuery();
            int patient_id = -1;
            int count = 0;
            while (rs.next()) {
                pprg = new PatientProgram();
                patient_id = rs.getInt("patient_id");
                pprg.setPatient_id(patient_id);
                pprg.setProgram_id(rs.getInt("program_id"));
                pprg.setDateEnrolled(rs.getDate("date_enrolled"));
                pprg.setProgram_name(rs.getString("program_name"));
                pprg.setChangedBy(rs.getString("changed_by"));
                pprg.setDateChanged(rs.getDate("date_changed"));
                pprg.setCreator(rs.getString("creator"));
                pprg.setDateCreated(rs.getDate("date_created"));
                pprg.setDateCompleted(rs.getDate("date_completed"));
                pprg.setLocationID(rs.getInt("location_id"));
                map.put(patient_id, pprg);
                count++;
                screen.updateStatus(count + " patient program records loaded....");
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
        }
        return map;

    }

    private HashMap<Integer, PatientProgram> getAllPeadEnrollment() {
        HashMap<Integer, PatientProgram> map = new HashMap<Integer, PatientProgram>();
        ResultSet rs = null;
        String query = "select \n"
                + "patient_id,\n"
                + "patient_program.program_id,\n"
                + "DATE_FORMAT(date_enrolled,'%Y-%m-%d') date_enrolled,\n"
                + "name program_name,\n"
                + "DATE_FORMAT(date_completed,'%Y-%m-%d') date_completed,\n"
                + "patient_program.creator,\n"
                + "DATE_FORMAT(patient_program.date_created,'%Y-%m-%d') date_created,\n"
                + "patient_program.changed_by,\n"
                + "DATE_FORMAT(patient_program.date_changed,'%Y-%m-%d') date_changed,\n"
                + "patient_program.location_id\n"
                + "from patient_program\n"
                + "inner join\n"
                + "program on(program.program_id=patient_program.program_id)\n"
                + "where voided=0 and patient_program.program_id=1 order by date_enrolled desc";
        PreparedStatement ps = prepareQuery(query);
        PatientProgram pprg = null;
        try {
            rs = ps.executeQuery();
            int patient_id = -1;
            int count = 0;
            while (rs.next()) {
                pprg = new PatientProgram();
                patient_id = rs.getInt("patient_id");
                pprg.setPatient_id(patient_id);
                pprg.setProgram_id(rs.getInt("program_id"));
                pprg.setDateEnrolled(rs.getDate("date_enrolled"));
                pprg.setProgram_name(rs.getString("program_name"));
                pprg.setChangedBy(rs.getString("changed_by"));
                pprg.setDateChanged(rs.getDate("date_changed"));
                pprg.setCreator(rs.getString("creator"));
                pprg.setDateCreated(rs.getDate("date_created"));
                pprg.setDateCompleted(rs.getDate("date_completed"));
                pprg.setLocationID(rs.getInt("location_id"));
                map.put(patient_id, pprg);
                count++;
                screen.updateStatus(count + " patient program records loaded....");
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
        }
        return map;

    }

    public HashMap<Integer, User> getAllUsers() {

        User usr = null;
        HashMap<Integer, User> map = new HashMap<Integer, User>();
        String query = "select "
                + "user_id, "
                + "users.person_id, "
                + "username, "
                + "family_name,"
                + "given_name ,"
                + "CONCAT(given_name,' ',family_name) fullname "
                + "from users\n"
                + "inner join person_name on(users.person_id=person_name.person_id and person_name.voided=0) ";
        PreparedStatement ps = prepareQuery(query);
        try {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                usr = new User();
                usr.setFirstName(rs.getString("given_name"));
                usr.setLastName(rs.getString("family_name"));
                usr.setFullName(rs.getString("fullname"));
                usr.setPerson_id(rs.getInt("person_id"));
                usr.setUserName(rs.getString("username"));
                usr.setUser_id(rs.getInt("user_id"));
                map.put(usr.getUser_id(), usr);
            }

            rs.close();
            ps.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
        }
        return map;
    }

    public HashMap<Integer, String> getDrugNames() {
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        ResultSet rs = null;
        String query = "select drug_id, name from drug where retired=0";
        PreparedStatement ps = prepareQuery(query);
        try {
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getInt("drug_id"), rs.getString("name"));
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
        }
        return map;
    }

    public HashMap<Integer, model.datapump.Concept> getAllConcepts() {
        HashMap<Integer, model.datapump.Concept> map = new HashMap<Integer, model.datapump.Concept>();
        ResultSet rs = null;
        String query = "select concept_name.concept_id, concept_name.name, concept.datatype_id\n"
                + " from concept_name inner join concept on(concept.concept_id=concept_name.concept_id) \n"
                + "where \n"
                + "locale='en' and locale_preferred=1";
        PreparedStatement ps = prepareQuery(query);
        model.datapump.Concept c = null;
        int conceptID = 0;
        String conceptName = "";
        int dataType = 0;
        try {
            rs = ps.executeQuery();
            while (rs.next()) {
                conceptID = rs.getInt("concept_id");
                conceptName = rs.getString("name");
                dataType = rs.getInt("datatype_id");
                c = new model.datapump.Concept();
                c.setConceptID(conceptID);
                c.setConceptName(conceptName);
                c.setDataType(dataType);
                map.put(rs.getInt("concept_id"), c);
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
        }
        return map;
    }

    public HashMap<Integer, PersonName> getAllPersonNames() {
        ResultSet rs = null;
        String query = "SELECT person_id,given_name,family_name,middle_name FROM person_name where voided=0";
        PreparedStatement ps = prepareQuery(query);
        HashMap<Integer, PersonName> map = new HashMap<Integer, PersonName>();
        try {
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getInt("person_id"), new PersonName(rs.getInt("person_id"), rs.getString("given_name"), rs.getString("family_name"), rs.getString("middle_name")));
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
        }
        return map;
    }

    public HashMap<Integer, Person> getAllPersonObj() {
        ResultSet rs = null;
        String query = "SELECT person_id, gender, birthdate,uuid FROM person where voided=0";
        PreparedStatement ps = prepareQuery(query);
        HashMap<Integer, Person> map = new HashMap<Integer, Person>();
        try {
            rs = ps.executeQuery();
            Person p = null;
            while (rs.next()) {
                p = new Person();
                p.setPerson_id(rs.getInt("person_id"));
                p.setGender(rs.getString("gender"));
                p.setBirthDate(rs.getDate("birthdate"));
                p.setUuid(rs.getString("uuid"));
                map.put(rs.getInt("person_id"), p);
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
        }
        return map;
    }

    public ArrayList<Location> getLocationList() {
        ArrayList<Location> locationList = new ArrayList<Location>();
        try {
            String query = "select location_id, TRIM(UPPER(name)) location_name from location order by location_name desc";
            PreparedStatement ps = prepareQuery(query);
            ResultSet result = ps.executeQuery();
            Location loc = null;
            String locationName = null;
            int location_id = 0;
            while (result.next()) {
                location_id = result.getInt("location_id");
                locationName = result.getString("location_name");
                loc = new Location();
                loc.setLocationID(location_id);
                loc.setLocationName(locationName);
                locationList.add(loc);
            }

            result.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return locationList;
    }

    public Map<Integer, String> getAllFacilities() {
        Map<Integer, String> locationDct = new HashMap<Integer, String>();
        Map<Integer, String> sortedLocation = new TreeMap<Integer, String>();
        try {
            String query = "select location_id, TRIM(UPPER(name)) location_name from location order by location_name desc";
            PreparedStatement ps = prepareQuery(query);
            ResultSet result = ps.executeQuery();
            Location loc = null;
            String locationName = null;
            int location_id = 0;
            while (result.next()) {
                location_id = result.getInt("location_id");
                locationName = result.getString("location_name");
                loc = new Location();
                loc.setLocationID(location_id);
                loc.setLocationName(locationName);
                locationDct.put(location_id, locationName);
            }
            sortedLocation = new TreeMap<Integer, String>(locationDct);
            result.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return sortedLocation;
    }

    private void loadDemographics(int location_id) {
        patientDemoList = getAllPatientsInDB(location_id);
        patientDemoMap = new HashMap<Integer, model.datapump.Demographics>();
        for (model.datapump.Demographics demo : patientDemoList) {
            patientDemoMap.put(demo.getPatientID(), demo);
        }
    }

    public ArrayList<model.datapump.Demographics> getAllPatientsInDB(int location_id) {
        ArrayList<model.datapump.Demographics> patients = new ArrayList<model.datapump.Demographics>();
        String sql_text = "select \n"
                + "                person.person_id person_source_pk ,\n"
                + "                person.uuid person_uuid,\n"
                + "                person.creator creator_id,\n"
                + "                DATE_FORMAT(person.birthdate,'%Y-%m-%d') dob,\n"
                + "                TIMESTAMPDIFF(YEAR,birthdate,curdate()) ageYrs,\n"
                + "                TIMESTAMPDIFF(MONTH,birthdate,curdate()) ageMnt,\n"
                + "                person.gender,\n"
                + "                DATE_FORMAT(person.date_created,'%Y-%m-%d %H:%i:%s') date_created,\n"
                + "                person.voided,\n"
                + "                pid.identifier,\n"
                + "                DATE_FORMAT(person.date_changed,'%Y-%m-%d %H:%i:%s') date_changed \n"
                + "                from person \n"
                + "                inner join patient_identifier pid on(pid.patient_id=person.person_id and pid.identifier_type=4 and pid.voided=0)\n"
                + "                order by pid.identifier asc";

        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            rs = stmt.executeQuery(sql_text);

            int person_id = -1;
            int creator_id = -1;
            int count = 0;
            PersonName pn = null;
            PatientProgram pprg = null;
            PatientAddress padd = null;
            User usr = null;
            String[] title_arr = {
                "person_source_pk",
                "person_uuid",
                "pepfar_id",
                "hosp_id",
                "ehnid",
                "other_id",
                "first_name",
                "last_name",
                "middle_name",
                "adult_enrollment_dt",
                "pead_enrollment_dt",
                "pmtct_enrollment_dt",
                "dob",
                "gender",
                "address1",
                "address2",
                "address_lga",
                "address_state",
                "creator_id",
                "date_created",
                "voided",
                "date_changed",
                "location_id",
                "creator",
                "location"
            };

            model.datapump.Demographics pts = null;
            while (rs.next()) {
                pts = new model.datapump.Demographics();
                person_id = rs.getInt("person_source_pk");
                pts.setPatientID(person_id);
                pts.setPatientUUID(rs.getString("person_uuid"));
                pts.setPepfarID(pepfarDictionary.get(person_id));
                pts.setHospID(hospIDDictionary.get(person_id));
                pts.seteHNID(ehnidDictionary.get(person_id));
                pts.setOtherID(otherIDDictionary.get(person_id));
                pts.setAgeMnt(rs.getInt("ageMnt"));
                pn = namesDictionary.get(person_id);
                if (pn != null) {
                    pts.setFirstName(pn.getFirstName());
                    pts.setLastName(pn.getLastName());
                    pts.setMiddleName(pn.getMiddleName());
                } else {
                    pts.setFirstName("");
                    pts.setLastName("");
                    pts.setMiddleName("");
                }
                pprg = adultARTPatientProgram.get(person_id);
                if (pprg != null) {
                    pts.setAdultEnrollmentDt(pprg.getDateEnrolled());
                    pts.setEnrollDate(pprg.getDateEnrolled());
                }
                pprg = peadPatientProgram.get(person_id);
                if (pprg != null) {
                    pts.setPeadEnrollmentDt(pprg.getDateEnrolled());
                    if (pts.getAdultEnrollmentDt() == null) {
                        pts.setEnrollDate(pprg.getDateEnrolled());
                    }
                }
                pprg = pmtctPatientProgram.get(person_id);
                if (pprg != null) {
                    pts.setPmtctEnrollmentDt(pprg.getDateEnrolled());
                }
                pprg = heiEnrollment.get(person_id);
                if (pprg != null) {
                    pts.setHeiEnrollmentDt(pprg.getDateEnrolled());
                }
                pprg = pepPatientEnrollment.get(person_id);
                if (pprg != null) {
                    pts.setPepEnrollmentDt(pprg.getDateEnrolled());
                }
                pts.setDateOfBirth(rs.getDate("dob"));
                pts.setAge(rs.getInt("ageYrs"));
                pts.setGender(rs.getString("gender"));
                padd = addressDictionary.get(person_id);
                if (padd != null) {
                    pts.setAddress1(padd.getAddress1());
                    pts.setAddress2(padd.getAddress2());
                    pts.setAddress_lga(padd.getCityVillage());
                    pts.setAddress_state(padd.getStateProvince());
                }
                pts.setCreatorID(rs.getInt("creator_id"));
                pts.setDateCreated(rs.getDate("date_created"));
                pts.setVoided(rs.getInt("voided"));
                pts.setDateChanged(rs.getDate("date_changed"));
                pts.setLocationID(location_id);
                pts.setLocationName(String.valueOf(idLocationDictionary.get(person_id)));

                creator_id = rs.getInt("creator_id");
                usr = userDictionary.get(creator_id);
                if (usr != null) {
                    int pid = usr.getPerson_id();
                    pn = namesDictionary.get(pid);
                    pts.setCreatorName(pn.getFullName());
                }
                patients.add(pts);
                count++;
                screen.updateStatus("Loading Patients...Please wait " + count);
            }
            screen.updateStatus("Loading Patients completed");
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                cleanUp(rs, stmt);
                mgr.closeXMLWriter();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return patients;
    }

    public ArrayList<model.datapump.Demographics> getAllPatientsInDB(int location_id, Set<Integer> idSet) {
        ArrayList<model.datapump.Demographics> patients = new ArrayList<model.datapump.Demographics>();
        String sql_text = "select \n"
                + "                person.person_id person_source_pk ,\n"
                + "                person.uuid person_uuid,\n"
                + "                person.creator creator_id,\n"
                + "                DATE_FORMAT(person.birthdate,'%Y-%m-%d') dob,\n"
                + "                TIMESTAMPDIFF(YEAR,birthdate,curdate()) ageYrs,\n"
                + "                TIMESTAMPDIFF(MONTH,birthdate,curdate()) ageMnt,\n"
                + "                person.gender,\n"
                + "                DATE_FORMAT(person.date_created,'%Y-%m-%d %H:%i:%s') date_created,\n"
                + "                person.voided,\n"
                + "                pid.identifier,\n"
                + "                DATE_FORMAT(person.date_changed,'%Y-%m-%d %H:%i:%s') date_changed \n"
                + "                from person \n"
                + "                inner join patient_identifier pid on(pid.patient_id=person.person_id and pid.identifier_type=3 and pid.voided=0)\n"
                + "                where person.person_id in(" + buildString(idSet) + ")  order by pid.identifier asc";

        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(Integer.MIN_VALUE);
            rs = stmt.executeQuery(sql_text);

            int person_id = -1;
            int creator_id = -1;
            int count = 0;
            PersonName pn = null;
            PatientProgram pprg = null;
            PatientAddress padd = null;
            User usr = null;
            String[] title_arr = {
                "person_source_pk",
                "person_uuid",
                "pepfar_id",
                "hosp_id",
                "ehnid",
                "other_id",
                "first_name",
                "last_name",
                "middle_name",
                "adult_enrollment_dt",
                "pead_enrollment_dt",
                "pmtct_enrollment_dt",
                "dob",
                "gender",
                "address1",
                "address2",
                "address_lga",
                "address_state",
                "creator_id",
                "date_created",
                "voided",
                "date_changed",
                "location_id",
                "creator",
                "location"
            };

            model.datapump.Demographics pts = null;
            String pepfarID = "";
            String hospID = "";
            while (rs.next()) {
                pts = new model.datapump.Demographics();
                person_id = rs.getInt("person_source_pk");
                pts.setPatientID(person_id);
                pts.setPatientUUID(rs.getString("person_uuid"));

                pepfarID = pepfarDictionary.get(person_id);
                hospID = hospIDDictionary.get(person_id);
                /*if (!StringUtils.isEmpty(pepfarID)) {
                    pts.setPepfarID(pepfarDictionary.get(person_id));
                } */

                pts.setPepfarID(pepfarDictionary.get(person_id));
                pts.setHospID(hospIDDictionary.get(person_id));
                pts.seteHNID(ehnidDictionary.get(person_id));
                pts.setOtherID(otherIDDictionary.get(person_id));
                pts.setAgeMnt(rs.getInt("ageMnt"));
                pn = namesDictionary.get(person_id);
                if (pn != null) {
                    pts.setFirstName(pn.getFirstName());
                    pts.setLastName(pn.getLastName());
                    pts.setMiddleName(pn.getMiddleName());
                } else {
                    pts.setFirstName("");
                    pts.setLastName("");
                    pts.setMiddleName("");
                }
                pprg = adultARTPatientProgram.get(person_id);
                if (pprg != null) {
                    pts.setAdultEnrollmentDt(pprg.getDateEnrolled());
                    pts.setEnrollDate(pprg.getDateEnrolled());
                }
                pprg = peadPatientProgram.get(person_id);
                if (pprg != null) {
                    pts.setPeadEnrollmentDt(pprg.getDateEnrolled());
                    if (pts.getAdultEnrollmentDt() == null) {
                        pts.setEnrollDate(pprg.getDateEnrolled());
                    }
                }
                pprg = pmtctPatientProgram.get(person_id);
                if (pprg != null) {
                    pts.setPmtctEnrollmentDt(pprg.getDateEnrolled());
                }
                pprg = heiEnrollment.get(person_id);
                if (pprg != null) {
                    pts.setHeiEnrollmentDt(pprg.getDateEnrolled());
                }
                pprg = pepPatientEnrollment.get(person_id);
                if (pprg != null) {
                    pts.setPepEnrollmentDt(pprg.getDateEnrolled());
                }
                pts.setDateOfBirth(rs.getDate("dob"));
                pts.setAge(rs.getInt("ageYrs"));
                pts.setGender(rs.getString("gender"));
                padd = addressDictionary.get(person_id);
                if (padd != null) {
                    pts.setAddress1(padd.getAddress1());
                    pts.setAddress2(padd.getAddress2());
                    pts.setAddress_lga(padd.getCityVillage());
                    pts.setAddress_state(padd.getStateProvince());
                }
                pts.setCreatorID(rs.getInt("creator_id"));
                pts.setDateCreated(rs.getDate("date_created"));
                pts.setVoided(rs.getInt("voided"));
                pts.setDateChanged(rs.getDate("date_changed"));
                pts.setLocationID(location_id);
                pts.setLocationName(String.valueOf(idLocationDictionary.get(person_id)));

                creator_id = rs.getInt("creator_id");
                usr = userDictionary.get(creator_id);
                if (usr != null) {
                    int pid = usr.getPerson_id();
                    pn = namesDictionary.get(pid);
                    pts.setCreatorName(pn.getFullName());
                }
                patients.add(pts);
                count++;
                screen.updateStatus("Loading Patients...Please wait " + count);
            }
            screen.updateStatus("Loading Patients completed");
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                cleanUp(rs, stmt);
                mgr.closeXMLWriter();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return patients;
    }

    private HashMap<Integer, PatientProgram> getAllPMTCTEnrollment() {
        HashMap<Integer, PatientProgram> map = new HashMap<Integer, PatientProgram>();
        ResultSet rs = null;
        String query = "select \n"
                + "patient_id,\n"
                + "patient_program.program_id,\n"
                + "DATE_FORMAT(date_enrolled,'%Y-%m-%d') date_enrolled,\n"
                + "name program_name,\n"
                + "DATE_FORMAT(date_completed,'%Y-%m-%d') date_completed,\n"
                + "patient_program.creator,\n"
                + "DATE_FORMAT(patient_program.date_created,'%Y-%m-%d') date_created,\n"
                + "patient_program.changed_by,\n"
                + "DATE_FORMAT(patient_program.date_changed,'%Y-%m-%d') date_changed,\n"
                + "patient_program.location_id\n"
                + "from patient_program\n"
                + "inner join\n"
                + "program on(program.program_id=patient_program.program_id)\n"
                + "where voided=0 and patient_program.program_id=1 order by date_enrolled desc";
        PreparedStatement ps = prepareQuery(query);
        PatientProgram pprg = null;
        try {
            rs = ps.executeQuery();
            int patient_id = -1;
            int count = 0;
            while (rs.next()) {
                pprg = new PatientProgram();
                patient_id = rs.getInt("patient_id");
                pprg.setPatient_id(patient_id);
                pprg.setProgram_id(rs.getInt("program_id"));
                pprg.setDateEnrolled(rs.getDate("date_enrolled"));
                pprg.setProgram_name(rs.getString("program_name"));
                pprg.setChangedBy(rs.getString("changed_by"));
                pprg.setDateChanged(rs.getDate("date_changed"));
                pprg.setCreator(rs.getString("creator"));
                pprg.setDateCreated(rs.getDate("date_created"));
                pprg.setDateCompleted(rs.getDate("date_completed"));
                pprg.setLocationID(rs.getInt("location_id"));
                map.put(patient_id, pprg);
                count++;
                screen.updateStatus(count + " patient program records loaded....");
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
        }
        return map;
    }

    public HashMap<Integer, String> getAllPatientEHNID() {
        ResultSet rs = null;
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        String query = "select patient_id,identifier from patient_identifier where identifier_type=5 and voided=0";
        PreparedStatement ps = prepareQuery(query);
        try {
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getInt("patient_id"), rs.getString("identifier"));
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
        }
        return map;
    }

    public HashMap<Integer, String> getAllPatientOtherID() {
        ResultSet rs = null;
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        String query = "select patient_id,identifier from patient_identifier where identifier_type=2 and voided=0";
        PreparedStatement ps = prepareQuery(query);
        try {
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getInt("patient_id"), rs.getString("identifier"));
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
        }
        return map;
    }

    public HashMap<Integer, String> getAllValueNumericConcept() {
        HashMap<Integer, String> numericConceptDictionary = new HashMap<Integer, String>();
        String query = "SELECT\n"
                + "concept_name.concept_id,\n"
                + "concept_name.`name`,\n"
                + "concept.datatype_id\n"
                + "from concept \n"
                + "inner join concept_name on\n"
                + "(concept_name.concept_id=concept.concept_id  and  concept_name.voided=0 and concept_name.locale_preferred='1')\n"
                + "where concept.datatype_id=1 and concept_name.locale='en'";
        ResultSet rs = null;
        PreparedStatement ps = prepareQuery(query);
        try {
            rs = ps.executeQuery();
            while (rs.next()) {
                numericConceptDictionary.put(rs.getInt("concept_id"), rs.getString("name"));
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        }
        return numericConceptDictionary;
    }

    public HashMap<Integer, String> getAllValueCodedConcept() {
        HashMap<Integer, String> codedObsDictionary = new HashMap<Integer, String>();
        String query = "SELECT\n"
                + "concept_name.concept_id,\n"
                + "concept_name.`name`,\n"
                + "concept.datatype_id\n"
                + "from concept \n"
                + "inner join concept_name on\n"
                + "( concept_name.concept_id=concept.concept_id  and  concept_name.voided=0 and concept_name.locale_preferred='1')\n"
                + "where concept.datatype_id=2 and concept_name.locale='en'";
        ResultSet rs = null;
        PreparedStatement ps = prepareQuery(query);

        try {

            rs = ps.executeQuery();
            while (rs.next()) {
                codedObsDictionary.put(rs.getInt("concept_id"), rs.getString("name"));
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        }
        return codedObsDictionary;
    }

    public HashMap<Integer, String> getAllPatientHospIDs() {
        ResultSet rs = null;
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        String query = "select patient_id,identifier from patient_identifier where identifier_type=6 and voided=0";
        PreparedStatement ps = prepareQuery(query);
        try {
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getInt("patient_id"), rs.getString("identifier"));
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
        }
        return map;
    }

    private HashMap<Integer, String> getIdentifierLocation() {
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        String sql_text = "select patient_id,name location_name from patient_identifier left join location on(patient_identifier.location_id=location.location_id) where identifier_type=4 and voided=0";
        ResultSet rs = null;
        PreparedStatement ps = prepareQuery(sql_text);
        PatientProgram pprg = null;
        try {
            rs = ps.executeQuery();
            int patient_id = -1;
            int count = 0;
            String location = null;
            while (rs.next()) {
                patient_id = rs.getInt("patient_id");
                location = rs.getString("location_name");
                map.put(patient_id, location);
                count++;
                screen.updateStatus(count + " patient program records loaded....");
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        }
        return map;
    }

    private HashMap<Integer, PatientProgram> getAllAdultEnrollment() {
        HashMap<Integer, PatientProgram> map = new HashMap<Integer, PatientProgram>();
        ResultSet rs = null;
        String query = "select \n"
                + "patient_id,\n"
                + "patient_program.program_id,\n"
                + "DATE_FORMAT(date_enrolled,'%Y-%m-%d') date_enrolled,\n"
                + "name program_name,\n"
                + "DATE_FORMAT(date_completed,'%Y-%m-%d') date_completed,\n"
                + "patient_program.creator,\n"
                + "DATE_FORMAT(patient_program.date_created,'%Y-%m-%d') date_created,\n"
                + "patient_program.changed_by,\n"
                + "DATE_FORMAT(patient_program.date_changed,'%Y-%m-%d') date_changed,\n"
                + "patient_program.location_id\n"
                + "from patient_program\n"
                + "inner join\n"
                + "program on(program.program_id=patient_program.program_id)\n"
                + "where voided=0 and patient_program.program_id=1 order by date_enrolled desc";
        PreparedStatement ps = prepareQuery(query);
        PatientProgram pprg = null;
        try {
            rs = ps.executeQuery();
            int patient_id = -1;
            int count = 0;
            while (rs.next()) {
                pprg = new PatientProgram();
                patient_id = rs.getInt("patient_id");
                pprg.setPatient_id(patient_id);
                pprg.setProgram_id(rs.getInt("program_id"));
                pprg.setDateEnrolled(rs.getDate("date_enrolled"));
                pprg.setProgram_name(rs.getString("program_name"));
                pprg.setChangedBy(rs.getString("changed_by"));
                pprg.setDateChanged(rs.getDate("date_changed"));
                pprg.setCreator(rs.getString("creator"));
                pprg.setDateCreated(rs.getDate("date_created"));
                pprg.setDateCompleted(rs.getDate("date_completed"));
                pprg.setLocationID(rs.getInt("location_id"));
                map.put(patient_id, pprg);
                count++;
                screen.updateStatus(count + " patient program records loaded....");
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
        }
        return map;
    }

    public PreparedStatement prepareQuery(String query) {
        ResultSet result = null;
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(query);
        } catch (SQLException e) {
            displayErrors(e);
            return ps;
        }
        return ps;
    }

    private long calculateDuration(long startTime) {
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime) / 1000;
        return duration;
    }

    public void loadDictionaries() {
        loadPepfarIDDictionary();
        loadPatientAddress();
        loadPatientProgram();
        loadPeadEnrollment();
        loadUserDictionary();
        loadDrugDictionary();
        loadConceptDictionary();
        loadConceptDictionaryRL();
        loadLocations();
        loadPersonNames();
        loadPersonObj();
        loadPMTCTEnrollment();
        loadPepEnrollment();
        loadHEIEnrollment();
        loadEHNIDDictionary();
        loadOtherIDDictionary();
        loadHospIDDictionary();
        loadAdultEnrollment();
        loadIDLocationDictionary();
        loadAllValueCodedConcepts();
        loadFormNamesDictionary();
        loadMaps();
        loadARTStartDates();
        loadLastARTPickupDate();
        loadLastVisitDictionary();
        loadFirstVisitDictionary();
        loadONARTPatients();

        //loadLastARTPickupDate();
    }

    private void loadPatientDemographics(int location_id) {
        patientDemoList = getAllPatientsInDB(location_id);
    }

    private void loadPepfarIDDictionary() {
        long startTime = System.currentTimeMillis();
        long duration = 0L;
        ResultSet rs = null;
        pepfarDictionary = getAllPatientPepfarIDs();
        duration = calculateDuration(startTime);
        screen.updateStatus("PEPFAR_ID dictionary loaded in " + duration + " secs");

    }

    private void loadPatientAddress() {
        long startTime = System.currentTimeMillis();
        long duration = 0L;
        addressDictionary = getAllPatientAddresses();
        duration = calculateDuration(startTime);
        screen.updateStatus("Address Dictionary loaded in " + duration + "secs");

    }

    private void loadPatientProgram() {
        long startTime = System.currentTimeMillis();
        long duration = 0L;
        programDictionary = getAllPatientProgram();
        duration = calculateDuration(startTime);
        screen.updateStatus("Program Dictionary loaded in " + duration + "secs");

    }

    private void loadPeadEnrollment() {
        long startTime = System.currentTimeMillis();
        long duration = 0L;
        int size = 0;
        peadPatientProgram = getAllPeadEnrollment();
        duration = calculateDuration(startTime);
        size = peadPatientProgram.size();
        screen.updateStatus(size + " Program Enrollment (Pead) loaded in " + duration + "secs");
    }

    private void loadFormNamesDictionary() {
        long startTime = System.currentTimeMillis();
        long duration = 0L;
        if (formNamesDictionary == null) {
            formNamesDictionary = getAllForms();
            duration = calculateDuration(startTime);
            screen.updateStatus("form names dictionary loaded in " + duration + "secs");

        }
    }

    public HashMap<Integer, String> getAllForms() {
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        ResultSet rs = null;
        String query = "select form_id,name from form";
        PreparedStatement ps = prepareQuery(query);
        try {
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getInt("form_id"), rs.getString("name"));
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        }
        return map;
    }

    private void loadUserDictionary() {
        int size = 0;
        long startTime = System.currentTimeMillis();
        long duration = 0L;
        userDictionary = this.getAllUsers();
        size = userDictionary.size();
        duration = calculateDuration(startTime);
        screen.updateStatus(size + " Users dictionary loaded in " + duration + "secs");
    }

    private void loadDrugDictionary() {
        long startTime = System.currentTimeMillis();
        long duration = 0L;
        //if (drugDictionary == null) {
        drugDictionary = getDrugNames();
        duration = calculateDuration(startTime);
        screen.updateStatus("Drug dictionary loaded in " + duration + "secs");
        //}
    }

    private void loadConceptDictionary() {
        long startTime = System.currentTimeMillis();
        long duration = 0L;
        if (conceptDictionary == null) {
            conceptDictionary = getAllConcepts();
            duration = calculateDuration(startTime);
            screen.updateStatus("CONCEPT dictionary loaded in " + duration + "secs");
        }
    }

    private void loadConceptDictionaryRL() {
        long startTime = System.currentTimeMillis();
        long duration = 0L;
        if (conceptDictionaryRL == null) {
            conceptDictionaryRL = getAllConcepts();
            duration = calculateDuration(startTime);
            screen.updateStatus("CONCEPT dictionary loaded in " + duration + "secs");
        }
    }

    private void loadPersonNames() {
        long startTime = System.currentTimeMillis();
        long duration = 0L;
        // if (namesDictionary == null) {
        namesDictionary = getAllPersonNames();
        duration = calculateDuration(startTime);
        screen.updateStatus("Names Dictionary loaded in " + duration + "secs");
        // }
    }

    private void loadLocations() {
        long startTime = System.currentTimeMillis();
        long duration = 0L;
        //if (locationDictionary == null) {
        locationDictionary = getAllFacilities();
        duration = calculateDuration(startTime);
        screen.updateStatus("Phone Dictionary loaded in " + duration + "secs");
        // }
    }

    private void loadPersonObj() {
        long startTime = System.currentTimeMillis();
        long duration = 0L;
        // if (personDictionary == null) {
        personDictionary = getAllPersonObj();
        duration = calculateDuration(startTime);
        screen.updateStatus("Person dictionary loaded in " + duration + "secs");
        //}
    }

    private void loadPMTCTEnrollment() {
        long startTime = System.currentTimeMillis();
        long duration = 0L;
        int size = 0;
        //if (totalEncounterDictionary == null) {
        pmtctPatientProgram = getAllPMTCTEnrollment();
        duration = calculateDuration(startTime);
        size = pmtctPatientProgram.size();
        screen.updateStatus(size + " Program Enrollment (PMTCT) loaded in " + duration + "secs");
    }

    private void loadHEIEnrollment() {
        long startTime = System.currentTimeMillis();
        long duration = 0L;
        int size = 0;
        //if (totalEncounterDictionary == null) {
        heiEnrollment = getAllHEIEnrollment();
        duration = calculateDuration(startTime);
        size = heiEnrollment.size();
        screen.updateStatus(size + " Program Enrollment (HEI) loaded in " + duration + "secs");
    }

    private void loadPepEnrollment() {
        long startTime = System.currentTimeMillis();
        long duration = 0L;
        int size = 0;
        //if (totalEncounterDictionary == null) {
        pepPatientEnrollment = getAllPepEnrollment();
        duration = calculateDuration(startTime);
        size = pepPatientEnrollment.size();
        screen.updateStatus(size + " Program Enrollment (PEP) loaded in " + duration + "secs");
    }

    private void loadEHNIDDictionary() {
        long startTime = System.currentTimeMillis();
        int size = 0;
        long duration = 0L;
        //if (pepfarDictionary == null) {
        ehnidDictionary = getAllPatientEHNID();
        size = ehnidDictionary.size();
        duration = calculateDuration(startTime);
        screen.updateStatus(size + " EHNID dictionary loaded in " + duration + "secs");
        //}
    }

    private void loadOtherIDDictionary() {
        long startTime = System.currentTimeMillis();
        int size = 0;
        long duration = 0L;
        otherIDDictionary = getAllPatientOtherID();
        size = otherIDDictionary.size();
        duration = calculateDuration(startTime);
        screen.updateStatus(size + " Other ID dictionary loaded in " + duration + "secs");
    }

    private void loadHospIDDictionary() {
        int size = 0;
        long startTime = System.currentTimeMillis();
        long duration = 0L;
        //if (hospIDDictionary == null) {
        hospIDDictionary = getAllPatientHospIDs();
        size = hospIDDictionary.size();
        duration = calculateDuration(startTime);
        screen.updateStatus(size + " HOSP_ID dictionary loaded in " + duration + "secs");
        // }
    }

    private void loadAdultEnrollment() {
        long startTime = System.currentTimeMillis();
        long duration = 0L;
        int size = 0;
        //if (totalEncounterDictionary == null) {
        adultARTPatientProgram = getAllAdultEnrollment();
        duration = calculateDuration(startTime);
        size = adultARTPatientProgram.size();
        screen.updateStatus(size + " Program Enrollment (Adult) loaded in " + duration + "secs");
    }

    private void loadIDLocationDictionary() {
        int size = 0;
        long startTime = System.currentTimeMillis();
        long duration = 0L;
        idLocationDictionary = getIdentifierLocation();
        size = idLocationDictionary.size();
        duration = calculateDuration(startTime);
        screen.updateStatus(size + " ID location dictionary loaded in " + duration + "secs");
    }

    private void loadAllValueCodedConcepts() {
        long startTime = System.currentTimeMillis();
        long duration = 0L;
        valueCodedDictionary = getAllValueCodedConcept();
        duration = calculateDuration(startTime);
        screen.updateStatus("value coded concept loaded in memory.....");
    }

    public String formatDate(Date date) {
        String dateString = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            dateString = df.format(date);
        }
        return dateString;

    }

    public String formatDate2(Date date) {
        String dateString = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            dateString = df.format(date);
        }
        return dateString;

    }

    public boolean isValueCodedConcept(int concept_id) {
        boolean ans = false;
        if (valueCodedDictionary.containsKey(concept_id)) {
            ans = true;
        }
        return ans;
    }

    public int getResultSetSize(ResultSet rs) throws SQLException {
        int rowCount = 0;
        if (rs.last()) {
            rowCount = rs.getRow();
            rs.beforeFirst();
        }
        return rowCount;
    }

    private void loadLastVisitDictionary() {
        lastVisitDateDictionary = new HashMap<Integer, Date>();
        screen.updateStatus("Loading last visit dates... Please wait");
        /*String sql_text = "select sinner.patient_id, max(sinner.visit_date) as last_visit_dt\n"
                + "from\n"
                + "(select obs.person_id as patient_id,obs.obs_datetime as visit_date from obs where obs.voided=0\n"
                + "union\n"
                + "select orders.patient_id,orders.start_date from orders) as sinner group by sinner.patient_id";*/
        String sql_text = "select encounter.patient_id, MAX(encounter.encounter_datetime) as last_visit_dt from encounter where encounter.voided=0 and encounter.encounter_datetime<=curdate() GROUP BY encounter.patient_id";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = prepareQuery(sql_text);
            rs = ps.executeQuery();
            while (rs.next()) {
                lastVisitDateDictionary.put(rs.getInt("patient_id"), rs.getDate("last_visit_dt"));
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            screen.updateStatus(ex.getMessage());
        } finally {
            cleanUp(rs, ps);
        }

    }

    private void loadLastVisitVisitDictionary(Date startDate, Date endDate) {
        lastVisitDateDictionary = new HashMap<Integer, Date>();
        screen.updateStatus("Loading last visit dates... Please wait");
        String sql_text = "select sinner.patient_id, max(sinner.visit_date) as first_visit_dt\n"
                + "from\n"
                + "(select obs.person_id as patient_id,obs.obs_datetime as visit_date from obs where obs.voided=0 where obs.obs_datetime between ? and ?\n"
                + "union\n"
                + "select orders.patient_id,orders.start_date from orders where orders.start_date between ? and ?) as sinner group by sinner.patient_id";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = prepareQuery(sql_text);
            ps.setDate(1, convertToSQLDate(startDate));
            ps.setDate(2, convertToSQLDate(endDate));
            rs = ps.executeQuery();
            while (rs.next()) {
                lastVisitDateDictionary.put(rs.getInt("PATIENT_ID"), rs.getDate("first_visit_dt"));
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            screen.updateStatus(ex.getMessage());
        } finally {
            cleanUp(rs, ps);
        }

    }

    private void loadFirstVisitDictionary() {
        firstVisitDateDictionary = new HashMap<Integer, Date>();
        screen.updateStatus("Loading last visit dates... Please wait");
        /* String sql_text = "select sinner.patient_id, min(sinner.visit_date) as first_visit_dt\n"
                + "from\n"
                + "(select obs.person_id as patient_id,obs.obs_datetime as visit_date from obs where obs.voided=0\n"
                + "union\n"
                + "select orders.patient_id,orders.start_date from orders) as sinner group by sinner.patient_id";*/
        String sql_text = "select encounter.patient_id, MIN(encounter.encounter_datetime) as first_visit_dt from encounter where encounter.voided=0 and encounter.encounter_datetime >'2001-01-01' GROUP BY encounter.patient_id";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = prepareQuery(sql_text);

            rs = ps.executeQuery();
            while (rs.next()) {
                firstVisitDateDictionary.put(rs.getInt("patient_id"), rs.getDate("first_visit_dt"));
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            screen.updateStatus(ex.getMessage());
        } finally {
            cleanUp(rs, ps);
        }

    }

    public void runDispensedPharmacyFormDrugs(Date startDate, Date endDate, int location_id, File file) {
        screen.updateStatus("Exporting drug data...Please wait");
        String sql_text = "select \n"
                + "   obs.person_id,\n"
                + "   encounter.encounter_datetime,\n"
                + "   encounter.form_id,\n"
                + "   encounter.provider_id ,\n"
                + "   encounter.date_created,\n"
                + "   encounter.creator\n"
                + "   ,MAX(IF(obs.concept_id=1537, obs.value_coded, NULL)) as `visit_type`\n"
                + "   ,MAX(IF(obs.concept_id=1542, obs.value_coded, NULL)) as `pickup_reason`\n"
                + "   ,MAX(IF(obs.concept_id= 1256, obs.value_coded, NULL)) as  `art_status`\n"
                + "   ,MAX(IF(obs.concept_id= 7778111, obs.value_coded, NULL)) as `regimen_line`\n"
                + "   ,MAX(IF(obs.concept_id= 7778108, obs.value_coded, NULL)) as `first_line`\n"
                + "   ,MAX(IF(obs.concept_id= 7778109, obs.value_coded, NULL)) as  `second_line`\n"
                + "    ,MAX(IF(obs.concept_id= 7778410, obs.value_coded, NULL)) as  `other_regimen`\n"
                + ",sinner.drug_order\n"
                + ",sinner.strength\n"
                + ",sinner.other_strength\n"
                + ",sinner.single_dose\n"
                + ",sinner.single_dose_unit\n"
                + ",sinner.frequency\n"
                + ",sinner.duration\n"
                + ",sinner.duration_unit\n"
                + ",sinner.quantity_prescribed\n"
                + ",sinner.quantity_prescribed_unit\n"
                + ",sinner.quantity_dispenced\n"
                + ",sinner.quantity_dispenced_unit\n"
                + " ,MAX(IF(obs.concept_id= 7778009, obs.value_text, NULL)) as  `ordered_by`\n"
                + " ,MAX(IF(obs.concept_id= 7778015, obs.value_datetime, NULL)) as  `date_ordered`\n"
                + " ,MAX(IF(obs.concept_id= 7778442, obs.value_text, NULL)) as  `counseled_by`\n"
                + " ,MAX(IF(obs.concept_id= 7778443, obs.value_datetime, NULL)) as  `date_counceled`\n"
                + ",MAX(IF(obs.concept_id= 7778444, obs.value_text, NULL)) as  `pickup_by`\n"
                + " \n"
                + "from\n"
                + "obs left join encounter on(obs.encounter_id=encounter.encounter_id)\n"
                + "left join (\n"
                + "       SELECT\n"
                + "obs.person_id,\n"
                + "obs.obs_datetime,\n"
                + "obs.encounter_id,\n"
                + "encounter.form_id,\n"
                + "MAX(IF(obs.concept_id=7778364,obs.value_coded, NULL)) AS drug_order,\n"
                + "MAX(IF(obs.concept_id=7778365,obs.value_coded, NULL)) AS strength,\n"
                + "MAX(IF(obs.concept_id=7778390,obs.value_coded, NULL)) AS other_strength,\n"
                + "MAX(IF(obs.concept_id=7778366,obs.value_numeric, NULL)) AS single_dose,\n"
                + "MAX(IF(obs.concept_id=7778367,obs.value_coded, NULL)) AS single_dose_unit,\n"
                + "MAX(IF(obs.concept_id=7778407,obs.value_coded, NULL)) AS frequency,\n"
                + "MAX(IF(obs.concept_id=7778370,obs.value_numeric, NULL)) AS duration,\n"
                + "MAX(IF(obs.concept_id=7778371,obs.value_coded, NULL)) AS duration_unit,\n"
                + "MAX(IF(obs.concept_id=7778374,obs.value_numeric, NULL)) AS quantity_prescribed,\n"
                + "MAX(IF(obs.concept_id=7778375,obs.value_coded, NULL)) AS quantity_prescribed_unit,\n"
                + "MAX(IF(obs.concept_id=7778372,obs.value_numeric, NULL)) AS quantity_dispenced,\n"
                + "MAX(IF(obs.concept_id=7778373,obs.value_coded, NULL)) AS quantity_dispenced_unit\n"
                + "FROM\n"
                + "obs inner join encounter on(obs.encounter_id=encounter.encounter_id)\n"
                + "where encounter.form_id in (46,53) and obs.obs_group_id is not null\n"
                + "GROUP BY obs.person_id,obs.encounter_id,obs.obs_group_id ORDER BY obs.person_id,obs.obs_datetime\n"
                + "\n"
                + ") sinner\n"
                + "on(sinner.encounter_id=obs.encounter_id)\n"
                + "\n"
                + "where encounter.form_id in (46,53)  and  obs_group_id is null and encounter\n"
                + "GROUP BY obs.person_id,obs.encounter_id,obs.obs_group_id ORDER BY obs.person_id,obs.obs_datetime";

        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        DrugOrder ord = null;
        User usr = null;
        int patient_id = 0;
        int count = 0;
        //String drugfileName = file.getAbsolutePath() + "DrugDWH" + new SimpleDateFormat("yyyyMMddhhmm'.xml'").format(new Date());
        //String drugfileName = file.getAbsolutePath() + "DrugDWH" + new SimpleDateFormat("yyyyMMddhhmm'.xml'").format(new Date());
        //String drugfileName = "drugs.xml";
        //File drugfile = new File(drugfileName);
        String[] headers = {"ORDER_ID", "PATIENT_ID", "PEPFAR_ID", "HOSP_ID", "DRUG", "CONCEPT_ID", "DOSE", "FRIQUENCY", "QUANTITY", "START_DATE", "STOP_DATE", "ENTERED_BY", "DATE_CREATED", "UUID"};

        try {
            //stmt = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
            // java.sql.ResultSet.CONCUR_READ_ONLY);
            // stmt.setFetchSize(Integer.MIN_VALUE);
            ps = prepareQuery(sql_text);
            ps.setDate(1, convertToSQLDate(startDate));
            ps.setDate(2, convertToSQLDate(endDate));

            rs = ps.executeQuery();//stmt.executeQuery(sql_text);
            int size = getResultSetSize(rs);
            //mgr.createXMLWriter(drugfileName);
            mgr.createCSVWriter(file.getAbsolutePath());
            //mgr.openXMLDocument();
            mgr.writeCSVHeaders(headers);
            screen.updateMinMaxProgress(0, size);
            while (rs.next()) {
                ord = new DrugOrder();
                patient_id = rs.getInt("patient_id");
                ord.setOrderID(rs.getInt("order_id"));
                ord.setPepfarID(pepfarDictionary.get(patient_id));
                ord.setHospID(hospIDDictionary.get(patient_id));
                ord.setConceptID(rs.getInt("concept_id"));
                ord.setDrugDose(String.valueOf(rs.getDouble("dose")));
                ord.setDrugName(drugDictionary.get(rs.getInt("drug_inventory_id")));
                ord.setFrequency(rs.getString("frequency"));
                ord.setPatientID(rs.getInt("patient_id"));
                ord.setStartDate(rs.getDate("start_date"));
                ord.setStopDate(rs.getDate("discontinued_date"));
                ord.setCreator(rs.getInt("creator"));
                ord.setOrderer(rs.getInt("orderer"));
                ord.setQuantity(rs.getDouble("quantity"));
                ord.setUuid(rs.getString("uuid"));
                ord.setVoided(rs.getInt("voided"));
                ord.setDateVoided(rs.getDate("date_voided"));
                ord.setVoidedBy(rs.getInt("voided_by"));
                usr = userDictionary.get(rs.getInt("creator"));
                if (usr != null) {
                    ord.setEnteredBy(namesDictionary.get(usr.getPerson_id()).getFullName());
                }
                ord.setDateEntered(rs.getDate("date_created"));
                count++;
                mgr.writeToCSV(ord);
                screen.updateProgress(count);
                screen.updateStatus("Writing drugs.xml...Please wait " + count);
            }
            mgr.closeCSVWriter();
            //mgr.endXMLDocument();
            //zipFileEntryNames.add(drugfileName);
            screen.updateStatus("Drug export completed");
            rs.close();
            ps.close();
            //mgr.closeXMLWriter();

        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());

        } catch (IOException ex) {
            screen.updateStatus(ex.getMessage());
        } finally {
            cleanUp(rs, stmt);
        }
    }

    public void runDispensedDrugs(Date startDate, Date endDate, int location_id, File file) {
        screen.updateStatus("Exporting drug data...Please wait");
        String sql_text = "select\n"
                + "orders.order_id,"
                + "orders.patient_id,\n"
                + "drug_order.drug_inventory_id,\n"
                + "orders.concept_id,\n"
                + "orders.start_date ,\n"
                + "orders.discontinued_date ,\n"
                + "drug_order.dose,\n"
                + "drug_order.units,\n"
                + "drug_order.frequency,\n"
                + "drug_order.quantity,\n"
                + "orders.orderer,\n"
                + "orders.creator,\n"
                + "orders.date_created,\n"
                + "orders.voided,\n"
                + "orders.date_voided,\n"
                + "orders.uuid,\n"
                + "orders.voided_by\n"
                + "from\n"
                + "orders\n"
                + "inner join drug_order on(drug_order.order_id=orders.order_id and orders.voided=0)\n"
                + "inner join drug on(drug.drug_id=drug_order.drug_inventory_id)\n"
                + "inner join patient on(patient.patient_id=orders.patient_id and patient.voided=0) where orders.start_date between ? and ?\n"
                + "ORDER BY patient_id asc ,orders.start_date asc;";

        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        DrugOrder ord = null;
        User usr = null;
        int patient_id = 0;
        int count = 0;
        //String drugfileName = file.getAbsolutePath() + "DrugDWH" + new SimpleDateFormat("yyyyMMddhhmm'.xml'").format(new Date());
        //String drugfileName = file.getAbsolutePath() + "DrugDWH" + new SimpleDateFormat("yyyyMMddhhmm'.xml'").format(new Date());
        //String drugfileName = "drugs.xml";
        //File drugfile = new File(drugfileName);
        String[] headers = {"ORDER_ID", "PATIENT_ID", "PEPFAR_ID", "HOSP_ID", "DRUG", "CONCEPT_ID", "DOSE", "FRIQUENCY", "QUANTITY", "START_DATE", "STOP_DATE", "ENTERED_BY", "DATE_CREATED", "UUID"};

        try {
            //stmt = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
            // java.sql.ResultSet.CONCUR_READ_ONLY);
            // stmt.setFetchSize(Integer.MIN_VALUE);
            ps = prepareQuery(sql_text);
            ps.setDate(1, convertToSQLDate(startDate));
            ps.setDate(2, convertToSQLDate(endDate));

            rs = ps.executeQuery();//stmt.executeQuery(sql_text);
            int size = getResultSetSize(rs);
            //mgr.createXMLWriter(drugfileName);
            mgr.createCSVWriter(file.getAbsolutePath());
            //mgr.openXMLDocument();
            mgr.writeCSVHeaders(headers);
            screen.updateMinMaxProgress(0, size);
            while (rs.next()) {
                ord = new DrugOrder();
                patient_id = rs.getInt("patient_id");
                ord.setOrderID(rs.getInt("order_id"));
                ord.setPepfarID(pepfarDictionary.get(patient_id));
                ord.setHospID(hospIDDictionary.get(patient_id));
                ord.setConceptID(rs.getInt("concept_id"));
                ord.setDrugDose(String.valueOf(rs.getDouble("dose")));
                ord.setDrugName(drugDictionary.get(rs.getInt("drug_inventory_id")));
                ord.setFrequency(rs.getString("frequency"));
                ord.setPatientID(rs.getInt("patient_id"));
                ord.setStartDate(rs.getDate("start_date"));
                ord.setStopDate(rs.getDate("discontinued_date"));
                ord.setCreator(rs.getInt("creator"));
                ord.setOrderer(rs.getInt("orderer"));
                ord.setQuantity(rs.getDouble("quantity"));
                ord.setUuid(rs.getString("uuid"));
                ord.setVoided(rs.getInt("voided"));
                ord.setDateVoided(rs.getDate("date_voided"));
                ord.setVoidedBy(rs.getInt("voided_by"));
                usr = userDictionary.get(rs.getInt("creator"));
                if (usr != null) {
                    ord.setEnteredBy(namesDictionary.get(usr.getPerson_id()).getFullName());
                }
                ord.setDateEntered(rs.getDate("date_created"));
                count++;
                mgr.writeToCSV(ord);
                screen.updateProgress(count);
                screen.updateStatus("Writing drugs.xml...Please wait " + count);
            }
            mgr.closeCSVWriter();
            //mgr.endXMLDocument();
            //zipFileEntryNames.add(drugfileName);
            screen.updateStatus("Drug export completed");
            rs.close();
            ps.close();
            //mgr.closeXMLWriter();

        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());

        } catch (IOException ex) {
            screen.updateStatus(ex.getMessage());
        } finally {
            cleanUp(rs, stmt);
        }
    }

    public void runAppointmentExport(Date startDate, Date endDate, int location_id, File file) {

        screen.updateStatus("Exporting Appointment data...Please wait");
        // String startDateStr=formatDate(startDate);
        // String endDateStr=formatDate(endDate);
        String sql_text = "select \n"
                + "appointment_id,\n"
                + "patient_id,\n"
                + "location_id,\n"
                + "provider_id,\n"
                + "appointment_date,\n"
                + "phone_number,\n"
                + "email,\n"
                + "other_contact_phone,\n"
                + "reason,\n"
                + "note,\n"
                + "attended,\n"
                + "voided\n"
                + "from appointment where appointment_date between ? and ? and location_id= ?";
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = prepareQuery(sql_text);
            ps.setDate(1, convertToSQLDate(startDate));
            ps.setDate(2, convertToSQLDate(endDate));
            ps.setInt(3, location_id);
            rs = ps.executeQuery();
            int size = getResultSetSize(rs);

            // stmt = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
            // java.sql.ResultSet.CONCUR_READ_ONLY);
            //stmt.setFetchSize(Integer.MIN_VALUE);
            //rs = stmt.executeQuery(sql_text);
            //String[] data = new String[12];
            //String aptfileName = file.getAbsolutePath() + "AppointmentDWH" + new SimpleDateFormat("yyyyMMddhhmm'.xml'").format(new Date());
            mgr.createCSVWriter(file.getAbsolutePath());
            //String aptfileName = "appointment.xml";
            //File aptfile = new File(aptfileName);
            //csvWriter = new CSVWriter(new FileWriter(aptfile));
            //zipFileEntryNames.add(aptfileName);
            String[] headers = {
                "pepfar_id",
                "hosp_id",
                "appointment_date",
                "provider_name",
                "phone_number",
                "email",
                "other_contact_phone",
                "reason",
                "note",
                "attended",
                "location_name",
                "voided",
                "appointment_id",
                "patient_id",
                "location_id",
                "provider_id"
            };
            mgr.writeCSVHeaders(headers);
            //mgr.createXMLWriter(aptfileName);
            //mgr.openXMLDocument();
            //mgr.writeXMLHeader("appointments");
            int count = 0;
            Appointment apt = null;
            int patient_id = 0;
            int provider_id = 0;
            //int location_id = 0;

            PersonName pn = null;
            screen.updateMinMaxProgress(0, size);
            while (rs.next()) {
                apt = new Appointment();
                apt.setAppointmentID(rs.getInt("appointment_id"));
                patient_id = rs.getInt("patient_id");
                apt.setPatientID(patient_id);
                apt.setPepfarID(pepfarDictionary.get(patient_id));
                apt.setHospID(hospIDDictionary.get(patient_id));
                location_id = rs.getInt("location_id");
                provider_id = rs.getInt("provider_id");
                apt.setLocationID(location_id);
                apt.setProviderID(provider_id);
                apt.setLocationName(locationDictionary.get(location_id));
                pn = namesDictionary.get(provider_id);
                if (pn != null) {
                    apt.setProviderName(pn.getFullName());
                }
                apt.setAppointmentDate(rs.getDate("appointment_date"));
                apt.setPhoneNumber(rs.getString("phone_number"));
                apt.setEmail(rs.getString("email"));
                apt.setOtherPhoneNumbers(rs.getString("other_contact_phone"));
                apt.setReason(rs.getString("reason"));
                apt.setNote(rs.getString("note"));
                apt.setAttended(rs.getString("attended"));
                apt.setVoided(rs.getInt("voided"));
                count++;
                mgr.writeToCSV(apt);
                screen.updateProgress(count++);
                screen.updateStatus("Writing appointment data....Please wait " + count);

            }
            screen.updateStatus("Appointment data completed");
            // screen.setState(false);
            screen.updateProgress(size);
            // mgr.endXMLDocument();
            //mgr.closeXMLWriter();
            mgr.closeCSVWriter();
            rs.close();
            ps.close();
            //stmt.close();

        } catch (SQLException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            screen.updateStatus(ex.getMessage());
            ex.printStackTrace();
        } finally {
            cleanUp(rs, stmt);
        }

    }

}
