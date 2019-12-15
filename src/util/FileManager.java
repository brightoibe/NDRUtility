
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import model.Demographics;
import model.VitalSign;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import model.Appointment;
import model.Concept;
import model.Drug;
import model.DrugOrder;
import model.datapump.Obs;
import model.PatientRegimen;
import model.Regimen;
import model.Relationship;
import model.User;
import model.Visit;
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
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author brightoibe
 */
public class FileManager {

    private CSVWriter csvWriter;
    private CSVReader csvReader;
    private File file;

    private XMLOutputFactory factory = null;
    private XMLStreamWriter writer = null;

    public FileManager() {

    }

    public void createCSVWriter(String fileName) throws IOException {
        //File demofile = new File(fileName);
        if (!fileName.endsWith(".csv")) {
            //demofile.renameTo(new File(fileName + ".csv"));
            fileName += ".csv";
        }
        csvWriter = new CSVWriter(new FileWriter(new File(fileName)));
    }

    public String formatDate(Date date) {
        String dateString = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            dateString = df.format(date);
        }
        return dateString;

    }

    public void createXMLWriter(String fileName) throws XMLStreamException, IOException {

        File dfile = new File(fileName);
        factory = XMLOutputFactory.newInstance();
        writer = factory.createXMLStreamWriter(new FileWriter(dfile));
        //writer.writeStartDocument("1.0");
    }

    public void openXMLDocument() throws XMLStreamException {
        writer.writeStartDocument("1.0");
    }

    public void endXMLDocument() throws XMLStreamException {
        writer.writeEndDocument();
    }

    public void writeXMLHeader(String header) throws XMLStreamException {
        writer.writeStartElement(header);
    }

    public void closeXMLWriter() throws XMLStreamException {
        if (writer != null) {
            writer.flush();
            writer.close();
        }

    }

    public void writeHeader(String[] headers) {
        csvWriter.writeNext(headers);
    }

    public void writeToCSV(Demographics demo) {
        String demoArr[] = toArray(demo);
        csvWriter.writeNext(demoArr);
    }
    public void writeToCSV(String[] data){
        csvWriter.writeNext(data);
    }

    public String[] toArray(Demographics demo) {
        String[] demoArr = new String[27];
        demoArr[0] = String.valueOf(demo.getPatientID());
        demoArr[1] = String.valueOf(demo.getPatientUUID());
        demoArr[2] = String.valueOf(demo.getPepfarID());
        demoArr[3] = String.valueOf(demo.getHospID());
        demoArr[4] = String.valueOf(demo.geteHNID());
        demoArr[5] = String.valueOf(demo.getOtherID());
        demoArr[6] = String.valueOf(demo.getFirstName());
        demoArr[7] = String.valueOf(demo.getLastName());
        demoArr[8] = String.valueOf(demo.getMiddleName());
        demoArr[9] = formatDate(demo.getAdultEnrollmentDt());
        demoArr[10] = formatDate(demo.getPeadEnrollmentDt());
        demoArr[11] = formatDate(demo.getPmtctEnrollmentDt());
        demoArr[12] = formatDate(demo.getHeiEnrollmentDt());
        demoArr[13] = formatDate(demo.getPepEnrollmentDt());
        demoArr[14] = formatDate(demo.getDateOfBirth());
        demoArr[15] = String.valueOf(demo.getAge());
        demoArr[16] = demo.getGender();
        demoArr[17] = demo.getAddress1();
        demoArr[18] = demo.getAddress2();
        demoArr[19] = demo.getAddress_lga();
        demoArr[20] = String.valueOf(demo.getCreatorID());
        demoArr[21] = formatDate(demo.getDateCreated());
        demoArr[22] = String.valueOf(demo.getVoided());
        demoArr[23] = formatDate(demo.getDateChanged());
        demoArr[24] = String.valueOf(demo.getLocationID());
        demoArr[25] = String.valueOf(demo.getCreatorName());
        demoArr[26] = demo.getLocationName();
        return demoArr;
    }

    public void writeToXML(User usr) throws XMLStreamException {
        writer.writeStartElement("user");

        writer.writeStartElement("user_id");
        writer.writeCharacters(String.valueOf(usr.getUser_id()));
        writer.writeEndElement();

        writer.writeStartElement("person_id");
        writer.writeCharacters(String.valueOf(usr.getPerson_id()));
        writer.writeEndElement();

        writer.writeStartElement("username");
        writer.writeCharacters(String.valueOf(usr.getUserName()));
        writer.writeEndElement();

        writer.writeStartElement("fullname");
        writer.writeCharacters(String.valueOf(usr.getFullName()));
        writer.writeEndElement();

        writer.writeStartElement("given_name");
        writer.writeCharacters(String.valueOf(usr.getFirstName()));
        writer.writeEndElement();

        writer.writeStartElement("family_name");
        writer.writeCharacters(String.valueOf(usr.getLastName()));
        writer.writeEndElement();

        writer.writeStartElement("gender");
        writer.writeCharacters(String.valueOf(usr.getGender()));
        writer.writeEndElement();

        writer.writeStartElement("date_created");
        writer.writeCharacters(formatDate(usr.getDateCreated()));
        writer.writeEndElement();

        writer.writeStartElement("creator");
        writer.writeCharacters(String.valueOf(usr.getCreator()));
        writer.writeEndElement();

        writer.writeStartElement("uuid");
        writer.writeCharacters(usr.getUuid());
        writer.writeEndElement();

        writer.writeStartElement("date_changed");
        writer.writeCharacters(formatDate(usr.getDateChanged()));
        writer.writeEndElement();

        writer.writeStartElement("retired");
        writer.writeCharacters(String.valueOf(usr.getRetired()));
        writer.writeEndElement();

        writer.writeStartElement("retired_by");
        writer.writeCharacters(String.valueOf(usr.getRetiredBy()));
        writer.writeEndElement();

        writer.writeEndElement();

    }

    public void writeToXML(Appointment apt) throws XMLStreamException {
        writer.writeStartElement("appointment");

        writer.writeStartElement("appointment_id");
        writer.writeCharacters(String.valueOf(apt.getAppointmentID()));
        writer.writeEndElement();

        writer.writeStartElement("patient_id");
        writer.writeCharacters(String.valueOf(apt.getPatientID()));
        writer.writeEndElement();

        writer.writeStartElement("pepfar_id");
        writer.writeCharacters(String.valueOf(apt.getPepfarID()));
        writer.writeEndElement();

        writer.writeStartElement("hosp_id");
        writer.writeCharacters(String.valueOf(apt.getHospID()));
        writer.writeEndElement();

        writer.writeStartElement("location_id");
        writer.writeCharacters(String.valueOf(apt.getLocationID()));
        writer.writeEndElement();

        writer.writeStartElement("provider_id");
        writer.writeCharacters(String.valueOf(apt.getProviderID()));
        writer.writeEndElement();

        writer.writeStartElement("appointment_date");
        writer.writeCharacters(formatDate(apt.getAppointmentDate()));
        writer.writeEndElement();

        writer.writeStartElement("provider_name");
        writer.writeCharacters(apt.getProviderName());
        writer.writeEndElement();

        writer.writeStartElement("other_phone_number");
        writer.writeCharacters(apt.getOtherPhoneNumbers());
        writer.writeEndElement();

        writer.writeStartElement("reason");
        writer.writeCharacters(apt.getReason());
        writer.writeEndElement();

        writer.writeStartElement("note");
        writer.writeCharacters(apt.getNote());
        writer.writeEndElement();

        writer.writeStartElement("attended");
        writer.writeCharacters(apt.getAttended());
        writer.writeEndElement();

        writer.writeStartElement("location_name");
        writer.writeCharacters(apt.getLocationName());
        writer.writeEndElement();

        writer.writeStartElement("voided");
        writer.writeCharacters(String.valueOf(apt.getVoided()));
        writer.writeEndElement();

        writer.writeEndElement();

    }
    public void writeToXML(Relationship rel) throws XMLStreamException{
        writer.writeStartElement("relationships");
         
        writer.writeStartElement("relationship_id");
        writer.writeCharacters(String.valueOf(rel.getRelationshipID()));
        writer.writeEndElement();
        
        writer.writeStartElement("person_a_name");
        writer.writeCharacters(String.valueOf(rel.getPersonAName()));
        writer.writeEndElement();
        
        writer.writeStartElement("person_b_name");
        writer.writeCharacters(String.valueOf(rel.getPersonBName()));
        writer.writeEndElement();
        
        writer.writeStartElement("person_b_name");
        writer.writeCharacters(String.valueOf(rel.getPersonBName()));
        writer.writeEndElement();
         
        writer.writeStartElement("a_is_to_b");
        writer.writeCharacters(String.valueOf(rel.getaIsToB()));
        writer.writeEndElement();
         
        writer.writeStartElement("b_is_to_a");
        writer.writeCharacters(String.valueOf(rel.getbIsToA()));
        writer.writeEndElement();
        
        writer.writeStartElement("entered_by");
        writer.writeCharacters(String.valueOf(rel.getEnteredBy()));
        writer.writeEndElement();
        
        writer.writeStartElement("date_entered");
        writer.writeCharacters(formatDate(rel.getDateEntered()));
        writer.writeEndElement();
        
        writer.writeStartElement("person_a_id");
        writer.writeCharacters(String.valueOf(rel.getPersonAID()));
        writer.writeEndElement();
        
        writer.writeStartElement("person_b_id");
        writer.writeCharacters(String.valueOf(rel.getPersonBID()));
        writer.writeEndElement();
        
        writer.writeStartElement("relationship_type_id");
        writer.writeCharacters(String.valueOf(rel.getRelationshipTypeID()));
        writer.writeEndElement();
        
        writer.writeStartElement("creator_id");
        writer.writeCharacters(String.valueOf(rel.getCreatorID()));
        writer.writeEndElement();
        
        writer.writeStartElement("voided");
        writer.writeCharacters(String.valueOf(rel.getVoided()));
        writer.writeEndElement();
        
        writer.writeStartElement("voided_by");
        writer.writeCharacters(String.valueOf(rel.getVoidedBy()));
        writer.writeEndElement();
        
        writer.writeStartElement("date_voided");
        writer.writeCharacters(formatDate(rel.getDateVoided()));
        writer.writeEndElement();
        
        writer.writeStartElement("uuid");
        writer.writeCharacters(rel.getUuid());
        writer.writeEndElement();
        
        
        writer.writeEndElement();
         
    }
    public void writeCSVHeaders(String[] headers) {
        csvWriter.writeNext(headers);
    }
    
    public void writeToXML(Concept concept) throws XMLStreamException{
        writer.writeStartElement("concept");
        
        writer.writeStartElement("CONCEPT_ID");
        writer.writeCharacters(String.valueOf(concept.getConceptID()));
        writer.writeEndElement();
        
        writer.writeStartElement("CONCEPT_NAME");
        writer.writeCharacters(String.valueOf(concept.getConceptName()));
        writer.writeEndElement();
        
        writer.writeStartElement("DATATYPE");
        writer.writeCharacters(String.valueOf(concept.getDataType()));
        writer.writeEndElement();
        
        writer.writeEndElement();
    }
    public void writeToXML(Obs obs) throws XMLStreamException {

        writer.writeStartElement("obs");
        
        writer.writeStartElement("OBS_ID");
        writer.writeCharacters(String.valueOf(obs.getObsID()));
        writer.writeEndElement();

        writer.writeStartElement("PATIENT_ID");
        writer.writeCharacters(String.valueOf(obs.getPatientID()));
        writer.writeEndElement();

        writer.writeStartElement("ENCOUNTER_ID");
        writer.writeCharacters(String.valueOf(obs.getEncounterID()));
        writer.writeEndElement();

        writer.writeStartElement("PEPFAR_ID");
        writer.writeCharacters(obs.getPepfarID());
        writer.writeEndElement();

        writer.writeStartElement("HOSP_ID");
        writer.writeCharacters(obs.getHospID());
        writer.writeEndElement();

        writer.writeStartElement("VISIT_DATE");
        writer.writeCharacters(formatDate(obs.getVisitDate()));
        writer.writeEndElement();

        writer.writeStartElement("PMM_FORM");
        writer.writeCharacters(obs.getFormName());
        writer.writeEndElement();

        writer.writeStartElement("CONCEPT_ID");
        writer.writeCharacters(String.valueOf(obs.getConceptID()));
        writer.writeEndElement();

        writer.writeStartElement("VARIABLE_NAME");
        writer.writeCharacters(obs.getVariableName());
        writer.writeEndElement();

        writer.writeStartElement("VARIABLE_VALUE");
        writer.writeCharacters(obs.getVariableValue());
        writer.writeEndElement();

        writer.writeStartElement("ENTERED_BY");
        writer.writeCharacters(obs.getEnteredBy());
        writer.writeEndElement();

        writer.writeStartElement("DATE_CREATED");
        writer.writeCharacters(formatDate(obs.getDateEntered()));
        writer.writeEndElement();

        writer.writeStartElement("DATE_CHANGED");
        writer.writeCharacters(formatDate(obs.getDateChanged()));
        writer.writeEndElement();

        writer.writeStartElement("PROVIDER");
        writer.writeCharacters(obs.getProvider());
        writer.writeEndElement();

        writer.writeStartElement("UUID");
        writer.writeCharacters(obs.getUuid());
        writer.writeEndElement();

        writer.writeStartElement("LOCATION");
        writer.writeCharacters(obs.getLocationName());
        writer.writeEndElement();

        writer.writeStartElement("LOCATION_ID");
        writer.writeCharacters(String.valueOf(obs.getLocationID()));
        writer.writeEndElement();

        writer.writeStartElement("CREATOR_ID");
        writer.writeCharacters(String.valueOf(obs.getCreator()));
        writer.writeEndElement();

        writer.writeStartElement("PROVIDER_ID");
        writer.writeCharacters(String.valueOf(obs.getProviderID()));
        writer.writeEndElement();

        writer.writeStartElement("VALUE_NUMERIC");
        writer.writeCharacters(String.valueOf(obs.getValueNumeric()));
        writer.writeEndElement();

        writer.writeStartElement("VALUE_DATETIME");
        writer.writeCharacters(formatDate(obs.getValueDate()));
        writer.writeEndElement();
        
        writer.writeStartElement("VALUE_CODED");
        writer.writeCharacters(String.valueOf(obs.getValueCoded()));
        writer.writeEndElement();

        writer.writeStartElement("VALUE_TEXT");
        writer.writeCharacters(obs.getValueText());
        writer.writeEndElement();

        writer.writeStartElement("VALUE_BOOL");
        writer.writeCharacters(String.valueOf(obs.isValueBoolean()));
        writer.writeEndElement();

        writer.writeStartElement("OBS_GROUP_ID");
        writer.writeCharacters(String.valueOf(obs.getObsGroupID()));
        writer.writeEndElement();

        writer.writeStartElement("VOIDED");
        writer.writeCharacters(String.valueOf(obs.getVoided()));
        writer.writeEndElement();

        writer.writeStartElement("DATE_VOIDED");
        writer.writeCharacters(formatDate(obs.getDateVoided()));
        writer.writeEndElement();

        writer.writeStartElement("VOIDED_BY");
        writer.writeCharacters(String.valueOf(obs.getVoidedBy()));
        writer.writeEndElement();

        writer.writeStartElement("CHANGED_BY");
        writer.writeCharacters(String.valueOf(obs.getChangedBy()));
        writer.writeEndElement();

        writer.writeStartElement("FORM_ID");
        writer.writeCharacters(String.valueOf(obs.getFormID()));
        writer.writeEndElement();

        writer.writeEndElement();

    }
    public void writeToCSV(Visit visit){
        String[] data=Visit.toArray(visit);
        csvWriter.writeNext(data);
    }
    public void writeToCSV(Obs obs) {
        String[] obsArr = toArray(obs);
        csvWriter.writeNext(obsArr);
    }
    public void writeToLab(Obs obs){
        String[] obsArr=toLabArray(obs);
        csvWriter.writeNext(obsArr);
    }
    public void writeToCSV(Appointment apt) {
        String[] aptArr = toArray(apt);
        csvWriter.writeNext(aptArr);
    }

    public void writeToCSV(DrugOrder order) {
        String[] orderArr = null;
        PatientRegimen pr = null;
        if (order instanceof PatientRegimen) {
            pr = (PatientRegimen) order;
            orderArr = toArray(pr);
        } else {
            orderArr = toArray(order);
        }
        csvWriter.writeNext(orderArr);
    }

    public String[] toArray(PatientRegimen regimen) {
        
        String[] rgm = new String[13];
        rgm[0] = String.valueOf(regimen.getOrderID());
        rgm[1] = String.valueOf(regimen.getPatientID());
        rgm[2] = String.valueOf(regimen.getPepfarID());
        rgm[3] = regimen.getHospID();
        rgm[4] = formatDate(regimen.getStartDate());
        rgm[5]=formatDate(regimen.getStopDate());
        rgm[6] = regimen.getDrugName();
        rgm[7] = regimen.getRegimenName();
        rgm[8]=regimen.getCode();
        rgm[9] = regimen.getRegimenLine();
        rgm[10] = regimen.getEnteredBy();
        rgm[11] = formatDate(regimen.getDateEntered());
        rgm[12] = String.valueOf(regimen.getCreator());

        return rgm;
    }

    public String[] toArray(DrugOrder order) {
        String[] ordArr = new String[13];
        ordArr[0]=String.valueOf(order.getOrderID());
        ordArr[1]=String.valueOf(order.getPatientID());
        ordArr[2]=order.getPepfarID();
        ordArr[3]=order.getHospID();
        ordArr[4]=order.getDrugName();
        ordArr[5]=String.valueOf(order.getConceptID());
        ordArr[6]=order.getDrugDose();
        ordArr[7]=order.getFrequency();
        ordArr[8]=String.valueOf(order.getQuantity());
        ordArr[9]=formatDate(order.getStartDate());
        ordArr[10]=formatDate(order.getStopDate());
        ordArr[11]=order.getEnteredBy();
        ordArr[12]=formatDate(order.getDateEntered());
        ordArr[13]=order.getUuid();
        return ordArr;
    }

    public String[] toArray(Obs obs) {
        String[] obsArr = new String[20];
        obsArr[0] = String.valueOf(obs.getObsID());
        obsArr[1] = obs.getPepfarID();
        obsArr[2] = obs.getHospID();
        obsArr[3] = formatDate(obs.getVisitDate());
        obsArr[4] = obs.getFormName();
        obsArr[5] = obs.getVariableName();
        obsArr[6] = obs.getVariableValue();
        obsArr[7] = obs.getEnteredBy();
        obsArr[8] = formatDate(obs.getDateEntered());
        obsArr[9] = formatDate(obs.getDateChanged());
        obsArr[10] = obs.getProvider();
        obsArr[11] = obs.getUuid();
        obsArr[12] = obs.getLocationName();
        obsArr[13] = String.valueOf(obs.getObsID());
        obsArr[14] = String.valueOf(obs.getPatientID());
        obsArr[15] = String.valueOf(obs.getEncounterID());
        obsArr[16] = String.valueOf(obs.getConceptID());
        obsArr[17] = String.valueOf(obs.getObsGroupID());
        obsArr[18] = String.valueOf(obs.getLocationID());
        obsArr[19] = String.valueOf(obs.getCreator());

        return obsArr;
    }
    public String[] toLabArray(Obs obs){
        String[] obsArr = new String[17];
        obsArr[0] = obs.getPepfarID();
        obsArr[1] = obs.getHospID();
        obsArr[2] = formatDate(obs.getVisitDate());
        obsArr[3] = obs.getFormName();
        obsArr[4] = obs.getVariableName();
        obsArr[5] = obs.getVariableValue();
        obsArr[6] = obs.getEnteredBy();
        obsArr[7] = formatDate(obs.getDateEntered());
        obsArr[8] = obs.getProvider();
        obsArr[9] = obs.getUuid();
        obsArr[10] = obs.getLocationName();
        obsArr[11] = String.valueOf(obs.getObsID());
        obsArr[12] = String.valueOf(obs.getPatientID());
        obsArr[13] = String.valueOf(obs.getEncounterID());
        obsArr[14] = String.valueOf(obs.getConceptID());
        obsArr[15] = String.valueOf(obs.getLocationID());
        obsArr[16] = String.valueOf(obs.getCreator());
        return obsArr;
    }

    public String[] toArray(Appointment apt) {
        String[] aptArr = new String[16];
        aptArr[0] = apt.getPepfarID();
        aptArr[1] = apt.getHospID();
        aptArr[2] = formatDate(apt.getAppointmentDate());
        aptArr[3] = apt.getProviderName();
        aptArr[4] = apt.getPhoneNumber();
        aptArr[5] = apt.getEmail();
        aptArr[6] = apt.getOtherPhoneNumbers();
        aptArr[7] = apt.getReason();
        aptArr[8] = apt.getNote();
        aptArr[9] = apt.getAttended();
        aptArr[10] = apt.getLocationName();
        aptArr[11]=String.valueOf(apt.getVoided());
        aptArr[11] = String.valueOf(apt.getAppointmentID());
        aptArr[12] = String.valueOf(apt.getPatientID());
        aptArr[13] = String.valueOf(apt.getLocationID());
        aptArr[14] = String.valueOf(apt.getProviderID());
        return aptArr;
    }

    public void writeToXML(PatientRegimen regimen) throws XMLStreamException {
        String[] headers = {"PATIENT_ID", "PEPFAR_ID", "HOSP_ID", "VISIT_DATE", "STOP_DATE","DRUGS", "REGIMEN","REGIMEN_CODE", "REGIMENLINE", "ENTERED_BY", "DATE_ENTERED", "CREATOR_ID"};
        writer.writeStartElement("regimen");
        
        writer.writeStartElement("ORDER_ID");
        writer.writeCharacters(String.valueOf(regimen.getOrderID()));
        writer.writeEndElement();
        
        writer.writeStartElement("PATIENT_ID");
        writer.writeCharacters(String.valueOf(regimen.getPatientID()));
        writer.writeEndElement();

        writer.writeStartElement("PEPFAR_ID");
        writer.writeCharacters(regimen.getPepfarID());
        writer.writeEndElement();

        writer.writeStartElement("HOSP_ID");
        writer.writeCharacters(regimen.getHospID());
        writer.writeEndElement();

        writer.writeStartElement("VISIT_DATE");
        writer.writeCharacters(formatDate(regimen.getStartDate()));
        writer.writeEndElement();
        
        writer.writeStartElement("STOP_DATE");
        writer.writeCharacters(formatDate(regimen.getStopDate()));
        writer.writeEndElement();

        writer.writeStartElement("DRUGS");
        writer.writeCharacters(regimen.getDrugName());
        writer.writeEndElement();

       
        writer.writeStartElement("REGIMEN");
        writer.writeCharacters(regimen.getRegimenName());
        writer.writeEndElement();
        
        writer.writeStartElement("REGIMEN_CODE");
        writer.writeCharacters(regimen.getCode());
        writer.writeEndElement();

        writer.writeStartElement("REGIMENLINE");
        writer.writeCharacters(regimen.getRegimenLine());
        writer.writeEndElement();

        writer.writeStartElement("ENTERED_BY");
        writer.writeCharacters(regimen.getEnteredBy());
        writer.writeEndElement();

        writer.writeStartElement("DATE_ENTERED");
        writer.writeCharacters(formatDate(regimen.getDateEntered()));
        writer.writeEndElement();

        writer.writeStartElement("CREATOR_ID");
        writer.writeCharacters(String.valueOf(regimen.getCreator()));
        writer.writeEndElement();

        writer.writeEndElement();

    }

    public void writeToXML(DrugOrder ord) throws XMLStreamException {
        if (ord instanceof PatientRegimen) {
            PatientRegimen rgm = (PatientRegimen) ord;
            writeToXML(rgm);
        } else {
            writer.writeStartElement("drug");

            writer.writeStartElement("ORDER_ID");
            writer.writeCharacters(String.valueOf(ord.getOrderID()));
            writer.writeEndElement();
            
            writer.writeStartElement("PATIENT_ID");
            writer.writeCharacters(String.valueOf(ord.getPatientID()));
            writer.writeEndElement();

            writer.writeStartElement("PEPFAR_ID");
            writer.writeCharacters(ord.getPepfarID());
            writer.writeEndElement();

            writer.writeStartElement("HOSP_ID");
            writer.writeCharacters(ord.getHospID());
            writer.writeEndElement();

            writer.writeStartElement("DRUG");
            writer.writeCharacters(ord.getDrugName());
            writer.writeEndElement();

            writer.writeStartElement("CONCEPT_ID");
            writer.writeCharacters(String.valueOf(ord.getConceptID()));
            writer.writeEndElement();

            writer.writeStartElement("FREQUENCY");
            writer.writeCharacters(ord.getFrequency());
            writer.writeEndElement();

            writer.writeStartElement("DOSE");
            writer.writeCharacters(ord.getDrugDose());
            writer.writeEndElement();

            writer.writeStartElement("QUANTITY");
            writer.writeCharacters(String.valueOf(ord.getQuantity()));
            writer.writeEndElement();

            writer.writeStartElement("START_DATE");
            writer.writeCharacters(formatDate(ord.getStartDate()));
            writer.writeEndElement();

            writer.writeStartElement("STOP_DATE");
            writer.writeCharacters(formatDate(ord.getStopDate()));
            writer.writeEndElement();

            writer.writeStartElement("ENTERED_BY");
            writer.writeCharacters(ord.getEnteredBy());
            writer.writeEndElement();

            writer.writeStartElement("DATE_ENTERED");
            writer.writeCharacters(formatDate(ord.getDateEntered()));
            writer.writeEndElement();

            writer.writeStartElement("UUID");
            writer.writeCharacters(ord.getUuid());
            writer.writeEndElement();

            writer.writeEndElement();
        }

    }

    public void writeToXML(Demographics demo) throws XMLStreamException {
        writer.writeStartElement("patient");

        writer.writeStartElement("person_source_pk");
        writer.writeCharacters(String.valueOf(demo.getPatientID()));
        writer.writeEndElement();

        writer.writeStartElement("person_uuid");
        writer.writeCharacters(demo.getPatientUUID());
        writer.writeEndElement();

        writer.writeStartElement("pepfar_id");
        writer.writeCharacters(demo.getPepfarID());
        writer.writeEndElement();

        writer.writeStartElement("hosp_id");
        writer.writeCharacters(demo.getHospID());
        writer.writeEndElement();

        writer.writeStartElement("ehnid");
        writer.writeCharacters(demo.geteHNID());
        writer.writeEndElement();

        writer.writeStartElement("other_id");
        writer.writeCharacters(demo.getOtherID());
        writer.writeEndElement();

        writer.writeStartElement("first_name");
        writer.writeCharacters(demo.getFirstName());
        writer.writeEndElement();

        writer.writeStartElement("last_name");
        writer.writeCharacters(demo.getLastName());
        writer.writeEndElement();

        writer.writeStartElement("middle_name");
        writer.writeCharacters(demo.getMiddleName());
        writer.writeEndElement();

        writer.writeStartElement("middle_name");
        writer.writeCharacters(demo.getMiddleName());
        writer.writeEndElement();

        writer.writeStartElement("adult_enrollment_dt");
        writer.writeCharacters(formatDate(demo.getAdultEnrollmentDt()));
        writer.writeEndElement();

        writer.writeStartElement("pead_enrollment_dt");
        writer.writeCharacters(formatDate(demo.getPeadEnrollmentDt()));
        writer.writeEndElement();

        writer.writeStartElement("pmtct_enrollment_dt");
        writer.writeCharacters(formatDate(demo.getPmtctEnrollmentDt()));
        writer.writeEndElement();

        writer.writeStartElement("hei_enrollment_dt");
        writer.writeCharacters(formatDate(demo.getHeiEnrollmentDt()));
        writer.writeEndElement();

        writer.writeStartElement("pep_enrollment_dt");
        writer.writeCharacters(formatDate(demo.getPepEnrollmentDt()));
        writer.writeEndElement();

        writer.writeStartElement("dob");
        writer.writeCharacters(formatDate(demo.getDateOfBirth()));
        writer.writeEndElement();
        
        writer.writeStartElement("age");
        writer.writeCharacters(String.valueOf(demo.getAge()));
        writer.writeEndElement();
        
        writer.writeStartElement("gender");
        writer.writeCharacters(demo.getGender());
        writer.writeEndElement();

        writer.writeStartElement("address1");
        writer.writeCharacters(demo.getAddress1());
        writer.writeEndElement();

        writer.writeStartElement("address2");
        writer.writeCharacters(demo.getAddress2());
        writer.writeEndElement();

        writer.writeStartElement("address_lga");
        writer.writeCharacters(demo.getAddress_lga());
        writer.writeEndElement();

        writer.writeStartElement("address_state");
        writer.writeCharacters(demo.getAddress_state());
        writer.writeEndElement();

        writer.writeStartElement("creator_id");
        writer.writeCharacters(String.valueOf(demo.getCreatorID()));
        writer.writeEndElement();

        writer.writeStartElement("date_created");
        writer.writeCharacters(formatDate(demo.getDateCreated()));
        writer.writeEndElement();

        writer.writeStartElement("date_changed");
        writer.writeCharacters(formatDate(demo.getDateChanged()));
        writer.writeEndElement();

        writer.writeStartElement("location_id");
        writer.writeCharacters(String.valueOf(demo.getLocationID()));
        writer.writeEndElement();

        writer.writeStartElement("creator");
        writer.writeCharacters(demo.getCreatorName());
        writer.writeEndElement();

        writer.writeStartElement("location");
        writer.writeCharacters(demo.getLocationName());
        writer.writeEndElement();

        writer.writeEndElement();

    }

    public void closeWriters() throws XMLStreamException {
        if (writer != null) {
            writer.close();
        }
    }

    public void writeToXML(VitalSign vs) {

    }

    public void writeHeaders(String[] headers) {
        csvWriter.writeNext(headers);
    }

    public void writeToCSV(VitalSign vs) {
        String[] vsarr = toArray(vs);
        csvWriter.writeNext(vsarr);
    }

    public String[] toArray(VitalSign vs) {
        String[] vsarr = new String[12];
        vsarr[0] = String.valueOf(vs.getPatientID());
        vsarr[1] = vs.getPepfarID();
        vsarr[2] = vs.getHospID();
        vsarr[3] = vs.getVisitDate();
        vsarr[4] = vs.getVitalSignName();
        vsarr[5] = vs.getVitalSignValue();
        vsarr[6] = vs.getFormName();
        vsarr[7] = String.valueOf(vs.getFormID());
        vsarr[8] = vs.getDateCreated();
        vsarr[9] = String.valueOf(vs.getFormID());
        vsarr[10] = String.valueOf(vs.getCreatorID());
        vsarr[11] = String.valueOf(vs.getLocationID());
        return vsarr;
    }

    public void closeCSVWriter() throws IOException {
        if (csvWriter != null) {
            csvWriter.close();
        }
        if (csvReader != null) {
            csvReader.close();
        }
    }
    public List<String[]> loadDataFromFile(String csvPath) throws FileNotFoundException,IOException{
        InputStreamReader isr;
        //FileReader fr;
        //fr=new FileReader
        isr = new InputStreamReader(getClass().getResourceAsStream(csvPath));
        BufferedReader br = new BufferedReader(isr);
        csvReader=new CSVReader(br);
        List<String[]> data=csvReader.readAll();
        closeCSVWriter();
        return data;
    }
    public List<String[]> loadDataFromFile(File csvFile) throws FileNotFoundException,IOException{
        //InputStreamReader isr;
        FileReader fr;
        fr=new FileReader(csvFile);
        //isr = new InputStreamReader(getClass().getResourceAsStream(csvPath));
        BufferedReader br = new BufferedReader(fr);
        csvReader=new CSVReader(br);
        List<String[]> data=csvReader.readAll();
        closeCSVWriter();
        return data;
    }
    public void writeXMLHeader(String header, String nameSpace) throws XMLStreamException {
        //writer.writeStartElement("xsi",nameSpace, header);
        writer.writeStartElement(header);
        // writer.writeNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        //writer.writeS, header);
    }
    public void writeToXML(DataPatientDemographics demo) throws XMLStreamException {
        /*
        <Lastname>MICHAEL</Lastname>
		<Firstname>ROSE</Firstname>
		<ClinicalVisit6MonthsPriorToReview>NO             </ClinicalVisit6MonthsPriorToReview>
		<MaritalStatus>Married</MaritalStatus>
		<HospitalNo>23842</HospitalNo>
		<RNL_SerialNo>69</RNL_SerialNo>
		<Gender>Female         </Gender>
		<DateOfBirth>'03/01/1979</DateOfBirth>
		<Age>35</Age>
		<HospitalAdmissionDuringReview>NO             </HospitalAdmissionDuringReview>
		<Occupation>Unemployed</Occupation>
		<Education>Senior Secondary</Education>
		<WardVillageTown_OfResidence>KAKURI</WardVillageTown_OfResidence>
		<LGA_OfResidence>KADUNA SOUTH</LGA_OfResidence>
		<State_OfResidence>KADUNA</State_OfResidence>
		<State_OfOrigin>BENUE</State_OfOrigin>
		<Tribe>IDOMA</Tribe>
		<FacilityID>7148</FacilityID>
		<PatientID>OO000002222</PatientID>
		<DateEnrolled>'04/30/2007</DateEnrolled>
		<RecordCompletionPosition>14</RecordCompletionPosition>
		<UploaderId>NULL</UploaderId>
		<UploadDt>NULL</UploadDt>
		<webUploadFlag>No</webUploadFlag>
		<ReviewPeriodID>3</ReviewPeriodID>
        
         */

        writer.writeStartElement("PatientDemographics_Record");

        writer.writeStartElement("Lastname");
        writer.writeCharacters(StringUtils.trim(demo.getLastName()));
        writer.writeEndElement();

        writer.writeStartElement("Firstname");
        writer.writeCharacters(demo.getFirstName());
        writer.writeEndElement();

        writer.writeStartElement("ClinicalVisit6MonthsPriorToReview");
        writer.writeCharacters(demo.getClinicVisitReviewPeriod());
        writer.writeEndElement();

        writer.writeStartElement("MaritalStatus");
        writer.writeCharacters(demo.getMaritalStatus());
        writer.writeEndElement();

        writer.writeStartElement("HospitalNo");
        writer.writeCharacters(demo.getHospID());
        writer.writeEndElement();

        writer.writeStartElement("RNL_SerialNo");
        writer.writeCharacters(demo.getRnl());
        writer.writeEndElement();

        writer.writeStartElement("Gender");
        writer.writeCharacters(demo.getGender());
        writer.writeEndElement();

        writer.writeStartElement("DateOfBirth");
        writer.writeCharacters(formatDate2(demo.getDob()));
        writer.writeEndElement();

        writer.writeStartElement("Age");
        writer.writeCharacters(String.valueOf(demo.getAge()));
        writer.writeEndElement();

        writer.writeStartElement("HospitalAdmissionDuringReview");
        writer.writeCharacters(demo.getHosptalAdmissionReviewPeriod());
        writer.writeEndElement();

        writer.writeStartElement("Occupation");
        writer.writeCharacters(demo.getOccupation());
        writer.writeEndElement();

        writer.writeStartElement("Education");
        writer.writeCharacters(StringEscapeUtils.escapeXml(demo.getEducation()));
        writer.writeEndElement();

        writer.writeStartElement("WardVillageTown_OfResidence");
        writer.writeCharacters(StringEscapeUtils.escapeXml(demo.getWardVillageTown()));
        writer.writeEndElement();

        writer.writeStartElement("LGA_OfResidence");
        writer.writeCharacters(StringEscapeUtils.escapeXml(StringUtils.abbreviate(StringUtils.upperCase(demo.getLgaOfResidence()),20)));
        writer.writeEndElement();

        writer.writeStartElement("State_OfResidence");
        writer.writeCharacters(StringEscapeUtils.escapeXml(StringUtils.abbreviate(StringUtils.upperCase(demo.getStateOfResidence()),20)));
        writer.writeEndElement();

        writer.writeStartElement("State_OfOrigin");
        writer.writeCharacters(StringEscapeUtils.escapeXml(StringUtils.abbreviate(StringUtils.upperCase(demo.getStateOfOrigin()),20)));
        writer.writeEndElement();

        writer.writeStartElement("Tribe");
        writer.writeCharacters(demo.getTribe());
        writer.writeEndElement();

       /* writer.writeStartElement("FacilityID");
        writer.writeCharacters(String.valueOf(demo.getFacilityID()));
        writer.writeEndElement();*/

        writer.writeStartElement("PatientID");
        writer.writeCharacters(StringUtils.trim(demo.getPatientID()));
        writer.writeEndElement();

        writer.writeStartElement("DateEnrolled");
        writer.writeCharacters(formatDate2(demo.getDateEnrolled()));
        writer.writeEndElement();
        
        writer.writeStartElement("HasThePatientHadaClinicalVisit3MonthsPriorToTheReviewPeriod");
        writer.writeCharacters(demo.getClinicVisit3MonthsReviewPeriod());
        writer.writeEndElement();
        
        writer.writeStartElement("DateTestedPositive");
        writer.writeCharacters(formatDate2(demo.getDateConfirmedPositive()));
        writer.writeEndElement();

        writer.writeStartElement("RecordCompletionPosition");
        writer.writeCharacters(String.valueOf(demo.getRecordCompletionPosition()));
        writer.writeEndElement();

        writer.writeStartElement("UploaderId");
        writer.writeCharacters(String.valueOf(demo.getUploaderID()));
        writer.writeEndElement();

        /*writer.writeStartElement("UploadDt");
        writer.writeCharacters(formatDate2(demo.getUploadDate()));
        writer.writeEndElement();

        writer.writeStartElement("webUploadFlag");
        writer.writeCharacters(demo.getWebUploadFlag());
        writer.writeEndElement();

        writer.writeStartElement("ReviewPeriod");
        writer.writeCharacters(String.valueOf(demo.getReviewPeriodID()));
        writer.writeEndElement();*/

        writer.writeEndElement();

    }
    public void writeToXML(DataARTRecord dataARTRecord) throws XMLStreamException {
        //if (!dataARTRecord.getPatientID().isEmpty()) {
        writer.writeStartElement("ART_Record");

        writer.writeStartElement("PatientID");
        writer.writeCharacters(StringUtils.trim(dataARTRecord.getPatientID()));
        writer.writeEndElement();

        writer.writeStartElement("PatientEverStartedOnART");
        writer.writeCharacters(dataARTRecord.getPatientEverStartedOnART());
        writer.writeEndElement();

        writer.writeStartElement("ART_Start_Date");
        writer.writeCharacters(formatDate2(dataARTRecord.getArtStartDate()));
        writer.writeEndElement();

        writer.writeStartElement("TreatmentPrepCompletedBeforeStartOfART");
        writer.writeCharacters(dataARTRecord.getTreatmentPrepationCompletedBeforeARTStart());
        writer.writeEndElement();

        writer.writeStartElement("FacilityID");
        writer.writeCharacters(String.valueOf(dataARTRecord.getFacilityID()));
        writer.writeEndElement();

        writer.writeStartElement("UploaderId");
        writer.writeCharacters(String.valueOf(dataARTRecord.getUploaderID()));
        writer.writeEndElement();

        writer.writeStartElement("UploadDt");
        writer.writeCharacters(formatDate2(dataARTRecord.getUploadDate()));
        writer.writeEndElement();

        writer.writeStartElement("webUploadFlag");
        writer.writeCharacters(dataARTRecord.getWebUploadFlag());
        writer.writeEndElement();

        writer.writeStartElement("ReviewPeriodID");
        writer.writeCharacters(String.valueOf(dataARTRecord.getReviewPeriodID()));
        writer.writeEndElement();

        writer.writeEndElement();

        /*
        <ART_Record>
		<PatientID>OO000002222</PatientID>
		<PatientEverStartedOnART>YES       </PatientEverStartedOnART>
		<ART_Start_Date>'3/14/2008</ART_Start_Date>
		<TreatmentPrepCompletedBeforeStartOfART>YES       </TreatmentPrepCompletedBeforeStartOfART>
		<FacilityID>7148</FacilityID>
		<UploaderId>NULL</UploaderId>
		<UploadDt>NULL</UploadDt>
		<webUploadFlag>No</webUploadFlag>
		<ReviewPeriodID>NULL</ReviewPeriodID>
	</ART_Record>
         */
    }
    public void writeToXML(DataBaselineParameters baselineParameter) throws XMLStreamException {
        if (baselineParameter != null && baselineParameter.getPatientID() != null && !baselineParameter.getPatientID().isEmpty()) {
            writer.writeStartElement("BaselineParameters_Record");

            writer.writeStartElement("PatientID");
            writer.writeCharacters(StringUtils.trim(baselineParameter.getPatientID()));
            writer.writeEndElement();

            writer.writeStartElement("CD4_Count");
            writer.writeCharacters(String.valueOf(baselineParameter.getCd4Count()));
            writer.writeEndElement();

            writer.writeStartElement("CD4_Count_Date");
            writer.writeCharacters(formatDate2(baselineParameter.getCd4CountDate()));
            writer.writeEndElement();

            writer.writeStartElement("Weight");
            writer.writeCharacters(String.valueOf(baselineParameter.getWeight()));
            writer.writeEndElement();

            writer.writeStartElement("Weight_Date");
            writer.writeCharacters(formatDate2(baselineParameter.getWeightDate()));
            writer.writeEndElement();

            writer.writeStartElement("WHO_Clinical_Stage");
            writer.writeCharacters(String.valueOf(baselineParameter.getWhoStaging()));
            writer.writeEndElement();

            writer.writeStartElement("WHO_Clinical_State_Date");
            writer.writeCharacters(formatDate2(baselineParameter.getWhoStagingDate()));
            writer.writeEndElement();
            
            

            /*writer.writeStartElement("FacilityID");
            writer.writeCharacters(String.valueOf(baselineParameter.getFacilityID()));
            writer.writeEndElement();

            writer.writeStartElement("UploaderId");
            writer.writeCharacters(String.valueOf(baselineParameter.getUploaderID()));
            writer.writeEndElement();

            writer.writeStartElement("UploadDT");
            writer.writeCharacters(formatDate2(baselineParameter.getUploadDate()));
            writer.writeEndElement();

            writer.writeStartElement("webUploadFlag");
            writer.writeCharacters(baselineParameter.getWebUploadFlag());
            writer.writeEndElement();

            writer.writeStartElement("ReviewPeriodID");
            writer.writeCharacters(String.valueOf(baselineParameter.getReviewPeriodID()));
            writer.writeEndElement();*/

            writer.writeEndElement();
        }

    }
    
    public String formatDefault2(String d){
        String date=d;
        if(d!=null && d.isEmpty()){
            date="1/1/1900";
        }
        return date;
    }
    public String formatDate2(Date date) {
        String dateString = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            dateString = df.format(date);
        }
        return dateString;

    }
    public void writeToXML(DataPatientStatusReviewPeriod pstatus) throws XMLStreamException {
        writer.writeStartElement("PatientStatusDuringReviewPeriod_Record");

        writer.writeStartElement("PatientID");
        writer.writeCharacters(StringUtils.trim(pstatus.getPatientID()));
        writer.writeEndElement();

        writer.writeStartElement("Status");
        writer.writeCharacters(pstatus.getStatus());
        writer.writeEndElement();

        writer.writeStartElement("DateOfStatusChange");
        writer.writeCharacters(formatDate2(pstatus.getDateOfStatusChange()));
        writer.writeEndElement();

        writer.writeStartElement("ReasonForStatusChange");
        writer.writeCharacters(pstatus.getReasonForStatusChange());
        writer.writeEndElement();

        writer.writeStartElement("ID");
        writer.writeCharacters(pstatus.getId());
        writer.writeEndElement();

        writer.writeStartElement("FacilityID");
        writer.writeCharacters(String.valueOf(pstatus.getFacilityID()));
        writer.writeEndElement();

        writer.writeStartElement("UploaderId");
        writer.writeCharacters(pstatus.getUploaderID());
        writer.writeEndElement();

        writer.writeStartElement("UploadDt");
        writer.writeCharacters(formatDate2(pstatus.getUploadDt()));
        writer.writeEndElement();

        writer.writeStartElement("webUploadFlag");
        writer.writeCharacters(pstatus.getWebUploadFlag());
        writer.writeEndElement();

        writer.writeStartElement("Transferred_Out");
        writer.writeCharacters(pstatus.getTransferredOut());
        writer.writeEndElement();

        writer.writeStartElement("Death");
        writer.writeCharacters(pstatus.getDeath());
        writer.writeEndElement();

        writer.writeStartElement("Discontinued_Care");
        writer.writeCharacters(pstatus.getDiscontinuedCare());
        writer.writeEndElement();

        writer.writeStartElement("Transferred_Out_Date");
        writer.writeCharacters(formatDate2(pstatus.getTransferredOutDate()));
        writer.writeEndElement();

        writer.writeStartElement("Death_Date");
        writer.writeCharacters(formatDate2(pstatus.getDeathDate()));
        writer.writeEndElement();

        writer.writeStartElement("Discontinued_Care_Date");
        writer.writeCharacters(formatDate2(pstatus.getDiscontinuedCareDate()));
        writer.writeEndElement();

        writer.writeStartElement("Discontinued_Care_Reason");
        writer.writeCharacters(pstatus.getDiscontinuedCareReason());
        writer.writeEndElement();

        writer.writeStartElement("Discontinued_Care_Reason_Other");
        writer.writeCharacters(pstatus.getDiscontinuedCareReasonOther());
        writer.writeEndElement();

        writer.writeStartElement("ReviewPeriodID");
        writer.writeCharacters(String.valueOf(pstatus.getReviewPeriodID()));
        writer.writeEndElement();

        writer.writeEndElement();

        /*
    PatientID
Status
DateOfStatusChange
ReasonForStatusChange
ID
FacilityID
UploaderId
UploadDt
webUploadFlag
Transferred_Out
Death
Discontinued_Care
Transferred_Out_Date
Death_Date
Discontinued_Care_Date
Discontinued_Care_Reason
Discontinued_Care_Reason_Other
ReviewPeriodID
         */
    }
     public void writeToXML(DataCotrimoxazole dc) throws XMLStreamException {
        //writer.writeStartElement("Data-set");
        writer.writeStartElement("COTRIMOXAZOLE_Record");

        writer.writeStartElement("PatientID");
        writer.writeCharacters(StringUtils.trim(dc.getPatientID()));
        writer.writeEndElement();

        writer.writeStartElement("PatientReceiveCotrimoxazoleDuringReviewPeriod");
        writer.writeCharacters(dc.getPatientReceiveCotrimoxazoleDuringReviewPeriod());
        writer.writeEndElement();

        writer.writeStartElement("PatientCurrentlyOnCotrimoxazoleProphylaxis");
        writer.writeCharacters(dc.getPatientCurrentlyOnCotrimoxazoleProphylaxis());
        writer.writeEndElement();

        writer.writeStartElement("DateOfLastPrescription");
        writer.writeCharacters(formatDate2(dc.getDateOfLastPrescription()));
        writer.writeEndElement();

        writer.writeStartElement("FacilityID");
        writer.writeCharacters(String.valueOf(dc.getFacilityID()));
        writer.writeEndElement();

        writer.writeStartElement("UploaderId");
        writer.writeCharacters(dc.getUploaderID());
        writer.writeEndElement();

        writer.writeStartElement("UploadDt");
        writer.writeCharacters(formatDate2(dc.getUploadDate()));
        writer.writeEndElement();

        writer.writeStartElement("webUploadFlag");
        writer.writeCharacters(dc.getWebUploadFlag());
        writer.writeEndElement();

        writer.writeStartElement("ReviewPeriodID");
        writer.writeCharacters(String.valueOf(dc.getReviewPeriodID()));
        writer.writeEndElement();

        writer.writeEndElement();
        //writer.writeEndElement();
    }

    public void writeToXML(DataRegimenDuringReview drr) throws XMLStreamException {
        if (!drr.getPatientID().isEmpty()) {
            writer.writeStartElement("ARTRegimenDuringReviewPeriod_Record");

            writer.writeStartElement("PatientID");
            writer.writeCharacters(StringUtils.trim(drr.getPatientID()));
            writer.writeEndElement();

            writer.writeStartElement("PatientOnARTFirstDayOfReviewPeriod");
            writer.writeCharacters(drr.getPatientOnARTFirstDayOfReviewPeriod());
            writer.writeEndElement();

            writer.writeStartElement("PatientOnARTAnytimeDuringReviewPeriod");
            writer.writeCharacters(drr.getPatientOnARTAnytimeDuringReviewPeriod());
            writer.writeEndElement();

            writer.writeStartElement("_1stRegminen");
            writer.writeCharacters(drr.getFirstRegimen());
            writer.writeEndElement();

            writer.writeStartElement("_1stRegimenStartDate");
            writer.writeCharacters(formatDefault2(formatDate2(drr.getFirstRegimenStartDate())));
            writer.writeEndElement();

            writer.writeStartElement("_1stRegimenChangeDate");
            writer.writeCharacters(formatDefault2(formatDate2(drr.getFirstRegimenChangeDate())));
            writer.writeEndElement();

            writer.writeStartElement("_2ndRegimen");
            writer.writeCharacters(drr.getSecondRegimen());
            writer.writeEndElement();

            writer.writeStartElement("_2ndRegimenStartDate");
            writer.writeCharacters(formatDefault2(formatDate2(drr.getSecondRegimenStartDate())));
            writer.writeEndElement();

            writer.writeStartElement("_2ndRegimenChangeDate");
            writer.writeCharacters(formatDefault2(formatDate2(drr.getSecondRegimenChangeDate())));
            writer.writeEndElement();

            writer.writeStartElement("_3rdRegimen");
            writer.writeCharacters(drr.getThirdRegimen());
            writer.writeEndElement();

            writer.writeStartElement("_3rdRegimenStartDate");
            writer.writeCharacters(formatDefault2(formatDate2(drr.getThirdRegimenStartDate())));
            writer.writeEndElement();

            writer.writeStartElement("_3rdRegimenChangeDate");
            writer.writeCharacters(formatDefault2(formatDate2(drr.getThirdRegimenChangeDate())));
            writer.writeEndElement();

            writer.writeStartElement("OtherRegimenSpecify");
            writer.writeCharacters(zeroIfNull(drr.getOtherRegimenSpecify()));
            writer.writeEndElement();

            writer.writeStartElement("DateOfLastDrugPickup");
            writer.writeCharacters(formatDefault2(formatDate2(drr.getDateOfLastARTPickup())));
            writer.writeEndElement();

            writer.writeStartElement("DurationOfMedicationCoverageInMonths");
            writer.writeCharacters(String.valueOf(drr.getDurationOfMedicationCoverageInMonths()));
            writer.writeEndElement();

            writer.writeStartElement("FacilityID");
            writer.writeCharacters(String.valueOf(drr.getFacilityID()));
            writer.writeEndElement();

            writer.writeStartElement("UploaderId");
            writer.writeCharacters(String.valueOf(drr.getUploaderID()));
            writer.writeEndElement();

            writer.writeStartElement("webUploadFlag");
            writer.writeCharacters(String.valueOf(drr.getWebUploadFlag()));
            writer.writeEndElement();

            writer.writeStartElement("ReviewPeriodID");
            writer.writeCharacters(String.valueOf(drr.getReviewPeriodID()));
            writer.writeEndElement();
            writer.writeEndElement();
        }
       

}
    public String zeroIfNull(String val){
       String ans=val;
       if(StringUtils.isBlank(val)|| StringUtils.isEmpty(val)){
           ans="0";
       }
       return ans;
   }
     public void writeToXML(DataClinicalEvaluationInReviewPeriod dceval) throws XMLStreamException {

        writer.writeStartElement("ClinicalEvaluationVisitsInReviewPeriod_Record");

        writer.writeStartElement("PatientID");
        writer.writeCharacters(StringUtils.trim(dceval.getPatientID()));
        writer.writeEndElement();

        writer.writeStartElement("Visit1");
        writer.writeCharacters(formatDate2(dceval.getVisit1()));
        writer.writeEndElement();

        writer.writeStartElement("Visit2");
        writer.writeCharacters(formatDate2(dceval.getVisit2()));
        writer.writeEndElement();

        writer.writeStartElement("Visit3");
        writer.writeCharacters(formatDate2(dceval.getVisit3()));
        writer.writeEndElement();

        writer.writeStartElement("Visit4");
        writer.writeCharacters(formatDate2(dceval.getVisit4()));
        writer.writeEndElement();

        writer.writeStartElement("FacilityID");
        writer.writeCharacters(String.valueOf(dceval.getFacilityID()));
        writer.writeEndElement();

        writer.writeStartElement("UploaderId");
        writer.writeCharacters(dceval.getUploaderID());
        writer.writeEndElement();

        writer.writeStartElement("UploadDt");
        writer.writeCharacters(formatDate2(dceval.getUploadDate()));
        writer.writeEndElement();

        writer.writeStartElement("webUploadFlag");
        writer.writeCharacters(dceval.getWebUploadFlag());
        writer.writeEndElement();

        writer.writeStartElement("ReviewPeriodID");
        writer.writeCharacters(String.valueOf(dceval.getReviewPeriodID()));
        writer.writeEndElement();

        writer.writeEndElement();
        //writer.writeEndElement();
    }
     public void writeToXML(DataHepatitisB dhb) throws XMLStreamException {

        writer.writeStartElement("HepatitisB_Record");

        writer.writeStartElement("PatientID");
        writer.writeCharacters(StringUtils.trim(dhb.getPatientID()));
        writer.writeEndElement();

        writer.writeStartElement("HepatitisBAssayEverDoneForPatient");
        writer.writeCharacters(dhb.getHepatitisBAssayEverDoneForPatient());
        writer.writeEndElement();

        writer.writeStartElement("ResultOfHepatitisBAssay");
        writer.writeCharacters(dhb.getResultOfHepatitisBAssay());
        writer.writeEndElement();

        writer.writeStartElement("ClinicalEvaluationARTFormFilledAtLastVisit");
        writer.writeCharacters(dhb.getClinicalEvaluationARTFormFilledAtLastVisit());
        writer.writeEndElement();

        writer.writeStartElement("FacilityID");
        writer.writeCharacters(String.valueOf(dhb.getFacilityID()));
        writer.writeEndElement();

        writer.writeStartElement("UploaderId");
        writer.writeCharacters(dhb.getUploaderId());
        writer.writeEndElement();

        writer.writeStartElement("UploadDt");
        writer.writeCharacters(formatDate2(dhb.getUploadDt()));
        writer.writeEndElement();

        writer.writeStartElement("webUploadFlag");
        writer.writeCharacters(dhb.getWebUploadFlag());
        writer.writeEndElement();

        writer.writeStartElement("ReviewPeriodID");
        writer.writeCharacters(String.valueOf(dhb.getReviewPeriodID()));
        writer.writeEndElement();

        writer.writeEndElement();

    }
     public void writeToXML(DataPatientMonitoringReviewPeriod ele) throws XMLStreamException {
        writer.writeStartElement("PatientMonitoringDuringReviewPeriod_Record");

        writer.writeStartElement("PatientID");
        writer.writeCharacters(StringUtils.trim(ele.getPatientID()));
        writer.writeEndElement();

        writer.writeStartElement("ADH001_Value");
        writer.writeCharacters(String.valueOf(Math.round(ele.getCd4Value1())));
        writer.writeEndElement();

        writer.writeStartElement("ADH001_TestDate");
        writer.writeCharacters(formatDefault(formatDate2(ele.getCd4Value1Date())));
        writer.writeEndElement();

        writer.writeStartElement("ADH002_Value");
        writer.writeCharacters(String.valueOf(Math.round(ele.getCd4Value2())));
        writer.writeEndElement();

        writer.writeStartElement("ADH002_TestDate");
        writer.writeCharacters(formatDefault(formatDate2(ele.getCd4Value2Date())));
        writer.writeEndElement();

        writer.writeStartElement("ADH003_Value");
        writer.writeCharacters(String.valueOf(Math.round(ele.getCd4Value3())));
        writer.writeEndElement();

        writer.writeStartElement("ADH003_TestDate");
        writer.writeCharacters(formatDefault(formatDate2(ele.getCd4Value3Date())));
        writer.writeEndElement();

        writer.writeStartElement("ADH004_Value");
        writer.writeCharacters(String.valueOf(Math.round(ele.getCd4Value4())));
        writer.writeEndElement();

        writer.writeStartElement("ADH004_TestDate");
        writer.writeCharacters(formatDefault(formatDate2(ele.getCd4Value4Date())));
        writer.writeEndElement();

        writer.writeStartElement("ADH005_Value");
        writer.writeCharacters(String.valueOf(Math.round(ele.getPctHct1())));
        writer.writeEndElement();

        writer.writeStartElement("ADHOO5_TestDate");
        writer.writeCharacters(formatDefault(formatDate2(ele.getPctHct1Date())));
        writer.writeEndElement();

        writer.writeStartElement("ADH006_Value");
        writer.writeCharacters(String.valueOf(Math.round(ele.getPctHct2())));
        writer.writeEndElement();

        writer.writeStartElement("ADH006_TestDate");
        writer.writeCharacters(formatDefault(formatDate2(ele.getPctHct2Date())));
        writer.writeEndElement();

        writer.writeStartElement("ADH007_Value");
        writer.writeCharacters(String.valueOf(Math.round(ele.getPctHct3())));
        writer.writeEndElement();

        writer.writeStartElement("ADH007_TestDate");
        writer.writeCharacters(formatDefault(formatDate2(ele.getPctHct3Date())));
        writer.writeEndElement();

        writer.writeStartElement("ADH008_Value");
        writer.writeCharacters(String.valueOf(Math.round(ele.getPctHct4())));
        writer.writeEndElement();

        writer.writeStartElement("ADH008_TestDate");
        writer.writeCharacters(formatDefault(formatDate2(ele.getPctHct4Date())));
        writer.writeEndElement();

        writer.writeStartElement("ADH009_Value");
        writer.writeCharacters(String.valueOf(Math.round(ele.getWeight1())));
        writer.writeEndElement();

        writer.writeStartElement("ADH009_TestDate");
        writer.writeCharacters(formatDefault(formatDate2(ele.getWeight1Date())));
        writer.writeEndElement();

        writer.writeStartElement("ADH010_Value");
        writer.writeCharacters(String.valueOf(Math.round(ele.getWeight2())));
        writer.writeEndElement();

        writer.writeStartElement("ADH010_TestDate");
        writer.writeCharacters(formatDefault(formatDate2(ele.getWeight2Date())));
        writer.writeEndElement();

        writer.writeStartElement("ADH011_Value");
        writer.writeCharacters(String.valueOf(Math.round(ele.getWeight3())));
        writer.writeEndElement();

        writer.writeStartElement("ADH011_TestDate");
        writer.writeCharacters(formatDefault(formatDate2(ele.getWeight3Date())));
        writer.writeEndElement();

        writer.writeStartElement("ADH012_Value");
        writer.writeCharacters(String.valueOf(Math.round(ele.getWeight4())));
        writer.writeEndElement();

        writer.writeStartElement("ADH012_TestDate");
        writer.writeCharacters(formatDefault(formatDate2(ele.getWeight4Date())));
        writer.writeEndElement();

        writer.writeStartElement("ADH013_Value");
        writer.writeCharacters(String.valueOf(ele.getWho1()));
        writer.writeEndElement();

        writer.writeStartElement("ADH013_TestDate");
        writer.writeCharacters(formatDefault(formatDate2(ele.getWho1Date())));
        writer.writeEndElement();

        writer.writeStartElement("ADH014_Value");
        writer.writeCharacters(String.valueOf(ele.getWho2()));
        writer.writeEndElement();

        writer.writeStartElement("ADH014_TestDate");
        writer.writeCharacters(formatDefault(formatDate2(ele.getWho2Date())));
        writer.writeEndElement();

        writer.writeStartElement("ADH015_Value");
        writer.writeCharacters(String.valueOf(ele.getWho3()));
        writer.writeEndElement();

        writer.writeStartElement("ADH015_TestDate");
        writer.writeCharacters(formatDefault(formatDate2(ele.getWho3Date())));
        writer.writeEndElement();

        writer.writeStartElement("ADH016_Value");
        writer.writeCharacters(String.valueOf(ele.getWho4()));
        writer.writeEndElement();

        writer.writeStartElement("ADH016_TestDate");
        writer.writeCharacters(formatDefault(formatDate2(ele.getWho4Date())));
        writer.writeEndElement();

        writer.writeStartElement("ADH017_Value");
        writer.writeCharacters(String.valueOf(Math.round(ele.getCreatinine1())));
        writer.writeEndElement();

        writer.writeStartElement("ADH017_TestDate");
        writer.writeCharacters(formatDefault(formatDate2(ele.getCreatinine1Date())));
        writer.writeEndElement();

        writer.writeStartElement("ADH018_Value");
        writer.writeCharacters(String.valueOf(Math.round(ele.getCreatinine2())));
        writer.writeEndElement();

        writer.writeStartElement("ADH018_TestDate");
        writer.writeCharacters(formatDefault(formatDate2(ele.getCreatinine2Date())));
        writer.writeEndElement();

        writer.writeStartElement("ADH019_VALUE");
        writer.writeCharacters(String.valueOf(Math.round(ele.getCreatinine3())));
        writer.writeEndElement();

        writer.writeStartElement("ADH019_TestDate");
        writer.writeCharacters(formatDefault(formatDate2(ele.getCreatinine3Date())));
        writer.writeEndElement();

        writer.writeStartElement("ADH020_Value");
        writer.writeCharacters(String.valueOf(Math.round(ele.getCreatinine4())));
        writer.writeEndElement();

        writer.writeStartElement("ADH020_TestDate");
        writer.writeCharacters(formatDefault(formatDate2(ele.getCreatinine4Date())));
        writer.writeEndElement();

        writer.writeStartElement("ADH021_Value");
        writer.writeCharacters(String.valueOf(Math.round(ele.getAlt1())));
        writer.writeEndElement();

        writer.writeStartElement("ADH021_TestDate");
        writer.writeCharacters(formatDefault(formatDate2(ele.getAlt1Date())));
        writer.writeEndElement();

        writer.writeStartElement("ADH022_Value");
        writer.writeCharacters(String.valueOf(Math.round(ele.getAlt2())));
        writer.writeEndElement();

        writer.writeStartElement("ADH022_TestDate");
        writer.writeCharacters(formatDefault(formatDate2(ele.getAlt2Date())));
        writer.writeEndElement();

        writer.writeStartElement("ADH023_Value");
        writer.writeCharacters(String.valueOf(Math.round(ele.getAlt3())));
        writer.writeEndElement();

        writer.writeStartElement("ADH023_TestDate");
        writer.writeCharacters(formatDefault(formatDate2(ele.getAlt3Date())));
        writer.writeEndElement();

        writer.writeStartElement("ADH024_Value");
        writer.writeCharacters(String.valueOf(Math.round(ele.getAlt4())));
        writer.writeEndElement();

        writer.writeStartElement("ADH024_TestDate");
        writer.writeCharacters(formatDefault(formatDate2(ele.getAlt4Date())));
        writer.writeEndElement();

        writer.writeStartElement("FacilityID");
        writer.writeCharacters(String.valueOf(ele.getFacilityID()));
        writer.writeEndElement();

        writer.writeStartElement("UploaderId");
        writer.writeCharacters(ele.getUploaderId());
        writer.writeEndElement();

        writer.writeStartElement("UploadDt");
        writer.writeCharacters(formatDate2(ele.getUploadDt()));
        writer.writeEndElement();

        writer.writeStartElement("webUploadFlag");
        writer.writeCharacters(ele.getWebUploadFlag());
        writer.writeEndElement();

        writer.writeStartElement("ReviewPeriodID");
        writer.writeCharacters(String.valueOf(ele.getReviewPeriodID()));
        writer.writeEndElement();

        writer.writeEndElement();

    }
 public String formatDefault(String d){
        String date=d;
        if(d!=null && d.isEmpty()){
            date="01/01/1900";
        }
        return date;
  }
