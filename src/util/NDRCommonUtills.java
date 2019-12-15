/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import model.datapump.Obs;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

/**
 *
 * @author The Bright
 */
public class NDRCommonUtills {
     public static model.datapump.Obs getConceptForForm(int conceptID, int obsGroupID, int formID, ArrayList<model.datapump.Obs> obsList) {
        model.datapump.Obs obs = null;
        for (model.datapump.Obs ele : obsList) {
            if (ele.getConceptID() == conceptID && ele.getObsGroupID() == obsGroupID) {
                obs = ele;
            }
        }
        return obs;
    }
     
    public static Set<Date> getAllVisitsFromObsList(List<model.datapump.Obs> obsList) {
        Set<Date> visitDateSet = new HashSet<Date>();
        Date visitDate = null;
        for (model.datapump.Obs ele : obsList) {
            visitDate = ele.getVisitDate();
            visitDateSet.add(visitDate);
        }
        return visitDateSet;
    }
    public static model.datapump.Obs getObsForConceptForForm(int conceptID, ArrayList<model.datapump.Obs> obsList, Date visitDate) {
        model.datapump.Obs obs = null;
        for (model.datapump.Obs ele : obsList) {
            if (ele.getConceptID() == conceptID && ele.getVisitDate().equals(visitDate)) {
                obs = ele;
            }
        }
        return obs;
    }
    public static model.datapump.Obs getObsForConceptFromList(int conceptID, List<model.datapump.Obs> obsList) {
        model.datapump.Obs obs = null;
        for (model.datapump.Obs ele : obsList) {
            if (ele.getConceptID() == conceptID) {
                obs = ele;
            }
        }
        return obs;
    }
    public static Obs extractConcept(int conceptID, List<Obs> obsList) {
        Obs obs = null;
        for (Obs ele : obsList) {
            if (ele.getConceptID() == conceptID) {
                obs = ele;
            }
        }
        return obs;
    }
     public static model.datapump.Obs getObsForConceptFromListWithGroupID(int conceptID, List<model.datapump.Obs> obsList,int obsGroupID) {
        model.datapump.Obs obs = null;
        for (model.datapump.Obs ele : obsList) {
            if (ele.getConceptID() == conceptID && ele.getObsGroupID()==obsGroupID) {
                obs = ele;
            }
        }
        return obs;
    }
    
      public static List<model.datapump.Obs> getAllObsForConceptFromListWithGroupID(List<model.datapump.Obs> obsList,int obsGroupID) {
        List<model.datapump.Obs> obsInGroupList = new ArrayList<>();
        for (model.datapump.Obs ele : obsList) {
            if (ele.getObsGroupID()==obsGroupID) {
                obsInGroupList.add(ele);
            }
        }
        return obsInGroupList;
    }
     public  static Date calculateStopDate(Date startDate, int duration, String unit) {
        Date stopDate = null;
        int dayVal = 30;
        if (StringUtils.isNotBlank(unit)) {
            if (StringUtils.equalsIgnoreCase(unit, "MONTH(S)")) {
                dayVal = duration * 30;
            } else if (StringUtils.equalsIgnoreCase(unit, "DAY(S)")) {
                dayVal = duration;
            } else if (StringUtils.equalsIgnoreCase(unit, "WEEK(S)")) {
                dayVal = duration * 7;
            }
            
            //if (dayVal > 120) {
                //dayVal = 30;
            //}
        }
        DateTime startDateTime = new DateTime(startDate);
        DateTime stopDateTime = startDateTime.plusDays(dayVal);
        stopDate = stopDateTime.toDate();
        return stopDate;
    }
     public static int calculateDayValue(int duration, String unit) {
        int dayVal = 0;
        if (StringUtils.isNotBlank(unit)) {
            if (StringUtils.equalsIgnoreCase(unit, "MONTH(S)")) {
                dayVal = duration * 30;
            } else if (StringUtils.equalsIgnoreCase(unit, "DAY(S)")) {
                dayVal = duration;
            } else if (StringUtils.equalsIgnoreCase(unit, "WEEK(S)")) {
                dayVal = duration * 7;
            }
        }
        return dayVal;
    }
     public static model.datapump.Obs getConceptForVisit(int conceptID, Date visitDate, ArrayList<model.datapump.Obs> obsList) {
        model.datapump.Obs obs = null;
        for (model.datapump.Obs ele : obsList) {
            if (ele.getConceptID() == conceptID && ele.getVisitDate().equals(visitDate)) {
                obs = ele;
            }
        }
        return obs;
    }
     public static List<model.datapump.Obs> getAllObsWithConceptForList(int conceptID, Date visitDate,List<model.datapump.Obs> obsList) {
        List<model.datapump.Obs> obsConceptList = new ArrayList<model.datapump.Obs>();
        for (model.datapump.Obs ele : obsList) {
            if (ele.getConceptID() == conceptID && ele.getVisitDate().equals(visitDate)) {
                obsConceptList.add(ele);
            }
        }
        return obsConceptList;
    }
      public static List<model.datapump.Obs> getAllObsForDate(Date visitDate, List<model.datapump.Obs> obsList) {
        List<model.datapump.Obs> obsContainerList = new ArrayList<model.datapump.Obs>();
        for (model.datapump.Obs ele : obsList) {
            if (ele.getVisitDate().equals(visitDate)) {
                obsContainerList.add(ele);
                
            }
        }
        return obsContainerList;
    }
      public static String formatDateYYYYMMDD(Date date) {
        String dateString = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            dateString = df.format(date);
        }
        return dateString;

    }
    
     
    
}
