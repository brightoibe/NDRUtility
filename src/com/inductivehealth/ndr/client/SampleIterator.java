package com.inductivehealth.ndr.client;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.xml.sax.SAXException;

import com.inductivehealth.ndr.schema.CodedSimpleType;
import com.inductivehealth.ndr.schema.ConditionType;
import com.inductivehealth.ndr.schema.RegimenType;

/**
 * 
 * @author Stephen Macauley, InductiveHealth Informatics (stephen@inductivehealth.com)
 * 
 * 
 *  */
public class SampleIterator {

	public static void main(String[] args) throws JAXBException, SAXException, DatatypeConfigurationException, IOException {

		//For this example, we will create multiple instances of Regimens to simulate a ResultSet pulled for a Patient from an EMR
		//This simple class represents a Regimen object returned from the EMR database
		//This is just for illustrative purposes
		final class EMRRegimen{

			  String RegimenName;
			  String RegimenCode;
			  Date RegimenStartDate;
			  String VisitNumber;
			  Date VisitDate;
			
			  /**
			 * @return the regimenName
			 */
			public String getRegimenName() {
				return RegimenName;
			}
			/**
			 * @param regimenName the regimenName to set
			 */
			public void setRegimenName(String regimenName) {
				RegimenName = regimenName;
			}
			/**
			 * @return the regimenCode
			 */
			public String getRegimenCode() {
				return RegimenCode;
			}
			/**
			 * @param regimenCode the regimenCode to set
			 */
			public void setRegimenCode(String regimenCode) {
				RegimenCode = regimenCode;
			}
			/**
			 * @return the regimenStartDate
			 */
			public Date getRegimenStartDate() {
				return RegimenStartDate;
			}
			/**
			 * @param regimenStartDate the regimenStartDate to set
			 */
			public void setRegimenStartDate(Date regimenStartDate) {
				RegimenStartDate = regimenStartDate;
			}
			/**
			 * @return the visitNumber
			 */
			public String getVisitNumber() {
				return VisitNumber;
			}
			/**
			 * @param visitNumber the visitNumber to set
			 */
			public void setVisitNumber(String visitNumber) {
				VisitNumber = visitNumber;
			}
			/**
			 * @return the visitDate
			 */
			public Date getVisitDate() {
				return VisitDate;
			}
			/**
			 * @param visitDate the visitDate to set
			 */
			public void setVisitDate(Date visitDate) {
				VisitDate = visitDate;
			}

		}
		
		
		//We will now put multiple instances of a Regimens into the EMRRegimen object
		//This is just sample / fictional data
		
		EMRRegimen emrRegimen1 = new EMRRegimen();
		emrRegimen1.setRegimenCode("1a");
		emrRegimen1.setRegimenName("AZT-3TC-EFV");
		emrRegimen1.setRegimenStartDate(new Date());
		emrRegimen1.setVisitDate(new Date());
		emrRegimen1.setVisitNumber("00001");
		

		EMRRegimen emrRegimen2 = new EMRRegimen();
		emrRegimen2.setRegimenCode("2a");
		emrRegimen2.setRegimenName("TDF-3TC-ATV/r");
		emrRegimen2.setRegimenStartDate(new Date());
		emrRegimen2.setVisitDate(new Date());
		emrRegimen2.setVisitNumber("00001");
		
		
		EMRRegimen emrRegimen3 = new EMRRegimen();
		emrRegimen3.setRegimenCode("2e");
		emrRegimen3.setRegimenName("AZT-3TC-LPV/r");
		emrRegimen3.setRegimenStartDate(new Date());
		emrRegimen3.setVisitDate(new Date());
		emrRegimen3.setVisitNumber("00001");
				
		//Create an ArrayList to simulate a database resultset

		ArrayList<EMRRegimen> resultSet = new ArrayList<EMRRegimen>();
		resultSet.add(emrRegimen1);
		resultSet.add(emrRegimen2);
		resultSet.add(emrRegimen3);
				
		//Simulate looping through the resultset and create the XML
		
		//Creating the condition goes outside of the loop, otherwise it would be overwritten each time
		ConditionType condition = new ConditionType();
		
		for(int i = 0; i  < resultSet.size() ; i++){
	
		//Create the Regimen instance in the XML
		RegimenType regimen = new RegimenType();
		CodedSimpleType cst = new CodedSimpleType();
		cst.setCode(resultSet.get(i).getRegimenCode());
		cst.setCodeDescTxt(resultSet.get(i).getRegimenName());
		regimen.setVisitDate(getXmlDate(resultSet.get(i).getVisitDate()));
		regimen.setVisitID(resultSet.get(i).getVisitNumber());
		regimen.setPrescribedRegimen(cst);
		regimen.setDateRegimenStarted(getXmlDate(resultSet.get(i).getRegimenStartDate()));
		
		//Assign the XML Regimen instance to the Condition in the XML
		condition.getRegimen().add(regimen);
		}
		
    
	}
	
	//Utility methods to format Date to XMLGregorianCalendar
	public static XMLGregorianCalendar getXmlDate(Date date) throws DatatypeConfigurationException {
	    return DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat("yyyy-MM-dd").format(date));
	}
	
	public static XMLGregorianCalendar getXmlDateTime(Date date) throws DatatypeConfigurationException {
	    return DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(date));
	}
}