public void writeToXML(PedPatientDemographics demo) throws XMLStreamException {
        /*
            PatientID
    Lastname
    Firstname
    HospitalNo
    Gender
    DateOfBirth
    Age
    UnitOfAgeMeasure
    DateEnrolledInCare
    ClinicalVisit6MonthsPriorToReviewPeriod
    DeliveryLocation
    PrimaryCareGiver
    StateOfResidence
    LGAOfResidence
    StateOfOrigin
    Tribe
    DateOfLastVisit
    AdmissionDuringReviewPeriod
    RNL_SerialNo
    CareGiverOccupation
    FacilityID
    RecordCompletionPosition
    UploaderId
    UploadDt
    webUploadFlag
    ReviewPeriodID


         */

        writer.writeStartElement("Pediatric_PatientDemographics_Record");

        writer.writeStartElement("PatientID");
        writer.writeCharacters(StringUtils.trim(demo.getPatientID()));
        writer.writeEndElement();

        writer.writeStartElement("Lastname");
        writer.writeCharacters(demo.getLastName());
        writer.writeEndElement();

        writer.writeStartElement("Firstname");
        writer.writeCharacters(demo.getFirstName());
        writer.writeEndElement();

        writer.writeStartElement("HospitalNo");
        writer.writeCharacters(demo.getHospitalNo());
        writer.writeEndElement();

        writer.writeStartElement("Gender");
        writer.writeCharacters(demo.getGender());
        writer.writeEndElement();

        writer.writeStartElement("DateOfBirth");
        writer.writeCharacters(formatDate2(demo.getDateOfBirth()));
        writer.writeEndElement();

        writer.writeStartElement("Age");
        writer.writeCharacters(String.valueOf(demo.getAge()));
        writer.writeEndElement();

        writer.writeStartElement("UnitOfAgeMeasure");
        writer.writeCharacters(demo.getUnitOfAgeMeasure());
        writer.writeEndElement();

        writer.writeStartElement("DateEnrolledInCare");
        writer.writeCharacters(formatDate2(demo.getDateEnrolledInCare()));
        writer.writeEndElement();

        writer.writeStartElement("ClinicalVisit6MonthsPriorToReviewPeriod");
        writer.writeCharacters(demo.getClinicVisit6MonthsPriorToReviewPeriod());
        writer.writeEndElement();

        writer.writeStartElement("DeliveryLocation");
        writer.writeCharacters(demo.getDeliveryLocation());
        writer.writeEndElement();

        writer.writeStartElement("PrimaryCareGiver");
        writer.writeCharacters(demo.getPrimaryCareGiver());
        writer.writeEndElement();

        writer.writeStartElement("StateOfResidence");
        writer.writeCharacters(StringEscapeUtils.escapeXml(StringUtils.abbreviate(StringUtils.upperCase(demo.getStateOfResidence()),20)));
        writer.writeEndElement();

        writer.writeStartElement("LGAOfResidence");
        writer.writeCharacters(StringEscapeUtils.escapeXml(StringUtils.abbreviate(StringUtils.upperCase(demo.getLgaOfResidence()),20)));
        writer.writeEndElement();

        writer.writeStartElement("StateOfOrigin");
        writer.writeCharacters(StringEscapeUtils.escapeXml(StringUtils.abbreviate(StringUtils.upperCase(demo.getStateOfOrigin()),20)));
        writer.writeEndElement();

        writer.writeStartElement("Tribe");
        writer.writeCharacters(StringEscapeUtils.escapeXml(StringUtils.upperCase(demo.getTribe())));
        writer.writeEndElement();

        writer.writeStartElement("DateOfLastVisit");
        writer.writeCharacters(formatDate2(demo.getDateOfLastVisit()));
        writer.writeEndElement();

        writer.writeStartElement("AdmissionDuringReviewPeriod");
        writer.writeCharacters(demo.getAdmissionDuringReviewPeriod());
        writer.writeEndElement();

        writer.writeStartElement("RNL_SerialNo");
        writer.writeCharacters(String.valueOf(demo.getRnlSerialNo()));
        writer.writeEndElement();

        writer.writeStartElement("CareGiverOccupation");
        writer.writeCharacters(demo.getCareGiverOccupation());
        writer.writeEndElement();

        writer.writeStartElement("FacilityID");
        writer.writeCharacters(String.valueOf(demo.getFacilityID()));
        writer.writeEndElement();

        writer.writeStartElement("RecordCompletionPosition");
        writer.writeCharacters(String.valueOf(demo.getRecordCompletionPosition()));
        writer.writeEndElement();

        writer.writeStartElement("UploaderId");
        writer.writeCharacters(String.valueOf(demo.getUploaderID()));
        writer.writeEndElement();

        writer.writeStartElement("UploadDt");
        writer.writeCharacters(formatDate2(demo.getUploadDate()));
        writer.writeEndElement();

        writer.writeStartElement("webUploadFlag");
        writer.writeCharacters(demo.getWebUploadFlag());
        writer.writeEndElement();

        writer.writeStartElement("ReviewPeriod");
        writer.writeCharacters(String.valueOf(demo.getReviewPeriodID()));
        writer.writeEndElement();

        writer.writeEndElement();

    }
