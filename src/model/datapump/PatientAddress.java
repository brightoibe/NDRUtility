/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.datapump;

/**
 *
 * @author brightoibe
 */
public class PatientAddress {
    private int patient_id;
    private String address1;
    private String address2;
    private String cityVillage;
    private String stateProvince;
    private String country;

    /**
     * @return the patient_id
     */
    public int getPatient_id() {
        return patient_id;
    }

    /**
     * @param patient_id the patient_id to set
     */
    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    /**
     * @return the address1
     */
    public String getAddress1() {
        return address1;
    }

    /**
     * @param address1 the address1 to set
     */
    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    /**
     * @return the address2
     */
    public String getAddress2() {
        return address2;
    }

    /**
     * @param address2 the address2 to set
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    /**
     * @return the cityVillage
     */
    public String getCityVillage() {
        return cityVillage;
    }

    /**
     * @param cityVillage the cityVillage to set
     */
    public void setCityVillage(String cityVillage) {
        this.cityVillage = cityVillage;
    }

    /**
     * @return the stateProvince
     */
    public String getStateProvince() {
        return stateProvince;
    }

    /**
     * @param stateProvince the stateProvince to set
     */
    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }
}
