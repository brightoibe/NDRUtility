/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.form;

import java.util.ArrayList;
import model.Obs;

/**
 *
 * @author openmrsdev
 */
public class Form {
    private String[] titles;
    private int[] conceptArr;
    private int[] formArr;
    private ArrayList<Obs> obsList;

    /**
     * @return the titles
     */
    public String[] getTitles() {
        return titles;
    }

    /**
     * @param titles the titles to set
     */
    public void setTitles(String[] titles) {
        this.titles = titles;
    }

    /**
     * @return the conceptArr
     */
    public int[] getConceptArr() {
        return conceptArr;
    }

    /**
     * @param conceptArr the conceptArr to set
     */
    public void setConceptArr(int[] conceptArr) {
        this.conceptArr = conceptArr;
    }

    /**
     * @return the formArr
     */
    public int[] getFormArr() {
        return formArr;
    }

    /**
     * @param formArr the formArr to set
     */
    public void setFormArr(int[] formArr) {
        this.formArr = formArr;
    }

    /**
     * @return the obsList
     */
    public ArrayList<Obs> getObsList() {
        return obsList;
    }

    /**
     * @param obsList the obsList to set
     */
    public void setObsList(ArrayList<Obs> obsList) {
        this.obsList = obsList;
    }
    
}
