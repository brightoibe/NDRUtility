/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.datapump;

import java.util.Date;

/**
 *
 * @author brightoibe
 */
public class Person {
    private int person_id;
    private String gender;
    private Date birthDate;
    private String uuid;
    
    

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the birthDate
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * @param birthDate the birthDate to set
     */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
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
     * @return the uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @param uuid the uuid to set
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
