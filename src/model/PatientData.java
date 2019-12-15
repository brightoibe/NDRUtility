/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;

/**
 *
 * @author brightoibe
 */
public class PatientData {
    private String pepfarID;
    private String hospID;
    private String gender;
    private Date dateOfBirth;
    private int age;
    private String firstName;
    private String middleName;
    private String lastName;
    private String patientPhone;
    private String address;
    private String regimen;
    private String crntARTStatus;
    private Date encounterDate;
    private String encounterType;
    private String formType;
    private Date artInitiationDate;
    private Date firstVisitDate;
    private String programName;
    private Date enrollmentDate;
    private Date lastClinicVisitDate;
    private String lvOutCome;
    private double cd4Count;
    private Date cd4TestDate;
    private double viralLoad;
    private Date viralLoadObsDate;
    private String tbScreenStatus;
    private Date tbScreenDate;
    private double weight;
    private Date weightObsDate;
    private double height;
    private double muac;
    private Date artPickupDate;
    private Date cotrimStartDate;
    private String servicesReceived;
    private Date receivedCotriDate;
    private String receivedCotri;
    private String location;
    private String enteredBy;
    private String provider;
    
    
    
    public PatientData(){
        
    }

    /**
     * @return the pepfarID
     */
    public String getPepfarID() {
        return pepfarID;
    }

    /**
     * @param pepfarID the pepfarID to set
     */
    public void setPepfarID(String pepfarID) {
        this.pepfarID = pepfarID;
    }

    /**
     * @return the hospID
     */
    public String getHospID() {
        return hospID;
    }

