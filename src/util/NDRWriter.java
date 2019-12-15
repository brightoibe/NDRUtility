/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import static com.inductivehealth.ndr.client.Client.getXmlDate;
import static com.inductivehealth.ndr.client.Client.getXmlDateTime;
import com.inductivehealth.ndr.client.Validator;
import com.inductivehealth.ndr.schema.AddressType;
import com.inductivehealth.ndr.schema.AnswerType;
import com.inductivehealth.ndr.schema.CodedSimpleType;
import com.inductivehealth.ndr.schema.CodedType;
import com.inductivehealth.ndr.schema.CommonQuestionsType;
import com.inductivehealth.ndr.schema.ConditionSpecificQuestionsType;
import com.inductivehealth.ndr.schema.ConditionType;
import com.inductivehealth.ndr.schema.Container;
import com.inductivehealth.ndr.schema.EncountersType;
import com.inductivehealth.ndr.schema.FacilityType;
import com.inductivehealth.ndr.schema.HIVEncounterType;
import com.inductivehealth.ndr.schema.HIVQuestionsType;
import com.inductivehealth.ndr.schema.IdentifierType;
import com.inductivehealth.ndr.schema.IdentifiersType;
import com.inductivehealth.ndr.schema.IndividualReportType;
import com.inductivehealth.ndr.schema.LaboratoryOrderAndResult;
import com.inductivehealth.ndr.schema.LaboratoryReportType;
import com.inductivehealth.ndr.schema.MessageHeaderType;
import com.inductivehealth.ndr.schema.NumericType;
import com.inductivehealth.ndr.schema.PatientDemographicsType;
import com.inductivehealth.ndr.schema.ProgramAreaType;
import com.inductivehealth.ndr.schema.RegimenType;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import model.datapump.Concept;
import model.datapump.Demographics;
import model.datapump.DrugOrder;
import model.datapump.Drugs;
import model.datapump.Location;
import model.datapump.Obs;
import model.datapump.PatientRegimen;
import model.datapump.Visit;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.Weeks;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author brightoibe
 */
public class NDRWriter {

    private JAXBContext context;
    private Container container;
    private MessageHeaderType messageHeaderType;
    private FacilityType sendingOrganization;
    private IndividualReportType indRpt;
    private PatientDemographicsType patient;
    private ConditionType condition;
    private ProgramAreaType pa;
    private CommonQuestionsType common;
    private ConditionSpecificQuestionsType disease;
    private HashMap<String, String> mapRegimenToCodeDictionary, stateCodeDictinary, lgaCodeDictionary, localCodeMapping, labTestUnitDescription, locationMap;
    private HashMap<Integer, String> hivQueDictionary, patientDemographicDictionary, hivEncounterTypeDictionary, labTestDictionary, labTestUnits;
    private HashMap<Integer, Concept> conceptDictionary;
    //private LocationMap locMap=new LocationMap();
    // private HashMap<Integer, String> locationMap;

    public NDRWriter() {

    }

    public void setConceptDictionary(HashMap<Integer, Concept> dictionary) {
        this.conceptDictionary = dictionary;
    }

    public void loadLabTestUnitDescription() {
        labTestUnitDescription = new HashMap<String, String>();
        labTestUnitDescription.put("48", "CellsPerMicroLiter,cell/ul");
        labTestUnitDescription.put("60", "CopiesPerMilliLiter,copies/ml");
        labTestUnitDescription.put("398", "percent,%");
        labTestUnitDescription.put("311", "MilliMolesPerLiter,mmol/L");
        labTestUnitDescription.put("25", "BillionPerLiter,10*9/L");
        labTestUnitDescription.put("257", "MicroMole,umol");
        labTestUnitDescription.put("136", "GramsPerDeciLiter,g/dL");
        labTestUnitDescription.put("93", "GramsPerDeciLiter,U/L");
    }

    public void loadLabTestUnitDictionary() {
        labTestUnits = new HashMap<Integer, String>();
        labTestUnits.put(88, "48");
        labTestUnits.put(315, "60");
        labTestUnits.put(1153, "398");
        labTestUnits.put(365, "311");
        labTestUnits.put(331, "25");
        labTestUnits.put(1717, "48");
        labTestUnits.put(1168, "398");
        labTestUnits.put(366, "311");
        labTestUnits.put(1169, "48");
        labTestUnits.put(1718, "398");
        labTestUnits.put(367, "311");
        labTestUnits.put(1716, "48");
        labTestUnits.put(1170, "398");
        labTestUnits.put(1531, "311");
        labTestUnits.put(1719, "48");
        labTestUnits.put(1156, "398");
        labTestUnits.put(451, "257");
        labTestUnits.put(1150, "48");
        labTestUnits.put(7777906, "398");
        labTestUnits.put(1528, "257");
        labTestUnits.put(7777907, "398");
        labTestUnits.put(228, "136");
        labTestUnits.put(1529, "93");
        labTestUnits.put(375, "25");
        labTestUnits.put(1175, "311");
        labTestUnits.put(1159, "311");
        labTestUnits.put(313, "257");
        labTestUnits.put(308, "93");
        labTestUnits.put(309, "93");
        labTestUnits.put(1176, "311");
        labTestUnits.put(1530, "93");
        labTestUnits.put(329, "398");
        labTestUnits.put(332, "25");
    }

    public void loadLocationMap() {
        locationMap = new HashMap<String, String>();
        locationMap.put("c0b45418-3046-4694-bd7d-66d2d93f7184", "IHVN/BNS/GBK/582");
        locationMap.put("dc72e945-2ee0-4788-befa-cae9c120ec3c", "IHVN/BNS/MAK/456");
        locationMap.put("df8e8bb4-174c-4353-bd2e-33282f2dbcb0", "IHVN/BNS/MAK/589");
        locationMap.put("d2e57013-253d-4bc3-88fe-a36e6a53fc86", "IHVN/BNS/UKM/925");
        locationMap.put("30a2dc90-6344-4835-8358-1d9a99b7bc6e", "IHVN/BNS/MAK/591");
        locationMap.put("72e46bf9-27fb-4474-bfb2-4a1655df9d35", "IHVN/BNS/UKM/636");
        locationMap.put("38bbac91-be6a-4ccc-85ac-a683fcb06009", "IHVN/BNS/GBK/922");
        locationMap.put("e0da02fa-45ff-42ce-9554-20fb3cef6f8d", "IHVN/BNS/MAK/456");
        locationMap.put("d0b0fae8-e1d4-4edb-81a8-0c0db5e3b948", "IHVN/BNS/KOH/996");
        locationMap.put("9c554083-1ec7-4728-8aac-32292cdb5e1b", "IHVN/BNS/GBK/553");
        locationMap.put("8294eafd-281f-49ec-95a9-9e2a5dc0c570", "IHVN/BNS/USH/924");
        locationMap.put("2beedc3b-3447-4576-9c93-3bf1d1d89d28", "IHVN/BNS/GBK/583");
        locationMap.put("056ac42b-8514-4c14-80c9-0a0f9704707e", "IHVN/BNS/MAK/585");
        locationMap.put("126bdf53-f162-4c9c-9934-08558b953232", "IHVN/BNS/GBK/548");
        locationMap.put("13d7b277-4f13-4ae6-8d9e-883b4dedaf2b", "IHVN/BNS/VAN/549");
        locationMap.put("b58b909b-a01f-4103-a0fe-5e418b1c23f5", "IHVN/BNS/MAK/554");
        locationMap.put("3dc17c86-f817-4733-871e-66384edbcc6e", "IHVN/BNS/UKM/716");
        locationMap.put("3ea88257-e6ba-476f-af78-3804c0abc550", "IHVN/BNS/MAK/992");
        locationMap.put("4faa683d-09e8-41b3-8a8d-bf4bc6899e4e", "IHVN/BNS/UKM/927");
        locationMap.put("b4e75dcf-0a30-4a50-a9e3-6f528df3851e", "IHVN/DEL/IKA/028");
        locationMap.put("68134ad9-f63a-4d8a-9eee-44363863612e", "IHVN/DEL/OSH/017");
        locationMap.put("3fc563e4-5281-4f29-b99c-b17b6605f5d8", "IHVN/DEL/ETE/1131");
        locationMap.put("bc8c3778-99e4-4cdd-abd6-ada0d078bfe6", "IHVN/DEL/OSH/092");
        locationMap.put("f99b428e-fa09-49aa-9195-f2618ac012f5", "IHVN/DEL/UGN/1657");
        locationMap.put("5c0a8cf9-e1e3-45cd-88a1-ca7bbebb96fb", "IHVN/FCT/AMA/639");
        locationMap.put("94c6fa6d-3ca1-4394-9a39-9b481242b0ee", "IHVN/FCT/GWA/640");
        locationMap.put("e97727be-9269-4af3-8f21-7397ca8efa12", "IHVN/FCT/AMA/007");
        locationMap.put("7e1c55fe-3dc4-4071-b021-184616bf4469", "IHVN/FCT/BWR/1515");
        locationMap.put("bcdeb072-46ef-4788-a059-bc6ada7e94bd", "IHVN/FCT/BWA/388");
        locationMap.put("bcb264af-4110-409b-aad0-6bd2b1bffddb", "IHVN/FCT/GWA/1650");
        locationMap.put("d6ecff43-5cf2-4757-9a47-47aca844a05b", "IHVN/FCT/BWA/1015");
        locationMap.put("db55d767-24dd-410f-b8d9-4c49356463c2", "IHVN/FCT/BWA/1015");
        locationMap.put("9119a8c0-af16-4e38-8894-a2fc91280719", "IHVN/FCT/AMA/651");
        locationMap.put("93e1baf4-c0f2-4a0c-9a0c-52f584429839", "IHVN/FCT/AMA/248");
        locationMap.put("6aee056b-75b8-4689-9228-f53a01b99c33", "IHVN/FCT/AMA/249");
        locationMap.put("f744356f-013b-4099-a636-17f9cb2ee193", "IHVN/FCT/AMA/035");
        locationMap.put("c0e799b5-3a65-45f1-a2ab-99e3c4376896", "IHVN/FCT/GWA/061");
        locationMap.put("4113a5bb-40ab-4ed9-8f86-66cc2fe0c255", "IHVN/FCT/AMA/508");
        locationMap.put("c66c4041-e3a7-477d-9109-d309d5287b8e", "IHVN/FCT/AMA/119");
        locationMap.put("131408ff-a00d-481b-b428-e58d7c4aa8c8", "IHVN/FCT/AMA/633");
        locationMap.put("9793ce1b-24fb-48fc-a22c-271c9a1cce46", "IHVN/FCT/AMA/002");
        locationMap.put("d32577eb-6d71-4eb1-bdbe-56974a40ac17", "IHVN/FCT/AMA/008");
        locationMap.put("8be33aeb-8fa9-4a84-aeed-ffa7ec76e64e", "IHVN/FCT/KAR/1029");
        locationMap.put("74b52262-3d24-4335-8104-25f5b36835fd", "IHVN/FCT/AMA/045");
        locationMap.put("4e02fd9a-b7a0-489e-9a86-3cbe7b580c43", "IHVN/FCT/BWA/389");
        locationMap.put("4be47cd5-bdbd-4a19-ba38-b92dd1f899e1", "IHVN/FCT/GWA/1019");
        locationMap.put("3e949404-59f0-44fd-8722-7aa709aa37e8", "IHVN/FCT/AMA/650");
        locationMap.put("dd70e438-bc06-4d71-aaf7-d5505de74833", "IHVN/FCT/BWR/634");
        locationMap.put("c7d11c1a-0752-497c-9dac-c2d8d30f7a27", "IHVN/FCT/GWA/001");
        locationMap.put("531555b0-7eed-430d-b843-534fa251203b", "IHVN/KAD/ZAR/010");
        locationMap.put("9e971f5b-f65b-4e18-b279-9ae6ac8ed753", "IHVN/KAN/NAS/427");
        locationMap.put("b2a4fd6e-d301-406e-9dce-ed760880d45a", "IHVN/KAN/TAR/003");
        locationMap.put("48e0e1ee-5dd0-41fe-84de-bc5a00938bf2", "IHVN/KAT/BIC/049");
        locationMap.put("88d91cd7-73bd-49fb-bedc-e735a58fb031", "IHVN/KAT/GEZ/115");
        locationMap.put("1085d5b8-9b30-4c3f-95ce-5f901bf6887f", "IHVN/KAT/TAK/047");
        locationMap.put("c6917767-5790-4347-b8ce-c9fba537dee3", "IHVN/KAT/TUW/118");
        locationMap.put("7d5439b0-fed1-4690-9112-5e353ca18e1d", "IHVN/KAT/UNG/110");
        locationMap.put("5597cf99-91f7-4b0e-b71b-e14a0d12f096", "IHVN/KAT/FUT/1522");
        locationMap.put("730db66b-e754-4bf7-8cc2-d0ee84bccf8a", "IHVN/KAT/KAN/107");
        locationMap.put("ef75304a-5fb2-44cf-9148-76c51404195b", "IHVN/KAT/KAT/022");
        locationMap.put("0b49736c-6b5d-4bdc-a0ac-b83b19e1c5a1", "IHVN/KAT/KAT/1521");
        locationMap.put("28da081f-78aa-46ae-b082-6352d39aac51", "IHVN/NAS/LAF/240");
        locationMap.put("5b6d7a1f-6d14-4de8-b7f8-4778e9196401", "IHVN/NAS/NAS/540");
        locationMap.put("61ae5d34-0fd2-462b-8347-261aff64f0cb", "IHVN/NAS/KAR/602");
        locationMap.put("f1824248-d5aa-4fe2-950d-09f6f5fa46e8", "IHVN/NAS/LAF/235");
        locationMap.put("449d8e1f-5e4f-4998-a823-1ef8dfa2786d", "IHVN/NAS/LAF/012");
        locationMap.put("7306d527-5268-48a5-ae80-55a26c612bdb", "IHVN/NAS/NAS/629");
        locationMap.put("2fe1493f-5247-4189-b143-176fc1bcc0ec", "IHVN/NAS/DOM/099");
        locationMap.put("1ae27303-255b-4946-8927-d031a10b8c39", "IHVN/NAS/DOM/523");
        locationMap.put("bdfacdda-c907-42dc-a69b-6cd3d1e6c3f1", "IHVN/NAS/KAR/985");
        locationMap.put("9a983ba6-ca26-4071-b418-7e8eb36c4950", "IHVN/NAS/KAR/609");
        locationMap.put("c96f8259-53e3-4c40-b0f2-ef94ee999eca", "IHVN/NAS/KAR/983");
        locationMap.put("1fdb9183-f121-4754-b5fa-e9928a5fe595", "IHVN/NAS/KAR/983");
        locationMap.put("e11c8f43-4322-4f17-ba52-c9afc2e91369", "IHVN/NAS/KOK/1654");
        locationMap.put("05cbb4e3-2078-49c2-b4bd-21bdbe4cffa1", "IHVN/NAS/NAS/539");
        locationMap.put("6ffcef8d-c818-4fbc-a488-5eab4feac63d", "IHVN/NAS/KAR/319");
        locationMap.put("0131424b-8c67-47db-8a4a-6f340d656093", "IHVN/NAS/KEF/011");
        locationMap.put("3c46de4b-f006-489a-be92-08550765e1c0", "IHVN/NAS/KAR/599");
        locationMap.put("5e97b3b9-9092-405f-8eb4-dc90cf9ac73a", "IHVN/NAS/LAF/1024");
        locationMap.put("85a9fd51-73a0-44a2-8d7e-5536bb1ce3ad", "IHVN/NAS/KAR/614");
        locationMap.put("b0c4f928-9138-4954-a845-1980b1cf3584", "IHVN/NAS/KAR/097");
        locationMap.put("a5cee066-bb44-404b-8c9c-4ca1dc8d03a9", "IHVN/NAS/KAR/615");
        locationMap.put("e1ea9882-82a8-42e0-aabb-4624bf3a4ece", "IHVN/NAS/KEF/1672");
        locationMap.put("bfe126d6-34e0-4b90-88f3-bab1bb76fc3c", "IHVN/NAS/NAS/630");
        locationMap.put("d22fc201-e1c8-43a2-b5f3-d003f2ff380f", "IHVN/NAS/LAF/098");
        locationMap.put("7d27cac7-5407-4308-bb20-aba02b7c1ca6", "IHVN/NAS/KAR/982");
        locationMap.put("46060896-4df6-4e75-8379-a28f6b9d5b64", "IHVN/NAS/OBI/104");
        locationMap.put("e1361f46-4ae7-4692-a0a1-7aac2fac0516", "IHVN/NAS/LAF/102");
        locationMap.put("80ab8061-5495-4881-a988-329124f705f8", "IHVN/NAS/LAF/1021");
        locationMap.put("4ad353f3-276f-4a84-ba27-c2a2eb261a2c", "IHVN/NAS/LAF/054");
        locationMap.put("f212413e-abfc-451b-8bf8-4701f7c327cf", "IHVN/NAS/KOK/106");
        locationMap.put("91c32aff-a8f0-4e7a-85ca-622605a8cd96", "IHVN/OGN/ABS/023");
        locationMap.put("f99a64ea-1be6-4a20-9ff4-dd6ee15aac39", "IHVN/OGN/IJO/1529");
        locationMap.put("ba30549d-8d26-49d6-b9c4-e05711eaa033", "IHVN/OGN/SAG/027");
        locationMap.put("4b16d457-e256-4456-9350-3bb09d284147", "IHVN/OSU/OSO/021");
        locationMap.put("466fbff5-5b52-4e23-bf9b-2bb76e348283", "IHVN/OSU/IFC/036");
        locationMap.put("db01db01b975-17ea-4bcd-87b6-52399df4fb19", "IHVN/FCT/AMA/509");
        locationMap.put("0e9d20d0-c695-430e-9488-900139e4c38e", "IHVN/FCT/AMA/1034");
        locationMap.put("570c4640-50f9-4a8a-b0bc-899a859b22a3", "IHVN/FCT/AMA/900");
        locationMap.put("ea753f36-22c7-41e5-8c55-e95e4ef6574b", "IHVN/NAS/KEF/137");
        locationMap.put("fd71cd09-9aea-436c-9cd4-da4ca05fd2e7", "IHVN/FCT/BWR/511");
        locationMap.put("b21776db-17c4-420f-8181-f2e786faca56", "IHVN/FCT/AMA/316");
        locationMap.put("1c2d64fa-8302-4618-a133-c2fef895d582", "IHVN/FCT/AMA/040");
        locationMap.put("548a2546-8ac5-4404-a7d8-ee4629301006", "IHVN/FCT/BWR/490");
        locationMap.put("548a2546-8ac5-4404-a7d8-ee4629301006", "IHVN/FCT/BWR/490");
        locationMap.put("a8c49f4f-3904-4ff1-99e0-b673cba98bdb", "IHVN/FCT/AMA/485");
        locationMap.put("85349a78-d9e8-4bc0-95b1-4c7f7b4c432a", "IHVN/FCT/AMA/1005");
        locationMap.put("649eb678-52ad-42b7-a782-fd7e8d9a74f1", "IHVN/FCT/AMA/515");
        locationMap.put("02204442-68c1-4502-a428-87d0e5b99c98", "IHVN/FCT/AMA/486");
        locationMap.put("366a8792-7e92-4b1d-82e0-4df5a30d0519", "IHVN/FCT/AMA/1009");
        locationMap.put("db657789-e836-40d6-9dd2-ea78f3960f2b", "IHVN/NAS/LAF/235");
        locationMap.put("3767a2ac-aa17-4f45-9688-2b0e817eac68", "IHVN/NAS/KAR/1022");
        locationMap.put("beCityHospital", "IHVN/BNS/MAK/592");
        locationMap.put("apexSpecialistHospital", "IHVN/FCT/BWA/120");
        locationMap.put("PreciousClinicAndMaternity", "IHVN/FCT/AMA/649");
        locationMap.put("HeleenClinicAndMaternity", "IHVN/BNS/GBK/994");

        locationMap.put("MbagenPHC", "IHVN/BNS/UHG/918");
        locationMap.put("MbagenCHA", "IHVN/BNS/BUR/358");
        locationMap.put("AkuComp", "IHVN/BNS/USH/333");
        locationMap.put("LukeHospital", "IHVN/BNS/GBK/581");
        locationMap.put("Joefag", "IHVN/FCT/GWA/493");
        locationMap.put("KujePHC", "IHVN/FCT/KUJ/318");
        locationMap.put("MGH", "IHVN/FCT/AMA/1516");
        locationMap.put("LauraHospitalAndMaternity", "IHVN/FCT/BWA/1016");
        locationMap.put("PHCKpaduma", "IHVN/FCT/AMA/041");
        locationMap.put("AdogiPHC", "IHVN/NAS/LAF/975");
        locationMap.put("AmosunMaternityHospital", "IHVN/NAS/KEF/620");
        locationMap.put("AngbasClinic", "IHVN/NAS/LAF/986");
        locationMap.put("ArumangyeBosco", "IHVN/NAS/DOM/328");
        locationMap.put("CityInternational", "IHVN/NAS/KAR/604");
        locationMap.put("KingscareHospital", "IHVN/NAS/KAR/612");
        locationMap.put("MararabaGuruku", "IHVN/NAS/KAR/046");
        locationMap.put("MCWC1Obi", "IHVN/NAS/OBI/436");
        locationMap.put("MissionClinic", "IHVN/NAS/KAR/475");
        locationMap.put("NewEraClinic", "IHVN/NAS/DOM/448");
        locationMap.put("PijagMaternityHome", "IHVN/NAS/KAR/603");
        locationMap.put("PHCMainTownNasarawa", "IHVN/NAS/NAS/972");
        locationMap.put("PHCHealthCenterBarkinAbdullahi", "IHVN/NAS/LAF/528");

    }

