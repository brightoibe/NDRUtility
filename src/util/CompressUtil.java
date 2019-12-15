package util;

import java.io.File;  
import java.util.ArrayList;  
import java.util.Collections;  
import java.util.List;  
  
import org.apache.commons.lang3.StringUtils;  
  
import net.lingala.zip4j.core.ZipFile;  
import net.lingala.zip4j.exception.ZipException;  
import net.lingala.zip4j.model.FileHeader;  
import net.lingala.zip4j.model.ZipParameters;  
import net.lingala.zip4j.util.Zip4jConstants;  
  
/** 
 * ZIP 
 *  
 * zip4j() 
 * 1.3.1 
 * @author ninemax 
 */  
public class CompressUtil {  
      
    /** 
     * ZIP 
     * <p> 
     * ,, 
     * @param zip ZIP 
     * @param dest  
     * @param passwd ZIP 
     * @return  
     * @throws ZipException  
     */  
    public static File [] unzip(String zip, String dest, String passwd) throws ZipException {  
        File zipFile = new File(zip);  
        return unzip(zipFile, dest, passwd);  
    }  
      
    /** 
     * ZIP 
     * @param zip ZIP 
     * @param passwd ZIP 
     * @return   
     * @throws ZipException  
     */  
    public static File [] unzip(String zip, String passwd) throws ZipException {  
        File zipFile = new File(zip);  
        File parentDir = zipFile.getParentFile();  
        return unzip(zipFile, parentDir.getAbsolutePath(), passwd);  
    }  
      
    /** 
     * ZIP 
     * <p> 
     * ,, 
     * @param zip ZIP 
     * @param dest  
     * @param passwd ZIP 
     * @return   
     * @throws ZipException  
     */  
    public static File [] unzip(File zipFile, String dest, String passwd) throws ZipException {  
        ZipFile zFile = new ZipFile(zipFile);  
        zFile.setFileNameCharset("GBK");  
        if (!zFile.isValidZipFile()) {  
            throw new ZipException(",.");  
        }  
        File destDir = new File(dest);  
        if (destDir.isDirectory() && !destDir.exists()) {  
            destDir.mkdir();  
        }  
        if (zFile.isEncrypted()) {  
            zFile.setPassword(passwd.toCharArray());  
        }  
        zFile.extractAll(dest);  
          
        List<FileHeader> headerList = zFile.getFileHeaders();  
        List<File> extractedFileList = new ArrayList<File>();  
        for(FileHeader fileHeader : headerList) {  
            if (!fileHeader.isDirectory()) {  
                extractedFileList.add(new File(destDir,fileHeader.getFileName()));  
            }  
        }  
        File [] extractedFiles = new File[extractedFileList.size()];  
        extractedFileList.toArray(extractedFiles);  
        return extractedFiles;  
    }  
      
    /** 
     *  
     * @param src  
     * @return ,null. 
     */  
    public static String zip(String src) {  
        return zip(src,null);  
    }  
      
    /** 
     *  
     * @param src  
     * @param passwd  
     * @return ,null. 
     */  
    public static String zip(String src, String passwd) {  
        return zip(src, null, passwd);  
    }  
      
    /** 
     *  
     * @param src  
     * @param dest  
     * @param passwd  
     * @return ,null. 
     */  
    public static String zip(String src, String dest, String passwd) {  
        return zip(src, dest, true, passwd);  
    }  
      
    /** 
     * . 
     * <p> 
     * dest,,null"".<br /> 
     * null"",,,.zip;<br /> 
     * (File.separator),,,.zip,. 
     * @param src  
     * @param dest  
     * @param isCreateDir ,.<br /> 
     * false,. 
     * @param passwd  
     * @return ,null. 
     */  
    public static String zip(String src, String dest, boolean isCreateDir, String passwd) {  
        File srcFile = new File(src);  
        dest = buildDestinationZipFilePath(srcFile, dest);  
        ZipParameters parameters = new ZipParameters();  
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);           //   
        //parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);    //
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_FAST);
        if (!StringUtils.isEmpty(passwd)) {  
            parameters.setEncryptFiles(true);  
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD); //   
            parameters.setPassword(passwd.toCharArray());  
        }  
        try {  
            ZipFile zipFile = new ZipFile(dest);  
            if (srcFile.isDirectory()) {  
                // ,,  
                if (!isCreateDir) {  
                    File [] subFiles = srcFile.listFiles();  
                    ArrayList<File> temp = new ArrayList<File>();  
                    Collections.addAll(temp, subFiles);  
                    zipFile.addFiles(temp, parameters);  
                    return dest;  
                }  
                zipFile.addFolder(srcFile, parameters);  
            } else {  
                zipFile.addFile(srcFile, parameters);  
            }  
            return dest;  
        } catch (ZipException e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
      
    /** 
     * , 
     * ,, 
     * @param srcFile  
     * @param destParam  
     * @return  
     */  
    private static String buildDestinationZipFilePath(File srcFile,String destParam) {  
        if (StringUtils.isEmpty(destParam)) {  
            if (srcFile.isDirectory()) {  
                destParam = srcFile.getParent() + File.separator + srcFile.getName() + ".zip";  
            } else {  
                String fileName = srcFile.getName().substring(0, srcFile.getName().lastIndexOf("."));  
                destParam = srcFile.getParent() + File.separator + fileName + ".zip";  
            }  
        } else {  
            createDestDirectoryIfNecessary(destParam);  //   
            if (destParam.endsWith(File.separator)) {  
                String fileName = "";  
                if (srcFile.isDirectory()) {  
                    fileName = srcFile.getName();  
                } else {  
                    fileName = srcFile.getName().substring(0, srcFile.getName().lastIndexOf("."));  
                }  
                destParam += fileName + ".zip";  
            }  
        }  
        return destParam;  
    }  
      
    /** 
     * , 
     * @param destParam , 
     */  
    private static void createDestDirectoryIfNecessary(String destParam) {  
        File destDir = null;  
        if (destParam.endsWith(File.separator)) {  
            destDir = new File(destParam);  
        } else {  
            destDir = new File(destParam.substring(0, destParam.lastIndexOf(File.separator)));  
        }  
        if (!destDir.exists()) {  
            destDir.mkdirs();  
        }  
    }  
  
  
}  
