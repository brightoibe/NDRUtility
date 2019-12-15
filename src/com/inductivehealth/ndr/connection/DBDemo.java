
package com.inductivehealth.ndr.connection;

import java.sql.*;

public class DBDemo
{
    private static Connection con = null;
    private static Statement stmt = null;
    private static ResultSet rs = null;

  public static void main(String[] args) throws
                             ClassNotFoundException,SQLException
  {
    con = ConnectionManager.getConnection();
    stmt = con.createStatement();
    String sql = "create or replace view all_visit as\n" +
"select obs.person_id, obs.obs_datetime as visit_date from obs where obs.voided=0\n" +
"union\n" +
"select orders.patient_id, orders.start_date from orders where orders.voided=0";
    stmt.executeUpdate(sql);
    
    //while(rs.next())
    //{
         System.out.println("It works !");
    //}

  }
}
