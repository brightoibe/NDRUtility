
package com.inductivehealth.ndr.dao;
import com.inductivehealth.ndr.schema.ProgramAreaType;

/**
 *
 * @author THANKGOD
 */
public class ProgramAreaDao {
    
       protected String programAreaCode;
       
       public static ProgramAreaType publishProgramAreaCode()
       {
           ProgramAreaType pa = new ProgramAreaType();
           //This value can also be fetched from a database, 
           //Considering other program areas.
           pa.setProgramAreaCode("HIV");
           return pa;
       }
   
    
}