    /* public void loadLocationMap() {
        locationMap = new HashMap<Integer, String>();
        locationMap.put(2, "IHVN/FCT/AMA/007");
        locationMap.put(3, "IHVN/NAS/LAF/012");
        locationMap.put(4, "IHVN/NAS/KEF/011");
        locationMap.put(6, "IHVN/NAS/DOM/523");
        locationMap.put(6, "IHVN/NAS/DOM/523");
        locationMap.put(7, "IHVN/NAS/KOK/1654");
        locationMap.put(8, "IHVN/NAS/KAR/046");
        locationMap.put(12, "IHVN/NAS/KOK/106");
        locationMap.put(14, "IHVN/NAS/LAF/054");
        locationMap.put(15, "IHVN/NAS/KOK/127");
        locationMap.put(16, "IHVN/NAS/OBI/104");
        locationMap.put(18, "IHVN/NAS/LAF/102");
        locationMap.put(19, "IHVN/NAS/AWE/526");
        locationMap.put(20, "IHVN/NAS/DOM/523");
        locationMap.put(21, "IHVN/NAS/KEA/100");
        locationMap.put(22, "IHVN/NAS/KAR/097");
        locationMap.put(26, "IHVN/NAS/TOT/129");
        locationMap.put(29, "IHVN/NAS/LAF/098");
        locationMap.put(36, "IHVN/FCT/AMA/002");
        locationMap.put(37, "IHVN/FCT/AMA/008");
        locationMap.put(38, "IHVN/FCT/AMA/035");
        locationMap.put(39, "IHVN/FCT/GWA/001");
        locationMap.put(41, "IHVN/KAD/ZAR/010");
        locationMap.put(42, "IHVN/DEL/IKA/028");
        locationMap.put(43, "IHVN/DEL/OSH/017");
        locationMap.put(44, "IHVN/OGN/ABS/023");
        locationMap.put(45, "IHVN/OSU/OSO/021");
        locationMap.put(46, "IHVN/OGN/SAG/027");
        locationMap.put(47, "IHVN/OSU/IFC/036");
        locationMap.put(48, "IHVN/KAT/KAT/022");
        locationMap.put(49, "IHVN/KAN/TAR/003");
        locationMap.put(50, "IHVN/FCT/AMA/119");
        locationMap.put(51, "IHVN/FCT/GWA/061");
        locationMap.put(52, "IHVN/FCT/GWA/1650");
        locationMap.put(54, "IHVN/FCT/AMA/1034");
        locationMap.put(55, "IHVN/FCT/AMA/248");
        locationMap.put(56, "IHVN/FCT/AMA/249");
        locationMap.put(63, "IHVN/BNS/VAN/549");
        locationMap.put(64, "IHVN/BNS/GBK/548");
        locationMap.put(65, "IHVN/BNS/MAK/456");
        locationMap.put(66, "IHVN/BNS/MAK/355");
        locationMap.put(68, "IHVN/DEL/ETE/1131");
        locationMap.put(71, "IHVN/DEL/ISS/797");
        locationMap.put(72, "IHVN/DEL/OSH/092");
        locationMap.put(74, "IHVN/DEL/UGN/1657");
        locationMap.put(75, "IHVN/DEL/UGN/1656");
        locationMap.put(78, "IHVN/OSU/IWO/290");
        locationMap.put(82, "IHVN/OGN/ABS/1528");
        locationMap.put(83, "IHVN/KAT/GEZ/115");
        locationMap.put(85, "IHVN/KAN/KAR/933");
        locationMap.put(86, "IHVN/KAN/NAS/1621");
        locationMap.put(87, "IHVN/KAN/NAS/427");
        locationMap.put(88, "IHVN/KAT/TAK/047");
        locationMap.put(89, "IHVN/KAN/TSN/932");
        locationMap.put(90, "IHVN/KAT/TUW/118");
        locationMap.put(91, "IHVN/KAT/UNG/110");
        locationMap.put(92, "IHVN/KAT/BIC/049");
        locationMap.put(93, "IHVN/KAT/BAT/475");
        locationMap.put(94, "IHVN/KAT/BAU/479");
        locationMap.put(95, "IHVN/KAT/FUT/1522");
        locationMap.put(96, "IHVN/KAT/ING/477");
        locationMap.put(97, "IHVN/KAT/KAN/474");
        locationMap.put(98, "IHVN/KAT/KAN/107");
        locationMap.put(99, "IHVN/KAT/KAT/1521");
        locationMap.put(101, "IHVN/KAT/MAL/480");
        locationMap.put(102, "IHVN/KAT/MAN/476");
        locationMap.put(103, "IHVN/KAT/MUS/478");
        //locationMap.put(104, "IHVN/BNS/BUR/358");
        locationMap.put(104, "IHVN/FCT/AMA/1516");
        locationMap.put(106, "IHVN/FCT/ABJ/500");
        locationMap.put(108, "IHVN/FCT/AMA/1034");
        locationMap.put(109, "IHVN/FCT/BWR/1515");
        locationMap.put(110, "IHVN/BNS/GBK/552");
        locationMap.put(111, "IHVN/BNS/GBK/553");
        locationMap.put(112, "IHVN/BNS/GBK/582");
        locationMap.put(113, "IHVN/BNS/GBK/583");
        locationMap.put(114, "IHVN/BNS/GBK/922");
        locationMap.put(115, "IHVN/BNS/UKM/716");
        locationMap.put(116, "IHVN/BNS/UKM/925");
        locationMap.put(117, "IHVN/BNS/UKM/927");
        locationMap.put(118, "IHVN/BNS/UKM/636");
        locationMap.put(119, "IHVN/BNS/GBK/548");
        locationMap.put(120, "IHVN/BNS/MAK/554");
        locationMap.put(121, "IHVN/BNS/KOH/996");
        locationMap.put(122, "IHVN/BNS/USH/924");
        locationMap.put(123, "IHVN/BNS/KWD/644");
        locationMap.put(124, "IHVN/NAS/KAR/610");
        locationMap.put(125, "IHVN/NAS/KOK/1021");
        locationMap.put(126, "IHVN/NAS/KOK/1021");
        locationMap.put(127, "IHVN/NAS/KEF/516");
        locationMap.put(128, "IHVN/OND/OWO/990");
        locationMap.put(129, "IHVN/OND/ANE/086");
        locationMap.put(130, "IHVN/OND/AKS/083");
        locationMap.put(131, "IHVN/OND/AKS/617");
        locationMap.put(132, "IHVN/EKI/IJE/719");
        locationMap.put(133, "IHVN/OND/OKT/085");
        locationMap.put(134, "IHVN/OND/OGB/075");
        locationMap.put(135, "IHVN/EKI/IDO/078");
        locationMap.put(136, "IHVN/EKI/ADE/079");
        locationMap.put(137, "IHVN/OGN/IJO/1529");
        locationMap.put(139, "IHVN/OGN/IKE/038");
        locationMap.put(140, "IHVN/OGN/IFO/082");
        locationMap.put(142, "IHVN/DEL/UVE/828");
        locationMap.put(143, "IHVN/DEL/UDU/1355");
        locationMap.put(144, "IHVN/DEL/UKI/209");
        locationMap.put(145, "IHVN/DEL/OSN/211");
        locationMap.put(146, "IHVN/DEL/WAS/1459");
        locationMap.put(147, "IHVN/DEL/ETW/803");
        locationMap.put(148, "IHVN/DEL/OSS/1302");
        locationMap.put(149, "IHVN/DEL/WAS/434");
        locationMap.put(150, "IHVN/DEL/INE/853");
        locationMap.put(152, "IHVN/DEL/ETW/206");
        locationMap.put(153, "IHVN/DEL/ETE/1655");
        locationMap.put(154, "IHVN/FCT/KAR/1029");
        locationMap.put(155, "IHVN/NAS/KAR/599");
        locationMap.put(156, "IHVN/NAS/KAR/965");
        locationMap.put(158, "IHVN/FCT/AMA/1011");
    }*/
    public void loadLGAMap() {
        lgaCodeDictionary = new HashMap<String, String>();
        lgaCodeDictionary.put("ABADAM", "1");
        lgaCodeDictionary.put("ABAJI", "2");
        lgaCodeDictionary.put("ABAK", "3");
        lgaCodeDictionary.put("ABAKALIKI", "4");
        lgaCodeDictionary.put("ABA NORTH", "5");
        lgaCodeDictionary.put("ABA SOUTH", "6");
        lgaCodeDictionary.put("ABEOKUTA NORTH", "7");
        lgaCodeDictionary.put("ABEOKUTA SOUTH", "8");
        lgaCodeDictionary.put("ABI", "9");
        lgaCodeDictionary.put("ABOH MBAISE", "10");
        lgaCodeDictionary.put("ABUA/ODUAL", "11");
        lgaCodeDictionary.put("ADAVI", "12");
        lgaCodeDictionary.put("ADO-EKITI", "13");
        lgaCodeDictionary.put("ADO ODO/OTA", "14");
        lgaCodeDictionary.put("AFIJIO", "15");
        lgaCodeDictionary.put("AFIKPO NORTH", "16");
        lgaCodeDictionary.put("AFIKPO SOUTH", "17");
        lgaCodeDictionary.put("AGAIE", "18");
        lgaCodeDictionary.put("AGATU", "19");
        lgaCodeDictionary.put("AGWARA", "20");
        lgaCodeDictionary.put("AGEGE", "21");
        lgaCodeDictionary.put("AGUATA", "22");
        lgaCodeDictionary.put("AHIAZU MBAISE", "23");
        lgaCodeDictionary.put("AHOADA EAST", "24");
        lgaCodeDictionary.put("AHOADA WEST", "25");
        lgaCodeDictionary.put("AJAOKUTA", "26");
        lgaCodeDictionary.put("AJEROMI/IFELODUN", "27");
        lgaCodeDictionary.put("AJINGI", "28");
        lgaCodeDictionary.put("AKAMKPA", "29");
        lgaCodeDictionary.put("AKINYELE", "30");
        lgaCodeDictionary.put("AKKO", "31");
        lgaCodeDictionary.put("AKOKO EDO", "32");
        lgaCodeDictionary.put("AKOKO NORTH EAST", "33");
        lgaCodeDictionary.put("AKOKO NORTH WEST", "34");
        lgaCodeDictionary.put("AKOKO SOUTH WEST", "35");
        lgaCodeDictionary.put("AKOKO SOUTH EAST", "36");
        lgaCodeDictionary.put("AKPABUYO	", "37");
        lgaCodeDictionary.put("AKUKU TORU", "38");
        lgaCodeDictionary.put("AKURE NORTH", "39");
        lgaCodeDictionary.put("AKURE SOUTH", "40");
        lgaCodeDictionary.put("AKWANGA", "41");
        lgaCodeDictionary.put("ALBASU", "42");
        lgaCodeDictionary.put("ALIERO", "43");
        lgaCodeDictionary.put("ALIMOSHO", "44");
        lgaCodeDictionary.put("ALKALERI", "45");
        lgaCodeDictionary.put("AMUWO-ODOFIN", "46");
        lgaCodeDictionary.put("ANAMBRA EAST", "47");
        lgaCodeDictionary.put("ANAMBRA WEST", "48");
        lgaCodeDictionary.put("ANAOCHA", "49");
        lgaCodeDictionary.put("ANDONI", "50");
        lgaCodeDictionary.put("ANINRI", "51");
        lgaCodeDictionary.put("ANIOCHA NORTH", "52");
        lgaCodeDictionary.put("ANIOCHA SOUTH", "53");
        lgaCodeDictionary.put("ANKA", "54");
        lgaCodeDictionary.put("ANKPA", "55");
        lgaCodeDictionary.put("APA", "56");
        lgaCodeDictionary.put("APAPA", "57");
        lgaCodeDictionary.put("ARDO KOLA", "59");
        lgaCodeDictionary.put("AREWA DANDI", "60");
        lgaCodeDictionary.put("ARGUNGU	", "61");
        lgaCodeDictionary.put("AROCHUKWU", "62");
        lgaCodeDictionary.put("ASA", "63");
        lgaCodeDictionary.put("ASARI TORU", "64");
        lgaCodeDictionary.put("ASKIRA/UBA", "65");
        lgaCodeDictionary.put("ATAKUMOSA EAST", "66");
        lgaCodeDictionary.put("ATAKUMOSA WEST", "67");
        lgaCodeDictionary.put("ATIBA", "68");
        lgaCodeDictionary.put("ATISBO", "69");
        lgaCodeDictionary.put("AUGIE", "70");
        lgaCodeDictionary.put("AUYO", "71");
        lgaCodeDictionary.put("AWE", "72");
        lgaCodeDictionary.put("AWGU", "73)");
        lgaCodeDictionary.put("AYAMELUM", "76");
        lgaCodeDictionary.put("AYEDAADE", "77");
        lgaCodeDictionary.put("AYEDIRE", "78");
        lgaCodeDictionary.put("BABURA", "79");
        lgaCodeDictionary.put("BADAGRY", "80");
        lgaCodeDictionary.put("BAGUDO", "81");
        lgaCodeDictionary.put("BAGWAI", "82");
        lgaCodeDictionary.put("BAKASSI", "83");
        lgaCodeDictionary.put("BOKKOS", "84");
        lgaCodeDictionary.put("BAKORI", "85");
        lgaCodeDictionary.put("BAKURA", "86");
        lgaCodeDictionary.put("BALANGA", "87");
        lgaCodeDictionary.put("BALI", "88");
        lgaCodeDictionary.put("BAMA", "89");
        lgaCodeDictionary.put("BADE", "90");
        lgaCodeDictionary.put("BARKIN LADI", "91");
        lgaCodeDictionary.put("BARUTEN", "92");
        lgaCodeDictionary.put("BASSA", "93");
        lgaCodeDictionary.put("BATAGARAWA", "95");
        lgaCodeDictionary.put("BATSARI", "96");
        lgaCodeDictionary.put("BAUCHI", "97");
        lgaCodeDictionary.put("BAURE", "98");
        lgaCodeDictionary.put("BAYO", "99");
        lgaCodeDictionary.put("BEBEJI", "100");
        lgaCodeDictionary.put("BEKWARA", "102");
        lgaCodeDictionary.put("BENDE", "103");
        lgaCodeDictionary.put("BIASE", "104");
        lgaCodeDictionary.put("BICHI", "105");
        lgaCodeDictionary.put("BIDA", "106");
        lgaCodeDictionary.put("BILLIRI", "107");
        lgaCodeDictionary.put("BINDAWA", "108");
        lgaCodeDictionary.put("BINJI", "109");
        lgaCodeDictionary.put("BIRNIWA", "110");
        lgaCodeDictionary.put("BIRNIN GWARI", "111");
        lgaCodeDictionary.put("BIRNIN KEBBI", "112");
        lgaCodeDictionary.put("BIRNIN KUDU", "113");
        lgaCodeDictionary.put("BIRNIN MAGAJI", "114");
        lgaCodeDictionary.put("BIU", "115");
        lgaCodeDictionary.put("BODINGA", "116");
        lgaCodeDictionary.put("BOGORO", "117");
        lgaCodeDictionary.put("BOKI", "118");
        lgaCodeDictionary.put("BOLUWADURO", "119");
        lgaCodeDictionary.put("BOMADI", "120");
        lgaCodeDictionary.put("BONNY", "121");
        lgaCodeDictionary.put("BORGU", "122");
        lgaCodeDictionary.put("BORIPE", "123");
        lgaCodeDictionary.put("BURSARI", "124");
        lgaCodeDictionary.put("BOSSO", "125");
        lgaCodeDictionary.put("BRASS", "126");
        lgaCodeDictionary.put("BUJI", "127");
        lgaCodeDictionary.put("BUKKUYUM", "128");
        lgaCodeDictionary.put("BURUKU", "129");
        lgaCodeDictionary.put("BUNGUDU", "130");
        lgaCodeDictionary.put("BUNKURE", "131");
        lgaCodeDictionary.put("BUNZA", "132");
        lgaCodeDictionary.put("BURUTU", "133");
        lgaCodeDictionary.put("BWARI", "134");
        lgaCodeDictionary.put("CALABAR MUNICIPAL", "135");
        lgaCodeDictionary.put("CALABAR SOUTH", "136");
        lgaCodeDictionary.put("CHANCHAGA", "137");
        lgaCodeDictionary.put("CHARANCHI", "138");
        lgaCodeDictionary.put("CHIBOK", "139");
        lgaCodeDictionary.put("CHIKUN", "140");
        lgaCodeDictionary.put("DALA", "141");
        lgaCodeDictionary.put("DAMATURU", "142");
        lgaCodeDictionary.put("DAMBAM", "143");
        lgaCodeDictionary.put("DAMBATTA", "144");
        lgaCodeDictionary.put("DAMBOA", "145");
        lgaCodeDictionary.put("DANDI", "146");
        lgaCodeDictionary.put("DANDUME", "147");
        lgaCodeDictionary.put("DANGE", "149");
        lgaCodeDictionary.put("DANJA", "149");
        lgaCodeDictionary.put("DAN-MUSA", "150");
        lgaCodeDictionary.put("DARAZO", "151");
        lgaCodeDictionary.put("DASS", "152");
        lgaCodeDictionary.put("DAURA", "153");
        lgaCodeDictionary.put("DAWAKIN KUDU", "154");
        lgaCodeDictionary.put("DAWAKIN TOFA", "155");
        lgaCodeDictionary.put("DEGEMA", "156");
        lgaCodeDictionary.put("DEKINA", "157");
        lgaCodeDictionary.put("DEMSA", "158");
        lgaCodeDictionary.put("DIKWA", "159");
        lgaCodeDictionary.put("DOGUWA", "160");
        lgaCodeDictionary.put("DOMA", "161");
        lgaCodeDictionary.put("DONGA", "162");
        lgaCodeDictionary.put("DUKKU", "163");
        lgaCodeDictionary.put("DUNUKOFIA", "164");
        lgaCodeDictionary.put("DUTSE", "165");
        lgaCodeDictionary.put("DUTSI", "166");
        lgaCodeDictionary.put("DUTSIN MA", "167");
        lgaCodeDictionary.put("EASTERN OBOLO", "168");
        lgaCodeDictionary.put("EBONYI", "169");
        lgaCodeDictionary.put("EDATI", "170");
        lgaCodeDictionary.put("EDE NORTH", "171");
        lgaCodeDictionary.put("EDE SOUTH", "172");
        lgaCodeDictionary.put("EDU", "173");
        lgaCodeDictionary.put("EFON", "178");
        lgaCodeDictionary.put("EGBEDA", "181");
        lgaCodeDictionary.put("EGBEDORE", "182");
        lgaCodeDictionary.put("EGOR", "183");
        lgaCodeDictionary.put("EHIME MBANO", "184");
        lgaCodeDictionary.put("EJIGBO", "185");
        lgaCodeDictionary.put("EKEREMOR", "186");
        lgaCodeDictionary.put("EKET", "187");
        lgaCodeDictionary.put("EKITI", "188");
        lgaCodeDictionary.put("EKITI EAST", "189");
        lgaCodeDictionary.put("EKITI SOUTH WEST", "190");
        lgaCodeDictionary.put("EKITI WEST", "191");
        lgaCodeDictionary.put("EKWUSIGO", "192");
        lgaCodeDictionary.put("ELEME", "193");
        lgaCodeDictionary.put("EMOHUA", "194");
        lgaCodeDictionary.put("EMURE", "195");
        lgaCodeDictionary.put("ENUGU EAST", "196");
        lgaCodeDictionary.put("ENUGU NORTH", "197");
        lgaCodeDictionary.put("ENUGU SOUTH", "198");
        lgaCodeDictionary.put("EPE", "199");
        lgaCodeDictionary.put("ESAN CENTRAL", "200");
        lgaCodeDictionary.put("ESAN NORTH EAST", "201");
        lgaCodeDictionary.put("ESAN SOUTH EAST", "202");
        lgaCodeDictionary.put("ESAN WEST", "203");
        lgaCodeDictionary.put("ESE ODO", "204");
        lgaCodeDictionary.put("ESIT EKET", "205");
        lgaCodeDictionary.put("ESSIEN UDIM", "206");
        lgaCodeDictionary.put("ETCHE", "207");
        lgaCodeDictionary.put("ETHIOPE EAST", "208");
        lgaCodeDictionary.put("ETHIOPE WEST", "209");
        lgaCodeDictionary.put("ETIM EKPO", "210");
        lgaCodeDictionary.put("ETINAN", "211");
        lgaCodeDictionary.put("ETI-OSA", "212");
        lgaCodeDictionary.put("ETSAKO EAST", "214");
        lgaCodeDictionary.put("ETSAKO CENTRAL", "215");
        lgaCodeDictionary.put("ETSAKO WEST", "215");
        lgaCodeDictionary.put("ETUNG", "216");
        lgaCodeDictionary.put("EWEKORO", "217");
        lgaCodeDictionary.put("EZEAGU", "218");
        lgaCodeDictionary.put("EZINIHITTE MBAISE", "219");
        lgaCodeDictionary.put("EZZA NORTH", "220");
        lgaCodeDictionary.put("EZZA SOUTH", "221");
        lgaCodeDictionary.put("FAGGE", "222");
        lgaCodeDictionary.put("FASKARI", "224");
        lgaCodeDictionary.put("FIKA", "225");
        lgaCodeDictionary.put("FUFORE", "226");
        lgaCodeDictionary.put("FUNAKAYE", "227");
        lgaCodeDictionary.put("FUNE", "228");
        lgaCodeDictionary.put("FUNTUA", "229");
        lgaCodeDictionary.put("GABASAWA", "230");
        lgaCodeDictionary.put("GADA", "231");
        lgaCodeDictionary.put("GAGARAWA", "232");
        lgaCodeDictionary.put("GAMAWA", "233");
        lgaCodeDictionary.put("GANJUWA", "234");
        lgaCodeDictionary.put("GANYE", "235");
        lgaCodeDictionary.put("GARKI", "236");
        lgaCodeDictionary.put("GARKO", "237");
        lgaCodeDictionary.put("GARUN MALLAM", "238");
        lgaCodeDictionary.put("GASHAKA", "239");
        lgaCodeDictionary.put("GASSOL", "240");
        lgaCodeDictionary.put("GAYA", "241");
        lgaCodeDictionary.put("GUYUK", "242");
        lgaCodeDictionary.put("GEZAWA", "243");
        lgaCodeDictionary.put("GBAKO", "244");
        lgaCodeDictionary.put("GBOKO", "245");
        lgaCodeDictionary.put("GBONYIN", "246");
        lgaCodeDictionary.put("GEIDAM", "247");
        lgaCodeDictionary.put("GIADE", "248");
        lgaCodeDictionary.put("GIWA", "249");
        lgaCodeDictionary.put("GOKANA", "250");
        lgaCodeDictionary.put("GOMBE", "251");
        lgaCodeDictionary.put("GOMBI", "252");
        lgaCodeDictionary.put("GORONYO", "253");
        lgaCodeDictionary.put("GIREI", "254");
        lgaCodeDictionary.put("GUBIO", "255");
        lgaCodeDictionary.put("GUDU", "256");
        lgaCodeDictionary.put("GUJBA", "257");
        lgaCodeDictionary.put("GULANI", "258");
        lgaCodeDictionary.put("GUMA", "259");
        lgaCodeDictionary.put("GUMEL", "260");
        lgaCodeDictionary.put("GUMMI", "261");
        lgaCodeDictionary.put("GURARA", "262");
        lgaCodeDictionary.put("GURI", "263");
        lgaCodeDictionary.put("GUSAU", "264");
        lgaCodeDictionary.put("GUZAMALA", "265");
        lgaCodeDictionary.put("GWADABAWA", "266");
        lgaCodeDictionary.put("GWAGWALADA", "267");
        lgaCodeDictionary.put("GWALE", "268");
        lgaCodeDictionary.put("GWANDU", "269");
        lgaCodeDictionary.put("GWARAM", "270");
        lgaCodeDictionary.put("GWARZO", "271");
        lgaCodeDictionary.put("GWER", "272");
        lgaCodeDictionary.put("GWER WEST", "273");
        lgaCodeDictionary.put("GWIWA", "274");
        lgaCodeDictionary.put("GWOZA", "275");
        lgaCodeDictionary.put("ADEJIA", "276");
        lgaCodeDictionary.put("HAWUL", "277");
        lgaCodeDictionary.put("HONG", "278");
        lgaCodeDictionary.put("IBADAN NORTH", "279");
        lgaCodeDictionary.put("IBADAN NORTH EAST", "280");
        lgaCodeDictionary.put("IBADAN NORTH WEST", "281");
        lgaCodeDictionary.put("IBADAN SOUTH EAST", "282");
        lgaCodeDictionary.put("IBADAN SOUTH WEST", "283");
        lgaCodeDictionary.put("IBAJI", "284");
        lgaCodeDictionary.put("IBARAPA CENTRAL", "285");
        lgaCodeDictionary.put("IBARAPA EAST", "286");
        lgaCodeDictionary.put("IBARAPA NORTH", "287");
        lgaCodeDictionary.put("IBEJU/LEKKI", "288");
        lgaCodeDictionary.put("IBENO", "289");
        lgaCodeDictionary.put("IBESIKPO ASUTAN", "290");
        lgaCodeDictionary.put("IBI", "291");
        lgaCodeDictionary.put("IBIONO IBOM", "292");
        lgaCodeDictionary.put("IDAH", "293");
        lgaCodeDictionary.put("IDANRE", "294");
        lgaCodeDictionary.put("IDEATO NORTH", "295");
        lgaCodeDictionary.put("IDEATO SOUTH", "296");
        lgaCodeDictionary.put("IDEMILI NORTH", "297");
        lgaCodeDictionary.put("IDEMILI SOUTH", "298");
        lgaCodeDictionary.put("IDO", "299");
        lgaCodeDictionary.put("IDO-OSI", "300");
        lgaCodeDictionary.put("IFAKO/IJAIYE", "301");
        lgaCodeDictionary.put("IFEDAYO", "302");
        lgaCodeDictionary.put("IFEDORE", "303");
        lgaCodeDictionary.put("IFELODUN", "304");
        lgaCodeDictionary.put("IFO", "306");
        lgaCodeDictionary.put("IGABI", "307");
        lgaCodeDictionary.put("IGALAMELLA", "308");
        lgaCodeDictionary.put("IGBOETITI", "309");
        lgaCodeDictionary.put("IGBOEZE NORTH", "310");
        lgaCodeDictionary.put("IGBOEZE SOUTH", "311");
        lgaCodeDictionary.put("IGUEBEN", "312");
        lgaCodeDictionary.put("IHIALA", "313");
        lgaCodeDictionary.put("IHITTE UBOMA", "314");
        lgaCodeDictionary.put("ILAJE", "315");
        lgaCodeDictionary.put("IJEBU EAST", "316");
        lgaCodeDictionary.put("IJEBU NORTH", "317");
        lgaCodeDictionary.put("IJEBU NORTH EAST", "318");
        lgaCodeDictionary.put("IJEBU ODE", "319");
        lgaCodeDictionary.put("IJERO", "320");
        lgaCodeDictionary.put("IJUMU", "321");
        lgaCodeDictionary.put("IKA", "322");
        lgaCodeDictionary.put("IKA NORTH EAST", "323");
        lgaCodeDictionary.put("IKARA", "324");
        lgaCodeDictionary.put("IKA SOUTH", "325");
        lgaCodeDictionary.put("IKEDURU", "326");
        lgaCodeDictionary.put("IKEJA", "327");
        lgaCodeDictionary.put("IKENNE", "328");
        lgaCodeDictionary.put("IKERE", "329");
        lgaCodeDictionary.put("IKOLE", "330");
        lgaCodeDictionary.put("IKOM", "331");
        lgaCodeDictionary.put("IKONO", "332");
        lgaCodeDictionary.put("IKORODU", "333");
        lgaCodeDictionary.put("IKOT ABASI", "334");
        lgaCodeDictionary.put("IKOT EKPENE", "335");
        lgaCodeDictionary.put("IKPOBA OKHA", "336");
        lgaCodeDictionary.put("IKWERRE", "337");
        lgaCodeDictionary.put("IKWO", "338");
        lgaCodeDictionary.put("IKWUANO", "339");
        lgaCodeDictionary.put("ILA", "340");
        lgaCodeDictionary.put("ILEJEMEJE", "341");
        lgaCodeDictionary.put("ILE OLUJI", "342");
        lgaCodeDictionary.put("ILESA EAST", "343");
        lgaCodeDictionary.put("ILESA WEST", "344");
        lgaCodeDictionary.put("ILLELA", "345");
        lgaCodeDictionary.put("ILORIN-EAST", "346");
        lgaCodeDictionary.put("ILORIN-SOUTH", "347");
        lgaCodeDictionary.put("ILORIN-WEST", "348");
        lgaCodeDictionary.put("IMEKO AFON", "349");
        lgaCodeDictionary.put("INGAWA", "350");
        lgaCodeDictionary.put("INI", "351");
        lgaCodeDictionary.put("IPOKIA", "352");
        lgaCodeDictionary.put("IRELE", "353");
        lgaCodeDictionary.put("IREPO", "354");
        lgaCodeDictionary.put("IREPODUN", "355");
        lgaCodeDictionary.put("IREPODUN/IFELODUN", "358");
        lgaCodeDictionary.put("IREWOLE", "358");
        lgaCodeDictionary.put("ISA", "359");
        lgaCodeDictionary.put("ISE ORUN	", "360");
        lgaCodeDictionary.put("ISEYIN", "361");
        lgaCodeDictionary.put("ISHIELU", "362");
        lgaCodeDictionary.put("ISIALA MBANO", "363");
        lgaCodeDictionary.put("ISIALA NGWA NORTH", "364");
        lgaCodeDictionary.put("ISIALA NGWA SOUTH", "365");
        lgaCodeDictionary.put("ISIUZO", "367");
        lgaCodeDictionary.put("ISOKAN", "368");
        lgaCodeDictionary.put("ISOKO NORTH", "369");
        lgaCodeDictionary.put("ISOKO SOUTH", "370");
        lgaCodeDictionary.put("ISU", "371");
        lgaCodeDictionary.put("ISUIKWUATO", "372");
        lgaCodeDictionary.put("ITAS GADAW", "373");
        lgaCodeDictionary.put("ITESIWAJU", "374");
        lgaCodeDictionary.put("ITU", "375");
        lgaCodeDictionary.put("IVO", "376");
        lgaCodeDictionary.put("IWAJOWA", "377");
        lgaCodeDictionary.put("IWO", "378");
        lgaCodeDictionary.put("IZZI", "379");
        lgaCodeDictionary.put("JABA", "380");
        lgaCodeDictionary.put("JADA", "381");
        lgaCodeDictionary.put("JAHUN", "382");
        lgaCodeDictionary.put("JAKUSKO", "383");
        lgaCodeDictionary.put("JALINGO", "384");
        lgaCodeDictionary.put("JAMAARE", "385");
        lgaCodeDictionary.put("JEGA", "386");
        lgaCodeDictionary.put("JEMAA", "387");
        lgaCodeDictionary.put("JERE", "388");
        lgaCodeDictionary.put("JIBIA", "389");
        lgaCodeDictionary.put("JOS EAST", "390");
        lgaCodeDictionary.put("JOS NORTH", "391");
        lgaCodeDictionary.put("JOS SOUTH", "392");
        lgaCodeDictionary.put("KABBA/BUNU", "393");
        lgaCodeDictionary.put("KABO", "394");
        lgaCodeDictionary.put("KACHIA", "395");
        lgaCodeDictionary.put("KADUNA NORTH", "396");
        lgaCodeDictionary.put("KADUNA SOUTH", "397");
        lgaCodeDictionary.put("KAFIN HAUSA", "398");
        lgaCodeDictionary.put("KAFUR", "399");
        lgaCodeDictionary.put("KAGA", "	400");
        lgaCodeDictionary.put("KAGARKO", "401");
        lgaCodeDictionary.put("KAIAMA", "402");
        lgaCodeDictionary.put("KAITA", "403");
        lgaCodeDictionary.put("KAJOLA", "404");
        lgaCodeDictionary.put("KAJURU", "405");
        lgaCodeDictionary.put("KALA/BALGE", "406");
        lgaCodeDictionary.put("KALGO", "407");
        lgaCodeDictionary.put("KALTUNGO", "408");
        lgaCodeDictionary.put("KANAM", "409");
        lgaCodeDictionary.put("KANKARA", "410");
        lgaCodeDictionary.put("KANKE", "411");
        lgaCodeDictionary.put("KANKIA", "412");
        lgaCodeDictionary.put("KANO MUNICIPAL", "413");
        lgaCodeDictionary.put("KARASUWA", "414");
        lgaCodeDictionary.put("KARAYE", "415");
        lgaCodeDictionary.put("KARIM-LAMIDO", "416");
        lgaCodeDictionary.put("KARU", "417");
        lgaCodeDictionary.put("KATAGUM", "418");
        lgaCodeDictionary.put("KATCHA", "419");
        lgaCodeDictionary.put("KATSINA", "420");
        lgaCodeDictionary.put("KATSINA - ALA", "421");
        lgaCodeDictionary.put("KAURA", "422");
        lgaCodeDictionary.put("KAURA-NAMODA", "423");
        lgaCodeDictionary.put("KAURU", "424");
        lgaCodeDictionary.put("KAZAURE", "425");
        lgaCodeDictionary.put("KEANA", "426");
        lgaCodeDictionary.put("KEBBE", "427");
        lgaCodeDictionary.put("KEFFI", "428");
        lgaCodeDictionary.put("KHANA", "429");
        lgaCodeDictionary.put("KIBIYA", "430");
        lgaCodeDictionary.put("KIRFI", "431");
        lgaCodeDictionary.put("KIRI KASAMA", "432");
        lgaCodeDictionary.put("KIRU", "433");
        lgaCodeDictionary.put("KIYAWA", "434");
        lgaCodeDictionary.put("KOGI", "	435");
        lgaCodeDictionary.put("KOKO/BESSE", "436");
        lgaCodeDictionary.put("KOKONA", "437");
        lgaCodeDictionary.put("OLOKUMA/OPOKUMA", "438");
        lgaCodeDictionary.put("KONDUGA", "439");
        lgaCodeDictionary.put("KONTAGORA", "441");
        lgaCodeDictionary.put("KOSOFE", "442");
        lgaCodeDictionary.put("KAUGAMA", "443");
        lgaCodeDictionary.put("KUBAU", "444");
        lgaCodeDictionary.put("KUDAN", "445");
        lgaCodeDictionary.put("KUJE", "446");
        lgaCodeDictionary.put("KUKAWA", "447");
        lgaCodeDictionary.put("KUMBOTSO", "448");
        lgaCodeDictionary.put("KURMI", "449");
        lgaCodeDictionary.put("KUNCHI", "450");
        lgaCodeDictionary.put("KURA", "451");
        lgaCodeDictionary.put("KURFI", "452");
        lgaCodeDictionary.put("KUSADA", "453");
        lgaCodeDictionary.put("KWALI", "454");
        lgaCodeDictionary.put("KWANDE", "455");
        lgaCodeDictionary.put("KWANI", "456");
        lgaCodeDictionary.put("KWARE", "457");
        lgaCodeDictionary.put("KWAYA", "458");
        lgaCodeDictionary.put("LAFIA", "459");
        lgaCodeDictionary.put("LAGELU", "460");
        lgaCodeDictionary.put("LAGOS ISLAND", "461");
        lgaCodeDictionary.put("AGOS MAINLAND", "462");
        lgaCodeDictionary.put("LANGTANG SOUTH", "463");
        lgaCodeDictionary.put("LANGTANG NORTH", "464");
        lgaCodeDictionary.put("LAPAI", "465");
        lgaCodeDictionary.put("LAMURDE", "466");
        lgaCodeDictionary.put("LAU", "467");
        lgaCodeDictionary.put("LAVUN", "468");
        lgaCodeDictionary.put("LERE", "469");
        lgaCodeDictionary.put("LOGO", "470");
        lgaCodeDictionary.put("LOKOJA", "471");
        lgaCodeDictionary.put("MACHINA", "472");
        lgaCodeDictionary.put("MADAGALI", "473");
        lgaCodeDictionary.put("MADOBI", "474");
        lgaCodeDictionary.put("MAFA", "475");
        lgaCodeDictionary.put("MGAMA", "476");
        lgaCodeDictionary.put("MAGUMERI", "477");
        lgaCodeDictionary.put("MAI'ADUA", "478");
        lgaCodeDictionary.put("MAIDUGURI", "479");
        lgaCodeDictionary.put("MAIGATARI", "480");
        lgaCodeDictionary.put("MAIHA", "481");
        lgaCodeDictionary.put("MAIYAMA", "482");
        lgaCodeDictionary.put("MAKARFI", "483");
        lgaCodeDictionary.put("MAKODA", "484");
        lgaCodeDictionary.put("MALAM MADORI", "485");
        lgaCodeDictionary.put("MALUMFASHI", "486");
        lgaCodeDictionary.put("MANGU", "487");
        lgaCodeDictionary.put("MANI", "488");
        lgaCodeDictionary.put("MARADUN", "489");
        lgaCodeDictionary.put("MARIGA", "490");
        lgaCodeDictionary.put("MAKURDI", "491");
        lgaCodeDictionary.put("MARTE", "492");
        lgaCodeDictionary.put("MARU", "493");
        lgaCodeDictionary.put("MASHEGU", "494");
        lgaCodeDictionary.put("MASHI", "495");
        lgaCodeDictionary.put("MATAZU", "496");
        lgaCodeDictionary.put("MAYO-BELWA", "497");
        lgaCodeDictionary.put("MBAITOLI", "498");
        lgaCodeDictionary.put("MBO", "499");
        lgaCodeDictionary.put("MICHIKA", "500");
        lgaCodeDictionary.put("MIGA", "501");
        lgaCodeDictionary.put("MIKANG", "502");
        lgaCodeDictionary.put("MINIJIBIR", "503");
        lgaCodeDictionary.put("MISAU", "504");
        lgaCodeDictionary.put("MOBA", "505");
        lgaCodeDictionary.put("MOBBAR", "506");
        lgaCodeDictionary.put("MUBI NORTH", "507");
        lgaCodeDictionary.put("MUBI SOUTH", "508");
        lgaCodeDictionary.put("MOKWA", "509");
        lgaCodeDictionary.put("MONGUNO", "510");
        lgaCodeDictionary.put("MOPAMURO", "511");
        lgaCodeDictionary.put("MORO", "512");
        lgaCodeDictionary.put("MUYA", "513");
        lgaCodeDictionary.put("MKPAT ENIN", "514");
        lgaCodeDictionary.put("MUSAWA", "516");
        lgaCodeDictionary.put("MUSHIN", "517");
        lgaCodeDictionary.put("NAFADA", "518");
        lgaCodeDictionary.put("NANGERE", "519");
        lgaCodeDictionary.put("NASARAWA", "520");
        lgaCodeDictionary.put("NASSARAWA", "521");
        lgaCodeDictionary.put("NASSARAWA-EGGON", "522");
        lgaCodeDictionary.put("NDOKWA EAST", "523");
        lgaCodeDictionary.put("NDOKWA WEST", "524");
        lgaCodeDictionary.put("NEMBE", "525");
        lgaCodeDictionary.put("NGALA", "526");
        lgaCodeDictionary.put("NGANZAI", "527");
        lgaCodeDictionary.put("NGASKI", "528");
        lgaCodeDictionary.put("NGOR OKPALA", "529");
        lgaCodeDictionary.put("NGURU", "530");
        lgaCodeDictionary.put("NINGI", "531");
        lgaCodeDictionary.put("NJABA", "532");
        lgaCodeDictionary.put("NJIKOKA", "533");
        lgaCodeDictionary.put("NKANU EAST", "534");
        lgaCodeDictionary.put("NKANU WEST", "535");
        lgaCodeDictionary.put("NKWERRE", "536");
        lgaCodeDictionary.put("NNEWI NORTH", "537");
        lgaCodeDictionary.put("NNEWI SOUTH", "538");
        lgaCodeDictionary.put("NSIT ATAI", "539");
        lgaCodeDictionary.put("NSIT IBOM", "540");
        lgaCodeDictionary.put("NSIT UBIUM", "541");
        lgaCodeDictionary.put("NSUKKA", "542");
        lgaCodeDictionary.put("NUMAN", "543");
        lgaCodeDictionary.put("NWANGELE", "544");
        lgaCodeDictionary.put("OBAFEMI OWODE", "545");
        lgaCodeDictionary.put("OBANLIKU", "546");
        lgaCodeDictionary.put("OBI", "547");
        lgaCodeDictionary.put("OBINGWA", "549");
        lgaCodeDictionary.put("OBIO/AKPOR", "550");
        lgaCodeDictionary.put("OBOKUN", "551");
        lgaCodeDictionary.put("OBOT AKARA", "552");
        lgaCodeDictionary.put("OBOWO", "553");
        lgaCodeDictionary.put("OBUBRA", "554");
        lgaCodeDictionary.put("OBUDU", "555");
        lgaCodeDictionary.put("ODEDA", "556");
        lgaCodeDictionary.put("ODIGBO", "557");
        lgaCodeDictionary.put("ODOGBOLU", "558");
        lgaCodeDictionary.put("ODOOTIN", "559");
        lgaCodeDictionary.put("ODUKPANI", "560");
        lgaCodeDictionary.put("OFFA", "561");
        lgaCodeDictionary.put("OFU", "562");
        lgaCodeDictionary.put("OGBA/EGBEMA/NDONI", "563");
        lgaCodeDictionary.put("OGBADIBO", "564");
        lgaCodeDictionary.put("OGBARU", "565");
        lgaCodeDictionary.put("OGBIA", "566");
        lgaCodeDictionary.put("OGBOMOSO NORTH", "567");
        lgaCodeDictionary.put("OGBOMOSO SOUTH", "568");
        lgaCodeDictionary.put("OGU/BOLO", "569");
        lgaCodeDictionary.put("OGOJA", "570");
        lgaCodeDictionary.put("OGO OLUWA", "571");
        lgaCodeDictionary.put("OGORI MAGOGO", "572");
        lgaCodeDictionary.put("OGUN WATERSIDE", "573");
        lgaCodeDictionary.put("OGUTA", "574");
        lgaCodeDictionary.put("OHAFIA", "575");
        lgaCodeDictionary.put("OHAJI EGBEMA", "576");
        lgaCodeDictionary.put("OHAOZARA", "577");
        lgaCodeDictionary.put("OHAUKWU", "578");
        lgaCodeDictionary.put("OHIMINI", "579");
        lgaCodeDictionary.put("ORHIONMWON", "580");
        lgaCodeDictionary.put("OJI-RIVER", "581");
        lgaCodeDictionary.put("OJO", "582");
        lgaCodeDictionary.put("OJU", "583");
        lgaCodeDictionary.put("OKEHI", "584");
        lgaCodeDictionary.put("OKENE", "585");
        lgaCodeDictionary.put("OKE ERO", "586");
        lgaCodeDictionary.put("OKIGWE", "587");
        lgaCodeDictionary.put("OKITIPUPA", "588");
        lgaCodeDictionary.put("OKOBO", "589");
        lgaCodeDictionary.put("OKPE", "590");
        lgaCodeDictionary.put("OKRIKA", "591");
        lgaCodeDictionary.put("OLAMABORO", "592");
        lgaCodeDictionary.put("OLAOLUWA", "593");
        lgaCodeDictionary.put("OLORUNDA", "594");
        lgaCodeDictionary.put("OLORUNSOGO", "595");
        lgaCodeDictionary.put("OLUYOLE", "596");
        lgaCodeDictionary.put("OMALA", "597");
        lgaCodeDictionary.put("OMUMA", "598");
        lgaCodeDictionary.put("ONA ORA", "599");
        lgaCodeDictionary.put("ONDO EAST", "600");
        lgaCodeDictionary.put("ONDO WEST", "601");
        lgaCodeDictionary.put("ONITCHA", "602");
        lgaCodeDictionary.put("ONITSHA NORTH", "603");
        lgaCodeDictionary.put("ONITSHA SOUTH", "604");
        lgaCodeDictionary.put("ONNA", "605");
        lgaCodeDictionary.put("OKPOKWU", "606");
        lgaCodeDictionary.put("OPOBO/NKORO", "607");
        lgaCodeDictionary.put("OREDO", "608");
        lgaCodeDictionary.put("OORELOPE", "609");
        lgaCodeDictionary.put("ORIADE", "610");
        lgaCodeDictionary.put("ORIRE", "611");
        lgaCodeDictionary.put("ORLU", "612");
        lgaCodeDictionary.put("OROLU", "613");
        lgaCodeDictionary.put("ORON", "614");
        lgaCodeDictionary.put("ORSU", "615");
        lgaCodeDictionary.put("ORU EAST", "616");
        lgaCodeDictionary.put("ORUK ANAM", "617");
        lgaCodeDictionary.put("ORUMBA NORTH", "618");
        lgaCodeDictionary.put("ORUMBA SOUTH", "	619");
        lgaCodeDictionary.put("ORU WEST", "620");
        lgaCodeDictionary.put("OSE", "621");
        lgaCodeDictionary.put("OSHIMILI NORTH", "622");
        lgaCodeDictionary.put("OSHIMILI", "623");
        lgaCodeDictionary.put("OSHODI/ISOLO", "624");
        lgaCodeDictionary.put("OSISIOMA", "625");
        lgaCodeDictionary.put("OSOGBO", "626");
        lgaCodeDictionary.put("OTUKPO", "627");
        lgaCodeDictionary.put("OVIA NORTH EAST", "628");
        lgaCodeDictionary.put("OVIA SOUTH WEST", "629");
        lgaCodeDictionary.put("OWAN EAST", "630");
        lgaCodeDictionary.put("OWAN WEST", "631");
        lgaCodeDictionary.put("OWERRI MUNICIPAL", "632");
        lgaCodeDictionary.put("OWERRI NORTH", "633");
        lgaCodeDictionary.put("OWERRI WEST", "634");
        lgaCodeDictionary.put("OWO", "635");
        lgaCodeDictionary.put("OYE", "636");
        lgaCodeDictionary.put("OYI", "637");
        lgaCodeDictionary.put("OYIGBO", "638");
        lgaCodeDictionary.put("OYO WEST", "639");
        lgaCodeDictionary.put("OYO EAST", "640");
        lgaCodeDictionary.put("OYUN", "641");
        lgaCodeDictionary.put("PAIKORO", "642");
        lgaCodeDictionary.put("PANKSHIN	", "643");
        lgaCodeDictionary.put("PATANI", "644");
        lgaCodeDictionary.put("PATEGI", "645");
        lgaCodeDictionary.put("PORT HARCOURT", "646");
        lgaCodeDictionary.put("POTISKUM", "647");
        lgaCodeDictionary.put("QUAANPAN", "648");
        lgaCodeDictionary.put("RABAH", "649");
        lgaCodeDictionary.put("RAFI", "650");
        lgaCodeDictionary.put("RANO", "651");
        lgaCodeDictionary.put("REMO NORTH", "652");
        lgaCodeDictionary.put("RIJAU", "653");
        lgaCodeDictionary.put("RIMI", "654");
        lgaCodeDictionary.put("RIMIN GADO", "655");
        lgaCodeDictionary.put("RINGIM", "656");
        lgaCodeDictionary.put("RIYOM", "657");
        lgaCodeDictionary.put("ROGO", "658");
        lgaCodeDictionary.put("RONI", "659");
        lgaCodeDictionary.put("SABON-BIRNI", "660");
        lgaCodeDictionary.put("SABON-GARI", "661");
        lgaCodeDictionary.put("SABUWA", "662");
        lgaCodeDictionary.put("SAFANA", "663");
        lgaCodeDictionary.put("SAGBAMA", "664");
        lgaCodeDictionary.put("SAKABA", "665");
        lgaCodeDictionary.put("SAKI EAST", "666");
        lgaCodeDictionary.put("SAKI WEST", "667");
        lgaCodeDictionary.put("SANDAMU", "668");
        lgaCodeDictionary.put("SANGA", "669");
        lgaCodeDictionary.put("SEPELE", "670");
        lgaCodeDictionary.put("SARDAUNA", "671");
        lgaCodeDictionary.put("SAGAMU", "672");
        lgaCodeDictionary.put("SHAGARI", "673");
        lgaCodeDictionary.put("SHANGA", "674");
        lgaCodeDictionary.put("SHANI", "675");
        lgaCodeDictionary.put("SHANONO", "676");
        lgaCodeDictionary.put("SHELLENG", "677");
        lgaCodeDictionary.put("SHENDAM", "678");
        lgaCodeDictionary.put("SHINKAFI", "679");
        lgaCodeDictionary.put("SHIRA", "680");
        lgaCodeDictionary.put("SHIRORO", "681");
        lgaCodeDictionary.put("SHOMGOM", "682");
        lgaCodeDictionary.put("SHOMOLU", "683");
        lgaCodeDictionary.put("SILAME", "684");
        lgaCodeDictionary.put("SOBA", "685");
        lgaCodeDictionary.put("SOKOTO", "686");
        lgaCodeDictionary.put("SOKOTO SOUTH", "687");
        lgaCodeDictionary.put("SONG", "688");
        lgaCodeDictionary.put("SOUTHERN IJAW", "689");
        lgaCodeDictionary.put("SULEJA", "690");
        lgaCodeDictionary.put("SULE TANKARKAR", "691");
        lgaCodeDictionary.put("SUMAILA", "692");
        lgaCodeDictionary.put("SURU", "693");
        lgaCodeDictionary.put("SURULERE", "694");
        lgaCodeDictionary.put("TAFA", "696");
        lgaCodeDictionary.put("TAFAWA BALEWA", "697");
        lgaCodeDictionary.put("TAI", "698");
        lgaCodeDictionary.put("TAKAI", "699");
        lgaCodeDictionary.put("TAKUM", "700");
        lgaCodeDictionary.put("TALATA-MAFARA", "701");
        lgaCodeDictionary.put("TAMBAWAL", "702");
        lgaCodeDictionary.put("TANGAZA", "703");
        lgaCodeDictionary.put("TARAUNI", "704");
        lgaCodeDictionary.put("TARKA", "705");
        lgaCodeDictionary.put("TARMUA", "706");
        lgaCodeDictionary.put("TAURA", "707");
        lgaCodeDictionary.put("TOUNGO", "708");
        lgaCodeDictionary.put("TOFA", "709");
        lgaCodeDictionary.put("TORO", "710");
        lgaCodeDictionary.put("TOTO", "711");
        lgaCodeDictionary.put("TSAFE", "712");
        lgaCodeDictionary.put("TSANYAWA", "713");
        lgaCodeDictionary.put("TUDUN", "714");
        lgaCodeDictionary.put("TURETA", "715");
        lgaCodeDictionary.put("UDENU", "716");
        lgaCodeDictionary.put("UDI", "717");
        lgaCodeDictionary.put("UDU", "718");
        lgaCodeDictionary.put("UDUNG UKO", "719");
        lgaCodeDictionary.put("UGHELLI NORTH", "720");
        lgaCodeDictionary.put("UGHELLI SOUTH", "721");
        lgaCodeDictionary.put("UGWUNAGBO", "722");
        lgaCodeDictionary.put("UHUNMWONDE", "723");
        lgaCodeDictionary.put("UKANAFUN", "724");
        lgaCodeDictionary.put("UKUM", "725");
        lgaCodeDictionary.put("UKWA EAST", "726");
        lgaCodeDictionary.put("UKWA WEST", "727");
        lgaCodeDictionary.put("UKWUANI", "728");
        lgaCodeDictionary.put("UMUAHIA NORTH", "729");
        lgaCodeDictionary.put("UMUAHIA SOUTH", "730");
        lgaCodeDictionary.put("UMUNNEOCHI", "731");
        lgaCodeDictionary.put("UNGOGO", "732");
        lgaCodeDictionary.put("URUAN", "734");
        lgaCodeDictionary.put("URUE OFFONG ORUKO", "735");
        lgaCodeDictionary.put("USHONGO", "736");
        lgaCodeDictionary.put("USSA", "737");
        lgaCodeDictionary.put("UVWIE", "738");
        lgaCodeDictionary.put("UYO", "739");
        lgaCodeDictionary.put("UZOUWANI", "740");
        lgaCodeDictionary.put("VANDEIKYA", "741");
        lgaCodeDictionary.put("WAMAKKO", "742");
        lgaCodeDictionary.put("WAMBA", "743");
        lgaCodeDictionary.put("WARAWA", "744");
        lgaCodeDictionary.put("WARJI", "745");
        lgaCodeDictionary.put("WARRI NORTH", "746");
        lgaCodeDictionary.put("WARRI SOUTH", "747");
        lgaCodeDictionary.put("WARRI SOUTH WEST", "748");
        lgaCodeDictionary.put("WASAGU/DANKO", "749");
        lgaCodeDictionary.put("WASE", "750");
        lgaCodeDictionary.put("WUDIL", "751");
        lgaCodeDictionary.put("WUKARI", "752");
        lgaCodeDictionary.put("WURNO", "753");
        lgaCodeDictionary.put("WUSHISHI", "754");
        lgaCodeDictionary.put("YABO", "75");
        lgaCodeDictionary.put("YAGBA EAST", "756");
        lgaCodeDictionary.put("YAGBA WEST", "757");
        lgaCodeDictionary.put("YAKURR", "758");
        lgaCodeDictionary.put("YALA", "759");
        lgaCodeDictionary.put("YAMALTU/DEBA", "760");
        lgaCodeDictionary.put("YANKWASHI", "761");
        lgaCodeDictionary.put("YAURI", "762");
        lgaCodeDictionary.put("YENAGOA", "763");
        lgaCodeDictionary.put("YOLA NORTH", "764");
        lgaCodeDictionary.put("YOLA SOUTH", "765");
        lgaCodeDictionary.put("YUNUSARI", "767");
        lgaCodeDictionary.put("YUSUFARI", "768");
        lgaCodeDictionary.put("ZAKI", "769");
        lgaCodeDictionary.put("ZANGO", "770");
        lgaCodeDictionary.put("ZANGO KATAF", "771");
        lgaCodeDictionary.put("ZARIA", "772");
        lgaCodeDictionary.put("ZING", "773");
        lgaCodeDictionary.put("ZURMI", "774");
        lgaCodeDictionary.put("ZURU", "775");

    }

