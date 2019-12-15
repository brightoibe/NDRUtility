/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.datapump;

/**
 *
 * @author brightoibe
 */
public class PersonName {
    private int person_id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String fullName;
    
    public PersonName(int person_id,String firstName,String lastName,String middleName){
        this.person_id=person_id;
        this.firstName=firstName;
        this.lastName=lastName;
        this.middleName=middleName;
        this.fullName=firstName +" "+lastName;
    }
    public PersonName(){
        
    }
    /**
     * @return the person_id
     */
    public int getPerson_id() {
        return person_id;
    }

    /**
     * @param person_id the person_id to set
     */
    public void setPerson_id(int person_id) {
        this.person_id = person_id;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the middleName
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * @param middleName the middleName to set
     */
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    /**
     * @return the fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