public void writeToXML(PediatricPatientStatus pstatus) throws XMLStreamException {
        writer.writeStartElement("Pediatric_PatientStatus_Record");

        writer.writeStartElement("PatientID");
        writer.writeCharacters(StringUtils.trim(pstatus.getPatientID()));
        writer.writeEndElement();

        writer.writeStartElement("Status");
        writer.writeCharacters(pstatus.getStatus());
        writer.writeEndElement();

        writer.writeStartElement("DateOfStatusChange");
        writer.writeCharacters(formatDate2(pstatus.getDateOfStatusChange()));
        writer.writeEndElement();

        writer.writeStartElement("ReasonForStatusChange");
        writer.writeCharacters(pstatus.getReason());
        writer.writeEndElement();

        writer.writeStartElement("ID");
        writer.writeCharacters(pstatus.getID());
        writer.writeEndElement();

        writer.writeStartElement("FacilityID");
        writer.writeCharacters(String.valueOf(pstatus.getFacilityID()));
        writer.writeEndElement();

        writer.writeStartElement("UploaderId");
        writer.writeCharacters(pstatus.getUploaderId());
        writer.writeEndElement();

        writer.writeStartElement("UploadDt");
        writer.writeCharacters(formatDate2(pstatus.getUploadDt()));
        writer.writeEndElement();

        writer.writeStartElement("webUploadFlag");
        writer.writeCharacters(pstatus.getWebUploadFlag());
        writer.writeEndElement();

        writer.writeStartElement("Transferred_Out");
        writer.writeCharacters(pstatus.getTransferredOut());
        writer.writeEndElement();

        writer.writeStartElement("Death");
        writer.writeCharacters(pstatus.getDeath());
        writer.writeEndElement();

        writer.writeStartElement("Discontinued_Care");
        writer.writeCharacters(pstatus.getDiscontinued());
        writer.writeEndElement();

        writer.writeStartElement("Transferred_Out_Date");
        writer.writeCharacters(formatDate2(pstatus.getTransferredOutDate()));
        writer.writeEndElement();

        writer.writeStartElement("Death_Date");
        writer.writeCharacters(formatDate2(pstatus.getDeathDate()));
        writer.writeEndElement();

        writer.writeStartElement("Discontinued_Care_Date");
        writer.writeCharacters(formatDate2(pstatus.getDiscontinuedCareDate()));
        writer.writeEndElement();

        writer.writeStartElement("Discontinued_Care_Reason");
        writer.writeCharacters(pstatus.getDiscontinuedCareReason());
        writer.writeEndElement();

        writer.writeStartElement("Discontinued_Care_Reason_Other");
        writer.writeCharacters(pstatus.getDiscontinuedCareReasonOther());
        writer.writeEndElement();

        writer.writeStartElement("ReviewPeriodID");
        writer.writeCharacters(String.valueOf(pstatus.getReviewPeriodID()));
        writer.writeEndElement();

        writer.writeEndElement();

        /*
    PatientID
Status
DateOfStatusChange
ReasonForStatusChange
ID
FacilityID
UploaderId
UploadDt
webUploadFlag
Transferred_Out
Death
Discontinued_Care
Transferred_Out_Date
Death_Date
Discontinued_Care_Date
Discontinued_Care_Reason
Discontinued_Care_Reason_Other
ReviewPeriodID
         */
    }