    public void loadLabTestDictionary() {
        labTestDictionary = new HashMap<Integer, String>();

        labTestDictionary.put(165923, "2");//ALT/SGPT
        //labTestDictionary.put(1529, "3");OpenMRS
        //labTestDictionary.put(1531, "8");OpenMRS
        labTestDictionary.put(654, "4");//AST
        labTestDictionary.put(655, "7");//T.Bilirubin
        labTestDictionary.put(5497, "11");//CD4 COUNT
        labTestDictionary.put(1319, "12");//LYMPHOCYTES
        labTestDictionary.put(1022, "13");//Neutrophils
        labTestDictionary.put(1134, "16");//Serum Chloride
        labTestDictionary.put(1006, "17");//Total Cholesterol
        labTestDictionary.put(1007, "18");//HDL
        labTestDictionary.put(1008, "19");//LDL
        labTestDictionary.put(164364, "21");//Creatinine
        labTestDictionary.put(160053, "31");//Serum Glucose
        //labTestDictionary.put(805, "32");//Urine glucose dipstick
        labTestDictionary.put(165920, "34");//HB/PCV
        labTestDictionary.put(165395, "35");//HB (PCV)%
        //labTestDictionary.put(391, "35");//HIV Test
        labTestDictionary.put(159430, "42");//Hepatitis B Test - Qualitative
        labTestDictionary.put(1325, "43");//Hepatitis C Test - Qualitative
        //labTestDictionary.put(338, "53"); //HIV DNA Polymerase Chain Reaction, Qualitative
        labTestDictionary.put(785, "54");//Alk.Phosphatase
        labTestDictionary.put(1133, "57");//Serum Potassium
        labTestDictionary.put(45, "58");//Urine Pregnancy Test
        labTestDictionary.put(165926, "59");//Total Protein
        labTestDictionary.put(1132, "65");//Soduim
        labTestDictionary.put(361, "71");//RPR
        labTestDictionary.put(1009, "74");//Triglycerides
        labTestDictionary.put(856, "80");//Viral Load
        labTestDictionary.put(678, "81");//WBC
        //labTestDictionary.put(391, "44");//HIV Test
        // labTestDictionary.put(451, "31");

    }

