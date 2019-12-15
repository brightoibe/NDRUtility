package com.inductivehealth.ndr.client;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import com.inductivehealth.ndr.schema.AnswerType;
import com.inductivehealth.ndr.schema.CodedSimpleType;
import com.inductivehealth.ndr.schema.CommonQuestionsType;
import com.inductivehealth.ndr.schema.ConditionSpecificQuestionsType;
import com.inductivehealth.ndr.schema.ConditionType;
import com.inductivehealth.ndr.schema.Container;
import com.inductivehealth.ndr.schema.EncountersType;
import com.inductivehealth.ndr.schema.FacilityType;
import com.inductivehealth.ndr.schema.HIVEncounterType;
import com.inductivehealth.ndr.schema.HIVQuestionsType;
import com.inductivehealth.ndr.schema.IndividualReportType;
import com.inductivehealth.ndr.schema.LaboratoryOrderAndResult;
import com.inductivehealth.ndr.schema.LaboratoryReportType;
import com.inductivehealth.ndr.schema.MessageHeaderType;
import com.inductivehealth.ndr.schema.NoteType;
import com.inductivehealth.ndr.schema.NumericType;
import com.inductivehealth.ndr.schema.PatientDemographicsType;
import com.inductivehealth.ndr.schema.ProgramAreaType;
import com.inductivehealth.ndr.schema.RegimenType;
import java.io.StringWriter;

/**
 * 
 * @author Stephen Macauley, InductiveHealth Informatics (stephen@inductivehealth.com)
 * 
 * <p>
 * This class will generate a sample NDR message using the NDR Schema and sample data
 * 
 * This class is intended provide an <i>example</i> of how to utilize the Java Architecture for XML Binding (JAXB) 
 * Application Programming Interface (API) [http://www.oracle.com/technetwork/articles/javase/index-140168.html]
 * to bind data based on the NDR Schema to generate a validate XML message.
 *
 * The data utilized in this example if fictional.
 * 
 * Test client implemented using jdk1.6.0_14 using Eclipse Java EE IDE for Web Developers 
 * using Dali Java Persistence Tools - JAXB Support Version 1.5.2.v201501171820 for the JAXB implementation
 * 
 * </p>
 * 
 *  */
public class ClientInitial {

