/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlers;

import daos.DataPumpDao;
import dictionary.NDRPharmacyDictionary;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author brightoibe
 */
public class TestClass {
    private static HashMap<String,Integer> map1;
    public static void loadMaps() {
        map1 = new HashMap<String, Integer>();
        map1.put("NVP/AZT/3TC", 1);
        map1.put("NVP/TDF/FTC", 2);
        map1.put("NVP/TDF/3TC", 2);
        map1.put("NVP/D4T/3TC", 3);
        map1.put("NVP/ABC/3TC", 4);
        map1.put("EFV/AZT/3TC", 5);
        map1.put("EFV/TDF/FTC", 6);
        map1.put("EFV/TDF/3TC", 6);
        map1.put("EFV/D4T/3TC", 7);
        map1.put("EFV/ABC/3TC", 8);
        map1.put("ABC/AZT/3TC", 9);
        map1.put("LPVr/TDF/FTC", 11);
        map1.put("LPVr/TDF/3TC", 11);
        map1.put("LPVr/AZT/3TC", 12);
        map1.put("LPVr/AZT/FTC", 12);
        map1.put("LPVr/TDF/AZT/FTC", 13);
        map1.put("LPVr/TDF/AZT/3TC", 13);
        map1.put("LPVr/D4T/3TC", 14);
        map1.put("LPVr/D4T/FTC", 14);
        map1.put("LPVr/ABC/3TC", 15);
        map1.put("LPVr/ABC/FTC", 15);
        map1.put("SQVr/TDF/FTC", 16);
        map1.put("SQVr/TDF/3TC", 16);
        map1.put("SQVr/AZT/3TC", 17);
        map1.put("SQVr/AZT/FTC", 17);
        map1.put("SQVr/TDF/AZT/FTC", 18);
        map1.put("SQVr/TDF/AZT/3TC", 18);
        map1.put("IDVr/TDF/FTC", 19);
        map1.put("IDVr/TDF/3TC", 19);
        map1.put("IDVr/AZT/3TC", 20);
        map1.put("IDVr/AZT/FTC", 20);
        map1.put("IDVr/TDF/AZT/3TC", 21);
        map1.put("IDVr/TDF/AZT/FTC", 21);
        map1.put("DDI/SQVr/TDF", 22);
        map1.put("DDI/IDVr/TDF", 22);
        map1.put("ABC/DDI/LPVr", 22);
        map1.put("LPVr/AZT/ABC", 23);
        map1.put("ATVr/AZT/3TC", 24);
        map1.put("ATVr/AZT/FTC", 24);
        map1.put("ATVr/TDF/3TC", 25);
        map1.put("ATVr/TDF/FTC", 25);
        map1.put("3TC/ABC/ATVr", 22);
        map1.put("ATVr/AZT/FTC/TDF", 26);
        map1.put("ATVr/AZT/3TC/TDF", 26);
        map1.put("3TC/TDF/AZT/ATVr", 26);
        map1.put("IDVr/3TC/D4T", 26);
        map1.put("IDVr/AZT/3TC", 20);
        map1.put("AZT/3TC/TDF", 10);
        map1.put("SQVr/D4T/3TC", 26);
        map1.put("SQVr/ABC/TDF", 26);

    }

    public static int getCode(String codedDrug) {
        int code = -1;
        Set<String> set = map1.keySet();
        for (String ele : set) {
            if (isEquivalent(ele, codedDrug)) {
                code = map1.get(ele);
            }
        }
        return code;
    }
   
    public static boolean isEquivalent(String code1,String code2){
        boolean ans=false;
        String[] arr1=code1.split("/");
        String[] arr2=code2.split("-");
        Arrays.sort(arr1);
        Arrays.sort(arr2);
        return Arrays.equals(arr1, arr2);
        
    }
    public static String sortRegimen(String code){
        String[] codeArr=code.split("/");
        Arrays.sort(codeArr);
        return StringUtils.join(codeArr, "/");
    }
    public static void main(String[] arg) throws FileNotFoundException  {
        String data="Lamivudine (150mg) / Zidovudine (300mg) / Nevirapine (200mg)";
        NDRPharmacyDictionary pd=new NDRPharmacyDictionary();
       
        //data =StringUtils.replacePattern(data,"\\/r", "r");
        //data = StringUtils.trim(StringUtils.replacePattern(data, "\\(.*?\\)", ""));
        System.out.println(pd.convertDrugToRegimenCode(data));
        //String codedDrug="D4T-3TC-LPV/r";
        //loadMaps();
        //System.out.println(getCode(StringUtils.replace(codedDrug, "/","")));
        //ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        /*int i=0;
        
        while(i<1000){
            i++;
            NDRPharmacyDictionary dictionary=new NDRPharmacyDictionary();
            System.out.println("Size: "+dictionary.ndrCodedValues.keySet().size());
        }*/
        
        
       /* HashMap<String,Integer> map=new HashMap<String,Integer>();
        
        String str1="AZT/3TC/NVP";
        String str2="NVP/AZT/3TC/LPVr";
        System.out.println(sortRegimen(str1));
        System.out.println(sortRegimen(str2));
        System.out.println(isEquivalent(str2,str1));
        
        /*XMLOutputFactory factory = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = factory.createXMLStreamWriter(System.out);

        writer.writeStartDocument("1.0");

        writer.writeStartElement("catalog");

        writer.writeStartElement("book");

        writer.writeAttribute("id", "1");

        writer.writeStartElement("code");
        writer.writeCharacters("I01");
        writer.writeEndElement();

        writer.writeStartElement("title");
        writer.writeCharacters("This is the title");
        writer.writeEndElement();

        writer.writeStartElement("price");
        writer.writeCharacters("$2.95");
        writer.writeEndElement();
        
        
        writer.writeEndDocument();

        writer.flush();
        writer.close();*/
       // int x=null;
       //List<Integer> idList=new ArrayList<Integer>();
       /*Integer[] elements={1,12,6,7,2,9,0,5,7};
       Set<Integer> idSet=new HashSet<Integer>();
       idSet.addAll(Arrays.asList(elements));
       Set<Integer> sampleSet=getRandomPatients(idSet, 4);
       Iterator<Integer> it=sampleSet.iterator();
       for(int i=0;i<4;i++){
           System.out.println(it.next());
       } */
        
    }
    public static Set<Integer> getRandomPatients(Set<Integer> idSet,int n){
        Set<Integer> sampleSet=new HashSet<Integer>();
        ArrayList<Integer> sampleList=new ArrayList<Integer>();
        sampleList.addAll(idSet);
        int size=idSet.size();
        if(size<=n){
            n=size;
        }
        Collections.shuffle(sampleList);
        int sele=0;
        for(int i=0;i<n;i++){
            sele=sampleList.get(i);
            sampleSet.add(sele);
        }
        return sampleSet;
    }
}