    public void loadHIVEncounterDictionary() {
        hivEncounterTypeDictionary = new HashMap<Integer, String>();

        /* EDD AND PMTCT LINK    */
        hivEncounterTypeDictionary.put(165048, "P");
        hivEncounterTypeDictionary.put(165049, "PMTCT");
        hivEncounterTypeDictionary.put(165047, "NP");
        //hivEncounterTypeDictionary.put(13, "NK");

        /*  FAMILY PLANNING CODE      */
        hivEncounterTypeDictionary.put(1, "FP");// True
        hivEncounterTypeDictionary.put(0, "NOFP");// False

        /*  FAMILY PLANNING METHOD */
        hivEncounterTypeDictionary.put(190, "FP1");// Condoms
        hivEncounterTypeDictionary.put(311, "FP2");// Oral Contraceptives pills
        hivEncounterTypeDictionary.put(405, "FP3");// Injectable/Implantable
        //hivEncounterTypeDictionary.put(408, "FP4");// Cervical Cap
        hivEncounterTypeDictionary.put(5279, "FP5"); // Intrauterine device
        hivEncounterTypeDictionary.put(1489, "FP6");// Vasectomy
        hivEncounterTypeDictionary.put(5622, "FP7");// Others

        /* FUNCTIONAL STATUS */
        hivEncounterTypeDictionary.put(162750, "W");
        hivEncounterTypeDictionary.put(160026, "A");
        hivEncounterTypeDictionary.put(162752, "B");
        /* DEVELOPMENTAL STATUS */
        //hivEncounterTypeDictionary.put(1114, "W");
        //hivEncounterTypeDictionary.put(1115, "A");
        //hivEncounterTypeDictionary.put(1089, "B");

        /* WHO STAGING */
        hivEncounterTypeDictionary.put(1204, "1");
        hivEncounterTypeDictionary.put(1205, "2");
        hivEncounterTypeDictionary.put(1206, "3");
        hivEncounterTypeDictionary.put(1207, "4");

        /* TB STATUS */
        hivEncounterTypeDictionary.put(1660, "1");//No sign or symptoms of disease
        hivEncounterTypeDictionary.put(142177, "2");//Disease suspected
        hivEncounterTypeDictionary.put(166042, "3");//Currently on INH Prophylaxis
        hivEncounterTypeDictionary.put(1662, "4");// On treatment for disease
        hivEncounterTypeDictionary.put(1661, "5");// TB Positive not on drugs

        /* OTHER OI PROBLEMS */
        hivEncounterTypeDictionary.put(117543, "1");//Herpes Zoster
        hivEncounterTypeDictionary.put(114100, "2");//Pneumonia
        hivEncounterTypeDictionary.put(119566, "3");//Dementia
        hivEncounterTypeDictionary.put(5340, "4");//Thrush
        hivEncounterTypeDictionary.put(140238, "5");//Fever
        hivEncounterTypeDictionary.put(143264, "6");//Cough

        /* NOTED SIDE EFFECTS */
        hivEncounterTypeDictionary.put(133473, "1");//Nausea and Vomiting
        hivEncounterTypeDictionary.put(139084, "2");//Headache/confusion
        hivEncounterTypeDictionary.put(116743, "3");//Insomnia/Bad dreams
        hivEncounterTypeDictionary.put(5226, "4");//Weakness
        hivEncounterTypeDictionary.put(165767, "5");//Bleeding
        hivEncounterTypeDictionary.put(512, "6");//Rash
        hivEncounterTypeDictionary.put(165052, "7");//Fat accumulation or loss
        hivEncounterTypeDictionary.put(121629, "8");// Anemia
        hivEncounterTypeDictionary.put(165053, "9");//Drainage of liquor
        hivEncounterTypeDictionary.put(125886, "10");// Steven Johnson syndrome
        hivEncounterTypeDictionary.put(138291, "11");// Hyperglycemia

        /* DRUG ADHERENCE */
        hivEncounterTypeDictionary.put(165287, "G");
        hivEncounterTypeDictionary.put(165289, "F");
        hivEncounterTypeDictionary.put(165288, "P");

        /* WHY POOR FAIR DRUG ADHERENCE */
 /*hivEncounterTypeDictionary.put(899, "1");
        hivEncounterTypeDictionary.put(1414, "2");
        hivEncounterTypeDictionary.put(1415, "3");
        hivEncounterTypeDictionary.put(1416, "4");
        hivEncounterTypeDictionary.put(1417, "5");
        hivEncounterTypeDictionary.put(983, "6");
        hivEncounterTypeDictionary.put(7777775, "7");
        hivEncounterTypeDictionary.put(44, "8");
        hivEncounterTypeDictionary.put(48, "9");
        hivEncounterTypeDictionary.put(900, "10");
        hivEncounterTypeDictionary.put(1419, "11");
        hivEncounterTypeDictionary.put(1421, "12");
        hivEncounterTypeDictionary.put(1422, "13");
        hivEncounterTypeDictionary.put(1423, "14");
        hivEncounterTypeDictionary.put(1425, "15");*/
        // code 16 was not availaible on the care card (VERIFY!)
        //hivEncounterTypeDictionary.put(1424, "17");

        /* COTRIM DOSE */
 /*hivEncounterTypeDictionary.put(1552, "CTX960");
        hivEncounterTypeDictionary.put(1547, "CTX480");
        hivEncounterTypeDictionary.put(7778548, "CTX240");// Mapped to Cotrim dispersible 120mg*/

 /* INH DOSE */
 /*hivEncounterTypeDictionary.put(1546, "H"); // Verify
        hivEncounterTypeDictionary.put(1543, "H"); // Verify*/

 /* DRUG NAME */
 /* hivEncounterTypeDictionary.put(1594, "H");
        hivEncounterTypeDictionary.put(35, "H");*/
    }

