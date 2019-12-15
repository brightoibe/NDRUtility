/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author brightoibe
 */
public class PatientRegimen extends DrugOrder {
    private String regimenLine;
    private String regimenName;
    private String code;

    /**
     * @return the regimenLine
     */
    public String getRegimenLine() {
        return regimenLine;
    }

    /**
     * @param regimenLine the regimenLine to set
     */
    public void setRegimenLine(String regimenLine) {
        this.regimenLine = regimenLine;
    }

    /**
     * @return the regimenName
     */
    public String getRegimenName() {
        return regimenName;
    }

    /**
     * @param regimenName the regimenName to set
     */
    public void setRegimenName(String regimenName) {
        this.regimenName = regimenName;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }
}
