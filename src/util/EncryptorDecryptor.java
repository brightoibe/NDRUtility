/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author brightoibe
 */
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
public class EncryptorDecryptor {
    private static String encryptionKey="OpenMRSSecretKeyPhrase";
    private static DESKeySpec keySpec;
    private static SecretKeyFactory keyFactory;
    private static SecretKey key;
    private static BASE64Decoder base64decoder;
    private static BASE64Encoder base64encoder;
    private static EncryptorDecryptor instance;
    private EncryptorDecryptor() throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException {
            keySpec = new DESKeySpec(encryptionKey.getBytes("UTF8"));
            keyFactory = SecretKeyFactory.getInstance("DES");
            key = keyFactory.generateSecret(keySpec);
            base64encoder = new BASE64Encoder();
            base64decoder = new BASE64Decoder();
    }
    public static EncryptorDecryptor getInstance() throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException{
         if(instance==null){
             instance=new EncryptorDecryptor();
         }
         return instance;
    }
    public  String encrypt(String data){
        if(data==null){
            data="";
        }
        String encryptedData="";
        try{
            byte[] cleartext = data.getBytes("UTF8");      
            Cipher cipher = Cipher.getInstance("DES"); // cipher is not thread safe
            cipher.init(Cipher.ENCRYPT_MODE, key);
            encryptedData = base64encoder.encode(cipher.doFinal(cleartext));
        }catch(Exception e){
            e.printStackTrace();
        }
        return encryptedData;
    }
    public  String decrypt(String hashData) {
        String plainText="";
        try{
            byte[] encrypedPwdBytes = base64decoder.decodeBuffer(hashData);
            Cipher cipher = Cipher.getInstance("DES");// cipher is not thread safe
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] plainTextPwdBytes = (cipher.doFinal(encrypedPwdBytes));
            plainText=new String(plainTextPwdBytes);
        }catch(Exception e){
            e.printStackTrace();
        }
        return plainText;
    }
    
}