    public void loadLgaCodeDictionary() {
        lgaCodeDictionary = new HashMap<String, String>();

    }

    public void loadStateCodeDictinary() {
        stateCodeDictinary = new HashMap<String, String>();
        stateCodeDictinary.put("ADAMAWA", "2");
        stateCodeDictinary.put("ABIA", "1");
        stateCodeDictinary.put("AKWA IBOM", "3");
        stateCodeDictinary.put("ANAMBRA", "4");
        stateCodeDictinary.put("BAUCHI", "5");
        stateCodeDictinary.put("BAYELSA", "6");
        stateCodeDictinary.put("BENUE", "7");
        stateCodeDictinary.put("BORNO", "8");
        stateCodeDictinary.put("CROSS RIVER", "9");
        stateCodeDictinary.put("DELTA", "10");
        stateCodeDictinary.put("EBONYI", "11");
        stateCodeDictinary.put("EDO", "12");
        stateCodeDictinary.put("EKITI", "13");
        stateCodeDictinary.put("ENUGU", "14");
        stateCodeDictinary.put("FCT", "15");
        stateCodeDictinary.put("GOMBE", "16");
        stateCodeDictinary.put("IMO", "17");
        stateCodeDictinary.put("JIGAWA", "18");
        stateCodeDictinary.put("KADUNA", "19");
        stateCodeDictinary.put("KANO", "20");
        stateCodeDictinary.put("KATSINA", "21");
        stateCodeDictinary.put("KEBBI", "22");
        stateCodeDictinary.put("KOGI", "23");
        stateCodeDictinary.put("KWARA", "24");
        stateCodeDictinary.put("LAGOS", "25");
        stateCodeDictinary.put("NASARAWA", "26");
        stateCodeDictinary.put("NIGER", "27");
        stateCodeDictinary.put("OGUN", "28");
        stateCodeDictinary.put("ONDO", "29");
        stateCodeDictinary.put("OSUN", "30");
        stateCodeDictinary.put("OYO", "31");
        stateCodeDictinary.put("PLATEAU", "32");
        stateCodeDictinary.put("RIVERS", "33");
        stateCodeDictinary.put("SOKOTO", "34");
        stateCodeDictinary.put("TARABA", "35");
        stateCodeDictinary.put("YOBE", "36");
        stateCodeDictinary.put("ZAMFARA", "37");

    }

    public void loadPatientDemographicDictionary() {
        patientDemographicDictionary = new HashMap<Integer, String>();
        /*patientDemographicDictionary.put(1080, "eng");
        patientDemographicDictionary.put(1081, "hau");
        patientDemographicDictionary.put(1082, "yor");
        patientDemographicDictionary.put(1519, "ibo");*/
        patientDemographicDictionary.put(1107, "1"); // None
        patientDemographicDictionary.put(1713, "2");// Completed Primary
        patientDemographicDictionary.put(1714, "3");// Secondary School Education
        patientDemographicDictionary.put(5622, "4");// Qur'anic
        patientDemographicDictionary.put(160292, "6");// Post Secondary
        patientDemographicDictionary.put(1540, "EMP");// Employed
        patientDemographicDictionary.put(159461, "RET");// Retired
        patientDemographicDictionary.put(159465, "STU");// Student
        patientDemographicDictionary.put(5622, "NA");// 5622
        patientDemographicDictionary.put(5555, "M");// Married
        patientDemographicDictionary.put(1060, "G");// Living With Partner
        patientDemographicDictionary.put(1057, "S");// Single
        patientDemographicDictionary.put(1058, "D");// Divorced
        patientDemographicDictionary.put(1059, "W");// Widowed
        patientDemographicDictionary.put(1056, "A");// Separated

    }

    public void loadHIVQuestionMapDictionary() {
        hivQueDictionary = new HashMap<Integer, String>();
        hivQueDictionary.put(160546, "1");//STI OUTPATIENT
        hivQueDictionary.put(160542, "2");//GOPD
        hivQueDictionary.put(160539, "3");//VCT
        hivQueDictionary.put(160543, "4");//CBO
        hivQueDictionary.put(160547, "5");//Private/Commercial Health Facility
        hivQueDictionary.put(160541, "6");//TB Outpatient
        hivQueDictionary.put(160536, "7");//Ward
        hivQueDictionary.put(5622, "8");//Casualty
        hivQueDictionary.put(160538, "9");//ANC/PMTCT
        hivQueDictionary.put(160548, "10");//IDU
        hivQueDictionary.put(160550, "11");//Sex Worker Outreach
        hivQueDictionary.put(160536, "12");//Current Clinic Patient
        hivQueDictionary.put(160551, "13");//Self Referral
        hivQueDictionary.put(160563, "14");//Pre-ART Transfer in
        hivQueDictionary.put(164949, "HIVAb");//HIV-Ab
        hivQueDictionary.put(164948, "HIVPCR");//PCR
        hivQueDictionary.put(165238, "T");//Transfered in with records
        hivQueDictionary.put(165239, "NT");//Earlier ARV but not transfered in
        hivQueDictionary.put(165240, "P");//PMTCT Only
        hivQueDictionary.put(1107, "N");//Has never received ARVs 
        /*    Reason Medically Eligible    */
 /* hivQueDictionary.put(1730, "1");
         hivQueDictionary.put(88, "2");
         hivQueDictionary.put(1153, "3");
         hivQueDictionary.put(1717, "4"); */

 /* Clinical Stage at start of ART */
 /* hivQueDictionary.put(7777925, "1");
          hivQueDictionary.put(7777926, "2");
          hivQueDictionary.put(7777927, "3");
          hivQueDictionary.put(7777928, "4");  */
 /* Functional status at ART start */
 /*hivQueDictionary.put(1009, "W");
        hivQueDictionary.put(1010, "A");
        hivQueDictionary.put(1012, "B");*/
    }

    public void loadNDRToOMRSDictionary() {
        loadRegimenToCodeDictionary();
        loadHIVQuestionMapDictionary();
        loadPatientDemographicDictionary();
        loadStateCodeDictinary();
        loadLabTestDictionary();
        loadHIVEncounterDictionary();
        loadLGAMap();
        loadLocationMap();
        loadLocalCodingDictionary();
        loadLabTestUnitDictionary();
        loadLabTestUnitDescription();
    }

    public void loadLocalCodingDictionary() {

        localCodeMapping = new HashMap<String, String>();

        localCodeMapping.put("ACTION MEAL PRESCRIBED", "ACM");
        localCodeMapping.put("ACYCLOVIR", "ACY");
        localCodeMapping.put("ALBENDAZOLE", "ALB");
        localCodeMapping.put("AMOXICILLIN", "AMX");
        localCodeMapping.put("AMOXICILLIN AND CLAVULANIC ACID", "AMX");
        localCodeMapping.put("AMITRIPTYLINE", "AMT");
        localCodeMapping.put("ARTEMETER", "ATM");
        localCodeMapping.put("ARTEMETHER-LUMEFANTRINE", "ALU");
        localCodeMapping.put("AZITHROMYCIN", "AZI");
        localCodeMapping.put("PENICILLIN G, BENZATHINE", "BEP");
        localCodeMapping.put("PENICILLIN G BENZATHINE / PENICILLIN G PROCAINE", "BEP");//
        localCodeMapping.put("BENZOIC ACID/SALICYLIC ACID CREAM", "BES");
        localCodeMapping.put("CEFTRIAXONE IV", "CFT");
        localCodeMapping.put("CEFTRIAXONE", "CFT");//
        localCodeMapping.put("CHLORPHENIRAMINE MALEATE", "CHP");
        localCodeMapping.put("CIPROFLOXACIN", "CPF");//
        localCodeMapping.put("CLOTRIMAZOLE VAG. PRESSARY", "CLX");
        localCodeMapping.put("CTX", "CTX960");
        localCodeMapping.put("CTX prophylaxis", "CTX960");
        //localCodeMapping.put("CTX", "CTX240");
        //localCodeMapping.put("Cotrimoxazole (TMP/SMX) Tablet", "CTX960");
        localCodeMapping.put("DAPSONE", "DDS");// DOXYCYCLINE
        localCodeMapping.put("DOXYCYCLINE", "DXY");// 
        localCodeMapping.put("ETHAMBUTOL", "E");// 
        localCodeMapping.put("Erythromycin", "ERN");
        localCodeMapping.put("FANSIDAR", "FAN");
        localCodeMapping.put("FERROUS GLUCONATE", "FEG");
        localCodeMapping.put("FERROUS SULPHATE", "FES");
        localCodeMapping.put("FERROUS SULFATE", "FES");
        localCodeMapping.put("FLUCONAZOLE", "FLUC");//
        localCodeMapping.put("FLUOXETINE", "FLUX");
        localCodeMapping.put("FOLIC ACID", "FOA");
        localCodeMapping.put("HYDROCORTISONE ACETATE", "HYA");
        localCodeMapping.put("HYDROCORTISONE", "HYA");
        localCodeMapping.put("IBUPROFEN", "IBP");
        localCodeMapping.put("INH", "H");
        localCodeMapping.put("INH/B6", "H");//ISONIAZID PROPHYLAXIS
        localCodeMapping.put("ISONIAZID PROPHYLAXIS", "H");//
        localCodeMapping.put("LOPERAMIDE", "LOP");
        //localCodeMapping.put("LORATADINE", "LRS");
        localCodeMapping.put("LORATADINE", "LOR");
        localCodeMapping.put("METOCLOPRAMIDE", "MEP");
        localCodeMapping.put("METRONIDAZOLE", "MET");
        localCodeMapping.put("MULTIVITAMIN", "MUV");
        // localCodeMapping.put("Multivitamin Drops", "MUV");
        //localCodeMapping.put("Multivitamin Syrup", "MUV");
        // localCodeMapping.put("Multivite Tab", "MUV");
        localCodeMapping.put("NYSTATIN SOLUTION", "NYS");
        localCodeMapping.put("NYSTATIN", "NYS");//
        localCodeMapping.put("ORAL REHYDRATION SALT", "ORS");
        localCodeMapping.put("Oral Nystatin", "ORN");//
        localCodeMapping.put("OFLOXACIN", "OFL");//
        localCodeMapping.put("PROMETHAZINE / PSEUDOEPHEDRINE", "PRM");
        localCodeMapping.put("PROMETHAZINE", "PRM");
        //localCodeMapping.put("Pyridoxine (Vitamin B6)", "PYR");
        localCodeMapping.put("PYRIZINAMIDE", "Z");
        localCodeMapping.put("RIFAMPICIN", "R");
        localCodeMapping.put("SULFADOXINE/PYRIMETHAMINE", "SXP");
        localCodeMapping.put("SULFADOXINE AND PYRIMETHAMINE", "SXP");
        localCodeMapping.put("SPECTINOMYCIN", "S");//
        localCodeMapping.put("STREPTOMYCIN INJECTION", "SI");
        localCodeMapping.put("TRAMADOL", "TMD");
        localCodeMapping.put("TRAMADOL HYDROCHLORIDE", "TMD");
        localCodeMapping.put("TINIDAZOLE", "TND");
        localCodeMapping.put("TETRACYCLINE", "TCL");
        localCodeMapping.put("RIFAMPICIN AND ISONIAZID", "HR");
        localCodeMapping.put("RIFAMPICIN ISONIAZID PYRAZINAMIDE AND ETHAMBUTOL ", "HRZE");

    }