public void writeToXML(PediatricTuberculosis dtr) throws XMLStreamException {

        /*
             PatientID
PatientOnTBTreatmentDuringReviewPeriod
PatientClinicallyScreenForTBDuringReviewPeriod
TBClinicalScreeningCriteria
BasedOnScreeningWasPatientedSuspectedToHaveTB
PatientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture
PatientHdChestXRay
PatientDiagnosedOfTBInReviewPeriod
PatientStartTBTreatment
TB_TreatmentStartDate
TBDiagnosis_Date
FacilityID
UploaderId
UploadDt
webUploadFlag
TBScreeningCriteria_CurrentCough
TBScreeningCriteria_ContactWithTBCase
TBScreeningCriteria_PoorWeighGain
ReviewPeriodID

         */
        writer.writeStartElement("Pediatric_Tuberculosis_Record");

        writer.writeStartElement("PatientID");
        writer.writeCharacters(StringUtils.trim(dtr.getPatientID()));
        writer.writeEndElement();

        writer.writeStartElement("PatientOnTBTreatmentDuringReviewPeriod");
        writer.writeCharacters(dtr.getPatientOnTBTreatmentDuringReviewPeriod());
        writer.writeEndElement();

        writer.writeStartElement("PatientClinicallyScreenForTBDuringReviewPeriod");
        writer.writeCharacters(dtr.getPatientClinicallyScreenForTBDuringReviewPeriod());
        writer.writeEndElement();

        writer.writeStartElement("TBClinicalScreeningCriteria");
        writer.writeCharacters(dtr.getTbClinicalScreeningCriteria());
        writer.writeEndElement();

        writer.writeStartElement("BasedOnScreeningWasPatientedSuspectedToHaveTB");
        writer.writeCharacters(dtr.getBasedOnScreeningWasPatientedSuspectedToHaveTB());
        writer.writeEndElement();

        writer.writeStartElement("PatientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture");
        writer.writeCharacters(dtr.getPatientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture());
        writer.writeEndElement();

        writer.writeStartElement("PatientHdChestXRay");
        writer.writeCharacters(dtr.getPatientHdChestXRay());
        writer.writeEndElement();

        writer.writeStartElement("PatientDiagnosedOfTBInReviewPeriod");
        writer.writeCharacters(dtr.getPatientDiagnosedOfTBInReviewPeriod());
        writer.writeEndElement();

        writer.writeStartElement("PatientStartTBTreatment");
        writer.writeCharacters(dtr.getPatientStartTBTreatment());
        writer.writeEndElement();

        writer.writeStartElement("TB_TreatmentStartDate");
        writer.writeCharacters(formatDate2(dtr.getTbTreatmentStartDate()));
        writer.writeEndElement();

        writer.writeStartElement("TBDiagnosis_Date");
        writer.writeCharacters(formatDate2(dtr.getTbDiagnosisDate()));
        writer.writeEndElement();

        writer.writeStartElement("FacilityID");
        writer.writeCharacters(String.valueOf(dtr.getFacilityID()));
        writer.writeEndElement();

        writer.writeStartElement("UploaderId");
        writer.writeCharacters(dtr.getUploaderID());
        writer.writeEndElement();

        writer.writeStartElement("UploadDt");
        writer.writeCharacters(formatDate2(dtr.getUploadDate()));
        writer.writeEndElement();

        writer.writeStartElement("webUploadFlag");
        writer.writeCharacters(dtr.getWebUploadFlag());
        writer.writeEndElement();

        writer.writeStartElement("TBScreeningCriteria_CurrentCough");
        writer.writeCharacters(dtr.getTBScreeningCriteria_CurrentCough());
        writer.writeEndElement();

        writer.writeStartElement("TBScreeningCriteria_ContactWithTBCase");
        writer.writeCharacters(dtr.getTBScreeningCriteria_ContactWithTBCase());
        writer.writeEndElement();

        writer.writeStartElement("TBScreeningCriteria_PoorWeighGain");
        writer.writeCharacters(dtr.getTBScreeningCriteria_PoorWeightGain());
        writer.writeEndElement();

        writer.writeStartElement("ReviewPeriodID");
        writer.writeCharacters(String.valueOf(dtr.getReviewPeriodID()));
        writer.writeEndElement();

        writer.writeEndElement();

    }
