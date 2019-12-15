package com.inductivehealth.ndr.dao;

import static com.inductivehealth.ndr.client.Client.getXmlDate;
import com.inductivehealth.ndr.connection.ConnectionManager;
import com.inductivehealth.ndr.schema.AnswerType;
import com.inductivehealth.ndr.schema.CodedSimpleType;
import com.inductivehealth.ndr.schema.LaboratoryOrderAndResult;
import com.inductivehealth.ndr.schema.NumericType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author THANKGOD
 */
public class LaboratoryOrderAndResultDao {
    private static Connection con = null;
    private static Statement stmt = null;
    private static ResultSet rs = null;
    private static PreparedStatement preparedStmt;
    
    protected String laboratoryTestTypeCode;    
    protected XMLGregorianCalendar orderedTestDate;   
    protected CodedSimpleType laboratoryOrderedTest;   
    protected CodedSimpleType laboratoryResultedTest;   
    protected AnswerType laboratoryResult;    
    protected XMLGregorianCalendar resultedTestDate;
    protected String otherLaboratoryInformation;
    
    public static void main(String[] args) throws DatatypeConfigurationException
    {
        retrieveLaboratoryOrderAndResult("16486");
    }
    
     public static ArrayList<LaboratoryOrderAndResult> retrieveLaboratoryOrderAndResult(String patientId) throws DatatypeConfigurationException 
    {
        ArrayList<LaboratoryOrderAndResult> resultWrapObject = new ArrayList<LaboratoryOrderAndResult>();
              
       try{           
           con = ConnectionManager.getConnection();
           stmt = con.createStatement();
           String sql = "SELECT\n" +
                        "obs.obs_id,\n" +
                        "obs.person_id,\n" +
                        "obs.concept_id,\n" +
                        "cn1.name AS VARIABLE_NAME,\n" +
                        "obs.encounter_id,\n" +
                        "obs.obs_datetime,\n" +
                        "obs.location_id,\n" +
                        "obs.obs_group_id,\n" +
                        "obs.value_coded,\n" +
                        "cn2.name AS CODED_VARIABLE_VALUE,\n" +
                        "obs.value_datetime,\n" +
                        "obs.value_numeric,\n" +
                        "obs.value_text,\n" +
                        "obs.creator,\n" +
                        "obs.date_created,\n" +
                        "obs.voided,\n" +
                        "obs.uuid,\n" +
                        "obs.value_group_id,\n" +
                        "obs.value_coded_name_id,\n" +
                        "encounter.form_id,\n" +
                        "encounter.encounter_datetime\n" +
                        "FROM\n" +
                        "obs\n" +
                        "inner join encounter using (encounter_id)\n" +
                        "left join concept_name cn1 on(cn1.concept_id=obs.concept_id and cn1.locale_preferred=1 and cn1.locale='en')\n" +
                        "left join concept_name cn2 on(cn2.concept_id=obs.value_coded and cn2.locale_preferred=1 and cn2.locale='en') where encounter.form_id=21 and obs.person_id=? \n" +
                        "ORDER BY person_id,obs_datetime,obs.concept_id,obs_group_id";
           
          
              preparedStmt = con.prepareStatement(sql);
              preparedStmt.setString(1,patientId);             
              rs = preparedStmt.executeQuery(); 
                         
               while(rs.next()){
                   
                    LaboratoryOrderAndResult resultWrap = new LaboratoryOrderAndResult(); 
                    
                    //Type Of Laboratory Test
                    String laboratoryTestTypeCode = rs.getString("VARIABLE_NAME"); 
                    resultWrap.setLaboratoryTestTypeCode(laboratoryTestTypeCode);
                    
                    //Date Order was made
                    //XMLGregorianCalendar orderedTestDate;
                    java.sql.Date orderDate = rs.getDate("obs.obs_datetime");
                    resultWrap.setOrderedTestDate(getXmlDate(orderDate));
                    
                    //Code that represents the Ordered Test
                    CodedSimpleType laboratoryOrderedTest = new CodedSimpleType();
                    String code = rs.getString("obs.concept_id");
                    String codeDescTxt = rs.getString("VARIABLE_NAME");
                    laboratoryOrderedTest.setCode(code);
                    laboratoryOrderedTest.setCodeDescTxt(codeDescTxt);
                    resultWrap.setLaboratoryOrderedTest(laboratoryOrderedTest);
                    
                    //Code that represents the Resulted Test
                    CodedSimpleType laboratoryResultedTest = new CodedSimpleType(); 
                    String resultCode = rs.getString("obs.concept_id");
                    String resultCodeDescTxt = rs.getString("VARIABLE_NAME");
                    laboratoryResultedTest.setCode(resultCode);
                    laboratoryResultedTest.setCodeDescTxt(resultCodeDescTxt);
                    resultWrap.setLaboratoryResultedTest(laboratoryResultedTest);
                    /*Captures the type of result (Numeric 
                    (including Units), Coded, Text, Date)*/
                    AnswerType laboratoryResult = new AnswerType();
                    /*AnswerType properties includes:
                            CodedType answerCode;
                            XMLGregorianCalendar answerDate;
                            XMLGregorianCalendar answerDateTime;
                            NumericType answerNumeric;
                            String answerText;
                    We will be representing answerNumeric only*/
                    Float answerValue = rs.getFloat("obs.value_numeric");
                    String answerText = rs.getString("obs.value_text");
                    NumericType value = new NumericType();
                    value.setValue1(answerValue);
                    laboratoryResult.setAnswerNumeric(value);
                    laboratoryResult.setAnswerText(answerText);
                    resultWrap.setLaboratoryResult(laboratoryResult);
                    
                    //Date Results are made available
                    //XMLGregorianCalendar resultedTestDate;
                    java.sql.Date resultDate = rs.getDate("date_created");
                    resultWrap.setResultedTestDate(getXmlDate(resultDate));
                   
                    
                    //Captures additional (free form) laboratory information
                    //String otherLaboratoryInformation;
                    resultWrapObject.add(resultWrap);
                    //we are going to test this downl here
                    //System.out.println(resultWrapObject);                           
        } 
        
    }catch(SQLException se){
       se.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(preparedStmt!=null)
            con.close();
      }catch(Exception se){
      }// do nothing
      try{
         if(con!=null)
            con.close();
      }catch(SQLException se){
      }
   }
return resultWrapObject;
}
     
          
    
}
