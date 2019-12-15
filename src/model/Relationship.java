/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;

/**
 *
 * @author bright
 */
public class Relationship {
    private int relationshipID;
    private String personAName;
    private String personBName;
    private String aIsToB;
    private String bIsToA;
    private String enteredBy;
    private Date dateEntered;
    private int personAID;
    private int personBID;
    private int relationshipTypeID;
    private int creatorID;
    private int voided;
    private int voidedBy;
    private Date dateVoided;
    private String uuid;

    /**
     * @return the relationshipID
     */
    public int getRelationshipID() {
        return relationshipID;
    }

    /**
     * @param relationshipID the relationshipID to set
     */
    public void setRelationshipID(int relationshipID) {
        this.relationshipID = relationshipID;
    }

    /**
     * @return the personAName
     */
    public String getPersonAName() {
        return personAName;
    }

    /**
     * @param personAName the personAName to set
     */
    public void setPersonAName(String personAName) {
        this.personAName = personAName;
    }

    /**
     * @return the personBName
     */
    public String getPersonBName() {
        return personBName;
    }

    /**
     * @param personBName the personBName to set
     */
    public void setPersonBName(String personBName) {
        this.personBName = personBName;
    }

    /**
     * @return the aIsToB
     */
    public String getaIsToB() {
        return aIsToB;
    }

    /**
     * @param aIsToB the aIsToB to set
     */
    public void setaIsToB(String aIsToB) {
        this.aIsToB = aIsToB;
    }

    /**
     * @return the bIsToA
     */
    public String getbIsToA() {
        return bIsToA;
    }

    /**
     * @param bIsToA the bIsToA to set
     */
    public void setbIsToA(String bIsToA) {
        this.bIsToA = bIsToA;
    }

    /**
     * @return the enteredBy
     */
    public String getEnteredBy() {
        return enteredBy;
    }

    /**
     * @param enteredBy the enteredBy to set
     */
    public void setEnteredBy(String enteredBy) {
        this.enteredBy = enteredBy;
    }

    /**
     * @return the dateEntered
     */
    public Date getDateEntered() {
        return dateEntered;
    }

    /**
     * @param dateEntered the dateEntered to set
     */
    public void setDateEntered(Date dateEntered) {
        this.dateEntered = dateEntered;
    }

    /**
     * @return the personAID
     */
    public int getPersonAID() {
        return personAID;
    }

    /**
     * @param personAID the personAID to set
     */
    public void setPersonAID(int personAID) {
        this.personAID = personAID;
    }

    /**
     * @return the personBID
     */
    public int getPersonBID() {
        return personBID;
    }

    /**
     * @param personBID the personBID to set
     */
    public void setPersonBID(int personBID) {
        this.personBID = personBID;
    }

    /**
     * @return the relationshipTypeID
     */
    public int getRelationshipTypeID() {
        return relationshipTypeID;
    }

    /**
     * @param relationshipTypeID the relationshipTypeID to set
     */
    public void setRelationshipTypeID(int relationshipTypeID) {
        this.relationshipTypeID = relationshipTypeID;
    }

    /**
     * @return the creatorID
     */
    public int getCreatorID() {
        return creatorID;
    }

    /**
     * @param creatorID the creatorID to set
     */
    public void setCreatorID(int creatorID) {
        this.creatorID = creatorID;
    }

    /**
     * @return the voided
     */
    public int getVoided() {
        return voided;
    }

    /**
     * @param voided the voided to set
     */
    public void setVoided(int voided) {
        this.voided = voided;
    }

    /**
     * @return the voidedBy
     */
    public int getVoidedBy() {
        return voidedBy;
    }

    /**
     * @param voidedBy the voidedBy to set
     */
    public void setVoidedBy(int voidedBy) {
        this.voidedBy = voidedBy;
    }

    /**
     * @return the dateVoided
     */
    public Date getDateVoided() {
        return dateVoided;
    }

    /**
     * @param dateVoided the dateVoided to set
     */
    public void setDateVoided(Date dateVoided) {
        this.dateVoided = dateVoided;
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
