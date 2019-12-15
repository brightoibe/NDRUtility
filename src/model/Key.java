/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author The Bright
 */
public class Key {

    /**
     * @return the formID
     */
    public int getFormID() {
        return formID;
    }

    /**
     * @param formID the formID to set
     */
    public void setFormID(int formID) {
        this.formID = formID;
    }
    public Key(int conceptID, int valueCoded){
        this.conceptID=conceptID;
        this.valueCoded=valueCoded;
    }
    /**
     * @return the conceptID
     */
    public int getConceptID() {
        return conceptID;
    }

    /**
     * @param conceptID the conceptID to set
     */
    public void setConceptID(int conceptID) {
        this.conceptID = conceptID;
    }

    /**
     * @return the valueCoded
     */
    public int getValueCoded() {
        return valueCoded;
    }

    /**
     * @param valueCoded the valueCoded to set
     */
    public void setValueCoded(int valueCoded) {
        this.valueCoded = valueCoded;
    }
    private int conceptID;
    private int valueCoded;
    private int formID;
    
    @Override
    public int hashCode() {
        return this.conceptID ^ this.valueCoded ^ this.getFormID();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Key other = (Key) obj;
        if (this.conceptID != other.conceptID) {
            return false;
        }
        if (this.valueCoded != other.valueCoded) {
            return false;
        }
        if (this.formID != other.formID) {
            return false;
        }
        return true;
    }

   
    
    
}