public void writeToXML(PediatricEducation pedu) throws XMLStreamException {
        writer.writeStartElement("Pediatric_Education_Record");

        writer.writeStartElement("PatientID");
        writer.writeCharacters(StringUtils.trim(pedu.getPatientID()));
        writer.writeEndElement();

        writer.writeStartElement("MotherReceivedInfantFeedingEducation");
        writer.writeCharacters(pedu.getMotherReceivedInfantFeedingEducation());
        writer.writeEndElement();

        writer.writeStartElement("FacilityID");
        writer.writeCharacters(String.valueOf(pedu.getFacilityID()));
        writer.writeEndElement();

        writer.writeStartElement("UploaderId");
        writer.writeCharacters(String.valueOf(pedu.getUploaderID()));
        writer.writeEndElement();

        writer.writeStartElement("UploadDt");
        writer.writeCharacters(formatDate2(pedu.getUploadDt()));
        writer.writeEndElement();

        writer.writeStartElement("webUploadFlag");
        writer.writeCharacters(pedu.getWebUploadFlag());
        writer.writeEndElement();

        writer.writeStartElement("ReviewPeriodID");
        writer.writeCharacters(String.valueOf(pedu.getReviewPeriodID()));
        writer.writeEndElement();

        writer.writeEndElement();
    }
