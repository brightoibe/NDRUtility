/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import static com.inductivehealth.ndr.client.Client.getXmlDate;
import static com.inductivehealth.ndr.client.Client.getXmlDateTime;
import com.inductivehealth.ndr.client.Validator;
import com.inductivehealth.ndr.schema.AddressType;
import com.inductivehealth.ndr.schema.CommonQuestionsType;
import com.inductivehealth.ndr.schema.ConditionSpecificQuestionsType;
import com.inductivehealth.ndr.schema.ConditionType;
import com.inductivehealth.ndr.schema.Container;
import com.inductivehealth.ndr.schema.EncountersType;
import com.inductivehealth.ndr.schema.FacilityType;
import com.inductivehealth.ndr.schema.FingerPrintType;
import com.inductivehealth.ndr.schema.HIVEncounterType;
import com.inductivehealth.ndr.schema.HIVQuestionsType;
import com.inductivehealth.ndr.schema.IndividualReportType;
import com.inductivehealth.ndr.schema.LaboratoryReportType;
import com.inductivehealth.ndr.schema.MessageHeaderType;
import com.inductivehealth.ndr.schema.PatientDemographicsType;
import com.inductivehealth.ndr.schema.ProgramAreaType;
import com.inductivehealth.ndr.schema.RegimenType;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import model.datapump.BiometricInfo;
import model.datapump.Concept;
import model.datapump.Demographics;
import model.datapump.DisplayScreen;
import model.datapump.DrugOrder;
import model.datapump.Drugs;
import model.datapump.Location;
import model.datapump.Obs;
import model.datapump.PatientRegimen;
import model.datapump.Visit;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import util.CustomErrorHandler;
import util.FileManager;
import util.LocationMap;

/**
 *
 * @author The Bright
 */
public class NDRMasterDictionary {

    private final static String schemaVersion = "1.3";
    private final static String HIV_NDR_CONDITION_CODE = "86406008";
    private final String PROGRAM_AREA_CODE = "HIV";
    private String validator="";
    private Container container;
    private FacilityType sendingOrganization;
    private ConditionType conditionType;
    private ProgramAreaType programAreaType;
    private String messageID;
    private NDRClinicalDictionary clinicalDictionary;
    private NDRLabDictionary labDictionary;
    private NDRDemographicsDictionary demoDictionary;
    private NDRHIVCommonQuestionsDictionary commonQuestionDictionary;
    private NDRHIVQuestionTypeDictionary hivQuestionTypeDictionary;
    private NDRPharmacyDictionary pharmacyDictionary;
    private FileManager mgr;
    private JAXBContext jaxbContext = null;
    private HashMap<Integer, Concept> conceptDictionary;

    public NDRMasterDictionary() {
        clinicalDictionary = new NDRClinicalDictionary();
        labDictionary = new NDRLabDictionary();
        demoDictionary = new NDRDemographicsDictionary();
        commonQuestionDictionary = new NDRHIVCommonQuestionsDictionary();
        hivQuestionTypeDictionary = new NDRHIVQuestionTypeDictionary();
        pharmacyDictionary = new NDRPharmacyDictionary();
        mgr = new FileManager();
    }

    public Container createContainer(String ipName, String ipCode, String messageStatus) throws DatatypeConfigurationException {
        messageID = UUID.randomUUID().toString();
        container = new Container();
        validator=UUID.randomUUID().toString();
        //Set the Header Information
        @SuppressWarnings("UnusedAssignment")
        MessageHeaderType header = new MessageHeaderType();

        header.setMessageCreationDateTime(getXmlDateTime(new Date()));
        header.setMessageStatusCode(messageStatus);
        header.setMessageSchemaVersion(new BigDecimal(schemaVersion));
        header.setMessageUniqueID(messageID);
        //Set the Sending Organization in the Header
        //In this scenario we are using a fictional IP
        sendingOrganization = new FacilityType();
        sendingOrganization.setFacilityName(ipName);
        sendingOrganization.setFacilityID(ipCode);
        sendingOrganization.setFacilityTypeCode("IP");
        header.setMessageSendingOrganization(sendingOrganization);
        //Set the Header to the Container
        container.setMessageHeader(header);
        container.setValidation(validator);
        return container;
    }

    public IndividualReportType createIndividualReport() {
        IndividualReportType indRpt = new IndividualReportType();
        return indRpt;
    }

    public PatientDemographicsType createPatientDemographics(Demographics patient, Location loc, List<Obs> obsList, LocationMap locMap) throws DatatypeConfigurationException {
        PatientDemographicsType demo = demoDictionary.createPatientDemographics(patient, loc, obsList, locMap);
        return demo;
    }

    public ConditionType createHIVConditionType() {
        conditionType = new ConditionType();
        conditionType.setConditionCode(HIV_NDR_CONDITION_CODE);

        return conditionType;
    }

    public ConditionType createConiditionTypeWithProgramArea(Demographics pts) {
        conditionType = createHIVConditionType();
        programAreaType = createHIVProgramAreaType();
        AddressType addressType = demoDictionary.createAddressType(pts);
        conditionType.setProgramArea(programAreaType);
        conditionType.setPatientAddress(addressType);
        return conditionType;

    }

