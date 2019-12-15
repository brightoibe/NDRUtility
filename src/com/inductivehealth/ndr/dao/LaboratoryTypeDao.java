/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inductivehealth.ndr.dao;

import static com.inductivehealth.ndr.client.Client.getXmlDate;
import com.inductivehealth.ndr.connection.ConnectionManager;
import com.inductivehealth.ndr.schema.LaboratoryOrderAndResult;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.inductivehealth.ndr.schema.LaboratoryReportType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author anazodo
 */
public class LaboratoryTypeDao {
    private static Connection con = null;
    private static Statement stmt = null;
    private static ResultSet rs = null;
    private static PreparedStatement preparedStmt;
    
    
    protected String visitID;
    protected XMLGregorianCalendar visitDate;
    protected String laboratoryTestIdentifier;
    protected XMLGregorianCalendar collectionDate;
    protected String baselineRepeatCode;
    protected String artStatusCode;
    protected List<LaboratoryOrderAndResult> laboratoryOrderAndResult;
    protected String clinician;
    protected String reportedBy;
    protected String checkedBy;
    
    public static void main(String[] args) throws DatatypeConfigurationException, SQLException
    {
        LaboratoryTypeDao.retrieveLaboratoryType("16486");
    }
    
    public static LaboratoryReportType retrieveLaboratoryType(String patientId)  throws DatatypeConfigurationException, SQLException
    {
       LaboratoryReportType lt = new LaboratoryReportType();
         try{        
           con = ConnectionManager.getConnection();
           stmt = con.createStatement();           
           String sql = "SELECT\n" +
                        "obs.obs_id,\n" +
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
                        "left join concept_name cn2 on(cn2.concept_id=obs.value_coded and cn2.locale_preferred=1 and cn2.locale='en') where encounter.form_id=21 and obs.person_id=?\n" +
                        "ORDER BY person_id,obs_datetime,obs.concept_id,obs_group_id";
           
              preparedStmt = con.prepareStatement(sql);
              preparedStmt.setString(1,patientId);
             
              ResultSet rs = preparedStmt.executeQuery(); 
             
               while(rs.next()){
                    
                        java.sql.Date visitDate = rs.getDate("obs.obs_datetime");
                        lt.setVisitDate(getXmlDate(visitDate));
                        String labTestIdentifier = rs.getString("obs.concept_id");
                        lt.setLaboratoryTestIdentifier(labTestIdentifier);
                        String visitId = rs.getString("obs.obs_id");
                        lt.setVisitID(visitId);
                        
                        //System.out.println(visitDate);
                        
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
return lt;
}
    
}