    public String getKeyOfCTXDrugValue(String drugName) {
        String ctxName = "";
        Set<String> keySet = localCodeMapping.keySet();
        String value = "";
        for (String ele : keySet) {
            value = localCodeMapping.get(ele);
            if (drugName.contains(ele)) {
                ctxName = ele;
            }
        }
        return ctxName;
    }

    public String getLocalCodingOtherDrugs(String drugName) {
        String coding = "";
        if (drugName != null && !drugName.isEmpty()) {
            Set<String> keySet = localCodeMapping.keySet();
            for (String key : keySet) {
                if (drugName.contains(key) && !localCodeMapping.get(key).contains("CTX")) {
                    coding += localCodeMapping.get(key) + "-";
                }
            }
            if (coding.endsWith("-")) {
                coding = coding.substring(0, coding.length() - 1);
            }
            String[] codingArray = coding.split("-");
            Arrays.sort(codingArray);
            coding = StringUtils.join(codingArray, "-");
        }
        return coding;
    }

    public boolean isValidOIDrug(String drugName) {
        boolean ans = false;
        if (localCodeMapping.containsKey(drugName)) {
            ans = true;
        }
        return ans;
    }

    public String getKeyOfOtherDrugValue(String drugNames) {
        String otherDrugNames = "";
        Set<String> keySet = localCodeMapping.keySet();
        String value = "";
        for (String ele : keySet) {
            //value=localCodeMapping.get(ele);

            if (drugNames.contains(ele) && !ele.contains("CTX")) {
                otherDrugNames += ele + "/";
            }
            if (otherDrugNames.endsWith("/")) {
                otherDrugNames = otherDrugNames.substring(0, otherDrugNames.length() - 1);
            }
        }
        return otherDrugNames;
    }

    public String getLocalCTXCoding(String drugName) {
        String coding = "";
        if (drugName != null && !drugName.isEmpty()) {
            Set<String> keySet = localCodeMapping.keySet();
            for (String key : keySet) {
                if (drugName.contains(key) && key.contains("CTX")) {
                    coding = localCodeMapping.get(key);
                }
            }

        }
        return coding;
    }

    public void loadRegimenToCodeDictionary() {
        mapRegimenToCodeDictionary = new HashMap<String, String>();
        mapRegimenToCodeDictionary.put("3TC-D4T-EFV", "1k");
        mapRegimenToCodeDictionary.put("TDF-3TC-DTG", "1m");
        mapRegimenToCodeDictionary.put("TDF-FTC-DTG", "1n");
        mapRegimenToCodeDictionary.put("ABC-3TC-DTG", "1o");
        mapRegimenToCodeDictionary.put("ABC-FTC-DTG", "1p");
        mapRegimenToCodeDictionary.put("Other first line", "1k");
        mapRegimenToCodeDictionary.put("AZT-3TC-EFV", "1a");
        mapRegimenToCodeDictionary.put("AZT-3TC-NVP", "1b");
        mapRegimenToCodeDictionary.put("TDF-FTC-EFV", "1c");
        mapRegimenToCodeDictionary.put("TDF-FTC-NVP", "1d");
        mapRegimenToCodeDictionary.put("TDF-3TC-EFV", "1e");
        mapRegimenToCodeDictionary.put("TDF-3TC-NVP", "1f");
        mapRegimenToCodeDictionary.put("AZT-3TC-ABC", "1g");
        mapRegimenToCodeDictionary.put("AZT-3TC-TDF", "1h");
        mapRegimenToCodeDictionary.put("TDF-FTC-LPV/r", "2a");
        mapRegimenToCodeDictionary.put("TDF-3TC-LPV/r", "2b");
        mapRegimenToCodeDictionary.put("TDF-FTC-ATV/r", "2c");
        mapRegimenToCodeDictionary.put("TDF-3TC-ATV/r", "2d");
        mapRegimenToCodeDictionary.put("AZT-3TC-LPV/r", "2e");
        mapRegimenToCodeDictionary.put("AZT-3TC-ATV/r", "2f");
        mapRegimenToCodeDictionary.put("AZT-3TC-EFV", "4a");
        mapRegimenToCodeDictionary.put("AZT-3TC-NVP", "4b");
        mapRegimenToCodeDictionary.put("ABC-3TC-EFV", "4c");
        mapRegimenToCodeDictionary.put("ABC-3TC-NVP", "4d");
        mapRegimenToCodeDictionary.put("AZT-3TC-ABC", "4e");
        mapRegimenToCodeDictionary.put("d4T-3TC-NVP", "4f");
        mapRegimenToCodeDictionary.put("ABC-3TC-LPV/r", "5a");
        mapRegimenToCodeDictionary.put("AZT-3TC-LPV/r", "5b");
        mapRegimenToCodeDictionary.put("d4T-3TC-LPV/r", "5c");
        mapRegimenToCodeDictionary.put("ABC-3TC-ddi", "5e");
        mapRegimenToCodeDictionary.put("AZT", "9a");
        mapRegimenToCodeDictionary.put("3TC", "9b");
        mapRegimenToCodeDictionary.put("NVP", "9c");
        mapRegimenToCodeDictionary.put("AZT-3TC", "9d");
        mapRegimenToCodeDictionary.put("AZT-NVP", "9e");
        mapRegimenToCodeDictionary.put("FTC-TDF", "9f");
        mapRegimenToCodeDictionary.put("3TC-d4T", "9g");
        mapRegimenToCodeDictionary.put("3TC-d4T", "9h");
        mapRegimenToCodeDictionary.put("AZT-FTC-LPV/r-TDF", "2g");
        mapRegimenToCodeDictionary.put("3TC-AZT-LPV/r-TDF", "2g");
        mapRegimenToCodeDictionary.put("IDV/r-AZT-3TC", "2g");
        mapRegimenToCodeDictionary.put("IDV/r-AZT-FTC", "2g");
        mapRegimenToCodeDictionary.put("IDV/r-AZT-3TC", "2g");
        mapRegimenToCodeDictionary.put("IDV/r-3TC-D4T", "2g");
        mapRegimenToCodeDictionary.put("AZT-FTC-ATV/r", "2g");
        mapRegimenToCodeDictionary.put("AZT-FTC-LPV/r", "2g");
        mapRegimenToCodeDictionary.put("ABC-AZT-3TC-LPV/r", "2g");
        mapRegimenToCodeDictionary.put("3TC-ABC-AZT-ATV/r", "2g");
        mapRegimenToCodeDictionary.put("ATV/r-AZT-3TC-TDF", "2g");
        mapRegimenToCodeDictionary.put("ATV/r-AZT-FTC-TDF", "2g");
        mapRegimenToCodeDictionary.put("3TC-ABC-ATV/r", "2g");
        mapRegimenToCodeDictionary.put("3TC-D4T-SQV/r", "2g");
        mapRegimenToCodeDictionary.put("ABC-SQV/r-TDF", "2g");
        mapRegimenToCodeDictionary.put("ABC-DDI-LPV/r", "2g");
        mapRegimenToCodeDictionary.put("TDF-FTC-SQV/r", "2g");
        mapRegimenToCodeDictionary.put("3TC-AZT-SQV/r", "2g");
        mapRegimenToCodeDictionary.put("3TC-IDV/r-TDF", "2g");
        mapRegimenToCodeDictionary.put("FTC-IDV/r-TDF", "2g");
        mapRegimenToCodeDictionary.put("3TC-AZT-SQV/r-TDF", "2g");
        mapRegimenToCodeDictionary.put("3TC-SQV/r-TDF", "2g");
        mapRegimenToCodeDictionary.put("ABC-AZT-LPV/r", "2g");
        mapRegimenToCodeDictionary.put("DDI-IDV/r-TDF", "2g");
        mapRegimenToCodeDictionary.put("DDI-SQV/r-TDF", "2g");
        mapRegimenToCodeDictionary.put("Other second line", "2g");

    }

    public FacilityType createFacilityType(String name) {
        FacilityType ft = new FacilityType();
        ft.setFacilityName(name);
        ft.setFacilityID(name.toUpperCase());
        ft.setFacilityTypeCode("FAC");
        return ft;
    }

    public static boolean isEquivalent(String openmrsRegimen, String careCardRegimen) {
        boolean ans = false;
        String[] arr1 = null, arr2 = null;
        if (openmrsRegimen != null && careCardRegimen != null) {
            arr1 = openmrsRegimen.toUpperCase().split("-");
            arr2 = careCardRegimen.toUpperCase().split("-");
            Arrays.sort(arr1);
            Arrays.sort(arr2);
        }
        return Arrays.equals(arr1, arr2);
    }

    public String getCareCardRegimen(String openmrsRegimen) {
        String regimen = null;
        Set<String> set = mapRegimenToCodeDictionary.keySet();
        for (String ele : set) {
            if (isEquivalent(openmrsRegimen, ele)) {
                regimen = ele;
                return regimen;
            }
        }
        return regimen;
    }

    public String getRegimenCode(String regimen) {
        String code = null;
        Set<String> set = mapRegimenToCodeDictionary.keySet();
        for (String ele : set) {
            if (isEquivalent(regimen, ele)) {
                code = mapRegimenToCodeDictionary.get(ele);
                return code;
            }
        }
        return code;
    }

    public String getRegimenCode(String regimen, int regimenLineConcept) {
        String code = null;
        int[] firstLineArr = {164506, 164507};
        int[] secondLineArr = {164513, 164514};
        Arrays.sort(firstLineArr);
        Arrays.sort(secondLineArr);

        Set<String> set = mapRegimenToCodeDictionary.keySet();
        for (String ele : set) {
            if (isEquivalent(regimen, ele)) {
                code = mapRegimenToCodeDictionary.get(ele);
                return code;
            }
        }
        if (StringUtils.isEmpty(code) && (Arrays.binarySearch(firstLineArr, regimenLineConcept) >= 0)) {
            code = "1k";
        }
        if (StringUtils.isEmpty(code) && (Arrays.binarySearch(secondLineArr, regimenLineConcept) >= 0)) {
            code = "2g";
        }
        return code;
    }

    public Container createContainer(String messageStatus, String messageID, String schemaVersion) throws DatatypeConfigurationException {
        container = new Container();

        //Set the Header Information
        @SuppressWarnings("UnusedAssignment")
        MessageHeaderType header = new MessageHeaderType();

        header.setMessageCreationDateTime(getXmlDateTime(new Date()));
        header.setMessageStatusCode(messageStatus);
        header.setMessageSchemaVersion(new BigDecimal(schemaVersion));
        header.setMessageUniqueID(messageID);

        //Set the Sending Organization in the Header
        //In this scenario we are using a fictional IP
        sendingOrganization = new FacilityType();
        sendingOrganization.setFacilityName("IHVN");
        sendingOrganization.setFacilityID("IHVN001");
        sendingOrganization.setFacilityTypeCode("IP");
        header.setMessageSendingOrganization(sendingOrganization);

        //Set the Header to the Container
        container.setMessageHeader(header);
        return container;

    }

    public IndividualReportType createIndividualReport() {
        indRpt = new IndividualReportType();
        return indRpt;
    }

    public PatientDemographicsType createPatientDemographics(Demographics patient, Location loc, List<Obs> obsList, LocationMap locMap) throws DatatypeConfigurationException {
        PatientDemographicsType demo = new PatientDemographicsType();
        demo.setPatientIdentifier(patient.getPepfarID());
        IdentifierType idt = null;
        IdentifiersType idtss = new IdentifiersType();
        String hospID = loc.getDatimID() + "-" + patient.getHospID();
        String otherID = patient.getOtherID();
        String pepfarID = loc.getDatimID() + "-" + patient.getPepfarID();
        if (StringUtils.isNotEmpty(hospID)) {
            idt = new IdentifierType();
            idt.setIDNumber(hospID);
            idt.setIDTypeCode("PI");
            idtss.getIdentifier().add(idt);
        }
        if (StringUtils.isNotEmpty(otherID)) {
            idt = new IdentifierType();
            idt.setIDNumber(otherID);
            idt.setIDTypeCode("PE");
            idtss.getIdentifier().add(idt);
        }
        if (StringUtils.isNoneBlank(pepfarID)) {
            idt = new IdentifierType();
            idt.setIDNumber(pepfarID.toUpperCase());
            idt.setIDTypeCode("PN");
            idtss.getIdentifier().add(idt);
        }
        demo.setOtherPatientIdentifiers(idtss);
        FacilityType treatmentFacility = new FacilityType();
        //treatmentFacility.setFacilityID(String.valueOf(loc.getLocationID()));
        //treatmentFacility.setFacilityID(locationMap.get(StringUtils.trim(loc.getUuid())));
        //treatmentFacility.setFacilityID(locRefIDMap.get(StringUtils.trim(StringUtils.upperCase(loc.getLocationName()))));
        //treatmentFacility.setFacilityID(locMap.getNDRID(loc));//StringUtils.trim(StringUtils.upperCase(loc.getLocationName()))));
        treatmentFacility.setFacilityID(loc.getDatimID());
        //treatmentFacility.setFacilityName(loc.getLocationName());
        treatmentFacility.setFacilityName(loc.getDefaultLoacation());
        treatmentFacility.setFacilityTypeCode("FAC");
        demo.setTreatmentFacility(treatmentFacility);
        String gender = patient.getGender();
        if (gender.equals("M") || gender.equalsIgnoreCase("Male")) {
            demo.setPatientSexCode("M");
        } else if (gender.equals("F") || gender.equalsIgnoreCase("Female")) {
            demo.setPatientSexCode("F");
        }
        demo.setPatientDateOfBirth(getXmlDate(patient.getDateOfBirth()));
        String stateOfOrigin = patient.getAddress_state();
        //if (stateOfOrigin != null) {
        //demo.setStateOfNigeriaOriginCode(stateCodeDictinary.get(stateOfOrigin.toUpperCase()));
        //}
        int conceptID = 0;
        int value_coded = 0;
        String value_text = "";
        double value_numeric = 0.0;

        for (Obs obs : obsList) {
            conceptID = obs.getConceptID();
            switch (conceptID) {
                case 165470://977
                    value_coded = obs.getValueCoded();
                    if (value_coded == 165889) {
                        demo.setPatientDeceasedIndicator(true);
                        demo.setPatientDeceasedDate(getXmlDate(obs.getVisitDate()));
                    } else {
                        demo.setPatientDeceasedIndicator(false);
                    }
                    break;
                /*case 1083:
                    value_coded = obs.getValueCoded();
                    demo.setPatientPrimaryLanguageCode(patientDemographicDictionary.get(value_coded));
                    break;*/
                case 1712://1079
                    value_coded = obs.getValueCoded();
                    if (value_coded != 789) {
                        demo.setPatientEducationLevelCode(patientDemographicDictionary.get(value_coded));
                    }
                    break;
                case 1542://915
                    value_coded = obs.getValueCoded();
                    demo.setPatientOccupationCode(patientDemographicDictionary.get(value_coded));
                    break;
                case 1054://352
                    value_coded = obs.getValueCoded();
                    if (value_coded == 789) {
                        demo.setPatientMaritalStatusCode("T");
                    } else {
                        demo.setPatientMaritalStatusCode(patientDemographicDictionary.get(value_coded));
                    }
                    break;
            }
        }
        return demo;
    }

    public ProgramAreaType createProgramAreaType(String paCode) {
        pa = new ProgramAreaType();
        pa.setProgramAreaCode(paCode);
        return pa;
    }