    /**
     * @param hospID the hospID to set
     */
    public void setHospID(String hospID) {
        this.hospID = hospID;
    }

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
     * @return the dateOfBirth
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * @param dateOfBirth the dateOfBirth to set
     */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(int age) {
        this.age = age;
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
     * @return the patientPhone
     */
    public String getPatientPhone() {
        return patientPhone;
    }

    /**
     * @param patientPhone the patientPhone to set
     */
    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the regimen
     */
    public String getRegimen() {
        return regimen;
    }

    /**
     * @param regimen the regimen to set
     */
    public void setRegimen(String regimen) {
        this.regimen = regimen;
    }

    /**
     * @return the crntARTStatus
     */
    public String getCrntARTStatus() {
        return crntARTStatus;
    }

    /**
     * @param crntARTStatus the crntARTStatus to set
     */
    public void setCrntARTStatus(String crntARTStatus) {
        this.crntARTStatus = crntARTStatus;
    }

    /**
     * @return the artInitiationDate
     */
    public Date getArtInitiationDate() {
        return artInitiationDate;
    }

    /**
     * @param artInitiationDate the artInitiationDate to set
     */
    public void setArtInitiationDate(Date artInitiationDate) {
        this.artInitiationDate = artInitiationDate;
    }

    /**
     * @return the firstVisitDate
     */
    public Date getFirstVisitDate() {
        return firstVisitDate;
    }

    /**
     * @param firstVisitDate the firstVisitDate to set
     */
    public void setFirstVisitDate(Date firstVisitDate) {
        this.firstVisitDate = firstVisitDate;
    }

    /**
     * @return the lastClinicVisitDate
     */
    public Date getLastClinicVisitDate() {
        return lastClinicVisitDate;
    }

    /**
     * @param lastClinicVisitDate the lastClinicVisitDate to set
     */
    public void setLastClinicVisitDate(Date lastClinicVisitDate) {
        this.lastClinicVisitDate = lastClinicVisitDate;
    }

    /**
     * @return the lvOutCome
     */
    public String getLvOutCome() {
        return lvOutCome;
    }

    /**
     * @param lvOutCome the lvOutCome to set
     */
    public void setLvOutCome(String lvOutCome) {
        this.lvOutCome = lvOutCome;
    }

    /**
     * @return the cd4Count
     */
    public double getCd4Count() {
        return cd4Count;
    }

    /**
     * @param cd4Count the cd4Count to set
     */
    public void setCd4Count(double cd4Count) {
        this.cd4Count = cd4Count;
    }

    /**
     * @return the cd4TestDate
     */
    public Date getCd4TestDate() {
        return cd4TestDate;
    }

    /**
     * @param cd4TestDate the cd4TestDate to set
     */
    public void setCd4TestDate(Date cd4TestDate) {
        this.cd4TestDate = cd4TestDate;
    }

    /**
     * @return the viralLoad
     */
    public double getViralLoad() {
        return viralLoad;
    }

    /**
     * @param viralLoad the viralLoad to set
     */
    public void setViralLoad(double viralLoad) {
        this.viralLoad = viralLoad;
    }

    /**
     * @return the viralLoadObsDate
     */
    public Date getViralLoadObsDate() {
        return viralLoadObsDate;
    }

    /**
     * @param viralLoadObsDate the viralLoadObsDate to set
     */
    public void setViralLoadObsDate(Date viralLoadObsDate) {
        this.viralLoadObsDate = viralLoadObsDate;
    }

    /**
     * @return the tbScreenStatus
     */
    public String getTbScreenStatus() {
        return tbScreenStatus;
    }

    /**
     * @param tbScreenStatus the tbScreenStatus to set
     */
    public void setTbScreenStatus(String tbScreenStatus) {
        this.tbScreenStatus = tbScreenStatus;
    }

    /**
     * @return the tbScreenDate
     */
    public Date getTbScreenDate() {
        return tbScreenDate;
    }

    /**
     * @param tbScreenDate the tbScreenDate to set
     */
    public void setTbScreenDate(Date tbScreenDate) {
        this.tbScreenDate = tbScreenDate;
    }

    /**
     * @return the weight
     */
    public double getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * @return the weightObsDate
     */
    public Date getWeightObsDate() {
        return weightObsDate;
    }

    /**
     * @param weightObsDate the weightObsDate to set
     */
    public void setWeightObsDate(Date weightObsDate) {
        this.weightObsDate = weightObsDate;
    }

    /**
     * @return the height
     */
    public double getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * @return the muac
     */
    public double getMuac() {
        return muac;
    }

    /**
     * @param muac the muac to set
     */
    public void setMuac(double muac) {
        this.muac = muac;
    }

    /**
     * @return the artPickupDate
     */
    public Date getArtPickupDate() {
        return artPickupDate;
    }

    /**
     * @param artPickupDate the artPickupDate to set
     */
    public void setArtPickupDate(Date artPickupDate) {
        this.artPickupDate = artPickupDate;
    }

    /**
     * @return the servicesReceived
     */
    public String getServicesReceived() {
        return servicesReceived;
    }

    /**
     * @param servicesReceived the servicesReceived to set
     */
    public void setServicesReceived(String servicesReceived) {
        this.servicesReceived = servicesReceived;
    }

    /**
     * @return the receivedCotriDate
     */
    public Date getReceivedCotriDate() {
        return receivedCotriDate;
    }

    /**
     * @param receivedCotriDate the receivedCotriDate to set
     */
    public void setReceivedCotriDate(Date receivedCotriDate) {
        this.receivedCotriDate = receivedCotriDate;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
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
    
    @Override
    public String toString(){
        String str=this.getPepfarID()+","+this.getHospID()+","+this.getRegimen()+
                this.getCrntARTStatus()+","+this.getLocation()+","+this.getServicesReceived();
        return str;
    }

    /**
     * @return the encounterDate
     */
    public Date getEncounterDate() {
        return encounterDate;
    }

    /**
     * @param encounterDate the encounterDate to set
     */
    public void setEncounterDate(Date encounterDate) {
        this.encounterDate = encounterDate;
    }

    /**
     * @return the encounterType
     */
    public String getEncounterType() {
        return encounterType;
    }

    /**
     * @param encounterType the encounterType to set
     */
    public void setEncounterType(String encounterType) {
        this.encounterType = encounterType;
    }

    /**
     * @return the receivedCotri
     */
    public String getReceivedCotri() {
        return receivedCotri;
    }

    /**
     * @param receivedCotri the receivedCotri to set
     */
    public void setReceivedCotri(String receivedCotri) {
        this.receivedCotri = receivedCotri;
    }

    /**
     * @return the enrollmentDate
     */
    public Date getEnrollmentDate() {
        return enrollmentDate;
    }

    /**
     * @param enrollmentDate the enrollmentDate to set
     */
    public void setEnrollmentDate(Date enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    /**
     * @return the formType
     */
    public String getFormType() {
        return formType;
    }

    /**
     * @param formType the formType to set
     */
    public void setFormType(String formType) {
        this.formType = formType;
    }

    /**
     * @return the programName
     */
    public String getProgramName() {
        return programName;
    }

    /**
     * @param programName the programName to set
     */
    public void setProgramName(String programName) {
        this.programName = programName;
    }

    /**
     * @return the cotrimStartDate
     */
    public Date getCotrimStartDate() {
        return cotrimStartDate;
    }

    /**
     * @param cotrimStartDate the cotrimStartDate to set
     */
    public void setCotrimStartDate(Date cotrimStartDate) {
        this.cotrimStartDate = cotrimStartDate;
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
     * @return the provider
     */
    public String getProvider() {
        return provider;
    }

    /**
     * @param provider the provider to set
     */
    public void setProvider(String provider) {
        this.provider = provider;
    }
}
