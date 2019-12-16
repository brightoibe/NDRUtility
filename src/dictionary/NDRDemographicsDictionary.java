/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import static com.inductivehealth.ndr.client.Client.getXmlDate;
import com.inductivehealth.ndr.schema.AddressType;
import com.inductivehealth.ndr.schema.FacilityType;
import com.inductivehealth.ndr.schema.FingerPrintType;
import com.inductivehealth.ndr.schema.IdentifierType;
import com.inductivehealth.ndr.schema.IdentifiersType;
import com.inductivehealth.ndr.schema.LeftHandType;
import com.inductivehealth.ndr.schema.PatientDemographicsType;
import com.inductivehealth.ndr.schema.RightHandType;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.datatype.DatatypeConfigurationException;
import model.datapump.BiometricInfo;
import model.datapump.Demographics;
import model.datapump.Location;
import model.datapump.Obs;
import org.apache.commons.lang3.StringUtils;
import util.LocationMap;

/**
 *
 * @author The Bright
 */
public class NDRDemographicsDictionary {

    private Map<String, String> stateCodeDictionary = new HashMap<String, String>();
    private Map<Integer, String> patientDemographicDictionary = new HashMap<Integer, String>();
    private Map<String, String> lgaCodeDictionary = new HashMap<String, String>();