    public static boolean isValidDate(Date dt) {
        boolean ans = true;
        if (dt == null) {
            ans = false;
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setLenient(false);
        Date date = null;
        if (dt != null) {
            try {
                date = df.parse(df.format(dt));
            } catch (ParseException ex) {
                // ex.printStackTrace();
                ans = false;
            }
        }
        return ans;
    }

    public ConditionType createConditionType(String conditionCode) {
        condition = new ConditionType();
        condition.setConditionCode(conditionCode);
        return condition;
    }

    //public HIVEncounterType createHIVEncounter(int encounter_id, Date artStartDate, ArrayList<Obs> obsList, ArrayList<DrugOrder> orders, ArrayList<Obs> obsCd4List) throws DatatypeConfigurationException {
    public HIVEncounterType createHIVEncounter(Visit visit, Date artStartDate, List<Obs> obsList, ArrayList<DrugOrder> orders, ArrayList<Drugs> drugList) throws DatatypeConfigurationException {
        HIVEncounterType hivEncType = new HIVEncounterType();
        Date visitDate = visit.getVisitDate();
        LocalDate d2 = new LocalDate(new DateTime(visitDate));
        LocalDate d1 = new LocalDate(new DateTime(artStartDate));
        int monthOnART = 0;
        if (artStartDate != null && (d2.isAfter(d1) || d2.isEqual(d1))) {
            monthOnART = Months.monthsBetween(d1, d2).getMonths();
            hivEncType.setDurationOnArt(monthOnART);
        }

        hivEncType.setVisitDate(getXmlDate(visitDate));
        hivEncType.setVisitID(visit.getVisitID());

        int conceptID = 0;
        int value_numeric = 0;
        String value_text = "";
        Date value_datetime = null;
        String code = "";
        String description = "";
        String regimen = "";
        CodedSimpleType cst = null;
        int value_coded = 0;
        if (!obsList.isEmpty()) {
            String signsAndSymptoms = extractConceptCodes(160170, obsList, hivEncounterTypeDictionary);//528
            if (StringUtils.isNoneEmpty(signsAndSymptoms)) {
                hivEncType.setOtherOIOtherProblems(signsAndSymptoms);
            }
            String notedSideEffects = extractConceptCodes(159935, obsList, hivEncounterTypeDictionary);//1607
            if (StringUtils.isNoneEmpty(notedSideEffects)) {
                hivEncType.setNotedSideEffects(notedSideEffects);
            }

            //String reasonForPoorARVAdh=extractConceptCodes(7778453, obsList, hivEncounterTypeDictionary);
            //hivEncType.setWhyPoorFairARVDrugAdherence(reasonForPoorARVAdh);
            //String reasonForPoorCTXAdh=extractConceptCodes(7778454, obsList, hivEncounterTypeDictionary);
            //hivEncType.setWhyPoorFairCotrimoxazoleDrugAdherence(reasonForPoorCTXAdh);
            //String reasonForPoorINHAdh=extractConceptCodes(7778455, obsList, hivEncounterTypeDictionary);
            //hivEncType.setWhyPoorFairINHDrugAdherence(reasonForPoorINHAdh);
            String systolic = "", diastolic = "", bp = "";
            Obs systolicObs = extractConcept(5085, obsList);//84
            if (systolicObs != null) {
                systolic = systolicObs.getVariableValue();
            }
            Obs diastolicObs = extractConcept(5086, obsList);//568
            if (diastolicObs != null) {
                diastolic = diastolicObs.getVariableValue();
            }
            if (!systolic.isEmpty() && !diastolic.isEmpty()) {
                bp = systolic + "/" + diastolic;
                hivEncType.setBloodPressure(StringEscapeUtils.escapeXml10(bp));
            }
            for (Obs obs : obsList) {
                conceptID = obs.getConceptID();

                switch (conceptID) {
                    case 5089://1734
                        value_numeric = (int) Math.round(obs.getValueNumeric());
                        hivEncType.setWeight(value_numeric);
                        break;
                    case 5090:
                        value_numeric = (int) Math.round(obs.getValueNumeric());
                        hivEncType.setChildHeight(value_numeric);
                        break;
                    case 5497:
                        value_numeric = (int) Math.round(obs.getValueNumeric());
                        hivEncType.setCD4(value_numeric);
                        hivEncType.setCD4TestDate(getXmlDate(obs.getVisitDate()));
                        break;
                    case 85:// investigate
                        value_numeric = (int) Math.round(obs.getValueNumeric());
                        hivEncType.setWeight(value_numeric);
                        break;

                    case 571:// Investigate
                        value_numeric = (int) Math.round(obs.getValueNumeric());
                        hivEncType.setChildHeight(value_numeric);
                        break;
                    case 165050:
                        value_coded = obs.getValueCoded();
                        code = hivEncounterTypeDictionary.get(value_coded);
                        if (StringUtils.isNotEmpty(code)) {
                            hivEncType.setEDDandPMTCTLink(code);
                        }
                        break;
                    case 165945:
                        value_coded = obs.getValueCoded();
                        if (value_coded == 165685) {
                            hivEncType.setEDDandPMTCTLink("PMTCT");
                        }
                        break;
                    case 1434:
                        value_coded = obs.getValueCoded();
                        if (value_coded == 1) {
                            hivEncType.setEDDandPMTCTLink("P");
                        } else if (value_coded == 0) {
                            hivEncType.setEDDandPMTCTLink("NP");
                        }
                        break;
                    case 5271://1741
                        value_coded = obs.getValueCoded();
                        if (value_coded == 1) {
                            hivEncType.setPatientFamilyPlanningCode("FP");
                        } else if (value_coded == 0) {
                            hivEncType.setPatientFamilyPlanningCode("NOFP");
                        }

                        break;
                    case 374:
                        value_coded = obs.getValueCoded();
                        hivEncType.setPatientFamilyPlanningMethodCode(hivEncounterTypeDictionary.get(value_coded));
                        break;
                    case 165039:
                        value_coded = obs.getValueCoded();
                        hivEncType.setFunctionalStatus(hivEncounterTypeDictionary.get(value_coded));
                        break;
                    case 5356:
                        value_coded = obs.getValueCoded();
                        hivEncType.setWHOClinicalStage(hivEncounterTypeDictionary.get(value_coded));
                        break;
                    case 1659:
                        value_coded = obs.getValueCoded();
                        hivEncType.setTBStatus(hivEncounterTypeDictionary.get(value_coded));
                        break;
                    /*case 528:
                        value_coded = obs.getValueCoded();
                        hivEncType.setOtherOIOtherProblems(hivEncounterTypeDictionary.get(value_coded));
                        break;
                    case 1607:
                        value_coded = obs.getValueCoded();
                        hivEncType.setNotedSideEffects(hivEncounterTypeDictionary.get(value_coded));
                        break;*/
 /*case 88:
                        value_numeric = (int) obs.getValueNumeric();
                        hivEncType.setCD4(value_numeric);
                        hivEncType.setCD4TestDate(getXmlDate(obs.getVisitDate()));
                        break;*/
                    case 164506: // Adult 1st line ARV regimen
                        regimen = obs.getVariableValue();
                        cst = new CodedSimpleType();
                        cst.setCode(getRegimenCode(regimen, 164506));
                        cst.setCodeDescTxt(getCareCardRegimen(regimen));
                        hivEncType.setARVDrugRegimen(cst);
                        break;
                    case 164513:// Adult 2nd line ARV regimen
                        regimen = obs.getVariableValue();
                        cst = new CodedSimpleType();
                        cst.setCode(getRegimenCode(regimen, 164513));
                        //cst.setCode(getRegimenCode(regimen));
                        cst.setCodeDescTxt(getCareCardRegimen(regimen));
                        hivEncType.setARVDrugRegimen(cst);
                        break;
                    case 165702:// Adult 3rd Line ARV Regimen
                        regimen = obs.getVariableValue();
                        cst = new CodedSimpleType();
                        cst.setCode(getRegimenCode(regimen, 164513));
                        //cst.setCode(getRegimenCode(regimen));
                        cst.setCodeDescTxt(getCareCardRegimen(regimen));
                        hivEncType.setARVDrugRegimen(cst);
                        break;
                    case 164507: // Child 1st line ARV regimen
                        regimen = obs.getVariableValue();
                        cst = new CodedSimpleType();
                        cst.setCode(getRegimenCode(regimen, 164507));
                        //cst.setCode(getRegimenCode(regimen));
                        cst.setCodeDescTxt(getCareCardRegimen(regimen));
                        hivEncType.setARVDrugRegimen(cst);
                        break;
                    case 164514: // Child 2nd line ARV regimen
                        regimen = obs.getVariableValue();
                        cst = new CodedSimpleType();
                        cst.setCode(getRegimenCode(regimen, 164514));
                        //cst.setCode(getRegimenCode(regimen));
                        cst.setCodeDescTxt(getCareCardRegimen(regimen));
                        hivEncType.setARVDrugRegimen(cst);
                        break;
                    case 165703: //Child 3rd Line ARV Regimen
                        regimen = obs.getVariableValue();
                        cst = new CodedSimpleType();
                        cst.setCode(getRegimenCode(regimen, 164514));
                        //cst.setCode(getRegimenCode(regimen));
                        cst.setCodeDescTxt(getCareCardRegimen(regimen));
                        hivEncType.setARVDrugRegimen(cst);
                        break;
                    case 165290:
                        value_coded = obs.getValueCoded();
                        hivEncType.setARVDrugAdherence(hivEncounterTypeDictionary.get(value_coded));
                        break;
                    /*case 7778453:
                        value_coded = obs.getValueCoded();
                        hivEncType.setWhyPoorFairARVDrugAdherence(hivEncounterTypeDictionary.get(value_coded));
                        break;*/
 /*case 7778203:
                        value_coded = obs.getValueCoded();
                        cst = new CodedSimpleType();
                        cst.setCode(hivEncounterTypeDictionary.get(value_coded));
                        cst.setCodeDescTxt("Cotrimoxazole " + obs.getVariableValue());
                        hivEncType.setCotrimoxazoleDose(cst);
                        break;*/
                    case 161625:
                        value_coded = obs.getValueCoded();
                        hivEncType.setCotrimoxazoleAdherence(hivEncounterTypeDictionary.get(value_coded));
                        break;
                    /*case 7778454:
                        value_coded = obs.getValueCoded();
                        hivEncType.setWhyPoorFairCotrimoxazoleDrugAdherence(hivEncounterTypeDictionary.get(value_coded));
                        break;*/
 /*case 7778202:
                        value_coded = obs.getValueCoded();
                        cst = new CodedSimpleType();
                        cst.setCode(hivEncounterTypeDictionary.get(value_coded));
                        cst.setCodeDescTxt("Isoniazid " + obs.getVariableValue());
                        hivEncType.setINHDose(cst);
                        break;*/
                    case 161653:
                        value_coded = obs.getValueCoded();
                        hivEncType.setINHAdherence(hivEncounterTypeDictionary.get(value_coded));
                        break;
                    /*case 7778455:
                        value_coded = obs.getValueCoded();
                        hivEncType.setWhyPoorFairINHDrugAdherence(hivEncounterTypeDictionary.get(value_coded));
                        break;*/
                    case 5096:
                        value_datetime = obs.getValueDate();
                        hivEncType.setNextAppointmentDate(getXmlDate(value_datetime));
                        break;
                    /*case 7777821:
                        value_coded = obs.getValueCoded();
                        if (hivEncType.getNextAppointmentDate() == null) {
                            hivEncType.setNextAppointmentDate(getXmlDate(getNextAppointmentDate(obs.getVisitDate(), value_coded)));
                        }
                        break;*/
 /*case 77778364:
                        value_coded = obs.getValueCoded();
                        if (hivEncType.getINHDose() == null) {
                            cst = new CodedSimpleType();
                            cst.setCode(hivEncounterTypeDictionary.get(value_coded));
                            cst.setCodeDescTxt(obs.getVariableValue());
                            hivEncType.setINHDose(cst);
                        }*/

                    default:
                        break;
                }

            }

            PatientRegimen rgm = null;
            String openmrsARVRegimen = "";
            String cotrimDose = "";
            String inhDose = "";
            String drugName = "";
            String strength = "";
            String otherStrength = "";

            String regimenCode = "";
            for (DrugOrder ord : orders) {
                rgm = (PatientRegimen) ord;
                if (rgm.getStartDate() != null && rgm.getStartDate().equals(visitDate)) {
                    openmrsARVRegimen = rgm.getRegimenName();
                    regimenCode = rgm.getCode();
                    if (!openmrsARVRegimen.isEmpty() && !regimenCode.equalsIgnoreCase("-1") && getRegimenCode(openmrsARVRegimen) != null) {
                        cst = new CodedSimpleType();
                        cst.setCode(getRegimenCode(openmrsARVRegimen));
                        cst.setCodeDescTxt(getCareCardRegimen(openmrsARVRegimen));
                        hivEncType.setARVDrugRegimen(cst);
                    }
                }
            }
            for (Drugs drg : drugList) {
                drugName = drg.getDrugName();
                strength = drg.getStrength();
                otherStrength = drg.getOtherStrength();
                if (drg.getDispensedDate() != null && drg.getDispensedDate().equals(visitDate)) {
                    if (hivEncType.getCotrimoxazoleDose() == null) {
                        if (StringUtils.equalsIgnoreCase(drugName, "CTX")) {
                            if (StringUtils.equalsIgnoreCase("960mg", strength) || StringUtils.equalsIgnoreCase("960mg", otherStrength)) {
                                code = "CTX960";
                                description = "Cotrimoxazole 960mg";
                                cst = new CodedSimpleType();
                                cst.setCode(code);
                                cst.setCodeDescTxt(description);
                                hivEncType.setCotrimoxazoleDose(cst);
                            }
                            if (StringUtils.equalsIgnoreCase("120mg", strength) || StringUtils.equalsIgnoreCase("120mg", otherStrength)) {
                                code = "CTX240";
                                description = "Cotrimoxazole 120mg";
                                cst = new CodedSimpleType();
                                cst.setCode(code);
                                cst.setCodeDescTxt(description);
                                hivEncType.setCotrimoxazoleDose(cst);
                            }
                            if (StringUtils.equalsIgnoreCase("480mg", strength) || StringUtils.equalsIgnoreCase("480mg", otherStrength)) {
                                code = "CTX480";
                                description = "Cotrimoxazole 480mg";
                                cst = new CodedSimpleType();
                                cst.setCode(code);
                                cst.setCodeDescTxt(description);
                                hivEncType.setCotrimoxazoleDose(cst);
                            }
                        }
                    }
                    if (hivEncType.getINHDose() == null) {
                        if (StringUtils.contains(drugName, "INH")) {
                            code = "H";
                            description = "Isoniazid " + strength + otherStrength;
                            cst = new CodedSimpleType();
                            cst.setCode(code);
                            cst.setCodeDescTxt(description);
                            hivEncType.setINHDose(cst);
                        }
                    }
                }

            }

        }

        return hivEncType;
    }

    public String extractConceptCodes(int conceptID, List<Obs> obsList, HashMap<Integer, String> answerMap) {
        String codedAnswer = "";
        String val = "";
        ArrayList<String> valList = new ArrayList<String>();
        for (Obs obs : obsList) {
            if (obs.getConceptID() == conceptID) {
                if (answerMap.containsKey(obs.getValueCoded())) {
                    val = answerMap.get(obs.getValueCoded());
                    valList.add(val);
                }
            }
        }
        codedAnswer = StringUtils.join(valList, "|");
        return codedAnswer;
    }

    public Date getNextAppointmentDate(Date visitDate, int valueCoded) {
        Date nextAptDate = null;
        DateTime visitDateTime = new DateTime(visitDate);
        int dayVal = 60;
        switch (valueCoded) {
            case 1570:
                dayVal = 7;
                break;
            case 157:
                dayVal = 14;
                break;
            case 1628:
                dayVal = 30;
                break;
            case 1574:
                dayVal = 60;
                break;
            case 1575:
                dayVal = 120;
                break;
            default:
                break;
        }
        nextAptDate = visitDateTime.plusDays(dayVal).toDate();
        return nextAptDate;
    }

    public boolean isValidLga(String lga) {
        boolean ans = false;
        Set<String> lgaNamesSet = lgaCodeDictionary.keySet();
        if (!StringUtils.isEmpty(lga)) {
            if (lgaNamesSet.contains(StringUtils.trim(lga))) {
                ans = true;
            }

        }

        return ans;
    }

    public AddressType createAddressType(Demographics pts) {
        AddressType address = null;
        String countryCode = "NGA";
        String addressType = "H";
        if (!StringUtils.isEmpty(pts.getAddress_state()) || !StringUtils.isEmpty(pts.getAddress_lga()) || !StringUtils.isEmpty(pts.getAddress2()) || !StringUtils.isEmpty(pts.getAddress1())) {
            address = new AddressType();
            String ward = pts.getAddress2();
            String town = pts.getAddress1();
            String address_state = stateCodeDictinary.get(StringUtils.upperCase(pts.getAddress_state()));
            String address_lga = null;
            address.setCountryCode(countryCode);
            address.setAddressTypeCode(addressType);
            //address.setWardVillage(ward);
            address.setStateCode(address_state);
            if (isValidLga(pts.getAddress_lga())) {
                address_lga = lgaCodeDictionary.get(StringUtils.upperCase(StringUtils.trim(pts.getAddress_lga())));
                address.setLGACode(address_lga);
            }

        }
        return address;
    }

    public String formatDate(Date date) {
        String dateString = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            dateString = df.format(date);
        }
        return dateString;

    }

    public ArrayList<RegimenType> createRegimenTypeList(ArrayList<DrugOrder> orders, model.datapump.Demographics pts) throws DatatypeConfigurationException {
        ArrayList<RegimenType> regimenTypeList = new ArrayList<RegimenType>();
        RegimenType regimenType = null;
        for (DrugOrder ele : orders) {
            ele.setPepfarID(pts.getPepfarID());
            regimenType = createRegimenType(ele);
            regimenTypeList.add(regimenType);
        }
        return regimenTypeList;
    }

    public ArrayList<RegimenType> createRegimenTypeListFromDrugs(ArrayList<Drugs> drugList, model.datapump.Demographics pts) throws DatatypeConfigurationException {
        ArrayList<RegimenType> regimenTypeList = new ArrayList<RegimenType>();
        PatientRegimen rgm = null;
        RegimenType regimenType = null;
        CodedSimpleType cst = null;
        for (Drugs ele : drugList) {
            if (isValidOIDrug(ele.getDrugName())) {
                ele.setPepfarID(pts.getPepfarID());
                regimenType = createRegimenType(ele);
                regimenTypeList.add(regimenType);
            }
        }
        return regimenTypeList;
    }

    public RegimenType createRegimenType(Drugs drg) throws DatatypeConfigurationException {
        RegimenType regimenType = null;
        String drugName = drg.getDrugName();
        String pepfarID = "";
        Date startDate = null;
        Date stopDate = null;
        int duration = 0;
        String durationUnit = "";
        String regimenLineCode = null;
        CodedSimpleType cst = null;
        String prescribedRegimenTypeCode = "";
        String strength = drg.getStrength();
        String otherStrength = drg.getOtherStrength();
        String code = "";
        String description = "";
        pepfarID = drg.getPepfarID();
        startDate = drg.getDispensedDate();
        duration = drg.getDuration();
        durationUnit = drg.getDurationUnit();
        stopDate = calculateStopDate(startDate, duration, durationUnit);
        prescribedRegimenTypeCode = "";
        regimenLineCode = null;
        if (StringUtils.equalsIgnoreCase(drugName, "CTX")) {
            if (StringUtils.isNotBlank(otherStrength)) {
                code = "CTX960";
                description = "Cotrimoxazole" + " " + otherStrength;
                cst = new CodedSimpleType();
                cst.setCode(code);
                cst.setCodeDescTxt(description);
                prescribedRegimenTypeCode = "CTX";
                regimenType = createRegimenType(pepfarID, startDate, stopDate, prescribedRegimenTypeCode, regimenLineCode, cst);
            }
            if (StringUtils.isNotBlank(strength)) {
                if (strength.contains("960mg")) {
                    code = "CTX960";
                    description = "Cotrimoxazole 960mg";
                    cst = new CodedSimpleType();
                    cst.setCode(code);
                    cst.setCodeDescTxt(description);
                    prescribedRegimenTypeCode = "CTX";
                    regimenType = createRegimenType(pepfarID, startDate, stopDate, prescribedRegimenTypeCode, regimenLineCode, cst);
                } else if (strength.contains("480mg")) {
                    code = "CTX480";
                    description = "Cotrimoxazole 480mg";
                    cst = new CodedSimpleType();
                    cst.setCode(code);
                    cst.setCodeDescTxt(description);
                    prescribedRegimenTypeCode = "CTX";
                    regimenType = createRegimenType(pepfarID, startDate, stopDate, prescribedRegimenTypeCode, regimenLineCode, cst);
                } else if (strength.contains("120mg")) {
                    code = "CTX120";
                    description = "Cotrimoxazole 120mg";
                    cst = new CodedSimpleType();
                    cst.setCode(code);
                    cst.setCodeDescTxt(description);
                    prescribedRegimenTypeCode = "CTX";
                    regimenType = createRegimenType(pepfarID, startDate, stopDate, prescribedRegimenTypeCode, regimenLineCode, cst);
                } else {
                    code = "CTX960";
                    description = "Cotrimoxazole 960mg";
                    cst = new CodedSimpleType();
                    cst.setCode(code);
                    cst.setCodeDescTxt(description);
                    prescribedRegimenTypeCode = "CTX";
                    regimenType = createRegimenType(pepfarID, startDate, stopDate, prescribedRegimenTypeCode, regimenLineCode, cst);
                }
            }
        } else if (isValidOIDrug(drugName)) {
            code = getLocalCodingOtherDrugs(drugName);
            description = drugName;// + " " + strength + otherStrength;
            if (!StringUtils.isEmpty(strength)) {
                description = description + " " + strength;
            }
            if (!StringUtils.isEmpty(otherStrength)) {
                description = description + " " + otherStrength;
            }
            cst = new CodedSimpleType();
            cst.setCode(code);
            cst.setCodeDescTxt(description);
            prescribedRegimenTypeCode = "NONART";
            regimenType = createRegimenType(pepfarID, startDate, stopDate, prescribedRegimenTypeCode, regimenLineCode, cst);
            //regimenType.
        }
        return regimenType;
    }

