
package com.inductivehealth.ndr.dao;

import static com.inductivehealth.ndr.client.Client.getXmlDateTime;
import com.inductivehealth.ndr.connection.ConnectionManager;
import com.inductivehealth.ndr.schema.FacilityType;
import com.inductivehealth.ndr.schema.MessageHeaderType;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;

/**
 *
 * @author THANKGOD
 */
public class MessageHeader {
    private static Connection con = null;
    private static Statement stmt = null;
    private static ResultSet rs = null;
    private static PreparedStatement preparedStmt;
    
    
    protected String messageStatusCode;   
    protected XMLGregorianCalendar messageCreationDateTime;   
    protected BigDecimal messageSchemaVersion;   
    protected String messageUniqueID;
    protected FacilityType messageSendingOrganization;

      
    public static MessageHeaderType publishMessageHeader()
    {
          
            MessageHeaderType mht = new MessageHeaderType();
  
       /*try{           
           con = ConnectionManager.getConnection();
           stmt = con.createStatement();
           String sql = "SELECT DISTINCT location.name,patient_identifier.location_id FROM patient_identifier \n" +
                        "INNER JOIN location\n" +
                        "ON patient_identifier.location_id = location.location_id\n" +
                        "WHERE patient_identifier.identifier_type=4 LIMIT 1";
           try (
               ResultSet rset = stmt.executeQuery(sql)) {                
               while(rset.next()){
                    
                    String facility_name = rset.getString("location.name");                   
                    String facility_id = rset.getString("patient_identifier.location");
                    
                    FacilityType ft = new FacilityType();
                    ft.setFacilityID(facility_id);
                    ft.setFacilityName(facility_name);
                    ft.setFacilityTypeCode("IP");
                 
                    //This will be controlled.
                    mht.setMessageStatusCode("INITIAL");                
                   
                    mht.setMessageCreationDateTime(getXmlDateTime(new Date()));
                    mht.setMessageSchemaVersion(new BigDecimal("1.2"));
                    mht.setMessageUniqueID("00000001");
                    mht.setMessageSendingOrganization(ft);

               } 
        }
                 
   }catch(SQLException | DatatypeConfigurationException se){
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
   }*/

   
   return mht;
}
    
}