    public void loadLGAMap() {
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

    public void loadStateCodeDictinary() {

        stateCodeDictionary.put("ADAMAWA", "2");
        stateCodeDictionary.put("ABIA", "1");
        stateCodeDictionary.put("AKWA IBOM", "3");
        stateCodeDictionary.put("ANAMBRA", "4");
        stateCodeDictionary.put("BAUCHI", "5");
        stateCodeDictionary.put("BAYELSA", "6");
        stateCodeDictionary.put("BENUE", "7");
        stateCodeDictionary.put("BORNO", "8");
        stateCodeDictionary.put("CROSS RIVER", "9");
        stateCodeDictionary.put("DELTA", "10");
        stateCodeDictionary.put("EBONYI", "11");
        stateCodeDictionary.put("EDO", "12");
        stateCodeDictionary.put("EKITI", "13");
        stateCodeDictionary.put("ENUGU", "14");
        stateCodeDictionary.put("FCT", "15");
        stateCodeDictionary.put("GOMBE", "16");
        stateCodeDictionary.put("IMO", "17");
        stateCodeDictionary.put("JIGAWA", "18");
        stateCodeDictionary.put("KADUNA", "19");
        stateCodeDictionary.put("KANO", "20");
        stateCodeDictionary.put("KATSINA", "21");
        stateCodeDictionary.put("KEBBI", "22");
        stateCodeDictionary.put("KOGI", "23");
        stateCodeDictionary.put("KWARA", "24");
        stateCodeDictionary.put("LAGOS", "25");
        stateCodeDictionary.put("NASARAWA", "26");
        stateCodeDictionary.put("NIGER", "27");
        stateCodeDictionary.put("OGUN", "28");
        stateCodeDictionary.put("ONDO", "29");
        stateCodeDictionary.put("OSUN", "30");
        stateCodeDictionary.put("OYO", "31");
        stateCodeDictionary.put("PLATEAU", "32");
        stateCodeDictionary.put("RIVERS", "33");
        stateCodeDictionary.put("SOKOTO", "34");
        stateCodeDictionary.put("TARABA", "35");
        stateCodeDictionary.put("YOBE", "36");
        stateCodeDictionary.put("ZAMFARA", "37");

    }

    public void loadPatientDemographicDictionary() {
        patientDemographicDictionary = new HashMap<Integer, String>();
        //Primary Language Coding
        patientDemographicDictionary.put(1080, "eng");
        patientDemographicDictionary.put(1081, "hau");
        patientDemographicDictionary.put(1082, "yor");
        patientDemographicDictionary.put(7778842, "ibo");
        //Educational Status Code
        patientDemographicDictionary.put(28, "1"); // None
        patientDemographicDictionary.put(850, "2");// Completed Primary
        patientDemographicDictionary.put(7777752, "3");// Secondary School Education
        patientDemographicDictionary.put(7777751, "3");// Secondary School Education
        patientDemographicDictionary.put(1078, "4");// Qur'anic
        patientDemographicDictionary.put(1523, "6");// Post Secondary
        //Ocupational Status Code
        patientDemographicDictionary.put(1076, "EMP");// Employed
        patientDemographicDictionary.put(1521, "RET");// Retired
        patientDemographicDictionary.put(1520, "STU");// Student
        patientDemographicDictionary.put(906, "UNE");// Unemployed
        
        
        patientDemographicDictionary.put(5622, "NA");// 5622
        patientDemographicDictionary.put(350, "M");// Married
        patientDemographicDictionary.put(346, "G");// Living With Partner
        patientDemographicDictionary.put(348, "S");// Single
        patientDemographicDictionary.put(351, "D");// Divorced
        patientDemographicDictionary.put(349, "W");// Widowed
        patientDemographicDictionary.put(1522, "A");// Separated

    }

    public PatientDemographicsType createPatientDemographics(Demographics patient, Location loc, List<Obs> obsList, LocationMap locMap) throws DatatypeConfigurationException {
        PatientDemographicsType demo = new PatientDemographicsType();
        demo.setPatientIdentifier(patient.getPepfarID());
        IdentifierType idt = null;
        IdentifiersType idtss = new IdentifiersType();
        //String hospID = loc.getDatimID() + "-" + patient.getHospID();
        String hospID = patient.getHospID();
        String otherID = patient.getOtherID();
        //String pepfarID = loc.getDatimID() + "-" + patient.getPepfarID();
        String pepfarID = patient.getPepfarID();
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
        if (stateOfOrigin != null) {
        demo.setStateOfNigeriaOriginCode(stateCodeDictionary.get(stateOfOrigin.toUpperCase()));
        }
        int conceptID = 0;
        int value_coded = 0;
        String value_text = "";
        double value_numeric = 0.0;

        for (Obs obs : obsList) {
            conceptID = obs.getConceptID();
            switch (conceptID) {
                case 977://Reason for Termination
                    value_coded = obs.getValueCoded();
                    if (value_coded == 975) {// if dead
                        demo.setPatientDeceasedIndicator(true);
                        demo.setPatientDeceasedDate(getXmlDate(obs.getVisitDate()));
                    } else {
                        demo.setPatientDeceasedIndicator(false);
                    }
                    break;
                case 7778447://Patient Dead
                    value_coded=obs.getValueCoded();
                    if (value_coded == 80) {// if dead
                        demo.setPatientDeceasedIndicator(true);
                        demo.setPatientDeceasedDate(getXmlDate(obs.getVisitDate()));
                    } else {
                        demo.setPatientDeceasedIndicator(false);
                    }
                    break;
                case 1083://Preferred Language
                    value_coded = obs.getValueCoded();
                    demo.setPatientPrimaryLanguageCode(patientDemographicDictionary.get(value_coded));
                    break;
                case 1079://Educational Level
                    value_coded = obs.getValueCoded();
                    //if (value_coded != 789) {
                        demo.setPatientEducationLevelCode(patientDemographicDictionary.get(value_coded));
                    //}
                    break;
                case 915://Occupation
                    value_coded = obs.getValueCoded();
                    demo.setPatientOccupationCode(patientDemographicDictionary.get(value_coded));
                    break;
                case 1075:// Parents Civil Status
                    value_coded = obs.getValueCoded();
                    demo.setPatientMaritalStatusCode(patientDemographicDictionary.get(value_coded));
                    
                    break;
            }
        }
        return demo;
    }

    private void loadDictionary() {
        loadStateCodeDictinary();
        loadPatientDemographicDictionary();
        loadLGAMap();
    }

    public NDRDemographicsDictionary() {
        loadDictionary();
    }

    public AddressType createAddressType(Demographics pts) {
        AddressType address = null;
        String countryCode = "NGA";
        String addressType = "H";
        if (!StringUtils.isEmpty(pts.getAddress_state()) || !StringUtils.isEmpty(pts.getAddress_lga()) || !StringUtils.isEmpty(pts.getAddress2()) || !StringUtils.isEmpty(pts.getAddress1())) {
            address = new AddressType();
            String ward = pts.getAddress2();
            String town = pts.getAddress1();
            String address_state = stateCodeDictionary.get(StringUtils.upperCase(pts.getAddress_state()));
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
     public static FacilityType createFacilityType(String name,String code) {
        FacilityType ft = new FacilityType();
        ft.setFacilityName(name);
        ft.setFacilityID(name.toUpperCase());
        ft.setFacilityTypeCode(code);
        return ft;
    }
     public String getTemplateFor(String fingerPosition,List<BiometricInfo> bioInfoList){
         String template=null;
         for(BiometricInfo ele: bioInfoList){
             if(StringUtils.equalsIgnoreCase(fingerPosition,ele.getFingerPosition())){
                 template=ele.getTemplate();
             }
         }
         return template;
     }
     public FingerPrintType createFingerPrintTypeFromBiometricInfoList(List<BiometricInfo> biometricInfoList) throws DatatypeConfigurationException{
         Date date_created=null;
         FingerPrintType fingerPrintType=new FingerPrintType();
         date_created=biometricInfoList.get(0).getDateCreated();
         RightHandType rightHandType=createRightHandTypeFromBiometricInfoList(biometricInfoList);
         LeftHandType leftHandType=createLeftHandTypeFromBiometricInfoList(biometricInfoList);
         fingerPrintType.setDateCaptured(getXmlDate(date_created));
         fingerPrintType.setLeftHand(leftHandType);
         fingerPrintType.setRightHand(rightHandType);
         return fingerPrintType;
     }
     public RightHandType createRightHandTypeFromBiometricInfoList(List<BiometricInfo> biometricInfoList){
         RightHandType rightHandType=null;
         String rightThumb="",rightIndex="",rightMiddle="",rightWedding="",rightSmall="";
         rightThumb=getTemplateFor("RightThumb", biometricInfoList);
         rightIndex=getTemplateFor("RightIndex", biometricInfoList);
         rightMiddle=getTemplateFor("RightMiddle", biometricInfoList);
         rightWedding=getTemplateFor("RightWedding", biometricInfoList);
         rightSmall=getTemplateFor("RightSmall", biometricInfoList);
         rightHandType=new RightHandType();
         rightHandType.setRightThumb(rightThumb);
         rightHandType.setRightIndex(rightIndex);
         rightHandType.setRightMiddle(rightMiddle);
         rightHandType.setRightWedding(rightWedding);
         rightHandType.setRightSmall(rightSmall);
         return rightHandType;
     }
     public LeftHandType createLeftHandTypeFromBiometricInfoList(List<BiometricInfo> biometricInfoList){
         LeftHandType leftHandType=new LeftHandType();
         String leftThumb="",leftIndex="",leftMiddle="",leftWedding="",leftSmall="";
         leftThumb=getTemplateFor("LeftThumb", biometricInfoList);
         leftIndex=getTemplateFor("LeftIndex", biometricInfoList);
         leftMiddle=getTemplateFor("LeftMiddle", biometricInfoList);
         leftWedding=getTemplateFor("LeftWedding", biometricInfoList);
         leftSmall=getTemplateFor("LeftSmall", biometricInfoList);
         leftHandType=new LeftHandType();
         leftHandType.setLeftThumb(leftThumb);
         leftHandType.setLeftIndex(leftIndex);
         leftHandType.setLeftMiddle(leftMiddle);
         leftHandType.setLeftWedding(leftWedding);
         leftHandType.setLeftSmall(leftSmall);
         return leftHandType;
     }
     
}