    public Date calculateStopDate(Date startDate, int duration, String unit) {
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
               // dayVal = 30;
            //}
        }
        DateTime startDateTime = new DateTime(startDate);
        DateTime stopDateTime = startDateTime.plusDays(dayVal);
        stopDate = stopDateTime.toDate();
        return stopDate;
    }

    public RegimenType createRegimenType(DrugOrder order) throws DatatypeConfigurationException {
        PatientRegimen rgm = (PatientRegimen) order;
        RegimenType regimenType = null;
        String pepfarID = "", prescribedRegimenTypeCode = "", regimenLine = "", regimenLineCode = "", drugName = rgm.getDrugName();
        Date startDate = null, stopDate = null;
        CodedSimpleType cst = null;
        int regimenConcept = 0;

        if (!rgm.getRegimenName().isEmpty() && getRegimenCode(rgm.getRegimenName()) != null) {
            pepfarID = rgm.getPepfarID();
            startDate = rgm.getStartDate();
            stopDate = rgm.getStopDate();
            prescribedRegimenTypeCode = "ART";
            regimenLine = rgm.getRegimenLine();
            if (regimenLine.equalsIgnoreCase("First Line")) {
                regimenConcept = 7778108;
            } else if (regimenLine.equalsIgnoreCase("Second Line")) {
                regimenConcept = 7778109;
            }
            cst = new CodedSimpleType();
            cst.setCode(getRegimenCode(rgm.getRegimenName(), regimenConcept));// To be handled latter
            cst.setCodeDescTxt(getCareCardRegimen(rgm.getRegimenName()));
            if (regimenLine.equalsIgnoreCase("First Line")) {
                regimenLineCode = "10";
            } else if (regimenLine.equalsIgnoreCase("Second Line")) {
                regimenLineCode = "20";
            }
            regimenType = createRegimenType(pepfarID, startDate, stopDate, prescribedRegimenTypeCode, regimenLineCode, cst);

        }
        return regimenType;
    }

    public RegimenType createRegimenType(String pepfarID, Date startDate, Date stopDate, String prescribedRegimenTypeCode, String regimenLine, CodedSimpleType cst) throws DatatypeConfigurationException {
        RegimenType regimenType = null;
        if (startDate != null) {
            regimenType = new RegimenType();
            regimenType.setVisitDate(getXmlDate(startDate));
            String visitID = String.valueOf(String.valueOf(formatDate(startDate) + "-" + pepfarID));
            regimenType.setVisitID(visitID);
            regimenType.setPrescribedRegimenTypeCode(prescribedRegimenTypeCode);
            regimenType.setPrescribedRegimen(cst);
            //regimenType.set
            Calendar cal = Calendar.getInstance();
            String month = "", year = "", day = "";
            cal.setTime(startDate);
            month = StringUtils.leftPad(String.valueOf(cal.get(Calendar.MONTH) + 1), 2, "0");
            year = String.valueOf(cal.get(Calendar.YEAR));
            day = StringUtils.leftPad(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)), 2, "0");
            regimenType.setDateRegimenStarted(getXmlDate(startDate));
            regimenType.setDateRegimenStartedDD(day);
            regimenType.setDateRegimenStartedMM(month);
            regimenType.setDateRegimenStartedYYYY(year);
            Date endDate = stopDate;
            if (endDate != null) {
                regimenType.setDateRegimenEnded(getXmlDate(endDate));
                cal.setTime(endDate);
                month = StringUtils.leftPad(String.valueOf(cal.get(Calendar.MONTH) + 1), 2, "0");
                year = String.valueOf(cal.get(Calendar.YEAR));
                day = StringUtils.leftPad(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)), 2, "0");
                regimenType.setDateRegimenEndedDD(day);
                regimenType.setDateRegimenEndedMM(month);
                regimenType.setDateRegimenEndedYYYY(year);
                DateTime startDateTime = new DateTime(startDate);
                DateTime endDateTime = new DateTime(endDate);
                //Weeks wks = Weeks.weeksBetween(startDateTime, endDateTime);
                //int wkVal = wks.getWeeks();
                Days days = Days.daysBetween(startDateTime, endDateTime);
                int daysVal = days.getDays();
                String regimenDuration = String.valueOf(daysVal);
                System.out.println("dayVal from here: "+daysVal);
                regimenType.setPrescribedRegimenDuration(regimenDuration);

            }

        }
        return regimenType;
    }

    public Obs extractConcept(int conceptID, List<Obs> obsList) {
        Obs obs = null;
        for (Obs ele : obsList) {
            if (ele.getConceptID() == conceptID) {
                obs = ele;
            }
        }
        return obs;
    }

    public LaboratoryReportType createLaboratoryReportType(Visit visit, ArrayList<Obs> obsList, Date artStartDate) throws DatatypeConfigurationException {
        LaboratoryReportType labReport = null;
        LaboratoryOrderAndResult result = null;
        Date orderedDate = null, reportedDate = null;
        int value_coded = 0;
        int conceptID = 0;
        int value_numeric = 0;
        int dataType = 0;
        Concept c = null;
        AnswerType answer = null;
        NumericType numeric = null;
        CodedSimpleType cst = null;
        labReport = new LaboratoryReportType();
        Date visitDate = visit.getVisitDate();
        String providerName = obsList.get(0).getProvider();
        labReport.setVisitID(visit.getVisitID());
        labReport.setVisitDate(getXmlDate(visitDate));
        //labReport.setReportedBy(providerName);
        labReport.setClinician(providerName);
        labReport.setCollectionDate(getXmlDate(visitDate));
        //Obs labIDObs = extractConcept(7777905, obsList);
        //if (labIDObs != null) {
        //   labReport.setLaboratoryTestIdentifier(labIDObs.getVariableValue());
        //}
        labReport.setLaboratoryTestIdentifier(visit.getVisitID());
        Obs reportedByObs = extractConcept(164982, obsList);
        if (reportedByObs != null) {
            labReport.setReportedBy(reportedByObs.getVariableValue());
        }
        Obs checkedByObs = extractConcept(164983, obsList);
        if (checkedByObs != null) {
            labReport.setCheckedBy(checkedByObs.getVariableValue());
        }
        Obs baselineFlag = extractConcept(164181, obsList);
        if (baselineFlag != null) {
            value_coded = baselineFlag.getValueCoded();
            if (value_coded == 164180) {
                labReport.setBaselineRepeatCode("B");
            } else if (value_coded == 160530) {
                labReport.setBaselineRepeatCode("R");
            }
        }
        Obs obsOrderedDate = extractConcept(164989, obsList);
        if (obsOrderedDate != null) {
            orderedDate = obsOrderedDate.getValueDate();
        }
        Obs obsReportedDate = extractConcept(165414, obsList);
        if (obsReportedDate != null) {
            reportedDate = obsReportedDate.getValueDate();
        }
        DateTime d1, d2;
        String artStatus = null;
        if (artStartDate != null) {
            d1 = new DateTime(artStartDate);
            d2 = new DateTime(visitDate);
            if (d1.isEqual(d2) || d1.isBefore(d2)) {
                artStatus = "A";
            } else if (d1.isAfter(d2)) {
                artStatus = "N";
            }
            labReport.setARTStatusCode(artStatus);

        }
        for (Obs obs : obsList) {
            conceptID = obs.getConceptID();
            c = conceptDictionary.get(conceptID);
            dataType = c.getDataType();
            if (dataType == 1 && labTestDictionary.containsKey(conceptID)) {
                cst = new CodedSimpleType();
                cst.setCode(labTestDictionary.get(conceptID));
                cst.setCodeDescTxt(c.getConceptName());
                result = new LaboratoryOrderAndResult();
                result.setLaboratoryResultedTest(cst);
                answer = new AnswerType();
                numeric = new NumericType();
                if (orderedDate != null) {
                    result.setOrderedTestDate(getXmlDate(orderedDate));
                }
                if (reportedDate != null) {
                    result.setResultedTestDate(getXmlDate(reportedDate));
                }
                numeric.setValue1((int) Math.round(obs.getValueNumeric()));
                if (labTestUnits.containsKey(conceptID)) {
                    String[] descriptionText = null;
                    CodedType ct = new CodedType();
                    ct.setCode(labTestUnits.get(conceptID));
                    descriptionText = StringUtils.split(labTestUnitDescription.get(ct.getCode()), ",");
                    if (descriptionText != null) {
                        ct.setCodeDescTxt(descriptionText[0]);
                        ct.setCodeSystemCode(StringEscapeUtils.escapeXml10(descriptionText[1]));
                    }
                    numeric.setUnit(ct);
                }
                answer.setAnswerNumeric(numeric);
                result.setLaboratoryResult(answer);
                labReport.getLaboratoryOrderAndResult().add(result);
            } else if (dataType == 2 && labTestDictionary.containsKey(conceptID)) {
                cst = new CodedSimpleType();
                value_coded = obs.getValueCoded();
                if (conceptDictionary.containsKey(value_coded)) {
                    cst.setCode(labTestDictionary.get(conceptID));
                    cst.setCodeDescTxt(c.getConceptName());
                    result = new LaboratoryOrderAndResult();
                    result.setLaboratoryResultedTest(cst);
                    answer = new AnswerType();
                    CodedType ct = new CodedType();
                    ct.setCode(conceptDictionary.get(value_coded).getConceptName());
                    ct.setCodeDescTxt(conceptDictionary.get(value_coded).getConceptName());
                    ct.setCodeSystemCode(conceptDictionary.get(value_coded).getConceptName());
                    answer.setAnswerCode(ct);
                    result.setLaboratoryResult(answer);
                    if (orderedDate != null) {
                        result.setOrderedTestDate(getXmlDate(orderedDate));
                    }
                    if (reportedDate != null) {
                        result.setResultedTestDate(getXmlDate(reportedDate));
                    }
                    labReport.getLaboratoryOrderAndResult().add(result);
                }
            }

        }
        if (labReport.getLaboratoryOrderAndResult().isEmpty()) {
            labReport = null;
        }
        return labReport;

    }

    public HIVQuestionsType createHIVQuestionType(PatientRegimen openmrsFirstRegimen, Date artStartDate, Date enrollmentDt, boolean onART, ArrayList<Obs> obsList) throws DatatypeConfigurationException {
        HIVQuestionsType hiv = new HIVQuestionsType();
        if (enrollmentDt != null) {
            hiv.setEnrolledInHIVCareDate(getXmlDate(enrollmentDt));
        }
        String regimenCode = null;
        String regimenName = null;
        if (openmrsFirstRegimen != null) {
            regimenName = openmrsFirstRegimen.getRegimenName();
            regimenCode = getRegimenCode(openmrsFirstRegimen.getRegimenName());
        }

        CodedSimpleType cst1 = null;
        int conceptID = 0;
        int form_id = 0;
        double value_numeric = 0.0;
        int value_coded = 0;
        String value_text = "";
        Date value_datetime = null;
        FacilityType ft = null;
        if (regimenCode != null && regimenName != null) {
            cst1 = new CodedSimpleType();
            cst1.setCode(regimenCode);
            cst1.setCodeDescTxt(getCareCardRegimen(regimenName));
            hiv.setFirstARTRegimen(cst1);
        }
        if (artStartDate != null) {
            hiv.setARTStartDate(getXmlDate(artStartDate));
        } else if (regimenCode != null && openmrsFirstRegimen != null) {
            hiv.setARTStartDate(getXmlDate(openmrsFirstRegimen.getStartDate()));
        }
        for (Obs obs : obsList) {
            conceptID = obs.getConceptID();
            //System.out.println("Concept ID: "+conceptID);
            switch (conceptID) {
                case 160540:
                    value_coded = obs.getValueCoded();
                    hiv.setCareEntryPoint(hivQueDictionary.get(value_coded));
                    break;
                case 160554:
                    value_datetime = obs.getValueDate();
                    if (value_datetime != null) {
                        hiv.setFirstConfirmedHIVTestDate(getXmlDate(value_datetime));
                    }
                    break;
                case 164947:
                    value_coded = obs.getValueCoded();
                    hiv.setFirstHIVTestMode(hivQueDictionary.get(value_coded));
                    break;
                /*case 7778238:
                    value_text = obs.getValueText();
                    //ft=createFacilityType(value_text);
                    hiv.setWhereFirstHIVTest(value_text);
                    break;*/
                case 165242:
                    value_coded = obs.getValueCoded();
                    hiv.setPriorArt(hivQueDictionary.get(value_coded));
                    break;
                /*case 1703:
                    value_datetime = obs.getValueDate();
                    if (value_datetime != null) {
                        hiv.setMedicallyEligibleDate(getXmlDate(value_datetime));
                    }
                    break;*/
 /*case 1731:
                    value_coded = obs.getValueCoded();
                    hiv.setReasonMedicallyEligible(hivQueDictionary.get(value_coded));
                    break;*/
 /* case 7777862:
                    value_datetime = obs.getValueDate();
                    hiv.setInitialAdherenceCounselingCompletedDate(getXmlDate(value_datetime));
                    break;*/
                case 160534:
                    form_id = obs.getFormID();
                    if (form_id == 23) {
                        value_datetime = obs.getValueDate();
                        if (value_datetime != null) {
                            hiv.setTransferredInDate(getXmlDate(value_datetime));
                        }
                    }
                    break;
                case 160535:
                    value_text = obs.getValueText();
                    FacilityType ft2 = createFacilityType(value_text);
                    hiv.setTransferredInFrom(ft2);
                    break;
                /*case 7778529:
                    value_coded = obs.getValueCoded();
                    hiv.setWHOClinicalStageARTStart(hivQueDictionary.get(value_coded));
                    break;
                case 1734:
                    value_numeric = obs.getValueNumeric();
                    hiv.setWeightAtARTStart((int) value_numeric);
                    break;
                case 1735:
                    value_numeric = obs.getValueNumeric();
                    hiv.setChildHeightAtARTStart((int) value_numeric);
                    break;
                case 7778530:
                    value_coded = obs.getValueCoded();
                    hiv.setFunctionalStatusStartART(hivQueDictionary.get(value_coded));
                    break;
                case 1733:
                    value_numeric = obs.getValueNumeric();
                    hiv.setCD4AtStartOfART(String.valueOf(value_numeric));
                    break;*/
                case 165470:
                    value_coded = obs.getValueCoded();
                    if (value_coded == 159492) {
                        hiv.setPatientTransferredOut(true);
                        if (onART) {
                            hiv.setTransferredOutStatus("A");
                        }
                    } else if (value_coded == 165889) {
                        hiv.setPatientHasDied(true);
                        if (onART) {
                            hiv.setStatusAtDeath("A");
                        }
                    }
                    break;
                /*case 980:
                    value_text = obs.getValueText();
                    hiv.setSourceOfDeathInformation(value_text);
                    break;*/
                default:
                    break;
            }

        }
        return hiv;
    }

    public ConditionSpecificQuestionsType createConditionSpecificQuestionType() throws DatatypeConfigurationException {
        ConditionSpecificQuestionsType disease = new ConditionSpecificQuestionsType();
        return disease;
    }

    public CommonQuestionsType createCommonQuestionType(String hospID, ArrayList<Obs> obsList, Date firstVisitDate, Date lastVisitDate, boolean patientdiedfromillness, int age, String gender) throws DatatypeConfigurationException {
        CommonQuestionsType common = new CommonQuestionsType();
        common.setPatientDieFromThisIllness(patientdiedfromillness);
        common.setPatientAge(age);
        common.setHospitalNumber(hospID);
        if (firstVisitDate != null) {
            common.setDateOfFirstReport(getXmlDate(firstVisitDate));
        }
        if (lastVisitDate != null) {
            common.setDateOfLastReport(getXmlDate(lastVisitDate));
        }
        int value_coded = 0;
        Date value_datetime = null;
        int conceptID = 0;
        for (Obs obs : obsList) {
            conceptID = obs.getConceptID();
            switch (conceptID) {
                case 165050:
                    value_coded = obs.getValueCoded();
                    if (gender.equals("F")) {
                        switch (value_coded) {
                            case 165048:
                                common.setPatientPregnancyStatusCode("P");
                                break;
                            case 165047:
                                common.setPatientPregnancyStatusCode("NP");
                                break;
                            /*case 13:
                                common.setPatientPregnancyStatusCode("NK");
                                break;*/
                            case 165049:
                                common.setPatientPregnancyStatusCode("PMTCT");
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                case 5596:
                    value_datetime = obs.getValueDate();
                    if (gender.equals("F")) {
                        common.setEstimatedDeliveryDate(getXmlDate(value_datetime));
                    }
                    break;
                case 160554:
                    value_datetime = obs.getValueDate();
                    common.setDiagnosisDate(getXmlDate(value_datetime));
                    break;
                case 165470:
                    value_coded = obs.getValueCoded();
                    if (value_coded == 165889) {
                        common.setPatientDieFromThisIllness(true);
                    }
                    break;

                default:
                    break;
            }
        }
        return common;
    }

    public EncountersType createEncounterType() {
        EncountersType encounterType = new EncountersType();
        return encounterType;
    }

    public Marshaller createMarshaller(JAXBContext jaxbContext) throws JAXBException, SAXException {
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = sf.newSchema(getClass().getResource("/resource/NDR 1.2.xsd"));

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

        jaxbMarshaller.setSchema(schema);

        //Call Validator class to perform the validation
        jaxbMarshaller.setEventHandler(new Validator());

        return jaxbMarshaller;
    }

    public void writeFile(Marshaller jaxbMarshaller, Container container, File file, CustomErrorHandler errorHandler) throws SAXParseException, SAXException, PropertyException, JAXBException, IOException {
        javax.xml.validation.Validator validator = jaxbMarshaller.getSchema().newValidator();
        jaxbMarshaller.marshal(container, file);
        validator.setErrorHandler(errorHandler);

        validator.validate(new StreamSource(file));

    }

    public static void main(String[] arg) {
        NDRWriter wr = new NDRWriter();
        wr.loadLocationMap();
        System.out.println(wr.locationMap.get("3ea88257-e6ba-476f-af78-3804c0abc550"));
        //System.out.println("Size: " + wr.locationMap.size());
    }

}