    public ProgramAreaType createHIVProgramAreaType() {
        programAreaType = new ProgramAreaType();
        programAreaType.setProgramAreaCode(PROGRAM_AREA_CODE);
        return programAreaType;
    }

    public ConditionSpecificQuestionsType createConditionSpecificQuestionType() throws DatatypeConfigurationException {
        ConditionSpecificQuestionsType disease = new ConditionSpecificQuestionsType();
        return disease;
    }

    public void writeFile(Marshaller jaxbMarshaller, Container container, File file, CustomErrorHandler errorHandler) throws SAXParseException, SAXException, PropertyException, JAXBException, IOException {
        javax.xml.validation.Validator validator = jaxbMarshaller.getSchema().newValidator();
        jaxbMarshaller.marshal(container, file);
        validator.setErrorHandler(errorHandler);

        validator.validate(new StreamSource(file));

    }

    public Marshaller createMarshaller(JAXBContext jaxbContext) throws JAXBException, SAXException {
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = sf.newSchema(getClass().getResource("/resource/NDR 1.3.xsd"));

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

        jaxbMarshaller.setSchema(schema);

        //Call Validator class to perform the validation
        jaxbMarshaller.setEventHandler(new Validator());

        return jaxbMarshaller;
    }

    public void initializeLogFile() throws IOException {
        mgr.createCSVWriter("errorlog.csv");
        String[] headers = {"ErrorFileName", "ErrorLine", "ErrorPosition", "ErrorPosition", "ErrorMessage"};
        mgr.writeHeader(headers);
    }

    public JAXBContext createJAXBContext() throws JAXBException {
        jaxbContext = JAXBContext.newInstance("com.inductivehealth.ndr.schema");
        return jaxbContext;
    }

    public void setConceptDictionary(HashMap<Integer, Concept> dictionary) {
        this.conceptDictionary = dictionary;
        this.labDictionary.setConceptDictionary(dictionary);
    }

    public CommonQuestionsType createCommonQuestionType(String hospID, List<Obs> obsList, Date firstVisitDate, Date lastVisitDate, boolean patientdiedfromillness, int age, String gender) throws DatatypeConfigurationException {
           return commonQuestionDictionary.createCommonQuestionType(hospID, obsList, firstVisitDate, lastVisitDate, patientdiedfromillness, age, gender);
    }

    public HIVQuestionsType createHIVQuestionType(PatientRegimen openmrsFirstRegimen, Date artStartDate, Date enrollmentDt, boolean onART, List<Obs> obsList) throws DatatypeConfigurationException {
        return hivQuestionTypeDictionary.createHIVQuestionType(openmrsFirstRegimen, artStartDate, enrollmentDt, onART, obsList);
    }

    public List<RegimenType> createRegimenTypeList(Demographics pts, List<Obs> pharmacyObsList) throws DatatypeConfigurationException {
        return pharmacyDictionary.constructRegimenTypeList(pts, pharmacyObsList);
    }

    public EncountersType createEncounterType() {
        EncountersType encounterType = new EncountersType();
        return encounterType;
    }

    public Date getEnrollmentDateForPatient(Demographics pts, Map<Integer, Date> firstVisitDateDictionary) {
        Date enrollDate = null, adultEnrollDate = null;
        adultEnrollDate = pts.getAdultEnrollmentDt();
        if (adultEnrollDate != null) {
            enrollDate = adultEnrollDate;
        } else {
            enrollDate = firstVisitDateDictionary.get(pts.getPatientID());
        }
        return enrollDate;
    }

    public void logError(File file2, SAXParseException ex, DisplayScreen screen) {
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
    }
    public void logError(File file2, SAXException ex, DisplayScreen screen){
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
    }
    public void logError(Exception ex,DisplayScreen screen){
        screen.updateStatus(ex.getMessage());
        ex.printStackTrace();
    }

    public HIVEncounterType createHIVEncounter(Visit ele, Date artStartDate, List<Obs> obsList, ArrayList<DrugOrder> orders, ArrayList<Drugs> drugList) throws DatatypeConfigurationException {
        return clinicalDictionary.createHIVEncounter(ele, artStartDate, obsList, orders, drugList);
    }
    public FingerPrintType createFingerPrintType(List<BiometricInfo> ptsBiometricInfoList) throws DatatypeConfigurationException{
        return demoDictionary.createFingerPrintTypeFromBiometricInfoList(ptsBiometricInfoList);
    }
    public LaboratoryReportType createLaboratoryReportType(Visit ele, ArrayList<Obs> labObsList, Date artStartDate) throws DatatypeConfigurationException {
       return labDictionary.createLaboratoryReportType(ele, labObsList, artStartDate);
    }
    public static void main(String[] arg){
        NDRPharmacyDictionary pharmDictionary=new NDRPharmacyDictionary();
        //System.out.println(pharmDictionary.getRegimenCode("AZT-3TC-NVP"));
        System.out.println(pharmDictionary.isValidOIDrug("CTX prophylaxis"));
    }

}
