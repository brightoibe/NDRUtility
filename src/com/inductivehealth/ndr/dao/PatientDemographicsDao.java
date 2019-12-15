
package com.inductivehealth.ndr.dao;

import static com.inductivehealth.ndr.client.Client.getXmlDate;
import com.inductivehealth.ndr.connection.ConnectionManager;
import com.inductivehealth.ndr.schema.FacilityType;
import com.inductivehealth.ndr.schema.IdentifierType;
import com.inductivehealth.ndr.schema.IdentifiersType;
import com.inductivehealth.ndr.schema.NoteType;
import com.inductivehealth.ndr.schema.PatientDemographicsType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author THANKGOD
 */
public class PatientDemographicsDao {
    private static Connection con = null;
    private static Statement stmt = null;
    private static ResultSet rs = null;
    private static PreparedStatement preparedStmt;
   
    protected String patientIdentifier;   
    protected FacilityType treatmentFacility;    
    protected IdentifiersType otherPatientIdentifiers;    
    protected XMLGregorianCalendar patientDateOfBirth;   
    protected String patientSexCode;    
    protected Boolean patientDeceasedIndicator;  
    protected XMLGregorianCalendar patientDeceasedDate;    
    protected String patientPrimaryLanguageCode;   
    protected String patientEducationLevelCode;    
    protected String patientOccupationCode;  
    protected String patientMaritalStatusCode;
    protected String stateOfNigeriaOriginCode;
    protected NoteType patientNotes;


  
    