	public static void main(String[] args) throws JAXBException, SAXException, DatatypeConfigurationException, IOException {

	
JAXBContext jaxbContext 
        = JAXBContext.newInstance("com.inductivehealth.ndr.schema");

		//Represents the Container (highest level of the schema)
		Container container = new Container();
		
		//Set the Header Information
		MessageHeaderType header = new MessageHeaderType();
		header.setMessageCreationDateTime(getXmlDateTime(new Date()));
		header.setMessageStatusCode("INITIAL");
		header.setMessageSchemaVersion(new BigDecimal("1.2"));
		header.setMessageUniqueID("00000001");
		
		//Set the Sending Organization in the Header
		//In this scenario we are using a fictional IP
		FacilityType sendingOrganization = new FacilityType();
		sendingOrganization.setFacilityName("FICTIONAL_IP");
		sendingOrganization.setFacilityID("abc123");
		sendingOrganization.setFacilityTypeCode("IP");
		header.setMessageSendingOrganization(sendingOrganization);
		
		//Set the Header to the Container
		container.setMessageHeader(header);
		
		//Create the Individual Report
		IndividualReportType individual = new IndividualReportType();
		
		//Patient Demographics
		PatientDemographicsType patient = new PatientDemographicsType();
		patient.setPatientIdentifier("1001"); //This is the EMR Identifier
		
		//Set the DOB
		Calendar dob = Calendar.getInstance();
		dob.set(Calendar.YEAR, 1996);
		dob.set(Calendar.MONTH, Calendar.JANUARY);
		dob.set(Calendar.DAY_OF_MONTH, 15);
		Date dobDate = dob.getTime();
		
		patient.setPatientDateOfBirth(getXmlDate(dobDate));
		patient.setPatientSexCode("F");
		patient.setPatientDeceasedIndicator(false);
		
		FacilityType treatmentFacility = new FacilityType();
		treatmentFacility.setFacilityName("Fictional Hospital");
		treatmentFacility.setFacilityID("def0987");
		treatmentFacility.setFacilityTypeCode("FAC");
		patient.setTreatmentFacility(treatmentFacility);

		//Add a Patient Note
		NoteType patientNote = new NoteType();
		patientNote.setNote("This is an example of a Patient Note that is provided");
		patient.setPatientNotes(patientNote);
		individual.setPatientDemographics(patient);
	
		//Condition
		ConditionType condition = new ConditionType();
		condition.setConditionCode("86406008");
		ProgramAreaType pa = new ProgramAreaType();
		pa.setProgramAreaCode("HIV");
		condition.setProgramArea(pa);
		
		//Common Questions
		CommonQuestionsType common = new CommonQuestionsType();
		common.setPatientDieFromThisIllness(false);
		common.setPatientAge(19);
		condition.setCommonQuestions(common);

		//HIV Specific
		ConditionSpecificQuestionsType disease = new ConditionSpecificQuestionsType();
		HIVQuestionsType hiv = new HIVQuestionsType();
		hiv.setCareEntryPoint("2");
		
		CodedSimpleType cst1 = new CodedSimpleType();
		cst1.setCode("1h");
		cst1.setCodeDescTxt("AZT-3TC-TDF");
		hiv.setFirstARTRegimen(cst1);
		hiv.setARTStartDate(getXmlDate(new Date()));
		
		disease.setHIVQuestions(hiv);
		condition.setConditionSpecificQuestions(disease);
		
		//Regimens 		
		RegimenType regimen = new RegimenType();
		CodedSimpleType cst2 = new CodedSimpleType();
		cst2.setCode("1a");
		cst2.setCodeDescTxt("Zidovudine (AZT, ZDV; Retrovir) 100mg");
		regimen.setVisitDate(getXmlDate(new Date()));
		regimen.setVisitID("0000001");
		regimen.setPrescribedRegimen(cst2);
		regimen.setDateRegimenStarted(getXmlDate(new Date()));
		regimen.setPrescribedRegimenCurrentIndicator(true);
		condition.getRegimen().add(regimen);


		//Encounters
		//Encounter 1
		EncountersType encounter = new EncountersType();
		HIVEncounterType hivEncounter = new HIVEncounterType();
		hivEncounter.setVisitDate(getXmlDate(new Date()));
		hivEncounter.setVisitID("0000001");
		hivEncounter.setTBStatus("1");
		encounter.getHIVEncounter().add(hivEncounter);
		
		condition.setEncounters(encounter);
		
		
		//Laboratory Report
		LaboratoryReportType lab = new LaboratoryReportType();
		
		lab.setVisitDate(getXmlDate(new Date()));
		lab.setVisitID("0000001");
		lab.setLaboratoryTestIdentifier("0000001");
		
		//Lab Result 1
		LaboratoryOrderAndResult result = new LaboratoryOrderAndResult();
		CodedSimpleType cst3 = new CodedSimpleType();
		cst3.setCode("1");
		cst3.setCodeDescTxt("CD4");
		result.setLaboratoryResultedTest(cst3);
		
		AnswerType answer = new AnswerType();
		NumericType numeric = new NumericType();
		numeric.setValue1(1110);
		answer.setAnswerNumeric(numeric);
		result.setLaboratoryResult(answer);
		
		lab.getLaboratoryOrderAndResult().add(result);
		condition.getLaboratoryReport().add(lab);
		individual.getCondition().add(condition);
		container.setIndividualReport(individual);
		
		
		//Validate Message Against NDR Schema (Version 1.2)
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); 
                Schema schema = sf.newSchema(new File("NDR 1.2.xsd")); 
		
                jaxbMarshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );
		jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		
		jaxbMarshaller.setSchema(schema);
		
		//Call Validator class to perform the validation		
		jaxbMarshaller.setEventHandler(new Validator());
				
		/** Date date = new Date() ;
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy") ;
		SimpleDateFormat dateForma2t = new SimpleDateFormat("HHmmss.ms") ;
		File file = new File("FICTIONALIP_39383933_" + dateFormat.format(date) + "_" + dateForma2t.format(date) +  ".xml") ;
		
		jaxbMarshaller.marshal(container, file);  */ 
                StringWriter xml = new StringWriter();
		jaxbMarshaller.marshal(container, xml);
	    System.out.println(xml);
        }
	
	//Utility methods to format Date to XMLGregorianCalendar
	public static XMLGregorianCalendar getXmlDate(Date date) throws DatatypeConfigurationException {
	    return DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat("yyyy-MM-dd").format(date));
	}
	
	public static XMLGregorianCalendar getXmlDateTime(Date date) throws DatatypeConfigurationException {
	    return DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(date));
	}
}