public void writeToXML(PediatricLinkage pl) throws XMLStreamException {
        /*
           
         */
        writer.writeStartElement("Pediatric_Linkage_Record");

        writer.writeStartElement("PatientID");
        writer.writeCharacters(StringUtils.trim(pl.getPatientID()));
        writer.writeEndElement();

        writer.writeStartElement("PatientReceivedNutritionalAssessmentInReviewPeriod");
        writer.writeCharacters(pl.getPatientReceivedNutritionalAssessmentInReviewPeriod());
        writer.writeEndElement();

        writer.writeStartElement("PatientQualifyForNutritionSupporot");
        writer.writeCharacters(pl.getPatientQualifyForNutritionSupporot());
        writer.writeEndElement();

        writer.writeStartElement("DidPatientReceivedNutritionSupport");
        writer.writeCharacters(pl.getDidPatientReceivedNutritionSupport());
        writer.writeEndElement();

        writer.writeStartElement("ChildImmunizationStatus");
        writer.writeCharacters(pl.getChildImmunizationStatus());
        writer.writeEndElement();

        writer.writeStartElement("FacilityID");
        writer.writeCharacters(String.valueOf(pl.getFacilityID()));
        writer.writeEndElement();

        writer.writeStartElement("UploaderId");
        writer.writeCharacters(String.valueOf(pl.getUploaderId()));
        writer.writeEndElement();

        writer.writeStartElement("UploadDt");
        writer.writeCharacters(formatDate2(pl.getUploadDt()));
        writer.writeEndElement();

        writer.writeStartElement("ServicesReceivedByPatient");
        writer.writeCharacters(pl.getServiceReceivedByPatient());
        writer.writeEndElement();

        writer.writeStartElement("webUploadFlag");
        writer.writeCharacters(pl.getWebUploadFlag());
        writer.writeEndElement();

        writer.writeStartElement("WaterguardReceived");
        writer.writeCharacters(pl.getWaterGuardReceived());
        writer.writeEndElement();

        writer.writeStartElement("InsecticideTreatedNetsReceived");
        writer.writeCharacters(pl.getInsecticideTreatedNetsReceived());
        writer.writeEndElement();

        writer.writeStartElement("NotIndicated");
        writer.writeCharacters(pl.getNotIndicated());
        writer.writeEndElement();

        writer.writeStartElement("NoneReceived");
        writer.writeCharacters(pl.getNoneReceived());
        writer.writeEndElement();

        writer.writeStartElement("ReviewPeriodID");
        writer.writeCharacters(String.valueOf(pl.getReviewPeriodID()));
        writer.writeEndElement();

        writer.writeEndElement();

    }
public void writeToXML(DataViralLoadTestingReviewPeriod vl) throws XMLStreamException {

        writer.writeStartElement("ViralLoadTesting_Record");

        writer.writeStartElement("PatientID");
        writer.writeCharacters(StringUtils.trim(vl.getPatientID()));
        writer.writeEndElement();

        writer.writeStartElement("HasPatientReceivedVLTesting");
        writer.writeCharacters(vl.getHasPatientReceivedVLTesting());
        writer.writeEndElement();

        writer.writeStartElement("VLTestDate");
        writer.writeCharacters(formatDate2(vl.getVlTestingDate()));
        writer.writeEndElement();

        writer.writeStartElement("Result_Copies_Per_ml");
        writer.writeCharacters(String.valueOf(vl.getVlResult()));
        writer.writeEndElement();

        writer.writeStartElement("FacilityID");
        writer.writeCharacters(vl.getFacilityID());
        writer.writeEndElement();

        writer.writeStartElement("UploaderId");
        writer.writeCharacters(String.valueOf(vl.getUploadID()));
        writer.writeEndElement();

        writer.writeStartElement("UploadDt");
        writer.writeCharacters(formatDate2(vl.getUploadDate()));
        writer.writeEndElement();

        writer.writeStartElement("webUploadFlag");
        writer.writeCharacters(vl.getWebUploadFlag());
        writer.writeEndElement();

        writer.writeStartElement("ReviewPeriodID");
        writer.writeCharacters(String.valueOf(vl.getReviewPeriodID()));
        writer.writeEndElement();

        writer.writeEndElement();

    }

public void writeToXML(PediatricCotrimoxazole dc) throws XMLStreamException {

        writer.writeStartElement("Pediatric_Cotrimoxazole_Record");

        writer.writeStartElement("PatientID");
        writer.writeCharacters(StringUtils.trim(dc.getPatientID()));
        writer.writeEndElement();

        writer.writeStartElement("PatientCurrentlyOnCotrimoxazoleProphylaxis");
        writer.writeCharacters(dc.getPatientCurrentlyOnCotrimoxazoleProphylaxis());
        writer.writeEndElement();

        writer.writeStartElement("DateOfFirstPrescription");
        writer.writeCharacters(formatDate2(dc.getDateOfFirstPrescription()));
        writer.writeEndElement();

        writer.writeStartElement("AgeOfFirstPrescription");
        writer.writeCharacters(String.valueOf(dc.getAgeOfFirstPrescription()));
        writer.writeEndElement();

        writer.writeStartElement("UnitOfAgeMeasure");
        writer.writeCharacters(dc.getUnitOfAgeMeasure());
        writer.writeEndElement();

        writer.writeStartElement("FacilityID");
        writer.writeCharacters(String.valueOf(dc.getFacilityID()));
        writer.writeEndElement();

        writer.writeStartElement("UploaderId");
        writer.writeCharacters(dc.getUploaderId());
        writer.writeEndElement();

        writer.writeStartElement("UploadDt");
        writer.writeCharacters(formatDate2(dc.getUploadDt()));
        writer.writeEndElement();

        writer.writeStartElement("webUploadFlag");
        writer.writeCharacters(dc.getWebUploadFlag());
        writer.writeEndElement();

        writer.writeStartElement("ReviewPeriodID");
        writer.writeCharacters(String.valueOf(dc.getReviewPeriodID()));
        writer.writeEndElement();

        writer.writeEndElement();

    }