    public static String getPatientIdentifier()
    {
        String patientIdentifier = "";
        
        try {
            
            con = ConnectionManager.getConnection();
            stmt = con.createStatement();
                     
            String sql = "SELECT DISTINCT patient_identifier.identifier,patient_identifier.patient_id\n" +
                    "FROM person\n" +
                    "INNER JOIN person_name\n" +
                    "ON person.person_id = person_name.person_id\n" +
                    "INNER JOIN patient_identifier\n" +
                    "ON person.person_id= patient_identifier.patient_id\n" +
                    "WHERE patient_identifier.identifier_type=4 LIMIT 1";
            
            ResultSet rs;
            rs = stmt.executeQuery(sql);                     
            
            while(rs.next()){                
                patientIdentifier = rs.getString("patient_identifier.patient_id");
                //System.out.println(patientIdentifier);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PatientDemographicsDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return patientIdentifier;
    }
    
    /*public static PatientDemographicsType patientsBasicDemographics(String patientId) throws DatatypeConfigurationException
    {
        
           PatientDemographicsType pdt = new PatientDemographicsType(); 
                
        try {
            
            con = ConnectionManager.getConnection();
            //stmt = con.createStatement();
                     
            String sql = "SELECT DISTINCT patient_identifier.identifier,person_address.city_village,person_address.address1,person_address.address2,person_address.state_province,patient_identifier.patient_id,person_name.given_name,person_name.family_name,person.gender,person.birthdate,person.dead,person.death_date,patient_identifier.identifier_type,patient_identifier.location_id\n" +
                    "FROM person\n" +
                    "INNER JOIN person_name\n" +
                    "ON person.person_id = person_name.person_id\n" +
                    "INNER JOIN patient_identifier\n" +
                    "ON person.person_id= patient_identifier.patient_id\n" +
                    "INNER JOIN person_address\n" +
                    "ON person.person_id= person_address.person_id\n" +
                    "WHERE patient_identifier.identifier_type=4 AND patient_identifier.patient_id=?";
                                
              preparedStmt = con.prepareStatement(sql);
              preparedStmt.setString(1,patientId);
              ResultSet rs = preparedStmt.executeQuery();
              
              while(rs.next()){
             
                String pepfarno = rs.getString("patient_identifier.identifier");
                pdt.setPatientIdentifier(pepfarno);
                //System.out.println(pepfarno);
                //Need to write a method to pull facility name from database
                String facility_name = "PHC Dagiri";
                String facility_id = rs.getString("patient_identifier.location_id");
                String facility_typecode = "FAC";
                
                FacilityType treatmentFacility = new FacilityType();
                treatmentFacility.setFacilityName(facility_name);
                treatmentFacility.setFacilityID(facility_id);                
                treatmentFacility.setFacilityTypeCode(facility_typecode);
                pdt.setTreatmentFacility(treatmentFacility);
                
                //ArrayList<IdentifierType> otherIdType = new ArrayList<>();
                //otherIdType = getHospId(patientIdentifier);
                
                
                String firstname = rs.getString("person_name.given_name");
                String lastname= rs.getString("person_name.family_name");
                
                java.sql.Date birthdate = rs.getDate("person.birthdate");
                pdt.setPatientDateOfBirth(getXmlDate(birthdate));
                
                String address1 = rs.getString("person_address.address1"); 
                String state = rs.getString("person_address.state_province");
                
                String gender = rs.getString("person.gender");
                pdt.setPatientSexCode(gender);
                
                Boolean dead = rs.getBoolean("person.dead");
                pdt.setPatientDeceasedIndicator(dead);
                java.sql.Date deathdate = rs.getDate("person.death_date");
                if (dead != false){
                   pdt.setPatientDeceasedDate(getXmlDate(deathdate));
                }
                
                 String langCode = getPrimaryLanguageCode(patientId);
                 if(!"null".equals(langCode)){
                  pdt.setPatientPrimaryLanguageCode(langCode);}
         
                String educode = getEducationLevelCode(patientId);
                 if(!"null".equals(educode)){
                 pdt.setPatientEducationLevelCode(educode);}
       
         
                 String occupationCode = getOccupationLevelCode(patientId);
                  if(!"null".equals(occupationCode)){
                  pdt.setPatientOccupationCode(occupationCode);
                }
         
               String maritalCode = getMaritalStatusCode(patientId);
               if(!"null".equals(maritalCode)){
               pdt.setPatientMaritalStatusCode(maritalCode);}         
         
               pdt.setStateOfNigeriaOriginCode(state); 
        
                

               
                  //System.out.println("Names are:"+ firstname);
            
              
              }
        } catch (SQLException ex) {
            Logger.getLogger(PatientDemographicsDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pdt;
     }
    
    
    
   public static ArrayList<IdentifierType> getHospId(String patient_id)
   {
       ArrayList<IdentifierType> idst = new ArrayList<IdentifierType>();
            
        try{
             String sql = "SELECT DISTINCT patient_identifier.identifier\n" +
            "FROM person\n" +
            "INNER JOIN person_name\n" +
            "ON person.person_id = person_name.person_id\n" +
            "INNER JOIN patient_identifier\n" +
            "ON person.person_id= patient_identifier.patient_id\n" +
            "WHERE patient_identifier.identifier_type=3 AND patient_identifier.patient_id= ?";      
         
         preparedStmt = con.prepareStatement(sql);
         preparedStmt.setString(1,patient_id);
         rs = preparedStmt.executeQuery();
        
         while (rs.next()) {
            IdentifierType idt = new IdentifierType(); 
            String hospitalnumber = rs.getString("patient_identifier.identifier");
            String idType ="3";
            
            idt.setIDNumber(hospitalnumber);
            idt.setIDTypeCode(idType);
            
            idst.add(idt);
           
         }         
         rs.close();
         
        }catch(Exception e){} 
        
        return idst;
   }
   
    public static String getPrimaryLanguageCode(String patient_id)
        {
        String code="";
        int languageCode = 0;
        try{
            String sql = "SELECT value_coded FROM obs "
            + "where person_id=? and concept_id=1083 and value_coded IN (1080,1081,1082,1519)";
                        
         preparedStmt = con.prepareStatement(sql);
         preparedStmt.setString(1,patient_id);
            try{
                ResultSet rs = preparedStmt.executeQuery();
                while (rs.next()) {
                    languageCode = rs.getInt("value_coded");
                    System.out.println(languageCode);
                }  
          }catch(SQLException e){
          }catch(Exception e){
              
          }finally{
            
          } 
        if(languageCode==1080){
            code="ENG";
        }else if(languageCode==1081){code="HAUSA";}
         else if(languageCode==1082){code="YORUBA";}
         else if(languageCode==1519){code="IGBO";}
         else{code="null";}
        
         return code;
   }
       
       public static String getEducationLevelCode(String patient_id)
        {
        String code = "";
        int educationCode = 0;
        try{
                    String sql4 = "SELECT value_coded FROM obs "
                            + "where person_id=? and concept_id=1079 and value_coded IN (789,28,1078,1524,1525,518,1523)";

         preparedStmt = con.prepareStatement(sql4);
         preparedStmt.setString(1,patient_id);
            try (ResultSet rs4 = preparedStmt.executeQuery()) {
                while (rs4.next()) {
                    educationCode = rs4.getInt("value_coded");
                    System.out.println(educationCode);
                }  }
          }catch(Exception e){
          } 
        if(educationCode == 1524){
            code="1";
        }else if(educationCode== 1525){code="1";}
         else if(educationCode==518){code="2";}
         else if(educationCode==1523){code="3";}
          else if(educationCode==1078){code="4";}
         else if(educationCode==28){code="5";}
         else if(educationCode==789){code="6";}
         else{code="null";}
        
         return code;
   }
              
       
       public static String getOccupationLevelCode(String patient_id)
        {
        String code = "";
        int occupationCode = 0;
        try{
            String sql4 = "SELECT value_coded FROM obs "
            + "where person_id=? and concept_id=1077 and value_coded IN (789,906,1076,1520,1521)";
                        
         
         /**get hospital number **/
         /*preparedStmt = con.prepareStatement(sql4);
         preparedStmt.setString(1,patient_id);
            try (ResultSet rs= preparedStmt.executeQuery()) {
                while (rs.next()) {
                    occupationCode = rs.getInt("value_coded");
                    System.out.println(occupationCode);
                }  }
          }catch(Exception e){
          } 
        if(occupationCode==789){
            code="NA";
        }else if(occupationCode==906){code="UNE";}
         else if(occupationCode==1076){code="EMP";}
         else if(occupationCode==1520){code="STU";}
         else if(occupationCode==1521){code="RET";}
         else{code="NA";}
        
         return code;
   }
       
       public static String getMaritalStatusCode(String patient_id)
        {
        String code = "";
        int maritalStatusCode = 0;
        try{
                    String sql3 = "SELECT value_coded FROM obs "
                    + "where person_id=? and concept_id=1075 and value_coded IN (350,346,348,351,349,1522)";
                        
         
         /**get hospital number **/
         /*preparedStmt = con.prepareStatement(sql3);
         preparedStmt.setString(1,patient_id);
            try (ResultSet rs3 = preparedStmt.executeQuery()) {
                while (rs3.next()) {
                    maritalStatusCode = rs3.getInt("value_coded");
                    System.out.println(maritalStatusCode);
                }  }
          }catch(Exception e){
          } 
        if(maritalStatusCode==350){
            code="M";
        }else if(maritalStatusCode==346){code="C";}
         else if(maritalStatusCode==348){code="N";}
         else if(maritalStatusCode==351){code="D";}
        else if(maritalStatusCode==349){code="W";}
         else if(maritalStatusCode==1522){code="S";}
         else{code="null";}
        
         return code;
   }*/
       

    
}
