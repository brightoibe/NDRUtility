package com.inductivehealth.ndr.dao;

/**
 *
 * @author THANKGOD
 */


import com.inductivehealth.ndr.connection.ConnectionManager;
import com.inductivehealth.ndr.schema.CodedSimpleType;
import com.inductivehealth.ndr.schema.RegimenType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * @author anazodo
 */
public class RegimenDao {
   
   private static Connection con = null;
    private static Statement stmt = null;
    private static ResultSet rs = null;
    private static PreparedStatement preparedStmt; 
      
   /*public static ArrayList<RegimenType> getRegimenType(String patientId)
   {
       
       ArrayList<RegimenType> reg = new ArrayList<RegimenType>();
        
       try{
        con = ConnectionManager.getConnection();
        stmt = con.createStatement();
           
       String sql = "select\n" +
                     "orders.order_id,\n" +
                     "orders.patient_id,\n" +
                     "patient_identifier.identifier as PEPFAR_ID,\n" +
                     "drug.`name` DRUG_NAME,\n" +
                     "orders.start_date  DISPENSED_DATE,\n" +
                     "DAY(orders.start_date)  DISPENSED_DAY,\n" +
                     "MONTH(orders.start_date)  DISPENSED_MONTH,\n" +
                     "YEAR(orders.start_date)  DISPENSED_YEAR,\n" +
                     "orders.discontinued_date STOP_DATE,\n" +
                     "DAY(orders.discontinued_date) STOPDATE_DAY,\n" +
                     "MONTH(orders.discontinued_date) STOPDATE_MONTH,\n" +
                     "YEAR(orders.discontinued_date) STOPDATE_YEAR,\n" +
                     "orders.discontinued_reason DISCONTINUED_REASON,\n" +
                     "drug_order.dose,\n" +
                     "drug_order.units,\n" +
                     "drug_order.frequency AS FREQUENCY,\n" +
                     "drug_order.quantity\n" +
                     "from\n" +
                     "orders\n" +
                     "inner join drug_order on(drug_order.order_id=orders.order_id and orders.voided=0)\n" +
                     "inner join drug on(drug.drug_id=drug_order.drug_inventory_id)\n" +
                     "inner join patient_identifier on(patient_identifier.patient_id=orders.patient_id and patient_identifier.identifier_type=4 and orders.voided=0 and patient_identifier.voided=0)\n" +
                     "WHERE orders.patient_id=? ORDER BY DISPENSED_DATE";
       
       preparedStmt = con.prepareStatement(sql);
              preparedStmt.setString(1,patientId);             
              rs = preparedStmt.executeQuery();
                   /* try (ResultSet rs = preparedStmt.executeQuery()) {
                
                    while(rs.next()){
                    RegimenType regimen = new RegimenType();
                    String visitID= rs.getString("orders.order_id");                   
                    String discontinued_reason = discontinuedReason(rs.getInt("DISCONTINUED_REASON"));
                    String regName = rs.getString("DRUG_NAME");
                    String regTypeCode = regimenTypeCode(regName);
                    //System.out.println(regTypeCode);
                    
                    String regLine = regimenLine(regName);
                    String regFrequency = rs.getString("FREQUENCY");
                    java.util.Date regimenStartDate = rs.getDate("DISPENSED_DATE");
                    String regimenstarted_day = rs.getString("DISPENSED_DAY");
                    String regimenstarted_month = rs.getString("DISPENSED_MONTH");
                    String regimenstarted_year = rs.getString("DISPENSED_YEAR");
                    java.util.Date regimenEnded = rs.getDate("STOP_DATE");
                    String regimenended_day = rs.getString("STOPDATE_DAY");
                    String regimenended_month = rs.getString("STOPDATE_MONTH");
                    String regimenended_year = rs.getString("STOPDATE_YEAR");
                    
                    
                    regimen.setVisitID(visitID);
                    regimen.setVisitDate(getXmlDate(regimenStartDate));
                    if(!"".equals(discontinued_reason)){
                     regimen.setReasonForRegimenSwitchSubs(discontinued_reason);
                     }
                    CodedSimpleType ct = new CodedSimpleType();
                    ct.setCode("1");
                    ct.setCodeDescTxt(regName );                    
                                    
                    regimen.setPrescribedRegimen(ct);
                  
                    regimen.setPrescribedRegimenTypeCode(regTypeCode);
                  
                    if(!"".equals(regLine)){
                    regimen.setPrescribedRegimenLineCode(regLine);
                    }
                    regimen.setPrescribedRegimenDuration(regFrequency);
                    
                    regimen.setPrescribedRegimenDispensedDate(getXmlDateTime(regimenStartDate ));
                    regimen.setDateRegimenStarted(getXmlDateTime(regimenStartDate ));
                    regimen.setDateRegimenStartedDD(regimenstarted_day);
                    regimen.setDateRegimenStartedMM(regimenstarted_month);
                    regimen.setDateRegimenStartedYYYY(regimenstarted_year);
                    if(regimenEnded != null){
                    regimen.setDateRegimenEnded(getXmlDateTime(regimenEnded));
                    regimen.setDateRegimenEndedDD(regimenended_day);
                    regimen.setDateRegimenEndedMM(regimenended_month);
                    regimen.setDateRegimenEndedYYYY(regimenended_year);
                    }
                    
                    //The main koko happens here
                     reg.add(regimen);                     
                    
                    /*Testing Testing microphone here
                    System.out.println(visitID);
                     System.out.println(visitID);
                    System.out.println(discontinued_reason);
                    System.out.println(regName );
                    System.out.println(regLine);
                    System.out.println(regFrequency);
                    System.out.println(regimenStartDate );
                    System.out.println(regimenstarted_day); 
                    System.out.println(regimenstarted_month); 
                    System.out.println(regimenstarted_year); 
                    System.out.println(regimenEnded); 
                    System.out.println(regimenended_day); 
                    System.out.println(regimenended_month); 
                    System.out.println(regimenended_year); 
                    System.out.println(regTypeCode);
                    
                }  
                
            }
                 
   }catch(Exception se){
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
      
   //System.out.println(reg.size());
    //System.out.println(reg);
   
   return reg;
  // System.out.println("Goodbye!");
    
}
   //Utility methods to format Date to XMLGregorianCalendar
	public static XMLGregorianCalendar getXmlDate(java.util.Date date) throws DatatypeConfigurationException {
	    return DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat("yyyy-MM-dd").format(date));
	}
	
	public static XMLGregorianCalendar getXmlDateTime(java.util.Date date) throws DatatypeConfigurationException {
	    return DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(date));
	}
        
        public static String regimenLine(String line)
        {
                    
             switch (line) {
             case "Zidovudine/Lamivudine/Nevirapine (300/150/200mg)":  
                line = "First Line Regimen";
                     break;
             default: line = "";
                     break;
        }     
            return line;
        }
        
         public static String regimenTypeCode(String line)
        {
            //String regLine="";
            
             switch (line) {
             case "Zidovudine/Lamivudine/Nevirapine (300/150/200mg)":  
                line = "AZT/3TC/NVP";
                     break;
             default: line = "";
                     break;
             }     
            return line;
        }
        
        public void checkRegimenSwitch()
        {
        
        }
        
          public static String discontinuedReason(int conceptId)
        {
            String discontinued_reason="";
           try{
        con = ConnectionManager.getConnection();
        stmt = con.createStatement();
              
       String sql1 = "Select name from concept_name where concept_id = ?";    
      
                    preparedStmt = con.prepareStatement(sql1);
                    preparedStmt.setInt(1,conceptId);
                    try (ResultSet rs = preparedStmt.executeQuery()) {
                
                    while(rs.next()){                                     
                    discontinued_reason = rs.getString("name");                   
                }  
                
            }
                 
   }catch(Exception se){
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
           return discontinued_reason ;
  }*/

}
