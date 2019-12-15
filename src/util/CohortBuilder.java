/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import model.Demographics;
import org.joda.time.LocalDate;
import org.joda.time.Years;

/**
 *
 * @author bright
 */
public class CohortBuilder {

    //private static Map<String,Set<Integer>> dimMap;
    private static Set<Integer> ageLess1, age1To4, age5To9, age10To14, age15To19, age20To24, age25To49, ageGreater50;
    private static Set<Integer> maleSet, femaleSet;

    public CohortBuilder() {
        //dimMap=new HashMap<String,Set<Integer>>();
        ageLess1 = new HashSet<Integer>();
        age1To4 = new HashSet<Integer>();
        age5To9 = new HashSet<Integer>();
        age10To14 = new HashSet<Integer>();
        age15To19 = new HashSet<Integer>();
        age20To24 = new HashSet<Integer>();
        age25To49 = new HashSet<Integer>();
        ageGreater50 = new HashSet<Integer>();
        maleSet = new HashSet<Integer>();
        femaleSet = new HashSet<Integer>();

    }
    public static void init(){
         ageLess1 = new HashSet<Integer>();
        age1To4 = new HashSet<Integer>();
        age5To9 = new HashSet<Integer>();
        age10To14 = new HashSet<Integer>();
        age15To19 = new HashSet<Integer>();
        age20To24 = new HashSet<Integer>();
        age25To49 = new HashSet<Integer>();
        ageGreater50 = new HashSet<Integer>();
        maleSet = new HashSet<Integer>();
        femaleSet = new HashSet<Integer>();
    }

    public static int getAge(Date dob, Date refDate) {
        int age = 0;
        LocalDate birthDate = new LocalDate(dob.getTime());
        LocalDate lastDate = new LocalDate(refDate.getTime());
        Years ageYrs = Years.yearsBetween(birthDate, lastDate);
        return ageYrs.getYears();
    }
    public static void main(String[] arg){
        Date dob=new GregorianCalendar(1985, 8, 15).getTime();
        Date now=new Date();
        System.out.println(getAge(dob,now));
        
    }
    public static void loadMap(ArrayList<model.datapump.Demographics> ptsList, HashMap<Integer, Date> lasVisitMap) {
        int age = 0;
        int patientID = 0;
        String gender = "";
        Date lastDate = null;
        Date dob = null;
        for (model.datapump.Demographics ele : ptsList) {
            if (!ele.getPepfarID().isEmpty() && ele.getDateOfBirth() != null && ele.getGender() != null) {
                patientID = ele.getPatientID();
                gender = ele.getGender();
                dob=ele.getDateOfBirth();
                lastDate = lasVisitMap.get(patientID);
                if (lastDate == null) {
                    lastDate = new Date();
                }
                age = getAge(dob, lastDate);
                //age = ele.getAge();

                if (ele.getAge() < 1) {
                    ageLess1.add(patientID);
                }
                if (age >= 1 && age <= 4) {
                    age1To4.add(patientID);
                }
                if (age >= 5 && age <= 9) {
                    age5To9.add(patientID);
                }
                if (age >= 10 && age <= 14) {
                    age10To14.add(patientID);
                }
                if (age >= 15 && age <= 19) {
                    age15To19.add(patientID);
                }
                if (age >= 20 && age <= 24) {
                    age20To24.add(patientID);
                }
                if (age >= 25 && age <= 49) {
                    age25To49.add(patientID);
                }
                if (age >= 50) {
                    ageGreater50.add(patientID);
                }
                if (gender.equalsIgnoreCase("M")) {
                    maleSet.add(patientID);
                }
                if (gender.equalsIgnoreCase("F")) {
                    femaleSet.add(patientID);
                }
            }

        }
    }

    public static Set<Integer> Intersect(Set<Integer> c1, Set<Integer> c2) {
        Set<Integer> ansSet = new HashSet<Integer>(c1);
        ansSet.retainAll(c2);
        return ansSet;
    }

    public static Set<Integer> Minus(Set<Integer> c1, Set<Integer> c2) {
        Set<Integer> ansSet = new HashSet<Integer>(c1);
        ansSet.removeAll(c2);
        return ansSet;
    }

    public static Set<Integer> Union(Set<Integer> c1, Set<Integer> c2) {
        Set<Integer> ansSet = new HashSet<Integer>(c1);
        ansSet.addAll(c2);
        return ansSet;
    }

    public static Set<Integer> getDim(int n, Set<Integer> cohortSet) {
        Set<Integer> ansSet = new HashSet<Integer>();
        switch (n) {
            case 1:
                ansSet = Intersect(ageLess1, Intersect(cohortSet, maleSet));
                break;
            case 2:
                ansSet = Intersect(age1To4, Intersect(cohortSet, maleSet));
                break;
            case 3:
                ansSet = Intersect(age5To9, Intersect(cohortSet, maleSet));
                break;
            case 4:
                ansSet = Intersect(age10To14, Intersect(cohortSet, maleSet));
                break;
            case 5:
                ansSet = Intersect(age15To19, Intersect(cohortSet, maleSet));
                break;
            case 6:
                ansSet = Intersect(age20To24, Intersect(cohortSet, maleSet));
                break;
            case 7:
                ansSet = Intersect(age25To49, Intersect(cohortSet, maleSet));
                break;
            case 8:
                ansSet = Intersect(ageGreater50, Intersect(cohortSet, maleSet));
                break;
            case 9:
                ansSet = Intersect(ageLess1, Intersect(cohortSet, femaleSet));
                break;
            case 10:
                ansSet = Intersect(age1To4, Intersect(cohortSet, femaleSet));
                break;
            case 11:
                ansSet = Intersect(age5To9, Intersect(cohortSet, femaleSet));
                break;
            case 12:
                ansSet = Intersect(age10To14, Intersect(cohortSet, femaleSet));
                break;
            case 13:
                ansSet = Intersect(age15To19, Intersect(cohortSet, femaleSet));
                break;
            case 14:
                ansSet = Intersect(age20To24, Intersect(cohortSet, femaleSet));
                break;
            case 15:
                ansSet = Intersect(age25To49, Intersect(cohortSet, femaleSet));
                break;
            case 16:
                ansSet = Intersect(ageGreater50, Intersect(cohortSet, femaleSet));
                break;
            case 17:
                ansSet = Intersect(ageLess1, cohortSet);
                break;
            case 18:
                ansSet = Intersect(age1To4, cohortSet);
                break;
            case 19:
                ansSet = Intersect(age5To9, cohortSet);
                break;
            case 20:
                ansSet = Intersect(age10To14, cohortSet);
                break;
            case 21:
                ansSet = Intersect(age15To19, cohortSet);
                break;
            case 22:
                ansSet = Intersect(age20To24, cohortSet);
                break;
            case 23:
                ansSet = Intersect(age25To49, cohortSet);
                break;
            case 24:
                ansSet = Intersect(ageGreater50, cohortSet);
                break;
            case 25:
                ansSet = Intersect(maleSet, cohortSet);
                break;
            case 26:
                ansSet = Intersect(femaleSet, cohortSet);
                break;
            case 27:
                ansSet=cohortSet;
            default:
                break;
        }

        return ansSet;
    }

}