public void writeToXML(PediatricBaselineParameters baselineParameter) throws XMLStreamException {
        if (baselineParameter != null && baselineParameter.getPatientID()!= null && !baselineParameter.getPatientID().isEmpty()) {
        writer.writeStartElement("Pediatric_BaselineParameters_Record");

        writer.writeStartElement("PatientID");
        writer.writeCharacters(StringUtils.trim(baselineParameter.getPatientID()));
        writer.writeEndElement();

        writer.writeStartElement("CD4_Count");
        writer.writeCharacters(String.valueOf(Math.round(baselineParameter.getCd4Count())));
        writer.writeEndElement();

        writer.writeStartElement("CD4_Count_Date");
        writer.writeCharacters(formatDate2(baselineParameter.getCd4CountDate()));
        writer.writeEndElement();

        writer.writeStartElement("Weight");
        writer.writeCharacters(String.valueOf(Math.round(baselineParameter.getWeight())));
        writer.writeEndElement();

        writer.writeStartElement("Weight_Date");
        writer.writeCharacters(formatDate2(baselineParameter.getWeightDate()));
        writer.writeEndElement();

        writer.writeStartElement("WHO_Clinical_Stage");
        writer.writeCharacters(String.valueOf(baselineParameter.getWhoClinicalStage()));
        writer.writeEndElement();

        writer.writeStartElement("WHO_Clinical_State_Date");
        writer.writeCharacters(formatDate2(baselineParameter.getWhoClinicalStageDate()));
        writer.writeEndElement();

        writer.writeStartElement("CD4_Not_Recorded");
        writer.writeCharacters(baselineParameter.getCd4NotRecorded());
        writer.writeEndElement();

        writer.writeStartElement("Weight_Not_Recorded");
        writer.writeCharacters(baselineParameter.getWeightNotRecorded());
        writer.writeEndElement();

        writer.writeStartElement("WHO_Clinical_Stage_Not_Recorded");
        writer.writeCharacters(baselineParameter.getWhoClinicalStageNotRecorded());
        writer.writeEndElement();

        /*writer.writeStartElement("FacilityID");
        writer.writeCharacters(String.valueOf(baselineParameter.getFacilityID()));
        writer.writeEndElement();*/

        writer.writeStartElement("PatientEverStartedOnART");
        writer.writeCharacters(baselineParameter.getPatientEverStartedOnART());
        writer.writeEndElement();

        writer.writeStartElement("ART_Start_Date");
        writer.writeCharacters(formatDate2(baselineParameter.getArtStartDate()));
        writer.writeEndElement();

        writer.writeStartElement("UploaderId");
        writer.writeCharacters(baselineParameter.getUploaderId());
        writer.writeEndElement();

        writer.writeStartElement("UploadDt");
        writer.writeCharacters(formatDate2(baselineParameter.getUploadDt()));
        writer.writeEndElement();

        writer.writeStartElement("webUploadFlag");
        writer.writeCharacters(baselineParameter.getWebUploadFlag());
        writer.writeEndElement();

        writer.writeStartElement("ReviewPeriodID");
        writer.writeCharacters(String.valueOf(baselineParameter.getReviewPeriodID()));
        writer.writeEndElement();

        writer.writeEndElement();
         }

    }

     public void writeToXML(DataTuberculosisRecord dtr) throws XMLStreamException {

        writer.writeStartElement("Tuberculosis_Record");

        writer.writeStartElement("PatientID");
        writer.writeCharacters(StringUtils.trim(dtr.getPatientID()));
        writer.writeEndElement();

        writer.writeStartElement("PatientOnTBTreatmentAtStartOfReviewPeriod");
        writer.writeCharacters(dtr.getPatientOnTBTreatmentAtStartOfReviewPeriod());
        writer.writeEndElement();

        writer.writeStartElement("PatientClinicallyScreenForTBDuringReviewPeriod");
        writer.writeCharacters(dtr.getPatientClinicallyScreenForTBDuringReviewPeriod());
        writer.writeEndElement();

        writer.writeStartElement("TBClinicalScreeningCriteria");
        writer.writeCharacters(dtr.getTBScreeningCriteria_ContactHistoryWithTBCase());
        writer.writeEndElement();

        writer.writeStartElement("BasedOnScreeningWasPatientedSuspectedToHaveTB");
        writer.writeCharacters(dtr.getBasedOnScreeningWasPatientedSuspectedToHaveTB());
        writer.writeEndElement();

        writer.writeStartElement("PatientHaveCRXPerformedDuringReviewPeriod");
        writer.writeCharacters(dtr.getPatientHaveCRXPerformedDuringReviewPeriod());
        writer.writeEndElement();

        writer.writeStartElement("PatientReferredToDOTsClinic");
        writer.writeCharacters(dtr.getPatientReferredToDOTsClinic());
        writer.writeEndElement();

        writer.writeStartElement("PatientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture");
        writer.writeCharacters(dtr.getPatientBeenEvaluatedInReviewPeriodForTBUsingSputumSmearOrCulture());
        writer.writeEndElement();

        writer.writeStartElement("PatientDiagnosedOfTBInReviewPeriod");
        writer.writeCharacters(dtr.getPatientDiagnosedOfTBInReviewPeriod());
        writer.writeEndElement();

        writer.writeStartElement("TBDiagnosis_Date");
        writer.writeCharacters(formatDate2(dtr.getTBDiagnosis_Date()));
        writer.writeEndElement();

        writer.writeStartElement("PatientStartTBTreatment");
        writer.writeCharacters(dtr.getPatientStartTBTreatment());
        writer.writeEndElement();

        writer.writeStartElement("TB_TreatmentStartDate");
        writer.writeCharacters(formatDate2(dtr.getTB_TreatmentStartDate()));
        writer.writeEndElement();

        writer.writeStartElement("FacilityID");
        writer.writeCharacters(String.valueOf(dtr.getFacilityID()));
        writer.writeEndElement();

        writer.writeStartElement("ReviewPeriodID");
        writer.writeCharacters(String.valueOf(dtr.getReviewPeriodID()));
        writer.writeEndElement();

        writer.writeStartElement("TBScreeningCriteria_CurrentCough");
        writer.writeCharacters(dtr.getTBScreeningCriteria_CurrentCough());
        writer.writeEndElement();

        writer.writeStartElement("TBScreeningCriteria_ContactHistoryWithTBCase");
        writer.writeCharacters(dtr.getTBScreeningCriteria_ContactHistoryWithTBCase());
        writer.writeEndElement();

        writer.writeStartElement("TBScreeningCriteria_PoorWeightGain");
        writer.writeCharacters(dtr.getTBScreeningCriteria_PoorWeightGain());
        writer.writeEndElement();

        writer.writeStartElement("UploaderId");
        writer.writeCharacters(dtr.getUploaderID());
        writer.writeEndElement();

        writer.writeStartElement("UploadDt");
        writer.writeCharacters(formatDate2(dtr.getUploadDate()));
        writer.writeEndElement();

        writer.writeStartElement("webUploadFlag");
        writer.writeCharacters(dtr.getWebUploadFlag());
        writer.writeEndElement();

        writer.writeEndElement();

    }
     public void writeToXML(PediatricPatientMonitoringDuringReviewPeriod ele) throws XMLStreamException {
        writer.writeStartElement("Pediatric_PatientMonitoringDuringReviewPeriod_Record");

        writer.writeStartElement("PatientID");
        writer.writeCharacters(StringUtils.trim(ele.getPatientID()));
        writer.writeEndElement();

        writer.writeStartElement("PDG001_VALUE");
        writer.writeCharacters(String.valueOf(Math.round(ele.getCd4Value1())));
        writer.writeEndElement();

        writer.writeStartElement("PDG001_DATE");
        writer.writeCharacters(formatDefault(formatDate2(ele.getCd4Value1Date())));
        writer.writeEndElement();

        writer.writeStartElement("PDG002_VALUE");
        writer.writeCharacters(String.valueOf(Math.round(ele.getCd4Value2())));
        writer.writeEndElement();

        writer.writeStartElement("PDG002_DATE");
        writer.writeCharacters(formatDefault(formatDate2(ele.getCd4Value2Date())));
        writer.writeEndElement();

        writer.writeStartElement("PDG003_VALUE");
        writer.writeCharacters(String.valueOf(Math.round(ele.getCd4Value3())));
        writer.writeEndElement();

        writer.writeStartElement("PDG003_DATE");
        writer.writeCharacters(formatDefault(formatDate2(ele.getCd4Value3Date())));
        writer.writeEndElement();

        writer.writeStartElement("PDG004_VALUE");
        writer.writeCharacters(String.valueOf(Math.round(ele.getCd4Value4())));
        writer.writeEndElement();

        writer.writeStartElement("PDG004_DATE");
        writer.writeCharacters(formatDefault(formatDate2(ele.getCd4Value4Date())));
        writer.writeEndElement();

        writer.writeStartElement("PDG005_VALUE");
        writer.writeCharacters(String.valueOf(Math.round(ele.getCd4Prc1())));
        writer.writeEndElement();

        writer.writeStartElement("PDG005_DATE");
        writer.writeCharacters(formatDefault(formatDate2(ele.getCd4Prc1Date())));
        writer.writeEndElement();

        writer.writeStartElement("PDG006_VALUE");
        writer.writeCharacters(String.valueOf(Math.round(ele.getCd4Prc2())));
        writer.writeEndElement();

        writer.writeStartElement("PDG006_DATE");
        writer.writeCharacters(formatDefault(formatDate2(ele.getCd4Prc2Date())));
        writer.writeEndElement();

        writer.writeStartElement("PDG007_VALUE");
        writer.writeCharacters(String.valueOf(Math.round(ele.getCd4Prc3())));
        writer.writeEndElement();

        writer.writeStartElement("PDG007_DATE");
        writer.writeCharacters(formatDefault(formatDate2(ele.getCd4Prc3Date())));
        writer.writeEndElement();

        writer.writeStartElement("PDG008_VALUE");
        writer.writeCharacters(String.valueOf(Math.round(ele.getCd4Prc4())));
        writer.writeEndElement();

        writer.writeStartElement("PDG008_DATE");
        writer.writeCharacters(formatDefault(formatDate2(ele.getCd4Prc4Date())));
        writer.writeEndElement();

        writer.writeStartElement("PDG009_VALUE");
        writer.writeCharacters(String.valueOf(Math.round(ele.getWeight1())));
        writer.writeEndElement();

        writer.writeStartElement("PDG009_DATE");
        writer.writeCharacters(formatDefault(formatDate2(ele.getWeight1Date())));
        writer.writeEndElement();

        writer.writeStartElement("PDG0010_VALUE");
        writer.writeCharacters(String.valueOf(Math.round(ele.getWeight2())));
        writer.writeEndElement();

        writer.writeStartElement("PDG0010_DATE");
        writer.writeCharacters(formatDefault(formatDate2(ele.getWeight2Date())));
        writer.writeEndElement();

        writer.writeStartElement("PDG0011_VALUE");
        writer.writeCharacters(String.valueOf(Math.round(ele.getWeight3())));
        writer.writeEndElement();

        writer.writeStartElement("PDG0011_DATE");
        writer.writeCharacters(formatDefault(formatDate2(ele.getWeight3Date())));
        writer.writeEndElement();

        writer.writeStartElement("PDG0012_VALUE");
        writer.writeCharacters(String.valueOf(Math.round(ele.getWeight4())));
        writer.writeEndElement();

        writer.writeStartElement("PDG0012_DATE");
        writer.writeCharacters(formatDefault(formatDate2(ele.getWeight4Date())));
        writer.writeEndElement();

        writer.writeStartElement("PDG0013_VALUE");
        writer.writeCharacters(String.valueOf(ele.getWho1()));
        writer.writeEndElement();

        writer.writeStartElement("PDG0013_DATE");
        writer.writeCharacters(formatDefault(formatDate2(ele.getWho1Date())));
        writer.writeEndElement();

        writer.writeStartElement("PDG0014_VALUE");
        writer.writeCharacters(String.valueOf(ele.getWho2()));
        writer.writeEndElement();

        writer.writeStartElement("PDG0014_DATE");
        writer.writeCharacters(formatDefault(formatDate2(ele.getWho2Date())));
        writer.writeEndElement();

        writer.writeStartElement("PDG0015_VALUE");
        writer.writeCharacters(String.valueOf(ele.getWho3()));
        writer.writeEndElement();

        writer.writeStartElement("PDG0015_DATE");
        writer.writeCharacters(formatDefault(formatDate2(ele.getWho3Date())));
        writer.writeEndElement();

        writer.writeStartElement("PDG0016_VALUE");
        writer.writeCharacters(String.valueOf(ele.getWho4()));
        writer.writeEndElement();

        writer.writeStartElement("PDG0016_DATE");
        writer.writeCharacters(formatDefault(formatDate2(ele.getWho4Date())));
        writer.writeEndElement();

        writer.writeStartElement("PDG0017_VALUE");
        writer.writeCharacters(String.valueOf(Math.round(ele.getPctHct1())));
        writer.writeEndElement();

        writer.writeStartElement("PDG0017_DATE");
        writer.writeCharacters(formatDefault(formatDate2(ele.getPctHct1Date())));
        writer.writeEndElement();

        writer.writeStartElement("PDG0018_VALUE");
        writer.writeCharacters(String.valueOf(Math.round(ele.getPctHct2())));
        writer.writeEndElement();

        writer.writeStartElement("PDG0018_DATE");
        writer.writeCharacters(formatDefault(formatDate2(ele.getPctHct2Date())));
        writer.writeEndElement();

        writer.writeStartElement("PDG0019_VALUE");
        writer.writeCharacters(String.valueOf(Math.round(ele.getPctHct3())));
        writer.writeEndElement();

        writer.writeStartElement("PDG0019_DATE");
        writer.writeCharacters(formatDefault(formatDate2(ele.getPctHct3Date())));
        writer.writeEndElement();

        writer.writeStartElement("PDG0020_VALUE");
        writer.writeCharacters(String.valueOf(Math.round(ele.getPctHct4())));
        writer.writeEndElement();

        writer.writeStartElement("PDG0020_DATE");
        writer.writeCharacters(formatDefault(formatDate2(ele.getPctHct4Date())));
        writer.writeEndElement();

        writer.writeStartElement("PDG0021_VALUE");
        writer.writeCharacters(String.valueOf(Math.round(ele.getAlt1())));
        writer.writeEndElement();

        writer.writeStartElement("PDG0021_DATE");
        writer.writeCharacters(formatDefault(formatDate2(ele.getAlt1Date())));
        writer.writeEndElement();

        writer.writeStartElement("PDG0022_VALUE");
        writer.writeCharacters(String.valueOf(Math.round(ele.getAlt2())));
        writer.writeEndElement();

        writer.writeStartElement("PDG0022_DATE");
        writer.writeCharacters(formatDefault(formatDate2(ele.getAlt2Date())));
        writer.writeEndElement();

        writer.writeStartElement("PDG0023_VALUE");
        writer.writeCharacters(String.valueOf(Math.round(ele.getAlt3())));
        writer.writeEndElement();

        writer.writeStartElement("PDG0023_DATE");
        writer.writeCharacters(formatDefault(formatDate2(ele.getAlt3Date())));
        writer.writeEndElement();

        writer.writeStartElement("PDG0024_VALUE");
        writer.writeCharacters(String.valueOf(Math.round(ele.getAlt4())));
        writer.writeEndElement();

        writer.writeStartElement("PDG0024_DATE");
        writer.writeCharacters(formatDefault(formatDate2(ele.getAlt4Date())));
        writer.writeEndElement();
        
        writer.writeStartElement("HasThePatientReceivedViralLoadTesting");
        writer.writeCharacters(ele.getHasThePatientReceivedViralLoadTesting());
        writer.writeEndElement();
        
        writer.writeStartElement("ViralLoadTestResult");
        writer.writeCharacters(String.valueOf(ele.getViralLoadTestResult()));
        writer.writeEndElement();
        
        writer.writeStartElement("ViralLoadTestDate");
        writer.writeCharacters(formatDefault(formatDate2(ele.getViralLoadTestDate())));
        writer.writeEndElement();
        
        writer.writeStartElement("FacilityID");
        writer.writeCharacters(String.valueOf(ele.getFacilityID()));
        writer.writeEndElement();

        writer.writeStartElement("UploaderId");
        writer.writeCharacters(String.valueOf(ele.getUploaderId()));
        writer.writeEndElement();

        writer.writeStartElement("UploadDt");
        writer.writeCharacters(formatDate2(ele.getUploadDt()));
        writer.writeEndElement();

        writer.writeStartElement("webUploadFlag");
        writer.writeCharacters(ele.getWebUploadFlag());
        writer.writeEndElement();

        writer.writeStartElement("ReviewPeriodID");
        writer.writeCharacters(String.valueOf(ele.getReviewPeriodID()));
        writer.writeEndElement();

        writer.writeEndElement();

    }

     public void writeToXML(PediatricClinicalEvaluationInReviewPeriod dceval) throws XMLStreamException {

        writer.writeStartElement("Pediatric_ClinicalEvaluationInReviewPeriod_Record");

        writer.writeStartElement("PatientID");
        writer.writeCharacters(StringUtils.trim(dceval.getPatientID()));
        writer.writeEndElement();

        writer.writeStartElement("Visit1");
        writer.writeCharacters(formatDate2(dceval.getVisit1()));
        writer.writeEndElement();

        writer.writeStartElement("Visit2");
        writer.writeCharacters(formatDate2(dceval.getVisit2()));
        writer.writeEndElement();

        writer.writeStartElement("Visit3");
        writer.writeCharacters(formatDate2(dceval.getVisit3()));
        writer.writeEndElement();

        writer.writeStartElement("Visit4");
        writer.writeCharacters(formatDate2(dceval.getVisit4()));
        writer.writeEndElement();

        writer.writeStartElement("FacilityID");
        writer.writeCharacters(String.valueOf(dceval.getFacilityID()));
        writer.writeEndElement();

        writer.writeStartElement("UploaderId");
        writer.writeCharacters(dceval.getUploaderId());
        writer.writeEndElement();

        writer.writeStartElement("UploadDt");
        writer.writeCharacters(formatDate2(dceval.getUploadDt()));
        writer.writeEndElement();

        writer.writeStartElement("webUploadFlag");
        writer.writeCharacters(dceval.getWebUploadFlag());
        writer.writeEndElement();

        writer.writeStartElement("ReviewPeriodID");
        writer.writeCharacters(String.valueOf(dceval.getReviewPeriodID()));
        writer.writeEndElement();

        writer.writeEndElement();

    }
     public void writeToXML(CareAndSupportAssessmentRecord csa) throws XMLStreamException {
        // writer.writeStartElement("Data-set");
        writer.writeStartElement("CareAndSupportAssessment_Record");

        writer.writeStartElement("PatientID");
        writer.writeCharacters(StringUtils.trim(csa.getPatientID()));
        writer.writeEndElement();

        writer.writeStartElement("CareAndSupportAssementFormInPatientFolder");
        writer.writeCharacters(csa.getCareAndSupportAssementFormInPatientFolder());
        writer.writeEndElement();

        writer.writeStartElement("PatientReceiveCareAndSupportAssessmentInReviewPeriod");
        writer.writeCharacters(csa.getPatientReceiveBasicCarePackageInReviewPeriod());
        writer.writeEndElement();

        writer.writeStartElement("NutritionalAssessmentEverDoneForPatientSinceEnrolment");
        writer.writeCharacters(csa.getNutritionalAssessmentEverDoneForPatientSinceEnrolment());
        writer.writeEndElement();

        writer.writeStartElement("PatientReceiveNutritionalAssessementInReviewPeriod");
        writer.writeCharacters(csa.getPatientReceiveNutritionalAssessementInReviewPeriod());
        writer.writeEndElement();

        writer.writeStartElement("PreventionGoaDocumentedInCareAndSupportForm");
        writer.writeCharacters(csa.getPreventionGoaDocumentedInCareAndSupportForm());
        writer.writeEndElement();

        writer.writeStartElement("PatientEverReceivedBasicCarePackage");
        writer.writeCharacters(csa.getPatientEverReceivedBasicCarePackage());
        writer.writeEndElement();

        writer.writeStartElement("PatientReceiveBasicCarePackageInReviewPeriod");
        writer.writeCharacters(csa.getPatientReceiveBasicCarePackageInReviewPeriod());
        writer.writeEndElement();

        writer.writeStartElement("FacilityID");
        writer.writeCharacters(String.valueOf(csa.getFacilityID()));
        writer.writeEndElement();

        writer.writeStartElement("UploaderId");
        writer.writeCharacters(csa.getUploaderID());
        writer.writeEndElement();

        writer.writeStartElement("UploadDt");
        writer.writeCharacters(formatDate2(csa.getUploadDate()));
        writer.writeEndElement();

        writer.writeStartElement("webUploadFlag");
        writer.writeCharacters(csa.getWebUploadFlag());
        writer.writeEndElement();

        writer.writeStartElement("ReviewPeriodID");
        writer.writeCharacters(String.valueOf(csa.getReviewPeriodID()));
        writer.writeEndElement();

        writer.writeEndElement();

    }
     public void writeToXML(PediatricARTAdherence dataARTAdh) throws XMLStreamException {
        /*
         ARTAdherenceAssessmentPerformedDuringLast3Months
         LastDateOfAssessment
         FacilityID
         UploaderId
         UploadDt
         PatientID
         webUploadFlag
         ReviewPeriodID
         */
        writer.writeStartElement("Pediatric_ART_Adherence_Record");
        
        writer.writeStartElement("PatientID");
        writer.writeCharacters(StringUtils.trim(dataARTAdh.getPatientID()));
        writer.writeEndElement();

        writer.writeStartElement("ARTAdherenceAssessmentPerformedDuringLast3Months");
        writer.writeCharacters(dataARTAdh.getArtAdherenceAssessmentPerformedDuringLast3Months());
        writer.writeEndElement();
        
         writer.writeStartElement("FacilityID");
        writer.writeCharacters(String.valueOf(dataARTAdh.getFacilityID()));
        writer.writeEndElement();
        
        writer.writeStartElement("UploaderId");
        writer.writeCharacters(dataARTAdh.getUploaderID());
        writer.writeEndElement();

        writer.writeStartElement("UploadDt");
        writer.writeCharacters(formatDate2(dataARTAdh.getUploadDate()));
        writer.writeEndElement();

        writer.writeStartElement("webUploadFlag");
        writer.writeCharacters(dataARTAdh.getWebUploadFlag());
        writer.writeEndElement();

        writer.writeStartElement("ReviewPeriodID");
        writer.writeCharacters(String.valueOf(dataARTAdh.getReviewPeriodID()));
        writer.writeEndElement();

       /* writer.writeStartElement("LastDateOfAssessment");
        writer.writeCharacters(formatDate2(dataARTAdh.getLastDateOfAssessment()));
        writer.writeEndElement();*/

       

        

        writer.writeEndElement();

    }
     public void writeToXML(PediatricARTRegimenSinceStartingTreatment ele) throws XMLStreamException {

        writer.writeStartElement("Pediatric_ARTRegimenSinceStartingTreatment_Record");

        writer.writeStartElement("PatientID");
        writer.writeCharacters(StringUtils.trim(ele.getPatientID()));
        writer.writeEndElement();

        writer.writeStartElement("PatientOnARTAnytimeDuringReviewPeriod");
        writer.writeCharacters(ele.getPatientOnARTAnytimeDuringReviewPeriod());
        writer.writeEndElement();

        writer.writeStartElement("C1stRegminen");
        writer.writeCharacters(ele.getC1stRegminen());
        writer.writeEndElement();

        writer.writeStartElement("C1stRegimenStartDate");
        writer.writeCharacters(formatDefault2(formatDate2(ele.getC1stRegimenStartDate())));
        writer.writeEndElement();

        writer.writeStartElement("C1stRegimenChangeDate");
        writer.writeCharacters(formatDefault2(formatDate2(ele.getC1stRegimenChangeDate())));
        writer.writeEndElement();

        writer.writeStartElement("C2ndRegimen");
        writer.writeCharacters(ele.getC2ndRegimen());
        writer.writeEndElement();

        writer.writeStartElement("C2ndRegimenStartDate");
        writer.writeCharacters(formatDefault2(formatDate2(ele.getC2ndRegimenStartDate())));
        writer.writeEndElement();

        writer.writeStartElement("C2ndRegimenChangeDate");
        writer.writeCharacters(formatDefault2(formatDate2(ele.getC2ndRegimenChangeDate())));
        writer.writeEndElement();

        writer.writeStartElement("C3rdRegimen");
        writer.writeCharacters(ele.getC3rdRegimen());
        writer.writeEndElement();

        writer.writeStartElement("C3rdRegimenStartDate");
        writer.writeCharacters(formatDefault2(formatDate2(ele.getC3rdRegimenStartDate())));
        writer.writeEndElement();

        writer.writeStartElement("C3rdRegimenChangeDate");
        writer.writeCharacters(formatDefault2(formatDate2(ele.getC3rdRegimenChangeDate())));
        writer.writeEndElement();

        writer.writeStartElement("OtherRegimenSpecify");
        writer.writeCharacters(StringUtils.upperCase(ele.getOtherRegimenSpecify()));
        writer.writeEndElement();

        writer.writeStartElement("FacilityID");
        writer.writeCharacters(String.valueOf(ele.getFacilityID()));
        writer.writeEndElement();

        writer.writeStartElement("UploaderId");
        writer.writeCharacters(String.valueOf(ele.getUploaderId()));
        writer.writeEndElement();

        writer.writeStartElement("UploadDt");
        writer.writeCharacters(formatDate2(ele.getUploadDt()));
        writer.writeEndElement();

        writer.writeStartElement("webUploadFlag");
        writer.writeCharacters(ele.getWebUploadFlag());
        writer.writeEndElement();

        writer.writeStartElement("ReviewPeriodID");
        writer.writeCharacters(String.valueOf(ele.getReviewPeriodID()));
        writer.writeEndElement();
        writer.writeEndElement();
    }
    
    
    public void writeToXML(DataARTAdherence dataARTAdh) throws XMLStreamException {
        writer.writeStartElement("ART_Adherence_Record");

        writer.writeStartElement("PatientID");
        writer.writeCharacters(StringUtils.trim(dataARTAdh.getPatientID()));
        writer.writeEndElement();

        writer.writeStartElement("ARTAdherenceAssessmentPerformedDuringLast3Months");
        writer.writeCharacters(dataARTAdh.getArtAdherenceAssessmentPerformedLast3Months());
        writer.writeEndElement();

        writer.writeStartElement("LastDateOfAssessment");
        writer.writeCharacters(formatDate2(dataARTAdh.getLastDateOfAssessment()));
        writer.writeEndElement();

        writer.writeStartElement("HighestCD4SinceARTinitiation");
        writer.writeCharacters(String.valueOf(dataARTAdh.getHighestCD4SinceARTInitiation()));
        writer.writeEndElement();

        writer.writeStartElement("DateOfHighestCD4Test");
        writer.writeCharacters(formatDate2(dataARTAdh.getDateOfHighestCD4Test()));
        writer.writeEndElement();

        writer.writeStartElement("FacilityID");
        writer.writeCharacters(String.valueOf(dataARTAdh.getFacilityID()));
        writer.writeEndElement();

        writer.writeStartElement("UploaderId");
        writer.writeCharacters(dataARTAdh.getUploaderID());
        writer.writeEndElement();

        writer.writeStartElement("UploadDT");
        writer.writeCharacters(formatDate2(dataARTAdh.getUploadDate()));
        writer.writeEndElement();

        writer.writeStartElement("webUploadFlag");
        writer.writeCharacters(dataARTAdh.getWebUploadFlag());
        writer.writeEndElement();

        writer.writeStartElement("ReviewPeriodID");
        writer.writeCharacters(String.valueOf(dataARTAdh.getReviewPeriodID()));
        writer.writeEndElement();

        writer.writeEndElement();
        /*
        <ART_Adherence_Record>
		<PatientID>OO000002222</PatientID>
		<ARTAdherenceAssessmentPerformedDuringLast3Months>YES            </ARTAdherenceAssessmentPerformedDuringLast3Months>
		<LastDateOfAssessment>39566</LastDateOfAssessment>
		<HighestCD4SinceARTinitiation>0668           </HighestCD4SinceARTinitiation>
		<DateOfHighestCD4Test>39658</DateOfHighestCD4Test>
		<FacilityID>7148</FacilityID>
		<UploaderId>NULL</UploaderId>
		<UploadDT>NULL</UploadDT>
		<webUploadFlag>No</webUploadFlag>
		<ReviewPeriodID>3</ReviewPeriodID>
	</ART_Adherence_Record>
         */

    }

    public void writeToXML(ARTCommence art) throws XMLStreamException  {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        writer.writeStartElement("art");
        writer.writeStartElement("patient_id");
        writer.writeCharacters(String.valueOf(art.getPatientID()));
        writer.writeEndElement();
        
        writer.writeStartElement("encounter_id");
        writer.writeCharacters(String.valueOf(art.getEncounterID()));
        writer.writeEndElement();
        
        writer.writeStartElement("form_id");
        writer.writeCharacters(String.valueOf(art.getFormID()));
        writer.writeEndElement();
        
        writer.writeStartElement("creator_id");
        writer.writeCharacters(String.valueOf(art.getCreatorID()));
        writer.writeEndElement();
        
        writer.writeStartElement("provider_id");
        writer.writeCharacters(String.valueOf(art.getProviderID()));
        writer.writeEndElement();
        
        writer.writeStartElement("location_id");
        writer.writeCharacters(String.valueOf(art.getLocationID()));
        writer.writeEndElement();
        
        writer.writeStartElement("uuid");
        writer.writeCharacters(String.valueOf(art.getUuid()));
        writer.writeEndElement();
        
        writer.writeStartElement("pepfar_id");
        writer.writeCharacters(String.valueOf(art.getPepfarID()));
        writer.writeEndElement();
        
        writer.writeStartElement("hosp_id");
        writer.writeCharacters(String.valueOf(art.getHospID()));
        writer.writeEndElement();
        
        writer.writeStartElement("visit_date");
        writer.writeCharacters(String.valueOf(formatDate(art.getVisitDate())));
        writer.writeEndElement();
        
        writer.writeStartElement("pmm_form");
        writer.writeCharacters(String.valueOf(art.getPmmForm()));
        writer.writeEndElement();
        
        writer.writeStartElement("location");
        writer.writeCharacters(String.valueOf(art.getLocation()));
        writer.writeEndElement();
        
        writer.writeStartElement("entered_by");
        writer.writeCharacters(String.valueOf(art.getEnteredBy()));
        writer.writeEndElement();
        
        writer.writeStartElement("date_entered");
        writer.writeCharacters(String.valueOf(formatDate(art.getDateEntered())));
        writer.writeEndElement();
        
        writer.writeStartElement("provider");
        writer.writeCharacters(String.valueOf(art.getProviderName()));
        writer.writeEndElement();
        
        writer.writeStartElement("date_medically_eligible");
        writer.writeCharacters(String.valueOf(formatDate(art.getDateMedicallyEligible())));
        writer.writeEndElement();
        
        writer.writeStartElement("why_eligible");
        writer.writeCharacters(String.valueOf(art.getWhyEligible()));
        writer.writeEndElement();
        
        writer.writeStartElement("date_initial_adherence_counceling_completed");
        writer.writeCharacters(String.valueOf(formatDate(art.getDateInitialAdherenceCouncelingCompleted())));
        writer.writeEndElement();
        
        writer.writeStartElement("date_transfered_in");
        writer.writeCharacters(String.valueOf(formatDate(art.getDateTransferedIn())));
        writer.writeEndElement();
        
        writer.writeStartElement("facility_transfered_from");
        writer.writeCharacters(String.valueOf(art.getFacilityTransferredFrom()));
        writer.writeEndElement();
        
        writer.writeStartElement("regimen_line_art_start");
        writer.writeCharacters(String.valueOf(art.getFirstRegimenLine()));
        writer.writeEndElement();
        
        writer.writeStartElement("first_line_regimen");
        writer.writeCharacters(String.valueOf(art.getFirstLineRegimen()));
        writer.writeEndElement();
        
        writer.writeStartElement("second_line_regimen");
        writer.writeCharacters(String.valueOf(art.getSecondLineRegimen()));
        writer.writeEndElement();
        
        writer.writeStartElement("art_start_date");
        writer.writeCharacters(String.valueOf(formatDate(art.getVisitDate())));
        writer.writeEndElement();
        
        writer.writeStartElement("age_art_start");
        writer.writeCharacters(String.valueOf(art.getAgeARTStart()));
        writer.writeEndElement();
        
        writer.writeStartElement("who_art_start");
        writer.writeCharacters(String.valueOf(art.getClinicalStageAtARTStart()));
        writer.writeEndElement();
        
        writer.writeStartElement("weight_art_start");
        writer.writeCharacters(String.valueOf(art.getWeight()));
        writer.writeEndElement();
        
        writer.writeStartElement("height_art_start");
        writer.writeCharacters(String.valueOf(art.getHeight()));
        writer.writeEndElement();
        
        writer.writeStartElement("functional_status_art_start");
        writer.writeCharacters(String.valueOf(art.getFunctionalStatus()));
        writer.writeEndElement();
        
        writer.writeStartElement("cd4_art_start");
        writer.writeCharacters(String.valueOf(art.getCd4CountAtARTStart()));
        writer.writeEndElement();
        
        writer.writeStartElement("art_register_serialno");
        writer.writeCharacters(String.valueOf(art.getArtRegisterSerialNo()));
        writer.writeEndElement();
        
        writer.writeEndElement();
    }

    public void writeToXML(CareCardFollowUp card) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void writeToXML(Drug ord) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void writeToXML(Regimen regimen) throws XMLStreamException {
        writer.writeStartElement("regimen");
        
        writer.writeStartElement("location_id");
        writer.writeCharacters(String.valueOf(regimen.getLocationID()));
        writer.writeEndElement();
                
        writer.writeStartElement("patient_id");
        writer.writeCharacters(String.valueOf(regimen.getPatientID()));
        writer.writeEndElement();
        
        writer.writeStartElement("form_id");
        writer.writeCharacters(String.valueOf(regimen.getFormID()));
        writer.writeEndElement();

        writer.writeStartElement("encounter_id");
        writer.writeCharacters(String.valueOf(regimen.getEncounterID()));
        writer.writeEndElement();

        writer.writeStartElement("creator_id");
        writer.writeCharacters(String.valueOf(regimen.getCreatoriD()));
        writer.writeEndElement();

        writer.writeStartElement("provider_id");
        writer.writeCharacters(String.valueOf(regimen.getProviderID()));
        writer.writeEndElement();
        
        writer.writeStartElement("uuid");
        writer.writeCharacters(String.valueOf(regimen.getUuid()));
        writer.writeEndElement();

        writer.writeStartElement("pepfar_id");
        writer.writeCharacters(String.valueOf(regimen.getPepfarID()));
        writer.writeEndElement();

        writer.writeStartElement("hosp_id");
        writer.writeCharacters(String.valueOf(regimen.getHospID()));
        writer.writeEndElement();
        
        writer.writeStartElement("visit_date");
        writer.writeCharacters(String.valueOf(formatDate(regimen.getVisitDate())));
        writer.writeEndElement();

        writer.writeStartElement("location");
        writer.writeCharacters(String.valueOf(regimen.getLocation()));
        writer.writeEndElement();

        writer.writeStartElement("pmm_form");
        writer.writeCharacters(String.valueOf(regimen.getPmmForm()));
        writer.writeEndElement();

        writer.writeStartElement("visit_type");
        writer.writeCharacters(String.valueOf(regimen.getVisitType()));
        writer.writeEndElement();

        writer.writeStartElement("pickup_reason");
        writer.writeCharacters(String.valueOf(regimen.getPickupReason()));
        writer.writeEndElement();
        
        writer.writeStartElement("treatment_type");
        writer.writeCharacters(String.valueOf(regimen.getTreatmentType()));
        writer.writeEndElement();
        
        writer.writeStartElement("regimen_line");
        writer.writeCharacters(String.valueOf(regimen.getRegimenLine()));
        writer.writeEndElement();
        
        writer.writeStartElement("first_line");
        writer.writeCharacters(String.valueOf(regimen.getFirstLine()));
        writer.writeEndElement();
        
        writer.writeStartElement("second_line");
        writer.writeCharacters(String.valueOf(regimen.getSecondLine()));
        writer.writeEndElement();
        
        writer.writeStartElement("regimen_code");
        writer.writeCharacters(String.valueOf(regimen.getRegimenCode()));
        writer.writeEndElement();
        
        writer.writeStartElement("other_regimen");
        writer.writeCharacters(String.valueOf(regimen.getOtherLine()));
        writer.writeEndElement();
        
        writer.writeStartElement("ordered_by");
        writer.writeCharacters(String.valueOf(regimen.getOrderedBy()));
        writer.writeEndElement();
        
        writer.writeStartElement("date_ordered");
        writer.writeCharacters(formatDate(regimen.getDateOrdered()));
        writer.writeEndElement();
        
        writer.writeStartElement("counseled_by");
        writer.writeCharacters(regimen.getCounseledBy());
        writer.writeEndElement();
        
        writer.writeStartElement("date_counseled");
        writer.writeCharacters(formatDate(regimen.getDateCounseled()));
        writer.writeEndElement();
        
        writer.writeStartElement("entered_by");
        writer.writeCharacters(regimen.getEnteredBy());
        writer.writeEndElement();
        
        writer.writeStartElement("date_entered");
        writer.writeCharacters(formatDate(regimen.getDateEntered()));
        writer.writeEndElement();
        

        writer.writeEndElement();
    }





}
